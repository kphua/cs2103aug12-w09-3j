import java.io.FileNotFoundException;
import java.util.*;

class FingerTips {

	Control control;
	Scanner sc;
	boolean cont;
	
	public FingerTips(){
		sc = new Scanner(System.in);
		cont = true;
		
		try{
			control = new Control();
		}
		catch(FileNotFoundException e){
			System.out.println("Fatal Error. Critical files are missing.");
			System.out.println("Re-install Program or contact your service provider.");
			System.out.println("Program will now terminate.");
			System.out.println("Press Enter to continue.");
			sc.nextLine();
			System.exit(0);
		}
	}
	
	public static void main(String args[]) throws FileNotFoundException {
		FingerTips ft = new FingerTips();
		ft.run();
	}

	private void run() {
		
		printWelcomeMSG();
		
		String userInput;
		
		while (cont) {
			System.out.print("Command: ");
			userInput = sc.nextLine();
			runUserInput(userInput);
		}
	}

	private void printWelcomeMSG() {
		System.out.println("Welcome To FingerTips!");
		System.out.println("Enter \"help\" for further usage instructions.");
	}

	private void runUserInput(String userInput) {
		CMD actionMSG = control.performAction(userInput);
		switch(actionMSG.getCommandType()){
		case ADD: 
			if(actionMSG.getData()==null){
				System.out.println("Please enter a description for your task:");
				String description = sc.nextLine().trim();
				description = "add".concat(" " + description);
				actionMSG = control.performAction(description);
			}
			
			System.out.println("Add further information? y/n");
			String answer = sc.nextLine();
			answer = answer.toLowerCase();
			while(!(answer.equals("y") || answer.equals("n"))){
				System.out.println("Invalid answer.");
				System.out.println("Add further information? y/n");
				answer = sc.nextLine();
			}
			
			if(answer.equals("y")){
				runUserInput("edit");
			} else {
				
			}
			break;
		case REMOVE: break;
		case UNDO: break;
		case DISPLAY: break;
		case EDIT: 										//incomplete
			if(actionMSG.getData()==null){
				while(true){
					System.out.println("Enter the field you wish to modify, and the new data to replace with.");
					
				}
			}
			else {
				System.out.println(actionMSG.getData());
				while(sc.hasNextInt()){
					//check input is a number, and is not larger than the size of tempList/activeList
				}
				//take the integer and load the specified entry it into tempHold
				
			}
			
			break;
		case DONE: break;
			
			case HELP:
				help();
				break;
			case QUIT: 
				cont = false;
				System.out.println("Goodbye.");
				break;
			default: System.out.println(actionMSG.getData());
		}
	}
	
	private static void help() {
		System.out.println(".a/.add to add, .r/.remove to remove, .e/.edit to edit");
		System.out.println(".u/.undo to undo, .d/.display to display, .q/.quit to quit");
		System.out.println(".h/.help to display help for FingerTips");
	}

	public static void showToUser(String text) {
		System.out.println(text);
	}
}
