package com.github.assisstion.Quizzer.vocab;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import com.github.assisstion.Quizzer.system.Question;

public class FixedTest{
	private Random random;
	private HashSet<Integer> exclude;
	private Set<String> answerExclusion;
	
	public FixedTest(){
		random = new Random();
		exclude = new HashSet<Integer>();
		answerExclusion = new HashSet<String>();
	}
	
	public boolean quiz(VocabQuiz quiz){
		int id = randomExclude(quiz.getQuestionMap().size());
		if(id < 0){
			return false;
		}
		Question question = quiz.getQuestionMap().get(id);
		while(question == null){
			exclude.add(id);
			id = randomExclude(quiz.getQuestionMap().size());
			if(id < 0){
				return false;
			}
			question = quiz.getQuestionMap().get(id);
		}
		int mode = question.getMode();
		String query = question.getQuery();
		System.out.println(query);
		Set<String> answerSet = question.getAnswer();
		int asi = random.nextInt(answerSet.size());
		String[] sa = answerSet.toArray(new String[]{});
		String answer = sa[asi];
		String shortForm = question.getShortForm();
		String hint = question.getHint();
		if(quiz.showHint()){
			System.out.println(hint);
			System.out.println();
		}
		String info = question.getInfo();
		answerExclusion.clear();
		answerExclusion.add(shortForm);
		for(String s : answerSet){
			answerExclusion.add(s);
		}
		int i = random.nextInt(4);
		String a = null;
		String b = null;
		String c = null;
		String d = null;
		if(i == 0){
			a = answer;
			b = getAnswer(quiz, mode);
			answerExclusion.add(b);
			c = getAnswer(quiz, mode);
			answerExclusion.add(c);
			d = getAnswer(quiz, mode);	
			answerExclusion.clear();
		}
		else if(i == 1){
			a = getAnswer(quiz, mode);
			answerExclusion.add(a);
			b = answer;
			c = getAnswer(quiz, mode);
			answerExclusion.add(c);
			d = getAnswer(quiz, mode);	
			answerExclusion.clear();
		}
		else if(i == 2){
			a = getAnswer(quiz, mode);
			answerExclusion.add(a);
			b = getAnswer(quiz, mode);
			answerExclusion.add(b);
			c = answer;
			d = getAnswer(quiz, mode);	
			answerExclusion.clear();
		}
		else if(i == 3){
			a = getAnswer(quiz, mode);
			answerExclusion.add(a);
			b = getAnswer(quiz, mode);
			answerExclusion.add(b);
			c = getAnswer(quiz, mode);
			d = answer;
			answerExclusion.clear();
		}
		else{
			System.out.println("System RNG Problem; Code: 103");
		}
		System.out.println("a. " + a);
		System.out.println("b. " + b);
		System.out.println("c. " + c);
		System.out.println("d. " + d);
		Scanner sc = new Scanner(System.in);
		while(true){
			String input = sc.next();
			System.out.println();
			boolean correct;
			if(input.length() != 1){
				System.out.println("Invalid Input! Try again.");
				System.out.println();
				continue;
			}
			if(input.substring(0, 1).equalsIgnoreCase("a")){
				if(i == 0){
					correct = true;
				}
				else{
					correct = false;
				}
			}
			else if(input.substring(0, 1).equalsIgnoreCase("b")){
				if(i == 1){
					correct = true;
				}
				else{
					correct = false;
				}
			}
			else if(input.substring(0, 1).equalsIgnoreCase("c")){
				if(i == 2){
					correct = true;
				}
				else{
					correct = false;
				}
			}
			else if(input.substring(0, 1).equalsIgnoreCase("d")){
				if(i == 3){
					correct = true;
				}
				else{
					correct = false;
				}
			}
			else if(input.substring(0, 1).equalsIgnoreCase("h")){
				System.out.println("Hint: " + hint);
				continue;
			}
			else if(input.substring(0, 1).equalsIgnoreCase("q")){
				return false;
			}
			else{
				System.out.println("Invalid Input! Try again.");
				continue;
			}
			if(correct){
				System.out.println("Correct Answer! Score: " + ++Main.totalCorrect + "/" + ++Main.totalCount);
			}
			else{
				System.out.println("Incorrect Answer; the correct answer is " + Main.toLetter(i) + 
						"! Score: " + Main.totalCorrect + "/" + ++Main.totalCount);
				Main.incorrect.add(shortForm);
			}
			System.out.println();
			break;
		}
		if(quiz.showInfo()){
			System.out.println(info);
			System.out.println();
		}
		return true;
	}
	
	private int randomExclude(int total){
		if(exclude.size() >= total){
			return -1;
		}
		while(exclude.size() < total){
			int i = random.nextInt(total);
			if(!exclude.contains(i)){
				exclude.add(i);
				return i;
			}
		}
		return -1;
	}

	private String getAnswer(VocabQuiz quiz, int mode){
		int limit = 1000000;
		int counter = 0;
		String s = null;
		boolean cont = true;
		while(cont == true){
			cont = false;
			if(++counter >= limit){
				System.out.println("Answer Parsing Problem; Code: 101");
			}
			s = quiz.getAnswerList().get(mode).get(random.nextInt(quiz.getAnswerList().get(mode).size()));
			for(String nto : answerExclusion){
				if(nto.equalsIgnoreCase(s)){
					cont = true;
				}
			}
		}
		return s;
	}
}
