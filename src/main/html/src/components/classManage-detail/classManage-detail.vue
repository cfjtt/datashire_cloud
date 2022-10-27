<template>
    <div id="detail">
      <!--头部-->
      <h-eader></h-eader>
      <h1 id="title">{{className}}</h1>
      <!--课程名称-->
      <el-tabs  v-model="activeName" type="card" v-for="(course,key) in courseList" :key="key">
        <el-tab-pane :label="course.name" name="first">
          <el-table
            ref="courseTable"
            :data="course.experiment"
            style="width: 100%">
            <el-table-column
              prop="name"
              label="章节名称/实验名称">
              <template slot-scope="scope">
                <span>{{scope.row.moduleName.concat('/').concat(scope.row.experimentName)}}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="操作">
              <template slot-scope="scope">
                <el-button @click="handleClick(scope.row,course.name)" type="primary" size="small">查看成绩</el-button>
              </template>
            </el-table-column>
            <div slot="empty">
              <div>
                <p>该课程还未布置过作业!</p>
              </div>
            </div>
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <!--成绩表弹出框-->
      <el-dialog
        title="成绩表"
        :visible.sync="dialogScore"
        width="70%"
        id="DialogResulit"
        style="margin-top: -5vh">
        <h1 id="markTitle" class="kong">{{className+'/'+courseName+'/'+experimentName}}</h1>
        <el-table
          id="DialogResulitTable"
          :data="scoreTableData"
          :default-sort = "{prop: 'snoNum', order: 'Ascending'}">
          <el-table-column
            show-overflow-tooltip
            width="200"
            label="姓名"
            property="studentName">
            <template slot-scope="alldata"  >
            <el-tooltip placement="top"
                        :disabled="stuInforDisabled">
              <div slot="content">{{ alldata.row.studentNameStrshow }}</div>
              <p v-on:mouseenter="stuInforMouseenter(alldata.$index, alldata.row.studentNameStrshow)">{{ alldata.row.studentNameStr }}</p>
            </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column
            property="snoNum"
            label="学号"
            width="200"
            show-overflow-tooltip>
            <template slot-scope="alldata"  >
              <el-tooltip placement="top"
                          :disabled="stuInforDisabled">
                <div slot="content">{{ alldata.row.studentNoStrshow }}</div>
                <p v-on:mouseenter="stuInforMouseenter(alldata.$index, alldata.row.studentNoStrshow)">{{ alldata.row.studentNoStr }}</p>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column property="gender" label="性别">
            <template slot-scope="scope">
              <span>{{scope.row.gender==1 ? '男' : '女'}}</span>
            </template>
          </el-table-column>
          <el-table-column property="createTime" label="提交时间">
            <template slot-scope="scope">
              <span>{{scope.row.createTime==null ? '未提交':scope.row.submitDate}}</span>
            </template>
          </el-table-column>
          <el-table-column property="score" label="分数">
            <template slot-scope="scope">
              <span>{{scope.row.createTime!=null && (scope.row.score == null)? '批改中' : scope.row.score}}</span>
            </template>
          </el-table-column>
        </el-table>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="importScore">导出成绩</el-button>
        </span>
      </el-dialog>
    </div>
</template>

