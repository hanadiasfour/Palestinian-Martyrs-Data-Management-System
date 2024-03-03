package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Location implements Comparable<Location> {

	private SimpleStringProperty place;// holds name of location
	private SingleLinkedList list = new SingleLinkedList();// SLL of martyrs in this location
	private ObservableList<Martyr> data = FXCollections.observableArrayList();// observable list of martyrs
	private ObservableList<Martyr> sortedData = data.sorted();// observable list of martyrs

	// constructor
	public Location(String place) {
		this.place = new SimpleStringProperty(place);
	}

	@Override // comparing locations by the first letter of the name
	public int compareTo(Location o) {
		if (Character.toLowerCase(this.place.get().charAt(0)) < Character.toLowerCase(o.place.get().charAt(0)))
			return -1;
		else if (Character.toLowerCase(this.place.get().charAt(0)) > Character.toLowerCase(o.place.get().charAt(0)))
			return 1;
		else
			return 0;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return place.get().equalsIgnoreCase(((Location) obj).place.get());
	}

	@Override
	public String toString() {
		return "Location [place=" + this.place.get() + "]";
	}

	// getters & setters

	public SingleLinkedList getList() {
		return list;
	}

	public void setList(SingleLinkedList list) {
		this.list = list;
	}

	public String getPlace() {
		return place.get();
	}

	public void setPlace(String place) {
		this.place.set(place);

	}

	public ObservableList<Martyr> getData() {
		return data;
	}

	public ObservableList<Martyr> getSortedData() {
		return sortedData;

	}

}
