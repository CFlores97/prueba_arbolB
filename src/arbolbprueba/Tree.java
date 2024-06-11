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
    
    public void delete(Node x, Key k) {
        int t = ((this.grade - 1) / 2);
        int i = 0;
        
        while (i < x.getKeys().size() && (k.hashCode() > x.getKey(i).hashCode())) {
            i++;
        }
        if (x.isIsLeaf()) {
            if (i < x.getN() && (k.hashCode() == x.getKey(i).hashCode())) {
                x.deleteKey(i);
            }
            return;
        }
        
        if (i < x.getKeys().size() && (k.hashCode() == x.getKey(i).hashCode())) {
            deleteInternalNode(x, k, i);
        } else if (x.getChild(i).getKeys().size() >= t) {
            delete(x.getChild(i), k);
        } else {
            if (i != 0 && (i + 2 < x.getChildren().size())) {
                if (x.getChild(i - 1).getKeys().size() >= t) {
                    deleteSibling(x, i, i - 1);
                } else if (x.getChild(i + 1).getKeys().size() >= t) {
                    deleteSibling(x, i, i + 1);
                } else {
                    deleteMerge(x, i, i + 1);
                }
            } else if (i == 0) {
                if (x.getChild(i + 1).getKeys().size() >= t) {
                    deleteSibling(x, i, i + 1);
                } else {
                    deleteMerge(x, i, i + 1);
                }
            } else if (i + 1 == x.getChildren().size()) {
                if (x.getChild(i - 1).getKeys().size() >= t) {
                    deleteSibling(x, i, i - 1);
                } else {
                    deleteMerge(x, i, i - 1);
                }
            }
            delete(x.getChild(i), k);
        }
    }
    
    public void deleteInternalNode(Node x, Key k, int i) {
        int t = ((this.grade - 1) / 2);
        if (x.isIsLeaf()) {
            if (x.getKey(i).equals(k)) {
                x.deleteKey(i);
            }
            return;
        }
        
        if (x.getChild(i).getKeys().size() >= t) {
            x.replacekey(i, deletePredecessor(x.getChild(i)));
            return;
        } else if (x.getChild(i + 1).getKeys().size() >= t) {
            x.replacekey(i, deleteSuccessor(x.getChild(i + 1)));
            return;
        } else {
            deleteMerge(x, i, i + 1);
            deleteInternalNode(x.getChild(i), k, t - 1);
        }
    }
    
    public Key deletePredecessor(Node x) {
        if (x.isIsLeaf()) {
            Key r = x.getKey(x.getKeys().size() - 1);
            x.deleteKey(x.getKeys().size() - 1);
            return r;            
        }
        int n = x.getKeys().size() - 1;
        if (x.getChild(n).getKeys().size() >= grade) {
            deleteSibling(x, n + 1, n);
        } else {
            deleteMerge(x, n, n + 1);
        }
        return deletePredecessor(x.getChild(n));
    }
    
    public Key deleteSuccessor(Node x) {
        if (x.isIsLeaf()) {
            Key r = x.getKey(0);
            x.deleteKey(0);
            return r;
        }
        if (x.getChild(1).getKeys().size() >= grade) {
            deleteSibling(x, 0, 1);
        } else {
            deleteMerge(x, 0, 1);
        }
        return deleteSuccessor(x.getChild(0));
    }
    
    public void deleteMerge(Node x, int i, int j) {
        Node cnode = x.getChild(i);
        
        if (j > i) {
            Node rsnode = x.getChild(j);
            cnode.setKey(x.getKey(i));
            for (int k = 0; k < rsnode.getKeys().size(); k++) {
                cnode.setKey(rsnode.getKey(k));
                if (rsnode.getChildren().size() > 0) {
                    cnode.setChild(rsnode.getChild(k));
                }
            }
            if (rsnode.getChildren().size() > 0) {
                cnode.setChild(rsnode.getChild(rsnode.getChildren().size() - 1));
                rsnode.deleteChild(rsnode.getChildren().size() - 1);
            }
            Node newChild = cnode;
            x.deleteKey(i);
            x.deleteChild(j);
        } else {
            Node lsnode = x.getChild(j);
            lsnode.setKey(x.getKey(j));
            for (int l = 0; l < cnode.getKeys().size(); l++) {
                lsnode.setKey(cnode.getKey(i));
                if (lsnode.getChildren().size() > 0) {
                    lsnode.setChild(cnode.getChild(i));
                }
            }
            if (lsnode.getChildren().size() > 0) {
                lsnode.setChild(cnode.getChild(cnode.getChildren().size() - 1));
                cnode.deleteChild(cnode.getChildren().size() - 1);
            }
            Node newChild = lsnode;
            x.deleteKey(j);
            x.deleteChild(i);
        }
        
        if (x.equals(root) && (x.getKeys().size() == 0)) {
            Node newChild = new Node();
            root = newChild;
        }
    }
    
    public void deleteSibling(Node x, int i, int j) {
        Node cNode = x.getChild(i);
        if (i < j) {
            Node rsNode = x.getChild(j);
            cNode.setKey(x.getKey(i));
            x.replacekey(i, rsNode.getKey(0));
            if (rsNode.getChildren().size() > 0) {
                cNode.setChild(rsNode.getChild(0));
                rsNode.deleteChild(0);
            }
            rsNode.deleteKey(0);
        } else {
            Node lsNode = x.getChild(j);
            cNode.insertKey(0, x.getKey(i - 1));
            x.replacekey(i - 1, lsNode.getKey(lsNode.getKeys().size() - 1));
            if (lsNode.getChildren().size() > 0) {
                cNode.setChild(0, lsNode.getChild(lsNode.getChildren().size() - 1));
            }
        }
    }
    
    public void printTree(Node x, int level) {
        System.out.print("Level " + level + ": ");
        
        for (Key key : x.getKeys()) {
            System.out.print(key + " ");
        }
        
        System.out.println();
        level++;
        
        if (!x.getChildren().isEmpty()) {
            for (Node child : x.getChildren()) {
                printTree(child, level);
            }
        }
    }
    
}
