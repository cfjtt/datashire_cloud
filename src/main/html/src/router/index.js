import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/components/main/Main'
import learning from '@/components/learning/learning'
import all from '@/components/all-course/all-course'
import data from '@/components/data/data'
import classManage from '@/components/classManage/classManage' // 班级管理
import classManageDetail from '@/components/classManage-detail/classManage-detail' // 班级管理详情
import leardetail from '@/components/learning-detail/learning-detail'
import teacher from '@/components/guanli-teacher/guanli-teacher'
import xueqi from '@/components/guanli-xueqi/guanli-xueqi'
import kecheng from '@/components/guanli-kecheng/guanli-kecheng'
import banji from '@/components/guanli-banji/guanli-banji' // 后台管理-班级管理
import htSj from '@/components/htshuju/htshuju'
import exTest from '@/components/experiment-test/experiment-test' // 管理员编辑答案
import exSubmit from '@/components/experiment-submit/experiment-submit' // 学生提交答案
import student from '@/components/guanli-student/guanli-student'
import license from '@/components/guanli-license/guanli-license'
import ClassDetail from '@/components/banji-detail/banji-detail'
import login from '@/components/login/login'
import header from '@/components/header/header'
import cloudware from '@/components/cloudware/cloudware'
import experiment from '@/components/experiment/experiment'
import Addexperiment from '@/components/Addexperiment/Addexperiment'
import shuju from '@/components/shuju/shuju'
import share from '@/components/shuju-Share/shuju-Share'
import hunting from '@/components/shuju-hunting/shuju-hunting'
import file from '@/components/hunting-file/hunting-file'
import Hdata from '@/components/hunting-data/hunting-data'
import watchExperiment from '@/components/watchExperiment/watchExperiment'
import shujuGuanli from '@/components/guanli-shuju/guanli-shuju'

