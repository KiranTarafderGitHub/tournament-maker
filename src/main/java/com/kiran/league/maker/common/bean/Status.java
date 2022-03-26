package com.kiran.league.maker.common.bean;

public class Status
{
    public static final int OK                            = 200; /*HttpStatus.OK*/

    public static final int CREATED                       = 201; /*HttpStatus.CREATED*/

    public static final int ACCEPTED                      = 202; /*HttpStatus.ACCEPTED*/

    public static final int NON_AUTHORITATIVE_INFORMATION = 203; /* HttpStatus.NON_AUTHORITATIVE_INFORMATION*/

    public static final int NO_CONTENT                    = 204; /*HttpStatus.NO_CONTENT*/

    public static final int RESET_CONTENT                 = 205; /*HttpStatus.RESET_CONTENT*/

    public static final int MULTI_STATUS                  = 207; /*HttpStatus.MULTI_STATUS*/

    public static final int ALREADY_REPORTED              = 208; /*HttpStatus.ALREADY_REPORTED*/

    public static final int IM_USED                       = 226; /*HttpStatus.IM_USED*/

    public static final int LOCKED                        = 423; /*HttpStatus.LOCKED*/

    public static final int BAD_REQUEST                   = 400; /*HttpStatus.BAD_REQUEST*/

    public static final int NOT_FOUND                     = 404; /* HttpStatus.NOT_FOUND*/

    public static final int EXPECTATION_FAILED            = 417; /* HttpStatus.EXPECTATION_FAILED*/

    public static final int CONNECTIVITY_FAILED           = 499;

    public static final int API_SERVICE_FAILED            = 498;

    int                     code;

    String                  message;

    public Status(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public Status(int code)
    {
        this.code = code;
    }

    public Status()
    {
        this.code = NO_CONTENT;
        this.message = "No Content";
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

}
