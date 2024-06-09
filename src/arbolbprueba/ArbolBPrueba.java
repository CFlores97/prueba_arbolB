package arbolbprueba;

import java.util.ArrayList;
import java.util.Scanner;

public class ArbolBPrueba {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Tree arbol = new Tree();
        //CRUD

        boolean running = true;
        
        while (running) {
            System.out.println("1. Create Tree\n" + "2. Search\n" + "3. Insert\n" + "4. imprimir arbol\n" + "5. Salir");
            int option = sc.nextInt();
            
            switch (option) {
                case 1:
                    System.out.println("Ingrese el grado del arbol: ");
                    int grado = sc.nextInt();
                    
                    Node root = arbol.createBTree();
                    
                    arbol = new Tree(root, grado);
                    
                    System.out.println("\nArbol creado exitosamente!");
                    break;
                case 2:
                    System.out.println("Ingrese la llave que desea buscar(numeros): ");
                    String keyValue = sc.next();
                    
                    Key keyToSearch = new Key(keyValue);
                    
                    Node nodoEncontrado = arbol.search(arbol.getRoot(), keyToSearch);
                    
                    if (nodoEncontrado == null) {
                        System.out.println("No existe esa llave en el arbol");
                    } else {
                        System.out.println(nodoEncontrado.getKeys());
                    }
                    break;
                case 3:
                    System.out.println("Ingrese el valor de la llave que va a insertar(numeros): ");
                    String valueToInsert = sc.next();
                    
                    Key keyToInsert = new Key(valueToInsert);
                    
                    arbol.insert(keyToInsert);
                    break;
                case 4:
                    System.out.println("Raíz: " + arbol.getRoot());
                    for (Node hijo : arbol.getRoot().getChildren()) {
                        System.out.println("Child: " + hijo);
                    }
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    throw new AssertionError();
            }
        }

//        int grado = 6;
//
//        Node root = new Node();
//        root.setKeys(new ArrayList<Key>());
//        root.setChildren(new ArrayList<Node>());
//
//        root.getKeys().add(new Key("3"));
//        root.setN(1);
//
//        Tree myTree = new Tree(root, grado);
//
//        Node hijo1 = new Node();
//        hijo1.setIsLeaf(true);
//        hijo1.setKeys(new ArrayList<Key>());
//        hijo1.getKeys().add(new Key("1"));
//        hijo1.getKeys().add(new Key("2"));
//        hijo1.setN(2);
//
//        Node hijo2 = new Node();
//        hijo2.setIsLeaf(true);
//        hijo2.setKeys(new ArrayList<Key>());
//        hijo2.getKeys().add(new Key("4"));
//        hijo2.getKeys().add(new Key("5"));
//        hijo2.getKeys().add(new Key("6"));
//        hijo2.setN(3);
//
//        root.getChildren().add(hijo1);
//        root.getChildren().add(hijo2);
//
//        System.out.println("Raíz: " + myTree.getRoot() + "\n\nBefore split");
//        for (Node hijo : myTree.getRoot().getChildren()) {
//            System.out.println("Child: " + hijo);
//        }
//
//        Key k = new Key ("10");
//        Key k2 = new Key ("11");
//        Key k3 = new Key ("12");
//        Key k4 = new Key ("20");
//        Key k5 = new Key ("21");
//        Key k6 = new Key ("22");
//        Key k7 = new Key ("17");
//        Key k8 = new Key ("15");
//        Key k9 = new Key ("19");
//        myTree.insert(k);
//        myTree.insert(k2);
//        myTree.insert(k3);
//        myTree.insert(k4);
//        myTree.insert(k5);
//        myTree.insert(k6);
//        myTree.insert(k7);
//        myTree.insert(k8);
//        myTree.insert(k9);
//        System.out.println("Insert");
//        System.out.println("Raíz: " + myTree.getRoot());
//        for (Node hijo : myTree.getRoot().getChildren()) {
//            System.out.println("Child: " + hijo);
//        }
//        System.out.println("delete");
    }
    
    
    
}
