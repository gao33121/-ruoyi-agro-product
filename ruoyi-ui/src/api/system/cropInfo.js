import request from '@/utils/request'

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
    url: '/plant/manage/' + id,
    method: 'delete'
  })
}
