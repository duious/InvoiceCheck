<template>
  <div class="main-div">
    <div class="title">发票信息</div>
    <van-divider dashed></van-divider>
    <van-cell-group>
      <van-field label="发票类型" type="text" size="large" label-width="76px"
                 v-model="formData.invoiceType"
                 placeholder="请输入发票类型"></van-field>
      <van-field label="发票代码" type="number" size="large" label-width="76px"
                 v-model="formData.invoiceCode"
                 placeholder="请输入发票代码"></van-field>
      <van-field label="发票号码" type="number" required size="large" label-width="76px"
                 v-model="formData.invoiceNo"
                 placeholder="请输入发票号码"></van-field>
      <van-field label="开票日期" type="data" required size="large" label-width="76px"
                 placeholder="请输入开票日期" readonly v-model="formData.invoiceDate"
                 @focus="initTimePicker"></van-field>
      <van-field label="校验码" type="text" size="large" label-width="76px"
                 v-model="formData.invoiceCheckCode"
                 placeholder="请输入校验码"></van-field>
      <van-field label="发票金额" type="number" size="large" label-width="76px"
                 maxlength="14" v-model="formData.invoiceAmountOfMoney"
                 placeholder="请输入发票金额">
        <span slot="right-icon">(不含税)</span>
      </van-field>
      <van-field label="税率(%)" type="text" size="large" label-width="76px"
                 placeholder="请输入税率(%)" readonly v-model="formData.taxRate"
                 @focus="initTaxRate"></van-field>
      <van-field label="税额" type="number" size="large" label-width="76px"
                 v-model="formData.invoiceTaxAmount"
                 placeholder="请输入税额"></van-field>
      <van-field label="价税合计" type="number" size="large" label-width="76px"
                 placeholder="请输入价税合计" readonly
      ></van-field>
    </van-cell-group>
    <br>
    <van-button type="primary" size="large" block round
                @click="commitFormData">提交保存
    </van-button>
    <van-popup v-model="showTimePicker" position="bottom">
      <van-datetime-picker v-if="showTimePicker"
                           type="date" @confirm="confirmTimePicker" @cancel="cancelTimePicker"
                           :formatter="timePickerFormatter"></van-datetime-picker>
    </van-popup>
    <van-popup v-model="showTaxRate" position="bottom">
      <van-picker v-if="showTaxRate" :columns="taxRate"
                  show-toolbar @confirm="confirmTaxRate" @cancel="cancelTaxRate"></van-picker>
    </van-popup>
  </div>
</template>

