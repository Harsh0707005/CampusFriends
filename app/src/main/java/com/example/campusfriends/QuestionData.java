package com.example.campusfriends;

import java.util.List;

public class QuestionData {
    private String question;
    private List<String> options;
    private String correctOption;

    public QuestionData(String question, List<String> options, String correctOption) {
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectOption() {
        return correctOption;
    }
}

