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
			/*				
  					case DISPLAY:
					if(temp.length = 1) return new CMD(userCMD, null);
					else{
						if it starts with # -> hashtag
						if it is a number -> index
						if array length is > 2, then need to merge string. String is search criteria  
					}
			 */
			/*				case EDIT:
 					//set Edit under control to true
					//if tempList in Control/Storage = null/size=0
					// return DISPLAY, null
					// display each field on numbered lines
			 */
			//				case REMOVE:
			//  
			//				case UNDO: return new CMD(userCMD, UNDO MSG);
			//				case QUIT: return new CMD(userCMD, QUIT MSG);	
			default:
				break;
			}
		}

		return null;		//to be removed
	}

	enum COMMAND_TYPE {
		ADD, REMOVE, UNDO, DISPLAY, EDIT, QUIT, ERROR
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
		} else
			return COMMAND_TYPE.ERROR;
	}
}
