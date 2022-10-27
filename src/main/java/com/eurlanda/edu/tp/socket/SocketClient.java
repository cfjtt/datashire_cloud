package com.eurlanda.edu.tp.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eurlanda.edu.tp.socket.pojo.DatashireServerDecoder;
import com.eurlanda.edu.tp.socket.pojo.DatashireServerEncoder;
import com.eurlanda.edu.tp.socket.pojo.MessagePacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by test on 2018/5/11.
 */
public class SocketClient {

    public static Channel channel;
    public static byte[] token = null;//保存登录后server返回的token
    public static Integer type = null; //操作类型，1.导入 2.导出

    private String host;
    private int port;


    public SocketClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    public SocketClient(){

    }


    public void login(){
        MessagePacket packet = new MessagePacket();
        //packet.setId(MessagePacket.geturrPacketID());
        packet.setGuid("123456789012345678901234567890123456".getBytes());
        packet.setDsObjectType(1);
        packet.setCommandId("2001");
        packet.setChildCommandId("0001");
        //packet.setId(1);
        //补全32位即可
        packet.setToken("12345678901234567890123456789012".getBytes());
        //获取Data字节
        Map<String,Object> paraMap = new HashMap<>();
        Map<String,Object> childMap = new HashMap<>();
        childMap.put("user_name","superuser");
        childMap.put("password","7066a4f427769cc43347aa96b72931a");
        paraMap.put("User",childMap);
        String data = JSON.toJSONString(paraMap);
        packet.setData(data.getBytes());
        channel.writeAndFlush(packet);
    }

    /**
     * {
     "IOFlows": [{
     "Id": 475,
     "Name": "数据清洗",
     "AllSquids": true,
     "AllVariables": true
     }],
     "DataShireFieldType": 0
     }
     * @param paraMap
     * @return
     */
    public MessagePacket exportProject(Map<String,Object> paraMap){
        MessagePacket packet = new MessagePacket();
        //packet.setId(MessagePacket.geturrPacketID());
        packet.setGuid("123456789012345678901234567890123456".getBytes());
        packet.setDsObjectType(1);
        packet.setCommandId("1022");
        packet.setChildCommandId("0001");
        //补全32位即可
        //packet.setId(2);
        packet.setToken(token);
        //获取Data字节
        //packet.setData("{\"IOFlows\":[{\"Id\":141,\"Name\":\"数据清洗\",\"AllSquids\":true,\"AllVariables\":true}],\"DataShireFieldType\":0}.getBytes());

        String data = JSONObject.toJSONString(paraMap);
        packet.setData(data.getBytes());
        return packet;
    }

    /**
     * {
     "IOFlows": [{
     "Id": 141,
     "Name": "数据清洗",
     "AllSquids": true,
     "AllVariables": true
     }],
     "ProjectId": 239,
     "FlowUnit": {
     "Count": 1,
     "Index": 0,
     "Data": "H4sIAAAAAAAEAO1d62/XvrgcT2Yk3056/T/6+HBJbLcBAA=="
     },
     "UserId": 1,
     "DataShireFieldType": 0
     }
     * @param paraMap
     * @return
     */
    public MessagePacket importProject(Map<String,Object> paraMap){
        MessagePacket packet = new MessagePacket();
        //packet.setId(MessagePacket.geturrPacketID());
        packet.setGuid("123456789012345678901234567890123456".getBytes());
        packet.setDsObjectType(1);
        packet.setCommandId("1023");
        packet.setChildCommandId("0001");
        //补全32位即可
        packet.setId(2);
        packet.setToken(token);
        //获取Data字节
        //packet.setData("{\"IOFlows\":[{\"Id\":141,\"Name\":\"数据清洗\",\"AllSquids\":true,\"AllVariables\":true}],\"DataShireFieldType\":0}".getBytes());
        String data = JSONObject.toJSONString(paraMap);
        packet.setData(data.getBytes());
        return packet;
    }



    //修改projectName
    public MessagePacket updateProjectName(Map<String,Object> paraMap){
        MessagePacket packet = new MessagePacket();
        //packet.setId(MessagePacket.geturrPacketID());
        packet.setGuid("123456789012345678901234567890123456".getBytes());
        packet.setDsObjectType(1);
        packet.setCommandId("1000");
        packet.setChildCommandId("0401");
        //补全32位即可
        packet.setToken(token);
        //获取Data字节
        String data = JSONObject.toJSONString(paraMap);
        packet.setData(data.getBytes());
        return packet;
    }





    //导入之前先创建一个project

    /**
     * {
     "RepositoryId": 3,
     "UserId": 1
     }
     *
     *
     */
    public MessagePacket createProject(Map<String,Object> paraMap){
        MessagePacket packet = new MessagePacket();
        //packet.setId(MessagePacket.geturrPacketID());
        packet.setGuid("123456789012345678901234567890123456".getBytes());
        packet.setDsObjectType(1);
        packet.setCommandId("1001");
        packet.setChildCommandId("0003");
        //补全32位即可
        packet.setToken(token);
        //获取Data字节
        String data = JSONObject.toJSONString(paraMap);
        packet.setData(data.getBytes());
        return packet;
    }

    /**
     * 操作完成后关闭客户端与server的连接
     * @param paraMap
     * @return
     */
    public MessagePacket logout(Map<String,Object> paraMap){
        MessagePacket packet = new MessagePacket();
        packet.setGuid("123456789012345678901234567890123456".getBytes());
        packet.setDsObjectType(1);
        packet.setCommandId("2001");
        packet.setChildCommandId("0002");
        packet.setToken(token);
        //获取Data字节
        String data = JSONObject.toJSONString(paraMap);
        packet.setData(data.getBytes());
        return packet;
    }



    public void connect(String host, int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DatashireServerDecoder())
                                    .addLast(new DatashireServerEncoder())
                                    .addLast(new com.eurlanda.edu.tp.socket.ClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            channel = future.channel();
            login();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }


    public static void main(String[] args) throws Exception {
        SocketClient client = new SocketClient();
        client.connect("192.168.137.45", 9999);

        //发送导出数据包


    }



}
