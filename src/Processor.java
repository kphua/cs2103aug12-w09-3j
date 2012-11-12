import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Logger;

public class Processor {

	private Hashtable <String, String> reservedWordsConverter;
	private Hashtable <String, String> reservedWordsConverterEditMode;
	private Hashtable <String, String> indicativeWordsIdentifier;
	private SimpleDateFormat[] dateChecks;
	
	private static final Logger logger = Logger.getLogger(Control.class.getName());

	private static final String ERROR_MSG_ADD_PROPER_FORM = "Error: add <description> <data>, where description is\nencapsuled by \" \".\n";
	private static final String ERROR_MSG_EDIT_INVALID_INDEX = "Error: Type \"edit\" with a valid index.\n";
	private static final String ERROR_MSG_RMV_INVALID_INPUT = "Invalid input. Input for remove should follow \"<command>\n<index>\".\n";
	private static final String ERROR_MSG_INVALID_INPUT = "Invalid Input. Input should follow a \"<command> <data>\"\nformat.\n";
	private static final String ERROR_MSG_FATAL_ERROR = "Fatal Error. Critical files are missing.\n" +
														"Re-install Program or contact your service provider.\n" +
														"Program will now terminate.\n" +
														"Press Enter to continue.\n";

	//loads reserved words into hashTables
	protected Processor() {
		logger.setParent(UI.getLoggingParent());
		logger.info("Initialising Processor");
				
		//Core files. Do Not Touch.
		String rw = "/reservedWords.txt";
		String rwem = "/reservedWordsEditMode.txt";
		String iw = "/indicativeWords.txt";
		
		reservedWordsConverter = new Hashtable<String, String>();
		reservedWordsConverterEditMode = new Hashtable<String, String>();
		indicativeWordsIdentifier = new Hashtable<String, String>();
		
		loadFromFile(rw, reservedWordsConverter);		
		loadFromFile(rwem, reservedWordsConverterEditMode);		
		loadFromFile(iw, indicativeWordsIdentifier);
		
		dateChecks = new SimpleDateFormat[10];
		loadDateCheck();
		
		logger.info("Processor Initialised.");
	}


	private void loadDateCheck() {
		dateChecks[0] = new SimpleDateFormat("d/M/y h.mma");
		dateChecks[1] = new SimpleDateFormat("d/M/y");
		dateChecks[2] = new SimpleDateFormat("d/M");
		dateChecks[3] = new SimpleDateFormat("d-M-y");
		dateChecks[4] = new SimpleDateFormat("d.M.y");
		dateChecks[5] = new SimpleDateFormat("h.mma");
		dateChecks[6] = new SimpleDateFormat("hha");
		dateChecks[7] = new SimpleDateFormat("HHmm");
		dateChecks[8] = new SimpleDateFormat("H.m");
		dateChecks[9] = new SimpleDateFormat("H:m");
	}

