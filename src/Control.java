import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

class Control {
	private static Control control;
	private Processor processor;
	private Storage storage;
	private boolean edit, newList; // modes
	private ArrayList<Entry> tempList;
	private CMD undo;
	private Entry tempHold;

	private String MSG_ERROR = "Invalid input!";

	private Control() {
		// initialise.
		// load entries
		processor = new Processor();
		storage = Storage.getInstance();
		tempList = new ArrayList<Entry>();
		undo = new CMD(null, null);
//		Collections.sort(tempList);
	}
	
	public static Control getInstance() {
		if (control == null) {
			control = new Control();
		}
		return control;
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
					undo.setCommandType(command.getCommandType());
					undo.setData(storage.getDisplayEntries().get(i-1));
					storage.removeEntry(i);
					storage.saveToStorage();
				}
				else{
					//if commandData was a String
				}
			}
			return command;
			
		case CLEAR:
			undo.setCommandType(command.getCommandType());
			undo.setData(storage.getActiveEntries());
			storage.clear();
			storage.saveToStorage();
			return command;
		case UNDO:
			command.setData(undo());
			storage.saveToStorage();
			return command;
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
					tempList.addAll(storage.displayKeyword(keyword.toLowerCase()));
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
				undo.setCommandType(command.getCommandType());
				undo.setData(e);
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

	private String undo() {
		if(undo.getCommandType()==null) return "There are no further undo-s."; 
		switch(undo.getCommandType()){
		case ADD: break;		//amend storage remove function to search for objects and not indexes before implement
		case REMOVE: 
			storage.addEntry((Entry)undo.getData());
			break;
		case CLEAR: 
			storage.setActiveEntries((ArrayList<Entry>) undo.getData());
			break;
		case EDIT: break;		//each entry needs a unique identity for this to work...
		case DONE: break;		//storage function to move it back...Entry is given undeer data of Undo
			default: 
				return "There are no further undo-s.";
		}
		return "Undo completed.";
		
	}

	private void getPrintEntry(ArrayList<String> toPrint, Entry entry) {
		String lineToPrint = null;
		lineToPrint = entry.getDesc();
		if (entry.getStart() != null) 
			lineToPrint = lineToPrint.concat(" at " + entry.getStart());
		if (entry.getEnd() != null) 
			lineToPrint = lineToPrint.concat(" to " + entry.getEnd());
		if (entry.getDate() != null) 
			lineToPrint = lineToPrint.concat(" on " + entry.getDate());
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
			if(processor.isDate(cmd[1])){			//to be amended
				tempHold.iniDDate();
				tempHold.setDateCal(cmd[1]);
			}
			else cmd = new String[] {"Error", "Invalid entry for date."};
		} else if(cmd[0].equals("starttime")){
			if(cmd[1].endsWith("am") || cmd[1].endsWith("pm"))
				tempHold.setStart(cmd[1]);
			else cmd = new String[] {"Error", "Invalid time entry."};
		} else if(cmd[0].equals("endtime")){
			if(cmd[1].endsWith("am") || cmd[1].endsWith("pm"))
				tempHold.setEnd(cmd[1]);
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
				tempHold.setPriority(cmd[1].toUpperCase());
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
		if (entry.getDate() != null) 
			lineToPrint = lineToPrint.concat(" on " + entry.getDate());
		if (entry.getVenue() != null) 
			lineToPrint = lineToPrint.concat(" " + entry.getVenue());
		if (entry.getPriority() != null) 
			lineToPrint = lineToPrint.concat(" " + entry.getPriority());
		if (entry.getTagDesc() != null) 
			lineToPrint = lineToPrint.concat(" " + entry.getTagDesc());
		return lineToPrint;
	}
}