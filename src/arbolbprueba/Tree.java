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
        x.setKeys(new ArrayList<Key>());
        x.setChildren(new ArrayList<Node>());
        x.setIsLeaf(true);
        return x;

    }

    //buscar
    public Node search(Node root, Key k) {
        int i = 0;

        // Compara los hashcodes en lugar de los valores
        while (i < root.getN() && k.hashCode() > root.getKey(i).hashCode()) {
            i++;
        }

        // Verifica si el hashcode es igual y luego usa equals para asegurar la igualdad
        if (i < root.getN() && k.hashCode() == root.getKey(i).hashCode()) {
            if (k.equals(root.getKey(i))) {
                return root;
            }
        }

        // Si es una hoja, devuelve null ya que no se encontró la clave
        if (root.isIsLeaf()) {
            return null;
        }

        // Llama recursivamente a search en el hijo correspondiente
        return search(root.getChild(i), k);
    }

    public void split(Node x, int i, Node y) {
        Node z = new Node();
        z.setIsLeaf(y.isIsLeaf());

        int t = grade / 2;
        z.setN(t - 1);

        // Move the second half of y's keys to z
        for (int j = 0; j < t - 1; j++) {
            z.getKeys().add(y.getKey(j + t));
        }

        // If y is not a leaf, move the second half of y's children to z
        if (!y.isIsLeaf()) {
            for (int j = 0; j < t; j++) {
                z.getChildren().add(y.getChild(j + t));
            }
        }

        y.setN(t - 1);

        // Shift children of x to make room for z
        x.getChildren().add(null);
        for (int j = x.getN(); j >= i + 1; j--) {
            x.getChildren().set(j + 1, x.getChild(j));
        }
        x.getChildren().set(i + 1, z);

        // Shift keys of x to make room for y's median key
        x.getKeys().add(null);
        for (int j = x.getN() - 1; j >= i; j--) {
            x.getKeys().set(j + 1, x.getKey(j));
        }
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
    }
//    public void split(Node x, int i, Node y) {
//    Node z = new Node();
//    z.setIsLeaf(y.isIsLeaf());
//
//    int minKeys = (grade - 1) / 2;
//    int gradoMin = grade / 2;
//
//    // Ajuste: Asegurarse de que las listas tienen el tamaño adecuado
//    while (z.getKeys().size() < minKeys) {
//        z.getKeys().add(null);
//    }
//    while (z.getChildren().size() < gradoMin) {
//        z.getChildren().add(null);
//    }
//
//    // Copiar la segunda mitad de las claves de y a z
//    for (int j = 0; j < minKeys; j++) {
//        z.getKeys().set(j, y.getKey(j + gradoMin));
//    }
//
//    // Copiar la segunda mitad de los hijos de y a z
//    if (!y.isIsLeaf()) {
//        for (int j = 0; j < gradoMin; j++) {
//            z.getChildren().set(j, y.getChild(j + gradoMin));
//        }
//    }
//
//    y.setN(minKeys);
//
//    // Mover los hijos de x para crear espacio para el nuevo hijo
//    x.getChildren().add(null); // Añadir un elemento nulo para asegurar el tamaño de la lista
//    for (int j = x.getN(); j >= i + 1; j--) {
//        x.getChildren().set(j + 1, x.getChild(j));
//    }
//
//    x.getChildren().set(i + 1, z);
//
//    // Mover las claves de x para crear espacio para la nueva clave
//    x.getKeys().add(null); // Añadir un elemento nulo para asegurar el tamaño de la lista
//    for (int j = x.getN() - 1; j >= i; j--) {
//        x.getKeys().set(j + 1, x.getKey(j));
//    }
//
//    x.getKeys().set(i, y.getKey(gradoMin - 1));
//    x.setN(x.getN() + 1);
//
//    // Ajustar las claves y los hijos de y
//    for (int j = gradoMin - 1; j < y.getKeys().size(); j++) {
//        y.getKeys().set(j, null);
//    }
//    if (!y.isIsLeaf()) {
//        for (int j = gradoMin; j < y.getChildren().size(); j++) {
//            y.getChildren().set(j, null);
//        }
//    }
//
//    System.out.println("Índice del hijo z: " + (i + 1));
//}


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
                    merge(x, idx); // Merge if the child has less than (grade - 1) / 2 keys
                    deleteKey(x.getChild(idx), k);
                }
            }
        } else {
            if (x.isIsLeaf()&& (search(root, k)==null)) {
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
    if (idx + 1 >= x.getChildren().size()) {
        System.out.println("Merge: Sibling index out of bounds: " + (idx + 1));
        return;
    }

    Node child = x.getChild(idx);
    Node sibling = x.getChild(idx + 1);

    // Asegurarse de que el hijo tiene suficiente espacio
    while (child.getKeys().size() < grade - 1) {
        child.getKeys().add(null);
    }

    child.keys.set(child.getN(), x.getKey(idx));

    for (int i = 0; i < sibling.getN(); ++i) {
        if (i + child.getN() + 1 >= child.keys.size()) {
            System.out.println("Merge: Child keys array out of bounds during merge.");
            return;
        }
        child.keys.set(i + child.getN() + 1, sibling.getKey(i));
    }

    if (!child.isIsLeaf()) {
        while (child.getChildren().size() < grade) {
            child.getChildren().add(null);
        }
        for (int i = 0; i <= sibling.getN(); ++i) {
            child.setChild(i + child.getN() + 1, sibling.getChild(i));
        }
    }

    for (int i = idx + 1; i < x.getN(); ++i) {
        if (i - 1 >= x.keys.size() || i >= x.keys.size()) {
            System.out.println("Merge: Parent keys array out of bounds during shift.");
            return;
        }
        x.keys.set(i - 1, x.getKey(i));
    }

    for (int i = idx + 2; i <= x.getN(); ++i) {
        if (i - 1 >= x.getChildren().size() || i >= x.getChildren().size()) {
            System.out.println("Merge: Parent children array out of bounds during shift.");
            return;
        }
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

    private Key RootKey(Node x, Key k) {
        Node parent = findParent(root, x); // Encuentra el padre del nodo x
        Key v = null;
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
