import java.util.Stack;

/**
 * @author shaff130
 * @author sweet136
 */
public class BinaryTree<K extends Comparable<K>, V> {
    private Node<K, V> root;
    private int numElements = 0;

    public BinaryTree(Node<K, V> root) {
        this.root = root;
        numElements = 1;
    }

    public Node<K, V> getRoot() {
        return this.root;
    }

    /**
     * Add takes in a key and value, and adds them in the appropriate spot in the BST. If there is already one with the
     * current key, will replace.
     * @param key - generic type K, represents the key in a key-value pair
     * @param value - generic type V, represents the value in a key-value pair
     */
    public void add(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> currNode = root;

        boolean found = false;

        if (root == null) {
            root = newNode;
            numElements++;
        } else if (root.getLeft() == null && newNode.getKey().compareTo(root.getKey()) < 0) {
            root.setLeft(newNode);
            numElements++;
        } else if (root.getRight() == null && newNode.getKey().compareTo(root.getKey()) > 0) {
            root.setRight(newNode);
            numElements++;
        } else if (root.getKey().equals(newNode.getKey())) {
            root.setValue(value);
        } else {
            while (!found) {
                if (currNode.getKey().compareTo(newNode.getKey()) < 0) {
                    if (currNode.getRight() != null) {
                        currNode = currNode.getRight();
                    } else {
                        currNode.setRight(newNode);
                        numElements++;
                        found = true;
                    }
                } else if (currNode.getKey().compareTo(newNode.getKey()) > 0) {
                    if (currNode.getLeft() != null) {
                        currNode = currNode.getLeft();
                    } else {
                        currNode.setLeft(newNode);
                        numElements++;
                        found = true;
                    }
                } else if (currNode.getKey().equals(newNode.getKey())) {
                    currNode.setValue(value);
                    found = true;
                }
            }
        }
    }

    /**
     * Find takes in a key, and returns a value V representing the value paired with the inputted key. Value will be
     * null if there is no key found in the tree.
     * @param key - type K, represents a desired key, querying its value
     * @return Value V, null if key not found
     */
    public V find(K key) {
        Node<K, V> currNode = root;
        V value = null;
        boolean found = false;

        while (!found) {

            if (currNode.getKey().equals(key)) {
                value = currNode.getValue();
                found = true;
            } else if (key.compareTo(currNode.getKey()) < 0) {
                if (currNode.getLeft() != null) {
                    currNode = currNode.getLeft();
                } else {
                    break;
                }
            } else if (key.compareTo(currNode.getKey()) > 0) {
                if (currNode.getRight() != null) {
                    currNode = currNode.getRight();
                } else {
                    break;
                }
            }
        }
        return value;
    }

