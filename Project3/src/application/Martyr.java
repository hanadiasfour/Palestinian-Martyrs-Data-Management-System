package application;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Martyr implements Comparable<Martyr>, Cloneable {

	private String name;
	private byte age;
	private Date dateOfDeath;
	private char gender;
	private String date;
	private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");// formatter for the date

	public Martyr(String name, byte age, Date dateOfDeath, char gender) throws IllegalArgumentException {

		// checking if values are correctly inputed
//		|| dateOfDeath.compareTo(new Date()) > 0
		if (name.isEmpty() || age < 0 || age > 150) {
			throw new IllegalArgumentException();
		} else {
			// assigning values
			this.name = name;
			this.age = age;
			this.dateOfDeath = dateOfDeath;
			this.gender = Character.toUpperCase(gender);
			this.date = formatter.format(dateOfDeath);
		}
	}

	// overriding comparable method
	@Override
	public int compareTo(Martyr o) {

		if (this.equals(o))
			return 0;

		int i = 0;

		while (i < name.length() && i < o.getName().length()) {

			char c1 = name.charAt(i);
			char c2 = o.getName().charAt(i);

			if (Character.toLowerCase(c1) != Character.toLowerCase(c2)) {

				return Character.compare(Character.toLowerCase(c1), Character.toLowerCase(c2));
			}

			i++;
		}

		return Integer.compare(name.length(), o.getName().length());

	}

	@Override
	public boolean equals(Object obj) {

		return this.name.equalsIgnoreCase(((Martyr) obj).name);

	}

	@Override
	public String toString() {
		return "( " + name + ", Gender=" + gender + ", Age=" + age + ") {  Date =  " + date + "  }";
	}

	// setters & getters

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
		date = formatter.format(dateOfDeath);

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

	@Override
	protected Object clone() {
		// TODO Auto-generated method stub
		try {
			return super.clone();

		} catch (CloneNotSupportedException e) {
			// TODO: handle exception
		}

		System.out.println("oh no!!!");
		return null;
	}

}
