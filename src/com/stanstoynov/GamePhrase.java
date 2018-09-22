package com.stanstoynov;
import java.util.ArrayList;

/* This class models a phrase object.
 * Each object contains 2 separate array lists,
 * each containing a transformed version of the phrase.
 *
 * 1) The first array list contains the phrase as it is within the dictionary.txt file,
 * but with one exception - it has whitespace characters between each character
 * (including original whitespaces, making them 2).
 * Contents from this array list are used to replace their corresponding element
 * from the second array list when a match has been found (read more about it below).
 *
 * 2) The second array list initially contains ONLY underscore and whitespace characters
 * (all placed in their appropriate locations). It basically models a hidden phrase.
 * As the game progresses (and the user tries to guess letters from the phrase),
 * upon finding a match, the corresponding element from the first array list
 * is used to replace the element within this array list (with the same index).
 * Turn by turn, if the player is successful, this array will be transformed
 * into a clone of the first array list. That is the win condition of the game.
 *
 * Both array lists have the same number of elements.
 * This forces each element to have a corresponding element from the other array list.
 *
 * Another significant feature is that objects of this class can be marked as "already used".
 * This prevents them from being used more than once in the same game session.
 */

public class GamePhrase
{
    private String category;
    private ArrayList<Character> transformedPhraseVisible = new ArrayList<>();
    private ArrayList<Character> transformedPhraseHidden = new ArrayList<>();
    private boolean isAlreadyUsed;

    public GamePhrase(String category, String phrase)
    {
        this.category = category;
        isAlreadyUsed = false;

        for (char tempChar : phrase.toCharArray())
        {
            if (tempChar == ' ')
            {
                transformedPhraseVisible.add(' ');
                transformedPhraseHidden.add(' ');
            }
            else
            {
                transformedPhraseVisible.add(tempChar);
                transformedPhraseVisible.add(' ');

                transformedPhraseHidden.add('_');
                transformedPhraseHidden.add(' ');
            }
        }
    }

    public void printTransformedPhraseHidden()
    {
        String tempString = "";

        for (char tempChar : transformedPhraseHidden)
        {
            tempString += tempChar;
        }

        System.out.println(tempString);
    }

    public String getCategory()
    {
        return category;
    }

    public ArrayList<Character> getTransformedPhraseVisible()
    {
        return transformedPhraseVisible;
    }

    public ArrayList<Character> getTransformedPhraseHidden()
    {
        return transformedPhraseHidden;
    }

    public boolean getIsAlreadyUsed()
    {
        return isAlreadyUsed;
    }

    public void setIsAlreadyUsed(boolean alreadyUsed)
    {
        isAlreadyUsed = alreadyUsed;
    }

}
