<template>
    <div id="spark">
      <div class="jsTitle">Spark作业统计</div>
      <el-row type="flex" class="row-bg">
        <el-col :span="24"><div class="grid-content" id="spark-finish"></div></el-col>
      </el-row>
      <el-row type="flex" class="row-bg">
        <el-col :span="24"><div class="grid-content" id="spark-paidui"></div></el-col>
      </el-row>
    </div>
</template>

<script type="text/ecmascript-6">
  export default {
    data() {
      return {
        // dateArray: []
      }

    },
    methods:{
      finish(xData){
        let echarts = require('echarts/lib/echarts')
        let line1=document.getElementById('spark-finish')
        let mainChart = echarts.init(line1)
        var option = null;
        // 指定图表的配置项和数据
        option = {
          title: {
            text: '完成/运行中数量',
            show:false,
            textStyle: {
              color: "#E6E6E6",
              fontSize: 15
            }
          },
          toolbox: {
            show: false,
            orient: 'horizontal',
            top: '-8',
            feature: {
              mark: {show: true},
              dataView: {show: true, readOnly: false},
              magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
              restore: {show: true},
              saveAsImage: {show: true}
            },
            // iconStyle:{
            //   color:'#6990AD'
            // }
          },
          color: ['#3398DB'],
          tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
              type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
          },
          legend: {
            data:['完成数量'],
            show: false,
            textStyle:{
              color:'#ffffff'
            }
          },
          grid: {
            left: '5%',
            right: '3%',
            bottom: '5%',
            top: '22%',
            containLabel: true
          },
          xAxis : [
            {
              type : 'category',
              data :xData,
              axisTick: {
                alignWithLabel: false
              },
              axisLine:{
                show:true,
                lineStyle:{
                  color: "#6990AD"
                }
              },
              axisLabel:{
                interval:0,
                show:true,
                fontSize:10
              }
            }
          ],
          yAxis : {
            name:'完成数量/个',
            nameGap: '32',
            nameLocation: 'center',
            nameTextStyle:{
              fontSize:10,
              color: "#A9A9A9"
            },
            type: 'value',
            axisLine:{
              show:true,
              lineStyle:{
                color: "#6990AD"
              }
            },
            splitLine:{
              show:false
            },
            axisLabel:{
              interval:0,
              show:true,
              fontSize:10
            }
          },
          series : [
            {
              name:'完成数量',
              type:'bar',
              barWidth: '60%',
              data:[10, 52, 200, 334, 390, 330, 220,10, 52, 200, 334, 390, 330, 220,25],
              itemStyle:{
                normal:{
                  // color:'#6990AD',
                  barBorderRadius: [30, 30, 0, 0],
                  color: new echarts.graphic.LinearGradient(
                    0, 0, 0, 1, [{
                      offset: 0,
                      // color: '#00feff'
                      color:'#6990AD'
                    },
                      {
                        offset: 0.5,
                        color: '#027eff'
                      },
                      {
                        offset: 1,
                        color: '#0286ff'
                      }
                    ]
                  )
                }
              },
              label:{
                show:true,
                position:'top',
                color:'#ffffff',
                fontSize:10
              }
            }
          ]
        };
        // 使用刚指定的配置项和数据显示图表。
        if (option && typeof option === "object") {
          mainChart.setOption(option, true)
        }
      },
      paidui(){
        let echarts = require('echarts/lib/echarts')
        let line1=document.getElementById('spark-paidui')
        let mainChart = echarts.init(line1)
        var option = null;
        // 指定图表的配置项和数据
        option = {
          title: {
            text: '排队数量/运行数量',
            show:false,
            textStyle: {
              color: "#E6E6E6",
              fontSize: 15
            }
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data:['排队数量','运行数量'],
            show: false
          },
          grid: {
            left: '5%',
            right: '3%',
            bottom: '12%',
            top: '22%',
            containLabel: true
          },
          // toolbox: {
          //   feature: {
          //     saveAsImage: {}
          //   }
          // },
          xAxis: {
            // name:'时间',
            nameGap: '3',
            nameLocation: 'center',
            nameTextStyle:{
              fontSize:10,
              color: "#A9A9A9",
              align: "center",
            },
            type: 'category',
            boundaryGap: false,
            data:['01:00','02:00','03:00','04:00','05:00'],
            axisLabel:{
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
            },
            itemStyle:{
              borderType:"dashed"
            }
          },
          yAxis: {
            name:'排队/运行数量/个',
            // show:true,
            nameGap: '32',
            nameLocation: 'center',
            nameTextStyle:{
              fontSize:10,
              color: "#A9A9A9",
              align: "center",
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
                width:0.02
              }
            },
            axisLabel:{
              interval:0,
              show:true,
              fontSize:10
            }
          },
          series: [
            {
              name:'排队数量',
              type:'line',
              showSymbol: false,
              // areaStyle: {
              //   color:'#FAC069'
              // },
              itemStyle:{
                normal:{
                  color:'#FAC069',
                  areaStyle: {
                    color:'#FAC069'
                  },
                }
              },
              data:[120, 132, 101, 134, 90, 230, 210],
              lineStyle: {
                color: '#FAC069'
              },
              smooth:true
            },
            {
              name:'运行数量',
              type:'line',
              itemStyle:{
                normal:{
                  color:'#006699',
                  areaStyle: {
                    color:'#006699'
                  },
                }
              },
              showSymbol: false,
              data:[20, 32, 61, 74, 20, 30, 50],
              lineStyle: {
                color: '#006699'
              },
              smooth:true
            }
          ]
        };
        // 使用刚指定的配置项和数据显示图表。
        if (option && typeof option === "object") {
          mainChart.setOption(option, true)
        }
      },
      //x轴数据动态显示
      getDates(){
        // var dd = new Date();
        // dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
        // var y = dd.getFullYear();
        // var m = (dd.getMonth()+1)<10?"0"+(dd.getMonth()+1):(dd.getMonth()+1);//获取当前月份的日期，不足10补0
        // var d = dd.getDate()<10?"0"+dd.getDate():dd.getDate();//获取当前几号，不足10补0
        // return y+"-"+m+"-"+d;
        // // ----
        var myDate = new Date(); //获取今天日期
        // console.log("今天日期为:"+myDate.getDate())
        // myDate.setDate(myDate.getDate() - 3);
        myDate.setDate(myDate.getDate()<10?"0"+myDate.getDate():myDate.getDate() - 15);
        var dateArray = [];
        var dateTemp="";
        var flag = 1;
        for (var i = 0; i < 15; i++) {
          dateTemp = (myDate.getMonth()+1)+"-"+myDate.getDate();
          dateArray.push(dateTemp);
          myDate.setDate(myDate.getDate() + flag);
        }
        // console.log(dateArray)
        // debugger
        dateArray.forEach((value, index) => {

          var Str=value.split('-');
          //var valueName=value
          // valueName=valueName.toString().substr(2,2)
          //valueName=valueName.toString()
          dateArray[index]=Str[1]+"号"
          // console.log(valueName)
        })
        this.finish(dateArray);
        // console.log(this.dateArray)
        console.log(this.finish.option)
      }
    },
    mounted(){
      //this.finish();
      this.paidui();
      this.getDates();
    }
    // created(){
    //   this.finish()
    //   this.paidui()
    //   this.getDates()
    // }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  #spark
    width 100%
    height 100%
    /*#sparkTitle*/
      /*width 256px*/
      /*height 86px*/
      /*background url("../../../static/images/jiashi-title.png") no-repeat*/
      /*background-size 80% 86px*/
      /*line-height 70px*/
      /*padding-left 85px*/
      /*font-size 14px*/
    #spark-finish, #spark-paidui
      height 150px
      margin-bottom 10px
</style>
