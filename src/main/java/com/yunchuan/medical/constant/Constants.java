package com.yunchuan.medical.constant;

/**
 * 系统常量
 */
public class Constants {

    /**
     * 状态 - 启用
     */
    public static final String STATUS_ENABLED = "enabled";

    /**
     * 状态 - 禁用
     */
    public static final String STATUS_DISABLED = "disabled";

    /**
     * 角色 - 管理员
     */
    public static final String ROLE_ADMIN = "ADMIN";

    /**
     * 角色 - 医生
     */
    public static final String ROLE_DOCTOR = "DOCTOR";

    /**
     * 角色 - 用户
     */
    public static final String ROLE_USER = "USER";

    /**
     * 支付状态 - 待支付
     */
    public static final String PAYMENT_PENDING = "pending";

    /**
     * 支付状态 - 支付成功
     */
    public static final String PAYMENT_SUCCESS = "success";

    /**
     * 支付状态 - 支付失败
     */
    public static final String PAYMENT_FAILED = "failed";

    /**
     * 支付状态 - 已退款
     */
    public static final String PAYMENT_REFUNDED = "refunded";

    /**
     * 支付方式 - 支付宝
     */
    public static final String PAYMENT_METHOD_ALIPAY = "alipay";

    /**
     * 支付方式 - 微信支付
     */
    public static final String PAYMENT_METHOD_WECHAT = "wechat";

    /**
     * 业务类型 - 预约挂号
     */
    public static final String BUSINESS_TYPE_APPOINTMENT = "appointment";

    /**
     * 业务类型 - 在线咨询
     */
    public static final String BUSINESS_TYPE_CONSULTATION = "consultation";

    // 医生职称
    public static final String TITLE_CHIEF = "主任医师";
    public static final String TITLE_ASSOCIATE_CHIEF = "副主任医师";
    public static final String TITLE_ATTENDING = "主治医师";
    public static final String TITLE_RESIDENT = "住院医师";
    
    // 排班时段
    public static final String PERIOD_MORNING = "上午";
    public static final String PERIOD_AFTERNOON = "下午";
    public static final String PERIOD_EVENING = "晚上";
    
    // 排班状态
    public static final String SCHEDULE_AVAILABLE = "可预约";
    public static final String SCHEDULE_FULL = "已约满";
    public static final String SCHEDULE_CANCELLED = "已取消";
    
    // 预约状态
    public static final String APPOINTMENT_PENDING = "PENDING";
    public static final String APPOINTMENT_COMPLETED = "COMPLETED";
    public static final String APPOINTMENT_CANCELLED = "CANCELLED";
    
    // 医生状态
    public static final String DOCTOR_ACTIVE = "在职";
    public static final String DOCTOR_INACTIVE = "离职";
    public static final String DOCTOR_ON_LEAVE = "休假";
    
    // 评价状态
    public static final String RATING_PENDING = "待回复";
    public static final String RATING_REPLIED = "已回复";
    public static final String RATING_HIDDEN = "已隐藏";
} 