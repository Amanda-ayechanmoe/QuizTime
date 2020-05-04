package com.exa.ci6222quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FinishScreenActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_QUIZ=3;
    public static final int REQUEST_CODE_START=4;
    public static final String RECORD_SCORE="recordScore";
    public static final String EXTRA_DIFFICULTY= "extraDifficulty";
    private int score;
    private String difficulty;
    private TextView Score;
    private Button ButtonTryAgain;
    private Button ButtonOkay;
    ImageView trophy;
    Animation frombottom;
    Animation fromtop;
    Animation fromright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_screen);
        Score=findViewById(R.id.text_view_finish_score);
        Intent intent=getIntent();
        score=intent.getIntExtra(QuizActivity.RECORD_SCORE,0);
        Bundle extras=getIntent().getExtras();
        if(extras==null){
            return;
        }
        //score=extras.getInt(QuizActivity.RECORD_SCORE);
        Score.setText("Score "+score);
        difficulty=extras.getString(QuizActivity.EXTRA_DIFFICULTY);
        ButtonTryAgain=findViewById(R.id.tryAgainButton);
        trophy=findViewById(R.id.trophy);
        fromtop= AnimationUtils.loadAnimation(this,R.anim.fromtop);
        trophy.setAnimation(fromtop);
        frombottom=AnimationUtils.loadAnimation(this,R.anim.frombottom);
        ButtonTryAgain.setAnimation(frombottom);
        ButtonOkay=findViewById(R.id.OkButton);
        ButtonOkay.setAnimation(frombottom);
        fromright=AnimationUtils.loadAnimation(this,R.anim.fromright);
        Score.setAnimation(fromright);

        ButtonTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FinishScreenActivity.this,QuizActivity.class);
                intent.putExtra(EXTRA_DIFFICULTY,difficulty);
                setResult(RESULT_OK,intent);
                startActivityForResult(intent,REQUEST_CODE_QUIZ);
                finish();

            }
        });

        ButtonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent=new Intent(FinishScreenActivity.this,StartScreenActivity.class);
                resultIntent.putExtra(RECORD_SCORE,score);
                setResult(RESULT_OK,resultIntent);
                startActivityForResult(resultIntent,REQUEST_CODE_START);
                finish();
            }
        });

    }
}
