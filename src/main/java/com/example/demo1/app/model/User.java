package com.example.demo1.app.model;
// This class represents a user in the application, encapsulating all relevant information about the user.
public class User {
    private String name;
    private String email;
    private String mobile;
    private String username;
    private String passwordHash;
    private String division;
    private String district;
    private String upazila;
    private String landAmount;
    private String soilType;
    private String profileImagePath;

    // Default no-argument constructor
    public User()
    {
    }

    // Constructor for new users (without location and land details) user first time registration in my frontend software
    public User(String name, String email, String mobile, String username, String passwordHash)
    {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // --- Getters and Setters for change data and set data ---
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPasswordHash()
    {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash)
    {
        this.passwordHash = passwordHash;
    }

    public String getDivision()
    {
        return division;
    }

    public void setDivision(String division)
    {
        this.division = division;
    }

    public String getDistrict()
    {
        return district;
    }

    public void setDistrict(String district)
    {
        this.district = district;
    }

    public String getUpazila()
    {
        return upazila;
    }

    public void setUpazila(String upazila)
    {
        this.upazila = upazila;
    }

    public String getLandAmount()
    {
        return landAmount;
    }

    public void setLandAmount(String landAmount)
    {
        this.landAmount = landAmount;
    }

    public String getSoilType()
    {
        return soilType;
    }

    public void setSoilType(String soilType)
    {
        this.soilType = soilType;
    }

    public String getProfileImagePath()
    {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath)
    {
        this.profileImagePath = profileImagePath;
    }
}


