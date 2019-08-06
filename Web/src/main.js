import Vue from 'vue'
import Vuex from 'vuex';
import App from './App'
import router from './router'
import axios from "axios";
import {
  Cell,
  CellGroup,
  Col,
  DatetimePicker,
  Divider,
  Field,
  Icon,
  List,
  NavBar,
  NoticeBar,
  Panel,
  Picker,
  Popup,
  Row,
  Search,
  Tabbar,
  TabbarItem,
  Toast
} from 'vant';
import { SwipeCell } from 'vant';
import { Dialog } from 'vant';
import Button from 'vant/lib/button';


import 'vant/lib/index.css';

Vue.use(Vuex);
Vue.use(NavBar);
Vue.use(Tabbar).use(TabbarItem);
Vue.use(Row).use(Col);
Vue.use(Icon);
Vue.use(Cell).use(CellGroup);
Vue.use(Field);
Vue.use(Divider);
Vue.use(NoticeBar);
Vue.use(Popup);
Vue.use(DatetimePicker);
Vue.use(Picker);
Vue.use(Button);
Vue.use(List);
Vue.use(Search);
Vue.use(Panel);
Vue.use(Toast);
Vue.use(SwipeCell);
Vue.use(Dialog);

Vue.config.productionTip = false;
Vue.prototype.$axios = axios;
const store = new Vuex.Store({state: {mainPage: true,}});
export default store;

Vue.prototype.$result = {
  code: {CODE_SUCCESS: "200"},
  msg: {}
};

Vue.prototype.$formatPostData = function (data) {
  var myParams = "";
  for (var elem in data) {
    myParams += elem + "=" + data[elem] + "&";
  }
  return  myParams;
};

new Vue({
  el: '#app',
  router, store,
  components: {App},
  template: '<App/>',
  render: h => h(App)
});

