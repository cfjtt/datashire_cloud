<template>
    <div class="license">
      <!--标题-->
      <h1 id="title">授权码信息</h1>
      <el-form :inline="true" id="inlienMain"  class="demo-form-inline" >
        <!--没有导入license-->
        <p v-if="noLicese" class="noLicese">未导入授权码</p>
        <!--导入/更新license的内容-->
        <p class="userNum" v-if="userShow">同时在线用户数限制: <span>{{userNum}}</span></p>
        <p class="userTime" v-if="userShow" style="text-indent: 14px">系统使用时长限制: <span>{{userTime}}</span></p>
        <el-button class="button-green" @click="ImportInformation" v-if="importShow">导入授权码</el-button>
        <el-button class="button-green" @click="updateInformation" v-if="updateShow">更新授权码</el-button>
      </el-form>
      <!--弹出框-->

      <el-dialog id="licenseDialog"
        :title="dialogTitle"
        :visible.sync="dialogVisible"
        width="50%">
        <el-form  :rules="dialogRule"  ref="dialogRef" :model="dialogModel" label-width="110px" class="demo-ruleForm">
        <el-form-item label="DEEP认证信息" prop="name">
          <el-input type="textarea" v-model="dialogModel.name" readonly  :autosize="{ minRows: 3}"></el-input>
        </el-form-item>
        <el-form-item label="DEEP授权码" prop="license">
          <el-input type="textarea" v-model="dialogModel.license" maxlength="172" placeholder='请输入授权码' :autosize="{ minRows: 3}" ></el-input>
        </el-form-item>
        </el-form>
        <!--<p class="deepTitle sub-title"></p>-->

        <!--<p class="licenseTitle sub-title" prop="license">deep license</p>-->

        <span slot="footer" class="dialog-footer">
           <el-button type="primary" @click="submitDialog">提交</el-button>
          <el-button @click="dialogVisible = false">取 消</el-button>
        </span>
      </el-dialog>
    </div>
</template>

