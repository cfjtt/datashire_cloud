<template>
    <div v-loading="updateFileLoading" style="padding-bottom: 60px;">
      <!--头部-->
      <h-eader></h-eader>
      <div id="scienUpdateFile">
        <h1 id="title" class="kong breakWord">{{title}}</h1>
        <div id="FileTitle" v-if="isShowTitle">
          <p>文档标题</p>
          <el-input id="fileinput" v-model="docName"  maxlength="45" placeholder="文档名称"></el-input>
        </div>
        <!--//图文显示-->
        <!--<div id="imageText" v-if="isShowConent" readonly="readonly" v-html="docContent">
        </div>-->
        <iframe width="1000" height="500" id="MyIFrame" v-if="isShowConent">
        </iframe>
        <!--文本编辑器-->
        <div id="finishFile" v-show="isUpload">
          <div class="edit_container">
            <quill-editor style="min-height: 180px;"
              id="editor"
              ref="myQuillEditor"
              :content="docContent"
              :options="editorOption"
              class="editer"
              @change="onEditorChange($event)"
              @focus="onEditorFocus($event)"
              @ready="onEditorReady($event)"></quill-editor>
            <!--弹出框-->
            <!--<div id="EditInput">-->
              <!--<p style="width: 80px;line-height: 40px;">文本</p>-->
              <!--<el-input v-model="EditInput" placeholder="请输入内容"></el-input>-->
              <!--<el-button type="primary">保存</el-button>-->
            <!--</div>-->
          </div>

        </div>
        <el-upload style="margin-top: 10px;"
          class="upload-demo1"
          id="uploadImageBtn"
          :action="actionImageUrl"
           name="img"
          :headers="uploadHeaders"
          :show-file-list="false"
          :on-error="uploadError"
          :data="imageData"
          accept=".jpg,.png"
          :http-request="customUploadImage">
        </el-upload>

        <!--添加附件-->
        <div id="editTable">
          <!--<el-button>添加附件</el-button>-->
          <el-upload
            class="upload-demo"
            :action="actionUrl"
            :on-change="handleChange"
            :data="uploadData"
            :http-request="customUploadHandler"
            :file-list="fileList3"
            v-loading="loading"
             v-if="isUpload">
            <el-button size="small" type="primary">添加附件</el-button>
          </el-upload>
          <!--进度条-->
      <!--    <div style="width: 100%;display: flex;margin-top: 10px;margin-bottom: 10px;">
            <p style="width: 100px;">文件名称:</p>
            <el-progress :text-inside="true" :stroke-width="18"
                         :percentage="70" style="width: 300px;"></el-progress>
          </div>-->
          <el-table id="tableData"
                    :data="tableData"
                    style="width: 100%">
            <el-table-column>
              <template slot-scope="scope">
                <span style="margin-left: 10px">{{ scope.row.fileName }}</span>
              </template>
            </el-table-column>
            <el-table-column>
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleEdit(scope.$index, scope.row)">下载</el-button>
                <el-button
                  v-if="isUpload"
                  size="mini"
                  type="danger"
                  @click="handleDelete(scope.$index, scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="float: right">
            <el-button type="primary" style="margin-top: 10px;" @click="savaData" v-if="isUpload">保存</el-button>
            <el-button  style="margin-top: 10px;" @click="back">返回</el-button>
          </div>
        </div>
      </div>
    </div>
</template>

