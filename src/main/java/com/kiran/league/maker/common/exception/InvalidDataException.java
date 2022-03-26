package com.kiran.league.maker.common.exception;

public class InvalidDataException extends RuntimeException
{

    private static final long serialVersionUID = -8405723008805644356L;

    public InvalidDataException(String msg, Throwable t)
    {
        super(msg, t);
    }

    public InvalidDataException(String msg)
    {
        super(msg);
    }

}
