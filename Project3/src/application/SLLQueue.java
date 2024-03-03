package application;

import java.util.NoSuchElementException;

public class SLLQueue {

	private QNode front;
	private QNode rear;
	private int size;

	public static class QNode {
		private Object data;
		private QNode next;

		public QNode(Object data) {
			this.data = data;
			next = null;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}

		public QNode getNext() {
			return next;
		}

		public void setNext(QNode next) {
			this.next = next;
		}

	}

	public SLLQueue() {
		front = null;
		rear = null;
		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public void enqueue(Object item) {
		QNode newNode = new QNode(item);

		if (isEmpty()) {
			front = newNode;
			rear = newNode;
		} else {
			rear.next = newNode;
			rear = newNode;
		}

		size++;
	}

	public Object dequeue() {
		if (isEmpty()) {
			throw new NoSuchElementException("Queue is empty");
		}

		Object item = front.getData();
		front = front.getNext();
		size--;

		if (isEmpty())
			rear = null;

		return item;
	}

	public Object peek() {
		if (isEmpty())
			throw new NoSuchElementException("Queue is empty");

		return front.getData();
	}

	public void clear() {
		front = null;
		rear = null;
		size = 0;
	}

	@Override
	public String toString() {

		String count = "";
		QNode curr = front;
		while (curr != null) {
			count += ((Martyr)curr.getData()).toString() + "\n";
			curr = curr.getNext();
		}
		return count;

	}

}
