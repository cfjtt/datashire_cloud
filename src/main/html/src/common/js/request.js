/* eslint-disable */
import axios from 'axios';

axios.defaults.baseURL = '/';
axios.defaults.headers = {
  withCredentials: true,
  Authorization:'Bearer '+(localStorage['token'] ? localStorage['token'] : '')
};
var shu_lie_yun_url="http://192.168.137.176:8080";
var edu_url = "http://192.168.137.181:8080";
var rancherHost="192.168.137.212";
var rancherPort="8001";
var fileManagementUrl="http://192.168.137.215:8090";
var adminAndTeacherUsername="manager";
var adminAndTeacgerPassword="eurlanda";

//四个链接接口
// function shu_lie_yun_url() {
//   var params=new URLSearchParams();
//   params.append("parameter","shulie.url");
//   return axios.post('/admin/getPropertiesValue',params);
// }

// function edu_url() {
//   var params=new URLSearchParams();
//   params.append("parameter","edu_url");
//   return axios.post('/admin/getPropertiesValue',params);
// }

// function rancherHost() {
//   var params=new URLSearchParams();
//   params.append("parameter","rancherHost");
//   return axios.post('/admin/getPropertiesValue',params);
//   // var params = {
//   //   rancherHost: "rancherHost"
//   // }
//   // return axios.post('/admin/getPropertiesValue',params);
// }

// function fileManagementUrl() {
//   var params=new URLSearchParams();
//   params.append("parameter","fileManagementUrl");
//   return axios.post('/admin/getPropertiesValue',params);
//   // var params = {
//   //   fileManagementUrl: "fileManagementUrl"
//   // }
//   // return axios.post('/admin/getPropertiesValue',params);
// }

// 登录
function login(username,password,isOffline) {
  var params = {
    username:username,
    password:password,
    isOffline:isOffline
  };
  var obj = axios.post('/auth/login',params);
  return obj;
}
//重新设置token
function setToken(){
  axios.defaults.headers={
    withCredentials: true,
    Authorization:'Bearer '+(localStorage['token'] ? localStorage['token'] : '')
  }
}
// 修改密码
function modifyPass(userId,oldPassword,newPassword,confirmPassword){
  var params = {
    userId: userId,
    oldPassword: oldPassword,
    newPassword: newPassword,
    confirmPassword: confirmPassword
  };
  return axios.post('/auth/updatePassword',params);
}
//重置密码
function resetPWD(userId){
  var params = {
    userId:userId
  };
  return axios.post("/admin/password/resetion",params);
}
//获取作业列表(登录成功之后，回调中使用)
function getNotifications(role,userId){
  if (role == 1) {
    //学生作业列表
    return axios.get("/student/course/homework/all/"+userId);
  } else if(role==2){
    //教师作业列表
    return axios.get("/teacher/course/homework/all/"+userId);
  }
}
//获取热门课程(不登录)
function getHotCourse(){
  return axios.get("/common/hotCourses/all");
}
//获取网站统计信息,包含学生，课程，实验，老师数量
function getSatistics(){
  return axios.get("/common/statistics/");
}
// 获取教师数据
function getCustomers(){
    return axios.get('/admin/teacher/all');
}
// 分页获取老师数据
function getCustomersByPage(currentPage,limit){
  var params = {
    currentPage:currentPage,
    limit:limit
  };
  return axios.post("/admin/teacher/byLimit",params);
}
// 添加教师数据
function addTeacher(data) {
  return axios.post('/admin/teacher/creation', data);
}
// 删除教师数据
function deleteTeacher(teacherid) {
  var params = {
    teacherId:teacherid
  };
  return axios.post('/admin/teacher/deletion',params);
}
// 编辑教师数据
function updateTeacher(gender,teacherContact,teacherName,teacherNo,teacherTitleId,id) {
  var params = {
    gender:gender,
    id:id,
    teacherContact:teacherContact,
    teacherName:teacherName,
    teacherNo:teacherNo,
    teacherTitleId:teacherTitleId
  };
  return axios.post('/admin/teacher/updation',params);
}

// 获取学期集合
function getXueQiListData() {
  return axios.get('/admin/semester/all')
}
// 删除学期数据
function deleteXueQi(semesterId) {
  var params = {
    semesterId:semesterId
  };
  return axios.post('/admin/semester/deletion',params);
}
// 修改学期数据
function updateTearm(params) {
  return axios.post('/admin/semester/updation',params);
}
//根据编号查询学期
function getTearmById(tearmId) {
  var params = new URLSearchParams();
  params.append("tearmId",tearmId);
  return axios.post('admin/semester/findTearmById',params)
}

