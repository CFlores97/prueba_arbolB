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

    //split  "x" no esta full, "y" esta full e hijo de "x"
    public void split(Node x, int i, Node y) {
        Node z = new Node();
        z.setIsLeaf(y.isIsLeaf());

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
}
