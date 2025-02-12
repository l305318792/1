package com.yunchuan.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunchuan.medical.entity.ScheduleRule;
import com.yunchuan.medical.dto.ScheduleRuleDTO;
import java.util.List;

/**
 * 排班规则服务接口
 */
public interface ScheduleRuleService extends IService<ScheduleRule> {
    
    /**
     * 创建排班规则
     */
    ScheduleRuleDTO createRule(ScheduleRuleDTO ruleDTO);
    
    /**
     * 更新排班规则
     */
    ScheduleRuleDTO updateRule(String id, ScheduleRuleDTO ruleDTO);
    
    /**
     * 删除排班规则
     */
    void deleteRule(String id);
    
    /**
     * 获取排班规则
     */
    ScheduleRuleDTO getRule(String id);
    
    /**
     * 获取医生的排班规则列表
     */
    List<ScheduleRuleDTO> getDoctorRules(String doctorId);
    
    /**
     * 生成排班表
     */
    void generateSchedules(String doctorId, String ruleId);
    
    /**
     * 预览排班表
     */
    List<ScheduleRuleDTO> previewSchedules(String doctorId, String ruleId);
    
    /**
     * 启用/禁用排班规则
     */
    void updateRuleStatus(String id, String status);
} 