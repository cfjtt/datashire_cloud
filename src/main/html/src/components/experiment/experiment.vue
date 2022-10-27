<template >
    <div style="padding: 20px 40px;">
      <div class="experiment">
        <div class="Title">
          <h2 style="margin-left: -24px" class="kong">{{courseName}} / 课程维护 </h2>
          <i class="el-icon-circle-plus-outline" @click="adddModuleDialog('form')"></i>
        </div>
        <div style="clear: both"></div>
        <!--进入实验-->
        <el-collapse style="width: 100%;" >
          <el-collapse-item Consistency
                            v-for="(item,idx) in tableData"
                            :key="idx"
                            :moduleId="item.moduleId"
                            @click.native="handleChange(idx)">
            <template slot="title">
              <span class="caret-wrapper" >
                <el-button type="text" class="sort-caret ascending"
                   ref="isActiveA"
                   :disabled="idx == 0"
                   :class="{'active':item.orderId !=activeOrderA}"
                    @click="topSort(item.moduleId,item.orderId)"></el-button>
                 <!--:disabled="{true:item.orderId !=activeOrderB}"-->

                <el-button type="text" class="sort-caret descending"
                   ref="isActiveB"
                    :disabled="item.orderId ==activeOrderB"
                   :class="{'active':item.orderId !=activeOrderB}"
                    @click="downSort(item.moduleId,item.orderId)"></el-button>
              </span>
              <!--<span>序号为::(item)</span>-->
              <span style="margin-right: 10px;">第<!--{{item.orderId.toLocaleString('zh-hans-CN-u-nu-hanidec').split(',').join('')}}-->{{item.orderIdCN }}章</span>
              <div id="exModuleName" >{{item.moduleName}}</div>
              <div id="colapse-title" :ref="idx">展开</div>
              <!--<div class="det_course"  @click="deleteDialog(item.moduleId)">删除章节</div>-->
              <el-button class="det_course" type="danger" @click.native="deleteDialog(item.moduleId)">删除章节</el-button>
              <el-button class="edit_course" type="danger" @click.native="editDialog(item.moduleId,item.moduleName)">编辑章节</el-button>
              <!--<div class="icon-radius"></div>-->
              <div style="clear: both"></div>

            </template>
            <template style="width: 100% !important;">
              <div style="padding: 20px 30px;" @click.stop="handleChange">
                <el-table
                  :data="item.moduleContent"
                  style="width: 100% !important;text-align: center;font-size: 15px"
                  header-row-class-name="column-center"
                  tooltip-effect="dark"
                  @click.stop="handleChange">
                  <el-table-column
                    width="50">
                    <template slot-scope="scope">
                       <span class="caret-wrapper" >
                         <!--:class="{'active':scope.row.orderId !=activeExmentOrderA}"-->
                         <!--:class="item.moduleContent.length == scope.row.orderId ? '':'active'"-->


                         <el-button type="text" class="sort-caret ascending"
                           ref="isActiveA"
                           :disabled="scope.$index == 0"
                           :class="{'active':scope.$index!=0}"
                           @click="expermentTopSort(scope.row.id,scope.row.orderId,scope.row.courseId)"
                           >
                        </el-button>
                        <el-button type="text" class="sort-caret descending"
                           ref="isActiveB"
                           :disabled="scope.$index==item.moduleContent.length-1"
                           :class="{'active':scope.$index!=item.moduleContent.length-1}"
                           @click="expermentDownSort(scope.row.id,scope.row.orderId,scope.row.courseId)"
                           >
                        </el-button>
                          <!--:class="{'active':=scope.row.orderId ==scope.row.orderId}"-->
                      </span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    id="ExTable"
                    label="序号">
                    <template slot-scope="scope">实验{{scope.row.ExpOrderCN}}</template>
                  </el-table-column>
                  <el-table-column
                    prop="experimentName"
                    label="实验名称"
                    show-overflow-tooltip>
                  </el-table-column>
                  <el-table-column
                    prop="experimentDes"
                    label="实验描述"
                    show-overflow-tooltip>
                  </el-table-column>
                  <el-table-column
                    prop="cloudwareType"
                    show-overflow-tooltip
                    label="实验类型">
                    <template slot-scope="scope">{{scope.row.cloudwareType}}</template>
                  </el-table-column>
                  <el-table-column
                    prop="publishDate"
                    show-overflow-tooltip
                    label="创建时间">
                  </el-table-column>
                  <el-table-column
                    label="操作"
                    width="200"
                    :render-header="renderHeader">
                    <template slot-scope="scope">
                      <el-radio-group size="mini">
                        <el-radio-button class="testBtn" label="数据管理" @click.native="data(courseId,courseName)"></el-radio-button>
                        <el-radio-button class="testBtn" label="创建demo" @click.native.prevent="getEx(item,scope)" v-if="scope.row.showEx"></el-radio-button>
                        <el-radio-button class="testBtn" label="编辑" @click.native.prevent="updateExDialog(item,scope)"></el-radio-button>
                        <el-radio-button class="detBtn" label="删除" @click.native.prevent="deleteExById(scope.row)"></el-radio-button>
                        <el-radio-button class="testBtn" label="编辑答案" @click.native="test(courseId,courseName,scope.row.experimentName,item.moduleName,scope.row.cloudwareType,scope.row.id)"></el-radio-button>
                      </el-radio-group>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
              <!--<div style="margin-top: 20px;float: right;margin-right: 1%">-->
                <!--<el-button size="mini" round type="primary" @click="Addexperiment(item.moduleId)">添加实验</el-button>-->
              <!--</div>-->
            </template>
          </el-collapse-item>
        </el-collapse>
        <!--添加实验弹出层-->
        <el-dialog :title="chapterTitle" :visible.sync="addVisible" width="40%">
          <el-form :model="form"
                   :rules="addRules" ref="form" @submit.native.prevent>
            <el-form-item label="章节名称" :label-width="formLabelWidth" prop="name">
              <el-input v-model="form.name" auto-complete="off"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="addVisible = false">取 消</el-button>
            <el-button type="primary" @click="saveModule('form')">保存</el-button>
          </div>
        </el-dialog>
        <!--更新实验-->
        <el-dialog title="编辑实验" id="updateDialog"
                   :visible.sync="updateVisible" style="margin-top: -5vh"
                   @close="closeUpdateDialog"
                   width="80%" class="experimentBianJi">
          <!--表单-->
          <el-form :inline="true"
                   :model="bianjiForm" id="bianjiForm"
                   class="demo-form-inline" style="margin-top: -20px;"
                   :rules="rules" ref="bianjiForm">
            <el-form-item label="实验名称" prop="experimentName">
              <el-input v-model="bianjiForm.experimentName" placeholder="实验名称"></el-input>
            </el-form-item>
            <el-form-item label="实验描述" prop="experimentDes">
              <el-input type="textarea"  :rows="1" style="height: 40px;" v-model="bianjiForm.experimentDes" placeholder="实验描述"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button class="button-green" @click="sureUpdate('bianjiForm')">保存</el-button>
            </el-form-item>
          </el-form>
          <div id="experimentMark" >
            <el-tabs type="border-card" v-model="activeName">
              <el-tab-pane label="上传markdown文件" name="uploadMark">
                <el-upload
                  class="upload-demo"
                  ref="upload"
                  :action="actionUrl"
                  :auto-upload="false"
                  :headers="uploadHeaders"
                  :on-success="uploadSuccess"
                  accept=".md"
                  :before-upload="beforeUploadHandler"
                  :limit="1"
                  :on-exceed="handleExceed"
                  v-loading="loading">
                  <el-button size="small" slot="trigger" type="primary">选择文件</el-button>
                  <el-button size="small"  type="success" @click="submitUpload()">点击上传</el-button>
                  <div slot="tip" class="el-upload__tip">只能上传markdown文件</div>
                </el-upload>
              </el-tab-pane>
              <el-tab-pane label="在线编辑markdown" name="editMark" v-if="isShowEditor">
                <mavon-editor
                  v-model="bianjiForm.experimentContent"
                  :ishljs = "true"
                  :scrollStyle="true"
                  @imgAdd="imgAddHandler"
                  ref="md"
                  :value="bianjiForm.experimentContent"
                  :toolbars="toobars"
                  :imageFilter="imgFilter"
                  :imgClick="imgClick"/>
              </el-tab-pane>
            </el-tabs>
          </div>

        </el-dialog>

        <!--删除弹出层-->

        <el-dialog id="dialog"
                   :visible.sync="selectSpaceDialog"
                   width="410px">
          <div>
            <div style="margin-bottom: 20px;text-align: center">根据自己情况酌情选择</div>
            <div class="title-left" style="width: 56%;margin: 0px auto;">
              <el-button size="small" @click="intoEx(1)">看老师demo</el-button>
              <el-button size="small" @click="intoEx(2)">自己做练习</el-button>
            </div>
            <!--<div class="title-right">-->
            <!--<p><a @click="intoEx(2)">自己做练习</a></p>-->
            <!--</div>-->
          </div>
        </el-dialog>

        <!--版权-->
        <div class="copyright">
          <p>版权信息  版权股所有   悦岚数据服务</p>
        </div>
      </div>
    </div>
