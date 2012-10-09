import java.io.FileNotFoundException;
import java.util.*;

class FingerTips {

	public static void main(String args[]) throws FileNotFoundException {
		String actionMSG, userInput;
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
		
		while (cont) {
			System.out.print("Command: ");
			userInput = sc.nextLine();
			actionMSG = control.performAction(userInput);
			if(actionMSG.equals("exit")) break;
			else if(actionMSG.equals("help")) help();
			else
				System.out.println(actionMSG);

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
