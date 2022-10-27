<template>
  <div>
    <!--头部-->
    <h-eader></h-eader>
    <div id="scienCenter">
      <div style="clear: both"></div>
      <!--主体部分-->
      <div id="projectTitle">
        <el-radio-group v-model="radio" id="projectRadio">
          <el-radio-button  label="我的项目" @click.native="toggle(1)">
          </el-radio-button>

          <el-radio-button label="公开项目" @click.native="toggle(2)">
          </el-radio-button>

        </el-radio-group>
      </div>
      <div id="projectMain" style=" border:1px solid #dcdfe6">
        <keep-alive>
          <router-view></router-view>
        </keep-alive>
      </div>
      <!--删除项目-->

    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import header from "../header/header";

  export default {
    data(){
      return {
        radio: ''
      }
    },
    components: {
      "h-eader": header
    },
    created() {
      this.route()
    },
    methods:{
      toggle(num){
        if(num==1){
          this.radio='我的项目';
          // this.$router.push('/scientificMain');
          this.$router.push({path: '/scientificMyProject', query:{name: 'scientificMyProject'}});
        }else if(num==2){
          this.radio='公开项目';
          // this.$router.push('/scientificModel');
          this.$router.push({path: '/scientificAllProject', query:{name: 'scientificAllProject'}});
        }
      },
      route(){
        var route = this.$route.path
        console.log(route)
        if(route=="/scientificMyProject"){
          this.radio='我的项目';
        }else if(route=="/scientificAllProject"){
          this.radio='公开项目';
        }
      }
    },
    watch:{
      '$route':function(){
        this.route()
      }
    }

  }
</script>

<style lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  #scienCenter
    margin-top 60px
    #titleHeader
      margin 20px 3% 0px
      #title
        font-size 22px
        font-family  "Microsoft YaHei"
        float left
      #back
        float right
    #projectTitle
      margin 10px 3% 0px
      #projectRadio
        max-width 400px
    #projectMain
      margin 0px 3% 0px
</style>
