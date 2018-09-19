package com.stanstoynov;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/* This class models the actual game play flow.
 * It handles player input, input validation, letter matching, score and attempts left.
 */

// TODO: 19-Sep-18 tell them why we avoid using a single, named scanner object.

//@SuppressWarnings("all")
public class Game
{
    private final int ATTEMPTS_ALLOWED = 10; // Number of failed attempts allowed per word/phrase.
    private final int POINTS_AWARDED = 100; // Points awarded per solved word/phrase.

    private int playerScore;
    private int attemptsLeft;
    private ArrayList<WordPhrase> fullDictionary;
    private ArrayList<WordPhrase> currentCategoryDictionary;
    private ArrayList<String> categoryList;
    private String currentCategoryName;
    private WordPhrase currentWordPhrase;
    private ArrayList<Character> usedLettersList;
    private String usedLettersString;

    private Random rng;
    private boolean continuePlaying;

    // Constructor.
    public Game(ArrayList<WordPhrase> fullDictionary)
    {
        // Initialize stuff.
        playerScore = 0;
        attemptsLeft = ATTEMPTS_ALLOWED;
        this.fullDictionary = fullDictionary;
        currentCategoryDictionary = new ArrayList<>();
        categoryList = new ArrayList<>();
        currentCategoryName = "";
        usedLettersList = new ArrayList<>();
        usedLettersString = "";

        rng = new Random();
        continuePlaying = false;

        // Collect all categories from the dictionary and put them in a list.
        // This list will be displayed to the user
        // when he/she is asked to pick a category.
        // Completely solved categories will be removed from this list
        // (during game play).
        for(WordPhrase wordPhrase: fullDictionary)
        {
            if(categoryList.isEmpty())
            {
                categoryList.add(wordPhrase.getCategory().substring(1));
            }
            else if(!(categoryList.contains(wordPhrase.getCategory().substring(1))))
            {
                categoryList.add(wordPhrase.getCategory().substring(1));
            }
        }
    }

    // This method prints the first things the user sees upon game startup.
    // It calls the next method that controls the game flow: selectCategoryScreen()
    public void welcomeScreen()
    {
        // Welcome message.
        System.out.println("Welcome to Hangman!");
        System.out.println("Made by Stan Stoynov");
        System.out.println("September 2018");
        System.out.println("Note: type \"quit game\" at any time to exit the game.");

        // Call the next method.
        selectCategoryScreen();
    }

    // Manages the category selection.
    // If a category has been selected successfully, it calls the next method extractWordPhrase()
    private void selectCategoryScreen()
    {
        // First check if there are any unsolved categories left
        // (a solved category has all its words/phrases already solved).
        // If the categoryList is empty, this means that there a no more unsolved categories.
        // The player has beaten the game!
        if(categoryList.isEmpty())
        {
            System.out.println();
            System.out.println("*** CONGRATULATIONS! ***");
            System.out.println("You have solved all categories!");
            System.out.println("Final score: " + playerScore);
            System.exit(0);
        }

        // If we still have unsolved categories... keep playing.
        // Print all categories and ask the user to pick one.
        System.out.println();
        System.out.println("Please select a category (by typing its name):");
        for (String tempString : categoryList)
        {
            System.out.println(tempString);
        }
        System.out.println();

        // Validate user input.
        String userInput;
        selectCategoryLabel:
        while(true)
        {
            userInput = new Scanner(System.in).nextLine().trim();

            // Check for "quit game" input.
            // At any point, in which the user is asked to provide input,
            // he/she can choose to quit the game by typing "quit game".
            if(userInput.toLowerCase().equals("quit game"))
            {
                System.out.println("Thank you for playing!");
                System.out.println("Final score: " + playerScore);
                System.exit(0);
            }

            // Check if the input exists as a category. If it does, break the while loop.
            for(String category: categoryList)
            {
                if(category.toLowerCase().equals(userInput.toLowerCase()))
                {
                    currentCategoryName = "_" + category;
                    break selectCategoryLabel;
                }
            }

            // If we are here, then the selected category does not exist, print message.
            System.out.println("Invalid input! Please try again.");
        }

        // We are out of the while loop (and the game is still running).
        // This means that a category has been selected successfully.
        // Print a message about it.
        System.out.println("You have selected the \"" + currentCategoryName.substring(1) + "\" category.");

        // Call the next method.
        extractWordPhrase();
    }

