<template>
    <div>
      <!--头部-->
      <h-eader></h-eader>
      <div id="main" >
        <!--banner-->
        <div id="mainBanner">
          <div id="dataChange">
            <img src="../../../static/images/dataChange.png" width="361" height="139" alt="">
          </div>
          <div id="dataAdvert">
            <h1 style="margin-top: 10px;">海量行业数据开放</h1>
            <h1>大数据产学研融合</h1>
          </div>
          <p id="datadesc">
            <span>覆盖行业{{bannerData.industryCount}}个</span>
            <span>数据目录{{bannerData.dmCount}}个</span>
            <span>数据总量{{parseFloat(bannerData.dmSize/1024) > 1 ? (parseFloat(bannerData.dmSize/1024/1024) >1 ? ((parseFloat(bannerData.dmSize/1024/1024)).toFixed(2).concat('M')) : (parseFloat(bannerData.dmSize/1024)).toFixed(2).concat('K')) : (parseFloat(bannerData.dmSize)).toFixed(2).concat('B')}}</span>
            <span>使用次数{{bannerData.downCount}}次</span>
          </p>
        </div>
        <!--行业分类-->
        <div id="industry">
          <div id="indMain">
            <h1 class="title">行业分类</h1>
              <ul id="indIconUl" :class="{'ActiveUl':ActiveUl, 'ActiveNo':ActiveNo}">
                <li  v-for="(item,idx) in industryData" :key="idx" :class="{active:idx==clickIndex}" @click="_getDmData(item.id,idx)">
                 <div id="imgpath">
                   <img :src="item.imgpath" alt="未上传图片">
                 </div>
                  <p class="kong" style="margin-top: 5px;">{{item.name}}</p>
                </li>
              </ul>
            <div style="clear: both"></div>
            <h1 class="title" @click="allIndustry">所有行业>></h1>
          </div>
        </div>
        <!--输入框-->
        <div id="input">
          <el-input type="text" placeholder="请输入行业数据关键字" style="width: 30%" v-model="dmName"></el-input>
          <el-button type="primary" @click="search">搜索</el-button>
          <div class="block" style="max-width: 70%;;float: right;overflow: hidden">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="page"
              :page-sizes="[5, 10, 20, 50,100]"
              :page-size="offset"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
            </el-pagination>
          </div>
        </div>
        <!--数据集-->
        <div id="dataSet">
          <div style="width: 100%;height: auto;display:flex;margin-bottom: 10px;" v-for="(item,idx) in dmdata" :key="idx">
            <div id="dataSetLeft">
              <img :src="item.imgPath" width="100" height="100" alt="未上传图片">
            </div>
            <div id="dataSetRight">
              <div id="dsRleft">
                <h1 class="title titleName ">数据集名称:<span>{{item.name}}</span></h1>
                <p class="desc kong">数据集描述:<span>{{item.desc}}</span></p>
                <p class="descTime">发布时间:<span>{{item.createTime}}</span></p>
                <p class="descPublic">数据发布:<span>{{item.publisher}}</span></p>
              </div>
              <div id="dsRright">
                <p class="dsrPrice">价格:<span class="price">{{item.price}}元</span></p>
                <div id="btn">
                  <el-button type="primary" @click="dialogDesc(item)">详情</el-button>
                  <el-button type="warning" style="display: block" disabled>购买</el-button>
                  <el-button type="primary" style="display: block" @click="download(item.id)">下载</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!--详情弹出框-->
      <el-dialog
        id="dialog"
        :title="dialogTitle"
        :visible.sync="dialogVisible"
        style="margin-top: -5vh"
        width="60%">
        <!--购买信息-->
        <div id="descData">
          <div id="descDataLeft">
            <img :src="descImg" width="100" height="100" alt="未上传图片">
          </div>
          <div id="descDataContent">
            交易价格:<span>￥{{dmdataOne.price}}元</span>
          </div>
          <div id="descDataRight">
            <el-button type="warning" style="display: block" disabled>购买</el-button>
            <el-button type="primary" style="display: block" @click="cancleShow">取消</el-button>
          </div>
        </div>
        <!--分栏-->
        <el-tabs type="border-card" id="borderCard">
          <el-tab-pane label="详细信息">
            <table id="descTable" style="">
              <tr>
                <th>数据描述</th>
                <td>{{dmdataOne.desc}}</td>
              </tr>
              <tr>
                <th>所属行业</th>
                <td>{{dmdataOne.industryName}}</td>
              </tr>
              <tr>
                <th>数据格式</th>
                <td>{{dmdataOne.format==0 ? 'csv' : dmdataOne.format==1 ? 'excel' : dmdataOne.format==2 ? 'xml' : dmdataOne.format==3 ? 'txt' : dmdataOne.format == 4 ? 'json' : '其他'}}</td>
              </tr>
              <tr>
                <th>文件大小</th>
                <td>{{parseFloat(dmdataOne.size/1024) > 1 ? (parseFloat(dmdataOne.size/1024/1024) >1 ? ((parseFloat(dmdataOne.size/1024/1024)).toFixed(2).concat('M')) : (parseFloat(dmdataOne.size/1024)).toFixed(2).concat('K')) : (parseFloat(dmdataOne.size)).toFixed(2).concat('B')}}</td>
              </tr>
              <tr>
                <th>数据发布</th>
                <td>{{dmdataOne.publisher}}</td>
              </tr>
              <tr>
                <th>版权信息</th>
                <td>{{dmdataOne.copyright}}</td>
              </tr>
            </table>
          </el-tab-pane>
          <el-tab-pane label="数据结构">
            <el-table
              id="dmdataOneTable"
              :data="dmdataOne.schema"
              style="width: 100%">
              <el-table-column
                type="index"
                width="120"
                label="序号"
              :index="indexMethod">
              </el-table-column>
              <el-table-column
                prop="name"
                label="字段名称"
                width="180">
              </el-table-column>
              <el-table-column
                prop="type"
                label="字段类型"
                width="180">
                <template slot-scope="scope">{{scope.row.type ==0 ? '文本' : scope.row.type ==1 ? '数值' : '日期'}}</template>
              </el-table-column>
              <el-table-column
                prop="length"
                label="字段长度">
              </el-table-column>
              <el-table-column
                prop="desc"
                label="字段描述">
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-dialog>
    </div>
