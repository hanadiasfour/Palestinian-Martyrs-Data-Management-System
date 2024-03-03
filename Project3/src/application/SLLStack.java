package application;

public class SLLStack {

	private Node first;

	public void push(Martyr data) {
		Node temp = new Node(data);
		temp.setNext(first);
		first = temp;
	}

	public Martyr pop() {
		Node temp = first;
		if (first != null) {
			first = first.getNext();
			temp.setNext(null);
		}
		return (Martyr) temp.getElement();
	}

	public Martyr peek() {
		return (Martyr) first.getElement();
	}

	public int size() {
		int count = 0;
		Node curr = first;
		while (curr != null) {
			count++;
			curr = curr.getNext();
		}
		return count;
	}

	public boolean isEmpty() {
		return (first == null);
	}

	public void clear() {
		first = null;
	}

	@Override
	public String toString() {

		String st = "Top-->";
		Node curr = first;
		while (curr != null) {
			st += "[" + curr.getElement().toString() + "]-->";
			curr = curr.getNext();
		}

		return st + "Null";

	}

}
