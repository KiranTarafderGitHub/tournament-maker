package com.kiran.league.maker.common.exception;

public class DuplicateDataException extends RuntimeException
{

    private static final long serialVersionUID = -8405723008805644356L;

    public DuplicateDataException(String msg, Throwable t)
    {
        super(msg, t);
    }

    public DuplicateDataException(String msg)
    {
        super(msg);
    }

}
