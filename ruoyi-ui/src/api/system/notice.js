import request from '@/utils/request'

// 查询通知的精简信息列表
export function getNotices() {
  return request({
      url: '/system/notice/notices',
      method: 'get'
  })
}

// 查询新闻中心（公告）的精简信息列表
export function getNews() {
  return request({
      url: '/system/notice/news',
      method: 'get'
  })
}

// 查询公告列表
export function listNotice(query) {
  return request({
    url: '/system/notice/list',
    method: 'get',
    params: query
  })
}

// 查询公告详细
export function getNotice(noticeId) {
  return request({
    url: '/system/notice/' + noticeId,
    method: 'get'
  })
}

// 新增公告
export function addNotice(data) {
  return request({
    url: '/system/notice',
    method: 'post',
    data: data
  })
}

// 修改公告
export function updateNotice(data) {
  return request({
    url: '/system/notice',
    method: 'put',
    data: data
  })
}

// 删除公告
export function delNotice(noticeId) {
  return request({
    url: '/system/notice/' + noticeId,
    method: 'delete'
  })
}