//删除班级里学生
function deleteStudentByClass(classId,userId) {
  var params = new URLSearchParams();
  params.append("classId",classId);
  params.append("userId",userId);
  return axios.post('admin/deleteStudentByClass',params)
}

function get(url, params) {
  return axios.get(url, params)
}

function post(url, params) {
  return axios.post(url, params)
}

// 获取学生的实验信息
function getExpirementInfo(experimentId,studentId){
  var url = "/student/experiment/"+experimentId+"/"+studentId+"/cloudware";
  return axios.get(url);
}
// 创建学生实验
function createExCloudWare(experimentId,pulsarId,serviceId,serviceName,studentId,webSocket) {
  var params = {
    experimentId:experimentId,
    pulsarId:pulsarId,
    serviceId:serviceId,
    serviceName:serviceName,
    studentId:studentId,
    webSocket:webSocket
  };
  return axios.post("/student/experiment/creation",params);
}


//删除实验
function deleteExCloudware(studentId,experimentId){
  var params={
    studentId:studentId,
    experimentId:experimentId
  };
  return axios.post("/student/experiment/delete",params);
}
//获取学生最近实验
function getLastExperiment(studentId) {
  var url = "/student/experiment/last/"+studentId;
  return axios.get(url);
}
// 获取学生实验细信息
function getStudentExperimentCloudware(experimentId,studentId){
  var url = "/student/experiment/"+experimentId+"/"+studentId+"/cloudware";
  return axios.get(url);
}
//获取实验的websocket地址链接
function getWebSocketUrl(secret,cloudware_type,user_id,experimentId){
  var params = new URLSearchParams();
  params.append("secret",secret);
  params.append("cloudware_type",cloudware_type);
  params.append("user_id",user_id);
  params.append("experimentId",experimentId);
  return axios.post("/services",params);
}
// 获取课程的详细信息
function getCourseDetail(courseId){
  var url="/common/course/"+courseId+"/detail";
  return axios.get(url);
}
//获取课程实验相关信息
function getCourseExperimentDetail(courseId){
  var url = "/common/course/"+courseId+"/experiments";
  return axios.get(url);
}
// 获取实验相关信息
function getExperimentById(id){
  var params = new URLSearchParams();
  params.append("id",id);
  return axios.post("/admin/course/module/experiment/getExperiment",params);
}
//获取学生所学课程
function getStudentCourse(studentId){
  var url="/student/course/all/"+studentId;
  return axios.get(url);
}

//教师课程
function getTeacherCourse(teacherId){
  var url="/teacher/"+teacherId+"/course/all";
  return axios.get(url);
}

//获取所有与该老师关联的课程
function getTeacherAllAssociateCourse(teacherId){
  var url="/teacher/"+teacherId+"/course/associate";
  return axios.get(url);
}


// 获取课程数据
function getKecheng() {
  return axios.get('/admin/course/all')
}

//获取所有课程
function getAllCourses(){
  return axios.get("/admin/course/allWithOutCondition");
}

// 删除课程
function deletekecheng(id) {
  var params = {
    id:id
  };
  return axios.post('/admin/course/deletion',params);
}
// 新增课程
function addkecheng(data) {
  return axios.post('/admin/course/creation', data);
}
// 更新选课
function updateKecheng(params){
  return axios.post("/admin/course/updation",params);
}
// 获取班级数据
function getbanji() {
  return axios.get('/admin/class/all')
}
// 新增班级选课
function addBanKe(params) {
  return axios.post('/admin/class/creation', params);
}
// 更新班级
function updateBanke(params){
  return axios.post("/admin/class/updation",params);
}
// 删除班级
function deleteBancheng(classId) {
  var params = {
    classId:classId
  };
  return axios.post('/admin/class/deletion',params);
}
// 获取班级学生信息
function getStudentInfo(classId){
  return axios.get('/admin/class/'+classId+'/students/all');
}
//获取不在当前班级的所有学生
function getStudentByNotClass(classId) {
  var params = new URLSearchParams();
  params.append("classId",classId);
  return axios.post('/admin/getStudentByNotClass',params);
}

