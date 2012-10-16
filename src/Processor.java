import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;
import java.io.*;

public class Processor {

	private Hashtable <String, String> reservedWordsConverter;
	private Hashtable <String, String> reservedWordsConverterEditMode;
	private Hashtable <String, Integer> indicativeWordsIdentifier;
	private File reservedWords, reservedWordsEditMode, indicativeWords;

	private static final String ERROR_MSG_INVALID_INPUT = "Invalid Input.";
	

	//loads reserved words into hashTables
	public Processor() throws FileNotFoundException{
		reservedWords = new File("reservedWords.txt");
		reservedWordsEditMode = new File("reservedWordsEditMode.txt");
		indicativeWords = new File("indicativeWOrds.txt");

		BufferedReader reader = new BufferedReader(new FileReader(reservedWords));
		Scanner sc = new Scanner(reader);
		reservedWordsConverter = new Hashtable<String, String>();
		reservedWordsConverterEditMode = new Hashtable<String, String>();
		indicativeWordsIdentifier = new Hashtable<String, Integer>();

		while(sc.hasNext()){
			reservedWordsConverter.put(sc.next(), sc.next());
		}

		reader = new BufferedReader(new FileReader(reservedWordsEditMode));

		while(sc.hasNext()){
			reservedWordsConverterEditMode.put(sc.next(), sc.next());
		}
		
		reader = new BufferedReader(new FileReader(indicativeWords));
		
		while(sc.hasNext()){
			indicativeWordsIdentifier.put(sc.next(), sc.nextInt());
		}

		sc.close();
	}


	public CMD translateToCMD(String userInput) {

		String[] temp = userInput.split(" ", 2);
		
		//check if input is valid
		if(temp.length==0){ 
			return new CMD(COMMAND_TYPE.ERROR, " ");
		}

		//check if first word is a command
		temp[0] = temp[0].toLowerCase();
		if(!reservedWordsConverter.containsKey(temp[0])) {
			return (new CMD(COMMAND_TYPE.ERROR, ERROR_MSG_INVALID_INPUT));
		}

		String cmd = reservedWordsConverter.get(temp[0]);
		COMMAND_TYPE userCMD = determineCommandType(cmd);

		switch(userCMD){
		
		case ADD:
			if(temp.length == 1) return new CMD(userCMD, null);			//add <nothing>
			else {
				Entry newTask = new Entry();
				buildEntry(newTask, temp);
				return new CMD(userCMD, newTask);						//add <long string of data>
			}
		
		case DONE:
			if(temp.length > 1 && isInteger(temp[2])){
				Integer i = Integer.parseInt(temp[2]);
				return new CMD(userCMD, i);
			}
			else return new CMD(COMMAND_TYPE.ERROR, ERROR_MSG_INVALID_INPUT);
		
		case DISPLAY:
			if(temp.length == 1) {								//display <nothing>
				return new CMD(userCMD, null);
			}
			else{
				if(temp.length > 2){							//display <long string>
					String searchCriteria = mergeString(temp, 1, temp.length);
					return new CMD(userCMD, searchCriteria);
				} else {
					if(isInteger(temp[1])){						//display <number>
						Integer i = Integer.parseInt(temp[1]);
						return new CMD(userCMD, i);
					}
					return new CMD(userCMD, temp[1]);			//display <hash/search criteria>
				}
			}

		case EDIT:
			if(temp.length == 1) {								//edit <nothing>
				return new CMD(userCMD, null);					
			}
			else {

				if(isInteger(temp[1])){							//edit <number>
					Integer i = Integer.parseInt(temp[1]);
					return new CMD(userCMD, i);
				}
				else{
					return new CMD(userCMD, null);				//input is invalid, but assumes user wants to edit something
				}
			}

		case REMOVE: 
			if(temp.length==1) {								//remove <nothing>
				return new CMD(userCMD, null);
			}
			if(isInteger(temp[1])){								//remove <number>
				Integer i = Integer.parseInt(temp[1]);
				return new CMD(userCMD, i);
			}
			else{
				return new CMD(userCMD, temp[1]);				//remove <hash>
			}
		case UNDO: return new CMD(userCMD, null);
		case QUIT: return new CMD(userCMD, null);	
		case HELP: return new CMD(userCMD, null);
		default:
			return new CMD(userCMD, ERROR_MSG_INVALID_INPUT);
		}

	}

	
	//INCOMPLETE
	private void buildEntry(Entry newTask, String[] data) {
		
		String input = data[1];
		String[] desc = input.split("\"");
		newTask.setDesc(desc[1]);  // get the exp in "..."
		String[] temp2 = null;
		if (desc.length > 2) {
			temp2 = desc[2].split(" ");
			for (int i=0; i<temp2.length; i++) {
				if (temp2[i].contains("am") || temp2[i].contains("pm")) {
					if (newTask.getStart() == null) {
						newTask.setStart(temp2[i]);
					}
					else
						newTask.setEnd(temp2[i]);
				}
				else if (temp2[i].contains("/") && temp2[i].split("/").length == 3) {
						newTask.setDate(temp2[i]);
				}
				else if (temp2[i].startsWith("@")) {
					newTask.setVenue(temp2[i]);
				}
				else if (temp2[i].startsWith("#")) {
					newTask.setTagDesc(temp2[i]);
				}
				else if (temp2[i].equalsIgnoreCase("HIGH") || temp2[i].equalsIgnoreCase("MED") 
						|| temp2[i].equalsIgnoreCase("LOW")) {
					newTask.setPriority(temp2[i]);
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
//							// TODO Auto-generated catch block
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
			Integer i = Integer.parseInt(string);
			return true;
		}
		catch(NumberFormatException e){
			return false;
		}
	}
	
	private boolean isDate(String s){
		Date date = null;
		try {
			date = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(date==null) return false;
		else return true;
	}
	
	
	private String mergeString(String[] temp, int i, int length) {
		String mergedString = temp[i];
		for(i+=1; i<length; i++){
			mergedString = mergedString.concat(" "+temp[i]);
		}
		return mergedString;
	}

	enum COMMAND_TYPE {
		ADD, REMOVE, UNDO, DISPLAY, EDIT, QUIT, ERROR, HELP, DONE
	};

	private COMMAND_TYPE determineCommandType(String commandString) {
		if (commandString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandString.equalsIgnoreCase("remove")) {
			return COMMAND_TYPE.REMOVE;
		} else if (commandString.equalsIgnoreCase("display")) {
			return COMMAND_TYPE.DISPLAY;
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
		} else
			return COMMAND_TYPE.ERROR;
	}
}
