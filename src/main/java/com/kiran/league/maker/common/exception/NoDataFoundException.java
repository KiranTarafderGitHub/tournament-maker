package com.kiran.league.maker.common.exception;

public class NoDataFoundException extends RuntimeException
{

    private static final long serialVersionUID = -8405723008805644356L;

    public NoDataFoundException(String msg, Throwable t)
    {
        super(msg, t);
    }

    public NoDataFoundException(String msg)
    {
        super(msg);
    }

}
