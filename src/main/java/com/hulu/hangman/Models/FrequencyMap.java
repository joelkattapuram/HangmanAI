package com.hulu.hangman.Models;

import java.util.*;

/**
 * A map with the most likely letters for a given word's length
 * <p>
 * <b>Credit</b>
 * <ul>
 * <li>The DataGenetics Blog (http://www.datagenetics.com/blog/april12012/index.html)</li>
 * </ul>
 */
public class FrequencyMap {

    private static final Character[][] FREQ_MAP = {
            {'A', 'I'},
            {'A', 'O', 'E', 'I', 'M', 'H', 'N', 'U', 'S', 'T', 'Y', 'B', 'L', 'P', 'X', 'D', 'F', 'R', 'W', 'G', 'J', 'K'},
            {'A', 'E', 'O', 'I', 'T', 'S', 'U', 'P', 'R', 'N', 'D', 'B', 'G', 'M', 'Y', 'L', 'H', 'W', 'F', 'C', 'K', 'X', 'V', 'J', 'Z', 'Q'},
            {'A', 'E', 'S', 'O', 'I', 'R', 'L', 'T', 'N', 'U', 'D', 'P', 'M', 'H', 'C', 'B', 'K', 'G', 'Y', 'W', 'F', 'V', 'J', 'Z', 'X', 'Q'},
            {'S', 'E', 'A', 'R', 'O', 'I', 'L', 'T', 'N', 'U', 'D', 'C', 'Y', 'P', 'M', 'H', 'G', 'B', 'K', 'F', 'W', 'V', 'Z', 'X', 'J', 'Q'},
            {'E', 'S', 'A', 'R', 'I', 'O', 'L', 'N', 'T', 'D', 'U', 'C', 'M', 'P', 'G', 'H', 'B', 'Y', 'K', 'F', 'W', 'V', 'Z', 'X', 'J', 'Q'},
            {'E', 'S', 'I', 'A', 'R', 'N', 'T', 'O', 'L', 'D', 'U', 'C', 'G', 'P', 'M', 'H', 'B', 'Y', 'F', 'K', 'W', 'V', 'Z', 'X', 'J', 'Q'},
            {'E', 'S', 'I', 'A', 'R', 'N', 'T', 'O', 'L', 'D', 'C', 'U', 'G', 'M', 'P', 'H', 'B', 'Y', 'F', 'K', 'W', 'V', 'Z', 'X', 'Q', 'J'},
            {'E', 'S', 'I', 'R', 'A', 'N', 'T', 'O', 'L', 'C', 'D', 'U', 'G', 'M', 'P', 'H', 'B', 'Y', 'F', 'V', 'K', 'W', 'Z', 'X', 'Q', 'J'},
            {'E', 'I', 'S', 'R', 'A', 'N', 'T', 'O', 'L', 'C', 'D', 'U', 'G', 'M', 'P', 'H', 'B', 'Y', 'F', 'V', 'K', 'W', 'Z', 'X', 'Q', 'J'},
            {'E', 'I', 'S', 'N', 'A', 'R', 'T', 'O', 'L', 'C', 'U', 'D', 'P', 'M', 'G', 'H', 'B', 'Y', 'F', 'V', 'K', 'W', 'Z', 'X', 'Q', 'J'},
            {'E', 'I', 'S', 'N', 'T', 'A', 'R', 'O', 'L', 'C', 'P', 'U', 'M', 'D', 'G', 'H', 'Y', 'B', 'V', 'F', 'Z', 'K', 'W', 'X', 'Q', 'J'},
            {'I', 'E', 'N', 'T', 'S', 'A', 'O', 'R', 'L', 'C', 'P', 'U', 'M', 'G', 'D', 'H', 'Y', 'B', 'V', 'F', 'Z', 'X', 'K', 'W', 'Q', 'J'},
            {'I', 'E', 'T', 'S', 'N', 'A', 'O', 'R', 'L', 'C', 'P', 'U', 'M', 'D', 'H', 'G', 'Y', 'B', 'V', 'F', 'Z', 'X', 'K', 'W', 'Q', 'J'},
            {'I', 'E', 'T', 'N', 'S', 'O', 'A', 'R', 'L', 'C', 'P', 'U', 'M', 'D', 'H', 'G', 'Y', 'B', 'V', 'F', 'Z', 'X', 'W', 'K', 'Q', 'J'},
            {'I', 'E', 'T', 'S', 'N', 'A', 'O', 'R', 'L', 'C', 'P', 'U', 'M', 'H', 'D', 'Y', 'G', 'B', 'V', 'F', 'Z', 'X', 'W', 'Q', 'K', 'J'},
            {'I', 'E', 'T', 'N', 'S', 'O', 'A', 'R', 'L', 'C', 'P', 'U', 'M', 'H', 'D', 'G', 'Y', 'B', 'V', 'F', 'Z', 'X', 'Q', 'W', 'J', 'K'},
            {'I', 'S', 'E', 'T', 'O', 'N', 'R', 'A', 'L', 'C', 'P', 'M', 'U', 'H', 'D', 'G', 'Y', 'B', 'V', 'Z', 'F', 'X', 'Q', 'W', 'K'},
            {'I', 'E', 'T', 'O', 'N', 'A', 'S', 'R', 'L', 'C', 'P', 'M', 'U', 'H', 'D', 'G', 'Y', 'B', 'V', 'F', 'Z', 'X', 'K', 'J', 'Q', 'W'},
            {'I', 'O', 'E', 'T', 'R', 'S', 'A', 'N', 'C', 'L', 'P', 'H', 'U', 'M', 'Y', 'D', 'G', 'B', 'Z', 'V', 'F', 'K', 'X', 'J', 'Q'}
    };

    /**
     * @param wordLength Length of word being guessed
     * @param attemptNum Number of attempts being used to guess (first (1), second (2) ... )
     * @return character from the frequency map
     */
    @Deprecated
    public static Character getCharacter(int wordLength, int attemptNum) {
        return FREQ_MAP[wordLength - 1][attemptNum - 1];
    }

    /**
     * @return A new {@link Map} instance of FREQ_MAP
     */
    public static Map<Integer, List<Character>> getFreqMap() {
        Map<Integer, List<Character>> map = new HashMap<>();
        for (int i = 0; i < FREQ_MAP.length; i++) {
            List<Character> listForWordLength = new ArrayList<>();
            Collections.addAll(listForWordLength, FREQ_MAP[i]);
            map.put(i + 1, listForWordLength);
        }
        return map;
    }

}