import static org.junit.Assert.*;

import org.junit.Test;


public class testControl {

	Control c = new Control();
	

	@Test
	public void testPerformAction() {
		fail("Not yet implemented");
	}


	@Test
	public void testProcessEditMode() {

		String[] input = new String[1];
		String[] actualOutput = new String[1];
		String[] expectedOutput = new String[1];
		
		input[0] = "et 1";		// returns ERROR & Invalid Input.
		input[1] = "#2"; 		// returns ERROR & Invalid Input.
		
		expectedOutput[0] = "ERROR Invalid Input. Input should follow a \"<command> <data>\"\nformat.\n";
		expectedOutput[1] = "ERROR Invalid Input. Input should follow a \"<command> <data>\"\nformat.\n";
		
		// run test
		for (int i=0; i<input.length; i++) {
			String[] cmd = c.processEditMode(input[i]);
			actualOutput[i] = cmd[i].getData().toString();
			if (cmd[i].getData() != null) {
				actualOutput[i] = actualOutput[i] + " " + cmd.getData().toString();
			}
			else {
				actualOutput[i] = actualOutput[i] + " null";
			}
		}
		
		assertArrayEquals(expectedOutput, actualOutput);
		
	}


}
