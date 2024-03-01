import request from '@/utils/request'

// 查询农作物种植记录跟踪信息列表
export function listInfo(query) {
  return request({
    url: '/plant/records/list',
    method: 'get',
    params: query
  })
}

// 查询农作物种植记录跟踪信息详细
export function getInfo(planId) {
  return request({
    url: '/plant/records/' + planId,
    method: 'get'
  })
}

// 新增农作物种植记录跟踪信息
export function addInfo(data) {
  return request({
    url: '/plant/records',
    method: 'post',
    data: data
  })
}

// 修改农作物种植记录跟踪信息
export function updateInfo(data) {
  return request({
    url: '/plant/records',
    method: 'put',
    data: data
  })
}

// 删除农作物种植记录跟踪信息
export function delInfo(planId) {
  return request({
    url: '/plant/records/' + planId,
    method: 'delete'
  })
}
// 查询全部农作物列表  字典表
export function CorpInfoList() {
  return request({
    url: '/plant/records/getCorpInfoList',
    method: 'get'
  })
}
// 查询农作物的种植记录信息
export function pathListByCrop(query) {
  return request({
    url: '/plant/records/pathListByCrop',
    method: 'get',
    params: query

  })
}
