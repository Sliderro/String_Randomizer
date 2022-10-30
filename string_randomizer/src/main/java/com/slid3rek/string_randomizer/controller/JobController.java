package com.slid3rek.string_randomizer.controller;


import com.slid3rek.string_randomizer.dto.NewJobDto;
import com.slid3rek.string_randomizer.jpa.model.Job;
import com.slid3rek.string_randomizer.jpa.repository.JobRepository;
import com.slid3rek.string_randomizer.payload.MessageResponse;
import com.slid3rek.string_randomizer.payload.RunningResponse;
import com.slid3rek.string_randomizer.threads.JobRunnable;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
    public ResponseEntity<?> addNewJob(@RequestBody NewJobDto newJobDto){

        if(newJobDto.getMin() == null || newJobDto.getMax() == null ||
                newJobDto.getNumberOfStrings() == null || newJobDto.getCharset() == null){
            return ResponseEntity.badRequest().body(new MessageResponse(
                    "Your request has been denied, missing 1 or more attributes"));
        }

        int possibleStrings = 0;

        for (int i = newJobDto.getMin(); i <= newJobDto.getMax(); i++) {
            possibleStrings += Math.pow(newJobDto.getCharset().length(), i);
            if(possibleStrings==Integer.MAX_VALUE){
                break;
            }
        }

        if(newJobDto.getNumberOfStrings() > possibleStrings){
            return ResponseEntity.badRequest().body(new MessageResponse(
                    "Number of requested strings cannot exceed the number of possible unique strings"));
        }

        Job job = new Job(newJobDto.getMin(), newJobDto.getMax(), newJobDto.getNumberOfStrings(),
                newJobDto.getCharset(), true, "");
        jobRepository.save(job);

        new Thread(new JobRunnable(job.getId(),job.getMin(), job.getMax(),job.getNumberOfStrings(),job.getCharset()))
                .start();

        return ResponseEntity.ok(new MessageResponse("Request Accepted. Your request ID is: " + job.getId()));
    }

    @GetMapping()
    public ResponseEntity<?> getNumberOfRunningJobs() {

        Set<Job> jobs = jobRepository.findAllByIsRunning(true);

        return ResponseEntity.ok(new RunningResponse(jobs.size()));
    }

}
