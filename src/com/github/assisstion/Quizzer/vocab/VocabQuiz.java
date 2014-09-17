package com.github.assisstion.Quizzer.vocab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import com.github.assisstion.Quizzer.system.QuestionFactory;
import com.github.assisstion.Quizzer.system.Quiz;

public class VocabQuiz extends Quiz{
	
	private static final int MAX_MODES = 3;
	
	private int mode = 0;
	
	public VocabQuiz(){
		for(int i = 0; i <= MAX_MODES; i++){
			answerList.add(new ArrayList<String>());
		}
	}
	
	public VocabQuiz(int mode){
		this();
		this.mode = mode;
	}
	
	@Override
	public void load(String location){
		int counter = 0;
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(location));
			String s = br.readLine();
			while(s != null){
				if(s.length() <= 0 || s.substring(0, 1).equals("#")){
					s = br.readLine();
					continue;
				}
				String[] strings = s.split(";", -1);
				for(int i = 0; i < strings.length; i++){
					String sx = cleanup(strings[i]);
					strings[i] = sx;
				}
				if(strings.length != 6){
					System.out.println(s);
					System.out.println("Loading Problem; Code: 201");
					s = br.readLine();
					continue;
				}
				factorize(counter++, strings);
				s = br.readLine();
			}
		}
		catch(IOException e){
			System.out.println("IO Exception Caught; Code: 010");
			e.printStackTrace();
			if(br != null){
				try{
					br.close();
				}
				catch(IOException e1){
					System.out.println("IO Exception Caught; Code: 010");
					e.printStackTrace();
				}
			}
		}
	}
	
	private void factorize(int counter, String[] strings){
		switch(mode){
			case 0:
				return;
			case 1:
				factorize1(counter, strings);
				return;
			case 2:
				factorize2(counter, strings);
				return;
			case 3:
				factorize3(counter, strings);
				return;
			default:
				System.out.println("Loading Problem; Code: 202");
				return;
		}
	}
	
	private void factorize1(int counter, String[] strings){
		if(strings[0] == null || strings[2] == null || strings[0].equals("") || strings[2].equals("")){
			System.out.println("Loading Problem; Code: 211");
			return;
		}
		String query = "What is the definition of " + strings[0] + "?";
		String answer = strings[2];
		String hint = strings[3];
		String info = "Synonyms: " + strings[4];
		QuestionFactory.generateQuestion(questionMap, answerList.get(1), counter++, 1, hint, query, Collections.singleton(answer), info, strings[0]);
	}
	
	private void factorize2(int counter, String[] strings){
		if(strings[4] == null || strings[4].equals("")){
			return;
		}
		if(strings[0] == null || strings[0].equals("")){
			System.out.println("Loading Problem; Code: 212");
			return;
		}
		String query = "What are synonyms of " + strings[0] + "?";
		String answer = strings[4];
		String hint = strings[3];
		String info = "Definition: " + strings[2];
		QuestionFactory.generateQuestion(questionMap, answerList.get(2), counter++, 2, hint, query, Collections.singleton(answer), info, strings[0]);
	}
	
	private void factorize3(int counter, String[] strings){
		if(strings[4] == null || strings[4].equals("")){
			return;
		}
		if(strings[0] == null || strings[0].equals("")){
			System.out.println("Loading Problem; Code: 212");
			return;
		}
		String query = "What is a synonym of " + strings[0] + "?";
		String answer = strings[4];
		String[] answerParts = answer.split(",");
		HashSet<String> realAnswer = new HashSet<String>();
		for(String s : answerParts){
			String sa = cleanup(s).toLowerCase();
			realAnswer.add(sa);
		}
		String hint = strings[3];
		String info = "Definition: " + strings[2];
		QuestionFactory.generateQuestion(questionMap, answerList.get(3), counter++, 2, hint, query, realAnswer, info, strings[0]);
	}
	
	
	private String cleanup(String string){
		String s = string;
		while(s.length() > 0){
			if(s.substring(s.length() - 1).equals(" ")){
				s = s.substring(0, s.length() - 1);
			}
			else{
				break;
			}
		}
		while(s.length() > 0){
			if(s.substring(0, 1).equals(" ")){
				s = s.substring(1);
			}
			else{
				break;
			}
		}
		return s;
	}
	
	@Override
	public boolean showHint(){
		switch(mode){
			case 0:
				return false;
			case 1:
				return false;
			case 2:
				return false;
			case 3:
				return false;
			default:
				System.out.println("Loading Problem; Code: 221");
				return false;
		}
	}
	
	@Override
	public boolean showInfo(){
		switch(mode){
			case 0:
				return false;
			case 1:
				return false;
			case 2:
				return true;
			case 3:
				return true;
			default:
				System.out.println("Loading Problem; Code: 221");
				return false;
		}
	}
	
	public int getMode(){
		return mode;
	}
}
