package application;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Statistics {

	// General statistics
	// number of martyrs M/F
	private static int COUNT_F = 0;
	private static int COUNT_M = 0;
	// number of Single/Married martyrs
	private static int COUNT_SINGLE = 0;
	private static int COUNT_MARRIED = 0;
	// number of martyrs by age
	private static int COUNT_CHILDREN = 0;
	private static int COUNT_ADULT = 0;
	private static int COUNT_SENIOR = 0;

	////////////////////////////////////////////

	// specific statistics
	private String location;
	private int childrenCount; // age 0-17
	private int adultCount;// age 18-64
	private int seniorCount;// age>=65
	private int femaleCount;
	private int maleCount;
	private float ageCount;
	private int avgAge;
	private String maxDateString;
	private int maxCount;
	private int marriedCount;
	private int singleCount;

	public Statistics() {
	}

	public Statistics(Location l) {
		calculateStat(l);
	}

	public static void count(Martyr m) {

		// counting by gender
		if (m.getGender() == 'F')
			COUNT_F++;
		else
			COUNT_M++;

		// counting single and married martyrs
		if (m.getStatus().equalsIgnoreCase("single"))
			COUNT_SINGLE++;
		else
			COUNT_MARRIED++;

		// counting ages
		if (m.getAge() >= 0 && m.getAge() <= 17)
			COUNT_CHILDREN++;

		else if (m.getAge() >= 18 && m.getAge() <= 64)
			COUNT_ADULT++;

		else
			COUNT_SENIOR++;

	}

	public void calculateStat(Location l) {// generates statistics age and gender O(n)

		// reseting values
		childrenCount = 0;
		adultCount = 0;
		seniorCount = 0;
		femaleCount = 0;
		maleCount = 0;
		ageCount = 0;
		avgAge = 0;
		location = l.getPlace();
		singleCount = 0;
		marriedCount = 0;

		SNode currentMartyr = l.getList().getFirstNode();

		// looping until end of martyr list in location l
		for (int i = 1; i <= l.getList().getSize(); i++) {
			// counting by age
			if (((Martyr) currentMartyr.getElement()).getAge() >= 0
					&& ((Martyr) currentMartyr.getElement()).getAge() <= 17) {
				childrenCount++;

			} else if (((Martyr) currentMartyr.getElement()).getAge() >= 18
					&& ((Martyr) currentMartyr.getElement()).getAge() <= 64) {
				adultCount++;

			} else
				seniorCount++;

			// counting by gender
			if (((Martyr) currentMartyr.getElement()).getGender() == 'F')
				femaleCount++;
			else
				maleCount++;

			// counting status
			if (((Martyr) currentMartyr.getElement()).getStatus().compareToIgnoreCase("single") == 0)
				singleCount++;
			else
				marriedCount++;

			ageCount += ((Martyr) currentMartyr.getElement()).getAge();
			currentMartyr = currentMartyr.getNext();// next martyr

		}

		// calculating average age
		avgAge = Math.round(ageCount / (float) l.getList().getSize());

	}

	// calculates the date with the most martyrs in a location O(n)
	public void calculateMaxMartyrDate(Location l) {

		Date maxDate = null;
		maxCount = 0;
		int currentCount = 0;
		Date currentDate = null;
		maxDateString = "";

		SNode current = l.getList().getFirstNode();

		// looping until end of martyr list in location l
		for (int i = 1; i <= l.getList().getSize(); i++) {
			Date date = ((Martyr) current.getElement()).getDateOfDeath();

			// when counting has started or when the date changes
			if (currentDate == null || !date.equals(currentDate)) {
				// checking if the previous date had most number repetition in the list
				// if so, we set the maxCount and maxDate to that date
				if (currentCount > maxCount) {
					maxCount = currentCount;
					maxDate = currentDate;

				}
				// continuing to the next new date that appeared
				// reseting counter
				currentDate = date;
				currentCount = 1;
			} else {
				// incrementing the count as long as the date is the same
				currentCount++;
			}
			current = current.getNext();
		}

		if (maxDate != null)// assigning the date value to a string using formatter
			maxDateString = formatter.format(maxDate);

	}

	// getters & setters

	public static int getCOUNT_F() {
		return COUNT_F;
	}

	public static void setCOUNT_F(int cOUNT_F) {
		COUNT_F = cOUNT_F;
	}

	public static int getCOUNT_M() {
		return COUNT_M;
	}

	public static void setCOUNT_M(int cOUNT_M) {
		COUNT_M = cOUNT_M;
	}

	public static int getCOUNT_SINGLE() {
		return COUNT_SINGLE;
	}

	public static void setCOUNT_SINGLE(int cOUNT_SINGLE) {
		COUNT_SINGLE = cOUNT_SINGLE;
	}

	public static int getCOUNT_MARRIED() {
		return COUNT_MARRIED;
	}

	public static void setCOUNT_MARRIED(int cOUNT_MARRIED) {
		COUNT_MARRIED = cOUNT_MARRIED;
	}

	public int getChildrenCount() {
		return childrenCount;
	}

	public void setChildrenCount(int childrenCount) {
		this.childrenCount = childrenCount;
	}

	public int getAdultCount() {
		return adultCount;
	}

	public void setAdultCount(int adultCount) {
		this.adultCount = adultCount;
	}

	public int getSeniorCount() {
		return seniorCount;
	}

	public void setSeniorCount(int seniorCount) {
		this.seniorCount = seniorCount;
	}

	public int getFemaleCount() {
		return femaleCount;
	}

	public void setFemaleCount(int femaleCount) {
		this.femaleCount = femaleCount;
	}

	public int getMaleCount() {
		return maleCount;
	}

	public void setMaleCount(int maleCount) {
		this.maleCount = maleCount;
	}

	public float getAgeCount() {
		return ageCount;
	}

	public void setAgeCount(float ageCount) {
		this.ageCount = ageCount;
	}

	public int getAvgAge() {
		return avgAge;
	}

	public void setAvgAge(int avgAge) {
		this.avgAge = avgAge;
	}

	public String getMaxDateString() {
		return maxDateString;
	}

	public void setMaxDateString(String maxDate) {
		this.maxDateString = maxDate;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public SimpleDateFormat getFormatter() {
		return formatter;
	}

	public void setFormatter(SimpleDateFormat formatter) {
		this.formatter = formatter;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getMarriedCount() {
		return marriedCount;
	}

	public void setMarriedCount(int marriedCount) {
		this.marriedCount = marriedCount;
	}

	public int getSingleCount() {
		return singleCount;
	}

	public void setSingleCount(int singleCount) {
		this.singleCount = singleCount;
	}

	public static int getCOUNT_CHILDREN() {
		return COUNT_CHILDREN;
	}

	public static void setCOUNT_CHILDREN(int cOUNT_CHILDREN) {
		COUNT_CHILDREN = cOUNT_CHILDREN;
	}

	public static int getCOUNT_ADULT() {
		return COUNT_ADULT;
	}

	public static void setCOUNT_ADULT(int cOUNT_ADULT) {
		COUNT_ADULT = cOUNT_ADULT;
	}

	public static int getCOUNT_SENIOR() {
		return COUNT_SENIOR;
	}

	public static void setCOUNT_SENIOR(int cOUNT_SENIOR) {
		COUNT_SENIOR = cOUNT_SENIOR;
	}

	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

}
