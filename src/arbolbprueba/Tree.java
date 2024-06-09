package arbolbprueba;

import java.util.ArrayList;

public class Tree {

    private Node root;
    private int grade;

    public Tree() {

    }

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

        int t = (grade + 1) / 2;
        z.setN(t - 1);

        // Copia las llaves superiores al nuevo nodo z
        for (int j = 0; j < t - 1; j++) {
            z.getKeys().add(y.getKey(j + t));
        }

        if (!y.isIsLeaf()) {
            // Copia los hijos superiores al nuevo nodo z
            for (int j = 0; j < t; j++) {
                z.getChildren().add(y.getChild(j + t));
            }
        }

        // Ajusta el número de llaves en y
        y.setN(t - 1);

        // Mueve los hijos de x para hacer espacio para z
        x.getChildren().add(null); // Añadir un elemento nulo para asegurar el tamaño de la lista
        for (int j = x.getN(); j >= i + 1; j--) {
            x.getChildren().set(j + 1, x.getChild(j));
        }

        // Enlaza z al nodo padre x
        x.getChildren().set(i + 1, z);

        // Mueve las llaves de x para hacer espacio para la llave del medio
        x.getKeys().add(null); // Añadir un elemento nulo para asegurar el tamaño de la lista
        for (int j = x.getN() - 1; j >= i; j--) {
            x.getKeys().set(j + 1, x.getKey(j));
        }

        // Mueve la llave del medio de y a x
        x.getKeys().set(i, y.getKey(t - 1));

        // Elimina las llaves movidas de y
        for (int j = y.getN(); j < y.getKeys().size();) {
            y.getKeys().remove(y.getN());
        }

        // Elimina los hijos movidos de y si no es hoja
        if (!y.isIsLeaf()) {
            for (int j = y.getN() + 1; j < y.getChildren().size();) {
                y.getChildren().remove(y.getN() + 1);
            }
        }

        x.setN(x.getN() + 1);

