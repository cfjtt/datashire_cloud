<template>
  <div id="share" style="width: 100%;">
    <div style="padding: 10px 10px 10px 10px;">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" >
        <el-button  size="medium" @click="refresh">刷新</el-button>
      </el-form>
      <!--输入搜索-->
      <div style="margin:15px 0px;">
        <p style="margin-bottom: 10px">路径</p>
        <el-input placeholder="请输入内容"  class="input-with-select" v-model="path" :disabled="isAdmin && type==2 || !isAuthority">
          <el-button v-if="isAuthority" slot="append" type="primary" style="background: #409EFF;color: #ffffff" @click="searchFile">进入</el-button>
        </el-input>
      </div>
      <!--表格-->

      <!--
      <div id="table" style="width: 100%;border-top: 2px solid #E6E9ED;padding: 20px 0px">
    -->
      <el-button size="small" @click="back()" type="primary"
                 icon="el-icon-caret-top el-icon-right"
                 style="margin: 15px 0px"
                 v-if="ishidden">返回上一级</el-button>
      <!--<p  style="margin: 20px 0px;color: #909399"></p>-->
      <el-table
        :data="tableData"
        border
        tooltip-effect="dark"
        style="width: 98%;white-space:pre-wrap">
        <el-table-column
          prop="name"
          label="名称"
          width="">
          <template slot-scope="scope">
            <a style="white-space: pre-wrap;" href="javascript:void(0);" @click="nextPath(scope.row)"><i :class="{'el-icon-document':scope.row.type==1,'el-icon-goods':scope.row.type==2}"></i>{{scope.row.name}}</a>
          </template>
        </el-table-column>
        <el-table-column
          prop="size"
          label="大小"
          width=""
          show-overflow-tooltip>
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{parseFloat(scope.row.size/1024) > 1 ? (parseFloat(scope.row.size/1024/1024) >1 ? ((parseFloat(scope.row.size/1024/1024)).toFixed(2).concat('M')) : (parseFloat(scope.row.size/1024)).toFixed(2).concat('K')) : (parseFloat(scope.row.size)).toFixed(2).concat('B')}}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="lastModified"
          label="修改日期"
          width=""
          show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          prop="lastModified"
          label="操作"
          width=""
          v-if="isAuthority"
          show-overflow-tooltip>
          <template slot-scope="scope" >
            <el-button size="mini" type="danger" @click="deleteFile(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--
      </div>
      -->

        <div style="color: #606266" v-if="isAuthority">
          <div style="border-bottom: 1px solid rgb(220, 223, 230);
          width: 100%;padding: 18px 0px;">
            <span style="font-size: 22px;margin-right: 5px;">文件上传</span>
            <span>将文件上传到自己的数猎场文件管理系统</span>
          </div>
        </div>
          <div v-loading="loading" v-if="isAuthority">
            <el-upload
              class="upload-demo"
              ref="upload"
              :action="actionUrl"
              :limit="1"
              :multiple="false"
              :data="uploadData"
              :auto-upload="false"
              :on-success="uploadSuccess"
              :http-request="customUploadHandler"
              :on-exceed="exceedHandler">
              <el-button class="elBtn" style="margin-top: 15px;" slot="trigger" size="small" type="primary" >选取文件</el-button>
              <el-button class="elBtn" style="margin-left: 10px;margin-top: 15px;"
                         size="small" type="success" @click="submitUpload" :disabled="!canUpload">开始上传</el-button>
              <div slot="tip" class="el-upload__tip">
                <h1 style="margin: 15px 0px;font-size: 14px;">您所选择的文件列表：</h1>
              </div>
            </el-upload>

          </div>
        </div>

    </div>
  </div>

</template>

