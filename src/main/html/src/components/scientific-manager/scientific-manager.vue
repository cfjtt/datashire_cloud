<template>
  <div id="scienManager" >
    <!--头部-->
    <h-eader></h-eader>
    <div id="titleHeader">
      <div style="float: right;">
        <el-input  placeholder="请输入关键字" style="width:200px;" v-model="searchCondition" @keyup.enter.native="search"></el-input>
        <el-button type="primary" @click="search">搜索</el-button>
      </div>
      <el-table
        :data="tableData"
        style="width: 100%">
        <el-table-column
          label="ID">
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
            <p>{{ scope.row.managerUser == null ? '' :  scope.row.managerUser.name.concat("/").concat((scope.row.managerUser.role == 1 ? scope.row.managerUser.sno : scope.row.managerUser.tno))}}</p>
          </template>
        </el-table-column>
        <el-table-column
          label="是否公开">
          <template slot-scope="scope">
            <p>{{ scope.row.isPublic == 1 ? '是' : '否' }}</p>
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
              @click="pushProject(scope.row.id)" disabled v-if="scope.row.isPublic==0">查看</el-button>
            <el-button
              size="mini"
              @click="pushProject(scope.row.id)"  v-if="scope.row.isPublic==1">查看</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="deleteProject(scope.row.id)">删除</el-button>
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
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import header from "../header/header";
  import { getAllPublicProject,searchProject,deleteProject } from '@/common/js/request'
  export default {
    data(){
      return {
        tableData: [],
        // 分页参数
        page: 1,
        p_pageSizes: [5, 10, 15, 20, 30, 50, 100],
        offset: 5,
        p_total: 0,
        searchCondition:null,
        sureSearch:null
      }
    },
    components: {
      "h-eader": header
    },
    methods:{
      // 进入页面
      pushProject(projectId){
        this.$router.push({ path :'/scientificMain', query:{projectId: projectId}});
      },
      deleteProject(projectId){
        this.$prompt('请注意！该项目将从系统被删除，所有项目资料将一并清除，是否确定执行此操作？', '删除项目', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputType:'password',
          inputPlaceholder:'请输入登录密码',
          // inputPattern:/\S/,
          inputPattern:/.+/,
          inputErrorMessage:'登录密码不允许为空',
        }).then(({ value }) => {
          var user = JSON.parse(localStorage['user']);
          if(user.role!=3){
            this.$message({
              type: 'error',
              message:'您不是管理员，没有权限操作'
            });
            return;
          }
          if(user.pwd!=value){
            this.$message({
              type: 'error',
              message:"密码不正确"
            });
            return;
          }
          //开始删除项目
          deleteProject(projectId).then(res=>{
            console.log("-------删除项目-------");
            if(res.data.errorCode==0){
              this.$message({
                type:'success',
                message:'删除成功'
              });
              this.page=1;
              this._searchProject();
            } else if(res.data.errorCode==115){
              this.$message({
                type:'error',
                message:'该项目不存在'
              });
              this.page=1;
              this._searchProject();
            } else {
              this.$message({
                type:'error',
                message:'删除失败，请稍后重试'
              });
            }
          })

        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消输入'
          });
        });
      },
      _searchProject(){
        searchProject(this.page,this.offset,this.searchCondition,null,1).then(res=>{
          console.log("--------模糊搜索信息--------");
          console.log(res);
          if(res.data.errorCode==0){
            this.p_total = res.data.data.count;
            res.data.data.projectList.map(item=>{
              item.createTime = this.dateFormat(new Date(item.createTime))
            });
            this.tableData  = res.data.data.projectList;
          }
        })
      },
      search(){
        this.page=1;
        this.sureSearch = this.searchCondition;
        this._searchProject();
      },
      dateFormat:function(date){
        var yyyy = date.getFullYear();
        var mm = (date.getMonth()+1) < 9 ? "0"+(date.getMonth()+1) : (date.getMonth()+1);
        var dd = date.getDate() < 9 ? "0"+date.getDate() : date.getDate()
        return yyyy+"-"+mm+"-"+dd;
      },
      handleSizeChange(val) {
        this.offset = val;
        this.searchCondition = this.sureSearch;
        this._searchProject();
      },
      handleCurrentChange(val) {
        this.page = val;
        this.searchCondition = this.sureSearch;
        this._searchProject();
      }
    },
    beforeRouteEnter:(to, from, next) => {
      next(vm => {
        vm.searchCondition='';
        vm.sureSearch = "";
        vm._searchProject();
      });
    }

  }
</script>

<style lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  #scienManager
    margin-top 60px
    #titleHeader
      margin 20px 3% 0px

</style>
