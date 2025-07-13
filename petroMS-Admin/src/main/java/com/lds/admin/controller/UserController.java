package com.lds.admin.controller;


import com.lds.admin.annotation.Encrypt;
import com.lds.admin.pojo.dto.UserDto;
import com.lds.admin.service.IUserService;
import com.lds.base.model.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lds
 * @since 2025-05-08
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;


    @PostMapping("/login")
    public RestResponse<String> token(@RequestBody UserDto userDto) {

        String token = userService.toLogin(userDto);

        return RestResponse.success(token);
    }



    @PostMapping("/test")
    @PreAuthorize("hasAuthority('test')")
    public RestResponse<String> test1() {
        return RestResponse.success("111111");
    }

    @PostMapping("/test1")
    @PreAuthorize("hasAuthority('add')")
    public RestResponse<String> test2() {
        return RestResponse.success("111111");
    }
}
