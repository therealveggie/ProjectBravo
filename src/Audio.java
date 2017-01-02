import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class Audio {
	private Clip clip;
	private long len = 0;
	private AudioInputStream audin;
	
	public Audio(){
		System.out.println("Empty Audio Created");
	}
	
	public Audio(String filename){
		try{
			audin = AudioSystem.getAudioInputStream(new File (filename));
			clip = AudioSystem.getClip();
			clip.open(audin);
			len = clip.getMicrosecondLength();
		}
		catch(Exception e){
			System.out.println("STRING went wrong");
			e.printStackTrace();
		}
	}
	
	public Audio(File file){
		try{
			audin = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(audin);
			len = clip.getMicrosecondLength();
		}catch(Exception e){
			System.out.println("FILE went wrong");
			e.printStackTrace();
		}
	}
	
	public void start(){
		clip.setMicrosecondPosition(0);
		clip.start();
	}
	
	public void resume(){
		clip.start();
	}
	
	public void stop(){
		clip.stop();
	}
	
	public boolean checkEnd(){
		return len==clip.getMicrosecondPosition();
	}
	
	public int percentDone(){
		return (int)(((double)clip.getMicrosecondPosition())/len*100);
	}
	
	public int getLength(){
		return (int)(len/1000000);
	}
	
	public String getTime(){
		return (len/1000000)/60 + ":" + String.format("%02d", (len/1000000)%60);
	}
	
	public void remove(){
		try {
			clip.stop();
			clip.close();
			audin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
			
}