    /**
     * Flatten creates an array of all the values V in the current BST
     * @return Array of values type V
     */
    @SuppressWarnings("unchecked")
    public V[] flatten() {
        V[] result = (V[]) new Object[numElements];
        Stack<Node<K, V>> recentValues = new Stack<>();
        Node<K, V> currNode = root;
        int index = 0;

        if (root == null) {
            return (V[]) new Object[0];
        } else {
            while (currNode != null) {
                recentValues.push(currNode);
                currNode = currNode.getLeft();
            }

            while (recentValues.size() > 0) {

                Node<K, V> popNode = recentValues.pop();
                if (popNode.getValue() != null) {
                    result[index] = popNode.getValue();
                    index++;
                }

                while (popNode.getRight() != null) {
                    popNode = popNode.getRight();

                    if (popNode.getLeft() != null) {

                        while (popNode.getLeft() != null) {
                            recentValues.push(popNode);
                            popNode = popNode.getLeft();
                            recentValues.push(popNode);
                        }

                        while (recentValues.size() > 0) {
                            popNode = recentValues.pop();
                            if (popNode.getValue() != null) {
                                result[index] = popNode.getValue();
                                index++;
                            }
                        }
                    } else {
                        if (popNode.getValue() != null) {
                            result[index] = popNode.getValue();
                            index++;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Remove takes in a key K and calls deleteNode with the current BST and desired key to remove
     * @param key - type K, represents a key desired to be removed (as well as its corresponding value)
     */
    public void remove(K key) {
        deleteNode(root, key);
    }

    /**
     * containsSubtree takes in a BST other and compares it with the current BST. If it is a subtree, return true, etc.
     * @param other - BinaryTree of types K and V, represents a subtree
     * @return boolean indicating if parameter passed in is a subtree of object with method called, or not
     */
    public boolean containsSubtree(BinaryTree<K, V> other) {
        boolean found = true;
        boolean subFound = false;
        int index = 0;

        Node[] nodeArray = this.flattenNodes();
        Node[] nodeArrayOther = other.flattenNodes();

        for (Node aNodeArrayOther : nodeArrayOther) {

            while (!subFound) {
                if (index == nodeArray.length - 1) {
                    found = false;
                    break;
                } else if (!aNodeArrayOther.getKey().equals(nodeArray[index].getKey()) || !aNodeArrayOther.getValue().equals(nodeArray[index].getValue())) {
                    index++;
                } else {
                    subFound = true;
                }
            }

            if (subFound) {
                if (nodeArray[index].getKey().equals(aNodeArrayOther.getKey()) && nodeArray[index].getValue().equals(aNodeArrayOther.getValue())) {
                    index++;
                } else {
                    found = false;
                    break;
                }
            }
        }
        return found;
    }


    /*
    Helper Functions:
     */

    /**
     * setter for numElements
     * @param numElements - represents total number of elements in the BST
     */
    void setNumElements(int numElements) {
        this.numElements = numElements;
    }

    /**
     * findMax takes in a starting node (normally the root), and returns the maximum key
     * @param start - Node of type K,V (representing a root)
     * @return Node with the maximum key
     */
    private Node<K, V> findMax(Node<K, V> start) {
        if (start != null) {
            while (start.getRight() != null) {
                start = start.getRight();
            }
        }
        return start;
    }

    /**
     * deleteNode takes in a root and key, and deletes the node with the corresponding tree if one is found
     * @param root - represents a BST
     * @param key - desired key to remove
     * @return - Node, recursively called. Will do nothing if no key is found
     */
    private Node<K, V> deleteNode(Node<K, V> root, K key) {
        if (root != null) {
            if (root.getKey().equals(key)) {
                if (root.getLeft() != null && root.getRight() == null) {
                    root.setKey(root.getLeft().getKey());
                    root.setValue(root.getLeft().getValue());

                    return root.getLeft();
                } else if (root.getLeft() == null && root.getRight() != null) {
                    root.setKey(root.getRight().getKey());
                    root.setValue(root.getRight().getValue());

                    return root.getRight();
                } else {
                    if (root.getLeft() == null && root.getRight() == null) {
                        return null;
                    } else {
                        Node<K, V> maxNode = findMax(root.getLeft());
                        root.setKey(maxNode.getKey());
                        root.setValue(maxNode.getValue());
                        deleteNode(root.getLeft(), maxNode.getKey());
                    }
                }
            }
            root.setLeft(deleteNode(root.getLeft(), key));
            root.setRight(deleteNode(root.getRight(), key));
        }
        return root;
    }

    /**
     * A variant of flatten(), only difference being return type (this being an array of Nodes, instead of values V)
     * @return an Array of all the Nodes in the BST, ordered by key
     */
    private Node[] flattenNodes() {
        Node[] result = new Node[numElements];
        Stack<Node<K, V>> recentValues = new Stack<>();
        Node<K, V> currNode = root;
        int index = 0;

        if (root == null) {
            return new Node[0];
        } else {
            while (currNode != null) {
                recentValues.push(currNode);
                currNode = currNode.getLeft();
            }

            while (recentValues.size() > 0) {

                Node<K, V> popNode = recentValues.pop();
                if (popNode != null) {
                    result[index] = popNode;
                    index++;
                }

                while ((popNode != null ? popNode.getRight() : null) != null) {
                    popNode = popNode.getRight();

                    if (popNode.getLeft() != null) {

                        while (popNode.getLeft() != null) {
                            recentValues.push(popNode);
                            popNode = popNode.getLeft();
                            recentValues.push(popNode);
                        }

                        while (recentValues.size() > 0) {
                            popNode = recentValues.pop();
                            if (popNode != null) {
                                result[index] = popNode;
                                index++;
                            }
                        }
                    } else {
                        result[index] = popNode;
                        index++;
                    }
                }
            }
        }
        return result;
    }
}
