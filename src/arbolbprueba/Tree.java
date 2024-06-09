package arbolbprueba;

public class Tree {

    private Node root;
    private int grade;

    public Tree(Node root, int grade) {
        this.root = root;
        this.grade = grade;
    }

    public Tree(int grade) {
        this.grade = grade;
    }

    public Tree(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    //crear Arbol B vacio
    public Node createBTree() {
        Node x = new Node();
        x.setIsLeaf(true);
        x.setN(0);
        return x;

    }

    //buscar
    public Node search(Node root, Key k) {
        int i = 0;

        while (i <= root.getN() && k.getKeyValue().compareTo(root.getKey(i).getKeyValue()) > 0) {
            i++;
        }

        if (i <= root.getN() && k.getKeyValue().compareTo(root.getKey(i).getKeyValue()) == 0) {
            return root;
        }

        if (root.isIsLeaf()) {
            return null;
        }

        return search(root.getChild(i), k);
    }

    //split 
    public void split(Node x, int i, Node y) {
        Node z = new Node();
        z.setIsLeaf(y.isIsLeaf());

        int minKeys = (grade - 1) / 2;
        int gradoMin = grade / 2;

        z.setN(minKeys);

        for (int j = 0; j < minKeys; j++) {
            z.getKeys().add(y.getKey(j + gradoMin));
        }

        if (!y.isIsLeaf()) {
            for (int j = 0; j < gradoMin; j++) {
                z.getChildren().add(y.getChild(j + gradoMin));
            }
        }

        y.setN(minKeys);

        for (int j = x.getN(); j > i; j--) {
            x.getChildren().add(j + 1, x.getChild(j));
        }

        x.getChildren().add(i + 1, z);

        for (int j = x.getN() - 1; j > i; j--) {
            x.getKeys().add(j + 1, x.getKey(j));
        }

        x.getKeys().add(i, y.getKey(gradoMin));
        x.setN(x.getN() + 1);
    }

    public void insert(Key k) {
        Node root1 = root;
        if (root1.getN() == (grade - 1)) {
            Node s = new Node();
            root = s;
            s.setIsLeaf(false);
            s.setN(0);
            s.setChild(root1);
            split(s, 0, root1);
            insertNonFull(s, k);
        } else {
            insertNonFull(root1, k);
        }
    }

    private void insertNonFull(Node x, Key k) {
        int i = x.getN();
        if (x.isIsLeaf()) {
            while (i >= 1 && k.hashCode() < x.getKey(i).hashCode()) {
                x.keys.set(i + 1, x.keys.get(i));
                i--;
            }
            x.keys.set(i + 1, k);
            x.setN(x.getN() + 1);
        } else {
            while (i >= 1 && k.hashCode() < x.getKey(i).hashCode()) {
                i--;
            }
            i++;
            if (x.getChild(i).getN() == grade - 1) {
                split(x, i, x.getChild(i));
                if (k.hashCode() > x.getKey(i).hashCode()) {
                    i++;
                }
            }
            insertNonFull(x.getChild(i), k);
        }
    }

    private void moveKey(Key k, Node n1, Node n2) {
        n1.keys.remove(k);
        n2.keys.add(k);
    }

    private void removeKey(Key k, Node n) {
        n.keys.remove(k);
        n.setN(n.getN() - 1);
    }

    private void deleteKey(Node x, Key k) {
        if (!x.isIsLeaf()) {
            Node y = precedingChild(x);
            Node z = succesorChild(x, k);
            if (y.getN() > grade - 1) {
                Key kPrime = findPredecessorKey(y, k);
                moveKey(kPrime, y, x);
                moveKey(k, x, z);
                deleteKey(z, k);
            } else if (z.getN() > grade - 1) {
                Key kPrime = findSuccessorKey(z, k);
                moveKey(kPrime, z, x);
                moveKey(k, x, y);
                deleteKey(y, k);
            } else {
                moveKey(k, x, y);
                Node mergedNode = mergeNodes(y, z);
                x.getChildren().remove(z);
                deleteKey(mergedNode, k);
            }
        } else {
            // Caso de nodo hoja
            Node y = precedingChild(x);
            Node z = succesorChild(x, k);
            Node w = root;
            Key v = RootKey(x, k);
            if (x.getN() > grade - 1) {
                removeKey(k, x);
            } else if (y.getN() > grade - 1) {
                Key kPrime = findPredecessorKey(w, v);
                moveKey(kPrime, y, w);
                kPrime = findSuccessorKey(w, v);
                moveKey(kPrime, w, x);
                deleteKey(x, k);
            } else if (w.getN() > grade - 1) {
                Key kPrime = findSuccessorKey(w, v);
                moveKey(kPrime, z, w);
                kPrime = findPredecessorKey(w, v);
                moveKey(kPrime, w, x);
                deleteKey(y, k);
            } else {
                Node s = findSibling(w);
                Node wPrime = w;
                if (wPrime.getN() == grade - 1) {
                    mergeNodes(wPrime, w);
                    mergeNodes(w, s);
                    deleteKey(x, k);
                } else {
                    moveKey(v, w, x);
                    deleteKey(x, k);
                }
            }
        }
    }

    private Node precedingChild(Node x) {
        return x.getChild(0);
    }

    private Node succesorChild(Node x, Key k) {
        int idx = x.keys.indexOf(k);
        if (idx == -1 || idx + 1 >= x.getChildren().size()) {
            return null; // Maneja el caso en el que k no se encuentra o no hay un hijo sucesor
        }
        return x.getChild(idx + 1);
    }

    private Key RootKey(Node x,Key k){
        Node parent = findParent(root, x); // Encuentra el padre del nodo x
        Key v = null; // Inicializa la clave padre como nula
        if (parent != null) {
            int index = parent.getChildren().indexOf(x);
            if (index > 0) {
                v = parent.keys.get(index - 1); // La clave padre es la clave anterior al nodo en el padre
            }
        } 
        return v;
    }
    private Node mergeNodes(Node n1, Node n2) {
        Node mergedNode = new Node();
        mergedNode.keys.addAll(n1.keys);
        mergedNode.keys.addAll(n2.keys);
        if (!n1.isIsLeaf()) {
            mergedNode.setChildren(n1.getChildren());
            mergedNode.setChildren(n2.getChildren());
        }
        mergedNode.setIsLeaf(n1.isIsLeaf());
        mergedNode.setN(n1.getN() + n2.getN());
        return mergedNode;
    }

    private Key findPredecessorKey(Node n, Key k) {
        Node current = n.getChild(n.keys.indexOf(k));
        while (!current.isIsLeaf()) {
            current = current.getChild(current.getN());
        }
        return current.keys.get(current.getN() - 1);
    }

    private Key findSuccessorKey(Node n, Key k) {
        Node current = n.getChild(n.keys.indexOf(k) + 1);
        while (!current.isIsLeaf()) {
            current = current.getChild(0);
        }
        return current.keys.get(0);
    }

    private Node findSibling(Node n) {
        Node parent = findParent(root, n);
        if (parent == null) {
            return null; // No tiene padre, por lo tanto no tiene hermanos
        }

        Node mostPopulousSibling = null;
        int maxKeys = -1;

        for (Node child : parent.getChildren()) {
            if (child != n && child.getN() > maxKeys) {
                mostPopulousSibling = child;
                maxKeys = child.getN();
            }
        }

        return mostPopulousSibling;
    }

    private Node findParent(Node current, Node child) {
        if (current.isIsLeaf()) {
            return null;
        }

        for (Node node : current.getChildren()) {
            if (node == child) {
                return current;
            } else {
                Node parent = findParent(node, child);
                if (parent != null) {
                    return parent;
                }
            }
        }

        return null;
    }

}
