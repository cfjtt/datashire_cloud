<template>
  <el-container>
    <el-header id="header">
      <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        text-color="#010101"
         router>
        <el-menu-item index="/learning" v-show="isLogin && (isStudent || isTeacher)" style="border-bottom-color: #888886">
          <a href="#" onclick="return false" v-show="isStudent" >我的课程</a>
          <a href="#" onclick="return false" v-show="isTeacher" >我的课程</a>
        </el-menu-item>
        <el-menu-item index="/learning" disabled v-show="!isLogin" style="border-bottom-color: #888886">
          <a href="#" onclick="return false" >我的课程</a>
        </el-menu-item>
        <el-menu-item index="/all" style="border-bottom-color: #888886">
          <a href="#" onclick="return false" >所有课程</a>
        </el-menu-item>
        <el-menu-item index="/data" v-show="isLogin && isManager" style="border-bottom-color: #888886">
          <a href="#" onclick="return false" >后台管理</a>
        </el-menu-item>
        <el-menu-item index="/jiashi" v-show="isLogin && isManager" style="border-bottom-color: #888886;border-right-color:  #888886">
          <a href="#"  @click="jiashi" >驾驶舱</a>
        </el-menu-item>
        <el-menu-item index="/share" v-show="isLogin && (isStudent || isTeacher)" style="border-bottom-color: #888886">
          <a href="#" onclick="return false" >我的数据</a>
        </el-menu-item>
        <el-menu-item index="/share" disabled v-show="!isLogin" style="border-bottom-color: #888886">
          <a href="#" onclick="return false" >我的数据</a>
        </el-menu-item>
        <el-menu-item index="/classManage" v-show="isLogin && (isTeacher)" style="border-bottom-color: #888886">
          <a href="#" onclick="return false" >班级管理</a>
        </el-menu-item>
        <el-menu-item index="/deepIndex"  style="border-bottom-color: #888886">

          <el-dropdown style="width: 142px;">
            <a href="#" onclick="return false" style="display: inline-block;width: 100%">DEEP市场</a>
            <el-dropdown-menu slot="dropdown" style="width: 143px;margin-top: -5px;text-align: center;height: 180px;">
              <el-dropdown-item >
                <router-link to="/deepIndex" >数据交易</router-link>
              </el-dropdown-item>
              <el-dropdown-item >
                <router-link :to="{path: '/deepModel', query:{name: 'deepModel'}}" >模型交易</router-link>
                </el-dropdown-item>
              <el-dropdown-item>
                <!--deepProject-->
                <router-link :to="{path: '/deepProject', query:{name: 'deepProject'}}" >项目交易</router-link>
                </el-dropdown-item>
              <el-dropdown-item>
                <!--deepCourse-->
                <router-link :to="{path: '/deepCourse', query:{name: 'deepCourse'}}" >课程交易</router-link>
                </el-dropdown-item>
              <el-dropdown-item>
                <!--deepPersonnel-->
                <router-link :to="{path: '/deepPersonnel', query:{name: 'deepPersonnel'}}" >人才交易</router-link>
                </el-dropdown-item>

            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>
        <el-menu-item index="/scientificMyProject" v-show="isLogin && (isStudent || isTeacher)" style="border-bottom-color: #888886">
          <a href="#" onclick="return false" >科研中心</a>
        </el-menu-item>
        <el-menu-item index="/scientificManager" v-show="isLogin && (isManager)" style="border-bottom-color: #888886">
          <a href="#" onclick="return false" >科研中心</a>
        </el-menu-item>
      </el-menu>
      <div class="menuBack">
        <div style="float: right;margin-right: 2%" v-show="!isLogin">
          <div class="name">
            <span class="el-dropdown-link" style="color: #ffffff" @click="show = true">
              <!--<img src="../../../static/images/icon/icon-login.png" alt="登录图标" class="icon">-->
              <span style="color: #ffffff;cursor: pointer;font-size: 18px;font-family: '黑体'">登录</span>
            </span>
          </div>
        </div>
        <div style="float: right;margin-right: 2%" v-show="isLogin">
          <div class="name hasLogin">
            <el-dropdown trigger="click">
              <span class="el-dropdown-link" style="color: #c2cbc8" >
                <!--<img src="../../../static/images/icon/icon-login.png" alt="登录图标" class="icon">-->
                <span style="color: #ffffff;cursor: pointer;font-size: 18px;font-family: '黑体'">{{username}}</span>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="updatePwd()">修改密码</el-dropdown-item>
                <el-dropdown-item @click.native="logout(1)">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
        <div id="headerTitle">数据科学与工程产学研综合平台</div>
      </div>
    </el-header>
    <!--登录框-->
    <el-dialog title="登录" id="login"
               :visible.sync="show"
               width="35%"
               center>
      <el-form :model="form">
        <el-form-item style="position: relative">
          <el-input v-model="form.name"
                    placeholder="请输入用户名"
                    auto-complete="off"
                    @keydown.native="loginKeyDown"></el-input>
          <i class=" el-icon--right" style="position: absolute;right: 13px;z-index: 999;top: 16px;">
            <img src="../../../static/images/icon/icon-login-gray.png" alt="">
          </i>
        </el-form-item>
        <el-form-item style="position: relative">
          <el-input type="password" v-model="form.pwd"
                    placeholder="请输入密码"
                    auto-complete="off"
                    @keydown.native="loginKeyDown"
                    ></el-input>
          <i class="el-icon--right" style="position: absolute;right: 13px;z-index: 999;top: 16px;">
            <img src="../../../static/images/icon/icon-pwd.png" alt="">
          </i>
        </el-form-item>
        <!--<p style="float: right;margin-bottom: 20px">-->
          <!--<a href="">忘记密码？</a>-->
        <!--</p>-->
      </el-form>
      <div slot="footer" class="dialog-footer" style="margin-top: -20px;padding-bottom: 10px">
        <el-button  class="login-btn button-green" @click="login" >登录</el-button>
      </div>
    </el-dialog>
    <!--修改密码弹出层-->
    <el-dialog title="修改密码" id="updatePwd"
               :visible.sync="updateViseble"
               width="35%"
               center>
      <el-form :model="updateForm">
        <el-form-item style="position: relative">
          <el-input type="password" v-model="updateForm.oldpwd" placeholder="请输入旧密码" maxlength="20" auto-complete="off" ></el-input>
          <i class="el-icon--right" style="position: absolute;right: 13px;z-index: 999;top: 10px;">
            <img src="../../../static/images/icon/icon-pwd.png" alt="">
          </i>
        </el-form-item>
        <el-form-item style="position: relative">
          <el-input type="password" v-model="updateForm.newPwd" placeholder="请输入新密码" maxlength="20" auto-complete="off" ></el-input>
          <i class="el-icon--right" style="position: absolute;right: 13px;z-index: 999;top: 10px;">
            <img src="../../../static/images/icon/icon-pwd.png" alt="">
          </i>
        </el-form-item>
        <el-form-item style="position: relative">
          <el-input type="password" v-model="updateForm.aginPwd" placeholder="请再次输入新密码" maxlength="20" auto-complete="off" ></el-input>
          <i class="el-icon--right" style="position: absolute;right: 13px;z-index: 999;top: 10px;">
            <img src="../../../static/images/icon/icon-pwd.png" alt="">
          </i>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" >
        <!--<el-button type="primary" class="login-btn" @click="pushJiaShi()">进入驾驶舱</el-button>-->
        <el-button  class="login-btn button-green" @click="update()">确认修改</el-button>
      </div>
    </el-dialog>



  </el-container>
