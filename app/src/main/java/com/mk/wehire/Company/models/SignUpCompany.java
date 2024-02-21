package com.mk.wehire.Company.models;

public class SignUpCompany {

    private String email;
    private String pass;
    private String isProfCom;
    private String firstName;
    private String lastName;
    private String mobile;
    private String isVerified;
    private String companyName;
    private String companyWebUrl;
    private String companyImgUrl;
    private String companyShortName;
    private String jobPosition;
    private String companyIndustry;
    private String companySize;
    private String companyLocation;
    private String uid;
    private String aboutCompany;
    private String ftoken;


    public SignUpCompany() {

    }

    public SignUpCompany(String email, String pass, String isProfCom, String firstName, String lastName, String isVerified) {
        this.email = email;
        this.pass = pass;
        this.isProfCom = isProfCom;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isVerified = isVerified;
    }

    public SignUpCompany(String email, String pass, String firstName, String lastName, String isProfCom, String companyLocation,
                         String companySize, String companyShortName, String mobile,String  isVerified,String companyName,
                         String companyWebUrl,String companyImgUrl,String companyIndustry,String aboutCompany,String ftoken) {
        this.email = email;
        this.pass = pass;
        this.isProfCom = isProfCom;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyLocation = companyLocation;
        this.companySize = companySize;
        this.companyIndustry = companyIndustry;
        this.companyShortName = companyShortName;
        this.mobile = mobile;
        this.isVerified = isVerified;
        this.companyName = companyName;
        this.companyWebUrl = companyWebUrl;
        this.companyImgUrl = companyImgUrl;
        this.aboutCompany = aboutCompany;
        this.ftoken = ftoken;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyWebUrl() {
        return companyWebUrl;
    }

    public void setCompanyWebUrl(String companyWebUrl) {
        this.companyWebUrl = companyWebUrl;
    }

    public String getCompanyImgUrl() {
        return companyImgUrl;
    }

    public void setCompanyImgUrl(String companyImgUrl) {
        this.companyImgUrl = companyImgUrl;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getCompanyIndustry() {
        return companyIndustry;
    }

    public void setCompanyIndustry(String companyIndustry) {
        this.companyIndustry = companyIndustry;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }
    

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAboutCompany() {
        return aboutCompany;
    }

    public void setAboutCompany(String aboutCompany) {
        this.aboutCompany = aboutCompany;
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }

    public String getFtoken() {
        return ftoken;
    }

    public void setFtoken(String ftoken) {
        this.ftoken = ftoken;
    }
}

