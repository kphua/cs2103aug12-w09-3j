import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Logger;

class Control {
	private static final String MSG_ERROR_INVALID_INDEX = "Invalid input. Enter a valid index number.";
	private static final String MSG_ERROR_EDIT_EMPTY_ACTIVELIST = "There is nothing to edit.";
	private static Control control;
	private Processor processor;
	private Storage storage;
//	private boolean edit, newList; // modes
	private LinkedList<CMD> undo, redo;
	private Entry editHolder;
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
		case ADD:			return add(command);
		case REMOVE:		return remove(command);
		case CLEAR:			return clear(command);
		case UNDO:			return undo(command);
		case REDO:			return redo(command);			
		case DISPLAY:		return display(command);
		case EDIT:			return edit(command);
		case DONE:			return done(command);
		default:			return command;
//		case QUIT: case HELP:
//			return command;
		
		}
	}

	/**
	 * @param command
	 * @return
	 */
	private CMD done(CMD command) {
		
		if(isInteger(command.getData())){
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
		} else {
			Entry e = (Entry) command.getData();
			storage.getActiveEntries().add(e);
			storage.getArchiveEntries().remove(e);
		}
		
		return command;
	}

	/**
	 * @param command
	 * @return
	 */
	private CMD edit(CMD command) {
		//check if number given by edit <number> is valid
		//if it is valid, load the entry into tempHold, then convert to add's edit
		//if not convert to edit <nothing>
		
		if(storage.getActiveEntries().isEmpty()) {
			command.setCommandType(Processor.COMMAND_TYPE.ERROR);
			command.setData(MSG_ERROR_EDIT_EMPTY_ACTIVELIST);
			return command;
		}
		
		if((int)command.getData() > storage.getActiveEntries().size() || (int)command.getData()<1) {	
			command.setCommandType(Processor.COMMAND_TYPE.ERROR);
			command.setData(MSG_ERROR_INVALID_INDEX);
		}
		else{
			editHolder = storage.getActiveEntries().get((int)command.getData()-1);
			command.setData(null);
		}
		
		return command;
	}

	/**
	 * @param command
	 * @return
	 */
	private CMD display(CMD command) {
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
	}


	/**
	 * @param command
	 * @return
	 */
	private CMD clear(CMD command) {
		command.setData(storage.getActiveEntries());
		undo.push(command);
		storage.clearActive();
		storage.save(true, false);
		return command;
	}

	/**
	 * @param command
	 * @return
	 */
	private CMD remove(CMD command) {
		//if there is nothing in command.getData, get active list over to tempList, 
					//then ask user what he want to remove
		//if command.data contains hashtag, do a search for the hashtag, port to tempList, 
					//then ask user what he want to remove
		//if there is something in the Storage's tempList 
		
		if(isInteger(command.getData())) {
			Integer i = (Integer) command.getData(); 
			if(i<1 || i>storage.getDisplayEntries().size()){
				command.setCommandType(Processor.COMMAND_TYPE.ERROR);
				command.setData("Invalid Index.\n");
				return command;
			}

			command.setData(storage.getDisplayEntries().get(i-1));
			undo.push(command);
			storage.removeEntry(i);
			storage.save(true, false);
		}
		return command;
	}

	/**
	 * @param command
	 * @return
	 */
	private CMD add(CMD command) {
		if(command.getData()!=null){
			editHolder = (Entry) command.getData();
			storage.addEntry(editHolder);
			storage.save(true, false);
			command.setData(editHolder);
			undo.push(command);
		}
					
		return command;
	}

	/**
	 * @param command
	 * @return
	 */
	
	private CMD redo(CMD command) {
		if(redo.isEmpty()) {
			command.setData("There are no further redo-s.\n");
			return command; 
		}
		
		CMD action = redo.pop();
		undo.push(action);
		
		switch(action.getCommandType()){
		case EDIT:
			Entry original = (Entry)action.getData();
			action.setData(storage.removeEntry(original.getID()));
			storage.addEntry(original);
			
			break;
			default:
				carryOutCMD(action);
			
		}
		if(action.getCommandType()==Processor.COMMAND_TYPE.DONE){
			storage.save(true, true);
		}
		else {
			storage.save(true, false);
		}
		
		command.setData("Redo completed\n");
		return command;
	}

	/**
	 * @param command
	 * @return
	 */
	
	private CMD undo(CMD command) {
		if(undo.isEmpty()) {
			command.setData("There are no further undo-s.\n");
			return command; 
		}
		
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
			storage.setDisplayEntries(storage.getActiveEntries());
			break;
		case EDIT: 
			Entry original = (Entry)action.getData();
			action.setData(storage.removeEntry(original.getID()));
			storage.addEntry(original);
			
			break;		//each entry needs a unique identity for this to work...
		case DONE:
			storage.undoDoneAction((Entry)action.getData());
			break;		//storage function to move it back...Entry is given under data of Undo
			default: 
				logger.severe("Unexpected CMD action recorded.");
				assert false;	
		}
		
		if(action.getCommandType()==Processor.COMMAND_TYPE.DONE){
			storage.save(true, true);
		}
		else {
			storage.save(true, false);
		}
		
		
		command.setData("Undo completed.\n");
		
		return command;
		
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
		return editHolder;
	}

	public void setEditHolder(Entry tempHold) {
		this.editHolder = tempHold;
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
		CMD clone = new CMD(Processor.COMMAND_TYPE.EDIT, new Entry(editHolder));
		
		if(cmd.length > 1 && cmd[1] != null) cmd[1] = cmd[1].trim();
		if(cmd[0].equals("description")){	
			if(cmd[1]!=null && cmd[1].length()!=0)
				editHolder.setDesc(cmd[1]);
		} else if(cmd[0].equals("duedate")){
			if(processor.isDate(cmd[1])){			//to be amended
				editHolder.iniDDate();
				editHolder.setDateCal(cmd[1]);
			}
			else cmd = new String[] {"Error", "Invalid entry for date."};
		} else if(cmd[0].equals("starttime")){
			if(cmd[1].endsWith("am") || cmd[1].endsWith("pm"))
				editHolder.setStart(cmd[1]);
			else cmd = new String[] {"Error", "Invalid time entry."};
		} else if(cmd[0].equals("endtime")){
			if(cmd[1].endsWith("am") || cmd[1].endsWith("pm"))
				editHolder.setEnd(cmd[1]);
			else cmd = new String[] {"Error", "Invalid time entry."};
		} else if(cmd[0].equals("hash")){
			if(cmd[1].startsWith("#"))
				editHolder.setTagDesc(cmd[1]);
			else cmd = new String[] {"Error", "Not a hashtag"};
		} else if(cmd[0].equals("display")){
			cmd = new String[]{cmd[0], null};
			cmd[1] = printEntry(editHolder);
		} else if(cmd[0].equals("priority")){
			boolean restrictedWords = cmd[1].equalsIgnoreCase("high") || 
					cmd[1].equalsIgnoreCase("medium") || cmd[1].equalsIgnoreCase("low");
			if(restrictedWords){
				editHolder.setPriority(cmd[1].toUpperCase());
				//sort
			}
			else cmd = new String[] {"Error", "Not a priority"};
		} else if(cmd[0].equals("venue")){
			if(cmd[1].startsWith("@"))
				editHolder.setVenue(cmd[1]);
			else cmd = new String[] {"Error", "Invalid format for location."};
		} else if(cmd[0].equals("help")){
			return cmd;
		} else if(cmd[0].equals("end")){
			return cmd;
		} else {
			cmd = new String[] {"Error", "Invalid edit field."};
		}
		
		
		if(!cmd[0].equals("Error")) {
			undo.push(clone);
			Collections.sort(storage.getActiveEntries());
			storage.save(true, false);
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