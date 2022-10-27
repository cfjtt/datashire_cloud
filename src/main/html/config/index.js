'use strict'
// Template version: 1.3.1
// see http://vuejs-templates.github.io/webpack for documentation.

const path = require('path')

module.exports = {
  dev: {

    // Paths
    assetsSubDirectory: 'static',
    assetsPublicPath: '/api',
    proxyTable: {
      '/auth':{
        // target: 'http://17tao.iok.la',//需要代理的地址
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
        // changeOrigin: false,//是否跨域
      },
      '/cloudware':{
        // target: 'http://17tao.iok.la',//需要代理的地址
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
        // changeOrigin: false,//是否跨域

      },
      '/common':{
        // target: 'http://17tao.iok.la',//需要代理的地址
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
        // changeOrigin: false,//是否跨域
      },
      '/admin':{
        // target: 'http://17tao.iok.la',//需要代理的地址
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
        // changeOrigin: false,//是否跨域
      },
      '/image':{
        // target: 'http://17tao.iok.la',//需要代理的地址
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
        // changeOrigin: false,//是否跨域
      },
      '/markdown':{
        // target: 'http://17tao.iok.la',//需要代理的地址
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
        // changeOrigin: false,//是否跨域
      },
      '/report':{
        // target: 'http://17tao.iok.la',//需要代理的地址
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
        // changeOrigin: false,//是否跨域
      },
      '/student':{
        // target: 'http://17tao.iok.la',//需要代理的地址
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
        // changeOrigin: false,//是否跨域
      },
      '/teacher':{
        // target: 'http://17tao.iok.la',//需要代理的地址
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
        // changeOrigin: false,//是否跨域
      },
      '/share':{
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
      },
      '/record':{
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
      },
      '/port':{
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
      },
      '/services':{
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
      },
      '/dmdata':{
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
      },
      '/folder':{
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
      },
      '/doc':{
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
      },
      '/project':{
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
      },
      '/webSocket':{
        target: 'http://192.168.137.56:8080',//需要代理的地址
        changeOrigin: true//是否跨域
      }
    },

    // Various Dev Server settings
    host: '0.0.0.0', // can be overwritten by process.env.HOST
    port: 8081, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
    autoOpenBrowser: false,
    errorOverlay: true,
    notifyOnErrors: true,
    poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-

    // Use Eslint Loader?
    // If true, your code will be linted during bundling and
    // linting errors and warnings will be shown in the console.
    useEslint: false,
    // If true, eslint errors and warnings will also be shown in the error overlay
    // in the browser.
    showEslintErrorsInOverlay: false,

    /**
     * Source Maps
     */

    // https://webpack.js.org/configuration/devtool/#development
    devtool: 'cheap-module-eval-source-map',

    // If you have problems debugging vue-files in devtools,
    // set this to false - it *may* help
    // https://vue-loader.vuejs.org/en/options.html#cachebusting
    cacheBusting: true,

    cssSourceMap: true
  },

  build: {
    // Template for index.html
    index: path.resolve(__dirname, '../../resources/static/index.html'),

    // Paths
    assetsRoot: path.resolve(__dirname, '../../resources/static'),
    assetsSubDirectory: 'rs',
    assetsPublicPath: '/',

    /**
     * Source Maps
     */

    productionSourceMap: true,
    // https://webpack.js.org/configuration/devtool/#production
    devtool: '#source-map',

    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],

    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  }
}
