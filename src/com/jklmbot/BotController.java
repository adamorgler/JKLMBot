package com.jklmbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BotController {

    private final int ALL_DIS_DEFAULT = 25;

    private ArrayList<String> words;

    private ArrayList<String> lastResults;

    private String lastLong;

    private String lastShort;

    private String lastKey;

    // 0 - all matches
    // 0 {n} - all matches, display top n
    // 1 - first match
    // 2 - random match
    // 3 - longest match
    // 4 - shortest match
    private int mode;

    private int allDisplayAmount;

    public BotController() {
        words = new ArrayList<String>();
        mode = 2;
        allDisplayAmount = ALL_DIS_DEFAULT;
        initWordList();
        displayModes();
    }

    public boolean step() {
        reset();
        System.out.println("Enter word part: ");
        Scanner sc = new Scanner(System.in);
        // check for mode change
        if(sc.hasNextInt()) {
            mode = sc.nextInt();
            displayModes();
            displayAllSpecial();
            if (mode == 0 && sc.hasNextInt()) {
                allDisplayAmount = sc.nextInt();
            } else {
                allDisplayAmount = ALL_DIS_DEFAULT;
            }
            return true;
        }
        lastKey = sc.nextLine();
        findMatches();
        switch(mode) {
            case(0):
                modeAll();
                break;
            case(1):
                modeFirst();
                break;
            case(2):
                modeRandom();
                break;
            case(3):
                modeLongest();
                break;
            case(4):
                modeShortest();
                break;
            default:
                modeRandom();
                mode = 2;
                break;
         }
        return true;
    }

    private void findMatches() {
        for (String word: words) {
            if(word.contains(lastKey)) {
                lastResults.add(word);
                if (lastLong == null && lastShort == null) {
                    lastLong = word;
                    lastShort = word;
                }
                if (word.length() > lastLong.length()) {
                    lastLong = word;
                }
                if (word.length() < lastShort.length()) {
                    lastShort = word;
                }
            }
        }
    }

    private void modeAll() {
        int size = lastResults.size();
        if (size > allDisplayAmount) {
            System.out.println("Found " + size + " results, showing only top " + allDisplayAmount);
            size = allDisplayAmount;
        }
        if (size > 0) {
            for(int i = 0; i < size; i++) {
                formatListResult(i + 1);
            }
        } else {
            displayNoWordExists();
        }
    }

    private void modeFirst() {
        if (lastResults.size() > 0) {
            formatResult(0);
        } else {
            displayNoWordExists();
        }
    }

    private void modeRandom() {
        if (lastResults.size() > 0) {
            Random rand = new Random();
            formatResult(rand.nextInt(lastResults.size()));
        } else {
            displayNoWordExists();
        }
    }

    private void modeLongest() {
        if (lastLong != null) {
            System.out.println(lastLong);
        } else {
            displayNoWordExists();
        }
    }

    private void modeShortest() {
        if (lastShort != null) {
            System.out.println(lastLong);
        } else {
            displayNoWordExists();
        }
    }

    private void formatResult(int i) {
        System.out.println(lastResults.get(i));
    }

    private void formatListResult(int i) {
        if (i < lastResults.size() && i >= 0) {
            System.out.println(i + ". " + lastResults.get(i));
        }
    }

    private void displayNoWordExists() {
        System.out.println("No word exists containing '" + lastKey + "'");
    }

    private void displayModes() {
        System.out.println("Modes: | 0-all | 1-first | 2-random | 3-long | 4-short |");
        System.out.println("Mode selected: " + mode);
    }

    private void displayAllSpecial() {
        System.out.println("Optional: type 0 {n} to show n amount of results");
    }
    private void reset() {
        lastResults = new ArrayList<String>();
        lastLong = null;
        lastShort = null;
    }

    private void initWordList() {
        File f = new File("words_alpha.txt");
        try {
            Scanner sc = new Scanner(f);
            while(sc.hasNextLine()) {
                String next = sc.nextLine();
                words.add(next);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