// 获取所有学生信息
function getAllStudentInfo(){
  return axios.get('/admin/students/all');
}
//添加模块
function addModule(name,courseId){
  var params = {
    name:name,
    courseId:courseId
  };
  return axios.post("/admin/course/module/creation",params);
}
// 删除模块
function deleteModule(moduleId,courseId){
  var params = {
    moduleId:moduleId,
    courseId:courseId
  };
  return axios.post("/admin/course/module/deletion",params);
}
// 查找该模块是否提交存在
function findModuleIsExist(courseId,moduleName){
  var params = {
    courseId:courseId,
    name:moduleName
  };
  return axios.post("/admin/course/module/moduleIsExist",params)
}
//删除学生
function deleteStudent(classId,studentId,courseId){
  var params = {
    classId:classId,
    studentId:studentId,
    courseId:courseId
  };
  return axios.post("/admin/class/student/deletion",params);
}
//单独添加学生
function addStudent(params){
  return axios.post("/admin/class/student/creation",params);
}

//批量添加学生
function addStudentList(params) {
  return axios.post("/admin/class/addStudentList",params);
}
//批量删除学生
function deleteStudentList(params) {
  return axios.post("/admin/class/deleteStudentList",params);
}
//添加学生
function createStudent(params){
  return axios.post("/admin/student/creation",params);
}
//修改学生
function updateStudent(params) {
  return axios.post("/admin/student/update",params);
}
//删除学生
function delStudent(studentNo) {
  var params = new URLSearchParams();
  params.append("studentNo",studentNo);
  return axios.post("/admin/student/delete",params);
}

//添加实验
function addExperiment(params){
  return axios.post("/admin/course/module/experiment/creation",params);
}
//删除实验
function deleteExperimentById(id,courseId){
  var params={
    id:id,
    courseId:courseId
  };
  return axios.post("/admin/course/experiment/deletion",params);
}
//更新实验
function updateExperiment(params){
  return axios.post("/admin/course/module/experiment/updation",params);
}
function experimentIsExist(moduleId,name){
  var params = {
    moduleId:moduleId,
    name:name
  };
  return axios.post("/admin/course/module/experiment/experimentExist",params);
}
// 获取数据库列表信息
function getDataShireDBList(phone,isAdmin,courseName,type,paraSpaceName){
  var params = new URLSearchParams();
  params.append("phone",phone);
  params.append("isAdmin",isAdmin);
  params.append("courseName",courseName);
  params.append("type",type);
  if(paraSpaceName != null){
    params.append("paraSpaceName",paraSpaceName);
  }
  return axios.post("/admin/getDataShireDBList",params);
}
// 获取文件列表
function getDataShireFileList(phone,path,isAdmin,courseId,type,paraSpaceName){
  var params = new URLSearchParams();
  params.append("phone",phone);
  params.append("path",path);
  params.append("isAdmin",isAdmin);
  params.append("courseName",courseId);
  params.append("type",type);
  if(paraSpaceName != null){
    params.append("paraSpaceName",paraSpaceName);
  }
  return axios.post("/admin/getDataShireFileList",params);
}

// 勾选/取消 可视化
function operateTable(spaceName,tableName,operate,phone){
  var params = new URLSearchParams();
  params.append("spaceName",spaceName);
  params.append("tableName",tableName);
  params.append("operate",operate);
  params.append("phone",phone);
  return axios.post("/admin/operateTable",params);
}
// 删除表
function deleteTable(spaceName,tableName,phone,courseName,isAdmin,type){
  var params = new URLSearchParams();
  params.append("spaceName",spaceName);
  params.append("tableName",tableName);
  params.append("phone",phone);
  params.append("courseName",courseName);
  params.append("isAdmin",isAdmin);
  params.append("type",type);
  return axios.post("/admin/deleteTable",params);
}
//删除文件
function deleteFile(spaceName,fileName,phone,path,isAdmin,courseName,type){
  var params = new URLSearchParams();
  params.append("spaceName",spaceName);
  params.append("fileName",fileName);
  params.append("phone",phone);
  params.append("path",path);
  params.append("isAdmin",isAdmin);
  params.append("courseName",courseName);
  params.append("type",type);
  return axios.post("/admin/deleteCloudHDFSFile",params);
}
//获取服务器时间
function getServerTime() {
  return axios.post("/admin/getServerTime");
}

//获取共享文件夹中所有信息
function getShareFileList(userId,path,type) {
  var params=new URLSearchParams();
  params.append("userId",userId);
  params.append("path",path);
  params.append("type",type);
  return axios.post("/share/getShareFileList",params);
}