</template>

<script type="text/ecmascript-6">
  import Vue from 'vue'
  import mavonEditor from 'mavon-editor'
  import 'mavon-editor/dist/css/index.css'
  import axios from 'axios'
  import $ from 'jquery'
  Vue.use(mavonEditor)
  import {getCourseDetail,getCourseExperimentDetail,getExpirementInfo,deleteExCloudware,getModuleInfo,updateModule,adjustModuleOrder,adjustExperimentOrder,getLastExperiment,getWebSocketUrl,createExCloudWare,addModule,deleteModule,deleteExperimentById,updateExperiment,edu_url,findModuleIsExist} from '@/common/js/request';
  import index from "../../router";
  export default {
    data(){
      // 实验名称校验
      var validateExperimentName = (rule, value, callback) => {
        if ( typeof value === 'undefined' || value === '') {
          callback(new Error('请输入实验名称'));
        }else if(value.trim().length >40){
          callback(new Error('实验名称长度不能超过40'));
        }else if((/^\s+/).test(value)){
          callback(new Error('开头不能输入空格'));
        }else if((/\s+$/).test(value)){
          callback(new Error('结尾不能输入空格'));
        } else {
          callback();
        }
      };
      // 章节名称校验
      var validateName = (rule, value, callback) => {
        if ( typeof value === 'undefined' ||  value === '') {
          callback(new Error('请输入章节名称'));
        }else if(value.trim().length >45){
          callback(new Error('章节名称长度不能超过45'));
        }else if((/^\s+/).test(value)){
          callback(new Error('开头不能输入空格'));
        }else if((/\s+$/).test(value)){
          callback(new Error('结尾不能输入空格'));
        } else {
          callback();
         //  var courseId = this.courseId;
         // // console.log(courseId)
         //  findModuleIsExist(courseId,value).then(res=>{
         //    //console.log(res);
         //    if(res.data.errorCode==0){
         //      if(res.data.data){
         //        callback(new Error("该章节已经存在"));
         //      } else {
         //        callback();
         //      }
         //    }
         //  });
          //findModuleIsExist()

          //callback();
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
      return {
        tableData:[],
        //i:0,
        addVisible:false,
        form:{name:''},
        courseId:null,
        formLabelWidth:'80px',
        dialogData:{},
        type:1,
        courseName:null,
        loading:false,
        dialogVisible:false,
        updateVisible:false,
        bianjiForm:{},
        actionUrl:null,
        photoVisible:false,//上传图片资源
        uploadHeaders: {
          'Authorization': 'Bearer ' + (localStorage['token'] ? localStorage['token'] : '')
        },
        selectSpaceDialog:false,
        fileList2: [],
        index:-1,
        // 校验
        rules: {
          experimentName: [
            // { required: true, message: '请输入实验名称', trigger: 'blur' },
            // { min: 1, max: 40, message: '长度在 1 到 40 个字符', trigger: 'blur' }
            { required: true,validator: validateExperimentName, trigger: 'blur' }
          ],
          experimentDes: [
            { required: true,validator: validateExperimentDes, trigger: 'blur' }
            // { required: true, message: '请输入实验描述', trigger: 'blur' },
            // { min: 1, max: 40, message: '长度在 1 到 40 个字符', trigger: 'blur' }
          ]
        },
        // 章节
        addRules: {
          name: [
            // { required: true, message: '请输入章节名称', trigger: 'blur' },
            // { min: 1, max: 40, message: '长度在 1 到 40 个字符', trigger: 'blur' }
            { required: true,validator: validateName, trigger: 'blur' }
          ],
        },
        toobars:{
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
        },
        alreadyAdd:false,
        showIntoEx:true,
        activeName:'uploadMark',
        chapterTitle:'',
        editModuleId:"",
        isActiveA:true,
        isActiveB:true,
        orderOne:"",//章节1的orderId
        moduleIdOne:"",//章节1的id
        moduleIdTwo:"",//章节2的id
        orderTwo:"",//章节2的orderId
        courseId:"",//课程id
        activeOrderA:"",
        activeOrderB:"",
        activeExmentOrderA:"",
        isShowEditor: false
      }
    },
    methods:{

      closeUpdateDialog: function(){
        console.log("closeUpdateDialog");
        this.isShowEditor = false;
      },
      cancelHand:function(){
        this.index= -1
      },

      // 进入数据管理页面
      data(courseId,courseName){
        this.getExperimentData();
        this.$router.push({ path :'/htSj/', query : { courseId , courseName}});
      },
      // 进入编辑答案页面
      test(courseId,courseName,experimentName,moduleName,cloudwareType,id){
        this.getExperimentData();
        // console.log("编辑答案开始")
        // console.log("课程名称:"+courseName)
        // console.log("章节名称:"+moduleName)
        // console.log("实验名称:"+experimentName)
        // console.log("实验id:"+id)
        this.$router.push({ path :'/exTest/', query : { courseId , courseName,experimentName,moduleName,cloudwareType,id}});
      },
      // 点击添加实验，进入其页面
      Addexperiment(moduleId){
        this.$router.push({ path :'/Addexperiment/'+moduleId });
      },
      uploadSuccess:function(response){
        this.loading = false;
        if(response.errorCode==0){
          this.$refs.upload.clearFiles();
          // console.log(response.data);
          this.bianjiForm.experimentContent = response.data;
          this.$refs.md.value = this.bianjiForm.experimentContent;
        }
      },
      imgAddHandler:function(filename,imgFile){
        // console.log("--------上传图片---------");
        //只允许上传.jpg,.jpeg,.bmp,.png
        if(imgFile.type != 'image/jpeg'&& imgFile.type != 'image/png'){
          this.$message({
            type:'warning',
            message:"只能上传jpg,png格式的图片"
          });
          return;
        }
        let param = new FormData();
        param.append('file', imgFile);
        param.append('type', '2');
        param.append('courseId', this.courseId);
        let config = {
          headers: {'Content-Type': 'multipart/form-data'},
        };
        // 添加请求头
        axios.post('/admin/course/experiment/piclib', param, config).then(res => {
          // console.log(res);
          if(res.status==200 && res.data.errorCode==0){
            //文件上传成功
            var path = res.data.data.url;

            var url = "/previewImage?path="+path;
            this.$refs.md.$img2Url(filename, url);
          }
        });
      },
      imgFilter:function(imgFile){
        if(imgFile.type != 'image/jpeg' && imgFile.type != 'image/png'){
          this.$message({
            type:'warning',
            message:"只能上传jpg,png格式的图片"
          });
          var inputEle = $("div[class='dropdown-item'] input[type='file']")[0];
          $(inputEle).attr("title"," ");
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
        // console.log(width+":"+height);
        // console.log(screenWidth+":"+screenHeight);
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
      submitUpload:function(){
        // console.log("-------上传-------");
        //上传
        this.$refs.upload.submit();
      },
      handleRemove(file, fileList) {
        // console.log(file, fileList);
      },
      handlePreview(file) {
        // console.log(file);
      },
      // beforeRemove(file, fileList) {
      //   console.log("-------------确定移除---------")
      //   return this.$confirm(`确定移除 ${ file.name }？`);
      // },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      beforeUploadHandler:function(file){
        // console.log("fiel文件:"+file);
        //判断是否是markdown文件
        if(file.name.lastIndexOf('.md')>-1){
          this.loading = true;
          return true;
        } else {
          // console.log("-------------只允许上传markdown文件---------")
          this.$message({
            title:"提示",
            message:"只允许上传markdown文件",
            type:"error"
          });
          this.loading = false;
          return false;
        }
      },
      deleteExById:function(row){
        this.$confirm('此操作将永久删除该实验, 是否继续?','提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          deleteExperimentById(row.id,this.courseId).then(res=>{
            if(res.data.errorCode==0){
              this.$message({
                title:"提示",
                message:"删除成功",
                type:"success"
              });
              this.getExperimentData();
            }else{
              this.$message({
                              type: 'error',
                              message: res.data.message
                             });
            }
          });
        }).catch(()=>{
          // this.$message({
          //   type: 'info',
          //   message: '已取消删除'
          // });
        });

      },
      handleRemove(file, fileList) {
        // console.log(file, fileList);
      },
      handlePreview(file) {
        // console.log(file);
      },
      // 删除弹出层
      deleteDialog(moduleId){
        this.$confirm('此操作将永久删除该章节, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteModule(moduleId,this.courseId).then(res=>{
            if(res.data.errorCode==0){
              this.getExperimentData();
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
            }else{
               this.$message({
                  type: 'error',
                  message: res.data.message
               });

              if(res.data.errorCode == 2){
                this.$router.push({ path :'/kecheng'});
              }

            }
          });
        }).catch(() => {
          // this.$message({
          //   type: 'info',
          //   message: '已取消删除'
          // });
        });
      },
      adddModuleDialog(form){
        //添加章节
        this.chapterTitle="添加章节"
        // this.form = {};
        this.form.name=""
        this.addVisible = true;
        this.alreadyAdd = false;
        if(this.$refs[form] !=undefined){
          this.$refs[form].clearValidate();
        }

      },
      // 编辑章节
      editDialog(moduleId,moduleName){
        this.chapterTitle="编辑章节"
        this.addVisible = true;
        if (this.$refs.form!==undefined) {
          this.$refs.form.clearValidate();
        }
        getModuleInfo(moduleId).then(res=>{
          // console.log("编辑章节开始")
          // console.log(res.data.data.name)
          // console.log("编辑章节结束")
          // console.log(res.data.data)
          this.form.name=res.data.data.name
        })
        this.editModuleId=moduleId
        // console.log("moduleid为:"+this.editModuleId)
        // console.log("章节id为:"+this.courseId)
        // this.form.name=moduleName
      },
      saveModule:function(form){
        this.$refs[form].validate((valid) => {
          if (valid) {
            // alert('submit!');
            //this.$message.success("提交成功")
            //保存章节
            if(this.chapterTitle=="添加章节"){
              if(!this.alreadyAdd) {
                this.form.courseId = this.courseId;
                addModule(this.form.name, this.form.courseId).then(res => {
                  this.alreadyAdd = true;
                  if (res.data.errorCode === 0) {
                    this.addVisible = false;
                    this.$message({
                      type: 'success',
                      message: '添加成功'
                    });
                    this.getExperimentData();
                  } else {
                    this.$message({
                      type: 'error',
                      message: res.data.message
                    });
                    this.addVisible = false;
                    //如果该课程已经被删除，则退出该课程维护页面
                    if (res.data.errorCode == 2) {
                      this.$router.push({path: '/kecheng'});
                    }
                  }
                });
              }
            }
            else{
              //课程章节id
              //课程章节课程id：this.editModuleId

              // this.form.courseId = this.courseId;
              // console.log("课程章节id为:"+ this.form.courseId)
              updateModule(this.editModuleId,this.courseId,this.form.name).then(res => {
                // this.addVisible = false;
                // this.getExperimentData();
                if (res.data.errorCode === 0) {
                  this.addVisible = false;
                  this.$message({
                    type: 'success',
                    message: res.data.message
                  });
                  this.getExperimentData();
                }else {
                  this.$message({
                    type: 'error',
                    message: res.data.message
                  });
                  this.addVisible = false;
                  //如果该课程已经被删除，则退出该课程维护页面
                  if (res.data.errorCode == 2) {
                    this.$router.push({path: '/kecheng'});
                  }
                }
                // console.log(res)
              })
            }

          } else {
            // console.log('error submit!!');
            return false;
          }
        });

      },
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      },
      getEx:function(item,scope){
        //进入实验
        //获取实现相关数据(markdown信息)
        var selectData = item.moduleContent[scope.$index];

        this.dialogData = selectData;
        //console.log(this.dialogData);
        //调用客户端
        var path = window.location.href;
        var currentPath = this.$route.fullPath;
        var domain = path.substring(0,path.indexOf(currentPath));
        var nextPath = domain+"/watchExperiment/"+selectData.id;
        if(this.dialogData.cloudwareTypeId == 7){
            //数猎云相关课程
            try {
              callbackobjectforjs.opendatashirecoursecallback(nextPath, this.dialogData.courseName, true);
            } catch (err){}
        } else {
          try {
            callbackobjectforjs.opennormalcoursecallback(nextPath,this.dialogData.courseName);
          } catch (err){}
          //this.$router.push('/watchExperiment/'+this.dialogData.id);
        }
      },
      sureUpdate:function(bianjiForm){
        this.$refs[bianjiForm].validate((valid) => {
          if (valid) {
            // alert('submit!');

            var updateForm = {};
            Object.assign(updateForm,this.bianjiForm);
            updateForm.cloudwareType = updateForm.cloudwareTypeId;
            updateExperiment(updateForm).then(res=>{
              if(res.data.errorCode==0){
                this.$message.success("提交成功");
                this.updateVisible = false;
                this.getExperimentData();
              }else{
                this.$message({
                              type: 'error',
                              message: res.data.message
                });
                this.updateVisible = false;
                //this.$router.history.go(-1);
              }
            })
          } else {
            // console.log('error submit!!');
            return false;
          }
        });
      },
      updateExDialog:function(item,scope){
        var selectData = item.moduleContent[scope.$index];
        Object.assign(this.bianjiForm,selectData);
        //去除添加图片链接
        this.updateVisible = true;
        this.isShowEditor = true;
        this.activeName='editMark';
        this.$nextTick(()=>{
          try {
            //this.$refs.md.$children[0].$mouseenter_img_dropdown = (() => {
              var imgElm = document.getElementsByClassName("op-image")[0];
              console.log(imgElm);
              if (imgElm.getElementsByClassName("dropdown-item").length > 1) {
                console.log("有图片");
                var childElm = imgElm.getElementsByClassName("dropdown-item")[0];
                imgElm.removeChild(childElm);
              }
              //this.$refs.md.$children[0].s_img_dropdown_open = true;
            //});
          } catch (e){}
        });
        try {
          console.log("清除记录");
          this.$refs.upload.clearFiles();
          this.$refs.md.d_history = new Array();
        } catch (e){}

      },
      deleteEx:function(lastExperimentId){
        //删除实验
        var userId = JSON.parse(localStorage["user"]).userId;
        deleteExCloudware(userId,lastExperimentId).then(response=>{
          if(response.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          if (response.errorCode == 0) {
            console.log("删除实验成功");
            // $scope.hideExText = false;
          } else {
            console.log("删除实验失败:"+response.message)
          }
        });
      },
      getExperimentData:function(){
        //获取实验数据
        this.index = -1;
        this.i = 0;
        this.tableData=[];
        this.actionUrl = edu_url + '/admin/course/experiment/markdownWithResolve';
        getCourseExperimentDetail(this.courseId).then(res=>{
          if(res.data=='alreadyResetPassword'){
            this.$message({
              type:"warning",
              message:"请重新登录"
            });
            return;
          }
          if(res.status==200){
            var data = res.data;
            // console.log(data);
            if(data.errorCode==0){
              //操作成功
              this.courseName = data.data.courseName;
              this.courseId=data.data.courseId;
              this.tableData = data.data.moduleList;
              this.tableData.map(item=>{
                var user = JSON.parse(localStorage["user"]);
                item.moduleContent.map(item2=>{
                  item2.ExpOrderCN=this.numberChinese(item2.orderId);
                  if(user.role==3 && item2.cloudwareTypeId !=7){
                    item2.showEx = false;
                  } else {
                    item2.showEx = true;
                  }
                });
              });
              //章节图标位置摆放
              this.tableData.forEach(((value,index) => {
                if(index==0){
                    this.activeOrderA=value.orderId;
                }
                if(index+1==this.tableData.length){
                  this.activeOrderB=value.orderId
                }
                var orderCN=this.numberChinese(value.orderId);
                value.orderIdCN=orderCN;
              }))

              //实验图标位置摆放
              this.tableData.forEach(((value,indext) => {

                this.tableData[indext].moduleContent.forEach((exvalue,exindex)=>{
                  if(exindex==0){
                    this.activeExmentOrderA=exvalue.orderId
                  }
                })
              }))




            }
          }
        });
      },
      addkshiyan(moduleId){
        //this.$router.push({ path :'/Addexperiment/' });
       // this.$router.push({ path :'/Addexperiment/'+moduleId,query:{moduleName} });
        this.$router.push({ path :'/Addexperiment/'+this.courseId +'/'+moduleId });
      },
      renderHeader(h,{column,$index}) {
        let index = this.i;
        //var moduleId = this.tableData[index].moduleId;
        var moduleId = this.tableData[index].moduleId;
        // var moduleName=this.tableData[index].moduleName;
        //console.log(moduleId)
        this.i +=1;
       // console.log(moduleId.moduleId)
        //console.log(column,$index)
        var add = () => {
          this.addkshiyan(moduleId)
        }
        return (
          <div>
          <el-button size="small" id="addText"  class="button-green" onClick={add}>添加实验</el-button>
          </div>
      )
      },
      handleChange(idx){
        try{
        //this.getExperimentData()
          var v = this.$refs[idx][0].innerHTML;
          if(v=="展开"){
            this.$refs[idx][0].innerHTML = "收起";
          } else {
            this.$refs[idx][0].innerHTML = "展开";
          }
        }catch (e){

        }
      },
      // 章节向上排序
      topSort(moduleId,orderId,){
        var moduleIdTwo=""
        var orderTwo=""
        this.tableData.forEach((value,index)=>{
          if(value.orderId==orderId && value.moduleId==moduleId){
            //上一章的序号
            orderTwo=this.tableData[index-1].orderId
            // 上一章的id
            moduleIdTwo=this.tableData[index-1].moduleId
          }
        })
        // console.log("章节排序开始")
        // console.log("序号为:"+orderId)
        // console.log("模块id为:"+moduleId)
        // console.log("模块id2为:"+moduleIdTwo)
        // console.log("课程id为:"+this.courseId)
        // console.log("章节排序结束")
        adjustModuleOrder(orderId,moduleId,moduleIdTwo,orderTwo,this.courseId).then(res=>{
          if(res.status==200){
            if(res.data.errorCode==0){
              this.$message({
                type: 'success',
                message: res.data.message
              });
              this.getExperimentData()
              // var sp1=this.tableData[index]
              // var sp2=this.tableData[index-1];
              // var sd=[];
              // for(var r=0;r<this.tableData[index].length;r++){
              //   if(r==index){
              //     sp2.orderId=r+1;
              //     sd.push(sp2);
              //   }else if(r==index-1){
              //     sp1.orderId=r+1;
              //     sd.push(sp1);
              //   }else {
              //     sd.push(this.tableData[r]);
              //   }
              // }
              // this.tableData[index]=sd;
            }else{
              this.$message({
                type: 'warning',
                message: res.data.message
              });
            }
          }
        })
      },
      // 章节向下排序
      downSort(moduleId,orderId){
        var moduleIdTwo=""
        var orderTwo=""
        this.tableData.forEach((value,index)=>{
          if(value.orderId==orderId && value.moduleId==moduleId){
            //下一章的序号
            orderTwo=this.tableData[index+1].orderId
            // 下一章的id
            moduleIdTwo=this.tableData[index+1].moduleId
          }
        })
        adjustModuleOrder(orderId,moduleId,moduleIdTwo,orderTwo,this.courseId).then(res=>{
          if(res.status==200){
            if(res.data.errorCode==0){
              this.$message({
                type: 'success',
                message: res.data.message
              });
              this.getExperimentData()
            }else{
              this.$message({
                type: 'warning',
                message: res.data.message
              });
            }
          }
        })
      },
      // 课程向上排序
      expermentTopSort(id,orderId,courseId){
        // console.log("id为:"+id)
        this.tableData.forEach((value,index)=>{
          this.tableData[index].moduleContent.forEach((exvalue,exindex)=>{
            //实验二id,实验二序号
            var experimentTwo=""
            var orderTwo=""
            if(id==exvalue.id && orderId==exvalue.orderId ){
              experimentTwo=this.tableData[index].moduleContent[exindex-1].id
              // console.log("实验开始，moduleId为:")
              // console.log(this.tableData[index].moduleContent)
              // console.log("实验结束")
              var moduleId=this.tableData[index].moduleId
              orderTwo=this.tableData[index].moduleContent[exindex-1].orderId
              adjustExperimentOrder(id,orderId,experimentTwo,orderTwo,moduleId).then(res=>{
                if(res.status==200){
                  if(res.data.errorCode==0){
                    this.$message({
                      type: 'success',
                      message: res.data.message
                    });
                    // this.getExperimentData()
                    // var tem=this.tableData[index].moduleContent[exindex]
                    // debugger
                    var sp1=this.tableData[index].moduleContent[exindex]
                    var sp2=this.tableData[index].moduleContent[exindex-1];
                    var sd=[];
                    for(var r=0;r<this.tableData[index].moduleContent.length;r++){
                      if(r==exindex){
                        sp2.orderId=r+1;
                        sd.push(sp2);
                      }else if(r==exindex-1){
                        sp1.orderId=r+1;
                        sd.push(sp1);
                      }else {
                        sd.push(this.tableData[index].moduleContent[r]);
                      }
                    }
                    this.tableData[index].moduleContent=sd;
                    // this.tableData[index].moduleContent[exindex-1]=this.tableData[index].moduleContent[exindex];
                    // this.tableData[index].moduleContent[exindex]=sd;

                  }else{
                    this.$message({
                      type: 'warning',
                      message: res.data.message
                    });
                  }
                }
              })
            }
          })
        })
      },
      //课程向下排序
      expermentDownSort(id,orderId,courseId){
        this.tableData.forEach((value,index)=>{
          this.tableData[index].moduleContent.forEach((exvalue,exindex)=>{
            // console.log("实验开始")
            // console.log(this.tableData[index])
            // console.log("实验结束")
            //实验二id,实验二序号
            var experimentTwo=""
            var orderTwo=""
            if(id==exvalue.id && orderId==exvalue.orderId ){
              experimentTwo=this.tableData[index].moduleContent[exindex+1].id
              var moduleId=this.tableData[index].moduleId
              orderTwo=this.tableData[index].moduleContent[exindex+1].orderId
              adjustExperimentOrder(id,orderId,experimentTwo,orderTwo,moduleId).then(res=>{
                if(res.status==200){
                  if(res.data.errorCode==0){
                    this.$message({
                      type: 'success',
                      message: res.data.message
                    });
                    // this.getExperimentData()
                    debugger
                    var sp1=this.tableData[index].moduleContent[exindex+1];
                    var sp2=this.tableData[index].moduleContent[exindex];
                    var sd=[];
                    for(var r=0;r<this.tableData[index].moduleContent.length;r++){
                      if(r==exindex){
                        sp1.orderId=r+1;
                        sd.push(sp1);

                      }else if(r==exindex+1){
                        sp2.orderId=r+1;
                        sd.push(sp2);
                      }else {
                        sd.push(this.tableData[index].moduleContent[r]);
                      }
                    }
                    this.tableData[index].moduleContent=sd;
                  }else{
                    this.$message({
                      type: 'warning',
                      message: res.data.message
                    });
                  }
                }
              })
            }
          })
        })
      },
       numberChinese:function (number) {
        var units = '个十百千万@#%亿^&~', chars = '零一二三四五六七八九';
        var a = (number + '').split(''), s = [];
        if (a.length > 12) {
          throw new Error('too big');
        } else {
          for (var i = 0, j = a.length - 1; i <= j; i++) {
            if (j == 1 || j == 5 || j == 9) {//两位数 处理特殊的 1*
              if (i == 0) {
                if (a[i] != '1') s.push(chars.charAt(a[i]));
              } else {
                s.push(chars.charAt(a[i]));
              }
            } else {
              s.push(chars.charAt(a[i]));
            }
            if (i != j) {
              s.push(units.charAt(j - i));
            }
          }
        }
        return s.join('').replace(/零([十百千万亿@#%^&~])/g, function (m, d, b) {//优先处理 零百 零千 等
          b = units.indexOf(d);
          if (b != -1) {
            if (d == '亿') return d;
            if (d == '万') return d;
            if (a[j - b] == '0') return '零'
          }
          return '';
        }).replace(/零+/g, '零').replace(/零([万亿])/g, function (m, b) {// 零百 零千处理后 可能出现 零零相连的 再处理结尾为零的
          return b;
        }).replace(/亿[万千百]/g, '亿').replace(/[零]$/, '').replace(/[@#%^&~]/g, function (m) {
          return { '@': '十', '#': '百', '%': '千', '^': '十', '&': '百', '~': '千' }[m];
        }).replace(/([亿万])([一-九])/g, function (m, d, b, c) {
          c = units.indexOf(d);
          if (c != -1) {
            if (a[j - c] == '0') return d + '零' + b
          }
          return m;
        });
      }
    },
    created: function () {
      var courseId = this.$route.params.courseId;
      this.courseId = courseId;
      this.actionUrl = edu_url + '/admin/course/experiment/markdownWithResolve';
      this.getExperimentData();
    },
    activated: function () {
      var courseId = this.$route.params.courseId;
      this.courseId = courseId;
      this.actionUrl = edu_url + '/admin/course/experiment/markdownWithResolve';
      this.getExperimentData();
      this.loading = false;
      this.index = -1;
    },
    beforeRouteEnter: (to, from, next) => {
      next(vm => {
        if (from && from.path.indexOf('/Addexperiment') > -1) {
          vm.getExperimentData()
        };
      });
    },
    cellClick:function(){
      alert(123)
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  .experiment
    width 100%
    height auto
    .Title
      width 100%
      font-size 22px
      color #000000
      margin 20px 0px
      h2
        float left
        margin-bottom 20px
      i
        width 15px
        height 15px
        float right
        margin-right 10px
  .table
    display table
    width 100%
    height auto
    .table-row
      display table-row
      width 100%
      height 30px
      line-height 30px
      .table-cell
        display table-cell
        width 20%
        text-align center
        th .cell
          color #000000
          text-align center
  #dialog
    #title
      width 100%
      height 50px
      border-bottom  1px solid #e5e5e5
      display flex
      text-align center
      color #00a2ff
      line-height 30px
      .title-left
        width 165px
        height 30px
        background url("../../../static/images/title-left.png")
        background-size cover
      .title-right
        width 175px
        height 30px
        background url("../../../static/images/title-right.png")
        background-size cover
        margin-left -3px
    .main
      width 100%
      display flex
      .m-left
        flex 1
        border-right 1px solid #e5e5e5
        div
          margin-top 0px
          margin-bottom 0px
          button
            margin-bottom 15px
          p
            font-size 13px
            color #646464
      .m-right
        flex 3
        position relative
        #icon
          position absolute
          width 30%
          left 35%
          bottom 20px
          z-index 999
  .el-dialog
    max-height 90%
    overflow auto
  .det_course
    float right
    margin-right 5px
    color #969696
    font-size 14px
    width 100px
    height 35px
    /*line-height 5px*/
    border-radius 5%
    border-color transparent
    margin-top 7px
    text-align center
    background-color #ffffff
    &:hover
      background-color #ff0000;
      color #ffffff
  .edit_course
    float right
    margin-right 5px
    color #969696
    font-size 14px
    width 100px
    height 35px
    /*line-height 5px*/
    border-radius 5%
    border-color transparent
    margin-top 7px
    text-align center
    background-color #ffffff
    &:hover
      background: linear-gradient(#c9dece 70%, #bbd0c0 30%) !important;
      background-size: cover;
      color: #494b4f;
  /*@media screen and (max-width: 2200px) and (min-width: 1540px)
    #mark
      overflow auto
      height 600px
      .v-note-panel
        height: 550px !important;
        overflow: auto;
        border 1px solid red
      !*border 1px solid red*!
  @media screen and (max-width: 1540px)
    #mark
      overflow auto
      max-height 300px*/


</style>
<style type="text/css" >
  @import "../../common/css/banji.css";
  /*.el-table th > .cell{
    text-align: center;font: 1.1em bolder;
  }*/
  .column-center th > .cell{
    text-align: center;font: 1.1em bolder;
  }
  .el-table th div{
    text-overflow: clip;
  }
  .experiment #colapse-title{
    position: absolute;
    top: 2px;
    right: 240px;
    color: #bebebe;
    font-size: 15px;
  }
  .el-table i{
    color: #000000;
  }
  .el-icon-error{
    color: #E3281A;
  }
  .experiment .el-collapse-item__header{
    color: #494b4f !important;font-size: 18px;
  }
  .experiment .el-collapse-item__arrow{
    display: none;
    /*border: 1px solid red;*/
  }

  /*添加实验*/
  #addText{
    width:186px;padding:13px 0px;background-color: #8ad7ff;border: none;
    transition: background-color 1s;font-size: 15px;
  }
  #addText:hover{
    background-color: #296ff0;
  }
  .testBtn,.detBtn{
    transition: background-color 1s;
  }
  .testBtn .el-radio-button__inner,.detBtn .el-radio-button__inner{
    font-size: 15px !important;
  }
  /*查看更新按钮*/
  .testBtn .el-radio-button__inner:hover{
    background: linear-gradient(#c9dece 70%, #bbd0c0 30%) !important;
    background-size: cover;
    color: #494b4f;
  }
  /*删除按钮*/
  .detBtn .el-radio-button__inner:hover{
    background-color: #ff0000;color: #ffffff;
  }
  .experiment .el-collapse-item__header{
    position: relative;
    width: 100%;
    /*border: 1px solid red;*/
  }
  /*.icon-radius{*/
    /*width: 8px;height: 8px;position: absolute;border-radius: 50%;*/
    /*left: -20px;top: 20px;background-color: #494b4f;*/
  /*}*/
  .el-textarea__inner{
    height: 40px;
    color: #606266 !important;
    font-size: inherit;
    font-family: '微软雅黑';
  }
  /*#bianjiForm .el-form-item{*/
    /*width: 20% !important;*/
  /*}*/
  /*.experiment .el-collapse-item__content{*/
    /*padding-bottom: 0px !important;*/
  /*}*/
  .experiment .el-collapse-item__content{
    padding-bottom: 0px !important;
  }
  .experiment  .sort-caret.descending.active{
    border-top-color: black !important;
  }
  .experiment  .sort-caret.ascending.active{
    border-bottom-color:black !important;
  }
 .experiment .caret-wrapper{
   width: 44px;
   height: 30px;
   /*border: 1px solid red;*/
 }
  .experiment .sort-caret.ascending{
    top: -23px;
    left: 13px;
  }
  .experiment .sort-caret.descending{
    bottom: -23px;
    left: 3px;
  }
  .experiment .sort-caret {
    border: solid 6.5px transparent;
   }
  /*课程名称*/
  #exModuleName{
    /*white-space: pre-wrap;*/
    display: inline-block;
    /*border: 1px solid red;*/
    position: absolute;
    left: 140px;
    right: 300px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: pre;
    word-wrap: normal !important;
    word-break: normal !important;
  }
  /*操作按钮自动换行*/
  .el-table .cell .el-radio-group{
    white-space: normal !important;
  }
  .el-table .cell .el-radio-group{
    border-bottom: 1px solid transparent;
  }
  .el-radio-button__inner{
    border-bottom: 1px solid #dcdfe6;
  }
  .el-table__empty-block{
    border-top: 1px solid #dcdfe6;
  }
</style>
