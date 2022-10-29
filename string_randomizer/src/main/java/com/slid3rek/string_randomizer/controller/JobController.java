package com.slid3rek.string_randomizer.controller;


import com.slid3rek.string_randomizer.dto.NewJobDto;
import com.slid3rek.string_randomizer.jpa.model.Job;
import com.slid3rek.string_randomizer.jpa.repository.JobRepository;
import com.slid3rek.string_randomizer.payload.MessageResponse;
import com.slid3rek.string_randomizer.payload.RunningResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    JobRepository jobRepository;

    @PostMapping()
    public ResponseEntity<?> addNewJob(@RequestBody NewJobDto newJobDto){

        Job job = new Job(newJobDto.getMin(), newJobDto.getMax(), newJobDto.getNumberOfStrings(), newJobDto.getCharset(), true, "");
        jobRepository.save(job);
        return ResponseEntity.ok(new MessageResponse("Request Accepted. Your request ID is: " + job.getId()));
    }

    @GetMapping
    public RunningResponse getNumberOfRunningJobs(){

        Set<Job> jobs = jobRepository.findAllByIsRunning(true);

        return new RunningResponse(jobs.size());
    }
}
