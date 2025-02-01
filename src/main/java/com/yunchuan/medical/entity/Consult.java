package com.yunchuan.medical.entity;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * 咨询实体类
 */
@Data
@TableName("consult")
public class Consult {
    
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    private String userId;
    
    private String doctorId;
    
    private String title;
    
    private String description;
    
    private BigDecimal fee;
    
    private String status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 