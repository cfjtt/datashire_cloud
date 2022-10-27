<template>
    <div>
      <!--头部标题-->
   <!--   <div id="header">
        <h3 class="page-title">数据交易管理</h3>
      </div>-->
      <!--中间部分-->
      <div id="dataContent">
        <div id="dataMenu">
          <div style="display: flex;width: 100%;height: auto;margin-bottom: 5px;">
            <el-button type="primary" class="newFile" @click="creatNewFile" v-if="isFolder">新建文件夹</el-button>
            <el-input style="width: 183px;" placeholder="文件夹关键字" v-on:input="inputFolder" v-model="findFolder"></el-input>
          </div>
          <ul class="dataMenuUl btn-group-vertical" role="group">
           <li  @click="findAllDoc" :class="{active:activeClass==-1}" v-if="isShowAllDoc">所有文档</li>
            <li v-for="(item,idx) in folderData" ref="dataMenuLi" :class="{active:item.id==activeIndex}" @click="_getFolderDoc(item.id,item.name,idx)" :key="idx">
              <el-tooltip class="item" effect="dark"
                          :disabled="item.name.length<9"
                          :content="item.name"
                          placement="top-start">
                <div id="dataMenuLeft">
                  <i class="el-icon-goods">
                    <span>{{item.name}}</span>
                  <!--<span>{{item.name.substr(0,10)}}</span>-->
                  <!--<span v-show="item.name.length>=10">...</span>-->
                  </i>
                </div>
              </el-tooltip>
              <div id="editFile" v-if="isProjectUser">
                <el-button-group>
                  <el-button   size="mini" @click="editData(item.name)">编辑</el-button>
                  <el-button type="danger" size="mini" @click="deletData(item.id)">删除</el-button>
                </el-button-group>
              </div>
            </li>
            <p v-if="isShowData" style="text-align: center;margin-top: 20px;">暂无数据</p>
          </ul>

        </div>
        <div id="dataRight">
          <div class="tableHeader" style="float: right;">
            <el-input placeholder="请输入关键字" style="width: 188px;margin-bottom: 5px;" v-model="keywordStr" @keyup.enter.native="search"></el-input>
            <el-button type="primary" @click="search" >搜索</el-button>
            <el-button type="primary"   @click="creatFile" v-if="isShow">创建文档</el-button>
          </div>
          <div id="dataTable">
            <div style="width: 100%;height: 100%;" class="dataTable">
              <el-table :data="docData" id="dmData" >
                <el-table-column label="ID"  prop="id"></el-table-column>
                <el-table-column label="文档标题" width="220"  prop="docName">
                  <template slot-scope="alldata">
                  <el-tooltip class="item" effect="dark" :disabled="alldata.row.docName.length<12" :content="alldata.row.docName" placement="top-start">
                    <span>
                    <span>{{alldata.row.docName.substr(0,12)}}</span>
                      <span v-show="alldata.row.docName.length>=12">...</span>
                    </span>
                  </el-tooltip>
                  </template>
                </el-table-column>
                <el-table-column label="创建人"  prop="creatorName"></el-table-column>
                <el-table-column label="创建时间"  prop="createTime"></el-table-column>
                <el-table-column label="编辑人"  prop="editorName"></el-table-column>
                <el-table-column label="编辑时间" prop="updateTime"></el-table-column>
                <el-table-column label="操作" width="150">
                  <template slot-scope="scope">
                    <el-button-group>
                    <el-button
                      v-if="isShowDoc"
                      size="mini"
                      @click="updateFile(scope.$index, scope.row)">查看</el-button>
                    <el-button
                      v-if="isEditDoc"
                      size="mini"
                      @click="updateFile(scope.$index, scope.row)">编辑</el-button>
                    <el-button
                      v-if="isEditDoc"
                      size="mini"
                      type="danger"
                      @click.prevent.stop="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </el-button-group>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
          <div class="pull-left" style="clear: both;">
            <div class="block" style="max-width:35%;margin:20px 10% 0px;">
              <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="page"
                :page-sizes="[5, 10, 20, 50,100]"
                :page-size="offset"
                :pager-count="7"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total">
              </el-pagination>
            </div>
          </div>
        </div>

      </div>
      <!--新建文件夹弹出框-->
      <el-dialog
        :title="fileTitle"
        :visible.sync="NewFileVisible"
        width="40%">
        <el-form  ref="newFilerule"   :model="formInline" >
          <el-form-item label="名称"  label-width="120">
            <el-input v-model="addFolderName" maxlength="45" style="width: 90%"  placeholder="请输入文件夹名称,限定45个字符"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="NewFileSure()">确 定</el-button>
    <el-button @click="NewFileVisible = false">取 消</el-button>

  </span>
      </el-dialog>
      <!--新建文档-->
      <el-dialog
        title="创建文档"
        :visible.sync="NewDocVisible"
        width="40%">
        <el-form  ref="newDocrule"   :model="DocformInline" >
          <el-form-item label="文档标题"  label-width="120">
            <el-input v-model="addDocName" maxlength="45" style="width: 80%"  placeholder="请输入文档标题,限定45个字符"></el-input>
          </el-form-item>
          <el-form-item label="文档路径"  label-width="120">
          <!--  <el-input v-model="docUrl" readonly style="width: 80%"  ></el-input>-->
            <el-select filterable  v-model="value" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="NewDocSure()">创 建</el-button>
    <el-button @click="NewDocVisible = false">取 消</el-button>
  </span>
      </el-dialog>
    </div>
