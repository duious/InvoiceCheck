<template>
  <div class="main-div">
    <video id="video" :width="width" :height="height" autoplay></video>
  </div>
</template>
<script>
  import QRCode from 'qrcode';
  import QRCodeDecoder from 'qrcode-decoder';

  export default {
    name: "QRCodeScan",
    data() {
      return {
        width: 320,
        height: 320,
        res: "",
        myInterval: "",
        qr: "",
        check: this.$route.params.check,
        // 媒体流
        mediaStreamTrack: "",
      }
    },
    components: {
      QRCode: QRCode
    },
    mounted() {
      this.width = document.getElementsByClassName("main-div")[0].offsetWidth;
      this.height = document.getElementsByClassName("main-div")[0].offsetHeight;
      this.scanQRCode();
    },
    created() {
    },
    destroyed() {
      this.mediaStreamTrack.stop();
    },
    methods: {
      scanQRCode() {
        var _this = this;
        var video = document.getElementById("video"),
          MediaErr = function (error) {
            if (error.PERMISSION_DENIED) {
              this.$toast('用户拒绝了浏览器请求媒体的权限');
            } else if (error.NOT_SUPPORTED_ERROR) {
              this.$toast('对不起，您的浏览器不支持拍照功能，请使用其他浏览器');
            } else if (error.MANDATORY_UNSATISFIED_ERROR) {
              this.$toast('指定的媒体类型未接收到媒体流');
            } else {
              this.$toast('系统未能获取到摄像头，请确保摄像头已正确安装。或尝试刷新页面，重试');
            }
          },
          mediaConfig = {audio: false, video: true};
        // 老的浏览器可能根本没有实现 mediaDevices，所以我们可以先设置一个空的对象
        if (navigator.mediaDevices === undefined) {
          navigator.mediaDevices = {};
        }
        try {
          // 一些浏览器部分支持 mediaDevices。我们不能直接给对象设置 getUserMedia
          // 因为这样可能会覆盖已有的属性。这里我们只会在没有getUserMedia属性的时候添加它。
          if (navigator.mediaDevices.getUserMedia === undefined) {
            navigator.mediaDevices.getUserMedia = function (constraints) {
              // 首先，如果有getUserMedia的话，就获得它
              var getUserMedia = navigator.webkitGetUserMedia || navigator.mozGetUserMedia ||
                navigator.mozGetUserMedia();
              // 一些浏览器根本没实现它 - 那么就返回一个error到promise的reject来保持一个统一的接口
              if (!getUserMedia) {
                this.$toast("浏览器不支持HTML5 CANVAS");
                return Promise.reject(new Error('getUserMedia is not implemented in this browser'));
              }
              // 否则，为老的navigator.getUserMedia方法包裹一个Promise
              return new Promise(function (resolve, reject) {
                getUserMedia.call(navigator, constraints, resolve, reject);
              });
            }
          }
          navigator.mediaDevices.enumerateDevices().then(function (devices) {
            var len = [];
            devices.forEach(function (dv) {
              var kind = dv.kind;
              if (kind.match(/^video.*/)) {
                len.push(dv.deviceId);
              }
            });
            // 默认使用后置摄像头。根据测试，在手机上后置摄像头的id会在之后获取
            mediaConfig.video = {
              deviceId: len[len.length - 1],
              facingMode: "environment",
              width: _this.width,
              height: _this.height,
            };
          });
          navigator.mediaDevices.getUserMedia(mediaConfig)
            .then(function (stream) {
              _this.mediaStreamTrack = stream.getTracks()[0];
              // 旧的浏览器可能没有srcObject
              if ("srcObject" in video) {
                video.srcObject = stream;
              } else {
                // 防止在新的浏览器里使用它，应为它已经不再支持了
                video.src = window.URL.createObjectURL(stream);
              }
              video.onloadedmetadata = function (e) {
                if (_this.myInterval != "finish") {
                  _this.myInterval = setInterval(function () {
                    if (_this.qr == "") {
                      _this.qr = new QRCodeDecoder()
                    }
                    _this.qr.decodeFromVideo(video).then((res) => {
                      if (res.data != "") {
                        _this.mediaStreamTrack.stop();
                        _this.res = res.data;
                        window.clearInterval(_this.myInterval);
                        _this.myInterval == "finish";
                        video.pause();
                        if (_this.check == "false") {
                          _this.$router.push({name: 'manualInput', params: {res: _this.res}});
                        } else {
                          _this.$axios.post('/check.do', _this.$formatPostData({no: _this.res.split(",")[3]})).then((response) => {
                            _this.$toast(response.data.msg);
                            if (response.data.code == _this.$result.code.CODE_SUCCESS) {
                              _this.$router.push({name: 'manualInput', params: {res: _this.res,}});
                            } else {
                              _this.$router.push({name: 'home',});
                            }
                          }).catch((response) => {
                          })
                          // _this.$toast('已报销！！！');
                          // _this.$router.push({name: 'home',});
                        }
                      }
                    });
                  }, 500);
                }
                video.play();
              };
            }).catch(MediaErr);
        } catch (e) {
          _this.$toast("浏览器不支持HTML5 CANVAS");
        }
      },
    }
  }
</script>

<style scoped>
  .main-div {
    height: 85vh;
    width: 100vw;
    padding-bottom: 55px;
  }
</style>
