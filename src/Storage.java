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
	public File activeTextFile = new File("activeTextFile.txt");
	public File archiveTextFile = new File("archiveTextFile.txt");
	public ArrayList<Entry> activeEntries = new ArrayList<Entry>();
	public ArrayList<Entry> archiveEntries = new ArrayList<Entry>();
	public ArrayList<Entry> displayEntries = new ArrayList<Entry>();
	public ArrayList<Entry> tempEntries = new ArrayList<Entry>();
	public FileWriter fw;
	public BufferedWriter bw;
	public String currentLine;

	/*
	 * Load activeFile and archiveFile into activeEntries and archiveEntries
	 */
	public Storage() {
		if (activeFile.exists()) {
			loadFromStorage(activeFile, activeEntries);
		}
		if (archiveFile.exists()) {
			loadFromStorage(archiveFile, archiveEntries);
		}
	}

	public void loadFromStorage(File source, ArrayList<Entry> entries) {
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
		} catch (ClassNotFoundException cnfe) {
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

		// for file reader and writer, update the corresponding
		// .txt files

		try {
			fw = new FileWriter(activeTextFile);
			bw = new BufferedWriter(fw);
			bw.write("");
			bw.close();
		} catch (IOException ioe) {
			System.out.println("Error saving to text file");
		}

		try {
			fw = new FileWriter(archiveTextFile);
			bw = new BufferedWriter(fw);
			bw.write("");
			bw.close();
		} catch (IOException ioe) {
			System.out.println("Error saving to text file");
		}

		try {
			fw = new FileWriter(activeTextFile);
			bw = new BufferedWriter(fw);
			for (Entry entry : activeEntries) {
				bw.write(entry + "\n");
			}
			bw.close();
		} catch (IOException ioe) {
			System.out.println("Error saving to text file");
		}

		try {
			fw = new FileWriter(archiveTextFile);
			bw = new BufferedWriter(fw);
			for (Entry entry : archiveEntries) {
				bw.write(entry + "\n");
			}
			bw.close();
		} catch (IOException ioe) {
			System.out.println("Error saving to text file");
		}
	}

	/*
	 * Adds new entry into the activeEntries list. ~storage.addEntry(ENTRY_NAME)
	 */
	public void addEntry(Entry entry) {
		activeEntries.add(entry);
	}

	/*
	 * This method should only be called after the display function is called.
	 * The entry in the displayEntries that match the index specified by the
	 * user will be removed from the displayEntries list. Subsequently, update
	 * activeEntries list to remove the specific entry from storage.
	 * ~storage.removeEntry(INDEX_OF_ENTRY)
	 */
	public void removeEntry(int index) {
		tempEntries.clear();
		tempEntries.add(displayEntries.get(index - 1));
		activeEntries.removeAll(tempEntries); // update activeEntries list
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
	public ArrayList<Entry> displayAll() {
		displayEntries.clear();
		for (Entry entry : activeEntries) {
			displayEntries.add(entry);
		}
		return displayEntries;

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
	public ArrayList<Entry> displayKeyword(String keyword) {
		displayEntries.clear();
		for (Entry entry : activeEntries) {
			if (entry.toString().contains(keyword)) {
				displayEntries.add(entry);
			}
		}	
		return displayEntries;
	}
	
	/*
	 * Method to display entries by specified keyword. Entries will be copied
	 * over to displayEntries for printing. displayEntries will be initialized
	 * each time this method is called. Keyword can be description, hashtag
	 * "#tagname", venue "@location". ~storage.displayKeyword(KEYWORD_TO_FIND)
	 */
	public ArrayList<Entry> displayIndex(int index) {
		displayEntries.clear();
		
		/* not completed*/
		
		return displayEntries;
	}

	/*
	 * Method to display entries by specified date. Entries will be copied over
	 * to displayEntries for printing. displayEntries will be initialized each
	 * time this method is called. User to enter date in the form dd/mm/yyyy.
	 * ~storage.displayDate(dd/mm/yyyy)
	 */
	public ArrayList<Entry> displayDate(String date) {
		displayEntries.clear();
		Entry newEntry = new Entry();
		newEntry.setDate(date);
		for (Entry entry : activeEntries) {
			if (entry.getDate().equals(newEntry.getDate())) {
				displayEntries.add(entry);
			}
		}
		return displayEntries;
	}

}