</template>

<script type="text/ecmascript-6">
  import axios from 'axios';
  import SockJS from 'sockjs-client';
  import Stomp from '@stomp/stompjs';
  import learning from "../learning/learning";
  import {
    adminAndTeacgerPassword,
    adminAndTeacherUsername,
    login,
    logoutCheck,
    modifyPass,
    recordUserOfflineData,
    recordUserOnlineData,
    setToken
  } from "@/common/js/request";
  import {isLogin, isManager, isStudent, isTeacher} from "@/common/js/commons";

  export default {
    data () {
      return {
        activeIndex: '/all',
        show: false,
        updateViseble: false, // 修改密码
        form: {
          name: '',
          pwd: ''
        },
        updateForm: [
          {
            oldpwd:"",
            newPwd:"",
            aginPwd:""
          }
        ],
        userId : 0,
        formLabelWidth: '120px',
        isLogin: false,
        username: '',
        isStudent: false,
        isTeacher: false,
        isManager: false,
        stopTimer: -1,
        isOffline: false
      };
    },
    methods: {
      addInterceptors: function(){
        //拦截所有的请求
        axios.interceptors.request.use(config => {
          // element ui Loading方法
         /* console.log('拦截执行request-------');
          console.log(config);
          console.log('拦截执行request-------');*/
          return config
        }, error => {
          // console.log('request error!');
          return Promise.reject(error)
        });
      // 拦截所有的响应
        axios.interceptors.response.use(data => {
          // console.log('拦截------');
          // console.log(data);
          // console.log('拦截------');

          if(typeof(data) == "undefined"){
            // console.log("response数据为null");
            return "";
          }
          var emptyData = {
            data:{
              data:null,
              errorCode:-1,
              message:'请重新登录！'
            }
          };
          if(data.data.errorCode == 90){
            //被T下线
            // console.log('拦截执行response-------');
            // console.log(data);
            this.$alert(data.data.message, '提示', {
              confirmButtonText: '确定',
              center: true,
              type: 'warning',
              callback: action => {

              }
            });
            this.logout(2);
            return emptyData;
          }else if(data.data.errorCode == 103){
            //重置密码
            //console.log('拦截执行到服务器其响应被管理员重置密码');
            //console.log(data);
            if(!this.isLogin){
              //console.log('已退出登录');
              return data;
            }
            this.logout(1);
            //被管理员重置密码
            this.$alert(data.data.message, '提示', {
              confirmButtonText: '确定',
              center: true,
              type: 'warning',
              callback: action => {

              }
            });

            return emptyData;
          }else if(data.data.errorCode == 45){
            //token过期
            if(!this.isLogin){
              return data;
            }
            this.logout(2);
            //被管理员重置密码
            this.$alert(data.data.message, '提示', {
              confirmButtonText: '确定',
              center: true,
              type: 'warning',
              callback: action => {

              }
            });

            return emptyData;
          }else if(data.data.errorCode == 46){
            //请登录
            if(!this.isLogin){
              return data;
            }
            this.logout(2);
            //被管理员重置密码
            this.$alert(data.data.message, '提示', {
              confirmButtonText: '确定',
              center: true,
              type: 'warning',
              callback: action => {

              }
            });

            return emptyData;
          }
          return data
        }, error => {
          //loadinginstace.close()
          // console.log('response error!');
          return Promise.reject(error)
        });

      },
      // pushJiaShi(){
      //   this.$router.push({path: "/jiashi"})
      //   // let routeData = this.$router.resolve({path: '/jiashi'});
      //   // window.open(routeData.href, '_blank');
      // },
      jiashi(){
        // var jiashiPath=this.$route.path
        // alert(jiashiPath)
        this.$router.push({path: "/jiashi" , query : {jiashiPath : 1}})
      },
      _initRole: function () {
        this.isLogin = isLogin()
        this.isStudent = isStudent()
        this.isTeacher = isTeacher()
        this.isManager = isManager()
      },
      showDialog: function() {
        this.show = true;
      },
      handleSelect (key, keyPath) {
//        this.activeIndex = key + '';
//        this.$router.push({path: this.activeIndex})
      },
      showdetail () {
        if (this.detailshow = true) {
          this.detailshow = false
        }
        else{
          this.detailshow = true
        }
      },
      login: function () {
        this.show = false;
        //console.log(this.isOffline);
        login(this.form.name,this.form.pwd,this.isOffline).then(res=>{
          //console.log(res)
          if(res.status==200){
            let data = res.data;
            if(data.errorCode==37){
              // alert("用户名或密码错误");
              this.$message({
                message: '用户名或密码错误',
                type: 'error'
              });
              this.form.pwd=""
              return;
            }else if(data.errorCode == 86){
              //系统未激活
              this.$message({
                message: data.message,
                type: 'error'
              });
              this.form.pwd="";
              return;
            }else if(data.errorCode == 87){
              //系统已过期
              this.$message({
                message: data.message,
                type: 'error'
              });
              this.form.pwd="";
              return;
            }else if(data.errorCode == 88){
              if(this.isLogin){
                //如果是登录状态，则退出登录
                this.logout(1);
              }
              //同时在线人数已达上限
              this.$message({
                message: data.message,
                type: 'error'
              });
              this.form.pwd="";
              return;
            }else if(data.errorCode == 92){
              //重复登录但不T除已经登录的用户
              /*this.$message({
                message: data.message,
                type: 'error'
              });*/
              return;
            }else if(data.errorCode == 89){
              //重复登录
              this.$confirm(data.message, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                center: true
              }).then(() => {
                this.isOffline = true;
                this.login();
              }).catch(() => {

                this.isOffline = false;
                this.form.pwd="";
              });

              return;
            }else if(data.errorCode == 103){
              //console.log('登录返回被管理员重置密码');
              //被管理员重置密码
              this.$alert(data.message, '提示', {
                confirmButtonText: '确定',
                center: true,
                type: 'warning',
                callback: action => {

                }
              });
              return;
            }else if(data.errorCode == 2147483647){
              this.$message({
                message: data.message,
                type: 'success'
              });
              return;
            }else if(data.errorCode == -1){
              //console.log("errorCode为-1");
              return;
            }
            this.$message({
              message: '恭喜你，登录成功',
              type: 'success'
            });

            // console.log(data);
            this.userId = data.data.body.userId;

            // 登录成功
            let userId = data.data.body.userId;
            let token = data.data.body.token;
            let userRole = data.data.body.role;
            this.form.id = userId;
            this.form.token = token;
            this.form.role = userRole;
            //拿到用户之后，
            let params;
            switch (userRole) {
              case 1:
                params = {
                  userId: userId,
                  role: userRole,
                  username: this.form.name,
                  pwd: this.form.pwd
                };
                try {
                  // console.log(this.form.id);
                  callbackobjectforjs.logoncallback(this.form.name, this.form.pwd,this.form.id);
                } catch (err) {}
                // 教师管理  发起请求
                break;
              case 2:
                params={
                  userId: userId,
                  role: userRole,
                  username: this.form.name,
                  pwd: this.form.pwd
                };

                try {
                  // console.log(this.form.id);
                  callbackobjectforjs.logoncallback(this.form.name, this.form.pwd,this.form.id);
                } catch (err) {}
                break;
              case 3:
                params = {
                  userId: userId,
                  username: this.form.name,
                  role: userRole, //0:student;1:teacher;2:administrator
                  pwd: this.form.pwd
                };
                try {
                  // console.log(this.form.id);
                  callbackobjectforjs.logoncallback(adminAndTeacherUsername,adminAndTeacgerPassword,this.form.id);
                } catch (err) {}
                break;
            }
            //index.startExperiment.cloudware' 实现页面路由（如果当前的页面是实验页面，那么点击登录之后，还需要到实验页面）
            //将user对象转换成字符串，存储到localStorage中
            localStorage.setItem('user',JSON.stringify(params));
            localStorage.setItem('token',this.form.token);
            this.isLogin = true;
            this.username = params.username;
            setToken();
            this._initRole();
            this.heartBeat();
            this.$router.push('/');
            //this.$router.go(0)
            this.activeIndex = "/"

            // console.log('----/----' + this.activeIndex)
            this.activeIndex = "/all";
            this.webSocketConnection();

          }
        })
      },

      webSocketConnection : function(){
        var user = JSON.parse(localStorage["user"]);
        var sock = new SockJS('http://192.168.137.56:8080/webSocket/'+user.userId);
        // 获取 STOMP 子协议的客户端对象
        var stompClient = Stomp.over(sock);
        // 向服务器发起websocket连接并发送CONNECT帧
        stompClient.connect(
          {},
          function connectCallback(frame) {
            // 连接成功时（服务器响应 CONNECTED 帧）的回调方法
            console.log('连接成功')
            /*stompClient.subscribe('/app/subscribeTest', function (response) {
              console.log("已成功订阅/app/subscribeTest");
              console.log('接收到的消息为：')
              console.log(response);
            });*/
            stompClient.send("/app/sendTest", {}, 'hello server');
          },
          function errorCallBack(error) {
            // 连接失败时（服务器响应 ERROR 帧）的回调方法
            console.log('连接失败');
            console.log(error);

          }
        );
        stompClient.subscribe('/topic/subscribeTest', function (response) {
          console.log('订阅')
          console.log(response);
        });
        stompClient.send("/app/sendTest", {}, 'hello server');

      },
      logout: function (type) {
        if(type != 2){
          //发送注销请求，被T出的用户不发送注销请求
          recordUserOfflineData(this.userId,type).then(res=>{
            // console.log('发送注销请求接收到回执');
            // console.log(res);
            this.userId = 0;
          });
        }
        this.isLogin = false;
        //console.log('i am running');
        //console.log(this.isLogin);
        localStorage.removeItem('user');
        try{
          window.clearInterval(localStorage['stopTimer']);
        }catch (e){
          // console.log(e);
        }
        localStorage.clear();
        this.username = '';
        setToken();

        try {
          callbackobjectforjs.logoutcallback();
        } catch (err) {}
        this.$router.push("/");

        this.form={};

      },
      //修改密码
      updatePwd(){
        this.updateForm = {};
        this.updateViseble = true
        // console.log('123')
      },
      update(){
        var user  = JSON.parse(localStorage['user']);
        //校验密码不能为空
        if(typeof this.updateForm.oldpwd == 'undefined' || this.updateForm.oldpwd.trim().length==0){
          this.$message({
            title:"提示",
            message:"旧密码不能为空",
            type:'error'
          });
          return;
        }
        if(typeof this.updateForm.newPwd == 'undefined' || this.updateForm.newPwd.trim().length==0){
          this.$message({
            title:"提示",
            message:"新密码不能为空",
            type:'error'
          });
          return;
        }
        if(typeof this.updateForm.aginPwd == 'undefined' || this.updateForm.aginPwd.trim().length==0){
          this.$message({
            title:"提示",
            message:"再次输入密码不能为空",
            type:'error'
          });
          return;
        }
        modifyPass(user.userId,this.updateForm.oldpwd,this.updateForm.newPwd,this.updateForm.aginPwd).then(res=>{

          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          // console.log(res);
          if(res.data.errorCode!=0){
            this.$message({
              title:"提示",
              message:res.data.message,
              type: 'error'
            });
          } else {
            this.$message({
              title:"提示",
              message:"密码修改成功",
              type: 'success'
            });
            this.updateViseble = false;
            this.logout(1);
          }
        })
      },
  heartBeat: function(){
    var isSend = localStorage['isSend'];
    //console.log(isSend);
    if(!this.isLogin || isSend == "true" ){
        //console.log("取消定时任务。。。。");
        return;
    }
    //开启定时任务一分钟之后才会发送请求，这里是开启定时任务之前就先发送第一次请求
    recordUserOnlineData(JSON.parse(localStorage['user']).userId,'').then(res=>{
      //console.log('----心跳------');
      //console.log(res);
      var errorCode = res.data.errorCode;
      var msg = res.data.message;
      if(errorCode == 90){
        //被T下线
        this.$alert(msg, '提示', {
          confirmButtonText: '确定',
          center: true,
          type: 'warning',
          callback: action => {

          }
        });

        this.form.pwd="";
        this.logout(2);
        return;
      }else if(errorCode == 88){
        //超过同时在线人数
        if(this.isLogin){
          //如果是登录状态，则退出登录
          this.logout(1);
        }
        //同时在线人数已达上限
        this.$alert(msg, '提示', {
          confirmButtonText: '确定',
          center: true,
          type: 'warning',
          callback: action => {

          }
        });
        this.form.pwd="";
        return;
      }else if(errorCode == 103){
        //console.log('心跳请求返回被管理员重置密码');
       // console.log(data);
        if(!this.isLogin){
          //console.log('已退出登录');
          return;
        }
        this.logout(1);
        //被管理员重置密码
        this.$alert(msg, '提示', {
          confirmButtonText: '确定',
          center: true,
          type: 'warning',
          callback: action => {

          }
        });

        return;
      }else if(errorCode == 45){
        //token过期
        if(!this.isLogin){
          return;
        }
        this.logout(2);
        //被管理员重置密码
        this.$alert(msg, '提示', {
          confirmButtonText: '确定',
          center: true,
          type: 'warning',
          callback: action => {

          }
        });

        return;
      }
    });

    localStorage.setItem('isSend',true);
      // console.log('--------开启定时任务 ------ ');
        this.stopTimer = setInterval(()=>{
          // console.log("-----send request!----");
          // console.log(this.stopTimer);
          localStorage.setItem('stopTimer',this.stopTimer);
          var userId = null;
          try{
            userId = JSON.parse(localStorage['user']).userId;
          }catch (e){
            // console.log(e);
            window.clearInterval(this.stopTimer);
            localStorage.removeItem('stopTimer')
            return;
          }
          recordUserOnlineData(userId,'').then(res=>{
            //console.log('----心跳2------');
            //console.log(res);
            var errorCode = res.data.errorCode;
            var msg = res.data.message;
            if(errorCode == 90){
              this.$alert(msg, '提示', {
                confirmButtonText: '确定',
                center: true,
                type: 'warning',
                callback: action => {

                }
              });

              this.form.pwd="";
              this.logout(2);
            }else if(errorCode == 88){
              // console.log("已超出最大同时在线人数")
              if(this.isLogin){
                //如果是登录状态，则退出登录
                this.logout(1);
              }
              //同时在线人数已达上限
              this.$alert(msg, '提示', {
                confirmButtonText: '确定',
                center: true,
                type: 'warning',
                callback: action => {

                }
              });
              this.form.pwd="";
            }else if(errorCode == 103){
              // console.log('拦截执行response-------');
              //console.log('心跳请求返回被管理员重置密码');
              if(!this.isLogin){
                // console.log('已退出登录');
                return;
              }
              this.logout(1);
              //被管理员重置密码
              this.$alert(msg, '提示', {
                confirmButtonText: '确定',
                center: true,
                type: 'warning',
                callback: action => {

                }
              });
            }else if(errorCode == 45){
              //token过期
              if(!this.isLogin){
                return ;
              }
              this.logout(2);
              //被管理员重置密码
              this.$alert(msg, '提示', {
                confirmButtonText: '确定',
                center: true,
                type: 'warning',
                callback: action => {

                }
              });

            }
          });
        },60 * 1000);
      },
      loginKeyDown(){
        //console.log("我按下了")
        var keycode = event.keyCode;
        if(keycode==13) {
          this.login()
        }
        else {
        }
      }
    },
    created:function(){
      this.addInterceptors();
      // console.log("created........");
     // console.log(this.$route.path.indexOf)
      this.activeIndex = this.$route.path
      var route=this.$route.query.name
      // console.log("本页面name为:")
      // console.log(route)
      if (route =="learning" ) {
        this.activeIndex = '/learning'
      } else if (route =="all" ) {
        this.activeIndex = '/all'
      }else if (route =="/classManage" ) {
        this.activeIndex = '/classManage'
      }
      else if(route =="deepModel" || route =="deepProject" || route =="deepCourse" || route =="deepPersonnel"){
        this.activeIndex = '/deepIndex'
      }
      // scientificManager
      else if(route =="scientificMyProject" || route =="scientificAllProject" || this.$route.path=="/scientificMyProject" ){
        this.activeIndex = '/scientificMyProject'
      }
      else if(this.$route.path =="/scientificManager" || route=="/scientificInfor" || route=="/scientificMember" || route=="/scientificupdateFile"){
        this.activeIndex = '/scientificManager'
      }
      else if(route =="scientificMain" || route =="scientificModel" || route =="scientificFile" || route =="scientificData" || this.$route.path=="/scientificMain"){
        this.activeIndex = '/scientificMyProject'
      }
      // scientificCenter
      else if (this.$route.path !== '/learning' && this.$route.path !== '/all' && this.$route.path !== '/share' && this.$route.path !== '/jiashi' && this.$route.path !== '/classManage' && this.$route.path !== '/deepIndex' ) {
        this.activeIndex = '/data'
      }
      // 判断是否登录
      if (localStorage['user']) {
        this.isLogin = true
        this.username = JSON.parse(localStorage['user']).username;
        this.userId = JSON.parse(localStorage['user']).userId;
        this.form.id = JSON.parse(localStorage['user']).userId;
        if(localStorage['isSend'] != 'true'){
          this.heartBeat();
        }
      } else {
        this.isLogin = false
      }
      this._initRole()
      // console.log('--------header created ------ ' + this.activeIndex)

    },
    activated: function () {
      this._initRole();
      // console.log('activated-------');
      // console.log('--------header activated ------ ' + this.activeIndex)
    },
    components: {
      'learning': learning
    }

  }
