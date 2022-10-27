<template>
  <div id="watch" >
    <div class="m-left" >
      <h1 class="kong" style="margin-right: 13px;">{{dialogData.courseName}}</h1>
      <div class="leftLarge">
        <img src="../../../static/images/icon/icon-largeScreen.png"
                :class="Disgray? 'gray-true':'gray-false'"
                v-if="canCelLeftShow"
                width="25" height="25"
                alt="点击图片,关闭大屏"
                @click="canCelLeftlargeScreen">
       <img src="../../../static/images/icon/icon-fullscreen.png"
               :class="Disgray? 'gray-true':'gray-false'"
               v-if="leftShow"
               width="25" height="25"
               alt="点击图片,开启大屏" @click="isLeftlargeScreen">
      </div>
      <!--表单-->
      <ul id="menuul">
        <!--<li>{{dialogData.moduleName}}</li>-->
        <!--<li>{{dialogData.experimentName}}</li>-->
        <li class="menuli1">
          <p>{{dialogData.moduleName}}</p>
        </li>
        <li class="menuli2">
          <p>{{dialogData.experimentName}}</p>
        </li>
      </ul>
      <mavon-editor  v-model="bianjiForm.experimentContent" id="markeditor"
                     :ishljs = "true" :scrollStyle="true"
                     defaultOpen="preview" :subfield="false"
                     placeholder="暂无相关描述信息" :editable="false"
                     :toolbarsFlag="false" />
    </div>
    <label class="handle" ></label>
    <div class="m-right" v-show="parseInt(dialogData.cloudwareTypeId) != 7" v-loading="loading">
      <div class="prompt" v-if="isprompt">
        <h1>实验区域</h1>
        <p v-show="showTips">点击头部工具栏开始实验</p>
      </div>
      <!--实验部分-->
      <div class="experiment-part" ref="experiment"></div>
      <div id="icon"  >
        <!--<img src="../../../static/images/icon/icon-hidden.png"  :class="Disgray? 'gray-true':'gray-false'"  v-if="isshow" width="25" height="25" alt="隐藏实验" @click="hidden">-->

        <img src="../../../static/images/icon/icon-pause.png"
             v-if="isClose"
             width="25"
             height="25"
             alt="关闭实验"
             @click="closeEx">

        <img src="../../../static/images/icon/icon-right.png"
             :class="Disgray? 'gray-true':'gray-false'"
             v-if="isStarShow" width="25"
             height="25" alt="开始实验" @click="startEx">

        <img src="../../../static/images/icon/icon-fullscreen.png"  :class="Disgray? 'gray-true':'gray-false'" v-if="islargeScreen" width="25" height="25" alt="点击图片,开启大屏"
             @click="largeScreen"
             native-type="button">

        <img src="../../../static/images/icon/icon-largeScreen.png" v-if="isBiglargeScreen"
             width="25" height="25" alt="点击图片,关闭大屏"
             @click="biglargeScreen"
             native-type="button">
      </div>

    </div>
    <!--<div style="clear: both"></div>-->
  </div>
</template>

