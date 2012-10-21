import java.io.FileNotFoundException;
import java.util.*;

class FingerTips {

	Control control;
	Scanner sc;
	boolean cont;

	public FingerTips(){
		sc = new Scanner(System.in);
		cont = true;
		control = Control.getInstance();
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
			System.out.println();
			runUserInput(userInput);
		}
	}

	private void printWelcomeMSG() {
		System.out.println("\nWelcome To FingerTips!\n");
		System.out.println("Enter \"help\" for further usage instructions.");
	}

	private void runUserInput(String userInput) {
		CMD actionMSG = control.performAction(userInput);
		switch(actionMSG.getCommandType()){
		case ADD: 
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
			System.out.println("Added.");
			//			}
			break;

		case REMOVE: 
			if(actionMSG.getData() == null){
				System.out.println("Which entry do you want to remove?");
				int rmvIndex;
				try{
					System.out.print("Index: ");
					rmvIndex = sc.nextInt();
					String newInstruction = "remove ".concat(Integer.toString(rmvIndex));
					control.performAction(newInstruction);

				} catch(InputMismatchException e){
					System.out.println("Invalid input. Action aborted.");

				}

				sc.nextLine();
			}
			else{
				System.out.println("Removed");
			}

			break;
		case UNDO: 
			System.out.println(actionMSG.getData());
			break;
		case DISPLAY: 
			ArrayList<String> print = (ArrayList<String>) actionMSG.getData();
			if(print.isEmpty()){
				System.out.println("There is nothing to print.");
			}
			else{
				int j=1;

				for (int i=0; i<print.size(); i++) {
					System.out.println(j + ". " + print.get(i));
					j++;
				}
			}
			break;
		case EDIT: 					
			if(actionMSG.getData()==null){

				while(true){
					System.out.println("\nEntry: ");
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
				System.out.println(actionMSG.getData());
				int a;
				while(true){
					try{
						System.out.print("Index: ");
						a = sc.nextInt();
						sc.nextLine();
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
		case DONE: 
			if(actionMSG.getData() == null){
				System.out.println("Invalid input. Action aborted.");
			}
			else{
				System.out.println("Entry marked as done.");
			}
			break;
		case HELP:
			help();
			break;
		case QUIT: 
			cont = false;
			System.out.println("Goodbye.");
			break;
		case CLEAR:
			System.out.println("All active entries deleted.");
			break;
		default: System.out.println(actionMSG.getData());
		}
	}

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

	}

	private static void help() {
		System.out.println("add <data>:\t\t   add an entry with related dates, description, priority etc.");
		System.out.println("\t\t\t   prefix @ indicates venue, prefix # indicates a hashtag.");
		System.out.println("remove <number>:\t   remove the selected entry for the active list.");
		System.out.println("edit <number>:\t\t   enters edit mode for selected entry.");
		System.out.println("undo:\t\t\t   reverses the previous action.");
		System.out.println("display:\t\t   shows the activelist.");
		System.out.println("display <search criteria>: generates a list of entries fulfilling the search criteria.");
		System.out.println("quit:\t\t\t   terminates the program.");
	}

	public static void showToUser(String text) {
		System.out.println(text);
	}
}
