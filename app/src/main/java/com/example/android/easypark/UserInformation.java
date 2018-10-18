package com.example.android.easypark;

public class UserInformation
{
    private String email;
    private String password;
    private String name;
    private String mobileNumber;
    private String vehicleNumber;

    public UserInformation(String email, String password, String mobileNumber, String vehicleNumber, String name) {
        this.email = email;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.vehicleNumber = vehicleNumber;
        this.name = name;
    }

    public UserInformation(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
