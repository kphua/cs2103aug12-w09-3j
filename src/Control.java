import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

class Control {
	private Processor processor;
	private Storage storage;
	private boolean edit, newList; // modes
	private ArrayList<Entry> tempList;

	private String MSG_ERROR = "Invalid input!";

	public Control() throws FileNotFoundException {
		// initialise.
		// load entries
		processor = new Processor();
//		storage = new Storage();
//		tempList = storage.getActiveEntries();
//		Collections.sort(tempList);
	}

	public CMD performAction(String userInput) {

		CMD command = processor.translateToCMD(userInput);

		// CMD will be a Pair of enumeration, data

		switch (command.getCommandType()) {
		// need to store previous version everytime in case of undo action
//		case ADD:
//			storage.addEntry(command.getData());
			//modify undo
			//activate edit mode.
			//ensure save
//			return processor.add(command.getData());
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
//		case DISPLAY:
//			return storage.display(command.getData());
//		case EDIT:
//			return processor.edit(command.getData());
		case QUIT:
			return command;
		case HELP:
			return command;
		default:
			return command;
		}
		
	}
}