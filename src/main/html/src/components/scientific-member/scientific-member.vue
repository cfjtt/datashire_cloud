<template>
  <div id="scienMenmber" style="padding-bottom: 40px;">
    <h1 class="title breakWord" style="margin: 10px 0px 10px">项目成员管理</h1>
    <div>
      <el-input placeholder="请输入关键字" style="width: 200px" v-model="searchUser" @keyup.enter.native="searchUserFunc"></el-input>
      <el-button type="primary" @click="searchUserFunc">搜索</el-button>
    </div>
    <div style="text-align: center;margin-top: 20px;">
      <!--:button-texts="['到左边', '到右边']"-->
      <el-transfer
        style="text-align: left; display: inline-block"
        v-model="userInProject"
        :titles="['本校师生名单', '项目成员名单']"

        :format="{noChecked: '${total}',hasChecked: '${checked}/${total}'}"
        :data="userNotInProject"
        @right-check-change="rightCheckChange">
        <span slot-scope="{ option }">{{ option.label }}</span>
      </el-transfer>
      <div class="el-transfer__buttons">
        <button type="button" class="el-button el-button--primary el-transfer__button">
          <span @click="replaceManager">
            <!--<span>到右边</span>-->
             <i class="el-icon-arrow-right"></i>
          </span>
        </button>
      </div>
      <div class="el-transfer-panel" titles="本校师生名单,项目成员名单" buttontexts="到左边,到右边"
           filterplaceholder leftdefaultchecked="2,3" rightdefaultchecked="1" value="1"
           targetorder="original">
        <p class="el-transfer-panel__header">
          <label role="checkbox" class="el-checkbox" style=";text-align: left">
             <span class="el-checkbox__label" >
               项目组长
                <span>1/1</span>
             </span>
          </label>
        </p>
        <div class="el-transfer-panel__body" id="panelBody">
          <div role="group" aria-label="checkbox-group" class="el-checkbox-group el-transfer-panel__list">
            <label role="checkbox" aria-checked="true" class="el-checkbox el-transfer-panel__item is-checked">
              <label role="checkbox" class="el-checkbox el-transfer-panel__item is-checked" >
                <span class="el-checkbox__label" style="text-align: left">
                  <span v-for="item in userIsManager">{{item.label}}</span>
                </span>
              </label>
            </label>
          </div>
          <p class="el-transfer-panel__empty" style="display: none">无匹配数据</p>
          <p class="el-transfer-panel__empty" style="display: none">无数据</p>
        </div>

      </div>
      <div style="clear: both"></div>
      <div style="float: right">
        <el-button type="primary" @click.native="save">保存</el-button>
      </div>
    </div>

  </div>
</template>

