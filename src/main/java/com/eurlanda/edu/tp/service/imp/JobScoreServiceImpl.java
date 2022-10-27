package com.eurlanda.edu.tp.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.eurlanda.edu.tp.Util.Utility;
import com.eurlanda.edu.tp.api.EduApi;
import com.eurlanda.edu.tp.dao.entity.Course;
import com.eurlanda.edu.tp.dao.entity.Experiment;
import com.eurlanda.edu.tp.dao.entity.JobScore;
import com.eurlanda.edu.tp.dao.entity.User;
import com.eurlanda.edu.tp.dao.entity.Student;
import com.eurlanda.edu.tp.dao.mapper.*;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.JobScoreService;
import com.eurlanda.edu.tp.webdomain.response.ResJobScore;
import com.eurlanda.edu.tp.webdomain.response.ResJobScoreList;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Date;

@Service
public class JobScoreServiceImpl implements JobScoreService{

    static Logger log = Logger.getLogger(JobScoreServiceImpl.class.getName());
    @Autowired
    private JobScoreMapper jobScoreMapper;

    @Autowired
    private EduApi eduApi;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ExperimentMapper experimentMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Value("${userDBIpAndPort}")
    private String userDBIpAndPort;

    @Value("${userDBusername}")
    private String userDBusername;

    @Value("${userDBpassword}")
    private String userDBpassword;

    @Value("${file.shareFileUrl}")
    private String shareFileUrl;

    @Value("${file.baseDir}")
    private String baseDir;

    @Value("${file.imageDir}")
    private String imageDir;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private StudentMapper studentMapper;

    private Connection connection = null;


