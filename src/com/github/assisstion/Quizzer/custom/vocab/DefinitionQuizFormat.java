package com.github.assisstion.Quizzer.custom.vocab;

import java.util.Collections;
import java.util.List;

import com.github.assisstion.Quizzer.system.Question;
import com.github.assisstion.Quizzer.system.QuestionFactory;
import com.github.assisstion.Quizzer.system.Quiz;

public class DefinitionQuizFormat implements CustomQuestionFormat{

	private int id = 0;

	public synchronized int getIDAndIncrement(){
		return id++;
	}

	@Override
	public Question getQuestion(List<String> input, Quiz quiz){
		if(v(input.get(0)) || v(input.get(2))){
			System.out.println("Loading Problem; Code: 211");
			return null;
		}
		String query = "What is the definition of " + input.get(0) + "?";
		String answer = input.get(2);
		String hint = input.get(3);
		String info = "Synonyms: " + input.get(4);
		return QuestionFactory.generateQuestion(getIDAndIncrement(), 1, hint, query, Collections.singleton(answer), info, input.get(0));
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
