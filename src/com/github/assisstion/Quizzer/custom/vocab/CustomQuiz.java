package com.github.assisstion.Quizzer.custom.vocab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.github.assisstion.Quizzer.system.Question;
import com.github.assisstion.Quizzer.system.Quiz;

public class CustomQuiz extends Quiz{

	protected CustomQuestionFormat format;

	protected CustomQuiz(){
		answerList.add(new ArrayList<String>());
	}

	public CustomQuiz(CustomQuestionFormat qf){
		this();
		format = qf;
	}

	@Override
	public void load(String location){
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
				int ps = format.getPartSize();
				if(ps > 0 && strings.length != ps){
					System.out.println(s);
					System.out.println("Loading Problem; Code: 201");
					s = br.readLine();
					continue;
				}
				Question q = format.getQuestion(Arrays.asList(strings), this);
				if(q == null){
					//System.out.println(s);
					//System.out.println("Loading Problem; Code: 202");
					s = br.readLine();
					continue;
				}
				for(String s1 : q.getAnswer()){
					answerList.get(0).add(s1);
				}
				questionMap.put(q.getID(), q);
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

	@Override
	public boolean showHint(){
		return format.showHint();
	}

	@Override
	public boolean showInfo(){
		return format.showInfo();
	}

	public static String cleanup(String string){
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

}
