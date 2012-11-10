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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class UI extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextArea mainArea;
	Vector<String> columnNames;
	private JTable table;
	private String userInput;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {	
		
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
		editMode = false;
		control = Control.getInstance();
		
		logger.info("Initialization Complete.");
		
		
		//END
		
		setMainUIWindow();
		setUserInputField();
		setMainOutputField();
		setMainDisplayField();
	}
	
	private void setMainUIWindow() {
		setTitle("FingerTips");
		setResizable(false);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1140, 550);
		getContentPane().setLayout(null);
	}

	private void setMainDisplayField() {
		setMainDisplayHeading();
		setMainDisplayTable();
		setMainDisplayFieldBorder();
	}

	private void setMainDisplayHeading() {
		JLabel lblCurrentList = new JLabel("Task List");
		lblCurrentList.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrentList.setOpaque(true);
		lblCurrentList.setBackground(new Color(192, 80, 77));
		lblCurrentList.setForeground(new Color(255, 255, 255));
		lblCurrentList.setFont(new Font("Consolas", Font.PLAIN, 22));
		lblCurrentList.setBounds(15, 26, 670, 24);
		getContentPane().add(lblCurrentList);
	}

	private void setMainDisplayTable() {
		setTableHeading();
	    setTableColFormat();
		setTableColSize();
		setTableScroll();
	}

	private void setTableHeading() {
		columnNames = new Vector<String>();
		columnNames.add("ID");
		columnNames.add("Task");
		columnNames.add("Start Time");
		columnNames.add("End Time");
		columnNames.add("Due Date");
		columnNames.add("Priority");
		
		Vector<Vector<String>> data = getDataToDisplay();
		
		table = new JTable(data, columnNames);
		table.getTableHeader().setFont(new Font("Consolas", Font.BOLD, 13));
		table.getTableHeader().setBackground(new Color(75, 172, 198));
		table.getTableHeader().setForeground(new Color(255, 255, 255));
	}

	private Vector<Vector<String>> getDataToDisplay() {
		Vector<Entry> a = control.getStorage().getDisplayEntries();
		Vector<Vector<String>> data = convertToVV(a);
		return data;
	}

	private void setTableColFormat() {
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
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	private void setTableColSize() {
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(290);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.getColumnModel().getColumn(4).setPreferredWidth(90);
		table.getColumnModel().getColumn(5).setPreferredWidth(89);
	}

	private void setTableScroll() {
		JScrollPane scrollPane2 = new JScrollPane(table);
		scrollPane2.setOpaque(true);
		scrollPane2.setBorder(new LineBorder(new Color(252, 213, 181)));
		scrollPane2.setBackground(new Color(252, 213, 181));
		scrollPane2.setBounds(15, 61, 670, 439);
		scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane2);
	}

	private void setMainDisplayFieldBorder() {
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(192, 80, 77), 2));
		panel.setBackground(new Color(252, 213, 181));
		panel.setBounds(10, 20, 680, 485);
		getContentPane().add(panel);
	}

	private void setMainOutputField() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(700, 21, 420, 403);
		getContentPane().add(scrollPane);
		scrollPane.setBorder(new LineBorder(new Color(139, 0, 139), 2));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		mainArea = new JTextArea();
		printWelcomeMSG();
		scrollPane.setViewportView(mainArea);
		// set view to stick to the bottom of the message box, but cannot 
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			public void adjustmentValueChanged(AdjustmentEvent e) {  
			e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
			}});
		
		mainArea.setEditable(false);
		mainArea.setFont(new Font("Consolas", Font.PLAIN, 13));
		mainArea.setBackground(new Color(204, 193, 218));
		mainArea.setWrapStyleWord(true);
		mainArea.setLineWrap(true);
		
		mainArea.append("\nCommand: ");
	}

	private void setUserInputField() {
		textField = new JTextField();
		textField.setBackground(new Color(240, 240, 240));
		textField.setBounds(700, 432, 420, 73);
		textField.setFont(new Font("Consolas", Font.PLAIN, 13));
		getContentPane().add(textField);
		textField.setColumns(10);
		textField.setBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192), 3), "Enter input:", TitledBorder.LEADING, TitledBorder.TOP, new Font("Consolas", Font.PLAIN, 11), new Color(102, 102, 102)));
		textField.addActionListener(new inputListener());
	}
	
	private Vector<Vector<String>> convertToVV(Vector<Entry> a) {
		Vector<Vector<String>> out = new Vector<Vector<String>>();
		for(int j=0; j<a.size(); j++)
			out.add(new Vector<String>());
		
		int i = 0;
		
		for(Entry e : a){
			Vector<String> v = out.get(i);
			v.add(i+1+"");
			v.add(e.getDesc());
			v.add(e.getStart());
			v.add(e.getEnd());
			v.add(e.getDate());
			v.add(e.getPriority());
			i++;
		}
		
		return out;
	}
	
	@Override
	public void actionPerformed(ActionEvent a) {
		userInput = textField.getText();
		//display_output = control.performAction(userInput);
	}
	
	//FingerTips portion
	
	private static final String MSG_WELCOME = "Welcome To FingerTips!\n";
	private static final String MSG_DEFAULT_ASSISTANCE = "Enter \"help\" for further usage instructions.\n";
	
	private static final String SUCCESS_MSG_ADD = "Added.\n";
	private static final String SUCCESS_MSG_REMOVE = "Removed.\n";
	private static final String SUCCESS_MSG_DONE = "Entry marked as done and shifted to archive.\n";
	private static final String SUCCESS_MSG_CLEAR = "All active entries deleted.\n";
	private static final String SUCCESS_MSG_EXIT = "Goodbye.\n";
	
	private static final Logger logger = Logger.getLogger(UI.class.getName());
	private static final String logFile = "runLog.log";
	private static final Level handlerLevel = Level.FINE;
	private static final Level loggerLevel = Level.FINE;
	
	private Control control;
	private Scanner sc;
	private boolean editMode;
	
	/*
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
	
	//Initializes Logger for tracking.
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
	
	
	public void runUserInput(String userInput) {
		CMD actionMSG = control.performAction(userInput);
		
		logger.info(actionMSG.toString());
		
		followUpAction(actionMSG);
	}

	/**
	 * 
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
		case EDIT:		edit();					break;
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
			control.setEditHolder(null);
			Collections.sort(control.getStorage().getActiveEntries());
			mainArea.append("\n" +SUCCESS_MSG_ADD);
		}
		
		//Edit Mode
		private void edit() {
			editMode = true;
			mainArea.append("\n" +"Entry: ");
			mainArea.append(control.processEditMode("display")[1]);

			mainArea.append("\n" +"Enter the field you wish to modify, and the new data to replace with.");
			mainArea.append("\n" +"Type \"end\" to exit edit mode and \"help\" for futher assistance.\n");
		}

		private void display(CMD actionMSG) {
			Vector<Entry> print = (Vector<Entry>) actionMSG.getData();
			if(print.isEmpty()){
				mainArea.append("\n" +"There is nothing to print.\n");
			}
		}

		private void remove(CMD actionMSG) {
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
			mainArea.append("\n" +SUCCESS_MSG_EXIT);
			 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
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
			mainArea.append(MSG_WELCOME);
			mainArea.append(MSG_DEFAULT_ASSISTANCE);
		}
		
		// default help message
		private static String help() {
			return ("add <data>:\t   add an entry with related dates,\t\t\t\t   description, priority etc.\n") +
					("\t\t   prefix @ indicates venue, prefix #\t\t\t\t   indicates a hashtag.\n") +
					("\nremove <number>:   remove the selected entry for the\t\t\t\t   active list.\n") +
					("\nedit <number>:\t   enters edit mode for selected entry.\n") +
					("\nundo:\t\t   reverses the previous action.\n") +
					("\ndisplay <keyword>: generates a list of entries\t\t\t\t\t   fulfilling the search criteria.\n") +
					("\ndone <number>:\t   marks an entry as completed.\n") +
					("\nclear:\t\t   deletes all entries permanently\t\t\t\t   (use with caution!).\n") +
					("\nquit:\t\t   terminates the program.\n");
		}

		//editMode help
		private static String helpEditMode() {
			return ("\nEnter a field followed by the new data it should be replaced with.\n") +
					("desc:\t  edit description\n") +
					("ddate:\t  edit due date\n") +
					("display:  shows data in the current node\n") +
					("priority: edit priority\n") +
					("hash #:\t  edit hash tags\n") +
					("st:\t  edit start time\n") +
					("et:\t  edit end time\n") +
					("venue @:  edit venue");
		}
		
		//for logger initialization use
		public static Logger getLoggingParent(){
			return logger;
		}
}