	//Read in saved entries from a txt file into the program.
	private void loadFromFile(String from, Hashtable<String,String> to) {	
		logger.fine("Loading " + from);
				
		InputStream is = getClass().getResourceAsStream(from);
		Scanner sc = null;
		try{
			sc = new Scanner(is);
		}catch(NullPointerException e){
			logger.severe(ERROR_MSG_FATAL_ERROR);
			System.exit(-1);
		}

		while(sc.hasNext()){
			to.put(sc.next(), sc.next());
		}

		sc.close();
		
		logger.fine("Done.");
	}

	
	//Checks if the initial input is valid for normal mode
	//Checks:
	//1. if there is no input
	//2. if the first word is a valid word
	//3. if there is any data
	//Returns true if input is bad, false if it passes all tests.
	private boolean isBadInput(String userInput) {
		if(userInput == null) return true;
		
		userInput = userInput.trim();
		
		if(userInput.length() == 0)	return true;
		
		String[] temp = userInput.split(" ", 2);
		
		assert temp.length > 0;
	
		temp[0] = temp[0].toLowerCase();
		if(!reservedWordsConverter.containsKey(temp[0])) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param userInput
	 * @return completed CMD
	 */
	public CMD translateToCMD(String userInput) {
		
		if(isBadInput(userInput)) 
			return new CMD(COMMAND_TYPE.ERROR, ERROR_MSG_INVALID_INPUT);
		
		userInput = userInput.trim();
		String[] inputBreakdown = userInput.split(" ", 2);
				
		return buildCMD(inputBreakdown);
	}


	/**
	 * @param inputBreakdown
	 * @return CMD
	 */
	private CMD buildCMD(String[] inputBreakdown) {
		
		String cmd = reservedWordsConverter.get(inputBreakdown[0].toLowerCase());
		COMMAND_TYPE userCMD = determineCommandType(cmd);
		
		if(inputBreakdown.length > 1) {
			inputBreakdown[1] = inputBreakdown[1].trim();
			if(inputBreakdown[1].length()==0) 
				inputBreakdown = new String[] {inputBreakdown[0]};
		}
		
		switch(userCMD){
		
		case ADD:
			return add(inputBreakdown, userCMD);
		case DONE:
			return done(inputBreakdown, userCMD);
		case DISPLAY: case DISPLAYP:
			return display(inputBreakdown, userCMD);
		case EDIT:
			return edit(inputBreakdown, userCMD);
		case REMOVE: 
			return remove(inputBreakdown, userCMD);
		case CLEAR:	case CLEARP: case QUIT: case HELP: case UNDO: case REDO: 
			return new CMD(userCMD, null);
		default:
			return new CMD(userCMD, ERROR_MSG_INVALID_INPUT);
		}
	}


	/**
	 * @param inputBreakdown
	 * @param userCMD
	 * @return
	 */
	private CMD remove(String[] inputBreakdown, COMMAND_TYPE userCMD) {
		if(inputBreakdown.length==1) {								//remove <nothing>
			return new CMD(Processor.COMMAND_TYPE.ERROR, ERROR_MSG_RMV_INVALID_INPUT);	
		}
		if(isInteger(inputBreakdown[1])){							//remove <number>
			Integer i = Integer.parseInt(inputBreakdown[1]);
			return new CMD(userCMD, i);
		}
		else{
			return new CMD(Processor.COMMAND_TYPE.ERROR, ERROR_MSG_RMV_INVALID_INPUT);	
		}
	}


	/**
	 * @param inputBreakdown
	 * @param userCMD
	 * @return
	 */
	private CMD edit(String[] inputBreakdown, COMMAND_TYPE userCMD) {
		if(inputBreakdown.length == 1){
			return new CMD(COMMAND_TYPE.ERROR, ERROR_MSG_EDIT_INVALID_INDEX);
		}
		
		if(isInteger(inputBreakdown[1])){							//edit <number>
			Integer i = Integer.parseInt(inputBreakdown[1]);
			return new CMD(userCMD, i);
		}
		else{
			return new CMD(COMMAND_TYPE.ERROR, ERROR_MSG_EDIT_INVALID_INDEX);
		}
	}


	/**
	 * @param inputBreakdown
	 * @param userCMD
	 * @return
	 */
	private CMD display(String[] inputBreakdown, COMMAND_TYPE userCMD) {
		if(inputBreakdown.length == 1) {								//display <nothing>
			return new CMD(userCMD, null);
		}
		else{
			if(inputBreakdown.length > 2){							//display <long string>
				String searchCriteria = mergeString(inputBreakdown, 1, inputBreakdown.length);
				return new CMD(userCMD, searchCriteria);
			} else {
				if(isInteger(inputBreakdown[1])){						//display <number>
					Integer i = Integer.parseInt(inputBreakdown[1]);
					return new CMD(userCMD, i);
				}
				return new CMD(userCMD, inputBreakdown[1]);			//display <hash/search criteria>
			}
		}
	}


	/**
	 * @param inputBreakdown
	 * @param userCMD
	 * @return
	 */
	private CMD done(String[] inputBreakdown, COMMAND_TYPE userCMD) {
		if(inputBreakdown.length > 1 && isInteger(inputBreakdown[1])){
			Integer i = Integer.parseInt(inputBreakdown[1]);
			return new CMD(userCMD, i);
		}
		else return new CMD(COMMAND_TYPE.ERROR, ERROR_MSG_INVALID_INPUT);
	}


	/**
	 * @param temp
	 * @param userCMD
	 * @return
	 */
	private CMD add(String[] temp, COMMAND_TYPE userCMD) {
		if(temp.length <= 1) {
			return new CMD(COMMAND_TYPE.ERROR, ERROR_MSG_ADD_PROPER_FORM);			//add <nothing>
		}
		
		else {
			Entry newTask = new Entry();
			buildEntry(newTask, temp[1]);
			return new CMD(userCMD, newTask);						//add <long string of data>
		}
	}

	
	private void buildEntry(Entry newTask, String data) {
		LinkedList<String> l = new LinkedList<String> (Arrays.asList(data.split(" ")));
		LinkedList<String> desc = new LinkedList<String>();
		Calendar startDate = null, endDate = null, startTime = null, endTime = null;
		
		while(!l.isEmpty()){
			String s = l.pop().trim();
			if(s.startsWith("@")) {
				newTask.setVenue(s); 
				continue;
			}
			if(s.startsWith("#")) {
				newTask.getHashTags().add(s); 	
				continue;
			}
			if(s.equalsIgnoreCase("HIGH") || s.equalsIgnoreCase("MED") || s.equalsIgnoreCase("LOW")){
				newTask.setPriority(s.toUpperCase());
			}
			l.push(s);
			
			Date newDate = parseDateTime(newTask, l, desc);
			
			if(newDate == null){
				desc.add(s);
			} else {
				Calendar cal = Calendar.getInstance();
				cal.setTime(newDate);
				if(cal.get(Calendar.YEAR)==1970) {
					if(endTime==null) endTime = cal;
					else if(startTime==null){
						startTime = cal;
					}
				} else {
					if(endDate==null) endDate = cal;
					else if(startDate==null){
						startDate = cal;
					}
				}
			}
			
		}
		
		newTask.setDate(startTime, endTime, startDate, endDate);
		newTask.setDesc(mergeString(desc));
	}


	/**
	 * @param newTask
	 * @param l
	 * Returns a date if string given is either a time or a date
	 * Returns null otherwise
	 */
	private Date parseDateTime(Entry newTask, LinkedList<String> l, LinkedList<String> desc) {
		if(l.isEmpty()) return null;
		String s = l.pop().trim();
		int type = 0;
		if(indicativeWordsIdentifier.containsKey(s.toLowerCase())){
			type = Integer.parseInt(indicativeWordsIdentifier.get(s));
		} else {
			Date date = isDate(s);
			if(date==null) date = isTime(s);
			return date;
		}
		
		assert (type>0 && type<17 && type!=8);
		
		switch(type){
		case 9:
			// "by" type words
			// recursive call handles 3 forms:
			// 1. by <date> -> returns date with proper date.
			// 2. by next <date> -> returns date with proper date.
			// 3. by (next) <non-date element> -> returns null
			Date date = parseDateTime(newTask, l, desc);			
			if(date==null){					
				l.push(s);
			}	
			return date;
		case 10:
			/* "next" type words
			*  handles:
			*  1. next <specific time> -> returns specific date
			*  2. next <time field> <specific date> -> increments time field of specific date by 1, and return
			*  3. next <non-time field or specific time element> -> returns null
			*  
			*/  
			
			int type2 = Integer.parseInt(indicativeWordsIdentifier.get(l.peek()));
			Date d = parseDateTime(newTask, l, desc);
			Calendar current = Calendar.getInstance();
			
			if(d==null){
				l.push(s);
			} else if(type2>0 && type2<8){
				
				int day = current.get(Calendar.DAY_OF_WEEK);
				if(day==type2){
					current.add(Calendar.DAY_OF_MONTH, 7);
				} else {
					current.add(Calendar.DAY_OF_MONTH, 7-day+type2);
				}
				d = current.getTime();
			
			} else if(type2>20 && type2<33){
				type2 = type2 - 20;
				int month = current.get(Calendar.MONTH);
				if(month==type2){
					current.add(Calendar.MONTH, 12);
				} else {
					current.add(Calendar.DAY_OF_MONTH, 12-month+type2);
				}
				d = current.getTime();
				
			} else if(type2>12 && type2<17){
				switch(type2){
				case 13:
					current.add(Calendar.DAY_OF_MONTH, 7); break;
				case 14:
					current.add(Calendar.MONTH, 7); break;
				case 15:
					current.add(Calendar.YEAR, 7); break;
				case 16:
					current.add(Calendar.DAY_OF_MONTH, 1); break;
				}
				d = current.getTime();
			}
			return d;
		
		case 12:
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, 1);
			return cal.getTime();
			
		case 13: case 14: case 15: case 16:
			return parseDateTime(newTask, l, desc);
			
		default: //case 1-7, case 11
			return Calendar.getInstance().getTime();
		}
		
	}



