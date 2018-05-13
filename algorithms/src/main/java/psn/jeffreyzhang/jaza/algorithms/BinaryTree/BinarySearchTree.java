package psn.jeffreyzhang.jaza.algorithms.BinaryTree;

public class BinarySearchTree<AnyType extends Comparable<? super AnyType>> {

	private BinaryNode<AnyType> root;

	public BinarySearchTree() {
		root = null;
	}

	public void makeEmpty() {
		root = null;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public boolean contains(AnyType x) {
		return contains(x, root);
	}

	public AnyType findMin() throws UnderflowException {
		if (isEmpty())
			throw new UnderflowException();
		return findMin(root).element;
	}

	public void insert(AnyType x) {
		insert(x, root);
	}

	public void remove(AnyType x) {
		remove(x, root);
	}

	private boolean contains(AnyType x, BinaryNode<AnyType> t) {
		if (t == null)
			return false;

		int compareResult = x.compareTo(t.element);
		if (compareResult < 0)
			return contains(x, t.left);
		else if (compareResult > 0)
			return contains(x, t.right);
		else
			return true;
	}

	private BinaryNode<AnyType> findMin(BinaryNode<AnyType> t) {
		if (t == null)
			return null;
		else if (t.left == null)
			return t;

		return findMin(t);
	}

	private BinaryNode<AnyType> findMax(BinaryNode<AnyType> t) {
		if (t != null)
			while (t.right != null)
				t = t.right;

		return t;
	}

	private BinaryNode<AnyType> insert(AnyType x, BinaryNode<AnyType> t) {
		if (t == null)
			t = new BinaryNode<AnyType>(x, null, null);

		int compareResult = x.compareTo(t.element);
		if (compareResult < 0)
			t.left = insert(x, t.left);
		else if (compareResult > 0)
			t.right = insert(x, t.right);
		else
			;

		return t;
	}

	private BinaryNode<AnyType> remove(AnyType x, BinaryNode<AnyType> t) {
		if (t == null)
			return t;

		int compareResult = x.compareTo(t.element);

		if (compareResult < 0)
			t.left = remove(x, t.left);
		else if (compareResult > 0)
			t.right = remove(x, t.right);
		else if (t.left != null && t.right != null) {
			t.element = findMin(t.right).element;
			t.right = remove(t.element, t.right);
		} else
			t = t.left != null ? t.left : t.right;

		return t;
	}

	private void printTree(BinaryNode<AnyType> t) {

	}

	private static class BinaryNode<AnyType> {

		AnyType element;
		BinaryNode<AnyType> left;
		BinaryNode<AnyType> right;
		BinaryNode(AnyType element) {
			this(element, null, null);
		}
		BinaryNode(AnyType thisElement, BinaryNode<AnyType> leftElement, BinaryNode<AnyType> rightElement) {
			element = thisElement;
			left = leftElement;
			right = rightElement;
		}
	}
}