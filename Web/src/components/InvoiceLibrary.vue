<template>
  <div class="main-div">
    <van-cell-group class="search-div">
      <van-field label="录入日期" type="text" size="large" label-width="76px"
                 placeholder="开始时间" :value="formData.stime"
                 readonly></van-field>
      <van-field label="-" type="text" size="large" label-width="16px"
                 placeholder="截止时间" :value="formData.etime"
                 readonly></van-field>
    </van-cell-group>
    <van-search label="查询内容" background="#ffffff"
                placeholder="请输入搜索关键词" v-model="formData.query"></van-search>
    <van-list v-model="invoiceListLoading" :finished="invoiceListFinished"
              direction="up">
      <van-swipe-cell v-for="item in invoiceList" :key="item.invoiceNo" :name="item.invoiceNo"
                      :on-close="closeInvoiceItem">
        <van-cell class="invoice-item-div">
          <template slot="title">
            <van-cell :title="item.invoiceDate" :value="item.invoiceNo"/>
          </template>
          <div slot="label" class="invoice-item-tax">
            <van-cell title="合计：" :value="item.taxTotal"/>
            <van-cell title="税：" :value="item.taxRate"/>
            <van-cell :value="item.invoiceAmountOfMoney"/>
          </div>
        </van-cell>
        <template slot="right">
          <van-button square type="danger" text="删除"/>
        </template>
      </van-swipe-cell>
    </van-list>
  </div>
</template>

<script>
  export default {
    name: "InvoiceLibrary",
    data() {
      return {
        invoiceListLoading: true,
        invoiceListFinished: false,
        showTimePicker: false,
        formData: {
          stime: "",
          etime: "",
          query: "",
        },
        page: {pageNum: 1, pageSize: 10},
        invoiceList: []
      }
    },
    created() {
      this.$store.state.menuIndex = 1;
    },
    mounted() {
      let _this = this;
      _this.initFormData();
      _this.$store.state.menuIndex = 1;
      _this.$axios.post('/list.do', _this.$formatPostData({
        pageNum: _this.page.pageNum,
        pageSize: _this.page.pageSize
      })).then((response) => {
        if (response.data.code == this.$result.code.CODE_SUCCESS) {
          var invoiceItem, newData = {
            invoiceType: "", invoiceCode: "", invoiceNo: "", invoiceDate: "",
            invoiceCheckCode: "", invoiceAmountOfMoney: "",
            taxRate: "", invoiceTaxAmount: "",
          };
          for (var i = 0; i < response.data.data.listData.length; i++) {
            invoiceItem = response.data.data.listData[i];
            newData.invoiceType = invoiceItem["type"];
            newData.invoiceCode = invoiceItem["code"];
            newData.invoiceNo = invoiceItem["number"];
            newData.invoiceDate = invoiceItem["date"];
            newData.invoiceCheckCode = invoiceItem["checkCode"];
            newData.invoiceAmountOfMoney = invoiceItem["amountOfMoney"];
            newData.taxRate = invoiceItem["taxRate"];
            newData.invoiceTaxAmount = invoiceItem["taxAmount"];
            _this.invoiceList.push(JSON.parse(JSON.stringify(newData)));
          }
          _this.invoiceListLoading = false;
          _this.invoiceListFinished = true;
        }
      }).catch((response) => {
      })
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
        this.formData.invoice_data = new Date(val).getTime();
        this.formData.invoiceData = this.formatDate(val, 3).substring(0, 11);
        this.showTimePicker = false;
      },
      cancelTimePicker(val) {
        this.showTimePicker = false;
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
        this.initFormData();
      },
      initFormData() {
        this.formData.stime = this.formatDate(new Date(), 3).substring(0, 11);
        this.formData.etime = this.formatDate(new Date(), 3).substring(0, 11);
        this.formData.query = "";
      },
      closeInvoiceItem(clickPosition, instance, detail) {
        let _this = this;
        switch (clickPosition) {
          case 'left':
          case 'cell':
          case 'outside':
          case 'right':
            _this.$dialog.confirm({
              message: '确定删除此条记录？',
              beforeClose: function (action, done) {
                if (action == 'confirm') {
                  _this.$axios.post('/del.do', _this.$formatPostData({no: detail.name,})).then((response) => {
                    this.$toast(response.data.msg);
                    if (response.data.code == this.$result.code.CODE_SUCCESS) {
                      for (var i = 0; i < _this.invoiceList.length; i++) {
                        if (_this.invoiceList[i].invoiceNo == detail.name) {
                          _this.invoiceList.splice(i, 1);
                          break;
                        }
                      }
                    }
                    done();
                    setTimeout(instance.close, 600);
                  }).catch((response) => {
                  });
                } else {
                  done();
                  setTimeout(instance.close, 600);
                }
              }
            });
            break;
        }
      },
    },
  }
</script>

<style scoped>
  .main-div {
    padding-bottom: 55px;
  }

  .search-div .van-cell {
    float: left;
  }

  .search-div .van-cell:first-child {
    padding-right: 0 !important;
    width: 56% !important;
  }

  .search-div .van-cell:last-child {
    padding-left: 10px !important;
    width: 44% !important;
  }

  .van-cell, .van-cell span, .van-search__label {
    font-size: 18px !important;
  }

  .van-search__content {
    background-color: transparent !important;
    padding-left: 0 !important;
  }

  .van-search {
    padding-left: 10px !important;
  }

  .van-list {
    margin-bottom: 55px !important;
  }

  .invoice-item-div {
    padding: 6px 0px !important;
    border-top: 1px solid #ff6100 !important;
    box-shadow: 0px 1px 4px 1px #ff6100 !important;
  }

  .van-swipe-cell:last-child .invoice-item-div {
    border-bottom: 1px solid #ff6100 !important;
  }

  .invoice-item-div .invoice-item-tax {
    height: 40px !important;
  }

  .invoice-item-div .invoice-item-tax .van-cell {
    width: 33% !important;
    float: left !important;
    padding: 10px 0;
  }

  .invoice-item-div .invoice-item-tax .van-cell:first-child{
    padding-left: 10px;
  }

  .invoice-item-div .invoice-item-tax .van-cell:last-child {
    padding-right: 10px;
  }

  .van-swipe-cell__right .van-button {
    height: 100% !important;
  }
</style>
