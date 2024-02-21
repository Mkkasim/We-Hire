package com.mk.wehire.User.sessionmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class SessionManagement {
    private static SharedPreferences sharedPreferences;

    public SessionManagement() {
    }

    public static void init(Context context){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences(SessionConstants.SESSION_NAME, Context.MODE_PRIVATE);
        }
    }

    public static boolean isLogged(){
        return sharedPreferences.getBoolean(SessionConstants.IS_LOGGED_IN, false);

    }

    public static void setLogin(boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SessionConstants.IS_LOGGED_IN, value);
        editor.apply();
    }

    public static void saveUserToken(String token){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.USER_TOKEN,token);
        editor.apply();
    }

    public static String  getUserToken(){

        return sharedPreferences.getString(SessionConstants.USER_TOKEN,"");

    }



    public static void saveUserNum(String number){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.USER_NUMBER,number);
        editor.apply();
    }

    public static String  getNumber(){

        return sharedPreferences.getString(SessionConstants.USER_NUMBER,"");

    }
    public static void saveUserEmail(String email){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.USER_EMAIL,email);
        editor.apply();
    }

    public static String  getEmail(){

        return sharedPreferences.getString(SessionConstants.USER_EMAIL,"");

    }
    public static void saveUserPass(String pass){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.USER_PASS,pass);
        editor.apply();
    }

    public static String  getPass(){
        return sharedPreferences.getString(SessionConstants.USER_PASS,"");
    }

    public static void saveUserFirstName(String firstName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.USER_FIRST_NAME,firstName);
        editor.apply();
    }

    public static String  getFirstName(){
        return sharedPreferences.getString(SessionConstants.USER_FIRST_NAME,"");
    }
    public static void saveUserLastName(String lastName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.USER_LAST_NAME,lastName);
        editor.apply();
    }

    public static String  getLastName(){
        return sharedPreferences.getString(SessionConstants.USER_LAST_NAME,"");
    }
    public static void saveUserProfileImage(Uri image){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.USER_PROFILE_IMAGE, String.valueOf(image));
        editor.apply();
    }

    public static String  getUserProfileImage(){
        return sharedPreferences.getString(SessionConstants.USER_PROFILE_IMAGE,"");
    }

    public static void saveUserLoginID(String number){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.USER_LOGIN_ID,number);
        editor.apply();
    }

    public static String  getUserLoginID(){
        return sharedPreferences.getString(SessionConstants.USER_LOGIN_ID,"");
    }

    public static boolean isPrefChecked(){
        return sharedPreferences.getBoolean(SessionConstants.CHECK_PREFERENCES, false);

    }

    public static void setCheckPref(boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SessionConstants.CHECK_PREFERENCES, value);
        editor.apply();
    }

    public static boolean isWorkHomeChecked(){
        return sharedPreferences.getBoolean(SessionConstants.CHECK_WORK_FROM_HOME, false);

    }

    public static void setCheckWorkHome(boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SessionConstants.CHECK_WORK_FROM_HOME, value);
        editor.apply();
    }

    public static void saveCheckWorkHome(String workhome){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.WORK_HOME,workhome);
        editor.apply();
    }

    public static String  getCheckWorkHome(){
        return sharedPreferences.getString(SessionConstants.WORK_HOME,"");
    }

    public static boolean isPartTimeChecked(){
        return sharedPreferences.getBoolean(SessionConstants.CHECK_PART_TIME, false);

    }

    public static void setCheckPartTime(boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SessionConstants.CHECK_PART_TIME, value);
        editor.apply();
    }

    public static void saveCheckPart(String part){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.PART_TIME,part);
        editor.apply();
    }

    public static String  getCheckPart(){
        return sharedPreferences.getString(SessionConstants.PART_TIME,"");
    }

    public static void saveMinimumStipend(String stipend){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.SAVE_STIPEND,stipend);
        editor.apply();
    }

    public static String  getMiniumStipend(){
        return sharedPreferences.getString(SessionConstants.SAVE_STIPEND,"");
    }

    public static void saveMaximumDuration(String duration){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.SAVE_DURATION,duration);
        editor.apply();
    }

    public static String  getMaximumDuration(){
        return sharedPreferences.getString(SessionConstants.SAVE_DURATION,"");
    }

    public static void saveUserLocation(String location){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.USER_LOCATION,location);
        editor.apply();
    }

    public static String  getUserLocation(){
        return sharedPreferences.getString(SessionConstants.USER_LOCATION,"");
    }

    public static void saveGender(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.USER_GENDER,gender);
        editor.apply();
    }

    public static String  getGender(){

        return sharedPreferences.getString(SessionConstants.USER_GENDER,"");

    }
    public static void saveLanguage(String language){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.USER_LANGUAGE,language);
        editor.apply();
    }

    public static String  getLanguage(){

        return sharedPreferences.getString(SessionConstants.USER_LANGUAGE,"");

    }

    public static void saveDegreeName(String degree){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.DEGREE_NAME,degree);
        editor.apply();
    }

    public static String  getDegreeName(){

        return sharedPreferences.getString(SessionConstants.DEGREE_NAME,"");

    }




    //companys
    public static boolean isCompLogged(){
        return sharedPreferences.getBoolean(SessionConstants.IS_COMP_LOGGED, false);

    }

    public static void setCompLogin(boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SessionConstants.IS_COMP_LOGGED, value);
        editor.apply();
    }

    public static void saveCompanyName(String companyName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.COMPANY_NAME,companyName);
        editor.apply();
    }

    public static String  getCompanyName(){
        return sharedPreferences.getString(SessionConstants.COMPANY_NAME,"");
    }

    public static void saveJobPosition(String jobPosition){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.JOB_POSITION,jobPosition);
        editor.apply();
    }

    public static String  getJobPosition(){
        return sharedPreferences.getString(SessionConstants.JOB_POSITION,"");
    }

    public static void saveCompanyShortName(String shortName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.COMPANY_SHORT_NAME,shortName);
        editor.apply();
    }

    public static String  getCompanyShortName(){
        return sharedPreferences.getString(SessionConstants.COMPANY_SHORT_NAME,"");
    }

    public static void saveCompLocation(String compLocation){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.COMPANY_LOCATION,compLocation);
        editor.apply();
    }

    public static String  getCompLocation(){
        return sharedPreferences.getString(SessionConstants.COMPANY_LOCATION,"");
    }

    public static void saveCompIndustry(String compIndustry){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.COMPANY_INDUSTRY,compIndustry);
        editor.apply();
    }

    public static String  getCompIndustry(){
        return sharedPreferences.getString(SessionConstants.COMPANY_INDUSTRY,"");
    }

    public static void saveCompSize(String compSize){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.COMPANY_SIZE,compSize);
        editor.apply();
    }

    public static String  getCompSize(){
        return sharedPreferences.getString(SessionConstants.COMPANY_SIZE,"");
    }

    public static void saveCompWeb(String compWeb){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.COMPANY_WEBSITE,compWeb);
        editor.apply();
    }

    public static String  getCompWeb(){
        return sharedPreferences.getString(SessionConstants.COMPANY_WEBSITE,"");
    }

    public static void saveCompAbout(String compAbout){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.COMPANY_ABOUT,compAbout);
        editor.apply();
    }

    public static String  getCompAbout(){
        return sharedPreferences.getString(SessionConstants.COMPANY_ABOUT,"");
    }

    public static void saveCompanyProfileImage(Uri image){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.COMPANY_IMAGE_URL, String.valueOf(image));
        editor.apply();
    }

    public static String  getCompanyProfileImage(){
        return sharedPreferences.getString(SessionConstants.COMPANY_IMAGE_URL,"");
    }

    public static boolean isCertificateChecked(){
        return sharedPreferences.getBoolean(SessionConstants.IS_PERK_CERTIFICATE, false);

    }

    public static void setCertificateChecked(boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SessionConstants.IS_PERK_CERTIFICATE, value);
        editor.apply();
    }

    public static boolean isLetterRecChecked(){
        return sharedPreferences.getBoolean(SessionConstants.IS_PERK_LETTEROFREC, false);

    }

    public static void setLetterRecChecked(boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SessionConstants.IS_PERK_LETTEROFREC, value);
        editor.apply();
    }

    public static boolean isFlexWorkChecked(){
        return sharedPreferences.getBoolean(SessionConstants.IS_PERK_FLEXWORK, false);

    }

    public static void setFlexWorkChecked(boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SessionConstants.IS_PERK_FLEXWORK, value);
        editor.apply();
    }

    public static boolean isDaysWeekChecked(){
        return sharedPreferences.getBoolean(SessionConstants.IS_PERK_DAYSWEEK, false);

    }

    public static void setDaysWeekChecked(boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SessionConstants.IS_PERK_DAYSWEEK, value);
        editor.apply();
    }
    public static void saveKey(String key){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SessionConstants.TEMP_KEY,key);
        editor.apply();
    }

    public static String  getKey(){

        return sharedPreferences.getString(SessionConstants.TEMP_KEY,"");

    }

}
