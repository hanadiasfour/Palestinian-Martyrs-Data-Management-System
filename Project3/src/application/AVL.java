package application;

public class AVL extends BinarySearchTree {

	public Object findByName(String data) {

		if (!isEmpty()) {
			Martyr m = (Martyr) ((Martyr) getRoot().getData()).clone();
			m.setName(data);

			TNode a = super.findNode(m, getRoot());
			if (a != null)
				return a.getData();
		}

		return null;
	}

	public TNode update(Object data, TNode node) {

		Martyr m = (Martyr) node.getData();
		delete(m);

		m.setName(((Martyr) data).getName());
		m.setAge(((Martyr) data).getAge());
		m.setGender(((Martyr) data).getGender());
		m.setDateOfDeath(((Martyr) data).getDateOfDeath());
		insert(m);

		return node;

	}

	@Override
	public TNode findnode(Object data) {
		// TODO Auto-generated method stub
		return super.findNode(data, getRoot());
	}

	@Override
	public void insert(Object data) {
		// TODO Auto-generated method stub
		setRoot(insert(data, getRoot()));

	}

	@Override
	protected TNode insert(Object data, TNode node) {
		// TODO Auto-generated method stub

		node = super.insert(data, node);
		updateHeight(node);
		return reBalance(node);

	}

	@Override
	public void delete(Object data) {
		// TODO Auto-generated method stub
		setRoot(delete(data, getRoot()));
	}

	@Override
	protected TNode delete(Object data, TNode node) {
		// TODO Auto-generated method stub
		node = super.delete(data, node);
		updateHeight(node);
		return reBalance(node);
	}

	private int height(TNode node) {

		return node != null ? node.getHeight() : -1;

	}

	private void updateHeight(TNode node) {

		if (node != null) {
			int leftChildHeight = height(node.getLeft());
			int rightChildHeight = height(node.getRight());

			node.setHeight(Math.max(leftChildHeight, rightChildHeight) + 1);
		}

	}

	private TNode reBalance(TNode node) {

		if (node != null) {
			int bf = balanceFactor(node);

			if (bf > 1) {
				if (balanceFactor(node.getLeft()) >= 0)
					node = rotateRight(node);

				else {
					node.setLeft(rotateLeft(node.getLeft()));
					node = rotateRight(node);
				}
			}

			if (bf < -1) {
				if (balanceFactor(node.getRight()) <= 0)
					node = rotateLeft(node);

				else {
					node.setRight(rotateRight(node.getRight()));
					node = rotateLeft(node);
				}
			}
		}

		return node;

	}

	private int balanceFactor(TNode node) {

		return height(node.getLeft()) - height(node.getRight());
	}

	private TNode rotateRight(TNode node) {

		TNode x = node.getLeft();
		node.setLeft(x.getRight());
		x.setRight(node);
		updateHeight(node);
		updateHeight(x);
		return x;

	}

	private TNode rotateLeft(TNode node) {

		TNode x = node.getRight();
		node.setRight(x.getLeft());
		x.setLeft(node);
		updateHeight(node);
		updateHeight(x);
		return x;

	}

	public int getSize() {

		return calculateSize(getRoot());

	}

	private int calculateSize(TNode root) {
		if (root == null) {
			return 0;
		}

		int leftSize = calculateSize(root.getLeft());
		int rightSize = calculateSize(root.getRight());

		return leftSize + rightSize + 1;
	}

	public int getAVLHeight() {

		return calculateHeight(getRoot());

	}

	public int calculateHeight(TNode node) {

		if (node == null)
			return 0;

		if (node.getLeft() == null && node.getRight() == null)
			return 0;

		int l = 0, r = 0;

		if (node.getLeft() != null)
			l = height(node.getLeft());

		if (node.getRight() != null)
			r = height(node.getRight());

		return (l > r) ? (l + 1) : (r + 1);
	}

	public boolean isEmpty() {

		return getRoot() == null;

	}

}
