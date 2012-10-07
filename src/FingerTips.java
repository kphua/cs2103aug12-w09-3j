import java.io.FileNotFoundException;
import java.util.*;

class FingerTips {

	public static void main(String args[]) throws FileNotFoundException {
		String actionMSG, userInput;
		Scanner sc = new Scanner(System.in);
		Control control = new Control();

		// print welcome/help msg etc.
		showToUser("Welcome To FingerTips!");
		showToUser("Commands:");
		showToUser(".a/.add to add, .r/.remove to remove, .e/.edit to edit");
		showToUser(".u/.undo to undo, .d/.display to display, .q/.quit to quit");
		showToUser(".h/.help to display help for FingerTips");
		System.out.print("Command: ");

		while (true) {
			userInput = sc.nextLine();
			actionMSG = control.performAction(userInput);
			System.out.println(actionMSG);

			// if userInput.equals("exit") break;
		}

	}
	
	public static void showToUser(String text) {
		System.out.println(text);
	}
}