<script type="text/ecmascript-6">
  import {getUserProject,saveUserProject,getUserProjectSearch} from '@/common/js/request';
    export default {
      data() {
        return {
          userInProject: [],
          projectId:0,
          userInProject:[],
          userIsManager:[],
          userNotInProject:[],
          rightCheckValue:[],
          searchUser:null,
          userInProjectTemp:[]
        };
      },

      methods: {
        rightCheckChange:function(value){
          this.rightCheckValue=value;
        },
        _getUserProject(projectId){
          getUserProject(projectId).then(res=>{
            console.log("--------成员信息-------");
            console.log(res);
            if(res.data.errorCode==0){

              var userIsManager = new Array();
              res.data.data.userIsManager.forEach(item=>{
                var v = {};
                v.key = item.user_id;
                v.label = item.no+"-"+item.name;
                v.disabled = false;
                userIsManager.push(v);
              });
              this.userIsManager = userIsManager;

              var userNotInProject = new Array();
              res.data.data.userNotInProject.forEach(item=>{
                var v = {};
                v.key = item.user_id;
                v.label = item.no+"-"+item.name;
                v.disabled = false;
                userNotInProject.push(v);
              });
              this.userNotInProject = userNotInProject;
              var userInProject = new Array();
              var userInProjectTemp = [];
              res.data.data.userInProject.forEach(item => {
                var v = {};
                v.key = item.user_id;
                v.label = item.no+"-"+item.name;
                v.disabled = false;
                userNotInProject.push(v);
                userInProject.push(item.user_id);
                userInProjectTemp.push(v);

              });
              this.userInProject = userInProject;
              this.userInProjectTemp = userInProjectTemp;

            }
          })
        },
        searchUserFunc(){
          if(this.searchUser==null){
            this.searchUser = "";
          }
          getUserProjectSearch(this.projectId,this.searchUser).then(res=>{
            console.log("------搜索信息--------");
            console.log(res);
            if(res.data.errorCode==0){
              //原始的人
              var originUserList = JSON.parse(JSON.stringify(this.userNotInProject));
              //将原来项目中的成员添加到userNotInProject中
              var needAddUser = [];
              this.userInProjectTemp.forEach(item=>{
                let flag = true;
                originUserList.forEach(item2=>{
                  if(item2.key == item.key){
                    flag = true;
                    return;
                  }
                });
                if(flag){
                  needAddUser.push(item);
                }
              });

              needAddUser.forEach(item=>{
                originUserList.push(item);
              });

              var originProjectUser = [];
              //找到当前项目中的成员
              this.userInProject.forEach(item=>{
                console.log("项目中原始的成员");
                console.log(item);
                for(let i=0;i<originUserList.length;i++){
                  var item2 = originUserList[i];
                  if(item2.key == item){
                    originProjectUser.push(item2);
                    originUserList.splice(i,1);
                    i=i-1;
                  }
                }
              });
              //项目中的管理员
              var managerId = this.userIsManager[0].key;
              //新的不在项目中的人
              var userNotInProject = new Array();
              res.data.data.userNotInProject.forEach(item=>{
                var v = {};
                v.key = item.user_id;
                v.label = item.no+"-"+item.name;
                v.disabled = false;
                if(managerId != v.key) {
                  userNotInProject.push(v);
                }
              });
              this.userNotInProject = userNotInProject;
              //var userInProject = new Array();
              originProjectUser.forEach(item => {
                var v = {};
                v.key = item.key;
                v.label = item.label;
                v.disabled = false;
                //判断该用户是否存在，不存在添加进去
                var flag = false;
                userNotInProject.forEach(item2=>{
                  if(item2.key == v.key){
                    flag = true;
                    return;
                  }
                });
                if(!flag && v.key != managerId) {
                  userNotInProject.push(v);
                }
                //this.userInProject.push(item.user_id);
              });

              // 遍历原来不存在的项目成员
              var search = this.searchUser;
              if(userNotInProject.length<100){
                originUserList.forEach(item=>{
                  console.log(item.label);
                  var no = item.label.split("-")[0];
                  var name = item.label.split("-")[1];
                  var key = item.key;
                  if(userNotInProject.length<100) {
                    if (no.indexOf(search) > -1 || name.indexOf(search) > -1) {
                      var flag = true;
                      userNotInProject.forEach(value=>{
                        if(value.key == key){
                          flag = false;
                        }
                      });
                      if(flag) {
                        userNotInProject.push(item);
                      }
                    }
                  }

                })
              }
            }
          })
        },
        replaceManager(){
          if(this.rightCheckValue.length==0){
            this.$message({
              type:"warning",
              message:"请选择一名项目成员"
            });
            return;
          }
          if(this.rightCheckValue.length>1){
            this.$message({
              type:"warning",
              message:"项目组长名额限定一个，请勿多选！"
            });
            return;
          }
          if(this.rightCheckValue.length==1){
            this.$message({
              type:"warning",
              message:"请注意！保存后，您的项目组长权限将被移除，请慎重操作！"
            });
          }
          //先移除总的人数
          var replaceManager = null;
          for(let i=0;i<this.userNotInProject.length;i++){
            var user = this.userNotInProject[i];
            if(user.key==this.rightCheckValue[0]){
              replaceManager = user;
              this.userNotInProject.splice(i,1);
              break;
            }
          }
          //把原来选择的组长，添加到总的人数里面
          this.userNotInProject.push(this.userIsManager[0]);
          //再把原来的组长添加到 项目成员里面
          this.userInProject.push(this.userIsManager[0].key);
          //再把要添加的组长，从项目成员列表中删除
          for(let i=0;i<this.userInProject.length;i++){
            var userId = this.userInProject[i];
            if(userId == this.rightCheckValue[0]){
              this.userInProject.splice(i,1);
              break;
            }
          }
          //再把原来的组长从组长列表中删除
          this.userIsManager.splice(-1);
          //最后将选中的组长添加到组长列表
          this.userIsManager.push(replaceManager);

          this.rightCheckValue=[];

        },
        save(){
          //将项目组长添加到项目成员名单中
          var userInProject = JSON.parse(JSON.stringify(this.userInProject));
          console.log(this.userInProject);
          //判断该项目组长是否存在，存在则不添加
          var isAddFlag = true;
          userInProject.forEach(item => {
            if(item==this.userIsManager[0].key){
              isAddFlag = false;
            }
          });
          if(isAddFlag) {
            userInProject.push(this.userIsManager[0].key);
          }
          saveUserProject(this.projectId,userInProject,this.userIsManager[0].key).then(res=>{
            console.log("-------保存成员信息--------");
            if(res.data.errorCode==0){
              this.$message({
                type:"success",
                message:"保存成功"
              });
              this.$router.push("/scientificMyProject");
              //this._getUserProject(this.projectId);
            } else if(res.data.errorCode == 115){
              this.$message({
                type:"error",
                message:"该项目不存在"
              });
              this.$router.push("/scientificMyProject");
            } else {
              this.$message({
                type:"error",
                message:"服务器异常,请稍后重试"
              })
            }
          })
        },
        filterMethod(query,item){
          return item.label.indexOf(query)>-1;
        }
      },
      beforeRouteEnter: (to, from, next) => {
        next(vm => {
          vm.projectId = vm.$route.query.projectId;
          vm._getUserProject(vm.projectId);
          vm.searchUser="";
        });
      }
    }
</script>

<style lang="stylus" scoped type="text/stylus">
  #scienMenmber
    .title
      font-size 18px
      line-height 30px
      margin-bottom 10px
</style>
<style type="text/css">
  .el-transfer-panel{
    width: 290px
  }
  #panelBody .el-transfer-panel__item{
    padding: 0px;
  }
  @media only screen and (min-width: 1540px) and (max-width: 2200px){
    .el-transfer-panel{
      width: 480px !important
    }
  }
</style>
