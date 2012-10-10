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
	public File activeFile = new File("activeFile.txt");
	public File archiveFile = new File("archiveFile.txt");
	public ArrayList<Entry> activeEntries = new ArrayList<Entry>();
	public ArrayList<Entry> archiveEntries = new ArrayList<Entry>();
	public FileReader fr;
	public BufferedReader br;
	public FileWriter fw;
	public BufferedWriter bw;
	public String currentLine;

	/*
	 * Load activeFile and archiveFile into activeEntries and archiveEntries
	 */
	public Storage() throws ClassNotFoundException {
		if (activeFile.exists()) {
			loadFromStorage(activeFile, activeEntries);
		}
		if (archiveFile.exists()) {
			loadFromStorage(archiveFile, archiveEntries);
		}
	}

	public void loadFromStorage(File source, ArrayList<Entry> entries)
			throws ClassNotFoundException {
		// read from file
		try {
			FileInputStream newFile = new FileInputStream(source);
			ObjectInputStream restore = new ObjectInputStream(newFile);
			Entry entry;
			while ((entry = (Entry) restore.readObject()) != null) {
				entries.add(entry);
			}
			restore.close();
		} catch (EOFException eofe) {
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Error loading from file");
		}

	}

	/*
	 * Save activeEntries and archiveEntries into activeFile and archiveFile
	 * (RMB TO CALL THIS METHOD BEFORE EXITING PROGRAM!)
	 */
	public void saveToStorage() {

		// clear both existing file first
		if (activeFile.exists()) {
			activeFile.delete();
		}
		if (archiveFile.exists()) {
			archiveFile.delete();
		}

		// copy entries from ArrayList back to the respective files
		try {
			FileOutputStream saveFile = new FileOutputStream(activeFile);
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			for (Entry entry : activeEntries) {
				save.writeObject(entry);
			}
			save.close();
		} catch (IOException ioe) {
			System.out.println("Error writing to file");
		}

		try {
			FileOutputStream saveFile = new FileOutputStream(archiveFile);
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			for (Entry entry : archiveEntries) {
				save.writeObject(entry);
			}
			save.close();
		} catch (IOException ioe) {
			System.out.println("Error writing to file");
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
		 * Display all - just copy into displayList for all the task.
		 * displayList will be initialized each time this method is called
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