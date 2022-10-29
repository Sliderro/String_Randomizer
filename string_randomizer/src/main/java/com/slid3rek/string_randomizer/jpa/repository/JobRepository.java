package com.slid3rek.string_randomizer.jpa.repository;

import com.slid3rek.string_randomizer.jpa.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface JobRepository extends JpaRepository<Job, Boolean> {

    /**
     * @param isRunning determines the state of a job.
     * @return Set of jobs with chosen state.
     */
    Set<Job> findAllByIsRunning(Boolean isRunning);


}
