package application;

public class BinarySearchTree extends BinaryBaseTree implements BSTIF {

	// Here!!

	@Override
	public TNode findnode(Object data) {
		// TODO Auto-generated method stub
		return findNode(data, getRoot());
	}

	protected TNode findNode(Object data, TNode node) {

		if (node != null) {

			if (data instanceof Martyr) {
				if (((Martyr) node.getData()).compareTo((Martyr) data) < 0)
					node = findNode(data, node.getRight());

				else if (((Martyr) node.getData()).compareTo((Martyr) data) > 0)
					node = findNode(data, node.getLeft());

			} else if (data instanceof DateStack) {
				if (((DateStack) node.getData()).compareTo((DateStack) data) < 0)
					node = findNode(data, node.getRight());

				else if (((DateStack) node.getData()).compareTo((DateStack) data) > 0)
					node = findNode(data, node.getLeft());
			}
		}
		return node;

	}

	@Override
	public void insert(Object data) {
		// TODO Auto-generated method stub
		setRoot(insert(data, getRoot()));

	}

	protected TNode insert(Object data, TNode node) {
		if (node == null)
			node = new TNode(data);

		else if (data instanceof Martyr) {
			if (((Martyr) node.getData()).compareTo((Martyr) data) < 0)
				node.setRight(insert(data, node.getRight()));

			else if (((Martyr) node.getData()).compareTo((Martyr) data) >= 0)
				node.setLeft(insert(data, node.getLeft()));

		} else if (data instanceof DateStack) {

			if (((DateStack) node.getData()).compareTo((DateStack) data) < 0)
				node.setRight(insert(data, node.getRight()));

			else if (((DateStack) node.getData()).compareTo((DateStack) data) >= 0)
				node.setLeft(insert(data, node.getLeft()));

		}
		return node;

	}

	@Override
	public void delete(Object data) {
		// TODO Auto-generated method stub

		setRoot(delete(data, getRoot()));

	}

	protected TNode delete(Object data, TNode node) {
		// TODO Auto-generated method stub

		if (data instanceof Martyr) {
			if (node == null)
				return null;
			else if (((Martyr) node.getData()).compareTo((Martyr) data) < 0)
				node.setRight(delete(data, node.getRight()));

			else if (((Martyr) node.getData()).compareTo((Martyr) data) > 0)
				node.setLeft(delete(data, node.getLeft()));

			else if (node.getLeft() == null && node.getRight() == null) {
				node = null;

			} else if (node.getLeft() == null)
				node = node.getRight();

			else if (node.getRight() == null) {
				node = node.getLeft();

			} else
				deleteNodeWithTwoChildren(node);

		} else if (data instanceof DateStack) {

			if (node == null)
				return null;
			else if (((DateStack) node.getData()).compareTo((DateStack) data) < 0)
				node.setRight(delete(data, node.getRight()));

			else if (((DateStack) node.getData()).compareTo((DateStack) data) > 0)
				node.setLeft(delete(data, node.getLeft()));

			else if (node.getLeft() == null && node.getRight() == null) {
				node = null;

			} else if (node.getLeft() == null)
				node = node.getRight();

			else if (node.getRight() == null) {
				node = node.getLeft();

			} else
				deleteNodeWithTwoChildren(node);

		}

		return node;
	}

	private void deleteNodeWithTwoChildren(TNode node) {

		TNode inOrderSuccessor = findMin(node.getRight());

		node.setData(inOrderSuccessor.getData());

		node.setRight(delete(inOrderSuccessor.getData(), node.getRight()));

	}

	private TNode findMin(TNode node) {

		if (node == null)
			return null;
		if (node.getLeft() == null)
			return node;
		else
			return findMin(node.getLeft());

	}

}
