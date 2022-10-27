<template>
  <div class="student">
    <h1 id="title">学生管理</h1>
    <el-form :inline="true"  id="btn-header" class="demo-form-inline" @submit.native.prevent>
      <el-form-item >
       <!-- <el-button type="primary" @click="_getCustomers()">刷新</el-button>-->
        <el-button type="primary" @click="_Refresh()">刷新</el-button>
        <el-button type="primary" @click="ImportStudent()">导入学生</el-button>
        <el-button type="primary" @click="addStudentDialog">新增学生</el-button>
        <el-button type="primary" @click="updateDialog">编辑</el-button>
        <el-button type="primary" @click="resetMiMa">重置密码</el-button>
        <el-button type="primary" @click="deleteDialog">删除</el-button>
      </el-form-item>
      <el-form-item style="margin-right: 0px">
        <el-button type="primary" @click="_getCustomers()">搜索</el-button>
      </el-form-item>
      <el-form-item style="float: right">
        <el-input v-model="searchQuery"
                  @keyup.enter.native="select()"
                  placeholder="搜索"></el-input>
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
        <!--<el-table-column-->
          <!--prop="id"-->
          <!--label="ID"-->
          <!--:resizable="false">-->
          <!--<template slot-scope="alldata">-->
            <!--<p>{{alldata.row.id}}</p>-->
          <!--</template>-->
        <!--</el-table-column>-->
        <el-table-column
          prop="studentNo"
          label="学号"
          :resizable="false">
          <template slot-scope="alldata">
            <p >{{alldata.row.studentNo}}</p>
          </template>
        </el-table-column>

        <el-table-column
          prop="studentName"
          label="学生姓名"
          :resizable="false">
          <template slot-scope="alldata">
            <p >{{alldata.row.studentName}}</p>
          </template>
        </el-table-column>

        <el-table-column
          prop="grade"
          label="年级" :resizable="false">
          <template slot-scope="alldata">
            <p >{{alldata.row.grade}}</p>
          </template>
        </el-table-column>

        <el-table-column
          prop="gender"
          label="性别" :resizable="false">
          <template slot-scope="alldata">
            <p >{{alldata.row.gender==1 ? '男' : '女'}}</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="phone"
          label="联系方式" :resizable="false">
          <template slot-scope="alldata">
            <p >{{alldata.row.phone}}</p>
          </template>
        </el-table-column>

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
    <!--新增或者编辑学生-->
    <el-dialog :title="dislogTitle" :visible.sync="dialogFormVisible"
               width="45%" id="editDialog">
      <el-form :model="form"  ref="form" >
        <el-form-item label="学号" :label-width="formLabelWidth" prop="studentNo">
          <el-input v-model="form.studentNo" placeholder="输入学号"
                    :disabled="Isdisabled"
                    @keyup.native.enter="addViseble"></el-input>
        </el-form-item>
        <el-form-item label="姓名" :label-width="formLabelWidth" prop="studentName">
          <el-input v-model="form.studentName"
                    placeholder="输入姓名"
                    @keyup.native.enter="addViseble"></el-input>
        </el-form-item>
        <el-form-item label="年级" :label-width="formLabelWidth" prop="studentName">
          <!--<el-input v-model="form.grade" placeholder="" ></el-input>-->

            <el-select  v-model="education" placeholder="请选择学历"
                        style="float: left;width: 49%"
                        @keyup.native.enter="addViseble">
              <el-option label="本科" value="本科"></el-option>
              <el-option label="硕士" value="硕士"></el-option>
              <el-option label="博士" value="博士"></el-option>
              <el-option label="专科" value="专科"></el-option>
              <el-option label="高职" value="高职"></el-option>
            </el-select>


            <el-date-picker style="float: left;width: 50%"
              v-model="selectTime"
              type="year" :editable="false" value-format="yyyy"
              :placeholder="selectPlaceOlder"
              @keyup.native.enter="addViseble">
            </el-date-picker>

        </el-form-item>


       <!-- <el-form-item label="性别" :label-width="formLabelWidth" prop="gender">
          <el-select  v-model="form.gender"
                      placeholder="请选择性别"
                      @keyup.native.enter="addViseble">
            <el-option label="男" value="1"></el-option>
            <el-option label="女" value="2"></el-option>
          </el-select>
        </el-form-item>-->


        <el-form-item label="性       别" :label-width="formLabelWidth" prop="gender">
          <el-select v-model="form.gender" placeholder="请选择性别" style="width: 100%">
            <el-option label="男" :value="1"></el-option>
            <el-option label="女" :value="2"></el-option>
          </el-select>
        </el-form-item>



        <el-form-item label="联系方式" :label-width="formLabelWidth" prop="studentName">
          <el-input v-model="form.phone"
                    placeholder="邮箱或者手机号码"
                    @keyup.native.enter="addViseble"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addViseble">保存</el-button>
        <el-button @click="dialogFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <!--导入文件-->
    <el-dialog
      id="studentDialog"
      title="学生导入"
      :visible.sync="importVisible"
      width="50%"
      @close="xStudentImportDialog()"
      v-if="importVisible" style="margin-top: -5vh">
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
        <el-button size="small" type="primary" slot="trigger">选择文件</el-button>
        <div slot="tip" class="el-upload__tip">可以支持xls和xlsx文件 小于20kb，表格样式请参考样图<br/>
          <img src="../../../static/images/studentImport.png"/>
        </div>
      </el-upload>
      <el-table :data="errorData"
                v-if="errorData.length>0"
                header-row-class-name="column-left"
                max-height="160px" style="overflow-y: auto">
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
      </el-table>
      <span slot="footer" class="dialog-footer">
    <el-button @click="closeImportDialog()">关闭</el-button>
    <el-button type="primary" @click="submitUpload()">上传</el-button>
  </span>
    </el-dialog>
    <!--版权-->
    <div class="copyright">
      <p>版权信息  版权股所有   悦岚数据服务</p>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import {createStudent, delStudent, getAllStudentInfo, resetPWD, updateStudent} from '@/common/js/request';

  export default{
    data(){
      return{
        form: {
          studentNo:'',
          studentName:'',
          gender:'',
          grade:'',
          phone:''
        },
        listData:[],
        listDataTmp:[],
        listDataFilterTmp: [],
        dialogFormVisible: false,
        formLabelWidth: '80px',
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
        isAdd:true,
        Isdisabled:false,
        // addRules: {
        //   studentNo: [
        //     { required: true ,message:'请输入学号', trigger: 'blur'}
        //   ],
        //   studentName: [
        //     { required: true ,message:'请输入姓名', trigger: 'blur'}
        //   ],
        //   gender: [
        //     { required: true,message:'请选择性别', trigger: 'blur' }
        //   ],
        //   grade: [
        //     { required: true,message:'请输入年级', trigger: 'blur' }
        //   ],
        //   phone: [
        //     { required: true,message:'请输入联系方式', trigger: 'blur' }
        //   ]
        // },
        multipleSelection:[],
        loading:false,
        dislogTitle:'',
        selectTime:'',
        education:'',
        selectPlaceOlder:'请选择学年'
      }
    },
    created() {
      this.batchUrl="/admin/student/batchCreation";
      this._getCustomers();
    },
    activated() {
      this.batchUrl="/admin/student/batchCreation";
      this._getCustomers();
      this.searchQuery=""
    },
   methods:{
     _Refresh:function () {
       this.searchQuery="";
       this._getCustomers();
       // 点击刷新默认显示每页5条数据
       this.p_pageSize=5
     },
     // 获取数据
     _getCustomers: function() {
       if(this.searchQuery.length>45){
         this.$message({
           type:'error',
           message:'搜索长度不能超过45个字符'
         })
         return;
       }

       // 点击搜索，默认显示每页5条数据
       this.p_pageSize=5

       getAllStudentInfo().then(res=>{
         if(res.data=="alreadyResetPassword"){
           this.$message({
             type:'warning',
             message:'请重新登录'
           });
           return;
         }
         this.listDataTmp=res.data.data.studentList;
         this.listDataTmp.forEach((value) =>{
           // console.log("年级为:"+typeof value.grade)
           //判断年级不为空
           if(typeof value.grade !="object"){
             // if(form.teacherContact.indexOf("@") != -1){
             //判断年级后缀没有“级”
             if(value.grade.indexOf("级") ==-1){
               value.grade = value.grade +"级"
             }
           }

         })
         this._filterData();
         this._initPageInfo();
       })
     },
     _validateForm: function (form) {
       // 判断编号不能为空
       let errorMsg = null
       if (!form.studentNo || form.studentNo.trim().length === 0) {
         errorMsg = '学号不能为空'
       }
       else if (form.studentNo.trim().length > 45) {
         errorMsg = '学号长度不能超过45'
       }else if((/^\s+/).test(form.studentNo)){
         errorMsg = '开头不能输入空格'
       }else if((/\s+$/).test(form.studentNo)){
         errorMsg = '结尾不能输入空格'
       }
       else {
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
         errorMsg = '姓名不能为空'
       } else if (form.studentName.trim().length > 45) {
         errorMsg = '姓名长度不能超过45'
       }else if((/^\s+/).test(form.studentName)){
         errorMsg = '开头不能输入空格'
       }else if((/\s+$/).test(form.studentName)){
         errorMsg = '结尾不能输入空格'
       }
       if (errorMsg && errorMsg.trim().length > 0) {
         this.$message.error(errorMsg)
         return false
       }
       //判断年级字符
       if(!this.education || this.education.trim().length==0){
          errorMsg='年级不能为空';
       }
       if (errorMsg && errorMsg.trim().length > 0) {
         this.$message.error(errorMsg)
         return false
       }
       if(this.selectTime==undefined || !this.selectTime || this.selectTime.length==0){
         errorMsg='学年不能为空';
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
      //判断联系方式字符
      //  var email=/^[A-Za-z0-9\u4e00-\u9fa5\.]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
       var email=/^[A-Za-z0-9\u4e00-\u9fa5+\.+\-+\_]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
       var phone= /^1\d{10}$/;
       var kong=/^\S+$/;
       if(!form.phone || form.phone.trim().length === 0){
         errorMsg="警告哦，联系方式不能为空"
       }else if(!kong.test(form.phone)){
         errorMsg="警告哦，联系方式不能输入空格"
       } else if (form.phone.trim().length > 45) {
         errorMsg="警告哦，联系方式长度不能超过45"
       }else{
         if(email.test(form.phone) ||  phone.test(form.phone)){
           return true
         }
         else{
           if(form.phone.indexOf("@") != -1){
             if(!email.test(form.phone)){
               errorMsg="请输入正确的手机号或者邮箱号"
             }
           }
           if(!phone.test(form.phone)){
             errorMsg="请输入正确的手机号或者邮箱号"
           }
         }
       }
       if (errorMsg && errorMsg.trim().length > 0) {
         this.$message.error(errorMsg)
         return false
       }
       return true
     },
     xStudentImportDialog: function(){
       this.importVisible = false;
       this._getCustomers();
     },
     select: function () {
       if(this.searchQuery.length>45){
         this.$message({
           type:'error',
           message:'搜索长度不能超过45个字符'
         })
         return;
       }
       this._filterData();
       this._initPageInfo();
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
           // (value.id && (value.id+'').indexOf(this.searchQuery) >= 0) ||
           (genderName && genderName.indexOf(this.searchQuery) >= 0) ||
           (value.studentNo && value.studentNo.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0) ||
           (value.studentName && value.studentName.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0)||
           (value.grade && value.grade.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0)||
           (value.phone && value.phone.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0)

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
     },
     // 选择数据
     handleSelectionChange(val) {
       this.multipleSelection = val;
     },
     // 关闭导入
     closeImportDialog(){
       this.importVisible=false;
       this._Refresh();
       //this._getCustomers();
     },
     submitUpload(){
         //上传
       this.$refs.upload.submit();
     },
      // 导入
     ImportStudent(){
       this.importVisible=true;
        this.errorData=[]
     },
     handleExceed(files, fileList) {
       this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
     },
     beforeUploadHandler:function(file){
       //判断文件格式是否正确
       var fileName = file.name;
       if(fileName.lastIndexOf(".xls")<0 && fileName.lastIndexOf(".xlsx")<0 ){
         this.$message({
           title:"提示",
           message:"只支持上传xls和xlsx文件",
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
            this.$message({
              type:'success',
              message:'导入成功!'
            });
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
         this._Refresh();
         //this._getCustomers();
       }else {
         this.importVisible = false;
         this.fileList=[];
         this.$message({
           title:"提示",
           message:response.message,
           type:"error"
         });
         this._Refresh();
       }
     },
     // 新增学生
     addStudentDialog(){
       //添加学生对话框
       this.dislogTitle="新增学生"
       this.form = {};
       this.dialogFormVisible = true;
       this.isAdd=true;
       this.Isdisabled=false;
       this.education="";
       this.selectTime="";
     },
     // 编辑
     updateDialog(){
       if(this.multipleSelection.length==0){
         this.$message({
           message: '警告哦，只能编辑单行数据',
           type: 'warning'
         });
         return;
       } else if(this.multipleSelection.length!=1){
         this.$message({
           message: '警告哦，只能编辑单行数据',
           type: 'warning'
         });
         return;
       }else{
         this.dislogTitle="编辑学生";
        // var selectDate=this.multipleSelection[0];
/*
         selectDate.studentNo=selectDate.studentNo;
         selectDate.studentName=selectDate.studentName;
         selectDate.grade=selectDate.grade;
         selectDate.gender=selectDate.gender;
         selectDate.phone=selectDate.phone;*/
         if(this.multipleSelection[0].gender==1){
           this.form.gender=this.multipleSelection[0].gender+"";
         }else if(this.multipleSelection[0].gender==2){
           this.form.gender=this.multipleSelection[0].gender+"";
         }
         this.dialogFormVisible = true;
         if(this.multipleSelection[0].grade!=null && this.multipleSelection[0].grade!=""){
           var grade=this.multipleSelection[0].grade.substr(0,2);
           var year=this.multipleSelection[0].grade.substr(2,this.multipleSelection[0].grade.length);
            this.education=grade;
            this.selectTime=year;
         }
         console.log(this.multipleSelection[0].gender);
         Object.assign(this.form,this.multipleSelection[0]);
         this.isAdd=false;
         this.Isdisabled=true;
       }

     },
     // 保存按钮
     addViseble: function () {
       if (!this._validateForm(this.form)) {
         return;
       }
       if(this.isAdd){
         //新增
         this.form.grade=this.education+this.selectTime;
         //console.log(this.form.grade);
         createStudent(this.form).then(res=>{
           console.log(res);
           if (res.data.errorCode === 0) {
             this.$message({
               type: 'success',
               message: '新增学生成功!'
             });
             this._Refresh();
             //this._getCustomers();
           } /* else if(res.data.errorCode === 65){
             this.$message({
               type: 'error',
               message: res.data.data.message
             });
             this._Refresh();
           }else if(res.data.errorCode === 28){
             this.$message({
               type: 'error',
               message: res.data.data.message
             });
             this._Refresh();
           }*/
           else {
             console.log("校验提示语:"+res.data.errorCode)
             this.$message({
               type: 'error',
               message: res.data.message
             });
             this._Refresh();
           }
           this.dialogFormVisible = false;
         });
       }else{
         //修改
         this.form.grade=this.education+this.selectTime;
         // console.log("性别为:"+typeof this.form.gender)
         // if(this.form.gender=="男"){
         //   this.form.gender=1
         // }else{
         //   this.form.gender=2
         // }
         updateStudent(this.form).then(res=>{
           if (res.data.errorCode === 0) {
             this.$message({
               type: 'success',
               message: '修改学生成功!'
             });
             this._Refresh();
            // this._getCustomers();
           } else {
             this.$message({
               type: 'error',
               message: res.data.message
             });
             this._Refresh();
           }
           this.dialogFormVisible = false;
         });

       }
     },
     // 重置密码
     resetMiMa(){
       console.log(this.multipleSelection)
       if(this.multipleSelection.length==0){
         this.$message({
           message: '警告哦，请至少选择一行数据',
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
                 this._Refresh();
               }
             });
           }
         }).catch(() => {
         });
       }
     },
     // 删除
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
             delStudent(item.studentNo).then(res => {
               console.log(res);
               if(res.status==200){
                 if(res.data.errorCode ==0){
                   if(idx==this.multipleSelection.length-1) {
                     this.$message({
                       type: 'success',
                       message: '删除成功!'
                     });
                     this._Refresh();
                     //this._getCustomers();
                   }
                 } else {
                   // 删除提示语
                   this.$message({
                     type: 'error',
                     message: res.data.message
                   });
                   this._Refresh();
                 }
               }
             })
           }
         }).catch(() => {
         });
       }

     },
     handleCurrentChange(val){
       this.p_currentPage = val;
       this.listData = this.listDataFilterTmp.slice((val - 1) * this.p_pageSize, (val) * this.p_pageSize)
     },
     handleSizeChange(val){
       this.p_pageSize = val;
       this._initPageInfo()
     }
   },
    beforeRouteEnter: (to, from, next) => {
      console.log("我是路由，我进入了");
      next(vm => {
        vm.p_currentPage=1
        vm.handleSizeChange(5)
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

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>

  .column-left th > .cell
    text-align left !important
  .student
    width 100%
    h1
      font-size 22px
      margin 20px 3% 0px
      font-family  "Microsoft YaHei"
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
  #btn-header .el-form-item:not(:nth-child(1)){
    float: right;
  }
  .el-table--border th .cell {
    text-align: center;
    font-weight: bolder;
    font-size: 1.1em;
  }
  #editDialog .el-form-item{
    width: 80%;
    margin: 0px auto 20px;
  }
  /*弹出框表单名称*/
  #editDialog .el-form-item__label{
    text-align: left;
  }
  /*studentDialog*/
  /*去除导入学生表格多余的线条*/
  #studentDialog .el-table--group::after,
  #studentDialog .el-table--border::after,
  #studentDialog .el-table::before {
    background-color: transparent;
  }
</style>
