package com.github.assisstion.Quizzer.gui;

import java.util.logging.Level;

public class CustomLevel extends Level{
	
	private static final long serialVersionUID = -5351557309080563213L;
	static final Level NOMESSAGE = new CustomLevel("NOMESSAGE", 1200);
	
	protected CustomLevel(String name, int value){
		super(name, value);
	}	

}
