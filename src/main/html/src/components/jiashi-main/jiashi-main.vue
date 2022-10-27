<template>
  <div id="jsTitle">
    <div id="jTMain">
      <div id="jsMLogo">
        <img src="../../../static/images/jsLogo.png" width="200" height="50" alt="logo">
      </div>
      <div id="jsMMain">数据科学与工程产学研综合平台</div>
      <ul id="jTmList">
        <li>{{week}}</li>
        <li>{{year_month_date}}</li>
        <li>{{hour_min}}</li>
        <li @click="back()" v-if="isshow">返回</li>
      </ul>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import {getSystemTime,getCustomers}  from '@/common/js/request';

  export default {
    data(){
      return {
        week: '',
        year_month_date: '',
        hour_min: '',
        stopTimer : -1,
        isshow:true
      }
    },
    methods: {

      timingGetTime: function(){
        this.stopTimer = setInterval(()=>{
          var path = this.$route.path;
          if(path != '/jiashi'){
            window.clearInterval(this.stopTimer);
            return;
          }
          getSystemTime().then(res=>{
            var data = res.data.data;
            this.week = data.week;
            this.year_month_date = data.nYr_hMs;
          });
        },1000);
      },
      getTime: function () {
        var todayDate = new Date();
        var date = todayDate.getDate();
        var month = todayDate.getMonth() + 1;
        var year = todayDate.getYear();
        //console.log(year);

        var hours = todayDate.getHours();
        var min = todayDate.getMinutes();
        var second = todayDate.getSeconds();
        this.hour_min = hours + ':' + min + ':' + second;
        var dateweek = "";
        if (navigator.appName == "Netscape") {
          year = (1900 + year) ;
        }
        if (navigator.appVersion.indexOf("MSIE") != -1) {
          year = dateweek + year ;
        }
        this.year_month_date = year + '-' + month + '-' + date;
        switch (todayDate.getDay()) {
          case 0:
            dateweek += "星期日";
            break;
          case 1:
            dateweek += "星期一";
            break;
          case 2:
            dateweek += "星期二";
            break;
          case 3:
            dateweek += "星期三";
            break;
          case 4:
            dateweek += "星期四";
            break;
          case 5:
            dateweek += "星期五";
            break;
          case 6:
            dateweek += "星期六";
            break;
        }
        this.week = dateweek;
      },

      back(){
        // window.history.go(-1)
        this.$router.push("/all")
      }
    },
    mounted(){
      this.timingGetTime();
      getCustomers().then(res => {
        console.log("登录成功errorcode为:"+res.data.errorCode)
        //如果没有登录
        if(res.data.errorCode==46){
          // this.$message({
          //   type:'warning',
          //   message:'请重新登录'
          // });
          this.isshow=false
          //登录成功
        }else if(res.data.errorCode==0){
          this.isshow=true
        }
      })
    },
    beforeRouteLeave (to, from, next) {
      window.clearInterval(this.stopTimer);
      next()
      // var queryCanShu = this.$route.query.jiashiPath;
      console.log("传递的参数为:"+this.$route.name)
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  #jsTitle
    width 100%
    height 100%
    background url("../../../static/images/jiashiTitle.jpg") no-repeat
    background-size 100% 100%
    #jTMain
      padding 0px 30px
      /*display flex*/
      position relative
      #jsMLogo
        width 200px
        height 70px
        float left
        img
          vertical-align middle
          margin-top 10px
      #jsMMain
        width 555px
        height 70px
        background url("../../../static/images/jsTopTitle.png") repeat
        background-size 100% 70px
        line-height 70px
        font-family "微软雅黑"
        font-weight bold
        font-size 32px
        color #ffffff
        text-align center
        margin:0px auto;
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
      #jTmList
        float right
        display flex
        li
          color #4ecbff
          line-height 70px
          padding 0px 10px
</style>
