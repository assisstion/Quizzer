package com.github.assisstion.Quizzer.custom.vocab;

import java.util.HashSet;
import java.util.List;

import com.github.assisstion.Quizzer.system.Question;
import com.github.assisstion.Quizzer.system.QuestionFactory;
import com.github.assisstion.Quizzer.system.Quiz;

public class SingleSynonymQuizFormat implements CustomQuestionFormat{

	private int id = 0;

	public synchronized int getIDAndIncrement(){
		return id++;
	}

	@Override
	public Question getQuestion(List<String> input, Quiz quiz){
		if(v(input.get(4))){
			return null;
		}
		if(v(input.get(0))){
			System.out.println("Loading Problem; Code: 212");
			return null;
		}
		String query = "What is a synonym of " + input.get(0) + "?";
		String answer = input.get(4);
		String[] answerParts = answer.split(",");
		HashSet<String> realAnswer = new HashSet<String>();
		for(String s : answerParts){
			String sa = CustomQuiz.cleanup(s).toLowerCase();
			realAnswer.add(sa);
		}
		String hint = input.get(3);
		String info = "Definition: " + input.get(2);
		return QuestionFactory.generateQuestion(getIDAndIncrement(), 2, hint, query, realAnswer, info, input.get(0));
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
		return true;
	}

}
