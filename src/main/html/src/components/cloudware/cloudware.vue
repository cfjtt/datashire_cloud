<template>
    <!--实验页面-->
    <div>
    <div style="width:500px; background-color: #0060b1; height:800px; float: left;cursor: ew-resize;" id="resizeDiv">
      <mavon-editor v-model="markvalue" :ishljs = "true" :scrollStyle="true"  style="min-height:800px;" @save="markdownsave"/>
    </div>
    <div style="background-color: #F5f5f5; z-index:9; height:700px; width:66%; float:right; margin-left:auto; margin-right:auto; position: relative " id="design">
      <div style="text-align:center;padding-top:250px">
        <h1 style="font-size:80px" @click="startEx">实验区域</h1>
        <small v-if="isLogin">点击右侧工具栏开始实验</small>
        <small v-if="!isLogin"><i class="fa fa-warning"></i> 登录后可以启动实验</small>
      </div>
      <span us-spinner="{ color: '#0060b1', radius: 10, width: 5, length: 10, zIndex: 10 ,top:'250px'}" spinner-key="ex-spinner" spinner-start-active="false"></span>
      <div id="cloudware" data-cloudware-env="rstudio" style="width:500px;z-index:100"></div>
    </div>
  <div left-Bar ></div>

  <span minimaliza-Right style="position: fixed;top:100px;right:0;z-index:101"></span>
    </div>
</template>

