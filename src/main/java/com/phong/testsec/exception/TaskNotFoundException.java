package com.phong.testsec.exception;

public class TaskNotFoundException extends RuntimeException
{
    public TaskNotFoundException(String message)
    {
        super(message);
    }
}