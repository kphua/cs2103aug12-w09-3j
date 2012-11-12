
class CMD {

	private Processor.COMMAND_TYPE commandType;
	private Object data;

	public CMD(Processor.COMMAND_TYPE command, Object data) {
		commandType = command;
		this.setData(data);
	}

	public CMD clone(CMD original) {
		return new CMD(original.getCommandType(), original.getData());
	}

	public Processor.COMMAND_TYPE getCommandType() {
		return commandType;
	}

	public void setCommandType(Processor.COMMAND_TYPE commandType) {
		this.commandType = commandType;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String toString() {
		String strVer = "";

		switch (commandType) {
		case ADD:
			strVer = "add ";
			break;
		case REMOVE:
			strVer = "remove ";
			break;
		case CLEAR:
			strVer = "clear";
			break;
		case CLEARP:
			strVer = "clear+";
			break;
		case UNDO:
			strVer = "undo";
			break;
		case EDIT:
			strVer = "edit ";
			break;
		case DISPLAY:
			strVer = "display ";
			break;
		case DISPLAYP:
			strVer = "display+ ";
			break;	
		case DONE:
			strVer = "done ";
			break;
		case ERROR:
			strVer = "ERROR: ";
			break;
		case REDO:
			strVer = "redo";
			break;
		default:
			assert false;
		}

		if (data == null)
			strVer.concat("null");
		else
			strVer.concat(data.toString());

		return strVer;

	}
}
