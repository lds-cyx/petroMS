package com.lds.base.expection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
//@RestControllerAdvice = @ControllerAdvice+ @ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {
    // 我可以预知道的异常
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(PMSException.class)
    public RestErrorResponse customException1(PMSException e){
        log.error("系统异常{}",e.getErrMessage(),e);
//        解析出异常 再返回给前端
        String errMessage = e.getErrMessage();
        RestErrorResponse restErrorResponse = new RestErrorResponse(errMessage);
        return restErrorResponse;
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public RestErrorResponse Exception(Exception e){
        // 系统异常
        log.error("系统异常{}",e.getMessage(),e);
        //解析出异常 再返回给前端
        String errMessage = e.getMessage();
        RestErrorResponse restErrorResponse = null;
        if (errMessage.equals("不允许访问")){
            restErrorResponse = new RestErrorResponse("您没有权限访问!");
            return restErrorResponse;
        }
        restErrorResponse = new RestErrorResponse(CommonError.UNKNoW_ERROR.getErrMessage());
        return restErrorResponse;
    }


}
