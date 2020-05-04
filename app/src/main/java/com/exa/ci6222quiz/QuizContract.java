package com.exa.ci6222quiz;

import android.provider.BaseColumns;

public final class QuizContract {

    private QuizContract(){} //to make sure this class is a container for the below class

    public static class QuestionsTable implements BaseColumns {
           public static final String TABLE_NAME="quiz_questions";
           public static final String COLUMN_QUESTION="question";
           public static final String COLUMN_OPTION1="option1";
           public static final String COLUMN_OPTION2="option2";
           public static final String COLUMN_OPTION3="option3";
           public static final String COLUMN_ANSWER_NR="answer_nr";
        public static final String COLUMN_DIFFICUlTY="difficulty";

    }
}
