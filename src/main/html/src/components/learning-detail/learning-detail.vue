<template>
    <div id="learnDetail">
      <h-eader></h-eader>
      <div style="padding: 0px 100px;position: relative;top: 50px;">
        <div id="detail">
          <h1 class="title" style="font-size: 1.5em !important;
                            margin: 20px 0px !important;
                            margin-left: -24px !important;">{{courseName}}
          </h1>
          <el-collapse style="width: 100%;">
            <el-collapse-item
                              v-for="(item,idx) in tableData"
                              :key="idx"
                              :moduleId="item.moduleId"
                              @click.native="handleChange(idx)">
              <template slot="title">
                <span style="margin-right: 10px;">第{{item.orderIdCN}}章</span>
                <span style="white-space: pre-wrap;" >{{item.moduleName}}</span>
                <!--<div class="icon-radius"></div>-->
                <div id="colapse-title" :ref="idx">展开</div>
              </template>
              <template>
                <!--停止冒泡-->
                <div style="padding: 20px 30px;" @click.stop="handleChange">
                  <el-table
                    :data="item.moduleContent"
                    style="width: 100%;text-align: center" @click.stop="handleChange">
                    <el-table-column
                      label="序号"
                      width="180">
                      <template slot-scope="scope">实验{{scope.row.orderIdCN2}}</template>
                    </el-table-column>
                    <!--停止冒泡-->
                    <el-table-column
                      prop="experimentName"
                      label="实验名称"
                      :resizable="false"
                      :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column
                      prop="experimentDes"
                      label="实验描述"
                      :resizable="false"
                      :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column
                      prop="cloudwareType"
                      label="实验类型"
                      :resizable="false"
                      width="180">
                      <template slot-scope="scope">{{scope.row.cloudwareType}}</template>
                    </el-table-column>
                    <el-table-column
                      prop="publishDate"
                      label="创建时间"
                      :resizable="false"
                      width="180">
                    </el-table-column>
                    <el-table-column label="操作" :resizable="false">
                      <template slot-scope="scope">
                        <el-button
                          size="mini"
                          round
                          class="button-green"
                          @click="getEx(item,scope)">进入实验</el-button>
                        <!--:disabled="submitAnswerDisabled"-->
                        <el-button
                          v-if="submitAnswerShow"
                          size="mini"
                          round
                          class="button-green"
                          :disabled="scope.row.selectProgrmDisabled"
                          style="margin-left: -2px;"
                          @click="submitAnswer(courseName,scope.row.experimentName,item.moduleName,scope.row.cloudwareType,className,courseId,scope.row.id,route)">提交答案</el-button>

                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </template>
            </el-collapse-item>
          </el-collapse>
          <!--弹出窗口-->
          <el-dialog
            title="提示"
            :visible.sync="dialogVisible"
            width="80%"
            :before-close="handleClose">
            <div class="main">
              <div class="m-left"></div>
              <div class="m-right"></div>
            </div>
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
          </el-dialog>

        </div>
        <!--进入实验-->
        <el-dialog id="dialog"
                   :visible.sync="dialogVisible"
                   width="70%"
                   :before-close="handleClose">
          <div id="title">
            <div class="title-left">
              <p>{{dialogData.moduleName}}</p>
            </div>
            <div class="title-right">
              <p>实验{{dialogData.index+1}}：{{dialogData.experimentName}}</p>
            </div>
          </div>
          <div class="main">
            <div class="m-left">
              <mavon-editor :value="dialogData.experimentContent" :ishljs = "true" :scrollStyle="true"   defaultOpen="preview" :subfield="false" placeholder="暂无相关描述信息" :editable="false" :toolbarsFlag="false"  style="min-height:500px;width:100%;margin-top: 0px;margin-bottom: 0px;"/>
            </div>
            <div class="m-right">
              <!--实验部分-->
              <div class="experiment-part" ref="experiment" v-loading="loading"></div>
              <div id="icon">
                <i>
                  <img src="../../../static/images/icon/icon-top.png" alt="">
                </i>
                <i>
                  <img src="../../../static/images/icon/icon-right.png" alt="开始实验" @click="startEx">
                </i>
                <i>
                  <img src="../../../static/images/icon/icon-fullscreen.png">
                </i>
              </div>
            </div>
          </div>
        </el-dialog>


        <!--选择进入那个数猎场-->
        <el-dialog id="dialog"
                   :visible.sync="selectSpaceDialog"
                   width="470px">
          <div>
            <div style="margin-bottom: 20px;text-align: center">根据自己情况酌情选择</div>
            <div class="title-left" style="width: 56%;margin: 0px auto;">
              <!--<p><a @click="intoEx(1)">看老师demo</a></p>-->
              <el-button size="small" @click="intoEx(1)">看老师demo</el-button>
              <el-button size="small" @click="intoEx(2)">自己做练习</el-button>
            </div>
            <!--<div class="title-right">-->
            <!--<p><a @click="intoEx(2)">自己做练习</a></p>-->
            <!--</div>-->
          </div>
        </el-dialog>
      </div>
    </div>
</template>

