/**
 * Creates the beatmap from user input
 * 
 * @author Yuting, Ganashsai
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
public class Edit extends JPanel implements ActionListener, KeyListener,GlobalVar{
	
	private JPanel pnlScreen, pnlBtn;
	private JButton btnPlay, btnClear, btnSub, btnExit;
	private BeatMap tempBeats = new BeatMap();
	private FileInputStream src;
	private FileOutputStream dest;
	private File tempSong, beatPlace;
	private boolean bd = false, bf = false, bl = false, bs = false, bj = false, bk = false; // If the button bX is being pressed
	private int ind = 0,inf = 0,inl = 0,ins = 0,inj = 0,ink = 0; // length of key inX
	private int bed = 0,bef = 0,bel = 0,bes = 0,bej = 0,bek = 0,totime = 0; // The beginning of the key press, and total time passed
	private int songPercent = 0;
	private Audio tempAud;
	private Timer time = new Timer(16, this);
	private String fileName;
	private File songPlace;
	private JLabel lblPercent;
	
	public Edit(File song, String name){
		songPlace = song;
		fileName = name;
		pnlScreen = new DrawPanel();
		pnlBtn = new JPanel();
		btnPlay = new JButton("Test");
		btnClear = new JButton("Clear");
		btnSub = new JButton("Submit");
		btnSub.setEnabled(false);
		btnExit = new JButton("Exit");
		tempAud = new Audio(songPlace);
		lblPercent = new JLabel(Integer.toString(songPercent));
		pnlScreen.setBackground(Color.WHITE);

		System.out.println(fileName);
		frame.setTitle("Rythmn Master - " + fileName);
		
		frame.add(this);
		addKeyListener(this);
		
		setLayout(new BorderLayout());
		add(pnlScreen, BorderLayout.CENTER);
		pnlBtn.setLayout(new FlowLayout());
		add(pnlBtn, BorderLayout.SOUTH);
		
		pnlBtn.add(btnPlay);
		pnlBtn.add(btnClear);
		pnlBtn.add(btnSub);
		pnlBtn.add(btnExit);
		pnlBtn.add(lblPercent);
		
		btnPlay.addActionListener(this);
		btnClear.addActionListener(this);
		btnSub.addActionListener(this);
		btnExit.addActionListener(this);
		
		this.setVisible(true);
		
		this.revalidate();
		this.repaint();
		//System.out.println("In edit mode");
		tempAud.start();
		time.start();
	}
	
	public void createFiles(){
		tempSong = new File("src/Songs/"+fileName);
		beatPlace = new File("src/Songs/"+fileName+"/"+fileName+".songMap");
		try{
			System.out.println("Created the folder: " + tempSong.mkdirs());
			src = new FileInputStream(songPlace);
			tempBeats.writeMap(beatPlace);
			dest = new FileOutputStream(tempSong+"/"+fileName+".wav");
			dest.getChannel().transferFrom(src.getChannel(), 0, src.getChannel().size());
			File file = new File(tempSong + "/" + fileName + ".scores");
			file.createNewFile();
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void addNotify(){
		super.addNotify();
		requestFocus();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0){
		if(arg0.getSource() == time){
			songPercent = tempAud.percentDone();
			lblPercent.setText(Integer.toString(songPercent) + "% Complete");
			if(songPercent == 100 && !btnSub.isEnabled()){
				time.stop();
				btnSub.setEnabled(true);
			}
			totime++;
			pnlScreen.repaint();
			if(bd){
				ind++;
			}
			if(bf){
				inf++;
			}
			if(bl){
				inl++;
			}
			if(bs){
				ins++;
			}
			if(bj){
				inj++;
			}
			if(bk){
				ink++;
			}
		}
		else if(arg0.getSource() == btnExit){
			tempAud.remove();
			frame.reset();
			frame.add(new MainMenu());
			System.out.println("Going Back");
		}
		else if (arg0.getSource() == btnSub){
			try {
				createFiles();
				//Get the images and stuff
				closeThings();
				frame.reset();
				frame.add(new MainMenu());
				System.out.println("Going Back");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// TEMPORARY CODE START-----------------------
		else if (arg0.getSource() == btnPlay){//     |
			try {//                                  |
				createFiles();
				closeThings();
				frame.reset();
				frame.add(new MainMenu());
				System.out.println("Going Back");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// TEMPORARY CODE END -------------------------
	}
	
	public void closeThings(){
		try {
			tempAud.remove();
			src.close();
			dest.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class DrawPanel extends JPanel{
		int std = 0, stf = 0, stj = 0, stk = 0;
		
		public DrawPanel(){
			this.repaint();
		}
		
		public void drawMap(Graphics g){
			int place = this.getWidth()/6;
			ArrayList<Note> notes = tempBeats.getMap();
			for(Note n : notes){
				g.setColor(Color.BLUE);
				g.fillRect(place*n.position,5*(totime - n.time) - 5*n.length, place,5*n.length);
			}
		}
		
		public void paintComponent(Graphics g){
			int place = this.getWidth()/6;
			super.paintComponent(g);
			g.setColor(Color.BLACK);
			g.drawLine(place, 0, place, this.getHeight());
			g.drawLine(place*2, 0, place*2, this.getHeight());
			g.drawLine(place*3, 0, place*3, this.getHeight());
			g.drawLine(place*4, 0, place*4, this.getHeight());
			g.drawLine(place*5, 0, place*5, this.getHeight());
			g.fillRect(0, 0, place, ins*5);
			g.fillRect(place, 0, place, ind*5);
			g.fillRect(place*2, 0, place, inf*5);
			g.fillRect(place*3, 0, place, inj*5);
			g.fillRect(place*4, 0, place, ink*5);
			g.fillRect(place*5, 0, place, inl*5);
			drawMap(g);
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// Find which key was pressed, and if it isn't being held, set the initial time to the current time
		if(arg0.getKeyChar() == 's'){
			if(!bs){
				bes = totime;
			}
			bs = true;
		}
		if(arg0.getKeyChar() == 'd'){
			if(!bd){
				bed = totime;
			}
			bd = true;
		}
		if(arg0.getKeyChar() == 'f'){
			if(!bf){
				bef = totime;
			}
			bf = true;
		}
		if(arg0.getKeyChar() == 'j'){
			if(!bj){
				bej = totime;
			}
			bj = true;
		}
		if(arg0.getKeyChar() == 'k'){
			if(!bk){
				bek = totime;
			}
			bk = true;
		}
		if(arg0.getKeyChar() == 'l'){
			if(!bl){
				bel = totime;
			}
			bl = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// Find which key was released, set the minimum length of note, create the note in the beatmap, and reset vars.
		if(arg0.getKeyChar() == 's'){
			if(ins < 5)
				ins = 5;
			tempBeats.add(ins, 0, bes + (int)Calibrate.avg);
			ins = 0;
			bes = 0;
			bs = false;
		}
		if(arg0.getKeyChar() == 'd'){
			if(ind < 5)
				ind = 5;
			tempBeats.add(ind, 1, bed+ (int)Calibrate.avg);
			ind = 0;
			bed = 0;
			bd = false;
		}
		if(arg0.getKeyChar() == 'f'){
			if(inf < 5)
				inf = 5;
			tempBeats.add(inf, 2, bef+ (int)Calibrate.avg);
			inf = 0;
			bef = 0;
			bf = false;
		}
		if(arg0.getKeyChar() == 'j'){
			if(inj < 5)
				inj = 5;
			tempBeats.add(inj, 3, bej + (int)Calibrate.avg);
			inj = 0;
			bej = 0;
			bj = false;
		}
		if(arg0.getKeyChar() == 'k'){
			if(ink < 5)
				ink = 5;
			tempBeats.add(ink, 4, bek + (int)Calibrate.avg);
			ink = 0;
			bek = 0;
			bk = false;
		}
		if(arg0.getKeyChar() == 'l'){
			if(inl < 5)
				inl = 5;
			tempBeats.add(inl, 5, bel + (int)Calibrate.avg);
			inl = 0;
			bel = 0;
			bl = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) { }

}
