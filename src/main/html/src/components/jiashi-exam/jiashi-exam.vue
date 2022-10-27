<template>
  <div class="exam">
    <div class="jsTitle">考试成绩统计</div>
    <el-row type="flex" class="row-bg">
      <el-col :span="24"><div class="grid-content" id="exam1"></div></el-col>
    </el-row>
  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    data() {
      return {}
    },
    methods: {
      exam1(options){
        let echarts = require('echarts/lib/echarts')
        let line1=document.getElementById('exam1')
        let mainChart = echarts.init(line1)
        var option = null;
        // 指定图表的配置项和数据
        app.config = {
          rotate: 90,
          align: 'left',
          verticalAlign: 'middle',
          position: 'insideBottom',
          distance: 15,
          onChange: function () {
            var labelOption = {
              normal: {
                rotate: app.config.rotate,
                align: app.config.align,
                verticalAlign: app.config.verticalAlign,
                position: 'top',
                distance: app.config.distance,
                color:'#ffffff'
              }
            };
            myChart.setOption({
              series: [{
                label: labelOption
              }, {
                label: labelOption
              }, {
                label: labelOption
              }, {
                label: labelOption
              }]
            });
          }
        };


        var labelOption = {
          normal: {
            show: false,
            position: "top",
            // distance: app.config.distance,
            // align: app.config.align,
            // verticalAlign: app.config.verticalAlign,
            // rotate: app.config.rotate,
            formatter: '{c}',
            color:'#ffffff',
            fontSize: 12,
            rich: {
              name: {
                textBorderColor: '#fff'
              }
            }
          }
        };

        option = {
          color: ['#003366', '#006699', '#4cabce', '#FAC069'],
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '5%',
            right: '8%',
            bottom: '0%',
            top: '30%',
            containLabel: true
          },
          legend: {
            data: ['平均分', '标准差', '最高分', '最低分'],
            textStyle:{
              color:'#ffffff',
              fontSize: 10
            },
            itemWidth:16,
            itemHeight:8
          },
          toolbox: {
            show: true,
            orient: 'vertical',
            right: '10',
            top: 'center',
            // feature: {
            //   mark: {show: true},
            //   dataView: {show: true, readOnly: false},
            //   magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
            //   restore: {show: true},
            //   saveAsImage: {show: true}
            // }
          },
          calculable: true,
          xAxis: [
            {
              type: 'category',
              axisTick: {show: false},
              data: ['科目一', '科目二', '科目三', '科目四', '科目五','科目六'],
              //坐标线颜色
              axisLine:{
                lineStyle:{
                  color: "#6990AD"
                }
              },
              axisTick:{
                show:false
              },
              axisLabel:{
                show: true,
                interval:0,
                fontSize:10
              },
              itemStyle:{
                borderType:"dashed"
              }
            },

          ],
          yAxis: [
            {
              type: 'value',
              name:'度量值/分',
              nameGap: '32',
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
              axisLabel:{
                show: true,
                interval:0,
                fontSize:10
              },
              splitLine: {
                show:false,
                lineStyle: {
                  color: ['#6990AD'],
                  type:"dashed"
                }
              }
            }
          ],
          series: [
            {
              name: '平均分',
              type: 'bar',
              barGap: 0,
              label: labelOption,
              data: [105, 100, 95, 87.5, 100,111]
            },
            {
              name: '标准差',
              type: 'bar',
              label: labelOption,
              data: [103, 99, 94, 84, 98,115]
            },
            {
              name: '最高分',
              type: 'bar',
              label: labelOption,
              data: [130, 125, 120, 115, 135,145]
            },
            {
              name: '最低分',
              type: 'bar',
              label: labelOption,
              data: [80, 75, 70, 60, 65,75]
            },
          ],
        };
        // 使用刚指定的配置项和数据显示图表。
        if (option && typeof option === "object") {
          mainChart.setOption(option, true)
        }
      }
    },
    mounted(){
      this.exam1()
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus">
  .exam
    width 100%
    height 100%
    /*border 1px solid red*/

    /*.examTitle
      width: 256px
      height: 96px
      /*border 1px solid red
      background: url("../../../static/images/jiashi-title.png") no-repeat
      background-size: 80% 80%
      line-height 65px
      padding-left 85px
      font-size 14px*/
    .grid-content
      height 200px
      margin-bottom 10px
</style>
