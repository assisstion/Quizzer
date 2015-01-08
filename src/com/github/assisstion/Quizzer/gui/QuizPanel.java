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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
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

	private static final String[] ORDINALS = {
		"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
		"k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
		"u", "v", "w", "x", "y", "z"
	};

	protected boolean excludeOn;
	protected HashSet<Integer> exclude;

	protected Map<Integer, Pair<Integer, Integer>> scores
	= new HashMap<Integer, Pair<Integer, Integer>>();

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

	protected int qsize = 4;

	protected MainFrame mainFrame;

	protected boolean silentlyExit = true;

	protected boolean complete;

	protected boolean balance;

	private boolean finished;

	/*
	 * Modes:
	 *  1 = Definition
	 *  2 = Multi-Synonym
	 *  3 = Single-Synonym
	 */
	public QuizPanel(MainFrame mainFrame, int mode, String location, boolean excludeOn, boolean balance){
		this(mainFrame, Quizzes.getQuiz(mode), location, excludeOn, balance);
	}

	public QuizPanel(MainFrame mainFrame, Quiz quiz, String location, boolean excludeOn, boolean balance){
		this.mainFrame = mainFrame;
		this.quiz = quiz;
		random = new Random();
		this.location = location;

		this.excludeOn = excludeOn;
		if(excludeOn){
			exclude = new HashSet<Integer>();
		}

		this.balance = balance;

		logger = Logger.getLogger("quiz-" + hashCode());
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
			finished = false;
			complete = false;
			safeExit = false;
			silentlyExit = false;
			logger.log(CustomLevel.NOMESSAGE, "Welcome to the Vocab Quiz!");
			quiz.load(location);
			logger.log(CustomLevel.NOMESSAGE, "Type in your answer in the console (and press Enter), or type q to quit.");
			logger.log(CustomLevel.NOMESSAGE, "");
			boolean n = true;
			while(n == true){
				n = quiz();
				if(finished){
					printData();
					exitPrompt();
					return;
				}
				if(silentlyExit){
					return;
				}
			}
			printData();
			safeExit = true;
			logger.log(CustomLevel.NOMESSAGE, "Thank you for playing!");
		}
		catch(Exception e){
			logger.log(CustomLevel.NOMESSAGE, "Uncaught " + e.getClass() +
					": " + e.getMessage() + "; Code: 000");
			e.printStackTrace();
			exitPrompt();
			return;
		}
		finally{
			complete = true;
			if(!silentlyExit){
				if(!safeExit){
					logger.log(CustomLevel.NOMESSAGE, "");
					logger.log(CustomLevel.NOMESSAGE, "");
					logger.log(CustomLevel.NOMESSAGE, "Program terminated.");
					mainFrame.setVisible(false);
					mainFrame.dispose();
				}
				mainFrame.setVisible(false);
				mainFrame.dispose();
			}
			else{
				mainFrame.returnToMenu();
			}
		}
	}

	private void exitPrompt(){
		logger.log(CustomLevel.NOMESSAGE, "Type \"quit\" to exit");
		logger.log(CustomLevel.NOMESSAGE, "Type \"menu\" to return to menu");
		String input;
		while(true){
			try{
				input = dis.readUTF();
				logger.log(CustomLevel.NOMESSAGE, "(" + input + ")");
			}
			catch(IOException e1){
				break;
			}
			if(input.equalsIgnoreCase("quit") || (qsize < 17 || qsize > 26) && input.equalsIgnoreCase("q")){
				safeExit = true;
				silentlyExit = false;
				break;
			}
			else if(input.equalsIgnoreCase("menu") || (qsize < 13 || qsize > 26) && input.equalsIgnoreCase("m")){
				silentlyExit = true;
				break;
			}
			else{
				logger.log(CustomLevel.NOMESSAGE, "Invalid Input! Try again.");
			}
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
		if(finished){
			return false;
		}
		if(id < 0){
			throw new IllegalArgumentException("Quiz size non-positive");
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
		int i = random.nextInt(qsize);
		String[] qAns = new String[qsize];
		for(int j = 0; j < qsize; j++){
			if(j == i){
				qAns[j] = answer;
			}
			else{
				String ans = getAnswer();
				qAns[j] = ans;
				answerExclusion.add(ans);
			}
		}
		answerExclusion.clear();
		for(int j = 0; j < qsize; j++){
			logger.log(CustomLevel.NOMESSAGE, ordinal(j) + ". " + qAns[j]);
		}
		while(true){
			String input = null;
			try{
				input = dis.readUTF();
				logger.log(CustomLevel.NOMESSAGE, "(" + input + ")");
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.log(CustomLevel.NOMESSAGE, "");
			boolean correct = false;
			int counterI = 0;
			boolean broke = false;
			if(input == null){
				return false;
			}
			if(input.equalsIgnoreCase("hint") || (qsize < 8 || qsize > 26) && input.equalsIgnoreCase("h")){
				logger.log(CustomLevel.NOMESSAGE, "Hint: " + hint);
				continue;
			}
			else if(input.equalsIgnoreCase("info") || (qsize < 9 || qsize > 26) && input.equalsIgnoreCase("i")){
				printData();
				continue;
			}
			else if(input.equalsIgnoreCase("quit") || (qsize < 17 || qsize > 26) && input.equalsIgnoreCase("q")){
				return false;
			}
			else if(input.equalsIgnoreCase("menu") || (qsize < 13 || qsize > 26) && input.equalsIgnoreCase("m")){
				silentlyExit = true;
				return true;
			}
			else if(input.equalsIgnoreCase("help")){
				printHelp();
				continue;
			}
			for(String s : ordinals()){
				if(input.equalsIgnoreCase(s)){
					if(i == counterI){
						correct = true;
					}
					else{
						correct = false;
					}
					broke = true;
					break;
				}
				counterI++;
				if(counterI >= qsize){
					break;
				}
			}
			if(!broke){
				logger.log(CustomLevel.NOMESSAGE, "Invalid Input! Try again.");
				continue;
			}
			if(correct){
				logger.log(CustomLevel.NOMESSAGE, "Correct Answer! Score: " + ++totalCorrect + "/" + ++totalCount);
				if(scores.containsKey(id)){
					Pair<Integer, Integer> pair = scores.get(id);
					Pair<Integer, Integer> pair2 = new Pair<Integer, Integer>(
							pair.getValueOne() + 1, pair.getValueTwo() + 1);
					scores.put(id, pair2);
				}
				else{
					scores.put(id, new Pair<Integer, Integer>(1, 1));
				}
			}
			else{
				logger.log(CustomLevel.NOMESSAGE, "Incorrect Answer; the correct answer is " + toLetter(i) +
						"! Score: " + totalCorrect + "/" + ++totalCount);
				incorrect.add(shortForm);
				if(scores.containsKey(id)){
					Pair<Integer, Integer> pair = scores.get(id);
					Pair<Integer, Integer> pair2 = new Pair<Integer, Integer>(
							pair.getValueOne(), pair.getValueTwo() + 1);
					scores.put(id, pair2);
				}
				else{
					scores.put(id, new Pair<Integer, Integer>(0, 1));
				}
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

	private void printHelp(){
		logger.log(CustomLevel.NOMESSAGE, "Available Commands:");
		logger.log(CustomLevel.NOMESSAGE, "help - Display available commands");
		logger.log(CustomLevel.NOMESSAGE, "hint - Gets a hint");
		logger.log(CustomLevel.NOMESSAGE, "info - Gets current information");
		logger.log(CustomLevel.NOMESSAGE, "quit - Shuts down program");
		logger.log(CustomLevel.NOMESSAGE, "menu - Return to main menu");
	}

	private Iterable<String> ordinals(){
		if(qsize > 26){
			return new Ordinals(1);
		}
		return Arrays.asList(ORDINALS);
	}

	private String ordinal(int n){
		if(qsize > 26){
			return String.valueOf(n + 1);
		}
		return ORDINALS[n];
	}

	private int randomExclude(int total){
		if(!excludeOn){
			if(balance){
				//max miss 1
				final double miss = 0.9;
				//min ratio 0
				final double ratio = 2.0;
				double totalX = 0;
				final double fix = total / (total - ratio);
				for(int i = 0; i < total; i++){
					if(scores.containsKey(i)){
						Pair<Integer, Integer> pint = scores.get(i);
						totalX += fix - (double)pint.getValueOne() /
								pint.getValueTwo();
					}
					else{
						totalX += fix - miss;
					}
				}
				double val = random.nextDouble() * totalX;
				for(int i = 0; i < total; i++){
					if(scores.containsKey(i)){
						Pair<Integer, Integer> pint = scores.get(i);
						val -= fix - (double)pint.getValueOne() /
								pint.getValueTwo();
					}
					else{
						val -= fix - miss;
					}
					if(val < 0){
						return i;
					}
				}
				throw new IllegalStateException(String.valueOf(val));
			}
			else{
				return random.nextInt(total);
			}
		}
		else{
			if(exclude.size() >= total){
				finished = true;
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
				break;
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

	private class Ordinals implements Iterable<String>{

		private long start = 0;

		public Ordinals(long startsAt){
			start = startsAt;
		}

		@Override
		public Iterator<String> iterator(){
			return new Iterator<String>(){

				long count = start;

				@Override
				public boolean hasNext(){
					//Always has next
					return true;
				}

				@Override
				public String next(){
					return String.valueOf(count++);
				}

				@Override
				public void remove(){
					throw new UnsupportedOperationException();
				}

			};
		}

	}

	public boolean isComplete(){
		return complete;
	}
}
