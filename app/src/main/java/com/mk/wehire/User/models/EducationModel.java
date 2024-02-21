package com.mk.wehire.User.models;

public class EducationModel {

    private String graduationStatus,collegeName,startYear,endYear,degreeName,
            streamName,performanceScale,performane,key;


    public EducationModel() {
    }

    public EducationModel(String graduationStatus, String collegeName, String startYear, String endYear, String degreeName, String streamName, String performanceScale, String performane, String key) {
        this.graduationStatus = graduationStatus;
        this.collegeName = collegeName;
        this.startYear = startYear;
        this.endYear = endYear;
        this.degreeName = degreeName;
        this.streamName = streamName;
        this.performanceScale = performanceScale;
        this.performane = performane;
        this.key = key;
    }

    public String getGraduationStatus() {
        return graduationStatus;
    }

    public void setGraduationStatus(String graduationStatus) {
        this.graduationStatus = graduationStatus;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public String getPerformanceScale() {
        return performanceScale;
    }

    public void setPerformanceScale(String performanceScale) {
        this.performanceScale = performanceScale;
    }

    public String getPerformane() {
        return performane;
    }

    public void setPerformane(String performane) {
        this.performane = performane;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
