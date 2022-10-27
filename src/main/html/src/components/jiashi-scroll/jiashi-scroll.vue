<template>
  <div>
    <div class="jsTitle">通知</div>
    <div class="scroll-box">
      <div style="padding: 5px 10px">
        <ul>
          <li></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import $ from 'jquery'
  import {getRecentlyLoginUserData} from '@/common/js/request';
  export default {
    data(){
      return {
        loginDataStopTimer: -1
      }
    },
    methods: {
      timingGetLoginData(){
        this.loginDataStopTimer = setInterval(()=>{
          var path = this.$route.path;
          if(path != '/jiashi'){
            window.clearInterval(this.loginDataStopTimer);
            return;
          }
          this.initData()
        },30 * 1000);
      }
      ,
      initData: function(){
        var pageSize = 20;
        getRecentlyLoginUserData(pageSize).then(res=>{
          var data = res.data.data;
          //var lis = $(".scroll-box ul li");
          //if(lis.length > 1){
          $(".scroll-box ul").empty();//清除上一次的
          //}
          for(var i = 0;i < data.length;i++){
            $(".scroll-box ul").prepend('<li>' + data[i] + '</li>');
          }
        })
      },
      realTimeRoll :function(){
        //获得当前<ul>
        var $uList = $(".scroll-box ul");
        var timer = null;
        //触摸清空定时器
        $uList.hover(function() {
            clearInterval(timer);
          },
          function() { //离开启动定时器
            timer = setInterval(function() {
                scrollList($uList);
              },
              1000);
          }).trigger("mouseleave"); //自动触发触摸事件
        //滚动动画
        function scrollList(obj) {
          //获得当前<li>的高度
          var scrollHeight = $("ul li:first").outerHeight();
        /*  console.log('------scrollHeight---------');
          console.log(scrollHeight);
          console.log('----------scrollHeight-------');*/
          //滚动出一个<li>的高度
          $uList.stop().animate({
              marginTop: -36
            },
            600,
            function() {
              //动画结束后，将当前<ul>marginTop置为初始值0状态，再将第一个<li>拼接到末尾。
              $uList.css({
                marginTop: 0
              }).find("li:first").appendTo($uList);
            });
        }
      }

    },
    mounted(){
      this.initData();
      this.timingGetLoginData();
      this.realTimeRoll();
    },
    beforeRouteLeave (to, from, next) {
      window.clearInterval(this.loginDataStopTimer);
      next()
    }

  }





</script>

<style  type="text/css">
  .scroll-box {
    width: 100%;
    height: 300px;
    margin-left: auto;
    margin-bottom: 30px;
    /*border: 2px solid #000;*/
    /*margin: 20px auto;*/
    overflow: hidden;
    /*border 1px solid red*/
    /*border 1px solid red*/
  }

  .scroll-box ul {
    list-style: none;
    width: 100%;
    height: 100%;
  }

  .scroll-box ul li {
    width: 100%;
    height: 36px;
    box-sizing: border-box;
    line-height: 36px;
    text-align: left;
    font-size: 13px;
    /*border 1px solid red*/
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: pre;
  }
 /* #scrollTitle {
    width: 256px
    height: 96px
    background: url("../../../static/images/jiashi-title.png") no-repeat
    background-size: 80% 80%
    line-height 65px
    padding-left 85px
    font-size 14px
  }*/
</style>
