<template>
  <!--头部-->
  <div>
    <h-eader ></h-eader>
    <div class="allcourses" >
      <div class="all-main">
        <el-row>
          <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="4" v-for="(item,idx) in tableData" :key="idx">
            <div style="padding: 10px 10px;">
              <el-card>
                <!--<img src="../../../static/images/course-img1.jpg" class="image">-->
                <router-link :to="{path: /leardetail/+item.courseId,query: {name: 'learning',className: item.classNamefull,courseId: courseId}}" class="image">
                  <img :src="'/common/image/preview?courseId='+item.courseId+'&time='+Math.random()*10000" alt="">
                </router-link>
                <div style="background-color: #ffffff">
                  <div class="desc">
                    <div style="padding: 0px 15px;">
                      <h2 id="desc_title" ref="descTitle" >{{item.courseName}}/{{item.className}}</h2>
                      <p class="desc_main">
                        <span><img src="../../../static/images/icon/icon-learn-teacher.png" alt=""></span>
                        <i >授课老师:{{item.teacherName}}</i>
                      </p>
                      <p class="desc_main">
                        <span><img src="../../../static/images/icon/icon-learn-phone.png" alt=""></span>
                        <i>联系方式:{{item.teacherContract}}</i>
                      </p>
                    </div>
                  </div>

                </div>
              </el-card>
            </div>
          </el-col>
        </el-row>
      </div>
      <!--版权-->
      <div class="copyright">
        <p>版权信息  版权股所有   悦岚数据服务</p>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import header from "../header/header";
  import { getHotCourse,getSatistics,getStudentCourse,getTeacherCourse } from "@/common/js/request";
  export default {
    data(){
      return{
        tableData:[],
        user:null,
        courseId:'' //课程id
      }
    },
    methods:{
      goLink:function(){
        var route=this.$route.path
        this.$router.push({ path :'/leardetail', query : {route}  });
      },
      getCourse:function(){
        var user = JSON.parse(localStorage["user"]);
       // console.log(user);
        this.user = user;
        if(user.role==1){
          //学生
          getStudentCourse(user.userId).then(res=>{
            if(res.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            if(res.status==200){
              var tableData = res.data.data.studentClassList;

             // console.log(tableData)
              this.tableData = tableData;
              tableData.forEach((value, index) => {
                // {{item.courseName}}/{{item.className}}
                var courseName=value.courseName
                var className=value.className
                value.classNamefull=value.className
                this.courseId=value.courseId
                console.log("实验id为:")
                console.log(this.courseId)
                console.log("-------className：------"+className)
                // var total=value.courseName+"/"+value.className
                // console.log(this.total)
                if(courseName.length>=14){
                  var course=courseName.substr(0,14)+"..."
                  value.courseName=course
                }else{
                  value.courseName=courseName
                }

                if(className.length>=14){
                  var CLASSS=className.substr(0,14)+"..."
                  value.className=CLASSS
                }else{
                  value.className=className
                }
              })
            }
          });
        } else if(user.role==2){
          //老师
          getTeacherCourse(user.userId).then(res=>{
            if(res.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            if(res.status==200){
              var tableData = res.data.data.teacherClassList;
              tableData.forEach((value, index) => {
                // {{item.courseName}}/{{item.className}}
                var courseName=value.courseName
                var className=value.className
                console.log("-------className：------"+className)
                // var total=value.courseName+"/"+value.className
                // console.log(this.total)
                if(courseName.length>=14){
                  var course=courseName.substr(0,14)+"..."
                  value.courseName=course
                }else{
                  value.courseName=courseName
                }

                if(className.length>=14){
                  var CLASSS=className.substr(0,14)+"..."
                  value.className=CLASSS
                }else{
                  value.className=className
                }

              })
              this.tableData = tableData;
            }
          })
        }
      }
    },
    components: {
      "h-eader": header
    },
    created:function(){
      //所有课程
      this.getCourse();
      console.log(this.tableData.length)
      // this.tableData.forEach((value, index) => {
      //    console.log("123")
      // })

    },
    activated: function () {
      this.getCourse();
    },
    beforeRouteEnter: (to, from, next) => {
      next(vm => {
        vm.getCourse;
      });
    }
  }
  // window.onload = function(){
  //   var text = document.getElementById('desc_title').innerHTML;
  //   var  str = text;
  //   console.log(str)
  //   var  textLeng = 30;
  //   if(str.length > textLeng ){
  //     text .innerHTML = str.substring(0,textLeng )+"...";
  //   }
  // }
</script>
<!--<script type="text/javascript">-->
 <!---->
<!--</script>-->

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  .allcourses
    position absolute
    top 35px
    width 100%
    min-height  100%
    /*background:url("../../../static/images/mb-backimg.png") no-repeat*/
    background-color #f0f0f0
    background-size 100% 100%
    .all-main
      width 85% !important
      height auto
      margin 5% auto
      color #ffffff
      .el-row
        .el-col
          margin-bottom 10px
        .image
          width 99%
          height 210px
          background-color #001a38
          img
            width 100%
            display block
            height 210px
            background-color #ffffff
            border-bottom 1px solid rgba(0,0,0,0.1) !important
            /*border  1px solid #00479d*/

        .desc
          width 100%
          height 150px
          border-bottom 1px solid rgba(0,0,0,.1)
          background-color #e6f7ff
          /*border 1px solid red*/
          #desc_title
            height 70px
            line-height 35px
            /*width 90%*/
            margin 0px auto
            color #3d3d3d
            word-break break-all
            overflow hidden
            white-space pre-wrap
            /*border 1px solid red*/
          .desc_main
            /*height 25px*/
            max-height 52px
            line-height 40px
            white-space nowrap
            /*margin 0px 15px*/
            font-size 0.9em
            color #979797
            letter-spacing 0.5px
            overflow hidden
            text-overflow ellipsis
            margin-top -10px
            i
              color #979797
              overflow hidden
              text-overflow ellipsis
              white-space pre
              word-wrap normal !important
              word-break normal !important
              /*border 1px solid red*/
              /*word-break break-all*/
            //border 1px solid red

</style>
<style type="text/css" >
  .el-card{
    background-color: transparent !important;
    border: 1px solid #a0a2a4;
    border-radius: 10px;
  }
  .el-card__body{
    padding: 0px;
    height: 360px;
    border-radius: 10px;
    white-space: normal;
  }
  .copyright{
    color: #b4b4b4;
    position: fixed;
    bottom: 10px;
    right: 2%;
  }

</style>

