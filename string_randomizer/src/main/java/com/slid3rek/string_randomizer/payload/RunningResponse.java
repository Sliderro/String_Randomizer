package com.slid3rek.string_randomizer.payload;

public class RunningResponse {

    private Integer numberOfRunningJobs;

    public RunningResponse(Integer numberOfRunningJobs){
        this.numberOfRunningJobs = numberOfRunningJobs;
    }


    public Integer getNumberOfRunningJobs() {
        return numberOfRunningJobs;
    }

    public void setNumberOfRunningJobs(Integer numberOfRunningJobs) {
        this.numberOfRunningJobs = numberOfRunningJobs;
    }
}
