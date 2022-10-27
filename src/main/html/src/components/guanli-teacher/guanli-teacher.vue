<template>
    <div style="width: 100%;" id="teacher">
      <h1 id="title">教师管理</h1>
      <el-form :inline="true" id="btn-header"  class="demo-form-inline" @submit.native.prevent>
        <el-form-item>
          <el-button class="button-green" @click="_Refresh()">刷新</el-button>
          <el-button class="button-green"  @click="importTeacher">导入教师</el-button>
          <el-button class="button-green" @click="addTeacher()">新增教师</el-button>
          <el-button class="button-green" @click="bianjiUser()">编辑</el-button>
          <el-button class="button-green" type="reset" @click="checkModifyPass">重置密码</el-button>
          <el-button class="button-green"  @click="deletedata()">删除</el-button>
        </el-form-item>
        <el-form-item style="margin-right: 0px">
          <el-button class="button-green" @click="_getCustomers()">搜索</el-button>
        </el-form-item>
        <!--<el-form-item>-->
          <!--&lt;!&ndash;<el-button type="button" @click="select()">查询</el-button>&ndash;&gt;-->
        <!--</el-form-item>-->
        <el-form-item >
          <el-input v-model="searchQuery" placeholder="搜索"
                    @keyup.native.enter="select()"></el-input>
        </el-form-item>
      </el-form>

      <!--表格-->
      <div id="Table">
        <template>
          <el-table
            id="teacherTable"
            :data="all"
            :border="true"
            center
            tooltip-effect="dark"
            highlight-current-row
            style="width: 100%;text-align: center;font-weight: bolder;"
            header-row-class-name="column-center"
            @selection-change="handleSelectionChange">
            <el-table-column
              type="selection"
              min-width="140" >
            </el-table-column>

            <el-table-column prop="id" label="工号" min-width="140" :resizable="false">
              <template slot-scope="alldata">
                <p >{{ alldata.row.teacherNo }}</p>
              </template>
            </el-table-column>

            <el-table-column prop="teacherName"
                             label="教师名称"
                             min-width="140"
                             :resizable="false"
                             show-overflow-tooltip>
              <template slot-scope="alldata">
                <p>{{ alldata.row.teacherName }}</p>
              </template>
            </el-table-column>

            <el-table-column
              prop="gender" label="性别" min-width="140" :resizable="false">
              <template slot-scope="alldata">
                <p> {{ alldata.row.gender}}</p>
              </template>
            </el-table-column>
            <el-table-column
              prop="teacherTitle" label="职称" min-width="140" :resizable="false">
              <template slot-scope="alldata">
                <p>{{ alldata.row.teacherTitle }}</p>
              </template>
            </el-table-column>

            <el-table-column
              prop="teacherContact" label="联系方式" min-width="140" :resizable="false"
              >

            <template slot-scope="alldata">
              <p>{{ alldata.row.teacherContact }}</p>
            </template>
            </el-table-column>
          </el-table>
        </template>
      </div>
      <!--导入-->
      <el-dialog id="teacherDialog"
        title="教师导入"
        :visible.sync="importVisible"
        width="45%"  v-if="importVisible"
        style="margin-top: -5vh">
        <el-upload
          class="upload-demo"
          ref="upload"
          :action="actionUrl"
          :auto-upload="false"
          :on-success="uploadSuccess"
          :headers="uploadHeaders"
          :before-upload="beforeUploadHandler"
           accept=".xls,.xlsx"
          :file-list="filelist"
          :limit="1"
          :on-exceed="handleExceed"
          v-loading="loading">
          <el-button size="small" type="primary">选择文件</el-button>
          <div slot="tip" class="el-upload__tip">文件支持xls和xlsx 格式，小于20kb，表格样式请参考样图<br/>
          <img src="../../../static/images/teacherImport.png"/>
          </div>

        </el-upload>
        <el-table :data="errorData"
                  v-if="errorData.length>0"
                  header-row-class-name="column-left"
                  id="improtTable"
                  max-height="160px" style="width: 100%;
                  overflow-y: auto;text-align: center">
          <el-table-column
            prop="teacherNum"
            label="工号">
          </el-table-column>
          <el-table-column
            prop="teacherName"
            label="姓名">
          </el-table-column>
          <el-table-column
            prop="reason"
            label="提示">
          </el-table-column>
        </el-table>
        <span slot="footer" class="dialog-footer">
    <el-button @click="importVisible = false">取 消</el-button>
    <el-button class="button-green" @click="upload()">上传</el-button>
  </span>
      </el-dialog>
      <!--新增-->
      <el-dialog title="新增教师"
                 :visible.sync="dialogFormVisible"
                 width="40%" id="addDialog">
        <el-form :model="form" >
          <el-form-item label="工号" :label-width="formLabelWidth" prop="teacherNo">
            <el-input v-model="form.teacherNo" placeholder="教师工号" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="姓名" :label-width="formLabelWidth" prop="teacherName">
            <el-input v-model="form.teacherName" placeholder="教师姓名" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="职称" :label-width="formLabelWidth" prop="teacherTitleId">
            <el-select v-model="form.teacherTitleId" placeholder="请选择职称" style="width: 100%">
              <el-option label="教授" value="1"></el-option>
              <el-option label="副教授" value="2"></el-option>
              <el-option label="讲师" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="性别" :label-width="formLabelWidth" prop="gender">
            <el-select v-model="form.gender" placeholder="请选择性别" style="width: 100%">
              <el-option label="男" value="1"></el-option>
              <el-option label="女" value="2"></el-option>

            </el-select>
          </el-form-item>
          <el-form-item label="联系方式" :label-width="formLabelWidth"  prop="teacherContact">
            <el-input v-model="form.teacherContact" placeholder="请输入联系方式" auto-complete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button class="button-green" @click="createUser()">保存</el-button>
        </div>
      </el-dialog>
      <!--编辑-->
      <el-dialog title="编辑教师"
                 :visible.sync="bianjiFormVisible"
                 width="40%" id="updateDialog">
        <el-form :model="bianjiForm" >
          <el-form-item label="工号" :label-width="formLabelWidth" prop="teacherNo" >
            <el-input v-model="bianjiForm.teacherNo" placeholder="教师工号" disabled auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="姓名" :label-width="formLabelWidth" prop="teacherName">
            <el-input v-model="bianjiForm.teacherName" placeholder="教师姓名" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="职称" :label-width="formLabelWidth" prop="teacherTitleId">
            <el-select v-model="bianjiForm.teacherTitleId" placeholder="请选择职称" style="width: 100%">
              <el-option label="教授" value="1"></el-option>
              <el-option label="副教授" value="2"></el-option>
              <el-option label="讲师" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="性别" :label-width="formLabelWidth" prop="gender">
            <el-select v-model="bianjiForm.gender" placeholder="请选择性别" style="width: 100%">
              <el-option label="男" value="1"></el-option>
              <el-option label="女" value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="联系方式" :label-width="formLabelWidth" prop="teacherContact">
            <el-input v-model="bianjiForm.teacherContact" placeholder="请输入联系方式" auto-complete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="bianjiFormVisible = false">取 消</el-button>
          <el-button class="button-green" @click="bianjiUser2()">保存</el-button>
        </div>
      </el-dialog>
      <!--重置密码-->
      <el-dialog
        :visible.sync="resetVisible"
        width="30%" style="text-align: center;">
        <i class="el-icon-warning" style="font-size: 70px;;color: red"></i>
        <h1 style="font-size: 24px;text-align: center;line-height: 60px;">确定要重置吗？</h1>
        <p style="line-height: 40px;font-size: 15px">
          编号为
        <div style="width: 100%;max-height: 100px;line-height: 30px;overflow-y: auto">[{{resetNo}}]</div>
        教师密码将被重置为<span>[12345678]</span></p>
        <span slot="footer" class="dialog-footer">
          <el-button  @click="resetVisible = false">取 消</el-button>
          <el-button type="danger" @click="modifyPass">确 定</el-button>
        </span>
      </el-dialog>
      <!--删除-->
      <!--分页begin-->
      <div style="max-width:100%;overflow: auto">
        <div class="block" style="max-width:35%;margin:20px 20%;">
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
      <!--分页end-->
      <!--版权-->
      <div class="copyright">
        <p>版权信息  版权股所有   悦岚数据服务</p>
      </div>
    </div>
