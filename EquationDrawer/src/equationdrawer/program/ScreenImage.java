package equationdrawer.program;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public final class ScreenImage extends BufferedImage {
	
	private int[] imageBuffer;


	public ScreenImage(int width, int height) {
		super(width, height, TYPE_INT_RGB);
		
		imageBuffer = ((DataBufferInt)this.getRaster().getDataBuffer()).getData();
	}
	
	public void setPixel(int index, int r, int g, int b) {
		imageBuffer[index] = getHex(r, g, b);
	}
	
	public void setPixelAdditive(int index, int r, int g, int b) {
		var curColor = imageBuffer[index];
		
		var curR = curColor >> 16;
		var curG = (curColor >> 8) & 255;
		var curB = curColor & 255;
		
		imageBuffer[index] = getHex(Math.min(curR + r, 255), Math.min(curG + g, 255), Math.min(curB + b, 255));
	}
	
	public void setPixelAdditive(int x, int y, int r, int g, int b) {
		var index = getIndex(x, y);
		var curColor = imageBuffer[index];
		
		var curR = curColor >> 16;
		var curG = (curColor >> 8) & 255;
		var curB = curColor & 255;
		
		imageBuffer[index] = getHex(Math.min(curR + r, 255), Math.min(curG + g, 255), Math.min(curB + b, 255));
	}
	
	public void setPixelAdditive(int index, int hex) {
		var curColor = imageBuffer[index];
		
		var r = hex >> 16;
		var g = (hex >> 8) & 255;
		var b = hex & 255;
		
		var curR = curColor >> 16;
		var curG = (curColor >> 8) & 255;
		var curB = curColor & 255;
		
		imageBuffer[index] = getHex(Math.min(curR + r, 255), Math.min(curG + g, 255), Math.min(curB + b, 255));
	}
	
	public void setPixelAdditive(int x, int y, int hex) {
		var index = getIndex(x, y);
		var curColor = imageBuffer[index];
		
		var r = hex >> 16;
		var g = (hex >> 8) & 255;
		var b = hex & 255;

		var curR = curColor >> 16;
		var curG = (curColor >> 8) & 255;
		var curB = curColor & 255;
		
		imageBuffer[index] = getHex(Math.min(curR + r, 255), Math.min(curG + g, 255), Math.min(curB + b, 255));
	}
	
	public void setPixel(int x, int y, int r, int g, int b) {
		imageBuffer[getIndex(x, y)] = getHex(r, g, b);
	}
	
	public void setPixel(int x, int y, int hex) {
		imageBuffer[getIndex(x,y)] = hex;
	}
	
	public void setPixel(int index, int hex) {
		imageBuffer[index] = hex;
	}
	
	public void fill(int r, int g, int b) {
		Arrays.fill(imageBuffer, getHex(r,g,b));
	}
	
	public int getIndex(int x, int y) {
		return y*getWidth()+x;
	}
	
	
	public static int getHex(int r, int g, int b) {
		return r << 16 | g << 8 | b;
	}
	
	public void drawLine(int x1, int y1, int x2, int y2, int hex) {
		double slope = ((float)y2 - y1) / ((float) (x2 - x1));
		for (var x = x1; x <=x2; x++) {
			var y = slope * (x - x1) + y1;
			setPixel(x, (int)y, hex);
		}
	}
	
	public void drawLineAdditive(int x1, int y1, int x2, int y2, int hex) {
		if (x1 >= getWidth() && x2 >= getWidth() || x1 < 0 && x2 < 0)
			return;
		if (Math.abs(((float)y2 - y1) / ((float) (x2 - x1))) < 1) {
			var firstX = x1;
			var firstY = y1;
			var secondX = x2;
			var secondY = y2;
			if (x2 < x1) {
				firstX = x2;
				firstY = y2;
				secondX = x1;
				secondY = y1;
			}
			double slope = ((float)secondY - firstY) / ((float) (secondX - firstX));
			if (secondX >= getWidth())
				secondX = getWidth();
			for (var x = Math.max(firstX, 0); x <=secondX; x++) {
				var y = slope * (x - firstX) + firstY;
				if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight())
					setPixelAdditive(x, (int)y, hex);
			}
		} else {
			var firstX = y1;
			var firstY = x1;
			var secondX = y2;
			var secondY = x2;
			if (y2 < y1) {
				firstX = y2;
				firstY = x2;
				secondX = y1;
				secondY = x1;
			}
			double slope = ((float)secondY - firstY) / ((float) (secondX - firstX));
			if (secondX >= getWidth())
				secondX = getWidth();
			for (var x = Math.max(firstX, 0); x <=secondX; x++) {
				var y = slope * (x - firstX) + firstY;
				if (y >= 0 && y < getWidth() && x >= 0 && x < getHeight())
					setPixelAdditive((int)y, x, hex);
				
			}
		}
	}

}
