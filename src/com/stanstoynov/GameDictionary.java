package com.stanstoynov;
import java.io.*;
import java.util.ArrayList;

/* This class reads the phrases (with their categories) from the dict/dictionary.txt file.
 * and puts them inside an array list. It throws different types of exceptions when
 * the dictionary.txt file content is invalid.
 */

public class GameDictionary
{
    private static ArrayList<GamePhrase> phraseDictionary = new ArrayList<>();

    public static ArrayList<GamePhrase> loadDictionary() throws CategoryNotFoundException,
            PhraseNotFoundException, StringIndexOutOfBoundsException, IOException
    {

        File file = new File("dict/dictionary.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String tempPhrase;
        String tempCategory = "";

        // Actual reading from file happens here.
        // If the phrase starts with underscore, then it is taken as a category instead.
        // The next phrases we encounter will have it (the category) as their category
        // until a new category is found.

        while((tempPhrase = bufferedReader.readLine()) != null)
        {
            tempPhrase = tempPhrase.trim();

            if(tempPhrase.charAt(0) == '_')
            {
                tempCategory = tempPhrase;
                continue;
            }

            // Protection against phrases without category (no category at the start of the file).
            if(tempCategory.equals(""))
            {
                throw new CategoryNotFoundException("The game dictionary file must always start with a category!");
            }
            else
            {
                phraseDictionary.add(new GamePhrase(tempCategory, tempPhrase));
            }
        }

        bufferedReader.close();

        // Protection against empty dictionary.
        if(phraseDictionary.isEmpty())
        {
            throw new PhraseNotFoundException("The game dictionary file must have at least one valid phrase!");
        }

        return phraseDictionary;
    }
}