	//checks if a string can be converted into an integer
	private boolean isInteger(String string) {
		try{
			@SuppressWarnings("unused")
			Integer i = Integer.parseInt(string);
			return true;
		}
		catch(NumberFormatException e){
			return false;
		}
	}
	
	
	//Checks if a given string is a date
	Date isDate(String s){
		
		for(int i=0; i<5; i++){
			try{
				return dateChecks[i].parse(s);
			}
			catch(ParseException e){}
		}
		
		return null;
	}
	
	Date isTime(String s){
		
		for(int i=5; i<10; i++){
			try{
				return dateChecks[i].parse(s);
			}
			catch(ParseException e){}
		}
		
		return null;
	}
	
	private String mergeString(LinkedList<String> temp) {
		String mergedString = "";
		while(!temp.isEmpty()) mergedString = mergedString.concat(temp.pop()+" ");
		return mergedString;
	}
	
	private String mergeString(String[] temp, int i, int length) {
		String mergedString = temp[i];
		for(i+=1; i<length; i++){
			mergedString = mergedString.concat(" "+temp[i]);
		}
		return mergedString;
	}
	//Determines the field to be edited.
	public String[] determineCmdEditMode(String userInput){
		String[] data = userInput.split(" ", 2); 
		data[0] = data[0].toLowerCase();
		if(!reservedWordsConverterEditMode.containsKey(data[0])){
			data = new String[]{"error", "Invalid input."};
		}
		else{
			data[0] = reservedWordsConverterEditMode.get(data[0]);
		}
		
		return data;
		
	}
	
