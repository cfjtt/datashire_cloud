package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.Util.Utility;
import com.eurlanda.edu.tp.api.EduApi;
import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.dao.entity.Experiment;
import com.eurlanda.edu.tp.dao.entity.User;
import com.eurlanda.edu.tp.dao.mapper.CourseMapper;
import com.eurlanda.edu.tp.dao.mapper.ExperimentMapper;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.CourseService;
import com.eurlanda.edu.tp.service.FileService;
import com.eurlanda.edu.tp.service.UserService;
import com.eurlanda.edu.tp.webdomain.response.ResUploadImage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FileServiceImp implements FileService {

    @Value("${file.baseUrl}")
    private String baseUrl;

    @Value("${file.baseDir}")
    private String baseDir;

    @Value("${file.imageDir}")
    private String imageDir;

    @Value("${file.markdownDir}")
    private String markdownDir;

    @Value("${file.reportDir}")
    private String reportDir;
    @Value("${file.shareFileUrl}")
    private String shareFileUrl;
    @Value("${file.courseFileUrl}")
    private String courseFileUrl;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private ExperimentMapper experimentMapper;
    @Autowired
    private EduApi eduApi;

    //private  String share="share/";
    @Override
    public ResUploadImage uploadImage(Map<String, Object> paraMap) throws ApplicationErrorException, IOException {
        MultipartFile file = (MultipartFile) paraMap.get("file");
        String type = paraMap.get("type") + "";
        String uploadFilePath = file.getOriginalFilename();
        String folderName = null;
        if (!uploadFilePath.endsWith(".jpg") &&
                !uploadFilePath.endsWith(".jpeg") &&
                !uploadFilePath.endsWith(".bmp") &&
                !uploadFilePath.endsWith(".png")) {
            throw new ApplicationErrorException(ErrorCode.FileIsNotImage);
        }
        File dir = null;
        String targetDir = null;
        Long currentTimeMillis = System.currentTimeMillis();
        if ("1".equals(type)) {
            //课程不存在
            targetDir = baseDir + imageDir + "/" + currentTimeMillis;
            dir = new File(targetDir);
            dir.setExecutable(true);
            dir.setWritable(true);
            dir.setReadable(true);
            dir.mkdirs();
        } else if ("2".equals(type)) {
            //课程已经存在
            Object courseIdObj = paraMap.get("courseId");
            if (courseIdObj == null) {
                throw new ApplicationErrorException(ErrorCode.GeneralError);
            }
            Integer courseId = Integer.parseInt(courseIdObj + "");
            synchronized (this) {
                //System.out.println(Thread.currentThread().getName() + "正在查询及修改库中数据");
                Course course = courseMapper.selectByPrimaryKey2(courseId);
                if (course == null) {
                    throw new ApplicationErrorException(ErrorCode.CourseNotExists);
                }
                folderName = course.getFolderName();
                if (folderName == null) {
                    targetDir = baseDir + imageDir + "/" + currentTimeMillis;
                    dir = new File(targetDir);
                    if(!dir.exists()){
                        dir.setExecutable(true);
                        dir.setWritable(true);
                        dir.setReadable(true);
                        boolean flag = dir.mkdirs();
                    }
                    //更新至数据库
                    course.setFolderName(currentTimeMillis + "");
                    courseMapper.updateByPrimaryKeySelective(course);
                } else {
                    targetDir = baseDir + imageDir + "/" + folderName;
                    dir = new File(targetDir);
                    if(!dir.exists()){
                        dir.setExecutable(true);
                        dir.setWritable(true);
                        dir.setReadable(true);
                        boolean flag = dir.mkdirs();
                    }
                }
            }
        }


        ResUploadImage resUploadImage = null;
        //System.out.println(Thread.currentThread().getName() + "正在执行，file：" + file.getOriginalFilename());
        String newFileName = saveFile(file, targetDir, 1);
        resUploadImage = new ResUploadImage();
        if(folderName == null){
            folderName = currentTimeMillis + "";
        }
        resUploadImage.setUrl(folderName + "/" + newFileName);
        //System.out.println(Thread.currentThread().getName() + "正在执行，url为：" + resUploadImage.getUrl());
        File image = new File(targetDir + "/" + newFileName);
        FileInputStream in = null;
        try {
            in = new FileInputStream(image);
            BufferedImage bufferedImage = ImageIO.read(in);
            resUploadImage.setName(file.getOriginalFilename());
            resUploadImage.setWidth(bufferedImage.getWidth());
            resUploadImage.setHeight(bufferedImage.getHeight());
            if ("1".equals(type)) {
                //课程不存在返回为该课程创建的文件夹名
                resUploadImage.setFolderName(currentTimeMillis + "");
            }
        } catch (FileNotFoundException e) {
            throw new ApplicationErrorException(ErrorCode.FileIsNotExist);
        }finally {
            if(in != null){
                in.close();
            }
        }
        return resUploadImage;
    }

    /**
     * 预览图片
     *
     * @param path
     * @param response
     */
    @Override
    public void previewImage(String path, HttpServletResponse response) {
        //根据courseId查找出当前的图片，图片不存在，那么展示默认图片
        response.setContentType("image/jpeg");
        response.setDateHeader("expries", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        File file = new File(path);
        FileInputStream in = null;
        ServletOutputStream out = null;
        try {
            in = new FileInputStream(file);
            out = response.getOutputStream();
            IOUtils.copy(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
    }


    /**
     * 共享文件夹上传文件
     *
     * @param userId
     * @param path
     * @param file
     * @param isCheckExists
     * @param type          1:用户   2：课程
     * @return
     * @throws ApplicationErrorException
     * @throws IOException
     */
    @Override
    public String shareUploadFile(String userId, String path, MultipartFile file, String isCheckExists, int type) throws ApplicationErrorException, IOException {
        String targetDir = "";
        String url = "";
        if (type == 1) {
            //查询userID 是否存在于数据库中
            User user = userService.getUserInfo(Integer.parseInt(userId));
            if (user == null) {
                return "3";
            }
            url = shareFileUrl;
        } else {
            //查询课程 是否存在于数据库中
            Course course = courseService.getCourseById(Integer.parseInt(userId));
            if (course == null) {
                return "4";
            }
            url = courseFileUrl;
        }
        if (path.equals("/")) {
            targetDir = url + userId;
        } else {
            targetDir = url + userId + path;
        }

        File isdir = new File(targetDir + "/" + file.getOriginalFilename());
        if (isCheckExists.equals("1")) {
            isdir.setExecutable(true);
            isdir.setWritable(true);
            isdir.setReadable(true);
            if (isdir.exists()) {
                if (isdir.isDirectory()) {
                    return "5";//同名的是个文件
                } else {
                    throw new ApplicationErrorException(ErrorCode.FileIsExist);
                }
            }
        }
        File dir = new File(targetDir);
        if (!dir.exists()) {
            dir.mkdirs();
            dir.setExecutable(true);
            dir.setWritable(true);
            dir.setReadable(true);
        }
        String newFileName = "";
        if (isCheckExists.equals("2") && isdir.exists() && isdir.isDirectory()) {
            FileUtils.deleteDirectory(isdir);
            newFileName = saveFile(file, targetDir, 2);
        } else {
            newFileName = saveFile(file, targetDir, 2);
        }


        return newFileName;
    }

    /**
     * 删除共享文件夹中文件
     *
     * @param userId
     * @param fileName
     * @param path
     * @return
     * @throws ApplicationErrorException
     * @throws IOException
     */
    @Override
    public String deleteShareFile(String userId, String fileName, String path, int type) throws ApplicationErrorException, IOException {
        String stats = "";
        try {
            String url = "";
            if (type == 1) {
                url = shareFileUrl;
            } else {
                url = courseFileUrl;
            }
            String deleteFilePath = "";
            if (path.equals("/")) {
                deleteFilePath = url + userId + path + fileName;
            } else {
                deleteFilePath = url + userId + path + "/" + fileName;
            }
            File file = new File(deleteFilePath);
            if (file.exists()) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    FileUtils.deleteDirectory(file);
                }
                stats = "1";
            } else {
                stats = "0";
            }

        } catch (Exception e) {
            throw new ApplicationErrorException(ErrorCode.FileIsNotExist);
        }
        return stats;
    }

    /**
     * 下载共享文件夹中文件
     *
     * @param userId
     * @param fileName
     * @param path
     * @param response
     * @throws ApplicationErrorException
     * @throws IOException
     */
    @Override
    public void downloadShareFile(String userId, String fileName, String path, HttpServletResponse response, int type) throws ApplicationErrorException, IOException {
        try {
            String url = "";
            if (type == 1) {
                url = shareFileUrl;
            } else {
                url = courseFileUrl;
            }
            String downloadPath = "";
            if (path.equals("/")) {
                downloadPath = url + userId;
            } else {
                downloadPath = url + userId + path;
            }
            fileName = URLDecoder.decode(fileName, "UTF-8");
            getFile(fileName, response, downloadPath);
        } catch (Exception e) {
            throw new ApplicationErrorException(ErrorCode.FileIsNotExist);
        }
    }

    /**
     * 共享文件夹 重命名
     *
     * @param userId
     * @param oldfileName
     * @param newFileName
     * @param path
     * @return
     * @throws ApplicationErrorException
     * @throws IOException
     */
    @Override
    public String renameShareFile(String userId, String oldfileName, String newFileName, String path, int type) throws ApplicationErrorException, IOException {
        String stats = "";
        try {
            String oldfilePath = "";
            String newFilePath = "";
            String url = "";
            if (type == 1) {
                url = shareFileUrl;
            } else {
                url = courseFileUrl;
            }
            newFileName = URLDecoder.decode(newFileName, "utf-8");
            if (path.equals("/")) {
                oldfilePath = url + userId + path + oldfileName;
                newFilePath = url + userId + path + newFileName;
            } else {
                oldfilePath = url + userId + path + "/" + oldfileName;
                newFilePath = url + userId + path + "/" + newFileName;
            }
            File oldFile = new File(oldfilePath);
            File newFile = new File(newFilePath);
            if (newFile.exists() && !oldfileName.equals(newFileName)) {
                stats = "2";
                return stats;
            }
            if (oldFile.exists()) {
                boolean flag = oldFile.renameTo(newFile);
                if (flag) {
                    stats = "0";
                    newFile.setExecutable(true);
                    newFile.setWritable(true);
                    newFile.setReadable(true);
                } else {
                    stats = "1";
                }
            } else {
                throw new ApplicationErrorException(ErrorCode.FileIsNotExist);
            }


        } catch (Exception e) {
            throw new ApplicationErrorException(ErrorCode.FileIsNotExist);
        }
        return stats;
    }

    /**
     * 共享文件夹  获取所有文件
     *
     * @param userId
     * @param path
     * @param type   1:查询用户共享文件夹  2：查询课程共享文件夹
     * @return
     * @throws ApplicationErrorException
     * @throws IOException
     */
    @Override
    public List<Map<String, Object>> getShareList(String userId, String path, int type) throws ApplicationErrorException, IOException {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String filePath = "";
            if (type == 1) {
                if (path.equals("/")) {
                    filePath = shareFileUrl + userId;
                } else {
                    filePath = shareFileUrl + userId + path;
                }
            } else {
                if (path.equals("/")) {
                    filePath = courseFileUrl + userId;
                } else {
                    filePath = courseFileUrl + userId + path;
                }
            }

            File baseFile = new File(filePath);
            if (baseFile.exists()) {
                File[] files = baseFile.listFiles();
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", file.getName());
                        map.put("time", sdf.format(new Date(file.lastModified())));
                        if (file.isFile()) {
                            map.put("size", file.length());
                            map.put("type", 1);
                        } else if (file.isDirectory()) {
                            map.put("size", FileUtils.sizeOfDirectory(file));
                            map.put("type", 2);
                        }
                        mapList.add(map);
                    }
                }
            }

        } catch (Exception e) {
            throw new ApplicationErrorException(ErrorCode.FileIsNotExist);
        }
        return mapList;
    }
    @Override
    public List<Map<String, Object>> getJobShareList(Integer userId, String path, int type) throws ApplicationErrorException, IOException {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String filePath = "";
            if (type == 1) {
                if (path.equals("/")) {
                    filePath = shareFileUrl + userId;
                } else {
                    filePath = shareFileUrl + userId + path;
                }
            } else {
                if (path.equals("/")) {
                    filePath = courseFileUrl + userId;
                } else {
                    filePath = courseFileUrl + userId + path;
                }
            }

            File baseFile = new File(filePath);
            if (baseFile.exists()) {
                File[] files = baseFile.listFiles();
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("label", file.getName());
                        map.put("time", sdf.format(new Date(file.lastModified())));
                        if (file.isFile()) {
                            map.put("size", file.length());
                            map.put("type", 1);
                        } else if (file.isDirectory()) {
                            map.put("size", FileUtils.sizeOfDirectory(file));
                            map.put("type", 2);
                        }
                        mapList.add(map);
                    }
                }
            }

        } catch (Exception e) {
            throw new ApplicationErrorException(ErrorCode.FileIsNotExist);
        }
        return mapList;
    }


    /**
     * 根据路径查询文件是否存在
     */
    @Override
    public String findFileIsExists(String userId, String path, int type) {
        String filePath = "";
        if (type == 1) {
            filePath = shareFileUrl + userId + path;
        } else {
            filePath = courseFileUrl + userId + path;
        }
        File file = new File(filePath);
        if (file.exists()) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * 获取实验作业答案
     * @param experimentId
     * @param type 0:编程类  1：数猎云
     * @return
     * @throws ApplicationErrorException
     * @throws IOException
     */
    @Override
    public List<Map<String, Object>> getExperimentAnswerFile(int courseId,int experimentId,int type,String courseName) throws ApplicationErrorException, IOException {
        List<Map<String, Object>> mapList = new ArrayList<>();
        if(experimentId>0){
            //查询这个数猎实验对应的关联表名称
            Experiment experiment=experimentMapper.selectByPrimaryKey(experimentId);
            if(type==0){
                if(experiment!=null && experiment.getAnswerUrl()!=null && !experiment.getAnswerUrl().equals("")){
                    Course course = courseMapper.selectByPrimaryKey2(courseId);
                    if(course == null){
                        throw new ApplicationErrorException(ErrorCode.CourseNotExists);
                    }
                    String folderName = course.getFolderName();
                    if(folderName == null){
                        course.setFolderName(System.currentTimeMillis() + "");
                        courseMapper.updateByPrimaryKeySelective(course);
                    }
                    String url = baseDir + imageDir + "/" + course.getFolderName() + "/" + experiment.getAnswerUrl();
                    File baseFile=new File(url);
                    if (baseFile.exists()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", experiment.getOldAnswerName());
                        if (baseFile.isFile()) {
                            map.put("size", baseFile.length());
                        }
                        mapList.add(map);
                    }else{
                        experiment.setIsAnswer(0);
                        experiment.setAnswerUrl("");
                        experiment.setOldAnswerName("");
                        experimentMapper.updateByPrimaryKeySelective(experiment);
                    }
                }
            }else{
                if(experiment!=null && experiment.getAnswerTableName()!=null && !experiment.getAnswerTableName().equals("")){
                    //查询这个表在cloudDB 中存在不
                      Map<String,Object> isMap=eduApi.checkCloudTableIsExists(null,courseName,experiment.getAnswerTableName(),1);
                      if(isMap!=null && isMap.size()>0){
                          String isExists=(String)isMap.get("isExists");
                          if(isExists.equals("1")){
                              Map<String, Object> map = new HashMap<>();
                              map.put("name",experiment.getAnswerTableName());
                              mapList.add(map);
                          }else if(isExists.equals("3")){//表不存在，将这个实验的答案清楚
                              experiment.setIsAnswer(0);
                              experiment.setAnswerTableName("");
                              experimentMapper.updateByPrimaryKeySelective(experiment);
                          }
                      }
                }
            }
        }
        return mapList;
    }


    /**
     *
     * @param courseId
     * @param experimentId
     * @param type 0:编程类 1：数猎云
     * @param tableName
     * @param file
     * @return
     */
    @Override
    public String submitExperimentAnswerFile(int courseId,int experimentId, int type, String tableName,String courseName, MultipartFile file)throws ApplicationErrorException,IOException{
        // 数猎云
        try{
            Experiment experiment=experimentMapper.selectByPrimaryKey(experimentId);
            if(experiment!=null){
                if(type==1){
                    //查询表是否存在
                    Map<String,Object> isMap=eduApi.checkCloudTableIsExists(null,courseName,tableName,1);
                    if(isMap!=null && isMap.size()>0){
                        String isExists=(String)isMap.get("isExists");
                        if(isExists.equals("1")){
                            experiment.setAnswerTableName(tableName);
                            experiment.setIsAnswer(1);
                            int a=experimentMapper.updateByPrimaryKeySelective(experiment);
                            if(a>0){
                                return tableName;
                            }
                        }else{
                            //表不存在
                            throw  new ApplicationErrorException(ErrorCode.DBTABLENOTEXISTS);
                        }
                    }else{
                        //表不存在
                        throw  new ApplicationErrorException(ErrorCode.DBTABLENOTEXISTS);
                    }

                }else if(type==0){
                    Course course = courseMapper.selectByPrimaryKey2(courseId);
                    if(course == null){
                        throw new ApplicationErrorException(ErrorCode.CourseNotExists);
                    }
                    String folderName = course.getFolderName();
                    if(folderName == null){
                        course.setFolderName(System.currentTimeMillis() + "");
                        courseMapper.updateByPrimaryKeySelective(course);
                    }
                    String targetDir = baseDir + imageDir + "/" +course.getFolderName();
                    File dir=new File(targetDir);
                    if(!dir.exists()){
                        dir.mkdirs();
                        dir.setExecutable(true);
                        dir.setWritable(true);
                        dir.setReadable(true);
                    }
                    //上传文件
                    experiment.setOldAnswerName(file.getOriginalFilename());
                    String answerUrl=saveFile(file,targetDir,3);
                    if(answerUrl!=null && !answerUrl.equals("")){
                        experiment.setIsAnswer(1);
                        experiment.setAnswerUrl(answerUrl);
                        int a=experimentMapper.updateByPrimaryKeySelective(experiment);
                        if(a>0){
                            return answerUrl;
                        }
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ApplicationErrorException(ErrorCode.UPLOADFILE);
        }
        throw new ApplicationErrorException(ErrorCode.UPLOADFILE);
    }

    /**
     * 管理员删除作业答案
     * @param courseId
     * @param experimentId
     * @param type
     * @param tableName
     * @return
     * @throws ApplicationErrorException
     * @throws IOException
     */
    @Override
    public String deleteExperimentAnswer(int courseId, int experimentId, int type, String tableName) throws ApplicationErrorException, IOException {
        //数猎云 需要将实验对应的关联dbName 置为空。
        Experiment experiment=experimentMapper.selectByPrimaryKey(experimentId);
        if(experiment!=null){
            if(type==1){
                experiment.setIsAnswer(0);
                experiment.setAnswerTableName("");
                experimentMapper.updateByPrimaryKeySelective(experiment);
            }else if(type==0){
                Course course = courseMapper.selectByPrimaryKey2(courseId);
                if(course == null){
                    throw new ApplicationErrorException(ErrorCode.CourseNotExists);
                }
                String folderName = course.getFolderName();
                if(folderName == null){
                    course.setFolderName(System.currentTimeMillis() + "");
                    courseMapper.updateByPrimaryKey(course);
                }
                File file=new File(baseDir + imageDir + "/" +course.getFolderName()+ "/" + experiment.getAnswerUrl());
                if(file.exists()){
                    boolean flag=file.delete();
                    if(flag){
                        experiment.setIsAnswer(0);
                        experiment.setOldAnswerName("");
                        experiment.setAnswerUrl("");
                        experimentMapper.updateByPrimaryKeySelective(experiment);
                    }
                }
            }
        }
        return null;
    }



    /**
     * 共享文件夹 创建文件夹
     *
     * @param userId
     * @param directoryPath
     * @param path
     * @return
     */
    @Override
    public String createShareDirectory(String userId, String directoryPath, String path, int type) {
        String stats = "";
        String filePath = "";
        String url = "";
        if (type == 1) {
            url = shareFileUrl;
        } else {
            url = courseFileUrl;
        }
        try {
            directoryPath = URLDecoder.decode(directoryPath, "UTF-8");
        } catch (Exception e) {
        }

        if (path.equals("/")) {
            if (directoryPath != null && !directoryPath.equals("")) {
                if (!directoryPath.startsWith("/")) {
                    filePath = url + userId + path + directoryPath;
                } else {
                    filePath = url + userId + directoryPath;
                }

            }
        } else {
            if (directoryPath != null && !directoryPath.equals("")) {
                if (!directoryPath.startsWith("/")) {
                    filePath = url + userId + path + "/" + directoryPath;
                } else {
                    filePath = url + userId + path + directoryPath;
                }
            }
        }

        File file = new File(filePath);
        file.setExecutable(true);
        file.setWritable(true);
        file.setReadable(true);
        if (!file.exists()) {
            file.mkdirs();
            stats = "1";
        } else {
            stats = "2";//文件夹已经存在
        }
        return stats;
    }

    /**
     * 共享文件夹 移动文件
     *
     * @param userId
     * @param targetPath
     * @param fileNameList
     * @param isCheckExists
     * @return
     * @throws ApplicationErrorException
     * @throws IOException
     */
    @Override
    public String moveShareFile(String userId, String targetPath, List<String> fileNameList, String isCheckExists, int type) throws ApplicationErrorException, IOException {
        String target = "";
        String stats = "";
        String url = "";
        if (type == 1) {
            url = shareFileUrl;
        } else {
            url = courseFileUrl;
        }
        if (targetPath != null && !targetPath.equals("")) {
            if (!targetPath.startsWith("/")) {
                target = url + userId + "/" + targetPath;
            } else {
                target = url + userId + targetPath;
            }

        }
        //目标文件夹不存在
        File targetFile = new File(target);
        if (!targetFile.exists()) {
            return "3";
        } else {
            //判断目标文件是否是文件夹
            if (!targetFile.isDirectory()) {
                return "2";
            }
        }
        String fileStr = "";
        for (String fileName : fileNameList) {
            String oldFilePath = url + userId + fileName;
            File oldFile = new File(oldFilePath);
            oldFile.setExecutable(true);
            oldFile.setWritable(true);
            oldFile.setReadable(true);
            String name = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
            File file = new File(targetFile.getPath() + "/" + name);
            file.setExecutable(true);
            file.setWritable(true);
            file.setReadable(true);
            if (oldFile.getPath().equals(targetFile.getPath())) {
                return "5";
            }

            //1：代表第一次请求 检查是否存在同名文件
            if (isCheckExists.equals("1")) {
                if (file.exists()) {
                    if (file.isDirectory() || oldFile.isDirectory()) {
                        return "4";
                    } else {
                        if (fileStr.equals("")) {
                            fileStr = fileName;
                        } else {
                            fileStr = fileStr + "," + fileName;
                        }
                        continue;

                    }
                }
            }
            if (oldFile.exists()) {
                if (oldFile.getPath().equals(file.getPath())) {
                    return "0";
                }
                if (isCheckExists.equals("2") && file.exists()) {
                    if (oldFile.isDirectory()) {
                        FileUtils.copyDirectoryToDirectory(oldFile, targetFile);
                        FileUtils.deleteDirectory(oldFile);
                    } else {
                        FileUtils.copyFile(oldFile, file);
                        //删除原来的文件
                        oldFile.delete();
                    }
                } else {
                    oldFile.renameTo(file);
                }

            }
        }
        if (fileStr != null && !fileStr.equals("")) {
            //说明有文件重复
            stats = "目标文件夹已存在" + fileStr;
            // throw new ApplicationErrorException(ErrorCode.FileIsExist);
        } else {
            stats = "0";
        }
        return stats;
    }


    @Override
    public String uploadMarkdown(MultipartFile file) throws ApplicationErrorException, IOException {
        String uploadFilePath = file.getOriginalFilename();

        if (!uploadFilePath.endsWith(".md")) {
            throw new ApplicationErrorException(ErrorCode.FileIsNotMarkdown);
        }

        String targetDir = baseDir + markdownDir;

        File dir = new File(targetDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String newFileName = saveFile(file, targetDir, 1);


        return baseUrl + "/markdown/" + newFileName;
    }

    @Override
    public String uploadMarkdownWithResolve(MultipartFile file) throws ApplicationErrorException, IOException {
        StringBuffer buffer = new StringBuffer("");
        if (file != null) {
            InputStream fis = file.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
        }
        return buffer.toString();
    }

    @Override
    public String uploadReport(MultipartFile file) throws ApplicationErrorException, IOException {
        String uploadFilePath = file.getOriginalFilename();

        if (!uploadFilePath.endsWith(".pdf")) {
//                !uploadFilePath.endsWith(".doc") &&
//                !uploadFilePath.endsWith(".docx")) {
            throw new ApplicationErrorException(ErrorCode.InvalidReportType);
        }

        String currentUserName = Utility.getCurrentUserName();

        String targetDir = baseDir + reportDir + File.separator + currentUserName;

        File dir = new File(targetDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String newFileName = saveFile(file, targetDir, 1);


        return String.format("%s%s/%s/%s", baseUrl, reportDir, currentUserName, newFileName);
    }

    @Override
    public void downloadImage(String fileName, HttpServletResponse response) throws ApplicationErrorException, IOException {

        getFile(fileName, response, baseDir + imageDir);
    }

    @Override
    public void downloadMarkdown(String fileName, HttpServletResponse response) throws ApplicationErrorException {

        try {
            getFile(fileName, response, baseDir + markdownDir);
        } catch (Exception e) {
            throw new ApplicationErrorException(ErrorCode.FileIsNotExist);
        }
    }

    @Override
    public void downloadReport(String path, String fileName, HttpServletResponse response) throws ApplicationErrorException {

        try {
            getFile(fileName, response, baseDir + reportDir + File.separator + path);
        } catch (Exception e) {
            throw new ApplicationErrorException(ErrorCode.FileIsNotExist);
        }
    }


    public String saveFile(MultipartFile file, String dir, int type) throws IOException {
        String uploadFilePath = file.getOriginalFilename();
        String uploadFileName = "";
        String uploadFileSuffix = "";
        if (uploadFilePath.lastIndexOf('\\') + 1 >= 0 && uploadFilePath.lastIndexOf('.') > 0) {
            uploadFileName = uploadFilePath.substring(uploadFilePath.lastIndexOf('\\') + 1, uploadFilePath.lastIndexOf('.'));
            uploadFileSuffix = uploadFilePath.substring(uploadFilePath.lastIndexOf('.') + 1);
        } else {
            uploadFileName = uploadFilePath;
        }

        if (type == 1) {
            Random random = new Random(System.currentTimeMillis());
            Double next = random.nextDouble();
            String seed = (next + "").substring(2);
            uploadFileName = String.format("%s", seed + Utility.formatDate(new Date(), Utility.shortDateFormat));
        }else if(type==3){
            uploadFileName="A_"+UUID.randomUUID();
        }
        try {

            if (uploadFileSuffix != null && !uploadFileSuffix.equals("")) {
                File newFile = new File(dir + "/" + uploadFileName + "." + uploadFileSuffix);
                file.transferTo(newFile);
            } else {
                File newFile = new File(dir + "/" + uploadFileName);
                file.transferTo(newFile);
            }

        }  catch (Exception e){
            e.printStackTrace();
            return "";
        }

        return uploadFileName + '.' + uploadFileSuffix;
    }

    /**
     * 保存图片，图片的命名规则是id为名字，不需要后缀
     * @param file
     * @param dir
     * @param id
     * @return
     */
    public void saveFileByNameWithId(MultipartFile file, String dir, int id) throws IOException {
        String uploadPath = dir+"/"+id;
        File newFile = new File(uploadPath);
        file.transferTo(newFile);
    }

    private String coverFile(File file, String dir, int type) throws IOException {
        String uploadFilePath = file.getName();
        String uploadFileName = "";
        String uploadFileSuffix = "";
        if (uploadFilePath != null && !uploadFilePath.equals("")) {
            if (uploadFilePath.lastIndexOf("\\") > 0 && uploadFilePath.lastIndexOf('.') > 0) {
                uploadFileName = uploadFilePath.substring(uploadFilePath.lastIndexOf('\\') + 1, uploadFilePath.lastIndexOf('.'));
                uploadFileSuffix = uploadFilePath.substring(uploadFilePath.lastIndexOf('.') + 1);
            } else {
                uploadFileName = uploadFilePath;
            }
            if (type == 1) {
                uploadFileName = String.format("%s_%s", uploadFileName, Utility.formatDate(new Date(), Utility.longDateFormat));
            }
            FileOutputStream fos = null;
            FileInputStream fis = null;
            try {

                fis = new FileInputStream(file);
                if (uploadFileSuffix != null && !uploadFileSuffix.equals("")) {
                    fos = new FileOutputStream(new File(dir + "/" + uploadFileName + ".") + uploadFileSuffix);
                } else {
                    fos = new FileOutputStream(new File(dir + "/" + uploadFileName));
                }
                byte[] temp = new byte[1024];
                int i = fis.read(temp);
                while (i != -1) {
                    fos.write(temp, 0, temp.length);
                    i = fis.read(temp);
                }
                fos.flush();

            } finally {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            }
        }


        return uploadFileName + '.' + uploadFileSuffix;
    }

    public void getFile(String fileName, HttpServletResponse response, String dir) throws ApplicationErrorException, IOException {

        final String filePath = dir + "/" + fileName;

        File file = new File(filePath);

        if (!file.exists()) {
            throw new ApplicationErrorException(ErrorCode.FileIsNotExist);
        }

        // fix chinese filename problem
        String filename = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
        //response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/force-download");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
        response.setContentLengthLong(file.length());
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                i = bis.read(buff);
            }
            os.flush();
        } finally {
            if (bis != null) {
                bis.close();
            }
        }
    }
}