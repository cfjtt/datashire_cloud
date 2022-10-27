package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.Util.CompressAndDecompressionUtils;
import com.eurlanda.edu.tp.Util.Utility;
import com.eurlanda.edu.tp.api.EduApi;
import com.eurlanda.edu.tp.dao.entity.*;
import com.eurlanda.edu.tp.dao.mapper.*;
import com.eurlanda.edu.tp.enums.CloudwareTypeEnum;
import com.eurlanda.edu.tp.enums.ResourceTypeEnum;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.*;
import com.eurlanda.edu.tp.webdomain.request.ReqAddCourse;
import com.eurlanda.edu.tp.webdomain.request.ReqDeleteClass;
import com.eurlanda.edu.tp.webdomain.request.ReqDeleteCourse;
import com.eurlanda.edu.tp.webdomain.request.ReqDeleteModule;
import com.eurlanda.edu.tp.webdomain.response.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class CourseServiceImp implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseResourceMapper courseResourceMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ExperimentMapper experimentMapper;

    @Autowired
    private ClassService classService;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private HomeworkMapper homeworkMapper;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentHomeworkMapper studentHomeworkMapper;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EduApi eduApi;

    @Value("${file.baseDir}")
    private String baseDir;

    @Value("${file.imageDir}")
    private String imageDir;

    @Value("${file.courseFileUrl}")
    private String courseFolderPath;

    @Autowired
    private RancherService rancherService;
    @Autowired
    private ClassCourseMapper classCourseMapper;
    @Autowired
    private JobScoreMapper jobScoreMapper;

    @Value("${rancher.secret}")
    private String secret;

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ImportAndExportServiceImpl.class.getName());

    public Course getCourseById(int courseID) throws ApplicationErrorException {

        Course course = courseMapper.selectByPrimaryKey2(courseID);

        if (course == null) {
            throw new ApplicationErrorException(ErrorCode.CourseNotExists);
        }

        course.setImageUrl(this.getImageUrl(courseID));
        return course;
    }

    @Override
    public ResCourseModuleExperiments getClassModuleExperiments(int classId) throws ApplicationErrorException {
        Clazz clazz = classService.getClassById(classId);
        Course course = this.getCourseById(clazz.getCourseId());
        return getCourseModuleExperiments(course);
    }

    @Override
    public ResCourseModuleExperiments getCourseModuleExperiments(int courseId) throws ApplicationErrorException {
        Course course = this.getCourseById(courseId);

        return getCourseModuleExperiments(course);
    }
    @Override
    public ResCourseModuleExperiments getCourseModuleExperimentsAndAnswer(Integer courseId,Integer userId,Integer classId) throws ApplicationErrorException {
        Course course = this.getCourseById(courseId);
        Clazz clazz=clazzMapper.selectByPrimaryKey(classId);
        return getCourseModuleExperimentsAndAnswer(course,userId,clazz);
    }


    private ResCourseModuleExperiments getCourseModuleExperimentsAndAnswer(Course course,int userId,Clazz clazz) {
        ResCourseModuleExperiments courseDetail = new ResCourseModuleExperiments();

        courseDetail.setCourseName(course.getName());
        courseDetail.setCourseId(course.getId());

        List<ResCourseModuleExperiments.ModuleInfo> moduleList = new ArrayList<>();
        courseDetail.setModuleList(moduleList);
        ResCourseModuleExperiments.ModuleInfo module = new ResCourseModuleExperiments.ModuleInfo();

        for (Map moduleInfo : moduleMapper.selectModuleExperimentInfoByCourseId(course.getId())) {
            int moduleId = (int) moduleInfo.get("moduleId");
            if (module.getModuleId() != moduleId) {
                module = new ResCourseModuleExperiments.ModuleInfo();
                moduleList.add(module);
                module.setModuleContent(new ArrayList<>());
                module.setModuleId(moduleId);
                module.setModuleName((String) moduleInfo.get("moduleName"));
                Object moOrder = moduleInfo.get("moOrderId");
                if (moOrder == null) {
                    module.setOrderId(0);
                } else {
                    module.setOrderId(Integer.parseInt(moOrder + ""));
                }
            }

            if (moduleInfo.get("experimentId") != null) {
                ResExperimentInfo resExperimentInfo = new ResExperimentInfo();
                module.getModuleContent().add(resExperimentInfo);
                //根据班级，实验，课程，用户 查询是否提交作业
                Map<String,Object> selectMap=new HashMap<>();
                selectMap.put("classId",clazz.getId());
                selectMap.put("courseId",course.getId());
                selectMap.put("experimentId",moduleInfo.get("experimentId"));
                selectMap.put("studentId",userId);
                List<JobScore> jobScoreList=jobScoreMapper.selectStuScoreByMap(selectMap);
                if(jobScoreList!=null && jobScoreList.size()>0){
                   resExperimentInfo.setStuIsAnswer("1");
                }else{
                    resExperimentInfo.setStuIsAnswer("0");
                }
                resExperimentInfo.setId((int) moduleInfo.get("experimentId"));
                resExperimentInfo.setExperimentName((String) moduleInfo.get("experimentName"));
                resExperimentInfo.setExperimentDes((String) moduleInfo.get("experimentDes"));
                CloudwareTypeEnum cloudwareTypeEnum = CloudwareTypeEnum.fromInt(
                        (int) moduleInfo.get("cloudwareType")
                );
                resExperimentInfo.setExperimentContent(moduleInfo.get("experimentContent") == null ? "" : moduleInfo.get("experimentContent").toString());
                resExperimentInfo.setCloudwareType(cloudwareTypeEnum == null ? "" : cloudwareTypeEnum.getZh());
                resExperimentInfo.setDueDate(Utility.formatDate((Date) moduleInfo.get("dueDate")));
                resExperimentInfo.setPublishDate(Utility.formatDate((Date) moduleInfo.get("publishDate")));
                resExperimentInfo.setCloudwareTypeId(cloudwareTypeEnum.getCode());
                resExperimentInfo.setCourseName(course.getName());
                resExperimentInfo.setCourseId(course.getId());
                Object exOrderId = moduleInfo.get("exOrderId");
                if (exOrderId == null) {
                    resExperimentInfo.setOrderId(0);
                } else {
                    resExperimentInfo.setOrderId(Integer.parseInt(exOrderId + ""));
                }

                Object isAnswerObj = moduleInfo.get("isAnswer");
                if(isAnswerObj != null){
                    //实验有答案的情况下 判断答案源Cloud表，或者文件位置是否被删除
                    if(Integer.parseInt(isAnswerObj + "")==1){
                        if(resExperimentInfo.getCloudwareType().equals(cloudwareTypeEnum.SHU_LIE_YUN.getZh())){
                            Map<String,Object> map=eduApi.checkCloudTableIsExists(null,course.getName(),(String) moduleInfo.get("answerTableName"),1);
                            if(map!=null && map.size()>0){
                                String result=(String)map.get("isExists");
                                if(result.equals("3")){//表不存在，属性设置为空
                                    Experiment experiment=new Experiment();
                                    experiment.setIsAnswer(0);
                                    experiment.setAnswerTableName("");
                                    experiment.setId(resExperimentInfo.getId());
                                    experimentMapper.updateByPrimaryKeySelective(experiment);
                                    resExperimentInfo.setIsAnswer(0);
                                    resExperimentInfo.setAnswerTableName("");
                                }else{
                                    resExperimentInfo.setIsAnswer(1);
                                    Object answerTableNameObj = moduleInfo.get("answerTableName");
                                    if(answerTableNameObj != null){
                                        resExperimentInfo.setAnswerTableName(answerTableNameObj + "");
                                    }
                                }
                            }
                        }else{
                            Course course1 = courseMapper.selectByPrimaryKey2(course.getId());
                            String folderName = course1.getFolderName();
                            if(folderName == null){
                                course1.setFolderName(System.currentTimeMillis() + "");
                                courseMapper.updateByPrimaryKeySelective(course1);
                            }

                            //判断文件是否存在
                            String filePath = baseDir + imageDir + "/" + course1.getFolderName() + "/" + moduleInfo.get("answerUrl");
                            File file=new File(filePath);
                            if(!file.exists()){
                                Experiment experiment=new Experiment();
                                experiment.setIsAnswer(0);
                                experiment.setAnswerUrl("");
                                experiment.setOldAnswerName("");
                                experiment.setId(resExperimentInfo.getId());
                                experimentMapper.updateByPrimaryKeySelective(experiment);
                                resExperimentInfo.setIsAnswer(0);
                                resExperimentInfo.setAnswerUrl("");
                                resExperimentInfo.setOldAnswerName("");
                            }else{
                                resExperimentInfo.setIsAnswer(1);
                                Object answerUrlObj = moduleInfo.get("answerUrl");
                                if(answerUrlObj != null){
                                    resExperimentInfo.setAnswerUrl(moduleInfo.get("answerUrl") + "");
                                }
                                Object oldAnswerNameObj =  moduleInfo.get("oldAnswerName");
                                if(oldAnswerNameObj != null){
                                    resExperimentInfo.setOldAnswerName(oldAnswerNameObj + "");
                                }
                            }
                        }
                    }
                }
            }
        }
        //按orderId对章节进行排序
        Collections.sort(courseDetail.getModuleList(), new Comparator<ResCourseModuleExperiments.ModuleInfo>() {

            @Override
            public int compare(ResCourseModuleExperiments.ModuleInfo o1, ResCourseModuleExperiments.ModuleInfo o2) {

                Integer o1Id = o1.getOrderId();
                Integer o2Id = o2.getOrderId();
                if (o1Id == null || o2Id == null) {
                    return o1.getModuleId() - o2.getModuleId();
                }
                return o1Id - o2Id;
            }
        });

        //按orderId对实验进行排序
        for (ResCourseModuleExperiments.ModuleInfo moduleInfo : courseDetail.getModuleList()) {

            Collections.sort(moduleInfo.getModuleContent(), new Comparator<ResExperimentInfo>() {
                @Override
                public int compare(ResExperimentInfo o1, ResExperimentInfo o2) {

                    Integer o1Id = o1.getOrderId();
                    Integer o2Id = o2.getOrderId();
                    if (o1Id == null || o2Id == null) {
                        return o1.getId() - o2.getId();
                    }
                    return o1Id - o2Id;
                }
            });
        }


        return courseDetail;
    }
    private ResCourseModuleExperiments getCourseModuleExperiments(Course course) {
        ResCourseModuleExperiments courseDetail = new ResCourseModuleExperiments();

        courseDetail.setCourseName(course.getName());
        courseDetail.setCourseId(course.getId());

        List<ResCourseModuleExperiments.ModuleInfo> moduleList = new ArrayList<>();
        courseDetail.setModuleList(moduleList);
        ResCourseModuleExperiments.ModuleInfo module = new ResCourseModuleExperiments.ModuleInfo();

        for (Map moduleInfo : moduleMapper.selectModuleExperimentInfoByCourseId(course.getId())) {
            int moduleId = (int) moduleInfo.get("moduleId");
            if (module.getModuleId() != moduleId) {
                module = new ResCourseModuleExperiments.ModuleInfo();
                moduleList.add(module);
                module.setModuleContent(new ArrayList<>());
                module.setModuleId(moduleId);
                module.setModuleName((String) moduleInfo.get("moduleName"));
                Object moOrder = moduleInfo.get("moOrderId");
                if (moOrder == null) {
                    module.setOrderId(0);
                } else {
                    module.setOrderId(Integer.parseInt(moOrder + ""));
                }
            }

            if (moduleInfo.get("experimentId") != null) {
                ResExperimentInfo resExperimentInfo = new ResExperimentInfo();
                module.getModuleContent().add(resExperimentInfo);
                resExperimentInfo.setId((int) moduleInfo.get("experimentId"));
                resExperimentInfo.setExperimentName((String) moduleInfo.get("experimentName"));
                resExperimentInfo.setExperimentDes((String) moduleInfo.get("experimentDes"));
                CloudwareTypeEnum cloudwareTypeEnum = CloudwareTypeEnum.fromInt(
                        (int) moduleInfo.get("cloudwareType")
                );
                resExperimentInfo.setExperimentContent(moduleInfo.get("experimentContent") == null ? "" : moduleInfo.get("experimentContent").toString());
                resExperimentInfo.setCloudwareType(cloudwareTypeEnum == null ? "" : cloudwareTypeEnum.getZh());
                resExperimentInfo.setDueDate(Utility.formatDate((Date) moduleInfo.get("dueDate")));
                resExperimentInfo.setPublishDate(Utility.formatDate((Date) moduleInfo.get("publishDate")));
                resExperimentInfo.setCloudwareTypeId(cloudwareTypeEnum.getCode());
                resExperimentInfo.setCourseName(course.getName());
                resExperimentInfo.setCourseId(course.getId());
                Object exOrderId = moduleInfo.get("exOrderId");
                if (exOrderId == null) {
                    resExperimentInfo.setOrderId(0);
                } else {
                    resExperimentInfo.setOrderId(Integer.parseInt(exOrderId + ""));
                }
                Object answerTableNameObj = moduleInfo.get("answerTableName");
                if(answerTableNameObj != null){
                    resExperimentInfo.setAnswerTableName(answerTableNameObj + "");
                }
                Object answerUrlObj = moduleInfo.get("answerUrl");
                if(answerUrlObj != null){
                    resExperimentInfo.setAnswerUrl(moduleInfo.get("answerUrl") + "");
                }
                Object isAnswerObj = moduleInfo.get("isAnswer");
                if(isAnswerObj != null){
                    resExperimentInfo.setIsAnswer(Integer.parseInt(isAnswerObj + "") );
                }

                Object oldAnswerNameObj =  moduleInfo.get("oldAnswerName");
                if(oldAnswerNameObj != null){
                    resExperimentInfo.setOldAnswerName(oldAnswerNameObj + "");
                }

            }
        }


        //按orderId对章节进行排序
        Collections.sort(courseDetail.getModuleList(), new Comparator<ResCourseModuleExperiments.ModuleInfo>() {

            @Override
            public int compare(ResCourseModuleExperiments.ModuleInfo o1, ResCourseModuleExperiments.ModuleInfo o2) {

                Integer o1Id = o1.getOrderId();
                Integer o2Id = o2.getOrderId();
                if (o1Id == null || o2Id == null) {
                    return o1.getModuleId() - o2.getModuleId();
                }
                return o1Id - o2Id;
            }
        });

        //按orderId对实验进行排序
        for (ResCourseModuleExperiments.ModuleInfo moduleInfo : courseDetail.getModuleList()) {

            Collections.sort(moduleInfo.getModuleContent(), new Comparator<ResExperimentInfo>() {
                @Override
                public int compare(ResExperimentInfo o1, ResExperimentInfo o2) {

                    Integer o1Id = o1.getOrderId();
                    Integer o2Id = o2.getOrderId();
                    if (o1Id == null || o2Id == null) {
                        return o1.getId() - o2.getId();
                    }
                    return o1Id - o2Id;
                }
            });
        }


        return courseDetail;
    }

    @Override
    public ResCourseHomeworks getCourseHomeworks(int classID, int studentId) throws ApplicationErrorException {
        if (studentMapper.selectByUserId(studentId) == null) {
            throw new ApplicationErrorException(ErrorCode.StudentNotExists);
        }

        ResCourseHomeworks courseDetail = new ResCourseHomeworks();

        Clazz clazz = classService.getClassById(classID);
        Course courseInfo = this.getCourseById(clazz.getCourseId());
        courseDetail.setCourseName(courseInfo.getName());

        List<ResCourseHomeworks.ModuleInfo> moduleList = new ArrayList<>();
        courseDetail.setModuleList(moduleList);
        ResCourseHomeworks.ModuleInfo moduleInfo = new ResCourseHomeworks.ModuleInfo();

        for (Map moduleHomeworkInfo : homeworkMapper.selectHomeworkDetailByClassAndStudentId(classID, studentId)) {
            int moduleId = (int) moduleHomeworkInfo.get("moduleId");
            if (moduleId != moduleInfo.getModuleId()) {
                moduleInfo = new ResCourseHomeworks.ModuleInfo();
                moduleList.add(moduleInfo);
                moduleInfo.setModuleId(moduleId);
                moduleInfo.setModuleName((String) moduleHomeworkInfo.get("moduleName"));
                moduleInfo.setModuleContent(new ArrayList<>());
            }

            ResCourseHomeworks.HomeworkInfo homeworkInfo = new ResCourseHomeworks.HomeworkInfo();
            moduleInfo.getModuleContent().add(homeworkInfo);
            homeworkInfo.setId((int) moduleHomeworkInfo.get("homeworkId"));
            homeworkInfo.setHomeworkName((String) moduleHomeworkInfo.get("homeworkName"));
            homeworkInfo.setHomeworkDes((String) moduleHomeworkInfo.get("homeworkDes"));
            CloudwareTypeEnum cloudwareType = CloudwareTypeEnum.fromInt((int) moduleHomeworkInfo.get("cloudwareType"));
            homeworkInfo.setCloudwareType(cloudwareType == null ? "" : cloudwareType.toString());
            homeworkInfo.setDueDate(Utility.formatDate((Date) moduleHomeworkInfo.get("dueDate")));
            homeworkInfo.setPublishDate(Utility.formatDate((Date) moduleHomeworkInfo.get("publishDate")));

            boolean isCompleted = moduleHomeworkInfo.get("studentHomeworkId") != null &&
                    moduleHomeworkInfo.get("submissionDate") != null;
            homeworkInfo.setCompleted(isCompleted);
        }

        return courseDetail;
    }

    @Override
    public ResCourseList getAllCourses() throws ApplicationErrorException {
        ResCourseList courseList = new ResCourseList();
        List<ResCourseList.CourseInfo> courseInfoList = new ArrayList<>();
        courseList.setCourseInfoList(courseInfoList);

        for (Map course : courseMapper.getAllCourses()) {
            ResCourseList.CourseInfo courseInfo = new ResCourseList.CourseInfo();
            courseInfoList.add(courseInfo);

            courseInfo.setId((int) course.get("courseId"));
            courseInfo.setCourseName((String) course.get("courseName"));
            courseInfo.setCourseDes((String) course.get("courseDes"));
            courseInfo.setAuthor((String) course.get("author"));
            courseInfo.setPhone((String) course.get("phone"));
            /*courseInfo.setTeacherId((int)course.get("teacherId"));
            courseInfo.setTeacherName((String) course.get("teacherName"));
            courseInfo.setTeacherContact((String) course.get("teacherContact"));*/
        }

        return courseList;
    }

    @Override
    public List<Course> selectAllCourses() {
        return courseMapper.selectAllCourses();
    }

    @Override
    public Course selectByCourseName(String courseName) {
        Course course=courseMapper.selectByName(courseName);;
        return course;
    }

    @Override
    public Map<String, Object> findExperimentCountByCouseId(int courseId) {
        return courseMapper.findExperimentCountByCouseId(courseId);
    }

    @Override
    public List<String> findStuByCourseId(int courseId) {
        return courseMapper.findStuByCourseId(courseId);
    }

    @Override
    public void createCourse(ReqAddCourse reqAddCourse) throws ApplicationErrorException {
        Course course = new Course(reqAddCourse.getName(), reqAddCourse.getDescription());
        course.setFolderName(reqAddCourse.getFolderName());
        course.setAuthor(reqAddCourse.getAuthor());
        course.setPhone(reqAddCourse.getPhone());
        synchronized (this){
            validateCourse(course);
            courseMapper.insert(course);
        }
        Resource resource = new Resource(
                reqAddCourse.getImageName(), reqAddCourse.getImageUrl(), 0, 0);
        resourceMapper.insert(resource);
        CourseResource courseResource = new CourseResource(
                course.getId(), resource.getId(), ResourceTypeEnum.IMAGE.getCode()
        );
        courseResourceMapper.insert(courseResource);
        //为课程创建文件夹
        rancherService.createVolumes(secret, course.getId(), 1);
    }

    @Override
    public void updateCourse(Course course) throws ApplicationErrorException {
        Course oldCourse = courseMapper.selectByPrimaryKey(course.getId());
        if (oldCourse == null) {
            throw new ApplicationErrorException(ErrorCode.CourseNotExists);
        }
        /*int oldTeacherId = oldCourse.getTeacherId();
        int newTeacherId = course.getTeacherId();*/

        validateCourse(course);

        String newCourseName = course.getName();
        courseMapper.updateByPrimaryKey(course);
        if (!newCourseName.equals(oldCourse.getName())) {
            //修改过名字,需要同步数猎云
            eduApi.updateCourse(oldCourse.getName(), newCourseName);
        }
        //同步老师/学生
        /*boolean isSync = courseMapper.selectCourseExistShulieyun(course.getId());
        if(isSync){
            //同步老师
            if(oldTeacherId != newTeacherId){
                //查询该课程对应的班级中 授课老师是否是原来的老师  是的话 不能 解除关系
                List<Clazz> clazzList=classService.selectByCourseId(oldCourse.getId());
                int i=0;
                if(clazzList!=null && clazzList.size()>0){
                    for(Clazz clazz:clazzList){
                        if(clazz.getTeacherId().intValue()==oldTeacherId){
                            i=1;
                            break;
                        }
                    }
                }
                //解除之前老师和数猎场的关系
                if(i==0){
                    Teacher teacher = teacherService.getTeacherByUserId(oldTeacherId);
                    if (teacher != null) {
                        String phone = teacher.getTno();
                        Map<String, Object> map = new HashMap<>();
                        List<String> userList = new ArrayList<>();
                        userList.add(phone);
                        map.put("userList", userList);
                        if(!newCourseName.equals(oldCourse.getName())){
                            map.put("courseName",newCourseName);
                        }else{
                            map.put("courseName",oldCourse.getName());
                        }
                        eduApi.delUser(map);
                    }
                }
                //调用接口，增加新的老师
                User user  = userMapper.selectByPrimaryKey(newTeacherId);
                if(user!=null) {
                    Map<String, Object> addUserMap = new HashMap<>();
                    List<Map<String, Object>> addUserList = new ArrayList<>();
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("phone", user.getUsername());
                    userMap.put("password",user.getPassword2());
                    addUserList.add(userMap);
                    addUserMap.put("coursename",newCourseName);
                    addUserMap.put("userList",addUserList);
                    eduApi.addUser(addUserMap);
                }
            }
        }*/
        if (course.getImageUrl() == null || course.getImageUrl().isEmpty()) {
            return;
        }

        CourseResource courseResource = courseResourceMapper.selectByCourseIdAndType(course.getId(), ResourceTypeEnum.IMAGE.getCode());
        if (courseResource == null) {
            Resource resource = new Resource();
            resource.setUrl(course.getImageUrl());
            resourceMapper.insert(resource);
            courseResource = new CourseResource();
            courseResource.setCourseId(course.getId());
            courseResource.setResourceId(resource.getId());
            courseResourceMapper.insert(courseResource);
        } else {
            Resource resource = resourceMapper.selectByPrimaryKey(courseResource.getResourceId());
            resource.setUrl(course.getImageUrl());
            resourceMapper.updateByPrimaryKey(resource);
        }
    }

    @Override
    public void updateCourseWithOutSync(Course course) throws ApplicationErrorException {
        courseMapper.updateByPrimaryKey(course);
    }

    @Override
    public void deleteCourse(ReqDeleteCourse reqDeleteCourse) throws ApplicationErrorException {
        int courseId = reqDeleteCourse.getId();

        Course course = courseMapper.selectByPrimaryKey2(courseId);
        if (course == null) {
            throw new ApplicationErrorException(ErrorCode.CourseNotExists);
        }

//        if (moduleMapper.isCourseUsedByModule(courseId)) {
//            throw new ApplicationErrorException(ErrorCode.CourseIsUsedByModule);
//        }

        //与班级关联的课程不能被删除
        if (clazzMapper.isCourseUsedByClass(courseId)) {
            throw new ApplicationErrorException(ErrorCode.CourseIsUsedByClass);
        }

        for (Module module : moduleMapper.selectByCourseID(courseId)) {
            moduleService.deleteModule(new ReqDeleteModule(module.getId()));
        }

        for (Clazz clazz : clazzMapper.selectByCourseId(courseId)) {
            classService.deleteClass(new ReqDeleteClass(clazz.getId()));
        }

        CourseResource courseResource =
                courseResourceMapper.selectByCourseIdAndType(courseId, ResourceTypeEnum.IMAGE.getCode());
        if (courseResource != null) {
            courseResourceMapper.deleteByPrimaryKey(courseResource.getId());
            resourceMapper.deleteByPrimaryKey(courseResource.getResourceId());
        }

        try{
            //删除课程文件夹
            log.info("删除课程文件路径为：" + courseFolderPath + course.getId());
            FileUtils.deleteDirectory(new File(courseFolderPath + course.getId()));
            //CompressAndDecompressionUtils.deleteFile(courseFolderPath + course.getId());
            //删除图片文件夹
            String path = course.getFolderName();
            if(path != null){
                String targetDir = baseDir + imageDir + "/" + path;
                log.info("删除课程图片文件夹路径为：" + targetDir);
                FileUtils.deleteDirectory(new File(targetDir));
                //CompressAndDecompressionUtils.deleteFile(targetDir);
            }
        }catch (Exception e){
            log.error("删除课程共享文件夹或图片文件夹错误！",e);
        }

        courseMapper.deleteByPrimaryKey(courseId);
    }

    @Override
    public ResHotCourseList getHotCourses() {
        ResHotCourseList resHotCourseList = new ResHotCourseList();
        resHotCourseList.setCourseList(courseMapper.getHotCourses());
        return resHotCourseList;
    }

    @Override
    public ResCommonCourseDetail getCommonCourseDetail(int courseId) throws ApplicationErrorException {
        Course course = this.getCourseById(courseId);

        //Teacher teacher = teacherMapper.selectByUserId(course.getTeacherId());
        ResTeacherInfo teacherInfo = new ResTeacherInfo();
        teacherInfo.setPhone(course.getPhone());
        teacherInfo.setTeacherName(course.getAuthor());
        int classNum = clazzMapper.getClassNumByCourseId(courseId);
        int studentNum = studentClassMapper.getStudentNumByCourseId(courseId);

        ResCommonCourseDetail detail = new ResCommonCourseDetail(teacherInfo, classNum, studentNum, course.getDescription());

        return detail;
    }

    @Override
    public int getCount() {
        return courseMapper.getCount();
    }

    private void validateCourse(Course course) throws ApplicationErrorException {

        //判断课程是否已经存在
        Integer count = courseMapper.selectCountByCourseName(course);
        if (count > 0) {
            throw new ApplicationErrorException(ErrorCode.CourseAlreadyExisted);
        }

        /*Teacher teacher = teacherMapper.selectByUserId(course.getTeacherId());
        if(teacher == null){
            throw new ApplicationErrorException(ErrorCode.TeacherNotExists);
        }*/
    }

    private String getImageUrl(int courseID) {
        CourseResource courseResource =
                courseResourceMapper.selectByCourseIdAndType(courseID, ResourceTypeEnum.IMAGE.getCode());

        if (courseResource == null) {
            return "";
        }

        return resourceMapper.selectByPrimaryKey(courseResource.getResourceId()).getUrl();
    }

    @Override
    public List<Course> getCourseByClassId(int classId) {
        return courseMapper.getCourseByClassId(classId);
    }

    @Override
    public boolean isTeacherUsedByCourse(int teacherId) {
        return false;
        //return courseMapper.isTeacherUsedByCourse(teacherId);
    }

    @Override
    public boolean selectCourseExistShulieyun(int courseId) {
        return courseMapper.selectCourseExistShulieyun(courseId);
    }

    @Override
    public boolean selectCourseExistOtherExperiment(int courseId) {
        return courseMapper.selectCourseExistOtherExperiment(courseId);
    }

    @Override
    public boolean selectCourseExistOtherExperimentByUserId(int userId) {
        List<Integer> existsList = courseMapper.selectCourseExistOtherExperimentByUserId(userId);
        if (existsList != null && existsList.size() > 0) {
            for (Integer exists : existsList) {
                if (exists == 1) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Integer selectCountByCourseName(Course course) {
        return courseMapper.selectCountByCourseName(course);
    }

    @Override
    public List<Course> selectByCourseName(Course course) {
        return courseMapper.selectByCourseName(course);
    }

    @Override
    public List<String> findTeaByCourseId(int courseId) {
        return courseMapper.findTeaByCourseId(courseId);
    }

    @Override
    public Map<String, Object> findModelCountByCouseId(int courseId) {
        return courseMapper.findModelCountByCouseId(courseId);
    }

    @Override
    public List<Course> findShulieyunCourseById(int courseId) {
        return courseMapper.findShulieyunCourseById(courseId);
    }

    @Override
    public Course selectCourseByCourseId(Integer courseId) {
        return courseMapper.selectByPrimaryKey(courseId);
    }

    @Override
    public List<Course> findCourseByStuId(Map<String, Object> map) {
        return courseMapper.findCourseByStuId(map);
    }

    @Override
    public Course selectCourseByCourseName(String courseName) {
        return courseMapper.selectCourseByCourseName(courseName);
    }

    @Override
    public List<Map<String, List<Map<String,Object>>>> getCouExpByTeaIdAndCId(int teacherId, int classId) {
        List<Map<String, List<Map<String,Object>>>>  mapList=new ArrayList<>();
        //查询该老师在该班级对应的课程编号
        Map<String,Object> map=new HashMap<>();
        map.put("classId",classId);
        map.put("teacherId",teacherId);
        List<Integer> studentList=studentClassMapper.getAllStudentIdsByClassId(classId);
        List<Course> courseIdList=classCourseMapper.getCourseByTeacherAndClassId(map);
        if(courseIdList!=null && courseIdList.size()>0){
            //根据课程查询 模型实验
            for(Course course:courseIdList){
                Map<String,Object> stuMap=new HashMap<>();
                stuMap.put("courseId",course.getId());
                stuMap.put("classId",classId);
                List<Map> experimentMap=new ArrayList<>();
                if(studentList!=null && studentList.size()>0){
                    stuMap.put("idList",studentList);
                    //查询这个课程 所有实验作业。并且学生在当前班级，有人提交作业得实验
                   experimentMap=experimentMapper.selectModuleAndExplerimentByStu(stuMap);
                }else{
                    experimentMap=experimentMapper.selectModuleAndExpleriment(stuMap);
                }

                Map<String,List<Map<String,Object>>> experMap=new HashMap<>();
                if(experimentMap!=null && experimentMap.size()>0){
                    List<Map<String,Object>> list=new ArrayList<>();
                    for(Map map1:experimentMap){
                        Map<String,Object> map2=new HashMap<>();
                        map2.put("moduleName",(String)map1.get("moduleName"));
                        map2.put("experimentName",(String)map1.get("experimentName"));
                        map2.put("experimentId",Integer.parseInt(map1.get("experimentId").toString()));
                        list.add(map2);
                    }
                    experMap.put(course.getName(),list);
                }else{
                    experMap.put(course.getName(),new ArrayList<>());
                }
                mapList.add(experMap);
            }
        }
        return mapList;
    }
}
