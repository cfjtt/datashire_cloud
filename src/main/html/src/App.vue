<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',

  created:function(){
    console.log('app created');
    localStorage.removeItem('isSend');
  }
}
</script>

<style lang="stylus">
  @import './common/css/index.styl'
#app {

}
</style>
