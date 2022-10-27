import Vue from 'vue';
import ElementUI from 'element-ui';
import '../theme/index.css'
import App from './App.vue';
import router from './router';
import vueEcharts from 'echarts'
import vueQuillEditor from 'vue-quill-editor' // 引入富文本工具
Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.use(vueEcharts)
Vue.use(vueQuillEditor)
/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    render: h => h(App)
});
