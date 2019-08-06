# InvoiceCheck
发票登记、查重工具
##### 可用于个人、组织内部搭建私有化、统一发票登记、查重平台；
##### 数据存储在Redis，可自行扩展为其他数据库；
##### 提供了浏览器端视屏输入，用于实现二维码识别(需要https协议，证书及nginx等配置自行处理，Web端已实现https协议，修改代理或端口即可使用)；
##### 安卓APP采取WebView套壳处理，封装zxing二维码识别以及原始摄像头调取，SDK27版本(密钥文件自行处理)；
##### Server端仅实现核心业务逻辑，可直接打包运行；

### APP安卓
##### 权限：摄像头、网络、手机信息、文件存取
##### 封装：zxing二维码识别
### Web
##### 框架：Vue 2.5.2、axios、vant UI、
##### 核心：浏览器唤起摄像头(原则上android、ios原装浏览器可以唤起，win主流最新可以唤起，ios摄像头唤起为前置无解，mac未测试)
##### 逻辑：1、自适应浏览器、App环境处理，
###### App：二维码识别zxing识别，web接受结果；
###### Web：decode识别+处理；
###### 无二维码：百度文字识别处理，后台实现(需要百度云账号，开发文档：https://cloud.baidu.com/doc/OCR/s/zjwvxzrw8/)；
##### 2、已录入发票查看(可自行添加导出、检索功能)
## Server
##### JDK1.8 + redis + jedis工具包 + springboot 2.0 + https协议
##### redis list存储全部信息
##### redis set存储验证信息
##### 图片文件base64形式传输

### 配置
##### 修改以下基础配置即可运行
#### App：
##### app/src/main/res/values/string.xml → host
#### Web：
##### config/index.js → proxyTable
#### Server：
##### src/main/resources/application.properties → server.port、spring.redis.host
##### src/main/java/com/sjxd/invoicecheckserver/util/UtilBaiDu.java → filePath、APP_ID、API_KEY、SECRET_KEY
