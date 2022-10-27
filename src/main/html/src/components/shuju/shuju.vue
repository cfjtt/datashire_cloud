<template>
  <div>
    <!--头部-->
    <h-eader></h-eader>
    <div id="shuju">
      <div id="title">
        <el-radio-group v-model="radio">
          <el-radio-button  label="共享文件夹管理" @click.native="toggle(1)">
            <!--<router-link to="/share" >-->
            <!--</router-link>-->
          </el-radio-button>

          <el-radio-button label="数猎云实验数据管理" @click.native="toggle(2)">
            <!--<router-link to="/hunting" >-->
            <!--</router-link>-->
          </el-radio-button>
        </el-radio-group>

      </div>
      <div id="main">
        <keep-alive>
          <router-view></router-view>
        </keep-alive>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import header from "../header/header";
  export default {
    data(){
      return{
        radio: ''
      }
    },
    components: {
      "h-eader": header
    },
    methods:{
      toggle(num){
        if(num==1){
          console.log("aaaa");
          this.radio='共享文件夹管理';
          this.$router.push('/share');
        }else if(num==2){
          this.radio='数猎云实验数据管理';
          this.$router.push('/file');
        }
        console.log(this.radio);
      },
      route(){
        var route = this.$route.path
        console.log(route)
        if(route=="/share"){
          this.radio='共享文件夹管理';
        }else{
          this.radio='数猎云实验数据管理';
        }
      }
    },
    created(){
     this.route()
    },
    watch:{
      '$route':function(){
       this.route()
      }
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
#shuju
  position relative
  top 60px
  #title
    width 96%
    margin 20px auto 0px
    position relative
    top 0px
    /*border 1px solid red*/
    & >a
      color #000000
  #main
    width 96%
    margin-left 2%
    /*border 1px solid blue*/
    /*border 1px solid red*/
</style>
<style type="text/css">
</style>
