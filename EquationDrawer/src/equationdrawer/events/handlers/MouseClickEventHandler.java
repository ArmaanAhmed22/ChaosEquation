package equationdrawer.events.handlers;

import java.awt.event.MouseEvent;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import equationdrawer.events.Event;
import equationdrawer.events.Event.EventType;

public class MouseClickEventHandler extends EventHandler implements MouseListener {

	public MouseClickEventHandler(List<Event> eventQueue) {
		super(eventQueue);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		addEvent(new Event(EventType.MOUSE_CLICK, e.getPoint()));
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
