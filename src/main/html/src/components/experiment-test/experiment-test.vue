<template>
  <div id="test">
    <h1 id="htTitle" class="kong">{{courseName}}/ {{moduleName}}/ {{experimentName}}</h1>

    <!--提交答案后的数据-->
    <!--表格数据-->
    <el-table id="Table"
              tooltip-effect="dark"
              :data="tableData">
      <el-table-column
        prop="name"
        show-overflow-tooltip
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
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="danger"
            v-show="scope.row.name !=''"
            @click="tableHandleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>

    </el-table>
    <!--选择答案以及提交答案按钮-->
    <el-form :inline="true" id="testBtnInline"  class="demo-form-inline" >
      <el-form-item>
        <el-upload
          class="upload-demo"
          ref="upload"
          :action="actionUrl"
          :on-success="uploadSuccess"
          :on-remove="handleRemove"
          :auto-upload="false"
          :http-request="customUploadHandler"
          :multiple="false"
          :limit="1"
          :on-exceed="handleExceed"
          :file-list="fileList"
          :disabled="selectProgrmDisabled"
          v-loading="loading"
          style="display: inline-block;vertical-align: top"
          >
          <!--当实验为编程类时，显示-->
          <el-button slot="trigger"
                     class="button-green"
                     v-show="programmShow"
                    :disabled="selectProgrmDisabled">选择答案</el-button>
          <!--当实验为数猎云时，显示-->
          <el-button class="button-green"
                     @click="selectShuLieAnswer()"
                     v-show="shuLieShow"
                    :disabled="selectShuLieDisabled">选择答案</el-button>
          <el-button class="button-green"
                     @click="submitAnswer"
                     id="submitBtn"
                     :disabled="submitBtnDisabled"
                     >提交答案</el-button>
          <ul v-if="dialogNameShow" class="el-upload-list__item is-ready">
            <i class="el-icon-document"></i>
            {{dialogSelectTableName}}
            <i class="el-icon-close" @click="closeTableName"></i>
          </ul>
        </el-upload>

      </el-form-item>
    </el-form>

    <!--选择一个表作为答案的弹出框-->
    <!--title="请选择一个表做为答案"-->
    <el-dialog
      :visible.sync="dialogSelect"
      width="50%"
      style="margin-top: -5vh">
      <el-table
        id="managerSelectTableData"
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
          :label="timeLable">
          >
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
         <el-button type="primary" @click="importSure">确 定</el-button>
        <el-button @click="dialogSelect = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script type="text/ecmascript-6">
  import {getDataShireDBList,getExperimentAnswer,deleteExperimentAnswer,submitExperimentAnswer} from '@/common/js/request';
  import axios from 'axios';
  export default {
    data(){
      const dialogListData = [{
        id: 1,
        label: '一级 1',
        children: [{
          id: 4,
          label: '二级 1-1',
          children: [{
            id: 9,
            label: '三级 1-1-1'
          }, {
            id: 10,
            label: '三级 1-1-2'
          }]
        }]
      }];
      return {
        courseName:'',
        fileLable:'',
        moduleName:'',
        experimentName:'',
        experimentId:'',
        experimentType:'',
        courseId:'',
        actionUrl:null,
        uploadData:{},
        dbName:'',
        fileList: [],
        //提交答案后表格数据
        tableData:[
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
        // 选择一个表作为答案的弹出框
        dialogSelectTable:[
          {
            header:'',
            size:'',
            date:''
          }
        ],
        dialogListData: JSON.parse(JSON.stringify(dialogListData)),
        programmShow:false, // 编程类
        shuLieShow:false, // 数猎云
        fileList: [],
        timeLable:'',
        dialogSelectTableName:'',
        multipleSelection:[],//选择的数据
        dialogNameShow:false,
        sizeShow:false,//是否显示表格'这一列'
        selectProgrmDisabled:false, // 编程类选择答案
        selectShuLieDisabled:false, // 数猎云选择答案
        submitBtnDisabled:false, //提交答案
        loading:false
      }
    },
    created:function(){
      this.actionUrl="/admin/experiment/submitAnswer";
      this.courseName=this.$route.query.courseName;
      this.courseId=this.$route.query.courseId;
      //章节名称
      this.moduleName=this.$route.query.moduleName;
      //实验名称
      this.experimentName=this.$route.query.experimentName;
      // 实验id为
      this.experimentId =this.$route.query.id;
      var cloudwareType=this.$route.query.cloudwareType;
      if(cloudwareType=="图形化－数猎云"){
        this.experimentType=1;
      }else{
        this.experimentType=0;
      }
      // 传值给标题
      this.findExperimentAnswer(this.courseId,this.experimentId,this.experimentType,this.courseName);
      },
    methods:{
      //数猎云选择答案
      selectShuLieAnswer(){
        // 实验类型
        var cloudwareType=this.$route.query.cloudwareType;
        if(cloudwareType=="图形化－数猎云"){
          //查询课程对应cloudDB中表
          var user = JSON.parse(localStorage["user"]);
          var phone = user.username;
          getDataShireDBList(phone,true,this.courseName,1).then(res=>{
            if(res.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            if(res.data.errorCode==0){
              this.dialogSelectTable = res.data.data.tableInfo;
              console.log(res.data.data.tableInfo);
            }
          });
          this.dialogSelect=true;
        }
        else{

        }
      },
      // 提交答案
      submitAnswer(){
        console.log(this.experimentType);
        //1 为数猎云类型
        if(this.experimentType==1){
            if(this.dialogSelectTableName=='' || this.dialogSelectTableName==null || this.dialogNameShow==false){
              this.$message({
                type:'error',
                message:'请先选择答案'
              });
              return;
            }

        }
        //2为编程类型
        else{
          if(this.$refs.upload !=undefined){
            if(this.$refs.upload.uploadFiles.length==0){
              this.$message({
                type:'error',
                message:'请先选择答案!'
              });
              return;
            }
            //获取文件类型
            var fileName=this.$refs.upload.uploadFiles[0].name;
            if(fileName.lastIndexOf(".")>1){
              var str=fileName.substr(fileName.lastIndexOf(".")+1,fileName.length);
              if(str!="csv"){
                this.$message({
                  type:'error',
                  message:'答案文件限定csv格式，请重新选择!'
                });
                return;
              }
            }else{
              this.$message({
                type:'error',
                message:'答案文件限定csv格式，请重新选择!'
              });
              return;
            }

          }
        }
        this.$confirm('是否确认提交答案!', {
          cancelButtonText: '取消',
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          if(this.experimentType==0){
            this.loading=true;
            this.$refs.upload.submit();
          }else{
            submitExperimentAnswer(this.courseId,this.experimentId,this.experimentType,this.dialogSelectTableName,this.courseName).then(res=>{
              if(res.status==200){
                  if(res.data.errorCode==0){
                      this.$message({
                        title:"提示",
                        message:"上传成功",
                        type:'success'
                      });
                      // 按钮禁用
                      this.commitBtnDisabled();
                      // 显示的表名
                      this.dialogNameShow=false;
                      this.dialogSelectTableName="";
                      this.findExperimentAnswer(this.courseId,this.experimentId,this.experimentType,this.courseName);
                      return;
                  }else{
                    this.$message({
                      title:"提示",
                      message:res.data.message,
                      type:'error'
                    });
                    this.dialogNameShow=false;
                    this.dialogSelectTableName="";
                    return;
                  }
                }
            })
          }

        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消提交'
          });

        });
      },
      uploadSuccess:function(response){
        this.fileList=[];
        this.$refs.upload.clearFiles();
        this.dialogNameShow=false;
        this.loading=false;
        this.findExperimentAnswer(this.courseId,this.experimentId,this.experimentType,this.courseName);
      },
      //自定义上传文件
      customUploadHandler:function(item){
        if(this.experimentType==0){
          var filename=item.file.name;
          console.log("提交答案!"+filename);
          if(filename.length>80){
            this.$message({
              title: "提示",
              message: "文件名不能超过80个字符!",
              type: 'error'
            });
            this.loading=false;
            return;
          }
          var url = item.action;
          var file = item.file;
          console.log("提交!"+file);
          //判断文件大小，目前允许20M
          if(file.size>20971520){
            this.$message({
              title:"提示",
              message:"答案文件大小限定20MB，请重新选择!",
              type:'error'
            });
            this.loading=false;
            return;
          }
          let param = new FormData();
          // 添加请求头
          param.append('courseId', this.courseId);
          param.append('experimentId', this.experimentId);
          param.append('type', this.experimentType);
          param.append('courseName', this.courseName);
          param.append('tableName', "");
          param.append('file', file);
          axios.post(url, param).then(res => {
            console.log(res);
            if(res.status==200){
              if(res.data.errorCode==0){
                  this.$message({
                    title:"提示",
                    message:"上传成功!",
                    type:'success'
                  });
                  // 按钮置灰
                  this.loading=false;
                  this.commitBtnDisabled();
                  this.fileList=[];
                  this.$refs.upload.clearFiles();
                  this.findExperimentAnswer(this.courseId,this.experimentId,this.experimentType,this.courseName);
              }else{
                this.$message({
                  title:"提示",
                  message:res.data.message,
                  type:'error'
                });
                this.loading=false;
                return;
              }

            }
          });

        }
      },
      // 上传之前的钩子
      beforeAvatarUpload(file){
        var fileName=file.name;
        var str=fileName.substr(fileName.indexOf(".")+1,fileName.length);
        if(str!="csv"){
          this.$message({
            message: '警告哦，只能选择csv文件!',
            type: 'error'
          });
          this.fileList=[];
          return;
        }
      },
      // 提交答案
      importSure(){
        // 如果选择的数据超过一行
/*        if(this.multipleSelection.length >1){
          this.$message({
            message: '警告哦，只能选择单行数据',
            type: 'warning'
          });
          this.dialogSelect=true
        }
        //如果没选择数据
        else if(this.multipleSelection.length ==0){
          this.dialogSelect=false
        }
        //如果选择单行数据
         else{*/
        if(this.multipleSelection.length ==0){
          this.dialogSelect=false
        } else if(this.dialogNameShow !="" ){
            this.$message({
              message: '您已选择过数据,请先删除，再重新选择!',
              type: 'warning'
            });
            this.dialogSelect=true
          }else{
            // 获取选择的数据
            console.log(this.multipleSelection);
            this.dialogSelectTableName=this.multipleSelection.name;
            this.dialogNameShow=true;
            // 关闭窗口
            this.dialogSelect=false;
          }
      },
      // 删除传递的表名
      closeTableName(){
        this.dialogNameShow=false;
      },
      // 提交答案，按钮置灰
      commitBtnDisabled:function(){
        var cloudwareType1=this.$route.query.cloudwareType;
        this.findExperimentAnswer(this.courseId,this.experimentId,this.experimentType,this.courseName);
        // console.log(this.tableData[0].name)
        if(cloudwareType1=="图形化－数猎云"){
          // 选择答案
          this.selectShuLieDisabled = true;
          // 提交答案
          this.submitBtnDisabled = true;
        }
        else{
          // 选择答案
          this.selectProgrmDisabled=true;
          // 提交答案
          this.submitBtnDisabled = true;
        }
      },
      // 点击删除按钮，按钮解禁
      deletBtnDisabled:function(){
        var cloudwareType2=this.$route.query.cloudwareType;
        if(cloudwareType2=="图形化－数猎云"){
          // 按钮置灰
          //编程类 文件名
          // this.selectProgrmDisabled=true
          // 数猎云 表名
          this.selectShuLieDisabled = false
          // 提交答案
          this.submitBtnDisabled = false
        }else{
                // 按钮置灰
                //编程类 文件名
                this.selectProgrmDisabled=false
                // 数猎云 表名
                // this.selectShuLieDisabled = false
                // 提交答案
                this.submitBtnDisabled = false
        }
      },
      // 提交答案后删除方法
      tableHandleDelete(index,row){
        this.$confirm('是否确定删除答案', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          var tableName=row.name;
          //数猎云实验
          deleteExperimentAnswer(this.courseId,this.experimentId,this.experimentType,tableName).then(res=>{
              if(res.status==200){
                if(res.data.errorCode==0){
                  this.$message({
                    type: 'success',
                    message: '删除成功!'
                  });
                  // 点击删除按钮，按钮激活
                  this.deletBtnDisabled()
                  // // 提交答案
                  // this.submitBtnDisabled=false
                  this.findExperimentAnswer(this.courseId,this.experimentId,this.experimentType,this.courseName);
                }
              }
          });
          // this.$message({
          //   type: 'success',
          //   message: '删除成功!'
          // });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      handleRemove(file, fileList) {
        // console.log(file, fileList);
      },
      // 点击列表
      handlePreview(file) {
        // console.log(file);
      },
      handleExceed(files, fileList) {
        this.$message.warning(`您已选择过文件,请先删除，再重新选择!`);
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },
      //查询答案
      findExperimentAnswer:function (courseId,experimentId,experimentType,courseName) {
        getExperimentAnswer(courseId,experimentId,experimentType,courseName).then(res=>{
          console.log(res);
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          if(res.data.errorCode==0){
            if(res.data.data.length==0){
              this.tableData=[];
              console.log(this.$route.query.cloudwareType);
              var cloudwareType=this.$route.query.cloudwareType;
                // 如果是数猎云类型
                if(cloudwareType=="图形化－数猎云"){
                  this.selectShuLieDisabled=false;
                  this.submitBtnDisabled=false;
                }
                // 如果是编程类
                else{
                  console.log("我是编程类我进来了");
                  this.selectProgrmDisabled=false;
                  this.submitBtnDisabled=false;
                }
            }
            else{
              this.tableData=res.data.data;
              console.log("数猎云类型为:");
              console.log(this.$route.query.cloudwareType);
              var cloudwareType=this.$route.query.cloudwareType;
                // 如果是数猎云类型
                if(cloudwareType=="图形化－数猎云"){
                  this.selectShuLieDisabled=true;
                  this.submitBtnDisabled=true;
                }
                // 如果是编程类
                else{
                  console.log("禁用上传按钮");
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
         console.log("选择的数据开始");
        console.log( val);
      }
    },
    beforeRouteEnter: (to, from, next) => {
      console.log("我是路由，我进入了");
      next(vm => {
        //标题
        var cloudwareType=vm.$route.query.cloudwareType;
        if(cloudwareType=='图形化－数猎云'){
          vm.experimentType=1;
        }else{
          vm.experimentType=0;
        }
        console.log(vm.experimentType+"类型");
        // 课程名称
        vm.courseName=vm.$route.query.courseName;
        vm.courseId=vm.$route.query.courseId;
        //章节名称
        vm.moduleName=vm.$route.query.moduleName;
        //实验名称
        vm.experimentName=vm.$route.query.experimentName;
        // 实验id为
        vm.experimentId =vm.$route.query.id;
        vm.findExperimentAnswer(vm.courseId,vm.experimentId,vm.experimentType,vm.courseName);
        // 数猎云 table
        if(vm.experimentType==1){
          vm.fileLable="表名"
          vm.shuLieShow=true
          vm.programmShow=false
          vm.timeLable="创建时间"
          vm.sizeShow=false
        }
        // 如果为编程类
        else{
          vm.fileLable="文件名"
          vm.shuLieShow=false
          vm.programmShow=true
          vm.timeLable="修改时间"
          vm.sizeShow=true
        }
        // 清除上传记录
        vm.fileList=[];
        vm.dialogNameShow=''
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
      border-bottom 1px solid #ebeef5
      /*white-space normal*/
    #testBtnInline
      width 94% !important
      margin 20px auto
      padding-top 20px
    #Table
      width 94% !important
      height auto
      margin 20px auto 10px
      text-align center
    .el-table
      th > .cell
        text-align center
        font 1.1em bolder
      .cell
        text-align center
        white-space nowrap
        overflow hidden
        word-break break-all
        text-overflow ellipsis
        padding-right: 10px
    .custom-tree-node
      flex: 1;
      display: flex;
      /*align-items: center;*/
      justify-content: space-between;
      font-size: 14px;
    .el-button--primary.is-disabled, .el-button--primary.is-disabled:hover, .el-button--primary.is-disabled:focus, .el-button--primary.is-disabled:active
      color #c0c4cc
    .el-upload-list__item
      padding 0px 20px 0px 20px
      word-wrap break-word !important
      word-break:break-all !important
  /*padding-right: 8px;*/
</style>
<style type="text/css">
  @import "../../common/css/banji.css";
</style>

