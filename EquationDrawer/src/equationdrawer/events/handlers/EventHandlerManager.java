package equationdrawer.events.handlers;

import java.awt.Component;

import java.awt.Container;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EventListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

import javax.swing.JFrame;

import equationdrawer.events.Event;
import equationdrawer.events.Event.EventType;

public class EventHandlerManager {
	
	private EnumMap<Event.EventType, Consumer<Event>> eventBindings;
	private List<EventHandler> eventHandlers;
	protected List<Event> eventQueue;
	
	private List<Event> tempEventQueue;
	
	private boolean verbose;
	
	private HashSet<Event> disallowedEvents;
	
	protected List<Event> getNewEventQueue(String fullListPath,Object enclosing) {
		List<Event> out = null;
		try {
			if (enclosing!=null)
				out = (List<Event>)Class.forName(fullListPath).getDeclaredConstructor(enclosing.getClass()).newInstance(enclosing);
			else
				out = (List<Event>)Class.forName(fullListPath).getConstructor().newInstance();
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}
	
	public EventHandlerManager(String fullListPath,Class<?> enclosing) {
		disallowedEvents = new HashSet<Event>();
		eventBindings = new EnumMap<Event.EventType,Consumer<Event>>(Event.EventType.class);
		eventHandlers = new ArrayList<EventHandler>();
		eventQueue = getNewEventQueue(fullListPath,enclosing);
		verbose = false;
		
	}
	
	public EventHandlerManager() {
		this("java.util.ArrayList",null);
	}
	
	public EventHandlerManager(boolean verbose) {
		this();
		this.verbose = verbose;
		
		
	}
	
	public <T extends EventHandler & EventListener> void addEventHandler(Class<T> ehClass, Component c) {
		try {
			var ehConstructor = ehClass.getConstructor(List.class);
			EventHandler eh = ehConstructor.newInstance(eventQueue);
			getComponentListenerMethod(ehClass).invoke(c, eh);
			if (verbose)
				System.out.println("EventHandlerManager added \""+ehClass.getName()+"\" onto \""+c.getClass().getName()+"\"");
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private <T extends EventHandler & EventListener> Method getComponentListenerMethod(Class<T> ehClass) throws ClassNotFoundException {
		var methodNames = new ArrayList<String>(EventHandler.eventListenerTypes);
		methodNames.replaceAll((s) -> "add"+s);
		
		for (Method method : JFrame.class.getMethods()) {
			if (!methodNames.contains(method.getName()))
				continue;
			if (!(method.getParameterCount() == 1))
				continue;
			if (method.getParameters()[0].getType().isAssignableFrom(ehClass)) {
				return method;
			}

		}
		throw new ClassNotFoundException("Could not find class");
	}
	
	public EventHandlerManager bind(Event.EventType e, Consumer<Event> c) {
		eventBindings.put(e, c);
		return this;
	}
	
	public void performAction(Event e) {
		try {
			eventBindings.get(e.getEventType()).accept(e);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void checkAndPerformAction() {
		disallowedEvents.clear();
		synchronized (eventQueue) {
			for (Event event : eventQueue) {
				if (disallowedEvents.contains(event))
					continue;
				else
					disallowedEvents.add(event);
				performAction(event);
			}
			eventQueue.clear();
		}
	}
	

}