//删除共享文件中文件
function deleteShareFile(userId,fileName,path,type) {
  var params=new URLSearchParams();
  params.append("userId",userId);
  params.append("fileName",fileName);
  params.append("path",path);
  params.append("type",type);
  return axios.post("/share/deleteShareFile",params);
}

//下载共享文件夹中文件
function downloadShareFile(userId,fileName,path,type) {
  var params=new URLSearchParams();
  params.append("userId",userId);
  params.append("fileName",fileName);
  params.append("path",path);
  params.append("type",type);
  return axios.post("/share/downloadShareFile",params);
}
//创建共享文件夹
function createShareDirectory(userId,directoryName,path,type) {
  var params=new URLSearchParams();
  params.append("userId",userId);
  params.append("directoryName",directoryName);
  params.append("path",path);
  params.append("type",type);
  return axios.post("/share/createShareDirectory",params);
}
//共享文件夹重命名
function renameShareFile(userId,oldFileName,newFileName,path,type) {
  var params=new URLSearchParams();
  params.append("userId",userId);
  params.append("oldFileName",oldFileName);
  params.append("newFileName",newFileName);
  params.append("path",path);
  params.append("type",type);
  return axios.post("/share/renameShareFile",params);
}

//移动共享文件夹
function  moveShareFile(userId,targetPath,fileNameList,isCheckExists,type) {
  var params=new URLSearchParams();
  params.append("userId",userId);
  params.append("targetPath",targetPath);
  params.append("fileNameList",fileNameList);
  params.append("isCheckExists",isCheckExists);
  params.append("type",type);
  return axios.post("/share/moveShareFile",params);
}

//查询共享文件是否存在
function  checkIsExistsShare(userId,path,type) {
  var params=new URLSearchParams();
  params.append("userId",userId);
  params.append("path",path);
  params.append("type",type);
  return axios.post("/share/checkIsExistsShare",params);
}

//根据课程名字查询课程相关信息
function getCourseById(courseId){
  var params = new URLSearchParams();
  params.append("courseId",courseId);
  return axios.post("/admin/getCourseById",params);
}
//根据课程名字查询课程相关信息
function getCourseByCourseName(courseName){
  var params = new URLSearchParams();
  params.append("courseName",courseName);
  return axios.post("/admin/getCourseByCourseName",params);
}

function selectCourseExistOtherExperimentByUserId(userId) {
  var params = new URLSearchParams();
  params.append("userId",userId);
  return axios.post("/student/selectCourseExistOtherExperimentByUserId",params);
}


function recordUserOnlineData(userId,token){
  var params = {
    userId: userId,
    token: token
  };

  return axios.post("/record/login",params);
}
/**
 *
 * @param userId 用户id
 * @param type 1.正常下线，2.重复登录下线
 * @returns {*}
 */
function recordUserOfflineData(userId,type){
  return axios.get("/record/" + userId + "/logout/" + type);
}

function getClouderaData(query,from,to){
  var params ={
    query:query,
    from :from,
    to : to
  };
  return axios.post("/record/getClouderaData",params);
}


function getRealTimeOnlineData(){
  return axios.post("/record/getRealTimeOnlineData");
}


function getAccumulativeOnlineData(){
  return axios.post("/record/getAccumulativeOnlineData");
}



function getDayAndWeekAndMonthOnlineAvgData(){
  return axios.post("/record/getDayAndWeekAndMonthOnlineAvgData");
}


function getCharacterCloudDashboardData(){
  return axios.post("/record/getCharacterCloudDashboardData");
}


function getRecentlyLoginUserData(pageSize){
  var params ={
    pageSize:pageSize
  };

  return axios.post("/record/getRecentlyLoginUserData",params);
}
function getSystemTime(){
  return axios.post("/record/getSystemTime");
}

//获取班级里所有课程
function getCourseByClassId(classId) {
  var params = new URLSearchParams();
  params.append("classId",classId);
  return axios.post("/admin/getCourseByClassId",params);
}
//批量添加班级课程
function addClassCourseList(params) {
  return axios.post("/admin/class/addClassCourseList",params);
}
//批量删除班级课程
function deleteClassCourseList(params) {
  return axios.post("/admin/class/delClassCourseList",params);
}

//调整章节顺序
/**
 *  orderOne;//章节1的orderId
    moduleIdOne;//章节1的id
    moduleIdTwo;
    orderTwo;
    courseId;//课程id
 * @param params
 * @returns {*}
 */

