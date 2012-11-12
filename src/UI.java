import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class UI extends JFrame implements ActionListener {

	/**
	 * Implements serialization
	 * Define attributes
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextArea mainArea;
	Vector<String> columnNames;
	private JTable table;
	private String userInput;

	/**
	 * @Author A084644Y
	 * Launch the application.
	 */
	public static void main(String[] args) {	

		Runnable run = new Runnable(){
			public void run() {
				try {	
					UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
					UI frame = new UI();
					frame.setVisible(true);
					frame.textField.requestFocus();
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

		editMode = false;
		control = Control.getInstance();

		logger.info("Initialization Complete.");

		//END

		setMainUIWindow();
		setUserInputField();
		setMainOutputField();
		setMainDisplayField();
	}
	
	/**
	 * Create the main UI window with specified size
	 */
	private void setMainUIWindow() {
		setTitle("FingerTips");
		setResizable(false);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1160, 636);
		getContentPane().setLayout(null);
	}

	/**
	 * Creates the left component on UI (main display for table)
	 */
	private void setMainDisplayField() {
		setMainDisplayHeading();
		setMainDisplayTable();
		setMainDisplayBorder();
	}
	
	/**
	 * Creates label component as Table title
	 */
	private void setMainDisplayHeading() {
		JLabel lblCurrentList = new JLabel("Task List");
		lblCurrentList.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrentList.setOpaque(true);
		lblCurrentList.setBackground(new Color(192, 80, 77));
		lblCurrentList.setForeground(new Color(255, 255, 255));
		lblCurrentList.setFont(new Font("Century Gothic", Font.PLAIN, 22));
		lblCurrentList.setBounds(16, 18, 703, 24);
		getContentPane().add(lblCurrentList);
	}
	
	/**
	 * Creates Table components
	 */
	private void setMainDisplayTable() {
		setTableHeading();
	    setTableFormat();
		setTableColSize();
		setTableScroll();
	}

	/**
	 * Set table headings for individual columns
	 */
	private void setTableHeading() {
		columnNames = new Vector<String>();
		columnNames.add("ID");
		columnNames.add("Task");
		columnNames.add("Start Time");
		columnNames.add("End Time");
		columnNames.add("Venue");
		columnNames.add("Priority");

		Vector<Vector<String>> data = getDataToDisplay();

		table = new JTable(data, columnNames);
		table.getTableHeader().setFont(new Font("Consolas", Font.BOLD, 13));
		table.getTableHeader().setBackground(new Color(24, 26, 28));
		table.getTableHeader().setForeground(new Color(255, 255, 255));
	}
	
	/**
	 * Get data to be displayed in the table
	 * @return data
	 */
	private Vector<Vector<String>> getDataToDisplay() {
		Vector<Entry> a = control.getStorage().getDisplayEntries();
		Vector<Vector<String>> data = convertToVV(a);
		return data;
	}
	
	/**
	 * Sets the overall format of the table
	 */
	private void setTableFormat() {
		setTableColSize();
		TableCellRenderer renderer = setTableDesign();
		table.setDefaultRenderer(Object.class, renderer);
		table.getTableHeader().getDefaultRenderer();
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(false);
		table.setEnabled(false);
		table.setFont(new Font("Consolas", Font.PLAIN, 13));
		//table.textShadow = false;
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	/**
	 * Sets design of the table (alternate row colour, text alignment)
	 * @return renderer
	 */
	private TableCellRenderer setTableDesign() {
		TableCellRenderer renderer = new TableCellRenderer() {
			JLabel label = new JLabel();
			
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				setTextAlignment(column);
				label.setOpaque(true);
				label.setFont(new Font("Century Gothic", Font.PLAIN, 13));
				if (value != null) {
					label.setText("" + value);
				} else {
					label.setText("");
				}
				Color alternate = UIManager.getColor("table.alternateRowColor");
				if (row % 2 == 1) {
					label.setBackground(alternate);
					label.setForeground(alternate);
				} else {
					label.setBackground(new Color(220, 220, 220));
					label.setForeground(new Color(97, 20, 3));
				}
				return label;
			}

			private void setTextAlignment(int column) {
				if (column != 1) {
					label.setHorizontalAlignment(SwingConstants.CENTER);
				}
				else {
					label.setHorizontalAlignment(SwingConstants.LEFT);
				}
			}
		
			
		};
		return renderer;
	}

	/**
	 * Sets table column width
	 */
	private void setTableColSize() {
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(240);
		table.getColumnModel().getColumn(2).setPreferredWidth(130);
		table.getColumnModel().getColumn(3).setPreferredWidth(130);
		table.getColumnModel().getColumn(4).setPreferredWidth(110);
		table.getColumnModel().getColumn(5).setPreferredWidth(55);
	}
	
	/**
	 * Create scrollbar for table
	 */
	private void setTableScroll() {
		JScrollPane scrollPane2 = new JScrollPane(table);
		scrollPane2.setOpaque(true);
		scrollPane2.setBorder(new LineBorder(new Color(4, 5, 6)));
		scrollPane2.setBackground(new Color(24, 26, 28));
		scrollPane2.setBounds(16, 48, 705, 545);
		scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane2);
	}
	
	/**
	 * Sets the border format and color for left component on UI (main display for table)
	 */
	private void setMainDisplayBorder() {
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(192, 80, 77), 2));
		panel.setBackground(new Color(4, 5, 6));
		panel.setBounds(9, 10, 718, 591);
		getContentPane().add(panel);
	}
	
	/**
	 * Creates the main output field (top right of UI)
	 */
	private void setMainOutputField() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(734, 10, 411, 505);
		scrollPane.setAutoscrolls(true);
		getContentPane().add(scrollPane);
		scrollPane.setBorder(new LineBorder(new Color(192, 80, 77), 2));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		mainArea = new JTextArea();
		printWelcomeMSG();
		scrollPane.setViewportView(mainArea);