<script type="text/ecmascript-6">
  import Vue from 'vue'
  import mavonEditor from 'mavon-editor'
  import 'mavon-editor/dist/css/index.css'
  import axios from 'axios'
  import $ from 'jquery'
  import {
    addModule,
    createExCloudWare,
    deleteExCloudware,
    deleteExperimentById,
    deleteModule,
    edu_url,
    getCourseDetail,
    getCourseExperimentDetail,
    getExperimentById,
    getExpirementInfo,
    getLastExperiment,
    getStudentExperimentCloudware,
    getWebSocketUrl,
    rancherHost,
    rancherPort,
    updateExperiment
  } from '@/common/js/request';

  Vue.use(mavonEditor)
  export default {
    data(){
      return{
        bianjiForm:{},
        actionUrl:null,
        dialogData:{
          cloudwareTypeId:null,
          cloudwareType:null,
          courseName:null,
          dueDate:null,
          experimentContent:null,
          experimentDes:null,
          experimentName:null,
          experimentUrl:null,
          id:null,
          moduleName:null,
          publishDate:null
        },
        type:1,
        experimentId:null,
        loading:false,
        ws:null,
        timer:null,
        isprompt:true,
        isClose:false,
        isStarShow:true,
        islargeScreen:true,
        isBiglargeScreen:false,
        showTips:true,
        Disgray:false,
        leftShow:true,//左边全屏按钮显示
        canCelLeftShow:false//左边取消全屏按钮隐藏
        // handleShow:true
        // grayTrue:false
      }
    },
    methods:{
      // 点击按钮，左边全屏展示
      isLeftlargeScreen(){
        // 全屏
        var mLeft=document.getElementsByClassName("m-left")[0];
        var mRight=document.getElementsByClassName("m-right")[0];
        var windoWidth=document.body.clientWidth
        // 拖动条
        var handle=document.getElementsByClassName("handle")[0]
        console.log(handle)
        if(mRight.style.display=="block"){
          mRight.style.display="none"
          mLeft.style.width="100%"
          // 隐藏滚动条
          handle.style.display="none"
          this.leftShow= !this.leftShow
          this.canCelLeftShow= !this.canCelLeftShow
        }
        else{
          // 取消全屏
          mRight.style.display="block"
          if(parseFloat(mRight.style.width) <100){
            mLeft.style.width=100- parseFloat(mRight.style.width)-0.6+"%"
          }
          else{
            mRight.style.width = ((parseFloat(mRight.style.width))/windoWidth)*100 + "%"

            mLeft.style.width = 100 - parseFloat(mRight.style.width)-0.6+ "%"
          }
          // 隐藏滚动条
          handle.style.display="block"
          handle.style.left=parseFloat(mLeft.style.width)+"%"
          this.leftShow= !this.leftShow
          this.canCelLeftShow= !this.canCelLeftShow
        }
      },
      // 左边取消全屏
      canCelLeftlargeScreen(){
        this.isLeftlargeScreen()
      },
      cancelLarge(){},
      // 右边全屏
      largeScreen(){
        // debugger
        var mLeft=document.getElementsByClassName("m-left")[0];
        var mLeftWidth= mLeft.style.width
        //document.getElementsByClassName("m-right")[0].clientWidth="none";
        var mRight=document.getElementsByClassName("m-right")[0];
        var windoWidth=document.body.clientWidth
        // 拖动条
        var handle=document.getElementsByClassName("handle")[0]
        console.log(handle)
        if(mLeft.style.display=="block"){
          mLeft.style.display="none"
          mRight.style.width="100%"
          // 隐藏滚动条
          handle.style.display="none"
          this.isBiglargeScreen= !this.isBiglargeScreen
          this.islargeScreen= !this.islargeScreen
        }
        else{

          // 右边取消全屏
          mLeft.style.display="block"
          if(parseFloat(mLeft.style.width) <100){
            mRight.style.width=100- parseFloat(mLeft.style.width)-0.6+"%"
            // console.log("左边的宽度为:"+ parseFloat(mLeft.style.width))
          }
          else{
            mLeft.style.width = ((parseFloat(mLeft.style.width))/windoWidth)*100 + "%"

            mRight.style.width = 100 - parseFloat(mLeft.style.width)-0.6+ "%"
            // console.log("右边宽度:"+ (100 - parseFloat(mLeft.style.width)))
            // mRight.style.width=(100-mLeft.style.width)+"%"
            // console.log("右边百分比为:"+mRight.style.width)
            // mLeft.style.width = 100 - mRight.style.width +"%"
          }
          // 隐藏滚动条
          handle.style.display="block"
          handle.style.left=parseFloat(mLeft.style.width)+"%"
          this.isBiglargeScreen= !this.isBiglargeScreen
          this.islargeScreen= !this.islargeScreen
        }
      },
      biglargeScreen(){
        this.largeScreen()
      },
      getEx:function(item,scope){
        //进入实验
        this.dialogVisible = true; //打开实验对话框
        //this.$router.push('/watchExperiment');
        //获取实现相关数据(markdown信息)
        var selectData = item.moduleContent[scope.$index];
        this.dialogData = selectData;
        this.dialogData.moduleName = item.moduleName;
        this.dialogData.index = scope.$index;
        // console.log(this.dialogData);
      },
      startEx:function(){
        //查看实验(管理员不允许查看实验)
        var user = JSON.parse(localStorage['user']);
        if(user.role==3){
          return;
        }
        if(this.isStarShow==true){
          this.isStarShow = !this.isStarShow;
          this.isClose=true;
          this.isprompt=false;
          //this.$refs.experiment.innerHTML="";
          this.loading = true;
          var user = JSON.parse(localStorage['user']);
          if (this.dialogData.cloudwareType === 'jupyter_python') {
            //获取学生云文件
            //this.getNoteBookInfo(user.userId,this.dialogData.cloudwareType,this.type,this.dialogData.id);
            this.startService(user.userId,this.dialogData.cloudwareType,this.type,this.dialogData.id);
          } else if (this.dialogData.cloudwareType === 'ide_java') {
            this.getWebideInfo();
          } else {
            //webSocket 调用docker ，如果存在，需要删除之前的容易
            this.startService(user.userId,this.dialogData.cloudwareType,this.type,this.dialogData.id);
          }
        }else{
          this.isStarShow = !this.isStarShow
          this.isClose=false
        }
      },
      closeEx:function(){
        // if(this.close){
        //
        // }
        if(this.isClose==true){

          this.$confirm('您确认停止实验吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.isClose = !this.isClose
            this.isStarShow=true
            //关闭实验
            this.isprompt=true
            var experimentId = this.dialogData.id;
            this.dialogData.isStart = true;
            this.dialogData.isDelete = true;
            this.$refs.experiment.innerHTML="";
            this.deleteEx(experimentId);
            this.$message({
              type: 'success',
              message: '已成功关闭实验!'
            });
            try{
              callbackobjectforjs.changenormalcourseexperimentstatus(false);
            } catch (err){}
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消关闭实验'
            });
          });
        }else{
          this.isClose = !this.isClose
          this.isStarShow=false
        }

      },
      createNewNoteBookInfo:function(studentId, cloudware_type, type,experimentId){
        // console.log(studentId+" "+cloudware_type+" "+type);
        this.dialogData.isStart = false;
        this.dialogData.isDelete = false;
        //判断是否存在该实验的容器,如果存在，那么直接使用上一次的容器
        this._getWebSocketUrl('eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmb28iOiJiYXIiLCJpYXQiOjE1MDU4MTM0NTd9.Ftw1yHeUrqdNvymFZcIpuEoS0RHBFZqu4MfUZON9Zm0',cloudware_type,studentId,experimentId).then(resp=>{
          console.log("我正在获取请求");
          if(resp.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          if (resp.data.errorCode == 0) {
            switch (type) {
              case 1:
                //保存到数据库
                // experimentId,pulsarId,serviceId,serviceName,studentId,webSocket
                createExCloudWare(this.dialogData.id,resp.data.data.pulsar_id,resp.data.data.service_id,resp.data.data.service_name,studentId,resp.data.data.ws);
                break;
              default:
                break;
            }
          }
          console.log("播放成功")
          //this.start(resp.data.data.ws, this.$refs.experiment);
          let url = edu_url+"/startEx?host="+rancherHost+"&port="+rancherPort+"&path="+resp.data.data.ws+"&time="+new Date().getTime();
          //let url = rancherHost+":8001/tree";
          this.$refs.experiment.innerHTML="<iframe src='"+url+"' style='width: 100%;height: 100%;'>";
          try{
            callbackobjectforjs.changenormalcourseexperimentstatus(true);
          } catch (err){}
          this.loading = false;
        });
        return;
      },
      getNoteBookInfo:function(studentId,cloudware_type,type,experimentId){
        if(type == 1) {
          // 获取学生上一次该课程的实验
          getLastExperiment(studentId).then(response=>{
            console.log("我正在获取上一次实验");
            if(response.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            if (response.data.errorCode == 0) {

              //如果上次实验存在且与本次实验id不一致，提醒用户删除上一次实验容器
              if (response.data.data.lastExperimentId != 0
                && response.data.data.lastExperimentId != this.dialogData.id) {
                console.log("开始删除上一次容器");
                this.dialogData.isDelete = true;
                deleteExCloudware(studentId,response.data.data.lastExperimentId).then(response=>{
                  // console.log(response);
                  if(response.data=="alreadyResetPassword"){
                    this.$message({
                      type:'warning',
                      message:'请重新登录'
                    });
                    return;
                  }
                  if (response.data.errorCode == 0) {
                    console.log("删除之前启动的容器成功");
                  } else {
                    this.$message({
                      title:'提示',
                      message:"删除上次创建的容器失败",
                      type:"success",
                      center:true
                    });
                  }
                  this.createNewNoteBookInfo(studentId, cloudware_type, type,experimentId);

                });
              } else {
                //如果上次实验存在且与本次实验id一致或没做过实验，直接开启
                //拿取该实验之前启动的容器的连接信息
                if(response.data.data.lastExperimentId == this.dialogData.id){
                  console.log("-----开启上次创建的容器-----");
                  //获取容器信息
                  getStudentExperimentCloudware(this.dialogData.id,JSON.parse(localStorage["user"]).userId).then(res=>{
                    if(res.data=="alreadyResetPassword"){
                      this.$message({
                        type:'warning',
                        message:'请重新登录'
                      });
                      return;
                    }
                    if(res.data.errorCode==0){
                      let url = edu_url+"/startEx?host="+rancherHost+"&port="+rancherPort+"&path="+res.data.data.webSocket+"&time="+new Date().getTime();
                      //let url = rancherHost+":8889/tree";
                      this.$refs.experiment.innerHTML="<iframe src='"+url+"' style='width: 100%;height: 100%'>";
                      try{
                        callbackobjectforjs.changenormalcourseexperimentstatus(true);
                      } catch (err){}
                      this.loading = false;
                      //this.start(res.data.data.webSocket, this.$refs.experiment);
                    }
                  });

                } else {
                  console.log("-----创建新容器了-----");
                  this.createNewNoteBookInfo(studentId, cloudware_type, type,experimentId);
                }
              }
            }
          });
        }
      },
      _getWebSocketUrl:function(secret,cloudware_type,user_id,experimentId){
        // var params = new URLSearchParams();
        // params.append("secret",secret);
        // params.append("cloudware_type",cloudware_type);
        // params.append("user_id",user_id);
        // params.append("experimentId",experimentId);
        // axios.defaults.headers = {
        //   withCredentials: true,
        //   Authorization:'Bearer '+(localStorage['token'] ? localStorage['token'] : '')
        // };
        // console.log(localStorage['token']);
        // return axios.post(edu_url+"/services",params);
        return getWebSocketUrl(secret,cloudware_type,user_id,experimentId);
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
              var webideServiceUrl = res.data.data.ws;
              var webideServiceId = res.data.data.service_id;
              var webideServiceName = res.data.data.service_name;
              webideUrl = webideBaseUrl+userId+"_"+this.dialogData.id+"/?wsUrl="+webideServiceUrl;
              this.$refs.experiment.innerHTML="<iframe src='"+webideUrl+"' style='width: 100%;height: 100%;min-height: 500px;' allowfullscreen></iframe>";
              // experimentId,pulsarId,serviceId,serviceName,studentId,webSocket
              createExCloudWare(this.dialogData.id,res.data.data.pulsar_id,webideServiceId,webideServiceName,userId,webideUrl);
            }
          }
        });
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
              if(response.status==200){
                if(response.data=="alreadyResetPassword"){
                  this.$message({
                    type:'warning',
                    message:'请重新登录'
                  });
                  return;
                }
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
      createCloudware:function(studentId, cloudware_type, type,experimentId){
        this.dialogData.isStart = false;
        this.dialogData.isDelete = false;
        //判断是否存在该实验的容器,如果存在，那么直接使用上一次的容器
        this._getWebSocketUrl('eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmb28iOiJiYXIiLCJpYXQiOjE1MDU4MTM0NTd9.Ftw1yHeUrqdNvymFZcIpuEoS0RHBFZqu4MfUZON9Zm0',cloudware_type,studentId,experimentId).then(resp=>{
          console.log("我正在获取请求");
          if(resp.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          console.log(resp);
          if (resp.data.errorCode == 0) {
            switch (type) {
              case 1:
                //保存到数据库
                // experimentId,pulsarId,serviceId,serviceName,studentId,webSocket
                //createExCloudWare(this.dialogData.id,resp.data.data.pulsar_id,resp.data.data.service_id,resp.data.data.service_name,studentId,resp.data.data.ws);
                break;
              default:
                break;
            }
          } else {
            //发生异常，提示信息
            this.$message({
              type:'error',
              message:'开始实验失败，请稍后重试'
            });
            return;
          }
          //this.start(resp.data.data.ws, this.$refs.experiment);
          let url = edu_url+"/startEx?host="+rancherHost+"&port="+rancherPort+"&path="+resp.data.data.ws+"&time="+new Date().getTime();
          this.$refs.experiment.innerHTML="<iframe src='"+url+"' style='width: 100%;height: 100%;'>";

          try{
            callbackobjectforjs.changenormalcourseexperimentstatus(true);
          } catch (err){}
          this.loading = false;
        });
        return;
      },
      deleteExCloudwareFromService:function(secret,serviceName,serviceId){
        var params = {
          secret:secret,
          serviceName:serviceName,
          serviceId:serviceId
        };
        axios.defaults.headers = {
          withCredentials: true,
          Authorization:'Bearer '+(localStorage['token'] ? localStorage['token'] : '')
        };
        return axios.post("http://api.cloudwarehub.com/homeworks",params);
      },
      startService:function(studentId,cloudware_type,type,experimentId){
        if(type == 1) {
          //this.createCloudware(studentId, cloudware_type, type);
          // 获取学生上一次该课程的实验
          getLastExperiment(studentId).then(response=>{
            console.log("我正在获取上一次实验");
            if(response.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            if (response.data.errorCode == 0) {
              //如果上次实验存在且与本次实验id不一致，提醒用户删除上一次实验容器
              if (response.data.data.lastExperimentId != 0
              && response.data.data.lastExperimentId != this.dialogData.id) {
                console.log("开始删除上一次容器");
                this.dialogData.isDelete = true;
                deleteExCloudware(studentId,response.data.data.lastExperimentId).then(response=>{
                  console.log(response);
                  if(response.data=="alreadyResetPassword"){
                    this.$message({
                      type:'warning',
                      message:'请重新登录'
                    });
                    return;
                  }
                  if (response.data.errorCode == 0) {
                    console.log("删除之前启动的容器成功");
                  } else {
                    this.$message({
                      title:'提示',
                      message:"删除上次创建的容器失败",
                      type:"success",
                      center:true
                    });
                  }
                  if (this.timer != null) {
                    clearTimeout(this.timer);
                  }

                  this.createCloudware(studentId, cloudware_type, type,experimentId);

                });
              } else {
                //如果上次实验存在且与本次实验id一致或没做过实验，直接开启
                //拿取该实验之前启动的容器的连接信息
                if(response.data.data.lastExperimentId == this.dialogData.id){
                  console.log("-----开启上次创建的容器-----");
                  //获取容器信息
                  getStudentExperimentCloudware(this.dialogData.id,JSON.parse(localStorage["user"]).userId).then(res=>{
                    if(res.data=="alreadyResetPassword"){
                      this.$message({
                        type:'warning',
                        message:'请重新登录'
                      });
                      return;
                    }
                    if(res.data.errorCode==0){
                      console.log("第一次播放成功")
                      let url = edu_url+"/startEx?host="+rancherHost+"&port="+rancherPort+"&path="+res.data.data.webSocket+"&time="+new Date().getTime();
                      this.$refs.experiment.innerHTML="<iframe src='"+url+"' style='width: 100%;height: 100%'>";
                      try{
                        callbackobjectforjs.changenormalcourseexperimentstatus(true);
                      } catch (err){}
                      this.loading = false;
                      //this.start(res.data.data.webSocket, this.$refs.experiment);
                    }
                  });

                } else {
                  console.log("-----创建新容器了-----");
                  this.createCloudware(studentId, cloudware_type, type,experimentId);
                }
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
      submitUpload:function(){
        //上传
        this.$refs.upload.submit();
      },
      sureUpdate(){
        console.log(this.bianjiForm);
        var updateForm = {};
        Object.assign(updateForm,this.bianjiForm);
        updateForm.cloudwareType = updateForm.cloudwareTypeId;
        updateExperiment(updateForm).then(res=>{
          if(res.data.errorCode==0){
            this.getExperimentData();
          }
        })
      },
      getExperimentData:function(){
        //获取实验数据
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
            if(data.errorCode==0){
              //操作成功
              this.courseName = data.data.courseName;
              this.tableData = data.data.moduleList;
            }
          }
        });
      },
      _getExpirementInfo:function(){
        try {
          this.$refs.experiment.innerHTML = "";
        } catch (err){
          console.log(err);
        }
        var experimentId = this.$route.params.experimentId;
        var user = JSON.parse(localStorage.user);
        if(user.role == 3){
          this.showTips = false;
          //在这里面置灰
          this.Disgray=true
          // this.isLeftlargeScreen().stop()
          // this.largeScreen().stop()
          // 方法禁用
          this.largeScreen=this.cancelLarge
          this.isLeftlargeScreen=this.cancelLarge
          // this.grayTrue= !this.grayTrue
        }
        //获取该实验相关信息
        getExperimentById(experimentId).then(res=>{
          console.log("获取实验相关信息");
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          if(res.data.errorCode==0){
            this.bianjiForm = res.data.data;
            this.dialogData = res.data.data;
            if(res.data.data.cloudwareType=="shu_lie_yun"){
              this.leftShow=false
              //当前页面类型为数猎云，左边内容铺满
              document.getElementsByClassName("m-left")[0].style.display="block"
              document.getElementsByClassName("m-right")[0].style.display="none"
              document.getElementsByClassName("handle")[0].style.display="none"
              document.getElementsByClassName("m-left")[0].style.width="100%"
              // console.log(document.getElementById("menu").getElementsByTagName("li")[0].style.width)
              document.getElementById("menu").getElementsByTagName("li")[0].style.width="15%"
              document.getElementById("menu").getElementsByTagName("li")[1].style.width="15%"

            }else{
              //当前页面类型为其他，左边内容为40%
              document.getElementsByClassName("m-left")[0].style.width="40%"
              document.getElementsByClassName("m-left")[0].style.display="block"
              document.getElementsByClassName("m-right")[0].style.display="block"
              document.getElementsByClassName("handle")[0].style.display="block"
              // var windowsWidth=document.body.clientWidth
              document.getElementsByClassName("m-right")[0].style.width="59%"
            }
          }
        })
      },
      spliceScreen: function(){

      }
    },
    created:function(){
    },
    beforeRouteEnter: (to, from, next) => {
      console.log("我是路由，我进入了");
      next(vm => {
        vm._getExpirementInfo();
      });
    }


  }
  $(function() {
    var DragChangeSize = {
      init: function() {
        var clickX, leftOffset, inxdex, nextW2, nextW;
        window.dragging = false;
        window.doc = document;
        window.dragBtn = $(".handle");
        // 查看实验宽度
        window.wrapWidth = $("#watch").width();
        console.log("实验宽度:"+ window.wrapWidth)
        this.mousedown();
        this.onmousemove();
        this.mouseup();

      },
      mousedown: function() {
        var _this = this;
        dragBtn.mousedown(function(event) {
          dragging = true;
          // 当前偏移
          window.leftOffset = $("#watch").offset().left;
          // 拖动条
          window.index = $(this).index('label');
          // console.log("window.leftOffset为:"+window.leftOffset)
        });
      },
      onmousemove: function() {
        $(doc).mousemove(function(e) {
          if (dragging) {
            //dragBtn.eq(index).prev().text(dragBtn.eq(index).prev().width());
            //dragBtn.eq(index).next().text(dragBtn.eq(idex).next().width());
            // debugger
            //----------------------------------------------------------------
            //e.pageX当前鼠标位置
            window.clickX = e.pageX;
            console.log("clickX为:"+ clickX);
            if(clickX < 350 || clickX > 850){
              return;
            }
            //判断拖动的第几个按钮
            if (index == 0) {
              //第一个拖动按钮左边不出界
              if (clickX > leftOffset) {
                console.log("clickX > leftOffset");
                console.log("clickX-----为:"+(
                  (clickX/window.wrapWidth*100))
                );
                console.log("watch宽度为:"+typeof (window.wrapWidth));
                // (((parseFloat(clickX/window.wrapWidth))*100)
                console.log("clickX百分比为:"+ typeof (leftOffset/window.wrapWidth*100));
                // dragBtn.eq(index).css('left', clickX - 7 - leftOffset + 'px');
                dragBtn.eq(index).css(
                  'left',  (
                    ((clickX/window.wrapWidth*100)-(leftOffset/window.wrapWidth*100) -0.8 + "%")
                  )
                );
                //前一个按钮移动
                // dragBtn.eq(index).prev().width(clickX - leftOffset - 5 + 'px');
                dragBtn.eq(index).prev().width(
                  // clickX - leftOffset - 5 + 'px'
                  (clickX/window.wrapWidth*100)-(leftOffset/window.wrapWidth*100) - 0.6 + "%"
                );
                // 后一个按钮移动
                // window.nextW2 = clickX - leftOffset + 5;
                window.nextW2 = ((clickX/window.wrapWidth*100)-(leftOffset/window.wrapWidth*100) +0.6 + "%")
                // console.log("wrapWidth为:"+wrapWidth)
                // console.log("nextW2为:"+nextW2)
                // 第一个按钮
                dragBtn.eq(index).next().width(100 - parseFloat(nextW2) +"%") ;
                // dragBtn.eq(index).next().width(wrapWidth - nextW2) ;
                // dragBtn.eq(index).next().width(wrapWidth - nextW2);
              } else {
                dragBtn.eq(index).css('left', '0px');
              }
            }

            // if (clickX > (leftOffset + wrapWidth - 5)) {
            if (clickX > (leftOffset + wrapWidth - 5)) {
              //第一个按钮右边不出界
              dragBtn.eq(index).css('left', parseFloat((wrapWidth - 11) + 'px'));
              //第一个按钮，左右容器不出界
              dragBtn.eq(index).prev().width(dragBtn.eq(index).offset().left - leftOffset + 'px');

              // dragBtn.eq(index).prev().children(".ex")
              dragBtn.eq(index).next().width('0px');

            }
          }
        });

      },
      mouseup: function() {
        $(doc).mouseup(function(e) {
          dragging = false;
          e.cancelBubble = true; //禁止事件冒泡
        })
      }
    };
    DragChangeSize.init();
    // 窗口调整
    $(window).resize(function() {
      // 左边宽度
      var Mleft=$(".m-left").width()
      // console.log("左边宽度" + Mleft)
      // 屏幕宽度
      var WindowWidth=$(window).width()
      // 右边宽度
      var Mright=$(".m-right").width()
      Mright = 100 - (Mleft/WindowWidth)*100 +"%"
      // console.log("右边宽度:"+Mright)
    });

  })




  // $(function() {
  //   var DragChangeSize = {
  //     init: function() {
  //       var clickX, leftOffset, inxdex, nextW2, nextW;
  //       window.dragging = false;
  //       window.doc = document;
  //       window.dragBtn = $(".handle");
  //       // 查看实验宽度
  //       window.wrapWidth = $("#watch").width();
  //       this.mousedown();
  //       this.onmousemove();
  //       this.mouseup();
  //
  //     },
  //     mousedown: function() {
  //       var _this = this;
  //       dragBtn.mousedown(function(event) {
  //         dragging = true;
  //         window.leftOffset = $("#watch").offset().left;
  //         window.index = $(this).index('label');
  //         console.log("window.leftOffset为:"+window.leftOffset)
  //       });
  //     },
  //     onmousemove: function() {
  //       $(doc).mousemove(function(e) {
  //         if (dragging) {
  //           //dragBtn.eq(index).prev().text(dragBtn.eq(index).prev().width());
  //           //dragBtn.eq(index).next().text(dragBtn.eq(index).next().width());
  //           //----------------------------------------------------------------
  //           window.clickX = e.pageX;
  //           console.log("clickX为:"+clickX);
  //           if(clickX < 350 || clickX > 850){
  //             return;
  //           }
  //           //判断拖动的第几个按钮
  //           if (index == 0) {
  //             //第一个拖动按钮左边不出界
  //             if (clickX > leftOffset) {
  //               console.log("clickX > leftOffset");
  //               console.log("clickX为:"+clickX);
  //               dragBtn.eq(index).css('left', clickX - 7 - leftOffset + 'px');
  //               //按钮移动
  //               dragBtn.eq(index).prev().width(clickX - leftOffset - 5 + 'px');
  //               window.nextW2 = clickX - leftOffset + 5;
  //               dragBtn.eq(index).next().width(wrapWidth - nextW2);
  //               console.log("左边的宽度为:"+$(".m-left").width())
  //               console.log("右边的宽度为:"+$(".m-right").width())
  //             } else {
  //               dragBtn.eq(index).css('left', '0px');
  //             }
  //           }
  //
  //           if (clickX > (leftOffset + wrapWidth - 5)) {
  //             //第一个按钮右边不出界
  //             dragBtn.eq(index).css('left', parseFloat((wrapWidth - 11) + 'px'));
  //             //第一个按钮，左右容器不出界
  //             dragBtn.eq(index).prev().width(dragBtn.eq(index).offset().left - leftOffset + 'px');
  //             dragBtn.eq(index).next().width('0px');
  //           }
  //         }
  //       });
  //
  //     },
  //     mouseup: function() {
  //       $(doc).mouseup(function(e) {
  //         dragging = false;
  //         e.cancelBubble = true; //禁止事件冒泡
  //       })
  //     }
  //   };
  //   DragChangeSize.init();
  //
  //   // 窗口调整
  //   $(window).resize(function() {
  //     // var Mleft=document.getElementsByClassName("m-left")[0];
  //     // console.log("左边的宽度为:"+Mleft.style.width)
  //     // var Mright=document.getElementsByClassName("m-right")[0];
  //     // console.log("右边的宽度为:"+Mright.style.width)
  //     // var windoWidth=document.body.clientWidth
  //     // if(parseFloat(Mleft.style.width)<100){
  //     //   Mright.style.width=100 - parseFloat(Mleft.style.width)-0.2 + "%"
  //     // }
  //     // else{
  //     //   Mleft.style.width=((parseFloat(Mleft.style.width)/windoWidth)*100) +"%"
  //     //   // Mleft.style.width = ((parseFloat(Mleft.style.width)/windoWidth))*100 + "%"
  //     //   //
  //     //   Mright.style.width = 100 - parseFloat(Mleft.style.width)-0.2+ "%"
  //     //   // console.log("右边宽度:"+ (100 - parseFloat(mLeft.style.width)))
  //     // }
  //
  //   });
  // })
</script >




<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  .handle
    height 100%
    position absolute
    display block
    width 0px
    top 0
    left 40%
    cursor: e-resize;
    z-index 999
    background-color #e5e5e5
  body
    /*background-color #f8f8f8*/
    #watch
      position absolute
      /*white-space pre*/
      //left 2%px
      width 100%
      /*border 1px solid #e5e5e5*/
      height 100%
      /*border 1px solid red*/
      /*border 1px solid red*/
      .m-left
        //float left
        /*width 500px !important*/
        /*width 40%*/
        margin-left 0px
        border-right 2px solid #e5e5e5
        height  100%
        overflow hidden
        float left
        display block
        position relative
        .leftLarge
          position absolute
          width 25px
          height 25px
          right 0px
          top 0px
        h1
          font-size 26px
          margin-bottom 20px
          margin-top 20px
          margin-left 15px
        #menuul
          min-width 280px
          color #010101
          font-size 13px
          line-height 30px
          text-align center
          display flex
          margin-bottom 20px
         // border 1px solid red
          /*border 1px solid red*/
          .menuli1
            min-width 120px
            width 200px
            height 30px
            background url("../../../static/images/title-left.png")
            background-size 100% 100%
            margin-right 5px
            margin-left 15px
            border none
            p
             padding 0px 3px 0px 3px
             overflow hidden
             text-overflow ellipsis
             white-space pre
             word-wrap normal !important
             word-break normal !important
             /*border 1px solid red*/
          .menuli2
            min-width 120px
            width 200px
            height 30px
            background url("../../../static/images/title-right.png")
            background-size 100% 100%
            border none
            p
             padding 0px 0px 0px 15px
             margin-right 10px
             overflow hidden
             text-overflow ellipsis
             white-space pre
             word-wrap normal !important
             word-break normal !important
        #markeditor
          height calc(100% - 120px)
          margin-top: 0px
          margin-bottom 0px
          border none
          z-index 1
          /*border 1px solid red*/
          /*border 1px solid red*/
             /*border 1px solid red*/
      .m-right
        //float right
        position relative
        /*width 59%*/
        min-height 100%
        /*border 1px solid blue*/
        float right
        right 0px
        //margin-right 1%
        height 100%
        font-size 14px
        background-color #D8D6D6
        overflow auto
        .prompt
          position absolute
          width 40%
          height 20%
          left 30%
          top 40%
          //border 1px solid red
          text-align center
          h1
            font-size 80px
          p
            font-size 16px
            line-height 50px
        .experiment-part
          width 100%
          height 99%
          /*border 1px solid red*/
          margin 0px auto
          overflow hidden
        #icon
          position absolute
          width 55px
          padding-top 5px
          right 0px
          top 0px
          z-index 999
          font-size 0px
          //border 1px solid red
      .gray-true
        -webkit-filter: grayscale(100%);
        -moz-filter: grayscale(100%);
        -ms-filter: grayscale(100%);
        -o-filter: grayscale(100%);
        filter: grayscale(100%);
        filter: gray;



</style>
