<template>
  <div id="share" style="border: 1px solid #dcdfe6;width: 100%;">
    <!--<iframe :src="fileManageUrl"-->
    <!--style="width: 100%;height: 600px;-->
    <!--overflow-y:auto;overflow-x: hidden;" id="fileSystem"></iframe>-->
    <div style="padding: 15px;">
      <el-form :inline="true"  class="demo-form-inline">
        <el-form-item >
          <el-button size="small" icon="el-icon-download" :disabled="isDown" @click="downloadFile">下载</el-button>
          <el-button size="small" icon="el-icon-close" @click="deleteFile" :disabled="isDelete">删除</el-button>
          <el-button size="small" icon="el-icon-rank" :disabled="isMove" @click="moveFile">移动</el-button>
          <el-button size="small" icon="el-icon-edit" :disabled="isrename" @click="renameFile">重命名</el-button>
          <!--<el-button size="small" icon="el-icon-upload2" :disabled="isPack" @click="packFile">打包</el-button>-->
        </el-form-item>
        <el-form-item style="float: right">
          <el-button size="small" icon="el-icon-upload" @click="upload">上传</el-button>
          <el-button size="small" icon="el-icon-tickets" @click="newFile">新建文件夹</el-button>
        </el-form-item>
      </el-form>
      <!--主目录-->
      <div class="title">
        <img src="../../../static/images/icon/icon-home.png" width="18" height="18" alt="主目录" class="icon">
        <a href="javascript:void(0);" style="color: #337ab7" @click="_getShareFileList('','','')">主目录</a>
        <el-tooltip placement="top" >
          <div slot="content">{{path}}</div>
          <el-tag type="success" size="mini" id="mulu" >{{path}}</el-tag>
        </el-tooltip>
      </div>

      <el-table id="Table"
                border
                :data="tableData"
                @selection-change="handleSelectionChange"
                style="width: 100%;text-align: center">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          prop="name"
          label="名称"
          width="180">
          <template slot-scope="scope">
            <a href="javascript:void(0);" @click="nextPath(scope.row)"><i :class="{'el-icon-document':scope.row.type==1,'el-icon-goods':scope.row.type==2}"></i>{{scope.row.name}}</a>
          </template>
        </el-table-column>
        <el-table-column
          prop="size"
          label="大小">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{parseFloat(scope.row.size/1024) > 1 ? (parseFloat(scope.row.size/1024/1024) >1 ? ((parseFloat(scope.row.size/1024/1024)).toFixed(2).concat('M')) : (parseFloat(scope.row.size/1024)).toFixed(2).concat('K')) : (parseFloat(scope.row.size)).toFixed(2).concat('B')}}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="time"
          label="修改时间">
        </el-table-column>
      </el-table>

      <!--上传文件-->
      <el-dialog
        title="上传文件"
        :visible.sync="uploadVisible"
        width="40%">
        <p style="margin-bottom: 20px;">
          <span>上传至:</span>
          <span style="margin-left: 5px;word-break: break-all">{{uploadPath}}</span>
        </p>
        <el-upload
          class="upload-demo"
          ref="upload"
          :action="actionUrl"
          :limit="1"
          :multiple="false"
          :data="uploadData"
          :auto-upload="false"
          :on-success="uploadSuccess"
          :file-list="fileList"
          :http-request="customUploadHandler"
          :on-exceed="exceedHandler"
          v-loading="loading">
          <el-button size="small" type="primary">选择文件</el-button>
        </el-upload>
        <span slot="footer" class="dialog-footer">
          <el-button @click="uploadVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitUpload" :disabled="!canUpload">确 定</el-button>
        </span>
      </el-dialog>
      <!--新建文件夹-->
      <el-dialog
        title="新建文件夹"
        :visible.sync="newVisible"
        width="38%" style="border-radius: 5%">
        <p style="margin-bottom: 20px;">
          <span>创建路径:</span>
          <span style="margin-left: 5px;word-break: break-all">{{newUploadPath}}{{newPath}}</span>
        </p>
        <h3>文件夹名称</h3>
        <el-input
          placeholder="请输入内容"
          v-model="newPath"
          clearable>
        </el-input>
        <span slot="footer" class="dialog-footer">
          <el-button @click="newVisible = false">取 消</el-button>
          <el-button type="primary" @click="newFileSave()">确 定</el-button>
        </span>
      </el-dialog>
      <!--移动-->
      <el-dialog
        title="移动"
        :visible.sync="moveVisible"
        width="38%" style="border-radius: 5%">
        <h1 style="margin-bottom: 10px;">目标路径</h1>
        <el-input
          placeholder="请输入内容"
          v-model="targetPath"
          clearable>
        </el-input>
        <span slot="footer" class="dialog-footer">
          <el-button @click="moveVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveMoveFile()">确 定</el-button>
        </span>
      </el-dialog>
      <!--重命名-->
      <el-dialog
        title="重命名"
        :visible.sync="renameVisible"
        width="38%" style="border-radius: 5%">
        <h1 style="margin-bottom: 10px;">新名称</h1>
        <el-input
          placeholder="请输入内容"
          v-model="newFileName"
          clearable>
        </el-input>
        <span slot="footer" class="dialog-footer">
          <el-button @click="renameVisible = false">取 消</el-button>
          <el-button type="primary" @click="renameShareFile()">确 定</el-button>
        </span>
      </el-dialog>
      <!--打包-->
      <el-dialog
        title="创建压缩包"
        :visible.sync="packVisible"
        width="38%" style="border-radius: 5%">
        <h1 style="margin-bottom: 10px;">名称</h1>
        <el-input
          placeholder="请输入内容"
          v-model="newInput"
          clearable>
        </el-input>
        <el-checkbox-group style="margin-top: 20px;">
          <el-checkbox label="在压缩包内嵌入内容" ></el-checkbox>
        </el-checkbox-group>

        <span slot="footer" class="dialog-footer">
          <el-button @click="packVisible = false">取 消</el-button>
          <el-button type="primary" @click="packVisible = false">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import { getShareFileList,edu_url,deleteShareFile,downloadShareFile,createShareDirectory,renameShareFile,moveShareFile,checkIsExistsShare} from '@/common/js/request';
  import axios from 'axios';
  export default {
    data (){
      return{
        tableData: [{
          type: '',
          name: '',
          size: '',
          time: ''
        }],
        formInline: {},
        userid:'',
        courseId:'',
        uploadHeaders: {
          'Authorization': 'Bearer ' + (localStorage['token'] ? localStorage['token'] : '')
        },
        uploadData:{},
        path:"/",
        uploadPath:"/",
        newUploadPath:"",
        targetPath:"",
        uploadVisible:false,
        actionUrl:null,
        newFileName:null,
        fileList: [],
        newVisible:false,
        newInput:"",
        newPath:"",
        isDown:true,
        isDelete:true,
        isMove:true,
        isrename:true,
        isPack:true,
        moveVisible:false,
        renameVisible:false,
        packVisible:false,
        canUpload:true,
        loading:false
      }
    },

    /* activated(){
       var courseId=this.$route.params.courseId;
       console.log(courseId+"课程");
       if(courseId==null || courseId==undefined){
         this.userid=localStorage['user'] ? JSON.parse(localStorage['user']).userId : '';
       }else{
         this.userid=courseId;
       }
       console.log(this.userid+"用户");
     },*/
    methods:{
      handleDelete(index, row) {
        console.log(index, row);
      },
      submitUpload() {
        if(this.$refs.upload.uploadFiles.length==0){
          this.$message({
            type:'warning',
            message:'请先选择文件哦！'
          });
          return;
        }
        this.loading = true;
        this.$refs.upload.submit();
      },
      //文件超出
      exceedHandler(files,fileList){
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      uploadSuccess:function(response){
        console.log("上传成功");
        this.loading = false;
        this.canUpload=true;
        this.fileList=[];
        this.$refs.upload.clearFiles();
        if(response=="success"){
          if(this.path.trim().length==0){
            this.path = "/";
          }
          var userId = "";
          if(this.userid!=null && this.userid!=""){
            userId=this.userid;
          }else{
            userId=this.courseId;
          }
          this._getShareFileList(userId,this.path);
        }
      },
      //上传
      upload(){
        this.loading=false;
        this.canUpload=true;
        console.log(this.path+"上传至");
        console.log(this.multipleSelection);
        this.fileList=[];
        if(this.multipleSelection!=null && this.multipleSelection.length>0){
          if(this.multipleSelection.length>1){
            this.$message({
              type:"error",
              message:"只能选择一行数据!"
            })
            return;
          }
          for(let[idx,item] of this.multipleSelection.entries()){
            if(item.type!=2){
              this.$message({
                type:"error",
                message:"只能选择文件夹类型!"
              })
              return;
            }
            if(item.type==2){
              if(this.path=="/"){
                this.uploadPath=this.path+item.name;
              }else{
                this.uploadPath=this.path+"/"+item.name;
              }
            }
          }

        }else{
          console.log("我没有选择数据");
          this.uploadPath=this.path;
        }
        console.log(this.path);
        this.uploadVisible=true
      },
      newFile(){
        this.newPath="";
        this.newUploadPath="";
        if(this.multipleSelection!=null && this.multipleSelection.length>0){
          if(this.multipleSelection.length>1){
            this.$message({
              type:"error",
              message:"只能选择一行数据!"
            })
            return;
          }
          for(let[idx,item] of this.multipleSelection.entries()){
            if(item.type!=2){
              this.$message({
                type:"error",
                message:"只能选择文件夹类型!"
              })
              return;
            }
            if(item.type==2){
              if(this.path=="/"){
                this.newUploadPath=this.path+item.name+"/";
              }else{
                this.newUploadPath=this.path+"/"+item.name+"/";
              }
            }
          }

        }else{
          if(this.path=="/"){
            this.newUploadPath=this.path;
          }else{
            this.newUploadPath=this.path+"/";
          }

        }
        this.newVisible=true
      },
      //重命名文件
      renameShareFile(){
        var fileName=this.newFileName.trim();
        if(fileName==this.multipleSelection[0].name){
          this.$message({
            type:"success",
            message:"重新命名成功!"
          });
          this._getShareFileList(paramId,this.path,type);
          this.renameVisible=false;
          return;
        }
        if(fileName==null || fileName==""){
          this.$message({
            type:"error",
            message:"文件名称不能为空!"
          });
          return;
        }
        /*var fileLength=this.getByFileLen(fileName);
        console.log("文件长度"+fileLength);*/
        if(fileName.length>80){
          this.$message({
            title: "提示",
            message: "文件名不能超过80个字符!",
            type: 'error'
          });
          return;
        }
        var Filepatt=/^:|\*|\?|\"|\/|\\|<|>|\|$/;
        console.log(fileName+"文件名称!")
        if(Filepatt.test(fileName)){
          this.$message({
            type:"error",
            message:"文件名称不能包含特殊字符(: * ? \" > < | \\ /)!"
          });
          return;
        }
        var patt=/^(\.)+$/;
        if(patt.test(fileName)){
          this.$message({
            type:"error",
            message:"文件名称不合法!"
          });
          return;
        }
        //var oldName=this.multipleSelection[0].name;
        //var suffix=oldName.substring(oldName.indexOf("."),oldName.length);
        //判断文件名是否重复，重复给出相关提示
        let isExist = false;
        if(typeof this.tableData != 'undefined') {
          for (let [idx, item] of this.tableData.entries()) {
            if(item.name==fileName.trim()){
              if(fileName.trim()!=this.multipleSelection[0].name){
                isExist = true;
                break;
              }
            }
          }
        }
        if(isExist){
          this.$message({
            type:"error",
            message:"该文件名已存在!"
          });
          return;
        }


        var paramId="";
        var type=0;
        if(this.userid!=null && this.userid!=""){
          paramId=this.userid;
          type=1;//用户
        }else{
          paramId=this.courseId;
          type=2;//课程
        }
        for(let[idx,item] of this.multipleSelection.entries()){
          renameShareFile(paramId,item.name,encodeURIComponent(fileName),this.path,type).then(res=>{
            if(res.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            if(res.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            if(res.status==200){
              if(res.data.errorCode==0 && res.data.data=="0"){
                this.$message({
                  type:"success",
                  message:"重新命名成功!"
                });
                this._getShareFileList(paramId,this.path,type);
              }else if(res.data.errorCode==0 && res.data.data=="1"){
                this.$message({
                  type:"error",
                  message:"重新命名失败!"
                });
              }else if(res.data.errorCode==0 && res.data.data=="2"){
                this.$message({
                  type:"error",
                  message:"新文件名称已存在!"
                });
              }else if(res.data.errorCode==42){
                this.$message({
                  type:"error",
                  message:"原文件不存在!"
                });
              }
            }
          });
        }
        this.renameVisible=false;

      },
      nextPath:function(item){
        if(parseInt(item.type)==2){
          //文件夹，那么点击进入下一级
          if(this.path.lastIndexOf("/")<0){
            this.path+="/";
          }
          if(this.path.lastIndexOf("/")>=0 && this.path!='/'){
            this.path += "/";
          }
          console.log(this.path);
          this.path = this.path+item.name;
          this.path = this.path.replace(/\\/g,"/");
          if(this.userid!=null && this.userid!=""){
            this._getShareFileList(this.userid,this.path,1);
          }else{
            this._getShareFileList(this.courseId,this.path,2);
          }

        }
      },
      //新建文件夹
      newFileSave(){
        var filePath=this.newPath.trim();
        if(filePath==null || filePath=="" || filePath.length<0){
          this.$message({
            type:"error",
            message:"文件夹名称不能为空!"
          });
          return;
        }else {
          filePath = filePath.replace(/\\/g, "/");
          /*console.log(filePath+"开始");
          var endStr=filePath.substr(filePath.length-1,1);
          console.log(endStr+"结束字符");
          if(endStr=="/"){
            filePath=filePath.substr(0,filePath.length-1);
          }*/
          //var dirs = filePath.split("/");
          // for (let i = 0; i < dirs.length; i++) {}
          // var dir = dirs[i];
          var patt=/^:|\*|\?|\"|\/|\\|<|>|\|$/;
          /*var fileLength=this.getByFileLen(filePath);
          console.log("文件长度"+fileLength);*/
          if (filePath.length > 80) {
            this.$message({
              title: "提示",
              message: "文件名不能超过80个字符!",
              type: 'error'
            });
            return;
          } else if (filePath==null || filePath.trim().length == 0) {
            //文件夹不允许为空
            this.$message({
              title: "提示",
              message: "文件夹名称不能为空",
              type: 'error'
            });
            return;
          }else if(patt.test(filePath.trim())){
            this.$message({
              type:"error",
              message:"文件夹名称不能包含特殊字符(: * ? \" > < | \\ /)!"
            });
            return;
          }
          var filePatt=/^(\.)+$/;
          if(filePatt.test(filePath)){
            this.$message({
              type:"error",
              message:"文件夹名称不合法!"
            });
            return;
          }
          console.log("最后的文件夹名称"+filePath.trim());
          console.log(this.multipleSelection);
          if(this.multipleSelection!=null && this.multipleSelection!=undefined
            && this.multipleSelection.length>0  && this.multipleSelection[0].type==2){
            if(this.path=="/"){
              this.path=this.path+this.multipleSelection[0].name;
            }else{
              this.path=this.path+"/"+this.multipleSelection[0].name;
            }

          }
          console.log("创建文件夹path"+this.path+filePath);
          var paramId="";
          var type=0;
          if(this.userid!=null && this.userid!=""){
            paramId=this.userid;
            type=1;
          }else{
            paramId=this.courseId;
            type=2;
          }
          createShareDirectory(paramId,encodeURIComponent(filePath.trim()),this.path,type).then(res=>{
            if(res.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            if(res.status==200){
              if(res.data.errorCode==0 && res.data.data=="1"){
                this.$message({
                  type:"success",
                  message:"创建成功!"
                });
                this._getShareFileList(paramId,this.path,type);
              }else if(res.data.errorCode==0 && res.data.data=="2"){
                this.$message({
                  type:"error",
                  message:"文件名已经存在!"
                });
                this._getShareFileList(paramId,this.path,type);
              }
            }
          })
          //}
        }
        this.newVisible=false;

      },
      //移动文件
      saveMoveFile(){
        var filePath=this.targetPath.trim();
        if(filePath==null || filePath=="" || filePath.length<0){
          this.$message({
            type:"error",
            message:"目标文件夹名称不能为空!"
          })
        }else {
          filePath = filePath.replace(/\\/g, "/");
          var endStr=filePath.substr(filePath.length-1,1);
          console.log(endStr+"结束字符");
          //去掉最后"/"
          if(endStr=="/"){
            filePath=filePath.substr(0,filePath.length-1);
          }
          var dirs = filePath.split("/");
          console.log(dirs);
          for (let i = 0; i < dirs.length; i++) {
            var dir = dirs[i];
            var patt = /^:|\*|\?|\"|\/|<|>|\|$/;
            var filePatt = /^(\.)+$/;
            if (dir.length > 80) {
              this.$message({
                title: "提示",
                message: "文件夹长度不能超过80个字符",
                type: 'error'
              });
              return;
            } else if (dir && dir.trim().length == 0) {
              //文件夹不允许为空
              this.$message({
                title: "提示",
                message: "目标文件夹名称不能为空",
                type: 'error'
              });
              return;
            } else if (patt.test(dir.trim())) {
              this.$message({
                type: "error",
                message: "目标文件夹名称不能包含特殊字符(: * ? \" > < | )!"
              });
              return;
            }else if(filePatt.test(dir.trim())){
              this.$message({
                type: "error",
                message: "目标文件夹名称不合法!"
              });
              return;
            }
          }
          var fileNameArry=new Array();
          //获取选中的文件
          for(let[idx,item] of this.multipleSelection.entries()){
            if(this.path=="/"){
              fileNameArry[idx]=this.path+item.name;
            }else{
              fileNameArry[idx]=this.path+"/"+item.name;
            }
          }
          var paramId="";
          var type=0;
          if(this.userid!=null && this.userid!=""){
            paramId=this.userid;
            type=1;
          }else{
            paramId=this.courseId;
            type=2;
          }
          moveShareFile(paramId,filePath,fileNameArry,1,type).then(res=>{
            if(res.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            if(res.status==200){
              if(res.data.errorCode==0 && res.data.data=="0"){
                this.$message({
                  type: "success",
                  message: "移动文件成功!"
                });
                this.moveVisible=false;
                if(filePath.substr(0,1)!="/"){
                  this.path="/"+filePath;
                }else{
                  this.path=filePath;
                }
                console.log(this.path+"移动之后路径");
                this._getShareFileList(paramId,this.path,type);
              }else if(res.data.errorCode==0 && res.data.data=="2"){
                this.$message({
                  type: "error",
                  message: "目标路径不能是文件!"
                });
                return;
              }else if(res.data.errorCode==0 && res.data.data=="3"){
                this.$message({
                  type: "error",
                  message: "目标路径不存在!"
                });
                return;
              }else if(res.data.errorCode==0 && res.data.data=="4"){
                this.$message({
                  type: "error",
                  message: "目标路径下存在相同的文件名!"
                });
                return;
              }else if(res.data.errorCode==0 && res.data.data=="5"){
                this.$message({
                  type: "error",
                  message: "目标路径不合法!"
                });
                return;
              }else if(res.data.errorCode==0 && res.data.data!="0"){
                this.$confirm(res.data.data+',是否覆盖?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                  moveShareFile(paramId,filePath,fileNameArry,2,type).then(res=>{
                    if(res.data=="alreadyResetPassword"){
                      this.$message({
                        type:'warning',
                        message:'请重新登录'
                      });
                      return;
                    }
                    if(res.status==200) {
                      if (res.data.errorCode == 0 && res.data.data == "0") {
                        this.$message({
                          type: "success",
                          message: "移动文件成功!"
                        });
                        if(filePath.substr(0,1)!="/"){
                          this.path="/"+filePath;
                        }else{
                          this.path=filePath;
                        }
                        console.log(this.path+"移动之后路径");
                        this.moveVisible=false;
                        this._getShareFileList(paramId,this.path,type);
                      }
                    }
                  })
                })
              }
            }
          });
        }
      },

      handleSelectionChange(val){
        this.multipleSelection = val;
        if(this.multipleSelection ==""){
          this.isDown=true
          this.isDelete=true
          this.isMove=true
          this.isrename=true
          this.isPack=true
          return
        }
        if(this.multipleSelection== val){
          this.isDown=false
          this.isDelete=false
          this.isMove=false
          this.isrename=false
          this.isPack=false
          this.multipleSelection = JSON.parse(JSON.stringify(val));
          console.log(this.multipleSelection.length+"测试");
          return
        }
      },
      downloadFile(){
        if(this.multipleSelection==null || this.multipleSelection.length==0){
          this.$message({
            message: '警告哦，至少选择一行数据',
            type: 'warning'
          });
          return;
        }else{
          if(this.multipleSelection.length>1){
            this.$message({
              message: '警告哦，只能选择一行数据',
              type: 'warning'
            });
            return;
          }else if(this.multipleSelection[0].type==2){
            this.$message({
              message: '不能下载文件夹',
              type: 'error'
            });
            return;
          }else{

            for(let [idx,item] of this.multipleSelection.entries()){
              console.log(this.multipleSelection.length+"下载个数");
              var filePath="";
              if(this.path=="/"){
                filePath=this.path+item.name;
              }else{
                filePath=this.path+"/"+item.name;
              }
              var paramId="";
              var type=0;
              if(this.userid!=null && this.userid!=""){
                paramId=this.userid;
                type=1;
              }else{
                paramId=this.courseId;
                type=2;
              }
              checkIsExistsShare(paramId,filePath,type).then(res=>{
                if(res.data=="alreadyResetPassword"){
                  this.$message({
                    type:'warning',
                    message:'请重新登录'
                  });
                  return;
                }
                if(res.status==200){
                  if(res.data.data!=null && res.data.data=="0"){
                    this.$message({
                      type:"error",
                      message:"该文件已不存在!"
                    });
                    this._getShareFileList(paramId,this.path,type);
                    return;
                  }else{
                    var name=encodeURIComponent(item.name);
                    console.log(name);
                    window.location.href=edu_url+"/share/downloadShareFile?userId="+encodeURIComponent(paramId)+"&fileName="+encodeURIComponent(name)+"&path="+encodeURIComponent(this.path)+"&type="+encodeURIComponent(type);
                  }
                }
              });
            }
          }
        }


      },
      deleteFile(){
        var paramId="";
        var type=0;
        if(this.userid!=null && this.userid!=""){
          paramId=this.userid;
          type=1;
        }else{
          paramId=this.courseId;
          type=2;
        }
        if(this.multipleSelection==null || this.multipleSelection.length==0){
          this.$message({
            message: '警告哦，至少选择一行数据',
            type: 'warning'
          });
        }else{
          this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
          }).then(()=>{
            for(let [idx,item] of this.multipleSelection.entries()){
              console.log("删除path"+this.path);
              deleteShareFile(paramId,item.name,this.path,type).then(res=>{
                if(res.data=="alreadyResetPassword"){
                  this.$message({
                    type:'warning',
                    message:'请重新登录'
                  });
                  return;
                }
                if(res.status==200){
                  if(res.data.errorCode==0 && res.data.data=="1"){
                    this.$message({
                      type:"success",
                      message:"删除成功!"
                    });
                  }else if(res.data.errorCode==0 && res.data.data=="0"){
                    this.$message({
                      type:"error",
                      message:"文件不存在!"
                    });
                  }
                }else{
                  this.$message({
                    type:"error",
                    message:"删除失败!"
                  });
                }
                this._getShareFileList(paramId,this.path,type);
              })
            }
          })
        }
      },
      // 移动
      moveFile(){
        this.targetPath="";
        this.moveVisible=true
      },
      // 重命名
      renameFile(){
        if(this.multipleSelection==null || this.multipleSelection.length==0){
          this.$message({
            message: '警告哦，至少选择一行数据',
            type: 'warning'
          });
          return;
        }else if(this.multipleSelection!=null && this.multipleSelection.length>1){
          this.$message({
            message: '警告哦，只能选择一行数据',
            type: 'warning'
          });
          return;
        }
        this.newFileName=this.multipleSelection[0].name;
        this.renameVisible=true
      },
      _getShareFileList:function (userId,path,type) {
        if(userId=="" && path=="" && type==""){
          if(this.userid!=null && this.userid!=""){
            userId=this.userid;
            type=1;
          }else{
            userId=this.courseId;
            type=2;
          }
          path="/";
          this.path="/";
        }
        console.log("开始查询文件");
        getShareFileList(userId,path,type).then(res=>{
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
            }else{
              this.tableData=res.data.data;
            }
          }
        })
        console.log(this.path);
      },
      //自定义上传文件
      customUploadHandler:function(item){
        var obj = this;
        var filename=item.file.name;
        this.canUpload=false;
        if(filename.length>80){
          this.$message({
            title: "提示",
            message: "文件名不能超过80个字符!",
            type: 'error'
          });
          this.loading=false;
          this.canUpload=true;
          return;
        }
        if(obj.multipleSelection!=null && obj.multipleSelection.length!=0){
          var type=this.multipleSelection[0].type;
          if(type==2){
            var filePath="";
            if(this.path!="/"){
              this.path=this.path+"/"+this.multipleSelection[0].name;
            }else{
              this.path=this.path+this.multipleSelection[0].name;
            }
          }
        }
        //自定义上传
        if(this.path.trim().length==0){
          this.path = "/";
        }
        if(this.path.indexOf("/")!=0){
          this.path="/"+this.path;
        }
        this.path = this.path.replace(/\\/g,"/");
        //判断文件夹长度是否超过限制大小，limit 255，这里限制100
        //中文字符在Linux上占3个字节   所以统一规定文件名只能输入80个字符
        console.log(this.path);
        if(this.path.trim() != '/') {
          var dirs = this.path.split("/");
          for (let i = 0; i < dirs.length; i++) {
            var dir = dirs[i];
            if (dir.trim().length> 80) {
              this.$message({
                title: "提示",
                message: "文件名不能超过80个字符!",
                type: 'error'
              });
              this.loading=false;
              this.canUpload=true;
              return;
            } else if (dir && dir.trim().length == 0) {
              //文件夹不允许为空
              this.$message({
                title: "提示",
                message: "文件夹名称不能为空",
                type: 'error'
              });
              this.loading=false;
              this.canUpload=true;
              return;
            }
          }
        }
        //用于传值的编号
        var id="";
        //用于表示用户还是课程
        var type=0;
        if(this.userid!=null && this.userid!=""){
          id=this.userid;
          type=1;
        }else{
          id=this.courseId;
          type=2;
        }

        this.uploadData={
          userId:id,
          path:this.path
        };
        var url = item.action;
        var file = item.file;
        //判断文件大小，目前允许200M
        console.log("我开始了");
        console.log(file.size);
        if(file.size>20971520){
          this.$message({
            title:"提示",
            message:"文件大小超过限额20M",
            type:'error'
          });
          this.loading=false;
          this.canUpload=true;
          return;
        }
        let param = new FormData();
        // 添加请求头
        param.append('file', file);
        param.append('userId', this.uploadData.userId);
        param.append('path', this.uploadData.path);
        param.append('isCheckExists', "1");
        param.append('type', type);
        axios.post(url, param).then(res => {
          console.log("路径"+url);
          console.log(res);
          if(res.status==200){
            if(res.data.errorCode==0){
              if(res.data.data=="3"){
                this.$message({
                  type:"error",
                  message:"该用户不存在!"
                });
                this.canUpload=false;
                this.loading=true;
                return;
              }else if(res.data.data=="4"){
                this.$message({
                  type:"error",
                  message:"该课程不存在!"
                });
                this.loading=false;
                this.canUpload=true;
                return;
              }else if(res.data.data=="5"){
                this.$message({
                  type:"error",
                  message:"存在同名的文件夹!"
                });
                this.loading=false;
                this.canUpload=true;
                return;
              }else{
                this.$message({
                  type:"success",
                  message:"上传成功!"
                });
                this.canUpload=true;
                this.loading=true;
              }
              //文件上传成功
              obj.$refs.upload.clearFiles();
              //弹框去掉
              this.uploadVisible = false;
              this._getShareFileList(this.uploadData.userId,this.uploadData.path,type);
            }else if(res.data.errorCode==61){
              this.$confirm('存在同名的文件，是否覆盖?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                let param = new FormData();
                param.append('file', file);
                param.append('userId', this.uploadData.userId);
                param.append('path', this.uploadData.path);
                param.append('isCheckExists', "2");
                param.append('type', type);
                // 添加请求头
                axios.post(url, param).then(res => {
                  if(res.status==200) {
                    if (res.data.errorCode == 0) {
                      this.$message({
                        type:"success",
                        message:"上传成功!"
                      });
                      this.loading=true;
                      this.canUpload=true;
                      //文件上传成功
                      obj.$refs.upload.clearFiles();
                      //弹框去掉
                      this.uploadVisible = false;
                      this._getShareFileList(this.uploadData.userId, this.uploadData.path,type);
                    }
                  }
                })
              }).catch(()=>{
                this.loading=false;
                this.canUpload=true;
              })
            }
          }
        });
      },
      init:function () {
        console.log("用户"+this.userid);
        this.actionUrl="/share/uploadShareFile";
        this.path="/";
        var courseId=this.$route.params.courseId;
        console.log(courseId+"课程");
        if(courseId==null || courseId==undefined || courseId==""){
          this.userid=localStorage['user'] ? JSON.parse(localStorage['user']).userId : '';
          this._getShareFileList(this.userid,this.path,1);
        }else{
          this.courseId=courseId;
          this._getShareFileList(this.courseId,this.path,2);
        }
      },
      //计算文件长度
      getByFileLen:function(val) {
        var len = 0;
        for (var i = 0; i < val.length; i++) {
          var a = val.charAt(i);
          if (a.match(/[^\x00-\xff]/ig) != null)
          {
            len += 3;
          }
          else
          {
            len += 1;
          }
        }
        return len;
      }

  },
    created:function () {
      this.init();
    },
    watch:{
      '$route':function(){
        if(this.$route.fullPath.indexOf("/htSj/share/")>=0){
          this.init();
        }
      }
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  .el-collapse-item__header
    color #909399 !important
  .el-input-group__append, .el-input-group__prepend
    color #ffffff
  .el-form-item__content
    font-size 0px
  .title
    width 100%
    height 36px
    line-height 36px
    background-color #f5f5f5
    border-radius 2px
    margin-bottom 20px
    font-size 0px
    display inline-block
    img
      vertical-align middle
      margin-top -10px
      margin-left 15px
    a
      font-size 14px
      color #337ab7
</style>
<style type="text/css">
  #share{
    position: relative;
    top: 0px;
  }
  #Table{
    text-align: center;
  }
  .el-table--border th .cell {
    text-align: center;
    font-weight: bolder;
    font-size: 1.1em;
  }
  .el-dialog__header{
    padding-bottom: 15px;
  }
  #share .el-dialog__body{
    border-top: 1px solid #e5e5e5 ;
    border-bottom: 1px solid #e5e5e5 ;
  }
  .el-dialog__footer{
    margin-top: 20px;
  }
  .el-dialog{
    border-radius: 10px;
  }
  .el-form--inline .el-form-item{
    margin-right: 0px;
  }
  #mulu{
    max-width: calc(100% - 100px);
    display: inline-block;
    margin-left: 5px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    vertical-align: middle;
    margin-top: -10px;
  }
  .el-tooltip__popper.is-dark{
    max-width: 92%;
    padding: 8px 10px ;
    word-break: break-all;
  }
  .el-message-box__status + .el-message-box__message{
    word-break: break-all;
  }
</style>