<script type="text/ecmascript-6">
  import { getDataShireFileList,shu_lie_yun_url,deleteFile,getUserProject} from '@/common/js/request';
  import axios from 'axios';
  export default {
    data (){
      return{
        tableData: [{
          type: '',
          name: '',
          size: '',
          time:''
        }],
        activeNames:['1'],
        fileList: [],
        actionUrl:null,
        uploadHeaders: {
          'Authorization': 'Bearer ' + (localStorage['token'] ? localStorage['token'] : '')
        },
        uploadData:{
          phone:localStorage['user'] ? JSON.parse(localStorage['user']).username : '',
          path:'/'
        },
        path:"/",
        spaceName:null,
        formInline:{},
        ishidden:false,//隐藏
        canUpload:true,
        isAdmin:false,
        courseName:0,
        type:1,
        loading:false,
        isProject: false,
        projectId : -1,
        managerNo : -1,
        isAuthority: true
      }
    },
    methods:{
      deleteFile:function(row){
        //删除文件
        var spaceName = this.spaceName;
        var fileName = row.name;
        var phone = null;
        if(this.isProject){
          phone = this.managerNo;
        }else {
          phone = JSON.parse(localStorage["user"]).username;
        }

        //var path = this.path = this.path.replace(/\\/g,"/");
        var path = row.path.replace(/\\/g,"/");
        this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteFile(spaceName,fileName,phone,path,this.isAdmin,this.courseName,this.type).then(res=>{
            if(res.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            if(res.data.errorCode==0){
              if(res.data.data=="1"){
                this.$message({
                  title:"提示",
                  message:"删除成功",
                  type:"success"
                })
              }
              this.path = path;
              this.searchFile();
            }
          });
        }).catch(() => {
          // this.$message({
          //   type: 'info',
          //   message: '已取消删除'
          // });
        });
      },
      refresh:function(){
        this.path="/";
        this.searchFile();
        //清空上传文件内容
        this.$refs.upload.clearFiles();
      },
      searchFile:function(){
        var user = JSON.parse(localStorage["user"]);
        var phone = this.isProject ? this.managerNo : user.username;
        // var patt = /^:|.$/;
        var patt= /[~#\^\$@%&!\*\?,`\(\)\{}\.|:\"<>]/gi;
        if(patt.test(this.path.trim().substr(1))){
          this.$message({
            type:'error',
            message:'文件夹路径不能包含特殊字符!'
          });
          return;
        }
       if(this.path.trim!=null && this.path.trim()!="" && this.path.trim().length>80){
         this.$message({
           type:'error',
           message:'文件夹路径不能超过80个字符!'
         });
         return false;
       }
        if(this.path.trim().length==0){
          this.path="/";
          this.ishidden = false;
        }else if(this.path=="/"){
          this.ishidden=false
        }else if(this.path !="/" && this.path.trim().length>0){
          this.ishidden=true
        }
        this.path = this.path.replace(/\\/g,"/");
        this._getDataShireFileList(phone,this.path,this.isAdmin,this.courseName,this.type);
      },
      _getDataShireFileList:function(phone,path,isAdmin,courseId,type){
        if(path.trim().length==0){
          this.ishidden = false;
        }else if(path=="/"){
          this.ishidden=false
        }else if(path !="/" && path.trim().length>0){
          this.ishidden=true
        }
        var paraSpaceName = null;
        if(this.isProject){
          paraSpaceName = this.spaceName;
        }
        phone = this.isProject ? this.managerNo : JSON.parse(localStorage["user"]).username;
        getDataShireFileList(phone,path,isAdmin,courseId,type,paraSpaceName).then(res=>{
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          if(res.data.errorCode==0){
            if(typeof res.data.data.fileInfo == 'string'){
              this.tableData = [];
            } else {
              this.tableData = res.data.data.fileInfo;
              console.log(this.tableData);
              this.tableData.map(item=>{
                item.path = res.data.data.path;
              })
            }

            this.spaceName = res.data.data.spaceName;
          }
        })
      },
      handleDelete(index, row) {
        console.log(index, row);
      },
      submitUpload() {
        if(this.$refs.upload.uploadFiles.length==0){
          this.$message({
            type:'warning',
            message:'请先选择文件哦！'
          })
        }
        if(this.$refs.upload.uploadFiles[0].size == 0){
          this.$message({
            type:'warning',
            message:'不允许上传空的文件！'
          });
          return;
        }
        this.$refs.upload.submit();
      },
      //文件超出
      exceedHandler(files,fileList){
        //上传文件
        /*console.log(fileList);
        console.log(files);
        console.log(this.$refs.upload);
        this.$refs.upload.clearFiles();*/
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      //返回上一级
      back(){
        //截取上一级
        if(this.path.trim().length==0){
          this.path = "/";
        } else {
          var path =  this.path.substring(0,this.path.lastIndexOf("/"));
          if(path.trim().length==0){
            this.path = "/";
          } else {
            this.path = path;
          }
        }
        this.searchFile();
      },
      uploadSuccess:function(response){
        this.$refs.upload.clearFiles();
        var user = JSON.parse(localStorage["user"]);
        var phone = this.isProject ? this.managerNo : user.username;
        if(response=="success"){
          if(this.path.trim().length==0){
            this.path = "/";
          }
          if(this.path.indexOf("/")!=0){
            this.path="/"+this.path;
          }
          this.path = this.path.replace(/\\/g,"/");
          this._getDataShireFileList(phone,this.path,this.isAdmin,this.courseName,this.type);
        }
      },
      nextPath:function(item){
        if(parseInt(item.type)==2){
          //文件夹，那么点击进入下一级
          if(this.path.lastIndexOf("/")<0){
            this.path+="/";
            this.ishidden=false
          }
          if(this.path.lastIndexOf("/")>=0 && this.path!='/'){
            this.path += "/";
            this.ishidden=true;
          }
          this.ishidden=true;
          this.path = this.path+item.name;
          this.path = this.path.replace(/\\/g,"/");
          this._getDataShireFileList(this.uploadData.phone,this.path,this.isAdmin,this.courseName,this.type);
        }
      },
      customUploadHandler:function(item){
        var obj = this;
        obj.canUpload = false;
        var fileName=item.file.name;
        var pa= /[~#\^\$@%&!\*\?,`\(\)\{}|:\"<>]/gi;
        if(pa.test(fileName.trim())){
          this.$message({
            type:'error',
            message:'文件名称不能包含特殊字符!'
          });
          obj.canUpload = true;
          return;
        }
        if(fileName.length>80){
          this.$message({
            title: "提示",
            message: "文件名长度不能超过80个字符",
            type: 'error'
          });
          obj.canUpload = true;
          return;
        }
        if(this.path.trim().length>80){
          this.$message({
            title: "提示",
            message: "文件夹路径不能超过80个字符",
            type: 'error'
          });
          obj.canUpload = true;
          return;
        }
        //自定义上传
        if(this.path.trim().length==0){
          this.path = "/";
        }
        if(this.path.indexOf("/")!=0){
          this.path="/"+this.path;
        }
        this.path = this.path.replace(/\\/g,"/");
        var patt= /[~#\^\$@%&!\*\?,`\(\)\{}\.|:\"<>]/gi;
        if(patt.test(this.path.trim())){
          this.$message({
            type:'error',
            message:'文件夹路径不能包含特殊字符!'
          });
          obj.canUpload = true;
          return;
        }
        //判断文件夹长度是否超过限制大小，limit 255，这里限制100
        if(this.path.trim() != '/') {
          //去掉首尾的/
          let path = this.path;
          if(path.indexOf("/")==0){
            path = path.substring(1);
          }
          if(path.lastIndexOf("/") == path.length-1){
            path = path.substring(0,path.length-1);
          }
          var dirs = path.split("/");
          for (let i = 0; i < dirs.length; i++) {
            var dir = dirs[i];
            if (dir.length > 80) {
              this.$message({
                title: "提示",
                message: "文件夹长度不能超过80个字符",
                type: 'error'
              });
              obj.canUpload = true;
              return;
            } else if (dir.trim().length == 0) {
              //文件夹不允许为空
              this.$message({
                title: "提示",
                message: "文件夹名称不能为空",
                type: 'error'
              });
              obj.canUpload = true;
              return;
            } else if(dir.trim()=='.'){
              this.$message({
                title: "提示",
                message: "文件夹名称不能包含特殊字符",
                type: 'error'
              });
              obj.canUpload = true;
              return;
            }
          }
        }
        this.uploadData={
          phone: this.isProject ? this.managerNo : (localStorage['user'] ? JSON.parse(localStorage['user']).username : ''),
          path:this.path
        };
        var url = item.action;
        var file = item.file;
        //判断文件大小，目前允许200M
        if(file.size>20971520){
          this.$message({
            title:"提示",
            message:"文件大小超过限额20M",
            type:'error'
          });
          obj.canUpload = true;
          return;
        }
        //判断文件名是否重复，重复给出相关提示
        let isExist = false;
        var paraPhone = JSON.parse(localStorage['user']).username;
        var paraSpaceName = null;
        if(this.isProject){
          paraPhone =  this.managerNo;
          paraSpaceName = this.spaceName;
        }
        getDataShireFileList(paraPhone,this.path,this.isAdmin,this.courseName,this.type,paraSpaceName).then(res=>{
          console.log('-----验证重复-----');
          console.log(res);
          var tableData = [];
          if(res.data.errorCode==0){
            if(typeof res.data.data.fileInfo == 'string'){
              tableData = [];
            } else {
              tableData = res.data.data.fileInfo;
            }
            for (let [idx, item] of tableData.entries()) {
              if (item.type == 1) {
                if (item.name == file.name) {
                  isExist = true;
                  break;
                }
              }
            }
            if(isExist){
              this.$confirm('存在同名的文件，是否覆盖?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                let param = new FormData();
                param.append('file', file);
                param.append('phone', this.isProject ? this.managerNo : this.uploadData.phone);
                param.append('path', this.uploadData.path);
                param.append("courseName",this.courseName);
                param.append("isAdmin",this.isAdmin);
                param.append("type",this.type);
                if(this.isProject){
                  param.append("paraSpaceName",this.spaceName);
                }
                let config = {
                  headers: {'Content-Type': 'multipart/form-data'},
                };
                axios.defaults.headers = {
                };
                obj.loading = true;
                // 添加请求头
                console.log(url);
                axios.post(url, param, config).then(res => {
                  obj.loading = false;
                  axios.defaults.headers = {
                    withCredentials: true,
                    Authorization:'Bearer '+(localStorage['token'] ? localStorage['token'] : '')
                  };
                  if(res.status==200 && res.data=='success'){
                    //文件上传成功
                    this.$message({
                      type:"success",
                      message:"上传成功",
                      title:'提示'
                    });
                    obj.$refs.upload.clearFiles();
                    this._getDataShireFileList(this.uploadData.phone,this.uploadData.path,this.isAdmin,this.courseName,this.type);
                    obj.canUpload = true;
                  } else if(res.status==200 && res.data=='nospace'){
                    this.$message({
                      type:'error',
                      message:"该用户目前不存在数猎场",
                      title:'提示'
                    });
                    obj.canUpload = true;
                  } else if(res.status == 200 && res.data=='fileAlreadyExist'){
                    this.$message({
                      type:'error',
                      message:"上传失败，存在同名的文件夹",
                      title:'提示'
                    });
                    obj.canUpload = true;
                  }
                });
              }).catch(() => {
                this.$message({
                  type: 'info',
                  message: '已取消上传'
                });
                obj.canUpload = true;
              });
            } else {
              let param = new FormData();
              param.append('file', file);
              param.append('phone', this.isProject ? this.managerNo : this.uploadData.phone);
              param.append('path', this.uploadData.path);
              param.append("courseName",this.courseName);
              param.append("isAdmin",this.isAdmin);
              param.append("type",this.type);
              if(this.isProject){
                param.append("paraSpaceName",this.spaceName);
              }
              let config = {
                headers: {'Content-Type': 'multipart/form-data'},
              };
              axios.defaults.headers = {
              };
              obj.loading = true;
              // 添加请求头
              axios.post(url, param, config).then(res => {
                obj.loading = false;
                axios.defaults.headers = {
                  Authorization:'Bearer '+(localStorage['token'] ? localStorage['token'] : '')
                };
                if(res.status==200 && res.data=='success'){
                  //文件上传成功
                  this.$message({
                    type:"success",
                    message:"上传成功",
                    title:'提示'
                  });
                  obj.$refs.upload.clearFiles();
                  obj.canUpload = true;
                  this._getDataShireFileList(this.uploadData.phone,this.uploadData.path,this.isAdmin,this.courseName,this.type);
                } else if(res.status==200 && res.data=='nospace'){
                  this.$message({
                    type:'error',
                    message:"该用户目前不存在数猎场",
                    title:'提示'
                  });
                  obj.canUpload = true;
                } else if(res.status == 200 && res.data=='fileAlreadyExist'){
                  this.$message({
                    type:'error',
                    message:"上传失败，存在同名的文件夹",
                    title:'提示'
                  });
                  obj.canUpload = true;
                }
              });
            }
          }
        });

      },
      init:function(){
        this.actionUrl = shu_lie_yun_url+"/api/uploadFileToHdfs";
        var user = JSON.parse(localStorage["user"]);
        var phone = user.username;
        var isAdmin = false;
        var courseName = '';
        var type = 1;
        if(typeof this.$route.params.projectId != 'undefined'){
          this.isProject = true;
          this.projectId = this.$route.params.projectId;
          this.isAdmin = false;
          phone = this.$route.params.managerNo;
          this.managerNo = phone;
          this.spaceName = this.$route.params.paraSpaceName;
          getUserProject(this.projectId).then(res=>{
            if(res.data.errorCode == 0){
              var data = res.data.data;
              var flag = false;
              for(var i = 0;i < data.userInProject.length;i++){
                if(user.username == data.userInProject[i].no){
                  //是否是项目内成员
                  flag = true;
                  break;
                }
              }
              if(user.username == data.userIsManager[0].no){
                flag = true;
              }
              if(!flag){
                this.isAuthority = false;
              }
            }
          });
        }
        if(typeof this.$route.params.isAdmin != 'undefined'){
          isAdmin = true;
          type = this.$route.params.isAdmin;
        }
        if(typeof this.$route.params.courseName != 'undefined'){
          courseName = this.$route.params.courseName;
        }
        this.type = type;
        this.isAdmin = isAdmin;
        this.courseName = courseName;
        this.path="/";

        this._getDataShireFileList(phone,this.path,isAdmin,courseName,type);
      }
    },
    created:function(){
       this.init();
       console.log("hunting-file初始化");
    },
    watch:{
      '$route':function(){

        if(this.$route.fullPath.indexOf("/htSj/file/") >=0 || this.$route.fullPath.indexOf("/scientificData/file") >= 0){
          this.init();
        }
      }
    }
    // beforeRouteUpdate(to,from,next){
    //   this.init();
    //   next();
    // },
    // beforeRouteEnter: (to, from, next) => {
    //   console.log("beforeEnter");
    //   next(vm => {
    //     vm.init();
    //   });
    // }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  #share
    border 1px solid rgb(220, 223, 230)
    /*border: 1px solid blue;*/
  .el-collapse-item__header
    color #909399 !important
  .el-input-group__append, .el-input-group__prepend
    color #ffffff
  body div#main
    display inline-block
</style>
<style type="text/css">
  #share .el-upload-list__item-name{
    white-space: pre-wrap
  }
</style>
