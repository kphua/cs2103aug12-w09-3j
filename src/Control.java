import java.io.FileNotFoundException;

class Control {
	private Processor processor;
	private Storage storage;
	private boolean edit, newList; // modes

	private String MSG_ERROR = "Invalid input!";

	public Control() throws FileNotFoundException {
		// initialise.
		// load entries
	}

	public String performAction(String userInput) {
		// TODO Auto-generated method stub

		CMD commands = processor.translateToCMD(userInput);

		// CMD will be a Pair of enumeration, data

		switch (commands.getCommandType()) {
		case ADD:
			return addTask(commands.getCommandType());
		case REMOVE:
			return remTask(commands.getCommandType());
		case UNDO:
			return undoTask(commands.getCommandType());
		case DISPLAY:
			return disTask(commands.getCommandType());
		case EDIT:
			return editTask(commands.getCommandType());
		case QUIT:
			return quitTask(commands.getCommandType());
		default:
			return errorMsg(commands.getCommandType());
		}

		return null;
	}
}