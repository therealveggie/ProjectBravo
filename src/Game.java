/**
 * This is the game. GV tells me it's the game. I'm not sure personally, but I sort of have to trust him on this
 * 
 * @author Yuting, Ganashsai
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.ArrayList;
public class Game extends JPanel implements ActionListener, KeyListener, GlobalVar{
    public static ArrayList<Song> songs = new ArrayList<Song>();
    public static RotatingMenu gameMenu = new RotatingMenu();
    private Timer timer = new Timer(10,this);
    private int buffer;
    private Song song;
    private int max_index;
    private int timeHeld[] = new int[6];
    private int currentNote[] = new int[6];
    private int score;
    private JLabel scoreBar;
    private int pos;
    
    private static BufferedImage image;

    
    public static void GameSetup(){
    	songs = new ArrayList<Song>();
        File folder = new File("Songs");
        try {
        System.out.println(folder.getCanonicalPath());
        } catch(Exception mother) {}
        for (final File fileEntry : folder.listFiles()){
            if(fileEntry.isDirectory()){
                songs.add(new Song(fileEntry.getName()));
            }
        }
        for (Song i: songs){
            gameMenu.add_button(i.title);
        }
        
        try{
            image= ImageIO.read(new File("Assets/background.png"));
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        
        frame.add(gameMenu);
        gameMenu.revalidate();
        gameMenu.repaint();
    }
    /* 
    public Game()
    {
    	this.setLayout(null);
        addKeyListener(this);
        setLayout(null);
        score=0;
        scoreBar = new JLabel("Score: "+Integer.toString(Math.abs(score)));
        scoreBar.setLocation(100, 100);
    }
   
    public void startGame(Song song)
    {
    	
    	this.song=song;
    	song.addMap();
        for (int i=0;i<6;i++)
            currentNote[i]=-1;
        timer.start();
        this.repaint();
        this.requestFocus();
        this.repaint();
        this.revalidate();

    }
    */
    public Game(Song song){
        frame.clear();
        frame.add(this);
        //System.out.println("playing: "+song.title);
        this.song=song;
        this.setLayout(null);
        addKeyListener(this);
        setLayout(null);
        //why = new DrawPanel();
        this.repaint();
        this.revalidate();
        song.addMap();
        for (int i=0;i<6;i++)
            currentNote[i]=-1;
        score=0;
        timer.start();
        this.repaint();
        this.requestFocus();
        scoreBar = new JLabel("Score: "+Integer.toString(Math.abs(score)));
        scoreBar.setLocation(100, 100);
        this.add(scoreBar);
        this.repaint();
        this.revalidate();
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        //g.drawImage(image, 0, 0, null);
        
        if (g instanceof Graphics2D){
        	Graphics2D g2D=(Graphics2D) g;
        	Paint p1 = null;
        	Paint p2=null;
            for(int i=0;i<6;i++){
            	if(timeHeld[i]!=0){
            		p1 = new GradientPaint(0.0f, 0.0f, new Color(0,0,0,200), 0.0f, getHeight(), new Color(40,40,255,15));
            		p2 = new GradientPaint(0.0f, 0.0f, new Color(40,40,255,15), 0.0f, getHeight(), new Color(0,0,0,50));
            	}
            	else
            	{
            		p1 = new GradientPaint(0.0f, 0.0f, new Color(0,0,0,150), 0.0f, getHeight(), new Color(40,40,40,10));
            		p2 = new GradientPaint(0.0f, 0.0f, new Color(40,40,40,10), 0.0f, getHeight(), new Color(0,0,0,10));
            	}
            	g2D.setPaint(p1);
            	g2D.fillRect(i*100,0,100,510);
            	g2D.setPaint(p2);
            	g2D.fillRect(i*100,510,100,90);
            	
            }
            
        }
        g.setColor(Color.WHITE);
        scoreBar.setText("Score: "+Integer.toString(score));
        
        g.setColor(Color.BLUE);
        g.fillRect(50, 10, (int)(5*song.audio.percentDone()), 10);
        g.drawLine(50, 15, 550, 15);
        
        g.setColor(Color.blue);
        for (int i=0;i<song.map.size();i++){
        	if(buffer==170){}
        		//System.out.println("Switch");
        	Note n = song.map.get(i);
        	if (buffer<169)
        		pos=((16*buffer)-n.time-n.length)/3-390;
        	else
        	{
        		pos = ((song.audio.getSecondTime() - n.time-n.length)/3) +510;
        	}
        
            if( timeHeld[n.position]!=0 && pos<=510 && pos+(n.length/3)>=510)
            {
            	//System.out.println(n.time+" "+n.length+" "+n.position+" "+buffer);
            	if(n.position < 3){
            		g.setColor(Color.GREEN);
            	}
            	else{
            		g.setColor(Color.BLUE);
            	}
                g.fillRect(100*n.position,pos, 100,510-pos);
            }
            else if(pos<510 && pos+(n.length/3)>0)
            {
            	if(n.position < 3){
            		g.setColor(Color.GREEN);
            	}
            	else{
            		g.setColor(Color.BLUE);
            	}
                g.fillRect(100*n.position,pos, 100,n.length/3);
            }
            
            if (pos > 510)
                max_index=i;
        }
        g.setColor(Color.GREEN);
        g.drawLine(100, 0, 100, 600);
        g.drawLine(100*2, 0, 100*2, 600);
        g.drawLine(100*3, 0, 100*3, 600);
        g.setColor(Color.BLUE);
        g.drawLine(100*4, 0, 100*4, 600);
        g.drawLine(100*5, 0, 100*5, 600);
        //g.drawLine(0, 550, 600, 550);
        g.drawLine(0, 510, 600, 510);
    }
    

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
    	if(buffer==170)
    		song.audio.start();
    	if(arg0.getSource() == timer){
            buffer++;
    	}
        if(song.audio.percentDone()==100)
        {
            this.timer.stop();
            frame.clear();
            RotatingMenu.reset();
            songs.clear();
            gameMenu= new RotatingMenu();
            frame.add(new Score(song.title,score));
            //System.out.println("just called Score()");
        }
        //System.out.println(song.audio.percentDone());
        this.repaint();
    }
    
    public void keyPressedChecker(int index)
    {
    	//System.out.println("Key Pressed");
        for (int i=max_index;i<song.map.size();i++)
        {
            Note n= song.map.get(i);
            if (song.audio.getSecondTime()>=n.time && song.audio.getSecondTime()<=n.time+n.length && n.position==index){
            	//System.out.println("Score");
                if (Math.abs(song.audio.getSecondTime()-n.time)<=6){
                    score+=7-Math.abs(song.audio.getSecondTime()-n.time);
                }
                else
                    score+=1;
                currentNote[index]=i;
                max_index++;
                break;
            }
            
        }
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        if(arg0.getKeyChar()=='s')
        {
            if(currentNote[0]==-1) keyPressedChecker(0);
            timeHeld[0]++;
        }
        if(arg0.getKeyChar()=='d')
        {
            if(currentNote[1]==-1) keyPressedChecker(1);
            timeHeld[1]++;
        }
        if(arg0.getKeyChar()=='f')
        {
            if(currentNote[2]==-1) keyPressedChecker(2);
            timeHeld[2]++;
        }
        if(arg0.getKeyChar()=='j')
        {
            if(currentNote[3]==-1) keyPressedChecker(3);
            timeHeld[3]++;
        }
        if(arg0.getKeyChar()=='k')
        {
            if(currentNote[4]==-1) keyPressedChecker(4);
            timeHeld[4]++;
        }
        if(arg0.getKeyChar()=='l')
        {
            if(currentNote[5]==-1) keyPressedChecker(5);
            timeHeld[5]++;
        }
    }
    
    public void keyReleasedChecker(int index)
    {
        if(currentNote[index]!=-1){
            Note n = song.map.get(currentNote[index]);
            int d=10;
            if (Math.abs(song.audio.getSecondTime()-n.time)<=5){
                score+=6-Math.abs(song.audio.getSecondTime()-n.time);
            }            
        }
        timeHeld[index]=0;
        currentNote[index]=-1;
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
        if(arg0.getKeyChar()=='s')
        {
            keyReleasedChecker(0);
        }
        if(arg0.getKeyChar()=='d')
        {
            keyReleasedChecker(1);
        }
        if(arg0.getKeyChar()=='f')
        {
            keyReleasedChecker(2);
        }
        if(arg0.getKeyChar()=='j')
        {
            keyReleasedChecker(3);
        }
        if(arg0.getKeyChar()=='k')
        {
            keyReleasedChecker(4);
        }
        if(arg0.getKeyChar()=='l')
        {
            keyReleasedChecker(5);
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }
}