package com.yunchuan.medical.service;

import com.yunchuan.medical.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名获取用户信息
     */
    User getUserByUsername(String username);

    /**
     * 根据ID获取用户
     */
    User getById(String id);
}
