import request from '@/utils/request'

// 获取在线用户数量
export function getOnlineUserCount() {
  return request({
    url: '/monitor/online/count',
    method: 'get'
  })
}

// 查询在线用户列表
export function list(query) {
  return request({
    url: '/monitor/online/list',
    method: 'get',
    params: query
  })
}

// 强退用户
export function forceLogout(tokenId) {
  return request({
    url: '/monitor/online/' + tokenId,
    method: 'delete'
  })
}