	enum COMMAND_TYPE {
		ADD, REMOVE, UNDO, DISPLAY, DISPLAYP, EDIT, QUIT, ERROR, HELP, DONE, CLEAR, CLEARP, REDO
	};

	private COMMAND_TYPE determineCommandType(String commandString) {
		if (commandString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandString.equalsIgnoreCase("remove")) {
			return COMMAND_TYPE.REMOVE;
		} else if (commandString.equalsIgnoreCase("display")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (commandString.equalsIgnoreCase("display+")) {
			return COMMAND_TYPE.DISPLAYP;
		} else if (commandString.equalsIgnoreCase("undo")) {
			return COMMAND_TYPE.UNDO;
		} else if (commandString.equalsIgnoreCase("edit")) {
			return COMMAND_TYPE.EDIT;
		} else if (commandString.equalsIgnoreCase("quit")) {
			return COMMAND_TYPE.QUIT;
		} else if (commandString.equalsIgnoreCase("help")) {
			return COMMAND_TYPE.HELP;	
		} else if (commandString.equalsIgnoreCase("done")) {
			return COMMAND_TYPE.DONE;
		} else if (commandString.equalsIgnoreCase("clear")) {
			return COMMAND_TYPE.CLEAR;
		} else if (commandString.equalsIgnoreCase("clear+")) {
			return COMMAND_TYPE.CLEARP;
		} else if (commandString.equalsIgnoreCase("redo")) {
			return COMMAND_TYPE.REDO;	
		} else
			return COMMAND_TYPE.ERROR;
	}
	
	public SimpleDateFormat getDateChecks(){
		return dateChecks[0];
	}
}
