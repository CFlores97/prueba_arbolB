
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
    
    public Node createBTree(){
        Node x = new Node();
        x.setIsLeaf(true);
        x.setN(0);
        return x;
        
    }
    
    //buscar
    public Node search(Node root, Key k){
        int i = 0;
        
        while (i <= root.getN() && k.getKeyValue().compareTo(root.getKey(i).getKeyValue()) > 0){
            i++;
        }
        
        if(i <= root.getN() && k.getKeyValue().compareTo(root.getKey(i).getKeyValue()) == 0){
            return root;
        }
        
        if (root.isIsLeaf()){
            return null;
        }
                
        return search(root.getChild(i), k);
    }
    
    //split 
    
    public void split(Node x, int i, Node y){
        Node z = new Node();
        z.setIsLeaf(y.isIsLeaf());
        
        int minKeys = (grade - 1) / 2;
        int gradoMin = grade/2;
        
        z.setN(minKeys);
        
        for (int j = 0; j < minKeys; j++) {
            z.getKeys().add(y.getKey(j + gradoMin));
        }
        
        if(!y.isIsLeaf()){
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
}


