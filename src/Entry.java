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
	private String ID;
	private String description;
	private String priority;
	private ArrayList <String> hashTags;
	private String venue;
	private String tagDesc;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Calendar getDueDate() {
		return dueDate;
	}

	public void setDueDate(Calendar dueDate) {
		this.dueDate = dueDate;
	}

	public Calendar getFrom() {
		return from;
	}

	public void setFrom(Calendar from) {
		this.from = from;
	}

	private int completeStatus;
	private Calendar dueDate, from;


	// constructor
	public Entry() {
		ID = assignID();
	}

	/**
	 * @return 
	 * 
	 */
	private String assignID() {
		Calendar cal = Calendar.getInstance();
		String year = ((Integer)cal.get(Calendar.YEAR)).toString();
		String month = ((Integer)cal.get(Calendar.MONTH)).toString();
		String day = ((Integer)cal.get(Calendar.DAY_OF_MONTH)).toString();
		String hr = ((Integer)cal.get(Calendar.HOUR_OF_DAY)).toString();
		String min = ((Integer)cal.get(Calendar.MINUTE)).toString();
		String sec = ((Integer)cal.get(Calendar.SECOND)).toString();
		return year.concat(month+day+hr+min+sec);
	}

	//clone constructor
	public Entry(Entry copy){
		ID = copy.ID;
		description = copy.description;
		priority = copy.priority;
		hashTags = copy.hashTags;
		venue = copy.venue;
		dueDate = copy.dueDate;
		from = copy.from;
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
		if(from!=null) converted = converted.concat("from " + from.get(Calendar.DATE)+"/"+from.get(Calendar.MONTH)+"/"+from.get(Calendar.YEAR)+ " ");
		if(from!=null) converted = converted.concat(from.get(Calendar.HOUR_OF_DAY)+"."+ from.get(Calendar.MINUTE) + " " + from.get(Calendar.AM_PM) + " ");
		if(dueDate != null) converted = converted.concat("to " + getDate() + " "); 
		if(dueDate != null) converted = converted.concat(dueDate.get(Calendar.HOUR_OF_DAY)+"."+ dueDate.get(Calendar.MINUTE) + " " + dueDate.get(Calendar.AM_PM) + " ");
		if(venue != null) converted = converted.concat(venue + " ");
		if(priority != null) converted = converted.concat(priority + " "); 
		if(tagDesc != null) converted = converted.concat(tagDesc);

		return converted;
	}
	
	public String getFromString(){
		if(from==null) return "-";
		String converted = "";
		converted = converted.concat("from " + from.get(Calendar.DATE)+"/"+from.get(Calendar.MONTH)+"/"+from.get(Calendar.YEAR)+ " ");
		converted = converted.concat(from.get(Calendar.HOUR_OF_DAY)+"."+ from.get(Calendar.MINUTE) + " " + from.get(Calendar.AM_PM) + " ");
		return converted;
	}
	public String getToString(){
		if(from==null) return "-";
		String converted = "";
		if(dueDate != null) converted = converted.concat("to " + getDate() + " "); 
		if(dueDate != null) converted = converted.concat(dueDate.get(Calendar.HOUR_OF_DAY)+"."+ dueDate.get(Calendar.MINUTE) + " " + dueDate.get(Calendar.AM_PM) + " ");
		return converted;
	}
	

	@Override
	public int compareTo(Entry entry) {

		int i;

		if(ID == entry.getID()) return 0;

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

	public void setDate(Calendar startTime, Calendar endTime,
			Calendar startDate, Calendar endDate) {
		boolean st = startTime != null;
		boolean et = endTime != null;
		boolean sd = startDate != null;
		boolean ed = endDate != null;
		Calendar endFinal, startFinal;
		
		if(!(st || sd || et || ed)){
			dueDate = null;
			from = null;
			return;
		}
		
		if(ed && sd && endDate.after(startDate)) {
			endFinal = endDate;
			endDate = startDate;
			startDate = endFinal;
			endFinal = endTime;
			endTime = startTime;
			startTime = endFinal;
			endFinal = null;
		}
		
		if(et){
			if(ed){
				endFinal = mergeCal(endTime, endDate);
			}
			else{
				endFinal = mergeCal(endTime, Calendar.getInstance());
			}
		} else {
			if(ed){
				endFinal = mergeCal(endTime, endDate);
			}
			else{
				endFinal = null;
			}
		}
		
		if(st){
			if(sd){
				startFinal = mergeCal(startTime, startDate);
			}
			else{
				startFinal = mergeCal(startTime, Calendar.getInstance());
			}
		} else {
			if(sd){
				startFinal = mergeCal(startTime, startDate);
			}
			else{
				startFinal = null;
			}
		}
		
		if(endFinal==null && startFinal!=null){
			endFinal = startFinal;
			startFinal = null;
			return;
		}
		
		dueDate = endFinal;
		from = startFinal;
		
		
	}
	
	public Calendar mergeCal(Calendar time, Calendar date) {
		if(time==null && date==null) return null;
		if(date==null){
			date = Calendar.getInstance();
			time.set(Calendar.YEAR, date.get(Calendar.YEAR));
			time.set(Calendar.MONTH, date.get(Calendar.MONTH));
			time.set(Calendar.DATE, date.get(Calendar.DATE));
			time.set(Calendar.AM_PM, date.get(Calendar.AM_PM));
			if(time.after(date)) {
				date.add(Calendar.DATE, 1);
			}
		}
		
		if(date.get(Calendar.YEAR)<2000) date.add(Calendar.YEAR, 2000);
		
		if(time==null) {
			date.set(Calendar.HOUR, 0);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
		}
		else{
			date.set(Calendar.HOUR, time.get(Calendar.HOUR));
			date.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
			date.set(Calendar.SECOND, time.get(Calendar.SECOND));
			date.set(Calendar.AM_PM, time.get(Calendar.AM_PM));
		}
		
		return date;
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

	public String getID(){
		return ID;
	}


}
