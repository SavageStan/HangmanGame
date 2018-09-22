package com.stanstoynov;

public class PhraseNotFoundException extends Exception
{
    public PhraseNotFoundException(String errorMessage)
    {
        super(errorMessage);
    }
}
