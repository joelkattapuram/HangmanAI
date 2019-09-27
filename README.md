### Compile and Run Instructions:

    Install Maven

    To build and run, run the following command:
    ./run.sh joelkattapuram@berkeley.edu {number of games}


    ex: 
    ./run.sh joelkattapuram@berkeley.edu 100


### Methodology:
    
I first built a frequency map of all characters based on word length. For example, a word of length 1 is most likely to contain characters 'a' or 'i' so those two characters are mapped to that value. This is sufficient to perform relatively decently in the hangman challenge, however, it fails to be optimal since it simply guesses the most common letters. 

So, I began working on a second algorithm based on a large list of all the most common english words. Similarily mapping word length to a list of words, I used regex expressions to match patterns in the input word to those within the mapping to identify the most likely characters remaining. To artificially introduce priority based on word commonality, I included multiple word lists, hoping that the most common words would end up appearing multiple times in the map. This worked well for the final aspects of the challenge, when only a few blanks remained, but was slow and innacurate initially due to the sheer number of potential matches given an entirely blank word. 

To be as optimal as possible, I combined these two algorithms, allowing the first algorithm to guess for the first 40% of guesses and letting the more word-specific algorithm take over for the remaining 60%. This achieved a success-rate between 45-65% in my testing.