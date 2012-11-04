import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Logger;

class Control {
	private static Control control;
	private Processor processor;
	private Storage storage;
//	private boolean edit, newList; // modes
	private LinkedList<CMD> undo, redo;
	private Entry tempHold;
	private static final Logger logger = Logger.getLogger(Control.class.getName());

	private Control() {
		logger.setParent(UI.getLoggingParent());
		logger.info("Initialising Control.");
		
		storage = Storage.getInstance();
		processor = new Processor();
		undo = new LinkedList<CMD>();
		redo = new LinkedList<CMD>();
		
		logger.info("Control Initialised.");
	}
	
	public static Control getInstance() {
		if (control == null) {
			control = new Control();
		}
		return control;
	}

	public CMD performAction(String userInput) {

		CMD command = processor.translateToCMD(userInput);

		return carryOutCMD(command);
	}

	/**
	 * @param command
	 * @return
	 */
	private CMD carryOutCMD(CMD command) {
		switch (command.getCommandType()) {
		// need to store previous version every time in case of undo action
		case ADD:
			if(command.getData()!=null){
				tempHold = (Entry) command.getData();
				storage.addEntry(tempHold);
				storage.save(true, false);
				command.setData(tempHold);
				undo.push(command);
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
					if(i<1 || i>storage.getDisplayEntries().size()){
						command.setCommandType(Processor.COMMAND_TYPE.ERROR);
						command.setData("Invalid Index.");
						return command;
					}
					
					command.setData(storage.getDisplayEntries().get(i-1));
					undo.push(command);
					storage.removeEntry(i);
					storage.save(true, false);
				}
				else{
					//if commandData was a String
				}
			}
			return command;
			
		case CLEAR:
			command.setData(storage.getActiveEntries());
			undo.push(command);
			storage.clearActive();
			storage.save(true, false);
			return command;
		case UNDO:
			command.setData(undo());
			
			return command;
			
		case REDO:
			command.setData(redo());
			
			return command;
			
			
		case DISPLAY:
			if (command.getData() == null) {
				command.setData((Vector<Entry>)storage.display());
			}
			else {
					
				// if the data is integer to specify index
				if(isInteger(command.getData())){						
					Integer index = (Integer) command.getData();
					command.setData(storage.displayIndex(index));
				}
				// if the data is string to be searched
				else {
					String keyword = (String) command.getData();
					command.setData(storage.displayKeyword(keyword));
				}
			}
			
			return command;
		case EDIT:
			//check if number given by edit <number> is valid
			//if it is valid, load the entry into tempHold, then convert to add's edit
			//if not convert to edit <nothing>
			if(command.getData()!=null){
				if((int)command.getData() > storage.getActiveEntries().size()) {	
					System.out.print("Invalid input. Enter a valid index number.");
					command.setData(" ");
				}
				else{
					tempHold = storage.getActiveEntries().get((int)command.getData()-1);
					command.setData(null);
				}
				
			} else {	
				if(tempHold == null){ 	//edit <nothing>						
					command.setData("Which entry do you want to edit?");
				}
			}
			//sort
			
			return command;
			
		case DONE:
			Integer i = (Integer) command.getData();
			if(i > storage.getActiveEntries().size() || i < 1) {
				command.setCommandType(Processor.COMMAND_TYPE.ERROR);
				command.setData("Invalid index.");
			} else {
				Entry e = storage.updateCompletedEntry(i);
				command.setData(e);
				undo.push(command);
				storage.save(true, true);
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

	private String redo() {
		if(redo.isEmpty()) return "There are no further redo-s."; 
		
		CMD action = redo.pop();
		undo.push(action);
		
		
		carryOutCMD(action);
		
		
		
		if(action.getCommandType()==Processor.COMMAND_TYPE.DONE){
			storage.save(true, true);
		}
		else {
			storage.save(true, false);
		}
		
		return "Redo completed";
	}

	private String undo() {
		if(undo.isEmpty()) return "There are no further undo-s."; 
		
		CMD action = undo.pop();
		redo.push(action);
		
		switch(action.getCommandType()){
		case ADD: 
			storage.removeEntry((Entry)action.getData());
			break;		//amend storage remove function to search for objects and not indexes before implement
		case REMOVE: 
			storage.addEntry((Entry)action.getData());
			break;
		case CLEAR: 
			storage.setActiveEntries((Vector<Entry>) action.getData());
			break;
		case EDIT: 
			
			
			break;		//each entry needs a unique identity for this to work...
		case DONE:
			storage.undoDoneAction((Entry)action.getData());
			break;		//storage function to move it back...Entry is given under data of Undo
			default: 
				return "There are no further undo-s.";	
		}
		
		if(action.getCommandType()==Processor.COMMAND_TYPE.DONE){
			storage.save(true, true);
		}
		else {
			storage.save(true, false);
		}
		
		
		return "Undo completed.";
		
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