// Events to be printed in the following order
// desc | start time | end time | date | venue | priority | tagDesc
// completeStatus will not be shown to the user, only for internal tracking

// not yet added: option to add notes to entry

class Entry {
	String desc;
	String priority;
	String tagDesc;
	String venue;
	int date;
	int startTime, endTime;
	int completeStatus;
	String newEntry;
	
	// constructor
	public Entry() {
		
	}
	
	public String getDesc() {
		return desc;
	}
	
	// description of event/task 
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getStart() {
		return startTime;
	}
	
	//start time (displayed in 24h format)
	public void setStart(int start) {
		this.startTime = start;
	}

	public int getEnd() {
		return endTime;
	}
	
	//end time (displayed in 24h format)
	public void setEnd(int end) {
		this.endTime = end;
	}

	public int getDate() {
		return date;
	}
	
	// date displayed as ddmmyyyy
	public void setDate(int date) {
		this.date = date;
	}
	
	public String getVenue() {
		return venue;
	}
	
	// venue of the event, identified by @location
	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getPriority() {
		return priority;
	}

	// priority, identified by !H/M/L for high/medium/low
	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getTagDesc() {
		return tagDesc;
	}
	
	// hashtag desc, identified by #tagname
	public void setTagDesc(String tagDesc) {
		this.tagDesc = tagDesc;
	}

	public int getCompleteStatus() {
		return completeStatus;
	}
	
	//completed (1 is completed, 0 is uncompleted)
	public void setCompleteStatus(int completeStatus) {
		this.completeStatus = completeStatus;
	}

}