        System.out.println("Índice del hijo z: " + (i + 1));
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
        int i = x.getN() - 1;
        if (x.isIsLeaf()) {
            x.keys.add(null);
            while (i >= 0 && k.hashCode() < x.getKey(i).hashCode()) {
                x.keys.set(i + 1, x.keys.get(i));
                i--;
            }
            x.keys.set(i + 1, k);
            x.setN(x.getN() + 1);
        } else {
            while (i >= 0 && k.hashCode() < x.getKey(i).hashCode()) {
                i--;
            }
            i++;
            Node child = x.getChild(i);
            if (child.getN() == grade - 1) {
                split(x, i, child);
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

//    public void deleteKey(Key k) {
//        deleteKey(root, k);
//    }
//
//    private void deleteKey(Node x, Key k) {
//        if (!x.isIsLeaf()) {
//            Node y = precedingChild(x);
//            Node z = succesorChild(x, k);
//            if (y.getN() > grade - 1) {
//                Key kPrime = findPredecessorKey(y, k);
//                moveKey(kPrime, y, x);
//                moveKey(k, x, z);
//                deleteKey(z, k);
//            } else if (z.getN() > grade - 1) {
//                Key kPrime = findSuccessorKey(z, k);
//                moveKey(kPrime, z, x);
//                moveKey(k, x, y);
//                deleteKey(y, k);
//            } else {
//                moveKey(k, x, y);
//                Node mergedNode = mergeNodes(y, z);
//                x.getChildren().remove(z);
//                deleteKey(mergedNode, k);
//            }
//        } else {
//            // Caso de nodo hoja
//            Node y = precedingChild(x);
//            Node z = succesorChild(x, k);
//            Node w = root;
//            Key v = RootKey(x, k);
//            if (x.getN() > grade - 1) {
//                removeKey(k, x);
//            } else if (y.getN() > grade - 1) {
//                Key kPrime = findPredecessorKey(w, v);
//                moveKey(kPrime, y, w);
//                kPrime = findSuccessorKey(w, v);
//                moveKey(kPrime, w, x);
//                deleteKey(x, k);
//            } else if (w.getN() > grade - 1) {
//                Key kPrime = findSuccessorKey(w, v);
//                moveKey(kPrime, z, w);
//                kPrime = findPredecessorKey(w, v);
//                moveKey(kPrime, w, x);
//                deleteKey(y, k);
//            } else {
//                Node s = findSibling(w);
//                Node wPrime = w;
//                if (wPrime.getN() == grade - 1) {
//                    mergeNodes(wPrime, w);
//                    mergeNodes(w, s);
//                    deleteKey(x, k);
//                } else {
//                    moveKey(v, w, x);
//                    deleteKey(x, k);
//                }
//            }
//        }
//    }
//
//    private Node precedingChild(Node x) {
//        return x.getChild(0);
//    }
//
//    private Node succesorChild(Node x, Key k) {
//        int idx = x.keys.indexOf(k);
//        if (idx == -1 || idx + 1 >= x.getChildren().size()) {
//            return null; // Maneja el caso en el que k no se encuentra o no hay un hijo sucesor
//        }
//        return x.getChild(idx + 1);
//    }
//
//    private Key RootKey(Node x, Key k) {
//        Node parent = findParent(root, x); // Encuentra el padre del nodo x
//        Key v = null;
//        if (parent != null) {
//            int index = parent.getChildren().indexOf(x);
//            if (index > 0) {
//                v = parent.keys.get(index - 1); // La clave padre es la clave anterior al nodo en el padre
//            }
//        }
//        return v;
//    }
//
//    private Node mergeNodes(Node n1, Node n2) {
//        Node mergedNode = new Node();
//        mergedNode.keys.addAll(n1.keys);
//        mergedNode.keys.addAll(n2.keys);
//        if (!n1.isIsLeaf()) {
//            mergedNode.setChildren(n1.getChildren());
//            mergedNode.setChildren(n2.getChildren());
//        }
//        mergedNode.setIsLeaf(n1.isIsLeaf());
//        mergedNode.setN(n1.getN() + n2.getN());
//        return mergedNode;
//    }
//
//    private Key findPredecessorKey(Node n, Key k) {
//        Node current = n.getChild(n.keys.indexOf(k));
//        while (!current.isIsLeaf()) {
//            current = current.getChild(current.getN());
//        }
//        return current.keys.get(current.getN() - 1);
//    }
//
//    private Key findSuccessorKey(Node n, Key k) {
//        Node current = n.getChild(n.keys.indexOf(k) + 1);
//        while (!current.isIsLeaf()) {
//            current = current.getChild(0);
//        }
//        return current.keys.get(0);
//    }
//
//    private Node findSibling(Node n) {
//        Node parent = findParent(root, n);
//        if (parent == null) {
//            return null; // No tiene padre, por lo tanto no tiene hermanos
//        }
//
//        Node mostPopulousSibling = null;
//        int maxKeys = -1;
//
//        for (Node child : parent.getChildren()) {
//            if (child != n && child.getN() > maxKeys) {
//                mostPopulousSibling = child;
//                maxKeys = child.getN();
//            }
//        }
//
//        return mostPopulousSibling;
//    }
//
//    private Node findParent(Node current, Node child) {
//        if (current.isIsLeaf()) {
//            return null;
//        }
//
//        for (Node node : current.getChildren()) {
//            if (node == child) {
//                return current;
//            } else {
//                Node parent = findParent(node, child);
//                if (parent != null) {
//                    return parent;
//                }
//            }
//        }
//
//        return null;
//    }
    public void delete(Key k) {
        if (root == null) {
            System.out.println("El árbol está vacío.");
            return;
        }

        deleteKey(root, k);

        if (root.getN() == 0) {
            if (root.isIsLeaf()) {
                root = null;
            } else {
                root = root.getChild(0);
            }
        }
    }

    private void deleteKey(Node x, Key k) {
        int idx = findKey(x, k);

        if (idx < x.getN() && x.getKey(idx).equals(k)) {
            if (x.isIsLeaf()) {
                for (int i = idx + 1; i < x.getN(); ++i) {
                    x.keys.set(i - 1, x.keys.get(i));
                }
                x.keys.remove(x.getN() - 1);
                x.setN(x.getN() - 1);
            } else {
                Key pred = getPredecessor(x, idx);
                Key succ = getSuccessor(x, idx);

                if (x.getChild(idx).getN() > (grade - 1) / 2) {
                    x.keys.set(idx, pred);
                    deleteKey(x.getChild(idx), pred);
                } else if (x.getChild(idx + 1).getN() > (grade - 1) / 2) {
                    x.keys.set(idx, succ);
                    deleteKey(x.getChild(idx + 1), succ);
                } else {
                    merge(x, idx);
                    deleteKey(x.getChild(idx), k);
                }
            }
        } else {
            if (x.isIsLeaf()) {
                System.out.println("La clave " + k + " no existe en el árbol.");
                return;
            }

            boolean flag = (idx == x.getN());
            Node child = x.getChild(idx);

            if (child.getN() <= (grade - 1) / 2) {
                if (idx != 0 && x.getChild(idx - 1).getN() > (grade - 1) / 2) {
                    borrowFromPrev(x, idx);
                } else if (idx != x.getN() && x.getChild(idx + 1).getN() > (grade - 1) / 2) {
                    borrowFromNext(x, idx);
                } else {
                    if (idx != x.getN()) {
                        merge(x, idx);
                    } else {
                        merge(x, idx - 1);
                        idx--;
                    }
                }
            }

            if (flag && idx > x.getN()) {
                deleteKey(x.getChild(idx - 1), k);
            } else {
                deleteKey(x.getChild(idx), k);
            }
        }
    }

    private int findKey(Node x, Key k) {
        int idx = 0;
        while (idx < x.getN() && x.getKey(idx).hashCode() < k.hashCode()) {
            ++idx;
        }
        return idx;
    }

    private Key getPredecessor(Node x, int idx) {
        Node cur = x.getChild(idx);
        while (!cur.isIsLeaf()) {
            cur = cur.getChild(cur.getN());
        }
        return cur.getKey(cur.getN() - 1);
    }

    private Key getSuccessor(Node x, int idx) {
        Node cur = x.getChild(idx + 1);
        while (!cur.isIsLeaf()) {
            cur = cur.getChild(0);
        }
        return cur.getKey(0);
    }

    private void merge(Node x, int idx) {
        Node child = x.getChild(idx);
        Node sibling = x.getChild(idx + 1);

        child.keys.set(child.getN(), x.getKey(idx));
        for (int i = 0; i < sibling.getN(); ++i) {
            child.keys.set(i + child.getN() + 1, sibling.getKey(i));
        }

        if (!child.isIsLeaf()) {
            for (int i = 0; i <= sibling.getN(); ++i) {
                child.setChild(i + child.getN() + 1, sibling.getChild(i));
            }
        }

        for (int i = idx + 1; i < x.getN(); ++i) {
            x.keys.set(i - 1, x.getKey(i));
        }
        for (int i = idx + 2; i <= x.getN(); ++i) {
            x.setChild(i - 1, x.getChild(i));
        }

        child.setN(child.getN() + sibling.getN() + 1);
        x.setN(x.getN() - 1);
    }

    private void borrowFromPrev(Node x, int idx) {
        Node child = x.getChild(idx);
        Node sibling = x.getChild(idx - 1);

        for (int i = child.getN() - 1; i >= 0; --i) {
            child.keys.set(i + 1, child.keys.get(i));
        }

        if (!child.isIsLeaf()) {
            for (int i = child.getN(); i >= 0; --i) {
                child.setChild(i + 1, child.getChild(i));
            }
        }

        child.keys.set(0, x.getKey(idx - 1));
        if (!child.isIsLeaf()) {
            child.setChild(0, sibling.getChild(sibling.getN()));
        }

        x.keys.set(idx - 1, sibling.getKey(sibling.getN() - 1));
        child.setN(child.getN() + 1);
        sibling.setN(sibling.getN() - 1);
    }

    private void borrowFromNext(Node x, int idx) {
        Node child = x.getChild(idx);
        Node sibling = x.getChild(idx + 1);

        child.keys.set(child.getN(), x.getKey(idx));
        if (!child.isIsLeaf()) {
            child.setChild(child.getN() + 1, sibling.getChild(0));
        }

        x.keys.set(idx, sibling.getKey(0));
        for (int i = 1; i < sibling.getN(); ++i) {
            sibling.keys.set(i - 1, sibling.keys.get(i));
        }

        if (!sibling.isIsLeaf()) {
            for (int i = 1; i <= sibling.getN(); ++i) {
                sibling.setChild(i - 1, sibling.getChild(i));
            }
        }

        child.setN(child.getN() + 1);
        sibling.setN(sibling.getN() - 1);
    }

}
