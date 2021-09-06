package com.example.trvia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trvia.controller.AppController;
import com.example.trvia.model.Question;

import org.json.JSONException;

import java.util.ArrayList;

public class Repository {

    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    private ArrayList<Question> questionArrayList = new ArrayList<>();

    public ArrayList<Question> getQuestions(final ArrayListAsyncResponse callback) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                url, null,
                response -> {
                        for(int i = 0; i < 15; i++) {
                            try{
                                Question question = new Question(
                                        response.getJSONArray(i).get(0).toString(),
                                        response.getJSONArray(i).getBoolean(1));
                                questionArrayList.add(question);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    if(callback != null)
                        callback.processFinished(questionArrayList);
                },
                error -> Log.d("Error Occurred", "There was a problem"));

        AppController.getInstance()
                .addRequest(request);

        return questionArrayList;
    }

}
