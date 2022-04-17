package equationdrawer.events.handlers;

import java.awt.event.WindowEvent;

import java.awt.event.WindowListener;
import java.util.List;

import equationdrawer.events.Event;

public class WindowEventHandler extends EventHandler implements WindowListener{

	public WindowEventHandler(List<Event> eventQueue) {
		super(eventQueue);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		//System.out.println("hello");
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
