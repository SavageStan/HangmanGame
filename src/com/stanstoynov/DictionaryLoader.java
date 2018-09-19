package com.stanstoynov;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/* This class reads the words/phrases (with their categories) from the dict/dictionary.txt file.
 * It has different types of protections against invalid content within the dictionary.txt file.
 */

//@SuppressWarnings("all")
public class DictionaryLoader
{
    // The actual dictionary array list.
    private ArrayList<WordPhrase> fullDictionary = new ArrayList<>();

    // Constructor.
    public DictionaryLoader()
    {
        Scanner scanner = new Scanner(System.in); // This scanner is used only for registering enter keystrokes.
        System.out.println("Loading game dictionary file (dict/dictionary.txt) ... \n");

        try
        {
            File file = new File("dict/dictionary.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String tempWordPhrase;
            String tempCategory = "";

            // Actual reading happens here.
            // If the word starts with underscore, then it is taken as a category instead.
            // The next encountered words will have it (the category) as their category
            // until a new category is found.
            while((tempWordPhrase = bufferedReader.readLine()) != null)
            {
                tempWordPhrase = tempWordPhrase.trim();

                if(tempWordPhrase.charAt(0) == '_')
                {
                    tempCategory = tempWordPhrase;
                    continue;
                }

                // Protection against non-existent categories.
                if(tempCategory.equals(""))
                {
                    System.out.println("The game dictionary file must always start with a category!");
                    System.out.println("Press Enter to exit the game...");
                    scanner.nextLine();
                    System.exit(0);
                }
                else
                {
                    // If everything is right, add the word (with its category) to the dictionary array list.
                    fullDictionary.add(new WordPhrase(tempCategory, tempWordPhrase));
                }
            }

            bufferedReader.close();

            // Check if the newly created dictionary has at least one record.
            // Basically a protection against invalid dictionary.txt content
            // (words without categories etc.).
            if(fullDictionary.size() == 0)
            {
                System.out.println("The game dictionary file must have at least one category, \n" +
                        "followed by at least one word/phrase!");
                System.out.println("Press Enter to exit the game...");
                scanner.nextLine();
                System.exit(0);
            }
        }
        // Catches empty lines within the dictionary.txt
        catch (StringIndexOutOfBoundsException e0)
        {
            System.out.println("The game dictionary file (dict/dictionary.txt) contains empty lines!\n" +
                    "Remove them and restart the game.");
            System.out.println("Press Enter to exit the game...");
            scanner.nextLine();
            System.exit(0);
        }
        // Catches the exception thrown when the file was not found.
        catch (FileNotFoundException e1)
        {
            System.out.println("The game dictionary file (dict/dictionary.txt) was not found!");
            System.out.println("Press Enter to exit the game...");
            scanner.nextLine();
            System.exit(0);
        }
        // Catches general exceptions (any other possible exceptions).
        catch (Exception e2)
        {
            //e2.printStackTrace();
            System.out.println("Something went wrong!");
            System.out.println("Press Enter to exit the game...");
            scanner.nextLine();
            System.exit(0);
        }
    }

    // Simple getter.
    public ArrayList<WordPhrase> getFullDictionary()
    {
        return fullDictionary;
    }

    // Used for testing purposes.
    public void printFullDictionary()
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

