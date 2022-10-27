<template>
  <div>
    <div id="jiashi">
      <el-row :gutter="10">
        <el-col :md="24" :lg="24" :xl="24">
          <div class="grid-content" id="jsMain">
            <js-main></js-main>
          </div>
        </el-col>
      </el-row>
      <div style="padding: 10px 30px">
        <el-row :gutter="10">
          <!--平台-->
          <el-col :md="9" :lg="9" :xl="9">
            <div class="grid-content" id="pingTai">
              <ping-tai></ping-tai>
            </div>
          </el-col>
          <!--<el-col :md="8" :lg="8" :xl="8"><div class="grid-content" id="jsMain">-->
          <!--<js-main></js-main>-->
          <!--</div></el-col>-->
          <!--spark作业统计-->
          <el-col :md="9" :lg="9" :xl="9">
            <div class="grid-content" id="jsSpark">
              <js-spark></js-spark>
            </div>
          </el-col>
          <!--通知-->
          <el-col :md="6" :lg="6" :xl="6" >
            <div class="grid-content" id="jsScroll">
              <js-scroll></js-scroll>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <!--deep指数-->
          <el-col :md="9" :lg="9" :xl="9">
            <div class="grid-content" id="jsDeep">
              <js-deep></js-deep>
            </div>
          </el-col>
          <!--实时在线-->
          <el-col :md="9" :lg="9" :xl="9">
            <div class="grid-content" id="jsShiShi">
              <js-shishi></js-shishi>
            </div>
          </el-col>
          <!--课程内容-->
          <el-col :md="6" :lg="6" :xl="6" >
            <div class="grid-content" id="jsCourse">
              <js-course></js-course>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <!--考试成绩统计-->
          <el-col :md="9" :lg="9" :xl="9">
            <div class="grid-content" id="jsExam">
              <js-exam></js-exam>
            </div>
          </el-col>
          <!--平均时长-->
          <el-col :md="9" :lg="9" :xl="9">
            <div class="grid-content" id="jsAverage">
              <js-averare></js-averare>
            </div>
          </el-col>
          <!--实验环境数量-->
          <el-col :md="6" :lg="6" :xl="6" >
            <div class="grid-content" id="jsEnvironment">
              <js-environment></js-environment>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import pingTai from "../jiashi-pingtai/jiashi-pingtai"// 平台硬件状态
  import jsMain from "../jiashi-main/jiashi-main"// 标题
  import jiashiSpark from "../jiashi-spark/jiashi-spark"// Spark作业统计
  import jiashiDeep from "../jiashi-deep/jiashi-deep"// deep指数/科研项目
  import jiashiShiShi from "../jiashi-shishi/jiashi-shishi"// 实时在线人数/累计使用时长1
  import jiashiScroll from "../jiashi-scroll/jiashi-scroll"//  实时滚动窗口
  import jiashiExam from "../jiashi-exam/jiashi-exam"// 考试成绩
  import jiashiAverage from "../jiashi-average/jiashi-average"// 平均时长
  import jiashiCourse from "../jiashi-course/jiashi-course"// 课程内容
  import jiashiEnvironment from "../jiashi-environment/jiashi-environment"// 环境保护
export default {
   data(){
     return {
       activeClassObj: {
         jiashi: ''
       },
     }
   },
    components: {
      "ping-tai": pingTai,
      "js-main": jsMain,
      "js-spark": jiashiSpark,
      "js-deep": jiashiDeep,
      "js-shishi": jiashiShiShi,
      "js-scroll": jiashiScroll,
      "js-exam": jiashiExam,
      "js-averare": jiashiAverage,
      "js-course": jiashiCourse,
      "js-environment": jiashiEnvironment
    },
  created(){
    //  console.log("该页面为:"+this.$route.fullPath)
    // var path = this.$route.fullPath;
    // this.activeClassObj = {}
    // if (path.indexOf('/jiashi') > -1) {
    //
    // }

  }

}
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus">
  #jiashi
    position absolute
    top 0px
    left 0px
    width 100%
    min-height 100%
    background:url("../../../static/images/jiashiBack.jpg")  no-repeat
    background-size 100% 100%
    background-position 40%
    font-family "微软雅黑"
    overflow-x hidden
    .jsTitle
      width 35%
      height 50px
      /*text-align center*/
      color #040c56
      font-size 15px
      padding-left 10px
    #jsMain
      width 100%
      height 70px
      /*border 1px solid red*/
    #pingTai, #jsSpark, #jsScroll
      height 370px
      color #ffffff
      background url("../../../static/images/jsModularBack.png") no-repeat
      background-size 100% 100%
      border-bottom 20px
      .jsTitle
        line-height 50px
    #jsDeep, #jsShiShi, #jsExam, #jsAverage, #jsCourse, #jsEnvironment
      min-height 280px
      color #ffffff
      background url("../../../static/images/jsModularBack.png") no-repeat
      background-size 100% 100%
      margin-bottom 10px
      /*border 1px solid red*/
      .jsTitle
        line-height 40px
</style>
