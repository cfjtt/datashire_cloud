<template>
    <div id="deep">
      <div class="jsTitle">deep指数/科研项目</div>
      <el-row type="flex" class="row-bg">
        <el-col :span="16"><div class="grid-content" id="jsDeep1"></div></el-col>
        <el-col :span="8"><div class="grid-content" id="jsScientific"></div></el-col>
      </el-row>
    </div>
</template>

<script type="text/ecmascript-6">
  export default {
    data() {
      return {}
    },
    methods:{
      jsDeep1(options){
        let echarts = require('echarts/lib/echarts')
        let line1=document.getElementById('jsDeep1')
        let mainChart = echarts.init(line1)
        var option = null;
        // 指定图表的配置项和数据
        option = {
          title: {
            text: 'deep指数',
            show:false,
            textStyle: {
              color: "#E6E6E6",
              fontSize: 16
            }
          },
          toolbox: {
            show: true,
            orient: 'horizontal',
            top: '-8',
            right:'0',
            feature: {
              // mark: {show: true},
              // magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
              saveAsImage: {show: false}
            },
            // iconStyle:{
            //   color:'#6990AD'
            // }
          },
          calculable : true,
          tooltip: {
            position:{
              top:5,
              right:5
            }
          },
          legend: {
            data: ['预算分配（Allocated Budget）', '实际开销（Actual Spending）'],
            show:false
          },
          radar: {
            // shape: 'circle',
            name: {
              textStyle: {
                color: '#fff',
                backgroundColor: '#999',
                borderRadius: 3,
                padding: [3, 5]
              }
            },
            nameGap:8,
            axisLabel:{
              // show:true,
              inside:true
            },
            indicator: [
              { name: '教学', max: 1},
              { name: '科研', max: 1},
              { name: '作业', max: 1},
              { name: '考试', max: 1},
              { name: '人均在线时长', max: 1},
            ]
          },
          series: [{
            name: 'deep指数',
            type: 'radar',
            radius: 40,
            // areaStyle: {normal: {}},
            data : [
              {
                value : [0.7, 0.8, 0.6, 0.66,0.8],
                name : 'deep指数'
              }
            ],
            itemStyle: {normal: {areaStyle: {type: 'default'}}},
            label:{
              show:true,
              position:'insideBottom'
            }
          }]
        };
        // 使用刚指定的配置项和数据显示图表。
        if (option && typeof option === "object") {
          mainChart.setOption(option, true)
        }
        window.onresize = mainChart.resize;
      },
      jsScientific(options){
        let echarts = require('echarts/lib/echarts')
        let line2=document.getElementById('jsScientific')
        let mainChart = echarts.init(line2)
        var option = null;
        // 指定图表的配置项和数据
        option = {
          tooltip : {
            trigger: 'item',
            formatter: "<br/>状态:{b} <br/>数量: {c}<br/>占比: ({d}%)"
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            data: ['已完成','筹备中','在研'],
            show:false
          },
          series : [
            {
              name: '访问来源',
              type: 'pie',
              radius : '80%',
              data:[
                {value:6, name:'筹备中',itemStyle:{color:'#5A98DE'}},
                {value:4, name:'在研',itemStyle:{color:'#5868C0'}},
                {value:20, name:'已完成',itemStyle:{color:'#005FA6'}}
              ],
              label: {
                normal: {
                  show: true,
                  position:'inside'
                }
              },
              itemStyle: {
                show:false,
                emphasis: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
        // 使用刚指定的配置项和数据显示图表。
        if (option && typeof option === "object") {
          mainChart.setOption(option, true)
        }
      },
    },
    mounted(){
      this.jsDeep1();
      this.jsScientific()
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus">
  #deep
    width 100%
    height 100%
    /*#deepTitle*/
      /*width: 256px*/
      /*height: 96px*/
      /*background: url("../../../static/images/jiashi-title.png") no-repeat*/
      /*background-size: 80% 80%*/
      /*line-height 65px*/
      /*padding-left 85px*/
      /*font-size 14px*/
    .grid-content
      height 200px
      margin-bottom 10px
      /*border 1px solid red*/
</style>
