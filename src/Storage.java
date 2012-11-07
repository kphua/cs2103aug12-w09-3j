// import all classes for now
import java.util.*;
import java.util.logging.Logger;
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
	// - load function: loads from txt file
	// - add/remove function: adding or deleting entries from list

	private static Storage storage;
	private File activeFile;
	private File archiveFile;
	private File activeTextFile;
	private File archiveTextFile;
	private Vector<Entry> activeEntries;
	private Vector<Entry> archiveEntries;
	private Vector<Entry> displayEntries;
	private FileWriter fw;
	private BufferedWriter bw;
	private static final Logger logger = Logger.getLogger(Control.class.getName());

	//Singleton implementation. Call this to create Storage
	public static Storage getInstance() {
		
		
		if (storage == null) {
			storage = new Storage();
		}
		return storage;
	}
	
	/*
	 * Initialise attributes. 
	 * Checks if activeFile and archiveFile exists.
	 * Creates them if they do not exist.
	 * Load activeFile and archiveFile into activeEntries and archiveEntries
	 */
	private Storage(){
		logger.setParent(UI.getLoggingParent());
		logger.info("Initialising Storage.");
		
		activeFile = new File("activeFile.txt");
		archiveFile = new File("archiveFile.txt");
		activeTextFile = new File("activeTextFile.txt");
		archiveTextFile = new File("archiveTextFile.txt");
		activeEntries = new Vector<Entry>();
		archiveEntries = new Vector<Entry>();
		displayEntries = activeEntries;
		
		if (activeFile.exists()) {
			logger.fine("Loading Active Entries.");
			loadFromFile(activeFile, activeEntries);
			Collections.sort(activeEntries);
			logger.fine("Done.");
		}
		else{
			logger.info("No active list found.");
			try {
				logger.fine("Creating new list.");
				activeFile.createNewFile();
				System.out.println("No active list found. New list created.");
				logger.fine("Created.");
			} catch (IOException e) {
				logger.severe("Unable to Create File.");
				System.out.println("IOException for activeFile.");
				System.exit(-1);
			}
		}

		if (archiveFile.exists()) {
			logger.fine("Loading Active Entries.");
			loadFromFile(archiveFile, archiveEntries);
			logger.fine("Done.");
		}
		else{
			logger.info("No archive found.");
			try {
				logger.fine("Creating new archive.");
				archiveFile.createNewFile();
				System.out.println("No archive found. New archive created.");
				logger.fine("Created.");
			} catch (IOException e) {
				logger.severe("Unable to Create File.");
				System.out.println("IOException for archiveFile.");
				System.exit(-1);
			}
		}
		
		
	}

	private void loadFromFile(File source, Vector<Entry> destination) {
		// read from file
		try {
			FileInputStream newFile = new FileInputStream(source);
			ObjectInputStream restore = new ObjectInputStream(newFile);
			Entry entry;
			while ((entry = (Entry) restore.readObject()) != null) {
				destination.add(entry);
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
	
	public void save(boolean activeChanged, boolean archiveChanged){
		if(activeChanged) saveToFile(activeFile, activeEntries, activeTextFile);
		if(archiveChanged) saveToFile(archiveFile, archiveEntries, archiveTextFile);
	}
	
	public void saveToFile(File objDest, Vector<Entry> list, File txtDest) {

		// copy entries from ArrayList back to the respective files
		try {
			FileOutputStream saveFile = new FileOutputStream(objDest);
			ObjectOutputStream objWriter = new ObjectOutputStream(saveFile);
			fw = new FileWriter(txtDest);
			bw = new BufferedWriter(fw);
			for (Entry entry : list) {
				objWriter.writeObject(entry);
				bw.write(entry + "\n");
			}
			objWriter.close();
		} catch (IOException ioe) {
			System.out.println("Error writing to file");
		}
	}

	/*
	 * Adds new entry into the activeEntries list. ~storage.addEntry(ENTRY_NAME)
	 */
	public void addEntry(Entry entry) {
		activeEntries.add(entry);
		Collections.sort(displayEntries);
	}

	/*
	 * This method should only be called after the display function is called.
	 * The entry in the displayEntries that match the index specified by the
	 * user will be removed from the displayEntries list. Subsequently, update
	 * activeEntries list to remove the specific entry from storage.
	 * ~storage.removeEntry(INDEX_OF_ENTRY)
	 */
	public Entry removeEntry(int index) {
		Entry e = displayEntries.get(index-1);
		displayEntries.remove(index-1);
		activeEntries.remove(e);
		return e;
	}
	
	public void removeEntry(Entry entry){
		boolean a = activeEntries.contains(entry);
		a = displayEntries.contains(entry);
		displayEntries.remove(entry);
		activeEntries.remove(entry);
		
		
	}
	
	/*
	 * This method should only be called after the display function is called.
	 * From control, the entry to be marked done will be passed to this method
	 * to be added to the archiveEntries list. Subsequently, update activeEntries
	 * list to remove the specific entry from storage.
	 * ~storage.updateCompletedEntry(ENTRY_OBJECT)
	 */
	public Entry updateCompletedEntry(int index) {		
		Entry e = removeEntry(index);
		archiveEntries.add(e);
		return e;
	}
	
	public void undoDoneAction(Entry entry){
		archiveEntries.remove(entry);
		activeEntries.add(entry);
	}

	/*
	 * Setters and getters methods for printing active/archive/displayEntries
	 * for (Entry entry : storage.getActiveEntries()) {
	 * System.out.println("activeList: " + entry); }
	 */
	public Vector<Entry> getActiveEntries() {
		return activeEntries;
	}

	public void setActiveEntries(Vector<Entry> entries) {
		this.activeEntries = entries;
	}

	public Vector<Entry> getArchiveEntries() {
		return archiveEntries;
	}

	public void setArchiveEntries(Vector<Entry> entries) {
		this.archiveEntries = entries;
	}

	public Vector<Entry> getDisplayEntries() {
		return displayEntries;
	}

	public void setDisplayEntries(Vector<Entry> entries) {
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
//	public Vector<Entry> displayAll() {
//		displayEntries.clear();
//		for (Entry entry : activeEntries) {
//			displayEntries.add(entry);
//		}
//		return displayEntries;

		// CODE FOR PRINTING OF DISPLAY ENTRIES
		// for (Entry entry : storage.getDisplayEntries()) {
		// System.out.println(entry); }
//	}
	
	public Vector<Entry> display(){
		displayEntries = activeEntries;
		return displayEntries;
	}
	
	/*
	 * Method to display entries by specified keyword. Entries will be copied
	 * over to displayEntries for printing. displayEntries will be initialized
	 * each time this method is called. Keyword can be description, hashtag
	 * "#tagname", venue "@location". ~storage.displayKeyword(KEYWORD_TO_FIND)
	 */
	public Vector<Entry> displayKeyword(String keyword) {
		
		keyword = keyword.toLowerCase();
		
		displayEntries = new Vector<Entry>();
		for (Entry entry : activeEntries) {
			if (entry.toString().toLowerCase().contains(keyword)) {
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
	public Vector<Entry> displayIndex(int index) {
		Vector<Entry> temp = new Vector<Entry>();
		Entry e = displayEntries.get(index); 
		temp.add(e);
		return temp;
	}

	/*
	 * Method to display entries by specified date. Entries will be copied over
	 * to displayEntries for printing. displayEntries will be initialized each
	 * time this method is called. User to enter date in the form dd/mm/yyyy.
	 * ~storage.displayDate(dd/mm/yyyy)
	 */
	public Vector<Entry> displayDate(String date) {
		displayEntries.clear();
		Entry newEntry = new Entry();
		newEntry.setDateCal(date);
		for (Entry entry : activeEntries) {
			if (entry.getDate().equals(newEntry.getDate())) {
				displayEntries.add(entry);
			}
		}
		return displayEntries;
	}

	//CLEAR function
	//removes all entries from activeEntries
	public void clearActive(){
		activeEntries = new Vector<Entry>();
		displayEntries = activeEntries;
		assert activeEntries.isEmpty();		// assert all entries cleared
	}

	public Entry removeEntry(String id) {
		for(Entry e : activeEntries){
			if(id.equals(e.getID())){
				activeEntries.remove(e);
				return e;
			}
		}
		return null;
	}
}