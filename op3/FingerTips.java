import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

class FingerTips {


	private static final String MSG_WELCOME = "\nWelcome To FingerTips!\n";
	private static final String MSG_DEFAULT_ASSISTANCE = "Enter \"help\" for further usage instructions.";
	
	private static final String SUCCESS_MSG_ADD = "Added.\n";
	private static final String SUCCESS_MSG_REMOVE = "Removed.\n";
	private static final String SUCCESS_MSG_DONE = "Entry marked as done and shifted to archive.\n";
	private static final String SUCCESS_MSG_CLEAR = "All active entries deleted.\n";
	private static final String SUCCESS_MSG_EXIT = "Goodbye.\n";
	
	private static final Logger logger = Logger.getLogger(FingerTips.class.getName());
	private static final String logFile = "runLog.log";
	private static final Level handlerLevel = Level.OFF;
	private static final Level loggerLevel = Level.OFF;
	
	private Control control;
	private Scanner sc;
	private boolean cont;
	

	public FingerTips(){
		initialiseLogger();
		
		sc = new Scanner(System.in);
		cont = true;
		control = Control.getInstance();
		
		logger.info("Initialization Complete.");
	}

	private void initialiseLogger() {
		try {
			Handler h = new FileHandler(logFile);
			h.setFormatter(new SimpleFormatter());
			h.setLevel(handlerLevel);
			logger.addHandler(h);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.setLevel(loggerLevel);
		logger.info("Logger initialization complete.");
	}

	public static void main(String args[]) throws FileNotFoundException {
		FingerTips ft = new FingerTips();
		ft.run();
	}

	private void run() {

		printWelcomeMSG();

		String userInput;

		while (cont) {
			System.out.print("\nCommand: ");
			userInput = sc.nextLine();
			userInput = userInput.trim();
			System.out.println();
			runUserInput(userInput);
		}
	}

//	private void runUserInput(String userInput) {
	public String runUserInput(String userInput) {
		CMD actionMSG = control.performAction(userInput);
		
		logger.info(actionMSG.toString());
		
		//followUpAction(actionMSG);
		String output = followUpAction(actionMSG);
		return output;
	}

	
//	private void followUpAction(CMD actionMSG) {
//		switch(actionMSG.getCommandType()){
//		case ADD: 		add(actionMSG); 		break;
//		case REMOVE: 	remove(actionMSG);		break;
//		case UNDO: 		undo(actionMSG);		break;
//		case DISPLAY:	display(actionMSG);		break;
//		case EDIT:		edit(actionMSG);		break;
//		case DONE: 		done(actionMSG);		break;
//		case HELP: 		help();					break;
//		case QUIT: 		quit();					break;
//		case CLEAR:		clear();				break;
//		case ERROR:		error(actionMSG);		break;
//		default: undo(actionMSG);
//		}
//	}
	
	private String followUpAction(CMD actionMSG) {
		switch(actionMSG.getCommandType()){
		case ADD: 		add(actionMSG); 		break;
		case REMOVE: 	remove(actionMSG);		break;
		case UNDO: 		undo(actionMSG);		break;
		case DISPLAY:	display(actionMSG);		break;
		case EDIT:		edit(actionMSG);		break;
		case DONE: 		done(actionMSG);		break;
		case HELP: 		help();					break;
		case QUIT: 		quit();					break;
		case CLEAR:		clear();				break;
		case ERROR:		error(actionMSG);		break;
		default: undo(actionMSG);
		
		}	
		return "incomplete";
	}
	
	//Add action
	private void add(CMD actionMSG) {
		if(actionMSG.getData()==null){
			System.out.println("Please enter a description for your task:");
			String description = sc.nextLine().trim();
			description = "add \"".concat(description);
			control.performAction(description);
		}

		//			System.out.println("Add further information? y/n");
		//			String answer = sc.nextLine();
		//			answer = answer.toLowerCase();
		//			while(!(answer.equals("y") || answer.equals("n"))){
		//				System.out.println("Invalid answer.");
		//				System.out.println("Add further information? y/n");
		//				answer = sc.nextLine();
		//			}
		//			
		//			if(answer.equals("y")){
		//				runUserInput("edit");
		//			} else {
		
		control.setTempHold(null);
		Collections.sort(control.getStorage().getActiveEntries());
		System.out.println(SUCCESS_MSG_ADD);
		
		//			}
	}
	
	//Edit Mode
	private void edit(CMD actionMSG) {
		String userInput;
		if(actionMSG.getData()==null){

			while(true){
				System.out.println("Entry: ");
				System.out.println(control.processEditMode("display")[1]);

				System.out.println("Enter the field you wish to modify, and the new data to replace with.");
				System.out.println("Type \"end\" to exit edit mode and \"help\" for futher assistance.");
				System.out.print("\nCommand (Edit Mode): ");
				userInput = sc.nextLine();

				userInput = userInput.trim();
				//call processor
				String[] response = control.processEditMode(userInput);
				if(response[0].equals("display")) System.out.println(response[1]);
				else if(response[0].equals("help")) helpEditMode();
				else if(response[0].equals("end")) break;
				else if(response[0].equals("Error")) System.out.println(response[1]);
			}

			control.setTempHold(null);
		}
		else {
			undo(actionMSG);
			int a;
			while(true){
				try{
					System.out.print("Index: ");
					a = sc.nextInt();
					sc.nextLine();
					System.out.println();
					if(control.getStorage().getActiveEntries().size() < a)
						System.out.println("Invalid input. Enter a valid index number.");
					else{
						runUserInput("edit "+ a);
						break;
					}
				}
				catch(InputMismatchException e){
					System.out.println("Invalid input. Action aborted.");
					break;
				}
			}
		}
	}

	private void display(CMD actionMSG) {
		ArrayList<Entry> print = (ArrayList<Entry>) actionMSG.getData();
		if(print.isEmpty()){
			System.out.println("There is nothing to print.");
		}
		else{
			int j=1;

			for (int i=0; i<print.size(); i++) {
				System.out.println(j + ". " + print.get(i).toString());
				j++;
			}
		}
	}

	private void remove(CMD actionMSG) {
		if(actionMSG.getData() == null){
			System.out.println("Which entry do you want to remove?");
			int rmvIndex;
			try{
				System.out.print("Index: ");
				rmvIndex = sc.nextInt();
				String newInstruction = "remove ".concat(Integer.toString(rmvIndex));
				control.performAction(newInstruction);
				System.out.println();
				
			} catch(InputMismatchException e){
				System.out.println("Invalid input. Action aborted.");

			}

			sc.nextLine();
		}
		System.out.println(SUCCESS_MSG_REMOVE);
	}

	//Follow up for DONE
		private void done(CMD actionMSG) {
			System.out.println(SUCCESS_MSG_DONE);
		}
	
	//Prints action taken.
	private void undo(CMD actionMSG) {
			Collections.sort(control.getStorage().getActiveEntries());
		System.out.println(actionMSG.getData());
	}
	
	//Prints SUCCESS MSG for Clear
	private void clear() {
		System.out.println(SUCCESS_MSG_CLEAR);
	}

	//Ending MSG
	private void quit() {
		cont = false;
		//System.out.println(SUCCESS_MSG_EXIT);
		System.exit(0);
	}
	
	
	//Print ERROR msg
	//Assumes:	CMD.data is a String
	private void error(CMD actionMSG) {
		System.out.println(actionMSG.getData());
	}
	
	//Welcome Message
	private void printWelcomeMSG() {
		System.out.println(MSG_WELCOME);
		System.out.println(MSG_DEFAULT_ASSISTANCE);
	}
	
	//normalMode help
	private void help() {
		System.out.println("add <data>:\t\t   add an entry with related dates, description, priority etc.");
		System.out.println("\t\t\t   prefix @ indicates venue, prefix # indicates a hashtag.");
		System.out.println("remove <number>:\t   remove the selected entry for the active list.");
		System.out.println("edit <number>:\t\t   enters edit mode for selected entry.");
		System.out.println("undo:\t\t\t   reverses the previous action.");
		System.out.println("display:\t\t   shows the activelist.");
		System.out.println("display <search criteria>: generates a list of entries fulfilling the search criteria.");
		System.out.println("done <number>:\t\t   marks an entry as completed");
		System.out.println("clear:\t\t\t   deletes all entries permanently (use with caution!)");
		System.out.println("quit:\t\t\t   terminates the program.");
//		return ("add <data>:\t\t   add an entry with related dates, description, priority etc.\n") +
//				("\t\t   prefix @ indicates venue, prefix # indicates a hashtag.\n") +
//				("remove <number>:\t   remove the selected entry for the active list.\n") +
//				("edit <number>:\t   enters edit mode for selected entry.\n") +
//				("undo:\t\t   reverses the previous action.\n") +
//				("display:\t\t   shows the activelist.\n") +
//				("display <search criteria>:\t   generates a list of entries fulfilling the search criteria.\n") +
//				("done <number>:\t   marks an entry as completed.\n") +
//				("clear:\t\t   deletes all entries permanently (use with caution!).\n") +
//				("quit:\t\t   terminates the program.\n");
	}

	//editMode help
	private void helpEditMode() {
		System.out.println("\nEnter a field followed by the new data it should be replaced with.");
		System.out.println("desc:     edit description.");
		System.out.println("ddate:    edit due date.");
		System.out.println("display:  shows data in the current node.");
		System.out.println("priority: edit priority");
		System.out.println("hash #:   edit hash tags");
		System.out.println("st:       edit start time");
		System.out.println("et:       edit end time");
		System.out.println("venue @:  edit venue");
//		return ("\nEnter a field followed by the new data it should be replaced with.\n") +
//				("desc:\t edit description\n") +
//				("ddate:\t edit due date\n") +
//				("display:\t shows data in the current node\n") +
//				("priority:\t edit priority\n") +
//				("hash #:\t edit hash tags\n") +
//				("st:\t edit start time\n") +
//				("et:\t edit end time\n") +
//				("venue @:\t edit venue\n");
	}
	
	//Public Methods

	public static void showToUser(String text) {
		System.out.println(text);
	}
	
	//for logger initialization use
	public static Logger getLoggingParent(){
		return logger;
	}
}
