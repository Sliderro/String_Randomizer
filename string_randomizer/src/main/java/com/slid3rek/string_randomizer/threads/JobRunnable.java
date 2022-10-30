package com.slid3rek.string_randomizer.threads;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class JobRunnable implements Runnable {

    private final static double coefficient = 0.4;
    private final Integer id;
    private final Integer min;
    private final Integer max;
    private final Integer numberOfStrings;
    private final String charset;
    private final Integer possibleStrings;


    public JobRunnable(Integer id, Integer min, Integer max, Integer numberOfStrings, String charset) {
        this.id = id;
        this.min = min;
        this.max = max;
        this.numberOfStrings = numberOfStrings;
        this.charset = charset;
        this.possibleStrings = calculatePossibleStrings();
    }

    @Override
    public void run() {

        Set<String> stringSet;

        System.out.println("I'm here and alive: " + Thread.currentThread());
        if (calculatePossibleStrings() == Integer.MAX_VALUE || possibleStrings * coefficient > numberOfStrings) {
            stringSet = new LinkedHashSet<>();
            while (stringSet.size() < numberOfStrings) {
                stringSet.add(generateRandomString());
                if (stringSet.size() % 100000 == 0) {
                    System.out.println(stringSet.size());
                }
            }
        } else {
            stringSet = generateRandomStringSet();
        }

        createFileFromSet(stringSet);
        System.out.println("My job here is done: " + Thread.currentThread());

    }

    private int calculatePossibleStrings() {
        int returnValue = 0;
        for (int i = this.min; i <= this.max; i++) {
            returnValue += Math.pow(this.charset.length(), i);
            if (!(returnValue < Integer.MAX_VALUE)) {
                break;
            }
        }
        return returnValue;
    }

    private String generateRandomString() {
        Random r = new Random();
        int noOfChars = weighedProbability();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < noOfChars; i++) {
            int k = r.nextInt(charset.length());
            s.append(charset.charAt(k));
        }
        return s.toString();
    }

    private Set<String> generateRandomStringSet() {

        return null;
    }

    private void/*?*/ createFileFromSet(Set<String> stringSet) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("./" + this.id + ".txt"));
            for (String s : stringSet) {
                out.write(s);
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int weighedProbability() {
        Random r = new Random();
        double p = r.nextDouble();
        int noOfChars;
        if (max.equals(min)) {
            noOfChars = max;
        } else if (max - 1 == min) {
            if (p < 1.0 / charset.length()) {
                noOfChars = min;
            } else {
                noOfChars = max;
            }
        } else {
            double v = (1.0 / charset.length()) / charset.length();
            if (max - 2 == min) {
                if (p < v) {
                    noOfChars = min;
                } else if (p < 1.0 / charset.length()) {
                    noOfChars = max - 1;
                } else {
                    noOfChars = max;
                }
            } else {
                if ((p < v)) {
                    noOfChars = r.nextInt(max - 2 - min) + min;
                } else if (p < 1.0 / charset.length()) {
                    noOfChars = max - 1;
                } else {
                    noOfChars = max;
                }
            }
        }
        return noOfChars;
    }

}
