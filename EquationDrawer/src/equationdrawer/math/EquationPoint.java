package equationdrawer.math;

import java.util.ArrayList;
import java.util.function.DoubleUnaryOperator;

import equationdrawer.program.ScreenImage;

public class EquationPoint {
	
	private double curX;
	private double curY;
	private double curT;
	private int color;
	
	private CoordinateUpdater equationX;
	private CoordinateUpdater equationY;
	
	private ArrayList<Trail> trails;
	
	
	public interface CoordinateUpdater {
		double updateCoordinate(double x, double y, double t);
	}
	
	public class Trail {
		
		public static final double MAX_SIZE = 0.5;
		
		private double x1;
		private double y1;
		private double x2;
		private double y2;
		private int last;
		private final int totalTime;
		
		private int color;
		private final float colorDeltaRed;
		private final float colorDeltaGreen;
		private final float colorDeltaBlue;
		
		public Trail(double x1, double y1, double x2, double y2, int last) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.last = last;
			this.totalTime = last;
			this.color = EquationPoint.this.color;
			colorDeltaRed = (float)( (color >> 16) & 255)/totalTime;
			colorDeltaGreen = (float)( (color >> 8) & 255)/totalTime;
			colorDeltaBlue = (float)( (color) & 255)/totalTime;
		}
		
		public int countDown( ) {
			var newRed = ((int)((color >> 16) - colorDeltaRed)) << 16;
			var newGreen = ((int)(((color >> 8) & 255 ) - colorDeltaGreen)) << 8;
			var newBlue = ((int)(((color) & 255 ) - colorDeltaBlue));
			
			//System.out.println(color);
			//var a = 0/0;
			color = newRed | newGreen | newBlue;
			
			return --last;
		}
		
		public double getX1() {
			return x1;
		}
		
		public double getY1() {
			return y1;
		}
		
		public double getX2() {
			return x2;
		}
		
		public double getY2() {
			return y2;
		}
		
		public int getColor() {
			return color;
		}
	}
	
	public EquationPoint(double initX, double initY, double initT, CoordinateUpdater equationX, CoordinateUpdater equationY, int color) {
		this.curX = initX;
		this.curY = initY;
		this.curT = initT;
		
		this.equationX = equationX;
		this.equationY = equationY;
		this.color = color;
		
		trails = new ArrayList<Trail>();
	}
	
	public void update(double t) {
		var oldX = curX;
		var oldY = curY;
		var tempX = equationX.updateCoordinate(curX, curY, t);
		var tempY = equationY.updateCoordinate(curX, curY, t);
		this.curX = tempX;
		this.curY = tempY;
		curT = t;
		
		for (int i = trails.size() - 1; i >= 0; i--) {
			if (trails.get(i).countDown() < 0) {
				trails.remove(i);
			}
		}
		if ((oldX - curX) * (oldX - curX) + (oldY - curY) * (oldY - curY) <= Trail.MAX_SIZE * Trail.MAX_SIZE )
			trails.add(new Trail(oldX, oldY, curX, curY, 10));
		else
			trails.add(new Trail(curX+0.01, curY+0.01, curX, curY, 10));
	}
	
	public double getX() {
		return curX;
	}
	
	public double getY() {
		return curY;
	}
	
	public double getT() {
		return curT;
	}
	
	public int getColor() {
		return color;
	}
	
	public ArrayList<Trail> getTrails() {
		return trails;
	}
	
	@Override
	public String toString() {
		return String.format("(%.5f, %.5f, %.5f)", curX, curY, curT);
	}

}
