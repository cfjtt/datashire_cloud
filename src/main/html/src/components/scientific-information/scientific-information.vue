<template>
    <div id="scienInformation">
      <h1 class="title breakWord">
        项目信息管理
      </h1>
      <el-form ref="form" :rules="rules"  :model="form" label-width="80px">
        <el-form-item label="是否公开" prop="isPublic">
          <!--<el-switch v-model="form.delivery">公开</el-switch>-->
          <el-checkbox  v-model="form.isPublic" label="1" >公开</el-checkbox>
        </el-form-item>
        <el-form-item label="项目状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="进行中" value="1"></el-option>
            <el-option label="已完成" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入项目名称,限定45个字符" maxlength=45 auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="项目简介" prop="brefIntro">
          <el-input type="textarea" :autosize="{ minRows: 3}" v-model="form.brefIntro" placeholder="创建文件时输入的项目简介"></el-input>
        </el-form-item>
        <el-form-item label="项目成果">
          <div class="edit_container" style="min-height: 350px;">
            <quill-editor style="height: 300px;"
              ref="myQuillEditor"
              v-model="form.results"
              :options="editorOption"
              class="editer"
              @change="onEditorChange($event)"
              @focus="onEditorFocus($event)"></quill-editor>
            <el-upload
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
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit" style="float: right">保存</el-button>
        </el-form-item>
      </el-form>
    </div>
</template>

<script type="text/ecmascript-6">
  //import vueQuillEditor from '../scientific-ue/scientific-ue.vue'
  import { quillEditor } from 'vue-quill-editor'
  import 'quill/dist/quill.core.css'
  import 'quill/dist/quill.snow.css'
  import 'quill/dist/quill.bubble.css'
  import axios from 'axios'
  import {getProjectById,updateProject,checkProjectNameIsExist} from '@/common/js/request';
  export default {
        data() {
          var checkNameExist=(rule,value,callback) =>{
            if(!value){
              return callback(new Error("名字不能为空"))
            }
            if(value.indexOf("[")>-1 || value.indexOf("]") >-1){
              return callback(new Error("项目名称不能包含[]"));
            }
            if(value == this.originName){
              callback();
            } else {
              //校验项目是否存在
              checkProjectNameIsExist(value).then(res => {
                if (res.status == 200) {
                  if (res.data.errorCode == 0) {
                    callback();
                  } else {
                    callback(new Error("该项目已经存在"));
                  }
                }
              });
            }
          };
          return {
            form:{
              results:'请输入项目名称'
            },
            rules: {
              name: [
                {required: true, message: '请输入项目名称', trigger: 'blur'},
                { validator: checkNameExist, messahe:'该项目已经存在',trigger: 'blur' }
              ],
              brefIntro:[
                {max:600,message:"长度超过600个字符",trigger: 'blur'}
                ]
            },
            projectId:0,
            editorOption:{
              placeholder:'请输入项目成果',
              modules:{
                toolbar: {
                  container: [
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
                  ],
                  handlers: {
                    'image': function (value) {
                      if (value) {
                        console.log("点击图片");1
                        document.querySelector('.upload-demo1 input').click()
                      } else {
                        this.quill.format('image', false);
                      }
                    }
                  }
                }
              }
            },
            originName:null,
            actionImageUrl:'/doc/uploadProjectImage',
            uploadHeaders: {
              'Authorization': 'Bearer ' + (localStorage['token'] ? localStorage['token'] : '')
            },
            imageData:{}
          }
        },
        computed: {
          editor() {
            return this.$refs.myQuillEditor.quill
          }
        },
        created(){
          this.$nextTick(function(){
            this.$refs.myQuillEditor.quill.enable(true);
            this.$refs.myQuillEditor.quill.focus();
          })
        },
        methods: {
          onSubmit() {
            //提交
            this.$refs.form.validate((valid) => {
              if (valid) {
                var copyForm = JSON.parse(JSON.stringify(this.form));
                copyForm.isPublic = copyForm.isPublic ? 1 : 0;
                this.saveProject(copyForm)
              } else {
                console.log('error submit!!');
                return false;
              }
            });
          },
          onEditorChange({editor,html,text}){
            this.form.results = html;
          },
          onEditorFocus(event){},
          customUploadImage:function (item) {
            console.log("图片上传前");
            var file = item.file;
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
            var user = JSON.parse(localStorage['user']);
            let param = new FormData();
            // 添加请求头
            param.append('projectId', this.projectId);
            param.append('userId', user.userId);
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
            //this.quillUpdateImg = false
          },
          uploadError(res, file) {
            console.log("图片上传失败");
          },
          _getProject(){
            getProjectById(this.projectId).then(res=>{
              console.log("-------项目信息管理------");
              if(res.data.errorCode==0){
                if(res.data.data.isPublic==1){
                  res.data.data.isPublic = true;
                } else {
                  res.data.data.isPublic = false;
                }
                res.data.data.status+='';
                this.form = res.data.data;
                this.originName = res.data.data.name;
                if(this.form.brefIntro=="null"){
                  this.form.brefIntro=""
                }
              }
            })
          },
          saveProject(project){
            updateProject(project).then(res=>{
              console.log("-----更新信息-----");
              console.log(res);
              if(res.status==200){
                if(res.data.errorCode==0){
                  this.$message({
                    type:"success",
                    message:"保存成功"
                  });
                  //重新加载
                  //跳转到项目主页
                  this.$router.push({ path :'/scientificMain', query:{projectId: this.projectId}});
                  //this._getProject();
                } else if(res.data.errorCode==115){
                  this.$message({
                    type:"error",
                    message:"该项目不存在"
                  })
                } else {
                  this.$message({
                    type:"error",
                    message:"系统异常，请稍后重试"
                  })
                }
              }
            })
          }
        },
        components: {
          quillEditor
        },
        beforeRouteEnter: (to, from, next) => {
          next(vm => {
            vm.projectId = vm.$route.query.projectId;
            if(vm.$refs.form !==undefined){
              vm.$refs.form.clearValidate()
            }
            vm._getProject()
          });
        }
    }
</script>

<style lang="stylus" scoped type="text/stylus">
  #scienInformation
    .title
      font-size 18px
      line-height 30px
      margin-bottom 10px
    /*.ql-toolbar.ql-snow*/
      /*line-height 24px*/
</style>
<style type="text/css">
  #scienInformation .ql-toolbar.ql-snow{
    line-height: 24px
  }
</style>
