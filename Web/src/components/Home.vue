<template>
  <div class="main-div">
    <van-row type="flex" justify="center" gutter="10">
      <van-col span="12">
        <div class="scan-div" @click="scanQRCode('false')">
          <van-icon name="scan"/>
          <span>登记</span>
        </div>
      </van-col>
      <van-col span="12">
        <div class="scan-div" @click="scanQRCode('true')">
          <van-icon name="scan" color="#f00"/>
          <span>查验</span>
        </div>
      </van-col>
    </van-row>
    <van-row type="flex" justify="center" gutter="10">
      <van-col span="12">
        <div class="scan-div" @click="scanText('false')">
          <van-icon name="scan"/>
          <span>文字识别</span>
          <input id="imageRecognition" class="imageRecognition" type="file" accept="image/*">
        </div>
      </van-col>
      <!--      <van-col span="12">-->
      <!--        <div class="scan-div" @click="scanQRCode('true')">-->
      <!--          <van-icon name="scan" color="#f00"/>-->
      <!--          <span>查验</span>-->
      <!--        </div>-->
      <!--      </van-col>-->
    </van-row>
    <br>
    <van-cell-group>
      <van-cell icon="edit" title="手工录入" size="large"
                is-link to="/ManualInput"></van-cell>
    </van-cell-group>
    <van-cell-group>
      <van-cell icon="orders-o" title="录入总计" size="large" v-model="total"></van-cell>
    </van-cell-group>
  </div>
</template>
<script>
  export default {
    name: 'Home',
    data() {
      return {
        total: 0,
        src: ""
      }
    },
    mounted: function () {
      // 将subscanQRCallBack方法绑定到window下面，提供给外部调用
      window['scanQRCallBack'] = (result) => {
        this.scanQRCallBack(result)
      };
      window['recognitionCallBack'] = (result) => {
        this.recognitionCallBack(result)
      };
      this.$axios.post('/index.do').then((response) => {
        this.total = response.data.data;
      }).catch((response) => {
      })
    },
    methods: {
      scanQRCode(check) {
        try {
          window.UtilForJS.scanQRCode();
        } catch (e) {
          this.$router.push({name: 'qRCodeScan', params: {check: check,}});
        }
      },
      scanQRCallBack(res) {
        this.$router.push({name: 'manualInput', params: {res: res,}});
      },
      recognitionCallBack(res) {
        this.$router.push({name: 'imageRecognition', params: {base64: res,}});
      },
      scanText(check) {
        try {
          window.UtilForJS.putValue("1","1");
          document.getElementById("imageRecognition").click();
        } catch (e) {
          this.$router.push({name: 'imageRecognition', params: {check: check,}});
        }
      },
    }
  }
</script>
<style scoped>
  .main-div {
    padding: 10px;
    padding-bottom: 55px;
  }

  .van-row {
    box-sizing: border-box !important;
    border: 1px solid #ff8400 !important;
    border-radius: 8px !important;
    padding: 8px 15px !important;
    margin: 5px 10px !important;
  }


  .scan-div {
    display: -webkit-flex;
    display: flex;
  }

  .scan-div .van-icon {
    color: #ff6100 !important;
    font-size: 50px !important;
    font-weight: bold !important;
  }

  .scan-div span {
    line-height: 48px !important;
    margin: 0 0 0 10px !important;
    font-size: 20px !important;
  }

  .van-cell {
    height: 60px !important;
    line-height: 36px !important;
  }

  .van-cell .van-icon, .van-cell .van-cell__title {
    font-size: 22px !important;
  }

  .van-cell .van-icon:first-child {
    color: #ff6100 !important;
    font-weight: bold !important;
  }

  .van-cell .van-icon {
    font-size: 30px !important;
    line-height: 36px !important;
  }

  .imageRecognition {
    display: none;
  }
</style>
