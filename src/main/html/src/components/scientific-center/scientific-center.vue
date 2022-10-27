<template>
  <div>
    <!--头部-->
    <h-eader></h-eader>
    <div id="scienCenter">
      <div id="titleHeader">
        <h1 id="title" class="kong breakWord">{{title}}</h1>
        <!--<el-button id="back" @click="backPage">返回</el-button>-->
      </div>
      <div style="clear: both"></div>
      <!--主体部分-->
      <div id="projectTitle">
        <el-radio-group v-model="radio" id="projectRadio">
          <el-radio-button  label="项目主页" @click.native="toggle(1)">
          </el-radio-button>

          <el-radio-button label="项目模型" @click.native.prevent="toggle(2)" v-if="user.role !=3 && isAccess">
          </el-radio-button>

          <el-radio-button label="项目模型" disabled v-if="user.role ==3 || (user.role !=3 && !isAccess)">
          </el-radio-button>

          <el-radio-button label="项目文档" @click.native="toggle(3)">
          </el-radio-button>

          <el-radio-button label="项目数据" @click.native="toggle(4)">
          </el-radio-button>
        </el-radio-group>
        <el-dropdown trigger="click" style="float: right" v-show="(user.userId == projectData.managerUser.userId) && isShow">
          <el-button  type="primary">
            项目管理
          </el-button>
          <el-dropdown-menu slot="dropdown" style="width: 117px;">
            <el-dropdown-item v-show="user.role !=3" @click.native="projectManager(1)">项目信息</el-dropdown-item>
            <el-dropdown-item v-show="user.role !=3" @click.native="projectManager(2)">项目成员</el-dropdown-item>
            <el-dropdown-item @click.native="deleteProject">删除项目</el-dropdown-item>
            <el-dropdown-item @click.native="startSync">开启同步</el-dropdown-item>
            <el-dropdown-item @click.native="SyncOperate">同步操作</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div id="projectMain">
        <keep-alive>
          <router-view></router-view>
        </keep-alive>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import header from "../header/header";
  import SockJS from 'sockjs-client';
  import Stomp from '@stomp/stompjs';
  import {getProjectById,deleteProject,isProjectUser} from '@/common/js/request';
  export default {
        data(){
          return {
            radio: '',
            projectId:0,
            projectData:null,
            title:null,
            user:null,
            isAccess:false,
            isShow:true,
            stompClient:null
          }
        },
        components: {
          "h-eader": header
        },
        created() {
          this.route()
        },
        methods:{
          startSync(){
            this.webSocketConnection();
          },
          SyncOperate(){
            if(this.user.role==2){
              //我是老师，我发送同步操作给学生
              var params = JSON.stringify({
                message:'订阅接受同步消息',
                courseId:1,
                userId:this.user.userId,
                role:this.user.role,
                page:1
              });
              this.stompClient.send("/app/subscribe/teacher/1/operate",{},params);
            }
          },
          webSocketConnection : function(){
            var user = JSON.parse(localStorage["user"]);
            var course = 1;
            var sock = new SockJS('http://192.168.137.56:8080/webSocket');
            // 获取 STOMP 子协议的客户端对象
            var stompClient = Stomp.over(sock);
            this.stompClient = stompClient;
            // 向服务器发起websocket连接并发送CONNECT帧
            stompClient.connect(
              {},
              function connectCallback(frame) {
                // 连接成功时（服务器响应 CONNECTED 帧）的回调方法
                console.log('连接成功')
                var params = JSON.stringify({
                  message:'订阅接受同步消息',
                  courseId:course,
                  userId:user.userId,
                  role:user.role
                })
                if(user.role==2) {
                  //我是老师用户，订阅接受学生同步消息
                  stompClient.subscribe('/app/subscribe/getAlreadySynced', function (response) {
                    console.log("已成功订阅老师获取学生同步消息");
                    console.log('接收到的消息为：')
                    console.log(response);
                  },params);
                  //我是老师用户，发送消息，测试是否可以接受学生的同步消息
                  stompClient.send("/app/subscribe/teacher/1", {}, params);
                } else if(user.role==1){
                  //我是学生，我订阅，老师同步操作
                  stompClient.subscribe("/app/topic/subscribe/student/SyncOperate",function(res){
                    console.log("我已成功订阅，同步老师操作");
                    console.log("接受到的消息为:");
                    console.log(res);
                  },params)

                  //我是学生，我连接成功了，我通知老师，我允许同步操作
                  stompClient.send("/app/subscribe/student/1", {}, params);
                }

              },
              function errorCallBack(error) {
                // 连接失败时（服务器响应 ERROR 帧）的回调方法
                console.log('连接失败');
                console.log(error);

              }
            );

          },
          toggle(num){
            console.log(this.projectId);
            // 切换页面
            if(num==1){
              this.radio='项目主页';
              // this.$router.push('/scientificMain');
              this.$router.push({path: '/scientificMain', query:{projectId: this.projectId}});
            }else if(num==2){
              this.radio='项目模型';
              // this.$router.push('/scientificModel');
              //this.$router.push({path: '/scientificModel', query:{projectId: this.projectId}});
              try {
                //console.log("------项目模型------");
                //console.log(this.projectData.name);
                callbackobjectforjs.openprojectmodel(this.projectData.name);
              } catch (e){}
            }else if(num==3){
              this.radio='项目文档';
              // this.$router.push('/scientificFile')
              this.$router.push({path: '/scientificFile', query:{projectId: this.projectId}});
            }else if(num==4){
              this.radio='项目数据';
              // this.$router.push('/scientificData');
              console.log(this.projectData);
              var managerNo = -1;
              if(this.projectData.managerUser.role == 2){
                managerNo = this.projectData.managerUser.tno;
              }else if(this.projectData.managerUser.role == 1){
                managerNo = this.projectData.managerUser.sno;
              }
              var path = '/scientificData/'+ this.projectId + "/" + managerNo + "/" + this.projectData.name;
              this.$router.push(path);
            }
          },
          webSocketClient(){
            /*var user = JSON.parse(localStorage["user"]);
            var ws = new WebSocket("ws://localhost:8080/websocket/"+user.userId);
            ws.onopen = function(){
              console.log("链接成功");
            };

            ws.onmessage = function(evt){
              console.log(evt);
              console.log("数据已接受")
            };

            ws.onclose=function(){
              console.log("链接已关闭");
            }
            this.ws = ws;*/
          },
          projectManager(type){
            if(type==1){
              this.$router.push({path:'/scientificInfor',query:{projectId:this.projectId}});
            } else if(type==2){
              this.$router.push({path:'/scientificMember',query:{projectId:this.projectId}});
            }

          },
          _getProjectById(projectId){
            getProjectById(projectId).then(res=>{
              console.log("-------项目信息-------");
              console.log(res);
              if(res.data.errorCode==0){
                if(res.data.data.managerUser == null){
                  res.data.data.managerUser = {
                    user_id:0
                  }
                }
                this.projectData = res.data.data;
                this.title = this.projectData.id+"/"+this.projectData.name+"/"+(this.projectData.status==1 ? '进行中' : '已完成')
              } else if(res.data.errorCode==115){
                this.$message({
                  type:'error',
                  message:'该项目不存在'
                });
                if(this.user.role==3) {
                  this.$router.push("/scientificManager");
                } else {
                  this.$router.push("/scientificMyProject");
                }

              }
            })
          },
          _isProjectUser(){
            if(this.user.role!=3) {
              isProjectUser(this.projectId, this.user.userId).then(res => {
                if(res.status==200){
                  if(res.data.data==1){
                    this.isAccess=true;
                  }else{
                    this.isAccess=false;
                  }
                }
              })
            }
          },
          route(){
            var route = this.$route.path;
            console.log(route)
            if(route=="/scientificMain"){
              this.radio='项目主页';
              this.isShow = true;
            }else if(route=="/scientificModel"){
              this.radio='项目模型';
              this.isShow = false;
            }else if(route=="/scientificFile"){
              this.radio='项目文档';
              this.isShow = false;
            } else if(route.indexOf("/scientificData") != -1){
              this.radio='项目数据';
              this.isShow = false;
            } else if(route=="/scientificInfor" || route=="/scientificMember") {
              this.radio='';
            }
          },
          // 删除项目
          deleteProject(){
            this.$prompt('请注意！该项目将从系统被删除，所有项目资料将一并清除，是否确定执行此操作？', '删除项目', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              inputType:'password',
              inputPlaceholder:'请输入登录密码',
              // inputPattern:/\S/,
              inputPattern:/.+/,
              inputErrorMessage:'登录密码不允许为空',
            }).then(({ value }) => {
              var user = JSON.parse(localStorage['user']);
              if(user.role!=3 && user.userId != this.projectData.managerUser.userId){
                this.$message({
                  type: 'error',
                  message:'您没有权限操作删除该项目'
                });
                return;
              }
              if(user.pwd!=value){
                this.$message({
                  type: 'error',
                  message:"密码不正确"
                });
                return;
              }
              //开始删除项目
              deleteProject(this.projectId).then(res=>{
                console.log("-------删除项目-------");
                if(res.data.errorCode==0){
                  this.$message({
                    type:'success',
                    message:'删除成功'
                  });
                  //返回我的项目
                  if(user.role==3) {
                    this.$router.push("/scientificManager");
                  } else {
                    this.$router.push("/scientificMyProject");
                  }
                } else if(res.data.errorCode==115){
                  this.$message({
                    type:'error',
                    message:'该项目不存在'
                  });
                  //返回我的项目
                  if(user.role==3) {
                    this.$router.push("/scientificManager");
                  } else {
                    this.$router.push("/scientificMyProject");
                  }
                } else {
                  this.$message({
                    type:'error',
                    message:'删除失败，请稍后重试'
                  });
                }
              })

            }).catch(() => {
              this.$message({
                type: 'info',
                message: '取消输入'
              });
            });
          },
          // 返回上一页
          // backPage(){
          //   this.$router.push({path: "/scientificMyProject" , query : {name : 'scientificMyProject'}})
          // }
        },
        watch:{
          '$route':function(){
            this.route()
          }
        },
        beforeRouteEnter:(to, from, next) => {
          next(vm => {
            var user = JSON.parse(localStorage["user"]);
            vm.user = user;
            var projectId = vm.$route.query.projectId;
            vm.projectId = projectId;
            vm._isProjectUser();
            //获取我的项目
            vm._getProjectById(projectId);
            vm.webSocketConnection()
            //vm.webSocketClient();
          });
        }

    }
</script>

<style lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  #scienCenter
    margin-top 60px
    #titleHeader
      margin 20px 3% 20px
      #title
        font-size 22px
        line-height 40px
        font-family  "Microsoft YaHei"
        float left
      #back
        float right
    #projectTitle, #projectMain
      margin 10px 3% 0px
    #projectMain
      border 1px solid rgb(220, 223, 230)
      padding 5px
      margin-top 0px
      #projectRadio
        max-width 400px
</style>
