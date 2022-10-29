package com.slid3rek.string_randomizer.dto;

import javax.validation.constraints.NotBlank;


public class NewJobDto {

    @NotBlank
    private Integer min;

    @NotBlank
    private Integer max;

    @NotBlank
    private Integer numberOfStrings;

    @NotBlank
    private String charset;

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

    public Integer getNumberOfStrings() {
        return numberOfStrings;
    }

    public void setNumberOfStrings(Integer numberOfStrings) {
        this.numberOfStrings = numberOfStrings;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
