<template>
  <div>
    <!--头部-->
    <h-eader></h-eader>
    <div class="guanli">
      <div class="menu" id="leftMenuEle">
        <div class="leftMenuEleLi">
          <router-link to="/teacher" :class="activeClassObj.jsClass">教师管理</router-link>
        </div>
        <!--<div class="leftMenuEleLi">
          <router-link to="/xueqi" :class="activeClassObj.xqClass">学期管理</router-link>
        </div>-->
        <div class="leftMenuEleLi">
          <router-link to="/kecheng" :class="activeClassObj.kcClass">课程管理</router-link>
        </div>
        <div class="leftMenuEleLi">
          <router-link to="/banji" :class="activeClassObj.bjClass">班级管理</router-link>
        </div>
        <div class="leftMenuEleLi">
          <router-link to="/student" :class="activeClassObj.studentClass">学生管理</router-link>
        </div>
        <div class="leftMenuEleLi">
          <router-link to="/htSj" :class="activeClassObj.sjClass">数据管理</router-link>
        </div>
        <div class="leftMenuEleLi">
          <router-link to="/license" :class="activeClassObj.licenseClass">授权管理</router-link>
        </div>
      </div >
      <div class="main" id="rightMainWindow">
        <keep-alive>
          <router-view></router-view>
        </keep-alive>
      </div>
      <div style="clear: both"></div>
    </div>
  </div>
</template>


<script type="text/ecmascript-6">
  import header from "../header/header";
  // window.body.onresize('resize',function(){
  //   console.log("wobianle");
  // })
  export default{
    data(){
      return {
        activeClassObj: {
          kcClass: '',
          bjClass: '',
          xqClass: '',
          jsClass: '',
          sjClass: '',
          studentClass:'',
          licenseClass:''
        },
        screenWidth: document.body.clientHeight
      }
    },
    components: {
      "h-eader": header
    },
    methods:{
      route(){
        var path = this.$route.fullPath;
        this.activeClassObj = {}
        if (path.indexOf('/experiment') > -1 || path.indexOf('/exTest') > -1) {
          this.activeClassObj.kcClass = 'active'
        }else if(path.indexOf('/ClassDetail') > -1) {
          this.activeClassObj.bjClass = 'active'
        } else if(path.indexOf('/Addexperiment') > -1 ||
           path.indexOf('/exTest') > -1) {
          this.activeClassObj.kcClass = 'active'
        }
      }
    },
    watch: {
      '$route':function(){
       this.route();
      },
      screenHeight (val) {
        if (!this.timer) {
          this.screenHeight = val
          this.timer = true
          let that = this
          setTimeout(function () {
            // that.screenWidth = that.$store.state.canvasWidth
            console.log(that.screenHeight())
            that.init()
            that.timer = false
          }, 400)
        }
      }
    },
    mounted () {
      const that = this
      window.onresize = () => {
        return (() => {
          window.screenHeight = document.body.clientHeight
          that.screenHeight = window.screenHeight
        })()
      }
    },
    created(){
      this.route();
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  .guanli
    position absolute
    top 44px
    left 0px
    width 100%
    /*bottom 0px*/
    display flex
    min-height 80%
    a
      font-family '宋体' !important
  .menu
      width 160px
      min-height 100%
      background url("../../../static/images/menuLeftBack.png")
      background-size 160px 100%
      position fixed
      left 0px
      top 44px
      .leftMenuEleLi
        width 100%
        height 60px
        line-height 60px
        text-align center
        background-color #cfcfcf
        & > a
          display block
          font-size 16px
          width 100%
          height 60px
          font-weight bold
          color #444545
          font-family "宋体" !important
          border-bottom  1px solid gray
          margin-bottom -20px
          &.active
            width 100%
            height 100%
            border-top  1px solid gray
            /*border-radius 10px*/
            background: linear-gradient(#dbdbdb 70%, #d1d1d3 30%);
            /*background-color rgba(0,26,56,0.4)*/
            /*background url("../../../static/images/menuTitleActive.png")*/
            /*background-size 100% 100%*/
            box-shadow 2px 2px 2px #6e6e6e
            /*border-radius 5px*/
            color #444545
    .main
      width calc(100% - 160px)
      min-height  100%
      background-color #ffffff
      position relative
      left 160px
      top 0px
      ul
        width 100%
</style>
<style type="text/css">
  .el-button--primary{
    /*background-color: #25afa8 !important;*/
    /*border: #25afa8 !important;*/
  }
  /*.el-menu--horizontal > .el-menu-item.is-active{*/
    /*border-bottom:2px solid #ffffff !important;*/
  /*}*/
</style>
