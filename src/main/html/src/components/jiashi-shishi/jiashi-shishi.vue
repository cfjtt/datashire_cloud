<template>
    <div id="shishi">
      <div class="jsTitle">实时在线人数/累积时长</div>
      <el-row type="flex" class="row-bg">
        <el-col :span="9"><div class="grid-content" id="shishiOnline"></div></el-col>
        <el-col :span="15"><div class="grid-content " id="shishiUse"></div></el-col>
      </el-row>
    </div>
</template>

<script type="text/ecmascript-6">
  import Vue from 'vue'
  import axios from 'axios'
  import {getRealTimeOnlineData,getAccumulativeOnlineData} from '@/common/js/request';
  export default {
    data() {
      return {
        accumulativeStopTimer : -1,
        realTimeStopTimer : -1
      }
    },
    methods:{

      loadRealTimeCharts: function(){
        getRealTimeOnlineData().then(res=>{
          var data = res.data.data;
          this.shishiOnline(data.timeAxisData,data.yAxisData);
        })
      },

      loadAccumulativeOnlineCharts: function(){
        getAccumulativeOnlineData().then(res=>{
          var data = res.data.data;
          this.shishiUse(data.xAxisData,data.yAxisData);
        })
      },
      shishiOnline(xData,yData){
        let echarts = require('echarts/lib/echarts')
        let line1=document.getElementById('shishiOnline')
        let mainChart = echarts.init(line1)
        var option = null;
        // 指定图表的配置项和数据
        option = {
          title: {
            text: '实时在线人数',
            show:false,
            textStyle: {
              color: "#E6E6E6",
              fontSize: 16
            }
          },
          tooltip: {
            trigger: 'axis',
            axisPointer:{
              type:'shadow'
            }
          },
          grid: {
            left: '15%',
            right: '2%',
            bottom: '8%',
            top: '8%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: xData,
            axisLabel:{
              show: false,
              interval:0
            },
            //坐标线颜色
            axisLine:{
              lineStyle:{
                color: "#6990AD"
              }
            },
            axisTick:{
              show:false
            },
            itemStyle:{
              borderType:"dashed"
            }
          },
          yAxis: {
            type: 'value',
            name:'实时在线人数/人',
            nameGap: '28',
            nameLocation: 'center',
            nameTextStyle:{
              fontSize:10,
              color: "#A9A9A9"
            },
            axisLine:{
              lineStyle:{
                color: "#6990AD"
              }
            },
            splitLine: {
              lineStyle: {
                color: ['#6990AD'],
                type:"dashed",
                width:0.2
              }
            },
            axisLabel:{
              show: true,
              interval:0,
              fontSize:10
            }
          },
          series: [{
            name:'实时在线人数',
            data: yData,
            type: 'line',
            itemStyle:{
              color:'#6990AD'
            }
          }]
        };
        // 使用刚指定的配置项和数据显示图表。
        if (option && typeof option === "object") {
          mainChart.setOption(option, true)
        }
      },
      shishiUse(xData,yData){
        let echarts = require('echarts/lib/echarts')
        let line1=document.getElementById('shishiUse')
        let mainChart = echarts.init(line1)
        var option = null;
        // 指定图表的配置项和数据
        option = {
          title: {
            text: '实时累计时长',
            show:false,
            textStyle: {
              color: "#E6E6E6",
              fontSize: 16
            }
          },
          tooltip: {
            trigger: 'axis',
            axisPointer:{
              type:'shadow'
            }
          },
          grid: {
            left: '5%',
            right: '2%',
            bottom: '8%',
            top: '8%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: xData,
            name:'时间/h',
            nameGap: '20',
            nameTextStyle:{
              fontSize:10,
              color: "#A9A9A9"
            },
            nameLocation: 'center',
            axisLabel:{
              show: true,
              interval:0,
              fontSize:10
            },
            //坐标线颜色
            axisLine:{
              lineStyle:{
                color: "#6990AD"
              }
            },
            axisTick:{
              show:true,
              length:2
            },
            itemStyle:{
              borderType:"dashed"
            }
          },
          yAxis: {
            type: 'value',
            name:'实时累计时长/h',
            nameGap: '24',
            nameLocation: 'center',
            nameTextStyle:{
              fontSize:10,
              color: "#A9A9A9"
            },
            axisLine:{
              lineStyle:{
                color: "#6990AD"
              }
            },
            splitLine: {
              lineStyle: {
                color: ['#6990AD'],
                type:"dashed",
                width:0.2
              }
            },
            axisLabel:{
              show: true,
              interval:0,
              fontSize:10
            }
          },
          series: [{
            data: yData,
            type: 'bar',
            barMaxWidth:'30%',
            name:'实时累计时长',
            itemStyle:{
              color:'#6990AD'
            },
            label:{
              show:true,
              position:'top',
              color:'#ffffff',
              fontSize: 10
            }
          }]
        };
        // 使用刚指定的配置项和数据显示图表。
        if (option && typeof option === "object") {
          mainChart.setOption(option, true)
        }
      },
      timingFlushRealTimeDashboardData: function(){
        this.realTimeStopTimer = setInterval(()=>{
          var path = this.$route.path;
          if(path != '/jiashi'){
            window.clearInterval(this.realTimeStopTimer);
            return;
          }
          this.loadRealTimeCharts();
        },60 * 1000);
      },
      timingFlushAccumulativeDashboardData: function(){
        this.accumulativeStopTimer = setInterval(()=>{
          var path = this.$route.path;
          if(path != '/jiashi'){
            window.clearInterval(this.accumulativeStopTimer);
            return;
          }
          this.loadAccumulativeOnlineCharts();
        },60 * 60 * 1000);
      }

    },
    /*beforeRouteEnter: (to, from, next) => {
      next(vm => {
        vm.loadRealTimeCharts();
        vm.loadAccumulativeOnlineCharts();
        //开启定时器
        vm.timingFlushRealTimeDashboardData();
      });
    },*/
    mounted(){
      /*this.shishiOnline()
      this.shishiUse()*/
      this.loadRealTimeCharts();
      this.loadAccumulativeOnlineCharts();
      this.timingFlushRealTimeDashboardData();
      this.timingFlushAccumulativeDashboardData();
    },
    beforeRouteLeave (to, from, next) {
      window.clearInterval(this.realTimeStopTimer);
      window.clearInterval(this.accumulativeStopTimer);
      next()
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus">
  #shishi
    width 100%
    height 100%
    /*#shishiTitle*/
      /*width: 256px*/
      /*height: 96px*/
      /*background: url("../../../static/images/jiashi-title.png") no-repeat*/
      /*background-size: 80% 80%*/
      /*line-height 65px*/
      /*padding-left 45px*/
      /*font-size 14px*/
    .grid-content
      height 180px
      margin-bottom 10px

</style>
