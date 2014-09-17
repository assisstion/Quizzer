package com.github.assisstion.Quizzer.system;

import java.util.Set;

public interface Question{
	String getHint();
	String getQuery();
	Set<String> getAnswer();
	String getInfo();
	String getShortForm();
	int getID();
	int getMode();
}
