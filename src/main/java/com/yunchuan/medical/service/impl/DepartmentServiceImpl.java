package com.yunchuan.medical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.mapper.DepartmentMapper;
import com.yunchuan.medical.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 科室表 服务实现类
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
@Slf4j
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public List<Department> getDepartmentList() {
        // 按照创建时间排序，确保父科室在前
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Department::getCreateTime);
        List<Department> departments = this.list(wrapper);
        log.info("查询到科室数量: {}", departments.size());
        departments.forEach(dept -> log.info("科室: {}, 父ID: {}", dept.getName(), dept.getParentId()));
        return departments;
    }
}
