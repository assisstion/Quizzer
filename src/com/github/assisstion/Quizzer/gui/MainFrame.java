package com.github.assisstion.Quizzer.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.github.assisstion.Quizzer.custom.vocab.Quizzes;

public class MainFrame extends JFrame{

	private static final long serialVersionUID = -1322465178993759990L;

	private JPanel contentPane;
	private JButton btnDefinitionQuiz;
	private JButton btnMultipleSynonymsQuiz;
	private JButton btnSingleSynonymQuiz;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JLabel lblWordListLocation;
	private JTextField textField;
	private JButton btnChooseFile;
	private JPanel panel_4;
	private JPanel panel_5;
	private JCheckBox chckbxFixedQuestions;
	private JPanel panel_6;
	private JPanel panel_6_1;
	private JButton btnReverseSentenceQuiz;
	private JPanel panel_7;
	private JButton btnSentenceQuiz;
	private QuizPanel quizPanel;
	private FlashCardPanel fcpanel;
	private JCheckBox chckbxLogOutput;

	private List<Closeable> leakableResources = new CopyOnWriteArrayList<Closeable>();
	private JCheckBox chckbxBalance;
	private JComboBox comboBox;
	private JLabel lblTime;
	private JPanel panel_8;
	private JButton btnFlashcards;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run(){
				try{
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame(){
		setTitle("Vocab Quizzer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new ShutdownWindowListener());
		setBounds(100, 100, 450, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		panel_4 = new JPanel();
		contentPane.add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.Y_AXIS));

		panel_3 = new JPanel();
		panel_4.add(panel_3);

		lblWordListLocation = new JLabel("File Location:");
		panel_3.add(lblWordListLocation);

		textField = new JTextField();
		panel_3.add(textField);
		textField.setColumns(18);

		btnChooseFile = new JButton("Open...");
		btnChooseFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				int value = fileChooser.showOpenDialog(MainFrame.this);
				if(value == JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					textField.setText(file.getAbsolutePath());
				}
			}
		});
		panel_3.add(btnChooseFile);

		panel = new JPanel();
		panel_4.add(panel);

		btnDefinitionQuiz = new JButton("Definition Quiz");
		btnDefinitionQuiz.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				startQuiz(1);
			}
		});
		panel.add(btnDefinitionQuiz);

		panel_2 = new JPanel();
		panel_4.add(panel_2);

		btnSingleSynonymQuiz = new JButton("Single Synonym Quiz");
		btnSingleSynonymQuiz.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				startQuiz(3);
			}
		});
		panel_2.add(btnSingleSynonymQuiz);

		panel_1 = new JPanel();
		panel_4.add(panel_1);

		btnMultipleSynonymsQuiz = new JButton("Multiple Synonyms Quiz");
		btnMultipleSynonymsQuiz.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startQuiz(2);
			}
		});
		panel_1.add(btnMultipleSynonymsQuiz);

		panel_7 = new JPanel();
		panel_4.add(panel_7);

		btnSentenceQuiz = new JButton("Sentence Quiz");
		btnSentenceQuiz.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startQuiz(5);
			}
		});
		panel_7.add(btnSentenceQuiz);

		panel_6 = new JPanel();
		panel_4.add(panel_6);

		btnReverseSentenceQuiz = new JButton("Reverse Sentence Quiz");
		btnReverseSentenceQuiz.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startQuiz(4);
			}
		});
		panel_6.add(btnReverseSentenceQuiz);

		panel_8 = new JPanel();
		panel_4.add(panel_8);

		btnFlashcards = new JButton("Flashcards");
		btnFlashcards.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fcpanel = new FlashCardPanel(MainFrame.this,Quizzes.getQuiz(1),
						chckbxFixedQuestions.isSelected(),textField.getText());
				contentPane.remove(panel_4);
				contentPane.add(fcpanel);
				//MainFrame.this.invalidate();
			}
		});
		panel_8.add(btnFlashcards);

		panel_5 = new JPanel();
		panel_4.add(panel_5);

		chckbxFixedQuestions = new JCheckBox("Fixed Questions");
		panel_5.add(chckbxFixedQuestions);

		chckbxLogOutput = new JCheckBox("Log Output");
		chckbxLogOutput.setSelected(true);
		panel_5.add(chckbxLogOutput);

		chckbxBalance = new JCheckBox("Balance");
		chckbxBalance.setSelected(true);
		panel_5.add(chckbxBalance);

		panel_6_1 = new JPanel();
		panel_4.add(panel_6_1);

		lblTime = new JLabel("Time:");
		panel_6_1.add(lblTime);

		comboBox = new JComboBox();
		comboBox.addItem("Fast");
		comboBox.addItem("Regular");
		comboBox.addItem("Slow");
		comboBox.addItem("None");
		comboBox.setSelectedItem("Regular");
		panel_6_1.add(comboBox);
	}

	protected void startQuiz(int i){
		quizPanel = new QuizPanel(this, i, textField.getText(),
				chckbxFixedQuestions.isSelected(), chckbxBalance.isSelected(),
				comboBox.getSelectedItem().toString());
		contentPane.remove(panel_4);
		contentPane.add(quizPanel);
		new Thread(new QuizStarter()).start();

	}

	public void returnToMenu(final JPanel panel){
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run(){
				contentPane.remove(panel);
				contentPane.add(panel_4);
				contentPane.validate();
				contentPane.repaint();
			}

		});
	}

	protected class QuizStarter implements Runnable{

		@Override
		public void run(){
			FileOutputConsumer fos = null;
			try{
				if(chckbxLogOutput.isSelected()){
					Calendar c = Calendar.getInstance();
					String timeStamp = String.format("%1$tY-%1$tm-%1td %1$tH:%1$tM:%1$tS", c);
					File dir = new File("logs");
					dir.mkdir();
					File outputFile = new File("logs" + File.separator + "quiz-" + timeStamp + ".txt");
					outputFile.createNewFile();
					fos = new FileOutputConsumer(new FileWriter(outputFile));
					leakableResources.add(fos);
					Logger log = Logger.getLogger("quiz-" + quizPanel.hashCode());
					log.addHandler(new LogHandler(fos));
				}
				contentPane.validate();
				Thread qt = new Thread(quizPanel);
				synchronized(qt){
					qt.start();
					while(!quizPanel.isComplete()){
						try{
							qt.join();
						}
						catch(InterruptedException e){
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
			finally{
				if(fos != null){
					try{
						fos.close();
						leakableResources.remove(fos);
					}
					catch(IOException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public class ShutdownWindowListener extends WindowAdapter{

		@Override
		public void windowClosed(WindowEvent arg0){
			for(Closeable closeable : leakableResources){
				try{
					closeable.close();
				}
				catch(IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.exit(0);
		}

	}
}
