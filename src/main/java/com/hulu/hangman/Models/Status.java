package com.hulu.hangman.Models;

/**
 * Entity representing data from Hulu's API
 */
public class Status {

    public enum STATUS {
        ALIVE, FREE, DEAD
    }

    private String token, status, state, remaining_guesses;

    //Constructor used during game initialization
    public Status(String status) {
        this.status = status;
        this.state = "";
    }

    //Constructor used during game play
    public Status(String token, String status, String state, String remaining_guesses) {
        super();
        this.token = token;
        this.status = status;
        this.state = state;
        this.remaining_guesses = remaining_guesses;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemaining_guesses() {
        return remaining_guesses;
    }

    public void setRemaining_guesses(String remaining_guesses) {
        this.remaining_guesses = remaining_guesses;
    }

    public boolean isPlaying() {
        return STATUS.ALIVE.toString().equals(status);
    }

    @Override
    public String toString() {
        return "Status: {status=" + status + ", token=" + token + ", remaining_guesses=" + remaining_guesses
                + ", state=" + state + "}";
    }

}