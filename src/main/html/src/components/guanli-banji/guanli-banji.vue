<template>
  <div class="xueqi">
    <h1>班级管理</h1>
    <el-form :inline="true" id="btn-header"  class="demo-form-inline" @submit.native.prevent>
      <el-form-item >
        <!--<el-button class="button-green" @click="_getCustomers()">刷新</el-button>-->
        <el-button type="primary" @click="_Refresh()">刷新</el-button>
        <el-button class="button-green" @click="showDialog">新增班级</el-button>
        <!--<el-button class="button-green" @click="studentDialog">学生管理</el-button>-->
        <el-button class="button-green" @click="updateClass">编辑班级</el-button>
        <el-button class="button-green" @click="deleteClass">删除班级</el-button>
        <!--&lt;!&ndash;<el-button type="primary" @click="_getCustomers()">刷新</el-button>&ndash;&gt;-->
        <!--<el-button type="primary" @click="showDialog">新增班级</el-button>-->
        <!--<el-button type="primary" @click="studentDialog">学生管理</el-button>-->
        <!--<el-button type="primary" @click="updateClass">编辑班级</el-button>-->
        <!--<el-button type="primary" @click="deleteClass">删除班级</el-button>-->
      </el-form-item>
     <!-- <el-form-item label="搜索" style="float: right;margin-right: 0px">
        <el-input v-model="searchQuery" @keyup.enter.native="select()" placeholder="搜索"></el-input>
      </el-form-item>-->
      <el-form-item style="margin-right: 0px">
        <el-button type="primary" @click="select()" >搜索</el-button>
      </el-form-item>
      <el-form-item >
        <el-input v-model="searchQuery" placeholder="搜索"
                  @keyup.native.enter="select()"></el-input>
      </el-form-item>
    </el-form>
    <!--表格-->
    <div id="Table">
      <el-table
        @row-click="openDetails"
        ref="multipleTable"
        tooltip-effect="dark"
        :border="true"
        :data="listData"
        @selection-change="handleSelectionChange"
        style="width: 100%;">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>

        <el-table-column
          prop="className"
          label="班级名称"
          :resizable="false"
          show-overflow-tooltip>
          <template slot-scope="alldata">
            <p style="white-space:pre-wrap">{{ alldata.row.className }}</p>
          </template>
        </el-table-column>

        <!--<el-table-column-->
          <!--prop="courseName"-->
          <!--:resizable="false"-->
          <!--label="课程名称"-->
          <!--show-overflow-tooltip>-->
          <!--<template slot-scope="alldata">-->
            <!--<p >{{ alldata.row.courseName }}</p>-->
          <!--</template>-->
        <!--</el-table-column>-->

        <!--<el-table-column-->
          <!--prop="term"-->
          <!--:resizable="false"-->
          <!--label="学期"-->
          <!--show-overflow-tooltip>-->
          <!--<template slot-scope="alldata">-->
            <!--<p >{{ alldata.row.term }}</p>-->
          <!--</template>-->
        <!--</el-table-column>-->

        <!--<el-table-column-->
          <!--prop="teacherName"-->
          <!--:resizable="false"-->
          <!--label="授课教师"-->
          <!--show-overflow-tooltip>-->
          <!--<template slot-scope="alldata">-->
            <!--<p >{{ alldata.row.teacherName }}</p>-->
          <!--</template>-->
        <!--</el-table-column>-->

        <!--<el-table-column-->
          <!--prop="teacherContact"-->
           <!--:resizable="false"-->
          <!--label="联系方式"-->
          <!--show-overflow-tooltip>-->
          <!--<template slot-scope="alldata">-->
            <!--<p >{{ alldata.row.teacherContact }}</p>-->
          <!--</template>-->
        <!--</el-table-column>-->

        <el-table-column
          prop="studentNum"
          :resizable="false"
          label="学生数"
          show-overflow-tooltip>
          <template slot-scope="alldata">
            <p >{{ alldata.row.studentNum }}</p>
          </template>
        </el-table-column>
        <el-table-column label="管理" min-width="140" :resizable="false">
          <template slot-scope="alldata">
            <el-button size="mini" @click="studentList(alldata.row)">学生名单</el-button>
            <el-button size="mini" @click="courseList(alldata.row)">课程列表</el-button>
          </template>
        </el-table-column>


        <!--<el-table-column label="操作" min-width="140" :resizable="false">-->
          <!--<template slot-scope="alldata">-->
            <!--<el-button size="mini"  @click="StudentGuanli(alldata.row.classId,alldata.row.courseName)">学生管理</el-button>-->
            <!--<el-button-->
              <!--size="mini"-->
              <!--@click="handleEdit(alldata.$index)">编辑班级</el-button>-->
            <!--<el-button-->
              <!--size="mini"-->
              <!--type="danger"-->
              <!--@click="handleDelete(alldata.$index)">删除班级</el-button>-->
          <!--</template>-->
        <!--</el-table-column>-->
      </el-table>
    </div>
    <div style="clear: both"></div>
    <div style="max-width:100%;overflow: auto">
      <div id="pagination" style="width:35%;margin:20px 20%">
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
    </div>
    <!--版权-->
    <div class="copyright">
      <p>版权信息  版权股所有   悦岚数据服务</p>
    </div>
    <!--新增-->
    <el-dialog :title="title" :visible.sync="dialogFormVisible"
               width="40%" id="editDialog" >
      <el-form :model="form" ref="form" :rules="rules" @submit.native.prevent>
        <el-form-item label="班级名称" prop="className"
                      :label-width="formLabelWidth">
          <el-input v-model="form.className" placeholder="班级名称"
                    @keydown.native="classKeyDown"></el-input>
          <!--@keyup.native.enter="addViseble('form')"-->
        </el-form-item>

        <!--<el-form-item label="学期" prop="termId" :label-width="formLabelWidth" id="AddTerm">-->
