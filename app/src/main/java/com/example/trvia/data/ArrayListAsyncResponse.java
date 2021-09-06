package com.example.trvia.data;

import com.example.trvia.model.Question;

import java.util.ArrayList;

public interface ArrayListAsyncResponse {
    public void processFinished(ArrayList<Question> questionList);
}