import deepIndex from '@/components/deep-index/deep-index' // deep市场首页
import deepModel from '@/components/deep-model/deep-model' // 模型交易
import deepProject from '@/components/deep-project/deep-project' // 项目交易
import deepCourse from '@/components/deep-course/deep-course' // 课程交易
import deepPersonnel from '@/components/deep-personnel/deep-personnel' // 课程交易
import jiashi from '@/components/jiashi/jiashi' // 驾驶舱
import jiashiPingTai from '@/components/jiashi-pingtai/jiashi-pingtai' // 驾驶舱-平台
import jiashiMain from '@/components/jiashi-main/jiashi-main' // 驾驶舱-标题
import jiashiSpark from '@/components/jiashi-spark/jiashi-spark' // 驾驶舱-Spark作业统计
import jiashiDeep from '@/components/jiashi-deep/jiashi-deep' // 驾驶舱-deep指数/科研指数
import jiashiShiShi from '@/components/jiashi-shishi/jiashi-shishi' // 驾驶舱-实时在线人数/累计使用
import jiashiScroll from '@/components/jiashi-scroll/jiashi-scroll' // 驾驶舱-实时滚动窗口
import jiashiExam from '@/components/jiashi-exam/jiashi-exam' // 驾驶舱-考试成绩
import jiashiAverage from '@/components/jiashi-average/jiashi-average' // 驾驶舱-平均时长
import jiashiCourse from '@/components/jiashi-course/jiashi-course' // 驾驶舱-课程内容
import jiashiEnvironment from '@/components/jiashi-environment/jiashi-environment' // 实验环境
import scientificProject from '@/components/scientific-project/scientific-project' // 项目列表-我的项目
import scientificMyProject from '@/components/scientific-myProject/scientific-myProject' // 项目列表-我的项目
import scientificAllProject from '@/components/scientific-allProject/scientific-allProject' // 项目列表-我的项目
import scientificManager from '@/components/scientific-manager/scientific-manager' // 项目列表-管理员
import scientificCenter from '@/components/scientific-center/scientific-center' // 科研中心
import scientificMain from '@/components/scientific-main/scientific-main' // 科研中心-项目主页
import scientificModel from '@/components/scientific-model/scientific-model' // 科研中心-项目模型
import scientificFile from '@/components/scientific-file/scientific-file' // 科研中心-项目模型
import scientificupdateFile from '@/components/scientific-updateFile/scientific-updateFile' // 科研中心-修改项目文档
import scientificData from '@/components/scientific-data/scientific-data' // 科研中心-项目模型
import scientificInfor from '@/components/scientific-information/scientific-information' // 科研中心-项目信息
// scientific-member
import scientificMember from '@/components/scientific-member/scientific-member' // 科研中心-项目成员
// import htBc from '@/components/shuju-Share/shuju-Share'
// import htFile from '@/components/htshuju-file/htshuju-file'
// import htTable from '@/components/htshuju-table/htshuju-table'
// import syFile from '@/components/shiyan-file/shiyan-file'
// import syTable from '@/components/shiyan-table/shiyan-table'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/main',
      name: 'Main',
      component: Main
    },
    {
      path: '/learning',
      name: 'learning',
      component: learning
    },
    {
      path: '/leardetail/:courseId',
      component: leardetail
    }, {
      path: '/all',
      name: 'all',
      component: all
    },
    {
      path: '/shuju',
      component: shuju,
      children: [
        {
          path: '/',
          redirect: '/',
          component: share
        },
        {
          path: '/share',
          name: 'share',
          component: share
        }, {
          path: '/hunting',
          component: hunting,
          children: [
            {
              path: '/',
              redirect: '/file',
              name: '/file'
            },
            {
              path: '/file',
              name: 'file',
              component: file
            }, {
              path: '/Hdata',
              name: 'Hdata',
              component: Hdata
            }
          ]
        }
      ]
    },
    {
      path: '/watchExperiment/:experimentId',
      component: watchExperiment
    },
    {
      path: '/data',
      component: data,
      children: [
        {
          path: '/',
          redirect: '/teacher',
          name: 'default'
        },
        {
          path: '/banji',
          name: 'banji',
          component: banji
        },
        {
          path: '/htSj',
          name: 'htSj',
          component: htSj,
          children: [
            {
              path: '/htSj/share/:courseId/:time',
              name: 'share',
              component: share
            },
            {
              path: '/htSj/file/:isAdmin/:courseName/:time',
              name: 'file',
              component: file
            },
            {
              path: '/htSj/Hdata/:isAdmin/:courseName/:time',
              name: 'Hdata',
              component: Hdata
            }
          ]
        },
        // exTest
        {
          path: '/exTest',
          name: 'exTest',
          component: exTest
        },
        // exSubmit

        {
          path: '/student',
          name: 'student',
          component: student
        },
        // license
        {
          path: '/license',
          name: 'license',
          component: license
        },
        {
          path: '/ClassDetail/:classId/:courseName',
          name: 'ClassDetail',
          component: ClassDetail
        },
        {
          path: '/kecheng',
          name: 'kecheng',
          component: kecheng
        },
        {
          path: '/experiment/:courseId',
          name: 'experiment',
          component: experiment
        },
        {
          path: '/Addexperiment/:courseId/:moduleId',
          name: 'Addexperiment',
          component: Addexperiment
        },
        {
          path: '/teacher',
          name: 'teacher',
          component: teacher
        },
        {
          path: '/xueqi',
          name: 'xueqi',
          component: xueqi
        },
        {
          path: '/shujuGuanli/:courseId',
          name: 'shujuGuanli',
          component: shujuGuanli
        }
      ]
    },
    {
      path: '/exSubmit',
      name: 'exSubmit',
      component: exSubmit
    },
    {
      path: '/scientificProject',
      component: scientificProject,
      children: [
        {
          path: '/',
          redirect: '/scientificMyProject',
          name: 'scientificMyProject'
        },
        {
          path: '/scientificMyProject',
          name: 'scientificMyProject',
          component: scientificMyProject
        },
        {
          path: '/scientificAllProject',
          name: 'scientificAllProject',
          component: scientificAllProject
        }
      ]
    },
    {
      path: '/scientificManager',
      component: scientificManager,
      name: 'scientificManager'
    },
    {
      path: '/scientificCenter',
      // name: 'scientificCenter',
      component: scientificCenter,
      children: [
        {
          path: '/',
          redirect: '/scientificMain',
          name: 'scientificMain'
        },
        {
          path: '/scientificMain',
          name: 'scientificMain',
          component: scientificMain
        }, {
          path: '/scientificModel',
          name: 'scientificModel',
          component: scientificModel
        }, {
          path: '/scientificFile',
          name: 'scientificFile',
          component: scientificFile
        },
        {
          path: '/scientificData/:projectId/:managerNo/:paraSpaceName',
          // name: 'scientificData',
          component: scientificData,
          children: [
            {
              path: '/',
              redirect: '/scientificData/file/:projectId/:managerNo/:paraSpaceName',
              name: 'scientificDataFile'
            },
            {
              path: '/scientificData/file/:projectId/:managerNo/:paraSpaceName',
              name: 'scientificDataFile',
              component: file
            }, {
              path: '/scientificData/Hdata/:projectId/:managerNo/:paraSpaceName',
              name: 'scientificDataHdata',
              component: Hdata
            }
          ]
        }, {
          path: '/scientificInfor',
          name: 'scientificInfor',
          component: scientificInfor
        }, {
          path: '/scientificMember',
          name: 'scientificMember',
          component: scientificMember
        }
      ]
    },
    // scientificupdateFile
    {
      path: '/scientificupdateFile',
      component: scientificupdateFile,
      name: 'scientificupdateFile'
    },
    {
      path: '/deepIndex',
      name: 'deepIndex',
      component: deepIndex
    },
    {
      path: '/deepModel',
      name: 'deepModel',
      component: deepModel
    },
    {
      path: '/deepProject',
      name: 'deepProject',
      component: deepProject
    },
    {
      path: '/deepCourse',
      name: 'deepCourse',
      component: deepCourse
    },
    {
      path: '/deepPersonnel',
      name: 'deepPersonnel',
      component: deepPersonnel
    },
    {
      path: '/classManage',
      component: classManage
    },
    {
      path: '/classManageDetail',
      component: classManageDetail
    },
    {
      path: '/login',
      component: login
    }, {
      path: '/header',
      component: header
    }, {
      path: '/',
      redirect: '/all'
    }, {
      path: '/cloudware',
      component: cloudware
    }, {
      path: '/jiashi',
      component: jiashi,
      children: [
        {
          path: '/jiashiPingTai',
          component: jiashiPingTai
        }, {
          path: '/jiashiMain',
          component: jiashiMain
        },
        {
          path: '/jiashiSpark',
          component: jiashiSpark
        },
        {
          path: '/jiashiDeep',
          component: jiashiDeep
        },
        {
          path: '/jiashiShiShi',
          component: jiashiShiShi
        },
        {
          path: '/jiashiScroll',
          component: jiashiScroll
        },
        {
          path: '/jiashiExam',
          component: jiashiExam
        },
        {
          path: '/jiashiAverage',
          component: jiashiAverage
        },
        {
          path: '/jiashiCourse',
          component: jiashiCourse
        },
        {
          path: '/jiashiEnvironment',
          component: jiashiEnvironment
        }
      ]
    }
  ],
  linkActiveClass: 'active'
})
