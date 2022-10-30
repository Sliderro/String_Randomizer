package com.slid3rek.string_randomizer.controller;


import com.slid3rek.string_randomizer.dto.NewJobDto;
import com.slid3rek.string_randomizer.jpa.model.Job;
import com.slid3rek.string_randomizer.jpa.repository.JobRepository;
import com.slid3rek.string_randomizer.payload.MessageResponse;
import com.slid3rek.string_randomizer.payload.RunningResponse;
import com.slid3rek.string_randomizer.threads.JobRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    JobRepository jobRepository;

    /**
     * Post request function, which checks if the request to start the job is valid.
     * Valid request will result in creating a new job.
     * Maximum number of requested strings is Integer.MAX_VALUE.
     *
     * @param newJobDto DTO of Job class attributes required to create new jobs.
     * @return Returns HTTP response for a given request depending on the parameters given.
     */
    @PostMapping()
    public ResponseEntity<?> addNewJob(@RequestBody NewJobDto newJobDto) {

        if (newJobDto.getMin() == null || newJobDto.getMax() == null ||
                newJobDto.getNumberOfStrings() == null || newJobDto.getCharset() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse(
                    "Your request has been denied, missing 1 or more attributes"));
        }

        int possibleStrings = 0;

        for (int i = newJobDto.getMin(); i <= newJobDto.getMax(); i++) {
            possibleStrings += Math.pow(newJobDto.getCharset().length(), i);
            if (possibleStrings == Integer.MAX_VALUE) {
                break;
            }
        }

        if (newJobDto.getNumberOfStrings() > possibleStrings) {
            return ResponseEntity.badRequest().body(new MessageResponse(
                    "Number of requested strings cannot exceed the number of possible unique strings"));
        }

        if (doesCharsetContainDuplicates(newJobDto.getCharset())) {
            return ResponseEntity.badRequest().body(new MessageResponse(
                    "Provided charset contains duplicated characters"));
        }

        Job job = new Job(newJobDto.getMin(), newJobDto.getMax(), newJobDto.getNumberOfStrings(),
                newJobDto.getCharset(), true, "");
        jobRepository.save(job);

        new Thread(new JobRunnable(job.getId(), job.getMin(), job.getMax(), job.getNumberOfStrings(), job.getCharset()))
                .start();

        return ResponseEntity.ok(new MessageResponse("Request Accepted. Your request ID is: " + job.getId()));
    }

    private boolean doesCharsetContainDuplicates(String charset) {
        char[] chars = charset.toCharArray();
        Set<Character> characters = new HashSet<>();
        for (char c : chars) {
            if (characters.contains(c)) {
                return true;
            }
            characters.add(c);
        }
        return false;
    }

    @GetMapping()
    public ResponseEntity<?> getNumberOfRunningJobs() {

        Set<Job> jobs = jobRepository.findAllByIsRunning(true);

        return ResponseEntity.ok(new RunningResponse(jobs.size()));
    }

    @GetMapping(value = "/get-file/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) {

        if(!jobRepository.existsById(id)){
            return ResponseEntity.badRequest().body(new MessageResponse("There is no job with requested id."));
        }

        Job job = jobRepository.findJobById(id);
        if (job.getRunning()) {
            return ResponseEntity.badRequest().body(new MessageResponse(
                    "Job generating requested results is still running"));
        }

        byte[] fileBytes = null;
        try {
            fileBytes = Files.readAllBytes(Paths.get("./src/jobs/" + id + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + ".txt\"")
                .body(fileBytes);
    }

}
