package com.stanstoynov;
import java.util.ArrayList;
import java.util.Scanner;

public class HelperClass
{
    /* This class contains methods that are used for various checks.
     * Win condition, failure condition, input validation and other various busywork.
     */

    public void checkForWinCondition(ArrayList<String>categoryList, int playerScore)
    {
        if(categoryList.isEmpty())
        {
            System.out.println();
            System.out.println("*** CONGRATULATIONS! ***");
            System.out.println("You have solved all categories!");
            System.out.println("Final score: " + playerScore);
            System.exit(0);
        }
    }

    public void checkForFailureCondition(int attemptsLeft, int playerScore)
    {
        if(attemptsLeft == 0)
        {
            System.out.println();
            System.out.println("*** GAME OVER! ***");
            System.out.println("You have no more attempts left!");
            System.out.println("Thank you for playing!");
            System.out.println("Final score: " + playerScore);
            System.exit(0);
        }
    }

    public void checkForQuitGameInput(String userInput, int playerScore)
    {
        if(userInput.toLowerCase().equals("quit game"))
        {
            System.out.println("Thank you for playing!");
            System.out.println("Final score: " + playerScore);
            System.exit(0);
        }
    }

    public String readNextLine()
    {
        return new Scanner(System.in).nextLine().toLowerCase().trim();
    }

    public boolean validateCharacterInput(String myInputString, boolean printErrorMessage)
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

}
