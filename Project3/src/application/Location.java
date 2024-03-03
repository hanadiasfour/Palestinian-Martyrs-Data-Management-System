package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Location implements Comparable<Location> {

	private SimpleStringProperty place;// holds name of location
	private SLLQueue list = new SLLQueue();
	private AVL AVL_Name = new AVL();
	private AVL AVL_Dates = new AVL();
	private Statistics stat = new Statistics();
	private ObservableList<Statistics> calculations = FXCollections.observableArrayList(stat);

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
		if (obj == null)
			return false;

		return place.get().equalsIgnoreCase(((Location) obj).place.get());
	}

	@Override
	public String toString() {
		return "Location [place=" + this.place.get() + "]";
	}

	public ObservableList<Martyr> getAVL1Items() {
		ObservableList<Martyr> elements = FXCollections.observableArrayList();
		if (AVL_Name.getRoot() == null) {
			return elements;
		}

		SLLQueue queue = new SLLQueue();
		queue.enqueue(AVL_Name.getRoot());

		while (!queue.isEmpty()) {
			TNode current = (TNode) queue.dequeue();
			elements.add((Martyr) current.getData());

			if (current.getLeft() != null) {
				queue.enqueue(current.getLeft());
			}
			if (current.getRight() != null) {
				queue.enqueue(current.getRight());
			}

		}

		return elements;
	}

	public ObservableList<Martyr> getAVL2Items() {
		ObservableList<Martyr> items = FXCollections.observableArrayList();

		if (AVL_Dates.getRoot() != null)
			traverseBackward(AVL_Dates.getRoot(), items);

		return items;
	}

	private void traverseBackward(TNode node, ObservableList<Martyr> items) {
		if (node != null) {
			traverseBackward(node.getRight(), items);

			// Process the current stack
			addMartyrsFromStack((SLLStack) ((DateStack) node.getData()).getMartyrStack(), items);
			stat.hasMostDeaths((SLLStack) ((DateStack) node.getData()).getMartyrStack());

			traverseBackward(node.getLeft(), items);
		}
	}

	private void addMartyrsFromStack(SLLStack s, ObservableList<Martyr> items) {

		SLLStack dummy = new SLLStack();

		while (!s.isEmpty())
			dummy.push(s.pop());

		while (!dummy.isEmpty()) {
			items.add(dummy.peek());
			s.push(dummy.pop());

		}

	}

	// getters & setters

	public SLLQueue getList() {
		return list;
	}

	public void setList(SLLQueue list) {
		this.list = list;
	}

	public String getPlace() {
		return place.get();
	}

	public void setPlace(String place) {
		this.place.set(place);

	}

	public AVL getAVL_Name() {
		return AVL_Name;
	}

	public void setAVL_Name(AVL aVL_Name) {
		AVL_Name = aVL_Name;
	}

	public AVL getAVL_Dates() {
		return AVL_Dates;
	}

	public void setAVL_Dates(AVL aVL_Dates) {
		AVL_Dates = aVL_Dates;
	}

	public ObservableList<Statistics> getCalculations() {
		return calculations;
	}

	public Statistics getStat() {
		return stat;
	}

//	public ObservableList<Martyr> getSortedByName() {
//	ObservableList<Martyr> items = FXCollections.observableArrayList();
//
//	appendtoList(items, AVL_Name.getRoot());
//
//	return items;
//}
//
//private void appendtoList(ObservableList<Martyr> items, TNode node) {
//
//	if (node.getLeft() != null)
//		appendtoList(items, node.getLeft());
//
//	items.add((Martyr) node.getData());
//
//	if (node.getRight() != null)
//		appendtoList(items, node.getRight());
//
//}

}