    @Override
    public ResJobScoreList findScoreByCidAndCouidAndExpid(int classId,int courseId,int experimentId) {
        Map<String,Object> map=new HashMap<>();
        map.put("classId",classId);
        map.put("courseId",courseId);
        map.put("experimentId",experimentId);
        //查询班级中没有提交作业得学生
        List<Integer> notStuList=jobScoreMapper.findNotSubWorkStu(map);
        //查询班级中提交学生得学生
        List<Integer> stuList=jobScoreMapper.findSubWorkStu(map);
        //查询学生得作业信息
        ResJobScoreList resJobScoreList=new ResJobScoreList();
        List<ResJobScore> jobScoreList=new ArrayList<>();
        if(stuList!=null && stuList.size()>0){
            map.put("idList",stuList);
            jobScoreList= jobScoreMapper.findStudentScore(map);
            if(jobScoreList!=null && jobScoreList.size()>0){
                for(ResJobScore resJobScore:jobScoreList){
                    resJobScore.setSubmitDate(Utility.formatDate((Date)resJobScore.getCreateTime()));
                }
            }
        }
       /* //查询这个班级学生
        List<Map> studentList=studentClassMapper.getAllStudentByClassId(classId);
           List<Integer> classStudentList=new ArrayList<>();
        if(studentList!=null && studentList.size()>0){
            for(Map stuMap:studentList){
                classStudentList.add(Integer.parseInt(stuMap.get("userId").toString()));
            }
            //移除有成绩  剩下就是没提交作业的学生
            classStudentList.removeAll(jobUserList);
        }*/
        //没提交作业的学生信息
        if(notStuList!=null && notStuList.size()>0){
            map=new HashMap<>();
            map.put("idList",notStuList);
            List<Student> students=studentMapper.selectByUserIdList(map);
            for(Student stu:students){
                ResJobScore resJobScore=new ResJobScore();
                resJobScore.setStudentName(stu.getName());
                resJobScore.setSno(stu.getSno());
                resJobScore.setCreateTime(null);
                resJobScore.setScore(null);
                resJobScore.setGender(stu.getGender());
                jobScoreList.add(resJobScore);
            }
        }

        resJobScoreList.setJobScoreList(jobScoreList);
        return resJobScoreList;
    }
    /**
     * 每个5分钟去批改一下未批改的作业
     */
    //@Scheduled(cron = "0 0/5 * * * ? ")
    @Override
    public void correctingHomework() {
        //查询未批改的作业
        List<JobScore> jobScores = jobScoreMapper.selectUnapprovedHomework();
        Integer score = 0;
        JobScore paraJob = new JobScore();
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String para = "?useUnicode=true&characterEncoding=UTF-8";
            connection = java.sql.DriverManager.getConnection(userDBIpAndPort + para, userDBusername, userDBpassword);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage(),e);
        }
        try{
            log.info("-----------------自动批改作业开始执行------------------");
            int i = 1;
            for (JobScore jobScore : jobScores) {
                log.info("批改第" + i + "条作业开始,jobId:" + jobScore.getId());
                log.info("批改第" + i + "条作业开始,jobId:" + jobScore.getId());
                log.info("批改第" + i + "条作业开始,jobId:" + jobScore.getId());
                String tableName = jobScore.getTableName();
                String fileUrl = jobScore.getFileUrl();
                if (tableName != null) {
                    //数猎云类作业
                    try {
                        score = correctingShuLieHomework(jobScore);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("批改数猎云作业错误！，msg：" + e.getMessage(),e);
                    }
                } else if (fileUrl != null) {
                    //编程类作业
                    try{
                        score = correctingProgrammingHomework(jobScore);
                    }catch (Exception e){
                        e.printStackTrace();
                        log.error("批改编程类作业错误！，msg：" + e.getMessage(),e);
                    }
                } else {
                    log.error("correctingHomework查询数据异常");
                    continue;
                }
                log.info("批改第" + i + "条作业结束,jobId:" + jobScore.getId());
                log.info("批改第" + i + "条作业结束,jobId:" + jobScore.getId());

                log.info("批改第" + i + "条作业结束,jobId:" + jobScore.getId());

                paraJob.setId(jobScore.getId());
                paraJob.setScore(score);
                jobScoreMapper.updateByPrimaryKeySelective(paraJob);
            }
            log.info("-----------------自动批改作业执行完毕------------------");
        }finally {
            try{
                if (connection != null) {
                    connection.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
                e.getMessage();
            }
        }

    }

    @Override
    public String findStudentIsSubAnswer(Integer classId, Integer courseId, Integer experimentId, Integer userId) {
        Map<String,Object> selectMap=new HashMap<>();
        selectMap.put("classId",classId);
        selectMap.put("courseId",courseId);
        selectMap.put("experimentId",experimentId);
        selectMap.put("studentId",userId);
        List<JobScore> jobScoreList=jobScoreMapper.selectStuScoreByMap(selectMap);
        if(jobScoreList!=null && jobScoreList.size()>0){
            return "1";
        }
        return "0";
    }

    @Override
    public int deleteBystuIdAndCidAndCouId(Integer classId,Integer userId) {
        Map<String,Object> stuMap=new HashMap<>();
        stuMap.put("classId",classId);
        stuMap.put("studentId",userId);
        jobScoreMapper.deleteByCidAndCouidAndStuid(stuMap);
        return 0;
    }


    /**
     * 批改数猎类型作业
     * 全表比较，匹配一行，算一份，拿标准答案去匹配学生提交的答案
     * @param shuLieJob
     * @return
     */
    private Integer correctingShuLieHomework(JobScore shuLieJob) throws Exception {
        Integer score = 0;
        String studentTableName = shuLieJob.getTableName();//学生作业表名
        Integer userId = shuLieJob.getStudentId();
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            log.error("job|correctingShuLieHomework查询学生数据为null");
            return 0;
        }
        String repositoryIdStr = eduApi.getRepositoryIdByUsername(user.getUsername());
        if(repositoryIdStr.equals("error")){
            log.error("job|correctingShuLieHomework查询RepositoryId数据错误");
            return 0;
        }
        Map<String, Object> repositoryIdMap = JSONObject.parseObject(repositoryIdStr);
        String repositoryId = repositoryIdMap.get("repositoryId") + "";
        if (repositoryId.equals("null")) {
            log.error("job|correctingShuLieHomework查询repositoryId为null");
            return 0;
        }
        String stuDbName = "u" + repositoryId + "db";//学生的业务数据库名
        Integer courseId = shuLieJob.getCourseId();
        //查询cloudDbName
        Course course = courseMapper.selectByPrimaryKey(courseId);
        if (course == null) {
            log.error("job|correctingShuLieHomework查询课程信息为null");
            return 0;
        }
        String result = eduApi.getCloudDbNameByCourse(course.getName());
        log.info("job|getCloudDbNameByCourse：" + result);
        Map<String, Object> resultMap = JSONObject.parseObject(result, Map.class);
        String trainDbName = resultMap.get("trainDb") + "";//标准答案库名
        if (trainDbName.equals("null")) {
            log.error("job|correctingShuLieHomework查询cloudDbName为null");
            return 0;
        }
        trainDbName = "u" + trainDbName + "db";
        Integer experimentId = shuLieJob.getExperimentId();
        Experiment experiment = experimentMapper.selectByPrimaryKey(experimentId);
        if (experiment == null) {
            log.error("job|correctingShuLieHomework查询实验数据为null");
            return 0;
        }
        if(experiment.getIsAnswer().intValue() == 0){
            log.error("job|correctingShuLieHomework没有标准答案数据");
            return 0;
        }
        String answerTableName = experiment.getAnswerTableName();//标准答案表名
        if(answerTableName == null || answerTableName.equals("")) {
            log.error("job|correctingShuLieHomework查询标准答案表名为null");
            return 0;
        }


        PreparedStatement answerPs = null;
        PreparedStatement studentPs = null;
        ResultSet answerResultSet = null;
        ResultSet studentResultSet = null;
        StringBuilder sb = null;
        try {

            //遍历表获取字段和数据信息
            List<String> answerColumns = new ArrayList<>();//标准答案字段
            List<String> studentColumns = new ArrayList<>();//标准答案字段
            String answerSql = "select * from " + trainDbName + "." + answerTableName;//标准答案表
            answerPs = connection.prepareStatement(answerSql);
            answerResultSet = answerPs.executeQuery();
            ResultSetMetaData answerMeta = answerResultSet.getMetaData();
            int answerColumnCount = answerMeta.getColumnCount();
            //获取每个字段的名字，类型，长度
            for (int i = 0; i < answerColumnCount; i++) {
                answerColumns.add(answerMeta.getColumnName(i + 1));
            }

            String studentSql = "select * from " + stuDbName + "." + studentTableName;//学生答案表
            studentPs = connection.prepareStatement(studentSql);
            studentResultSet = studentPs.executeQuery();
            ResultSetMetaData studentMeta = studentResultSet.getMetaData();
            int studentColumnCount = studentMeta.getColumnCount();
            if (answerColumnCount != studentColumnCount) {
                log.info("job|标准答案表与学生表字段数量不一致");
                return 0;
            }
            //获取每个字段的名字，类型，长度
            for (int i = 0; i < studentColumnCount; i++) {
                studentColumns.add(studentMeta.getColumnName(i + 1));
            }

            for (String answerColumn : answerColumns) {
                boolean flag = false;
                for (String studentColumn : studentColumns) {
                    if (studentColumn.equals(answerColumn)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    log.info("job|标准答案表与学生表字段不一致");
                    return 0;
                }

            }
            //对学生表中答案做了去重处理，如果标准答案中有重复的记录分数会出现异常
            String compareSQL = "SELECT ROUND(COUNT(1)/(SELECT COUNT(1) FROM " + trainDbName + "." + answerTableName + ") * 100) score " +
                    "FROM " + trainDbName + "." + answerTableName + " t1" +
                    " INNER JOIN " + " (SELECT DISTINCT * from "+ stuDbName + "." + studentTableName +")" + " t2" ;
            sb = new StringBuilder(compareSQL);
            int i = 0;
            for (String answerColumn : answerColumns) {
                if (i++ == 0) {
                    sb.append(" ON ");
                } else {
                    sb.append(" AND ");
                }
                sb.append("t1.").append(answerColumn).append(" = t2.").append(answerColumn);
            }
            answerPs = connection.prepareStatement(sb.toString());
            answerResultSet = answerPs.executeQuery();
            String scoreStr = null;
            while (answerResultSet.next()) {
                scoreStr = answerResultSet.getString("score");
            }

            if (scoreStr != null) {
                score = Integer.parseInt(scoreStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("job|批改数猎云作业出错" + e.getMessage(),e);
            log.error("jon|批改数猎云执行SQL："+ sb.toString());
            return 0;
        } finally {
            if (answerResultSet != null) {
                answerResultSet.close();
            }
            if (studentResultSet != null) {
                studentResultSet.close();
            }
            if (answerPs != null) {
                answerPs.close();
            }
            if (studentPs != null) {
                studentPs.close();
            }


        }

        return score;
    }

    /**
     * 批改编程类的试验
     *
     * @param programmingJob
     * @return
     */
    private Integer correctingProgrammingHomework(JobScore programmingJob) throws Exception{
        Double score = 0.0;
        CSVReader answerReader = null;
        CSVReader studentReader = null;
        Integer experimentId = programmingJob.getExperimentId();
        Experiment experiment = experimentMapper.selectByPrimaryKey(experimentId);
        if (experiment == null) {
            log.error("job|correctingProgrammingHomework查询实验答案为null");
            return 0;
        }
        if(experiment.getIsAnswer() == 0){
            log.error("job|correctingProgrammingHomework没有标准答案数据");
            return 0;
        }
        Integer courseId = programmingJob.getCourseId();
        Course course = courseMapper.selectByPrimaryKey2(courseId);
        if(course == null){
            log.error("job|correctingProgrammingHomework错误，该课程不存在");
            return 0;
        }
        String folderName = course.getFolderName();
        if(folderName == null){
            course.setFolderName(System.currentTimeMillis() + "");
            courseMapper.updateByPrimaryKeySelective(course);
        }
        //标准答案文件地址
        String answerFilePath = baseDir + imageDir + "/" + course.getFolderName() + "/" + experiment.getAnswerUrl();
        //学生答案地址
        String studentFilePath = shareFileUrl + programmingJob.getStudentId() + "/" + programmingJob.getFileUrl();
        InputStreamReader answerIn = null;
        InputStreamReader studentIn = null;
        try {
            File answerFile = new File(answerFilePath);
            if (!answerFile.exists()) {
                log.error("job|标准答案地址：" + answerFilePath);
                log.error("job|correctingProgrammingHomework答案文件不存在");
                return 0;
            }
            File studentFile = new File(studentFilePath);
            if (!studentFile.exists()) {
                log.error("job|学生答案地址：" + studentFilePath);
                log.error("job|correctingProgrammingHomework学生文件不存在!");
                return 0;
            }
            //防止中文乱码
            answerIn = new InputStreamReader(new FileInputStream(answerFilePath),"UTF-8");
            studentIn = new InputStreamReader(new FileInputStream(studentFile),"UTF-8");
            RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().build();
            CSVReaderBuilder answerBuilder = new CSVReaderBuilder(answerIn).withCSVParser(rfc4180Parser);
            CSVReaderBuilder studentBuilder = new CSVReaderBuilder(studentIn).withCSVParser(rfc4180Parser);
            answerReader = answerBuilder.build();
            studentReader = studentBuilder.build();
            String[] answerCol = answerReader.readNext();
            String[] studentCol = studentReader.readNext();
            if(answerCol.length != studentCol.length){
                log.error("job|学生csv文件与标准答案字段数量不一致!");
                return 0;
            }
            boolean flag = false;
            Map<Integer,Integer> indexMap = new HashMap<>();
            for (int i = 0; i < answerCol.length ;i++){
                for (int j = 0; j < studentCol.length ;j++){
                    if(answerCol[i].equals(studentCol[j])){
                        flag = true;
                        indexMap.put(i,j);
                        break;
                    }
                }
                if(!flag){
                    log.error("job|学生csv文件与标准答案字段不一致!");
                    return 0;
                }
            }
            Double match = 0.0;
            List<String[]> studentData = studentReader.readAll();
            List<String[]> answerData = answerReader.readAll();
            for (int i = 0;i < answerData.size(); i++){
                String[] answerRow = answerData.get(i);
                for (int j = 0;j < studentData.size();j++){
                    String[] studentRow = studentData.get(j);
                    boolean isTrue = true;
                    for (int k = 0;k < answerRow.length;k++){
                        String answerStr = answerRow[k];
                        //不区分大小写
                        if(!answerStr.toLowerCase().equals(studentRow[indexMap.get(k)].toLowerCase())){
                            isTrue = false;
                            break;
                        }
                    }
                    //匹配到一行，停止匹配
                    if(isTrue){
                        match++;
                        log.info("----------匹配到" + match + "行-----------------，jobId：" + programmingJob.getId());
                        break;
                    }
                }
            }
            score = (match / answerData.size()) * 100;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("job|correctingProgrammingHomework解析csv错误：" + e.getMessage() + ",有可能是文件编码格式引起，请检查文件是否是UTF-8编码",e);
            return 0;
        }finally {
            try{
                if(answerReader != null){
                    answerReader.close();
                }
                if(studentReader != null){
                    studentReader.close();
                }
                if(answerIn != null){
                    answerIn.close();
                }
                if(studentIn != null){
                    studentIn.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("0");//格式化小数，不足的补0
        String scoreStr = decimalFormat.format(score);//返回的是String类型的
        return Integer.parseInt(scoreStr);
    }

    /**
     * 批改编程类的试验
     *
     * @param
     * @return
     */
   /* public Integer correctingProgrammingHomework(String answerFilePath,String studentFilePath) {
        Double score = 0.0;
        CSVReader answerReader = null;
        CSVReader studentReader = null;
        try {
            File answerFile = new File(answerFilePath);
            if (!answerFile.exists()) {
                log.error("correctingProgrammingHomework答案文件不存在");
                return 0;
            }
            File studentFile = new File(studentFilePath);
            if (!studentFile.exists()) {
                log.error("correctingProgrammingHomework学生文件不存在!");
                return 0;
            }
            InputStreamReader answerIn = new InputStreamReader(new FileInputStream(answerFilePath),"UTF-8");
            InputStreamReader studentIn = new InputStreamReader(new FileInputStream(studentFile),"UTF-8");
            RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().build();
            CSVReaderBuilder answerBuilder = new CSVReaderBuilder(answerIn).withCSVParser(rfc4180Parser);
            CSVReaderBuilder studentBuilder = new CSVReaderBuilder(studentIn).withCSVParser(rfc4180Parser);
            answerReader = answerBuilder.build();
            studentReader = studentBuilder.build();
            String[] answerCol = answerReader.readNext();
            String[] studentCol = studentReader.readNext();
            if(answerCol.length != studentCol.length){
                log.error("学生csv文件与标准答案字段数量不一致!");
                return 0;
            }
            boolean flag = false;
            Map<Integer,Integer> indexMap = new HashMap<>();
            for (int i = 0; i < answerCol.length ;i++){
                for (int j = 0; j < studentCol.length ;j++){
                    if(answerCol[i].equals(studentCol[j])){
                        flag = true;
                        indexMap.put(i,j);
                        break;
                    }
                }
                if(!flag){
                    log.error("学生csv文件与标准答案字段不一致!");
                    return 0;
                }
            }
            Double match = 0.0;
            List<String[]> studentData = studentReader.readAll();
            List<String[]> answerData = answerReader.readAll();
            for (int i = 0;i < answerData.size(); i++){
                String[] answerRow = answerData.get(i);
                for (int j = 0;j < studentData.size();j++){
                    String[] studentRow = studentData.get(j);
                    boolean isTrue = true;
                    for (int k = 0;k < answerRow.length;k++){
                        String answerStr = answerRow[k];
                        if(!answerStr.equals(studentRow[indexMap.get(k)])){
                            isTrue = false;
                            break;
                        }
                    }
                    if(isTrue){
                        match++;
                        break;
                    }
                }
            }
            score = (match / answerData.size()) * 100;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("correctingProgrammingHomework解析csv错误：" + e.getMessage());
            return 0;
        }finally {
            try{
                if(answerReader != null){
                    answerReader.close();
                }
                if(studentReader != null){
                    studentReader.close();
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return ((Long)Math.round(score)).intValue();
    }*/

    public static void main(String[] args) throws Exception {
        String str = "dsdsdsdsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssd ";
        String str1 = "dsdsdsdsd";

    }

}
