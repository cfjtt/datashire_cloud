<template>
  <div id="htshuju">
    <h1 id="htTitle">数据管理</h1>
    <el-form :inline="true" :model="formInline" id="line" class="demo-form-inline">
     <!-- <el-form-item label="课程名称">
        <el-input v-model="formInline.user" placeholder="请输入课程名称"></el-input>
      </el-form-item>-->
      <el-form-item label="课程名称">
      <el-select v-model="formInline.courseId"
                 placeholder="请选择"
                 @change="change"
                 filterable
                 style="overflow: hidden;font-size: 15px;width: 370px;">
        <el-option v-for="(item, idx) in items"
                   :title="item.name"
                   :label="item.name"
                   :value="item.id"
                   :key="idx">
        </el-option>
      </el-select>
      </el-form-item>
      <el-form-item label="数据属性" style="margin-left: 5px;">
        <el-cascader placeholder="请选择"
          :options="options"
          v-model="selectedOptions" @change="change1">
        </el-cascader>
      </el-form-item>
      <el-form-item>
        <el-button class="button-green" :disabled="disabled" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
    <div id="Main">
      <div class="M-main" v-if="mmshow">
        请选择相应的课程名称和数据属性！
      </div>
      <div>
        <keep-alive v-if="show">
          <router-view></router-view>
        </keep-alive>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import axios from 'axios'
  import {getKecheng,edu_url,getCourseById,getAllCourses} from '@/common/js/request'
  export default {
    data() {
      return {
        formInline: {
          courseId: '',
        },
        items:'',
        options: [
          {
          value: 'bcl',
          label: '编程类'
        },
          {
          value: 'sly',
          label: '数猎云',
          children: [
            {
            value: 'sysj',
            label: '实验数据',
            children: [{
              value: 'syFile',
              label: '文件'
            }, {
              value: 'sytable',
              label: '表'
            }]
          },
            {
              value: 'sjh',
              label: '数据湖',
              children: [{
                value: 'sjfile',
                label: '文件'
              }, {
                value: 'sjtable',
                label: '表'
              }]
            }
          ]
        }],
        selectedOptions: [],
        mmshow:true,
        disabled:false,
        show:false
      }
    },
    beforeRouteEnter: (to, from, next) => {
      console.log("beforeEnter");
      next(vm => {
        vm.init();
      });
    },
    methods: {
      // 失去焦点
      // blurchange(){
      //   this.blurClick()
      //   console.log("失去焦点")
      // },
      // blurClick(){
      //   console.log("触发事件")
      // },
      onSubmit() {
        if(this.formInline.courseId==""){
          this.mmshow=true
          this.$message({
            type:"error",
            message:"请选择课程!"
          });
          return;
        }
        this.mmshow=false;
        getCourseById(this.formInline.courseId).then(res=>{
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          if(res.data.errorCode==0){
            if(res.data.data != null){
              var courseName = res.data.data.name;
              var programExist = res.data.data.programExist;
              var shulieyunExist = res.data.data.shulieyunExist;
              var value = this.selectedOptions[this.selectedOptions.length-1];
              if(value=="bcl"){
                //查询是否存在编程实验
                if(programExist) {
                  this.$router.push('/htSj/share/' + this.formInline.courseId+"/"+new Date().getTime());
                  this.show = true;
                } else {
                  this.show = false;
                  this.$message({
                    type:'warning',
                    message:'该课程暂无编程类实验'
                  });
                }
              }else if(value=="syFile" || value=="sjfile"){
                  //查找是否存在数猎云实验
                if(!shulieyunExist){
                  this.$message({
                    type:'warning',
                    message:'该课程暂无数猎云实验'
                  });
                  this.show = false;
                  return;
                }
                this.show = true;
                if(value=='syFile'){
                  this.$router.push('/htSj/file/2/'+encodeURIComponent(courseName)+"/"+new Date().getTime());
                } else {
                  this.$router.push('/htSj/file/1/'+encodeURIComponent(courseName)+"/"+new Date().getTime());
                }

              }else if(value=="sytable" || value=="sjtable"){
                  //查找是否存在数猎云实验
                  if(!shulieyunExist){
                    this.$message({
                      type:'warning',
                      message:'该课程暂无数猎云实验'
                    });
                    this.show = false;
                    return;
                  }
                  this.show = true;
                  if(value == 'sytable') {
                    this.$router.push('/htSj/Hdata/2/' + encodeURIComponent(courseName)+"/"+new Date().getTime());
                  } else {
                    this.$router.push('/htSj/Hdata/1/' + encodeURIComponent(courseName)+"/"+new Date().getTime());
                  }
                }
            } else {
              this.$message({
                type:'error',
                message:'该课程不存在'
              });
              this.show = false;
            }
          }

        });
      },
      init:function(){
        this.selectedOptions = [];
        this.changeState();
        getAllCourses().then(res=>{
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          if(res.status==200 && res.data.errorCode==0){
            res.data.data.map(item=>{
              item.id = item.id+"";
              // if(item.name.length>10){
              //   console.log("name值为:"+item.name.substr(0,10))
              //   item.name=item.name.substr(0,10)
              // }
            });
            this.items=res.data.data;
            if(this.formInline.courseId.length==0){
              //禁用掉
              this.options.map(item=>{
                item.disabled = true;
              });
            } else {
              this.change(this.formInline.courseId);
            }
          }
        })
      },
      change(vId){
        // 获取value和label值
        // console.log(vId);
        // let obj = {};
        // obj = this.items.find((item)=>{
        //   return item.id === vId;
        // });
        // if(obj != {} && typeof obj != 'undefined') {
        //   if (obj.name.length > 20) {
        //     // obj.name.substr(0,12)
        //     obj.name = obj.name.substr(0, 20) + "..."
        //   }
        // }

        this.selectedOptions=[];
        this.show = false;
        getCourseById(this.formInline.courseId).then(res=>{
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          if(res.data.errorCode==0){
            if(res.data.data != null){
              var programExist = res.data.data.programExist;
              var shulieyunExist = res.data.data.shulieyunExist;
              this.options.map(item=>{
                // console.log("选中的值为:"+item.value)
                if(item.value=='bcl' && !programExist){
                  item.disabled = true;
                } else if(item.value=='bcl' && programExist) {
                  item.disabled = false;
                }
                if(item.value == 'sly' && !shulieyunExist){
                  item.disabled = true;
                } else if(item.value == 'sly' && shulieyunExist) {
                  item.disabled = false;
                }
              });
            } else {
              this.$message({
                type:'error',
                message:'该课程不存在'
              });
              this.show = false;
            }
          }

        });
        if(typeof this.formInline.courseId != 'undefined' && this.selectedOptions.length > 0 && this.formInline.courseId != ''){
          this.disabled= false
        }else{
          this.disabled= true;
          console.log("this.formInline.courseId为"+this.formInline.courseId)
        }
      },
      change1(){
        this.show = false;
        if(typeof this.formInline.courseId != 'undefined' && this.selectedOptions.length > 0 && this.formInline.courseId != ''){
          this.disabled= false
        }else{
          this.disabled= true
        }
      },
      changeState(){
        var courseId = this.$route.query.courseId;
        if(typeof courseId != 'undefined') {
          this.formInline.courseId = courseId + "";
        } else {
          this.formInline.courseId = '';
        }
        if(typeof this.formInline.courseId != 'undefined' && this.selectedOptions.length>0 && this.formInline.courseId != ''){
          this.disabled= false
        }else{
          this.disabled= true
        }
      }
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  #htshuju
    #htTitle
      font-size 22px
      margin 20px 3% 0px
      font-family  "Microsoft YaHei"
    #line
      width 94%
      margin 20px auto -20px
      padding-top 20px
      border-top 1px solid #ebeef5 !important
    #Main
      width 94%
      margin 0px auto
      padding 20px 20px 0px 20px
      .M-main
        padding 150px 0px 150px 0px
        text-align center
        border 1px solid rgb(220, 223, 230)
</style>
<style type="text/css">
   .el-cascader-menu {
    max-height: 80px !important;
  }
  /*#htshuju  .el-input__inner {*/
     /*width: 370px;*/
    /*padding-left: 16px;*/
    /*font-size: 15px;*/
    /*text-align: justify;*/
   /*}*/
   /*.el-input__inner{*/
     /*width: 150px !important;*/
     /*overflow:hidden;*/
     /*text-overflow:ellipsis;*/
     /*white-space: nowrap;*/
   /*}*/
</style>
