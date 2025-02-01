package com.yunchuan.medical.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class LoginResultDTO {
    private String token;
    private UserDTO user;
} 