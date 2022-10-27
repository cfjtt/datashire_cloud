<template>
    <div id="hunting">

      <!--文件管理-->
      <div id="top">
        <el-radio-group v-model="radio">
          <el-radio-button label="文件管理" @click.native="toggle(1)">
            <!--<router-link to="/share" >-->
            <!--</router-link>-->
          </el-radio-button>

          <el-radio-button label="数据库管理" @click.native="toggle(2)">
            <!--<router-link to="/hunting" >-->
            <!--</router-link>-->
          </el-radio-button>
        </el-radio-group>
      </div>
      <div id="xia">
        <keep-alive>
          <router-view></router-view>
        </keep-alive>
      </div>
    </div>
</template>

<script type="text/ecmascript-6">
  export default {
    data(){
      return{
        radio:''
      }
    },
    methods:{
      toggle(num){
        if(num==1){
          this.radio='文件管理';
          this.$router.push('/file');
        }else if(num==2){
          this.radio='数据库管理'
          this.$router.push('/Hdata');
        }
        console.log(this.radio);
      },
      route(){
        var route = this.$route.path
        console.log(route)
        if(route=="/file"){
          this.radio='文件管理';
        }else{
          this.radio='数据库管理';
        }
      }
    },
    created(){
      this.route()
    },
    watch:{
      '$route':function() {
        this.route()
      }
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus">
#hunting
  /*border orangered*/
  border 1px solid rgb(220, 223, 230)
  #top
    width 100%
    margin 20px auto 0px
    & >a
      color #000000
  #xia
    width 100%
    margin 0px auto 0px
    min-height  200px
    /*border 1px solid red*/
</style>