</template>

<script type="text/ecmascript-6">
  import header from "../header/header";
  import {industry,getIndustryCountDmCountDownCount,getDmData,getEncryptTime,shu_lie_yun_url} from '@/common/js/request';
  import axios from 'axios';
  // industry
  export default {
    data(){
      return{
        dialogVisible: false,
        tableData:[
          {
            name:'name',
            type:'type',
            length:'length',
            desc:'desc'
          }
        ],
        clickIndex:-1,
        total:0,
        page:1,
        offset:10,
        industryId:null,
        dmName:null,
        _queryStr : null, // 查询条件
        bannerData:[],
        industryData:null,
        dmdata:null,
        dmdataOne:[],
        descImg:'', // 弹出框图片
        dialogTitle:'',
        ActiveUl:true,
        ActiveNo:false
      }
    },
    beforeRouteEnter: (to, from, next) => {
      next(vm => {
        vm.page=1;
        vm.offset=10;
        vm.init();
      });
    },
    components: {
      "h-eader": header
    },
    methods: {
      // 所有行业
      allIndustry(){
        if(this.ActiveUl==true){
          this.ActiveUl = !this.ActiveUl
          this.ActiveNo= !this.ActiveNo
        }
        else{
          this.ActiveUl = !this.ActiveUl
          this.ActiveNo= !this.ActiveNo
        }
      //   // this.getIndustry(null)
      //   if(this.industryData.length>20){
      //     // console.log(this.industryDataSome.splice(0,20))
      //     this.industryData=this.industryDataSome.splice(0,20)
      //     console.log(this.industryDataSome)
      //   }
      //   else{
      //     this.industryData=this.industryDataSome
      //   }
      //   // industryDataSome
      //   // this.industryData.forEach((value, index)=> {
      //   //   if(index>20){
      //   //     index=index-20
      //   //   }else{
      //   //     index=index
      //   //   }
      //   // })
      },
      cancleShow:function(){
        this.dialogVisible = !this.dialogVisible;
      },
      dialogDesc(item){
        console.log("传输的数据")
        console.log(item)
        //图片
        this.descImg=item.imgPath
        // 弹出框名称
        this.dialogTitle=item.name
        this.dialogVisible=true;
        item.schema = JSON.parse(item.schema);
        this.dmdataOne = item;
      },
      handleSizeChange(val) {
        console.log(val);
        this.offset = val;
        this.getDmData2(this.industryId);
      },
      handleCurrentChange(val) {
        this.page = val;
        this.getDmData2(this.industryId);
      },
      indexMethod(index) {
        return index + 1;
      },
      init(){
        this.getBannderData();
        this.getIndustry(null);
        this._getDmData(null,-1);
      },
      getIndustry(key){
        industry(key,null).then(res=>{
          if(res.status==200){
            var industryData = res.data.data;
            industryData.map(item=>{
              item.imgpath = res.data.cdn+"/dm/industry/"+item.id+"?time="+Math.random()*10000000;
            });
            this.industryData = industryData;
            //this.industryId = industryData[0].id;
            //this._getDmData(this.industryId);
          }
        })
      },
      dateFormat:function(date){
        var yyyy = date.getFullYear();
        var mm = (date.getMonth()+1) < 9 ? "0"+(date.getMonth()+1) : (date.getMonth()+1);
        var dd = date.getDate() < 9 ? "0"+date.getDate() : date.getDate()
        return yyyy+"-"+mm+"-"+dd;
      },
      getDmData2(industryId){
        //获取
        getDmData(industryId,this.page,this.offset,this._queryStr).then(res=>{

          if (res.status === 200) {
            this.total = res.data.count;
            var dmdataArray = res.data.data;
            dmdataArray.map(item => {
              item.imgPath = res.data.imgPath + "/dm/data/" + item.id + "dmpic?time="+Math.random()*1000000;
              var date = new Date(item.createTime);
              item.createTime = this.dateFormat(date);
              if (item.size == null ) {
                item.size = 0;
              }
            });
            this.dmdata = dmdataArray;
            this.dmName = this._queryStr;
          }
        })
      },
      _getDmData(industryId,idx){
        //this.industryId = industryId;
        this.dmName = null;
        if(this.clickIndex==idx){
          this.clickIndex = -1;
          this.industryId=null;
          industryId = null;
        } else {
          this.clickIndex = idx;
          this.industryId = industryId;
        }
        this.page = 1;
        this.getDmData2(industryId);
      },
      download(id){
        //获取服务器上当前时间，并进行加密
        var userStr = localStorage["user"];
        if(typeof userStr == 'undefined'){
          this.$message({
            message: '请先登录',
            type: 'warning'
          });
          return;
        }
        var obj = this;
        var user = JSON.parse(localStorage["user"]);
        var userId = user.userId;
        getEncryptTime(userId).then(res=>{
          if(res.status==200){
            var encrypt = res.data;
            //校验是否可以下载
            var url = shu_lie_yun_url+"/dmdata/admin/download?id="+id+"&token="+encodeURIComponent(encrypt);
            axios.defaults.headers = null;
            axios.get(url).then(res=>{
              if(res.data.errorcode==4){
                //校验成功
                window.location.href=res.data.url;
              } else if(res.data.errorcode==0){
                //超过允许下载时间
                obj.$message({
                  type:'error',
                  message:'超过允许下载的时间限制'
                });
              } else if(res.data.errorcode==1){
                //未登录
                obj.$message({
                  type:'error',
                  message:'未登录，请先登录'
                });
              } else if(res.data.errorcode==2){
                // 该数据集不存在
                obj.$message({
                  type:'error',
                  message:'该数据集不存在'
                });
              } else if(res.data.errorcode==3){
                // 系统异常，请稍后重试
                obj.$message({
                  type:'error',
                  message:'系统异常，请稍后重试'
                });
              }
              axios.defaults.headers = {
                withCredentials: true,
                Authorization:'Bearer '+(localStorage['token'] ? localStorage['token'] : '')
              };
            });
          }
        })
      },
      getBannderData:function(){
        var obj = this;
        getIndustryCountDmCountDownCount().then(res=>{
          if(res.status==200){
            obj.bannerData = res.data;
          }
        });
        setInterval(()=>{
          getIndustryCountDmCountDownCount().then(res=>{
            if(res.status==200){
              obj.bannerData = res.data;
            }
          })
        },1000*60);

      },
      search:function(){
        this.page = 1;
        // 只有点击搜索时才变更查询条件
        this._queryStr = this.dmName;
        this.getDmData2(this.industryId);
      }
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus">
  @import "../../common/css/deepIndex.styl"
</style>
<style type="text/css">
  @import "../../common/css/banji.css";
  #dialog .el-dialog__title{
    white-space: pre-wrap !important;
  }
</style>
