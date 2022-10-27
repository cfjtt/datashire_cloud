package com.eurlanda.edu.tp.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eurlanda.edu.tp.Util.CompressAndDecompressionUtils;
import com.eurlanda.edu.tp.socket.pojo.MessagePacket;
import com.eurlanda.edu.tp.socket.utils.StringUtils;
import com.eurlanda.edu.tp.socket.utils.ZipStrUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipOutputStream;

/**
 * Created by test on 2018/5/11.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * {
     * "IOFlows": [{
     * "Id": 141,
     * "Name": "数据清洗",
     * "AllSquids": true,
     * "AllVariables": true
     * }],
     * "ProjectId": 239,
     * "FlowUnit": {
     * "Count": 1,
     * "Index": 0,
     * "Data": "H4JuceQLjXT+PVq+i63" //JSON.toJsonString(byte[])
     * },
     * "UserId": 1,
     * "DataShireFieldType": 0
     * }
     */
    public static Map<String, Object> importMap;//导入参数

    private static Logger logger = LoggerFactory.getLogger(ClientHandler.class);


    public static ZipOutputStream zipOutStream;//压缩数据流

    public static int closeFlag = 0;
    public static int count = 0;
    public static String userId;
    public static String repositoryId;
    public static String projectId;
    public static String newProjectId;

    public static List<Map<String, Object>> exportProjects;
    public static List<Map<String, Object>> importProjects;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


    }




    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //读取服务端发过来的数据
        MessagePacket packet = (MessagePacket) msg;
        byte[] dataByte = packet.getData();
        String info = StringUtils.bytes2Str(dataByte);
        String command = packet.getCommandId() + packet.getChildCommandId();
        Map infoMap = null;
        try {
            infoMap = JSONObject.parseObject(info, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("错误的json" + info);
            throw e;
        }
        String code = infoMap.get("code") + "";
        logger.info("接收到服务器传来的数据，code:{}",code);
        if (code.equals("-9999")) {
            logger.error("未知的错误!");;
            ctx.close();
            throw new Exception();
        }else if(code.equals("70003")){
            logger.error("数猎场squid不正确!");;
            ctx.close();
            throw new Exception();
        }

        if (command.equals("20010001")) {
            //登录

            String type = infoMap.get("type") + "";
            if (type.equals("USER")) {
                byte[] bytes = packet.getToken();
                if (bytes.length != 0) {
                    SocketClient.token = bytes;
                    Map map = (Map) infoMap.get("info");
                    Map userMap = (Map) map.get("User");
                    ClientHandler.userId = userMap.get("id") + "";
                    SocketClient client = new SocketClient();
                    MessagePacket paraPacket = null;
                    if (SocketClient.type == 2) {
                        //导出project
                        for (Map project : exportProjects) {
                            paraPacket = client.exportProject(project);
                            SocketClient.channel.writeAndFlush(paraPacket);
                        }
                    } else if (SocketClient.type == 1) {
                        //导入project,导入前先创建一个project
                        //paraPacket = client.importProject(new HashMap<>());
                        Map<String, Object> paraMap = new HashMap<>();
                        paraMap.put("RepositoryId", repositoryId);
                        paraMap.put("UserId", userId);
                        paraPacket = client.createProject(paraMap);
                        SocketClient.channel.writeAndFlush(paraPacket);
                    }

                }
            }
        } else if (command.equals("10220001")) {
            //导出project
            Map map = (Map) infoMap.get("info");
            Map flowUnit = (Map) map.get("FlowUnit");
            count++;
            if (count >= closeFlag) {
                SocketClient client = new SocketClient();
                MessagePacket logoutPacket = client.logout(new HashMap<>());
                System.out.println("发送注销请求。。。。");
                logger.info("导出成功关闭通道！");
                count = 0;
                closeFlag = 0;
                SocketClient.channel.writeAndFlush(logoutPacket);
                //ctx.close();

            }
            if(flowUnit == null){
                logger.info("---没有数据--");
                return;
            }
            String str = flowUnit.get("data") + "";
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes2 = decoder.decodeBuffer(str);
            ;
           /* StringBuilder sb = new StringBuilder("[");
            for (byte b : dataByte){
                sb.append(b + ",");
            }
            sb.append("]");
            System.out.println(sb.toString());*/
            byte[] bytes = ZipStrUtil.unCompress(bytes2);
            info = StringUtils.bytes2Str(bytes);

            InputStream in = null;
            try {

                in = new ByteArrayInputStream(info.getBytes());
                //IOUtil.copyCompletely(in,out);
                //count++;
                CompressAndDecompressionUtils.compressFileByInputStream(in, zipOutStream, "project_" + count + ".dse");

            } finally {

                if (in != null) {
                    in.close();
                }
            }
            System.out.println("接收到导出SquidFlow的返回结果");
        } else if (command.equals("10230001")) {
            //导入project,导入之前先创建一个project作为根project
            System.out.println("接收到导入project回执消息");
            //导入成功后，关闭客户端与server的连接
            SocketClient client = new SocketClient();
            if (count >= closeFlag) {
                client = new SocketClient();
                MessagePacket logoutPacket = client.logout(new HashMap<>());
                System.out.println("10220001发送注销请求。。。。");
                logger.info("导入成功关闭通道！");
                count = 0;
                closeFlag = 0;
                SocketClient.channel.writeAndFlush(logoutPacket);
                //ctx.close();
                return;
            }

            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("RepositoryId", repositoryId);
            paraMap.put("UserId", userId);
            MessagePacket paraPacket = client.createProject(paraMap);
            SocketClient.channel.writeAndFlush(paraPacket);

            /*Map importMap = importProjects.get(count++);
            importMap.put("ProjectId", projectId);
            importMap.put("UserId", userId);
            importMap.put("DataShireFieldType", 0);
            MessagePacket importPacket = client.importProject(importMap);
            SocketClient.channel.writeAndFlush(importPacket);*/


        } else if (command.equals("10010003")) {
            //创建project后开始调用导入
            Map newMap = (Map) infoMap.get("info");
            Map map = (Map) newMap.get("Project");
            String projectId = map.get("id") + "";//新建后的projectId
            this.projectId = projectId;
            //修改projectName
            Map updateProjectNameMap = new HashMap<>();
            updateProjectNameMap.put("Creation_date",null);
            updateProjectNameMap.put("Repository_id",repositoryId);
            updateProjectNameMap.put("Creator",1);
            updateProjectNameMap.put("Description",null);
            updateProjectNameMap.put("Modification_date",formatDate());
            updateProjectNameMap.put("SquidFlowList",new ArrayList<>());
            updateProjectNameMap.put("BatchSquidFlowList",null);
            updateProjectNameMap.put("StreamSquidFlowList",null);
            updateProjectNameMap.put("Variables",new ArrayList<>());
            updateProjectNameMap.put("Id",projectId);
            updateProjectNameMap.put("Key",null);
            updateProjectNameMap.put("Name",importProjects.get(count).get("name"));
            Map<String,Object> updateMap = new HashMap<>();
            updateMap.put("Project",updateProjectNameMap);
            SocketClient client = new SocketClient();
            MessagePacket updatePacket = client.updateProjectName(updateMap);
            SocketClient.channel.writeAndFlush(updatePacket);




        }else if(command.equals("10000401")){
            //接收到修改projectName的消息
            //开始发送导入请求
            //发送第一个包
            Map importMap = importProjects.get(count++);
            importMap.put("ProjectId", projectId);
            //this.projectId = projectId;
            importMap.put("UserId", userId);
            importMap.put("DataShireFieldType", 0);
            SocketClient client = new SocketClient();
            MessagePacket importPacket = client.importProject(importMap);
            SocketClient.channel.writeAndFlush(importPacket);
        }

        // 消息包内容
        System.out.println("客户端收到的消息： " + info);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("channelReadComplete----");
    }

    public String formatDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = null;
        try{
            result = sdf.format(new Date());
        }catch (Exception e){
            e.getMessage();
        }
        return result;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("发生错误！，即将关闭与服务器连接");
        cause.printStackTrace();
        count = 0;
        closeFlag = 0;
        this.zipOutStream = null;
        ctx.close();
        throw new Exception();
    }

    public static void main(String[] args) throws Exception {
        String versionStr = "-1,-1,-1,-1,-1,-1,-1,-1,0,0,0,12,0,0,0,0,-1,-1,-1,-1,-1,-1,-1,-1";
        String[] strArray = versionStr.split(",");
        byte[] versionByte = new byte[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            versionByte[i] = (byte) Integer.parseInt(strArray[i]);
        }
        System.out.println("xxx");

        byte[] newByte = {1, 2, 3, 4, 5};
        Map map = new HashMap<>();
        map.put("byte", newByte);
        map.put("name", "fastJson");
        String str = JSON.toJSONString(map);
        System.out.println(str);

        Map map1 = JSON.parseObject(str, Map.class);

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes2 = decoder.decodeBuffer(map1.get("byte") + "");
        //byte[] bytes = Base64.decodeFast();
        /*String byteStr = map1.get("byte") + "";
        byte[] bytes = JSON.parseObject(str, byte[].class);;*/
        //byte[] result = byteStr.getBytes();
        System.out.println(1);
    }


}
