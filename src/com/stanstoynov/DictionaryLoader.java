package com.stanstoynov;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

//@SuppressWarnings("all")
public class DictionaryLoader
{
    private ArrayList<WordPhrase> fullDictionary = new ArrayList<>();

    public DictionaryLoader()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Loading game dictionary file (dict/dictionary.txt) ... \n");

        try
        {
            File file = new File("dict/dictionary.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String tempWordPhrase;
            String tempCategory = "";

            while((tempWordPhrase = bufferedReader.readLine()) != null)
            {
                tempWordPhrase = tempWordPhrase.trim();

                if(tempWordPhrase.charAt(0) == '_')
                {
                    tempCategory = tempWordPhrase;
                    continue;
                }

                if(tempCategory.equals(""))
                {
                    System.out.println("The game dictionary file must always start with a category!");
                    System.out.println("Press Enter to exit the game...");
                    scanner.nextLine();
                    System.exit(0);
                }
                else
                {
                    fullDictionary.add(new WordPhrase(tempCategory, tempWordPhrase));
                }
            }

            bufferedReader.close();

            // Check if the newly created dictionary has at least one 1 record.
            if(fullDictionary.size() == 0)
            {
                System.out.println("The game dictionary file must have at least one category, \n" +
                        "followed by at least one word/phrase!");
                System.out.println("Press Enter to exit the game...");
                scanner.nextLine();
                System.exit(0);
            }
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            System.out.println("The game dictionary file (dict/dictionary.txt) was not found\n" +
                    "(or contains empty lines)!");
            System.out.println("Press Enter to exit the game...");
            scanner.nextLine();
            System.exit(0);
        }
    }

    public ArrayList<WordPhrase> getFullDictionary()
    {
        return fullDictionary;
    }

    public void printFullDictionary() // Used for testing purposes.
    {
        for(WordPhrase wordPhrase : fullDictionary)
        {
            System.out.println(wordPhrase.getCategory());
            wordPhrase.printTransformedWordPhraseVisible();
            wordPhrase.printTransformedWordPhraseVisibleFullLowerCase();
            wordPhrase.printTransformedWordPhraseHidden();
            System.out.println();
        }
    }
}