<script type="text/ecmascript-6">
  import {findClassByTeacherId,getCouExpByTeaIdAndCId,findStudentScore,findCourseByName,edu_url} from '@/common/js/request';
  import header from "../header/header";
  export default {
    data(){
      return {
        activeName:'first',
        tableData:[
          {
            name:''
          }
        ],
        classId:'',
        teacherId:localStorage['user'] ? JSON.parse(localStorage['user']).userId : '',
        dialogScore:false,
        scoreTableData: [
          {
            studentName:'',
            sno:'',
            gender:'',
            createTime:'',
            score:''
          }
        ],
        courseList:[],
        className:'',
        experimentName:'',
        courseName:'',
        courseId:'',
        moduleName:'',
        experimentId:'',
        stuInforDisabled:false
      }
    },
    components:{
      "h-eader": header
    },
    // 查看实验
    methods: {
      stuInforMouseenter(index,row){
        if(row.length>12){
          this.stuInforDisabled=false
        }else{
          this.stuInforDisabled=true
        }
      },
      handleClick (row,name) {
        var experimentId=row.experimentId;
        this.experimentName=row.experimentName;
        this.courseName=name;
        this.moduleName=row.moduleName;
        findCourseByName(name).then(res=>{
          if(res.status==200){
              if(res.data.errorCode==0){
                console.log("课程信息");
                console.log(res);
                var courseId=res.data.data.id;
                findStudentScore(this.classId,courseId,experimentId).then(res=>{
                  console.log(res);
                    if(res.status==200){
                      if(res.data.errorCode==0){
                        this.experimentId=experimentId;
                        this.courseId=courseId;
                        this.scoreTableData = res.data.data.jobScoreList;
                        this.scoreTableData.forEach((value) =>{
                          value.studentNoStr=value.sno;
                          value.studentNoStrshow=value.sno;
                          value.studentNameStr=value.studentName;
                          value.studentNameStrshow=value.studentName;
                          if(value.studentNoStr.length>12){
                            var studentNoStr=value.studentNoStr.substr(0,12)+"..."
                            value.studentNoStr=studentNoStr
                          }else{
                            value.studentNoStr=value.studentNoStr
                          }
                          if(value.studentNameStr.length>12){
                            var studentNameStr=value.studentNameStr.substr(0,12)+"..."
                            value.studentNameStr=studentNameStr
                          }else{
                            value.studentNameStr=value.studentNameStr
                          }
                          value.snoNum = parseFloat(value.sno);
                        })

                      }
                    }
                });
              }
          }
        });

        this.dialogScore=true;
      },
      // 导出成绩
      importScore(){
        window.location.href=edu_url+"/teacher/class/export?classId="+encodeURIComponent(this.classId)+"&courseId="+encodeURIComponent(this.courseId) +"&experimentId="+encodeURIComponent(this.experimentId)+"&className="+encodeURIComponent(this.className)+"&courseName="+encodeURIComponent(this.courseName)+"&moduleName="+encodeURIComponent(this.moduleName)+"&experimentName="+encodeURIComponent(this.experimentName);
      },
      //查询班级对应课程
      _findCourseByClassIdAndTeacherId:function () {
        getCouExpByTeaIdAndCId(this.teacherId,this.classId).then(res=>{
            if(res.status==200){
              if(res.data.errorCode==0){
                var courseList = res.data.data;
                console.log(courseList);
                courseList.map(obj=>{
                  for(var key in obj){
                    obj.name = key;
                    obj.experiment = obj[key];
                    if(obj.experiment.length==0){

                    }
                  }
                });
                this.courseList = courseList;
              }
            }
        });
      }
    },
    created:function () {
      this.classId=this.$route.query.classId;
      this.className=this.$route.query.className;
      this._findCourseByClassIdAndTeacherId();
      console.log("表格数据")
      console.log(this.$refs.courseTable)
    },
    beforeRouteEnter:(to,from,next)=>{
      next(vm=>{
          vm.classId=vm.$route.query.classId;
          vm.className=vm.$route.query.className;
          console.log("班级编号"+vm.classId);
          vm._findCourseByClassIdAndTeacherId();
      })
  }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus">
  #detail
    width 94%
    margin 0px auto
    margin-top 60px
    #title
      font-size 22px
      font-family  "Microsoft YaHei"
      border-bottom 1px solid #ebeef5
      padding-bottom 18px
      margin-bottom 30px
    .el-table
      th > .cell
        text-align center;font: 1.1em bolder
      .cell
        text-align center
      th
        border-bottom 1px solid #ebeef5 !important
    #DialogResulit  .el-table--group::after,
    #DialogResulit .el-table--border::after,
    #DialogResulit .el-table::before
      background-color transparent
    #DialogResulitTable
      .caret-wrapper
        display none
</style>
<style type="text/css">
  /*弹出框分辨率*/
  @import "../../common/css/banji.css";
  .el-tabs--top.el-tabs--card .el-tabs__item:last-child{
    white-space: pre-wrap;
  }
</style>
