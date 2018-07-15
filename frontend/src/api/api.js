import axios from 'axios';

let base = '/api';

//登录
export const requestLogin = params => { return axios.post(`/login`, params).then(res => res.data); };

/**测试报告相关API */
//1. 添加测试报告
export const addTestReport = params => { return axios.post(`${base}/testReport`, params); };
//2. 获取测试报告列表
export const getTestReportList = params => { return axios.get(`${base}/testReport/list/`+params.id,{ params: params });}
//3. 查询指定报告
export const getTestReport = params => { return axios.get(`${base}/testReport/`+params);}
//4. 删除指定报告
export const removeTestReport = params => {return axios.delete(`${base}/testReport/`+ params);}
//5. 创建报告的选项
export const getOption = params => { return axios.get(`${base}/testReport/options/`+params);}

/**项目相关 */
//1. 获取项目列表
export const getProjectList = params => { return axios.get(`${base}/project`, { params: params });}
//2. 添加项目
export const addProject = params => { return axios.post(`${base}/project`, params,{headers: {"Content-Type": "application/json"}});}
//3. 删除项目
export const removeProject = params => { return axios.delete(`${base}/project/`+params);}
//4. 编辑项目
export const editProject = params => { return axios.put(`${base}/project`,params,{headers: {"Content-Type": "application/json"}});}








