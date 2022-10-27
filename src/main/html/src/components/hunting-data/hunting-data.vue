<template>
    <div style="border: 1px solid #dcdfe6;width: 100%;">
      <div style="padding: 10px">
        <el-form :inline="true"  class="demo-form-inline" style="width: 100%;" @submit.native.prevent>
          <el-button  size="medium" @click="getDatashireDBList">刷新</el-button>
          <el-form-item label="模糊查询" style="float: right">
            <el-input @keyup.enter.native="search" v-model="searchQuery"></el-input>
          </el-form-item>
        </el-form>
        <div id="content">
          <span v-if="shujuShow">数据库容量:<b>{{dbLimit}}</b>MB</span>
          <!--<span>数据库已使用:<b>0.02</b>MB</span>-->
        </div>
        <!--模糊查询-->
        <!--<el-form  :model="form" label-width="80px" style="width: 20%;float: right">
          <el-form-item label="查询">
            <el-input ></el-input>
          </el-form-item>
        </el-form>-->
        <template>
          <el-table
            border
            :data="tableData"
            style="width: 100%">
            <el-table-column
              prop="name"
              label="表名"
              width="">
            </el-table-column>
            <el-table-column
              prop="createTime"
              label="创建时间"
              width="">
            </el-table-column>
            <el-table-column
              prop="isExplore"
              label="允许可视化" v-if="!isAdmin || (isAdmin && type==1)">
              <template slot-scope="scope">
                <el-checkbox v-if="isAuthority" @click.native="changeExplore(scope.row,$event)" v-model="scope.row.isExplore=='Y'"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column label="操作" >
              <template slot-scope="scope">
                <el-button
                  v-if="isAuthority"
                  size="mini"
                  type="danger"
                  @click="handleDelete(scope.row)">删除</el-button>
                <el-button
                  size="mini" @click="downloadFile(scope.row)">下载</el-button>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </div>
    </div>
</template>

