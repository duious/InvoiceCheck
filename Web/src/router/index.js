import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/Home'
import ManualInput from '../components/ManualInput'
import InvoiceLibrary from '../components/InvoiceLibrary'
import QRCodeScan from '../components/QRCodeScan'
import ImageRecognition from '../components/ImageRecognition'

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
      menuIndex: 0,
    },
    {
      path: '/ManualInput',
      name: 'manualInput',
      component: ManualInput,
      menuIndex: 0,
    },
    {
      path: '/InvoiceLibrary',
      name: 'invoiceLibrary',
      component: InvoiceLibrary,
      menuIndex: 1,
    },
    {
      path: '/QRCodeScan',
      name: 'qRCodeScan',
      component: QRCodeScan,
      menuIndex: 1,
    },
    {
      path: '/ImageRecognition',
      name: 'imageRecognition',
      component: ImageRecognition,
      menuIndex: 1,
    }
  ]
})
