<template>
  <div id="app">
    <el-container>
      <el-header><Header></Header></el-header>
      <el-main><router-view></router-view></el-main>
    </el-container>
  </div>
</template>


<script>
    import Header from './components/Header.vue'
    export default {
        name: 'app',
        components: {
            Header
        }
    }
</script>
<style>
#app {
  font-family: '新宋体','Avenir', Helvetica, Arial, sans-serif;

}
</style>
