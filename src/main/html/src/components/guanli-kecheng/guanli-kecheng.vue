<template>
  <div class="xueqi">
    <h1>课程管理</h1>
    <el-form :inline="true"  id="btn-header" class="demo-form-inline" @submit.native.prevent>
      <el-form-item >
        <!--<el-button class="button-green" @click="_getListData()">刷新</el-button>-->
        <el-button type="primary" @click="_Refresh()">刷新</el-button>
        <el-button class="button-green" @click="addCourse">新增课程</el-button>
        <el-button class="button-green" @click="kechengMain">课程章节维护</el-button>
        <el-button class="button-green" @click="updateCourse">编辑课程</el-button>
        <el-button class="button-green" @click="deleteCourse">删除课程</el-button>
        <!--<el-button type="primary" @click="exportCourse">导出课程</el-button>
        <el-button type="primary" @click="importCourse">导入课程</el-button>-->
      </el-form-item>
      <!--<el-form-item label="搜索" style="float: right;margin-right: 0px;">
        <el-input v-model="searchQuery"  @keyup.enter.native="select" placeholder="搜索"></el-input>
      </el-form-item>-->
      <el-form-item style="margin-right: 0px">
        <el-button type="primary" @click="_getListData()">搜索</el-button>
      </el-form-item>
      <el-form-item >
        <el-input v-model="searchQuery" placeholder="搜索"
                  @keyup.native.enter="select()"></el-input>
      </el-form-item>
    </el-form>
    <!--表格-->
    <div id="Table">
      <el-table
        ref="multipleTable"
        tooltip-effect="dark"
        :border="true"
        center
        :data="listData"
        @selection-change="handleSelectionChange"
        style="width: 100%;text-align: center">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          prop="id"
          label="课程编号"
          center
          width="125" :resizable="false">
          <template slot-scope="alldata">
            <p >{{ alldata.row.id }}</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="courseName"
          label="课程名称"
          :resizable="false"
          show-overflow-tooltip>
          <template slot-scope="alldata">
            <p >{{ alldata.row.courseName }}</p>
          </template>
        </el-table-column>

        <el-table-column
          prop="courseDes"
          label="课程描述"
          :resizable="false"
          show-overflow-tooltip>
          <template slot-scope="alldata">
            <p >{{ alldata.row.courseDes }}</p>
          </template>
        </el-table-column>

        <el-table-column
          prop="author"
          label="课程作者"
          :resizable="false"
          show-overflow-tooltip>
          <template slot-scope="alldata">
            <p >{{ alldata.row.author }}</p>
          </template>
        </el-table-column>

        <el-table-column
          prop="phone"
          label="联系方式"
          :resizable="false"
          show-overflow-tooltip>
          <template slot-scope="alldata">
            <p >{{ alldata.row.phone }}</p>
          </template>
        </el-table-column>

        <!--<el-table-column width="" label="操作" min-width="140" :resizable="false">-->
          <!--<template slot-scope="scope">-->
            <!--<el-button size="mini" @click="courseMaintenance(scope.$index)">课程实验维护</el-button>-->
            <!--<el-button size="mini"  @click="updateKecheng(scope.$index)">编辑课程</el-button>-->
            <!--<el-button-->
              <!--size="mini"-->
              <!--type="danger"-->
              <!--@click="deletedata(scope.$index, scope.row)">删除课程-->
            <!--</el-button>-->
          <!--</template>-->
        <!--</el-table-column>-->
      </el-table>
    </div>
    <div style="clear: both"></div>
    <div style="max-width:100%;overflow: auto">
      <div id="pagination" style="width:35%;margin:20px 20%">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="p_currentPage"
          :page-sizes="p_pageSizes"
          :page-size="p_pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="p_total">
        </el-pagination>
      </div>
    </div>
    <div class="copyright" style="margin-right: 3%">
      <p>版权信息  版权股所有   悦岚数据服务</p>
    </div>
    <!--新增/修改-->
    <el-dialog :title="title" id="editDialog"
               :visible.sync="dialogFormVisible"
               style="margin-top: -8vh"
               width="45%" v-if="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label=" 课程图片" :label-width="formLabelWidth">
          <!--<el-input v-model="form.school" placeholder="教师编号" auto-complete="off"></el-input>-->
          <el-upload
            class="upload-demo"
            :action="actionUrl"
            :limit="1"
            :auto-upload="false"
            ref="upload"
            :headers="uploadHeaders"
            :on-success="uploadSuccess"
            :before-upload="beforeUploadHandler"
            accept=".jpg,.png"
            :data="para"
            :on-exceed="handleExceed"
           >
            <el-button size="small" type="primary" slot="trigger">选择文件</el-button>
            <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传</el-button>
            <div slot="tip" class="el-upload__tip">图片支持格式：jpg／png，建议尺寸：266*212，小于或等于200kb</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="课程名称" :label-width="formLabelWidth" prop="name">
          <!--<el-input v-model="form.date" placeholder="教师姓名" auto-complete="off"></el-input>-->
          <el-input v-model="form.name" placeholder="课程名称" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="课程描述" :label-width="formLabelWidth" prop="description" >
          <el-input type="textarea"  v-model="form.description" placeholder="课程描述"  ></el-input>
        </el-form-item>
        <el-form-item label="课程作者" :label-width="formLabelWidth" prop="author">
          <!--<el-select v-model="form.teacherId" placeholder="请选择主讲教师" style="width: 100%">
            <el-option v-for="(item, index) in TeacherData"
                       :label="item.teacherName"
                       :key="item.id"
                       :value="item.id" >
            </el-option>
          </el-select>-->
          <el-input   v-model="form.author" placeholder="课程作者"  ></el-input>
        </el-form-item>
        <el-form-item label="联系方式" :label-width="formLabelWidth" prop="phone" >
          <el-input   v-model="form.phone" placeholder="联系方式"  ></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button class="button-green" @click="addSave('form')">保存</el-button>
      </div>
    </el-dialog>


    <!--课程导入-->
    <el-dialog :title="title"
               :visible.sync="importVisible"
               width="40%" v-if="importVisible">
      <el-form >
        <el-form-item label="压缩文件" :label-width="formLabelWidth">
          <el-upload
            class="upload-demo"
            :action="importActionUrl"
            :limit="1"
            :auto-upload="false"
            ref="upload"
            :headers="uploadHeaders"
            :on-success="importUploadSuccess"
            :before-upload="importBeforeUploadHandler"
            accept=".deep"
            :on-exceed="handleExceed"
          >
            <el-button size="small" type="primary" slot="trigger">选择文件</el-button>
            <el-button style="margin-left: 10px;" size="small" type="success" @click="importCourseSubmit">导入</el-button>
            <div slot="tip" class="el-upload__tip">请选择deep格式的压缩文件</div>
          </el-upload>
        </el-form-item>

      </el-form>

    </el-dialog>
  </div>
