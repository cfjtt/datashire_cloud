package com.eurlanda.edu.tp.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.eurlanda.edu.tp.Util.CompressAndDecompressionUtils;
import com.eurlanda.edu.tp.api.EduApi;
import com.eurlanda.edu.tp.dao.entity.*;
import com.eurlanda.edu.tp.dao.mapper.*;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.ImportAndExportService;
import com.eurlanda.edu.tp.service.RancherService;
import com.eurlanda.edu.tp.socket.ClientHandler;
import com.eurlanda.edu.tp.socket.SocketClient;
import com.eurlanda.edu.tp.socket.utils.ZipStrUtil;
import com.eurlanda.edu.tp.webdomain.response.ResCourseModuleExperiments;
import com.eurlanda.edu.tp.webdomain.response.ResExperimentInfo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by test on 2018/5/5.
 */
@Service
public class ImportAndExportServiceImpl implements ImportAndExportService {


    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ImportAndExportServiceImpl.class.getName());

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseServiceImp courseService;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private CourseResourceMapper courseResourceMapper;

    @Autowired
    private RancherService rancherService;

    @Value("${file.baseDir}")
    private String baseDir;

    @Value("${file.imageDir}")
    private String imageDir;

    @Value("${file.courseFileUrl}")
    private String courseFileUrl;

    @Value("${hdfsIpAndPort}")
    private String hdfsIpAndPort;

    @Value("${userDBIpAndPort}")
    private String userDBIpAndPort;

    @Value("${userDBusername}")
    private String userDBusername;

    @Value("${userDBpassword}")
    private String userDBpassword;

    @Value("${rancher.secret}")
    private String secret;

    @Value("${serverIP}")
    private String serverIP;

    @Value("${serverPort}")
    private int serverPort;

    @Autowired
    private EduApi eduApi;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private ExperimentMapper experimentMapper;

    public static final String transHdfs = "/cdata/";
    public static final String cloudHdfs = "/udata/";

    private static FileSystem fileSystem;


    public static Logger logger = Logger.getLogger("ImportAndExportServiceImpl");

    /**
     * 根据课程id导出课程对应的数据
     * 1.章节：实验：a.文本 b.图片
     * 2.trainFile，traindb：a.文件+文件夹 b.数据库表 + 数据
     * 3.cloudFile，clounddb：a.文件+文件夹 b.数据库表 + 数据
     * 4.基础数据：a.封面图片 b.其它字段
     *
     * @param courseId
     */
    @Override
    public OutputStream exportCourseData(Integer courseId, OutputStream out) throws Exception {
        //课程字段数据
        Course course = courseMapper.selectByPrimaryKey2(courseId);
        //课程章节数据
        ResCourseModuleExperiments courseData = courseService.getCourseModuleExperiments(courseId);
        boolean isDataShireExperiment = courseService.selectCourseExistShulieyun(courseId);
        courseData.setFolderName(course.getFolderName());
        courseData.setDescription(course.getDescription());
        courseData.setVolumeId(course.getVolumeId());
        courseData.setAuthor(course.getAuthor());
        courseData.setPhone(course.getPhone());
        courseData.setHavingDatashireExperiment(isDataShireExperiment);
        Resource resource = resourceMapper.selectReSourceByCourseId(courseId);
        if (resource != null) {
            courseData.setResource(resource);
        }
        String courseDataStr = JSONObject.toJSONString(courseData);

        byte[] courseDataStrBytes = courseDataStr.getBytes();
        ByteArrayInputStream inputStringStream = new ByteArrayInputStream(courseDataStrBytes);
        ZipOutputStream zipOut = null;
        String imgSrcPath = baseDir + imageDir + "/" + course.getFolderName();
        String courseSrc = courseFileUrl + courseId;
        List<String> srcPaths = new ArrayList<>();
        srcPaths.add(imgSrcPath);
        srcPaths.add(courseSrc);
        try {
            //1.课程封面图片，实验图片,共享文件夹
            OutputStream outputStreams = CompressAndDecompressionUtils.compress(srcPaths, out);
            zipOut = (ZipOutputStream) outputStreams;
            //2.课程基本数据，模块及实验数据
            CompressAndDecompressionUtils.compressFileByInputStream(inputStringStream, zipOut, "data.json");
            //如果有数猎云类型的实验，则需要打包在Hdfs中上传的文件，还有数据库，表数据

            if (isDataShireExperiment) {
                Configuration configuration = new Configuration();
                configuration.set("fs.defaultFS", "hdfs://" + hdfsIpAndPort);
                CompressAndDecompressionUtils.setHdfsConfig(configuration);
                //获取hdfs路径
                String data = eduApi.getHdfsPathByCourse(course.getName());
                Map<String, Object> dataMap = JSONObject.parseObject(data, Map.class);
                String hdfsSrc = dataMap.get("hdfsPath") + "";//transFile 路径
                Object repositoryId = dataMap.get("repositoryId");
                String cloudFilePath = cloudHdfs + "u" + repositoryId;//cloudFile 路径
                //该课程对应的数猎场下的所有的projects
                List<Map<String, Object>> projects = (List) dataMap.get("projects");
                if (hdfsSrc == null
                        || "".equals(hdfsSrc)
                        || "null".equals(hdfsSrc)
                        || repositoryId == null
                        || "".equals(repositoryId)) {
                    logger.info(course.getName() + "该课程存在数猎云实验但没有相应的hdfs文件");
                    throw new ApplicationErrorException(ErrorCode.GeneralError);
                }
                //transFile hdfs文件及目录 cdata/ + courseId
                CompressAndDecompressionUtils.download(hdfsSrc, zipOut, "");
                //transFile hdfs文件及目录 udata/ + repositoryId
                CompressAndDecompressionUtils.download(cloudFilePath, zipOut, "");
                //数据库,表
                String transDbJsonStr = getDataBaseData(hdfsSrc.replace(transHdfs, ""));
                String cloudDbJsonStr = getDataBaseData(cloudFilePath.replace(cloudHdfs, "") + "db");
                if (!transDbJsonStr.equals("[]")) {
                    ByteArrayInputStream transDbJsonInS = new ByteArrayInputStream(transDbJsonStr.getBytes());
                    CompressAndDecompressionUtils.compressFileByInputStream(transDbJsonInS, zipOut, "transDb.json");
                }
                if (!cloudDbJsonStr.equals("[]")) {
                    ByteArrayInputStream cloudDbJsonInS = new ByteArrayInputStream(cloudDbJsonStr.getBytes());
                    CompressAndDecompressionUtils.compressFileByInputStream(cloudDbJsonInS, zipOut, "cloudDb.json");
                }
                //导出project
                //synchronized (this){
                ClientHandler.exportProjects = importCourseProjects(projects);
                if (ClientHandler.exportProjects == null || ClientHandler.exportProjects.size() == 0) {
                    logger.info("该课程没有project数据");
                    return zipOut;
                }
                //保存原先projectName
                String projectStr = JSONObject.toJSONString(projects);
                if(!projectStr.equals("[]")){
                    ByteArrayInputStream projectJsonInS = new ByteArrayInputStream(projectStr.getBytes());
                    CompressAndDecompressionUtils.compressFileByInputStream(projectJsonInS, zipOut, "projects.json");
                }
                ClientHandler.zipOutStream = zipOut;
                SocketClient.type = 2;
                ClientHandler.closeFlag = projects.size();
                SocketClient client = new SocketClient();
                Long start = System.currentTimeMillis();
                logger.info("---------开始调用客户端：-----------------");
                try {
                    client.connect(serverIP, serverPort);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("exportCourseData导出project错误！" + e.getMessage(),e);
                    throw e;
                } finally {
                    ClientHandler.closeFlag = 0;
                    ClientHandler.count = 0;
                    ClientHandler.zipOutStream = null;
                }
                Long end = System.currentTimeMillis();
                logger.info("---------调用客户端结束：-----------------");
                logger.info("---------导出调用客户端所花时间为：-----------------" + (end - start));
                // }

            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("exportCourseData导出课程出错,msg" + e.getMessage(),e);
            throw e;
        }
        return zipOut;

    }

    /**
     * socket连接后台调用project导出接口
     *
     * @param projects
     */
    public List<Map<String, Object>> importCourseProjects(List<Map<String, Object>> projects) {

        //请求参数格式化
        Map<String, Object> paraMap = new HashMap<>();
        //该参数目前不知道什么意思，保留
        paraMap.put("DataShireFieldType", 0);
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Map project : projects) {
            Map<String, Object> map = new HashMap<>();
            List<Map> ioFlowsMaps = (List) project.get("IOFlows");
            map.put("IOFlows", ioFlowsMaps);
            map.put("DataShireFieldType", 0);
            maps.add(map);
        }
        return maps;

    }

    public void setFileSystemConfig(Configuration config) throws Exception {
        config.set("fs.defaultFS", "hdfs://" + hdfsIpAndPort);
        fileSystem = FileSystem.get(config);
    }

    public String getDataBaseData(String dataBaseName) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<String> tableNames = new ArrayList<>();
        List<Map<String, Object>> maps = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String para = "?useUnicode=true&characterEncoding=UTF-8";
            connection = java.sql.DriverManager.getConnection(userDBIpAndPort + "/" + dataBaseName + para, userDBusername, userDBpassword);
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
            //遍历表获取字段和数据信息
            for (String table : tableNames) {
                Map<String, Object> tableData = new HashMap<>();
                tableData.put("tableName", table);
                List<Map> columnsMap = new ArrayList<>();
                Map<String, List> rowsData = new LinkedHashMap<>();
                String sql = "select * from " + table;
                String createSql = "show create table " + table;
                ps = connection.prepareStatement(sql);
                resultSet = ps.executeQuery();
                ResultSetMetaData meta = resultSet.getMetaData();
                int columnCount = meta.getColumnCount();
                //获取每个字段的名字，类型，长度
                for (int i = 0; i < columnCount; i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("columnName", meta.getColumnName(i + 1));
                    //longText变成varchar
                    int pre = meta.getPrecision(i + 1);
                    String type = meta.getColumnTypeName(i + 1);
                    int length = meta.getColumnDisplaySize(i + 1);

                    System.out.println(meta.getColumnName(i + 1) + ":" + pre + meta.getColumnTypeName(i + 1));
                    //以下这几种数据类型getTypeName都会返回VARCHAR现在根据字符的长度来精确到具体的类型
                    if(type.equals("VARCHAR")){
                        String realType = null;
                        int realLength = 0;
                        switch (pre){
                            case 21845 :
                                realType = "text";
                                break;
                            case 85 :
                                realType = "TINYTEXT";
                                break;
                            case 5592405 :
                                realType = "MEDIUMTEXT";
                                break;
                            case 715827882 :
                                realType = "longtext";
                                break;
                            default:
                                realType = "VARCHAR";
                                realLength = length;
                        }
                        map.put("columnType",realType );
                        map.put("length", realLength);
                    }else {
                        map.put("columnType",type );
                        map.put("length", length);
                    }
                    //map.put("tableName", table);

                    columnsMap.add(map);
                }
                //获取每一行的数据
                int i = 0;
                while (resultSet.next()) {
                    List<String> rowData = new ArrayList<>();
                    for (Map columnMap : columnsMap) {
                        String columnName = columnMap.get("columnName") + "";
                        String value = resultSet.getString(columnName);
                        rowData.add(value);
                    }
                    rowsData.put(i++ + "", rowData);
                }

                tableData.put("columnData", columnsMap);
                tableData.put("columnValue", rowsData);
                tableData.put("dbName", dataBaseName);
                maps.add(tableData);

                ps = connection.prepareStatement(createSql);
                resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    tableData.put("createSql",resultSet.getString(2));
                }

            }
            String data = JSONObject.toJSONString(maps);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getDataBaseData错误：" + e.getMessage(),e);
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }


    @Override
    public void importCourseData(MultipartFile file) throws Exception {
        String uploadFileName = file.getOriginalFilename();
        //验证文件格式
        if (!uploadFileName.endsWith(".deep")) {
            throw new ApplicationErrorException(ErrorCode.FileIsNotDEEP);
        }
        //会生成临时文件夹，执行完要删除
        InputStream zipIn = null;
        OutputStream zipOut = null;
        try {
            zipIn = file.getInputStream();
            zipOut = new FileOutputStream(new File(baseDir + "/" + uploadFileName));
            IOUtil.copyCompletely(zipIn, zipOut);

        } finally {
            if (zipIn != null) {
                zipIn.close();
                zipIn = null;
            }
            if (zipOut != null) {
                zipOut.close();
                zipOut = null;
            }
            //System.gc();
        }
        /*CommonsMultipartFile cf = (CommonsMultipartFile) file;
        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
        File newFile = fi.getStoreLocation();*/
        File newFile = new File(baseDir + "/" + uploadFileName);
        newFile.setExecutable(true);//设置可执行权限
        newFile.setReadable(true);//设置可读权限
        newFile.setWritable(true);
        if (!newFile.exists()) {
            log.error("importCourseData生成的临时文件不存在！path：" + baseDir + "/" + uploadFileName);
            throw new ApplicationErrorException(ErrorCode.GeneralError);
        }
        ZipFile zip = new ZipFile(newFile);
        //课程图片文件夹为13位的时间戳
        String courseImgNameRex = "^\\d{13}$";
        //先将课程，模块，实验，数据入库
        boolean isHavingDataShireExperiment = false;
        String courseName = null;
        String oldCourseId = null;
        String newCourseId = null;
        boolean isContainsDataJson = false;
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = null;
            try {

                String dataJson = null;
                if (zipEntryName.contains("data.json")) {
                    isContainsDataJson = true;
                    in = zip.getInputStream(entry);
                    //课程，模块，实验，数据,直接转换为json串
                    dataJson = getStrFromInputSteam(in);
                    Map<String, Object> resultMap = parseCourseJsonAndInsertData(dataJson);
                    isHavingDataShireExperiment = (boolean) resultMap.get("isHavingDatashireExperiment");
                    courseName = resultMap.get("courseName") + "";
                    oldCourseId = resultMap.get("oldCourseId") + "";
                    newCourseId = resultMap.get("newCourseId") + "";
                    break;
                }
                //判断路径是否存在,不存在则创建文件路径
            } finally {
                if (null != in) {
                    in.close();
                    in = null;
                }
            }
        }
        if (!isContainsDataJson) {
            log.error("importCourseData导入的课程包没有包含课程基础数据");
            throw new ApplicationErrorException(ErrorCode.CoursePackageInvalid);
        }


        String transHdfsPath = null;
        String cloudHdfsPath = null;
        String repositoryId = null;
        String tmpStr = "";
        if (isHavingDataShireExperiment) {
            String data = eduApi.getHdfsPathByCourse(courseName);
            log.info("importCourseData调用数猎云getHdfsPathByCourse接口返回值为：" + data);
            Map<String, Object> dataMap = JSONObject.parseObject(data, Map.class);
            transHdfsPath = dataMap.get("hdfsPath") + "";
            repositoryId = dataMap.get("repositoryId") + "";
            cloudHdfsPath = cloudHdfs + "u" + repositoryId;
        }
        List<Map<String, Object>> importMaps = new ArrayList<>();
        String sourceName = uploadFileName.substring(0, uploadFileName.lastIndexOf("."));
        InputStream in = null;
        OutputStream out = null;
        List<Map> projectNames = new ArrayList<>();
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName().replace(sourceName, "");
            String curSeparator = "\\";
            if (zipEntryName.contains("/")) {
                curSeparator = "/";
                if (zipEntryName.startsWith(curSeparator)) {
                    if (zipEntryName.equals(curSeparator)) {
                        continue;
                    }
                    zipEntryName = zipEntryName.substring(1);
                }
            }
            //找出根目录名字
            int sepIndex = zipEntryName.indexOf(curSeparator);
            String rootPath = zipEntryName;
            if (sepIndex != -1) {
                rootPath = zipEntryName.substring(0, sepIndex);
            }

            try {

                String dstPath = "";
                String dbJson = null;
                if (isHavingDataShireExperiment) {

                    if (transHdfsPath == null || "".equals(transHdfsPath)) {
                        logger.info("查询课程hdfs路径错误！");
                        throw new ApplicationErrorException(ErrorCode.GeneralError);
                    }
                    //包含数猎云实验
                    if (Pattern.matches(courseImgNameRex, rootPath)) {
                        //课程图片文件夹
                        dstPath = baseDir + imageDir;
                    } else if (rootPath.equals("cdata") || rootPath.equals("udata")) {
                        //hdfs文件,先生成一个临时文件，再上传至hdfs
                        tmpStr = System.currentTimeMillis() + "";
                        dstPath = baseDir + "/" + tmpStr + rootPath;
                        //去掉二级目录
                        for (int i = 0; i < 2; i++) {
                            int secIndex = zipEntryName.indexOf(curSeparator);
                            zipEntryName = zipEntryName.substring(secIndex + 1);
                        }
                        in = zip.getInputStream(entry);
                        String outPath1 = (dstPath + "/" + zipEntryName).replaceAll("\\*", "/");
                        writeByte(in, out, outPath1);
                        setFileSystemConfig(new Configuration());
                        if (rootPath.equals("cdata")) {
                            upload(dstPath, transHdfsPath);
                        } else if (rootPath.equals("udata")) {
                            upload(dstPath, cloudHdfsPath);
                        }
                        //删除临时文件
                        /*File tmpFile = new File(outPath1);
                        tmpFile.delete();*/
                        continue;
                    } else if (rootPath.endsWith(".json")) {
                        //用户业务数据库，表数据
                        in = zip.getInputStream(entry);
                        if (rootPath.equals("transDb.json")) {
                            dbJson = getStrFromInputSteam(in);
                            parseDbJsonAndInsertData(dbJson, transHdfsPath.replace(transHdfs, ""));
                        } else if (rootPath.equals("cloudDb.json")) {
                            dbJson = getStrFromInputSteam(in);
                            parseDbJsonAndInsertData(dbJson, cloudHdfsPath.replace(cloudHdfs, "") + "db");
                        }else if(rootPath.equals("projects.json")){
                            dbJson = getStrFromInputSteam(in);
                            projectNames = JSONObject.parseObject(dbJson, List.class);
                        }
                        continue;
                    } else if (rootPath.equals(oldCourseId)) {
                        //课程共享文件夹
                        //去除一级目录
                        int secIndex = zipEntryName.indexOf(curSeparator);
                        zipEntryName = zipEntryName.substring(secIndex + 1);
                        dstPath = courseFileUrl + newCourseId;
                    } else if (rootPath.endsWith(".dse")) {
                        //导入project
                        SocketClient.type = 1;
                        //导入之前新建一个project
                        ClientHandler.repositoryId = repositoryId;
                        Map<String, Object> unitMap = new HashMap<>();
                        unitMap.put("Count", 1);
                        unitMap.put("Index", 0);
                        in = zip.getInputStream(entry);
                        String xmlStr = getStrFromInputSteamSaveNewlineCharacter(in);
                        unitMap.put("Data", ZipStrUtil.compress(xmlStr.getBytes()));
                        Map<String, Object> resultMap = new HashMap<>();
                        resultMap.put("FlowUnit", unitMap);
                        List<Map<String, Object>> maps = null;
                        try {
                            maps = dom4jParse(xmlStr.getBytes());
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("importCourseData解析xml发生错误：" + e.getMessage(),e);
                            throw new ApplicationErrorException(ErrorCode.ImportSquidFlowError);
                        }
                        resultMap.put("IOFlows", maps);
                        importMaps.add(resultMap);
                        continue;
                    }
                } else {
                    //不包含数猎云实验
                    if (Pattern.matches(courseImgNameRex, rootPath)) {
                        //课程图片文件夹
                        dstPath = baseDir + imageDir;
                    } else if (rootPath.equals(oldCourseId)) {
                        //课程共享文件夹
                        //去除一级目录
                        int secIndex = zipEntryName.indexOf(curSeparator);
                        zipEntryName = zipEntryName.substring(secIndex + 1);
                        dstPath = courseFileUrl + newCourseId;
                    }

                }
                in = zip.getInputStream(entry);
                String outPath = (dstPath + "/" + zipEntryName).replaceAll("\\*", "/");
                //判断路径是否存在,不存在则创建文件路径
                writeByte(in, out, outPath);
            }catch (Exception e){
                e.printStackTrace();
                log.error("importCourseData发生错误！" + e.getMessage(),e);
                throw e;
            } finally{
                if (null != in) {
                    in.close();
                }

                if (null != out) {
                    out.close();
                }
            }
        }
        in = null;
        out = null;
        zip = null;
        newFile = null;
        //删除生成的临时文件
        System.gc();
        CompressAndDecompressionUtils.deleteFile(baseDir + "/" + tmpStr + "cdata");
        CompressAndDecompressionUtils.deleteFile(baseDir + "/" + tmpStr + "udata");
        CompressAndDecompressionUtils.deleteFile(baseDir + "/" + uploadFileName);

        if (importMaps.size() > 0) {

            for (Map map : importMaps){
                List<Map> iOFlows = (List)map.get("IOFlows");
                for (Map ioFlow : iOFlows){
                    String flowId = ioFlow.get("Id") + "";
                    for (Map nameMap : projectNames){
                        List<Map> nameIOFlows = (List)nameMap.get("IOFlows");
                        for (Map nameIoFlow :nameIOFlows){
                            String nameFlowId = nameIoFlow.get("Id") + "";
                            if(flowId.equals(nameFlowId)){
                                map.put("name",nameMap.get("name"));
                                break;
                            }
                        }
                    }

                }
            }

            ClientHandler.closeFlag = importMaps.size();
            ClientHandler.count = 0;//
            ClientHandler.importProjects = importMaps;
            ClientHandler.repositoryId = repositoryId;
            SocketClient client = new SocketClient();
            try {
                client.connect(serverIP, serverPort);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("importCourseData导入project错误！" + e.getMessage(),e);
                throw new ApplicationErrorException(ErrorCode.ImportSquidFlowError);
            } finally {
                ClientHandler.closeFlag = 0;
                ClientHandler.count = 0;
            }
        }


    }


    public List<Map<String, Object>> dom4jParse(byte[] bytes) throws Exception {
        InputStream in = null;
        List<Map<String, Object>> maps = new ArrayList<>();
        SAXReader reader = new SAXReader();
        try {
            //获取ioFlows
            in = new ByteArrayInputStream(bytes);
            org.dom4j.Document document = reader.read(in);
            org.dom4j.Element root = document.getRootElement();
            Element infoEles = root.element("ioflowList");
            List<Element> iOflows = infoEles.elements();
            for (Element ele : iOflows) {
                String id = ele.element("id").getText();
                String name = ele.element("name").getText();
                String allSquids = ele.element("allSquids").getText();
                String allVariables = ele.element("allVariables").getText();
                Map<String, Object> map = new HashMap<>();
                map.put("Id", id);
                map.put("Name", name);
                map.put("AllSquids", allSquids);
                map.put("AllVariables", allVariables);
                maps.add(map);
            }
        } finally {
            if(in != null){
                in.close();
            }
        }
        return maps;
    }
    /**
     * 将导出的hdfs文件上传至为该课程新建的hdfs文件
     *
     * @param filePath
     * @param hdfsPath
     */
    public void upload(String filePath, String hdfsPath) throws IOException {

        //需要上传的文件
        File uploadFile = new File(filePath);
        if (!uploadFile.exists()) {
            logger.info("需要上传的文件不存在!");
            return;
        }
        //hdfs文件路径
        //Path path = new Path(hdfsPath);

        if (uploadFile.isDirectory()) {
            //上传文件夹
            uploadFolder(filePath, hdfsPath);
        } else {
            //上传文件
            uploadFile(filePath, hdfsPath);
        }


    }


    public void uploadFile(String srcPath, String toPath) throws IOException {
        File uploadFile = new File(srcPath);
        InputStream in = new FileInputStream(uploadFile);
        Path path = new Path(toPath);

        OutputStream out = fileSystem.create(path);
        try {
            IOUtils.copyBytes(in, out, 4096, false);
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public void uploadFolder(String filePath, String hdfsPath) throws IOException {
        File uploadFile = new File(filePath);
        File[] files = uploadFile.listFiles();
        Path path = new Path(hdfsPath);
        if (!fileSystem.exists(path)) {
            fileSystem.mkdirs(path);
        }
        for (File file : files) {
            String fileName = file.getName();
            upload(filePath + "/" + fileName, hdfsPath + "/" + fileName);
        }

    }


    public void writeByte(InputStream in, OutputStream out, String outPath) throws IOException {
        File file2 = new File(outPath.substring(0, outPath.lastIndexOf('/')));
        if (!file2.exists()) {
            file2.setExecutable(true);//设置可执行权限
            file2.setReadable(true);//设置可读权限
            file2.setWritable(true);//设置可写权限
            file2.mkdirs();
        }
        //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
        if (new File(outPath).isDirectory()) {
            return;
        }

        out = new FileOutputStream(outPath);
        byte[] buf1 = new byte[1024];
        int len;
        while ((len = in.read(buf1)) > 0) {
            out.write(buf1, 0, len);
        }
    }


    public String getStrFromInputSteam(InputStream in) throws IOException {
        InputStreamReader newIn = new InputStreamReader(in, "UTF-8");
        BufferedReader bf = new BufferedReader(newIn);
        StringBuffer buffer = null;
        //最好在将字节流转换为字符流的时候 进行转码
        try{
            buffer = new StringBuffer();
            String line = "";
            while ((line = bf.readLine()) != null) {
                buffer.append(line);
            }
        }finally {
            if (newIn != null){
                newIn.close();
            }
            if(bf != null){
                bf.close();
            }
        }

        return buffer.toString();
    }



    public String getStrFromInputSteamSaveNewlineCharacter(InputStream in) throws Exception {
        InputStreamReader newIn = new InputStreamReader(in, "UTF-8");
        OutputStream out = null;
        String tmpPath = baseDir + "/tempFile/";
        File newFile = new File(tmpPath);
        if(!newFile.exists()){
            newFile.mkdir();
        }
        //FileInputStream fileIn = new FileInputStream();
        //BufferedReader bf = new BufferedReader(newIn);
        //StringBuffer buffer = null;
        //最好在将字节流转换为字符流的时候 进行转码
        String result = null;
        FileInputStream inputStream = null;
        File file = null;
        try{
            String name = tmpPath + System.currentTimeMillis() + ".dse";
            out = new FileOutputStream(name);
            //生成一个临时文件
            org.apache.commons.io.IOUtils.copy(in,out);
            file = new File(name);
            inputStream = new FileInputStream(file);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            result = new String(b);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        } finally{
            if (newIn != null){
                newIn.close();
            }

            if(out != null){
                out.close();
            }
            if(inputStream != null){
                inputStream.close();
            }
            //删除临时文件
            if(file != null){
                file.delete();
            }

        }

        return result;
    }

    /**
     * 解析课程json字符串，插入相应的数据
     *
     * @param courseJson
     * @return
     */
    public Map<String, Object> parseCourseJsonAndInsertData(String courseJson) throws ApplicationErrorException {

        Map<String, Object> resultMap = new HashMap<>();
        ResCourseModuleExperiments resCourseModuleExperiments = JSONObject.parseObject(courseJson, ResCourseModuleExperiments.class);
        Course course = new Course();
        String courseName = resCourseModuleExperiments.getCourseName();
        String des = resCourseModuleExperiments.getDescription();
        String folderName = resCourseModuleExperiments.getFolderName();
        String volumeId = resCourseModuleExperiments.getVolumeId();
        String author = resCourseModuleExperiments.getAuthor();
        String phone = resCourseModuleExperiments.getPhone();
        course.setName(courseName);
        course.setDescription(des);
        course.setFolderName(folderName);
        course.setVolumeId(volumeId);
        course.setAuthor(author);
        course.setPhone(phone);

        //导入前做课程验证，不合法则跑出异常
        Integer count = courseMapper.selectCountByCourseName(course);
        if (count > 0) {
            throw new ApplicationErrorException(ErrorCode.CourseAlreadyExisted);
        }
        //插入课程
        courseMapper.insert(course);
        Integer courseId = course.getId();
        boolean isHavingDatashireExperiment = resCourseModuleExperiments.isHavingDatashireExperiment();
        //同步数猎云
        if (isHavingDatashireExperiment) {
            Map<String, Object> initCourseMap = new HashMap<>();
            initCourseMap.put("coursename", courseName);
            String result = eduApi.initCourse(initCourseMap);
            log.info("parseCourseJsonAndInsertData调用数猎云接口返回值：" + result);
            if (!result.equals("success")) {
                log.error("parseCourseJsonAndInsertData调用数猎云接口错误！" + result);
                throw new ApplicationErrorException(ErrorCode.GeneralError);
            }
        }
        //为课程创建文件夹
        rancherService.createVolumes(secret, courseId, 1);
        //resource
        Resource resource = resCourseModuleExperiments.getResource();
        if (resource != null) {
            resourceMapper.insertSelective(resource);
            CourseResource courseResource = new CourseResource();
            courseResource.setCourseId(courseId);
            courseResource.setResourceId(resource.getId());
            courseResource.setType(1);
            courseResourceMapper.insertSelective(courseResource);
        }
        //模块
        List<ResCourseModuleExperiments.ModuleInfo> modules = resCourseModuleExperiments.getModuleList();
        Integer orderId = modules.get(0).getOrderId();
        boolean isOlderCourse = false;
        if(orderId == null || orderId == 0){
            //老课程赋予默认的顺序
            isOlderCourse = true;
        }
        int i = 1;
        for (ResCourseModuleExperiments.ModuleInfo moduleInfo : modules) {
            Module module = new Module();
            module.setName(moduleInfo.getModuleName());
            module.setCourseId(courseId);
            //老课程orderId为null
            if(isOlderCourse){
                module.setOrderId(i++);
            }else {
                module.setOrderId(moduleInfo.getOrderId());
            }
            //插入模块
            moduleMapper.insertSelective(module);
            List<ResExperimentInfo> experimentInfos = moduleInfo.getModuleContent();
            int j = 1;
            for (ResExperimentInfo experimentInfo : experimentInfos) {
                //实验
                Experiment experiment = new Experiment();
                experiment.setName(experimentInfo.getExperimentName());
                experiment.setDescription(experimentInfo.getExperimentDes());
                experiment.setModuleId(module.getId());
                experiment.setCloudwareType(experimentInfo.getCloudwareTypeId());
                Date publishDate = stringConvertData(experimentInfo.getPublishDate());
                experiment.setPublishDate(publishDate);
                experiment.setExperimentContent(experimentInfo.getExperimentContent());
                experiment.setOldAnswerName(experimentInfo.getOldAnswerName());
                experiment.setIsAnswer(experimentInfo.getIsAnswer());
                experiment.setAnswerTableName(experimentInfo.getAnswerTableName());
                experiment.setAnswerUrl(experimentInfo.getAnswerUrl());
                //老课程orderId为null
                if(isOlderCourse){
                    experiment.setOrderId(j++);
                }else {
                    experiment.setOrderId(experimentInfo.getOrderId());
                }
                //experiment.setOrderId(experimentInfo.getOrderId());
                experimentMapper.insertSelective(experiment);
            }
        }
        resultMap.put("isHavingDatashireExperiment", isHavingDatashireExperiment);
        resultMap.put("courseName", courseName);
        //创建课程时为课程建立的文件夹，已courseId命名
        resultMap.put("oldCourseId", resCourseModuleExperiments.getCourseId());
        resultMap.put("newCourseId", courseId);
        return resultMap;
    }

    public Date stringConvertData(String dataStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dataStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解析数据库，表json字符串，建立对应的数据库和表
     *
     * @param dbJson
     * @return
     */
    public boolean parseDbJsonAndInsertData(String dbJson, String dbName) throws Exception {
        List<Map> maps = JSON.parseObject(dbJson, List.class, Feature.OrderedField);
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            String para = "?useUnicode=true&characterEncoding=UTF-8";

            connection = java.sql.DriverManager.getConnection(userDBIpAndPort + "/" + dbName + para, userDBusername, userDBpassword);
            statement = connection.createStatement();


            for (Map map : maps) {
                //给该课程建立表
                StringBuilder createTableSQL = new StringBuilder();
                String tableName = map.get("tableName") + "";
                String dropSQL = "DROP TABLE IF EXISTS " + tableName;
                //statement.addBatch(dropSQL);
                //createTableSQL.append(tableName).append("( ");
                List<Map> columns = (List) map.get("columnData");
                /*for (Map columnMap : columns) {
                    String columnName = columnMap.get("columnName") + "";
                    String columnType = columnMap.get("columnType") + "";
                    String length = columnMap.get("length") + "";
                    if(length.equals("0")){
                        createTableSQL.append(columnName).append(" " + columnType + " ").append(",");
                    }else {
                        createTableSQL.append(columnName).append(" " + columnType + "(")
                                .append(length + ")").append(",");

                    }
                }
                if (createTableSQL.toString().endsWith(",")) {
                    String sql = createTableSQL.toString().substring(0, createTableSQL.length() - 1);
                    createTableSQL = new StringBuilder(sql);
                }
                createTableSQL.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8");*/
                statement.addBatch(dropSQL.toString());
                String createSql = map.get("createSql") + "";
                statement.addBatch(createSql.replaceAll("\\n",""));
                //statement.executeBatch();
                //statement.clearBatch();
                //拼接插入数据SQL
                Map columnValueMap = (Map) map.get("columnValue");
                int i = 0;
                String numRegex = "^[0-9]+$";
                for (Object obj : columnValueMap.values()) {
                    StringBuilder sb = new StringBuilder("");
                    sb.append("INSERT INTO ").append(tableName).append(" VALUES").append("(");
                    List rowData = (List) obj;
                    for (Object value : rowData) {
                        if(value == null){
                            sb.append("NULL").append(",");
                        }else {
                            String string = ("" + value);
                            string = string.replaceAll("\\\\","\\\\\\\\").replaceAll("'","\\\\'");
                            //String str = strArr.toString() ;
                            if(string.matches(numRegex)){
                                sb.append(" " + value + ",");
                            }else {
                                sb.append("'" + string + "'" + ",");
                            }
                        }
                    }
                    //去掉多余的逗号
                    sb = new StringBuilder(sb.toString().substring(0, sb.length() - 1));
                    sb.append(")");
                    statement.addBatch(sb.toString());
                    i++;
                    //每2000条插入一次
                    if((i % 2000 == 0) && i != 0){
                        statement.executeBatch();
                        statement.clearBatch();
                        log.info("第" + (i * 2000) + "数据插入完成！");
                    }
                }
                //执行剩下的没有插入的SQL
                statement.executeBatch();
                statement.clearBatch();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("parseDbJsonAndInsertData插入数据错误！",e);
            throw new ApplicationErrorException(ErrorCode.ImportTrainingDbOrCloudDbError);
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    public static void main(String[] args){
        String str = "'xx";
        String str1 = "x'x";
        String str2 = "xx'";
        System.out.println(str);
        System.out.println(str.matches(".*'.*"));
        System.out.println(str1.matches(".*'.*"));
        System.out.println(str2.matches(".*'.*"));



        System.out.println((0 % 5000));
        System.out.println((55 % 5000));
        System.out.println((5000 % 5000));
        System.out.println((5200 % 5000));
        System.out.println((10000 % 5000));
        System.out.println((15000 % 5000));
    }

    public static void main3(String[] args) {
        String str = "\"'闹钟\\\"诗歌\"";
        String regex = ".*(').*(\\).*";
        str.replaceAll("'","\\\\'").replaceAll("\\\\","\\\\\\\\");
        String[] strArr = str.split("'");
        //String regex = "'";
        String regex2 = "\\\\";
        //if (userName.matches(regex)) {
        for (String s : strArr){
            if(s.matches(".*'.*")){
                s = s.replaceAll("'","\\\\'");
            }else if(s.matches(".*\\\\.*")){
                s = s.replaceAll("\\\\","\\\\\\\\");
            }
        }
        String result = "";
        for (String s : strArr){
            result += s;
        }

        System.out.println(result);
        /*String[] strArr = str.split("'");
        str = strArr[0].replaceAll("'","\\\\'");
        System.out.println(str);
        str = str.replaceAll("\\\\","\\\\\\\\");
        System.out.println(str);
        String a = "xxx'x'";
        System.out.println(a.replaceAll("'","\\\\'"));*/

    }


}
