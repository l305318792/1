import request from '@/utils/request'

// 修改前: /api/departments
// 修改后: /api/admin/departments
export function getDepartmentList() {
  return request({
    url: '/api/admin/departments',
    method: 'get'
  })
}

export function addDepartment(data: any) {
  return request({
    url: '/api/admin/departments',
    method: 'post',
    data
  })
}

export function updateDepartment(id: string, data: any) {
  return request({
    url: `/api/admin/departments/${id}`,
    method: 'put',
    data
  })
}

export function deleteDepartment(id: string) {
  return request({
    url: `/api/admin/departments/${id}`,
    method: 'delete'
  })
} 