</template>

<script type="text/ecmascript-6">
  import {
    addTeacher,
    deleteTeacher,
    edu_url,
    getCustomers,
    getCustomersByPage,
    resetPWD,
    updateTeacher
  } from '@/common/js/request';

  export default {
    data() {
      return {
        // 当前显示的
        all: [
          {id: ''}, // 需要显示的数据
          {teacherName: ''}, // 姓名
          {teacherTitle: ''}, // 职称
          {gender: ''}, // 性别
          {teacherContact: ''} // 联系方式
        ],
        // 全部数据
        allTmp: [],
        // 当前数据，经过过滤的
        allCurTmp: null,
        // 添加所有教师
        form: {
          teacherNo: '',
          teacherName: '',
          teacherTitleId: '',
          gender: '',
          teacherContact: ''
        },
        // 编辑所有教师信息
        bianjiForm: {
          teacherNo: '',
          teacherName: '',
          teacherTitleId: '',
          gender: '',
          teacherContact: ''
        },
        importVisible: false,//
        dialogFormVisible: false,
        formLabelWidth: '80px',
        resetVisible: false, //重置密码
        deleteVisible: false, //删除
        multipleSelection: [], //已经选择的项
        bianjiFormVisible: false,// 编辑
        searchQuery: '', // 查询数据
        uploadHeaders: {
          'Authorization': 'Bearer ' + (localStorage['token'] ? localStorage['token'] : '')
        },
        resetNo: '',
        // 分页参数
        p_currentPage: 1,
        p_pageSizes: [5, 10, 15, 20, 30, 50, 100],
        p_pageSize: 5,
        p_total: 0,
        actionUrl:null,
        filelist:[],
        errorData:[],
        // addRules :{
        //   teacherNo :[{ required: true,message: '请输入工号', trigger: 'blur' }],
        //   teacherName :[{ required: true,message: '请输入教师姓名', trigger: 'blur' }],
        //   teacherTitleId :[{ required: true,message: '请选择教师职称', trigger: 'blur' }],
        //   gender :[{ required: true,message: '请选择教师性别', trigger: 'blur' }],
        //   teacherContact :[{ required: true,message: '请输入教师联系方式', trigger: 'blur' }]
        // },
        // editRules:{
        //   teacherNo :[{ required: true,message: '请输入工号', trigger: 'blur' }],
        //   teacherName :[{ required: true,message: '请输入教师姓名', trigger: 'blur' }],
        //   teacherTitleId :[{ required: true,message: '请选择教师职称', trigger: 'blur' }],
        //   gender :[{ required: true,message: '请选择教师性别', trigger: 'blur' }],
        //   teacherContact :[{ required: true,message: '请输入教师联系方式', trigger: 'blur' }]
        // },
        loading:false
      }
    },
    created() {
      // "http://192.168.137.185:8080/admin/teacher/batchCreation"
      this.actionUrl = "/admin/teacher/batchCreation";
      //this._getTeacherByLimit();


    },
    activated () {
      this.searchQuery="";
      this._getCustomers();
    },
    methods: {
      //点击刷新按钮
      _Refresh:function () {
        this.searchQuery="";
        this._getCustomers();
        // 点击刷新默认显示每页5条数据
        this.p_pageSize=5
      },
      // 获取数据
      _getCustomers: function () {
        if(this.searchQuery.length>45){
          this.$message({
            type:'error',
            message:'搜索长度不能超过45个字符'
          });
          return;
        }
        // 点击搜索，默认显示每页5条数据
        this.p_pageSize=5
        getCustomers().then(res => {
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          this.allTmp = res.data.data.teacherInfoList;
          this._filterData()
          this._initPageInfo()
        })
      },
      // 初始化分页信息
      _initPageInfo: function () {
        this.p_total = this.allCurTmp.length
        this.p_currentPage = 1
        this.all = this.allCurTmp.slice(0, this.p_pageSize)
      },
      // filter data
      _filterData: function () {
        if (this.searchQuery === '') {
          this.allCurTmp = this.allTmp
          return
        }
        let filterTableDataEnd = []
        this.allTmp.forEach((value, index) => {
          if (
              (value.teacherName && value.teacherName.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0) ||
              (value.teacherNo && value.teacherNo.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0) ||
              (value.teacherTitle && value.teacherTitle.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0) ||
              (value.gender && value.gender.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0) ||
              (value.teacherContact && value.teacherContact.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0)
          ) {
            filterTableDataEnd.push(value)
          }
        });
        this.allCurTmp = filterTableDataEnd
      },
      // 改变分页
      handleSizeChange(val) {
        this.p_pageSize = val
        this._initPageInfo()
        console.log("页数为:"+ val)
      },
      handleCurrentChange(val) {
        this.p_currentPage = val
        this.all = this.allCurTmp.slice((val - 1) * this.p_pageSize, (val) * this.p_pageSize)
      },
      // 根据查询条件查询所有数据
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
      addTeacher: function () {
        this.form = {};
        this.dialogFormVisible = true;
      },
      checkModifyPass:function(){
        console.log(this.multipleSelection.length);
        if (this.multipleSelection.length === 0){
          this.$message({
            message: '警告哦，请至少选择一行数据',
            type: 'warning'
          });
        } else {
          console.log(this.multipleSelection)
          this.$confirm('是否重置密码, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            // for(let [idx,item] of this.multipleSelection.entries()){
            //   let userId=item.id;
            //   resetPWD(userId).then(res=>{
            //     if(res.data.errorCode==0){
            //       this.$message({
            //         type: 'success',
            //         message: '重置成功!  重置密码为12345678'
            //       });
            //       this._Refresh();
            //     }
            //   });
            // }
            // this.resetVisible = true;
            this.modifyPass()
            let restNoTmp = ''
            for (let [idx, item] of this.multipleSelection.entries()) {

              restNoTmp += ',' + item.teacherNo;
            }
            this.resetNo = restNoTmp.substr(1)
          }).catch(() => {
          });



        }
      },
      modifyPass:function(){
        console.log(this.multipleSelection.length);
        //重置密码
        var mulit=JSON.parse(JSON.stringify(this.multipleSelection));
        this._reSetPwd(mulit);
  /*      for(let[idx,item] of this.multipleSelection.entries()){
          //重置密码
          resetPWD(item.id).then(res=>{
            if(res.data.errorCode == 0){
              if(idx==this.multipleSelection.length){
                this.$message({
                  title:"提示",
                  message:"重置密码成功",
                  type:"success"
                });
              }
            }else {
              this.$message({
                type: 'error',
                message: res.data.message
              });
            }
          });
          this._getCustomers();
          this.resetVisible = false;

        }*/
      },
      _reSetPwd:function (mulit) {
        resetPWD(mulit.splice(-1)[0].id).then(res=>{
          if(res.data.errorCode!=0){
            this.$message({
              type: 'error',
              message: res.data.message
            });
            this._Refresh();
          } else {
            if(mulit.length==0) {
              this.$message({
                title:"提示",
                message:"重置成功!  重置密码为12345678",
                type:"success"
              });
              this._Refresh();
             // this._getCustomers();
              this.resetVisible = false;
            }
            if(mulit.length==0){
              return;
            }
            this._reSetPwd(mulit);
          }
        });
      },

      // 选择的数据
      handleSelectionChange(val) {
        //深度拷贝对象
        this.multipleSelection = JSON.parse(JSON.stringify(val));
        //Object.assign(this.multipleSelection,val);
        this.multipleSelection.map(item=>{
          if(item.teacherTitle=="教授"){
            item.teacherTitleId="1";
          } else if(item.teacherTitle=="副教授"){
            item.teacherTitleId="2";
          } else if(item.teacherTitle=="讲师"){
            item.teacherTitleId="3";
          }
          if(item.gender == '男') {
            item.gender = "1";
          }else if(item.gender == '女'){
            item.gender = "2";
          }
        });
      },
      // 添加数据
      createUser: function () {
        if (!this._validateForm(this.form)) {
          return
        }
        addTeacher(this.form).then(res => {
          //console.log(res)
          if (res.data.errorCode === 0) {
            this.$message({
              type: 'success',
              message: '新增教师成功!'
            });
            this._Refresh();
            //this._getCustomers();

          } else {
            this.$message({
              type: 'error',
              message: res.data.message
            });
            this._Refresh();
          }
          this.dialogFormVisible = false;
        });


      },
      // 删除数据
      deletedata:function () {
        if(this.multipleSelection.length==0){
          // alert("请选择数据")
          this.$message({
            message: '警告哦，至少选择一行数据',
            type: 'warning'
          });
        }
        else{
          this.$confirm('此操作将永久删除选中的教师数据, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
          }).then(() => {
            for(let [idx,item] of this.multipleSelection.entries()){
              //console.log(item.id);
              deleteTeacher(item.id).then(res => {
                console.log(res);
                if(res.status==200){
                  if(res.data.errorCode!=0){
                    // 删除提示语
                    this.$message({
                      type: 'error',
                      message: res.data.message
                    });
                    this._Refresh();
                  } else {
                    if(idx==this.multipleSelection.length-1) {
                      this.$message({
                        type: 'success',
                        message: '删除成功!'
                      });
                      this._Refresh();
                    }
                  }
                }
              })
            }
          }).catch(() => {
          });
        }
      },
      // 点击按钮，弹出框获取数据
      bianjiUser:function () {
        if (this.multipleSelection.length === 1) {
          // 开启窗口
          this.bianjiFormVisible= true;
          // 弹出框获取数据
          Object.assign(this.bianjiForm,this.multipleSelection[0]);
        }
        else {
          // alert("抱歉,只能修改单行代码")
          this.$message({
            message: '警告哦，只能编辑单行数据',
            type: 'warning'
          });
        }
      },
      _validateForm: function (form) {
        // 判断教师编号不能为空
        let errorMsg = null


        if (!form.teacherNo || form.teacherNo.trim().length === 0) {
          errorMsg = '教师工号不能为空'
        } else if (form.teacherNo.trim().length > 45) {
          errorMsg = '教师工号长度不能超过45'
        }else if((/^\s+/).test(form.teacherNo)){
          errorMsg = '开头不能输入空格'
        }else if((/\s+$/).test(form.teacherNo)){
          errorMsg = '结尾不能输入空格'
        } else {
         // var regex = /^[0-9]+$/;
          var regex = /^\+?[0-9]\d*$/;
          if(!regex.test(form.teacherNo.trim())){
            errorMsg = "教师工号只能为大于或等于0的整数";
          }
        }
        if (errorMsg && errorMsg.trim().length > 0) {
          this.$message.error(errorMsg)
          return false
        }
        // 判断教师名称不能为空
        if (!form.teacherName || form.teacherName.trim().length === 0) {
          errorMsg = '教师名称不能为空'
        } else if (form.teacherName.trim().length > 45) {
          errorMsg = '教师名称长度不能超过45'
        }else if((/^\s+/).test(form.teacherName)){
          errorMsg = '开头不能输入空格'
        }else if((/\s+$/).test(form.teacherName)){
          errorMsg = '结尾不能输入空格'
        }
        if (errorMsg && errorMsg.trim().length > 0) {
          this.$message.error(errorMsg)
          return false
        }
        // 判断教师职称不能为空
        if (!form.teacherTitleId) {
          errorMsg = '教师职称不能为空'
        }
        if (errorMsg && errorMsg.trim().length > 0) {
          this.$message.error(errorMsg)
          return false
        }
        // 判断教师性别不能为空
        if (!form.gender) {
          errorMsg = '教师性别不能为空'
        }
        if (errorMsg && errorMsg.trim().length > 0) {
          this.$message.error(errorMsg)
          return false
        }
        // 联系方式
        // var email=/^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,})$/;
        var email=/^[A-Za-z0-9\u4e00-\u9fa5+\.+\-+\_]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
        var phone= /^1\d{10}$/;
        var kong=/^\S+$/;
        if(!form.teacherContact || form.teacherContact.trim().length === 0){
          errorMsg="警告哦，联系方式不能为空"
        }else if(!kong.test(form.teacherContact)){
          errorMsg="警告哦，联系方式不能输入空格"
        } else if (form.teacherContact.trim().length > 45) {
          errorMsg="警告哦，联系方式长度不能超过45"
        }else{
          if(email.test(form.teacherContact) ||  phone.test(form.teacherContact)){
            return true
          }
          else{
            if(form.teacherContact.indexOf("@") != -1){
              if(!email.test(form.teacherContact)){
                errorMsg="请输入正确的手机号或者邮箱号"
              }
            }
             if(!phone.test(form.teacherContact)){
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
      // 点击弹出框保存修改数据
      bianjiUser2: function () {
        if (!this._validateForm(this.bianjiForm)) {
          return
        }

        updateTeacher(this.bianjiForm.gender,this.bianjiForm.teacherContact,
          this.bianjiForm.teacherName,this.bianjiForm.teacherNo,
         this.bianjiForm.teacherTitleId,this.bianjiForm.id).then(res => {
          console.log(this.bianjiForm)
           console.log(res)
          if (res.status === 200) {
             if(res.data.errorCode == 12){
               this.$message({
                 type: 'error',
                 message: "教师工号已存在"
               });
             } else if (res.data.errorCode !== 0 && res.data.errorCode !== 12) {
              // 更新提示语
              this.$message({
                type: 'error',
                //message: "教师编号已存在"
                message: res.data.message
              });
               this._Refresh();
            }
            else {
              this.bianjiFormVisible= false;
              //this._getCustomers();
               this._Refresh();
              // 更新提示语
              this.$message({
                type: 'success',
                message: '更新成功!'
              });
            }
          }
        })
      },
      // 上传文件
      upload: function () {

        this.uploadHeaders = {
          'Authorization': 'Bearer ' + (localStorage['token'] ? localStorage['token'] : '')
        };
        this.$refs.upload.submit();
      },
      importTeacher:function(){
        //清除之前的文件列表
        this.fileList=[];
        this.errorData=[];
        this.importVisible = true;
        this.loading=false;
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
    /*  beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },*/
      beforeUploadHandler:function(file){
        var fileName = file.name;
        if(fileName.lastIndexOf(".xls")<0 && fileName.lastIndexOf(".xlsx")<0){
          this.$message({
            title:"提示",
            message:"只支持上传xls和xlsx文件",
            type:"error"
          });
          this._Refresh();
          this.loading=false;
          return false;
        }
        console.log("文件大小"+file.size);
        if(file.size>1024*21){
          this.$message({
            title:"提示",
            message:"文件超过20KB",
            type:"error"
          });
          this._Refresh();
          this.loading=false;
          return false;
        } else {
          this.loading = true;
          return true;
        }
      },
      uploadSuccess: function (msg) {
        console.log(msg)
        console.log(msg+"返回结果"+msg.errorCode);
        this.loading = false;
        if(msg.errorCode==0){
          this.$refs.upload.clearFiles();
          if(msg.data.failureReasonList.length==0){
            //直接关闭上传的框
            this.importVisible = false;
            this.$message({
              type:'success',
              message:'导入成功'
            });
          } else {
            console.log("未关闭")
            this.errorData = msg.data.failureReasonList;
             console.log(this.errorData)
        /*    this.errorData.forEach((value, index) => {
              console.log(value)
              var regex = /^\+?[0-9]\d*$/;
              if(!regex.test(value.teacherNum.trim())){
                // errorMsg = "教师编号只能为大于或等于0的整数";
                value.reason="教师工号只能为大于或等于0的整数"
              }else if(value.teacherNum.trim().length >45){
                value.reason="教师工号长度不能超过45"
              }else if(value.teacherName.trim().length >45){
                value.reason="教师姓名长度不能超过45"
              }else{
                value.reason="导入成功"
              }
            })*/
          }
          this._Refresh();
        }
        if(msg.errorCode==51){
              this.importVisible = false;
              this.fileList=[];
              this.errorData=[];
              this.$message({
                title:"提示",
                message:msg.message,
                type:"error"
              });
          this._Refresh();
          this.loading=false;
            }
        //this._getCustomers()
        this._Refresh();
      }
    },
    beforeRouteEnter: (to, from, next) => {
      console.log("我是路由，我进入了");
      next(vm => {
        // vm._getExpirementInfo();
        // vm.handleSizeChange(5)
        vm.p_currentPage=1
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
  .column-left th > .cell
    text-align left !important
  #teacher
    #title
      font-size 22px
      margin 20px 3% 0px
      font-family  "Microsoft YaHei"
    .demo-form-inline
      width 94% !important
      margin 20px auto
      padding-top 20px
      border-top 1px solid #ebeef5
    /*#btn*/
      /*width 94%*/
      /*margin 0px auto 50px*/
      /*border-top 1px solid #ebeef5*/
      /*padding-top 20px*/


    #Table
      width 94% !important
      height auto
      margin 0px auto
      text-align center
    el-dialog
      width 30%
      padding 20px
    .copyright
      text-align right
      color #b4b4b4
      font-size 14px
      margin-top 50px
      margin-right 3%
  /*.el-table--group::after, .el-table--border::after, .el-table::before*/
    /*background-color #ffffff*/
  .el-button--primary
    background: linear-gradient(#c9dece 70%, #bbd0c0 30%);
    background-size: cover;
    transition: 2s background;
    border: 1px solid transparent;
    color: #494b4f;
  /*.el-table--group::after, .el-table--border::after, .el-table::before*/
    /*background-color: transparent;*/
  /*#teacherTable .el-table--group::after, .el-table--border::after, .el-table::before {*/
  /*background-color: #ebeef5;*/
  /*}*/

</style>
<style type="text/css" >
  #btn-header .el-form-item:not(:nth-child(1)){
    float: right;
  }
  th > .cell{
    text-align: center;font: 1.1em bolder;
  }
  /*.el-table--border th .cell {
    text-align: center;
    font-weight: bolder;
    font-size: 1.1em;
  }*/
  #Table .el-table .cell{
    white-space: nowrap;
    overflow: hidden;
    word-break:break-all;
    text-overflow: ellipsis;
    padding-right: 10px;
  }
  /*弹出框输入框宽度*/
    #addDialog .el-form-item,
    #updateDialog .el-form-item{
    width: 80%;
    margin: 0px auto 20px;
  }
    /*弹出框表单名称*/
    #addDialog .el-form-item__label,
    #updateDialog .el-form-item__label{
      text-align: left;
    }
  /*#teacherTable .el-table--group::after, .el-table--border::after, .el-table::before {*/
    /*background-color: #ebeef5;*/
  /*}*/
  /*去除导入教师表格多余的线条*/
  #teacherDialog .el-table--group::after,
  #teacherDialog .el-table--border::after,
  #teacherDialog .el-table::before {
    background-color: transparent;
  }
  .el-dialog__wrapper{
    overflow: hidden;
  }
  /*#teacherTable .el-table__empty-block{*/
    /*width: 100%;*/
    /*border-bottom: 1px solid #ebeef5;*/
    /*border-right: 1px solid #ebeef5;*/
  /*}*/
</style>

