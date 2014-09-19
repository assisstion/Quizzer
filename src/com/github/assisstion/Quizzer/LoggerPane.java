package com.github.assisstion.Quizzer.gui;

import java.awt.BorderLayout;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.text.DefaultCaret;

public class LoggerPane extends JPanel{
	
	public Logger log;
	
	protected String message = "";
	
	private static final long serialVersionUID = 1022490441168101112L;
	private JTextPane textPane;
	private JScrollPane scrollPane;
	protected LogWorker worker;
	protected ProgressWorker progress;
	private JProgressBar progressBar;
	
	
	/**
	 * Create the frame.
	 */
	public LoggerPane(Logger log, boolean showProgress){
		//setTitle();
		
		setLayout(new BorderLayout(0, 0));
		
		this.log = log;
		LogHandler handler = new LogHandler(this);
		log.addHandler(handler);
		
		scrollPane = new JScrollPane();
		add(scrollPane);
		
		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		textPane.setEditable(false);
		
		progressBar = new JProgressBar(0, 0);
		progressBar.setEnabled(false);
		progressBar.setStringPainted(false);
		if(showProgress){
			add(progressBar, BorderLayout.SOUTH);
		}
		
		DefaultCaret caret = (DefaultCaret) textPane.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		worker = new LogWorker();
		progress = new ProgressWorker();
	}
	
	public void setProgress(IntegerPair values){
		progress.push(values);
	}
	
	public class LogWorker extends SwingWorker<Object, String>{
		
		protected void push(String message){
			publish(message);
		}
		
		@Override
		protected Object doInBackground() throws Exception{
			return null;
		}
		
		@Override
		protected void process(List<String> messages){
			for(String messageOne : messages){
				if(!message.equals("")){
					message += "\n";
				}
				message += messageOne;
			}
			textPane.setText(message);
		}
	}
	
	public class ProgressWorker extends SwingWorker<Object, IntegerPair>{
		
		protected void push(IntegerPair progress){
			publish(progress);
		}
		
		@Override
		protected Object doInBackground() throws Exception{
			return null;
		}
		
		@Override
		protected void process(List<IntegerPair> integerPairs){
			for(IntegerPair pairOne : integerPairs){
				if(pairOne.getValueTwo().equals(0)){
					progressBar.setEnabled(false);
					progressBar.setStringPainted(false);
				}
				else{
					progressBar.setEnabled(true);
					progressBar.setStringPainted(true);
				}
				progressBar.setValue(pairOne.getValueOne());
				progressBar.setMaximum(pairOne.getValueTwo());
			}
		}
	}
}