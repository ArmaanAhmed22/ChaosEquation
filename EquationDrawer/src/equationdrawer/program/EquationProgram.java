package equationdrawer.program;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import equationdrawer.events.Event;
import equationdrawer.events.Event.EventType;
import equationdrawer.events.handlers.ComponentEventHandler;
import equationdrawer.events.handlers.KeyBoardEventHandler;
import equationdrawer.events.handlers.MouseEventHandler;
import equationdrawer.events.handlers.WindowEventHandler;
import equationdrawer.events.handlers.MouseEventHandler.MouseChange;
import equationdrawer.events.handlers.MouseEventHandler.MouseChange.Modifier;
import equationdrawer.math.EquationPoint;
import equationdrawer.math.EquationPoint.CoordinateUpdater;
import equationdrawer.math.EquationPointGroup;

public class EquationProgram extends Program {
	
	EquationPointGroup pGroup;
	private double zoom = 100;
	
	private double originX = 2;
	private double originY;
	
	private double a = 0;
	
	private int pGroupNumber = 1000;
	private CoordinateUpdater xCU;
	private CoordinateUpdater yCU;
	
	private ImagesSaver saver;
	private boolean recording = false;
	private boolean equationPaused = true;
	
	private double getA() { 
		
		return a;
	}

	public EquationProgram(Dimension size, int fps, int ups) {
		super(fps, ups);
		
		recording = true;
		
		saver = new ImagesSaver("out", "png");
		
		addEventHandler(KeyBoardEventHandler.class);
		addEventHandler(MouseEventHandler.class);
		addEventHandler(ComponentEventHandler.class);
		ehm.bind(EventType.KEY_PRESS, (Event e) -> {
			int key = (int)e.getAssociatedData();
			switch (key) {
			case 90: //Z
				zoom-=0.05;
				break;
			case 88: //X
				zoom+=0.05;
				break;
			case 39: //RIGHT
				a+=0.01;
				break;
			case 37:
				a-=0.01;
				break;
			case 38: //UP
				pGroupNumber+=10;
				createPGroup(pGroupNumber);
				break;
			case 40:
				pGroupNumber-=10;
				createPGroup(pGroupNumber);
				break;
			case 82: //r
				if (recording) {
					saver.consumeImages();
				}
				recording = !recording;
				
			}
			System.out.println(key);
		});
		ehm.bind(EventType.MOUSE_MOVE, (Event e) -> {
			var data = (MouseChange)e.getAssociatedData();
			if (data.modifier == Modifier.DRAG) {
				var dX = data.after.x - data.before.x;
				var dY = data.after.y - data.before.y;
				originX-=(dX+0d)/zoom;
				originY+=(dY+0d)/zoom;
				
			}
		});
		ehm.bind(EventType.SCREEN_RESIZE, (Event e) -> {
			
			super.adjustSize();
		});
		
		/*xCU = (double x, double y, double t) -> (Math.sin(getA()*y*t - getA()*t*t*x)+1)*Math.sin(x)*(Math.pow(Math.E, Math.cos(t)) - 2*Math.cos(4*t) - Math.pow(Math.sin(t/12), 5+t))+x;
		yCU = (double x, double y, double t) -> Math.sin(Math.cosh(getA()*x*y+t))*Math.cos(t-x)*(Math.pow(Math.E, Math.sin(Math.cosh(x))) - 2*Math.cos(4*t) - Math.pow(Math.sin(t/12), 5-x));
		xCU = (double x, double y, double t) -> Math.sin(t)*(Math.pow(Math.E, Math.cos(t)) - 2*Math.cos(4*t) - Math.pow(Math.sin(t/12), 5));
		yCU = (double x, double y, double t) -> Math.cos(t)*(Math.pow(Math.E, Math.cos(t)) - 2*Math.cos(4*t) - Math.pow(Math.sin(t/12), 5));*/
		xCU = (double x, double y, double t) -> Math.cos(x*x+t-y);
		yCU = (double x, double y, double t) -> Math.sin(t-y)+Math.sqrt(t)-t*Math.sin(Math.sinh(t));
		
		
		
		createPGroup(pGroupNumber);
		pGroup.setTimeScale(0.1);
		
		
	}
	
	private void createPGroup(int numPoints) {
		pGroup = new EquationPointGroup(getTime() / 100f, 0, Math.PI*3, numPoints, xCU, yCU);
	}

	@Override
	public void onUpdate() {
		ehm.checkAndPerformAction();
		pGroup.update(getTime() / 100f);
		
	}
	
	private double pointToPixelX(double x, double pixelPerX) {
		var pixelsFromOriginX = x * pixelPerX;
		var pixelsLeftToOrigin = getSize().width / 2;
		var pixelX = pixelsLeftToOrigin + pixelsFromOriginX;
		return pixelX;
	}
	
	private double pointToPixelY(double y, double pixelPerY) {
		var pixelsFromOriginY = y * pixelPerY;
		var pixelsTopToOrigin = getSize().height / 2;
		var pixelY = pixelsTopToOrigin - pixelsFromOriginY;
		return pixelY;
	}
	
	private int pointToIndex(double x, double y, double pixelPerX, double pixelPerY) {
		
		var pixelX = pointToPixelX(x, pixelPerX);
		var pixelY = pointToPixelY(y, pixelPerY);
		if (pixelX < getSize().width && pixelX >= 0 && pixelY < getSize().height && pixelY >= 0)
			return getScreenImage().getIndex((int)pixelX, (int)pixelY);
		else
			return -1;
	}

	@Override
	public void onRender(Graphics g) {
		// TODO Auto-generated method stub
		
		getScreenImage().fill(0, 0, 0);
		for (var p : pGroup.getPoints()) {
			var index = pointToIndex(p.getX()-originX, p.getY()-originY, zoom, zoom);
			if (index != -1)
				getScreenImage().setPixel(index, p.getColor());
			
			for (var trail : p.getTrails()) {
				var tX1 = (int)pointToPixelX(trail.getX1()-originX, zoom);
				var tY1 = (int)pointToPixelY(trail.getY1()-originY, zoom);
				var tX2 = (int)pointToPixelX(trail.getX2()-originX, zoom);
				var tY2 = (int)pointToPixelY(trail.getY2()-originY, zoom);
				getScreenImage().drawLineAdditive(tX1, tY1, tX2, tY2, trail.getColor());
			}
		}
		
		
		
		g.drawImage(getScreenImage(), 0, 0, null);
		g.setColor(Color.white);
		if (recording)
			saver.saveImage(getScreenImage());
		
	}
	
	public static void main(String[] args) {
		new EquationProgram(new Dimension(500,500), 60, 60).start();
	}

}
