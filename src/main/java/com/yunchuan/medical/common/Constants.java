package com.yunchuan.medical.common;

public class Constants {
    
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    
    public static class Status {
        public static final String ENABLE = "1";
        public static final String DISABLE = "0";
    }
    
    public static class Role {
        public static final String ADMIN = "ADMIN";
        public static final String DOCTOR = "DOCTOR";
        public static final String PATIENT = "PATIENT";
    }
    
    public static class Time {
        public static final String DATE_FORMAT = "yyyy-MM-dd";
        public static final String TIME_FORMAT = "HH:mm:ss";
        public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    }
    
    public static class File {
        public static final String UPLOAD_PATH = "upload/";
        public static final long MAX_SIZE = 10 * 1024 * 1024;
        public static final String[] ALLOW_TYPES = {".jpg", ".jpeg", ".png", ".gif", ".pdf", ".doc", ".docx"};
    }
} 