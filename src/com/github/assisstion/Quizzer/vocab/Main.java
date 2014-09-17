package com.github.assisstion.Quizzer.vocab;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import com.github.assisstion.Quizzer.system.Question;

public class Main{
	
	static Set<String> incorrect = new HashSet<String>();
	private static Set<String> answerExclusion = new HashSet<String>();
	static int totalCorrect;
	static int totalCount;
	private static boolean safeExit = false;
	private static final boolean FIXED = true;
	
	public static void main(String[] args){
		try{
			System.out.println("Welcome to the Vocab Quiz!");
			VocabQuiz quiz = new VocabQuiz(1);
			quiz.load("/Users/markusfeng/Desktop/word_list.txt");
			System.out.println("Type in your answer in the console (and press Enter), or type q to quit.");
			System.out.println();
			boolean n = true;
			if(!FIXED){
				while(n == true){
					n = quiz(quiz);
				}
			}
			else{
				FixedTest fq = new FixedTest();
				while(n == true){
					n = fq.quiz(quiz);
				}
			}
			if(incorrect.size() > 0){
				System.out.println("Incorrect Words:");
				for(String s : incorrect){
					System.out.println(s);
				}
				System.out.println();
			}
			if(totalCount > 0){
				System.out.println("Your final score is: " + totalCorrect + "/" + totalCount +
						", or " + (double)totalCorrect/(double)totalCount*100 + "%.");
			}
			safeExit = true;
			System.out.println("Thank you for playing!");
		}
		catch(Exception e){
			System.out.println("Uncaught Exception; Code: 000");
			e.printStackTrace();
		}
		finally{
			if(!safeExit){
				System.out.println();
				System.out.println();
				System.out.println("Program terminated.");
			}
		}
	}
	
	private static boolean quiz(VocabQuiz quiz){
		Random random = new Random();
		int id = random.nextInt(quiz.getQuestionMap().size());
		Question question = quiz.getQuestionMap().get(id);
		int limit = 1000000;
		int counter = 0;
		while(question == null){
			if(++counter >= limit){
				System.out.println("Question Parsing Problem; Code: 104");
				return false;
			}
			id = random.nextInt(quiz.getQuestionMap().size());
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
			b = getAnswer(quiz, mode, random);
			answerExclusion.add(b);
			c = getAnswer(quiz, mode, random);
			answerExclusion.add(c);
			d = getAnswer(quiz, mode, random);
			answerExclusion.clear();
		}
		else if(i == 1){
			a = getAnswer(quiz, mode, random);
			answerExclusion.add(a);
			b = answer;
			c = getAnswer(quiz, mode, random);
			answerExclusion.add(c);
			d = getAnswer(quiz, mode, random);
			answerExclusion.clear();
		}
		else if(i == 2){
			a = getAnswer(quiz, mode, random);
			answerExclusion.add(a);
			b = getAnswer(quiz, mode, random);
			answerExclusion.add(b);
			c = answer;
			d = getAnswer(quiz, mode, random);
			answerExclusion.clear();
		}
		else if(i == 3){
			a = getAnswer(quiz, mode, random);
			answerExclusion.add(a);
			b = getAnswer(quiz, mode, random);
			answerExclusion.add(b);
			c = getAnswer(quiz, mode, random);
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
				System.out.println("Correct Answer! Score: " + ++totalCorrect + "/" + ++totalCount);
			}
			else{
				System.out.println("Incorrect Answer; the correct answer is " + toLetter(i) +
						"! Score: " + totalCorrect + "/" + ++totalCount);
				incorrect.add(shortForm);
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
	
	private static String getAnswer(VocabQuiz quiz, int mode, Random random){
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
	
	protected static String toLetter(int i){
		switch(i){
			case 0:
				return "a";
			case 1:
				return "b";
			case 2:
				return "c";
			case 3:
				return "d";
			default:
				System.out.println("System Parsing Problem; Code: 102");
				return "";
		}
	}
}

