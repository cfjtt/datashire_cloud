<template>
  <div class="xueqi">
    <h1 id="title">学期管理</h1>
    <el-form :inline="true"  id="btn-header" class="demo-form-inline" @submit.native.prevent>
      <el-form-item >
        <!--<el-button class="button-green" @click="_getListData()">刷新</el-button>-->
        <el-button type="primary" @click="_Refresh()">刷新</el-button>
        <el-button class="button-green" @click="addTerm()">新增</el-button>
        <el-button class="button-green" @click="updateTerm()">编辑</el-button>
        <el-button class="button-green" @click="deleteTerm()">删除</el-button>
        <!--<el-button type="primary" @click="_getListData()">刷新</el-button>-->
        <!--<el-button type="primary" @click="addTerm()">新增</el-button>-->
        <!--<el-button type="primary" @click="updateTerm()">编辑</el-button>-->
        <!--<el-button type="primary" @click="deleteTerm()">删除</el-button>-->
      </el-form-item>
      <!--<el-form-item label="搜索" style="float: right;margin-right: 0px;">-->
        <!--<el-input v-model="searchQuery" @keyup.enter.native="select()" placeholder="搜索"></el-input>-->
      <!--</el-form-item>-->
      <el-form-item style="margin-right: 0px">
        <el-button type="primary" @click="_getListData()">搜索</el-button>
      </el-form-item>
      <el-form-item >
        <el-input v-model="searchQuery" placeholder="搜索"
                  @keyup.native.enter="select()"></el-input>
      </el-form-item>
    </el-form>
    <!--表格-->
    <div id="Table">
      <el-table
        ref="multipleTable"
        tooltip-effect="dark"
        center
        :data="listData"
        :border="true"
        style="width: 100%;"
        @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          type="id"
          label="编号"
          width="" :resizable="false">
          <template slot-scope="alldata">
            <p >{{ alldata.row.id }}</p>
          </template>
        </el-table-column>

        <el-table-column
          prop="year"
          label="学年"
          width=""
          :resizable="false"
          show-overflow-tooltip>
          <template slot-scope="alldata">
            <p >{{ alldata.row.year.split('-').length==1 ? alldata.row.year.concat('-').concat(parseInt(alldata.row.year)+1) : alldata.row.year}}</p>
          </template>
        </el-table-column>

        <el-table-column
          prop="semester"
          width=""
          label="学期"
          :resizable="false"
          show-overflow-tooltip>
          <template slot-scope="alldata">
            <p >{{ alldata.row.semester==1 ? '第一学期' : alldata.row.semester==2 ? '第二学期' : '第三学期' }}</p>
          </template>
        </el-table-column>

        <!--<el-table-column label="操作" min-width="140" :resizable="false">-->
          <!--<template slot-scope="scope" >-->
            <!--<el-button-->
              <!--size="mini"-->
              <!--@click="showEdit(scope.$index)">编辑</el-button>-->
            <!--<el-button-->
              <!--size="mini"-->
              <!--type="danger"-->
              <!--@click="deletedata(scope.$index)">删除</el-button>-->
          <!--</template>-->
        <!--</el-table-column>-->
      </el-table>
    </div>
    <div style="clear: both"></div>
    <div id="pagination" style="width:35%;margin:20px 20%;">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="p_currentPage"
        :page-sizes="p_pageSizes"
        :page-size="p_pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="p_total">
      </el-pagination>
    </div>

    <!--新增-->
    <el-dialog title="新增学期" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="addRules">
        <el-form-item label="学年" :label-width="formLabelWidth" prop="year">
          <el-input v-model="form.year" placeholder='若输入"2017"则代表"2017-2018"学年' auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="学期" :label-width="formLabelWidth" prop="semester">
          <el-select v-model="form.semester" placeholder="请选择学期">
            <el-option label="第一学期" value="1"></el-option>
            <el-option label="第二学期" value="2"></el-option>
            <el-option label="第三学期" value="3"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button class="button-green" @click="save()">保存</el-button>
      </div>
    </el-dialog>
    <!--编辑-->
    <el-dialog title="编辑学期"
               :visible.sync="bianjiFormVisible"
               width="40%">
      <el-form :model="bianjiForm" :rules="editRules">
        <el-form-item label="学年" :label-width="formLabelWidth" prop="year">
          <el-input v-model="bianjiForm.year" placeholder='若输入"2017"则代表"2017-2018"学年' auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="学期" :label-width="formLabelWidth" prop="semester">
          <el-select v-model="bianjiForm.semester" placeholder="请选择学期">
            <el-option label="第一学期" value="1"></el-option>
            <el-option label="第二学期" value="2"></el-option>
            <el-option label="第三学期" value="3"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="bianjiFormVisible = false">取 消</el-button>
        <el-button class="button-green" @click="updateInfo()">保存</el-button>
      </div>
    </el-dialog>
    <!--版权-->
    <div class="copyright">
      <p>版权信息  版权股所有   悦岚数据服务</p>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import {getXueQiListData, deleteXueQi, get, post,updateTearm,getTearmById} from '@/common/js/request';
  export default{
    data() {
      return {
        listData: [],
        listDataTmp: [],
        listDataFilterTmp: [], // 获取过滤后所有的数据
        form: {
          year: '',
          semester: ''
        },
        bianjiForm:{
          year:'',
          semester:''
        },
        bianjiFormVisible: false,
        dialogFormVisible: false,
        formLabelWidth: '100px',
        multipleSelection: [],//已经选择的项
        searchQuery:'',//查询数据
        handleEdit: '',
        // 分页参数
        p_currentPage: 1,
        p_pageSizes: [5, 10, 15, 20, 30, 50, 100],
        p_pageSize: 5,
        p_total: 0,
        addRules :{
          year :[{ required: true,message: '请输入学年',min: 1, max: 45, trigger: 'blur' }],
          semester :[{ required: true,message: '请选择学期', trigger: 'blur' }]
        },
        editRules:{
          year :[{ required: true, message: '请输入学年',trigger: 'blur' }],
          semester :[{ required: true, message: '请选择学期',trigger: 'blur' }]
        }
      }
    },
    activated () {
      this.searchQuery="";
      this._getListData()
    },
    methods: {
      //刷新按钮
      _Refresh:function () {
        this.searchQuery="";
        this._getListData()
        // 点击刷新默认显示每页5条数据
        this.p_pageSize=5
      },
      // 获取数据
      _getListData: function () {
        if(this.searchQuery.length>45){
          this.$message({
            type:'error',
            message:'搜索长度不能超过45个字符'
          })
          return;
        }

        // 点击搜索，默认显示每页5条数据
        this.p_pageSize=5

        getXueQiListData().then(res => {
          this.listDataTmp = res.data.data.semesterList;
          // this.listDataTmp.forEach((value, index) => {
          //   if(value.year.split('-').length==1){
          //     value.year=value.year+"-"+(parseInt(value.year)+1)
          //   }
          // })
          this._filterData()
          this._initPageInfo()
        })
      },
      // 初始化分页信息
      _initPageInfo: function () {
        this.p_total = this.listDataFilterTmp.length
        this.p_currentPage = 1
        this.listData = this.listDataFilterTmp.slice(0, this.p_pageSize)
      },
      // filter data
      _filterData: function () {
        if (this.searchQuery === '') {
          this.listDataFilterTmp = this.listDataTmp
          return
        }
        let filterTableDataEnd = []
        this.listDataTmp.forEach((value, index) => {
//          alldata.row.semester==1 ? '第一学期' : alldata.row.semester==2 ? '第二学期' : '第三学期'
          value.semesterDes = value.semester === 1 ? '第一学期' : value.semester === 2 ? '第二学期' : '第三学期'
          if (
            (value.id && (value.id + '').indexOf(this.searchQuery) >= 0) ||
            (value.year && value.year.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0) ||
            (value.semesterDes && value.semesterDes.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0)
          ) {
            filterTableDataEnd.push(value)
          }
        });
        this.listDataFilterTmp = filterTableDataEnd
      },
      // 改变分页
      handleSizeChange(val) {
        this.p_pageSize = val
        this._initPageInfo()
      },
      handleCurrentChange(val) {
        this.p_currentPage = val
        this.listData = this.listDataFilterTmp.slice((val - 1) * this.p_pageSize, (val) * this.p_pageSize)
      },
      // 根据查询条件查询所有数据
      select: function () {
        if(this.searchQuery.length>45){
          this.$message({
            type:'error',
            message:'搜索长度不能超过45个字符'
          })
          return;
        }
        this._filterData()
        this._initPageInfo()
      },

      addTerm:function(){
        this.form = {};
        this.dialogFormVisible = true;
      },
      // 选择的数据
      handleSelectionChange(val) {
        this.multipleSelection = val;
        // console.log(value.length)
      },
      // 新增
      save: function () {
        var regPos = /^\d+$/
        if (!regPos.test(this.form.year)) {
          this.$message({
            type: 'error',
            message: '学年填写错误'
          })
          return;
        }
        // else if(this.form.year.trim().length >45){
        //   this.$message({
        //     type: 'error',
        //     message: '学年长度不能超过45'
        //   })
        //   return;
        // }
        else if(this.form.year.trim().indexOf("-") >0){
          if(this.form.year.trim().length>9){
            this.$message({
              type: 'error',
              message: '学年长度不能超过9'
            })
            return;
          }
        }else if(this.form.year.trim().length>4){
          this.$message({
            type: 'error',
            message: '学年长度不能超过4'
          })
          return;
        }
        if(!this.form.semester){
          this.$message({
            type: 'error',
            message: '请选择学期'
          })
          return;
        }
        this.form.description = this.form.year + '第' + this.form.semester + '学期'
        post('/admin/semester/creation', this.form).then(res => {
          if (res.status === 200) {
            if(res.data.errorCode == 0){
              this.$message({
                type: 'success',
                message: '添加成功'
              });
              this.dialogFormVisible = false;
              this._getListData();
            } else {
              this.$message({
                type: 'error',
                message: res.data.message
              });
            }
          } else {
            this.$message({
              type:'error',
              message:'添加失败'
            });
            this.dialogFormVisible = false;
          }

        })
      },
      // 获取数据到弹出层
      // showEdit(index) {
      //   console.log(this.listData[index]);
      //   //先查询学期是否存在
      //   let id = this.listData[index].id;
      //   getTearmById(id).then(res => {
      //     if(res.data.data==null){
      //       this.$message({
      //         type:'error',
      //         message:'该学期已经不存在!'
      //       });
      //       this._getListData();
      //     }else{
      //       this.bianjiFormVisible = true;
      //       this.listData[index].semester+="";
      //       Object.assign(this.bianjiForm,this.listData[index]);
      //     }
      //   })
      // },
      // 修改弹出层数据
      updateInfo() {
        var regPos = /^\d+$/
        if (!regPos.test(this.bianjiForm.year)) {
          this.$message({
            type: 'error',
            message: '学年填写错误'
          })
          return;
        }
       else if(this.bianjiForm.year.trim().length>4){
          this.$message({
            type: 'error',
            message: '学年长度不能超过4'
          })
          return;
        }
        updateTearm(this.bianjiForm).then(res => {
          if (res.status === 200) {
            if(res.data.errorCode == 0){
              this.$message({
                type: 'success',
                message: '修改成功'
              });

            } else {
              this.$message({
                type: 'error',
                message: res.data.message
              });
            }

            this._getListData()
          } else {
            this.$message({
              type:'error',
              message:'修改失败'
            })
          }
          this.bianjiFormVisible = false;
        })
      },
      // 删除
      // deletedata(index) {
      //   this.$confirm('此操作将永久删除该文件, 是否继续?', '提示',
      //     {
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     type: 'warning'
      //   }).then((res) => {
      //     let id = this.listData[index].id;
      //     deleteXueQi(id).then(res => {
      //       // console.log(this.listData.id)
      //       if(res.status==200){
      //         if(res.data.errorCode!=0){
      //           // 删除提示语
      //           this.$message({
      //             type: 'error',
      //             message: res.data.message
      //           });
      //         } else {
      //           this._getListData();
      //           // 删除提示语
      //           this.$message({
      //             type: 'success',
      //             message: '删除成功!'
      //           });
      //         }
      //       }
      //     })
      //   }).catch(() => {
      //     this.$message({
      //       type: 'info',
      //       message: '已取消删除'
      //     });
      //   });
      // },
      // 编辑
      updateTerm(){
        // debugger
        if (this.multipleSelection.length === 1) {
          // 开启窗口
          this.bianjiFormVisible= true;
          // var check = this.multipleSelection;
          //   let id =check[v].id;
          // console.log(check)
          // getTearmById(id).then(res => {
          //   if(res.data.data==null){
          //     this.$message({
          //       type:'error',
          //       message:'该学期已经不存在!'
          //     });
          //     this._getListData();
          //   }else{
          //     this.bianjiFormVisible = true;
          //     this.listData[id].semester+="";
          //     Object.assign(this.bianjiForm,this.listData[id]);
          //   }
          // })
          // // 弹出框获取数据
          // Object.assign(this.bianjiForm,this.multipleSelection[0]);
          //先查询学期是否存在
          // let id = this.listData[index].id;
          let id = this.multipleSelection[0].id;
          getTearmById(id).then(res => {
            if(res.data.data==null){
              this.$message({
                type:'error',
                message:'该学期已经不存在!'
              });
              this._getListData();
            }else{
              this.bianjiFormVisible = true;
              this.multipleSelection[0].semester+="";
              // Object.assign(this.bianjiForm,this.listData[0]);
              Object.assign(this.bianjiForm,this.multipleSelection[0]);
            }
          })
          console.log(check)
        }
        else {
          // alert("抱歉,只能修改单行代码")
          this.$message({
            message: '警告哦，只能修改单行数据',
            type: 'warning'
          });
        }
      },
      // 删除
      deleteTerm() {
        // var check = this.multipleSelection;
        // if (check.length == 0)
        //   return this.$message.error('请选择要删除的数据。');
        // for (var v = 0; v<check.length; v++){
        //   debugger
        //   let id =check[v].id;
        //   deleteXueQi(id);
        // }
        // this._getListData();
        // // 删除提示语
        // this.$message({
        //   type: 'success',
        //   message: '删除成功!'
        // });
        if(this.multipleSelection.length==0){
          // alert("请选择数据")
          this.$message({
            message: '警告哦，至少选择一行数据',
            type: 'warning'
          });
        }
        else{
          // debugger
          this.$confirm('此操作将永久删除选中的学期数据, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
          }).then(() => {
            for(let [idx,item] of this.multipleSelection.entries()){
              //console.log(item.id);
              deleteXueQi(item.id).then(res => {
                console.log("------res为-------"+res);
                if(res.status==200){
                  console.log(res.data.errorCode)
                  if(res.data.errorCode!=0){
                    // 删除提示语
                    this.$message({
                      type: 'error',
                      message: res.data.message
                    });
                  } else {
                    if(idx==this.multipleSelection.length-1) {
                      this.$message({
                        message: '删除成功',
                        type: 'success'
                      });
                      this._getListData();
                    }
                  }
                }
              })
            }
          }).catch(() => {
            // this.$message({
            //   type: 'info',
            //   message: '已取消删除'
            // });
          });
        }
      }
    },
    beforeRouteEnter: (to, from, next) => {
      console.log("我是路由，我进入了");
      next(vm => {
        // vm._getExpirementInfo();
        // vm.handleSizeChange(5)
        vm.p_currentPage=1
      });
    }
    // watch监听输入框长度
    // watch: {
    //   searchQuery(){
    //     if(this.searchQuery.length>45){
    //       this.$message({
    //         title:"提示",
    //         message:"输入长度不能超过45",
    //         type:"error"
    //       });
    //     }
    //   }
    // }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus" type="text/stylus" scoped>
  #btn-header .el-form-item:not(:nth-child(1)){
    float: right;
  }
  .xueqi
    #title
      font-size 22px
      margin 20px 3% 0px
      font-family  "Microsoft YaHei"
    .demo-form-inline
      width 94%
      margin 20px auto
      padding-top 20px
      border-top 1px solid #ebeef5 !important
    .el-dialog
      width 30%
    #Table
      width 94%
      height auto
      margin 0px auto
      text-align: center
      th
        .cell
          text-align center
          font-weight: bolder;
          font-size: 1.1em;
    .copyright
      text-align right
      color #b4b4b4
      font-size 14px
      margin-top 20px
      margin-right 3%
</style>
<style type="text/css" >
  .el-table--border th .cell {
    text-align: center;
    font-weight: bolder;
    font-size: 1.1em;
  }
  .xueqi .el-table .cell{
    white-space: nowrap;
    overflow: hidden;
    word-break:break-all;
    text-overflow: ellipsis;
    padding-right: 10px;
  }
</style>
