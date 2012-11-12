import static org.junit.Assert.*;

import org.junit.Test;


public class testUI {
	
	UI ui = new UI();

	@Test
	public void testMain() {
		fail("Not yet implemented");
	}

	@Test
	public void testUI() {
		fail("Not yet implemented");
	}

	@Test
	public void testActionPerformed() {
		ActionEvent[] input = new ActionEvent[10];
		ActionEvent[] actualOutput = new ActionEvent[10];
		ActionEvent[] expectedOutput = new ActionEvent[10];
		
		input[0] = return add(command);
		input[1] = return remove(command);
		input[2] = return clear(command);
		input[3] = return undo(command);
		input[4] = return redo(command);
		input[5] = return display(command, false);
		input[6] = return display(command, true);
		input[7] = return edit(command);
		input[8] = return done(command);
		input[9] = return command;
		
		// run test
		for (int i=0; i<input.length; i++) {
			CMD userCMD = p.translateToCMD(input[i]);
			actualOutput[i] = userCMD.getCommandType().toString();
			if (userCMD.getData() != null) {
				actualOutput[i] = actualOutput[i] + " " + userCMD.getData().toString();
			}
			else {
				actualOutput[i] = actualOutput[i] + " null";
			}
		}
		
		assertArrayEquals(expectedOutput, actualOutput);
	}

	@Test
	public void testRunUserInput() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLoggingParent() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddInputMethodListener() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetInputMethodListeners() {
		fail("Not yet implemented");
	}

}