</template>

<script type="text/ecmascript-6">
  import {
    addFolderDoc,
    addProjectFolder,
    deleteDocFile,
    deleteProjectDoc,
    deleteProjectFolder,
    findFolderByProjectId,
    findFolderDoc,
    saveEditorProjectDoc,
    updateProjectDoc,
    updateProjectFolder,
    isProjectUser,
    findFolderByd,
    checkDocIsExists
  } from '@/common/js/request';

  export default {
       data () {
         return {
           folderData:[],
           folderDataTmp:[],
           docData:[],
           NewFileVisible:false, // 新建文件夹
           NewDocVisible: false, // 新建文档
           formInline:{
             user:''
           },
           userId:JSON.parse(localStorage["user"]).userId,
           DocformInline:{
             title:'',
             url:''
           }, //文档
           activeIndex:-1,
           page:1,
           offset:10,
           total:0,
           projectId:0,
           folderId:0,
           keywordStr:"",
           queryStr:'',
           findFolder:'',
           addFolderName:null,
           folderName:null,
           addDocName:null,
           docUrl:null,
           isshowFile:false,
           fileName:'', // 修改文件夹弹出框，文件名称
           fileVisible:false, // 编辑文件夹弹出框
           fileTitle:'',// 弹出框名称
           isAddFolder:'',
           oldFolderName:null,
           activeClass:-1,
           isShow:true,
           isProjectUser:true,
           isFolder:true,
           isEditDoc:false,
           isShowDoc:false,
           isShowAllDoc:true,
           options: [],
           value: '',
           isShowData:true
         }
       },
      created:function(){
         console.log("created");
          this.init();
      },
       methods:{
        init:function () {
          this.isShowAllDoc=true;
          this.keywordStr="";
          this.findFolder="";
          this.queryStr="";
          this.projectId=this.$route.query.projectId;
          this.folderId=this.$route.query.folderId;
          if(this.folderId==undefined || this.folderId==0){
            this.folderId=0;
            this.getFolder();
          }else{
            console.log("回转!");
            this.activeClass=1;
            this.activeIndex=this.folderId;
            this.getFolderByEditor();
            findFolderByd(this.folderId).then(res=>{
              console.log(res);
              if(res.status==200){
                this.options=[{
                  id:res.data.data.id,
                  name:res.data.data.name
                }];
                this.value=res.data.data.id;
              }
            });

          }
          this.getFolderDoc(this.folderId);
          this.isUser();
          console.log(this.folderId+"文件夹编号");
        },
         _findFolderAll:function () {

         },
        isUser:function () {
          isProjectUser(this.projectId,this.userId).then(res=>{
            console.log("是否有权限");
            console.log(res);
            if(res.status==200){
              if(res.data.data==1){
                this.isProjectUser=true;
             /*   if(this.folderId==0){
                  this.isShow=false;
                }else{
                  this.isShow=true;
                }*/
                this.isFolder=true;
                this.isEditDoc=true;
                this.isShowDoc=false;
              }else{
                this.isProjectUser=false;
                this.isShow=false;
                this.isFolder=false;
                this.isEditDoc=false;
                this.isShowDoc=true;
              }
            }
          })
        },
         findAllDoc:function () {
           this.activeClass=-1;
           this.folderId=0;
           this.keywordStr="";
           this.queryStr="";
           this.activeIndex=-1;
           this.page=1;
           findFolderByProjectId(this.projectId).then(res=>{
             if(res.data.errorCode == 0){
               this.options=res.data.data;
             }
           });
           this.getFolderDoc(this.folderId);
         },
         inputFolder:function () {
           // console.log(this.findFolder+"搜索");
            this.isShowAllDoc=false;
           if(this.findFolder==null || this.findFolder==""){
             this.isShowAllDoc=true;
             var dataFilter=JSON.parse(JSON.stringify(this.folderDataTmp));
             this.folderData=dataFilter;
             return;
           }
           let dataTmp=[];
           for(let i=0;i<this.folderDataTmp.length;i++){
             if(this.folderDataTmp[i].name.indexOf(this.findFolder)>=0){
               dataTmp.push(this.folderDataTmp[i]);
             }
           }
           this.folderData=dataTmp;
         },
         // 新建文件夹
         creatNewFile:function () {
          this.fileTitle="新建文件夹";
           this.addFolderName="";
           this.isAddFolder="1";
          this.NewFileVisible=true;
         },
         // 删除数据
         handleDelete:function(index,row) {
           this.$confirm('请注意，该文档将从系统被删除，是否确定执行此操作', '提示', {
             confirmButtonText: '确定',
             cancelButtonText: '取消',
             type: 'warning'
           }).then(() => {
             deleteProjectDoc(this.projectId,row.id,this.userId).then(res=>{
                if(res.status=200){
                  if(res.data.errorCode==0){
                    this.$message({
                      type: 'success',
                      message: '删除成功!'
                    });
                    this.page=1;
                    this.getFolderDoc(this.folderId);
                  }else{
                    this.$message({
                      title:"提示",
                      message:res.data.message,
                      type:'error'
                    });
                    this.page=1;
                    this.getFolderDoc(this.folderId);
                  }
                }
             });
           }).catch(() => {
             this.$message({
               type: 'info',
               message: '已取消删除'
             });
           });
         },
         // 切换菜单
         _getFolderDoc:function (folderId,folderName,idx) {
            this.isUser();
           this.activeClass=1;
           this.activeIndex=folderId;
           this.keywordStr="";
           this.folderId=folderId;
           this.folderName=folderName;
           this.options=[{
             id:folderId,
             name:folderName
           }];
           this.value=folderId;
           this.docUrl="/"+folderName;
           this.page=1;
           this.getFolderDoc(folderId);
         },
         dateFormat:function(date){
           var yyyy = date.getFullYear();
           var mm = (date.getMonth()+1) < 9 ? "0"+(date.getMonth()+1) : (date.getMonth()+1);
           var dd = date.getDate() < 9 ? "0"+date.getDate() : date.getDate()
           return yyyy+"-"+mm+"-"+dd;
         },
         getFolderDoc:function (folderId) {
           if(folderId==undefined){
             folderId=0;
           }
           console.log("查询文件夹编号"+folderId);
           console.log("当前页码"+this.page);
           console.log("当前条数"+this.offset);
           console.log("查询条件"+this.queryStr);
           findFolderDoc(this.projectId,folderId,this.page,this.offset,this.queryStr).then(res=>{
             console.log(res);
             if(res.data.errorCode==0){
               this.total=res.data.data.count;
               this.docData=res.data.data.docList;
               this.docData.map(item=>{
                 var createTime=new Date(item.createTime);
                  item.createTime=this.dateFormat(createTime);
                 var updateTime=new Date(item.updateTime);
                 item.updateTime=this.dateFormat(updateTime);
               });
               this.keywordStr = this.queryStr;
             }
           });
         },
         //获取所有文件夹
         getFolder:function () {
           this.activeIndex=-1;
           this.activeClass=-1;
           findFolderByProjectId(this.projectId).then(res=>{
             if(res.data.errorCode == 0){
              this.folderData=res.data.data;
              this.options=res.data.data;
              this.folderDataTmp=JSON.parse(JSON.stringify(this.folderData));
               // isShowData
               console.log("文件夹数据个数")
               console.log(this.folderDataTmp.length)
               //暂无数据
               if(this.folderDataTmp.length ==0){
                 this.isShowData=true
               }else{
                 this.isShowData=false
               }
              console.log("获取文件夹数据");
               console.log(res.data.data)
             }
           })
         },
         getFolderByEditor:function () {
           findFolderByProjectId(this.projectId).then(res=>{
             if(res.data.errorCode == 0){
               this.folderData=res.data.data;
               this.folderDataTmp=JSON.parse(JSON.stringify(this.folderData));
             }
           })
         },
         handleSizeChange:function(val) {
           console.log("切换页面");
           this.offset = val;
           this.page = 1;
           this.getFolderDoc(this.folderId);
         },
         handleCurrentChange:function(val) {
           console.log("切换页面1");
           this.page = val;
           this.getFolderDoc(this.folderId);
         },
         // 新建文件确定
         NewFileSure:function(){
           if(!this.fileValidatar()){
             return;
           }
            if(this.isAddFolder=="1"){
              addProjectFolder(this.projectId,this.addFolderName,this.userId).then(res=>{
                if(res.status==200){
                  if(res.data.errorCode==0){
                    this.$message({
                      title:"提示",
                      message:"创建成功",
                      type:'success'
                    });
                    this.NewFileVisible=false;
                    this.getFolder();
                    this.findAllDoc();
                  }else{
                    this.$message({
                      title:"提示",
                      message:res.data.message,
                      type:'error'
                    });
                  }
                }

              });
            }else if(this.isAddFolder=="0"){
              updateProjectFolder(this.projectId,this.oldFolderName,this.addFolderName,this.userId).then(res=>{
                if(res.status==200){
                  if(res.data.errorCode==0){
                    this.$message({
                      title:"提示",
                      message:"编辑成功",
                      type:'success'
                    });
                    this.NewFileVisible=false;
                    this.getFolder();
                    this.findAllDoc();
                    return;
                  }else if(res.data.errorCode==110){
                    this.$message({
                      title:"提示",
                      message:res.data.message,
                      type:'error'
                    });
                    this.NewFileVisible=false;
                    this.getFolder();
                    this.findAllDoc();
                    return;
                  }else {
                    this.$message({
                      title:"提示",
                      message:res.data.message,
                      type:'error'
                    });
                  }
                }
              });
            }
         },
         // 新建文档确定
         NewDocSure:function () {
           if(!this.docValidatar()){
             return
           }
           console.log("文件夹编号"+this.value);
          addFolderDoc(this.projectId,this.value,this.addDocName,this.userId).then(res=>{
            if(res.status==200){
                if(res.data.errorCode==0){
                  // console.log("成功!");
                  this.$message({
                    title:"提示",
                    message:"创建成功",
                    type:'success'
                  });
                  this.NewDocVisible=false;
                  this.queryStr="";
                  this.getFolderDoc(this.folderId);
                }else if(res.data.errorCode==110){
                  this.$message({
                    title:"提示",
                    message:res.data.message,
                    type:'error'
                  });
                  this.NewDocVisible=false;
                  this.getFolder();
                  this.findAllDoc();
                  return;
                }else{
                  this.$message({
                    title:"提示",
                    message:res.data.message,
                    type:'error'
                  });
                }
            }
          });
         },
         // 创建文档
         creatFile:function () {
          this.addDocName="";
          this.NewDocVisible=true;
          if(this.folderId==0){
            this.value="";
          }
         },
         //查看文档
         seeAll:function(){

         },
         // 编辑文档
         updateFile:function (index,row) {
           console.log("编辑");
           var docId=row.id;
           checkDocIsExists(docId).then(res=>{
             console.log(res);
             if(res.data.errorCode==0){
               this.$router.push({ path :'/scientificupdateFile', query:{name: 'scientificupdateFile',projectId:this.projectId,folderId:this.folderId,docId:docId}});
             }else if(res.data.errorCode==110){
               this.$message({
                 type: 'error',
                 message: res.data.message
               });
               this.getFolder();
               this.findAllDoc();
               return;
             }else if(res.data.errorCode==113){
               this.$message({
                 type: 'error',
                 message: res.data.message
               });
               this.queryStr="";
               this.getFolderDoc(this.folderId);
             }
           });



         },
         // 编辑文件夹
         editData:function (folderName) {
          this.fileTitle="编辑文件夹名称";
           this.oldFolderName=folderName;
           this.addFolderName=folderName;
           this.isAddFolder="0";
          this.NewFileVisible=true;
         },
         // 删除文件夹
         deletData:function (folderId) {
           this.$confirm('请注意，该文件夹将从系统被移除，是否确定执行此操作', '提示', {
             confirmButtonText: '确定',
             cancelButtonText: '取消',
             type: 'warning'
           }).then(() => {
             deleteProjectFolder(this.projectId,folderId,this.userId).then(res=>{
               if(res.status=200){
                 if(res.data.errorCode==0){
                   this.$message({
                     type: 'success',
                     message: '删除成功!'
                   });
                   this.getFolder();
                   this.findAllDoc();
                 }else{
                   this.$message({
                     type: 'error',
                     message: res.data.message
                   });
                   this.getFolder();
                   this.findAllDoc();
                 }
               }
             });
           }).catch(() => {
             this.$message({
               type: 'info',
               message: '已取消删除'
             });
           });
         },
         //搜索关键字查询
         search:function () {
           this.queryStr=this.keywordStr;
           if(this.queryStr==null || this.queryStr==""){
             this.page=1;
           }
           this.getFolderDoc(this.folderId);
         },
         // 文件夹校验
         fileValidatar:function () {
           console.log(this.addFolderName.length+"文件夹名称");
           var errorMsg= null;
           if(this.addFolderName==null || this.addFolderName=='' || this.addFolderName.length==0){
              errorMsg="请输入文件夹名称";
           }
           if(this.addFolderName.length>45){
             errorMsg="文件夹名称限定45个字符";
           }
           if(errorMsg!=null && errorMsg.length>0){
             this.$message.error(errorMsg);
             return false
           }
           return true
         },
         // 文档校验
         docValidatar:function () {
           var errorMsg= null;
           if(this.addDocName==null || this.addDocName=="" || this.addDocName.length==0){
             errorMsg="请输入文档标题";
           }
           if(this.addDocName!=null && this.addDocName.length>45){
             errorMsg="文档标题限定45个字符"
           }
           if(this.value=='' || this.value==null){
             errorMsg="文档路径不能为空"
           }
           if(errorMsg && errorMsg.length>0){
             this.$message.error(errorMsg)
             return false
           }
           return true
         }
       },
      beforeRouteEnter: (to, from, next) => {
        next(vm => {
          vm.projectId=vm.$route.query.projectId;
          vm.folderId=vm.$route.query.folderId;
          console.log(vm.projectId);
          vm.init();
        });
      }
    }
