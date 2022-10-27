<template>
  <div class="xueqi">
    <h1>班级详情</h1>
    <el-form :inline="true"  class="demo-form-inline" @submit.native.prevent>
      <el-form-item >
        <el-button class="button-green" @click="_getCustomers()">刷新</el-button>
        <el-button class="button-green" @click="ImportStudent()">导入学生名单</el-button>
        <el-button class="button-green" @click="addStudentDialog">添加学生</el-button>
        <el-button class="button-green" @click="resetMiMa">重置密码</el-button>
        <el-button class="button-green" @click="deleteDialog">删除</el-button>
      </el-form-item>
      <el-form-item label="搜索" style="float: right">
        <el-input v-model="searchQuery" @keyup.enter.native="select()" placeholder="搜索"></el-input>
      </el-form-item>
    </el-form>
    <!--表格-->
    <div id="table">
      <el-table
        ref="multipleTable"
        tooltip-effect="dark"
        :border="true"
        :data="listData"
        @selection-change="handleSelectionChange"
        style="width: 100%;">
        <el-table-column
          type="selection"
          min-width="140" >
        </el-table-column>
        <el-table-column
          prop="id"
          label="ID"
           :resizable="false">
          <template slot-scope="alldata">
            <p >{{ alldata.row.id }}</p>
          </template>
        </el-table-column>

        <el-table-column
          prop="studentNo"
          label="学号"
         :resizable="false">
          <template slot-scope="alldata">
            <p >{{ alldata.row.studentNo }}</p>
          </template>
        </el-table-column>

        <el-table-column
          prop="studentName"
          label="姓名" :resizable="false">
          <template slot-scope="alldata">
            <p >{{ alldata.row.studentName }}</p>
          </template>
        </el-table-column>

        <el-table-column
          prop="gender"
          label="性别" :resizable="false">
          <template slot-scope="alldata">
            <p >{{ alldata.row.gender == 1 ? '男' : '女' }}</p>
          </template>
        </el-table-column>


        <!--<el-table-column label="操作" min-width="140" :resizable="false">-->
          <!--<template slot-scope="alldata">-->
            <!--<el-button size="mini" type="warning" @click="resetPwd(alldata.row.id)">重置密码</el-button>-->
            <!--<el-button size="mini" type="danger" @click="handleDelete(alldata.row)">删除</el-button>-->
          <!--</template>-->
        <!--</el-table-column>-->
      </el-table>
    </div>
    <div style="clear: both"></div>
    <div style="width:100%;">
      <div id="pagination" style="width:25%;margin:20px 20%">
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
    <!--新增-->
    <el-dialog title="添加学生" :visible.sync="dialogFormVisible"
               width="40%">
      <el-form :model="form" :rules="addRules" ref="form" >
        <el-form-item label="学号" :label-width="formLabelWidth" prop="studentNo">
          <el-input v-model="form.studentNo" placeholder="学号" ></el-input>
        </el-form-item>
        <el-form-item label="姓名" :label-width="formLabelWidth" prop="studentName">
          <el-input v-model="form.studentName" placeholder="姓名" ></el-input>
        </el-form-item>
        <el-form-item label="性别" :label-width="formLabelWidth" prop="gender">
          <el-select  v-model="form.gender" placeholder="请选择性别">
            <el-option label="男" value="1"></el-option>
            <el-option label="女" value="2"></el-option>

          </el-select>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button class="button-green" @click="addViseble">保存</el-button>
      </div>
    </el-dialog>
    <!--导入文件-->
    <el-dialog
      title="学生导入"
      :visible.sync="importVisible"
      width="50%"
      @close="xStudentImportDialog()"
      v-if="importVisible">
      <el-upload
        class="upload-demo"
        ref="upload"
        :headers="uploadHeaders"
        :action="batchUrl"
        :auto-upload="false"
        :file-list="fileList"
        :on-success="uploadSuccess"
        :before-upload="beforeUploadHandler"
        accept=".xls,.xlsx"
        :limit="1"
        :on-exceed="handleExceed" v-loading="loading">
        <el-button size="small" class="button-green" slot="trigger">选择文件</el-button>
        <div slot="tip" class="el-upload__tip">可以支持xls和xlsx文件 小于20kb，表格样式请参考样图<br/>
        <img src="../../../static/images/studentImport.png"/>
        </div>
      </el-upload>
      <el-table :data="errorData"
                v-if="errorData.length>0"
                header-row-class-name="column-left"
                max-height="200px" style="overflow-y: auto">
        <el-table-column
          prop="studentNum"
          label="学号"
          min-width="180">
        </el-table-column>
        <el-table-column
          prop="studentName"
          label="姓名"
          min-width="180">
        </el-table-column>
        <el-table-column
          prop="reason"
          label="提示"
          min-width="180">
        </el-table-column>
        <el-table-column
          prop="errorCode"
          label="错误码"
          min-width="180"
          v-if="false">
        </el-table-column>
        <!--<el-table-column-->
          <!--label="操作"-->
          <!--width="180">-->
          <!--<template slot-scope="scope">-->
            <!--<el-button-->
              <!--size="mini"-->
              <!--@click="addStudent(scope)">确定</el-button>-->
          <!--</template>-->
        <!--</el-table-column>-->
      </el-table>
      <span slot="footer" class="dialog-footer">
    <el-button @click="closeImportDialog()">关闭</el-button>
    <el-button class="button-green" @click="submitUpload()">上传</el-button>
  </span>
    </el-dialog>
    <!--版权-->
    <div class="copyright">
      <p>版权信息  版权股所有   悦岚数据服务</p>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import {getStudentInfo,resetPWD,deleteStudent,addStudent,edu_url,getCourseByCourseName} from '@/common/js/request';
  export default{
    data(){
      return{
        form: {},
        listData:[],
        listDataTmp:[],
        listDataFilterTmp: [],
        dialogFormVisible: false,
        formLabelWidth: '100px',
        importVisible:false,
        classId:null,
        batchUrl:null,
        errorData:[],
        couId:"",
        searchQuery:"",
        fileList:[],
        uploadHeaders: {
          'Authorization': 'Bearer ' + (localStorage['token'] ? localStorage['token'] : '')
        },
        // 分页参数
        p_currentPage: 1,
        p_pageSizes: [5, 10, 15, 20, 30, 50, 100],
        p_pageSize: 5,
        p_total: 0,
        addRules: {
          studentNo: [
            { required: true ,message:'请输入学号', trigger: 'blur'}
          ],
          studentName: [
            { required: true ,message:'请输入姓名', trigger: 'blur'}
          ],
          gender: [
            { required: true,message:'请选择性别', trigger: 'blur' }
          ]
        },
        multipleSelection:[],
        loading:false
      }
    },
    created() {
      var classId = this.$route.params.classId;
      this.classId = classId;
      this.batchUrl="/admin/class/student/batchCreation?classId="+this.classId;
      this._getCustomers();
    },
    activated() {
      var courseName= this.$route.params.courseName;
      console.log(courseName+"课程名称");
      if(courseName!=""){
        getCourseByCourseName(courseName).then(res=>{
          if(res.data.data!=null){
            this.couId=res.data.data.id;
          }
        })
      }
      var classId = this.$route.params.classId;
      this.classId = classId;
      this.batchUrl="/admin/class/student/batchCreation?classId="+this.classId;
      this._getCustomers();
      this.searchQuery=""
    },
    methods: {
    /*  fileChange: function(file, fileList){
        console.log('fileChange');
        if(fileList.length < 1){
          this.isDisabled = true;
        }else {
          this.isDisabled = false;
        }
      },*/
      xStudentImportDialog: function(){
        this.importVisible = false;
        this._getCustomers();
      },
      closeImportDialog: function(){
        this.importVisible = false;
        this._getCustomers();

      },
      addStudentDialog:function(){
        //添加学生对话框
        this.form = {};
        this.dialogFormVisible = true;
      },
      addStudent:function(scope){
        var row = scope.row;
        var errorCode = row.errorCode;
        if(errorCode == 29){
          row.override = true;
          row.studentNo = row.studentNum;
          addStudent(row).then(res=>{
            if(res.data.errorCode==0){
              this.$message({
                type: 'success',
                message: '添加成功'
              });
              this._getCustomers();
            }else {
              this.$message({
                type: 'error',
                message: res.data.message
              });
            }
          });

        }
        //this.importVisible = false;
        this._getCustomers();
        this.fileList=[];

      },
      _validateForm: function (form) {
        // 判断编号不能为空
        let errorMsg = null
        if (!form.studentNo || form.studentNo.trim().length === 0) {
          errorMsg = '学号不能为空'
        }
        else if (form.studentNo.trim().length > 40) {
          errorMsg = '学号长度不能超过40'
        }else if((/^\s+/).test(form.studentNo)){
          errorMsg = '开头不能输入空格'
        }else if((/\s+$/).test(form.studentNo)){
          errorMsg = '结尾不能输入空格'
        }
        else {
          //var regex = /^[0-9]+$/;
          var regex = /^\+?[0-9]\d*$/;
          if(!regex.test(form.studentNo.trim())){
            errorMsg = "学号只能为大于或等于0的整数";
          }
        }
        if (errorMsg && errorMsg.trim().length > 0) {
          this.$message.error(errorMsg)
          return false
        }
        // 判断名称不能为空
        if (!form.studentName || form.studentName.trim().length === 0) {
          errorMsg = '名称不能为空'
        } else if (form.studentName.trim().length > 40) {
          errorMsg = '名称长度不能超过40'
        }else if((/^\s+/).test(form.studentName)){
          errorMsg = '开头不能输入空格'
        }else if((/\s+$/).test(form.studentName)){
          errorMsg = '结尾不能输入空格'
        }
        if (errorMsg && errorMsg.trim().length > 0) {
          this.$message.error(errorMsg)
          return false
        }
        // 性别不能为空
        if (!form.gender) {
          errorMsg = '性别不能为空'
        }
        if (errorMsg && errorMsg.trim().length > 0) {
          this.$message.error(errorMsg)
          return false
        }
        return true
      },
      //添加学生
      addViseble: function () {
        if (!this._validateForm(this.form)) {
          return
        }
        this.form.classId = this.classId;
        this.form.override = false;
        addStudent(this.form).then(res => {
          console.log(res)
          if(res.data.errorCode === 29) {
            this.$confirm(res.data.message,'提示',{
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(()=>{
              this.form.override=true;
              addStudent(this.form).then(res=>{
                if(res.data.errorCode==0){
                  this.$message({
                    type: 'success',
                    message: '添加成功'
                  });
                  this.dialogFormVisible=false;
                  this._getCustomers();
                }
                else {
                  this.$message({
                    type: 'error',
                    message: '学生添加失败，请稍后重试'
                  });
                  this.dialogFormVisible=false;
                }

              });

            }).catch(()=>{
              this.$message({
                type: 'info',
                message: '已取消添加'
              });
              this.dialogFormVisible=false;
            })
          } else if (res.data.errorCode === 0) {
            this.$message({
              type: 'success',
              message: '添加成功'
            });
            this.dialogFormVisible = false;
            this._getCustomers();
          } else if (res.data.errorCode === 32) {
            this.$message.warning('该用户名已被非学生用户使用')
            this.dialogFormVisible = false;
          } else {
            // 异常
            this.$message.error(res.data.message)
            this.dialogFormVisible=false;
          }
        })

      },
      submitUpload:function(){
          //上传
          this.$refs.upload.submit();
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      // beforeRemove(file, fileList) {
      //   return this.$confirm(`确定移除 ${ file.name }？`);
      // },
      beforeUploadHandler:function(file){
        //判断文件格式是否正确
        var fileName = file.name;
        if(fileName.lastIndexOf(".xls")<0 && fileName.lastIndexOf(".xlsx")<0 && fileName.lastIndexOf(".xlt")<0){
          this.$message({
            title:"提示",
            message:"只能上传excel格式文件",
            type:"error"
          });
          this.loading=false;
          return false;
        }
        if(file.size>1024*20){
          this.loading = false;
          this.$message({
            title:"提示",
            message:"文件超过20KB",
            type:"error"
          });
          this.loading=false;
          return false;
        } else {
          this.loading = true;
          return true;
        }
      },
      uploadSuccess:function(response){
        this.loading = false;
        if(response.errorCode==0){
          this.$refs.upload.clearFiles();
          this.errorData = response.data.failureReasonList;
          if(this.errorData.length==0){
            this.importVisible = false;
            this.fileList=[];
          }else {
            for(var i = 0;i < this.errorData.length;i++){
              var data = this.errorData[i];
              if(data.errorCode == 29){
                data.reason = (data.reason.split("。"))[0];
              }
            }
          }
          this._getCustomers();
        }else {
          this.importVisible = false;
          this.fileList=[];
          this.$message({
            title:"提示",
            message:response.message,
            type:"error"
          });
        }
      },
      // 获取数据
      _getCustomers: function() {
        getStudentInfo(this.classId).then(res=>{
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          this.listDataTmp=res.data.data.studentList;
          this._filterData();
          this._initPageInfo();
          //this.listData=res.data.data.studentList;
        })
      },
      _filterData: function () {
        if (this.searchQuery === '') {
          this.listDataFilterTmp = this.listDataTmp;
          return
        }
        let filterTableDataEnd = [];
        this.listDataTmp.forEach((value, index) => {
          var genderName = '';
          if(value.gender == 1){
            genderName = '男';
          }else if(value.gender == 2){
            genderName = '女';
          }
          if (
            (value.id && (value.id+'').indexOf(this.searchQuery) >= 0) ||
            (genderName && genderName.indexOf(this.searchQuery) >= 0) ||
            (value.studentNo && value.studentNo.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0) ||
            (value.studentName && value.studentName.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0)

          ) {
            filterTableDataEnd.push(value)
          }
        });
        this.listDataFilterTmp = filterTableDataEnd
      },
      _initPageInfo: function () {
        this.p_total = this.listDataFilterTmp.length;
        this.p_currentPage = 1;
        this.listData = this.listDataFilterTmp.slice(0, this.p_pageSize);
      },handleSizeChange(val) {
        this.p_pageSize = val;
        this._initPageInfo()
      },
      handleCurrentChange(val) {
        this.p_currentPage = val;
        this.listData = this.listDataFilterTmp.slice((val - 1) * this.p_pageSize, (val) * this.p_pageSize)
      },
      // 根据查询条件查询所有数据
      select: function () {
        console.log("我执行了");
        this._filterData();
        this._initPageInfo();
      },
      // 重置密码
      // resetPwd(userId){
      //   this.$confirm('是否重置密码, 是否继续?', '提示', {
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     type: 'warning'
      //   }).then(() => {
      //     resetPWD(userId).then(res=>{
      //       if(res.data.errorCode==0){
      //         this.$message({
      //           type: 'success',
      //           message: '重置成功!  重置密码为12345678'
      //         });
      //       }
      //     });
      //   }).catch(() => {
      //     this.$message({
      //       type: 'info',
      //       message: '重置失败'
      //     });
      //   });
      // },
      // 删除
      // handleDelete(row){
      //   debugger
      //   this.$confirm('是否删除该学生, 是否继续?', '提示', {
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     type: 'warning'
      //   }).then(() => {
      //     var studentId = row.id;
      //     console.log("-----classId:------"+this.classId)
      //     console.log("-----studentId:-------"+studentId)
      //     console.log("-----studentId:-------"+this.couId)
      //     deleteStudent(this.classId,studentId,this.couId).then(res=>{
      //       if(res.data.errorCode==0){
      //         this.$message({
      //           type: 'success',
      //           message: '删除成功!'
      //         });
      //         this._getCustomers();
      //       } else {
      //         this.$message({
      //           type: 'error',
      //           message: res.data.message
      //         });
      //       }
      //     });
      //   }).catch(() => {
      //     this.$message({
      //       type: 'info',
      //       message: '已取消删除'
      //     });
      //   });
      // },
      // 导入学生名单
      ImportStudent(){
        this.tableData=[];
        this.errorData=[];
        this.importVisible=true;
        this.loading=false;
      },
      // 选择数据
      handleSelectionChange(val) {
        this.multipleSelection = val;
        // console.log(value.length)
      },
      //表格外面 --- 重置密码
      resetMiMa(){
        console.log(this.multipleSelection)
        if(this.multipleSelection.length==0){
          this.$message({
            message: '警告哦，至少选择一行数据',
            type: 'warning'
          });
        }else{
          console.log(this.multipleSelection)
          this.$confirm('是否重置密码, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            for(let [idx,item] of this.multipleSelection.entries()){
              let userId=item.id;
              resetPWD(userId).then(res=>{
                if(res.data.errorCode==0){
                  this.$message({
                    type: 'success',
                    message: '重置成功!  重置密码为12345678'
                  });
                }
              });
            }
          }).catch(() => {
            // this.$message({
            //   type: 'info',
            //   message: '重置失败'
            // });
          });
        }
      },
      //表格外面 --删除数据
      deleteDialog(){
        // debugger
        console.log(this.multipleSelection)
        console.log("-----classId为:------"+this.classId)
        if(this.multipleSelection.length==0){
          // alert("请选择数据")
          this.$message({
            message: '警告哦，至少选择一行数据',
            type: 'warning'
          });
        }
        else{
          this.$confirm('此操作将永久删除选中的学生数据, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
          }).then(() => {
            for(let [idx,item] of this.multipleSelection.entries()){
              //console.log(item.id);
              deleteStudent(this.classId,item.id,this.couId).then(res => {
                console.log(res);
                if(res.status==200){
                   if(res.data.errorCode ==0){
                    if(idx==this.multipleSelection.length-1) {
                      this.$message({
                        type: 'success',
                        message: '删除成功!'
                      });
                      this._getCustomers();
                    }
                  } else {
                     // 删除提示语
                     this.$message({
                       type: 'error',
                       message: res.data.message
                     });
                   }
                }
              })
            }


            // var studentId = row.id;
            // deleteStudent(this.classId,studentId,this.couId).then(res=>{
            //   if(res.data.errorCode==0){
            //     this.$message({
            //       type: 'success',
            //       message: '删除成功!'
            //     });
            //     this._getCustomers();
            //   } else {
            //     this.$message({
            //       type: 'error',
            //       message: res.data.message
            //     });
            //   }
            // });
          }).catch(() => {
            // this.$message({
            //   type: 'info',
            //   message: '已取消删除'
            // });
          });
        }

      }
    },
    beforeRouteEnter: (to, from, next) => {
      console.log("我是路由，我进入了");
      next(vm => {
        // vm._getExpirementInfo();
        // vm.handleSizeChange(5)
        vm.p_currentPage=1
      });
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>

  .column-left th > .cell
    text-align left !important
  .xueqi
    width 100%
    h1
      font-size 28px
      margin 20px 3% 0px
    .demo-form-inline
      width 94%
      margin 20px auto
      padding-top 20px
      border-top 1px solid #ebeef5 !important
    #table,#pagination
      width 94%
      height auto
      margin 0px auto
    #pagination
      .block
        border 1px solid red
    .el-dialog
      width 50%
    #table
      text-align center
      .el-table--border th .cell
        text-align center
    .copyright
      text-align right
      color #b4b4b4
      font-size 14px
      margin-top 20px
      margin-right 3%
</style>
<style type="text/css" >
  .el-table--border th .cell {
    text-align: center;
    font-weight: bolder;
    font-size: 1.1em;
  }
</style>
