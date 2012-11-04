import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.factories.FormFactory;
//import com.jgoodies.forms.layout.RowSpec;


public class UI extends JFrame implements ActionListener {

	private JPanel mainPane;
	private JTextField textField;
	private String userInput;
	private TableColumn id, task, start_time, end_time, duedate, hashtags;
	private DefaultTableColumnModel headers;
	private JTextArea mainArea;
	Vector<String> columnNames;
	
	private JTable table;
	
	
	
	private FingerTips ft;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		boolean cont = true;
		String input;
		UI frame = null;
		String output;
		
		
		Runnable run = new Runnable(){
			public void run() {
				try {
					UI frame = new UI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		EventQueue.invokeLater(run);

		
	}

	/**
	 * Create the frame.
	 */
	public UI() {
		//FingerTips Portion
		initialiseLogger();
		
		sc = new Scanner(System.in);
		cont = true;
		control = Control.getInstance();
		
		logger.info("Initialization Complete.");
		
		
		//END
		
		setTitle("FingerTips");
		
		setResizable(false);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1100, 550);
		getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBackground(new Color(240, 240, 240));
		textField.setBounds(700, 432, 375, 73);
		textField.setFont(new Font("Consolas", Font.PLAIN, 13));
		getContentPane().add(textField);
		textField.setColumns(10);
		textField.setBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192), 3), "Enter input:", TitledBorder.LEADING, TitledBorder.TOP, new Font("Consolas", Font.PLAIN, 11), new Color(102, 102, 102)));
		textField.addActionListener(new inputListener());
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(700, 21, 375, 403);
		getContentPane().add(scrollPane);
		scrollPane.setBorder(new LineBorder(new Color(139, 0, 139), 2));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		mainArea = new JTextArea(welcome);
		scrollPane.setViewportView(mainArea);
		// set view to stick to the bottom of the message box, but cannot 
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			public void adjustmentValueChanged(AdjustmentEvent e) {  
			e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
			}});
		
		mainArea.setEditable(false);
		mainArea.setFont(new Font("Consolas", Font.PLAIN, 13));
		mainArea.setBackground(new Color(204, 193, 218));
		mainArea.setLineWrap(true);
		
//		mainArea.setLineWrap(true);
//		mainArea.setCaretPosition(mainArea.getText().length() - 1);
//		cols();
		
//		Object[][] data;
		
//		String[] columnNames = {"ID",
//                "Task",
//                "Start Time",
//                "End Time",
//                "Due Date",
//                "Priority"};
		
//		data = new Object[][] {
//			    {"Kathy", "Smith",
//			     "Snowboarding Hahaha Hehehehe Hohohoho ZZZZZZZZZZ", new Integer(5), new Boolean(false), "-"},
//			    {"John", "Doe",
//			     "Rowing", new Integer(3), new Boolean(true), "-"},
//			    {"Sue", "Black",
//			     "Knitting", new Integer(2), new Boolean(false), "-"},
//			    {"Jane", "White",
//			     "Speed reading", new Integer(20), new Boolean(true), "-"},
//			    {"Joe", "Brown",
//			     "Pool", new Integer(10), new Boolean(false), "-"}
//		        };
		
		columnNames = new Vector<String>();
		columnNames.add("ID");
		columnNames.add("Task");
		columnNames.add("Start Time");
		columnNames.add("End Time");
		columnNames.add("Due Date");
		columnNames.add("Priority");
		
		Vector<Entry> a = control.getStorage().getDisplayEntries();
		Vector<Vector> data = convertToVV(a);
		
		table = new JTable(data, columnNames);
		table.getTableHeader().setFont(new Font("Consolas", Font.BOLD, 13));
		table.getTableHeader().setBackground(new Color(75, 172, 198));
		table.getTableHeader().setForeground(new Color(255, 255, 255));
//		final JTable table = new JTable(data, columnNames);
//		table.setBackground(UIManager.getColor("Button.background"));
	    TableCellRenderer renderer = new TableCellRenderer() {
	        JLabel label = new JLabel();
	        @Override
	        public Component getTableCellRendererComponent(JTable table,
	                Object value, boolean isSelected, boolean hasFocus,
	                int row, int column) {
	            label.setOpaque(true);
	            label.setFont(new Font("Consolas", Font.PLAIN, 13));
	            if (value != null) {
	            	label.setText("" + value);
	            } else {
	            	label.setText("");
	            }
	            Color alternate = UIManager.getColor("table.alternateRowColor");
	            if (row % 2 == 1) {
	                label.setBackground(alternate);
	            } else {
	                label.setBackground(new Color(198, 217, 241));
	            }
	            return label;
	        }
	    };
	    table.setDefaultRenderer(Object.class, renderer);
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(false);
		table.setEnabled(false);
		table.setFont(new Font("Consolas", Font.PLAIN, 13));