<script type="text/ecmascript-6">
  import {getSysKey,isImportLicense,importOrUpdateLicense} from "@/common/js/request";
  export default {
    data() {
      // license名称
      var validaTEName = (rule, value, callback) => {
        if (typeof value == 'undefined' || value =="") {
          callback(new Error('请输入授权码'));
        } else if(value.trim().length > 300){
          callback(new Error('授权码长度不能超过300'));
        }  else {
          callback();
        }
      };
      return {
        dialogVisible: false,
        dialogTitle:"",
        userShow:false,
        noLicese:true,
        importShow:true,
        updateShow:false,
        dialogModel:{
          name:'',
          license:''
        },
        dialogRef:'',
        userNum:'',
        userTime:'',
        dialogRule:{
          license: [
            { required: true,validator: validaTEName, trigger: 'blur' }
          ]
        },
        passLicense:''//传递的license值
      }
    },
    methods:{
      // 导入license
      ImportInformation(){
        // 打开弹出框
        this.dialogVisible=true,
          // 标题
        this.dialogTitle="导入"
        // this.dialogModel.license=""
        // 清除校验记录
        if (this.$refs.dialogRef !==undefined) {
          this.$refs.dialogRef.resetFields();
        }
        // 获取deep认证信息
        getSysKey().then(res =>{

          console.log(res.data.data.key)
          this.dialogModel.name=res.data.data.key

        })
      },
      // 更新license
      updateInformation(){
        // 打开弹出框
        this.dialogVisible=true,
          // 标题
        this.dialogTitle="更新"
        // this.dialogModel.license=""
        // 清除校验记录
        if (this.$refs.dialogRef !==undefined) {
          this.$refs.dialogRef.resetFields();
          // this.$refs.dialogRef.clearValidate();
        }
        // 获取deep认证信息
        getSysKey().then(res =>{
          console.log(res.data.data.key)
          this.dialogModel.name=res.data.data.key
        })

      },
      // 提交信息
      submitDialog(){
        this.$refs.dialogRef.validate((valid) => {
          if (valid) {
            //判断是否导入成功
            isImportLicense().then(res=> {
              if (res.status == 200) {
                // 如果errorCode为0
                if (res.data.errorCode == 0) {
                  // 判断是导入还是更新，1. 导入 2.更新
                  // 导入
                  if(this.dialogTitle=="导入"){
                    importOrUpdateLicense(this.dialogModel.license,1).then(ress=>{
                      //导入成功
                      if(ress.status==200){
                        if(ress.data.errorCode==0){
                          this.$message({
                            message: '导入授权码成功',
                            type: 'success'
                          });
                          // 未导入license信息隐藏
                          this.noLicese=false
                          // 导入的license信息显示
                          this.userShow=true
                          // 隐藏导入按钮
                          this.importShow=false
                          // 显示更新按钮
                          this.updateShow=true
                          this.dialogVisible=false
                          this.userNum=ress.data.data.maxOnlineNum
                          this.userTime=ress.data.data.endTime
                          this.passLicense=this.dialogModel.license
                          this.userTime=ress.data.data.endTime
                          if(this.userTime=="" || this.userTime==undefined){
                            this.userTime="不限时间"
                          }else{
                            this.userTime=ress.data.data.endTime
                          }


                        }

                        //导入失败
                        else{
                          this.$message({
                            message: ress.data.message,
                            type: 'error'
                          });
                        }
                      }
                    })
                  }
                  //更新信息
                  else{
                    this.dialogVisible=false
                    this.$confirm('新授权码将覆盖原有的授权码, 是否继续操作?', '提示', {
                      confirmButtonText: '确定',
                      cancelButtonText: '取消',
                      type: 'warning'
                    }).then(() => {
                      // 判断是否导入
                      isImportLicense().then(updateres=> {
                        if (updateres.status == 200) {
                          if (updateres.data.errorCode == 0) {
                            // 更新
                            importOrUpdateLicense(this.dialogModel.license, 2).then(resCover => {
                              console.log("更新提示语")
                              console.log(resCover.data)
                              // 更新成功
                              if (resCover.data.errorCode == 0) {
                                this.$message({
                                  message: '更新授权码成功',
                                  type: 'success'
                                });
                                    // 未导入授权码信息隐藏
                                    this.noLicese=false
                                    // 导入的license信息显示
                                    this.userShow=true
                                    // 隐藏导入按钮
                                    this.importShow=false
                                    // 显示更新按钮
                                    this.updateShow=true
                                this.userNum = resCover.data.data.maxOnlineNum
                                this.userTime = resCover.data.data.endTime
                                if (this.userTime == "" || this.userTime == undefined) {
                                  this.userTime = "不限时间"
                                } else {
                                  this.userTime = resCover.data.data.endTime
                                }
                              }
                              // 更新失败
                              else{
                                this.$message({
                                  message: resCover.data.message,
                                  type: 'error'
                                });
                              }
                            })
                          }
                        }
                      })
                    }).catch(() => {
                      this.$message({
                        type: 'info',
                        message: '取消覆盖'
                      });
                    });

                  }
                }
                else if(res.data.errorCode == 95){
                  importOrUpdateLicense(this.dialogModel.license,1).then(ress=>{
                    //导入成功
                    if(ress.status==200){
                      if(ress.data.errorCode==0){
                        this.$message({
                          message: '导入授权码成功',
                          type: 'success'
                        });
                        // 未导入license信息隐藏
                        this.noLicese=false
                        // 导入的license信息显示
                        this.userShow=true
                        // 隐藏导入按钮
                        this.importShow=false
                        // 显示更新按钮
                        this.updateShow=true
                        this.dialogVisible=false
                        this.userNum=ress.data.data.maxOnlineNum
                        this.userTime=ress.data.data.endTime
                        this.passLicense=this.dialogModel.license
                        this.userTime=ress.data.data.endTime
                        if(this.userTime=="" || this.userTime==undefined){
                          this.userTime="不限时间"
                        }else{
                          this.userTime=ress.data.data.endTime
                        }


                      }

                      //导入失败
                      else{
                        this.$message({
                          message: ress.data.message,
                          type: 'error'
                        });
                      }
                    }
                  })
                }
                else{
                  this.$message({
                    message: res.data.message,
                    type: 'error'
                  });
                }
              }



            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      }
    },

    activated: function(){
      console.log("activated我执行了")
      isImportLicense().then(res=>{
        // console.log(res);
        // this.dialogModel.license=""
        // if(resupdate.data.errorCode==0){
        // 判断是否导入
        if(res.data.errorCode==0){
          // 未导入license信息隐藏
          this.noLicese=false
          // 导入的license信息显示
          this.userShow=true
          // 隐藏导入按钮
          this.importShow=false
          // 显示更新按钮
          this.updateShow=true
          // this.passLicense=this.dialogModel.license
          this.userNum=res.data.data.maxOnlineNum
          this.userTime=res.data.data.endTime
          if(this.userTime=="" || this.userTime==undefined){
            this.userTime="不限时间"
          }else{
            this.userTime=res.data.data.endTime
          }
        }else{
          // 未导入license信息隐藏
          this.noLicese=true
          // 导入的license信息显示
          this.userShow=false
          // 隐藏导入按钮
          this.importShow=true
          // 显示更新按钮
          this.updateShow=false
        }
      })
    }
  }
</script>

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus">
  .license
    #title
      font-size 22px
      margin 20px 3% 0px
      font-family  "Microsoft YaHei"
    #inlienMain
      width 94% !important
      margin 20px auto
      padding-top 20px
      border-top 1px solid #ebeef5
      padding-left 30px
      .userNum,.userTime,.noLicese
        height 40px
        line-height 40px
        font-size 1em
      .noLicese
        margin-bottom 10px
    #licenseDialog
      .el-dialog__body
        padding 8px 20px 10px
        .deepTitle,.licenseTitle
          font-size 16px
          height 40px
          line-height 40px
</style>
