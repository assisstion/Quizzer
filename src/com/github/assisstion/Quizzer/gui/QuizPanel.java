package com.github.assisstion.Quizzer.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.assisstion.Quizzer.custom.vocab.Quizzes;
import com.github.assisstion.Quizzer.system.Question;
import com.github.assisstion.Quizzer.system.Quiz;

public class QuizPanel extends JPanel implements Runnable{

	protected boolean excludeOn;
	protected HashSet<Integer> exclude;


	protected Set<String> incorrect = new HashSet<String>();
	protected Set<String> answerExclusion = new HashSet<String>();
	protected int totalCorrect;
	protected int totalCount;
	private boolean safeExit = false;
	private static final long serialVersionUID = -3723376082239462955L;

	protected Quiz quiz;
	protected Random random;
	protected int mode;

	protected Logger logger;
	protected LoggerPane loggerPane;
	protected String location;
	private JPanel panel;
	private JTextField textField;
	private JButton btnGo;

	protected PipedOutputStream pos;
	protected PipedInputStream pis;
	protected DataOutputStream dos;
	protected DataInputStream dis;

	protected Scanner sc;

	/*
	 * Modes:
	 *  1 = Definition
	 *  2 = Multi-Synonym
	 *  3 = Single-Synonym
	 */
	/**
	 * @wbp.parser.constructor
	 */
	public QuizPanel(int mode, String location, boolean excludeOn){
		this(Quizzes.getQuiz(mode), location, excludeOn);
	}