// function adjustModuleOrder(params){
//
//   return axios.post("/admin/module/adjustModuleOrder",params);
// }
function adjustModuleOrder(orderOne,moduleIdOne,moduleIdTwo,orderTwo,courseId){
  var params={
    orderOne:orderOne,
    moduleIdOne:moduleIdOne,
    moduleIdTwo:moduleIdTwo,
    orderTwo: orderTwo,
    courseId: courseId
  };
  return axios.post("/admin/module/adjustModuleOrder",params);
}


//调整实验顺序
/**
 *
 *  experimentOne;//第一个实验id
    orderOne;//第一个orderId
    experimentTwo;//第二个实验id
    orderTwo;//第二个orderid
    moduleId;//模块id
    courseId;//课程id
 * @param params
 * @returns {*}
 */
// function adjustExperimentOrder(params){
//
//
//   return axios.post("/admin/experiment/adjustExperimentOrder",params);
// }
function adjustExperimentOrder(experimentOne,orderOne,experimentTwo,orderTwo,moduleId){
  var params={
    experimentOne:experimentOne,
    orderOne:orderOne,
    experimentTwo:experimentTwo,
    orderTwo: orderTwo,
    moduleId: moduleId
  };
  return axios.post("/admin/experiment/adjustExperimentOrder",params);
}
/**
 * 编辑章节
 *  id;//章节id
    courseId;//章节所属课程id
    name;//修改后的name
 * @param params
 * @returns {*}
 */
// 编辑模块
// function updateModule(params){
//
//   return axios.post("/admin/course/module/update",params);
// }
function updateModule(id,courseId,name){
  var params={
    id:id,
    courseId:courseId,
    name:name
  };
  return axios.post("/admin/course/module/update",params);
}

// function deleteExCloudware(studentId,experimentId){
//   var params={
//     studentId:studentId,
//     experimentId:experimentId
//   };
//   return axios.post("/student/experiment/delete",params);
// }

//查询module
function getModuleInfo(id){
  var params={
    id:id
  };
  return axios.post("/admin/course/module/getModule",params);
}
//获取实验答案
function getExperimentAnswer(courseId,experimentId,type,courseName) {
  var params = new URLSearchParams();
  params.append("courseId",courseId);
  params.append("experimentId",experimentId);
  params.append("type",type);
  params.append("courseName",courseName);
  return axios.post("/admin/experiment/getAnswerFile",params);
}
function deleteExperimentAnswer(courseId,experimentId,type,tableName) {
  var params = new URLSearchParams();
  params.append("courseId",courseId);
  params.append("experimentId",experimentId);
  params.append("type",type);
  params.append("tableName",tableName);
  return axios.post("/admin/experiment/deleteAnswer",params);

}
function submitExperimentAnswer(courseId,experimentId,type,tableName,courseName,file) {
  var params = new URLSearchParams();
  params.append("courseId",courseId);
  params.append("experimentId",experimentId);
  params.append("type",type);
  params.append("tableName",tableName);
  params.append("courseName",courseName);
  params.append("file",file);
  return axios.post("/admin/experiment/submitAnswer",params);

}

/**
 * 获取系统的key
 * key
 * @returns {*}
 */

function getSysKey(){
  return axios.get("/admin/getKey");
}
/**
 * 如果没有导入license则返回错误码，否则返回限制信息
 * 返回值：
 * maxOnlineNum 最大在线人数
 * startTime 开始时间
 * endTime 结束时间（如果不限时间，则没有endtime）
 * @returns {*}
 */
function isImportLicense(){
  return axios.get("/admin/isImportLicense");
}
/**
 * 导入或更新license
 * @param license license字符串
 * @param type 操作类型 1. 导入 2.更新
 * @returns {*}
 */
function importOrUpdateLicense(license,type) {
  var params={
    license:license,
    type:type
  };
  return axios.post("/admin/importOrUpdateLicense",params);
}

function findClassByTeacherId(teacherId) {
 var url="/teacher/class/all/"+teacherId;
  return axios.get(url);
}

function getCouExpByTeaIdAndCId(teacherId,classId) {
  var params = new URLSearchParams();
  params.append("teacherId",teacherId);
  params.append("classId",classId);
  return axios.post("/teacher/class/course",params);
}

