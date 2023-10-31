package com.example.ueeapp;

public class Feedback {
    String name;
    String email;
    String rate;
    String task;
    String suggestion;

    public Feedback(String name, String email, String rate, String task, String suggestion) {
        this.name = name;
        this.email = email;
        this.rate = rate;
        this.task = task;
        this.suggestion = suggestion;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRate() {
        return rate;
    }

    public String getTask() {
        return task;
    }

    public String getSuggestion() {
        return suggestion;
    }
}
