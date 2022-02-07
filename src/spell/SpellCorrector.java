package spell;
import org.w3c.dom.Attr;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {
    private Trie myTrie;

    //Constructor
    public SpellCorrector() {
        myTrie = new Trie();
    }

    //Primary functions. In order called.
    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        //Read dictionary file
        processFile(new File(dictionaryFileName));
    }

    public void processFile(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("(\\s+)+");

        //Populate the Trie
        while (scanner.hasNext()) {
            String str = scanner.next();
            myTrie.add(str);
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        String lowerWord = inputWord.toLowerCase();
        Node wordNode = myTrie.find(lowerWord);
        //If the word is found
        if (wordNode != null) {
            return lowerWord;
        }

        //If the word isn't found...
        ArrayList<String> possibleWords = new ArrayList<>();
        //Find Edit level 1 words and add them to possible words list.
        ArrayList<String> editLevel1Words = getAllPossibleWords(lowerWord);
        for (String level1Word : editLevel1Words) {
            if (myTrie.find(level1Word) != null) {
                if (myTrie.find(level1Word).getValue() > 0) {
                    possibleWords.add(level1Word);
                }
            }
        }
        //If possible words list is not empty...
        if (possibleWords.size() != 0) {
            //Return word that appears the most, sorted alphabetically.
            return getMostFrequentWord(possibleWords);
        }
        //If list is empty...
        //Find Edit level 2 words and add them to possible words list.
        ArrayList<String> editLevel2Words = new ArrayList<>();
        if (possibleWords.size() == 0) {
            //Use each word in list to run through each measure of distance.
            for (String editLevel1Word : editLevel1Words) {
                ArrayList<String> tempLevel2Array = getAllPossibleWords(editLevel1Word);
                editLevel2Words.addAll(tempLevel2Array);
            }
            for (String editLevel2Word : editLevel2Words) {
                if (myTrie.find(editLevel2Word) != null) {
                    if (myTrie.find(editLevel2Word).getValue() > 0) {
                        possibleWords.add(editLevel2Word);
                    }
                }
            }
            //If possible words list is not empty...
            if (possibleWords.size() != 0) {
                //Return word that appears the most, sorted alphabetically.
                return getMostFrequentWord(possibleWords);
            }
            //If possible words list is empty..]
            if (possibleWords.size() == 0) {
                return null;
            }
        }

        return null;
    }

    public ArrayList<String> getAllPossibleWords(String input) {
        ArrayList<String> possibleWords = new ArrayList<String>();
        char letter;

        //Deletion
        for (int i = 0; i < input.length(); i++) {
            possibleWords.add(input.substring(0,i) + input.substring(i+1));
        }
        //Transposition
        for (int i = 0; i < (input.length() - 1); i++) {
            possibleWords.add(input.substring(0,i) + input.charAt(i+1) + input.charAt(i) + input.substring(i+2));
        }
        //Alteration
        for (int i = 0; i < input.length(); i++) {
            for (int j = 0; j < 26; j++) {
                letter = (char)('a' + j);
                possibleWords.add(input.substring(0,i) + letter + input.substring(i+1));
            }
        }
        //Insertion
        for (int i = 0; i < (input.length() + 1); i++) {
            for (int j = 0; j < 26; j++) {
                letter = (char)('a' + j);
                possibleWords.add(input.substring(0,i) + letter + input.substring(i));
            }
        }
        return possibleWords;
    }

    public String getMostFrequentWord(ArrayList<String> words) {
        ArrayList<String> mostFrequentWords = new ArrayList<>();
        int maxValue = 0;
        for (String s : words) {
            if (myTrie.find(s).getValue() > maxValue) {
                maxValue = myTrie.find(s).getValue();
            }
        }
        for (String word : words) {
            if (myTrie.find(word).getValue() == maxValue) {
                mostFrequentWords.add(word);
            }
        }

        if (mostFrequentWords.size() > 1) {
            //Sort alphabetically
            Collections.sort(mostFrequentWords);
            //Return first alphabetical word
            return mostFrequentWords.get(0);
        }
        else if (mostFrequentWords.size() == 1) {
            //Return only result of most frequent word
            return mostFrequentWords.get(0);
        }
        return null;
    }
}