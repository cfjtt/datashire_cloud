<template>
  <div id="course">
    <div class="jsTitle" style="height: 37px">课程内容</div>
    <el-row type="flex" class="row-bg">
      <el-col :span="24"><div class="grid-content" id="courseContent"></div></el-col>
      <!--<el-col :span="12"><div class="grid-content " id="courseExperiment"></div></el-col>-->
    </el-row>
  </div>
</template>

<script type="text/ecmascript-6">
  import Vue from 'vue'
  import axios from 'axios'
  import {getCharacterCloudDashboardData} from '@/common/js/request';
  export default {
    data() {
      return {
        courseStopTimer: -1
      }
    },
    methods:{

      loadCharacterCloudCharts: function(){
        getCharacterCloudDashboardData().then(res=>{
          var data = res.data.data;
          for(var i = 0;i < data.length;i++){
            console.log(data[i]);
            var sign = data[i].name;
            if(sign == 'course'){
              this.courseContent(data[i].data,data[i].nameMap);
            }
          }
        })
      },
      courseContent(data,nameMap){
        let echarts = require('echarts/lib/echarts')
        let line1=document.getElementById('courseContent')
        let mainChart = echarts.init(line1)
        var option = null;
        // 指定图表的配置项和数据
        option = {
          grid:{
            x:0,
            x2:0,
            y:0,
            y2:0
          },

          title: {
            text: "",
            subtext: "",
            textStyle: {
              color: "#fff",
              fontSize: 18
            },
            top: "top",
            left: "center"
          },
          tooltip: {
            formatter: "{b}<br/>"
          },
          legend: {
            show:false
          },
          toolbox: {
            show: false,
            feature: {
              dataView: {
                show: true,
                readOnly: true
              },
              restore: {
                show: true
              },
              saveAsImage: {
                show: true
              }
            }
          },
          animationDuration: 3000,
          animationEasingUpdate: 'quinticInOut',
          series: [{
            name: '综合治理主题视图',
            type: 'graph',
            layout: 'force',

            force: {
              // initLayout: 'circular',
              repulsion: 240,
              gravity: 0.2,
              edgeLength: 90
            },


            data: data,
            categories: nameMap,

            focusNodeAdjacency: true,
            roam: true,
            label: {
              normal: {

                show: true,
                position: 'top',
                textStyle: {
                  fontSize: 14
                }

              }
            },
            lineStyle: {
              normal: {
                color: 'source',
                curveness: 0
                //type: "solid"
              }
            }
          }]
        };
        // 使用刚指定的配置项和数据显示图表。
        if (option && typeof option === "object") {
          mainChart.setOption(option, true)
        }
        window.onresize = mainChart.resize;
      },
      timingFlushCourseDashboardData: function(){
        this.courseStopTimer = setInterval(()=>{
          var path = this.$route.path;
          if(path != '/jiashi'){
            window.clearInterval(this.courseStopTimer);
            return;
          }
          this.loadCharacterCloudCharts();
        },60 * 60 * 1000);
      }
    },
    mounted(){
     /* this.courseContent();
      this.courseExperiment()*/
      this.loadCharacterCloudCharts();
      this.timingFlushCourseDashboardData();
    },
    beforeRouteLeave (to, from, next) {
      window.clearInterval(this.courseStopTimer);
      next()
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus">
  #course
    width 100%
    height 100%
    /*#courseTitle*/
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
