package my.gui.com;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JScrollPane;

/**
 * @author Tatu
 *
 */
public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtFileName;
	static JTable tableResults;
	private Calculations calc;
	private ArrayList<Person> people = new ArrayList<Person>();
	//decimal format with 2 decimal points, used for rounding currency values to 2 decimals
	private static DecimalFormat df2 = new DecimalFormat("#0.00");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setForeground(Color.LIGHT_GRAY);
		setResizable(false);
		setTitle("Monthly CSV Wage Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 677, 307);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JLabel lblCsvfile = new JLabel("CSV-File:");
		lblCsvfile.setBounds(10, 11, 46, 14);
		contentPane.add(lblCsvfile);
		txtFileName = new JTextField();
		txtFileName.setText("HourList201403.csv");
		txtFileName.setEditable(false);
		txtFileName.setEnabled(false);
		txtFileName.setBounds(67, 8, 594, 20);
		contentPane.add(txtFileName);
		txtFileName.setColumns(10);
		
		JButton btnCalculateTotalSalaries = new JButton("Calculate Total salaries");
		//Calculate Total Salaries button mouseListener
		btnCalculateTotalSalaries.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				calc = new Calculations();
				people = calc.readCSVFile();
				//show the final results in the JTable tableResults
				showResults(people);
			}

		});
		btnCalculateTotalSalaries.setBounds(10, 240, 317, 23);
		contentPane.add(btnCalculateTotalSalaries);
		
		JButton btnClearAll = new JButton("Clear Table");
		//Clear All button mouseListener
		btnClearAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//clear the tableResults
				DefaultTableModel tm=new DefaultTableModel();
				tableResults.setModel(tm);

			}
		});
		btnClearAll.setBounds(337, 240, 324, 23);
		contentPane.add(btnClearAll);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 651, 193);
		contentPane.add(scrollPane);
		tableResults = new JTable();
		tableResults.setFillsViewportHeight(true);
		tableResults.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollPane.setViewportView(tableResults);	
	}
	
	//Shows the currency equivalents for the completed hours for each user:
	//Scott Scala hours: regular= 146, evening= 12, overtime1= 7.5, overtime2= 1.75, overtime3= 0
	//Janet Java hours: regular= 144, evening= 12, overtime1= 10, overtime2= 4, overtime3= 3
	//Larry Lolcode hours: regular= 87.5, evening= 5, overtime1= 4, overtime2= 1, overtime3= 0
	//shows the results of the people present in the csv-file
	private void showResults(ArrayList<Person> people) {
       tableResults.setModel(buildTableModel(people));	
	}
	
	//creates and builds a default table model with the table headers and data for each person.
	private static DefaultTableModel buildTableModel(ArrayList<Person> people) {
		Vector<String> columnNames = new Vector<String>();
		//create the names for the columns in the table
		int columnCount = 8;
		columnNames.add("ID");
		columnNames.add("Name");
		columnNames.add("Regular($)");
		columnNames.add("Evening($)");
		columnNames.add("Overtime1($)");
		columnNames.add("Overtime2($)");
		columnNames.add("Overtime3($)");
		columnNames.add("Total($)");
		
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		for(Person p: people){
			Vector<Object> vector = new Vector<Object>();
			//round double values to 2 decimal places
			double regWorkDollars = Math.round(p.getRegularWorkingHours() * 3.75 * 1e2)/1e2;
			double eveWorkDollars = Math.round(p.getEveningWorkingHours() * 4.90 * 1e2)/1e2;
			double ot1Dollars = Math.round(p.getOvertime1() * 4.6875 * 1e2)/1e2;
			double ot2Dollars = Math.round(p.getOvertime2() * 5.625 * 1e2)/1e2;
			double ot3Dollars = Math.round(p.getOvertime3() * 7.50 * 1e2)/1e2;
			double total = Math.round((regWorkDollars + eveWorkDollars + ot1Dollars + ot2Dollars + ot3Dollars) *1e2) /1e2;
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				vector.add(p.getId());
				vector.add(p.getName());
				//add the calculated currency values formatted with df2 decimal format to show dollars and cents
				vector.add(df2.format(regWorkDollars));
				vector.add(df2.format(eveWorkDollars));
				vector.add(df2.format(ot1Dollars));
				vector.add(df2.format(ot2Dollars));
				vector.add(df2.format(ot3Dollars));
				vector.add(df2.format(total));		
			}
			data.add(vector);
		}
		//no need to be able to edit the cells in the table
		DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};				
		return  tableModel;
	}
}
