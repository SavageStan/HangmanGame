package com.stanstoynov;

public class CategoryNotFoundException extends Exception
{
    public CategoryNotFoundException(String errorMessage)
    {
        super(errorMessage);
    }
}
