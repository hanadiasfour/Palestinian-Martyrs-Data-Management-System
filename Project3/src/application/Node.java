package application;

public class Node {
	Object element;
	Node next;

	Node(Object element){
		this.element=element;

	}

	public Object getElement() {
		return element;
	}

	public void setElement(Object element) {
		this.element = element;
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "Node [element=" + element + ", next=" + next + "]";
	}

	
}