<script>
    import Vue from 'vue'
    import mavonEditor from 'mavon-editor'
    import 'mavon-editor/dist/css/index.css'
    import {getExpirementInfo,deleteExCloudware,getLastExperiment,getWebSocketUrl} from '@/common/js/request'
    import axios from 'axios'
    Vue.use(mavonEditor)
    export default {
        data(){
          return {
            markvalue:'## Vue markdown',
            isLogin:true,
            type:1,
            cloudwareInfo:{},
            $scope:{
              cloudwareInfo:{}
            }
          }
        },
        methods:{
          markdownsave:function(value,render){
            console.log(value);
          },
          getCloudwareInfo:function(){
            //获取实验容器信息
            switch (this.type) {
              case 0:
                //获取学生的作业信息(作业暂时不做)
                console.log("获取学生的作业信息");
                break;
              case 1:
                //获取学生的实验信息
                console.log("开始获取实现信息");
                getExpirementInfo(34,12).then(response=>{
                  console.log(response);
                  if(response.status==200){
                    if (response.data.errorCode == 0) {
                      console.log("已启动过...");
                      this.cloudwareInfo = response.data.data;
                    } else if (response.data.errorCode == 35 || response.data.errorCode == 47) {
                      console.log("未启动过...");
                    } else {
                      alert("获取实验云件信息失败，请重试")
                    }
                  }
                });
                break;
              default:
                break;
            }
          },
          deleteEx:function(){
            //删除实验
            deleteExCloudware(12,34).then(response=>{
              if (response.errorCode == 0) {
                alert("删除成功")
                // $scope.hideExText = false;
              } else {
                alert(response.message)
              }
            }).catch(()=>{
              alert("删除失败");
            });
          },
          startEx:function(){
            /*if (this.$scope.notFirst) {
              console.log("start...")
              console.log(this.$scope.cloudwareInfo);
              this.start(this.$scope.cloudwareInfo.webSocket, this.$refs.cloudware)
            } else {*/
              //this.startService(12, 7, this.type);
            //}
            //this.$scope.hideExText = true;
            //this.$scope.hasStarted = true;
            //this.start("wss://192.168.137.172:8888/Sy2QQEU9M",this.$refs.cloudware,3);
            this.startService(8, 'rstudio', this.type);
          },
          start:function(wsaddr,el,retryTime){
            console.log("socket地址:"+wsaddr);
            var retryTime = retryTime || 0;
            var ws = new WebSocket(wsaddr);
            console.log(ws);
            var instance = {
              canvas: null,
              isFullscreen: false,
              setFullscreen: function() {
                var docElm = document.documentElement;
                if (docElm.requestFullscreen) {
                  canvas.requestFullscreen();
                } else if (docElm.webkitRequestFullScreen) {
                  canvas.webkitRequestFullScreen();
                }
                this.isFullscreen = true;
              },
              ws: ws,
              fsapi: ''
            };
            ws.onerror = function(error) {
              console.log("error occurred")
            }
            ws.onclose = function () {
              console.log("close and restart")
              if(!this.$scope.leavePage) {
                setTimeout(function() {
                  if (retryTime > 10) {
                    alert("启动云件失败，请重试")
                    this.$scope.hasStarted = false
                    if (this.type == 1) { //如果是实验，删除改实验云件
                      this.$scope.notFirst = false
                      this.deleteEx(12, 34)
                    }
                  } else {
                    retryTime++
                    this.start(wsaddr, el, retryTime)
                  }
                }, 1000)
              }
            }
            ws.onopen = function() {
              var canvas = document.getElementById("theCanvas")
              if(!canvas) {
                canvas = document.createElement('canvas')
                el.appendChild(canvas);
                canvas.width = 1440;
                canvas.height = 900;
                canvas.style.width = '100%';
                canvas.style.height = '100%';
                canvas.id = "theCanvas"
                canvas.tabIndex = 0
              }

              var canvasOnFocus = true
              canvas.oncontextmenu = function(e) {
                return false;
              }
              canvas.focus()
              instance.canvas = canvas

              canvas.onmousemove = function(e) {
                var dom_left = canvas.offsetLeft + canvas.offsetParent.offsetLeft;
                var dom_top = canvas.offsetTop + canvas.offsetParent.offsetTop;
                var scroll_top = el.scrollTop;
                if (instance.isFullscreen) {
                  scroll_top = 0;
                }
                var xbei = canvas.offsetWidth / 1440;
                var ybei = canvas.offsetHeight / 900;
                var x = Math.floor((e.pageX - dom_left) / xbei);
                var y = Math.floor((e.pageY - dom_top + scroll_top) / ybei);
                var buf = new ArrayBuffer(5);
                var dv = new DataView(buf);
                dv.setUint8(0, 0);
                dv.setUint16(1, x, true);
                dv.setUint16(3, y, true);
                if (ws.readyState == 1 && canvasOnFocus)
                  ws.send(buf);
              };
              canvas.onblur = function (e) {
                canvasOnFocus = false;
              }
              canvas.onfocus = function (e) {
                canvasOnFocus = true;
              }
              canvas.onmousedown = function(e) {
                var buf = new ArrayBuffer(5);
                var dv = new DataView(buf);
                dv.setUint8(0, 1);
                dv.setUint32(1, e.which, true);
                if (ws.readyState == 1 && canvasOnFocus)
                  ws.send(buf);
              };
              canvas.onmouseup = function(e) {
                var buf = new ArrayBuffer(5);
                var dv = new DataView(buf);
                dv.setUint8(0, 2);
                dv.setUint32(1, e.which, true);
                if (ws.readyState == 1 && canvasOnFocus)
                  ws.send(buf);
              };
              document.onkeydown = function(e) {
                if (e.keyCode == 9 || e.keyCode == 32) { //tab pressed
                  e.preventDefault(); // stops its action
                }
                var buf = new ArrayBuffer(5);
                var dv = new DataView(buf);
                dv.setUint8(0, 3);
                dv.setUint32(1, this.mapKey(e.which), true);
                if (ws.readyState == 1 && canvasOnFocus)
                  ws.send(buf);
              };
              document.onkeyup = function(e) {
                if (e.keyCode == 9 || e.keyCode == 32) { //tab pressed
                  e.preventDefault(); // stops its action
                }
                var buf = new ArrayBuffer(5);
                var dv = new DataView(buf);
                dv.setUint8(0, 4);
                dv.setUint32(1, mapKey(e.which), true);
                if (ws.readyState == 1 && canvasOnFocus)
                  ws.send(buf);
              };
            };
            ws.binaryType = "arraybuffer";
            ws.onmessage = function(msg) {
              var data = msg.data;
              if (!(data instanceof ArrayBuffer)) {
                return
              }
              var dv = new DataView(data);

              var x = dv.getInt32(0, true);
              var y = dv.getInt32(4, true);
              var d = data.slice(8);
              var blob = new Blob([d], { type: 'image/webp' });
              var url = URL.createObjectURL(blob);
              var img = new Image;
              img.onload = function() {
                var ctx = instance.canvas.getContext('2d');
                ctx.drawImage(img, x, y);
                URL.revokeObjectURL(url);
              };
              img.src = url;
            };
          },
          onExit:function(){

          },
          startService:function(studentId,cloudware_type,type){
            if(type == 1) {
              getLastExperiment(studentId).then(response=>{
                console.log("开始创建容器");
                this.createCloudware(studentId, cloudware_type, type)
              });
            } else {
              this.createCloudware(studentId, cloudware_type, type)
            }
          },
          _getWebSocketUrl:function(secret,cloudware_type,user_id){
            var params = {
              secret:secret,
              cloudware_type:cloudware_type,
              user_id:user_id
            };
            axios.defaults.headers = {
              withCredentials: true
            };
            return axios.post("http://api.cloudwarehub.com/services",params);
          },
          createCloudware:function(studentId, cloudware_type, type){
           this._getWebSocketUrl('eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmb28iOiJiYXIiLCJpYXQiOjE1MDU4MTM0NTd9.Ftw1yHeUrqdNvymFZcIpuEoS0RHBFZqu4MfUZON9Zm0',cloudware_type,studentId).then(resp=>{
                console.log("获取到地址");
                console.log(resp);
                if (resp.data.errorCode == 0) {
                  this.start(resp.data.ws, document.getElementById("cloudware"));
                  switch (type) {
                    case 0:
                      break;
                    case 1:
                      //保存到数据库
                      break;
                    default:
                      break;
                  }
                }
              });
              return;
          },
          mapKey:function(keyCode){
            var xkm = [
              [65406, 0, 65406, 0, 0, 0, 0],
              [65307, 0, 65307, 0, 0, 0, 0],
              [49, 33, 49, 33, 0, 0, 0],
              [50, 64, 50, 64, 0, 0, 0],
              [51, 35, 51, 35, 0, 0, 0],
              [52, 36, 52, 36, 0, 0, 0],
              [53, 37, 53, 37, 0, 0, 0],
              [54, 94, 54, 94, 0, 0, 0],
              [55, 38, 55, 38, 0, 0, 0],
              [56, 42, 56, 42, 0, 0, 0],
              [57, 40, 57, 40, 0, 0, 0],
              [48, 41, 48, 41, 0, 0, 0],
              [45, 95, 45, 95, 0, 0, 0],
              [61, 43, 61, 43, 0, 0, 0],
              [65288, 65288, 65288, 65288, 0, 0, 0],
              [65289, 65056, 65289, 65056, 0, 0, 0],
              [113, 81, 113, 81, 0, 0, 0],
              [119, 87, 119, 87, 0, 0, 0],
              [101, 69, 101, 69, 0, 0, 0],
              [114, 82, 114, 82, 0, 0, 0],
              [116, 84, 116, 84, 0, 0, 0],
              [121, 89, 121, 89, 0, 0, 0],
              [117, 85, 117, 85, 0, 0, 0],
              [105, 73, 105, 73, 0, 0, 0],
              [111, 79, 111, 79, 0, 0, 0],
              [112, 80, 112, 80, 0, 0, 0],
              [91, 123, 91, 123, 0, 0, 0],
              [93, 125, 93, 125, 0, 0, 0],
              [65293, 0, 65293, 0, 0, 0, 0],
              [65507, 0, 65507, 0, 0, 0, 0],
              [97, 65, 97, 65, 0, 0, 0],
              [115, 83, 115, 83, 0, 0, 0],
              [100, 68, 100, 68, 0, 0, 0],
              [102, 70, 102, 70, 0, 0, 0],
              [103, 71, 103, 71, 0, 0, 0],
              [104, 72, 104, 72, 0, 0, 0],
              [106, 74, 106, 74, 0, 0, 0],
              [107, 75, 107, 75, 0, 0, 0],
              [108, 76, 108, 76, 0, 0, 0],
              [59, 58, 59, 58, 0, 0, 0],
              [39, 34, 39, 34, 0, 0, 0],
              [96, 126, 96, 126, 0, 0, 0],
              [65505, 0, 65505, 0, 0, 0, 0],
              [92, 124, 92, 124, 0, 0, 0],
              [122, 90, 122, 90, 0, 0, 0],
              [120, 88, 120, 88, 0, 0, 0],
              [99, 67, 99, 67, 0, 0, 0],
              [118, 86, 118, 86, 0, 0, 0],
              [98, 66, 98, 66, 0, 0, 0],
              [110, 78, 110, 78, 0, 0, 0],
              [109, 77, 109, 77, 0, 0, 0],
              [44, 60, 44, 60, 0, 0, 0],
              [46, 62, 46, 62, 0, 0, 0],
              [47, 63, 47, 63, 0, 0, 0],
              [65506, 0, 65506, 0, 0, 0, 0],
              [65450, 65450, 65450, 65450, 65450, 65450, 269024801],
              [65513, 65511, 65513, 65511, 0, 0, 0],
              [32, 0, 32, 0, 0, 0, 0],
              [65509, 0, 65509, 0, 0, 0, 0],
              [65470, 65470, 65470, 65470, 65470, 65470, 269024769],
              [65471, 65471, 65471, 65471, 65471, 65471, 269024770],
              [65472, 65472, 65472, 65472, 65472, 65472, 269024771],
              [65473, 65473, 65473, 65473, 65473, 65473, 269024772],
              [65474, 65474, 65474, 65474, 65474, 65474, 269024773],
              [65475, 65475, 65475, 65475, 65475, 65475, 269024774],
              [65476, 65476, 65476, 65476, 65476, 65476, 269024775],
              [65477, 65477, 65477, 65477, 65477, 65477, 269024776],
              [65478, 65478, 65478, 65478, 65478, 65478, 269024777],
              [65479, 65479, 65479, 65479, 65479, 65479, 269024778],
              [65407, 0, 65407, 0, 0, 0, 0],
              [65300, 0, 65300, 0, 0, 0, 0],
              [65429, 65463, 65429, 65463, 0, 0, 0],
              [65431, 65464, 65431, 65464, 0, 0, 0],
              [65434, 65465, 65434, 65465, 0, 0, 0],
              [65453, 65453, 65453, 65453, 65453, 65453, 269024803],
              [65430, 65460, 65430, 65460, 0, 0, 0],
              [65437, 65461, 65437, 65461, 0, 0, 0],
              [65432, 65462, 65432, 65462, 0, 0, 0],
              [65451, 65451, 65451, 65451, 65451, 65451, 269024802],
              [65436, 65457, 65436, 65457, 0, 0, 0],
              [65433, 65458, 65433, 65458, 0, 0, 0],
              [65435, 65459, 65435, 65459, 0, 0, 0],
              [65438, 65456, 65438, 65456, 0, 0, 0],
              [65439, 65454, 65439, 65454, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [60, 62, 60, 62, 124, 166, 124],
              [65480, 65480, 65480, 65480, 65480, 65480, 269024779],
              [65481, 65481, 65481, 65481, 65481, 65481, 269024780],
              [65360, 0, 65360, 0, 0, 0, 0],
              [65362, 0, 65362, 0, 0, 0, 0],
              [65365, 0, 65365, 0, 0, 0, 0],
              [65361, 0, 65361, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [65363, 0, 65363, 0, 0, 0, 0],
              [65367, 0, 65367, 0, 0, 0, 0],
              [65364, 0, 65364, 0, 0, 0, 0],
              [65366, 0, 65366, 0, 0, 0, 0],
              [65379, 0, 65379, 0, 0, 0, 0],
              [65535, 0, 65535, 0, 0, 0, 0],
              [65421, 0, 65421, 0, 0, 0, 0],
              [65508, 0, 65508, 0, 0, 0, 0],
              [65299, 65387, 65299, 65387, 0, 0, 0],
              [65377, 65301, 65377, 65301, 0, 0, 0],
              [65455, 65455, 65455, 65455, 65455, 65455, 269024800],
              [65514, 65512, 65514, 65512, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [65515, 0, 65515, 0, 0, 0, 0],
              [65516, 0, 65516, 0, 0, 0, 0],
              [65383, 0, 65383, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [65027, 0, 65027, 0, 0, 0, 0],
              [0, 65513, 0, 65513, 0, 0, 0],
              [65469, 0, 65469, 0, 0, 0, 0],
              [0, 65515, 0, 65515, 0, 0, 0],
              [0, 65517, 0, 65517, 0, 0, 0],
              [269025074, 0, 269025074, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [65454, 65454, 65454, 65454, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025046, 0, 269025046, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025071, 0, 269025071, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025047, 0, 269025047, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 65511, 0, 65511, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025042, 0, 269025042, 0, 0, 0, 0],
              [269025053, 0, 269025053, 0, 0, 0, 0],
              [269025044, 269025073, 269025044, 269025073, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025045, 269025068, 269025045, 269025068, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025068, 0, 269025068, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025041, 0, 269025041, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025043, 0, 269025043, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025070, 0, 269025070, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025068, 0, 269025068, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025113, 0, 269025113, 0, 0, 0, 0],
              [269025028, 0, 269025028, 0, 0, 0, 0],
              [269025030, 0, 269025030, 0, 0, 0, 0],
              [269025029, 0, 269025029, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025066, 0, 269025066, 0, 0, 0, 0],
              [269025040, 0, 269025040, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025067, 0, 269025067, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025051, 0, 269025051, 0, 0, 0, 0],
              [269025072, 0, 269025072, 0, 0, 0, 0],
              [269025139, 0, 269025139, 0, 0, 0, 0],
              [269025064, 0, 269025064, 0, 0, 0, 0],
              [269025063, 0, 269025063, 0, 0, 0, 0],
              [269025062, 0, 269025062, 0, 0, 0, 0],
              [269025075, 0, 269025075, 0, 0, 0, 0],
              [269025049, 0, 269025049, 0, 0, 0, 0],
              [269025074, 0, 269025074, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025171, 0, 269025171, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [269025173, 0, 269025173, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0]
            ];
            var exceptionKeys = {
              '190': '46', // PERIOD CHROME
              '16': '65506', // SHIFT CHROME
              '17': '65507', // CTRL CHROME
              '18': '65513', // ALT CHROME
              '34': '65307', // ESC CHROME
              '191': '47', // BACKSLASH CHROME
              '20': '65509', // CAPS CHROME
              '9': '65289', // TAB CHROME
              '189': '45', // MINUS CHROME
              '187': '61', // EQUALS CHROME
              '8': '65288', // BACKSPACE CHROME
              '220': '92', // FOWARD SLASH CHROME
              '13': '65293', // ENTER CHROME
              '192': '96', // TILDE CHROME
              '186': '59', // SEMICOLON CHROME
              '222': '34', // QUOTES CHROME
              '188': '44', // COMMA CHROME
              '27': '65307', // ESC CHROME
              '39': '65363', // RIGHT ARROW CHROME
              '37': '65361', // LEFT ARROW CHROME
              '38': '65362', // UP ARROW CHROME
              '40': '65364', // DOWN ARROW CHROME
              '91': '65506', // set osx cmd to shift
            };

            function buildASCIIToXKeyMap(XKeysMap, min) {
              var asciiToX = {};
              for (var i = 0; i < XKeysMap.length; i++) {
                for (var j = 0; j < XKeysMap[i].length; j++) {
                  var key = XKeysMap[i][j];
                  var value = i + min;
                  if (key !== 0)
                    asciiToX[key] = value;
                }
              }
              return asciiToX;
            }

            var keyMap = buildASCIIToXKeyMap(xkm, 8);
            if (exceptionKeys[keyCode])
              keyCode = exceptionKeys[keyCode];
            var key = keyMap[keyCode];
            if (key === undefined || key === null)
              key = 62; // shift
            if (keyCode == 219) {
              key = 34;
            } else if (keyCode === 221) {
              key = 35
            }
            return key;
          }
        },
        created:function(){
          this.getCloudwareInfo();
        }
    }
</script>

<style scoped>

</style>
