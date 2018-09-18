package com.stanstoynov;

public class Main
{
    public static void main(String[] args)
    {
        Game hangmanGame = new Game(new DictionaryLoader().getFullDictionary());
        hangmanGame.welcomeScreen();
    }
}

// TODO: 16-Sep-18 create very good comments everywhere

// The project description says we can only enter latin letters,
// but at he same time the example word/phrase dictionary has U umlaut and dash symbols.
// I removed these symbols from the word/phrases from the dictionary.