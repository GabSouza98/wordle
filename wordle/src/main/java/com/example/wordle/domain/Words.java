package com.example.wordle.domain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Words {

    public static List<String> wordsList = new ArrayList<>();

    public static void getWords(String inPath) {

        try (BufferedReader br = new BufferedReader(new FileReader(inPath))) {
            String word = br.readLine();
            while(word != null) {
                wordsList.add(word);
                word = br.readLine();
            }
            System.out.println("Palavras lidas: " + wordsList.size());
        } catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static List<String> getWordsList() throws IOException {

        List<String> words = new ArrayList<>();

//        try (BufferedReader br = new BufferedReader(new FileReader(inPath))) {
//            String word = br.readLine();
//            while(word != null) {
//                words.add(word);
//                word = br.readLine();
//            }
//            System.out.println("Palavras lidas: " + words.size());
//        } catch(IOException e) {
//            System.out.println("Error: " + e.getMessage());
//        }

        InputStream inputStream = Words.class.getResourceAsStream("/palavras_com_5_letras.txt");
        try {
            assert inputStream != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String word = br.readLine();
                while(word != null) {
                    words.add(word);
                    word = br.readLine();
                }
                System.out.println("Palavras lidas: " + words.size());
            }
        } catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return words;
    }

    public static void exportWordsWithNLetters(String outPath, Integer NLetters) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outPath))) {
            if(!wordsList.isEmpty()) {
                for(String word : wordsList) {
                    if(word.length() == NLetters) {
                        bw.write(word.toLowerCase());
                        bw.newLine();
                    }
                }
            }
        } catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void contaPalavras() {
        int totalPalavras = 0;

        for(int i = 1; i <= 23; i++) {
            for(String word : wordsList) {
                if(word.length() == i) {
                    totalPalavras++;
                }
            }
            System.out.println("Total de palavras com " + i + " letras: " + totalPalavras);
            totalPalavras = 0;
        }
    }

    public static void buscaPalavras(Integer wordSize, ArrayList<Letter> existingLetters, ArrayList<Letter> rightLetters, ArrayList<Letter> absentLetters) {

        List<String> sameSizeWords = new ArrayList<>();
        List<String> wordsWithRightLetters = new ArrayList<>();
        List<String> wordsWithExistingLetters = new ArrayList<>();
        List<String> possibleWords = new ArrayList<>();
        List<String> finalPossibleWords = new ArrayList<>();

        if(!wordsList.isEmpty()) {
            for(String word : wordsList) {
                if(word.length() == wordSize) {
                    sameSizeWords.add(word);
                }
            }
        }

        if(rightLetters != null) {
            for(String word : sameSizeWords) {
                if(containsRightLetters(word, rightLetters)) {
                    wordsWithRightLetters.add(word);
                }
            }
        } else {
            wordsWithRightLetters.addAll(sameSizeWords);
        }

        if(existingLetters != null) {
            for(String word : sameSizeWords) {
                if(containsExistingLetters(word, existingLetters)) {
                    wordsWithExistingLetters.add(word);
                }
            }
        } else {
            wordsWithExistingLetters.addAll(sameSizeWords);
        }

        for(String word : sameSizeWords) {
            if(wordsWithRightLetters.contains(word) && wordsWithExistingLetters.contains(word)) {
                possibleWords.add(word);
            }
        }

        finalPossibleWords = new ArrayList<>(possibleWords);

        if(absentLetters != null) {
            for(String word : possibleWords) {
                for(Letter letter : absentLetters) {
                    if(letter.getPosition() == null) {
                        if(word.contains(String.valueOf(letter.getLetter()))) {
                            finalPossibleWords.remove(word);
                        }
                    } else if(word.charAt(letter.getPosition()) == letter.getLetter()) {
                        finalPossibleWords.remove(word);
                    }
                }
            }
        }

        System.out.println(finalPossibleWords);
    }

    public static boolean containsRightLetters(String word, ArrayList<Letter> rightLetters) {
        boolean contains = true;
        for(Letter letter : rightLetters) {
            if(word.charAt(letter.getPosition()) != letter.getLetter()) {
                contains = false;
            }
        }
        return contains;
    }

    public static boolean containsExistingLetters(String word, ArrayList<Letter> existingLetters) {
        boolean contains = true;
        for(Letter letter : existingLetters) {
            if(!word.contains(String.valueOf(letter.getLetter()))) {
                contains = false;
            } else if (word.charAt(letter.getPosition()) == letter.getLetter()) {
                contains = false;
            }
        }
        return contains;
    }


}
