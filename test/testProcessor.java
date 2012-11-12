import static org.junit.Assert.*;

import org.junit.Test;


/**
 * @author A0084960X
 * Unit testing for Processor.java
 */
public class testProcessor {
	
	Processor p = new Processor();

	@Test
	public void testTranslateToCMD() {
		
		String[] input = new String[19];
		String[] actualOutput = new String[19];
		String[] expectedOutput = new String[19];
		
		input[0] = "  ";		// returns ERROR & Invalid Input.
		input[1] = "testing"; 	// returns ERROR & Invalid Input.
		// returns ADD & Test for add 25/12/2012
		input[2] = "add Test for add 12pm 25/12/2012";
		input[3] = "clear";		// returns CLEAR & null
		input[4] = "done";		// returns ERROR & Invalid Input.
		input[5] = "done 2";	// returns DONE & 2
		input[6] = "display";	// returns DISPLAY & null
		input[7] = "display HIGH";	// returns DISPLAY & HIGH
		input[8] = "display testing display";	// returns DISPLAY & testing display
		input[9] = "display #hash";	// returns DISPLAY & #hash
		input[10] = "edit";	// returns EDIT & null
		input[11] = "edit 3";	// returns EDIT & 3
		input[12] = "edit nonsense";	// returns EDIT & null
		input[13] = "remove";	// returns REMOVE & null
		input[14] = "remove 3";	// returns REMOVE & 3
		input[15] = "remove hello";	// returns REMOVE & hello
		input[16] = "undo";	// returns UNDO & null
		input[17] = "quit";	// returns QUIT & null
		input[18] = "help";	// returns HELP & null
		
		// set expectedOutput results
		expectedOutput[0] = "ERROR Invalid Input. Input should follow a \"<command> <data>\"\nformat.\n";
		expectedOutput[1] = "ERROR Invalid Input. Input should follow a \"<command> <data>\"\nformat.\n";
		expectedOutput[2] = "ADD Test for add  25/12/2012 12.00PM ";
		expectedOutput[3] = "CLEAR null";
		expectedOutput[4] = "ERROR Invalid Input. Input should follow a \"<command> <data>\"\nformat.\n";
		expectedOutput[5] = "DONE 2";
		expectedOutput[6] = "DISPLAY null";
		expectedOutput[7] = "DISPLAY HIGH";
		expectedOutput[8] = "DISPLAY testing display";
		expectedOutput[9] = "DISPLAY #hash";
		expectedOutput[10] = "ERROR Error: Type \"edit\" with a valid index.\n";
		expectedOutput[11] = "EDIT 3";
		expectedOutput[12] = "ERROR Error: Type \"edit\" with a valid index.\n";
		expectedOutput[13] = "ERROR Invalid input. Input for remove should follow \"<command>\n<index>\".\n";
		expectedOutput[14] = "REMOVE 3";
		expectedOutput[15] = "ERROR Invalid input. Input for remove should follow \"<command>\n<index>\".\n";
		expectedOutput[16] = "UNDO null";
		expectedOutput[17] = "QUIT null";
		expectedOutput[18] = "HELP null";
		
		// run test
		for (int i=0; i<input.length; i++) {
			CMD userCMD = p.translateToCMD(input[i]);
			actualOutput[i] = userCMD.getCommandType().toString();
			if (userCMD.getData() != null) {
				actualOutput[i] = actualOutput[i] + " " + userCMD.getData();
			}
			else {
				actualOutput[i] = actualOutput[i] + " null";
			}
		}
		
		assertArrayEquals(expectedOutput, actualOutput);
	}

	@Test
	public void testIsDate() {
		
		// initialise arrays to store test cases and answers
		String[] input = new String[10];
		String[] actualOutput = new String[10];
		String[] expectedOutput = new String[10];
		
		// initialise expectedOutputs and actualOutputs to true;
		for (int i=0; i<actualOutput.length; i++) {
			actualOutput[i] = "true";
			expectedOutput[i] = "true";
		}
		
		// set test cases
		input[0] = "12May2012";	// false - not a valid format
		input[1] = "12 10 12";// false - not a valid format
		input[2] = "12122012";// false - not a valid format
		input[3] = "0/10/2012";	// true - will re-calculate to 30 sep
		input[4] = "1/12/12/12";// true - will take in first 3 fields only
		input[5] = "1-13-2012";	// true - valid date
		input[6] = "14.10.12";	// true - valid date
		input[7] = "28/02/12";	// true - valid date
		input[8] = "24/01/1999";// true - valid date
		input[9] = "28/10/2012";// true - valid date
		
		// set expectedOutput results
		// expectedOutput[0], [1] and [2] will be false
		for (int i=0; i<3; i++) {
			expectedOutput[i] = "false";
		}
		
		// run test
		for (int i=0; i<input.length; i++) {
			if (p.isDate(input[i]) != null) {
				actualOutput[i] = "true";
			}
			else {
				actualOutput[i] = "false";
			}
		}
		
		assertArrayEquals(expectedOutput, actualOutput);	
	}

	@Test
	public void testDetermineCmdEditMode() {
		
		// initialise arrays to store test cases and answers
		String[] input = new String[10];
		String[] answer = new String[2];
		String[] actualOutput = new String[10];
		String[] expectedOutput = new String[10];
				
		// set test cases
		input[0] = "descriptions";	// invalid, returns error
		input[1] = "due";			// invalid, returns error
		input[2] = "hasheS";		// invalid, returns error
		input[3] = "nothing";		// invalid, returns error
		input[4] = "test";			// invalid, returns error
		input[5] = "endTIME";		// valid, returns endtime
		input[6] = "q";				// valid, returns end
		input[7] = "#";				// valid, returns hash
		input[8] = "Display";		// valid, returns display
		input[9] = "desc";			// valid, returns description
				
		// set expectedOutput results
		for (int i=0; i<5; i++) {
			expectedOutput[i] = "error";
		}
		expectedOutput[5] = "duetime";
		expectedOutput[6] = "end";
		expectedOutput[7] = "hash";
		expectedOutput[8] = "display";
		expectedOutput[9] = "description";
				
		// run test
		for (int i=0; i<input.length; i++) {
			answer = p.determineCmdEditMode(input[i]);
			actualOutput[i] = answer[0];	// just catch the first word that is returned
		}
		
		assertArrayEquals(expectedOutput, actualOutput);
	}

}
