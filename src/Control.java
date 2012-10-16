import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

class Control {
	private Processor processor;
	private Storage storage;
	private boolean edit, newList; // modes
	private ArrayList<Entry> tempList;
	private CMD undo;
	private Entry tempHold;

	private String MSG_ERROR = "Invalid input!";

	public Control() throws FileNotFoundException {
		// initialise.
		// load entries
		processor = new Processor();
		storage = new Storage();
//		tempList = storage.getActiveEntries();
//		Collections.sort(tempList);
	}

	public CMD performAction(String userInput) {

		CMD command = processor.translateToCMD(userInput);

		switch (command.getCommandType()) {
		// need to store previous version everytime in case of undo action
		case ADD:
			if(command.getData()!=null){
				tempHold = (Entry) command.getData();
				storage.addEntry(tempHold);
				storage.saveToStorage();
				undo = command;
				undo.setData(new Entry(tempHold));
			}
						
			return command;
			
//		case REMOVE:
			//if there is nothing in command.getData, get active list over to tempList, 
						//then ask user what he want to remove
			//if command.data contains hashtag, do a search for the hashtag, port to tempList, 
						//then ask user what he want to remove
			//if there is something in the Storage's tempList 
//			storage.display();
//			return processor.remove(command.getData());
//		case UNDO:
//			return storage.undo(command.getData());
		case DISPLAY:
			ArrayList<String> toPrint = new ArrayList<String>();
			tempList = new ArrayList<Entry>();
			tempList.clear();
			
			if (command.getData() == null) {	
				tempList.addAll(storage.displayAll());
				for (Entry entry : tempList) { 
					getPrintEntry(toPrint, entry);
				}
				command.setData(toPrint);
			}
			else {
				// if the data is integer to specify index
				if(isInteger(command.getData())){						
					int index = Integer.parseInt((String) command.getData());
					tempList.addAll(storage.displayIndex(index));
					for (Entry entry : tempList) { 
						getPrintEntry(toPrint, entry);
					}
					command.setData(toPrint);
				}
				// if the data is string to be searched
				else {
					String keyword = command.getData().toString();
					tempList.addAll(storage.displayKeyword(keyword));
					for (Entry entry : tempList) { 
						getPrintEntry(toPrint, entry);
					}
					command.setData(toPrint);
				}
			}
			
			return command;
		case EDIT:
			//check if number given by edit <number> is valid
			//if it is valid, load the entry into tempHold, then convert to add's edit
			//if not convert to edit <nothing>
			if(command.getData()!=null){
				if((int)command.getData() >= storage.getActiveEntries().size()) {	
					System.out.println("Invalid input. Enter a valid index.");
				}
				else{
					tempHold = storage.getActiveEntries().get((int)command.getData()-1);
				}
				
				command.setData(null);
			} else {	
				if(tempHold == null){ 	//edit <nothing>						
					command.setData("Which entry do you want to edit?");
				}
			}
			return command;
			
		case DONE:
			return command;
		case QUIT:
			return command;
		case HELP:
			return command;
		default:
			return command;
		}
		
	}

	private void getPrintEntry(ArrayList<String> toPrint, Entry entry) {
		String lineToPrint = null;
		lineToPrint = entry.getDesc();
		if (entry.getStart() != null) 
			lineToPrint = lineToPrint.concat(" at " + entry.getStart());
		if (entry.getEnd() != null) 
			lineToPrint = lineToPrint.concat(" to " + entry.getEnd());
		if (entry.getDate1() != null) 
			lineToPrint = lineToPrint.concat(" on " + entry.getDate1());
		if (entry.getVenue() != null) 
			lineToPrint = lineToPrint.concat(" " + entry.getVenue());
		if (entry.getPriority() != null) 
			lineToPrint = lineToPrint.concat(" " + entry.getPriority());
		if (entry.getTagDesc() != null) 
			lineToPrint = lineToPrint.concat(" " + entry.getTagDesc());
		toPrint.add(lineToPrint);
	}

	//checks if a string can be converted into an integer
	private boolean isInteger(Object object) {
		try{
			Integer i = Integer.parseInt((String) object);
			return true;
		}
		catch(NumberFormatException e){
			return false;
		}
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public Entry getTempHold() {
		return tempHold;
	}

	public void setTempHold(Entry tempHold) {
		this.tempHold = tempHold;
	}

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}
	
	public Processor getProcessor() {
		return processor;
	}

	public void setProcessor(Processor processor) {
		this.processor = processor;
	}
}