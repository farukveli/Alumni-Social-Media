package com.example.app_2;

public class Users {

    private String name, surname, enteredYear, graduatedYear
            ,email, dataImage, degree, job, phone;
    private int numberOfPost;
    public Users(String name, String surname, String enteredYear, String graduatedYear, String email) {
        this.name = name;
        this.surname = surname;
        this.enteredYear = enteredYear;
        this.graduatedYear = graduatedYear;
        this.email = email;
    }

    public Users() {

    }

    public int getNumberOfPost() {
        return numberOfPost;
    }

    public void setNumberOfPost(int numberOfPost) {
        this.numberOfPost = numberOfPost;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEnteredYear() {
        return enteredYear;
    }

    public void setEnteredYear(String enteredYear) {
        this.enteredYear = enteredYear;
    }

    public String getGraduatedYear() {
        return graduatedYear;
    }

    public void setGraduatedYear(String graduatedYear) {
        this.graduatedYear = graduatedYear;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDataImage() {
        return dataImage;
    }

    public void setDataImage(String dataImage) {
        this.dataImage = dataImage;
    }


}
