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
                <!--:to="{path: /leardetail/+item.courseId,query: {name: 'learning'}}"-->
                <!--<img src="../../../static/images/course-img1.jpg" class="image">-->
                <router-link :to="{path: /leardetail/+item.courseId,query: {name: 'all'}}" class="image">
                  <img :src="'/common/image/preview?courseId='+item.courseId+'&time='+Math.random()*10000" alt="上传的图片">
                </router-link>
                <div style="background-color: #ffffff">
                  <div class="desc">
                    <div style="padding: 0px 10px">
                      <h2 class="descCourseName">{{item.courseName}}</h2>
                      <p ref="courseDes"><i>{{item.courseDes}}</i></p>
                    </div>
                  </div>
                  <div class="Footer">
                    <div class="f-left">
                      <span><i><img src="../../../static/images/icon/icon-learning2.png" alt=""></i>{{item.actualStudentNum}}人在学习</span>
                    </div>
                    <div class="f-right">
                      <span><img src="../../../static/images/icon/icon-teacher.png" alt=""></span>
                      <span>{{item.teacherName}}</span>
                    </div>
                  </div>
                </div>
              </el-card>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="4" >
            <div style="padding: 10px 10px;">
              <el-card @click.native="showDetail">
                <!--<img src="../../../static/images/course-img1.jpg" class="image">-->
                <div  class="image">
                  <img src="../../../static/images/car-img.png" alt="上传的图片">
                </div>
                <div style="background-color: #ffffff">
                  <div class="desc">
                    <div style="padding: 0px 10px">
                      <h2 class="descCourseName">更多网上课程，请登录数猎云官网！</h2>
                      <p><i>悦岚（上海）数据服务有限公司</i></p>
                    </div>
                  </div>
                  <div class="Footer">
                    <div class="f-left">
                    <span><img src="../../../static/images/icon/icon-learning2.png" alt="">
                    </span>
                      <span>99+人在学习</span>
                    </div>
                    <div class="f-right">
                      <span><i><img src="../../../static/images/icon/icon-teacher.png" alt=""></i>Eurlanda</span>
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
  import {getHotCourse, getSatistics, getStudentCourse} from "@/common/js/request";

  export default {
    data(){
      return{
        tableData:[],
        courseDes:""
      }
    },
    methods:{
      goLink:function(){
        var route=this.$route.path
        this.$router.push({ path :'/main/all/leardetail', query : {route} });
      },
      getAllCourse:function(){
        getHotCourse().then(res=>{
          console.log(res);
          if(res.status==200) {
            if(res.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            this.tableData = res.data.data.courseList;
            this.tableData.forEach((value, index) => {
              // console.log(value.courseDes)
              this.courseDes=value.courseDes
              if(this.courseDes.length>30){
                var str= this.courseDes.substr(0,30)+"..."
                this.courseDes=str
                value.courseDes=this.courseDes
              }else{
                value.courseDes=this.courseDes
              }
            })
          }
        });
      },
      // 更多课程详情
      showDetail(){
        console.log("123")
        callbackobjectforjs.opendatashirecloud();
      }
    },
    components: {
      "h-eader": header
    },
    created:function(){
      //所有课程
      this.getAllCourse();
    },
    activated: function () {
      this.getAllCourse;
    },
    beforeRouteEnter: (to, from, next) => {
      next(vm => {
        vm.getAllCourse;
      });
    }
  }
  // window.onload = function(){
  //   var text = document.getElementById('txt'),
  //     str = this.courseDes
  //   console.log(str)
  //   //   textLeng = 30;
  //   // if(str.length > textLeng ){
  //   //   text .innerHTML = str.substring(0,textLeng )+"...";
  //   // }
  // }
</script>

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
          width 100%
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
          height 90px
          padding 15px 0px
          border-bottom 1px solid rgba(0,0,0,.1)
          background-color #e6f7ff
          .descCourseName
            line-height 22px
            margin 0px auto
            margin-bottom 5px
            color #3d3d3d
            overflow hidden
            text-overflow ellipsis
            white-space pre
            word-wrap normal !important
            word-break normal !important

          p
            font-size 0.9em
            color #979797
            letter-spacing 1px
            height 60px
            line-height 30px
            overflow hidden
            white-space pre-wrap
            /*white-space pre*/
        .Footer
          width 100%
          height 40px
          line-height 40px
          display flex
          background-color #ffffff
          /*text-align center*/
          color #25afa8
          font-size 0px
          margin-top -14px
          img
            width 21px
            height 20px
            vertical-align middle
          .f-left
            width 50%
            height 50px
            display inline-block
            border-right 1px solid rgba(0,0,0,.1)
            font-size 14px
            font-weight bold
            padding 0px  15px
            white-space nowrap
            overflow hidden
            text-overflow ellipsis
          .f-right
            width 50%
            height 50px
            display inline-block
            font-size 13px
            padding 0px 15px
            overflow hidden
            text-overflow ellipsis
            white-space pre
            word-wrap normal !important
            word-break normal !important

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

