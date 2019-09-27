package com.hulu.hangman;

import java.io.IOException;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.hulu.hangman.Models.Status;

/**
 * Handles all the queries to Hulu's test API
 */
public class Engine {

    private static final String NEW_GAME_URL =
            "http://gallows.hulu.com/play?code=joelkattapuram@berkeley.edu";
    private static String CURRENT_GAME_URL =
            "http://gallows.hulu.com/play?code=joelkattapuram@berkeley.edu&token=%s&guess=%s";

    private Gson gsonEngine;
    private CloseableHttpClient httpclient;

    public Engine() {
        gsonEngine = new Gson();
        httpclient = HttpClients.createDefault();
    }

    /**
     * Initialize a new game
     *
     * @return Status object from game initialization
     */
    public Status createNewGame() {
        return gsonEngine.fromJson(generateHttpRequest(NEW_GAME_URL, 5), Status.class);
    }

    /**
     * Perform a guess by making an API call
     *
     * @param token string token for this game instance
     * @param guess character to guess
     * @return Status object from result of guess
     */
    public Status makeGuess(String token, Character guess) {
        if (guess == null) return new Status("");
        String guessRequest = String.format(CURRENT_GAME_URL, token, guess);
        return gsonEngine.fromJson(generateHttpRequest(guessRequest, 5), Status.class);
    }

    /**
     * call hulu api for guess
     *
     * @param getRequest request string
     * @param retryCount number of retries upon bad gateway failure
     * @return String response
     */
    private String generateHttpRequest(String getRequest, int retryCount) {
        CloseableHttpResponse response1 = null;
        String response = "";
        try {
            response1 = httpclient.execute(new HttpGet(getRequest));
            int statusCode = response1.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                if (retryCount == 0) {
                    System.out.println("Maximum Retry Count Exceeded");
                    throw new HttpResponseException(statusCode, response);
                }   
                System.out.println("Failed Connection; Retrying...");
                response1.close();
                return generateHttpRequest(getRequest, retryCount - 1);
            } else {
                HttpEntity entity1 = response1.getEntity();

                try (Scanner s = new Scanner(entity1.getContent())) {
                    while (s.hasNextLine()) response += s.nextLine();
                }
                EntityUtils.consume(entity1);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (response1 != null) response1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return response;
    }

}