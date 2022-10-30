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

    private final Integer id;
    private final Integer min;
    private final Integer max;
    private final Integer numberOfStrings;
    private final String charset;

    public JobRunnable(Integer id, Integer min, Integer max, Integer numberOfStrings, String charset) {
        this.id = id;
        this.min = min;
        this.max = max;
        this.numberOfStrings = numberOfStrings;
        this.charset = charset;
    }

    @Override
    public void run() {

        Set<String> stringSet;


        stringSet = new LinkedHashSet<>();
        while (stringSet.size() < numberOfStrings) {
            stringSet.add(generateRandomString());
        }

        createFileFromSet(stringSet);

        JobRepository jobRepository = BeanFetcherService.getBean(JobRepository.class);
        jobRepository.findById(id).map(job -> {
                    job.setRunning(false);
                    job.setUrl("./src/jobs/" + this.id + ".txt");
                    return jobRepository.save(job);
                });
    }

    /**
     * Method returns a randomly generated string with length between given constraints.
     *
     * @return Returns a randomly generated string.
     */
    private String generateRandomString() {
        Random random = new Random();

        int noOfChars = random.nextInt(max - min + 1) + min;

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < noOfChars; i++) {
            int randomChar = random.nextInt(charset.length());
            stringBuilder.append(charset.charAt(randomChar));
        }

        return stringBuilder.toString();
    }

    /**
     * Function creates a file from a given set of String objects.
     *
     * @param stringSet the set of String objects that need to be converted into the file.
     */
    private void createFileFromSet(Set<String> stringSet) {
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
