import request from '@/utils/request'

// 修改前: /api/doctors
// 修改后: /api/admin/doctors
export function getDoctorList() {
  return request({
    url: '/api/admin/doctors',
    method: 'get'
  })
}

// 其他医生相关接口也需要修改路径 