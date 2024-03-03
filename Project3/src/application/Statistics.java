package application;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Statistics {

	private int size=0;
	private int  height1=0;
	private int  height2=0;
	private String mostDate;
	private int currMostDeaths = 0;

	public Statistics() {

	}

	public Statistics(Location l) {
		if (l != null)
			doStat(l);

	}

	public void doStat(Location l) {
		size = l.getAVL_Name().getSize();
		height1 = l.getAVL_Name().getAVLHeight();
		height2 = l.getAVL_Dates().getAVLHeight();

	}

	public void hasMostDeaths(SLLStack s) {

		if (s != null && s.size() > currMostDeaths) {
			currMostDeaths = s.size();
			mostDate = s.peek().getDate();

		}

	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getHeight1() {
		return height1;
	}

	public void setHeight1(int height1) {
		this.height1 = height1;
	}

	public int getHeight2() {
		return height2;
	}

	public void setHeight2(int height2) {
		this.height2 = height2;
	}

	public String getMostDate() {
		return mostDate;
	}

	public void setMostDate(String mostDate) {
		this.mostDate = mostDate;
	}
	
	

}
