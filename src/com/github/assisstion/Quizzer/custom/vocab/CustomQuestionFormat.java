package com.github.assisstion.Quizzer.custom.vocab;

import java.util.List;

import com.github.assisstion.Quizzer.system.Question;
import com.github.assisstion.Quizzer.system.Quiz;

public interface CustomQuestionFormat{
	Question getQuestion(List<String> input, Quiz quiz);

	int getPartSize();

	boolean showInfo();

	boolean showHint();
}
