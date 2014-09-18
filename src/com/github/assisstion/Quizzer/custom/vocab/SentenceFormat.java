package com.github.assisstion.Quizzer.custom.vocab;

import java.util.Collections;
import java.util.List;

import com.github.assisstion.Quizzer.system.Question;
import com.github.assisstion.Quizzer.system.QuestionFactory;
import com.github.assisstion.Quizzer.system.Quiz;

public class SentenceFormat implements CustomQuestionFormat{

	private int id = 0;

	public synchronized int getIDAndIncrement(){
		return id++;
	}

	@Override
	public Question getQuestion(List<String> input, Quiz quiz){
		if(v(input.get(3))){
			return null;
		}
		if(v(input.get(0))){
			System.out.println("Loading Problem; Code: 212");
			return null;
		}
		String query = "What sentence fits the word " + input.get(0) + "?";
		String answer = answerize(input.get(3), input.get(0));
		String hint = input.get(4);
		String info = "Definition: " + input.get(2);
		return QuestionFactory.generateQuestion(getIDAndIncrement(), 5, hint, query, Collections.singleton(answer), info, input.get(0));
	}

	private String answerize(String string, String ans){
		return string.replaceAll(ans, "%");
	}

	protected static boolean v(String s){
		return s == null || s.equals("");
	}

	@Override
	public int getPartSize(){
		return 6;
	}

	@Override
	public boolean showInfo(){
		return false;
	}

	@Override
	public boolean showHint(){
		return false;
	}

}
