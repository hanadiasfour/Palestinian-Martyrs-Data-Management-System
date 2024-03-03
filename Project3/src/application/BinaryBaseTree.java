package application;

public class BinaryBaseTree implements BinaryTree {

	private TNode root;

	public void setRoot(TNode root) {
		this.root = root;
	}

	@Override
	public TNode getRoot() {
		// TODO Auto-generated method stub
		return root;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		appendStringToTree(builder, root);
		return builder.toString();

	}

	private void appendStringToTree(StringBuilder builder, TNode node) {


		if (node.getLeft() != null)
			appendStringToTree(builder, node.getLeft());

		builder.append(node.getData()+"\n");

		if (node.getRight() != null)
			appendStringToTree(builder, node.getRight());

	}
	
	

}
