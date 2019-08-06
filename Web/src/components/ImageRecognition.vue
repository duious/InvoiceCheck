<template>
  <div class="main-div">
    <video id="video" :width="width" :height="height" autoplay></video>
    <canvas id="canvas" :width="videoWidth" :height="videoHeight"></canvas>
    <van-button type="primary" size="large" block round
                @click="scanImg">拍照
    </van-button>
  </div>
</template>

<script>
  export default {
    name: "ImageRecognition",
    data() {
      return {
        width: 320,
        height: 320,
        videoWidth: "",
        videoHeight: "",
        check: this.$route.params.check,
        video: "",
        // 媒体流
        mediaStreamTrack: "",
      }
    },
    mounted() {
      this.width = document.getElementsByClassName("main-div")[0].offsetWidth;
      this.height = document.getElementsByClassName("main-div")[0].offsetHeight;
      if (this.$route.params.base64) {
        this.postData(this.$route.params.base64);
      } else {
        this.recognition();
      }
    },
    created() {
    },
    destroyed() {
      this.mediaStreamTrack.stop();
    },
    methods: {
      recognition() {
        var _this = this;
        _this.video = document.getElementById("video");
        var MediaErr = function (error) {
            if (error.PERMISSION_DENIED) {
              _this.$toast('用户拒绝了浏览器请求媒体的权限');
            } else if (error.NOT_SUPPORTED_ERROR) {
              _this.$toast('对不起，您的浏览器不支持拍照功能，请使用其他浏览器');
            } else if (error.MANDATORY_UNSATISFIED_ERROR) {
              _this.$toast('指定的媒体类型未接收到媒体流');
            } else {
              _this.$toast('系统未能获取到摄像头，请确保摄像头已正确安装。或尝试刷新页面，重试');
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
                _this.$toast("浏览器不支持HTML5 CANVAS");
                return Promise.reject(new Error('getUserMedia is not implemented in this browser'));
              }
              // 否则，为老的navigator.getUserMedia方法包裹一个Promise
              return new Promise(function (resolve, reject) {
                getUserMedia.call(navigator, constraints, resolve, reject);
              });
            }
          }
          navigator.mediaDevices.enumerateDevices().then(function (devices) {
            let len = [];
            devices.forEach(function (dv) {
              if (dv.kind.match(/^video.*/)) {
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
              if ("srcObject" in _this.video) {
                _this.video.srcObject = stream;
              } else {
                // 防止在新的浏览器里使用它，应为它已经不再支持了
                _this.video.src = window.URL.createObjectURL(stream);
              }
              _this.video.onloadedmetadata = function (e) {
                _this.video.play();
                _this.videoWidth = _this.video.videoWidth;
                _this.videoHeight = _this.video.videoHeight;
              };
            }).catch(MediaErr);
        } catch (e) {
          _this.$toast("浏览器不支持HTML5 CANVAS");
        }
      },
      scanImg() {
        let _this = this;
        _this.mediaStreamTrack.stop();
        let canvas = document.getElementById("canvas");
        canvas.style.marginTop = _this.height - _this.videoHeight - 6 + "px";
        let ctx = canvas.getContext('2d');
        ctx.drawImage(_this.video, 0, 0, _this.videoWidth, _this.videoHeight);  // 将video中的数据绘制到canvas里
        let base64 = canvas.toDataURL("image/png");//这个就是base64编码
        // base64 = base64.replace("data:image/png;base64,", "");
        base64 = encodeURIComponent(base64);
        // console.log(base64);
        //this.$formatPostData({
        _this.postData(base64);
      },
      postData(base64) {
        let _this = this;
        _this.$axios.post('/recognition.do', "file=" + base64 + "&").then((response) => {
          _this.$toast(response.data.msg);
          if (response.data.code == _this.$result.code.CODE_SUCCESS) {
            _this.$router.push({name: 'manualInput', params: {rec: response.data.data,}});
          } else {
            if (_this.$route.params.base64) {
              _this.$router.push({name: 'home',});
            } else {
              window.location.reload();
            }
          }
        }).catch((response) => {
        })
      },
    }
  }
</script>

<style scoped>
  .main-div {
    height: 80vh;
    width: 100vw;
  }

  .van-button {
    position: absolute;
    bottom: 55px;
  }

  video：-webkit-full-screen {
    width: 100%;
    height: 80%;
  }

  canvas {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
  }
</style>
