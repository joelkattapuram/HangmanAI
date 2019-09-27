package com.hulu.hangman.Controller;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hulu.hangman.Models.*;

import static java.util.Arrays.asList;

import java.io.*;

public class Controller {

    private static final String REGEX = "[\\s-:\"]";

    private final int mapSize;

    private Status game;
    private List<String> words;
    private Set<Character> guessedLetters;
    private Map<Integer, List<Character>> frequencyMap;

    private Map<Integer, List<String>> wordList = new HashMap<Integer, List<String>>();
	private Set<Character> correct_letters; 
	private Set<Character> incorrect_letters;

	/**
     * Intializes a frequency map for characters and a hash map based on words
     *  from several dictionary files
     */
    public Controller() {
        this.frequencyMap = FrequencyMap.getFreqMap();
        this.mapSize = frequencyMap.size();

        try {
			for (File f : new File("word_list/").listFiles()) {
				BufferedReader reader = new BufferedReader(new FileReader(f));
				for (String str; (str = reader.readLine()) != null;) {
                    str = str.trim();
					if (!wordList.containsKey(str.length())) {
                        wordList.put(str.length(), new ArrayList<String>());
					}
                    wordList.get(str.length()).add(str);
                }
                
			}
		} catch (IOException e) {
			System.err.println(e);
		}
    }

    // Resets parameters for a new game
    public void initializeController(Status game) {
        this.game = game;
        this.guessedLetters = new HashSet<>();
        this.words = sortWordList(game.getState());
        this.correct_letters = new HashSet<>();
        this.incorrect_letters = new HashSet<>();
    }

    //updates game status
    public void updateStatus(Status currentStatus) {
        if (currentStatus == null) {
            System.out.println("null current status");
        } else {
            this.game = currentStatus;
            this.words = sortWordList(currentStatus.getState());
        }
    }

    /**
     * Sort words by priority
     *
     * @param rawState string returned by the API
     * @return sorted list of words to be utilized
     */
    private List<String> sortWordList(String rawState) {
        List<String> wordList = Arrays.asList(rawState.split(REGEX));
        wordList.sort(Comparator.comparingInt(String::length));

        return wordList;
    }

    /**
     * @param words status words
     * @return next word to test
     */
    private int getWordToGuessFor(List<String> words) {
        int i = 0;
        for (String word : words) {
            if (word.contains("_")) break;
            i++;
        }
        return i;
    }

    /**
     * Get the next character to test for based on naive frequency map
     *  (Used initially for first several guesses for greater efficiency)
     * 
     * @return the 'guessed' Character to be tested
     */
    public Character nextGuess() {
        String wordToGuess = words.get(getWordToGuessFor(words));

        //use word length to determine where in map to look
        int wordLength = (wordToGuess.length() < mapSize) ? wordToGuess.length() : mapSize - 1;

        //pick most likely character
        Character guessChar = getCharToGuess(wordLength);
        while (guessedLetters.contains(guessChar)) {
            frequencyMap.get(wordLength).remove(0);
            guessChar = getCharToGuess(wordLength);
        }

        return guessChar;
    }

    /**
     * Get the next character to test for based on dictionary files
     * (used after first guesses already filled in for better accuracy)
     * 
     * @return the 'guessed' Character to be tested
     */
    public char nextGuess(String state) {
		List<String> state_list = new ArrayList<String>(Arrays.asList(state
				.split("[^A-Z_']+")));
		StringBuilder excluding = new StringBuilder();
		for (Iterator<Character> ex = incorrect_letters.iterator(); ex.hasNext();) {
			excluding.append(ex.next());
		}
		List<String> possibleWords = new ArrayList<String>();

		int count = 100;
		String guessing = null;
		int underscore_counter = 0;
		for (String word : state_list) { //guess the word with the least unknown letters
			for (int i = 0; i < word.length(); i++) {
				if ('_' == word.charAt(i)) {
					underscore_counter++;
				}
            }
			if (underscore_counter < count && underscore_counter > 0) {
				guessing = word;
				count = underscore_counter;
				underscore_counter = 0;
			}
		}
		//if the length is 1, then most likely letters are 'a' and 'i'
		if(guessing.length() == 1){
			return (!correct_letters.contains('a') && !incorrect_letters.contains('a')) ? 'a' : 'I';
		}
		//guessing the one with least number of '_'
		String word = guessing.toLowerCase();
		Pattern regex = Pattern.compile(word.replace(
				"_",
				(excluding.length() > 0) ? String.format("[a-z&&[^%s]]",
						excluding) : "[a-z]"));
		if (wordList.containsKey(word.length())) {
			for (String guess : wordList.get(word.length())) {
				Matcher match = regex.matcher(guess);
				if (match.find()) {
					possibleWords.add(guess); // matching words based on pattern of state word
				}
			}
        }

		//count the frequency of each letter within the possible words
		Map<Character, Integer> frequency = new HashMap<Character, Integer>();
		for (String possible : possibleWords) {
			Set<String> letters = new HashSet<String>();
			for (char letter : possible.toCharArray()) {
				if (!letters.contains(letter)) {
					if (!frequency.containsKey(letter)) {
						frequency.put(letter, 1);
					} else {
						frequency.put(letter, frequency.get(letter) + 1);
					}
				}
			}
		}
		//find the best letter by frequency
		char guessLetter = 'a';
		int freq = 0;
		boolean no_letter = true;
		for (char c = 'a'; c <= 'z'; c++) {
			if (!correct_letters.contains(c) && !incorrect_letters.contains(c)) {
				if (frequency.get(c) != null && frequency.get(c) > freq) {
					guessLetter = c;
					freq = frequency.get(c);
					no_letter = false;
				}
			}
		}
		//last resort
		if (no_letter) {
			for (char c = 'a'; c <= 'z'; ++c) {
				if (!(correct_letters.contains(c) || incorrect_letters.contains(c))) {
					return c;
				}
			}
        }
        
		return guessLetter;
    }
    
    // put the guessed letters into appropriate set
	public void update(char guess, boolean success) {
        guessedLetters.add(guess);
		if (success) {
			correct_letters.add(guess);
		} else {
			incorrect_letters.add(guess);
		}
    }
    
    /**
     * @param status current status
     * @return Percentage of blank characters remaining
     */
    public int percentageRemaining(Status status) {
        char[] arr = status.getState().toCharArray();
        int totalchars = arr.length;
        double incomplete = 0;
        for(char c : status.getState().toCharArray()) {
            if (c == '_') {
                incomplete++;
            }
        }
        
        return (int) Math.ceil(100.0 * incomplete / totalchars);
    }


    /**
     * @param wordLength length of the currently selected word
     * @return Character to be guessed
     */
    private Character getCharToGuess(int wordLength) {
        return (frequencyMap.get(wordLength).size() != 0) ? frequencyMap.get(wordLength).get(0) : getLikelyAlphabet();
    }

    /**
     * List of most likely characters
     * @return most likely character not guessed yet
     */
    private Character getLikelyAlphabet() {
        Character[] alphaFreq = {'t', 'a', 'o', 'i', 'n', 's', 'h', 'r', 'd', 'l', 'c', 'u',
                'm', 'w', 'f', 'g', 'y', 'p', 'b', 'v', 'k', 'j', 'x', 'q', 'z'};
        Character chosen = null;
        for (Character alphabet : alphaFreq) {
            if (!guessedLetters.contains(alphabet)) {
                chosen = alphabet;
                break;
            }
        }
        return chosen;
    }

}