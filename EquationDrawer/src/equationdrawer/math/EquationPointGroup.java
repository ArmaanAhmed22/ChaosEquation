package equationdrawer.math;

import java.util.ArrayList;

import equationdrawer.math.EquationPoint.CoordinateUpdater;

public class EquationPointGroup {
	
	private final EquationPoint[] points;
	
	private double prevT;
	private double scale = 1;
	
	public EquationPointGroup(double programT, double tStart, double tEnd, int numPoints, CoordinateUpdater cX, CoordinateUpdater cY) {
		points = new EquationPoint[numPoints];
		
		for (var i = 0; i < numPoints; i++) {
			var curT = (tEnd - tStart) / numPoints * (i+1) + tStart;
			points[i] = new EquationPoint(curT, curT, curT, cX, cY,(int) (Math.random()*(255 << 16 | 255 << 8 | 255)));
		}
		
		prevT = programT;
		
	}
	
	public void setTimeScale(double scale) {
		this.scale = scale;
	}
	
	public void update(double curT) {
		for (var point : points) {
			point.update(point.getT()+(curT - prevT) * scale);
		}
		prevT = curT;
	}
	
	public EquationPoint[] getPoints() {
		return points;
	}

}
