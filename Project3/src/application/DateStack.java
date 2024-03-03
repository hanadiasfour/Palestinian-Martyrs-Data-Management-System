package application;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateStack implements Comparable<DateStack> {

	private Date date;
	private SLLStack martyrStack = new SLLStack();
	private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");// formatter for the date

	public DateStack(Date date) {
		this.date = date;

	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public SLLStack getMartyrStack() {
		return martyrStack;
	}

	@Override
	public int compareTo(DateStack o) {
		return date.compareTo(o.date);

	}

	@Override
	public String toString() {
		return "DateStack [date=" + formatter.format(date) + ", martyrStack=" + martyrStack.toString() + "]\n";
	}
	
	

}
