package psn.jeffreyzhang.jaza.algorithms.AVLTree;

public class AVLTree<T extends Comparable<? super T>> {

    private AVLTreeNode<T> root;

    private int height(AVLTreeNode<T> node) {
        return node == null ? 0 : node.height;
    }

    private int max(int a, int b) {
        return a > b ? a : b;
    }

    /**
     * Left-left rotation
     *
     * @param k2
     * @return
     */
    private AVLTreeNode<T> leftLeftRotation(AVLTreeNode<T> k2) {
        AVLTreeNode<T> k1;
        k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;

        k2.height = max(height(k2.left), height(k2.right)) + 1;
        k1.height = max(height(k1.left), k2.height) + 1;

        return k1;
    }

    /**
     * Right-right rotation
     *
     * @param k1
     * @return
     */
    private AVLTreeNode<T> rightRightRotation(AVLTreeNode<T> k1) {
        AVLTreeNode<T> k2;

        k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;

        k1.height = max(height(k1.left), height(k1.right)) + 1;
        k2.height = max(height(k2.right), k1.height) + 1;

        return k2;
    }

    /**
     * Left-right rotation
     *
     * @param k3
     * @return
     */
    private AVLTreeNode<T> leftRightRotation(AVLTreeNode<T> k3) {
        k3.left = rightRightRotation(k3.left);
        return leftLeftRotation(k3);
    }

    /**
     * Right-left rotation
     *
     * @param k1
     * @return
     */
    private AVLTreeNode<T> rightLeftRotation(AVLTreeNode<T> k1) {
        k1.right = leftLeftRotation(k1.right);
        return rightRightRotation(k1);
    }

    private AVLTreeNode<T> insert(AVLTreeNode<T> tree, T key) {
        if (tree == null) {
            tree = new AVLTreeNode<T>(key, null, null);
        } else {
            int compareResult = key.compareTo(tree.key);
            if (compareResult < 0) {
                tree.left = insert(tree.left, key);
                if (height(tree.left) - height(tree.right) == 2) {
                    if (key.compareTo(tree.left.key) < 0) {
                        tree = leftLeftRotation(tree);
                    } else {
                        tree = leftRightRotation(tree);
                    }
                } else {

                }
            } else if (compareResult > 0) {
                tree.right = insert(tree.right, key);
                if (height(tree.right) - height(tree.left) == 2) {
                    if (key.compareTo(tree.right.key) > 0) {
                        tree = rightRightRotation(tree);
                    } else {
                        tree = rightLeftRotation(tree);
                    }
                }
            } else {

            }
        }

        tree.height = max(height(tree.left), height(tree.right)) + 1;
        return tree;
    }

    public class AVLTreeNode<T extends Comparable<? super T>> {
        T key;
        int height;
        AVLTreeNode<T> left;
        AVLTreeNode<T> right;

        public AVLTreeNode(T key, AVLTreeNode<T> left, AVLTreeNode<T> right) {
            this.key = key;
            this.left = left;
            this.right = right;
        }
    }


}
