package com.stanstoynov;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/* This class models the actual game play flow.
 * It handles player input, input validation, letter matching, score and attempts left.
 */

public class Game
{
    private final int ATTEMPTS_ALLOWED = 10; // Number of failed attempts allowed per phrase.
    private final int POINTS_AWARDED = 100; // Points awarded per solved phrase.

    private ArrayList<GamePhrase> gameDictionary;
    private int playerScore;
    private int attemptsLeft;
    private ArrayList<String> categoryList;
    private String currentCategoryName;
    private ArrayList<GamePhrase> currentCategoryDictionary;
    private GamePhrase currentPhrase;
    private ArrayList<Character> usedLettersList;
    private String usedLettersString;
    private Random rng;
    private boolean continuePlaying;
    private HelperClass helperObject;
    private boolean isFirstRun;

    public Game()
    {
        try
        {
            this.gameDictionary = GameDictionary.loadDictionary();
        }
        catch (CategoryNotFoundException | PhraseNotFoundException e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        catch (IOException e)
        {
            System.out.println("The game dictionary file was not found!");
            System.exit(1);

        }
        catch (StringIndexOutOfBoundsException e)
        {
            System.out.println("The game dictionary file contains empty lines!");
            System.exit(1);

        }

        playerScore = 0;
        attemptsLeft = ATTEMPTS_ALLOWED;
        categoryList = new ArrayList<>();
        currentCategoryName = "";
        currentCategoryDictionary = new ArrayList<>();
        usedLettersList = new ArrayList<>();
        usedLettersString = ""; // TODO: 23-Sep-18 maybe use string builder?
        rng = new Random();
        continuePlaying = false;
        helperObject = new HelperClass();
        isFirstRun = true;
    }

    public void startGame()
    {
        if(isFirstRun)
        {
            displayWelcomeScreen();
            populateCategoryList();
        }

        selectCategoryScreen();
        extractPhrase();
        displayGameScreen();
    }

    public void displayWelcomeScreen()
    {
        System.out.println("Welcome to Hangman!");
        System.out.println("Made by Stan Stoynov");
        System.out.println("September 2018");
        System.out.println("Note: type \"quit game\" at any time to exit the game.");
        isFirstRun = false;
    }

    public void populateCategoryList()
    {
        // Collect all categories from the dictionary and put them in a list.
        // This list will be displayed to the user
        // when he/she is asked to pick a category.
        // Completely solved categories will be removed from this list
        // (during game play).

        for(GamePhrase phrase : gameDictionary)
        {
            if(categoryList.isEmpty())
            {
                categoryList.add(phrase.getCategory().substring(1));
            }
            else if(!(categoryList.contains(phrase.getCategory().substring(1))))
            {
                categoryList.add(phrase.getCategory().substring(1));
            }
        }
    }

    private void selectCategoryScreen()
    {
        // This method manages the category selection.

        // First, check if there are any unsolved categories left
        // (a solved category has all its phrases already solved).
        // If the categoryList is empty, this means that there a no more unsolved categories.
        // The player has beaten the game! Print message and exit game.

        helperObject.checkForWinCondition(categoryList, playerScore);

        // If we still have unsolved categories, then keep playing.
        // Print all categories and ask the user to pick one.

        System.out.println();
        System.out.println("Please select a category:");

        for (String tempString : categoryList)
        {
            System.out.println(tempString);
        }

        System.out.println();

        // Next, collect and validate user input.
        String userInput;

        selectCategoryLabel:
        while(true)
        {
            userInput = helperObject.readNextLine();

            // Check for "quit game" input.
            // At any point, in which the user is asked to provide input,
            // he/she can choose to quit the game by typing "quit game".
            // This makes sure the infinite loop can be stopped at any point.

            helperObject.checkForQuitGameInput(userInput, playerScore);

            // Check if the input exists as a category.
            // If it does, break out of the while loop.

            for(String category: categoryList)
            {
                if(category.toLowerCase().equals(userInput))
                {
                    currentCategoryName = "_" + category;
                    break selectCategoryLabel;
                }
            }

            // If we are here, then the selected category does not exist,
            // print message and loop again.

            System.out.println("Invalid input! Please try again.");
        }

        // We are out of the while loop (and the game is still running).
        // This means that a category has been selected successfully.
        // Print a message about it.

        System.out.println("You have selected the \"" + currentCategoryName.substring(1) + "\" category.");
    }

    private void extractPhrase()
    {
        // This method manages the selection of a random, unused phrase
        // (of the user selected category) from the dictionary array list.

        // First, collect all phrases from the selected category
        // that have not been used yet (in the current session).

        currentCategoryDictionary.clear();
        for(GamePhrase phrase: gameDictionary)
        {
            if(phrase.getCategory().equals(currentCategoryName) && !phrase.getIsAlreadyUsed())
            {
                currentCategoryDictionary.add(phrase);
            }
        }

        // Check if any phrases have been collected at all.
        // If all phrases from the current category have already been used in a game,
        // this list would be empty.
        // Therefore the player has already solved them.
        // There is no point to show this category (on the category selection screen) anymore.
        // Remove said category from the category list and ask the user to select a new category.

        if(currentCategoryDictionary.isEmpty())
        {
            System.out.println("You have already solved all phrases from this category!");
            categoryList.remove(currentCategoryName.substring(1));
            selectCategoryScreen();
        }

        // Select a random phrase from the list of unused phrases of the selected category.
        // We use size() instead of size()-1 (as a parameter of the nextInt() method).
        // It excludes the specified index from the randomization
        // (we basically randomize from 0 to size()-1).

        currentPhrase = currentCategoryDictionary.get(rng.nextInt(currentCategoryDictionary.size()));

        // Mark the selected phrase as used within the dictionary.
        // That way it will not be selected again (in the current game session).

        currentPhrase.setIsAlreadyUsed(true);
    }

    private void displayGameScreen() // TODO: 23-Sep-18 refactor this method!
    {
        // This method handles the actual game play.
        // It prints game info such as attempts left, current score, used letters etc.
        // It accepts single latin letters as input (with validation).
        // If valid, the input is compared with the letters from the current game phrase.
        // Upon match, it handles the replacement of the placeholder (_) character
        // with the real letter.

        gameScreenLabel:
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
            if(!usedLettersList.isEmpty())
            {
                usedLettersString = usedLettersString.substring(0, usedLettersString.length()-2);
            }

            // Print all used letters so far.
            if(usedLettersString.length() !=0)
            System.out.println("Letters: " + usedLettersString);

            // Print the hidden game phrase on screen.
            // Initially this is just underscore and whitespace characters.
            // During the game, it gets replaced with real letters.
            currentPhrase.printTransformedPhraseHidden();

            // Ask the user to input a letter.
            System.out.println("Enter a letter: ");
            String userInput = helperObject.readNextLine();

            // Check for "quit game" input.
            helperObject.checkForQuitGameInput(userInput, playerScore);

            // Validate input.
            // The validateCharacterInput() method also takes care of
            // printing an error messages when the input is invalid.
            if(!helperObject.validateCharacterInput(userInput, true))
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

            // Check if the phrase contains the provided letter.
            // If it does, replace the underscore character with in
            // (in correct position(s)) in the hidden phrase array list.

            boolean phraseContainsLetter = false;
            ArrayList<Character> visibleList = currentPhrase.getTransformedPhraseVisible();
            ArrayList<Character> hiddenList = currentPhrase.getTransformedPhraseHidden();
            int listSize = visibleList.size();

            // See if the phrase contains the provided letter.
            // If it does, replace the _ with the actual letter
            // (in the same position), taken from visibleList.
            for(int i=0; i<listSize; i++)
            {
                if(userInputChar == visibleList.get(i).toString().toLowerCase().charAt(0))
                {
                    hiddenList.set(i, visibleList.get(i));
                    phraseContainsLetter = true;
                }
            }

            // Print message that informs the player if he has guessed correct or not.
            if(phraseContainsLetter)
            {
                System.out.println("The phrase DOES contain this letter!");
            }
            else
            {
                System.out.println("The phrase DOES NOT contain this letter (-1 attempt)!");
                attemptsLeft--;
            }

            // Check if failure condition has been met,
            // aka check if the player has any attempts left.

            helperObject.checkForFailureCondition(attemptsLeft, playerScore);

            // Check if the current phrase has been solved
            // (it contains no more underscore characters).
            // If true, add points to the playerScore
            // and ask if the player wants to keep playing.

            if(checkIfCurrentPhraseIsSolved())
            {
                String yesNo;
                while (true)
                {
                    System.out.println();
                    System.out.println("Do you want to keep playing? (Y/N) or (YES/NO)");
                    yesNo = helperObject.readNextLine();

                    if (yesNo.equals("y") || yesNo.equals("yes"))
                    {
                        continuePlaying = true;

                        // If the user selects to keep playing then break out of the major while loop.
                        // Once outside of it, a call to the startGame() method will continue the game.
                        break gameScreenLabel;
                    }
                    else if (yesNo.equals("n") || yesNo.equals("no") || yesNo.equals("quit game"))
                    {
                        System.out.println("Thank you for playing!");
                        System.out.println("Final score: " + playerScore);
                        System.exit(0);
                    } else
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
            startGame();
        }
    }

    private boolean checkIfCurrentPhraseIsSolved()
    {
        // Checks if the current phrase contains any underscore characters.
        // I could place this in the helper class, but way too many things need to be passed
        // to it in order to do the same job. It makes it not worthwhile.

        if(!currentPhrase.getTransformedPhraseHidden().contains('_'))
        {
            System.out.println();
            currentPhrase.printTransformedPhraseHidden();
            System.out.println("*** CONGRATULATIONS! ***");
            System.out.println("+ " + POINTS_AWARDED + " points");
            playerScore += POINTS_AWARDED;
            attemptsLeft = ATTEMPTS_ALLOWED;
            System.out.println("Current score: " + playerScore);
            System.out.println("Attempts reset!");

            usedLettersList.clear();
            usedLettersString = "";
            return true;
        }
        return false;
    }

}

// TODO: 22-Sep-18 methods must have a verb in their name DONE
// TODO: 23-Sep-18 booleans must contain a question
// TODO: 22-Sep-18 user input only in game class DONE
// TODO: 22-Sep-18 remove unnecessary logic from constructors DONE
// TODO: 22-Sep-18 break method "chain" DONE
// TODO: 22-Sep-18 do stuff right before you actually need it MAYBE DONE




