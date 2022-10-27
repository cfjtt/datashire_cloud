<template>
  <div id="Environment1">
    <div class="jsTitle" style="height: 37px">实验环境数量</div>
    <el-row type="flex" class="row-bg">
      <el-col :span="24"><div class="grid-content " id="courseExper"></div></el-col>
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
        experimentStopTimer: -1
      }
    },
    methods:{

      loadCharacterCloud: function(){
        getCharacterCloudDashboardData().then(res=>{
          var data = res.data.data;
          for(var i = 0;i < data.length;i++){
            console.log(data[i]);
            var sign = data[i].name;
            if(sign == 'experiment'){
              this.courseExper(data[i].data,data[i].nameMap);

            }
          }
        })
      },
      courseExper(data,nameMap){
        let echarts = require('echarts/lib/echarts')
        let line1=document.getElementById('courseExper')
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
      timingFlushExperimentDashboardData: function(){
        this.experimentStopTimer = setInterval(()=>{
          var path = this.$route.path;
          if(path != '/jiashi'){
            window.clearInterval(this.experimentStopTimer);
            return;
          }
          this.loadCharacterCloud();
        },60 * 60 * 1000);
      }
    },
    mounted(){
      /* this.courseContent();
       this.courseExperiment()*/
      this.loadCharacterCloud();
      this.timingFlushExperimentDashboardData();
    },
    beforeRouteLeave (to, from, next) {
      window.clearInterval(this.experimentStopTimer);
      next()
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus">
  #Environment1
    width 100%
    height 100%
    .grid-content
      height 200px
      margin-bottom 10px
  /*border 1px solid red*/
</style>