//		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
//			public void adjustmentValueChanged(AdjustmentEvent e) {  
//			e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
//			}});
		
		mainArea.setEditable(false);
		mainArea.setFont(new Font("Consolas", Font.PLAIN, 13));
		mainArea.setBackground(new Color(48, 48, 48));
		mainArea.setWrapStyleWord(true);
		mainArea.setLineWrap(true);

		mainArea.append("\nCommand: ");
	}
	
	/**
	 * Creates the field for user input
	 */
	private void setUserInputField() {
		textField = new JTextField();
		textField.setBackground(new Color(48, 48, 48));
		textField.setBounds(734, 526, 412, 76);
		textField.setFont(new Font("Consolas", Font.PLAIN, 13));
		getContentPane().add(textField);
		textField.setColumns(10);
		textField.setBorder(new TitledBorder(new LineBorder(new Color(192, 80, 77), 3), "Enter input:", TitledBorder.LEADING, TitledBorder.TOP, new Font("Consolas", Font.PLAIN, 12), new Color(105, 105, 105)));
		textField.addActionListener(new inputListener());
	}
	
	/**
	 * Conversion of each entry field from Vector<Entry> to Vector<String>
	 */
	private Vector<Vector<String>> convertToVV(Vector<Entry> a) {
		Vector<Vector<String>> out = new Vector<Vector<String>>();
		for(int j=0; j<a.size(); j++)
			out.add(new Vector<String>());

		int i = 0;

		for(Entry e : a){
			Vector<String> v = out.get(i);
			v.add(i+1+"");
			v.add(e.getDesc());
			v.add(e.getFromString());
			v.add(e.getToString());
			v.add(e.getVenue());
			v.add(e.getPriority());
			i++;
		}

		return out;
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		userInput = textField.getText();
	}

	//FingerTips portion

	private static final String MSG_WELCOME = "Welcome To FingerTips!\n";
	private static final String MSG_DEFAULT_ASSISTANCE = "Enter \"help\" for further usage instructions.\n";

	private static final String SUCCESS_MSG_ADD = "Added.\n";
	private static final String SUCCESS_MSG_REMOVE = "Removed.\n";
	private static final String SUCCESS_MSG_DONE = "Task marked as done and shifted to archive.\n";
	private static final String SUCCESS_MSG_CLEAR = "All active tasks deleted.\n";
	private static final String SUCCESS_MSG_CLEARP = "All archive tasks deleted.\n";
	private static final String SUCCESS_MSG_EXIT = "Goodbye.\n";
	private static final String ERROR_MSG_DISPLAY = "There is nothing to print.";
	
	private static final Logger logger = Logger.getLogger(UI.class.getName());
	private static final String logFile = "runLog.log";
	private static final Level handlerLevel = Level.FINE;
	private static final Level loggerLevel = Level.FINE;

	private Control control;
	private boolean editMode;

	/**
	 * Activates the following block of code when enter is hit for textField.
	 * Edit Mode:  
	 */
	class inputListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//Take in userInput
			userInput = textField.getText().trim();

			textField.setText(null);
			mainArea.append(userInput);

			if(editMode){	
				//EditMode

				//Process input
				String[] response = control.processEditMode(userInput);

				//Carry out follow up.
				if(response[0].equals("display")) mainArea.append("\n" + response[1]);
				else if(response[0].equals("help")) mainArea.append("\n" +helpEditMode());
				else if(response[0].equals("Error")) mainArea.append("\n" +response[1]);
				else if(response[0].equals("end")) {
					editMode = false;
					control.setEditHolder(null);
					mainArea.append("\nEdit Mode ended.");
					mainArea.append("\n\nCommand: ");
					return;
				}

				mainArea.append("\n\nCommand (Edit Mode): ");

			} else {	
				//Normal Mode

				//Process and take action on userInput
				runUserInput(userInput);

				if(editMode) mainArea.append("\nCommand (Edit Mode): ");
				else mainArea.append("\nCommand: ");

			}

			//refresh display
			refreshTable();
		}
	}

	/**
	 * Initializes Logger for tracking.
	 */
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

	/**
	 * Process user input
	 * @param userInput
	 */
	public void runUserInput(String userInput) {
		CMD actionMSG = control.performAction(userInput);

		logger.info(actionMSG.toString());

		followUpAction(actionMSG);
	}

	/**
	 * refresh display table according to latest command by user
	 */
	private void refreshTable() {
		Vector<Vector<String>> data = getDataToDisplay();

		DefaultTableModel dm = new DefaultTableModel(data, columnNames);
		table.setModel(dm);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setTableColSize();
		dm.fireTableDataChanged();
	}

	private void followUpAction(CMD actionMSG) {
		switch(actionMSG.getCommandType()){
		case ADD: 		add(actionMSG); 		break;
		case REMOVE: 	remove(actionMSG);		break;
		case UNDO: 		undo(actionMSG);		break;
		case DISPLAY:	display(actionMSG);		break;
		case DISPLAYP:	display(actionMSG);		break;
		case EDIT:		edit();					break;
		case DONE: 		done(actionMSG);		break;
		case HELP: 		mainArea.append("\n" + help());
		break;
		case QUIT: 		quit();					break;
		case CLEAR:		clear();				break;
		case CLEARP:	clearP();				break;
		case ERROR:		error(actionMSG);		break;
		case REDO:		redo(actionMSG);		break;
		default: undo(actionMSG);
		}
	}

	/**
	 * Add Action
	 * @param actionMSG
	 */
	private void add(CMD actionMSG) {
		control.setEditHolder(null);
		Collections.sort(control.getStorage().getActiveEntries());
		mainArea.append("\n" +SUCCESS_MSG_ADD);
	}

	/**
	 * Edit Mode
	 */
	private void edit() {
		editMode = true;
		mainArea.append("\n" +"Entry: ");
		mainArea.append(control.processEditMode("display")[1]);

		mainArea.append("\n" +"Enter the field you wish to modify, and the new data to \nreplace with.");
		mainArea.append("\n" +"Type \"end\" to exit edit mode and \"help\" for futher \nassistance.\n");
	}
	
	/**
	 * Display Action
	 * @param actionMSG
	 */
	@SuppressWarnings("unchecked")
	private void display(CMD actionMSG) {
		Vector<Entry> print = (Vector<Entry>) actionMSG.getData();
		if(print.isEmpty()){
			mainArea.append("\n" +ERROR_MSG_DISPLAY);
		}
		mainArea.append("\n" + print.get(0).toString());
	}
	
	/**
	 * Remove Action
	 * @param actionMSG
	 */
	private void remove(CMD actionMSG) {
		mainArea.append("\n" +SUCCESS_MSG_REMOVE);
	}

	/**
	 * Mark done Action
	 * @param actionMSG
	 */
	private void done(CMD actionMSG) {
		mainArea.append("\n" +SUCCESS_MSG_DONE);
	}

	/**
	 * Undo Action
	 * @param actionMSG
	 */
	private void undo(CMD actionMSG) {
		mainArea.append("\n" +actionMSG.getData());
	}
	
	/**
	 * Redo Action
	 * @param actionMSG
	 */
	private void redo(CMD actionMSG) {
		mainArea.append("\n" +actionMSG.getData());
	}

	/**
	 * Clear ActiveList Action
	 */
	private void clear() {
		mainArea.append("\n" +SUCCESS_MSG_CLEAR);
	}
	
	/**
	 * Clear ArchiveList Action
	 */
	private void clearP() {
		mainArea.append("\n" +SUCCESS_MSG_CLEARP);
	}

	/**
	 * Quit Action
	 */
	private void quit() {
		mainArea.append("\n" +SUCCESS_MSG_EXIT);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * Error encountered. Prints ERROR_MSG
	 * Assumes:	CMD.data is a String
	 * @param actionMSG
	 */
	private void error(CMD actionMSG) {
		mainArea.append("\n" +actionMSG.getData());
	}
	
	
	/**
	 * Print welcome message on launch of program
	 */
	private void printWelcomeMSG() {
		mainArea.append(MSG_WELCOME);
		mainArea.append(MSG_DEFAULT_ASSISTANCE);
	}

	/**
	 * Prints default help message
	 * @return string of help messages
	 */
	private static String help() {
		return ("\nadd <data>:\t    add a task with related dates,\n\t\t    description, priority etc.\n") +
				("\t\t    prefix @ indicates venue, prefix\n\t\t    # indicates a hashtag.\n") +
				("\nremove <ID>:\t    remove selected task from active\n\t\t    list.\n") +
				("\nedit <ID>:\t    enters edit mode for selected task.\n") +
				("\nundo:\t\t    reverses the previous action.\n") +
				("\ndisplay <keyword>:  generates a list of active tasks\n\t\t    fulfilling the search criteria.\n") +
				("\ndisplay+ <keyword>: generates a list of archived tasks\n\t\t    fulfilling the search criteria.\n") +
				("\ndone <number>:\t    marks a task as completed.\n") +
				("\nclear:\t\t    deletes all active tasks\n\t\t    permanently (use with caution!).\n") +
				("\nclear+:\t\t    deletes all archived task\n\t\t    permanently (use with caution!).\n") +
				("\nquit:\t\t    terminates the program.\n");
	}

	/**
	 * Prints edit mode help message
	 * @return string of help messages for edit mode
	 */
	private static String helpEditMode() {
		return ("\nEnter a field followed by the new data it should be replaced with.\n") +
				("desc:\t  edit description\n") +
				("ddate:\t  edit due date\n") +
				("display:  shows all details of the selected task\n") +
				("priority: edit priority\n") +
				("hash #:\t  edit hash tags\n") +
				("st:\t  edit start time\n") +
				("et:\t  edit end time\n") +
				("venue @:  edit venue");
	}

	/**
	 * for logger initialization use
	 * @return logger
	 */
	public static Logger getLoggingParent(){
		return logger;
	}
}