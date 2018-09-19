package com.stanstoynov;

public class Main
{
    public static void main(String[] args)
    {
        // We create a Game object and pass it a loaded dictionary.
        Game hangmanGame = new Game(new DictionaryLoader().getFullDictionary());
        // We initiate the welcome screen.
        hangmanGame.welcomeScreen();
    }
}

// The project description says we can only enter latin letters,
// but at he same time the example word/phrase dictionary has U umlaut and dash symbols.
// I removed these symbols from the word/phrases from the dictionary.