</template>

<script type="text/ecmascript-6">
  import {addkecheng, deletekecheng, edu_url, getCustomers, getKecheng, updateKecheng} from '@/common/js/request';
  import axios from 'axios'

  export default{
    data () {
      //检验课程名称
      var validaTEName = (rule, value, callback) => {
        if (typeof value == 'undefined' || value =="") {
          callback(new Error('请输入课程名称'));
        } else if(value.trim().length > 45){
          callback(new Error('课程名称长度不能超过45'));
        } else if((/^\s+/).test(value)){
          callback(new Error('开头不能输入空格'));
        }else if((/\s+$/).test(value)){
          callback(new Error('结尾不能输入空格'));
        }else if((/^_+/).test(value)){
          callback(new Error('开头不能输入下划线'));
        } else {
          callback();
        }
      };
      //检验课程描述
      var validaDescription = (rule, value, callback) => {
        if (typeof value == 'undefined' || value =="") {
          callback(new Error('请输入课程描述'));
        } else if(value.trim().length > 1024){
          callback(new Error('课程描述长度不能超过1024'));
        } else if((/^\s+/).test(value)){
          callback(new Error('开头不能输入空格'));
        }else if((/\s+$/).test(value)){
          callback(new Error('结尾不能输入空格'));
        } else {
          callback();
        }
      };
      var validaAuthor = (rule, value, callback) => {
        if (typeof value == 'undefined' || value =="") {
          callback(new Error('请输入课程作者'));
        } else if(value.length > 45){
          callback(new Error('作者名称长度不能超过45'));
        } else {
          callback();
        }
      };
      var validaPhone = (rule, value, callback) => {
        var phoneRex = /^1\d{10}$/;
        var mailRex = /^[A-Za-z0-9\u4e00-\u9fa5+\.+\-+\_]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
        if (typeof value == 'undefined' || value =="") {
          callback(new Error('请输入作者联系方式'));
        }
        if(value.length > 45){
          callback(new Error('联系方式长度不能超过45'));
        }
        if(!((phoneRex).test(value) || (mailRex).test(value))){
          callback(new Error('请输入正确的联系方式'));
        } else {
          callback();
        }
      };
      return {
        importVisible: false,
        form: {
          teacherId: 1,
          name:'',
          description:''
        },
        para:{
          type: '0',
          courseId: -1
        },
        listData: [], // 表单展示的数据
        listDataTmp: [], // 获取所有的数据
        listDataFilterTmp: [], // 获取过滤后所有的数据
        dialogFormVisible: false, // 新增数据弹出框
        formLabelWidth: '80px',
        fileList: [{name: 'food.jpeg', url: ''}],
        searchQuery: '', // 搜索的结果
        // 获取教师数据
        TeacherData: [],
        isAdd: true,
        // 分页参数
        p_currentPage: 1,
        p_pageSizes: [5, 10, 15, 20, 30, 50, 100],
        p_pageSize: 5,
        p_total: 0,
        actionUrl:null,
        importActionUrl: null,
        rules: {
          name: [
            // {required: true, message: '请输入课程名称', trigger: 'blur'},
            // {min: 1, max: 45, message: '长度在 1 到 45个字符', trigger: 'blur'}
            { required: true,validator: validaTEName, trigger: 'blur' }
          ],
          description: [
            // {required: true, message: '请输入课程描述', trigger: 'blur'},
            // {min: 1, max: 1024, message: '长度在 1 到 1024个字符', trigger: 'blur'}
            { required: true,validator: validaDescription, trigger: 'blur' }
          ],
          author: [
            { required: true,validator: validaAuthor, trigger: 'blur' }
          ]
          /*phone: [
            { required: true,validator: validaPhone, trigger: 'blur' }
          ]*/
        },
        uploadHeaders: {
          'Authorization': 'Bearer ' + (localStorage['token'] ? localStorage['token'] : '')
        },
        title:'',
        multipleSelection:[]
      }
    },
    created() {
      console.log("created");
      // console.log(getKecheng())
      //http://192.168.137.185:8080/admin/course/experiment/piclib
      this.actionUrl =  '/admin/course/experiment/piclib';
      this.importActionUrl = '/port/importCourse';
    },
    activated () {
      console.log("activated");
      //this.searchQuery='';
      this._getListData();
      this.GetTeacher();//教师数据
    },
    methods: {

      checkPhoneOrMail: function(value){
        var phoneRex = /^1\d{10}$/;
        var mailRex = /^[A-Za-z0-9\u4e00-\u9fa5+\.+\-+\_]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
        var spaceRex = /^\S+$/;
        if (typeof value == 'undefined' || value =="") {
          this.$message({
            //title:'提示',
            message: '警告哦，联系方式不能为空',
            type:'error'
          });
          return false;
          //callback(new Error('请输入作者联系方式'));
        }

        if(value.length > 45){
          this.$message({
            //title:'提示',
            message: '警告哦，联系方式长度不能超过45',
            type:'error'
          });
          return false;
          //callback(new Error('联系方式长度不能超过45'));
        }
        if(!((phoneRex).test(value) || (mailRex).test(value))){
          this.$message({
            //title:'提示',
            message: '请输入正确的手机号或者邮箱号',
            type:'error'
          });
          return false;
          //callback(new Error('请输入正确的联系方式'));
        }

        return true;

      },
      //点击刷新按钮
      _Refresh:function () {
        this.searchQuery="";
        this._getListData();
        // 点击刷新默认显示每页5条数据
        this.p_pageSize=5
      },
      // 获取数据
      addCourse:function(){
        this.isAdd = true;
        this.form = {};
        this.dialogFormVisible = true;
        this.title="新增课程";
      },
      submitUpload:function(){
        if(this.isAdd){
          this.para.type = '1'
        }else {
          this.para.type = '2';
          this.para.courseId = this.form.id;
        }
        //上传文件
        this.$refs.upload.submit();
      },
      importCourse: function(){
        //上传文件
        this.importVisible = true;

      },
      importCourseSubmit :function(){
        this.$refs.upload.submit();
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },
      importUploadSuccess: function(response, file, fileList){
        console.log(response);

        if(response.errorCode==0){
          /* this.$refs.upload.clearFiles();*/
          this.$message({
            title:'提示',
            message: '导入成功！',
            type:'success'
          });
          this._getListData();
        }else {
          this.$message({
            title:'提示',
            message: response.message,
            type:'error'
          });
        }
        this.importVisible = false;
      },
      importBeforeUploadHandler: function(file){
        console.log(file);
        var fileName = file.name;
        var fileTypeRex = /\.deep$/;
        //var index = fileName.indexOf(".deep");
        //const isDEEP = file.name. === 'application/zip';
        const isLt50M = file.size / 1024 / 1024 < 50;

        if (!fileTypeRex.test(fileName)) {
          this.$message({
            title:'提示',
            message:'文件格式不正确',
            type:'error'
          });
          return false;
        }
        if (!isLt50M) {
          this.$message({
            title:'提示',
            message:'上传课程压缩包的大小不能超过 50MB!',
            type:'error'
          });
          return false;
        }
      },
      uploadSuccess:function(response, file, fileList){
        console.log("我上传成功了");
        console.log(response);
        if(response.errorCode==0){
         /* this.$refs.upload.clearFiles();*/
          this.form.imageUrl=response.data.url;
          this.form.folderName = response.data.folderName;
        }
      },
      beforeUploadHandler:function(file){
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
            message:'上传课程图片大小不能超过 200kB!',
            type:'error'
          });
          return false;
        }
        /*var fileName = file.name;
        if(fileName.lastIndexOf('.jpg') < 0 && fileName.lastIndexOf('.png') <0){
          this.$message({
            title:'提示',
            message:'图片格式不正确',
            type:'error'
          });
          return false;
        }*/
      },
      _getListData: function () {
        if(this.searchQuery.length>45){
          this.$message({
            type:'error',
            message:'搜索长度不能超过45个字符'
          });
          return;
        }

        // 点击搜索，默认显示每页5条数据
        this.p_pageSize=5

        getKecheng().then(res => {
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          this.listDataTmp = res.data.data.courseInfoList;
          this._filterData()
          this._initPageInfo()
          //console.log(this.listDataTmp)
        })
      },
      // 初始化分页信息
      _initPageInfo: function () {
        this.p_total = this.listDataFilterTmp.length
        this.p_currentPage = 1
        this.listData = this.listDataFilterTmp.slice(0, this.p_pageSize)
      },
      // filter data
      _filterData: function () {
        if (this.searchQuery === '') {
          this.listDataFilterTmp = this.listDataTmp;
          return
        }
        let filterTableDataEnd = [];
        this.listDataTmp.forEach((value, index) => {
          if (
            (value.id && (value.id+'').indexOf(this.searchQuery) >= 0) ||
            (value.courseName && value.courseName.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0) ||
            (value.courseDes && value.courseDes.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0) ||
            (value.author && value.author.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0) ||
            (value.phone && (value.phone + "").indexOf(this.searchQuery) >= 0)
          ) {
            filterTableDataEnd.push(value)
          }
        });
        this.listDataFilterTmp = filterTableDataEnd
      },
      // 改变分页
      handleSizeChange(val) {
        this.p_pageSize = val
        this._initPageInfo()
      },
      handleCurrentChange(val) {
        this.p_currentPage = val
        this.listData = this.listDataFilterTmp.slice((val - 1) * this.p_pageSize, (val) * this.p_pageSize)
      },
      // 根据查询条件查询所有数据
      select: function () {
        if(this.searchQuery.length>45){
          this.$message({
            type:'error',
            message:'搜索长度不能超过45个字符'
          });
          return;
        }
        console.log("我执行了");
        this._filterData();
        this._initPageInfo();
      },

      // 获取教师数据
      GetTeacher: function () {
        getCustomers().then(res => {
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          this.TeacherData = res.data.data.teacherInfoList
        })
      },
      // 删除
      // deletedata(index, row) {
      //   this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     type: 'warning'
      //   }).then((res) => {
      //     console.log(row.id)
      //     deletekecheng(row.id).then(res => {
      //       // console.log(this.listData.id)
      //       if(res.status==200){
      //         if(res.data.errorCode!=0){
      //           // 删除提示语
      //           this.$message({
      //             type: 'error',
      //             message: res.data.message
      //           });
      //         } else {
      //           this._getListData();
      //           // 删除提示语
      //           this.$message({
      //             type: 'success',
      //             message: '删除成功!'
      //           });
      //         }
      //       }
      //     })
      //   }).catch(() => {
      //     this.$message({
      //       type: 'info',
      //       message: '已取消删除'
      //     });
      //   });
      // },
      // updateKecheng(index){
      //   var selectData = this.listData[index];
      //   selectData.name = selectData.courseName;
      //   selectData.description = selectData.courseDes;
      //   //this.title=selectData.courseName
      //   Object.assign(this.form,selectData);
      //   this.isAdd = false;
      //   this.dialogFormVisible = true;
      //   //
      //   this.title="编辑课程"
      // },
      // 添加课程
      addSave(form) {

        this.$refs[form].validate((valid) => {

          if(!this.checkPhoneOrMail(this.form.phone)){
              return;
          }
         // console.log(this.$refs[form])
          if (valid) {
            if (this.isAdd) {
              //添加
              console.log('添加');
              console.log(this.form);
              addkecheng(this.form).then(res => {
                if(res.data.errorCode==0) {
                  this.$message({
                    type: 'success',
                    message: '添加成功'
                  });
                }else{
                  this.$message({
                    type: 'error',
                    message: res.data.message
                  });
                }
                this.dialogFormVisible = false;
                this._Refresh();
                //this._getListData();
              })
            } else {
              //更新
              updateKecheng(this.form).then(res=>{
                if(res.data.errorCode==0){
                  this.$message({
                    type: 'success',
                    message: '更新成功'
                  });

                }else{
                  this.$message({
                    type: 'error',
                    message: res.data.message
                  });
                }
                this.dialogFormVisible = false;
                this._Refresh();
                //this._getListData();
              });
            }
          } else {
            console.log('error submit!!');
            // this.$message.error('错了哦，提交失败')
            return false;
          }
        });
      },
      // courseMaintenance: function (index) {
      //   var courseId = this.listData[index].id;
      //   this.$router.push('/experiment/' + courseId);
      // },
      // 选择的数据
      handleSelectionChange(val) {
        this.multipleSelection = val;
        console.log("获取的数据长度为:" + this.multipleSelection.length)
      },
      // 课程实验维护
      kechengMain(){
        if (this.multipleSelection.length === 1) {
          let id = this.multipleSelection[0].id;
          console.log(id)
          this.$router.push('/experiment/' + id);
        }else{
          this.$message({
            message: '警告哦，只能选择单行数据',
            type: 'warning'
          });
        }
      },
      exportCourse :function(){
        if (this.multipleSelection.length < 5) {
          for(var i = 0; i < this.multipleSelection.length; i++) {
            let id = this.multipleSelection[i].id;
            let courseName = this.multipleSelection[i].courseName;
            console.log(id);
            axios({
              method: 'get',
              url: '/port/exportCourse',
              params: {
                courseName: courseName
              },
              responseType: 'blob'
            }).then(response => {
              console.log(response);
              this.download(response,courseName);
            }).catch((error) => {

            })
          }

        }else{
          this.$message({
            message: '最多只能同时导出5个课程',
            type: 'warning'
          });
        }
      },
      download :function(data,courseName){
        if (!data) {
          return
        }
        var blob = new Blob([data.data], {type: "application/zip"});
        let url = window.URL.createObjectURL(blob);
        //console.log(url)
        let link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        link.setAttribute('download', courseName + '.deep');
        document.body.appendChild(link);
        link.click()
      },
      // 编辑课程
      updateCourse(){
        if (this.multipleSelection.length === 1) {
          var selectData = this.multipleSelection[0];
          console.log(selectData)
          selectData.name=selectData.courseName
          selectData.description=selectData.courseDes
          Object.assign(this.form,selectData);
          this.isAdd = false;
          this.dialogFormVisible = true;
          //
          this.title="编辑课程"
        }else{
          this.$message({
            message: '警告哦，只能编辑单行数据',
            type: 'warning'
          });
        }
      },
      // 删除课程
      deleteCourse(){
        if(this.multipleSelection.length==0){
          this.$message({
            message: '警告哦，至少选择一行数据',
            type: 'warning'
          });
        } else{
          this.$confirm('此操作将永久删除选中的课程数据, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
          }).then(() => {
            console.log(this.multipleSelection);
            var mulit=JSON.parse(JSON.stringify(this.multipleSelection));
            this._deleteKecheng(mulit);

          /*  for(let [idx,item] of this.multipleSelection.entries()){
              //console.log(item.id);
              deletekecheng(item.id).then(res => {
                console.log("------res为-------"+res);
                if(res.status==200){
                  console.log(res.data.errorCode)
                  if(res.data.errorCode!=0){
                    // 删除提示语
                    this.$message({
                      type: 'error',
                      message: res.data.message
                    });
                  } else {
                    if(idx==this.multipleSelection.length-1) {
                      this.$message({
                        message: '删除成功',
                        type: 'success'
                      });
                      this._getListData();
                    }
                  }
                }
              })
            }*/
          }).catch(() => {
            // this.$message({
            //   type: 'info',
            //   message: '已取消删除'
            // });
          });
        }
      },
      _deleteKecheng:function (mulit) {
        deletekecheng(mulit.splice(-1)[0].id).then(res=>{
          if(res.status==200){
            console.log(res.data.errorCode);
            if(res.data.errorCode!=0){
              // 删除提示语
              this.$message({
                type: 'error',
                message: res.data.message
              });
              this._Refresh();
            } else {
              if(mulit.length==0) {
                this.$message({
                  message: '删除成功',
                  type: 'success'
                });
                this._Refresh();
                //this._getListData();
              }
              if(mulit.length==0){
                console.log("没有数据");
                return;
              }
              this._deleteKecheng(mulit);
            }
          }
        });
    }

    },
    beforeRouteEnter: (to, from, next) => {
      console.log("我是路由，我进入了");
      next(vm => {
        // vm._getExpirementInfo();
        // vm.handleSizeChange(5)
        vm.p_currentPage=1
        vm.searchQuery=""
        vm.p_pageSize=5
      });
    }
    // watch监听输入框长度
    // watch: {
    //   searchQuery(){
    //     if(this.searchQuery.length>45){
    //       this.$message({
    //         title:"提示",
    //         message:"输入长度不能超过45",
    //         type:"error"
    //       });
    //     }
    //   }
    // }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  .xueqi
    h1
      font-size 22px
      margin 20px 3% 0px
      font-family  "Microsoft YaHei"
    .demo-form-inline
      width 94%
      margin 20px auto
      padding-top 20px
      border-top 1px solid #ebeef5 !important
    #Table
      width 94%
      height auto
      margin 0px auto
    .el-dialog
      width 50%
    .copyright
      text-align right
      color #b4b4b4
      font-size 14px
      margin-top 20px
      margin-right 3%

</style>
<style type="text/css">
  #btn-header .el-form-item:not(:nth-child(1)){
    float: right;
  }
  .xueqi #Table .el-table--border th .cell{
    text-align: center;
    font-weight: bolder;
    font-size: 1.1em;
  }
  .el-textarea__inner{
    color: #606266 !important;
    font-size: inherit;
    font-family: '微软雅黑';
  }
  .el-select-dropdown__item span{
    white-space:pre-wrap
  }
  .xueqi .el-table .cell{
    white-space: nowrap;
    overflow: hidden;
    word-break:break-all;
    text-overflow: ellipsis;
    padding-right: 10px;
  }
  /*editDialog*/
  /*弹出框输入框宽度*/
  #editDialog .el-form-item{
    width: 80%;
    margin: 0px auto 20px;
  }
  /*弹出框表单名称*/
  #editDialog .el-form-item__label{
    text-align: left;
  }
  #editDialog .el-dialog__body{
    padding: 5px 20px;
  }
</style>
