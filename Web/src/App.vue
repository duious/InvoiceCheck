<template>
  <div id="app">
    <van-nav-bar :title="title" :left-arrow="!$store.state.mainPage" @click-left="goBack"></van-nav-bar>
    <div class="main">
      <router-view/>
    </div>
    <van-tabbar v-model="menuIndex" active-color="#ff6100">
      <van-tabbar-item icon="home-o" to="/" @click="initMainPage" replace>主页</van-tabbar-item>
      <van-tabbar-item icon="points" to="/InvoiceLibrary" @click="initMainPage" replace>仓库</van-tabbar-item>
      <van-tabbar-item icon="setting-o" replace>配置</van-tabbar-item>
    </van-tabbar>
  </div>
</template>
<script>
  export default {
    name: 'App',
    data() {
      return {
        header: '发票查重工具',
        mainPage: this.$store.state.mainPage,
        title: "发票查重工具",
        menuIndex: this.$store.state.menuIndex,
      }
    },
    created() {
      this.$store.state.mainPage = true;
      this.mainPage = this.$store.state.mainPage;
    },
    activated() {
      this.mainPage = this.$store.state.mainPage;
    },
    methods: {
      initMainPage() {
        this.$store.state.mainPage = true;
      },
      goBack() {
        if (this.$route.matched) {
          if (this.$route.matched.length == 2) {
            this.$router.go(-1);
            this.$route.matched = this.$route.matched[0];
            this.initMainPage();
          } else if (this.$route.matched.length > 2) {
            this.$router.go(-1);
            var i = 0;
            do {
              this.$route.matched[i] = this.$route.matched[i];
            } while (i < this.$route.matched.length);
          } else if (this.$route.matched.length == 1) {
            this.$router.go(-1);
          } else {
            this.$router.go(-1);
          }
          this.initMainPage();
        }
      }
    },
  }
</script>
<style>
  #app {
    margin: 0 auto;
    padding: 0 0 0 0;
    height: 100vh;
    width: 100%;
  }

  .van-nav-bar {
    height: 60px !important;
    line-height: 60px !important;
    background-color: #ff6100 !important;
  }

  .van-nav-bar__title, .van-nav-bar .van-icon {
    font-size: 22px !important;
    color: #fff !important;
  }

  .van-tabbar-item {
    font-size: 20px !important;
    font-weight: bold !important;
  }

  .van-tabbar-item__icon {
    font-size: 24px !important;
    margin-bottom: 0 !important;
    font-weight: bold !important;
  }
</style>