function findStudentScore(classId,courseId,experimentId) {
  var params = new URLSearchParams();
  params.append("classId",classId);
  params.append("courseId",courseId);
  params.append("experimentId",experimentId);
  return axios.post("/teacher/class/score",params);
}

function findCourseByName(courseName) {
  var params = new URLSearchParams();
  params.append("courseName",courseName);
  return axios.post("/teacher/course/courseInfo",params);
}


/**
 * 学生提交数猎作业
 * @param
 * @returns {*}
 */
function submitHomeworkOfShuLie(courseId,experimentId,userId,tableName,className){
  var params={
    courseId:courseId,
    experimentId:experimentId,
    userId:userId,
    tableName:tableName,
    className:className
  };
  return axios.post("/student/submitHomeworkOfShuLie",params);
}

/**
 * 学生提交非数猎云实验
 * @param experimentId
 * @param userId
 * @param tableName
 * @returns {*}
 */
function submitHomeworkOfOther(courseId,experimentId,userId,homeworkPath,className){
  var params={
    courseId:courseId,
    experimentId:experimentId,//实验id
    userId:userId,//学生id
    homeworkPath:homeworkPath,//学生作业路径
    className:className

  };
  return axios.post("/student/submitHomeworkOfOther",params);
}
function getStudentSubmitHomework(className,courseId,experimentId,userId,type) {
  var params = new URLSearchParams();
  params.append("className",className);
  params.append("courseId",courseId);
  params.append("experimentId",experimentId);
  params.append("userId",userId);
  params.append("type",type);
  return axios.post("/student/getStudentSubmitHomework",params);
}


//查询实验是否有答案
function getCourseIsAnswer(experimentId) {
  var params = new URLSearchParams();
  params.append("experimentId",experimentId);
  return axios.post("/admin/course/isAnswer",params);
}
//查询学生是否提交答案
function getStudentIsSubAnswer(className,courseId,experimentId,userId) {
  var params = new URLSearchParams();
  params.append("className",className);
  params.append("courseId",courseId);
  params.append("experimentId",experimentId);
  params.append("userId",userId);
  return axios.post("/student/getStudentIsSubAnswer",params);
}
function getJobShareFileList(userId,path,type) {
  var params = new URLSearchParams();
  params.append("userId",userId);
  params.append("path",path);
  params.append("type",type);
  return axios.post("/student/getJobShareFile",params);
}
function getCourseExperimentDetailAndAnswer(courseId,userId,classId) {
  var url = "/common/course/"+classId+"/"+courseId+"/"+userId+"/getExperimentAndAnswer";
  return axios.get(url);
}
function findClassByName(className) {
  var params = new URLSearchParams();
  params.append("className",className);
  return axios.post("/student/findClassName",params);
}
function logoutCheck(userId,token){
  var params = {
    userId: userId,
    token: token
  };
  return axios.post("/record/logoutCheck",params);
}

// 行业数据
function industry(key,isTop) {
  var params = new URLSearchParams();
  params.append("key",key);
  params.append("isTop",isTop);
  return axios.post("/dmdata/industry/all",params);
}

// 获取行业数量，数据数量，数据总量，下载次数
function getIndustryCountDmCountDownCount(){
  return axios.get("/dmdata/getMsg");
}

function getDmData(industryId,page,offset,queryStr){
  var params = new URLSearchParams();
  if(industryId != null) {
    params.append("industryId", industryId);
  }
  params.append("page",page);
  params.append("offset",offset);
  if(queryStr!=null && queryStr != '') {
    params.append("dmName", queryStr);
  }
  return axios.post("/dmdata/dm/getDmData",params);
}

