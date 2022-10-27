<template>
  <div>
    <span>课程名称</span>
    <el-select v-model="courseId" placeholder="请选择" @change="change">
      <el-option v-for="(item,idx) in items" :label="item.courseName" :value="item.id" :key="idx"></el-option>
    </el-select>
    <span>数据属性</span>
      <el-cascader
        expand-trigger="hover"
        :options="Objoptions"
        v-model="selectedOptions2"
        @change="handleChange">
      </el-cascader>
    <el-button type="primary" icon="el-icon-search" @click="find()">搜索</el-button>
    <div class="main" id="rightMainWindow">
      <keep-alive>
        <router-view></router-view>
      </keep-alive>
    </div>
  </div>
</template>
<script type="text/ecmascript-6">
  import Vue from 'vue'
  import axios from 'axios'
  import {getKecheng,edu_url} from '@/common/js/request'
  export default {
    data() {
      return {
        courseId:"",
        items:"",
        Objoptions: [
          {
          value: 'biancheng',
          label: '编程类'
        },{
          value: 'shulieyun',
          label: '数列云',
          children: [
            {
            value: 'shujuhu',
            label: '数据湖',
            children:[
              {
                value:'table',
                label:'表'

            },{
                value:'file',
                label:'文件'
            }
            ]
          }
          ]
        }],
        selectedOptions2: []
      };
    },
    activated(){
      var courseId=this.$route.params.courseId;
      this.courseId=courseId;
      //查询所有课程
      getKecheng().then(res=>{
        console.log(res);
        if(res.data=="alreadyResetPassword"){
          this.$message({
            type:'warning',
            message:'请重新登录'
          });
          return;
        }
        if(res.status==200 && res.data.errorCode==0){
          this.items=res.data.data.courseInfoList;
        }
      })

    },
    methods:{
      handleChange(value){
       this.selectedOptions2=value;
        console.log(this.selectedOptions2);
       console.log(this.selectedOptions2[0]);
      },
      change(value){
        this.courseId=value;
      },
      find(){
      if(this.selectedOptions2[0]=="biancheng"){
         this.$router.push("/share");
      }else if(this.selectedOptions2[0]=="shulieyun" && this.selectedOptions2[1]=="shujuhu" && this.selectedOptions2[2]=="table"){

      }else if(this.selectedOptions2[0]=="shulieyun" && this.selectedOptions2[1]=="shujuhu" && this.selectedOptions2[2]=="file"){
        this.$router.push("/file");
      }
      }
    }
  }
</script>
