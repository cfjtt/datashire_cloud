<template>
<div>
  <el-dialog title="登录" id="login"
             :visible.sync="show"
             width="35%"
             center>
    <el-form :model="user">
      <el-form-item style="position: relative">
        <el-input v-model="user.name" placeholder="请输入用户名" auto-complete="off"></el-input>
        <i class=" el-icon--right" style="position: absolute;right: 13px;z-index: 999;top: 10px;">
          <img src="../../../static/images/icon/icon-login-gray.png" alt="">
        </i>
      </el-form-item>
      <el-form-item style="position: relative">
        <el-input v-model="user.pwd" placeholder="请输入密码" auto-complete="off" ></el-input>
        <i class="el-icon--right" style="position: absolute;right: 13px;z-index: 999;top: 10px;">
          <img src="../../../static/images/icon/icon-pwd.png" alt="">
        </i>
      </el-form-item>
      <p style="float: right;margin-bottom: 20px">
        <a href="">忘记密码？</a>
      </p>
    </el-form>
    <div slot="footer" class="dialog-footer" >
      <el-button type="primary" class="login-btn" @click="_login">登录</el-button>
    </div>
  </el-dialog>
</div>


  <!--测试页面链接-->
</template>

<script>
    import {login,getNotifications,modifyPass,getHotCourse,getSatistics} from '@/common/js/request';
    export default {
      data(){
          return {
            user:{},
            show:false
          }
      },
      methods:{
        loadHomeWork:function(role){
          if(role==1){
            //获取学生作业列表
          }

        },
        _login:function(){
          //发起请求(并且将用户的信息，保存到localStorage中)
          login(this.user.username,this.user.password).then(res=>{
            console.log(res);
            if(res.status==200){
              let data = res.data;
              if(data.errorCode==37){
                // alert("用户名或密码错误");
                this.message.$error="用户名或密码错误";
                return;
              }
              callbackobjectforjs.logoncallback(this.user.username,this.user.password);
              // 登录成功
              let userId = data.data.body.userId;
              let token = data.data.body.token;
              let userRole = data.data.body.role;
              this.user.id = userId;
              this.user.token = token;
              this.user.role = userRole;
              //拿到用户之后，
              let params;
              switch(userRole){
                case 1:
                  params={
                    userId: userId,
                    role: userRole,
                  };
                  alert("我是学生,userId:"+userId+",role:1，我拥有的权限:我的课程，我的文件夹");
                  // 教师管理  发起请求
                  break;
                case 2:
                  params={
                    userId: userId,
                    role: userRole
                  };
                  alert("我是老师,userId:"+userId+",role:2,我拥有的权限:我的课程，我的文件夹");
                  break;
                case 3:
                  params = {
                    userId: userId,
                    role: userRole //0:student;1:teacher;2:administrator
                  };
                  alert("我是管理员,userId:"+userId+",role:3,我拥有的权限:教师管理，学期管理，课程管理，班级管理");
                  break;
              }
              //index.startExperiment.cloudware' 实现页面路由（如果当前的页面是实验页面，那么点击登录之后，还需要到实验页面）
              //将user对象转换成字符串，存储到localStorage中
              localStorage.setItem("user",JSON.stringify(params));
              localStorage.setItem("token",this.user.token);
              this.$router.push("/all");
            }
          })
        },
        logout:function(){
          //这里的登出只是将localStorage清除掉，然后对应的js对象清除掉，没有请求到后台，因为后台不存储session
          //清除localStorage
          localStorage.removeItem("user");
          localStorage.clear();
          //将相关组件里面的动态绑定的数据给清空
          this.user={};
          //路由链接到课程列表(获取作业列表，热门课程，统计信息等)
          this.$route.push("/main");
        },
        _modifyPass:function(){
          //修改密码
          /*modifyPass(userId,oldPassword,newPassword,confirmPassword).then(res=>{
            console.log(res);
          })*/
        },
        _getNotifications:function(){
          //获取作业列表,这里都改为从localStorage中拿取数据
          if(this.user.role!=3){
              getNotifications(this.user.id,this.user.role).then(res=>{
                console.log(res);
              })
          }
        },
        _getHotCourse:function(){
          //热门课程(未登录)
          getHotCourse().then(res=>{
            console.log(res);
          })
        },
        //获取网站统计信息,包含学生，课程，实验，老师数量
        _getSatistics:function(){
          getSatistics().then(res=>{
            console.log(res);
          });
         }


      }
    }
</script>

<style scoped>

</style>
