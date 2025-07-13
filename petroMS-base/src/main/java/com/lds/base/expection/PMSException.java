package com.lds.base.expection;


/**
 * @description 石油项目异常类
 * @author lds
 * @version 1.0
 */
public class PMSException extends RuntimeException {

   private String errMessage;

   public PMSException() {
      super();
   }

   public PMSException(String errMessage) {
      super(errMessage);
      this.errMessage = errMessage;
   }

   public String getErrMessage() {
      return errMessage;
   }

   public static void cast(String errMessage){
      throw new PMSException(errMessage);
   }

   public static void cast(CommonError commonError){
       throw new PMSException(commonError.getErrMessage());
   }

}