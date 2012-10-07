class Control {
	private Processor processor;
	private Storage storage;
	private boolean edit, newList; // modes

	private String MSG_ERROR = "Invalid input!";

	public Control() {
		// initialise.
		// load entries
		// print welcome/help msg etc.
		showToUser("Welcome To FingerTips!");
		showToUser("Commands:");
		showToUser(".a/.add to add, .r/.remove to remove, .e/.edit to edit");
		showToUser(".u/.undo to undo, .d/.display to display, .q/.quit to quit");
		showToUser(".h/.help to display help for FingerTips");
		System.out.print("Command: ");
	}

	public static void showToUser(String text) {
		System.out.println(text);
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