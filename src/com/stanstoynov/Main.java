package com.stanstoynov;

public class Main
{
    public static void main(String[] args)
    {
        Game hangmanGame = new Game();
        hangmanGame.startGame();
    }
}

// The project description says we can only enter latin letters,
// but at he same time the example phrase dictionary has U umlaut and dash symbols.
// I removed these symbols from the phrases from the dictionary.