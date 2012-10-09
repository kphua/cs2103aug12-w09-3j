import java.io.FileNotFoundException;

class Control {
	private Processor processor;
	private Storage storage;
	private boolean edit, newList; // modes

	private String MSG_ERROR = "Invalid input!";

	public Control() throws FileNotFoundException {
		// initialise.
		// load entries
		processor = new Processor();
		storage = new Storage();
	}

	public String performAction(String userInput) {
		// TODO Auto-generated method stub

		CMD commands = processor.translateToCMD(userInput);

		// CMD will be a Pair of enumeration, data

		switch (commands.getCommandType()) {
		// need to store previous version everytime in case of undo action
		case ADD:
			return processor.add(commands.getData());
		case REMOVE:
			storage.display();
			return processor.remove(commands.getData());
		case UNDO:
			return storage.undo(commands.getData());
		case DISPLAY:
			return storage.display(commands.getData());
		case EDIT:
			return processor.edit(commands.getData());
		case QUIT:
			return storage.quit(commands.getData());
		default:
			return MSG_ERROR;
		}
		
		return null;
	}
}