//		ColumnsAutoSizer.sizeColumnsToFit(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(290);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.getColumnModel().getColumn(4).setPreferredWidth(90);
		table.getColumnModel().getColumn(5).setPreferredWidth(89);
		
	
		// automatically resize the columns whenever the data in the table changes
		table.getModel().addTableModelListener(new TableModelListener() {
		    public void tableChanged(TableModelEvent e) {
		        ColumnsAutoSizer.sizeColumnsToFit(table);
		    }
		});
		
		JScrollPane scrollPane2 = new JScrollPane(table);
		scrollPane2.setOpaque(true);
		scrollPane2.setBorder(new LineBorder(new Color(252, 213, 181)));
		scrollPane2.setBackground(new Color(252, 213, 181));
		scrollPane2.setBounds(15, 61, 670, 439);
		getContentPane().add(scrollPane2);
		
		JLabel lblCurrentList = new JLabel("Task List");
		lblCurrentList.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrentList.setOpaque(true);
		lblCurrentList.setBackground(new Color(192, 80, 77));
		lblCurrentList.setForeground(new Color(255, 255, 255));
		lblCurrentList.setFont(new Font("Consolas", Font.PLAIN, 22));
		lblCurrentList.setBounds(15, 26, 670, 24);
		getContentPane().add(lblCurrentList);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(192, 80, 77), 2));
		panel.setBackground(new Color(252, 213, 181));
		panel.setBounds(10, 20, 680, 485);
		getContentPane().add(panel);
		
		//display_output = control.performAction("display");
		//mainArea.display(output);
		//Document doc = mainArea.getDocument();
		//PrintStream out = new PrintStream(new DocumentOutputStream(doc));
		//out.println("Hello World");

	}
	
	private void setOpacity(boolean b) {
		// TODO Auto-generated method stub
		
	}

	private Vector<Vector> convertToVV(Vector<Entry> a) {
		// TODO Auto-generated method stub
		Vector<Vector> out = new Vector<Vector>();
		for(int j=0; j<a.size(); j++)
			out.add(new Vector());
		
		int i = 0;
		
		for(Entry e : a){
			Vector v = out.get(i);
			v.add(i+1);
			v.add(e.getDesc());
			v.add(e.getStart());
			v.add(e.getEnd());
			v.add(e.getDate());
			v.add(e.getPriority());
			i++;
		}
		
		return out;
	}

	private void cols() {
		id = new TableColumn(0);
		id.setHeaderValue("ID #");
		id.setPreferredWidth(10);
		
		headers = new DefaultTableColumnModel();
		headers.addColumn(id);
		headers.addColumn(task);
		headers.addColumn(start_time);
		headers.addColumn(end_time);
		headers.addColumn(duedate);
		headers.addColumn(hashtags);
	}
	
	static String welcome = "Welcome to FingerTips!\n" +
	"Enter \"help\" for further usage instructions.\n";

	@Override
	public void actionPerformed(ActionEvent a) {
		// TODO Auto-generated method stub
		userInput = textField.getText();
		//display_output = control.performAction(userInput);
	}
	
	//FingerTips portion
	
	private static final String MSG_WELCOME = "\nWelcome To FingerTips!\n";
	private static final String MSG_DEFAULT_ASSISTANCE = "Enter \"help\" for further usage instructions.";
	
	private static final String SUCCESS_MSG_ADD = "Added.\n";
	private static final String SUCCESS_MSG_REMOVE = "Removed.\n";
	private static final String SUCCESS_MSG_DONE = "Entry marked as done and shifted to archive.\n";
	private static final String SUCCESS_MSG_CLEAR = "All active entries deleted.\n";
	private static final String SUCCESS_MSG_EXIT = "Goodbye.\n";
	
	private static final Logger logger = Logger.getLogger(FingerTips.class.getName());
	private static final String logFile = "runLog.log";
	private static final Level handlerLevel = Level.FINE;
	private static final Level loggerLevel = Level.FINE;
	
	private Control control;
	private Scanner sc;
	private boolean cont;
	
	class inputListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			mainArea.append("\nCommand: ");
			String input = textField.getText();
			textField.setText(null);
			mainArea.append(input);
			input = input.trim();
			
			
			runUserInput(input);
			
		}
		
	}
	
	private void initialiseLogger() {
		try {
			Handler h = new FileHandler(logFile);
			h.setFormatter(new SimpleFormatter());
			h.setLevel(handlerLevel);
			logger.addHandler(h);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.setLevel(loggerLevel);
		logger.info("Logger initialization complete.");
	}
	
//	private void runUserInput(String userInput) {
	public void runUserInput(String userInput) {
		CMD actionMSG = control.performAction(userInput);
		
		logger.info(actionMSG.toString());
		
		//followUpAction(actionMSG);
		followUpAction(actionMSG);
		
		Vector<Entry> a = control.getStorage().getDisplayEntries();
		Vector<Vector> data = convertToVV(a);
		
//		table = new JTable(data, columnNames);
//		table.repaint();
		DefaultTableModel dm = new DefaultTableModel(data, columnNames);
		table.setModel(dm);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(290);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.getColumnModel().getColumn(4).setPreferredWidth(90);
		table.getColumnModel().getColumn(5).setPreferredWidth(89);
		dm.fireTableDataChanged();
	}
	
	private void followUpAction(CMD actionMSG) {
		switch(actionMSG.getCommandType()){
		case ADD: 		add(actionMSG); 		break;
		case REMOVE: 	remove(actionMSG);		break;
		case UNDO: 		undo(actionMSG);		break;
		case DISPLAY:	display(actionMSG);		break;
		case EDIT:		edit(actionMSG);		break;
		case DONE: 		done(actionMSG);		break;
		case HELP: 		mainArea.append("\n" + help());
												break;
		case QUIT: 		quit();					break;
		case CLEAR:		clear();				break;
		case ERROR:		error(actionMSG);		break;
		case REDO:		redo(actionMSG);		break;
		default: undo(actionMSG);
		}
	}
	
	//Add action
		private void add(CMD actionMSG) {
			if(actionMSG.getData()==null){
				mainArea.append("\n" +"Please enter a description for your task:");
				String description = textField.getText().trim();
				textField.setText(null);
				description = "add \"".concat(description);
				control.performAction(description);
			}

			//			mainArea.append("\n" +"Add further information? y/n");
			//			String answer = sc.nextLine();
			//			answer = answer.toLowerCase();
			//			while(!(answer.equals("y") || answer.equals("n"))){
			//				mainArea.append("\n" +"Invalid answer.");
			//				mainArea.append("\n" +"Add further information? y/n");
			//				answer = sc.nextLine();
			//			}
			//			
			//			if(answer.equals("y")){
			//				runUserInput("edit");
			//			} else {
			
			control.setTempHold(null);
			Collections.sort(control.getStorage().getActiveEntries());
			mainArea.append("\n" +SUCCESS_MSG_ADD);
			
			//			}
		}
		
		//Edit Mode
		private void edit(CMD actionMSG) {
			String userInput;
			if(actionMSG.getData()==null){

				while(true){
					mainArea.append("\n" +"Entry: ");
					mainArea.append("\n" +control.processEditMode("display")[1]);

					mainArea.append("\n" +"Enter the field you wish to modify, and the new data to replace with.");
					mainArea.append("\n" +"Type \"end\" to exit edit mode and \"help\" for futher assistance.");
					System.out.print("\nCommand (Edit Mode): ");
					userInput = textField.getText().trim();

					userInput = userInput.trim();
					//call processor
					String[] response = control.processEditMode(userInput);
					if(response[0].equals("display")) mainArea.append("\n" +response[1]);
					else if(response[0].equals("help")) mainArea.append("\n" +helpEditMode());
					else if(response[0].equals("end")) break;
					else if(response[0].equals("Error")) mainArea.append("\n" +response[1]);
				}

				control.setTempHold(null);
			}
			else {
//				undo(actionMSG);
				int a;
				while(true){
					try{
						System.out.print("Index: ");
						a = Integer.parseInt(textField.getText());
						sc.nextLine();
						mainArea.append("\n");
						if(control.getStorage().getActiveEntries().size() < a || a<1)
							mainArea.append("\n" +"Invalid input. Enter a valid index number.");
						else{
							runUserInput("edit "+ a);
							break;
						}
					}
					catch(InputMismatchException e){
						mainArea.append("\n" +"Invalid input. Action aborted.");
						break;
					}
				}
			}
		}

		private void display(CMD actionMSG) {
			Vector<Entry> print = (Vector<Entry>) actionMSG.getData();
			if(print.isEmpty()){
				mainArea.append("\n" +"There is nothing to print.");
			}
//			else{
//				int j=1;
//
//				for (int i=0; i<print.size(); i++) {
//					mainArea.append("\n" + j + ". " + print.get(i).toString());
//					j++;
//				}
//			}
		}

		private void remove(CMD actionMSG) {
			if(actionMSG.getData() == null){
				mainArea.append("\n" +"Which entry do you want to remove?");
				int rmvIndex;
				try{
					System.out.print("Index: ");
					rmvIndex = Integer.parseInt(textField.getText());
					String newInstruction = "remove ".concat(Integer.toString(rmvIndex));
					control.performAction(newInstruction);
					mainArea.append("\n");
					
				} catch(InputMismatchException e){
					mainArea.append("\n" +"Invalid input. Action aborted.");

				}

				sc.nextLine();
			}
			mainArea.append("\n" +SUCCESS_MSG_REMOVE);
		}

		//Follow up for DONE
			private void done(CMD actionMSG) {
				mainArea.append("\n" +SUCCESS_MSG_DONE);
			}
		
		//Prints action taken.
		private void undo(CMD actionMSG) {
			
			mainArea.append("\n" +actionMSG.getData());
		}
		
		private void redo(CMD actionMSG) {
			
			mainArea.append("\n" +actionMSG.getData());
		}
		
		//Prints SUCCESS MSG for Clear
		private void clear() {
			mainArea.append("\n" +SUCCESS_MSG_CLEAR);
		}

		//Ending MSG
		private void quit() {
			cont = false;
			mainArea.append("\n" +SUCCESS_MSG_EXIT);
			 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}
		
		
		//Print ERROR msg
		//Assumes:	CMD.data is a String
		private void error(CMD actionMSG) {
			mainArea.append("\n" +actionMSG.getData());
		}
		
		//Welcome Message
		private void printWelcomeMSG() {
			mainArea.append("\n" +MSG_WELCOME);
			mainArea.append("\n" +MSG_DEFAULT_ASSISTANCE);
		}
	
		private static String help() {
//			System.out.println("add <data>:\t\t   add an entry with related dates, description, priority etc.");
//			System.out.println("\t\t\t   prefix @ indicates venue, prefix # indicates a hashtag.");
//			System.out.println("remove <number>:\t   remove the selected entry for the active list.");
//			System.out.println("edit <number>:\t\t   enters edit mode for selected entry.");
//			System.out.println("undo:\t\t\t   reverses the previous action.");
//			System.out.println("display:\t\t   shows the activelist.");
//			System.out.println("display <search criteria>: generates a list of entries fulfilling the search criteria.");
//			System.out.println("done <number>:\t\t   marks an entry as completed");
//			System.out.println("clear:\t\t\t   deletes all entries permanently (use with caution!)");
//			System.out.println("quit:\t\t\t   terminates the program.");
			return ("add <data>:\t   add an entry with related dates,\t\t   description, priority etc.\n") +
					("\t\t   prefix @ indicates venue, prefix\t\t   # indicates a hashtag.\n") +
					("remove <number>:   remove the selected entry for\t\t   the active list.\n") +
					("edit <number>:\t   enters edit mode for selected\t\t   entry.\n") +
					("undo:\t\t   reverses the previous action.\n") +
					("display <keyword>: generates a list of entries\t\t\t   fulfilling the search criteria.\n") +
					("done <number>:\t   marks an entry as completed.\n") +
					("clear:\t\t   deletes all entries permanently\t\t   (use with caution!).\n") +
					("quit:\t\t   terminates the program.\n");
		}

		//editMode help
		private static String helpEditMode() {
//			System.out.println("\nEnter a field followed by the new data it should be replaced with.");
//			System.out.println("desc:     edit description.");
//			System.out.println("ddate:    edit due date.");
//			System.out.println("display:  shows data in the current node.");
//			System.out.println("priority: edit priority");
//			System.out.println("hash #:   edit hash tags");
//			System.out.println("st:       edit start time");
//			System.out.println("et:       edit end time");
//			System.out.println("venue @:  edit venue");
			return ("\nEnter a field followed by the new data it should be replaced with.\n") +
					("desc:\t edit description\n") +
					("ddate:\t edit due date\n") +
					("display:\t shows data in the current node\n") +
					("priority:\t edit priority\n") +
					("hash #:\t edit hash tags\n") +
					("st:\t edit start time\n") +
					("et:\t edit end time\n") +
					("venue @:\t edit venue\n");
		}
		
		//for logger initialization use
		public static Logger getLoggingParent(){
			return logger;
		}
}


