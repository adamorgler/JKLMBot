package com.jklmbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BotController {

    private ArrayList<String> words;

    private ArrayList<String> lastResults;

    private String lastLong;

    private String lastShort;

    // 0 - all matches
    // 1 - first match
    // 2 - random match
    // 3 - longest match
    // 4 - shortest match
    private int mode;

    public BotController() {
        words = new ArrayList<String>();
        mode = 2;
        initWordList();
        System.out.println("Modes: | 0-all | 1-first | 2-random | 3-long | 4-short |");
    }

    public boolean step() {
        reset();
        System.out.println("Enter word part: ");
        Scanner sc = new Scanner(System.in);
        if(sc.hasNextInt()) {
            mode = sc.nextInt();
            return true;
        }
        String input = sc.nextLine();
        findMatches(input);
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

    private void findMatches(String key) {
        for (String word: words) {
            if(word.contains(key)) {
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
        if (size > 100) {
            System.out.println("Found " + size + " results, showing only top 100");
            size = 100;
        }
        for(int i = 0; i < size; i++) {
            formatListResult(i + 1);
        }
    }

    private void modeFirst() {
        formatResult(0);
    }

    private void modeRandom() {
        Random rand = new Random();
        formatResult(rand.nextInt(lastResults.size()));
    }

    private void modeLongest() {
        System.out.println(lastLong);
    }

    private void modeShortest() {
        System.out.println(lastShort);
    }

    private void formatResult(int i) {
        System.out.println(lastResults.get(i));
    }

    private void formatListResult(int i) {
        if (i < lastResults.size() && i >= 0) {
            System.out.println(i + ". " + lastResults.get(i));
        }
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
