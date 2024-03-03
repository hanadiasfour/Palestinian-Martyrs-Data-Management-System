package application;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Martyr implements Comparable<Martyr> {

	private String name;
	private byte age;
	private Date dateOfDeath;
	private char gender;
	private String date;
	private String status;
	private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");// formatter for the date

	public Martyr(String name, byte age, Date dateOfDeath, char gender, String status) throws IllegalArgumentException {

		// checking if values are correctly inputed
		if (name.isEmpty() || age < 0 || age > 150 || dateOfDeath.compareTo(new Date()) > 0) {
			throw new IllegalArgumentException();
		} else {
			// assigning values
			this.name = name;
			this.age = age;
			this.dateOfDeath = dateOfDeath;
			this.gender = Character.toUpperCase(gender);
			this.status = status;
			this.date = formatter.format(dateOfDeath);
			Statistics.count(this);
		}
	}

	// overriding comparable method
	@Override
	public int compareTo(Martyr o) {

		return -1 * this.dateOfDeath.compareTo(o.dateOfDeath);
	}

	@Override
	public boolean equals(Object obj) {

		return this.name.equalsIgnoreCase(((Martyr) obj).name);

	}

	@Override
	public String toString() {
		return "[ " + name + ", Status=" + status + ", Gender=" + gender + ", Age=" + age + " ]\n{  Date =  " + date
				+ "  }";
	}

	// setters & getters
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getAge() {
		return age;
	}

	public void setAge(byte age) {
		this.age = age;
	}

	public Date getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