//获取服务器加密时间
function getEncryptTime(userId){
  return axios.get("/dmdata/getencrypt?userId="+userId);
}
//根据项目 获取所有文件夹
function findFolderByProjectId(projectId) {
  var params = new URLSearchParams();
  params.append("projectId",projectId);
  return axios.post("/folder/findFolderByProjectId",params);
}
//获取文档信息
function findFolderDoc(projectId,folderId,page,offset,keywordStr) {
  var params = new URLSearchParams();
  params.append("projectId",projectId);
  params.append("folderId",folderId);
  params.append("page",page);
  params.append("offset",offset);
  params.append("keywordStr",keywordStr);
  return axios.post("/doc/findDoc",params);
}
//添加文件夹
function addProjectFolder(projectId,folderName,userId) {
  var params = new URLSearchParams();
  params.append("projectId",projectId);
  params.append("folderName",folderName);
  params.append("userId",userId);
  return axios.post("/folder/addProjectFolder",params);
}
//编辑文件夹
function updateProjectFolder(projectId,oldName,newName,userId) {
  var params = new URLSearchParams();
  params.append("projectId",projectId);
  params.append("oldName",oldName);
  params.append("newName",newName);
  params.append("userId",userId);
  return axios.post("/folder/updateProjectFolder",params);
}
//删除文件夹
function deleteProjectFolder(projectId,folderId,userId) {
  var params = new URLSearchParams();
  params.append("projectId",projectId);
  params.append("folderId",folderId);
  params.append("userId",userId);
  return axios.post("/folder/deleteProjectFolder",params);
}
//创建文档
function addFolderDoc(projectId,folderId,docName,userId) {
  var params = new URLSearchParams();
  params.append("projectId",projectId);
  params.append("folderId",folderId);
  params.append("docName",docName);
  params.append("userId",userId);
  return axios.post("/doc/addProjectDoc",params);
}
//点击编辑文档
function updateProjectDoc(docId) {
  var params = new URLSearchParams();
  params.append("docId",docId);
  return axios.post("/doc/updateProjectDoc",params);
}
//保存编辑文档
function saveEditorProjectDoc(id,content,editor,docName,projectId,updateTime) {
  var params = {
    id: id,
    content:content,
    editor:editor,
    docName:docName,
    projectId:projectId,
    updateTime:updateTime
  };
  return axios.post("/doc/saveEditorProjectDoc",params);
}
//删除文档
function deleteProjectDoc(projectId,docId,userId) {
  var params = new URLSearchParams();
  params.append("projectId",projectId);
  params.append("docId",docId);
  params.append("userId",userId);
  return axios.post("/doc/deleteProjectDoc",params);
}

//删除附件
function deleteDocFile(projectId,docId,userId,docAnnexName) {
  var params = new URLSearchParams();
  params.append("projectId",projectId);
  params.append("docId",docId);
  params.append("userId",userId);
  params.append("docAnnexName",docAnnexName);
  return axios.post("/doc/deleteDocFile",params);
}

//获取我的项目
function getMyProject(page,offset,userId){
  var params = new URLSearchParams();
  params.append("page",page);
  params.append("offset",offset);
  params.append("creator",userId);
  return axios.post("/project/getMyProject",params);
}
// 获取项目信息
function getProjectById(projectId){
  var params = new URLSearchParams();
  params.append("projectId",projectId);
  return axios.post("/project/getProjectById",params);
}
//校验项目名字是否存在
function checkProjectNameIsExist(name){
  //校验名字是否存在
  return axios.get("/project/checkNameIsExist?name="+encodeURIComponent(name));
}
//创建项目
function addProject(name,isPublic,brefIntro,userId){
  var params = {
    name:name,
    isPublic: isPublic,
    brefIntro:brefIntro,
    creator:userId,
    status:1
  };
  return axios.post("/project/addProject",params);
}
//获取所有的公开的项目
function getAllPublicProject(page,offset){
  var params = new URLSearchParams();
  params.append("page",page);
  params.append("offset",offset);
  return axios.post("/project/getAllProject",params);
}
function findDocAnnexBydocId(docId) {
  var params = new URLSearchParams();
  params.append("docId",docId);
  return axios.post("/doc/findDocAnnexBydocId",params);
}

//项目成员管理--获取各个部分的成员
function getUserProject(projectId){
  var params = new URLSearchParams();
  params.append("projectId",projectId);
  return axios.post("/project/getUserProject",params);
}
//成员管理保存
function saveUserProject(projectId,userInProjects,userManagerId){
  var params = new URLSearchParams();
  params.append("projectId",parseInt(projectId));
  params.append("userInProjects",userInProjects);
  params.append("userManagerId",parseInt(userManagerId));
  return axios.post("/project/saveUserProject",params);
}

function isProjectUser(projectId,userId) {
  var params = new URLSearchParams();
  params.append("projectId",projectId);
  params.append("userId",userId);
  return axios.post("/doc/isProjectUser",params);
}
//所有项目全表搜索
function searchProject(page,offset,search,isPublic,isSearchPublic){
  console.log(isPublic);
  var params = new URLSearchParams();
  params.append("page",page);
  params.append("offset",offset);
  params.append("search",search);
  if(isPublic!=null) {
    params.append("isPublic", isPublic);
  }
  if(isSearchPublic != null){
    params.append("isSearchPublic",isSearchPublic);
  }
  return axios.post("/project/searchProject",params);
}

