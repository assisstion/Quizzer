package com.github.assisstion.Quizzer.gui;

import java.util.Calendar;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogHandler extends Handler{

	protected LoggerPane gui;
	
	public LogHandler(LoggerPane gui){
		this.gui = gui;
	}
	
	@Override
	public void close() throws SecurityException{
		// Unimplemented

	}

	@Override
	public void flush(){
		// Unimplemented
	}

	@Override
	public void publish(LogRecord record){
		Calendar c = Calendar.getInstance();
		String timeStamp = String.format("%1$tY-%1$tm-%1td %1$tH:%1$tM:%1$tS", c);
		if(!record.getLevel().getName().equals("NOMESSAGE")){
			gui.worker.push(timeStamp + " - [" + record.getLevel().getName() + "] " + record.getMessage());
		}
		else{
			gui.worker.push(record.getMessage());
		}
	}
}
