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
		tempList = new ArrayList<Entry>();
		Collections.sort(tempList);
	}

	public CMD performAction(String userInput) {

		CMD command = processor.translateToCMD(userInput);
		ArrayList<String> toPrint = new ArrayList<String>();
		tempList = new ArrayList<Entry>();

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
			
		case REMOVE:
			//if there is nothing in command.getData, get active list over to tempList, 
						//then ask user what he want to remove
			//if command.data contains hashtag, do a search for the hashtag, port to tempList, 
						//then ask user what he want to remove
			//if there is something in the Storage's tempList 
			if(command.getData()!=null){
				if(isInteger(command.getData())) {
					Integer i = (Integer) command.getData(); 
//					command.setData(storage.displayEntries.get(i-1));
					storage.removeEntry(i);
					storage.saveToStorage();
				}
				else{
					//if commandData was a String
				}
			}
			return command;
//		case UNDO:
//			return storage.undo(command.getData());
		case DISPLAY:
			toPrint.clear();
//			tempList = new ArrayList<Entry>();
//			tempList.clear();
			
			if (command.getData() == null) {
				tempList.clear();
				tempList.addAll(storage.displayAll());
				for (Entry entry : tempList) { 
//					getPrintEntry(toPrint, entry);
					toPrint.add(printEntry(entry));
				}
				command.setData(toPrint);
			}
			else {
				if(tempList.isEmpty()){
					tempList.addAll(storage.displayAll());
				}
					
				// if the data is integer to specify index
				if(isInteger(command.getData())){						
					Integer index = (Integer) command.getData();
					toPrint.add(printEntry(tempList.get(index-1)));
					command.setData(toPrint);
				}
				// if the data is string to be searched
				else {
					tempList.clear();
					String keyword = (String) command.getData();
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
				if((int)command.getData() > storage.getActiveEntries().size()) {	
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
			//sort
			
			return command;
			
		case DONE:
			Integer i = (Integer) command.getData();
			if(i > storage.getActiveEntries().size() ||
					i < 1) {
				command.setCommandType(Processor.COMMAND_TYPE.ERROR);
				command.setData("Invalid index.");
			} else {
				tempList.addAll(storage.displayAll()); // need to initialise tempList and remove this line
				Entry e = tempList.get(i-1);
				storage.updateCompletedEntry(e);
				storage.saveToStorage();
			}
			
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
			Integer.parseInt((String) object);
			return true;
		}
		catch(NumberFormatException e){
			return false;
		}
		catch(ClassCastException c){
			return true;
		}
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

	public String[] processEditMode(String userInput) {
		String[] cmd = processor.determineCmdEditMode(userInput);
		if(cmd.length > 1 && cmd[1] != null) cmd[1] = cmd[1].trim();
		if(cmd[0].equals("description")){	
			if(cmd[1]!=null && cmd[1].length()!=0)
				tempHold.setDesc(cmd[1]);
		} else if(cmd[0].equals("duedate")){
			if(cmd[1].split("/").length == 3){			//to be amended
				tempHold.setDate(cmd[1]);
			}
			else cmd = new String[] {"Error", "Invalid entry for date."};
		} else if(cmd[0].equals("starttime")){
			if(cmd[1].endsWith("am") || cmd[1].endsWith("pm"))
				tempHold.setStart(cmd[1]);
			else cmd = new String[] {"Error", "Invalid time entry."};
		} else if(cmd[0].equals("endtime")){
			if(cmd[1].endsWith("am") || cmd[1].endsWith("pm"))
				tempHold.setStart(cmd[1]);
			else cmd = new String[] {"Error", "Invalid time entry."};
		} else if(cmd[0].equals("hash")){
			if(cmd[1].startsWith("#"))
				tempHold.setTagDesc(cmd[1]);
			else cmd = new String[] {"Error", "Not a hashtag"};
		} else if(cmd[0].equals("display")){
			cmd = new String[]{cmd[0], null};
			cmd[1] = printEntry(tempHold);
		} else if(cmd[0].equals("priority")){
			boolean restrictedWords = cmd[1].equalsIgnoreCase("high") || 
					cmd[1].equalsIgnoreCase("medium") || cmd[1].equalsIgnoreCase("low");
			if(restrictedWords){
				tempHold.setPriority(cmd[1]);
				//sort
			}
			else cmd = new String[] {"Error", "Not a priority"};
		} else if(cmd[0].equals("venue")){
			if(cmd[1].startsWith("@"))
				tempHold.setVenue(cmd[1]);
			else cmd = new String[] {"Error", "Invalid format for location."};
		} else if(cmd[0].equals("help")){
			
		} else if(cmd[0].equals("end")){
		} else {
			cmd = new String[] {"Error", "Invalid edit field."};
		}
		
		return cmd;
	}
	
	private String printEntry(Entry entry) {
		String lineToPrint = entry.getDesc();
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
		return lineToPrint;
	}
}