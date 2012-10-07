import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Storage {
	//uses Entries class.
	
	//Variables
	//Readers, Writers
	//ArrayList/lists etc for quick access
	//File for link to doc.
	
	//Functions
	// - searches for items with a certain criteria, returns list
	// - save function: writes into txt file
	// - load function
	//
	
	public static String WRITE_ERROR_MSG = "error writing: active list.txt not found.";
	public static String READ_ERROR_MSG = "error reading: active list.txt not found.";
	public File activeList = new File ("active list.txt");
	public File archiveList = new File ("archive list.txt");
	public FileReader fr;
	public BufferedReader br;
	public FileWriter fw;
	public BufferedWriter bw;
	public String currentLine;
	public List<String> displayList = new ArrayList<String>();
	
	public Storage() {
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
			System.out.println("addEntry " + WRITE_ERROR_MSG);
		}
	}
	
	// different mtd for each display criteria
	// have to decide on return type
	
	// display all
	public List<String> displayAll () {
		
		// initialise displayList
		displayList.clear();
		
		try {
			fr = new FileReader(activeList);
			br = new BufferedReader(fr);
			int lineIndex=1;			
			while ((currentLine = br.readLine()) != null) {
				displayList.add(lineIndex + ". " + currentLine);
				lineIndex++;
			}						
			br.close();
		} catch (IOException ioe) {
			System.out.println("displayAll " + READ_ERROR_MSG);
		}
		
		return displayList;
	}
	
	// display by keyword
	// search by description
	// search by hashtag "#tagname"
	// search by venue "@location"
	public List<String> displayKeyword (String keyword) {
		
		// initialise displayList
		displayList.clear();
		
		try {
			fr = new FileReader(activeList);
			br = new BufferedReader(fr);
			int lineIndex=1;
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
			System.out.println("displayKeyword " + READ_ERROR_MSG);
		}
		
		return displayList;
	}
		
}