    // Manages the selection of a random, unused word/phrase (from the specified category)
    // from the dictionary array list.
    // Once the selection is done, it calls the next method: gameScreen()
    private void extractWordPhrase()
    {
        // This method extracts a random unused word/phrase (of the selected category)
        // from the full dictionary array list.

        // Collect all words/phrases from the selected category
        // that have not been used yet in the current game.

        currentCategoryDictionary.clear(); // Clear for safety.
        for(WordPhrase wordPhrase: fullDictionary)
        {
            if(wordPhrase.getCategory().equals(currentCategoryName) && !wordPhrase.isAlreadyUsed())
            {
                currentCategoryDictionary.add(wordPhrase);
            }
        }

        // Check if any words have been collected at all.
        // If all words from the current category have already been used in a game,
        // this list would be empty.
        // Therefore the player has already solved them.
        // There is no point to show this category (on the category selection screen) anymore.
        // Remove said category from the category list and ask the user to select a new category.

        if(currentCategoryDictionary.isEmpty())
        {
            System.out.println("You have already solved all words/phrases from this category!");
            categoryList.remove(currentCategoryName.substring(1));
            selectCategoryScreen();
        }

        // Select a random word/phrase from the list of unused words/phrases and create a "copy" of it.
        // This copy will be the current word/phrase of the game.
        // We are going to be changing the values of said copy.
        // By using a copy we will not "break" the selected word/phrase within the dictionary itself.

        WordPhrase randomlySelectedWordPhrase = currentCategoryDictionary.get(rng.nextInt(currentCategoryDictionary.size()));

        // We use size() instead of size()-1 (as a parameter of the nextInt() method).
        // It excludes the specified index from the randomization
        // (we basically randomize from 0 to size()-1).

        // Create "copy".
        currentWordPhrase = new WordPhrase(randomlySelectedWordPhrase.getCategory(), randomlySelectedWordPhrase.getWordPhrase());

        // Mark the selected word/phrase as used within the dictionary.
        // That way it will not be selected again (in the current game session).
        randomlySelectedWordPhrase.setAlreadyUsed(true);

        // Call next method.
        gameScreen();
    }

    // This method is called within the boundaries of the gameScreen() method for input validation.
    private boolean isValidCharacterInput(String myInputString, boolean printErrorMessage)
    {
        String errorMessage = "Error: only single latin characters (A-Z, a-z) are allowed!";

        // Check if input is a single character.
        if(myInputString.length() > 1)
        {
            if(printErrorMessage)
            System.out.println(errorMessage);

            return false;
        }

        // Check if input is a latin letter (between 'A' and 'Z', a and 'z')
        char myChar = myInputString.charAt(0);
        if (((myChar >= 65) && (myChar <= 90)) || ((myChar >= 97) && (myChar <= 122)))
        {
            return true;
        }
        else
        {
            if(printErrorMessage)
            System.out.println(errorMessage);

            return false;
        }
    }

