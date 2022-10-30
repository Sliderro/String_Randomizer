package com.slid3rek.string_randomizer.jpa.model;


import javax.persistence.*;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    private Integer min;
    private Integer max;
    private Integer numberOfStrings;
    private String charset;
    private Boolean isRunning;

    private String url;

    public Job(Integer min, Integer max, Integer numberOfStrings, String charset, Boolean isRunning, String url) {
        this.min = min;
        this.max = max;
        this.numberOfStrings = numberOfStrings;
        this.charset = charset;
        this.isRunning = isRunning;
        this.url = url;
    }

    public Job() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Boolean getRunning() {
        return isRunning;
    }

    public void setRunning(Boolean running) {
        isRunning = running;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getNumberOfStrings() {
        return numberOfStrings;
    }

    public void setNumberOfStrings(Integer numberOfStrings) {
        this.numberOfStrings = numberOfStrings;
    }
}
