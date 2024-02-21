package com.mk.wehire.User.models;

public class ApplicationModel {

    private String courseName,workLocation,companyName,salary,
            totalMonths,companyImgUrl,partFullTime,key,jobOrIntern,
            compLongDes,compWeb,internRole,openingNum,roleResponsibility,formLink,appStatus,totalApplicants;

    private String perk,perkCert,perkLetterOfR,perkFlexWh,perkDayWeek;
    private String skill;
    public ApplicationModel() {
    }

    public ApplicationModel(String perk) {
        this.perk = perk;
    }



    public ApplicationModel(String courseName, String workLocation, String companyName,
                            String salary, String totalMonths, String companyImgUrl,
                            String partFullTime, String key, String jobOrIntern, String roleResponsibility,
                            String compWeb, String openingNum, String skill, String compLongDes, String formLink,
                            String appStatus) {
        this.courseName = courseName;
        this.workLocation = workLocation;
        this.companyName = companyName;
        this.salary = salary;
        this.totalMonths = totalMonths;
        this.companyImgUrl = companyImgUrl;
        this.partFullTime = partFullTime;
        this.key = key;
        this.jobOrIntern = jobOrIntern;
        this.roleResponsibility = roleResponsibility;
        this.compWeb = compWeb;
        this.openingNum = openingNum;
        this.skill = skill;
        this.compLongDes = compLongDes;
        this.formLink = formLink;
        this.appStatus = appStatus;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getTotalMonths() {
        return totalMonths;
    }

    public void setTotalMonths(String totalMonths) {
        this.totalMonths = totalMonths;
    }

    public String getCompanyImgUrl() {
        return companyImgUrl;
    }

    public void setCompanyImgUrl(String companyImgUrl) {
        this.companyImgUrl = companyImgUrl;
    }

    public String getPartFullTime() {
        return partFullTime;
    }

    public void setPartFullTime(String partFullTime) {
        this.partFullTime = partFullTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getJobOrIntern() {
        return jobOrIntern;
    }

    public void setJobOrIntern(String jobOrIntern) {
        this.jobOrIntern = jobOrIntern;
    }

    public String getCompLongDes() {
        return compLongDes;
    }

    public void setCompLongDes(String compLongDes) {
        this.compLongDes = compLongDes;
    }

    public String getCompWeb() {
        return compWeb;
    }

    public void setCompWeb(String compWeb) {
        this.compWeb = compWeb;
    }

    public String getInternRole() {
        return internRole;
    }

    public void setInternRole(String internRole) {
        this.internRole = internRole;
    }

    public String getOpeningNum() {
        return openingNum;
    }

    public void setOpeningNum(String openingNum) {
        this.openingNum = openingNum;
    }

    public String getPerk() {
        return perk;
    }

    public void setPerk(String perk) {
        this.perk = perk;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getRoleResponsibility() {
        return roleResponsibility;
    }

    public void setRoleResponsibility(String roleResponsibility) {
        this.roleResponsibility = roleResponsibility;
    }

    public String getFormLink() {
        return formLink;
    }

    public void setFormLink(String formLink) {
        this.formLink = formLink;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getTotalApplicants() {
        return totalApplicants;
    }

    public void setTotalApplicants(String totalApplicants) {
        this.totalApplicants = totalApplicants;
    }
}
