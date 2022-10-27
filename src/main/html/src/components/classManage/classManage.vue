<template>
    <div>
      <!--头部-->
      <h-eader></h-eader>
      <div  id="classManager">
        <h1 id="title">班级列表</h1>
        <!--表格数据-->
        <el-table
          id="Table"
          :data="tableData"
          style="width: 100%">
          <!--<el-table-column-->
            <!--prop="classId"-->
            <!--label="班级编号">-->
          <!--</el-table-column>-->
          <el-table-column
            prop="className"
            label="班级名称">
          </el-table-column>
          <el-table-column
            prop="studentNum"
            label="学生数">
          </el-table-column>
          <el-table-column
            label="操作">
            <template slot-scope="scope">
              <el-button @click="handleClick(scope.row)" type="primary" size="small">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
</template>

<script type="text/ecmascript-6">
  import {findClassByTeacherId} from '@/common/js/request';
  import header from "../header/header";
  export default {
    data(){
      return {
        tableData:[
          {
            classId:'',
            className:'',
            studentNum:'',
            pathName:''
          }
        ],
        teacherId:localStorage['user'] ? JSON.parse(localStorage['user']).userId : '',
      }
    },
    components:{
      "h-eader": header
    },
    created:function(){
      this._findClassByTeacherId();
      this.pathName=this.$route.path
      console.log("页面名称为:"+this.pathName)
    },
    methods: {
      // 进入查看成绩页面
      handleClick(row){
        var classId=row.classId;
        var className=row.className;
        var name=this.pathName
        this.$router.push({path: '/classManageDetail/',query:{classId,className,name}});
      },
      _findClassByTeacherId(){
        findClassByTeacherId(this.teacherId).then(res=>{
          console.log(res);
          if(res.status==200){
            if(res.data.errorCode==0){
              this.tableData=res.data.data.classInfoList;
            }
          }

        })
      }
    },
    beforeRouteEnter: (to, from, next) => {
      next(vm => {
        vm._findClassByTeacherId();
      })
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus">
  #classManager
    #title
      font-size 22px
      margin 60px 3% 0px
      font-family  "Microsoft YaHei"
      border-bottom 1px solid #ebeef5
      padding-bottom 18px
    #Table
      width 94% !important
      height auto
      margin 30px auto 10px
      text-align center
    .el-table
      th > .cell
        text-align center;font: 1.1em bolder
      .cell
        text-align center
  #classManagerDetail
    width 100%
    margin-top 60px
</style>
