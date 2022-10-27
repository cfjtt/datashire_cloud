<template>
    <div>
      <div id="pingtai">
        <div class="jsTitle">平台硬件状态</div>
        <el-row type="flex" class="row-bg">
          <el-col :span="12"><div class="grid-content" id="pingtai-line1"></div></el-col>
          <el-col :span="12"><div class="grid-content " id="pingtai-line2"></div></el-col>
        </el-row>
        <el-row type="flex" class="row-bg">
          <el-col :span="12"><div class="grid-content" id="pingtai-line3"></div></el-col>
          <el-col :span="12"><div class="grid-content" id="pingtai-line4"></div></el-col>
        </el-row>
      </div>
    </div>
</template>

<script type="text/ecmascript-6">
  import Vue from 'vue'
  import axios from 'axios'
  import {getClouderaData} from '@/common/js/request';
  import echarts from "echarts";
  export default {
    data(){
      return {
        stopTimer : -1
      }
    },
    methods:{
      // line1(options){
      //   let echarts = require('echarts/lib/echarts')
      //   let line1=document.getElementById('pingtai-line1')
      //   let mainChart = echarts.init(line1)
      //   var option = null;
      //   // 指定图表的配置项和数据
      //   option = {
      //     title: {
      //       text: 'cpu利用率',
      //       textStyle: {
      //         color: "#E6E6E6",
      //         fontSize: 16
      //       }
      //     },
      //     tooltip: {
      //       trigger: 'axis'
      //     },
      //     // legend: {
      //     //   data:['01:00','02:00','03:00','04:00','04:00'],
      //     //   show: false
      //     // },
      //     grid: {
      //       left: '12%',
      //       right: '3%',
      //       bottom: '5%',
      //       top: '22%',
      //       containLabel: true
      //     },
      //     // toolbox: {
      //     //   feature: {
      //     //     saveAsImage: {}
      //     //   }
      //     // },
      //     xAxis: {
      //       name:"",
      //       type: 'category',
      //       boundaryGap: false,
      //       data:['01:00','02:00','03:00','04:00','04:00'],
      //       axisLabel:{
      //         show: false
      //       },
      //       //坐标线颜色
      //       axisLine:{
      //         lineStyle:{
      //           color: "#6990AD"
      //         }
      //       },
      //       axisTick:{
      //         show:false
      //       },
      //       itemStyle:{
      //         borderType:"dashed",
      //         opacity:0.3
      //       }
      //     },
      //     yAxis: {
      //       name:'cpu利用率',
      //       nameGap: '32',
      //       nameLocation: 'center',
      //       nameTextStyle:{
      //         fontSize:12,
      //         color: "#A9A9A9"
      //       },
      //       type: 'value',
      //       axisLine:{
      //         lineStyle:{
      //           color: "#6990AD"
      //         }
      //       },
      //       splitLine: {
      //         lineStyle: {
      //           color: ['#6990AD'],
      //           type:"dashed",
      //           opacity:0.3
      //         }
      //       },
      //     },
      //     series: [
      //       {
      //         name:'cpu利用率(%)',
      //         type:'line',
      //         showSymbol: false,
      //         data:[120, 132, 101, 134, 90, 230, 210],
      //         lineStyle: {
      //           color: '#6990AD'
      //         },
      //         smooth:true
      //       }
      //     ]
      //   };
      //   // 使用刚指定的配置项和数据显示图表。
      //   if (option && typeof option === "object") {
      //     mainChart.setOption(option, true)
      //   }
      // },
      // line2(options){
      //   let echarts = require('echarts/lib/echarts')
      //   let line1=document.getElementById('pingtai-line2')
      //   let mainChart = echarts.init(line1)
      //   var option = null;
      //   // 指定图表的配置项和数据
      //   option = {
      //     title: {
      //       text: '占用内存',
      //       textStyle: {
      //         color: "#E6E6E6",
      //         fontSize: 16
      //       }
      //     },
      //     tooltip: {
      //       trigger: 'axis'
      //     },
      //     // legend: {
      //     //   data:['01:00','02:00','03:00','04:00','04:00'],
      //     //   show: false
      //     // },
      //     grid: {
      //       left: '12%',
      //       right: '3%',
      //       bottom: '5%',
      //       top: '22%',
      //       containLabel: true
      //     },
      //     // toolbox: {
      //     //   feature: {
      //     //     saveAsImage: {}
      //     //   }
      //     // },
      //     xAxis: {
      //       name:"",
      //       type: 'category',
      //       boundaryGap: false,
      //       data:['01:00','02:00','03:00','04:00','04:00'],
      //       axisLabel:{
      //         show: false
      //       },
      //       //坐标线颜色
      //       axisLine:{
      //         lineStyle:{
      //           color: "#6990AD"
      //         }
      //       },
      //       axisTick:{
      //         show:false
      //       },
      //       itemStyle:{
      //         borderType:"dashed",
      //         opacity:0.3
      //       }
      //     },
      //     yAxis: {
      //       name:'占用内存(M)',
      //       // show:true,
      //       nameGap: '32',
      //       nameLocation: 'center',
      //       nameTextStyle:{
      //         fontSize:12,
      //         color: "#A9A9A9",
      //         align: "center",
      //       },
      //       type: 'value',
      //       axisLine:{
      //         lineStyle:{
      //           color: "#6990AD"
      //         }
      //       },
      //       splitLine: {
      //         lineStyle: {
      //           color: ['#6990AD'],
      //           type:"dashed",
      //           opacity:0.3
      //         }
      //       },
      //     },
      //     series: [
      //       {
      //         name:'占用内存',
      //         type:'line',
      //         showSymbol: false,
      //         data:[120, 132, 101, 134, 90, 230, 210],
      //         lineStyle: {
      //           color: '#6990AD'
      //         },
      //         smooth:true
      //       }
      //     ]
      //   };
      //   // 使用刚指定的配置项和数据显示图表。
      //   if (option && typeof option === "object") {
      //     mainChart.setOption(option, true)
      //   }
      // },
      // line3(options){
      //   let echarts = require('echarts/lib/echarts')
      //   let line1=document.getElementById('pingtai-line3')
      //   let mainChart = echarts.init(line1)
      //   var option = null;
      //   // 指定图表的配置项和数据
      //   option = {
      //     title: {
      //       text: '磁盘(IO)',
      //       textStyle: {
      //         color: "#E6E6E6",
      //         fontSize: 16
      //       }
      //     },
      //     tooltip: {
      //       trigger: 'axis'
      //     },
      //     // legend: {
      //     //   data:['01:00','02:00','03:00','04:00','04:00'],
      //     //   show: false
      //     // },
      //     grid: {
      //       left: '15%',
      //       right: '3%',
      //       bottom: '5%',
      //       top: '22%',
      //       containLabel: true
      //     },
      //     // toolbox: {
      //     //   feature: {
      //     //     saveAsImage: {}
      //     //   }
      //     // },
      //     xAxis: {
      //       name:"",
      //       type: 'category',
      //       boundaryGap: false,
      //       data:['01:00','02:00','03:00','04:00','04:00'],
      //       axisLabel:{
      //         show: false
      //       },
      //       //坐标线颜色
      //       axisLine:{
      //         lineStyle:{
      //           color: "#6990AD"
      //         }
      //       },
      //       axisTick:{
      //         show:false
      //       },
      //       itemStyle:{
      //         borderType:"dashed",
      //         opacity:0.3
      //       }
      //     },
      //     yAxis: {
      //       name:'磁盘(IO)',
      //       // show:true,
      //       nameGap: '32',
      //       nameLocation: 'center',
      //       nameTextStyle:{
      //         fontSize:12,
      //         color: "#A9A9A9",
      //         align: "center",
      //       },
      //       type: 'value',
      //       axisLine:{
      //         lineStyle:{
      //           color: "#6990AD"
      //         }
      //       },
      //       splitLine: {
      //         lineStyle: {
      //           color: ['#6990AD'],
      //           type:"dashed",
      //           opacity:0.3
      //         }
      //       },
      //     },
      //     series: [
      //       {
      //         name:'磁盘(IO)',
      //         type:'line',
      //         showSymbol: false,
      //         data:[120, 132, 101, 134, 90, 230, 210],
      //         lineStyle: {
      //           color: '#6990AD'
      //         },
      //         smooth:true
      //       }
      //     ]
      //   };
      //   // 使用刚指定的配置项和数据显示图表。
      //   if (option && typeof option === "object") {
      //     mainChart.setOption(option, true)
      //   }
      // },
      // line4(options){
      //   let echarts = require('echarts/lib/echarts')
      //   let line1=document.getElementById('pingtai-line4')
      //   let mainChart = echarts.init(line1)
      //   var option = null;
      //   // 指定图表的配置项和数据
      //   option = {
      //     title: {
      //       text: '网络(IO)',
      //       textStyle: {
      //         color: "#E6E6E6",
      //         fontSize: 16
      //       }
      //     },
      //     tooltip: {
      //       trigger: 'axis',
      //       transitionDuration:0,
      //       position: [10, 10]
      //     },
      //     // legend: {
      //     //   data:['01:00','02:00','03:00','04:00','04:00'],
      //     //   show: false
      //     // },
      //     grid: {
      //       left: '12%',
      //       right: '3%',
      //       bottom: '5%',
      //       top: '22%',
      //       containLabel: true
      //     },
      //     // toolbox: {
      //     //   feature: {
      //     //     saveAsImage: {}
      //     //   }
      //     // },
      //     xAxis: {
      //       name:"时间",
      //       type: 'category',
      //       boundaryGap: false,
      //       data:['01:00','02:00','03:00','04:00','04:00'],
      //       axisLabel:{
      //         show: false
      //       },
      //       //坐标线颜色
      //       axisLine:{
      //         lineStyle:{
      //           color: "#6990AD"
      //         }
      //       },
      //       axisTick:{
      //         show:false
      //       },
      //       itemStyle:{
      //         borderType:"dashed"
      //       }
      //     },
      //     yAxis: {
      //       name:'网络(IO)',
      //       // show:true,
      //       nameGap: '32',
      //       nameLocation: 'center',
      //       nameTextStyle:{
      //         fontSize:12,
      //         color: "#A9A9A9",
      //         align: "center",
      //       },
      //       type: 'value',
      //       axisLine:{
      //         lineStyle:{
      //           color: "#6990AD"
      //         }
      //       },
      //       splitLine: {
      //         lineStyle: {
      //           color: ['#6990AD'],
      //           type:"dashed"
      //         }
      //       },
      //     },
      //     series: [
      //       {
      //         name:'网络(IO)',
      //         type:'line',
      //         showSymbol: false,
      //         data:[120, 132, 101, 134, 90, 230, 210],
      //         lineStyle: {
      //           color: '#6990AD'
      //         },
      //         smooth:true
      //       }
      //     ]
      //   };
      //   // 使用刚指定的配置项和数据显示图表。
      //   if (option && typeof option === "object") {
      //     mainChart.setOption(option, true)
      //   }
      // },
      setOption: function () {
        var query = "SELECT cpu_percent,physical_memory_buffers ,total_bytes_receive_rate_across_network_interfaces ,total_write_ios_rate_across_disks WHERE category=host";
        var from = new Date().getTime() - (30 * 60 * 1000);
        var to = new Date().getTime();
        getClouderaData(query, from, to).then(res => {
          var sourceData = res.data.data;
          for (var key in sourceData) {
            var curOption = this.getOption();
            var id = '';
            var title = '';
            var yAxisName = '';
            if (key == 'cpuData') {
              id = 'pingtai-line1';
              title = 'CPU利用率';
              yAxisName = 'percent'
            } else if (key == 'memoryData') {
              id = 'pingtai-line2';
              title = '占用内存';
              yAxisName = 'Mb';
            } else if (key == 'networkData') {
              id = 'pingtai-line3';
              title = '网络IO';
              yAxisName = 'Kb / second';
            } else if (key == 'disksData') {
              id = 'pingtai-line4';
              title = '磁盘IO';
              yAxisName = ' ios / second';
            }
            var data = sourceData[key];
            var datacolor=["orange","#0EA8CD","#12437B"]
            for (var i = 0; i < data.length; i++) {
              // curOption.color[i] = '#6990AD';
              curOption.color[i] = datacolor[i]
              curOption.legend.data[i] = {
                name: data[i].name,
                icon: 'circle'
              };
              curOption.series[i] = {
                name: data[i].name,
                type: 'line',
                data: [],
                smooth: true,
                symbol: 'emptyCircle',
                showSymbol:false,
                // symbolSize: 6,
                itemStyle: {
                  normal: {
                    lineStyle: {
                      width: 1,
                      color: '#6990AD'
                    }
                  }
                },
                // label:{
                //   show:true,
                //   color:'#ffffff'
                // }
              };
              curOption.series[i].data = data[i].yAxisData;
              curOption.series[i].itemStyle.normal.lineStyle.color = datacolor[i];//折线的颜色
              curOption.xAxis.data = data[i].timeAxisData;
            }
            curOption.title.text = title;
            curOption.yAxis.name = yAxisName;
            var myChart = echarts.init(document.getElementById(id));
            myChart.setOption(curOption);
            //console.log(curOption)
          }
        })
      },
      getOption: function () {
        var options = {
          title: {
            text: '',
            textStyle: {
              color: "#E6E6E6",
              fontSize: 15
            }
          },
          calculable: true,
          tooltip: {
            trigger: 'axis',
            confine:true
            // transitionDuration:0,
            // // showDelay:0,
            // // alwaysShowContent:false,
            // hideDelay:0,
            // // textStyle:{
            // //   align:'right'
            // // },
            // position:{
            //   // top:5,
            //   // right:5
            // }
          },
          grid: {
            left: '12%',
            right: '3%',
            bottom: '3%',
            top: '22%',
            containLabel: true
          },
          color: [],
          legend: {
            x: 'center',
            y: 20,
            data: [],
            show:false
          },
          toolbox: {
            show: false,
            feature: {
              dataZoom: {
                yAxisIndex: 'none'
              },
              dataView: {show: false},
              magicType: {type: ['line', 'bar']},
              restore: {},
              saveAsImage: {}
            }
          },
          xAxis: {
            //show: false,
            type: 'category',
            boundaryGap: false,
            data: [], /*时间轴节点*/
            axisLabel: {
              interval: 15,
              fontSize:10,
              show: false
            },
            //坐标线颜色
            axisLine:{
              lineStyle:{
                color: "#6990AD"
              }
            },
            axisTick:{
              show:false
            }
          },
          yAxis: {
            name: '',
            nameGap: '30',
            nameLocation: 'center',
            nameTextStyle:{
              fontSize:10,
              color: "#A9A9A9"
            },
            type: 'value',
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
            axisLabel: {
              formatter: '{value}',
              fontSize:10
            }
          },
          series: []
        };


        return options;
      },
      timingFlushDashboard(){
        this.stopTimer = setInterval(()=>{
          var path = this.$route.path;
          if(path != '/jiashi'){
            window.clearInterval(this.stopTimer);
            return;
          }
          this.setOption();
        },120 * 1000);
      }
    },
    mounted(){
      /*this.line1()
      this.line2()
      this.line3()
      this.line4()*/
      this.setOption();
      this.timingFlushDashboard();
    },
    beforeRouteLeave (to, from, next) {
      window.clearInterval(this.stopTimer);
      next()
    }
    /*beforeRouteEnter: (to, from, next) => {
      next(vm => {

      });
    },*/
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  #pingtai
    width 100%
    height 100%
    border 1px solid red
      /*padding-left 10px*/
    /*#pingTitle*/
      /*width: 256px*/
      /*height: 96px*/
      /*background: url("../../../static/images/jiashi-title.png") no-repeat*/
      /*background-size: 80% 80%*/
      /*line-height 65px*/
      /*padding-left 85px*/
      /*font-size 14px*/
      /*border 1px solid red*/
    .grid-content
      height 140px
      margin-bottom 10px
    /*  border 1px solid red*/
</style>
