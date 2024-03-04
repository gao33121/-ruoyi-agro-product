import request from '@/utils/request'

//报表查询灌溉方式
export function irrStatement(){
   return request({
     url:"/plant/manage/irrStatement",
     method:"get"
   })
}

// 查询农作物信息列表
export function listInfo(query) {
  return request({
    url: '/plant/manage/list',
    method: 'get',
    params: query
  })
}

// 查询农作物信息详细
export function getInfo(id) {
  return request({
    url: '/plant/manage/' + id,
    method: 'get'
  })
}

// 新增农作物信息
export function addInfo(data) {
  return request({
    url: '/plant/manage',
    method: 'post',
    data: data
  })
}

//获取种植方式列表
export function planList(){
  return request({
    url:"/plant/manage/planList",
    method:"post"
  })
}
//获取灌溉方式列表
export function irrList(){
  return request({
    url:"/plant/manage/irrList",
    method:"post"
  })
}
//获取肥料类型列表
export function fertList(){
  return request({
    url:"/plant/manage/fertList",
    method:"post"
  })
}
// 修改农作物信息
export function updateInfo(data) {
  return request({
    url: '/plant/manage',
    method: 'put',
    data: data
  })
}

// 删除农作物信息
export function delInfo(id) {
  return request({
    url: '/plant/manage/del?ids=' + id,
    method: 'get'
  })
}

// 导入农作物信息
export function getrntry() {
  return request({
    url: '/plant/manage/getRid',
    method:'post'
  })
}
//饼形灌溉方式
export function bingStatement(){
  return request({
    url: "/plant/manage/getBing",
    method:'post'
  })
}
