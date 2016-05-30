package my.gui.com;

/**
 * @author Tatu
 *
 */
public class Person {
	
	private String name;
	private int id;
	private double regularWorkingHours = 0;		//1 hour = $3.75
	private double eveningWorkingHours = 0;		//1 hour = $3.75 + $1.15 = $4.90 (18:00 - 06:00)
	
	//Other extra compensations are not included in hourly wage when calculating overtime compensations
	private double overtime1 = 0;				//First	2 Hours	> 8	Hours =	$3.75 + 25%	= $4.6875
	private double overtime2 = 0;				//Next 2 Hours = $3.75 + 50% = $5.625
	private double overtime3 = 0;				//After That = $3.75 + 100% = $7.50
	//Overtime	Compensations = Overtime Hours * Overtime Compensation Percent * Hourly Wage
	private String checkerDate;
	private double sameDayRegHours = 0;
	private double sameDayEveHours = 0;
	private double sameDayOt1 = 0;
	private double sameDayOt2 = 0;
	private double sameDayOt3 = 0;
	
	/**
	 * @param name
	 * @param id
	 */
	public Person(String name, int id) {
		super();
		this.name = name;
		this.id = id;
	}
	
	/**
	 * @return the name of the person
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to be set for the person
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id of the person
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to be set for the person
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the regularWorkingHours of the person
	 */
	public double getRegularWorkingHours() {
		return regularWorkingHours;
	}

	/**
	 * @param regularWorkingHours the regularWorkingHours to set for the person
	 */
	public void setRegularWorkingHours(double regularWorkingHours) {
		this.regularWorkingHours = regularWorkingHours;
	}

	/**
	 * @return the eveningWorkingHours (18:00 - 06:00)
	 */
	public double getEveningWorkingHours() {
		return eveningWorkingHours;
	}

	/**
	 * @param eveningWorkingHours the eveningWorkingHours to set for the person
	 */
	public void setEveningWorkingHours(double eveningWorkingHours) {
		this.eveningWorkingHours = eveningWorkingHours;
	}

	/**
	 * @return the overtime1
	 */
	public double getOvertime1() {
		return overtime1;
	}

	/**
	 * @param overtime1 the overtime1 to set
	 */
	public void setOvertime1(double overtime1) {
		this.overtime1 = overtime1;
	}

	/**
	 * @return the overtime2
	 */
	public double getOvertime2() {
		return overtime2;
	}

	/**
	 * @param overtime2 the overtime2 to set
	 */
	public void setOvertime2(double overtime2) {
		this.overtime2 = overtime2;
	}

	/**
	 * @return the overtime3
	 */
	public double getOvertime3() {
		return overtime3;
	}

	/**
	 * @param overtime3 the overtime3 to set
	 */
	public void setOvertime3(double overtime3) {
		this.overtime3 = overtime3;
	}

	/**
	 * @return the checkerDate
	 */
	public String getCheckerDate() {
		return checkerDate;
	}

	/**
	 * @param checkerDate the checkerDate to set
	 */
	public void setCheckerDate(String checkerDate) {
		this.checkerDate = checkerDate;
	}
	
	/**
	 * @return the sameDayRegHours
	 */
	public double getSameDayRegHours() {
		return sameDayRegHours;
	}

	/**
	 * @param sameDayRegHours the sameDayRegHours to set
	 */
	public void setSameDayRegHours(double sameDayRegHours) {
		this.sameDayRegHours = sameDayRegHours;
	}

	/**
	 * @return the sameDayEveHours
	 */
	public double getSameDayEveHours() {
		return sameDayEveHours;
	}

	/**
	 * @param sameDayEveHours the sameDayEveHours to set
	 */
	public void setSameDayEveHours(double sameDayEveHours) {
		this.sameDayEveHours = sameDayEveHours;
	}

	/**
	 * @return the sameDayOt1
	 */
	public double getSameDayOt1() {
		return sameDayOt1;
	}

	/**
	 * @param sameDayOt1 the sameDayOt1 to set
	 */
	public void setSameDayOt1(double sameDayOt1) {
		this.sameDayOt1 = sameDayOt1;
	}

	/**
	 * @return the sameDayOt2
	 */
	public double getSameDayOt2() {
		return sameDayOt2;
	}

	/**
	 * @param sameDayOt2 the sameDayOt2 to set
	 */
	public void setSameDayOt2(double sameDayOt2) {
		this.sameDayOt2 = sameDayOt2;
	}

	/**
	 * @return the sameDayOt3
	 */
	public double getSameDayOt3() {
		return sameDayOt3;
	}

	/**
	 * @param sameDayOt3 the sameDayOt3 to set
	 */
	public void setSameDayOt3(double sameDayOt3) {
		this.sameDayOt3 = sameDayOt3;
	}
	
	
	
	
	
}
