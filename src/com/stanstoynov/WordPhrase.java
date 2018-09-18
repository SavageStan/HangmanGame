package com.stanstoynov;

import java.util.ArrayList;

//@SuppressWarnings("all")
public class WordPhrase
{
    private String category;
    private String wordPhrase;
    private ArrayList<Character> transformedWordPhraseVisible = new ArrayList<>();
    private ArrayList<Character> transformedWordPhraseVisibleFullLowerCase = new ArrayList<>();
    private ArrayList<Character> transformedWordPhraseHidden = new ArrayList<>();
    private boolean isAlreadyUsed;

    public WordPhrase(String category, String wordPhrase)
    {
        this.category = category;
        this.wordPhrase = wordPhrase;
        isAlreadyUsed = false;

        String tempString = "";

        for (char tempChar : wordPhrase.toCharArray())
        {
            if (tempChar == ' ')
            {
                transformedWordPhraseVisible.add(' ');
                transformedWordPhraseVisibleFullLowerCase.add(' ');
                transformedWordPhraseHidden.add(' ');
            }
            else
            {
                transformedWordPhraseVisible.add(tempChar);
                transformedWordPhraseVisible.add(' ');

                tempString += tempChar;
                tempChar = tempString.toLowerCase().charAt(0);
                transformedWordPhraseVisibleFullLowerCase.add(tempChar);
                transformedWordPhraseVisibleFullLowerCase.add(' ');
                tempString = "";

                transformedWordPhraseHidden.add('_');
                transformedWordPhraseHidden.add(' ');
            }
        }
    }

    public void printTransformedWordPhraseVisible()
    {
        String tempString = "";

        for (char tempChar : transformedWordPhraseVisible)
        {
            tempString += tempChar;
        }

        System.out.println(tempString);
    }

    public void printTransformedWordPhraseVisibleFullLowerCase()
    {
        String tempString = "";

        for (char tempChar : transformedWordPhraseVisibleFullLowerCase)
        {
            tempString += tempChar;
        }

        System.out.println(tempString);
    }

    public void printTransformedWordPhraseHidden()
    {
        String tempString = "";

        for (char tempChar : transformedWordPhraseHidden)
        {
            tempString += tempChar;
        }

        System.out.println(tempString);
    }

    public String getCategory()
    {
        return category;
    }

    public String getWordPhrase()
    {
        return wordPhrase;
    }

    public ArrayList<Character> getTransformedWordPhraseVisible()
    {
        return transformedWordPhraseVisible;
    }

    public ArrayList<Character> getTransformedWordPhraseVisibleFullLowerCase()
    {
        return transformedWordPhraseVisibleFullLowerCase;
    }

    public ArrayList<Character> getTransformedWordPhraseHidden()
    {
        return transformedWordPhraseHidden;
    }

    public boolean isAlreadyUsed()
    {
        return isAlreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed)
    {
        isAlreadyUsed = alreadyUsed;
    }

}
