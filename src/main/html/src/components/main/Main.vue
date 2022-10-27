<template>
  <l-earning></l-earning>
</template>

<script type="text/ecmascript-6">
  import learning from "../learning/learning";

  export default {
    data () {
      return {
        activeIndex: '1',
        show: false,
        form: {
          name: '',
          pwd: ''
        },
        formLabelWidth: '120px',
        index:1
      };
    },
    methods: {
      handleSelect (key, keyPath) {
      },
      showdetail() {
        if(this.detailshow = true){
          this.detailshow = false
        }
        else{
          this.detailshow = true
        }
      }
    },
    created:function(){
      // var path = this.$route.fullPath;
      // var param = path.substring(path.lastIndexOf("/"));
      // if(param=="all"){
      //   this.index=2;
      // }
    },
    watch:{
      // '$route':function(){
      //   var path  = this.$route.fullPath;
      //   console.log("我改变了");
      // }
    },
    components: {
      "l-earning": learning
    }

  }
</script>

<style lang="stylus" rel="stylesheet/stylus" type="text/stylus">
  .el-header
    color #333
    text-align center
    line-height 60px
    padding 0 0
    z-index 999
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
  #login
    padding 44px 60px
  .el-dialog--center .el-dialog__body
    padding-bottom 0px
    border-radius 10px
  .dialog-footer .login-btn
    width 98%
    padding-top 18px
    padding-bottom 18px
</style>
<style type="text/css">
  .isactive {
    background: url("../../../static/images/icon/menu-active.png") ;
    background-size: cover;
  }
  .el-menu  {
    background: url("../../../static/images/header-backimg.png") bottom;
    background-size: 100% 60px;
  }
  .el-menu-item{
    padding: 0px 32px;
  }
  .el-menu-item a{
    font-size: 16px;
  }
  .el-menu--horizontal > .el-menu-item{
    border-bottom-color: blue;
  }
  .el-menu--horizontal > .el-menu-item:not(.is-disabled):hover, .el-menu--horizontal > .el-menu-item:not(.is-disabled):focus{
    background-color: transparent !important;
  }
  .el-menu-item:hover, .el-menu-item:focus{
    background-color: transparent !important;
  }
</style>
