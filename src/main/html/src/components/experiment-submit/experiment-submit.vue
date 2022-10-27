<template>
  <div id="test">
    <h-eader ></h-eader>
    <h1 id="htTitle" class="kong">{{courseName}}/ {{moduleName}}/ {{experimentName}}</h1>

    <!--提交答案后的数据-->
    <!--表格数据-->
    <el-table id="Table"
              :data="tableData">
      <el-table-column
        prop="fileName"
        :label="fileLable">
      </el-table-column>
      <el-table-column
        prop="size"
        v-if="sizeShow"
        label="大小">
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{parseFloat(scope.row.size/1024) > 1 ? (parseFloat(scope.row.size/1024/1024) >1 ? ((parseFloat(scope.row.size/1024/1024)).toFixed(2).concat('M')) : (parseFloat(scope.row.size/1024)).toFixed(2).concat('K')) : (parseFloat(scope.row.size)).toFixed(2).concat('B')}}</span>
        </template>
      </el-table-column>
      <!--<el-table-column label="操作">-->
        <!--<template slot-scope="scope">-->
          <!--<el-button-->
            <!--size="mini"-->
            <!--type="danger"-->
            <!--v-if="scope.row.length !=0"-->
            <!--@click="tableHandleDelete(scope.$index, scope.row)">删除</el-button>-->
        <!--</template>-->
      <!--</el-table-column>-->

    </el-table>
    <!--选择答案以及提交答案按钮-->
    <el-form :inline="true" id="btnInline"  class="demo-form-inline" >
      <el-form-item>
        <el-button class="button-green"
                   @click="selectAnswer()"
                   :disabled="selectProgrmDisabled">选择答案</el-button>
        <el-button class="button-green"
                   @click="submitAnswer()"
                   id="submitBtn"
                   :disabled="submitBtnDisabled">提交答案</el-button>
      </el-form-item>
      <p v-if="dialogNameShow" class="el-upload-list__item is-ready">
        <i class="el-icon-document"></i>
        <span class="kong">{{dialogSelectFileName}}</span>
        <i class="el-icon-close" @click="closeTableName"></i>
      </p>
    </el-form>


    <!--选择一个表作为答案的弹出框-->
    <!--title="请选择一个表作为答案"-->
    <el-dialog
      :visible.sync="dialogSelect"
      width="60%"
      style="margin-top: -5vh">
      <el-table
        id="dialogSelectTable"
        :data="dialogSelectTable"
        style="width: 100%"
        highlight-current-row
        @current-change="handleSelectionChange">
        <el-table-column
          prop="name"
          label="表名"
          width="180">
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间">
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
         <el-button type="primary" @click="selectSure">确 定</el-button>
        <el-button @click="dialogSelect = false">取 消</el-button>
      </span>
    </el-dialog>
    <!--请选择一个文件做为答案-->
    <el-dialog
      id="dialogList"
      :visible.sync="dialogList"
      width="50%"
      style="margin-top: -5vh;">
      <div style="display: flex;padding-right: 8px;width: 100%;justify-content: space-between;margin-right: 10px;">
        <div style="margin-left: 24px;border: 0px solid red">文件名</div>
        <div style="width: 400px;margin-right: 18px;text-align: center;display: flex;">
          <div style="width: 50%;border: 0px solid red">大小</div>
          <div style="width: 50%;border: 0px solid red">修改时间</div>
        </div>
        <!--show-checkbox-->
        <!--node-key="id"-->
      </div>
      <el-tree
        :data="data"
        :load="loadNode"
        :lazy=true
        :props="props"
        id="dataListTable"
        @node-click="handleClick"
        :highlight-current="true"
        :check-on-click-node="true"
        ref="tree"
        :expand-on-click-node="false">
        <div class="custom-tree-node" slot-scope="{ node, data }" >
          <el-tooltip  effect="dark" style="text-overflow:unset"
                      :content="data.label"
                      placement="top-start"
                      :disabled="data.label.length<14">
            <div style="flex: 1;" id="fileTitle">
                <i ref="document" :class="{'el-icon-document':data.type==1,'el-icon-goods':data.type==2}" ></i>
                <span style="white-space: pre">{{data.label.substr(0,14)}}</span>
                <span v-if="data.label.length>14" style="margin-left: -4px;">...</span>
            </div>
          </el-tooltip>
          <div style="width: 400px;text-align: center;display: flex;border: 0px solid red">
             <div style="width: 50%;">{{parseFloat( data.size /1024) > 1 ? (parseFloat( data.size /1024/1024) >1 ? ((parseFloat( data.size /1024/1024)).toFixed(2).concat('M')) : (parseFloat( data.size /1024)).toFixed(2).concat('K')) : (parseFloat( data.size )).toFixed(2).concat('B')}}</div>
             <div style="width: 50%">{{ data.time }}</div>
          </div>
        </div>
      </el-tree>
      <span slot="footer" class="dialog-footer">
         <el-button type="primary" @click="selectListSure">确 定</el-button>
        <el-button @click="dialogList = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script type="text/ecmascript-6">
  import header from "../header/header";
  import {
    getDataShireDBList,
    getExperimentAnswer,
    getJobShareFileList,
    getStudentSubmitHomework,
    submitHomeworkOfOther,
    submitHomeworkOfShuLie
  } from '@/common/js/request';

  let id = 1000;
  export default {
    data(){
      const dialogListData = [{
        type: '',
        name: '',
        size: '',
        time: '',
        isLeaf:'leaf'
      }];
      const data = [];
      return {
       // data: JSON.parse(JSON.stringify(data)),
        data:[{
            type: '',
            name: '',
            size: '',
            time: '',
            isLeaf:''
          }],
        props:{
          label:'label',
          isLeaf:'leaf'
        },
        courseName:'',
        fileLable:'',
        moduleName:'',
        experimentName:'',
        courseId:'',
        experimentType:'',
        path:'',
        dialogNameShow:false,
        dialogSelectFileName:'',
        userId:localStorage['user'] ? JSON.parse(localStorage['user']).userId : '',
        //提交答案后表格数据
        tableData:[
          {
            fileName:'',
            size:''
          }
        ],
        //选择答案后表格数据
        selectTable:[
          {
            file:'',
            large:''
          }
        ],
        dialogSelectList:[
          {
            file:'',
            large:''
          }
        ],
        dialogSelect:false,
        dialogList:false,
        // 选择一个表作为答案的弹出框
        dialogSelectTable:[
          {
            header:'',
            size:'',
            date:''
          }
        ],
        dialogListData: JSON.parse(JSON.stringify(dialogListData)),
        sizeShow:false,//是否显示表格'这一列'
        className:'',//班级名称
        multipleSelection:[],
        multipleselectionList:[],
        experimentId:'', //实验id
        selectProgrmDisabled:false, //选择答案
        submitBtnDisabled:false, // 提交答案
        saveFilePath:'',
        selectList:[
          {
            label:'',
            type:''
          }
        ],
        tooltipDisabled:false
      }
        },
    components: {
      "h-eader": header
    },
    created:function(){
      var cloudwareType=this.$route.query.cloudwareType;
      if(cloudwareType=='图形化－数猎云'){
        this.experimentType=1;
      }
      else{
        this.experimentType=0;
        // tooltipDisabled
      }
      // 课程名称
      this.courseName=this.$route.query.courseName;
      //章节名称
      this.moduleName=this.$route.query.moduleName;
      //实验名称
      this.experimentName=this.$route.query.experimentName;
      this.className=this.$route.query.className;
      this.courseId=this.$route.query.courseId;
      // 实验id为
      this.experimentId=this.$route.query.experimentId;
     this._getStudentSubmitHomework()
    },
    methods:{
      loadNode(node, resolve){
        console.log("进入");
        var data=[];
        if(node.level==0){
          this.path="/";
          getJobShareFileList(this.userId,this.path,1).then(res=>{
            if(res.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            if(res.data.errorCode==0){
              if(res.data.data.length==0){
               data=[];
              }else{
               data=res.data.data;
              }
            }
            data.forEach((value)=>{
              if(value.type==1){
                value.leaf=true;
              }else{
                value.leaf=false;
              }

            });
            console.log(data);
            return resolve(data);
          });
        }
        else{
          this.path="/"+this.getParentNode(node,"/");
          console.log("最终路径"+this.path);
          getJobShareFileList(this.userId,this.path,1).then(res=>{
            if(res.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            if(res.data.errorCode==0){
              if(res.data.data.length==0){
                data=[];
              }else{
                data=res.data.data;
              }
            }
            console.log("加载数据");
            console.log(data);
            data.forEach((value)=>{
              if(value.type==1){
                value.leaf=true;
              }else{
                value.leaf=false;
              }
            });
            return resolve(data);
          });

        }
      },
      handleClick:function(data,node){
       console.log("点击节点");
        this.selectList[0].label=data.label;
        this.selectList[0].type=data.type;
        var path="/"+this.getParentNode(node,"/");
         path=path.substr(0,path.length-1);
        this.saveFilePath=path;
        console.log("保存路径"+path);
      },
      getParentNode:function(node,path){
        if(node.parent!=null){
          if(path=="/"){
            path=node.label+path;
          }else{
            path=node.label+"/"+path;
          }
          return this.getParentNode(node.parent,path);
        }
        return path;
      },
      //选择答案
      selectAnswer(){
        // 实验类型
        var cloudwareType=this.$route.query.cloudwareType;
        // 如果实验类型为数猎云
        if(cloudwareType=="图形化－数猎云") {
          //查询课程对应cloudDB中表
          var user = JSON.parse(localStorage["user"]);
          console.log("用户名为:" + user.username)
          var phone = user.username;
          getDataShireDBList(phone, false, this.courseName,1).then(res => {
            console.log(res);
            if (res.data == "alreadyResetPassword") {
              this.$message({
                type: 'warning',
                message: '请重新登录'
              });
              return;
            }
            if (res.data.errorCode == 0) {
              this.dialogSelectTable = res.data.data.tableInfo;
            }
            this.dialogSelect = true;
            this.dialogList = false;
          })
        }
        // 如果实验类型为编程类
        else{
          console.log("编程类");
          //编程类  查询个人共享文件夹
          this.path="/";
          this.dialogSelect=false;
          this.dialogList=true;
          if(this.$refs.tree!=undefined){
            //this.$refs.tree.data={};
            this._getShareFileList(this.userId,"/",1);
          }
          console.log(this.$refs.tree);
          // 点击选择答案按钮，清除之前选择的数据
          this.selectList[0].label=""
        }
      },
      //查询共享文件夹中文件
      _getShareFileList:function (userId,path,type) {
        console.log("开始查询文件");
        getJobShareFileList(userId,path,type).then(res=>{
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          if(res.data.errorCode==0){
            if(res.data.data.length==0){
              this.data=[];
            }else{
              this.data=res.data.data;
              this.data.forEach(value=>{
                console.log(value);
                if(value.type==2){
                  value.leaf=false;
                }else{
                  value.leaf=true;
                }
              })
            }
          }
        });
      },
      // 提交答案
      submitAnswer(){
        if(this.dialogSelectFileName=="" || this.dialogSelectFileName==null || this.dialogNameShow==false){
          if(this.experimentType==1){
            this.$message({
              type: 'error',
              message: '请先选择表哦!'
            });
            return;
          }else if(this.experimentType==0){
            this.$message({
              type: 'error',
              message: '请先选择文件哦!'
            });
            return;
          }
        }
        this.$confirm('是否确认提交答案!', {
          cancelButtonText: '取消',
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
            if(this.experimentType==1){
              submitHomeworkOfShuLie(this.courseId,this.experimentId,this.userId,this.dialogSelectFileName,this.className).then(res=>{
                  if(res.status==200){
                    if(res.data.errorCode==0){
                      this.$message({
                        type: 'success',
                        message: '提交成功!'
                      });
                      this.dialogNameShow=false;
                      this.dialogSelectFileName="";
                      this._getStudentSubmitHomework();
                      return;
                    }else{
                      this.$message({
                        type: 'error',
                        message: res.data.message
                      });
                    }
                  }
              });
            }else{
              console.log("文件路径"+this.saveFilePath);
              submitHomeworkOfOther(this.courseId,this.experimentId,this.userId,this.saveFilePath,this.className).then(res=>{
                console.log(res);
                if(res.status==200){
                  if(res.data.errorCode==0){
                      this.$message({
                        type: 'success',
                        message: '提交成功!'
                      });
                      this.dialogNameShow=false;
                      this.dialogSelectFileName="";
                      this._getStudentSubmitHomework();
                      return;
                  }else{
                    this.$message({
                      type: 'error',
                      message: res.data.message
                    });
                  }
                }
              });
            }
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消提交答案!'
          });
        });
      },
      //选择答案后的删除方法
      selectHandleDelete(){
        this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      _getStudentSubmitHomework:function (){
        getStudentSubmitHomework(this.className,this.courseId,this.experimentId,this.userId,this.experimentType).then(res=>{
          console.log(res);
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
            if(res.status==200){
              if(res.data.errorCode==0){
                if(res.data.data.length==0){
                  console.log("aa");
                  this.tableData=[];
                  this.selectProgrmDisabled=false;
                  this.submitBtnDisabled=false;
                }else{
                  console.log("bb");
                  this.tableData=res.data.data;
                  this.selectProgrmDisabled=true;
                  this.submitBtnDisabled=true;
                }
              }
            }
        });
      },
      // 复选框选择
      handleSelectionChange(val){
        this.multipleSelection = val;
      },
      // 确定选择答案
      selectSure(){
        console.log(this.multipleSelection);
        // 如果选择的数据超过一行
        /*if(this.multipleSelection.length >1){
          this.$message({
            message: '警告哦，只能选择单行数据',
            type: 'warning'
          });
          this.dialogSelect=true
        }*/
        //如果没选择数据
        //else if(this.multipleSelection.length ==1){

          if(this.multipleSelection=="" || this.multipleSelection==null){
            this.dialogSelect=false;
          }else if(this.dialogNameShow !="" ){
            this.$message({
              message: '您已选择过数据,请先删除，再重新选择!',
              type: 'warning'
            });
            this.dialogSelect=true;
            return;
          }else{
            this.dialogSelect=false;
            this.dialogNameShow=true;
            this.dialogSelectFileName=this.multipleSelection.name;
            console.log("选择的数据为："+this.multipleSelection);
          }
       // }
      },
      // 下拉列表确定
      selectListSure(){
        // debugger
        var fileName=this.selectList[0].label
        var str = fileName.substr(fileName.lastIndexOf("."), fileName.length);
        console.log(this.dialogNameShow)
          // 判断是不是csv文件
        if(this.selectList[0].type==1){
            if(str==".csv"){
              //如果选择的文件为空
              if(this.selectList[0].label==""){
                this.dialogList=false;
              }
              // 如果选择的文件不为空
              else if(this.selectList[0].label !="" && this.selectList[0].label !=undefined){
                // 如果下方显示表名
                if(this.dialogSelectFileName !="" && this.dialogSelectFileName !=undefined){
                  this.$message({
                    message: '您已选择过数据,请先删除，再重新选择!',
                    type: 'warning'
                  });
                }
                else{
                  this.dialogNameShow=true;
                  this.dialogSelectFileName=this.selectList[0].label;
                  //this.saveFilePath=this.selectList[0].label;
                  this.dialogList=false;
                }
              }

            }
            // 其他类型文件
            else if(str !=".csv" && fileName !=""){
                this.$message({
                  message: '答案文件限定csv格式，请重新选择!',
                  type: 'warning'
                });
                // this.dialogList=false;
              }else{
              this.dialogList=false;
            }
          }
         else if(this.selectList[0].type==2){
            this.$message({
              message: '警告哦,不能选择文件夹',
              type: 'warning'
            });

          }
          else{
          this.dialogList=false;
        }
      },
      // 删除传递的表名
      closeTableName(){
        this.selectList[0].label=""
        this.dialogSelectFileName=""
        this.dialogNameShow=false
        // this.dialogSelectFileName=""

      }
    },
    beforeRouteEnter: (to, from, next) => {
      console.log("我是路由，我进入了");
      next(vm => {
        //标题传值
        var cloudwareType=vm.$route.query.cloudwareType;
        if(cloudwareType=='图形化－数猎云'){
          vm.experimentType=1;
        }else{
          vm.experimentType=0;
        }
        // 课程名称
        vm.courseName=vm.$route.query.courseName;
        //章节名称
        vm.moduleName=vm.$route.query.moduleName;
        //实验名称
        vm.experimentName=vm.$route.query.experimentName;
        vm.className=vm.$route.query.className;
        // 实验id为
        vm.courseId=vm.$route.query.courseId;
        console.log("课程id为:"+vm.courseId);
        // 实验id为;
        vm.experimentId=vm.$route.query.experimentId;
        console.log("实验id为:"+ vm.experimentId);
        //数据类型判断
        if(cloudwareType=="图形化－数猎云"){
          vm.sizeShow=false;
          vm.fileLable="表名";
          vm.sizeShow=false;
        }else{
          vm.sizeShow=true;
          vm.fileLable="文件名";
          vm.sizeShow=true;
          // 如果是其他类型的话，调用方法
          vm.dialogNameShow=false
          vm.dialogSelectFileName=""
        }

      });
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus">
  #test
    #htTitle
      font-size 22px
      margin 10px 3% 0px
      font-family  "Microsoft YaHei"
      line-height 30px
      padding-bottom 15px
      margin-top 60px
      border-bottom 1px solid #ebeef5
      /*white-space normal*/
    #btnInline
      margin-left 3%
      display inline-block
      vertical-align top
      /*width 94% !important*/
      /*margin 20px auto*/
      /*padding-top 20px*/
    #Table,#selectTable
      width 94% !important
      height auto
      margin 20px auto 10px
      text-align center
    .el-table
      th > .cell
        text-align center
        font 1.1em bolder
      .cell
        white-space nowrap
        overflow hidden
        word-break break-all
        text-overflow ellipsis
        padding-right: 10px
        text-align center
    .custom-tree-node
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-size: 14px;
      width 100%
      overflow-x auto
    .el-upload-list__item
      padding 0px 20px 0px 20px
    #dialogList
      .el-dialog
        white-space normal !important
    /*#dataListTable*/
      /*.item*/
        /*word-wrap break-word*/
        /*width 100px*/
</style>
<style type="text/css">
  @import "../../common/css/banji.css";
  .el-tooltip__popper{
    white-space: pre;
  }
</style>
