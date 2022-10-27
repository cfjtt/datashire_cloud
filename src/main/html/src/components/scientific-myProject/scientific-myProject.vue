<template>
  <div style="padding: 5px;" id="myProject">
    <el-button type="primary" @click="creatProject" style="float: right">创建项目</el-button>
    <el-table
      id="tableData"
      :data="tableData"
      :default-sort = "{prop: 'id', order: 'descending'}"
      style="width: 100%">
      <el-table-column
        label="ID"
        prop="id"
        sortable>
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="项目名称">
        <template slot-scope="scope">
            <p>{{ scope.row.name }}</p>
        </template>
      </el-table-column>
      <el-table-column
        label="项目组长">
        <template slot-scope="scope">
          <p>{{scope.row.managerUser == null ? '' : scope.row.managerUser.name.concat("/").concat((scope.row.managerUser.role == 1 ? scope.row.managerUser.sno : scope.row.managerUser.tno)) }}</p>
        </template>
      </el-table-column>
      <el-table-column
        label="是否公开">
        <template slot-scope="scope">
          <p>{{ scope.row.isPublic == 0 ? '否' : '是' }}</p>
        </template>
      </el-table-column>
      <el-table-column
        label="状态">
        <template slot-scope="scope">
          <p>{{ scope.row.status ==0 ? '已完成' : '进行中' }}</p>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间">
        <template slot-scope="scope">
          <p>{{ scope.row.createTime }}</p>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="pushProject(scope.row.id)">进入</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--分页-->
    <div style="max-width:100%;overflow: auto">
      <div id="pagination" style="width:35%;margin:20px 20%">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page"
          :page-sizes="p_pageSizes"
          :page-size="offset"
          layout="total, sizes, prev, pager, next, jumper"
          :total="p_total">
        </el-pagination>
      </div>
    </div>
    <!--创建项目弹出框-->
    <el-dialog title="创建项目" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form" >
        <el-form-item label="项目名称" prop="name" :label-width="formLabelWidth">
          <el-input v-model="form.name" placeholder="请输入项目名称,限定45个字符" maxlength=45 auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="是否公开" prop="radio" :label-width="formLabelWidth">
          <el-checkbox v-model="form.isPublic" label="1">公开</el-checkbox>
        </el-form-item>
        <el-form-item label="项目简介" prop="brefIntro" :label-width="formLabelWidth">
          <el-input type="textarea" placeholder="请输入项目简介,限定600个字符" maxlength=600 :rows="4" v-model="form.brefIntro" ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click.prevent.stop="submitForm('form')" :disabled="isDisabled">提 交</el-button>
        <el-button @click="dialogFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script type="text/ecmascript-6">
  import {getMyProject,checkProjectNameIsExist,addProject} from '@/common/js/request';
  export default {
    data() {
      var checkNameExist=(rule,value,callback) =>{
        if(!value){
          return callback(new Error("名字不能为空"))
        }
        //项目中不能使用[]
        if(value.indexOf("[")>-1 || value.indexOf("]") >-1){
          return callback(new Error("项目名称不能包含[]"));
        }
        //校验项目是否存在
        checkProjectNameIsExist(value).then(res=>{
          if(res.status==200){
            if(res.data.errorCode==0){
              callback();
            } else {
              callback(new Error("该项目已经存在"));
            }
          }
        });
      };
      return {
        tableData: [],
        form: {
          name: null,
          brefIntro: null,
          isPublic:'0'
        },
        // 校验form表单
        rules: {
          name: [
            { required: true, message: '请输入项目名称', trigger: 'blur' },
            {max:45,message:'项目名称不能超过45个字符',trigger: 'blur'},
            { validator: checkNameExist, messahe:'该项目已经存在',trigger: 'blur' }
          ],
          brefIntro:[
            {max:600,message:'项目简介不能超过600个字符',trigger: 'blur'}
          ]
        },
        // 分页参数
        page: 1,
        p_pageSizes: [5, 10, 15, 20, 30, 50, 100],
        offset: 5,
        p_total: 0,
        dialogFormVisible: false,
        formLabelWidth: '80px',
        isDisabled:false

      }
    },
    methods: {
      // 进入页面
      pushProject(id){
        this.$router.push({ path :'/scientificCenter', query:{projectId: id}});
      },
      handleSizeChange(val) {
        this.page = 1;
        this.offset = val;
        var user = JSON.parse(localStorage['user']);
        this._getMyProject(user.userId);
      },
      handleCurrentChange(val) {
        this.page = val;
        var user = JSON.parse(localStorage['user']);
        this._getMyProject(user.userId);
      },
      // 创建项目
      creatProject(){
        this.form={
          name:null,
          isPublic:false,
          brefIntro:null
        };
        if(this.$refs.form !==undefined) {
          this.$refs.form.clearValidate()
        }
        //清空复选框
        // this.form.isPublic=1
        this.dialogFormVisible=true
      },
      _addProject(name,isPublic,brefIntro){
        this.isDisabled=true;
        var user = JSON.parse(localStorage['user']);
        addProject(name,isPublic,brefIntro,user.userId).then(res=>{
          console.log("-------创建项目------");
          if(res.data.errorCode==104){
            this.$message({
              type:"error",
              message:"项目名称不能为空"
            });
          } else if(res.data.errorCode==108){
            this.$message({
              type:"error",
              message:"超过允许创建的项目数"
            });
          } else if(res.data.errorCode==0){
            //添加成功，返回我的项目列表
            this.$message({
              type:"success",
              message:"添加项目成果"
            });
            this._getMyProject(user.userId);
            this.$message({
              message: '添加项目成功',
              type: 'success'
            });
            this.dialogFormVisible=false;
          }
          this.isDisabled = false;
        })
      },
      dateFormat:function(date){
        var yyyy = date.getFullYear();
        var mm = (date.getMonth()+1) < 9 ? "0"+(date.getMonth()+1) : (date.getMonth()+1);
        var dd = date.getDate() < 9 ? "0"+date.getDate() : date.getDate()
        return yyyy+"-"+mm+"-"+dd;
      },
      _getMyProject(userId){
        getMyProject(this.page,this.offset,userId).then(res=>{
          if(res.data.errorCode==0){
            this.p_total = res.data.data.count;
            res.data.data.projectList.map(item=>{
              item.createTime = this.dateFormat(new Date(item.createTime))
            });
            this.tableData = res.data.data.projectList;
          }
        })
      },
      // 提交
      submitForm(form){
        this.$refs[form].validate((valid) => {
          if (valid) {
            var isPublic = this.form.isPublic ? 1 : 0;
            var form = JSON.parse(JSON.stringify(this.form));
            var name = encodeURIComponent(form.name);
            var brefIntro = encodeURIComponent(form.brefIntro);
            this._addProject(name,isPublic,brefIntro);
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      }
    },
    beforeRouteEnter:(to, from, next) => {
      next(vm => {
        var user = JSON.parse(localStorage["user"]);
        var userId = user.userId;
        //获取我的项目
        vm.page = 1;
        vm.offset=5;
        vm.isDisabled = false;
        vm._getMyProject(userId);
      });
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>

</style>
<style type="text/css">
  #myProject .caret-wrapper {
    display: none
  }
</style>
