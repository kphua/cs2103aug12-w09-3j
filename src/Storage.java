// import all classes for now
import java.util.*;
import java.io.*;

class Storage {
	// uses Entries class.

	// Variables
	// Readers, Writers
	// ArrayList/lists etc for quick access
	// File for link to doc.

	// Functions
	// - searches for items with a certain criteria, returns list
	// - save function: writes into txt file
	// - load function
	//

	public static String ERROR_MSG = "IOException error in ";
	public File activeFile = new File("active.txt");
	public File archiveFile = new File("archive.txt");
	public FileReader fr;
	public BufferedReader br;
	public FileWriter fw;
	public BufferedWriter bw;
	public String currentLine;
	public List<String> activeList = new ArrayList<String>();
	public List<String> displayList = new ArrayList<String>();

	public Storage() {
		/*
		 * copy the file onto an ArrayList to access it
		 */
		try {
			fr = new FileReader(activeFile);
			br = new BufferedReader(fr);
			while ((currentLine = br.readLine()) != null) {
				activeList.add(currentLine);
			}
			br.close();
		} catch (IOException ioe) {
			System.out.println(ERROR_MSG + "Storage.");
		}
	}

	public void addEntry(String newEntry) {
		// file writer to add entry to file
		// using true to append into file
		// exception catching if file cannot be found
		String inputText = newEntry;

		try {
			fw = new FileWriter(activeList, true);
			bw = new BufferedWriter(fw);
			bw.write(inputText + "\n");
			bw.close();
		} catch (IOException ioe) {
			System.out.println(ERROR_MSG + "addEntry.");
		}
	}

	// different mtd for each display criteria
	// have to decide on return type

	// display all
	public List<String> displayAll() {

		// initialise displayList
		displayList.clear();

		try {
			fr = new FileReader(activeList);
			br = new BufferedReader(fr);
			int lineIndex = 1;
			while ((currentLine = br.readLine()) != null) {
				displayList.add(lineIndex + ". " + currentLine);
				lineIndex++;
			}
			br.close();
		} catch (IOException ioe) {
			System.out.println(ERROR_MSG + "displayAll.");
		}

		return displayList;
	}

	// display by keyword
	// search by description
	// search by hashtag "#tagname"
	// search by venue "@location"
	public List<String> displayKeyword(String keyword) {

		// initialise displayList
		displayList.clear();

		try {
			fr = new FileReader(activeList);
			br = new BufferedReader(fr);
			int lineIndex = 1;
			while ((currentLine = br.readLine()) != null) {
				String[] compareLine = currentLine.split("\\s+");
				for (String str : compareLine) {
					if (str.equalsIgnoreCase(keyword)) {
						displayList.add(lineIndex + ". " + currentLine);
						lineIndex++;
					}
				}
			}
			br.close();
		} catch (IOException ioe) {
			System.out.println(ERROR_MSG + "displayKeyword");
		}

		return displayList;
	}

}