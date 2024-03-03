package application;

public class TNode {

	// AllOverHere!!

	private Object data;
	private TNode left, right;
	private int height;

	TNode(Object data) {

		this.data = data;

	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public TNode getLeft() {
		return left;
	}

	public void setLeft(TNode left) {
		this.left = left;
	}

	public TNode getRight() {
		return right;
	}

	public void setRight(TNode right) {
		this.right = right;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "[data=" + data + ", left=" + left + ", right=" + right + "]";
	}

}