<script>
  export default {
    name: "ManualInput",
    data() {
      return {
        formCheck: 'info',
        showTimePicker: false,
        showTaxRate: false,
        taxRate: ['0%', '3%', '5%', '6%', '7%', '10%', '14%', '16%', '17%'],
        formData: {
          invoiceType: "", invoiceCode: "", invoiceNo: "",
          invoiceDate: "", invoice_date: "",
          invoiceCheckCode: "", invoiceAmountOfMoney: "",
          taxRate: "", invoiceTaxAmount: "",
        },
        QRCodeRes: this.$route.params.res,
        check: this.$route.params.check,
        recognition: this.$route.params.rec,
      }
    },
    created: function () {
      this.$store.state.mainPage = false;
      if (this.QRCodeRes && this.QRCodeRes.indexOf(",") != -1) {
        this.QRCodeRes = this.QRCodeRes.split(",");
        this.formData.invoiceType = this.QRCodeRes[1];
        switch (this.QRCodeRes[1]) {
          case "10":
            this.formData.invoiceType = "增值税电子普通发票";
            break;
          case "04":
            this.formData.invoiceType = "增值税普通发票";
            break;
          case "01":
            this.formData.invoiceType = "增值税专用发票";
            break;
        }
        this.formData.invoiceCode = this.QRCodeRes[2];
        this.formData.invoiceNo = this.QRCodeRes[3];
        this.formData.invoiceDate = this.QRCodeRes[5].substring(0, 4) + "年" + this.QRCodeRes[5].substring(4, 6) + "月" + this.QRCodeRes[5].substring(6) + "日";
        this.formData.invoiceCheckCode = this.QRCodeRes[7];
        this.formData.invoiceAmountOfMoney = this.QRCodeRes[4];
        if (this.check == "true") {
          this.commitFormData();
        }
      }
      if (this.recognition) {
        this.formData.invoiceAmountOfMoney = this.recognition.amountOfMoney;
        this.formData.invoiceCheckCode = this.recognition.checkCode;
        this.formData.invoiceCode = this.recognition.code;
        this.formData.invoiceDate = this.recognition.date.substring(this.recognition.date.indexOf("20"));
        this.formData.invoiceNo = this.recognition.number.replace(/[^0-9]/ig, "");
        this.formData.invoiceTaxAmount = this.recognition.taxAmount;
        this.formData.taxRate = this.recognition.taxRate;
        this.formData.invoiceType = this.recognition.type;
      }
    },
    mounted() {
      this.initFormData();
    },
    methods: {
      initTimePicker() {
        this.showTimePicker = true
      },
      timePickerFormatter(type, value) {
        if (type === 'year') {
          return `${value}年`;
        } else if (type === 'month') {
          return `${value}月`
        } else if (type === 'day') {
          return `${value}日`
        }
        return value;
      },
      confirmTimePicker(val) {
        this.formData.invoice_date = new Date(val).getTime();
        this.formData.invoiceDate = this.formatDate(val, 3).substring(0, 11);
        this.showTimePicker = false;
      },
      cancelTimePicker(val) {
        this.showTimePicker = false;
      },
      initTaxRate() {
        this.showTaxRate = true
      },
      confirmTaxRate(val) {
        this.formData.taxRate = val;
        this.showTaxRate = false;
      },
      cancelTaxRate(val) {
        this.showTaxRate = false;
      },
      formatDate(date, type) {
        var d = this.validate(date) == true ? new Date(date) : new Date(), s1 = "-", s2 = ":",
          fN = function getFormatNum(val) {
            if (val >= 0 && val <= 9) {
              return "0" + val;
            }
            return val;
          }, sd = fN(d.getDate()), sh = fN(d.getHours()), sm = fN(d.getMinutes()), ss = fN(d.getSeconds()),
          m = d.getMonth() + 1;
        if (m >= 1 && m <= 9) {
          m = "0" + m;
        }
        if (type && type == 1) {
          return d.getFullYear() + s1 + m + s1 + sd + " " + sh + s2 + sm + s2 + ss;
        }
        if (type && type == 2) {
          return d.getFullYear() + m + sd + sh + sm + ss;
        }
        if (type && type == 3) {
          return d.getFullYear() + "年" + m + "月" + sd + "日" + sh + "时" + sm + "分" + ss + "秒";
        }
        return d.getFullYear() + s1 + m + s1 + sd + " " + sh + s2 + sm + s2 + ss;
      },
      validate(val, type) {
        if ('' == type || "" == type || null == type || undefined == type) {
          if (!String(val).trim() || '' == String(val).trim() || "" == String(val).trim() ||
            null == String(val).trim() || "null" == String(val).trim() || 'null' == String(val).trim() ||
            undefined == String(val).trim()) {
            return false;
          }
          if (0 == parseInt(String(val).trim())) {
            return false;
          }
          if (String(val).trim().length > 60) {
            return false;
          }
        }
        if ("mobile" == type) {
          if (String(val).trim().length != 11 || !(/^1[3|4|5|7|8][0-9]\d{4,8}$/.test(String(val).trim()))) {
            return false;
          }
        }
        if ("email" == type) {
          if (!(/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(String(val).trim()))) {
            return false;
          }
        }
        if ("uuid" == type) {
          if (!(/^[0-9a-z]{8}[0-9a-z]{4}[0-9a-z]{4}[0-9a-z]{4}[0-9a-z]{12}$/.test(String(val).trim()))) {
            return false;
          }
        }
        if ("url" == type) {
          if (!(/^((ht|f)tps?):\/\/([\w\-]+(\.[\w\-]+)*\/)*[\w\-]+(\.[\w\-]+)*\/?(\?([\w\-\.,@?^=%&:\/~\+#]*)+)?/.test(String(val).trim()))) {
            return false;
          }
        }
        if ("pwd" == type) {
          if (!(/^[a-zA-Z][a-zA-Z0-9_]{5,15}$/.test(String(val).trim()))) {
            return false;
          }
        }
        if ("num" == type) {
          if (!(/^^[0-9]*$/.test(String(val).trim()))) {
            return false;
          }
        }
        return true;
      },
      commitFormData() {
        if (this.validate(this.formData.invoice_date) != true) {
          this.formData.invoice_date = new Date(this.formData.invoiceDate.replace("年", "/").replace("月", "/").replace("日", "") + " 08:00:00").getTime();
        }
        var postData = {
          amountOfMoney: this.formData.invoiceAmountOfMoney,
          checkCode: this.formData.invoiceCheckCode,
          code: this.formData.invoiceCode,
          date: this.formData.invoice_date,
          number: this.formData.invoiceNo,
          taxAmount: this.formData.invoiceTaxAmount,
          taxRate: this.formData.taxRate,
          type: this.formData.invoiceType
        };
        this.$axios.post('/save.do', this.$formatPostData(postData)).then((response) => {
          this.$toast(response.data.msg);
          if (response.data.code == this.$result.code.CODE_SUCCESS) {
            setTimeout(this.resetFormData, 800);
          }
        }).catch((response) => {
        })
      },
      initFormData() {
        // this.formData.invoiceDate = this.formatDate(new Date(), 3).substring(0, 11);
        this.formData.invoice_date = "";
        this.formData.taxRate = "";
      },
      resetFormData() {
        this.formData = {
          invoiceType: "", invoiceCode: "", invoiceNo: "",
          invoiceDate: "", invoice_date: "",
          invoiceCheckCode: "", invoiceAmountOfMoney: "",
          taxRate: "", invoiceTaxAmount: "",
        }
      },
    }
  }
</script>

<style scoped>
  .main-div {
    padding: 10px 20px;
    padding-bottom: 55px;
  }

  .title {
    text-align: center;
    font-size: 20px;
  }

  .van-cell, .van-cell span {
    font-size: 20px !important;
  }

  .van-button--primary {
    background-color: #ff6100 !important;
    border: 1px solid #ff6100 !important;
  }

</style>