	public QuizPanel(Quiz quiz, String location, boolean excludeOn){
		this.quiz = quiz;
		random = new Random();
		this.location = location;

		this.excludeOn = excludeOn;
		if(excludeOn){
			exclude = new HashSet<Integer>();
		}

		logger = Logger.getLogger("main");
		setLayout(new BorderLayout(0, 0));
		loggerPane = new LoggerPane(logger, false);
		add(loggerPane);

		panel = new JPanel();
		loggerPane.add(panel, BorderLayout.SOUTH);

		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(25);

		btnGo = new JButton("Go");
		btnGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					dos.writeUTF(textField.getText());
					dos.flush();
					textField.setText("");
				}
				catch(IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		panel.add(btnGo);

		textField.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					btnGo.doClick();
				}
			}
		});

		pos = new PipedOutputStream();
		try{
			pis = new PipedInputStream(pos);
		}
		catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dos = new DataOutputStream(pos);
		dis = new DataInputStream(pis);

		sc = new Scanner(dis);
	}

	@Override
	public void run(){
		try{
			logger.log(CustomLevel.NOMESSAGE, "Welcome to the Vocab Quiz!");
			quiz.load(location);
			logger.log(CustomLevel.NOMESSAGE, "Type in your answer in the console (and press Enter), or type q to quit.");
			logger.log(CustomLevel.NOMESSAGE, "");
			boolean n = true;
			while(n == true){
				n = quiz();
			}
			printData();
			safeExit = true;
			logger.log(CustomLevel.NOMESSAGE, "Thank you for playing!");
		}
		catch(Exception e){
			logger.log(CustomLevel.NOMESSAGE, "Uncaught Exception; Code: 000");
			e.printStackTrace();
		}
		finally{
			if(!safeExit){
				logger.log(CustomLevel.NOMESSAGE, "");
				logger.log(CustomLevel.NOMESSAGE, "");
				logger.log(CustomLevel.NOMESSAGE, "Program terminated.");
				System.exit(1);
			}
			System.exit(0);
		}
	}

	private void printData(){
		if(incorrect.size() > 0){
			logger.log(CustomLevel.NOMESSAGE, "Incorrect Words:");
			for(String s : incorrect){
				logger.log(CustomLevel.NOMESSAGE, s);
			}
			logger.log(CustomLevel.NOMESSAGE, "");
		}
		if(totalCount > 0){
			logger.log(CustomLevel.NOMESSAGE, "Your score is: " + totalCorrect + "/" + totalCount +
					", or " + (double)totalCorrect/(double)totalCount*100 + "%.");
		}
	}

	private boolean quiz(){
		int id = randomExclude(quiz.getQuestionMap().size());
		if(id < 0){
			return false;
		}
		Question question = quiz.getQuestionMap().get(id);
		int limit = 1000000;
		int counter = 0;
		while(question == null){
			if(excludeOn){
				exclude.add(id);
			}
			if(++counter >= limit){
				logger.log(CustomLevel.NOMESSAGE, "Question Parsing Problem; Code: 104");
				return false;
			}
			id = randomExclude(quiz.getQuestionMap().size());
			if(id < 0){
				return false;
			}
			question = quiz.getQuestionMap().get(id);
		}
		String query = question.getQuery();
		logger.log(CustomLevel.NOMESSAGE, query);
		Set<String> answerSet = question.getAnswer();
		int asi = random.nextInt(answerSet.size());
		String[] sa = answerSet.toArray(new String[]{});
		String answer = sa[asi];
		String shortForm = question.getShortForm();
		String hint = question.getHint();
		if(quiz.showHint()){
			logger.log(CustomLevel.NOMESSAGE, hint);
			logger.log(CustomLevel.NOMESSAGE, "");
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
			b = getAnswer();
			answerExclusion.add(b);
			c = getAnswer();
			answerExclusion.add(c);
			d = getAnswer();
			answerExclusion.clear();
		}
		else if(i == 1){
			a = getAnswer();
			answerExclusion.add(a);
			b = answer;
			c = getAnswer();
			answerExclusion.add(c);
			d = getAnswer();
			answerExclusion.clear();
		}
		else if(i == 2){
			a = getAnswer();
			answerExclusion.add(a);
			b = getAnswer();
			answerExclusion.add(b);
			c = answer;
			d = getAnswer();
			answerExclusion.clear();
		}
		else if(i == 3){
			a = getAnswer();
			answerExclusion.add(a);
			b = getAnswer();
			answerExclusion.add(b);
			c = getAnswer();
			d = answer;
			answerExclusion.clear();
		}
		else{
			logger.log(CustomLevel.NOMESSAGE, "System RNG Problem; Code: 103");
		}
		logger.log(CustomLevel.NOMESSAGE, "a. " + a);
		logger.log(CustomLevel.NOMESSAGE, "b. " + b);
		logger.log(CustomLevel.NOMESSAGE, "c. " + c);
		logger.log(CustomLevel.NOMESSAGE, "d. " + d);
		while(true){
			String input = null;
			try{
				input = dis.readUTF();
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.log(CustomLevel.NOMESSAGE, "");
			boolean correct;
			if(input.length() != 1){
				logger.log(CustomLevel.NOMESSAGE, "Invalid Input! Try again.");
				logger.log(CustomLevel.NOMESSAGE, "");
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
				logger.log(CustomLevel.NOMESSAGE, "Hint: " + hint);
				continue;
			}
			else if(input.substring(0, 1).equalsIgnoreCase("i")){
				printData();
				continue;
			}
			else if(input.substring(0, 1).equalsIgnoreCase("q")){
				return false;
			}
			else{
				logger.log(CustomLevel.NOMESSAGE, "Invalid Input! Try again.");
				continue;
			}
			if(correct){
				logger.log(CustomLevel.NOMESSAGE, "Correct Answer! Score: " + ++totalCorrect + "/" + ++totalCount);
			}
			else{
				logger.log(CustomLevel.NOMESSAGE, "Incorrect Answer; the correct answer is " + toLetter(i) +
						"! Score: " + totalCorrect + "/" + ++totalCount);
				incorrect.add(shortForm);
			}
			logger.log(CustomLevel.NOMESSAGE, "");
			break;
		}
		if(quiz.showInfo()){
			logger.log(CustomLevel.NOMESSAGE, info);
			logger.log(CustomLevel.NOMESSAGE, "");
		}
		return true;
	}

	private int randomExclude(int total){
		if(!excludeOn){
			return random.nextInt(total);
		}
		else{
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
	}

	private String getAnswer(){
		int limit = 1000000;
		int counter = 0;
		String s = null;
		boolean cont = true;
		while(cont == true){
			cont = false;
			if(++counter >= limit){
				logger.log(CustomLevel.NOMESSAGE, "Answer Parsing Problem; Code: 101");
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
