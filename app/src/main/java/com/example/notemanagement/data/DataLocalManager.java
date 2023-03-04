package com.example.notemanagement.data;

import android.content.Context;

import java.util.Set;

public class DataLocalManager {
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    private static final String FIRSTNAME = "FIRSTNAME";
    private static final String LASTNAME = "LASTNAME";
    private static final String CATEGORY_NAME = "DETAIL_NAME";
    private static final String PRIORITY_NAME = "PRIORITY_NAME";
    private static final String STATUS_NAME = "STATUS_NAME";
    private static final String NOTE_NAME = "NOTE_NAME";
    private static final String PLAN_DATE = "PLAN_DATE";
    private static final String CHECK_REMEMBER = "CHECK_REMEMBER";
    private static final String CHECK_EDIT = "CHECK_EDIT";

    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context) {
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static DataLocalManager getInstance() {
        if (instance == null) {
            instance = new DataLocalManager();
        }
        return instance;
    }

    public static void setFirstName(String value) {
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(FIRSTNAME, value);
    }

    public static void setLastname(String value) {
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(LASTNAME, value);
    }

    public static void setCategoryName(String value) {
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(CATEGORY_NAME, value);
    }

    public static void setPriorityName(String value) {
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(PRIORITY_NAME, value);
    }

    public static void setStatusName(String value) {
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(STATUS_NAME, value);
    }

    public static void setNoteName(String value) {
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(NOTE_NAME, value);
    }

    public static void setPlanDate(String value) {
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(PLAN_DATE, value);
    }

    public static void setEmail(String value) {
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(EMAIL, value);
    }

    public static void setPassword(String value) {
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(PASSWORD, value);
    }

    public static void setCheckRemember(boolean value) {
        DataLocalManager.getInstance().mySharedPreferences.putBoolean(CHECK_REMEMBER, value);
    }

    public static void setCheckEdit(boolean value) {
        DataLocalManager.getInstance().mySharedPreferences.putBoolean(CHECK_EDIT, value);
    }

    public static String getFirstname() {
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(FIRSTNAME);
    }

    public static String getLastname() {
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(LASTNAME);
    }

    public static String getCategoryName() {
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(CATEGORY_NAME);
    }

    public static String getPriorityName() {
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(PRIORITY_NAME);
    }

    public static String getStatusName() {
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(STATUS_NAME);
    }

    public static String getNoteName() {
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(NOTE_NAME);
    }

    public static String getPlanDate() {
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(PLAN_DATE);
    }

    public static String getEmail() {
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(EMAIL);
    }

    public static String getPassword() {
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(PASSWORD);
    }

    public static boolean getCheckRemember() {
        return DataLocalManager.getInstance().mySharedPreferences.getBooleanValue(CHECK_REMEMBER);
    }

    public static boolean getCheckEdit() {
        return DataLocalManager.getInstance().mySharedPreferences.getBooleanValue(CHECK_EDIT);
    }
}