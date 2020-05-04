package com.exa.ci6222quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    //private static final int REQUEST_CODE_QUIZ=2;
    public static final String RECORD_SCORE="recordScore";
    public static final String EXTRA_DIFFICULTY= "extraDifficulty";
    private static final long COUNTDOWN_IN_MILLIS=30000; //3s

    private static final String KEY_SCORE="keyScore";
    private static final String KEY_QUESTION_COUNT="keyQuestionCount";
    private static final String KEY_MILLIS_LEFT="keyMillisLeft";
    private static final String KEY_ANSWERED="keyAnswered";
    private static final String KEY_QUESTION_LIST="keyQuestionList";

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewDifficulty;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private String difficulty;
    private boolean answered;

    private long backPressTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion=findViewById(R.id.text_view_question);
        textViewScore=findViewById(R.id.text_view_score);
        textViewQuestionCount=findViewById(R.id.text_view_question_count);
        textViewDifficulty=findViewById(R.id.text_view_difficulty);
        textViewCountDown=findViewById(R.id.text_view_countdown);
        rbGroup=findViewById(R.id.radio_group);
        rb1=findViewById(R.id.radio_button1);
        rb2=findViewById(R.id.radio_button2);
        rb3=findViewById(R.id.radio_button3);
        buttonConfirmNext=findViewById(R.id.button_confirm_next);

        textColorDefaultRb=rb1.getTextColors();
        textColorDefaultCd=textViewCountDown.getTextColors();

        //Intent intent=getIntent();
        //String difficulty=intent.getStringExtra(StartScreenActivity.EXTRA_DIFFICULTY);
        Bundle extras= getIntent().getExtras();
        if(extras==null){
            return;
        }
        difficulty=extras.getString(StartScreenActivity.EXTRA_DIFFICULTY);
        if(difficulty==null){
            difficulty=extras.getString(FinishScreenActivity.EXTRA_DIFFICULTY);
        }else {
            difficulty=extras.getString((StartScreenActivity.EXTRA_DIFFICULTY));
        }

        textViewDifficulty.setText("Difficulty: " +difficulty);

        if(savedInstanceState==null) {
            QuizDbHelper dbHelper = new QuizDbHelper(this);
            questionList = dbHelper.getQuestions(difficulty); //first time call create database
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();
        }else{
            questionList=savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            if(questionList==null){
                finish();
            }
            questionCountTotal=questionList.size();
            questionCounter=savedInstanceState.getInt(KEY_QUESTION_COUNT); //how many questions we have answered
            currentQuestion=questionList.get(questionCounter-1); //counter is 1 ahead than questionList which index start with 0
            score=savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis=savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered=savedInstanceState.getBoolean(KEY_ANSWERED);

            if(!answered) {  //question not answer yet
                startCountDown();
            }else{
                updateCountDownText();
                showSolution();
            }

        }

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(!answered) {  //not answer yet false
                 if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked()){
                     checkAnswer();
                 }else {
                     Toast.makeText(QuizActivity.this,"Please select an answer",Toast.LENGTH_SHORT).show();
                 }

             } else {  //already answer
                 showNextQuestion();
             }
            }
        });
    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if(questionCounter < questionCountTotal) {
            currentQuestion=questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            questionCounter++;
            textViewQuestionCount.setText("Question"+ questionCounter + "/" + questionCountTotal);
            answered=false;
            buttonConfirmNext.setText("Confrim");

            timeLeftInMillis=COUNTDOWN_IN_MILLIS;
            startCountDown();
        }else {
            finishQuiz();
        }
    }

    private void startCountDown(){
        countDownTimer=new CountDownTimer(timeLeftInMillis,1000) { //tick after 1 s
            @Override
            public void onTick(long milliUntilFinished) {
                timeLeftInMillis=milliUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
              timeLeftInMillis=0;
              updateCountDownText(); //show 0 when timer finish
                 checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText(){
        int minutes=(int)(timeLeftInMillis/1000)/60;
        int seconds=(int)(timeLeftInMillis/1000)%60;

        String timeFormat=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        textViewCountDown.setText(timeFormat);
        if(timeLeftInMillis<10000) {
            textViewCountDown.setTextColor(Color.RED);
        }else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
        answered=true;

        countDownTimer.cancel();
        RadioButton rbselected=findViewById(rbGroup.getCheckedRadioButtonId()); //get selected radiobutton id
        int answerNr=rbGroup.indexOfChild(rbselected)+1; //index start with 0 but answerNr start with 1

        if(answerNr==currentQuestion.getAnswerNr()) {
            score++;
            textViewScore.setText("Score "+ score);
            showNextQuestion();
            changeButtonComfirmStatus();
        } else {
            showSolution();
        }



    }

    public void showSolution(){

        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);




        switch(currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is correct");

        }

            changeButtonComfirmStatus();

    }

    public void changeButtonComfirmStatus(){
        if (questionCounter<questionCountTotal){
            buttonConfirmNext.setText("Next");
        } else {
            buttonConfirmNext.setText("Finish");
        }
    }

    public void finishQuiz(){
        int finalScore=score;
        String finalDifficulty= difficulty;
        Intent finishIntent=new Intent(this,FinishScreenActivity.class);
        finishIntent.putExtra(RECORD_SCORE,finalScore);
        finishIntent.putExtra(EXTRA_DIFFICULTY,finalDifficulty);
        setResult(RESULT_OK,finishIntent);
        startActivity(finishIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(backPressTime +2000 > System.currentTimeMillis()){ //within 2s to control click back rather than button
            finishQuiz();
        }else {
            Toast.makeText(this,"Press back again to finish",Toast.LENGTH_SHORT).show();
        }
         backPressTime= System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {  //to cancel countdown timer when activity finish otherwise it will run in background
        super.onDestroy();
        if(countDownTimer !=null) {  //to prevent crash when timer doesn't start yet
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE,score);
        outState.putInt(KEY_QUESTION_COUNT,questionCounter);
        outState.putLong(KEY_MILLIS_LEFT,timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED,answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST,questionList);
    }
}
