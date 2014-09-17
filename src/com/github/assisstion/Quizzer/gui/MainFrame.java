package com.github.assisstion.Quizzer.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
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

		panel_5 = new JPanel();
		panel_4.add(panel_5);

		chckbxFixedQuestions = new JCheckBox("Fixed Questions");
		panel_5.add(chckbxFixedQuestions);
	}
	
	protected void startQuiz(int i){
		QuizPanel quizPanel = new QuizPanel(i, textField.getText(), chckbxFixedQuestions.isSelected());
		contentPane.remove(panel_4);
		contentPane.add(quizPanel);
		contentPane.validate();
		new Thread(quizPanel).start();
	}
	
}