    // This method handles the actual game play.
    // It prints game info such as attempts left, current score, used letters etc.
    // It accepts single latin letters as input with validation.
    // If valid, the input is compared with the current game word/phrase.
    // Upon match, it handles the replacement of the placeholder (_) character with the real letter.
    // It handles win and failure conditions.
    private void gameScreen()
    {
        gameLabel:
        while(attemptsLeft > 0)
        {
            // This needs to be reset every turn.
            usedLettersString = "";

            // Print game info.
            System.out.println();
            System.out.println("==============================");
            System.out.println("Score: " + playerScore);
            System.out.println("Attempts left: " + attemptsLeft);

            // Place all used letters so far in the string.
            for(char usedLetter: usedLettersList)
            {
                usedLettersString += usedLetter + ", ";
            }

            // Get rid of the last comma and whitespace from the string.
            // I could use .trim() to get rid of the whitespace,
            // but I already need to remove the comma, so I do it for both at the same time.
            if(!usedLettersList.isEmpty())
            {
                usedLettersString = usedLettersString.substring(0, usedLettersString.length()-2);
            }

            // Print all used letters so far.
            System.out.println("Letters: " + usedLettersString);

            // Print the hidden game word/phrase on screen.
            // Initially this is just underscore and whitespace characters.
            // During the game, it gets replaced with real letters.
            currentWordPhrase.printTransformedWordPhraseHidden();

            // Ask the user to input a letter.
            System.out.println("Enter a letter: ");
            String userInput = new Scanner(System.in).nextLine().trim();

            // Check for "quit game" input.
            // At any point, in which the user is asked to provide input,
            // he/she can choose to quit the game by typing "quit game".
            if(userInput.equals("quit game"))
            {
                System.out.println("Thank you for playing!");
                System.out.println("Final score: " + playerScore);
                System.exit(0);
            }

            // Validate input.
            // The isValidCharacterInput() method also takes care of printing error messages
            // when the input is invalid.
            if(!isValidCharacterInput(userInput, true))
            {
                continue; // Go back and ask for a valid letter.
            }

            // Check if the letter has been used already.
            char userInputChar = userInput.charAt(0);
            if(usedLettersList.contains(userInputChar))
            {
                System.out.println("Error: you have already used this letter! Enter another.");
                continue; // Go back and ask for another (unused) letter.
            }

            // Add the letter to the used letters list.
            usedLettersList.add(userInputChar);

            // Check if the word/phrase contains the provided letter.
            // If it does contain it, place it on the correct positions in the hidden word/phrase.
            // Note that we modify and then print the hidden word/phrase.
            // That is ok, we do not "break" it within the dictionary,
            // because we have made a copy of it.
            boolean wordPhraseContainsLetter = false;
            ArrayList<Character> visibleFullLowerCaseList = currentWordPhrase.getTransformedWordPhraseVisibleFullLowerCase();
            ArrayList<Character> visibleList = currentWordPhrase.getTransformedWordPhraseVisible();
            ArrayList<Character> hiddenList = currentWordPhrase.getTransformedWordPhraseHidden();
            int listSize = visibleFullLowerCaseList.size();

            // See if the word/phrase contains the provided letter.
            // If it does, replace the _ (in the same position)
            // with the actual letter, taken from visibleList.
            for(int i=0; i<listSize; i++)
            {
                if(userInputChar == visibleFullLowerCaseList.get(i))
                {
                    hiddenList.set(i, visibleList.get(i));
                    wordPhraseContainsLetter = true;
                }
            }

            // Print message that informs the player if he has guessed correct or not.
            if(wordPhraseContainsLetter)
            {
                // Note: the player gets points only on FULL/COMPLETED words! Not on guessed letters.
                System.out.println("The word/phrase DOES contain this letter!");
            }
            else
            {
                System.out.println("The word/phrase DOES NOT contain this letter (-1 attempt)!");
                attemptsLeft--;
            }

            // Check if failure condition has been met,
            // aka check if the player has any attempts left.
            // If he does not, print a game over message, final score etc.
            // Prompt the user to press enter to exit the game.
            // TODO Might want to make a restart game function.
            if(attemptsLeft == 0)
            {
                System.out.println();
                System.out.println("*** GAME OVER! ***");
                System.out.println("You have no more attempts left!");
                System.out.println("Thank you for playing!");
                System.out.println("Final score: " + playerScore);
            }

            // Check if win condition has been met aka the hidden word/phrase no longer contains
            // underscore characters.
            // If true, add points to the playerScore and ask if the player wants to keep playing.
            // Print a congratulation message and current score.
            if(!currentWordPhrase.getTransformedWordPhraseHidden().contains('_'))
            {
                System.out.println();
                currentWordPhrase.printTransformedWordPhraseHidden();
                System.out.println("*** CONGRATULATIONS! ***");
                System.out.println("+ " + POINTS_AWARDED + " points");
                playerScore += POINTS_AWARDED;
                attemptsLeft = ATTEMPTS_ALLOWED;
                System.out.println("Current score: " + playerScore);
                System.out.println("Attempts reset!");

                usedLettersList.clear();

                String yesNo;
                while(true)
                {
                    System.out.println();
                    System.out.println("Do you want to keep playing? (Y/N) or (YES/NO)");
                    yesNo = new Scanner(System.in).nextLine().toLowerCase().trim();

                    if(yesNo.equals("y") || yesNo.equals("yes"))
                    {
                        continuePlaying = true;

                        // If the user selects to keep playing then
                        // break out of the major while loop.
                        // Once outside of it, a call to the selectCategoryScreen() method
                        // will continue the game.
                        break gameLabel;
                    }
                    else if(yesNo.equals("n") || yesNo.equals("no") || yesNo.equals("quit game"))
                    {
                        System.out.println("Thank you for playing!");
                        System.out.println("Final score: " + playerScore);
                        System.exit(0);
                    }
                    else
                    {
                        System.out.println("Invalid input!");
                    }
                }
            }
        } // This is the closing brace of the major while loop.

        // Keep playing the game, ask for a category... etc.
        // The score will continue to add up.
        if(continuePlaying)
        {
            continuePlaying = false; // Make sure to set it back to false.
            selectCategoryScreen();
        }
    }
}

