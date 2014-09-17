package com.github.assisstion.Quizzer.system;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class QuestionFactory{

	private static int n1;
	private static int m1;
	private static String h1;
	private static String q1;
	private static Set<String> a1;
	private static String i1;
	private static String s1;

	private QuestionFactory(){

	}

	public static Question generateQuestion(Collection<String> answers, int id, int mode, String hint,
			String query, Set<String> answer, String info, String shortForm){
		Question q = generateQuestion(id, mode, hint, query, answer, info, shortForm);
		for(String s : answer){
			answers.add(s);
		}
		return q;
	}

	public static Question generateQuestion(Map<Integer, Question> questionMap, Collection<String> answers,
			int id, int mode, String hint, String query, Set<String> answer, String info, String shortForm){
		Question q = generateQuestion(answers, id, mode, hint, query, answer, info, shortForm);
		questionMap.put(id, q);
		return q;
	}

	public static Question generateQuestion(int id, int mode, String hint, String query, Set<String> answer, String info, String shortForm){
		n1 = id;
		m1 = mode;
		h1 = hint;
		q1 = query;
		a1 = answer;
		i1 = info;
		s1 = shortForm;
		Question q = new Question(){

			private int n = n1;
			private int m = m1;
			private String h = h1;
			private String q = q1;
			private Set<String> a = a1;
			private String i = i1;
			private String s = s1;

			@Override
			public String getHint(){
				return h;
			}

			@Override
			public String getQuery(){
				return q;
			}

			@Override
			public Set<String> getAnswer(){
				return a;
			}

			@Override
			public String getInfo(){
				return i;
			}

			@Override
			public int getMode(){
				return m;
			}

			@Override
			public String getShortForm(){
				return s;
			}

			@Override
			public String toString(){
				return "Hint: " + h + "; Query: " + q + "; Answer: " + a + "; Info: " + i + ";";
			}

			@Override
			public int getID(){
				return n;
			}

			@Override
			public int hashCode(){
				return getID();
			}

		};
		return q;
	}

}
