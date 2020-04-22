import Vue from 'vue'
import axios from 'axios'
import config from './config'

axios.defaults.baseURL = config.api;
axios.defaults.timeout = 2000;

axios.interceptors.request.use(function (config) {
  // console.log(config);
  return config;
})

Vue.prototype.$http = axios;//将axios添加到Vue对象原型，一切Vue对象都可以使用该对象

axios.loadData = async function (url) {
  const resp = await axios.get(url);
  return resp.data;
}









