</script>

<style lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  .el-header
    color #333
    text-align center
    line-height 44px
    padding 0 0
    z-index 999
    position fixed
    top 0px
    left 0px
    width 100%
    height 44px
    display flex
    overflow auto
    /*border 1px solid red*/
    .icon
      width 25px
      height 25px
    .name
      float right
      margin-left 10px
  .detail
    background-color #F8FAFB
    width 30%
    padding 30px 30px 20px 20px
    margin 50px auto
    &.fade-enter-active, &.fade-leave-active
      transition: opacity .5s
    &.fade-enter, &.fade-leave-to /* .fade-leave-active below version 2.1.8 */
      opacity: 0
    el-button
      margin 0px auto
  #login,#updatePwd
    padding 44px 60px
  .el-dialog--center .el-dialog__body
    padding 40px
    border-radius 10px
  .dialog-footer .login-btn
    width 100%
    padding-top 18px
    padding-bottom 18px

  .copyright
    text-align right
    color #b4b4b4
    font-size 14px
    margin-top 60px
    margin-right 2%

  .el-menu-item
    background: linear-gradient(#efefef 70%, #dadada 30%);
    background-size cover
    box-sizing border-box
    border 1px solid #888886
    margin-right:-1px;
    z-index 999
    &
      border-right 1px solid transparent
      border-top 1px solid transparent
    &.is-active
      /*background url("../../../static/images/icon/menu-active.png")*/
      background: linear-gradient(#c8dccd 70%, #bacfc0 30%);
      background-size: cover;
      transition: 2s background;
      /*border-bottom  2px solid #888886*/
    &:hover
      /*background url("../../../static/images/icon/menu-active.png")*/
      background: linear-gradient(#c8dccd 70%, #bacfc0 30%);
      background-size: cover;
      transition: 2s background;
  .el-dialog__footer
    padding 0px 40px 30px
  #header
    width 100%
    height 44px !important
    overflow hidden
    text-overflow ellipsis
    white-space nowrap
    border-bottom 1px solid rgb(136, 136, 134)
    display flex
  .el-menu--horizontal > .el-menu-item
    height 49px
    line-height 49px
    padding 0px
  .el-menu-item
    width 143px
    a
     font-family "宋体" !important
     font-size 18px
     font-weight bold
     color #010101
</style>
<style type="text/css">
  /*#header .is-active {*/
  /*background: url("../../../static/images/icon/menu-active.png") ;*/
  /*background-size: cover;*/
  /*}*/
  /*#header{*/
    /*display: ;*/
  /*}*/
  .el-dialog .el-table--group::after,
  .el-dialog .el-table--border::after,
  .el-dialog .el-table::before{
    background-color: transparent
  }

  #header .isactive {
    background: url("../../../static/images/icon/menu-active.png") ;
    background-size: cover;
  }
  #header .el-menu  {
    z-index: 1;
    /*width: 429px;*/
    height: 100%;
    box-sizing: border-box;
  }
  .menuBack{
    background: url("../../../static/images/headerTitle.png") bottom;
    background-size: 100% 100%;
    flex: 1;
    height: 100%;
    box-sizing: border-box;
  }
  .el-menu-item{
    padding: 0px 32px;
  }
  /*.el-menu-item a{*/
    /*font-size: 16px;*/
  /*}*/
  /*.el-menu--horizontal > .el-menu-item{*/
    /*border-bottom-color: blue;*/
  /*}*/
  .el-collapse-item__header{
    font-size: 16px;
  }
  #login .el-dialog__title{
    font-size: 20px;
    color: #000000;
  }
  #login .el-input__inner{
    height: 50px;
  }
  .el-dialog{
    border-radius: 10px;
  }
  /*登录输入框之间的距离*/
  #login .el-dialog__body{
    border-top: 1px solid #ffffff;
    border-bottom: 1px solid #ffffff;
  }
  .button-green,.el-button--primary{
    background: linear-gradient(#c9dece 70%, #bbd0c0 30%) !important;
    background-size: cover;
    transition: 2s background;
    border: 1px solid transparent;
    color: #494b4f;
  }
  .el-radio-button__orig-radio:checked + .el-radio-button__inner{
    background-color:  #bbd0c0 ;
    background-size: cover;
    transition: 2s background;
    border: 1px solid #bbd0c0;
    box-shadow: -1px 0 0 0 #bbd0c0;
    color: #494b4f;
  }
  .el-radio-button__inner:hover{
    color: #494b4f;
  }
  .button-green:hover, .button-green:focus,.el-button--primary:hover,.el-button--primary:focus {
    color: #494b4f;
    border-color: transparent;
    background: linear-gradient(#c9dece 70%, #bbd0c0 30%) !important; }
  .button-green:active,.el-button--primary:active {
    color: #494b4f;
    border-color: transparent;
    outline: none; }
  .button-green:hover,.el-button--primary:hover{
    color: #494b4f;
  }
  .el-checkbox__input.is-checked .el-checkbox__inner{
    background-color: #bbd0c0;
    border-color: #bbd0c0;
  }
  .el-checkbox__input.is-indeterminate .el-checkbox__inner{
    background-color: #bbd0c0;
    border-color: #bbd0c0;
  }
  .el-checkbox__inner:hover{
    background-color: #bbd0c0;
    border-color: #bbd0c0;
  }
  .el-menu-item.is-disabled{
    opacity: 1;
    background-color: #ffffff !important;
    z-index: 999;
  }
  .el-menu--horizontal > .el-menu-item a, .el-menu--horizontal > .el-menu-item a:hover{
    color: #040404;
  }
  #headerTitle{
    padding: 0px 10px;
    display: inline-block;
    line-height: 50px;
    height: 44px;
    text-align: center;
    color: #c2cbc8;
    font-size: 18px;
    font-weight: bold;
  }
  .el-tabs--border-card > .el-tabs__header .el-tabs__item.is-active{
    color: black;
  }
  .el-table .descending .sort-caret.descending{
    border-top-color: black;
  }
  .el-table .ascending .sort-caret.ascending{
    border-bottom-color:black;
  }
  /*去除校验*必选项*/
  .el-form-item.is-required .el-form-item__label:before{
    content:''
  }
  /*输入框名称*/
  .el-form-item__label{
    white-space:pre-wrap;
    text-align:justify;
    text-justify:distribute-all-lines;
    text-align-last:justify
  }
  /*表格数据居中对齐*/
  .el-table .cell{
    text-align: center;
  }
  /*.el-menu--horizontal > .el-menu-item.is-active{*/
    /*border-bottom: 2px solid red;*/
  /*}*/
  /*.el-button--primary{*/
    /*background-color: #bbd0c0  !important;*/
    /*background-size: cover;*/
    /*transition: 2s background;*/
    /*border-color:  #bbd0c0;*/
    /*color: #494b4f;*/
  /*}*/
</style>
