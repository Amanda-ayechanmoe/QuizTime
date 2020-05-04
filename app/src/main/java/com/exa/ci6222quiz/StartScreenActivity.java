package com.exa.ci6222quiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class StartScreenActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QUIZ=4;
    public static final String EXTRA_DIFFICULTY= "extraDifficulty";

    public static final String SHARED_PREFS="sharedPrefs";
    public static final String KEY_HIGHSCORE="keyHighScore";

    private TextView textViewHighScore;
    private Spinner spinnerDifficulty;
    private TextView welcomeText;

    private int highscore;

    ImageView questionGirl;
    Animation frombottom;
    Animation fromtop;
    Animation fromright;
    Animation fromleft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startingscreen);

        textViewHighScore=findViewById(R.id.text_view_highscore);
        spinnerDifficulty=findViewById(R.id.spinner_difficulty);
        welcomeText=findViewById(R.id.welcomeText);



        String[] difficultyLevels=Question.getAllDifficultyLevels();    //spinner
        ArrayAdapter<String> adapterDifficulty=new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,difficultyLevels) {
            @Override
            public boolean isEnabled(int position){
                if(highscore>=1 && position==1) {
                    return true; //medium
                } else if (position==2 && highscore >=3) {
                    return true; //hard
                }else if (position==0){
                    return true;   //easy
                }
                    return false;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(highscore>=1 && position==1) {
                    // Set the disable item text color
                    tv.setTextColor(Color.WHITE);
                }  else if (position==2 && highscore >=3) {
                    tv.setTextColor(Color.WHITE);
                } else if (position==0){
                    tv.setTextColor(Color.WHITE);
                }
                else {
                    tv.setTextColor(Color.GRAY);
                }
                return view;
            }

        };

        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);

        loadHighscore();
        Intent intent=getIntent();
        int score=intent.getIntExtra(FinishScreenActivity.RECORD_SCORE,0);
        if(score>highscore) {
            updateHighScore(score);
        }

        Button buttonStartQuiz=findViewById(R.id.StartButton);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuiz();
            }
        });

        questionGirl=(ImageView)findViewById(R.id.quizGirl);
        fromtop=AnimationUtils.loadAnimation(this,R.anim.fromtop);
        questionGirl.setAnimation(fromtop);
        frombottom= AnimationUtils.loadAnimation(this,R.anim.frombottom);
        buttonStartQuiz.setAnimation(frombottom);
        fromright=AnimationUtils.loadAnimation(this,R.anim.fromright);
        textViewHighScore.setAnimation(fromright);
        spinnerDifficulty.setAnimation(fromright);
        fromleft=AnimationUtils.loadAnimation(this,R.anim.fromleft);
        welcomeText.setAnimation(fromleft);
    }
    private void startQuiz() {
        String difficulty=spinnerDifficulty.getSelectedItem().toString();

        Intent intent=new Intent(this,QuizActivity.class);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        startActivityForResult(intent,REQUEST_CODE_QUIZ);
    }



   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode==RESULT_OK) {
                int score=data.getIntExtra(FinishScreenActivity.RECORD_SCORE,0);

                if(score>highscore) {
                    updateHighScore(score);
                }
            }
        }
    }*/

    private void loadHighscore() {
        SharedPreferences prefs=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        highscore=prefs.getInt(KEY_HIGHSCORE,0);
        textViewHighScore.setText("Highscore: " + highscore);  //to load highscore when launch app
    }

    private void updateHighScore(int newHighScore) {
        highscore=newHighScore;
        textViewHighScore.setText("Highscore: " + highscore);


        SharedPreferences prefs=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit(); //editor use to save
        editor.putInt(KEY_HIGHSCORE,highscore);
        editor.apply(); //when load app highscore need to load so use share preferences to save
    }
}