function getProjectDataShireFileList(projectId,path){
  var params = new URLSearchParams();
  params.append("projectId",projectId);
  params.append("path",path);
  return axios.post("/admin/getProjectDataShireFileList",params);
}
//删除项目
function deleteProject(projectId){
  var params = new URLSearchParams();
  params.append("projectId",projectId);
  return axios.post("/project/delProject",params);
}
function updateProject(project){
  project.createTime = null;
  project.updateTime = null;
  return axios.post("/project/updateProjectMsg",project);
}
//查询项目成员
function getUserProjectSearch(projectId,search){
  var params = new URLSearchParams();
  params.append("projectId",projectId);
  params.append("search",search);
  return axios.post("/project/getUserProjectSearch",params);
}

//根据编号查询 文件夹信息
function findFolderByd(folderId) {
  var params = new URLSearchParams();
  params.append("folderId",folderId);
  return axios.post("/folder/findFolderByd",params);
}
//查询文档是否存在
function checkDocIsExists(docId) {
  var params = new URLSearchParams();
  params.append("docId",docId);
  return axios.post("/doc/checkDocIsExists",params);
}



export { login,
  modifyPass, getNotifications, getHotCourse, getSatistics, getCustomers,
  addTeacher, deleteTeacher, updateTeacher,
  getXueQiListData, deleteXueQi,
  get, post,
  getExpirementInfo, deleteExCloudware, getLastExperiment, getWebSocketUrl,
  getCourseDetail, getCourseExperimentDetail,
  createExCloudWare,
  getStudentCourse, getTeacherCourse,
  getKecheng, deletekecheng, addkecheng,
  getbanji, updateTearm, addBanKe, deleteBancheng,
  getStudentInfo, resetPWD,
  updateBanke, updateKecheng, getCustomersByPage,
  addModule, deleteModule,
  deleteStudent, addStudent,
  addExperiment, deleteExperimentById, updateExperiment,
  shu_lie_yun_url,edu_url,
  getDataShireDBList, getDataShireFileList, operateTable,deleteTable,deleteFile,
  fileManagementUrl,setToken,
  getExperimentById,getTearmById,getServerTime,getStudentExperimentCloudware,adminAndTeacherUsername,adminAndTeacgerPassword,
  findModuleIsExist,experimentIsExist,getShareFileList,deleteShareFile,downloadShareFile,
  createShareDirectory,renameShareFile,rancherHost,rancherPort,moveShareFile,checkIsExistsShare,
  getCourseById,getTeacherAllAssociateCourse,getAllCourses,
  selectCourseExistOtherExperimentByUserId,getCourseByCourseName,recordUserOnlineData,recordUserOfflineData,getClouderaData,getRealTimeOnlineData,
  getAccumulativeOnlineData,getDayAndWeekAndMonthOnlineAvgData,getCharacterCloudDashboardData,getRecentlyLoginUserData,
  getSystemTime,getAllStudentInfo,createStudent,updateStudent,delStudent,getStudentByNotClass,getCourseByClassId,
  deleteStudentByClass,addStudentList,deleteStudentList,addClassCourseList,deleteClassCourseList,adjustModuleOrder,adjustExperimentOrder,
  updateModule,getModuleInfo,getExperimentAnswer,isImportLicense,getSysKey,importOrUpdateLicense,deleteExperimentAnswer,
  submitExperimentAnswer,findClassByTeacherId,getCouExpByTeaIdAndCId,findStudentScore,findCourseByName,submitHomeworkOfOther,
  submitHomeworkOfShuLie,getStudentSubmitHomework,getCourseIsAnswer,getStudentIsSubAnswer,
  getJobShareFileList,getCourseExperimentDetailAndAnswer,findClassByName,logoutCheck,industry,
  getIndustryCountDmCountDownCount,getDmData,getEncryptTime,findFolderByProjectId,findFolderDoc,addProjectFolder,updateProjectFolder,
  deleteProjectFolder,addFolderDoc,updateProjectDoc,saveEditorProjectDoc,deleteProjectDoc,deleteDocFile,
  getMyProject,checkProjectNameIsExist,addProject,getProjectById,getAllPublicProject,updateProject,findDocAnnexBydocId,getUserProject,saveUserProject,searchProject,isProjectUser,
  deleteProject,getUserProjectSearch,findFolderByd,checkDocIsExists
};
