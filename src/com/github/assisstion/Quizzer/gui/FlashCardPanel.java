package com.github.assisstion.Quizzer.gui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.NavigableSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JPanel;

import com.github.assisstion.Quizzer.system.Question;
import com.github.assisstion.Quizzer.system.Quiz;

public class FlashCardPanel extends JPanel implements MouseListener, KeyListener{

	private static final long serialVersionUID = 5741923347508783398L;
	protected HashSet<Integer> exclude = new HashSet<Integer>();
	protected Random random = new Random();
	protected String nextQuestion;
	protected String nextAnswer;
	protected Quiz quiz;
	protected MainFrame frame;
	protected boolean excludeOn;
	protected boolean finished = false;
	protected boolean init = false;
	protected boolean showAnswer = false;
	protected String location;

	/**
	 * Create the panel.
	 */
	public FlashCardPanel(MainFrame mf, Quiz quiz, boolean excludeOn, String loc){
		frame = mf;
		this.quiz = quiz;
		this.excludeOn = excludeOn;
		location = loc;
		addMouseListener(this);
		addKeyListener(this);
		setFocusable(true);
	}

	@Override
	public void paint(Graphics g){
		requestFocusInWindow();
		if(!init){
			init = true;
			quiz.load(location);
			next();
		}
		if(showAnswer){
			int size = 20;
			g.setFont(new Font("Arial", 0, size));
			String[] sa = format(g, nextAnswer, getWidth(), getHeight());
			int i = 0;
			for(String s : sa){
				g.drawString(s, getWidth()/2 -
						g.getFontMetrics().stringWidth(s)/2,
						getHeight()/2-sa.length*size/2+i*size);
				i++;
			}
		}
		else{
			int size = 40;
			g.setFont(new Font("Arial", 0, size));
			String[] sa = format(g, nextQuestion, getWidth(), getHeight());
			int i = 0;
			for(String s : sa){
				g.drawString(s, getWidth()/2 -
						g.getFontMetrics().stringWidth(s)/2,
						getHeight()/2-sa.length*size/2+i*size);
				i++;
			}
		}
	}

	protected boolean next(){
		Pair<String, String> ps = getNext();
		if(ps == null){
			return false;
		}
		nextQuestion = ps.getValueOne();
		nextAnswer = ps.getValueTwo();
		repaint();
		return true;
	}

	protected Pair<String, String> getNext(){
		int id = randomExclude(quiz.getQuestionMap().size());
		if(finished){
			return null;
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
				System.out.println("Question Parsing Problem; Code: 104");
				return null;
			}
			id = randomExclude(quiz.getQuestionMap().size());
			if(id < 0){
				return null;
			}
			question = quiz.getQuestionMap().get(id);
		}
		String query = question.getQuery();
		Set<String> ss = question.getAnswer();
		String sn = "";
		for(String s : ss){
			if(!(sn.length() == 0 || s.length() == 0)){
				sn += ", ";
			}
			sn += s;
		}
		return new Pair<String, String>(query, sn);
	}

	private int randomExclude(int total){
		if(!excludeOn){
			return random.nextInt(total);
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

	@Override
	public void mouseClicked(MouseEvent e){
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e){
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e){
		if(showAnswer){
			if(!next()){
				frame.returnToMenu(this);
			}
		}
		showAnswer = !showAnswer;
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e){
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e){
		// TODO Auto-generated method stub

	}

	public static String[] format(Graphics g, String str, double width, double height){
		NavigableSet<Integer> breaks = new TreeSet<Integer>();
		int lastLastSpace = 0;
		int lastSpace = 0;
		for(int i = 0; i < str.length(); i++){
			if(str.charAt(i) == ' ' || str.charAt(i) == '-'){
				lastLastSpace = lastSpace;
				lastSpace = i;
			}
			if(g.getFontMetrics().stringWidth(stripSpaces(str.substring(
					breaks.size() == 0 ? 0 : breaks.last(), i+1))) >= width){
				if(lastSpace != lastLastSpace){
					breaks.add(lastSpace + 1);
				}
				else{
					breaks.add(i);
				}
			}
			else if(str.charAt(i) == '\n'){
				breaks.add(i);
			}
		}
		int size = breaks.size() + 1;
		int maxRows = (int) (height / g.getFont().getSize());
		if(size > maxRows){
			size = maxRows;
		}
		String[] sa = new String[size];
		int last = 0;
		int i = 0;
		boolean max = false;
		for(int n : breaks){
			String s = str.substring(last, n).replace("\n", "");
			if(i == maxRows - 1){
				max = true;
				if(g.getFontMetrics().stringWidth(s + "...") <= width){
					s = s + "...";
				}
				else{
					boolean broken = false;
					for(int j = s.length() - 1; j >= 0; j--){
						if(s.charAt(j) == ' ' || s.charAt(j) == '-'){
							String stmp = s.substring(0, j);
							if(g.getFontMetrics().stringWidth(stmp + "...") <= width){
								s = stmp + "...";
								broken = true;
								break;
							}
						}
					}
					if(!broken){
						String stmp = s.substring(0, s.length() - 1);
						boolean blank = false;
						while(g.getFontMetrics().stringWidth(stmp + "...") > width){
							if(stmp.length() <= 1){
								blank = true;
								break;
							}
							stmp = stmp.substring(0, stmp.length() - 1);
						}
						if(blank){
							s = "";
						}
						else{
							s = stmp + "...";
						}
					}
				}
			}
			sa[i] = s;
			last = n;
			i++;
			if(i == maxRows){
				max = true;
				break;
			}
		}
		if(!max){
			sa[breaks.size()] = str.substring(last);
		}
		return sa;
	}

	public static String stripSpaces(String s){
		while(s.endsWith(" ")){
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}

	@Override
	public void keyTyped(KeyEvent e){
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e){
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_M){
			frame.returnToMenu(this);
		}
		else if(e.getKeyCode() == KeyEvent.VK_Q){
			System.exit(0);
		}
	}
}