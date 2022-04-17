package equationdrawer.program;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImagesSaver {
	
	private ArrayList<String> imageNames;
	private String dir;
	private String extension;
	
	
	public ImagesSaver(String dir,String extension) {
		
		this.dir = dir;
		this.extension = extension;
		
		imageNames = new ArrayList<String>();
		
		clearDir();
		
	}
	
	private void clearDir() {
		ProcessBuilder rm = new ProcessBuilder("/bin/bash","-c","rm"+" *."+extension);
		rm.directory(new File("out"));
		rm.inheritIO();
		try {
			
			rm.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveImage(BufferedImage img,String name) {
		new Thread(() -> {
			try {
				ImageIO.write(img, extension, new File(dir+"/"+name+"."+extension));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
		
		imageNames.add(name);
	}
	
	public void saveImage(BufferedImage img) {
		var name = "";
		for (var i = 0; i<8-(""+(imageNames.size()+1)).length();i++) name+="0";
		name+=imageNames.size()+1;
		System.out.println(name);
		try {
			ImageIO.write(img, extension, new File(dir+"/"+name+"."+extension));
			
			imageNames.add(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void consumeImages() {
		imageNames.clear();
		String all = "";
		for (var name : imageNames) {
			all+=" out/"+name+"."+extension;
		}
		ProcessBuilder ffmpeg = new ProcessBuilder("ffmpeg -framerate 24 -i"+all+" out.mp4");
		//ffmpeg.directory(new File("out/"));
		ffmpeg.inheritIO();
		try {
			ffmpeg.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//clearDir();
	}

}
