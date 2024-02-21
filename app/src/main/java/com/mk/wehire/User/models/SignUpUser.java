package com.mk.wehire.User.models;

public class SignUpUser {

    private String email;
    private String pass;
    private String isProfCom,isProfVerified;
    private String firstName;
    private String lastName;
    private String location;
    private String gender;
    private String language;
    private String mobile;

    private String graduationStatus,collegeName,startYear,endYear,degreeName,
            streamName,performanceScale,performane,key,applicantStatus,Ftoken;



    public SignUpUser() {

    }

    public SignUpUser(String email, String pass, String isProfCom,String isProfVerified, String firstName, String lastName) {
        this.email = email;
        this.pass = pass;
        this.isProfCom = isProfCom;
        this.isProfVerified = isProfVerified;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public SignUpUser(String email, String pass, String firstName, String lastName, String isProfCom,String isProfVerified, String location, String gender, String language, String mobile, String Ftoken) {
        this.email = email;
        this.pass = pass;
        this.isProfCom = isProfCom;
        this.isProfVerified = isProfVerified;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.gender = gender;
        this.language = language;
        this.mobile = mobile;
        this.Ftoken = Ftoken;

    }

    public SignUpUser(String email, String firstName, String lastName, String mobile, String applicantStatus, String location, String degreeName,String key) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.applicantStatus = applicantStatus;
        this.location = location;
        this.degreeName = degreeName;
        this.key = key;

    }

    public SignUpUser(String graduationStatus, String collegeName, String startYear, String endYear, String degreeName, String streamName, String performanceScale, String performane, String key) {
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


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getIsProfCom() {
        return isProfCom;
    }

    public void setIsProfCom(String isProfCom) {
        this.isProfCom = isProfCom;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIsProfVerified() {
        return isProfVerified;
    }

    public void setIsProfVerified(String isProfVerified) {
        this.isProfVerified = isProfVerified;
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

    public String getApplicantStatus() {
        return applicantStatus;
    }

    public void setApplicantStatus(String applicantStatus) {
        this.applicantStatus = applicantStatus;
    }

    public String getFtoken() {
        return Ftoken;
    }

    public void setFtoken(String ftoken) {
        Ftoken = ftoken;
    }
}

