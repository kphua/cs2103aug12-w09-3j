import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// Events to be printed in the following order
// desc | start time | end time | date | venue | priority | tagDesc
// completeStatus will not be shown to the user, only for internal tracking

// not yet added: option to add notes to entry

class Entry implements Serializable, Comparable<Entry> {
	/**
	 * Entry class implements serializable for input/output stream in Storage
	 * class so as to preserve the object properties of Entry
	 */
	private static final long serialVersionUID = 1L;
	private String description;
	private String priority;
	private ArrayList <String> hashTags;
	private String venue;
	//	String date;w
	private String tagDesc;
	private String startTime, endTime;
	private int completeStatus;
	private Calendar dueDate;

	// constructor
	public Entry() {
	}

	//clone constructor
	public Entry(Entry copy){
		description = copy.description;
		priority = copy.priority;
		hashTags = copy.hashTags;
		venue = copy.venue;
		dueDate = copy.dueDate;
		startTime = copy.startTime;
		endTime = copy.endTime;
		completeStatus = copy.completeStatus;
	}

	public void print(){
		System.out.printf("%s\t%d\n", getDate(), description);
	}

	public void printDetailed(){
		System.out.printf("%s\t%s\t%s%t", getDate(), venue, description);
	}

	public void iniDDate() {
		dueDate = Calendar.getInstance();
	}

	public String getDesc() {
		return description;
	}

	// description of event/task
	public void setDesc(String desc) {
		this.description = desc;
	}

	public String getStart() {
		return startTime;
	}

	// start time (displayed in 12h format)
	public void setStart(String start) {
		this.startTime = start;
	}

	public String getEnd() {
		return endTime;
	}

	// end time (displayed in 12h format)
	public void setEnd(String end) {
		this.endTime = end;
	}

	public String getDate() {
		int day, month, year;
		if(dueDate != null){
			day = dueDate.get(Calendar.DAY_OF_MONTH);
			month = dueDate.get(Calendar.MONTH) + 1;
			year = dueDate.get(Calendar.YEAR);
			String date = Integer.toString(day);
			date = date.concat("/"+Integer.toString(month));
			date = date.concat("/"+Integer.toString(year));

			return date;
		}
		else{
			return null;
		}
	}

	// date displayed as e.g. Mon, Dec 14, 2012
	//	public void setDate(String _date) {
	//
	//		String[] dateArr = _date.split("/");
	//		
	//		int month = Integer.parseInt(dateArr[1]);
	//	    int day = Integer.parseInt(dateArr[0]);
	//	    int year = Integer.parseInt(dateArr[2]);
	//
	//		Calendar cal = Calendar.getInstance();
	//		cal.set(year, month - 1, day);
	//		Date date = cal.getTime();
	//		// Transformation of the date
	//		SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd, yyyy");
	//		this.date = sdf.format(date);
	//
	//	}

	//setDate for calendar
	public void setDateCal(String newdate){

		Date date = null;
		try {
			date = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(newdate);
		} catch (ParseException e) {}

		dueDate.setTime(date);

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

	// priority, identified by HIGH/MED/LOW
	public void setPriority(String priority) {
		this.priority = priority;
	}

	public int getCompleteStatus() {
		return completeStatus;
	}

	// completed (1 is completed, 0 is uncompleted)
	public void setCompleteStatus(int completeStatus) {
		this.completeStatus = completeStatus;
	}

	// for printing of entries
	public String toString() {
		String converted;

		converted = description + " "; 
		if(startTime!=null) converted = converted.concat("from " + startTime + " ");
		if(endTime != null) converted = converted.concat("to " + endTime + " ");
		if(dueDate != null) converted = converted.concat("on " + getDate() + " ");
		if(venue != null) converted = converted.concat(venue + " ");
		if(priority != null) converted = converted.concat(priority + " "); 
		if(tagDesc != null) converted = converted.concat(tagDesc);

		return converted;
	}

	@Override
	public int compareTo(Entry entry) {

		int i;

		if(dueDate == null || entry.dueDate == null || dueDate.equals(entry.dueDate)){

			if(dueDate!=null && entry.dueDate == null) return 1;
			if(dueDate==null && entry.dueDate != null) return -1;

			if(priority == null || entry.priority == null || priority.equals(entry.priority)){
				if(priority!=null && entry.priority == null) return -1;
				if(priority==null && entry.priority != null) return 1;
				if(description.equals(entry.description)){
					return 0;
				}
				else{
					i = description.compareTo(entry.description);
				}
			}
			else{
				if(priority.equals("high")) return 1;
				if(priority.equals("low")) return -1;
				if(entry.priority.equals("high")) return -1;
				else return 1;
			}
		} else i = dueDate.compareTo(entry.dueDate);

		return i;



	}

	public void setDate(Date date) {
		dueDate.setTime(date);
	}

	public ArrayList<String> getHashTags() {
		return hashTags;
	}

	public void setHashTags(ArrayList<String> hashTags) {
		this.hashTags = hashTags;
	}

	public String getTagDesc() {
		return tagDesc;
	}

	public void setTagDesc(String tagDesc) {
		this.tagDesc = tagDesc;
	}
}