</script>

<style scoped type="text/stylus">

</style>
<style type="text/css">
  #dataMain{
    padding: 10px 20px 0;
    /*min-height: 1720.99px;*/
    /*margin-left: 230px;*/
  }
  /*头部*/
  #header{
    /*padding-left: 15px;*/
    /*padding-right: 15px;*/
    padding: 0px 0px;
  }
  /*中间部分*/
  #dataContent{
    /*padding: 15px 15px 15px 15px;*/
    min-height: 500px;
    /*border: 1px solid red;*/
  }
  #dataContent #dataMenu{
    width: 300px;
    height: 490px;
    float: left;
    /*border: 1px solid red;*/
    box-sizing: border-box;
    /*overflow-y: auto;*/
    /*border: 1px solid red;*/
  }
  #dmData{
    height: 360px;
    /*border: 1px solid red;*/
    overflow: auto;
  }
  #dataContent #dataMenu .dataMenuUl{
    width: 300px;
    margin-top: 0px;
    /*text-align: center;*/
    height: 430px;
    border: 1px solid #E6E9ED;
    padding: 0px;
    overflow-y: auto;
    overflow-x: hidden;
    text-overflow: ellipsis;
  }
  #dataTable{
    height: 430px;
    padding-left: 10px;
    /*border: 1px solid red;*/
  }
  #dataContent #dataMenu .dataMenuUl li{
    width: 300px;
    height: 43px;
    line-height: 43px;
    position: relative;
    padding-left: 5px;
    cursor: pointer;
  }
  #dataMenuLeft{
    display: inline-block;
    width: 160px;
    /*border: 1px solid red;*/
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: pre;
    word-wrap: normal !important;
    word-break: normal !important;
  }
  /*#dataContent #dataMenu .dataMenuUl li:hover #editFile{*/
  /*display: block;*/
  /*}*/
  #fileName{
    width: 180px;
    line-height: 43px;
    list-style: none;
    white-space: pre-wrap;
    word-wrap: break-word;
    /*overflow: hidden;*/
    text-overflow: ellipsis;
    position: relative;
    display: flex;
    text-align: center;
  }
  #editFile{
    position: absolute;
    right: 8px;
    top: 0px;
    width: 130px;
    /*display: none;*/
    /*border: 1px solid red;*/
  }
  .el-table .cell{
    white-space: pre-wrap !important;
  }
  #dataContent #dataMenu .dataMenuUl li.active{
    background-color: rgba(215, 215, 215, 1);
    cursor: pointer;
  }
  #dataContent #dataRight{
    float: right;
    width: calc(100% - 305px);
    height: 485px;
    vertical-align: top;
    /*border: 1px solid red;*/
    box-sizing: border-box;
    border: 1px solid #E6E9ED;
  }
  #dataContent #dataRight .tableHeader,.newFile{
    margin-bottom: 5px;
  }
  .table>thead > tr>th,.table>tbody > tr>td{
    text-align: center;
    vertical-align: middle;
  }
  @media only screen and (min-width: 1540px) and (max-width: 2200px){
    /*中间部分*/
    #dataContent{
      min-height: 760px !important;
      /*border: 1px solid red !important;*/
    }
    #dataContent #dataMenu{
      height: 800px;
    }
    #dmData{
      height: 590px !important;
      overflow: auto;
      /*border: 1px solid red;*/
    }
    #dataContent #dataMenu .dataMenuUl{
      height: 670px;
    }
    #dataTable{
      height: 640px;
    }
    #dataContent #dataRight{
      height: 720px;
    }
  }
</style>
