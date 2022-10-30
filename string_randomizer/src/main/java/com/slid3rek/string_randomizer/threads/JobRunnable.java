package com.slid3rek.string_randomizer.threads;

import com.slid3rek.string_randomizer.jpa.repository.JobRepository;
import com.slid3rek.string_randomizer.services.BeanFetcherService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class JobRunnable implements Runnable {

    //private final static double coefficient = 0.4;
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

        //todo: optimization of algorithm.
        //if (calculatePossibleStrings() == Integer.MAX_VALUE || possibleStrings * coefficient > numberOfStrings) {
        stringSet = new LinkedHashSet<>();
        while (stringSet.size() < numberOfStrings) {
            stringSet.add(generateRandomString());
        }
        //} else {
        //    stringSet = generateRandomStringSet();
        //}

        createFileFromSet(stringSet);

        JobRepository jobRepository = BeanFetcherService.getBean(JobRepository.class);
        jobRepository.findById(id).
                map(job -> {
                    job.setRunning(false);
                    job.setUrl("./src/jobs/" + this.id + ".txt");
                    return jobRepository.save(job);
                });
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
        int noOfChars = r.nextInt(max - min+1) + min;
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
            BufferedWriter out = new BufferedWriter(new FileWriter("./src/jobs/" + this.id + ".txt"));
            for (String s : stringSet) {
                out.write(s);
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
