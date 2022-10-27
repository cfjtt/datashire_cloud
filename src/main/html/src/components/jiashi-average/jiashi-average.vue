<template>
  <div id="average">
    <div class="jsTitle">平均时长日/周/月</div>
    <el-row type="flex" class="row-bg">
      <el-col :span="8"><div class="grid-content" id="averageDay"></div></el-col>
      <el-col :span="8"><div class="grid-content " id="averageWeek"></div></el-col>
      <el-col :span="8"><div class="grid-content " id="averageMonth"></div></el-col>
    </el-row>
  </div>
</template>

<script type="text/ecmascript-6">
  import Vue from 'vue'
  import axios from 'axios'
  import {getDayAndWeekAndMonthOnlineAvgData} from '@/common/js/request';
  export default {
    data() {
      return {
        dayData: {},
        weekData: {},
        monthData: {},
        avgStopTimer: -1

      }
    },
    methods: {

      loadDashboard:function(){
        getDayAndWeekAndMonthOnlineAvgData().then(res=>{
          console.log("---res.data.data----");
          var data = res.data.data;
          console.log("data数据为:"+data)
          // for(var i=0;i<this.dayData.xAxisData.length;i++){
          //   this.dayData.xAxisData[i]=this.dayData.xAxisData[i].substr(8,2)
          // }
          console.log(data);
          console.log("---res.data.data----");
          for(var i = 0;i < data.length;i++){
            var name = data[i].name;
            if(name == 'day'){
              this.dayData = data[i];
              // console.log("数据为:"+this.dayData.xAxisData)
              for(var r=0;r<this.dayData.xAxisData.length;r++){
                this.dayData.xAxisData[r]=this.dayData.xAxisData[r].substr(8,2)
              }
              console.log("dayx轴数据:"+this.dayData.xAxisData.slice(1,7))
              this.averageDay(this.dayData.durationYaxisData.slice(1,7),this.dayData.uvYaxisData.slice(1,7),this.dayData.xAxisData.slice(1,7));
              // var xAxisData=this.dayData.xAxisData.substr(8,2)
              // this.dayData.xAxisData=xAxisData
            }else if(name == 'week'){
              this.weekData = data[i];
              console.log("week数据为"+this.weekData.xAxisData)
              for(var a=0;a<this.weekData.xAxisData.length;a++){
                this.weekData.xAxisData[a]=this.weekData.xAxisData[a].substr(4,2)
              }
              this.averageWeek(this.weekData.durationYaxisData,this.weekData.uvYaxisData,this.weekData.xAxisData)
            }else if(name == 'month'){
              this.monthData = data[i];
             console.log("month数据为"+this.monthData.xAxisData)
              for(var z=0;z<this.monthData.xAxisData.length;z++){
                this.monthData.xAxisData[z]=this.monthData.xAxisData[z].substr(5,2)
              }
              this.averageMonth(this.monthData.durationYaxisData,this.monthData.uvYaxisData,this.monthData.xAxisData)
            }
          }
        })
      },
      averageDay(duration,uv,xData){
        let echarts = require('echarts/lib/echarts')
        let line1=document.getElementById('averageDay')
        let mainChart = echarts.init(line1)
        var option = null;
        /*let averData=[
          [110, 120, 100, 70, 60, 50],
          [120, 200, 150, 80, 70, 110],
          ['1周', '2周', '3周', '4周', '5周', '6周']
        ]*/
        // 指定图表的配置项和数据
        option = {
          title: {
            text: 'uv/平均时长-日',
            show:false,
            textStyle: {
              color: "#E6E6E6",
              fontSize: 16,
              show:false
            }
          },
          tooltip : {
            trigger: 'axis',
            axisPointer:{
              type:'shadow'
            },
            // formatter: "平均时长: {d}<br/>自然人数: {c}<br/>"
            // formatter: function(data)
            // {
            //   var res = name + '<br/>', val;
            //   for(var i = 0, length = averData.length; i < length; i++) {
            //     name= val = (averData[i].value*100) + '<br/>';
            //     val = (averData[i].value*100) + '%';
            //     res += averData[i].seriesName + '：' + val + '<br/>';
            //   }
            //   return res;
            // }
          },
          grid: {
            left: '12%',
            right: '5%',
            bottom: '5%',
            top: '25%',
            containLabel: true
          },
          legend: {
            data:['uv/人数','日平均时长/分钟'],
            textStyle:{
              color:'#ffffff',
              fontSize: 10
            },
            orient:'vertical',
            itemWidth:16,
            itemHeight:8
          },
          xAxis: {
            type: 'category',
            data: xData,
            name:'时间/日',
            nameGap: '18',
            nameTextStyle:{
              fontSize: 10
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
              show:true
            },
            itemStyle:{
              borderType:"dashed"
            }
          },
          yAxis: {
            type: 'value',
            // name:'uv/平均时长-日',
            nameGap: '30',
            nameLocation: 'center',
            nameTextStyle:{
              fontSize:12,
              color: "#A9A9A9"
            },
            axisLine:{
              show:true,
              lineStyle:{
                color: "#6990AD"
              }
            },
            splitLine: {
              show:false,
              lineStyle: {
                color: ['#6990AD'],
                type:"dashed"
              }
            },
            axisLabel:{
              show: true,
              interval:0,
              fontSize:10
            },
          },
          series: [
            {
              data: uv,
              type: 'bar',
              name: 'uv/人数',
              itemStyle:{
                color:'#6990AD'
              },

            },
            {
              data: duration,
              type: 'line',
              name: '日平均时长/分钟',
              itemStyle:{
                color:'#FAC069'
              },
              showAllSymbol:true
            }
          ]
        };
        // 使用刚指定的配置项和数据显示图表。
        if (option && typeof option === "object") {
          mainChart.setOption(option, true)
        }
      },
      averageWeek(duration,uv,xData){
        let echarts = require('echarts/lib/echarts')
        let line1=document.getElementById('averageWeek')
        let mainChart = echarts.init(line1)
        /*let averData=[
          [110, 120, 100, 70, 60, 50],
          [120, 200, 150, 80, 70, 110],
          ['1周', '2周', '3周', '4周', '5周', '6周']
        ]*/
        var option = null;
        // 指定图表的配置项和数据
        option = {
          title: {
            text: 'uv/平均时长-周',
            show:false,
            textStyle: {
              color: "#E6E6E6",
              fontSize: 16,
              show:false
            }
          },
          tooltip : {
            trigger: 'axis',
            axisPointer:{
              type:'shadow'
            },
            // formatter: "平均时长: {b}<br/>自然人数: {c}<br/>"
            // formatter: function(data)
            // {
            //   var res = name + '<br/>', val;
            //   for(var i = 0, length = averData.length; i < length; i++) {
            //     name= val = (averData[i].value*100) + '<br/>';
            //     val = (averData[i].value*100) + '%';
            //     res += averData[i].seriesName + '：' + val + '<br/>';
            //   }
            //   return res;
            // }
          },
          grid: {
            left: '12%',
            right: '5%',
            bottom: '5%',
            top: '25%',
            containLabel: true
          },
          legend: {
            data:['uv/人数','周平均时长/分钟'],
            textStyle:{
              color:'#ffffff',
              fontSize: 10
            },
            orient:'vertical',
            itemWidth:16,
            itemHeight:8

            // data:['uv/人数','日平均时长/分钟'],
            // textStyle:{
            //   color:'#ffffff',
            //   fontSize: 12
            // },
            // orient:'vertical',
            // itemWidth:20,
            // itemHeight:10
          },
          xAxis: {
            type: 'category',
            data: xData,
            name:'时间/周',
            nameGap: '18',
            nameLocation: 'center',
            nameTextStyle:{
              fontSize: 10
            },
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
              show:true
            },
            itemStyle:{
              borderType:"dashed"
            }
          },
          yAxis: {
            type: 'value',
            // name:'uv/平均时长-日',
            nameGap: '30',
            nameLocation: 'center',
            nameTextStyle:{
              fontSize:12,
              color: "#A9A9A9"
            },
            axisLine:{
              show:true,
              lineStyle:{
                color: "#6990AD"
              }
            },
            splitLine: {
              show:false,
              lineStyle: {
                color: ['#6990AD'],
                type:"dashed"
              }
            },
            axisLabel:{
              show: true,
              interval:0,
              fontSize:10
            },
          },
          series: [
            {
              data: uv,
              type: 'bar',
              name: 'uv/人数',
              itemStyle:{
                color:'#6990AD'
              },

            },
            {
              data: duration,
              type: 'line',
              name: '周平均时长/分钟',
              itemStyle:{
                color:'#FAC069'
              },
              showAllSymbol:true
            }
          ]
        };
        // 使用刚指定的配置项和数据显示图表。
        if (option && typeof option === "object") {
          mainChart.setOption(option, true)
        }
      },
      averageMonth(duration,uv,xData){
        let echarts = require('echarts/lib/echarts')
        let line1=document.getElementById('averageMonth')
        let mainChart = echarts.init(line1)
        var option = null;
        /*let averData=[
          [110, 120, 100, 70, 60, 50],
          [120, 200, 150, 80, 70, 110],
          ['1周', '2周', '3周', '4周', '5周', '6周']
        ]*/
        // 指定图表的配置项和数据
        option = {
          title: {
            text: 'uv/平均时长-月',
            show:false,
            textStyle: {
              color: "#E6E6E6",
              fontSize: 16,
              show:false
            }
          },
          tooltip : {
            trigger: 'axis',
            axisPointer:{
              type:'shadow'
            },
            // formatter: "月份: {a}<br/>uv: {b}<br/>时长:{c}"
            // formatter: function(data)
            // {
            //   var res = name + '<br/>', val;
            //   for(var i = 0, length = averData.length; i < length; i++) {
            //     name= val = (averData[i].value*100) + '<br/>';
            //     val = (averData[i].value*100) + '%';
            //     res += averData[i].seriesName + '：' + val + '<br/>';
            //   }
            //   return res;
            // }
          },
          grid: {
            left: '12%',
            right: '5%',
            bottom: '5%',
            top: '30%',
            containLabel: true
          },
          legend: {
            // data:['uv','时长/月'],
            // textStyle:{
            //   color:'#ffffff'
            // }


            data:['uv/人数','月平均时长/分钟'],
            textStyle:{
              color:'#ffffff',
              fontSize: 10
            },
            orient:'vertical',
            itemWidth:16,
            itemHeight:8
          },
          xAxis: {
            type: 'category',
            data: xData,
            name:'时间/月',
            nameGap: '18',
            nameTextStyle:{
              fontSize: 10
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
              show:true
            },
            itemStyle:{
              borderType:"dashed"
            }
          },
          yAxis: {
            type: 'value',
            // name:'uv/平均时长-月',
            nameGap: '30',
            nameLocation: 'center',
            nameTextStyle:{
              fontSize:10,
              color: "#A9A9A9"
            },
            axisLine:{
              show:true,
              lineStyle:{
                color: "#6990AD"
              }
            },
            splitLine: {
              show:false,
              lineStyle: {
                color: ['#6990AD'],
                type:"dashed"
              }
            },
            axisLabel:{
              show: true,
              interval:0,
              fontSize:10
            }
          },
          series: [
            {
              data: uv,
              type: 'bar',
              name: 'uv/人数',
              itemStyle:{
                color:'#6990AD'
              },

            },
            {
              data: duration,
              type: 'line',
              name: '月平均时长/分钟',
              itemStyle:{
                color:'#FAC069'
              },
              showAllSymbol:true
            }
          ]
        };
        // 使用刚指定的配置项和数据显示图表。
        if (option && typeof option === "object") {
          mainChart.setOption(option, true)
        }
      },
      timingFlushAvgDashboardData: function(){
        this.avgStopTimer = setInterval(()=>{
          var path = this.$route.path;
          if(path != '/jiashi'){
            window.clearInterval(this.avgStopTimer);
            return;
          }
          this.loadDashboard();
        },60 * 60 * 1000);
      }
    },
    mounted(){
      /*this.averageDay()
      this.averageWeek()
      this.averageMonth()*/
      this.loadDashboard();
      this.timingFlushAvgDashboardData();
    },
    beforeRouteLeave (to, from, next) {
      window.clearInterval(this.avgStopTimer);
      next()
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus">
  #average
    width 100%
    height 100%
    /*border 1px solid red*/
    /*#averageTitle*/
      /*width: 256px*/
      /*height: 96px*/
      /*background: url("../../../static/images/jiashi-title.png") no-repeat*/
      /*background-size: 80% 80%*/
      /*line-height 65px*/
      /*padding-left 85px*/
      /*font-size 14px*/
      /*border 1px solid red*/
    .grid-content
      height 200px
      margin-bottom 10px
</style>
