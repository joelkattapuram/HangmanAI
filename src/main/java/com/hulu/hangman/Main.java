package com.hulu.hangman;

import com.hulu.hangman.Models.*;
import com.hulu.hangman.Controller.*;

public class Main {

    public static void main(String[] args) {
        
        double runs, wins;
        runs = wins = 0.0;

        int iterations = Integer.parseInt(args[1]);
        System.out.println(String.format("%s will run %d games", args[0], iterations));

        //Create new engine to handle api calls
        Engine gameEngine = new Engine();
        Controller gameController = new Controller();

        //Run the game $iterations times
        while(iterations-- != 0) {
            Status status = gameEngine.createNewGame();
            gameController.initializeController(status);

            do {
                // System.out.println(status);

                String prevStatus = status.getRemaining_guesses();
                int remaining = gameController.percentageRemaining(status);

                // Make a guess
                Character guess;
                if (remaining <= 60) {
                    guess = gameController.nextGuess(status.getState());
                } else {
                    guess = gameController.nextGuess();
                }

                status = gameEngine.makeGuess(status.getToken(), guess);

                // Update the game after every turn
                gameController.updateStatus(status);

                if (status.getRemaining_guesses() != prevStatus) {
                    gameController.update(guess, false);
                } else {
                    gameController.update(guess, true);
                }

            } while (status.isPlaying());
            runs++;
            boolean won = Status.STATUS.FREE.toString().equals(status.getStatus());
            System.out.println(won ? "Win!" : "Lose :/ " );
            wins += won ? 1 : 0;
            System.out.println("Win Rate: " + wins/runs);
            System.out.println("___________________\n");
        }

    }
}
