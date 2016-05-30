package my.gui.com;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

/**
 * @author Tatu
 *
 */
public class Calculations {
	
	
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	private Date eveningWorkStartTime;
	private Date eveningWorkEndTime;
	private ArrayList<Person> persons = new ArrayList<Person>();
	private List<Integer> ids = new ArrayList<Integer>();
	//variables used while looping through csv-file
	private int currentId;
	private String currentName;
	private String theDate;
	//helper variables, reset after person is changed
	private double currentRegWorkHours = 0;
	private double currentEveWorkHours = 0;
	private double currentOt1 = 0;
	private double currentOt2 = 0;
	private double currentOt3 = 0;
	
	/**
	 * reads the HourList201403.csv-file
	 * @return the people read from the csv-file, and their regular, evening, overtime1, overtime2, and overtime3 hours done.
	 */
	public ArrayList<Person> readCSVFile(){
	   
		//Create an input stream Object from the csv-file
		InputStream is = Calculations.class.getResourceAsStream("/resources/HourList201403.csv");
		//initialize the readers
		BufferedReader br = null;
		CSVReader reader = null;
		//initialize start and end times
		Date startingHour = null;
		Date endingHour = null;
		try {
			//set the FileReader object with the csv-file in it as parameter
			br = new BufferedReader(new InputStreamReader(is));
			//set the CSVReader with parameters BufferedReader, default separator and quote char, because
			//csv-file has default needs, and start reading from line 1 (no headings needed)
		    reader = new CSVReader(br ,CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1);
		    String[] nextLine;
		    while ((nextLine = reader.readNext()) != null) {
			    if (nextLine != null) {
		        	//get the column values of the next line into variables
		        	currentName = nextLine[0];
		        	//check that nextLine[1] matches numeric value before parse for better error checking
		        	if(nextLine[1].matches("\\d+")){
		        		 currentId = Integer.parseInt(nextLine[1]);
		        	 }
		        	 else {
		        		 JOptionPane.showMessageDialog(MainWindow.tableResults, "An ID of a person in the CSV-file is not in numeric form. Please check your IDs in the file.","Check Persons ID", JOptionPane.ERROR_MESSAGE);
		        		 break;
		        	 }
		        	 
		        	 theDate = nextLine[2];
		        	 
		        	//check that nextLine[3] and nextLine[4] match correct time format for better error checking
		        	 if(nextLine[3].matches("\\d{1,2}\\:\\d{2}") && nextLine[3].matches("\\d{1,2}\\:\\d{2}")){
		        		 startingHour = timeFormat.parse(nextLine[3]);
		        		 endingHour = timeFormat.parse(nextLine[4]);
		        	 }
		        	 else{
		        		 JOptionPane.showMessageDialog(MainWindow.tableResults, "An ending or starting time of a person in the CSV-file is not in correct form. Please check your IDs in the file.","Check Persons ID", JOptionPane.ERROR_MESSAGE);
		        		 break; 
		        	 }
		        	 //find out the difference in milliseconds
		        	 long difference = endingHour.getTime() - startingHour.getTime();
		        	//24 hours in milliseconds
		        	 long fullDay = 86400000;	
		        	 //if the ending time went to the next day, negative difference is converted to positive by using full day
		        	 if(difference < 0 ){
		        		 difference += fullDay;
		        	 } 
		            //if the persons is empty or null, add the first person into the list
		            if(persons == null || persons.isEmpty()){
		            	persons.add(new Person (currentName, currentId));
		            	ids.add(currentId);
		            	//set the hours for the first person read from the file
		            	setHours(persons.get(persons.size()-1), startingHour, endingHour, difference, theDate);	
		            }
		            //else there is already people in the persons, add their hours to their information
		            else{
		            	//iterate through the people in persons and add hours to the right person
		            	for(Person p : persons){
			            	//if the person is already in the array, do not add them again (for this file, will result in 3 people being saved)
			            	if (ids.isEmpty() || !ids.contains(currentId)){
			            		persons.add(new Person (currentName, currentId));
			            		ids.add(currentId);
			            		//set the hours for the person that was just added, then exit the loop
			            		setHours(persons.get(persons.size()-1), startingHour, endingHour, difference, theDate);
			            		break;		       
			            	}
			            	//if the person is already in the array, need to add the hours done to the persons information
			            	else if (ids.contains(currentId) && p.getId() == currentId){
			            		//add new hours to the person iterated with matching Id, then exit the loop
				            	setHours(p, startingHour, endingHour, difference, theDate);	
				            	break;
			            	}	
			            }
		            }         
			    }  
		    }
		    //close the readers
		    reader.close();
		    br.close();
		} 
		catch (FileNotFoundException fnfe) {
			JOptionPane.showMessageDialog(MainWindow.tableResults, "The file you are trying to read cannot be found in the specified location.","Check File location", JOptionPane.ERROR_MESSAGE);
			fnfe.printStackTrace();
		} 
		catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(MainWindow.tableResults, "A number was not formatted correctly. Please contact support for assistance","Number Format exception", JOptionPane.ERROR_MESSAGE);
			nfe.printStackTrace();
		} 
		catch (IOException ioe) {
			JOptionPane.showMessageDialog(MainWindow.tableResults, "An input/output exception has occurred. Please contact support for assistance", "Input/Output exception", JOptionPane.ERROR_MESSAGE);
			ioe.printStackTrace();
		} 
		catch (ParseException pe) {
			JOptionPane.showMessageDialog(MainWindow.tableResults, "While the program was trying to parse a variable into a different type, something went wrong. Please contact support for assistance","Parse Exception", JOptionPane.ERROR_MESSAGE);
			pe.printStackTrace();
		}
		return persons;
	}

	//This sets the regular working hours, evening working hours and overtime for the person being processed.
	private void setHours(Person person, Date startingHour, Date endingHour, long difference, 
			String theDate) throws ParseException {
		eveningWorkStartTime = timeFormat.parse("18:00");
		eveningWorkEndTime = timeFormat.parse("06:00");
		int overtimeType;
		long diffMinutes;
		long diffHours;
		//set the minutes and hours into their own variables based on the difference (milliseconds)
		diffMinutes = difference / (60 * 1000) % 60;
		diffHours = difference / (60 * 60 * 1000) % 24;
		//is the person working again on the same day or not
		boolean sameDayWork = isSameDay(person.getCheckerDate(),theDate);
		// creates calendar which will help with time manipulation
		Calendar cal = Calendar.getInstance();
		
		//convert the total hours and minutes to match double values
		double hr = (double) diffHours;
        double min = (double) diffMinutes;
        //make the minutes into double value with 60 = 1.00
        double dailyTotal = hr + min / 60d;
        //person is not working on the same day as last row read
		if(sameDayWork == false){
			//set checkers for future reference
			person.setCheckerDate(theDate);
		}
		//more than 1 row of working hours for a single day
		else if(sameDayWork){
			dailyTotal = dailyTotal + person.getSameDayRegHours() + person.getSameDayEveHours() + person.getSameDayOt1() + person.getSameDayOt2()+ person.getSameDayOt3();
			// remove the last rows hours from the persons hours, so as to not read them twice
			person.setRegularWorkingHours(person.getRegularWorkingHours() - person.getSameDayRegHours());
			person.setEveningWorkingHours(person.getEveningWorkingHours() - person.getSameDayEveHours());
			person.setOvertime1(person.getOvertime1() - person.getSameDayOt1());
			person.setOvertime2(person.getOvertime2() - person.getSameDayOt2());
			person.setOvertime3(person.getOvertime3() - person.getSameDayOt3());
		}
		overtimeType = checkOvertimeType(dailyTotal);
	    //set the current overtime hours according to overtime type
        setOvertimes(overtimeType, dailyTotal);
        //change the ending time according to overtime done, and add overtime to persons hours
        //person has done more than 8 hours in one sitting, resulting in overtime
        if(overtimeType > 0){
        	person.setOvertime1(person.getOvertime1() + currentOt1);
        	person.setOvertime2(person.getOvertime2() + currentOt2);
        	person.setOvertime3(person.getOvertime3() + currentOt3);
        	//remove the already counted hours from the daily total
			dailyTotal = dailyTotal - currentOt1 - currentOt2 - currentOt3;
			
        }
        //quick counter that will only only allow for one action to happen within the while-loop
		int counter = 1;
		while(dailyTotal > 0){	
			//only add the previously removed hours back, while removing them from the dailyTotal
			if(sameDayWork && counter == 1){
				person.setRegularWorkingHours(person.getRegularWorkingHours() + person.getSameDayRegHours());
				person.setEveningWorkingHours(person.getEveningWorkingHours() + person.getSameDayEveHours());
				dailyTotal = dailyTotal - person.getSameDayRegHours() - person.getSameDayEveHours();
				counter++;
			}
			//regular working hours between evening work start- and end-times
			else if(startingHour.before(eveningWorkStartTime) && (startingHour.after(eveningWorkEndTime) || startingHour.equals(eveningWorkEndTime))&& !startingHour.equals(endingHour) ){
				startingHour = manipulateStarting(cal, startingHour);
			    //add 15 minutes (0.25) into the persons regular working hours
			    person.setRegularWorkingHours(person.getRegularWorkingHours() + 0.25);
			    //set the amount also to the helper variable currentRegWorkHours for possible future rows for same person, same day
			    currentRegWorkHours += 0.25;
			    //subtracts the added amount from the total
			    dailyTotal = dailyTotal - 0.25;
			}
			//else if evening working hours
			else if(startingHour.equals(eveningWorkStartTime) || startingHour.after(eveningWorkStartTime) || startingHour.before(eveningWorkEndTime) && !startingHour.equals(endingHour)){
				startingHour = manipulateStarting(cal, startingHour);
			    //add 15 minutes (0.25) into the persons evening working hours
			    person.setEveningWorkingHours(person.getEveningWorkingHours() + 0.25);
			    //set the amount also to the helper variable for possible future rows for same person, same day
			    currentEveWorkHours += 0.25;
			    //subtracts the added amount from the total
			    dailyTotal = dailyTotal - 0.25;
			}		
			//something unusual has happened and hours are still left over
			else {
				JOptionPane.showMessageDialog(MainWindow.tableResults, "An unexpected error has happened. Please contact support for assistance \n We apologise for any inconvenience caused.","Unexpected error", JOptionPane.ERROR_MESSAGE);
				break;
			}	
		}
		//set same day variables for future reference on new rows being read having same date and same person
		if(sameDayWork == false){
			person.setSameDayRegHours(currentRegWorkHours);
			person.setSameDayEveHours(currentEveWorkHours);
			person.setSameDayOt1(currentOt1);
			person.setSameDayOt2(currentOt2);
			person.setSameDayOt3(currentOt3);
		}
		else{
			person.setSameDayRegHours(person.getSameDayRegHours() + currentRegWorkHours);
			person.setSameDayEveHours(person.getSameDayEveHours() + currentEveWorkHours);
			person.setSameDayOt1(person.getSameDayOt1() + currentOt1);
			person.setSameDayOt2(person.getSameDayOt2() + currentOt2);
			person.setSameDayOt3(person.getSameDayOt3() + currentOt3);
		}
		//reset the helper variables for each iteration
		resetHelperVariables();        
	}

	/**
	 * @param overtimeType 1= 2 hours or less, 2= more than 2 hours, equal to or less than 4 hours, 3= more than 4 hours
	 * @param total the total number of hours worked in the day
	 */
	private void setOvertimes(int overtimeType, double total) {
		if(overtimeType == 1){
			currentOt1 = total - 8.00;
			currentOt2 = 0.00;
        	currentOt3 = 0.00;
		}
        else if(overtimeType == 2){
        	currentOt1 = 2.00;
        	currentOt2 = total - 10.00;
        	currentOt3 = 0.00;
        	
        }
        else if(overtimeType == 3){
        	currentOt1 = 2.00;
        	currentOt2 = 2.00;
        	currentOt3 = total - 12.00;
        }
        //no overtime done
        else{
        	currentOt1 = 0.00;
			currentOt2 = 0.00;
        	currentOt3 = 0.00;
        }	
	}

	/**
	 * @param total number of hours worked
	 * @return as int the type of overtime, 0 =no overtime, 1 = more than 8 hours but less than 10, 2 = more than 10 hours but less than 12, 3 = more than 12 hours
	 */
	private int checkOvertimeType(double total) {
		//less than 8 hours, no overtime
		if(total <= 8.00){
			return 0;
		}
		//more than 8 hours but less than 10 hours, overtime1
		else if(total > 8.00 && total <= 10.00){
			return 1;
		}
		//more than 10 hours but less than 12 hours, overtime2
		else if(total > 10.00 && total <= 12.00){
			return 2;
		}
		//more than 12 hours, overtime3
		else return 3;
	}

	
	/**
	 * @param cal
	 * @param startingHour
	 * @return the manipulated starting time with 15 minutes added to it.
	 */
	private Date manipulateStarting(Calendar cal, Date startingHour) {
		// sets calendar time
		cal.setTime(startingHour);
		// adds 15 minutes
	    cal.add(Calendar.MINUTE, 15);
	    // returns new date object, 15 minutes in the future
	    startingHour = cal.getTime(); 
		return startingHour;
	}
	
	/**
	 * @param checkerDate
	 * @param theDate
	 * @return true if the checkerDate and theDate are both equal, false otherwise.
	 */
	private boolean isSameDay(String checkerDate, String theDate) {
		if(checkerDate == null || checkerDate.equals("")){
			return false;
		}
		else if(checkerDate.equals(theDate)){
			return true;
		}
		else return false;
	}  
	
	/**
	 * resets the helper variables to 0 values for the next person being processed
	 */
	private void resetHelperVariables() {
		currentOt1 = 0.00;
		currentOt2 = 0.00; 
		currentOt3 = 0.00;
		currentRegWorkHours = 0.00;
		currentEveWorkHours = 0.00;	
	}
}

