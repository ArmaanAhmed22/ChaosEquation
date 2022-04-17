package equationdrawer.events;

public class Event {
	
	private final EventType eventType;
	private final Object associatedData;
	
	public enum EventType {
		MOUSE_MOVE,KEY_PRESS,SCREEN_RESIZE,MOUSE_CLICK;
	}
	
	public Event(EventType eventType,Object associatedData) {
		this.eventType = eventType;
		this.associatedData = associatedData;
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public Object getAssociatedData() {
		return associatedData;
	}
	
	/*@Override
	public boolean equals(Object o) {
		Event object;
		if (!(o instanceof Event))
			return false;
		object = (Event) o;
		if (!this.eventType.equals(object.eventType))
			return false;
		var info1 = this.getAssociatedData().getInfo();
		var info2 = object.getAssociatedData().getInfo();
		if (info1 == null && info2 == null)
			return true;
		else if (info1 == null)
			return false;
		return info1.equals(info2);
		
	}*/
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((associatedData == null) ? 0 : associatedData.hashCode());
		result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (associatedData == null) {
			if (other.associatedData != null)
				return false;
		} else if (!associatedData.equals(other.associatedData))
			return false;
		if (eventType != other.eventType)
			return false;
		return true;
	}
	
	

}
