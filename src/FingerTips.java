import java.util.*;

class FingerTips {
	
	public static void main(String args[]){
		String actionMSG, userInput;
		Scanner sc = new Scanner(System.in);
		Control control = new Control();
		
		while(true){
			userInput = sc.nextLine();
			actionMSG = control.performAction(userInput);
			System.out.println(actionMSG);
			
			//if userInput.equals("exit") break;
		}
		
	}
}
