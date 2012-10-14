import java.io.FileNotFoundException;
import java.util.*;

class FingerTips {

	public static void main(String args[]) throws FileNotFoundException {
		String userInput;
		CMD actionMSG;
		Scanner sc = new Scanner(System.in);
		boolean cont = true;
		Control control = null;
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
		
		// print welcome/help msg etc.
		System.out.println("Welcome To FingerTips!");
		System.out.println("Enter .help for further usage instructions.");
		
		//print current items
		
		
		while (cont) {
			System.out.print("Command: ");
			userInput = sc.nextLine();
			actionMSG = control.performAction(userInput);
			switch(actionMSG.getCommandType()){
				//other cases
			case ADD: 
				if(actionMSG.getData()==null){
					System.out.println("Please enter a description for your task:");
					String description = sc.nextLine();
					description = "add".concat(description);
					actionMSG = control.performAction(description);
				}
				
				System.out.println("Add further information? y/n");
				String answer = sc.nextLine();
				while(!(answer.equals("y") || answer.equals("n"))){
					System.out.println("Invalid answer.");
					System.out.println("Add further information? y/n");
					answer = sc.nextLine();
				}
				
				if(answer.equals("y")){
					//edit
					
				}
				break;
			case REMOVE: break;
			case UNDO: break;
			case DISPLAY: break;
			case EDIT: break;
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
		
		sc.close();
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
