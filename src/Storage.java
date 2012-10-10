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

	public File activeFile = new File("activeFile.txt");
	public File archiveFile = new File("archiveFile.txt");
	public ArrayList<Entry> activeEntries = new ArrayList<Entry>();
	public ArrayList<Entry> archiveEntries = new ArrayList<Entry>();
	public ArrayList<Entry> displayEntries = new ArrayList<Entry>();

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
	 * (RMB TO CALL THIS METHOD BEFORE EXITING PROGRAM!
	 * ~storage.saveToStorage())
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

	/*
	 * Adds new entry into the activeEntries list. Entry passed must be in the
	 * form "homework--12--13--14121998--hello--bye--me". All fields must be
	 * present for now to prevent exception. ~storage.addEntry(ENTRY_NAME)
	 */
	public void addEntry(String entry) {
		Entry e = new Entry();
		String[] arr = entry.split("--"); // token can change accordingly

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
		activeEntries.add(e);
	}

	/*
	 * Removes an entry from the activeEntries, and stores in archiveEntries.
	 * Assumes that removed event is a completed event/user specified to remove.
	 * Current implementation of this function is remove by event description.
	 * ~storage.removeEntry(WORD_TO_DELETE)
	 */
	public void removeEntry(String del) {

		for (Entry entry : activeEntries) {
			if (entry.getDesc().equalsIgnoreCase(del)) {
				archiveEntries.add(entry);
			}
		}
		activeEntries.removeAll(archiveEntries);
	}

	/*
	 * Setters and getters methods for printing active/archive/displayEntries
	 * for (Entry entry : storage.getActiveEntries()) {
	 * System.out.println("activeList: " + entry); }
	 */
	public ArrayList<Entry> getActiveEntries() {
		return activeEntries;
	}

	public void setActiveEntries(ArrayList<Entry> entries) {
		this.activeEntries = entries;
	}

	public ArrayList<Entry> getArchiveEntries() {
		return archiveEntries;
	}

	public void setArchiveEntries(ArrayList<Entry> entries) {
		this.archiveEntries = entries;
	}

	public ArrayList<Entry> getDisplayEntries() {
		return displayEntries;
	}

	public void setDisplayEntries(ArrayList<Entry> entries) {
		this.displayEntries = entries;
	}

	/*
	 * Different methods for different display commands. Have to decide on the
	 * return type after that 1. displayAll 2. displayKeyword 3. displayDate (to
	 * be implemented)
	 */

	/*
	 * Method to display all entries. Entries will be copied over to
	 * displayEntries for printing. displayEntries will be initialized each time
	 * this method is called. ~storage.displayAll()
	 */
	public void displayAll() {
		displayEntries.clear();
		for (Entry entry : activeEntries) {
			displayEntries.add(entry);
		}

		// CODE FOR PRINTING OF DISPLAY ENTRIES
		// for (Entry entry : storage.getDisplayEntries()) {
		// System.out.println(entry); }
	}

	/*
	 * Method to display entries by specified keyword. Entries will be copied
	 * over to displayEntries for printing. displayEntries will be initialized
	 * each time this method is called. Keyword can be description, hashtag
	 * "#tagname", venue "@location". ~storage.displayKeyword(KEYWORD_TO_FIND)
	 */
	public void displayKeyword(String keyword) {
		displayEntries.clear();
		for (Entry entry : activeEntries) {
			if (entry.getDesc().contains(keyword)) {
				displayEntries.add(entry);
			}
		}
	}

	/*
	 * Method to display entries by specified date. Entries will be copied over
	 * to displayEntries for printing. displayEntries will be initialized each
	 * time this method is called. Current implementation of date format will be
	 * in the form ddmmyyyy. ~storage.displayDate(ddmmyyyy)
	 */
	public void displayDate(int date) {
		displayEntries.clear();
		for (Entry entry : activeEntries) {
			if (entry.getDate() == date) {
				displayEntries.add(entry);
			}
		}
	}

}