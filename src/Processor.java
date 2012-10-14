import java.util.Hashtable;
import java.util.Scanner;
import java.io.*;

public class Processor {

	private Hashtable <String, String> reservedWordsConverter;
	private Hashtable <String, String> reservedWordsConverterEditMode;
	private File reservedWords, reservedWordsEditMode;

	private static final String ERROR_MSG_INVALID_INPUT = "Invalid Input.";


	//loads reserved words into hashTables
	public Processor() throws FileNotFoundException{
		reservedWords = new File("reservedWords.txt");
		reservedWordsEditMode = new File("reservedWordsEditMode.txt");

		BufferedReader reader = new BufferedReader(new FileReader(reservedWords));
		Scanner sc = new Scanner(reader);
		reservedWordsConverter = new Hashtable<String, String>();
		reservedWordsConverterEditMode = new Hashtable<String, String>();

		while(sc.hasNext()){
			reservedWordsConverter.put(sc.next(), sc.next());
		}

		reader = new BufferedReader(new FileReader(reservedWordsEditMode));

		while(sc.hasNext()){
			reservedWordsConverterEditMode.put(sc.next(), sc.next());
		}

		sc.close();
	}


	public CMD translateToCMD(String userInput) {

		String[] temp = userInput.split(" ");
		if(!reservedWordsConverter.contains(temp[0])) {
			return (new CMD(COMMAND_TYPE.ERROR, ERROR_MSG_INVALID_INPUT));
		}
		else{
			String cmd = reservedWordsConverter.get(temp[0]);
			COMMAND_TYPE userCMD = determineCommandType(cmd);

			switch(userCMD){
			// Add a command to end edit mode			
			/*				case ADD:
					create entry
					return newCMD(userCMD, entry);
			 *note case when user only enters .add alone
			 */

			case DISPLAY:
				if(temp.length == 1) return new CMD(userCMD, null);
				else{
					if(temp.length > 2){
						String searchCriteria = mergeString(temp, 1, temp.length);
						return new CMD(userCMD, searchCriteria);
					} else {
						if(isInteger(temp[1])){
							Integer i = Integer.parseInt(temp[1]);
							return new CMD(userCMD, i);
						}
						return new CMD(userCMD, temp[1]);
					}
				}
			
			case EDIT:
				if(temp.length == 1) return new CMD(userCMD, null);
				else {
					
					if(isInteger(temp[1])){
						Integer i = Integer.parseInt(temp[1]);
						return new CMD(userCMD, i);
					}
					else{
						return new CMD(userCMD, null);
					}
				}

			case REMOVE: 
				if(temp.length==1) return new CMD(userCMD, null);
				if(isInteger(temp[1])){
					Integer i = Integer.parseInt(temp[1]);
					return new CMD(userCMD, i);
				}
				else{
					return new CMD(userCMD, temp[1]);
				}
			case UNDO: return new CMD(userCMD, null);
			case QUIT: return new CMD(userCMD, null);	
			default:
				return new CMD(userCMD, ERROR_MSG_INVALID_INPUT);
			}
		}
	}


	private boolean isInteger(String string) {
		try{
			Integer i = Integer.parseInt(string);
			return true;
		}
		catch(NumberFormatException e){
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

	enum COMMAND_TYPE {
		ADD, REMOVE, UNDO, DISPLAY, EDIT, QUIT, ERROR, HELP
	};

	private COMMAND_TYPE determineCommandType(String commandString) {
		if (commandString.equalsIgnoreCase(".add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandString.equalsIgnoreCase(".remove")) {
			return COMMAND_TYPE.REMOVE;
		} else if (commandString.equalsIgnoreCase(".display")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (commandString.equalsIgnoreCase(".undo")) {
			return COMMAND_TYPE.UNDO;
		} else if (commandString.equalsIgnoreCase(".edit")) {
			return COMMAND_TYPE.EDIT;
		} else if (commandString.equalsIgnoreCase(".quit")) {
			return COMMAND_TYPE.QUIT;
		} else if (commandString.equalsIgnoreCase(".help")) {
			return COMMAND_TYPE.HELP;	
		} else
			return COMMAND_TYPE.ERROR;
	}
}
