package com.lds.admin.pojo.po;

import lombok.Data;

@Data
public class PermitDetail {
    private Long userId;
    private int roleId;
    private String description;
    private String permit;
}
