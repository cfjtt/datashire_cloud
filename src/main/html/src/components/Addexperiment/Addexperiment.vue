<template>
    <div id="addExperiment">
      <!--<h1 class="title">{{moduleName}}</h1>-->
      <h1 class="title">添加实验</h1>
      <!--表单-->
      <el-form :inline="true" :model="formInline"
               :rules="rules" ref="formInline" class="demo-form-inline">
        <el-form-item label="实验类型" prop="cloudwareType">
          <el-select v-model="formInline.cloudwareType"  placeholder="请选择实验类型">
            <el-option label="编程类－Rstudio" value="1"></el-option>
            <el-option label="编程类－Python" value="2"></el-option>
            <el-option label="编程类－Base" value="3"></el-option>
            <el-option label="编程类－Hadoop" value="4"></el-option>
            <el-option label="编程类－JupyterPython" value="5"></el-option>
            <el-option label="编程类－IdeJava" value="6"></el-option>
            <el-option label="图形化－数猎云" value="7"></el-option>
          </el-select>

        </el-form-item>
        <el-form-item label="实验名称" prop="experimentName">
          <el-input v-model="formInline.experimentName" placeholder="实验名称"></el-input>
        </el-form-item>
        <br/>
        <el-form-item label="实验描述" prop="experimentDes">
          <el-input type="textarea" v-model="formInline.experimentDes" placeholder="实验描述"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit('formInline')">保存</el-button>
        </el-form-item>
      </el-form>
      <div id="mark">
        <el-tabs type="border-card" v-model="activeName">
          <el-tab-pane label="上传markdown文件" name="uploadMark">
            <el-upload
              class="upload-demo"
              ref="upload"
              :action="actionUrl"
              :auto-upload="false"
              :on-success="uploadSuccess"
              :headers="uploadHeaders"
              :before-upload="beforeUploadHandler"
              accept=".md"
              :limit="1"
              :on-exceed="handleExceed" v-loading="loading">
              <el-button size="small" slot="trigger" type="primary">选择文件</el-button>
              <el-button size="small"  type="success" @click="submitUpload()">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">只能上传markdown文件</div>
            </el-upload>
          </el-tab-pane>
          <el-tab-pane label="在线编辑markdown" name="editMark">
            <mavon-editor v-model="markvalue" :ishljs = "true" :scrollStyle="true"
                          ref="md" @imgAdd="imgAddHandler"
                          style="margin-top: 0px;"
                          :value="markvalue"
                          :toolbars="toobars"
                          :imageFilter="imgFilter"
                          :imageClick="imgClick"
                          @fullScreen="fullLargeScreen"/>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
</template>

