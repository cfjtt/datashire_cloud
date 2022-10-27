<template>
  <div style="width: 500px;height: 1000px;font-size: 30px;">
    我是markdown测试区域
  </div>
</template>

<script>
    export default {
      data(){

      },
      methods:{

      },
      created(){
          var user = JSON.parse(localStorage.getItem("user"));
          var isTeacher = false;
          if(user.role==2){
            isTeacher = true;
          }
          callbackobjectforjs.opendatashirecoursecallback("http://192.168.137.185:8081/coursedetail","new password course",isTeacher);
        }
    }
</script>

<style scoped>

</style>
