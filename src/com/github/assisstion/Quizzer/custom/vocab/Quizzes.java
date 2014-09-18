package com.github.assisstion.Quizzer.custom.vocab;

import com.github.assisstion.Quizzer.system.Quiz;

public final class Quizzes{

	private Quizzes(){

	}

	public static Quiz getQuiz(int mode){
		switch(mode){
			case 1:
				return new CustomQuiz(new DefinitionQuizFormat());
			case 2:
				return new CustomQuiz(new MultipleSynonymQuizFormat());
			case 3:
				return new CustomQuiz(new SingleSynonymQuizFormat());
			case 4:
				return new CustomQuiz(new ReverseSentenceFormat());
			case 5:
				return new CustomQuiz(new SentenceFormat());
			default:
				throw new IllegalArgumentException("Invalid quiz number");
		}
	}
}