<script type="text/ecmascript-6">
  import header from "../header/header";
  // import vueQuillEditor from '../scientific-ue/scientific-ue.vue'
  import {quillEditor} from 'vue-quill-editor'
  import 'quill/dist/quill.core.css'
  import 'quill/dist/quill.snow.css'
  import 'quill/dist/quill.bubble.css'
  import axios from 'axios';
  import {
    deleteDocFile,
    edu_url,
    findDocAnnexBydocId,
    saveEditorProjectDoc,
    updateProjectDoc,
    isProjectUser
  } from '@/common/js/request'
  const toolbarOptions =[
    ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
    ['blockquote', 'code-block'],

    [{'header': 1}, {'header': 2}],               // custom button values
    [{'list': 'ordered'}, {'list': 'bullet'}],
    [{'script': 'sub'}, {'script': 'super'}],      // superscript/subscript
    [{'indent': '-1'}, {'indent': '+1'}],          // outdent/indent
    [{'direction': 'rtl'}],                         // text direction

    [{'size': ['small', false, 'large', 'huge']}],  // custom dropdown
    [{'header': [1, 2, 3, 4, 5, 6, false]}],

    [{'color': []}, {'background': []}],          // dropdown with defaults from theme
    [{'font': []}],
    [{'align': []}],
    ['link', 'image', 'formula']//去除video即可
  ]
  export default {
    components: {
      quillEditor,
      "h-eader": header
    },
      data() {
        return {
          editorOption:{
            modules:{
              toolbar: {
                container: toolbarOptions,  // 工具栏
                handlers: {
                  'image': function (value) {
                    if (value) {
                      console.log("点击图片");
                      document.querySelector('.upload-demo1 input').click()
                    } else {
                      this.quill.format('image', false);
                    }
                  },
                  // 'link':function (value) {
                  //   if(value){
                  //     console.log("点击链接");
                  //     var testStr=window.getSelection().toString();
                  //     console.log(testStr);
                  //   }else{
                  //     this.quill.format('link', false);
                  //   }
                  // }
                }
             }
            },
            placeholder:'请输入文档内容',
            readOnly: false
          },
          tableData: [],
          docName:'',
          docId:'',
          projectId:0,
          updateTime:null,
          docContent:'请输入文档内容',
          userId:JSON.parse(localStorage["user"]).userId,
          fileList3:[],
          actionUrl:null,
          uploadData:{},
          loading:false,
          folderId:0,
          title:"",
          isShowTitle:false,
          isUpload:false,
          isShowConent:false,
          para:null,
          actionImageUrl:'',
          imageData:{},
          uploadHeaders: {
            'Authorization': 'Bearer ' + (localStorage['token'] ? localStorage['token'] : '')
          },
          EditInput:'',
          updateFileLoading:false
        }
      },
    created:function(){
      this.projectId=this.$route.query.projectId;
      this.docId=this.$route.query.docId;
      this.folderId=this.$route.query.folderId;
      this.findDocById();
      this.actionUrl="/doc/uploadDocFile";
      this.isUser();
      this.actionImageUrl="/doc/uploadDocImage";
      this.$nextTick(function(){
        this.$refs.myQuillEditor.quill.enable(true);
        this.$refs.myQuillEditor.quill.focus();
        console.log("富文本编辑器");
        console.log(this.$refs.myQuillEditor);

      })
    },
    computed: {
      editor() {
        return this.$refs.myQuillEditor.quill
      }
    },
      methods: {
        isUser:function () {
          isProjectUser(this.projectId,this.userId).then(res=>{
            console.log("是否有权限");
            if(res.status==200){
              if(res.data.data==1){
                this.isShowTitle=true;
                this.isUpload=true;
                this.isShowConent=false;
              }else{
                this.isShowTitle=false;
                this.isUpload=false;
                this.isShowConent=true;
                if(this.isShowConent){
                  console.log("啊啊啊");
                  this.$nextTick(()=>{
                    console.log(document.getElementById("MyIFrame"));
                    var doc = document.getElementById("MyIFrame").contentDocument;
                    doc.body.innerHTML = this.docContent;
                  })

                }
              }
            }
          })
        },

        onEditorChange({editor,html,text}){
          this.docContent = html;
          console.log("change");
          var length=html.length;
          this.$refs.myQuillEditor.quill.setSelection(length + 1);
          console.log(html);
        },
        onEditorFocus(event){

        },
        onEditorReady(event){

        },
        findDocById:function () {
          updateProjectDoc(this.docId).then(res=>{
            console.log(res);
            if(res.status==200){
              if(res.data.errorCode==0){
                this.docName=res.data.data.projectDoc.docName;
                this.updateTime=res.data.data.projectDoc.updateTime;
                this.docContent=res.data.data.projectDoc.content;
                this.tableData=res.data.data.docAnnexList;
                this.title=this.docId+"/"+res.data.data.projectDoc.docName;

              }
            }
          });
        },
        findAnnex:function () {
          findDocAnnexBydocId(this.docId).then(res=>{
              this.tableData=res.data.data;
          });
        },
        handleEdit(index, row) {
          console.log("下载!");
          var name=encodeURIComponent(row.fileName);
          window.location.href=edu_url+"/doc/downloadDocFile?docId="+encodeURIComponent(this.docId)+"&docAnnexName="+encodeURIComponent(name);
        },
        uploadSuccess:function(response){
          console.log("上传成功");
          this.fileList3=[];
          this.loading = false;
          this.$refs.upload.clearFiles();
          if(response=="success"){
            this.findAnnex();
          }
        },
        handleDelete(index, row) {
          this.$confirm('请注意，该附件将从系统被删除，是否确定执行此操作', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            var fileName=row.fileName;
            deleteDocFile(this.projectId,this.docId,this.userId,fileName).then(res=>{
              if(res.status==200){
                if(res.data.errorCode==0){
                  this.$message({
                    title:"提示",
                    message:"删除成功",
                    type:'success'
                  });
                  this.findAnnex();
                }else{
                  this.$message({
                    title:"提示",
                    message:res.data.message,
                    type:'error'
                  });
                  this.findAnnex();
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
        // 校验
        _validateForm:function(){
          var errormsg = null
          if(this.docName==null || this.docName=="" || this.docName.length==0){
            errormsg="请输入文档名称"
          }
          if(errormsg && errormsg.length >0){
            this.$message.error(errormsg)
            return false;
          }
          return true
        },
        // 保存
        savaData:function () {
          if(!this._validateForm()){
            return;
          }
          console.log("用户名"+this.userId);
          saveEditorProjectDoc(this.docId,this.docContent,this.userId,this.docName,this.projectId,this.updateTime).then(res=>{
            if(res.status==200){
              if(res.data.errorCode==0){
                this.$message({
                  title:"提示",
                  message:"编辑成功",
                  type:'success'
                });
                this.$router.push({path: '/scientificFile', query:{name: 'scientificFile',projectId:this.projectId,folderId:this.folderId}});
              }else{
                this.$message({
                  title:"提示",
                  message:res.data.message,
                  type:'error'
                });
                this.findDocById();
              }
            }
          });
        },
        handleChange(file, fileList) {
          this.fileList3 = fileList.slice(-3);
        },
        customUploadHandler:function (item) {
          console.log("上传文件!");
          this.findAnnex();
          this.updateFileLoading=true
          var fileCount=this.tableData.length;
          console.log(fileCount+"文件个数");
          if(fileCount==20){
            this.$message({
              title: "提示",
              message: "最多可上传20个附件!",
              type: 'error'
            });
            this.fileList3=[];
            this.loading = false;
            return;
          }
          var filename=item.file.name;
          var pa= /[\*\?`\.|:\"<>\/\\]/gi;
          if(filename.lastIndexOf('.') != -1){
            filename = filename.substr(0,filename.lastIndexOf('.'));
          }
          if(pa.test(filename.trim())){
            this.$message({
              title: "提示",
              message: "文件名称包含不合法字符!",
              type: 'error'
            });
            this.updateFileLoading=false
            this.fileList3=[];
            this.loading = false;
            return;
          }
            if(filename.length>80){
              this.$message({
                title: "提示",
                message: "文件名不能超过80个字符!",
                type: 'error'
              });
              this.updateFileLoading=false
              this.fileList3=[];
              this.loading = false;
              return;
            }
          var url = item.action;
          var file = item.file;
          //判断文件大小，目前允许200M
          if(file.size>20971520){
            this.$message({
              title:"提示",
              message:"文件大小超过限额20M!",
              type:'error'
            });
            this.updateFileLoading=false
            this.fileList3=[];
            this.loading = false;
            return;
          }
          if(file.size==0){
            this.$message({
              title:"提示",
              message:"文件大小不能为空!",
              type:'error'
            });
            this.updateFileLoading=false
            this.fileList3=[];
            this.loading = false;
            return;
          }
          this.loading = true;
          let param = new FormData();
          // 添加请求头
          param.append('projectId', this.projectId);
          param.append('docId', this.docId);
          param.append('userId', this.userId);
          param.append('file', file);
          axios.post(url, param).then(res => {
            console.log("开始上传!"+param);
            console.log(res);
              if(res.status==200){
                if(res.data.errorCode==0){
                  this.fileList3=[];
                  this.loading = false;
                  this.findAnnex();
                  this.updateFileLoading=false
                }else{
                  console.log("失败");
                  this.$message({
                    title:"提示",
                    message:res.data.message,
                    type:'error'
                  });
                  this.updateFileLoading=false
                  this.fileList3=[];
                  this.loading = false;
                  this.findAnnex();
                }
              }
          })


        },
        customUploadImage:function (item) {
          console.log("图片上传前");
          var file = item.file;
          console.log(file);
          const isJPG = file.type === 'image/jpeg';
          const isPNG=file.type === 'image/png';
          const isLt2M = file.size / 1024 < 200;
          if (!isJPG && !isPNG) {
            this.$message({
              title:'提示',
              message:'图片格式不正确',
              type:'error'
            });
            return false;
          }
          if (!isLt2M) {
            this.$message({
              title:'提示',
              message:'上传图片大小不能超过 200kB!',
              type:'error'
            });
            return false;
          }
          var url = item.action;
          let param = new FormData();
          // 添加请求头
          param.append('projectId', this.projectId);
          param.append('docId', this.docId);
          param.append('userId', this.userId);
          param.append('file', file);
          axios.post(url, param).then(res => {
            if(res.status==200){
              if(res.data.errorCode==0){
                this.uploadSuccessImage(res);
              }else{
              }
            }
          });
        },
        uploadSuccessImage:function(res) {
          console.log("上传成功!");
          console.log(res);
          // res为图片服务器返回的数据
          // 获取富文本组件实例
          let quill = this.$refs.myQuillEditor.quill;
          // 如果上传成功
          if (res.status === 200 && res.data.data!== null) {
            // 获取光标所在位置
            let length = quill.getSelection().index;
            // 插入图片  res.info为服务器返回的图片地址
            quill.insertEmbed(length, 'image', res.data.data);
            // 调整光标到最后
            quill.setSelection(length + 1)
          } else {
            this.$message.error('图片插入失败');
          }
          // loading动画消失
          this.quillUpdateImg = false
        },
        uploadError(res, file) {
          console.log("图片上传失败");
        },
        // 返回上一级
        back(){
          this.$router.push({path: '/scientificFile', query:{name: 'scientificFile',projectId:this.projectId,folderId:this.folderId}});
        }
      },
    beforeRouteEnter: (to, from, next) => {
      console.log("我是路由，我进入了");
      next(vm => {
        console.log(vm.$route.query);
        vm.projectId=vm.$route.query.projectId;
        vm.folderId=vm.$route.query.folderId;
        vm.docId=vm.$route.query.docId;
      });
    }
    }
</script>

<style lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  #scienUpdateFile
    margin-top 60px
    height auto
    /*border 1px solid red*/
    #title
      margin 20px 3% 0px
      height 20px
      font-size 22px
      font-family  "Microsoft YaHei"
    #finishFile,#imageText
      margin 20px 3% 0px
      width 94%
      /*border 1px solid red*/
    #finishFile
      position relative
      min-height  180px
      /*border 1px solid red*/
      #EditInput
        position absolute
        left 20px
        bottom 10px
        z-index 999
        display flex
    #imageText
      border 1px solid rgb(220, 223, 230)
      width 94%
      min-height 200px
      padding 10px
      line-height 30px
    #editTable
      width 94%
      margin 20px 3% 0px
    #FileTitle
      display flex
      width 94%
      height 40px
      margin 20px 3% 0px
      /*border 1px solid red*/
      p
        width 100px
        height 40px
        line-height 40px
      #fileinput
        width calc(100% - 120) !important

  body{
    -moz-user-select: all !important;
    -webkit-user-select: all !important;
    -khtml-user-select: all;
    -ms-user-select: all;
    user-select:all !important;
  }
</style>
<style type="text/css">
  .ql-editor{
    position: relative;
    min-height: 140px;
    padding-bottom: 60px;
  }
  .ql-editor div:last-child{

  }
</style>
