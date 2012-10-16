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
				control.setTempHold(null);
			}
			break;
		case REMOVE: 
			System.out.println("removal done.");
			break;
		case UNDO: break;
		case DISPLAY: 
			ArrayList<String> print = new ArrayList<String>();
			print.addAll((ArrayList<String>) actionMSG.getData());
			int j=1;
			for (int i=0; i<print.size(); i++) {
				System.out.println(j + ". " + print.get(i));
				j++;
			}
			break;
		case EDIT: 										
			if(actionMSG.getData()==null){
				while(true){
					System.out.println("Enter the field you wish to modify, and the new data to replace with.");
					System.out.println("Type \"end\" to exit edit mode.");
					System.out.println("Type \"help\" for further assistance.");
					
					
					
					//call processor
					
					
					
					break;		

				}
				
				control.setTempHold(null);
			}
			else {
				System.out.println(actionMSG.getData());
				int a;
				while(true){
					try{
						a = sc.nextInt();
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
