import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Logger;

public class Processor {

	private Hashtable <String, String> reservedWordsConverter;
	private Hashtable <String, String> reservedWordsConverterEditMode;
	private Hashtable <String, String> indicativeWordsIdentifier;
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
		
		logger.info("Processor Initialised.");
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
//		if(temp.length <= 1 || temp[1].equals("\"") || temp[1].equals("\" ") || temp[1].equals("\" \"") ||
//		temp[1].startsWith("\"  ") || !temp[1].startsWith("\"")) 
//			return new CMD(COMMAND_TYPE.ERROR, ERROR_MSG_ADD_PROPER_FORM);
		
		//add <nothing> or add without " "
		if(temp.length <= 1 || !temp[1].startsWith("\""))
			return new CMD(COMMAND_TYPE.ERROR, ERROR_MSG_ADD_PROPER_FORM);
		//add <nothing> or add without " "
		
		String[] arr = temp[1].split("\"", 3); //arr[1] == desc arr[2]==the rest
		arr[1] = arr[1].trim();
		
		// add only a single "
		if(temp[1].startsWith("\"") && !temp[1].endsWith("\""))
			return new CMD(COMMAND_TYPE.ERROR, ERROR_MSG_ADD_PROPER_FORM);
		
		if(arr[1].length()<1)
			return new CMD(COMMAND_TYPE.ERROR, ERROR_MSG_ADD_PROPER_FORM);
		//add <nothing> or add without " "
		
		else {
			Entry newTask = new Entry();
			buildEntry(newTask, temp);
			return new CMD(userCMD, newTask);						//add <long string of data>
		}
	}


	//INCOMPLETE
	private void buildEntry(Entry newTask, String[] data) {
		
		String input = data[1];
		String[] desc = input.split("\"");
		newTask.setDesc(desc[1].trim());  // get the exp in "..."
//		String[] temp2 = null;
//		if (desc.length > 2) {
//			desc[2] = desc[2].trim();
//			temp2 = desc[2].split(" ");
//			for (int i=0; i<temp2.length; i++) {
//				if (temp2[i].toLowerCase().contains("am") || temp2[i].toLowerCase().contains("pm")) {
//					if (newTask.getStart() == null) {
//						newTask.setStart(temp2[i]);
//					}
//					else
//						newTask.setEnd(temp2[i]);
//				}
//				else if (isDate(temp2[i])) {
//					newTask.iniDDate();
//					newTask.setDateCal(temp2[i]);
//				}
//				else if (temp2[i].startsWith("@")) {
//					newTask.setVenue(temp2[i]);
//				}
//				else if (temp2[i].startsWith("#")) {
//					newTask.setTagDesc(temp2[i]);
//				}
//				else if (temp2[i].equalsIgnoreCase("HIGH") || temp2[i].equalsIgnoreCase("MED") 
//						|| temp2[i].equalsIgnoreCase("LOW")) {
//					newTask.setPriority(temp2[i].toUpperCase());
//				}
//			}
//		}
		String[] temp2 = null;
		if (desc.length > 2) {
			desc[2] = desc[2].trim();
			temp2 = desc[2].split(" ");
			int rep = 0;
			for (int i=0; i<temp2.length; i++) {
				if(indicativeWordsIdentifier.containsKey(temp2[i].toLowerCase())) 
					rep = Integer.parseInt((indicativeWordsIdentifier.get(temp2[i])));
				else 
					rep = 8;
				
				switch (rep){
				// incomplete
				case 9: 
					if(isDate(temp2[i+1])){
						newTask.iniDDate();
						newTask.setDateCal(temp2[i+1]);
					}
					break;
				case 10: break;
				
				// today
				case 11: 						
					newTask.iniDDate();
					newTask.setDateCal(newTask.getDate());
					break;
				
				// tomorrow
				case 12: break;
				
				// all other cases
				case 8: 
					if (temp2[i].toLowerCase().contains("am") || temp2[i].toLowerCase().contains("pm")) {
						if (newTask.getStart() == null) {
							newTask.setStart(temp2[i]);
						}
						else {
							newTask.setEnd(temp2[i]);
						}
					}
					else if (temp2[i].startsWith("#")) {
						newTask.setTagDesc(temp2[i]);
					}
					else if (temp2[i].startsWith("@")) {
						newTask.setVenue(temp2[i]);
					}
					else if (temp2[i].equalsIgnoreCase("HIGH") || temp2[i].equalsIgnoreCase("MED") 
						|| temp2[i].equalsIgnoreCase("LOW")) {
						newTask.setPriority(temp2[i].toUpperCase());
					}
					break;
				}
			}
		}
		
//		LinkedList<String> dataList = new LinkedList<String>();
//		String description;
//		int rep;
//		for(int i=1; i<data.length; i++){
//			if(indicativeWordsIdentifier.contains(data[i].toLowerCase()))
//				rep = indicativeWordsIdentifier.get(data[i]);
//			else rep = 8;
//			
//			switch (rep){
//			case 9: 
//				if(i+1<data.length){
//					if(isDate(data[i+1])){
//						try {
//							newTask.setDate(new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(data[i+1]));
//						} catch (ParseException e) {
//							e.printStackTrace();
//						}
//					} else if(indicativeWordsIdentifier.get(i+1)<8){
//						Calendar cal = Calendar.getInstance();
//						int target = indicativeWordsIdentifier.get(i+1);
//						if(cal.get(cal.DAY_OF_WEEK) < target){
//							cal.add(cal.DAY_OF_MONTH, target - cal.get(cal.DAY_OF_WEEK));
//						}
//						else{
//							cal.add(cal.DAY_OF_MONTH, 7 - cal.DAY_OF_WEEK + target);
//						}
//					}
//					else if(indicativeWordsIdentifier.get(i+1) == 10 && i+2 < data.length){
//						/***********************************/
//					}
//				}
//				break;
//			case 10: break;
//			case 11: break;
//			case 8: 
//				if(data[i].startsWith("#")) newTask.getHashTags().add(data[i]);
//				if(data[i].startsWith("@")) newTask.setVenue(data[i].substring(1));
//				break;
//			}
//		}
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
	boolean isDate(String s){
		String[] strArr = s.split("/");
		if(strArr.length != 3) return false;
		boolean dayFalse = Integer.parseInt(strArr[0]) > 31 || Integer.parseInt(strArr[0]) < 1;
		boolean monthFalse = Integer.parseInt(strArr[1]) > 12 || Integer.parseInt(strArr[1]) < 1;
		boolean yearFalse = Integer.parseInt(strArr[2]) > 2100 || Integer.parseInt(strArr[2]) < 1900;
		
		if(dayFalse || monthFalse || yearFalse) return false;
		
		try {
			@SuppressWarnings("unused")
			Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(s);
			return true;
		} catch (ParseException e) {
			return false;
		}
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
}
