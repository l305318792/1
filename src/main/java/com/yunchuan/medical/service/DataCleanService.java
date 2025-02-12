package com.yunchuan.medical.service;

/**
 * 数据清理服务接口
 */
public interface DataCleanService {
    
    /**
     * 清理所有相关表的数据
     */
    void cleanAllData();
    
    /**
     * 清理用户表数据
     */
    void cleanUserData();
    
    /**
     * 清理医生表数据
     */
    void cleanDoctorData();
    
    /**
     * 清理科室表数据
     */
    void cleanDepartmentData();
    
    /**
     * 清理问诊表数据
     */
    void cleanConsultationData();
} 