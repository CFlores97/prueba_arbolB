package arbolbprueba;

import java.util.ArrayList;

public class ArbolBPrueba {

    public static void main(String[] args) {

        int grado = 4;

        Node root = new Node();
        root.setKeys(new ArrayList<Key>());
        root.setChildren(new ArrayList<Node>());

        root.getKeys().add(new Key("3"));
        root.setN(1);

        Tree myTree = new Tree(root, grado);

        Node hijo1 = new Node();
        hijo1.setIsLeaf(true);
        hijo1.setKeys(new ArrayList<Key>());
        hijo1.getKeys().add(new Key("1"));
        hijo1.getKeys().add(new Key("2"));
        hijo1.setN(2);

        Node hijo2 = new Node();
        hijo2.setIsLeaf(true);
        hijo2.setKeys(new ArrayList<Key>());
        hijo2.getKeys().add(new Key("4"));
        hijo2.getKeys().add(new Key("5"));
        hijo2.getKeys().add(new Key("6"));
        hijo2.setN(3);

        root.getChildren().add(hijo1);
        root.getChildren().add(hijo2);

        System.out.println("Raíz: " + myTree.getRoot() + "\n\nBefore split");
        for (Node hijo : myTree.getRoot().getChildren()) {
            System.out.println("Child: " + hijo);
        }

        // Realizar split en hijo2 para este ejemplo
        myTree.split(myTree.getRoot(), 1, hijo2);

        System.out.println("\nAfter split");
        System.out.println("Raíz: " + myTree.getRoot());
        for (Node hijo : myTree.getRoot().getChildren()) {
            System.out.println("Child: " + hijo);
        }

    }

}
