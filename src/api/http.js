// 引入axios
import axios from 'axios';
import store from '@/api/index.js'
let baseUrl = "http://localhost:1145/"
//let baseUrl="http://116.204.112.5:1145/";
// 创建axios实例
const httpService = axios.create({
  // url前缀-'http:xxx.xxx'
  // baseURL: process.env.BASE_API, // 需自定义
  baseURL:baseUrl,
  // 请求超时时间
  timeout: 90000 // 需自定义
});

//添加请求和响应拦截器
// 添加请求拦截器
httpService.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  //config.headers.token=window.sessionStorage.getItem('token');
  console.log("store="+store.getters.GET_TOKEN)
  config.headers.token=store.getters.GET_TOKEN
  return config;
}, function (error) {
  // 对请求错误做些什么
  return Promise.reject(error);
});

// 添加响应拦截器
httpService.interceptors.response.use(function (response) {
  // 对响应数据做点什么
  return response;
}, function (error) {
  // 对响应错误做点什么
  return Promise.reject(error);
});

/*网络请求部分*/

/*
 *  get请求
 *  url:请求地址
 *  params:参数
 *  headers
 * */
export function get(url, params = {}, headers = {}) {
  return new Promise((resolve, reject) => {
    httpService({
      url: url,
      method: 'get',
      params: params,
      headers: headers
    }).then(response => {
      resolve(response);
    }).catch(error => {
      reject(error);
    });
  });
}

export function put(url, params = {}) {
  return new Promise((resolve, reject) => {
    httpService({
      url: url,
      method: 'put',
      params: params
    }).then(response => {
      resolve(response);
    }).catch(error => {
      reject(error);
    });
  });
}
/*

 */

/*
 *  post请求
 *  url:请求地址
 *  params:参数
 * */
export function post2(url, params = {}) {
  return new Promise((resolve, reject) => {
    httpService({
      url: url,
      method: 'post',
      params: params,
      headers: {
        'Content-Type': 'application/json'
      }

    }).then(response => {
      resolve(response);
    }).catch(error => {
      reject(error);
    });
  });
}

export function post(url, params = {}) {
  return new Promise((resolve, reject) => {
    httpService({
      url: url,
      method: 'post',
      data: params,
      headers: {
        'Content-Type': 'application/json'
      }

    }).then(response => {
      resolve(response);
    }).catch(error => {
      reject(error);
    });
  });
}

export function postWithHeader(url, params = {}, headers = {}) {
  return new Promise((resolve, reject) => {
    httpService({
      url: url,
      method: 'post',
      data: params,
      headers: headers

    }).then(response => {
      resolve(response);
    }).catch(error => {
      reject(error);
    });
  });
}

export function post2WithHeader(url, params = {}, headers = {}) {
  return new Promise((resolve, reject) => {
    httpService({
      url: url,
      method: 'post',
      params: params,
      headers: headers

    }).then(response => {
      resolve(response);
    }).catch(error => {
      reject(error);
    });
  });
}

/*
 *  文件上传
 *  url:请求地址
 *  params:参数
 * */
export function fileUpload(url, params = {}) {
  return new Promise((resolve, reject) => {
    httpService({
      url: url,
      method: 'post',
      data: params,
      headers: { 'Content-Type': 'multipart/form-data' }
    }).then(response => {
      resolve(response);
    }).catch(error => {
      reject(error);
    });
  });
}

export function getServerUrl(){
  return baseUrl;
}

export function getUrlWithoutSlash() {
  return baseUrl.endsWith('/') ? baseUrl.slice(0, -1) : baseUrl;
}


export default {
  get,
  put,
  post2,
  post,
  baseUrl,
  fileUpload,
  getServerUrl,
  getUrlWithoutSlash
}