<!--&lt;!&ndash;-->
            <!--<el-date-picker style="float: left;width: 180px;"-->
              <!--v-model="form.selectTime1"-->
              <!--type="year"-->
              <!--placeholder="请选择学年"-->
              <!--@change="changeTime">-->
            <!--</el-date-picker>-->


            <!--<el-select  v-model="form.term" placeholder="请选择学期"-->
                        <!--style="float: left;width: 180px;">-->
              <!--<el-option label="第一学期" value="1"></el-option>-->
              <!--<el-option label="第二学期" value="2"></el-option>-->
              <!--<el-option label="第三学期" value="3"></el-option>-->
              <!--<el-option label="第四学期" value="4"></el-option>-->
            <!--</el-select>&ndash;&gt;-->

          <!--<el-select  v-model="form.termId" placeholder="请选择学期">-->
            <!--<el-option v-for="(item, index) in XueQiData"-->
                       <!--:key="item.id"-->
                       <!--:label="item.description"-->
                       <!--:value="item.id" >-->
            <!--</el-option>-->

          <!--</el-select>-->
        <!--</el-form-item>-->

        <!--<el-form-item label="课程选择" prop="courseId" :label-width="formLabelWidth">-->
          <!--<el-select  v-model="form.courseId" placeholder="请选择课程">-->
            <!--&lt;!&ndash;<el-option label="课程1" value="1"></el-option>&ndash;&gt;-->
            <!--&lt;!&ndash;<el-option label="课程2" value="2"></el-option>&ndash;&gt;-->
            <!--<el-option v-for="item in KeChengData"-->
                       <!--:key="item.id"-->
                       <!--:label="item.courseName"-->
                       <!--:value="item.id" >-->
            <!--</el-option>-->
          <!--</el-select>-->
        <!--</el-form-item>-->

        <!--<el-form-item label="授课教师" prop="teacherId" :label-width="formLabelWidth">-->
          <!--<el-select  v-model="form.teacherId" placeholder="请选择授课教师">-->
            <!--&lt;!&ndash;<el-option label="教师1" value="1"></el-option>&ndash;&gt;-->
            <!--&lt;!&ndash;<el-option label="教师2" value="2"></el-option>&ndash;&gt;-->
            <!--<el-option v-for="(item, index) in TeacherData"-->
                       <!--:key="item.id"-->
                       <!--:label="item.teacherName"-->
                       <!--:value="item.id" >-->
            <!--</el-option>-->
          <!--</el-select>-->
        <!--</el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addViseble('form')">保存</el-button>
      </div>
    </el-dialog>
    <!--学生名单弹出框-->
    <el-dialog id="studentListDialog"
      :visible.sync="studentListDialog"
      width="90%" title="学生名单"
      :show-close="true" style="margin-top: -5vh">
      <el-form :inline="true" :model="studentListForm" class="demo-form-inline"
                ref="studentListForm"
                :validate-on-rule-change="false">
        <el-form-item label="学号:" prop="sno1">
            <el-input v-model="studentListForm.sno1"
                      style="width: 120px"
                      @keyup.native.enter="findStu('studentListForm')"></el-input>
        </el-form-item>
        <el-form-item >
          <span>-</span>
        </el-form-item>
        <el-form-item prop="sno2">
            <el-input v-model="studentListForm.sno2"
                      style="width: 120px"
                      @keyup.native.enter="findStu('studentListForm')"></el-input>
        </el-form-item>
        <el-form-item prop="studentName">
            <el-input v-model="studentListForm.studentName"
                      style="width: 160px"
                      @keyup.native.enter="findStu('studentListForm')"></el-input>
        </el-form-item>
        <el-form-item >
            <el-button  type="primary" size="mini" @click="findStu('studentListForm')">搜索</el-button>
          <el-button type="primary" size="mini"   @click="RefreshStu">刷新</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20" >
        <el-col :span="12" >
          <el-tabs v-model="studentActive" type="border-card" id="studentInformation">
            <el-tab-pane label="学生信息库" name="studentFirst" style="overflow: auto;">
              <el-table id="studentTable"
                        :data="studentListTable"
                        :default-sort = "{prop: 'studentNo', order: 'descending'}"
                        :show-header="true"
                        :border="false"
                        ref="studentMultipleTable"
                        @selection-change="studentSelectionChange"
                        style="width: 100%;overflow: auto;">
                <el-table-column
                  type="selection"
                  min-width="140" @change.native="toggleSelection(studentListTable)">
                </el-table-column>
                <el-table-column
                  label="学号"
                  prop="studentNo"
                  sortable
                  width="130"
                  show-overflow-tooltip>
                    <template slot-scope="alldata"  >
                      <el-tooltip placement="top"
                                  :disabled="stuInforDisabled">
                        <div slot="content">{{ alldata.row.studentNoStrshow }}</div>
                          <p v-on:mouseenter="stuInforMouseenter(alldata.$index, alldata.row.studentNoStrshow)">{{ alldata.row.studentNoStr }}</p>
                      </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column
                  label="学生姓名"
                  width="100"
                  show-overflow-tooltip
                  prop="studentName">
                </el-table-column>
                <el-table-column
                  label="年级"
                  show-overflow-tooltip
                  prop="grade">
                </el-table-column>
                <el-table-column
                  label="性别"
                  prop="gender">
                  <template slot-scope="alldata">
                    <p >{{ alldata.row.gender==1 ? '男' : '女' }}</p>
                  </template>
                </el-table-column>
                <el-table-column
                  label="联系方式"
                  show-overflow-tooltip
                  prop="phone">
                </el-table-column>
                <!--<el-table-column-->
                <!--type="selection"-->
                <!--width="55">-->

                <!--</el-table-column>-->
              </el-table>
            </el-tab-pane>
          </el-tabs>
          <el-button type="primary" size="mini" @click="addStudentOfClass" style="float: right;margin-top: 5px;">添加到班级</el-button>
          <!--<el-form :inline="true" :model="studentListBottom" class="demo-form-inline">-->
            <!--<el-form-item label="学生信息库">-->
            <!--</el-form-item>-->
            <!--<el-form-item style="float: right">-->
              <!--<el-button type="primary" size="mini" @click="addStudentOfClass">添加到班级</el-button>-->
            <!--</el-form-item>-->
            <!--&lt;!&ndash;<el-form-item style="float: right">&ndash;&gt;-->
              <!--&lt;!&ndash;<el-checkbox v-model="checked" @change.native="toggleSelection(studentListTable)"  name="type">全选</el-checkbox>&ndash;&gt;-->
            <!--&lt;!&ndash;</el-form-item>&ndash;&gt;-->
          <!--</el-form>-->
        </el-col>
        <el-col :span="12" >
          <el-tabs v-model="classActive" type="border-card" id="classInformation">
            <el-tab-pane label="本班学生名单" name="classFirst">
              <el-table id="classTable"
                :data="classListTable"
                :show-header="true"
                header-align="center"
                style="width: 100%;;overflow: auto;">
                <el-table-column
                  label="学号"
                  width="120"
                  show-overflow-tooltip
                  prop="studentNo">
                  <template slot-scope="alldata"  >
                    <el-tooltip placement="top"
                                :disabled="stuInforDisabled">
                      <div slot="content">{{ alldata.row.studentNoStrshow }}</div>
                      <p v-on:mouseenter="stuInforMouseenter(alldata.$index, alldata.row.studentNoStrshow)">{{ alldata.row.studentNoStr }}</p>
                    </el-tooltip>
                  </template>
                </el-table-column>
                <el-table-column
                  label="学生姓名"
                  width="100"
                  show-overflow-tooltip
                  prop="studentName">
                  <!--<template slot-scope="scope">-->
                    <!--<p >{{ scope.row.studentName }}</p>-->
                  <!--</template>-->
                </el-table-column>
                <el-table-column
                  label="年级"
                  show-overflow-tooltip
                  prop="grade">
                </el-table-column>
                <el-table-column
                  label="性别"
                  prop="gender">
                  <template slot-scope="scope">
                    <p >{{ scope.row.gender==1 ? '男' : '女' }}</p>
                  </template>
                </el-table-column>
                <el-table-column
                  label="联系方式"
                  show-overflow-tooltip
                  prop="phone">
                </el-table-column>
                <el-table-column  label="操作">
                  <template slot-scope="scope">
                    <el-button
                      size="mini"
                      type="danger" @click="deleteStu(scope)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>

          <!--<el-form :inline="true" :model="studentListBottom2" class="demo-form-inline">-->
            <!--<el-form-item label="本班学生名单">-->
            <!--</el-form-item>-->
          <!--</el-form>-->
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="saveStudent">保存</el-button>
    <el-button @click="CancelStuBtn">取 消</el-button>
  </span>
    </el-dialog>
    <!--课程列表弹出框-->
    <el-dialog
      :visible.sync="courseDialog"
      :show-close="true"
      id="courseDialog"  title="课程列表"
      width="45%" style="margin-top: -5vh">
      <el-form  :model="courseListModel" label-width="70px">
        <el-form-item label="课程名称">
          <el-select v-model="courseListModel.courseId"
                     placeholder="请选择课程名称"
                     filterable
                     @keyup.native.enter="addCourseList(courseListModel)"
                      style="width: 230px;">
            <el-option v-for="item in KeChengData"
                       :key="item.id"
                       :label="item.courseName"
                       :value="item.id" >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学期">

            <!--<el-select v-model="courseListModel.term" placeholder="请选择学年">-->
              <!--<el-option v-for="(item, index) in XueQiData"-->
                         <!--:key="item.id"-->
                         <!--:label="item.description"-->
                         <!--:value="item.id" >-->
              <!--</el-option>-->
            <!--</el-select>-->
            <el-date-picker
              v-model="courseListModel.selectTime2"
              type="year" style="float: left;width: 230px;"
              placeholder="2017,代表'2017-2018'学年"
              :editable="false"
              value-format="yyyy">
            </el-date-picker>


            <!--<el-select v-model="courseListModel.termId"-->
                       <!--placeholder="请选择学期"-->
                       <!--@keyup.native.enter="addCourseList(courseListModel)">-->
              <!--<el-option v-for="(item, index) in XueQiData"-->
                         <!--:key="item.id"-->
                         <!--:label="item.description"-->
                         <!--:value="item.id" >-->
              <!--</el-option>-->
            <!--</el-select>-->
            <el-select  v-model="courseListModel.semester" placeholder="请选择学期" style="float: left;width: 127px;margin-left: 8px;">
              <el-option label="第一学期" value="1"></el-option>
              <el-option label="第二学期" value="2"></el-option>
              <el-option label="第三学期" value="3"></el-option>
              <el-option label="第四学期" value="4"></el-option>
            </el-select>


        </el-form-item>
        <el-form-item label="授课老师">
          <el-select v-model="courseListModel.teacherId"
                     placeholder="请选择授课老师"
                     filterable
                     @keyup.native.enter="addCourseList(courseListModel)"
                    style="width: 230px;">
            <el-option v-for="(item, index) in TeacherData"
                       :key="item.id"
                       :label="item.teacherName"
                       :value="item.id" >
            </el-option>
          </el-select>
          <el-button type="primary" size="mini" style="margin-left: 5px;" @click="addCourseList(courseListModel)">添加到课程列表</el-button>
        </el-form-item>
      </el-form>
      <template>
        <el-tabs v-model="activeCourse" type="border-card" >
          <el-tab-pane label="班级课程列表" name="first" >
            <template >
              <el-table id="classCourseList"
                :data="courseListTable"
                :show-header="true"
                style="width: 100%;text-align: center;overflow: auto;">
                <el-table-column
                  label="课程名称"
                  show-overflow-tooltip
                  prop="courseName">
                  <!--<template slot-scope="scope">-->
                    <!--<p >{{ scope.row.courseName}}</p>-->
                  <!--</template>-->
                </el-table-column>
                <el-table-column
                  label="学期"
                  show-overflow-tooltip
                  prop="term">
                  <template slot-scope="scope">
                    <p >{{ scope.row.schoolYear}} {{ scope.row.semester=='1' ? '第一学期' : scope.row.semester=='2' ? '第二学期' : scope.row.semester=='3' ? '第三学期' : scope.row.semester=='4' ? '第四学期' : '' }}</p>
                  </template>
                </el-table-column>
                <el-table-column
                  label="授课老师"
                  show-overflow-tooltip
                  prop="teacherName">
                  <!--<template slot-scope="scope">-->
                    <!--<p >{{ scope.row.teacherName}}</p>-->
                  <!--</template>-->
                </el-table-column>
                <el-table-column label="操作">
                  <template slot-scope="scope">
                    <el-button
                      size="mini"
                      type="danger" @click="removeCourse(scope)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </template>
          </el-tab-pane>
        </el-tabs>
      </template>
      <span slot="footer" class="dialog-footer">
         <el-button type="primary" @click="saveClassCourse">保存</el-button>
          <el-button @click="courseDialog = false">取 消</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script type="text/ecmascript-6">
  import {
    addBanKe,
    addClassCourseList,
    addStudentList,
    deleteBancheng,
    deleteClassCourseList,
    deleteStudentList,
    getbanji,
    getCourseByClassId,
    getCustomers,
    getKecheng,
    getStudentByNotClass,
    getStudentInfo,
    getXueQiListData,
    updateBanke
  } from '@/common/js/request';

  export default{
    data(){
      //检验班级名称
      var validateClassName = (rule, value, callback) => {
        if (typeof value == 'undefined' || value =="") {
          callback(new Error('请输入班级名称'));
        } else if(value.trim().length > 45){
          callback(new Error('班级名称长度不能超过45'));
        }
        // else if((/^\s+/).test(value)){
        //   callback(new Error('开头不能输入空格'));
        // }
        // /^\s+|\s+$/g
          else if((/\s+/g).test(value)){
          callback(new Error('班级名称不能输入空格'));
        }
        // else if((/\s+$/).test(value)){
        //   callback(new Error('结尾不能输入空格'));
        // }
        else {
          callback();
        }
      };
      // var validateSno1 = (rule, value, callback) => {
      //   if (typeof value != 'undefined' && value.trim().length>0) {
      //       var regex = /^\+?[0-9]\d*$/;
      //       if(!regex.test(value.trim())) {
      //         this.$message({
      //           type: 'error',
      //           message: '学生学号只能为大于或等于0的整数!'
      //         });
      //       }
      //       return false
      //   }
      // };
      // var validateSno2 = (rule, value, callback) => {
      //   if (typeof value != 'undefined' && value.trim().length>0) {
      //     var regex = /^\+?[0-9]\d*$/;
      //     if(!regex.test(value.trim())) {
      //       this.$message({
      //         type: 'error',
      //         message: '学生学号只能为大于或等于0的整数!'
      //       });
      //     }
      //     return false
      //   }
      // };
      // var validateStudentName = (rule, value, callback) => {
      //   if (typeof value != 'undefined' && value.trim().length>0) {
      //     if(value.trim().length>45){
      //       this.$message({
      //         type: 'error',
      //         message: '搜索长度不能超过45个字符!'
      //       });
      //     }
      //     return false
      //   }
      // };
      return{
        form: {
          className: '',
          termId: 1, // 学期
          courseId: 1, // 课程选择
          teacherId: 1, // 授课教师
          classId: 1,
          selectTime1:'',
        },
        rowData: {},
        formIsChange: false,
        isChangeCount: 0,
        listData: [],
        listDataTmp: [],
        listDataFilterTmp: [], // 获取过滤后所有的数据
        classListTableTmp:[],
        XueQiData:[],//学期数据
        KeChengData:[],//课程数据
        TeacherData:[],//老师数据
        dialogFormVisible: false,
        formLabelWidth: '80px',
        fileList: [{name: 'food.jpeg', url: ''}],
        currentId: '',
        searchQuery:'',//查询数据,
        isAdd: true,
        thisClassId:'',
        updateDialog: false, // 更新选课弹出框
        // 分页参数
        p_currentPage: 1,
        p_pageSizes: [5, 10, 15, 20, 30, 50, 100],
        p_pageSize: 5,
        p_total: 0,
        rules: {
          className: [
            // {required: true, message: '请输入班级名称', trigger: 'blur'},
            // {min: 1, max: 45, message: '长度在 1 到 45个字符', trigger: 'blur'}
            { required: true,validator: validateClassName, trigger: 'blur' }
          ],
          schoolYear: [
            { required: true, message: '请选择学年', trigger: 'change' }
          ],
          semester: [
            { required: true, message: '请选择学期', trigger: 'change' }
          ],
          courseId: [
            { required: true, message: '请选择课程', trigger: 'change' }
          ],
          teacherId: [
            { required: true, message: '请选择授课老师', trigger: 'change' }
          ],
        },
        // studentRules:{
        //   sno1:[
        //     { required: true,validator: validateSno1, trigger: 'manual' }
        //   ],
        //   sno2:[
        //     { required: true,validator: validateSno2, trigger: 'manual' }
        //   ],
        //   studentName:[
        //     { required: true,validator: validateStudentName, trigger: 'manual' }
        //   ]
        // },

        title:null,
        multipleSelection:[],
        studentMultipleSelection:[],
        studentId1:'',//学号1
        studentId2:'',//学号2
        studentListDialog:false,
        studentListForm:{},
        //学生信息库
        studentListTable:[
          {
            studentNo: '',
            studentName: '',
            gender: '',
            phone:''
          }
        ],
        //本班学生名单
        classListTable:[
          {
            studentNo: '',
            studentName: '',
            gender: '',
            phone:''
          }

        ],
        studentListTableFilterTmp:[],
        studentListBottom:{},
        studentListBottom2:{},
        courseDialog:false,
        courseListModel:{},
        activeCourse:'first',
        courseListTable:[
          {
            course:'',
            term:'',
            name:''
          }
        ],//课程列表
        courseListTmp:[],
        checked: false,
        studentActive: 'studentFirst',
        classActive:'classFirst',
        selectTime2:'',
        stuInforDisabled:false//学生名单tooltip是否禁用
      }
    },
    watch: {
      form: {
          handler :function(val){
            var newClassnName = val.className;
            var newTermId = val.termId;
            var newCourseId = val.courseId;
            var newTeacherId = val.teacherId;

            if(this.rowData.className != newClassnName) {
              this.formIsChange = true;
              return;
            }
            if(this.rowData.termId != newTermId) {
              this.formIsChange = true;
              return;
            }
            if(this.rowData.courseId  != newCourseId) {
              this.formIsChange = true;
              return;
            }
            if(this.rowData.teacherId != newTeacherId) {
              this.formIsChange = true;
              return;
            }
            this.formIsChange = false;
          },
        deep : true

      }

    },
    activated () {
      this.searchQuery="";
      this.GetXueQi(); // 学期数据
      this.GetKeCheng(); // 课程数据
      this.GetTeacher(); // 老师数据
      this._getCustomers();
    },
    methods: {
      _Refresh:function () {
        this.searchQuery="";
        this.GetXueQi(); // 学期数据
        this.GetKeCheng(); // 课程数据
        this.GetTeacher(); // 老师数据
        this._getCustomers();
        // 点击刷新默认显示每页5条数据
        this.p_pageSize=5
      },
      openDetails(row) {
        this.rowData = row;
      },
      // handleSchollInput(e){
      //   this.schollVal=e.target.value.replace(/[^\d]/g,'');
      // },
      showDialog:function(){
        this.form={};
        this.isAdd = true;
        this.dialogFormVisible = true;
        this.title="新增班级";
        this.$refs.form.resetFields()
      },
      // 获取数据
      _getCustomers: function() {
        console.log("刷新");
        getbanji().then(res => {
          this.listDataTmp = res.data.data.classInfoList;
            // this.listDataTmp.map(item => {
            //   if (this.listDataTmp.term.match("-") != -1) {
            //     this.listDataTmp.term = this.listDataTmp.term
            //   } else if (this.listDataTmp.term.match("-") == -1) {
            //     this.listDataTmp.term = (parseInt(this.listDataTmp.term)) + "-" + (parseInt(this.listDataTmp.term) + 1) + this.listDataTmp.term.substr(this.listDataTmp.term.length - 4, 4);
            //   }
            // }
         /* this.listDataTmp.map(item =>{
            if(item.term.search("-") != -1 ){

              item.term= item.term
            }
            else if(item.term.search("-") == -1){
              item.term = (parseInt(item.term))+"-"+(parseInt(item.term)+1)+item.term.substr(item.term.length-4,4);
            }
            // console.log(typeof item.year)
          })*/
         // console.log(this.listDataTmp);
          this._filterData()
          this._initPageInfo()
          //console.log(this.listDataTmp.termId)
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
        let filterTableDataEnd = [];
        this.listDataTmp.forEach((value, index) => {
          if (
            (value.className && value.className.toLowerCase().indexOf(this.searchQuery.toLowerCase()) >= 0) ||
            (value.studentNum && (value.studentNum + '').indexOf(this.searchQuery) >= 0)

          ) {
            filterTableDataEnd.push(value)
          }
        });
        this.listDataFilterTmp = filterTableDataEnd
      },
      // 改变分页
      handleSizeChange (val) {
        this.p_pageSize = val
        this._initPageInfo()
      },
      handleCurrentChange (val) {
        this.p_currentPage = val
        this.listData = this.listDataFilterTmp.slice((val - 1) * this.p_pageSize, (val) * this.p_pageSize)
      },
      // 根据查询条件查询所有数据
      select: function () {
        if(this.searchQuery.length>45){
          this.$message({
            type:'error',
            message:'搜索长度不能超过45个字符'
          });
          return;
        }

        // 点击搜索，默认显示每页5条数据
        this.p_pageSize=5

        this._filterData();
        this._initPageInfo();
      },

      // 获取学期数据
      GetXueQi:function () {
        getXueQiListData().then(res=>{
          /*this.XueQiData=res.data.data.semesterList;
          this.XueQiData.map(item =>{
            // if(typeof item.year === 'undefined'){
            //   item.description = item.year+"-"+(parseInt(item.year)+1)+item.description;
            // }else{
            //   item.description = item.year+item.description;
            // }

            if(item.description.search("-") != -1 ){
              // item.description = item.year+item.description;
              // console.log(item.description)
              //item.description=item.description
              //console.log(item.description)
              //console.log(this.form)
             this.form.term= item.description
              console.log(this.form.term)
            }
            else if(item.description.search("-") == -1){
              item.description = (parseInt(item.description))+"-"+(parseInt(item.description)+1)+item.description.substr(item.description.length-4,4);
              //console.log(item.description)
              // 将选择的数据传输给表单
              this.form.term= item.description
            }
          })*/
        })
      },
      // 获取课程数据
      GetKeCheng:function () {
        getKecheng().then(res=>{
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          this.KeChengData=res.data.data.courseInfoList;

        })
      },
      // 获取老师数据
      GetTeacher:function () {
        getCustomers().then(res=>{
          if(res.data=="alreadyResetPassword"){
            this.$message({
              type:'warning',
              message:'请重新登录'
            });
            return;
          }
          if(res.data.errorCode==0) {
            this.TeacherData = res.data.data.teacherInfoList;
          }

        })
      },
      // 编辑选课
      addViseble(form) {
        this.$refs[form].validate((valid) => {
          if (valid) {
            if(this.isAdd) {
              console.log(this.form);
              addBanKe(this.form).then(res => {
                if (res.data.errorCode == 0) {
                  this.$message({
                    type:'success',
                    message:'添加成功'
                  });
                  this._Refresh();
                  //this._getCustomers();
                }else{
                  this.$message({
                    type: 'error',
                    message: res.data.message
                  });
                  this._Refresh();
                }
                this.dialogFormVisible = false;
              })
            } else {
              updateBanke(this.form).then(res=>{
                if(res.data.errorCode==0){
                  this.$message({
                    type:'success',
                    message:'修改成功'
                  });
                  this._Refresh();
                  //this._getCustomers();
                }else {
                  this.$message({
                    type: 'error',
                    message: res.data.message
                  });
                  this._Refresh();
                }
                this.dialogFormVisible = false;
              });
              this.formIsChange = false;
            }
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      // handleEdit(index){
      //   var data = this.listData[index];
      //   Object.assign(this.form,data);
      //   this.isAdd = false;
      //   this.dialogFormVisible = true;
      //   this.title="编辑班级"
      // },
      // 删除
      // handleDelete(index) {
      //   this.$confirm('此操作将永久删除该班级, 是否继续?', '提示', {
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     type: 'warning'
      //   }).then(() => {
      //     let id = this.listData[index].classId;
      //     deleteBancheng(id).then(res => {
      //       // console.log(this.listData.id)
      //       if(res.status==200){
      //         if(res.data.errorCode!=0){
      //           // 删除提示语
      //           this.$message({
      //             type: 'error',
      //             message: res.data.message
      //           });
      //         } else {
      //           this._getCustomers();
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
      // 更新选课
      updateEdit() {
        this.updateDialog=true
      },
      // 学生管理
      // StudentGuanli(classId,courseName){
      //   this.$router.push({ path :'/ClassDetail/'+classId+'/'+courseName});
      // },
      // 学生信息库选择的数据
      studentSelectionChange(val){
        this.studentMultipleSelection=val
      },
      // 选择的数据
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      //表格外面--学生管理
      studentDialog(){
        if (this.multipleSelection.length === 1) {
          // alldata.row.classId,alldata.row.courseName
          let classId= this.multipleSelection[0].classId
          let courseName=this.multipleSelection[0].courseName
          this.$router.push({ path :'/ClassDetail/'+classId+'/'+courseName});
        }else{
          this.$message({
            message: '警告哦，只能选择单行数据',
            type: 'warning'
          });
        }
      },
      //表格外面--编辑班级
      updateClass(){
        // this.$refs.form.resetFields()
        if(this.$refs.form !=undefined){
          this.$refs.form.clearValidate()
        }
        if (this.multipleSelection.length === 1) {
          var selectData = this.multipleSelection[0];
          Object.assign(this.form,selectData);
          this.isAdd = false;
          this.dialogFormVisible = true;
          //
          this.title="编辑班级"

          // var data = this.listData[index];
          // Object.assign(this.form,data);
          // this.isAdd = false;
          // this.dialogFormVisible = true;
          // this.title="编辑班级"
        }else{
          this.$message({
            message: '警告哦，只能编辑单行数据',
            type: 'warning'
          });
        }
      },
      //表格外面--删除班级
      deleteClass(){
        if(this.multipleSelection.length==0){
          this.$message({
            message: '警告哦，至少选择一行数据',
            type: 'warning'
          });
        } else{
          this.$confirm('此操作将永久删除选中的班级数据, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
          }).then(() => {
            var multi = JSON.parse(JSON.stringify(this.multipleSelection));
              this._deleteBancheng(multi);
          }).catch(() => {
            // this.$message({
            //   type: 'info',
            //   message: '已取消删除'
            // });
          });
        }
      },
      _deleteBancheng:function (multi) {
        console.log(multi+"aaa");
        deleteBancheng(multi.splice(-1)[0].classId).then(res => {
          console.log("------res为-------"+res);
          if(res.status==200){
            if(res.data.errorCode!=0){
              // 删除提示语
              this.$message({
                type: 'error',
                message: res.data.message
              });
              this._Refresh();
              return;
            } else {
              console.log("长度"+multi.length)
              if(multi.length==0) {
                this.$message({
                  message: '删除成功',
                  type: 'success'
                });
                this._Refresh();
               // this._getCustomers();
              }
              if(multi.length==0){
                console.log("没有数据");
                return;
              }
              this._deleteBancheng(multi);
            }
          }
        })
      },
      // 学生名单
      studentList(row){
        this.studentListForm={};
        this.thisClassId=row.classId;
        console.log("classId为:"+this.thisClassId)
        //查询不在本班级的学生名单
        this._getStudentByNotThisClass(row.classId);
        //查询本班级的学生名单
        this._getStudentByClass(row.classId);
        this.studentListDialog=true;
        //最初本班级的学生
        getStudentInfo(row.classId).then(res=>{
          this.classListTableTmp=res.data.data.studentList;
        });
      },
      //查询全部学生
      _getStudentByNotThisClass:function (classId) {
        getStudentByNotClass(classId).then(res=>{
          this.studentListTable=res.data.data.studentList;
          this.studentListTable.forEach((value) =>{
            // 显示的值
            value.studentNoStr=value.studentNo;
            value.studentNoStrshow=value.studentNo;
            if(value.studentNoStr.length>10){
              var studentNoStr=value.studentNoStr.substr(0,10)+"..."
              value.studentNoStr=studentNoStr
            }else{
              value.studentNoStr=value.studentNoStr
            }
            value.studentNo=parseFloat(value.studentNo)
            if(typeof value.grade !="object"){
              // if(form.teacherContact.indexOf("@") != -1){
              //判断年级后缀没有“级”
              if(value.grade.indexOf("级") ==-1){
                value.grade = value.grade+"级"
              }
            }
          });
          var studentFilter=JSON.parse(JSON.stringify(this.studentListTable));
          this.studentListTableFilterTmp=studentFilter;

        });
      },
      //查询本班学生
      _getStudentByClass:function (classId) {
        getStudentInfo(classId).then(res=>{
          this.classListTable=res.data.data.studentList;
          this.classListTable.forEach((value) =>{
            // 显示的值
            value.studentNoStr=value.studentNo;
            value.studentNoStrshow=value.studentNo;
            if(value.studentNoStr.length>10){
              var studentNoStr=value.studentNoStr.substr(0,10)+"..."
              value.studentNoStr=studentNoStr
            }else{
              value.studentNoStr=value.studentNoStr
            }
            value.studentNo=parseFloat(value.studentNo)
            if(typeof value.grade !="object"){
              // if(form.teacherContact.indexOf("@") != -1){
              //判断年级后缀没有“级”
              if(value.grade.indexOf("级") ==-1){
                value.grade = value.grade+"级"
              }
            }
            // console.log("为:"+value.grade)
            // if(typeof value.grade !="object"){
            //   // if(form.teacherContact.indexOf("@") != -1){
            //   //判断年级后缀没有“级”
            //   if(value.grade.indexOf("级") ==-1){
            //     value.grade = value.grade+"级"
            //   }
            // }
          })
        });
      },
      //取消保存
      CancelStuBtn:function () {
        this.studentListTable=[];
        this.studentListTableFilterTmp=[];
        this.classListTableTmp=[];
        this.classListTable=[];
        this.studentListDialog = false;
      },
      //保存按钮
      saveStudent:function () {
        var oldStuList=[];
       this.classListTableTmp.forEach((value1, index) => {
         this.classListTable.forEach((value2,index)=>{
          if(value1.id==value2.id){
            oldStuList.push(value1);
            return false;
          }
         })
       });
       //最初班级人集合去掉相等的就是需要添加的
        if(oldStuList!=null && oldStuList.length>0){
          for(let[idx,item] of oldStuList.entries()){
            for(let[idx1,item1] of this.classListTableTmp.entries()){
              if(item.id==item1.id){
                this.classListTableTmp.splice(idx1,1);
              }
            }
            for(let[idx2,item2] of this.classListTable.entries()){
              if(item.id==item2.id){
                this.classListTable.splice(idx2,1);
              }
            }
          }
        }
        //删除学生的集合
        if(this.classListTableTmp.length>0){
          for(let[idx,item] of this.classListTableTmp.entries()){
            this.classListTableTmp[idx].classId=this.thisClassId;
            this.classListTableTmp[idx].studentId=item.id;
          }
          deleteStudentList(this.classListTableTmp).then(res=>{
            console.log(res);
            if(res.data.errorCode!=0){
              this.$message({
                type: 'error',
                message: res.data.message
              });
            }else{
              this._getCustomers();
            }
          })
        }

        //添加学生的集合
        if(this.classListTable.length>0){
          for(let[idx,item] of this.classListTable.entries()){
            this.classListTable[idx].classId=this.thisClassId;
          }
          addStudentList(this.classListTable).then(res=>{
            if(res.data.errorCode!=0){
              this.$message({
                type: 'error',
                message: res.data.message
              });
            }else{
              this._getCustomers();
            }
          });
        }

          this.$message({
            type:'success',
            message:'保存成功!'
          });
          this.studentListDialog=false;
          this.studentListTable=[];
          this.studentListTableFilterTmp=[];
      },
      //删除本班学生
      deleteStu:function (scope) {
        this.classListTable.splice(scope.$index,1);
        this.studentListTable.push(scope.row);
        this.studentListTableFilterTmp.push(scope.row);
      },
      RefreshStu:function () {
        this.studentListForm={};
        var filterTmp = JSON.parse(JSON.stringify(this.studentListTableFilterTmp));
        this.studentListTable=filterTmp;
        this.$refs.studentMultipleTable.clearSelection();
      },
      //添加学生到班级里
      addStudentOfClass:function () {
        console.log(this.studentMultipleSelection.length);
        if(this.studentMultipleSelection.length==0){
          this.$message({
            message: '警告哦，至少选择一行数据',
            type: 'warning'
          });
        }else{
          for(let[idx1,item1] of this.studentMultipleSelection.entries()){
            item1.studentNo=item1.studentNoStrshow
            this.classListTable.push(item1);
            for(let[idx2,item2] of this.studentListTable.entries()){
              if(item1.id==item2.id){
                this.studentListTable.splice(idx2,1);
              }
            }
            for(let[idx3,item3] of this.studentListTableFilterTmp.entries()){
              if(item1.id==item3.id){
                this.studentListTableFilterTmp.splice(idx3,1);
              }
            }

          }
        }
      },
      _validateForm: function (studentListForm) {
        // ------------------------------------------

        // 判断开始学号不能为空
        let errorMsg = null
        if (studentListForm.sno1!=undefined && studentListForm.sno1.trim().length>0) {
          var regex = /^\+?[0-9]\d*$/;
          if(!regex.test(studentListForm.sno1.trim())){
            errorMsg = "学生学号只能为大于或等于0的整数";
          }
        }
        if (errorMsg && errorMsg.trim().length > 0) {
          this.$message.error(errorMsg)
          return false
        }

        // 判断结尾学号不能为空
        if (studentListForm.sno2!=undefined && studentListForm.sno2.trim().length>0) {
          var regex = /^\+?[0-9]\d*$/;
          if(!regex.test(studentListForm.sno2.trim())){
            errorMsg = "学生学号只能为大于或等于0的整数";
          }
        }
        if (errorMsg && errorMsg.trim().length > 0) {
          this.$message.error(errorMsg)
          return false
        }

        // 判断学生姓名不能为空
        if (studentListForm.studentName!=undefined && studentListForm.studentName.trim().length>0) {
          if(studentListForm.studentName.trim().length>45){
            errorMsg = "搜索长度不能超过45个字符";
          }
        }
        if (errorMsg && errorMsg.trim().length > 0) {
          this.$message.error(errorMsg)
          return false
        }

        return true
      },
      //搜索学生
      findStu:function () {
        if (!this._validateForm(this.studentListForm)) {
          return;
        }
        // console.log(this.studentListTableFilterTmp);
        // console.log(this.studentListTable);
        // 输入框校验
        // this.$refs[studentListForm].validate((valid) => {
        //   if (valid) {
        //     // alert('submit!');
        //    console.log("校验成功")
        //
        //   } else {
        //     // console.log('error submit!!');
        //     console.log("校验失败")
        //     return false;
        //   }
        // });
        console.log("aaa")
        if((this.studentListForm.sno1==undefined || this.studentListForm.sno1.trim().length==0) &&
          (this.studentListForm.sno2==undefined || this.studentListForm.sno2.trim().length==0) &&
          (this.studentListForm.studentName==undefined || this.studentListForm.studentName.trim().length==0)){
          this.studentListTable=this.studentListTableFilterTmp;
          return;
        }
        let filterTableDataEnd = [];
        this.studentListTableFilterTmp.forEach((value, index) => {
          var genderName = '';
          if(value.gender == 1){
            genderName = '男';
          }else if(value.gender == 2){
            genderName = '女';
          }
          //三个输入框 都输入
          if(
            (this.studentListForm.sno1!=undefined && this.studentListForm.sno1.trim().length>0) &&
            (this.studentListForm.sno2!=undefined && this.studentListForm.sno2.trim().length>0) &&
            (this.studentListForm.studentName!=undefined && this.studentListForm.studentName.trim().length>0)
          ) {
            if (
              (value.studentNoStrshow && parseInt(value.studentNoStrshow) >= parseInt(this.studentListForm.sno1)) &&
              (value.studentNoStrshow && parseInt(value.studentNoStrshow) <= parseInt(this.studentListForm.sno2)) &&
              (
                (value.studentName!=undefined && value.studentName.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)||
                (genderName && genderName.indexOf(this.studentListForm.studentName) >= 0) ||
                (value.studentNoStrshow!=undefined && value.studentNoStrshow.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)||
                (value.grade!=undefined && value.grade.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)||
                (value.phone!=undefined && value.phone.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)
              )
            ) {
              filterTableDataEnd.push(value)
            }
          }
          //输入前面两个学号 搜索框为空
          if(
            (this.studentListForm.sno1!=undefined && this.studentListForm.sno1.trim().length>0) &&
            (this.studentListForm.sno2!=undefined && this.studentListForm.sno2.trim().length>0) &&
            (this.studentListForm.studentName==undefined || this.studentListForm.studentName.trim().length==0)
          ){
            if (
              (value.studentNoStrshow && parseInt(value.studentNoStrshow) >= parseInt(this.studentListForm.sno1)) &&
              (value.studentNoStrshow && parseInt(value.studentNoStrshow) <= parseInt(this.studentListForm.sno2))
            ) {
              filterTableDataEnd.push(value)
            }
          }
          //只输入前面一个学号 搜索框为空
          if(
            (this.studentListForm.sno1!=undefined && this.studentListForm.sno1.trim().length>0) &&
            (this.studentListForm.sno2==undefined || this.studentListForm.sno2.trim().length==0) &&
            (this.studentListForm.studentName==undefined || this.studentListForm.studentName.trim().length==0)
          ){
             if(value.studentNoStrshow && parseInt(value.studentNoStrshow) >= parseInt(this.studentListForm.sno1)){
               filterTableDataEnd.push(value)
             }
          }

          //只输入后面一个学号  搜索框为空
          if(
            (this.studentListForm.sno1==undefined || this.studentListForm.sno1.trim().length==0) &&
            (this.studentListForm.sno2!=undefined && this.studentListForm.sno2.trim().length>0) &&
            (this.studentListForm.studentName==undefined || this.studentListForm.studentName.trim().length==0)
          ){
            if(value.studentNoStrshow && parseInt(value.studentNoStrshow) <= parseInt(this.studentListForm.sno2)){
              filterTableDataEnd.push(value)
            }
          }
          //只输入前面一个学号  搜索框不为空
          if(
            (this.studentListForm.sno1!=undefined && this.studentListForm.sno1.trim().length>0) &&
            (this.studentListForm.sno2==undefined || this.studentListForm.sno2.trim().length==0) &&
            (this.studentListForm.studentName!=undefined && this.studentListForm.studentName.trim().length>0)
          ){
            if (
              (value.studentNoStrshow && parseInt(value.studentNoStrshow) >= parseInt(this.studentListForm.sno1)) &&
              (
                (value.studentName!=undefined && value.studentName.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)||
                (genderName && genderName.indexOf(this.studentListForm.studentName) >= 0) ||
                (value.studentNoStrshow!=undefined && value.studentNoStrshow.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)||
                (value.grade!=undefined && value.grade.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)||
                (value.phone!=undefined && value.phone.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)
              )
            ) {
              filterTableDataEnd.push(value)
            }
          }
          //只输入后面一个学号  搜索框不为空
          if(
            (this.studentListForm.sno1==undefined || this.studentListForm.sno1.trim().length==0) &&
            (this.studentListForm.sno2!=undefined && this.studentListForm.sno2.trim().length>0) &&
            (this.studentListForm.studentName!=undefined && this.studentListForm.studentName.trim().length>0)
          ){
            if (
              (value.studentNoStrshow && parseInt(value.studentNoStrshow) <= parseInt(this.studentListForm.sno2)) &&
              (
                (value.studentName!=undefined && value.studentName.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)||
                (genderName && genderName.indexOf(this.studentListForm.studentName) >= 0) ||
                (value.studentNoStrshow!=undefined && value.studentNoStrshow.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)||
                (value.grade!=undefined && value.grade.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)||
                (value.phone!=undefined && value.phone.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)
              )
            ) {
              filterTableDataEnd.push(value)
            }
          }
          //只输入搜索框
          if(
            (this.studentListForm.sno1==undefined || this.studentListForm.sno1.trim().length==0) &&
            (this.studentListForm.sno2==undefined || this.studentListForm.sno2.trim().length==0) &&
            (this.studentListForm.studentName!=undefined && this.studentListForm.studentName.trim().length>0)
          )
          {
            if(
              (value.studentName!=undefined && value.studentName.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)||
              (genderName && genderName.indexOf(this.studentListForm.studentName) >= 0) ||
              (value.studentNoStrshow!=undefined && value.studentNoStrshow.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)||
              (value.grade!=undefined && value.grade.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)||
              (value.phone!=undefined && value.phone.toLowerCase().indexOf(this.studentListForm.studentName.trim().toLowerCase()) >= 0)
            ) {
              filterTableDataEnd.push(value)
            }
          }
        });
        this.studentListTable = filterTableDataEnd;
      },
      // 课程列表
      courseList(row){
        this.courseListModel={};
        this.courseDialog=true;
        this.thisClassId=row.classId;
        this._getCourseByClassId(row.classId);
        //最初本班级的课程
        getCourseByClassId(row.classId).then(res=>{
            this.courseListTmp=res.data.data.teacherClassList;
          // console.log( this.courseListTmp);
          });
      },
      //查询所有课程列表
      _getCourseByClassId:function(classId){
        getCourseByClassId(classId).then(res=>{
          this.courseListTable=res.data.data.teacherClassList;
          // console.log( this.courseListTable);
        });
      },
      //添加到课程列表中
      addCourseList:function (courseListModel) {
        console.log(this._valiCourse(courseListModel));
        if(this._valiCourse(courseListModel)){
          var courseId = courseListModel.courseId;
          var teacherId=courseListModel.teacherId;
          var selectTime=courseListModel.selectTime2;
          var schoolYear=parseInt(selectTime)+"-"+parseInt(parseInt(selectTime)+1);
          var courseObj = this.KeChengData.find( item =>{
            return item.id == courseId;
          });
          var teacherObj = this.TeacherData.find( item =>{
            return item.id == teacherId;
          });
          var courseName = courseObj.courseName;
          if(this.courseListTable.length>0){
            var courseList=[];
            Object.assign(courseList,this.courseListTable);
            var a=1;
            for(let[idx,value] of courseList.entries()){
              console.log(value.courseName);
              if(value.courseId==courseId){
                this.$message({
                  type:'error',
                  message:'已经存在该课程'
                });
                a=2;
                break;
              }
            }
            if(a==1){
              console.log("ccc");
              courseListModel.courseName=courseName;
              courseListModel.teacherName=teacherObj.teacherName;
              courseListModel.schoolYear=schoolYear;
              courseListModel.classId=this.thisClassId;
              courseListModel.teacherId=teacherId;
              this.courseListTable.push(courseListModel);
              console.log(this.courseListTable);
            }
          }else{
            console.log("aaa");
            courseListModel.courseName=courseName;
            courseListModel.teacherName=teacherObj.teacherName;
            courseListModel.schoolYear=schoolYear;
            courseListModel.classId=this.thisClassId;
            courseListModel.teacherId=teacherId;
            this.courseListTable.push(courseListModel);
          }
          this.courseListModel={};
        }
      },
      //删除课程
      removeCourse:function (scope) {
        this.courseListTable.splice(scope.$index,1)

      },

      //验证是否课程
      _valiCourse:function (courseListModel) {
        console.log(courseListModel);
        if(courseListModel.courseId==undefined || courseListModel.courseId==""){
          this.$message({
            type:'error',
            message:'请选择课程!'
          });
          return false;
        }
        if(courseListModel.selectTime2==undefined || !courseListModel.selectTime2 || courseListModel.selectTime2.length==0){
          this.$message({
            type:'error',
            message:'请选择学年!'
          });
          return false;
        }
        if(courseListModel.semester==undefined || courseListModel.semester=="" || courseListModel.semester.length==0){
          this.$message({
            type:'error',
            message:'请选择学期!'
          });
          return false;
        }
        if(courseListModel.teacherId==undefined || courseListModel.teacherId==""){
          this.$message({
            type:'error',
            message:'请选择老师!'
          });
          return false;
        }
        return true;
      },
      //保存班级课程
      saveClassCourse:function () {
        var oldCourse=[];
        console.log(this.courseListTable);
        console.log(this.courseListTmp);
        this.courseListTmp.forEach((value1,index1)=>{
          this.courseListTable.forEach((value2,index2)=>{
            if(value1.courseId==value2.courseId && value1.schoolYear==value2.schoolYear
              && value1.semester==value2.semester && value1.teacherId==value2.teacherId){
              oldCourse.push(value1);
              return false;
            }
          })
        });
        console.log(oldCourse);
        if(oldCourse!=null && oldCourse.length>0){
          for(let[idx,item] of oldCourse.entries()){
            for(let[idx1,item1] of this.courseListTmp.entries()){
              if(item.courseId==item1.courseId){
                this.courseListTmp.splice(idx1,1);
              }
            }
            for(let[idx2,item2] of this.courseListTable.entries()){
              if(item.courseId==item2.courseId){
                this.courseListTable.splice(idx2,1);
              }
            }
          }
        }
        //需要删除的课程
        console.log("删除!");
        console.log(this.courseListTmp.length);
        if(this.courseListTmp.length>0){
          for(let[idx1,item1] of this.courseListTmp.entries()){
            this.courseListTmp[idx1].classId=this.thisClassId;
          }
          deleteClassCourseList(this.courseListTmp).then(res=>{
            console.log("aaaa");
            if(res.data.errorCode!=0){
              this.$message({
                type: 'error',
                message: res.data.message
              });
            }
          })
        }

        console.log(this.courseListTable.length);
        //需要添加的课程
        if(this.courseListTable.length>0){
            addClassCourseList(this.courseListTable).then(res=>{
              if(res.data.errorCode!=0){
                this.$message({
                  type: 'error',
                  message: res.data.message
                });
              }
          })
        }

        this.$message({
          type:'success',
          message:'保存成功!'
        });
        this.courseDialog=false;
        this.courseListTmp=[];
        this.courseListTable=[];
        this._getCustomers();
      },
      // 学生名单全选
      toggleSelection(rows){
        if (rows) {
          rows.forEach(row => {
            console.log("行为:"+row)
            this.$refs.studentMultipleTable.toggleRowSelection(row);
          });
        } else {
          this.$refs.studentMultipleTable.clearSelection();
        }
      },
      changeTime(date){
        // this.form.selectTime1=(date.getFullYear()+1).toString()
        console.log("获取的年份为:")
        console.log(typeof this.form.selectTime1)
        var year= new Date(Date.parse(date))
        var newYear=year.getFullYear() + "-" + (year.getFullYear() +1)
        console.log(newYear)
        this.form.selectTime1= new Object(newYear)
        console.log( this.form.selectTime1)
      },
      changesort(){

      },
      // 学生信息库移入学号
      stuInforMouseenter(index,row){
        // console.log("移入的数据为1:"+index)
        // console.log("移入的数据为2:"+row)
        if(row.length>10){
          this.stuInforDisabled=false
        }else{
          this.stuInforDisabled=true
        }
      },
      classKeyDown(){
        console.log("我按下了")
        var keycode = event.keyCode;
        if(keycode==13) {
          this.addViseble('form')
        }
        else {
        }
      }
    },
    beforeRouteEnter: (to, from, next) => {
      console.log("我是路由，我进入了");
      next(vm => {
        // vm._getExpirementInfo();
        vm.handleSizeChange(5)
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

<style lang="stylus" lang="stylus" rel="stylesheet/stylus" type="text/stylus" >
  .xueqi
    h1
      font-size 22px
      margin 20px 3% 0px
      font-family  "Microsoft YaHei"
    #btn-header
      width 94%
      margin 20px auto
      padding-top 20px
      border-top 1px solid #ebeef5 !important
    #Table
      width 94%
      height auto
      margin 0px auto
    .el-dialog
      width 50%
    #Table
      text-align center
      .el-table--border th .cell
        text-align center
        font-weight: bolder;
        font-size: 1.1em;
    .copyright
      text-align right
      color #b4b4b4
      font-size 14px
      margin-top 20px
      margin-right 3%
    /*@media screen and (max-width: 1920px) and (min-width: 1550px)*/
      /*#studentTable, #classTable*/
        /*height 70%*/
        /*overflow auto*/
      /*#classCourseList*/
        /*max-height 350px*/
        /*overflow auto*/
      /*#studentListDialog .el-dialog*/
        /*max-height 70%*/
        /*overflow auto*/
    /*@media screen and (max-width: 1540px)*/
      /*#studentTable, #classTable*/
        /*height 70%*/
        /*border 1px solid red*/
      /*#classCourseList*/
        /*height 200px*/
      /*#studentListDialog .el-dialog*/
        /*height 80%*/
        /*overflow auto*/
    /*@media screen and (max-width: 340px)*/
      /*#studentTable, #classTable*/
        /*height 70%*/
        /*border 1px solid blue*/
      /*#classCourseList*/
        /*height 200px*/
      /*#studentListDialog .el-dialog*/
        /*height 90%*/
        /*overflow auto*/
</style>
<style type="text/css" >
  @import "../../common/css/banji.css";
  #btn-header .el-form-item:not(:nth-child(1)){
    float: right;
  }
  #classCourseList {
    height: 460px; overflow: auto
  }

  /*#studentListDialog .el-dialog {*/
    /*height: 78%; overflow: auto;*/
  /*}*/
  #classCourseList{height: 200px}
  /*#studentListDialog .el-dialog {max-height: 90%; overflow: auto;}*/

  .el-table--border th .cell,
  .el-table th .cell{
    text-align: center;
    font-weight: bolder;
    font-size: 1.1em;
  }
  .el-select-dropdown__item span{
    white-space:pre-wrap
  }
  .xueqi .el-table .cell{
    white-space: pre;
    overflow: hidden;
    word-break:break-all;
    text-overflow: ellipsis;
    padding-right: 10px;
    text-align: center;
  }
  .el-tooltip{
    white-space: pre !important;
  }
 #classStudentList .el-button--danger, #studentListDialog .el-button--danger{
    padding: 3px 12px;
  }
 /*#studentListDialog .el-dialog{*/
   /*max-height: 80%;*/
   /*!*overflow: auto;*!*/
 /*}*/
  #courseDialog .el-form-item, #studentListDialog .el-form-item{
    margin-bottom: 5px;
  }
  #courseDialog .el-input__inner{
    width: 100% ;
  }
  #studentListDialog  .el-input__inner , #courseDialog .el-input__inner{
    height: 30px;
    line-height: 30px;
  }
  #studentListDialog .el-dialog__body, #courseDialog .el-dialog__body{
    margin-top: -40px;
  }
  #studentListDialog .el-dialog__footer, #courseDialog .el-dialog__footer{
   margin-top: -25px;
  }
  /*.el-form-item{*/
    /*border: 1px solid red;*/
  /*}*/
  #courseDialog .el-form-item__label{
    text-align: left;
  }
  #AddTerm .el-date-editor.el-input, .el-date-editor.el-input__inner{
    width: 180px;
  }
   /*.el-table--group::after, .el-table--border::after, .el-table::before {*/
     /*background-color: transparent;*/
   /*}*/
  /*弹出框输入框宽度*/
  #editDialog .el-form-item{
    width: 80%;
    margin: 0px auto 20px;
  }
  /*弹出框表单名称*/
  #editDialog .el-form-item__label{
    text-align: left;
  }
  /*学生名单表格多余的横线*/
  /*studentListDialog*/
  #studentListDialog .el-table--group::after,
  #studentListDialog .el-table--border::after,
  #studentListDialog .el-table::before,

  #courseDialog .el-table--group::after,
  #courseDialog .el-table--border::after,
  #courseDialog .el-table::before
  {
    background-color: transparent;
  }

  /*@media screen and (max-width: 1920px) and (min-width: 1550px){*/
    /*#studentTable, #classTable{height: 400px}*/
    /*#classCourseList{height: 400px}*/
    /*#studentListDialog .el-dialog {max-height: 70%; overflow: auto;}*/
  /*}*/
  /*@media screen and (max-width: 1540px){*/
    /*#studentTable, #classTable{height: 280px}*/
    /*#classCourseList{height: 200px}*/
    /*#studentListDialog .el-dialog {max-height: 80%; overflow: auto;}*/
  /*}*/
  /*@media screen and (max-width: 340px){*/
    /*#studentTable, #classTable{height: 180px}*/
    /*#classCourseList{height: 200px}*/
    /*#studentListDialog .el-dialog {max-height: 90%; overflow: auto;}*/
  /*}*/
</style>
