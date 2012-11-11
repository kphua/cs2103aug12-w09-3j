import static org.junit.Assert.*;

import org.junit.Test;


public class testControl {

	Control c = new Control();
	
	@Test
	public void testGetInstance() {
		fail("Not yet implemented");
	}

	@Test
	public void testPerformAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTempHold() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetEditHolder() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStorage() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetStorage() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetProcessor() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetProcessor() {
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
			actualOutput[i] = cmd.getData.toString();
			if (cmd.getData() != null) {
				actualOutput[i] = actualOutput[i] + " " + cmd.getData().toString();
			}
			else {
				actualOutput[i] = actualOutput[i] + " null";
			}
		}
		
		assertArrayEquals(expectedOutput, actualOutput);
		
	}

	@Test
	public void testObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetClass() {
		fail("Not yet implemented");
	}

	@Test
	public void testHashCode() {
		fail("Not yet implemented");
	}

	@Test
	public void testEquals() {
		fail("Not yet implemented");
	}

	@Test
	public void testClone() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testNotify() {
		fail("Not yet implemented");
	}

	@Test
	public void testNotifyAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testWaitLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testWaitLongInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testWait() {
		fail("Not yet implemented");
	}

	@Test
	public void testFinalize() {
		fail("Not yet implemented");
	}

}