<script type="text/ecmascript-6">
  import header from "../header/header";
  import Vue from 'vue'
  import mavonEditor from 'mavon-editor'
  import 'mavon-editor/dist/css/index.css'
  import axios from 'axios'
  import {
    adjustExperimentOrder,
    createExCloudWare,
    deleteExCloudware,
    findClassByName,
    getCourseDetail,
    getCourseExperimentDetail,
    getCourseExperimentDetailAndAnswer,
    getExpirementInfo,
    getLastExperiment,
    getStudentCourse,
    getStudentIsSubAnswer,
    getTeacherAllAssociateCourse,
    getTeacherCourse,
    getWebSocketUrl
  } from '@/common/js/request';
  import {isLogin} from '@/common/js/commons';

  Vue.use(mavonEditor)
  export default {
    data() {
      return {
        dialogVisible: false,
        tableData:[],
        courseId:'',
        dialogData:{},
        courseName:'',
        type:1,
        loading:false,
        selectSpaceDialog:false,
        index:-1,
        submitAnswerShow:true,
        className:'',//班级名称
        classId:'',
        route:'' //传递页面的name名
        // submitAnswerDisabled:true
      };
    },
    methods: {
      handleChange(idx){
        // if(this.index == idx){
        //   this.index =-idx;
        // } else {
        //   this.index = idx;
        // }

        var v=this.$refs[idx][0].innerHTML
        if(v=="展开"){
          this.$refs[idx][0].innerHTML = "收起";
        }else{
          this.$refs[idx][0].innerHTML = "展开";
        }
      },
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      },
      intoEx:function(type){
        var path = window.location.href;
        var currentPath = this.$route.fullPath;
        var domain = path.substring(0,path.indexOf(currentPath));
        var nextPath = domain+"/watchExperiment/"+this.dialogData.id;
        if(this.dialogData.cloudwareTypeId == 7){
          //数猎云相关课程
          var isTeacher = false;
          if(type==1){
            isTeacher = true;
          } else if(type==2){
            isTeacher = false;
          }
          try {
            callbackobjectforjs.opendatashirecoursecallback(nextPath, this.dialogData.courseName, isTeacher)
          } catch (err){
            console.log(err);
          }
        }
        this.selectSpaceDialog=false;
        //this.$router.push('/watchExperiment/'+this.dialogData.id);
      },
      getExData:function(item,scope){
        var selectData = item.moduleContent[scope.$index];
        this.dialogData = selectData;
        this.dialogData.moduleName = item.moduleName;
        this.dialogData.index = scope.$index;
        var path = window.location.href;
        var currentPath = this.$route.fullPath;
        var domain = path.substring(0,path.indexOf(currentPath));
        var nextPath = domain+"/watchExperiment/"+selectData.id;
        if(this.dialogData.cloudwareTypeId == 7) {
          var user = JSON.parse(localStorage["user"]);
          if(user.role !=3 ) {
            this.selectSpaceDialog = true;
          } else {
            // this.$router.push('/watchExperiment/'+this.dialogData.id);
            callbackobjectforjs.opendatashirecoursecannotruncallback(nextPath,this.dialogData.courseName);
          }
        } else {
          try {
            callbackobjectforjs.opennormalcoursecallback(nextPath,this.dialogData.courseName);
          } catch (err){}
          //this.$router.push('/watchExperiment/'+this.dialogData.id);
        }
      },
      getEx:function(item,scope){
        // debugger
        this.index=1
        //进入实验
        //获取实现相关数据(markdown信息)
        if(!isLogin()){
          this.$message({
            title:"提示",
            message:"请先登录",
            type:"error"
          });
          return;
        }
        //判断是否是学生的在学课程
        var user = JSON.parse(localStorage["user"]);
        // console.log("user开始")
        // // submitAnswerShow
        // console.log("user为:"+user)
        if(user.role==1){
          //获取在学课程
          var courseId = this.courseId;
          getStudentCourse(user.userId).then(res=>{
            console.log("我正在判断是否是在学课程");
            if(res.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            let isExist = false;
            if(res.data.errorCode==0){
              var studentClassList = res.data.data.studentClassList;
              for(let [idx,item] of studentClassList.entries()){
                if(item.courseId==courseId){
                  isExist = true;
                  break;
                }
              }
            }
            if(!isExist){
              this.$message({
                title:"提示",
                message:"该学生没有选择学习该课程",
                type:"error"
              });
              return;
            }
            this.getExData(item,scope);
          })
        }else if(user.role == 2){
          //获取在学课程
          var courseId = this.courseId;
          getTeacherAllAssociateCourse(user.userId).then(res=>{
            console.log("我正在判断是否是在学课程");
            console.log(res);
            if(res.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            let isExist = false;
            if(res.data.errorCode==0){
              var teacherCourseList = res.data.data;
              for(let [idx,item] of teacherCourseList.entries()){
                if(item.id==courseId){
                  isExist = true;
                  break;
                }
              }
            }
            if(!isExist){
              this.$message({
                title:"提示",
                message:"该老师没有教授这门课程",
                type:"error"
              });
              return;
            }
            this.getExData(item,scope);
          })
        } else {
          this.getExData(item,scope);
        }
      },
      // 提交答案
      submitAnswer(courseName,experimentName,moduleName,cloudwareType,className,courseId,experimentId,name){
        console.log("班级名称为:")
        console.log(className)
        // 判断是否登录
        if(!isLogin()){
          this.$message({
            title:"提示",
            message:"请先登录",
            type:"error"
          });
          return;
        }else{
          console.log("页面名称为:"+name)
          // console.log("实验id为:"+experimentId)
          this.$router.push({ path :'/exSubmit/', query : {courseName,experimentName,moduleName,cloudwareType,className,courseId,experimentId,name}});
        }
      },
      // 判断提交答案是否置灰
      judgeAnswerBtn(){
        var user = JSON.parse(localStorage["user"]);
        var routeName=this.$route.query.name;
       // 如果是所有课程详情
        if(routeName=="all"){
          // 如果是学生的话,
          if(user.role==1){
            // var courseId = this.courseId;
            var courseId = this.courseId;
            getStudentCourse(user.userId).then(res=>{
              console.log("我正在判断是否是在学课程");
              if(res.data=="alreadyResetPassword"){
                this.$message({
                  type:'warning',
                  message:'请重新登录'
                });
                return;
              }
              if(res.data.errorCode==0){
                var studentClassList = res.data.data.studentClassList;
                for(let [idx,item] of studentClassList.entries()){
                  if(item.courseId==courseId){
                    // idx=-idx
                    console.log(res)
                    break;
                  }
                }
                this.getExperimentData();
                console.log(this.tableData)
              }

            })
          }
          //如果是老师
          else if(user.role==2){
            this.submitAnswerShow=false
          }
          //如果是管理员
          else if(user.role==3){
            this.submitAnswerShow=false
          }
        }
        //如果是在学课程详情
        else if(routeName=="learning"){
          // 如果是学生的话,
          if(user.role==1){
            this.submitAnswerShow=true
          }
          //如果是老师
          else if(user.role==2){
            this.submitAnswerShow=false
          }
          //如果是管理员
          else if(user.role==3){
            this.submitAnswerShow=false
          }
        }
      },
      getNoteBookInfo:function(){
        if(this.type==0){
          //作业，暂时不做
        } else {
          //实验文件
          var user = JSON.parse(localStorage["user"]);
          getExpirementInfo(this.dialogData.id,user.userId).then(res=>{
            var data = res.data;
            if(data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            if(data.errorCode==0){
              var src="http://"+data.webSocket+"/tree?";
              this.$refs.experiment.innerHTML="<iframe src='"+src+"' style='width: 100%;height: 100%;min-height: 500px;' allowfullscreen></iframe>";
            } else if(data.errorCode ==7 || data.errorCode == 47){
              this.getNewNotebookInfo();
            } else if(data.errorCode == 35 && user.role==3){
              //管理员
              this.getNewNotebookInfo();
            }
          })
        }
      },
      _getWebSocketUrl:function(secret,cloudware_type,user_id){
        var params = {
          secret:secret,
          cloudware_type:cloudware_type,
          user_id:user_id
        };
        axios.defaults.headers = {
          withCredentials: true
        };
        return axios.post("http://api.cloudwarehub.com/services",params);
      },
      getNewNotebookInfo:function(){
        var secret="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmb28iOiJiYXIiLCJpYXQiOjE1MDU4MTM0NTd9.Ftw1yHeUrqdNvymFZcIpuEoS0RHBFZqu4MfUZON9Zm0";
        var cloudwareType = this.dialogData.cloudwareType;
        var user_id = JSON.parse(localStorage["user"]).userId;
        this._getWebSocketUrl(secret,cloudwareType,user_id).then(res=>{
          var data = res.data;
          if(data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          if(res.status==200){
            if(this.type==0){
              //作业，暂时不做
            } else {
              //实验
              var src="http://"+data.ws+"/tree?";
              this.$refs.experiment.innerHTML="<iframe src='"+src+"' style='width: 100%;height: 100%;min-height: 500px;' allowfullscreen></iframe>";
              // 添加实验信息
              this.dialogData.serviceName = data.service_name;
              this.dialogData.serviceId = data.service_id;
              createExCloudWare(this.dialogData.id,data.pulsar_id,data.service_id,data.service_name,JSON.parse(localStorage["user"]).userId,data.ws);
            }
          }
        });
      },
      getWebideInfo:function(){
        if(this.type==0){
          //作业，暂时不做
        } else {
          this.getNewWebideInfo();
        }
      },
      getNewWebideInfo:function(){
        var webideBaseUrl="http://localhost:7070/ws/";
        var webideUrl="http://baidu.com";

        //获取webideInfoUrl相关信息
        var secret="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmb28iOiJiYXIiLCJpYXQiOjE1MDU4MTM0NTd9.Ftw1yHeUrqdNvymFZcIpuEoS0RHBFZqu4MfUZON9Zm0";
        var cloudwareType = this.dialogData.cloudwareType;
        var userId = JSON.parse(localStorage["user"]).userId;
        this._getWebSocketUrl(secret,cloudwareType,userId).then(res=>{
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
            if(res.status==200){
              if(this.type==0){
                //作业，暂不做
              } else {
                //实验
                var webideServiceUrl = res.data.ws;
                var webideServiceId = res.data.service_id;
                var webideServiceName = res.data.service_name;
                webideUrl = webideBaseUrl+userId+"_"+this.dialogData.id+"/?wsUrl="+webideServiceUrl;
                this.$refs.experiment.innerHTML="<iframe src='"+webideUrl+"' style='width: 100%;height: 100%;min-height: 500px;' allowfullscreen></iframe>";
                // experimentId,pulsarId,serviceId,serviceName,studentId,webSocket
                createExCloudWare(this.dialogData.id,res.data.pulsar_id,webideServiceId,webideServiceName,userId,webideUrl);
              }
            }
        });
      },
      startEx:function(){
        //查看实验
        this.loading = true;
        var user = JSON.parse(localStorage['user']);
        if (this.dialogData.cloudwareType === 'jupyter_python') {
          //获取学生云文件
          this.getNoteBookInfo();
        } else if (this.dialogData.cloudwareType === 'ide_java') {
          this.getWebideInfo();
        } else if(this.dialogData.cloudwareTypeId == 7){
          //数猎云相关课程
          var user = JSON.parse(localStorage.getItem("user"));
          var isTeacher = false;
          if(user.role==2){
            isTeacher = true;
          }
          var path = window.location.href;
          callbackobjectforjs.opendatashirecoursecallback(path,this.dialogData.courseName,isTeacher)

        } else {
          //webSocket 调用docker ，如果存在，需要删除之前的容易
          if(this.dialogData.isStart){
            //已经启动过，直接来之前的docker路径来启动
            this.start(this.dialogData.webSocket,this.$refs.experiment);
          } else {
            // 没有启动过
            console.log("我没有启动过，开始启动容器");
            this.startService(user.userId,this.dialogData.cloudwareType,this.type);
          }
        }
        this.loading = false;
      },
      getCloudwareInfo:function(){
        //获取实验容器信息
        switch (this.type) {
          case 0:
            //获取学生的作业信息(作业暂时不做)
            console.log("获取学生的作业信息");
            break;
          case 1:
            //获取学生的实验信息
            console.log("开始获取实现信息");
            var experimentId = this.dialogData.id;
            var userId = JSON.parse(localStorage["user"]).userId;
            getExpirementInfo(experimentId,userId).then(response=>{
              if(response.data=="alreadyResetPassword"){
                this.$message({
                  type:'warning',
                  message:'请重新登录'
                });
                return;
              }
              if(response.status==200){
                if (response.data.errorCode == 0) {
                  console.log("已启动过...");
                  this.dialogData.isStart = true;
                  this.dialogData.webSocket = response.data.webSocket;
                  this.dialogData.serviceId = response.data.serviceId;
                  this.dialogData.instanceId = response.data.instanceId;
                  this.dialogData.serviceName = response.data.serviceName;
                  this.dialogData.pulsarId = response.data.pulsarId;
                } else if (response.data.errorCode == 35 || response.data.errorCode == 47) {
                  console.log("未启动过...");
                  this.dialogData.isStart = false;
                } else {
                  this.$notify.error({
                    title:"提示",
                    message:"获取实验云件失败,请重试",
                    duration:2
                  });
                }
              }
            });
            break;
          default:
            break;
        }
      },
      createCloudware:function(studentId, cloudware_type, type){
        this._getWebSocketUrl('eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmb28iOiJiYXIiLCJpYXQiOjE1MDU4MTM0NTd9.Ftw1yHeUrqdNvymFZcIpuEoS0RHBFZqu4MfUZON9Zm0',cloudware_type,studentId).then(resp=>{
          this.start(resp.data.ws, this.$refs.experiment);
          if (resp.data.errorCode == 0) {
            switch (type) {
              case 1:
                //保存到数据库
                // experimentId,pulsarId,serviceId,serviceName,studentId,webSocket
                createExCloudWare(this.dialogData.id,resp.data.pulsarId,resp.data.serviceId,resp.data.serviceName,studentId,resp.data.ws);
                break;
              default:
                break;
            }
          }
        });
        return;
      },
      startService:function(studentId,cloudware_type,type){
        if(type == 1) {
          getLastExperiment(studentId).then(response=>{
            if(response.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            if (response.data.errorCode == 0) {
              //如果上次实验存在且与本次实验id不一致，提醒用户删除上一次实验容器
              if (response.data.data.lastExperimentId != 0 &&
                response.data.data.lastExperimentId != 34) {
                deleteExCloudware(studentId,response.data.data.lastExperimentId).then(response=>{
                  if(response.data=="alreadyResetPassword"){
                    this.$message({
                      type:'warning',
                      message:'请重新登录'
                    });
                    return;
                  }
                  if (response.data.errorCode == 0) {
                    //删除上次创建的容器成功
                    this.$notify({
                      title:'提示',
                      message:"删除上次创建的容器成功",
                      type:"success",
                      duration:2,
                      center:true
                    });
                    this.createCloudware(studentId,cloudware_type,type);
                  }
                });
              } else {
                //如果上次实验存在且与本次实验id一致或没做过实验，直接开启
                console.log("我开始创建容器了");
                this.createCloudware(studentId, cloudware_type, type)
              }
            }
          });
        }
      },
      deleteEx:function(lastExperimentId){
        //删除实验
        var userId = JSON.parse(localStorage["user"]).userId;
        deleteExCloudware(userId,lastExperimentId).then(response=>{
          if(response.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          if (response.errorCode == 0) {
            console.log("删除实验成功");
            // $scope.hideExText = false;
          } else {
            console.log("删除实验失败:"+response.message)
          }
        });
      },
      start:function(wsaddr,el,retryTime){
        el.innerHTML="";
        var obj = this;
        var retryTime = retryTime || 0;
        var ws = new WebSocket(wsaddr);
        var instance = {
          canvas: null,
          isFullscreen: false,
          setFullscreen: function() {
            var docElm = document.documentElement;
            if (docElm.requestFullscreen) {
              canvas.requestFullscreen();
            } else if (docElm.webkitRequestFullScreen) {
              canvas.webkitRequestFullScreen();
            }
            this.isFullscreen = true;
          },
          ws: ws,
          fsapi: ''
        };
        ws.onerror = function(error) {
          console.log("error occurred")
        }
        ws.onclose = function () {
          console.log("close and restart");
          setTimeout(function() {
            if (retryTime > 10) {
              this.$notify.error({
                title: '错误',
                message: '启动云件失败，请重试',
                duration:2
              });
              if (this.type == 1) { //如果是实验，删除改实验云件
                this.dialogData.isStart = false;
                this.deleteEx(this.dialogData.id);
              }
            } else {
              retryTime++;
              obj.start(wsaddr, el, retryTime)
            }
          }, 10)
        };
        ws.onopen = function() {
          var canvas = document.getElementById("theCanvas")
          if(!canvas) {
            canvas = document.createElement('canvas')
            el.appendChild(canvas);
            canvas.width = 1440;
            canvas.height = 900;
            canvas.style.width = '100%';
            canvas.style.height = '100%';
            canvas.id = "theCanvas"
            canvas.tabIndex = 0
          }

          var canvasOnFocus = true
          canvas.oncontextmenu = function(e) {
            return false;
          }
          canvas.focus()
          instance.canvas = canvas

          canvas.onmousemove = function(e) {
            var dom_left = canvas.offsetLeft + canvas.offsetParent.offsetLeft;
            var dom_top = canvas.offsetTop + canvas.offsetParent.offsetTop;
            var scroll_top = el.scrollTop;
            if (instance.isFullscreen) {
              scroll_top = 0;
            }
            var xbei = canvas.offsetWidth / 1440;
            var ybei = canvas.offsetHeight / 900;
            var x = Math.floor((e.pageX - dom_left) / xbei);
            var y = Math.floor((e.pageY - dom_top + scroll_top) / ybei);
            var buf = new ArrayBuffer(5);
            var dv = new DataView(buf);
            dv.setUint8(0, 0);
            dv.setUint16(1, x, true);
            dv.setUint16(3, y, true);
            if (ws.readyState == 1 && canvasOnFocus)
              ws.send(buf);
          };
          canvas.onblur = function (e) {
            canvasOnFocus = false;
          }
          canvas.onfocus = function (e) {
            canvasOnFocus = true;
          }
          canvas.onmousedown = function(e) {
            var buf = new ArrayBuffer(5);
            var dv = new DataView(buf);
            dv.setUint8(0, 1);
            dv.setUint32(1, e.which, true);
            if (ws.readyState == 1 && canvasOnFocus)
              ws.send(buf);
          };
          canvas.onmouseup = function(e) {
            var buf = new ArrayBuffer(5);
            var dv = new DataView(buf);
            dv.setUint8(0, 2);
            dv.setUint32(1, e.which, true);
            if (ws.readyState == 1 && canvasOnFocus)
              ws.send(buf);
          };
          document.onkeydown = function(e) {
            if (e.keyCode == 9 || e.keyCode == 32) { //tab pressed
              e.preventDefault(); // stops its action
            }
            var buf = new ArrayBuffer(5);
            var dv = new DataView(buf);
            dv.setUint8(0, 3);
            dv.setUint32(1, this.mapKey(e.which), true);
            if (ws.readyState == 1 && canvasOnFocus)
              ws.send(buf);
          };
          document.onkeyup = function(e) {
            if (e.keyCode == 9 || e.keyCode == 32) { //tab pressed
              e.preventDefault(); // stops its action
            }
            var buf = new ArrayBuffer(5);
            var dv = new DataView(buf);
            dv.setUint8(0, 4);
            dv.setUint32(1, mapKey(e.which), true);
            if (ws.readyState == 1 && canvasOnFocus)
              ws.send(buf);
          };
        };
        ws.binaryType = "arraybuffer";
        ws.onmessage = function(msg) {
          var data = msg.data;
          if (!(data instanceof ArrayBuffer)) {
            return
          }
          var dv = new DataView(data);

          var x = dv.getInt32(0, true);
          var y = dv.getInt32(4, true);
          var d = data.slice(8);
          var blob = new Blob([d], { type: 'image/webp' });
          var url = URL.createObjectURL(blob);
          var img = new Image;
          img.onload = function() {
            var ctx = instance.canvas.getContext('2d');
            ctx.drawImage(img, x, y);
            URL.revokeObjectURL(url);
          };
          img.src = url;
        };
      },
      mapKey:function(keyCode){
        var xkm = [
          [65406, 0, 65406, 0, 0, 0, 0],
          [65307, 0, 65307, 0, 0, 0, 0],
          [49, 33, 49, 33, 0, 0, 0],
          [50, 64, 50, 64, 0, 0, 0],
          [51, 35, 51, 35, 0, 0, 0],
          [52, 36, 52, 36, 0, 0, 0],
          [53, 37, 53, 37, 0, 0, 0],
          [54, 94, 54, 94, 0, 0, 0],
          [55, 38, 55, 38, 0, 0, 0],
          [56, 42, 56, 42, 0, 0, 0],
          [57, 40, 57, 40, 0, 0, 0],
          [48, 41, 48, 41, 0, 0, 0],
          [45, 95, 45, 95, 0, 0, 0],
          [61, 43, 61, 43, 0, 0, 0],
          [65288, 65288, 65288, 65288, 0, 0, 0],
          [65289, 65056, 65289, 65056, 0, 0, 0],
          [113, 81, 113, 81, 0, 0, 0],
          [119, 87, 119, 87, 0, 0, 0],
          [101, 69, 101, 69, 0, 0, 0],
          [114, 82, 114, 82, 0, 0, 0],
          [116, 84, 116, 84, 0, 0, 0],
          [121, 89, 121, 89, 0, 0, 0],
          [117, 85, 117, 85, 0, 0, 0],
          [105, 73, 105, 73, 0, 0, 0],
          [111, 79, 111, 79, 0, 0, 0],
          [112, 80, 112, 80, 0, 0, 0],
          [91, 123, 91, 123, 0, 0, 0],
          [93, 125, 93, 125, 0, 0, 0],
          [65293, 0, 65293, 0, 0, 0, 0],
          [65507, 0, 65507, 0, 0, 0, 0],
          [97, 65, 97, 65, 0, 0, 0],
          [115, 83, 115, 83, 0, 0, 0],
          [100, 68, 100, 68, 0, 0, 0],
          [102, 70, 102, 70, 0, 0, 0],
          [103, 71, 103, 71, 0, 0, 0],
          [104, 72, 104, 72, 0, 0, 0],
          [106, 74, 106, 74, 0, 0, 0],
          [107, 75, 107, 75, 0, 0, 0],
          [108, 76, 108, 76, 0, 0, 0],
          [59, 58, 59, 58, 0, 0, 0],
          [39, 34, 39, 34, 0, 0, 0],
          [96, 126, 96, 126, 0, 0, 0],
          [65505, 0, 65505, 0, 0, 0, 0],
          [92, 124, 92, 124, 0, 0, 0],
          [122, 90, 122, 90, 0, 0, 0],
          [120, 88, 120, 88, 0, 0, 0],
          [99, 67, 99, 67, 0, 0, 0],
          [118, 86, 118, 86, 0, 0, 0],
          [98, 66, 98, 66, 0, 0, 0],
          [110, 78, 110, 78, 0, 0, 0],
          [109, 77, 109, 77, 0, 0, 0],
          [44, 60, 44, 60, 0, 0, 0],
          [46, 62, 46, 62, 0, 0, 0],
          [47, 63, 47, 63, 0, 0, 0],
          [65506, 0, 65506, 0, 0, 0, 0],
          [65450, 65450, 65450, 65450, 65450, 65450, 269024801],
          [65513, 65511, 65513, 65511, 0, 0, 0],
          [32, 0, 32, 0, 0, 0, 0],
          [65509, 0, 65509, 0, 0, 0, 0],
          [65470, 65470, 65470, 65470, 65470, 65470, 269024769],
          [65471, 65471, 65471, 65471, 65471, 65471, 269024770],
          [65472, 65472, 65472, 65472, 65472, 65472, 269024771],
          [65473, 65473, 65473, 65473, 65473, 65473, 269024772],
          [65474, 65474, 65474, 65474, 65474, 65474, 269024773],
          [65475, 65475, 65475, 65475, 65475, 65475, 269024774],
          [65476, 65476, 65476, 65476, 65476, 65476, 269024775],
          [65477, 65477, 65477, 65477, 65477, 65477, 269024776],
          [65478, 65478, 65478, 65478, 65478, 65478, 269024777],
          [65479, 65479, 65479, 65479, 65479, 65479, 269024778],
          [65407, 0, 65407, 0, 0, 0, 0],
          [65300, 0, 65300, 0, 0, 0, 0],
          [65429, 65463, 65429, 65463, 0, 0, 0],
          [65431, 65464, 65431, 65464, 0, 0, 0],
          [65434, 65465, 65434, 65465, 0, 0, 0],
          [65453, 65453, 65453, 65453, 65453, 65453, 269024803],
          [65430, 65460, 65430, 65460, 0, 0, 0],
          [65437, 65461, 65437, 65461, 0, 0, 0],
          [65432, 65462, 65432, 65462, 0, 0, 0],
          [65451, 65451, 65451, 65451, 65451, 65451, 269024802],
          [65436, 65457, 65436, 65457, 0, 0, 0],
          [65433, 65458, 65433, 65458, 0, 0, 0],
          [65435, 65459, 65435, 65459, 0, 0, 0],
          [65438, 65456, 65438, 65456, 0, 0, 0],
          [65439, 65454, 65439, 65454, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [60, 62, 60, 62, 124, 166, 124],
          [65480, 65480, 65480, 65480, 65480, 65480, 269024779],
          [65481, 65481, 65481, 65481, 65481, 65481, 269024780],
          [65360, 0, 65360, 0, 0, 0, 0],
          [65362, 0, 65362, 0, 0, 0, 0],
          [65365, 0, 65365, 0, 0, 0, 0],
          [65361, 0, 65361, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [65363, 0, 65363, 0, 0, 0, 0],
          [65367, 0, 65367, 0, 0, 0, 0],
          [65364, 0, 65364, 0, 0, 0, 0],
          [65366, 0, 65366, 0, 0, 0, 0],
          [65379, 0, 65379, 0, 0, 0, 0],
          [65535, 0, 65535, 0, 0, 0, 0],
          [65421, 0, 65421, 0, 0, 0, 0],
          [65508, 0, 65508, 0, 0, 0, 0],
          [65299, 65387, 65299, 65387, 0, 0, 0],
          [65377, 65301, 65377, 65301, 0, 0, 0],
          [65455, 65455, 65455, 65455, 65455, 65455, 269024800],
          [65514, 65512, 65514, 65512, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [65515, 0, 65515, 0, 0, 0, 0],
          [65516, 0, 65516, 0, 0, 0, 0],
          [65383, 0, 65383, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [65027, 0, 65027, 0, 0, 0, 0],
          [0, 65513, 0, 65513, 0, 0, 0],
          [65469, 0, 65469, 0, 0, 0, 0],
          [0, 65515, 0, 65515, 0, 0, 0],
          [0, 65517, 0, 65517, 0, 0, 0],
          [269025074, 0, 269025074, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [65454, 65454, 65454, 65454, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025046, 0, 269025046, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025071, 0, 269025071, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025047, 0, 269025047, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 65511, 0, 65511, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025042, 0, 269025042, 0, 0, 0, 0],
          [269025053, 0, 269025053, 0, 0, 0, 0],
          [269025044, 269025073, 269025044, 269025073, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025045, 269025068, 269025045, 269025068, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025068, 0, 269025068, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025041, 0, 269025041, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025043, 0, 269025043, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025070, 0, 269025070, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025068, 0, 269025068, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025113, 0, 269025113, 0, 0, 0, 0],
          [269025028, 0, 269025028, 0, 0, 0, 0],
          [269025030, 0, 269025030, 0, 0, 0, 0],
          [269025029, 0, 269025029, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025066, 0, 269025066, 0, 0, 0, 0],
          [269025040, 0, 269025040, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025067, 0, 269025067, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025051, 0, 269025051, 0, 0, 0, 0],
          [269025072, 0, 269025072, 0, 0, 0, 0],
          [269025139, 0, 269025139, 0, 0, 0, 0],
          [269025064, 0, 269025064, 0, 0, 0, 0],
          [269025063, 0, 269025063, 0, 0, 0, 0],
          [269025062, 0, 269025062, 0, 0, 0, 0],
          [269025075, 0, 269025075, 0, 0, 0, 0],
          [269025049, 0, 269025049, 0, 0, 0, 0],
          [269025074, 0, 269025074, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025171, 0, 269025171, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [269025173, 0, 269025173, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0],
          [0, 0, 0, 0, 0, 0, 0]
        ];
        var exceptionKeys = {
          '190': '46', // PERIOD CHROME
          '16': '65506', // SHIFT CHROME
          '17': '65507', // CTRL CHROME
          '18': '65513', // ALT CHROME
          '34': '65307', // ESC CHROME
          '191': '47', // BACKSLASH CHROME
          '20': '65509', // CAPS CHROME
          '9': '65289', // TAB CHROME
          '189': '45', // MINUS CHROME
          '187': '61', // EQUALS CHROME
          '8': '65288', // BACKSPACE CHROME
          '220': '92', // FOWARD SLASH CHROME
          '13': '65293', // ENTER CHROME
          '192': '96', // TILDE CHROME
          '186': '59', // SEMICOLON CHROME
          '222': '34', // QUOTES CHROME
          '188': '44', // COMMA CHROME
          '27': '65307', // ESC CHROME
          '39': '65363', // RIGHT ARROW CHROME
          '37': '65361', // LEFT ARROW CHROME
          '38': '65362', // UP ARROW CHROME
          '40': '65364', // DOWN ARROW CHROME
          '91': '65506', // set osx cmd to shift
        };

        function buildASCIIToXKeyMap(XKeysMap, min) {
          var asciiToX = {};
          for (var i = 0; i < XKeysMap.length; i++) {
            for (var j = 0; j < XKeysMap[i].length; j++) {
              var key = XKeysMap[i][j];
              var value = i + min;
              if (key !== 0)
                asciiToX[key] = value;
            }
          }
          return asciiToX;
        }

        var keyMap = buildASCIIToXKeyMap(xkm, 8);
        if (exceptionKeys[keyCode])
          keyCode = exceptionKeys[keyCode];
        var key = keyMap[keyCode];
        if (key === undefined || key === null)
          key = 62; // shift
        if (keyCode == 219) {
          key = 34;
        } else if (keyCode === 221) {
          key = 35
        }
        return key;
      },
      numberChinese:function (number) {
        var units = '个十百千万@#%亿^&~', chars = '零一二三四五六七八九';
        var a = (number + '').split(''), s = [];
        if (a.length > 12) {
          throw new Error('too big');
        } else {
          for (var i = 0, j = a.length - 1; i <= j; i++) {
            if (j == 1 || j == 5 || j == 9) {//两位数 处理特殊的 1*
              if (i == 0) {
                if (a[i] != '1') s.push(chars.charAt(a[i]));
              } else {
                s.push(chars.charAt(a[i]));
              }
            } else {
              s.push(chars.charAt(a[i]));
            }
            if (i != j) {
              s.push(units.charAt(j - i));
            }
          }
        }
        return s.join('').replace(/零([十百千万亿@#%^&~])/g, function (m, d, b) {//优先处理 零百 零千 等
          b = units.indexOf(d);
          if (b != -1) {
            if (d == '亿') return d;
            if (d == '万') return d;
            if (a[j - b] == '0') return '零'
          }
          return '';
        }).replace(/零+/g, '零').replace(/零([万亿])/g, function (m, b) {// 零百 零千处理后 可能出现 零零相连的 再处理结尾为零的
          return b;
        }).replace(/亿[万千百]/g, '亿').replace(/[零]$/, '').replace(/[@#%^&~]/g, function (m) {
          return { '@': '十', '#': '百', '%': '千', '^': '十', '&': '百', '~': '千' }[m];
        }).replace(/([亿万])([一-九])/g, function (m, d, b, c) {
          c = units.indexOf(d);
          if (c != -1) {
            if (a[j - c] == '0') return d + '零' + b
          }
          return m;
        });
      },
      getExperimentData:function(){
        if(this.classId!="" && this.classId!=null && this.classId!=undefined){
           var user = JSON.parse(localStorage["user"]);
          if(user!=null && user.role==1){
            console.log("查询作业");
            //获取实验数据
            getCourseExperimentDetailAndAnswer(this.courseId,user.userId,this.classId).then(res=>{
              if(res.data=='alreadyResetPassword'){
                this.$message({
                  type:"warning",
                  message:"请重新登录"
                });
                return;
              }
              if(res.status==200){
                var data = res.data;
                this.courseName = data.data.courseName;
                if(data.errorCode==0){
                  //操作成功
                  this.tableData = data.data.moduleList;
                  if(this.tableData != undefined){
                    this.tableData.map(item => {
                      item.orderIdCN=this.numberChinese(item.orderId);
                      item.moduleContent.map(item2=>{
                        console.log(item2);
                        if(user.role==1){
                          if(item2.stuIsAnswer=="1"){
                            item2.selectProgrmDisabled = true;
                          }else if(item2.stuIsAnswer=="0" && item2.isAnswer!="1"){
                            item2.selectProgrmDisabled = true;
                          }else{
                            item2.selectProgrmDisabled = false;
                          }
                        }
                        item2.orderIdCN2=this.numberChinese(item2.orderId);
                      });
                      item.colspace = 0;
                    })
                  }
                }
              }
            });
          }else{
            console.log("正常查询011");
            getCourseExperimentDetail(this.courseId).then(res=>{
              if(res.data=='alreadyResetPassword'){
                this.$message({
                  type:"warning",
                  message:"请重新登录"
                });
                return;
              }
              if(res.status==200){
                var data = res.data;
                this.courseName = data.data.courseName;
                if(data.errorCode==0){
                  //操作成功
                  this.tableData = data.data.moduleList;
                  if(this.tableData != undefined){
                    this.tableData.map(item => {
                      item.orderIdCN=this.numberChinese(item.orderId);
                      item.moduleContent.map(item2=>{
                        console.log("禁用");
                        item2.selectProgrmDisabled = true;
                        item2.orderIdCN2=this.numberChinese(item2.orderId);
                      });
                      item.colspace = 0;
                    })
                  }
                }
              }

            });
          }
        }else{
          console.log("正常查询");
          getCourseExperimentDetail(this.courseId).then(res=>{
            if(res.data=='alreadyResetPassword'){
              this.$message({
                type:"warning",
                message:"请重新登录"
              });
              return;
            }
            if(res.status==200){
              var data = res.data;
              this.courseName = data.data.courseName;
              if(data.errorCode==0){
                //操作成功
                this.tableData = data.data.moduleList;
                if(this.tableData != undefined){
                  this.tableData.map(item => {
                    item.orderIdCN=this.numberChinese(item.orderId);
                    item.moduleContent.map(item2=>{
                      item2.orderIdCN2=this.numberChinese(item2.orderId);
                      console.log("禁用");
                      item2.selectProgrmDisabled = true;
                    });
                    item.colspace = 0;
                  })
                }
              }
            }

          });
        }


      },
      init:function(){
        var courseId = this.$route.params.courseId;
        this.courseId = courseId;
        this.className=this.$route.query.className;
        console.log(this.className+"班级");
        if(this.className!=null && this.className!="" && this.className!=undefined){
          var user = JSON.parse(localStorage["user"]);
          if(user!=null && user.role==1){
            findClassByName(this.className).then(res=>{
              if(res.status=200){
                if(res.data.errorCode==0){
                  this.classId=res.data.data.id;
                  this.getExperimentData();
                }
              }
            });
          }else{
            this.getExperimentData();
          }
        }else{
          this.getExperimentData();
        }
      }
    },
    beforeRouteEnter: (to, from, next) => {
      next(vm => {
        vm.init();
      });
    },
    // 判断提交答案是否置灰
    created(){
      this.init();
      this.route=this.$route.query.name;
      console.log(this.route);
      //判断提交答案按钮是否置灰
      this.judgeAnswerBtn()
    },
    components: {
      "h-eader": header
    },
  };
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  #detail
    bottom 0px
    width 100%
    .table
      display table
      width 100%
      height auto
      .table-row
        display table-row
        width 100%
        height 30px
        line-height 30px
        .table-cell
          display table-cell
          width 20%
          text-align center
          th .cell
            color #000000
  #dialog
    #title
      width 100%
      height 50px
      border-bottom  1px solid #e5e5e5
      display flex
      text-align center
      color #00a2ff
      line-height 30px
      .title-left
        width 165px
        height 30px
        background url("../../../static/images/title-left.png")
        background-size cover
      .title-right
        width 175px
        height 30px
        background url("../../../static/images/title-right.png")
        background-size cover
        margin-left -3px
    .main
      width 100%
      display flex
      .m-left
        flex 1
        border-right 1px solid #e5e5e5
        div
          margin-top 0px
          margin-bottom 0px
          button
            margin-bottom 15px
          p
            font-size 13px
            color #646464
      .m-right
        flex 3
        position relative
        #icon
          position absolute
          width 30%
          left 35%
          bottom 20px
          z-index 999

</style>
<style type="text/css" >
  .el-dialog{
    /*background-color: #f8f8f8*/
  }
  #detail .el-table th > .cell{
    text-align: center;font: 1.1em bolder;
  }

 #detail .title{
    font-size: 1.5em !important;
    margin: 20px 0px !important;
    margin-left: -20px !important;
  }
  #detail .el-collapse-item__header{
    color: #494b4f !important;
    font-size: 18px !important;
  }
  #detail .el-collapse-item__header{
    position: relative;
    color: #494b4f !important;font-size: 18px;
    /*border: 1px solid red;*/
  }
  .icon-radius{
    width: 8px;height: 8px;position: absolute;border-radius: 50%;
    left: -20px;top: 20px;background-color: #494b4f;
  }
  #colapse-title{
    position: absolute;
    top: 0px;
    right: 15px;
    color: #bebebe;
    font-size: 15px;
  }
  .el-textarea__inner{
    height: 40px;
    color: #606266 !important;
    font-size: inherit;
    font-family: '微软雅黑';
  }
  #detail .el-collapse-item__arrow{
    display: block;
    margin-right: 46px;
    /*background: url("../../../static/images/icon/icon-Top.png");*/
    /*background-size: 18px 18px;*/
    /*border: 1px solid red;*/
  }
  #detail .el-icon-arrow-right:before{
    content: "\e603";
    color: #bebebe;
  }
  #detail .el-collapse-item__arrow.is-active{
    transform: rotate(180deg);
    content:""
  }
  #detail .el-collapse-item__content{
    padding-bottom: 0px !important;
  }
  #learnDetail .sort-caret.descending.active{
    border-top-color: black !important;
  }
  #learnDetail .sort-caret.ascending.active{
    border-bottom-color:black !important;
  }
</style>

