
class Control {
	private Processor processor;
	private Storage storage;
	private boolean edit, newList;		//modes
	
	public Control(){
		//initialise.
		//load entries
		//print welcome/help msg etc.
	}

	public String performAction(String userInput) {
		// TODO Auto-generated method stub
		
		CMD[] commands = processor.translateToCMD(userInput);
		
		//CMD will be a Pair of enumeration, data
		
		for(CMD cmd: commands){
			switch(cmd.type){
				default: 
				return "Invalid Command";
			}
		}
		
		return null;
	}
}
