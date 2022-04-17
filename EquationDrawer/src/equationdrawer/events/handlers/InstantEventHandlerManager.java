package equationdrawer.events.handlers;

import java.util.ArrayList;


import equationdrawer.events.Event;

public class InstantEventHandlerManager extends EventHandlerManager {
	
	public InstantEventHandlerManager() {
		super();
	}
	
	public void initQueue() {
		this.eventQueue = getNewEventQueue("mandelbrot.events.handlers.InstantEventHandlerManager$InstantEventHandlerManagerArrayList",this);
	}
	
	public class InstantEventHandlerManagerArrayList extends ArrayList<Event> {
		
		public InstantEventHandlerManagerArrayList() {
			super();
		}
		
		@Override
		public boolean add(Event e) {
			var result = super.add(e);
			checkAndPerformAction();
			return result;
		}
	}

}
