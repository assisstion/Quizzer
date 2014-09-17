package com.github.assisstion.Quizzer.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Quiz{
	protected Map<Integer, Question> questionMap = new HashMap<Integer, Question>();
	protected List<ArrayList<String>> answerList = new LinkedList<ArrayList<String>>();

	public abstract void load(String location);


	public Map<Integer, Question> getQuestionMap(){
		return questionMap;
	}
	public List<ArrayList<String>> getAnswerList(){
		return answerList;
	}

	public abstract boolean showHint();
	public abstract boolean showInfo();
}
