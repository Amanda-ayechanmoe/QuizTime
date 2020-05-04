package com.exa.ci6222quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.exa.ci6222quiz.QuizContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CI6222Quiz.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICUlTY +" TEXT" +
                ")";
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionTable(); //after table create want to fill question table right away
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionTable() {
        Question q1 = new Question("Easy: One of the major activities of designing user interface is",
                "Requirement Gathering", "Testing", "Drawing", 1,Question.DIFFICULTY_EASY);
        addQuestion(q1);
        Question q2 = new Question("Medium: Which one is NOT the Major types of Prototyping",
                "High fidelity", "Medium fidelity", "Low fidelity", 2,Question.DIFFICULTY_MEDIUM);
        addQuestion(q2);
        Question q3 = new Question("Medium: Type of Layout in Android is",
                "Horizontal Layout", "Multiple Layout", "TableLayout", 3,Question.DIFFICULTY_MEDIUM);
        addQuestion(q3);
        Question q4 = new Question("Hard: In lifecycle of Activity which method is called when activity is becoming visible to the user",
                "onCreate()", "onStart()", "onStop()", 2,Question.DIFFICULTY_HARD);
        addQuestion(q4);
        Question q5 = new Question("Hard: The suitable prototype for idea generation is",
                "Scenario prototypes", "Screen prototypes", "Paper prototypes", 3,Question.DIFFICULTY_HARD);
        addQuestion(q5);
        Question q6 = new Question("Hard: The advantage of Field Studies is",
                "People won't theorize,generalize", "Explanations grounded in actual activity", "Longer periods of access", 3,Question.DIFFICULTY_HARD);
        addQuestion(q6);
        Question q7 = new Question("Easy: Specify the directory name where the XML layout file are stored",
                "/res/values", "/res/layout", "/res/drawable", 2,Question.DIFFICULTY_EASY);
        addQuestion(q7);
        Question q8 = new Question("Medium: Which way is NOT for implementation of scene transitions in Android",
                "Intent", "Transition Manager", "Router", 3,Question.DIFFICULTY_MEDIUM);
        addQuestion(q8);
        Question q9 = new Question("Easy: The foundation of Android is",
                "Microsoft", "Middleware", "Linux", 3,Question.DIFFICULTY_EASY);
        addQuestion(q9);
        Question q10 = new Question("Medium: Which one provides data sharing between applications",
                "Activity Manager", "Content Providers", "Resource Manager", 2,Question.DIFFICULTY_MEDIUM);
        addQuestion(q10);
        Question q11 = new Question("Easy: The High Priority process is",
                "Visible Process", "Empty Process", "Background Process", 1,Question.DIFFICULTY_EASY);
        addQuestion(q11);
        Question q12 = new Question("Hard: What layout positions child view in a single row or column depending on the orientation selected",
                "Frame Layout", "Table Layout", "Linear Layout", 3,Question.DIFFICULTY_HARD);
        addQuestion(q12);


    }  //Question object

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION,question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1,question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2,question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3,question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR,question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_DIFFICUlTY,question.getDifficulty());
        db.insert(QuestionsTable.TABLE_NAME,null,cv);  //no need to insert id auto increment
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList=new ArrayList<>();
        db=getReadableDatabase(); //reference the database to get data
        Cursor c=db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);
        if (c.moveToFirst()) {
          do {
            Question question=new Question();
            question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
            question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
            question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
            question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
            question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
              question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICUlTY)));
            questionList.add(question);
          }while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(String difficulty) {
        ArrayList<Question> questionList=new ArrayList<>();
        db=getReadableDatabase(); //reference the database to get data

        String[] selectionArgs= new String[]{difficulty};
        Cursor c=db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME +
                " WHERE " + QuestionsTable.COLUMN_DIFFICUlTY + " = ?", selectionArgs);
        if (c.moveToFirst()) {
            do {
                Question question=new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICUlTY)));
                questionList.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

}