<script type="text/ecmascript-6">
  import Vue from 'vue'
  import mavonEditor from 'mavon-editor'
  import 'mavon-editor/dist/css/index.css'
  import {addExperiment,shu_lie_yun_url,edu_url,getServerTime,experimentIsExist} from '@/common/js/request'
  import axios from 'axios'
  import $ from 'jquery'
  export default {
    data() {
      var validaName = (rule, value, callback) => {
        if (typeof value == 'undefined' ||  value == '') {
          callback(new Error('请输入实验名称'));
        }else if(value.trim().length>40){
          callback(new Error('实验名称长度不能大于40'));
        }
        // else if(value.trim().length != value.length){
        //   callback(new Error('不能输入空格'));
        // }
        else if((/^\s+/).test(value)){
          callback(new Error('开头不能输入空格'));
        }else if((/\s+$/).test(value)){
          callback(new Error('结尾不能输入空格'));
        }
        else {
          var moduleId = this.moduleId;
          console.log(value);
          experimentIsExist(moduleId,value).then(res=>{
            console.log(res);
            if(res.data.errorCode==0){
              if(res.data.data){
                callback(new Error("该实验已存在"))
              } else {
                callback();
              }
            }
          })
        }
      };
      // 实验描述校验
      var validateExperimentDes = (rule, value, callback) => {
        if ( typeof value === 'undefined' || value=="") {
          callback(new Error('请输入实验描述'));
        }else if(value.trim().length >1024){
          callback(new Error('实验描述长度不能超过1024'));
        }else if((/^\s+/).test(value)){
          callback(new Error('开头不能输入空格'));
        }else if((/\s+$/).test(value)){
          callback(new Error('结尾不能输入空格'));
        } else {
          callback();
        }
      };
      // 实验类型
      var validaTtype = (rule, value, callback) => {
        if (typeof value == 'undefined' ||  value == '') {
          callback(new Error('请输入实验类型'));
        } else {
          callback();
        }
      };
      return {
        activeName:'uploadMark',
        formInline: {},
        markvalue:'',
        courseId: -1,
        moduleId:null,
        shu_lie_yun_url:shu_lie_yun_url,
        actionUrl:null,
        uploadHeaders: {
          'Authorization': 'Bearer ' + (localStorage['token'] ? localStorage['token'] : '')
        },
        photoVisible:false,
        fileList2: [],
        loading:false,
       // moduleName:"",
        rules: {
          //实验类型
          cloudwareType: [
            // { required: true, message: '请选择实验类型', trigger: 'change' }
            // validaTtype
            { required: true,validator: validaTtype, trigger: 'blur' }
          ],
          //实验名称
          experimentName: [
            { required: true,validator: validaName, trigger: 'blur' }
            // { required: true, message: '请输入实验名称', trigger: 'blur' },
            // { min: 1, max: 40, message: '长度在 1 到 40 个字符', trigger: 'blur' },
          ],
          experimentDes: [
            {required: true,validator: validateExperimentDes, trigger: 'blur'}
            // { required: true, message: '请输入实验描述', trigger: 'blur' },
            // { min: 1, max: 40, message: '长度在 1 到 40 个字符', trigger: 'blur' },
          ]
        }, toobars:{
          bold: true, // 粗体
          italic: true, // 斜体
          header: true, // 标题
          underline: true, // 下划线
          strikethrough: true, // 中划线
          mark: true, // 标记
          superscript: true, // 上角标
          subscript: true, // 下角标
          quote: true, // 引用
          ol: true, // 有序列表
          ul: true, // 无序列表
          link: false, // 链接
          imagelink: true, // 图片链接
          code: true, // code
          table: true, // 表格
          fullscreen: true, // 全屏编辑
          readmodel: false, // 沉浸式阅读
          htmlcode: true, // 展示html源码
          help: true, // 帮助
          undo: true, // 上一步
          redo: true, // 下一步
          trash: true, // 清空
          save: false, // 保存（触发events中的save事件）
          navigation: true, // 导航目录
          alignleft: true, // 左对齐
          aligncenter: true, // 居中
          alignright: true, // 右对齐
          subfield: true, // 单双栏模式
          preview: true
        }
      }
    },
    methods: {
      onSubmit(formInline) {
        this.$refs[formInline].validate((valid) => {
          if (valid) {
            // alert('submit!');
            //this.$message.success("提交成功")
            this.formInline.moduleId = parseInt(this.moduleId);
            this.formInline.experimentContent = this.markvalue;
            getServerTime().then(res => {
              if(res.data.errorCode==0){
                this.formInline.experimentCreateDate = new Date(res.data.data);
                addExperiment(this.formInline).then(res=>{
                  if(res.data.errorCode==0){
                    this.$message({
                      title:"提示",
                      message:"添加成功",
                      type:"success"
                    })
                    this.$router.history.go(-1)
                  }else{
                    this.$message({
                      type: 'error',
                      message: res.data.message
                    });
                    if(res.data.errorCode == 2){
                      this.$router.push({ path :'/kecheng'});
                    }else{
                      this.$router.history.go(-1)
                    }
                  }
                })
              }
            });

          } else {
            console.log('error submit!!');
            // this.$message.error("提交失败")
            return false;
          }
        });
      },
      imgAddHandler:function(filename,imgFile){
        if(imgFile.type != 'image/jpeg'  && imgFile.type != 'image/png'){
          this.$message({
            type:'warning',
            message:"只能上传jpg,png格式的图片"
          });
          return;
        }
        let param = new FormData();
        param.append('file', imgFile);
        param.append('type', '2');
        param.append('courseId', this.$route.params.courseId);
        let config = {
          headers: {'Content-Type': 'multipart/form-data'},
        };

        //console.log('get ', param.get('file'));
        // 添加请求头
        axios.post('/admin/course/experiment/piclib', param, config).then(res => {
          if(res.status==200 && res.data.errorCode==0){
            //文件上传成功
            var path = res.data.data.url;

            var url = "/previewImage?path="+path;
            //console.log(path)
            this.$refs.md.$img2Url(filename, url);
          }
        });
      },
      imgFilter:function(imgFile){
        if(imgFile.type != 'image/jpeg'  && imgFile.type != 'image/png'){
          this.$message({
            type:'warning',
            message:"只能上传jpg,png格式的图片"
          });
          var inputEle = $("div[class='dropdown-item'] input[type='file']")[0];
          //dadEle.
          $(inputEle).attr("title"," ");
          /*try{
            this.$refs.md.$refs.toolbar_left.$imgDelByFilename(imgFile.name);
            console.log("xxxx");
          }catch (e){
            console.log(e)
          }*/
          return false;
        }
        return true;
      },
      imgClick:function(obj){
        //获取图片原始大小
        var width = obj.naturalWidth;
        var height = obj.naturalHeight;
        //获取屏幕的大小
        var screenWidth = screen.availWidth;
        var screenHeight = screen.availHeight;
        console.log(width+":"+height);
        console.log(screenWidth+":"+screenHeight);
        var imgWidth = width;
        var imgHeight = height;
        if(width>=(screenWidth-100)){
            imgWidth = screenWidth/2;
            imgHeight = imgWidth / (width/height);
        } else {
          imgWidth = width;
          imgHeight = height;
        }
        var src = obj.getAttribute("src");
        this.$refs.md.d_preview_imgsrc = src;
        this.$nextTick(()=>{
          var imgParent= document.getElementsByClassName("v-note-img-wrapper")[0];
          var imgElm = imgParent.getElementsByTagName("img")[0];
          //设置大小
          imgElm.width = imgWidth;
          imgElm.height = imgHeight;
        })
      },
      uploadSuccess:function(response){
        this.loading = false;
        if(response.errorCode==0){
          /*this.$refs.upload.clearFiles();*/
          this.markvalue = response.data;
          //this.$refs.md.value = this.markvalue;
        }
      },
      submitUpload:function(){
        //上传
        this.$refs.upload.submit();
      },
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },
      beforeUploadHandler:function(file){
        console.log(file);
        //判断是否是markdown文件
        if(file.name.lastIndexOf('.md')>-1){
          this.loading = true;
          return true;
        }
        else {
          this.$message({
            title:"提示",
            message:"只允许上传markdown文件",
            type:"error"
          });
          this.loading = false;
          return false;
        }
      },
      handleClose(){},
      fullLargeScreen:function(Boolean , String){
        console.log("123")
        // document.getElementsByClassName("el-header")[0].style.display="none"
        console.log("状态为:"+Boolean)
        // 全屏
        if(Boolean==true){
          document.getElementById("header").style.display="none"
          if(screen.width <1540){
            document.getElementById("mark").getElementsByClassName("v-note-panel")[0].style.maxHeight="590px"
          }else if(screen.width >1540 && screen.width <2200){
            document.getElementById("mark").style.maxHeight="1040px"
            document.getElementById("mark").getElementsByClassName("v-note-panel")[0].style.maxHeight="1030px"
          }
        }else if(Boolean==false){
          // 取消全屏
          document.getElementById("header").style.display=""
          if(screen.width <1540 ){
            document.getElementById("mark").getElementsByClassName("v-note-panel")[0].style.maxHeight="280px"
          }else if(screen.width >1540 && screen.width <2200){
            document.getElementById("mark").getElementsByClassName("v-note-panel")[0].style.maxHeight="750px"
          }
        }
      }
    },
    created:function(){
      var moduleId = this.$route.params.moduleId;
      this.courseId = this.$route.params.courseId;
      // var moduleName = this.$route.params.moduleName;
      this.moduleId = moduleId;
     // console.log(moduleName)
      //this.getExperimentData()
      this.formInline = {}
      this.actionUrl = "/admin/course/experiment/markdownWithResolve";
      this.$refs.upload.clearFiles();
      console.log(this.moduleId)
    },
    activated: function () {
      this.loading = false;
      // this.$router.push()
      // 进入页面，重新刷新
      var route=this.$route.fullPath;
      this.$router.push({path:route});
      console.log("屏幕宽度为:"+screen.width)
    },
    beforeRouteEnter: (to, from, next) => {
      next(vm => {
        var moduleId = vm.$route.params.moduleId;
        vm.moduleId = moduleId;
        vm.formInline = {};
        vm.markvalue="";
        vm.$refs.upload.clearFiles();
        vm.$refs.formInline.resetFields();
        //清空历史记录
        try {
          vm.$refs.md.d_history = new Array();
          vm.$refs.md.$children[0].img_file.splice(1);
          vm.$nextTick(()=>{
            try {
              //this.$refs.md.$children[0].$mouseenter_img_dropdown = (() => {
              var imgElm = document.getElementsByClassName("op-image")[0];
              if (imgElm.getElementsByClassName("dropdown-item").length > 1) {
                var childElm = imgElm.getElementsByClassName("dropdown-item")[0];
                imgElm.removeChild(childElm);
              }
              //this.$refs.md.$children[0].s_img_dropdown_open = true;
              //});
            } catch (e){}
          });
        } catch (e){}
        // 进入页面,默认显示第一栏
        vm.activeName="uploadMark"
        // var nowRoute=this.$route.fullPath
        // vm.$router.push({path:nowRoute});
      });
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  .demo-form-inline
    width 96%
    height 110px
    /*border 1px solid red*/
    margin 20px auto
</style>
<style type="text/css">
  @import "../../common/css/banji.css";
  #addExperiment{
    width: 100%;
    height: 97%;
   /* border: 1px solid red;*/
    /*border: 1px solid red;*/
  }

  #mark .el-tabs__content, .el-tabs__header{
    z-index: 1;
  }
  .el-textarea__inner{
    height: 40px;
    color: #606266 !important;
    font-size: inherit;
    font-family: '微软雅黑';
  }
  #addExperiment .title{
    width: 96%;
    display: block;
    font-size: 22px;
    color: #000000;
    margin: 20px auto;
  }
</style>
