import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.border.BevelBorder;

import javax.swing.JTextField;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.border.TitledBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.Dialog.ModalExclusionType;
import javax.swing.SwingConstants;
import java.awt.Window.Type;
import java.awt.TextArea;
//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.factories.FormFactory;
//import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.*;

import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.ImageIcon;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JTable;
import java.awt.Panel;
import javax.swing.JSeparator;
import java.awt.TextField;
import java.awt.Label;
import java.awt.List;
import javax.swing.UIManager;

public class UI extends JFrame implements ActionListener {

	private JPanel mainPane;
	private JTextField textField;
	private String userInput;
	private TableColumn id, task, start_time, end_time, duedate, hashtags;
	private DefaultTableColumnModel headers;
	private JTextArea mainArea;
	
	
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
		ft = new FingerTips();
		
		setTitle("FingerTips");
		
		setResizable(false);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 548);
		getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBackground(new Color(245, 245, 245));
		textField.setBounds(609, 438, 375, 73);
		getContentPane().add(textField);
		textField.setColumns(10);
		textField.setBorder(new TitledBorder(new CompoundBorder(new BevelBorder(BevelBorder.LOWERED, new Color(204, 204, 204), new Color(153, 153, 153), new Color(204, 204, 204), new Color(153, 153, 153)), new BevelBorder(BevelBorder.LOWERED, new Color(204, 204, 204), new Color(153, 153, 153), new Color(204, 204, 204), new Color(153, 153, 153))), "Enter input:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(102, 102, 102)));
		textField.addActionListener(new inputListener());
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(609, 24, 375, 403);
		getContentPane().add(scrollPane);
		scrollPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		mainArea = new JTextArea(welcome);
		scrollPane.setViewportView(mainArea);
		
		mainArea.setEditable(false);
		mainArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mainArea.setBackground(new Color(230, 230, 250));
		
//		mainArea.setLineWrap(true);
//		mainArea.setCaretPosition(mainArea.getText().length() - 1);
//		cols();
		
		String[] columnNames = {"ID",
                "Task",
                "Start Time",
                "End Time",
                "Due Date",
                "Priority"};
		
		Object[][] data = {
			    {"Kathy", "Smith",
			     "Snowboarding Hahaha Hehehehe Hohohoho ZZZZZZZZZZ", new Integer(5), new Boolean(false), "-"},
			    {"John", "Doe",
			     "Rowing", new Integer(3), new Boolean(true), "-"},
			    {"Sue", "Black",
			     "Knitting", new Integer(2), new Boolean(false), "-"},
			    {"Jane", "White",
			     "Speed reading", new Integer(20), new Boolean(true), "-"},
			    {"Joe", "Brown",
			     "Pool", new Integer(10), new Boolean(false), "-"}
		        };
		
		final JTable table = new JTable(data, columnNames);
		table.setBackground(UIManager.getColor("Button.background"));
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(false);
		table.setEnabled(false);
		ColumnsAutoSizer.sizeColumnsToFit(table);
		
		// automatically resize the columns whenever the data in the table changes
		table.getModel().addTableModelListener(new TableModelListener() {
		    public void tableChanged(TableModelEvent e) {
		        ColumnsAutoSizer.sizeColumnsToFit(table);
		    }
		});
		
		JScrollPane scrollPane2 = new JScrollPane(table);
		scrollPane2.setBackground(new Color(220, 220, 220));
		scrollPane2.setBounds(10, 61, 589, 448);
		getContentPane().add(scrollPane2);
		scrollPane2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JLabel lblCurrentList = new JLabel("Tasks List:");
		lblCurrentList.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrentList.setBackground(new Color(216, 191, 216));
		lblCurrentList.setForeground(new Color(153, 102, 153));
		lblCurrentList.setFont(new Font("Franklin Gothic Book", Font.BOLD, 21));
		lblCurrentList.setBounds(10, 26, 589, 24);
		getContentPane().add(lblCurrentList);
		
		//display_output = control.performAction("display");
		//mainArea.display(output);
		//Document doc = mainArea.getDocument();
		//PrintStream out = new PrintStream(new DocumentOutputStream(doc));
		//out.println("Hello World");

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
	
	
	class inputListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String input = textField.getText();
			textField.setText(null);
			System.out.println();
			String output = ft.runUserInput(input);
			mainArea.append("\n" + output);
			
		}
		
	}
}


