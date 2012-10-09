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
		/*
		 * For now, assume first time adding new entry into program. New entry
		 * is added to ArrayList of Entry (entries). Precond: entry passed is in
		 * the form "Go to the beach--null--Sentosa". cannot have missing fields
		 * for now, else will have exception.
		 */

		String[] entrytest = new String[10];
		entrytest[0] = "birthday--12--13--14121998--hello--bye--me";
		entrytest[1] = "birthday--12--13--14121998--hello--bye--you";

		for (int i = 0; i < 2; i++) {
			String[] arr = entrytest[i].split("--");

			Entry e = new Entry();
			e.setDesc(arr[0]);
			int start = Integer.parseInt(arr[1]);
			e.setStart(start);
			int end = Integer.parseInt(arr[2]);
			e.setEnd(end);
			int date = Integer.parseInt(arr[3]);
			e.setDate(date);
			e.setVenue(arr[4]);
			e.setPriority(arr[5]);
			e.setTagDesc(arr[6]);
			e.setCompleteStatus(0); // for all new entry, default to 0
									// (incomplete)

			// add new entry to activeList
			activeList.add(printNewEntry(e));
		}

	}

	public String printNewEntry(Entry entry) {
		/*
		 * method to link all attributes to a single string of line
		 */

		return entry.getDesc() + " " + entry.getStart() + " " + entry.getEnd()
				+ " " + entry.getDate() + " " + entry.getVenue() + " "
				+ entry.getPriority() + " " + entry.getTagDesc();
	}

	/*
	 * Different methods for different display commands. Have to decide on the
	 * return type after that 1. displayAll 2. displayKeyword 3. displayDate (to
	 * be implemented)
	 */

	public List<String> displayAll() {
		/*
		 * Display all - just copy into displayList for all the task. displayList
		 * will be initialized each time this method is called
		 */

		displayList.clear();
		Collections.copy(displayList, activeList);
		return displayList;
	}

	public List<String> displayKeyword(String keyword) {
		/*
		 * display by keyword description, hashtag "#tagname", venue "@location"
		 */

		// initialise displayList
		displayList.clear();

		try {
			fr = new FileReader(activeFile);
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
			System.out.println(ERROR_MSG + "displayKeyword.");
		}

		return displayList;
	}
	
	public void archiveEntry(File activeList) {
		/*
		 * Method to transfer completed task to archive list. possibly limit to
		 * the last month/year?
		 */

		try {
			fr = new FileReader(activeList);
			br = new BufferedReader(fr);
			fw = new FileWriter(archiveFile, true);
			bw = new BufferedWriter(fw);
			while ((currentLine = br.readLine()) != null) {

				// not yet completed
				// basic idea
				// if (entry.getCompletedStatus == 1) remove and write to
				// archiveList
				// else do nothing
			}

		} catch (IOException ioe) {
			System.out.println(ERROR_MSG + "archiveEntry.");
		}
	}

}