<script type="text/ecmascript-6">
  import {shu_lie_yun_url,getDataShireDBList,getDataShireFileList,operateTable,deleteTable,getUserProject} from '@/common/js/request';
  export default {
    data(){
      return{
        form:{},
        tableData:[
          {name:'',createTime:'',isExplore:''}
        ],
        tableDataTmp:[
          {name:'',createTime:'',isExplore:''}
        ],
        dbLimit:null,
        spaceName:null,
        searchQuery:'',
        isAdmin:false,
        courseName:null,
        type:1,
        shujuShow:true,
        isProject: false,
        projectId : -1,
        managerNo : -1,
        isAuthority : true
      }
    },
    methods:{
      search(){
        //搜索表
        if(this.searchQuery==''){
          this.tableData = this.tableDataTmp;
        } else {
          let filterDataTmp = [];
          this.tableDataTmp.forEach((value)=>{
            console.log(value);
            if(value.name.toLowerCase().indexOf(this.searchQuery.toLowerCase())>-1 || value.createTime.toLowerCase().indexOf(this.searchQuery.toLowerCase())>-1){
              filterDataTmp.push(value);
            }
          });
          this.tableData = filterDataTmp;
        }
      },
      handleDelete(row) {
        var spaceName = this.spaceName;
        var tableName = row.name;
        var phone = JSON.parse(localStorage["user"]).username;
        this.$confirm('确定要删除该表吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteTable(spaceName,tableName,phone,this.courseName,this.isAdmin,this.type).then(res=>{
            if(res.data=="alreadyResetPassword"){
              this.$message({
                type:'warning',
                message:'请重新登录'
              });
              return;
            }
            if(res.data.errorCode==0){
              if(res.data.data=="1"){
                this.$message({
                  title:"提示",
                  message:"操作成功",
                  type:"success"
                });
                this.getDatashireDBList();
              }
            }
          })
        }).catch(() => {
          // this.$message({
          //   type: 'info',
          //   message: '已取消删除'
          // });
        });

      },
      downloadFile:function(row){
        var spaceName = this.spaceName;
        if(spaceName.indexOf("项目[") != 0 || spaceName.indexOf("项目[") == -1){
          spaceName = "项目[" + spaceName + "]";
        }
        var tableName = row.name;
        var phone = this.isProject ? this.managerNo : JSON.parse(localStorage["user"]).username;
        var courseName = this.courseName;
        var type = this.type;

        var url = shu_lie_yun_url+"/api/downloadSpaceTable?spaceName="+encodeURIComponent(spaceName)+"&tableName="+encodeURIComponent(tableName)+"&phone="+phone+"&courseName="+encodeURIComponent(courseName)+"&type="+type+"&isAdmin="+this.isAdmin;
        window.location.href = url;
      },
      changeExplore:function(row,event){
        event.stopPropagation();
        //spaceName,tableName,operate,phone
        var spaceName = this.spaceName;
        var tableName = row.name;
        var operate = event.target.checked;
        if(typeof operate != 'undefined') {
          var phone = JSON.parse(localStorage["user"]).username;
          var confirmTitle = '';
          if(operate){
            confirmTitle = "是否添加"+tableName+"可视化";
          } else {
            confirmTitle = "警告:取消"+tableName+"可视化后BI中与该表相关的切片和看板也会被删除。请小心操作，取消后不可恢复";
          }
          this.$confirm(confirmTitle, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            operateTable(spaceName, tableName, operate, phone).then(res => {
              if(res.data=="alreadyResetPassword"){
                this.$message({
                  type:'warning',
                  message:'请重新登录'
                });
                return;
              }
              if(res.data.errorCode==0){
                if(res.data.data=="1"){
                  this.$message({
                    title:"提示",
                    message:"操作成功",
                    type:"success"
                  })
                  this.getDatashireDBList();
                }
              }
            })
          }).catch(() => {
            // this.$message({
            //   type: 'info',
            //   message: '已取消可视化'
            // });
          });

        }
      },
      getDatashireDBList:function(){
        var user = JSON.parse(localStorage["user"]);
        console.log("用户名为:"+user.username)
        var phone = user.username;
        // 如果是管理员的话，隐藏提示
        if(phone=="manager"){
          this.shujuShow=false
        }else{
          this.shujuShow=true
        }
        var isAdmin = false;
        var type = 1;
        if(typeof this.$route.params.isAdmin != 'undefined'){
          isAdmin = true;
          type = this.$route.params.isAdmin;
        }
        this.isAdmin = isAdmin;
        this.type = type;
        if(typeof this.$route.params.courseName != 'undefined'){
          this.courseName = this.$route.params.courseName;
        }
        if(typeof this.$route.params.projectId != 'undefined'){
          this.isProject = true;
          this.projectId = this.$route.params.projectId;
          this.isAdmin = false;
          phone = this.$route.params.managerNo;
          this.managerNo = phone;
          this.spaceName = this.$route.params.paraSpaceName;
          getUserProject(this.projectId).then(res=>{
            console.log('获取用户成员');
            console.log(res);
            if(res.data.errorCode == 0){
              var data = res.data.data;
              var flag = false;
              for(var i = 0;i < data.userInProject.length;i++){
                if(user.username == data.userInProject[i].no){
                  //是否是项目内成员
                  flag = true;
                  break;
                }
              }
              if(user.username == data.userIsManager[0].no){
                flag = true;
              }
              if(!flag){
                this.isAuthority = false;
              }
            }
          });
        }
        this.searchQuery = '';
        var paraSpaceName = null;
        if(this.isProject){
          paraSpaceName = this.spaceName;
        }
        //需要跨域
        getDataShireDBList(phone,this.isAdmin,this.courseName,this.type,paraSpaceName).then(res=>{
          console.log(res);
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          if(res.data.errorCode==0){
            this.tableData = res.data.data.tableInfo;
            this.tableDataTmp = res.data.data.tableInfo;
            this.spaceName = res.data.data.space.name;
            this.dbLimit = res.data.data.space.pack.dbCapacity;
          }
        })
      }
    },
    created:function(){
      this.getDatashireDBList();
    },
    watch:{
      '$route':function(){
        if(this.$route.fullPath.indexOf("/htSj/Hdata/") >= 0 || this.$route.fullPath.indexOf("/scientificData/file") >= 0){
          this.getDatashireDBList();
        }
      }
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus">
  #content
    width 100%
    margin 20px 0px

</style>
