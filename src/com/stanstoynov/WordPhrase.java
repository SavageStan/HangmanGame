package com.stanstoynov;
import java.util.ArrayList;

/* This class models a word/phrase object.
 * Each object contains 3 separate array lists,
 * each containing a transformed version of the word/phrase.
 *
 * 1) The first array list contains the word/phrase AS IT IS within the dictionary.txt file,
 * but with whitespace between each character (including original whitespaces, making them 2).
 * Contents from this array list are used to replace their corresponding element
 * from the third array list when a match has been found (read more about it below).
 *
 * 2) The second array list has the same content as the first array list,
 * EXCEPT that it is all in lower case.
 * This is used when reading input from the user.
 * Using it instead of the first array list protects against being unable to read input with varying
 * capitalization.
 * Basically all user input is first converted to lower case and then compared to objects within
 * this array list.
 *
 * 3) The third array list initially contains ONLY underscore and whitespace characters
 * (all placed in their appropriate locations). It basically models a hidden word/phrase.
 * As the game progresses (and the user tries to guess letters from the word/phrase),
 * upon finding a match, the corresponding element from the first array list
 * (with actual capitalization) is used to replace the element within this array (with the same index).
 * Turn by turn, if the player is successful, this array will be transformed
 * into a clone of the first array list. That is the win condition of the game.
 *
 * All three array lists have the same number of elements.
 * This forces each element (from one of the array list) to have 2 other corresponding elements
 * from the other 2 array lists.
 *
 * Another significant feature that objects of class WordPhrase have is that they can be
 * marked as "Already used". This prevents them from being used more than once in the same game session.
 */

//@SuppressWarnings("all")
public class WordPhrase
{
    private String category;
    private String wordPhrase;
    private ArrayList<Character> transformedWordPhraseVisible = new ArrayList<>();
    private ArrayList<Character> transformedWordPhraseVisibleFullLowerCase = new ArrayList<>();
    private ArrayList<Character> transformedWordPhraseHidden = new ArrayList<>();
    private boolean isAlreadyUsed;

    // Constructor. All three array lists are populated here with transformed contents.
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

    // The following are 3 print methods.
    // Each one prints its corresponding array list.
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

    // The rest are just getter and setter the methods.
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
