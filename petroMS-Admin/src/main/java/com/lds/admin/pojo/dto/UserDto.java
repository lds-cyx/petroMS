package com.lds.admin.pojo.dto;

import com.lds.admin.pojo.po.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto extends User {
    private String validateCode;
    private List<String> permission = new ArrayList<>();
}
