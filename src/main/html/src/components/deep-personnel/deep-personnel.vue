<template>
    <div>
      <!--头部-->
      <h-eader></h-eader>
      <div id="img">
        <img src="../../../static/images/deepPersonelImg.png" width="434" height="334" alt="">
      </div>
    </div>
</template>

<script type="text/ecmascript-6">
  import header from "../header/header";
  export default {
    data(){
      return{

      }
    },
    components: {
      "h-eader": header
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus">
  #img
    position absolute
    width 434px
    height 334px
    top 50%
    left 50%
    margin-top -167px
    margin-left -217px
</style>
