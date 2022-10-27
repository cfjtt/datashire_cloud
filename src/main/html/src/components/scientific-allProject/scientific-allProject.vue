<template>
  <div style="padding: 5px;">
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
        label="状态">
        <template slot-scope="scope">
          <p>{{ scope.row.status == 1 ? '进行中' : '已完成' }}</p>
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
            @click="pushProject(scope.row.id)">查看</el-button>
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
</template>

<script type="text/ecmascript-6">
  import { getAllPublicProject,searchProject } from '@/common/js/request'
  export default {
    data() {
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
    methods: {
      // 进入页面
      pushProject(projectId){
        this.$router.push({ path :'/scientificMain', query:{projectId: projectId}});
      },
      _searchProject(){
        searchProject(this.page,this.offset,this.sureSearch,1).then(res=>{
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
      _getAllProject(){
        this.sureSearch = '';
        this.searchCondition = '';
        this._searchProject();
      },
      dateFormat:function(date){
        var yyyy = date.getFullYear();
        var mm = (date.getMonth()+1) < 9 ? "0"+(date.getMonth()+1) : (date.getMonth()+1);
        var dd = date.getDate() < 9 ? "0"+date.getDate() : date.getDate()
        return yyyy+"-"+mm+"-"+dd;
      },
      handleSizeChange(val) {
        this.page=1;
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
        vm.page=1;
        vm.offset=5;
        vm.sureSearch="";
        vm.searchCondition=null;
        vm._getAllProject();
      });
    }
  }
</script>

<style scoped type="text/stylus">

</style>
