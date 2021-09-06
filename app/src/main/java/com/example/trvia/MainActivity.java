package com.example.trvia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.trvia.data.Repository;
import com.example.trvia.databinding.ActivityMainBinding;
import com.example.trvia.model.Question;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String MSG_KEY = "msg_key";
    ActivityMainBinding bind;
    int currentQuestionIndex;
    ArrayList<Question> questions;
    int correctAnswers;
    boolean gameActive = true;
    boolean lastQuestion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main);

        int lastScore = getLastScore();
        String result;
        if(lastScore == -1)
            result = "";
        else
            result ="Last Score : " + lastScore + "/15";
        bind.resultText.setText(result);

        questions = new Repository().getQuestions( questionList -> {
            Log.d("Answers : ", questionList.toString());
            bind.quesView.setText(questionList.get(currentQuestionIndex++).getAnswer());
            String text = "Questions : " + currentQuestionIndex + "/" + questionList.size();
            bind.questionsCount.setText(text);
        });



        bind.nextButton.setOnClickListener( v -> {
            if(gameActive)
                showNextQues();
            else{
                reStartGame();
            }
        });

        bind.trueButton.setOnClickListener( v -> {
            if(!lastQuestion){
                if(questions.get(currentQuestionIndex - 1).getAnswerTrue())
                    correctAnswers++;
                else
                    shakeAnimation();
                showNextQues();
            }
        });

        bind.falseButton.setOnClickListener( v -> {
            if(!lastQuestion){
                if(!questions.get(currentQuestionIndex - 1).getAnswerTrue())
                    correctAnswers++;
                else
                    shakeAnimation();
                showNextQues();
            }
        });

    }

    private void reStartGame() {
        gameActive = true;
        lastQuestion = false;
        currentQuestionIndex = 0;
        bind.quesView.setText(questions.get(currentQuestionIndex++).getAnswer());
        String text = "Questions : " + currentQuestionIndex + "/" + questions.size();
        bind.questionsCount.setText(text);
    }

    public void showNextQues(){
        if(currentQuestionIndex < 15){
            bind.quesView.setText(questions.get(currentQuestionIndex).getAnswer());
            String text = "Questions : " + (currentQuestionIndex + 1) + "/" + questions.size();
            bind.questionsCount.setText(text);
            bind.resultText.setVisibility(View.INVISIBLE);
        }
        if (currentQuestionIndex == 14){
            bind.nextButton.setText(R.string.end_text);
            gameActive = false;
        }
        else if(currentQuestionIndex == 15){
            gameActive = false;
            lastQuestion = true;
            showFinalScore();
        }
        currentQuestionIndex++;
    }

    public void showFinalScore(){
        saveData(correctAnswers);
        bind.resultText.setVisibility(View.VISIBLE);
        String result = "Final score : " + correctAnswers + "/15";
        bind.resultText.setText(result);
        correctAnswers = 0;
    }

    public void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim);
        bind.cardView.setAnimation(shake);
    }

    public void saveData(int correctAnswers){
        SharedPreferences sharedPreferences = getSharedPreferences(MSG_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Score", correctAnswers);
        editor.apply();
    }

    public int getLastScore(){
        SharedPreferences getScore = getSharedPreferences(MSG_KEY, MODE_PRIVATE);
        return getScore.getInt("Score", -1);
    }
}