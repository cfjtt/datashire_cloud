<template>
    <div id="scaienMain">
      <h1 class="title kong ">项目组长</h1>
      <!--如果组长是老师的话，显示-->
      <el-table
        v-show="isTeacher"
        :data="tableData"
        style="width: 100%">
        <el-table-column
          prop="number"
          label="工号">
        </el-table-column>
        <el-table-column
          prop="name"
          label="教师名称">
        </el-table-column>
        <el-table-column
          prop="gender"
          label="性别">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.gender == 1 ? '男' : '女'}}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="teacherTitle"
          label="职称">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.title == 1 ? '教授' : (scope.row.title == 2 ? '副教授' : '讲师')}}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="email"
          label="联系方式">
        </el-table-column>
      </el-table>
      <!--如果是学生的话，显示-->
      <el-table
        v-if="isStudent"
        :data="tableData"
        style="width: 100%">
        <el-table-column
          prop="number"
          label="学号">
        </el-table-column>
        <el-table-column
          prop="name"
          label="学生姓名">
        </el-table-column>
        <el-table-column
          prop="gender"
          label="性别">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.gender == 1 ? '男' : '女'}}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="grade"
          label="年级">
        </el-table-column>
        <el-table-column
          prop="phone"
          label="联系方式">
        </el-table-column>
      </el-table>
      <h1 class="title">项目简介:</h1>
      <p class="kong breakWord">{{projectData.brefIntro}}</p>
      <h1 class="title" v-show="projectData.results != null && projectData.results !=''">项目成果:</h1>
      <iframe width="1000" height="500" id="MyIFrame" v-if="projectData.results != null && projectData.results !=''">
      </iframe>
    </div>
</template>

<script type="text/ecmascript-6">
    import {getProjectById} from '@/common/js/request';
    export default {
        data(){
          return {
            tableData: [],
            isTeacher:false,
            isStudent:false,
            projectId:0,
            projectData:{},
            role:null
          }
        },
        methods:{
          _getProjectById(projectId){
            getProjectById(projectId).then(res=>{
              console.log("-------项目信息-------");
              console.log(res);
              if(res.data.errorCode==0){
                var managerUser = res.data.data.managerUser;
                if(managerUser != null && managerUser != undefined){
                  if(managerUser.role == 1){
                    managerUser.number = managerUser.sno;
                  } else if(managerUser.role == 2){
                    managerUser.number = managerUser.tno;
                  }
                  if(managerUser.role=="1"){
                    this.isStudent=true;
                    this.isTeacher=false;
                  }
                  // 老师
                  if(managerUser.role=="2"){
                    this.isStudent=false;
                    this.isTeacher=true;
                  }
                  var arr = new Array();
                  arr.push(res.data.data.managerUser);
                  this.tableData = arr;
                } else {
                  this.tableData = [];
                }
                this.projectData = res.data.data;
                var doc = document.getElementById("MyIFrame").contentDocument;
                doc.body.innerHTML = this.projectData.results;
               //document.getElementById("insertHtml").innerHTML=this.projectData.results;
                console.log("获取的数据为:");
                console.log(document.getElementById("insertHtml"));
                console.log(this.projectData.results);
                // 如果项目简介为null,那么为空展示
                if(this.projectData.brefIntro=="null"){
                  this.projectData.brefIntro=""
                }
              } else if(res.data.errorCode==115){
                this.$message({
                  type:'error',
                  message:'该项目不存在'
                })
                if(this.role==3) {
                  this.$router.push("/scientificManager");
                } else {
                  this.$router.push("/scientificMyProject");
                }
              }
            })
          }
        },
      beforeRouteEnter:(to, from, next) => {
        next(vm => {
          var user = JSON.parse(localStorage["user"]);
          vm.role = user.role;
          //var userId = user.userId;
          //获取我的项目
          var projectId = vm.$route.query.projectId;
          vm.projectId = projectId;
          vm._getProjectById(projectId);
        });
      }
    }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
 #scaienMain
    .title
      font-size 18px
      line-height 30px
      margin-bottom 10px
    .kong
      line-height 30px
      text-indent 16px

</style>
