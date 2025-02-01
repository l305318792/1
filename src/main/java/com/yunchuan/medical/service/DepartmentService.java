package com.yunchuan.medical.service;

import com.yunchuan.medical.entity.Department;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 科室表 服务类
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 获取所有科室列表
     */
    List<Department> getDepartmentList();
}
