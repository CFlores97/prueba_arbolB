
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
    public void insert(Key k){
        Node root1 = root;
        if (root1.getN()==(grade-1)){
            Node s = new Node();
            root = s;
            s.setIsLeaf(false);
            s.setN(0);
            s.setChild(root1);
            split(s,0,root1);
            insertNonFull(s,k);
        }else{
           insertNonFull(root1,k);
        }
    }

    private void insertNonFull(Node x, Key k) {
        int i = x.getN();
        if (x.isIsLeaf()) {
            while(i>=1 && k.getASCII() < x.getKey(i).getASCII()){
                x.keys.set(i + 1, x.keys.get(i));
                i--;
            }
             x.keys.set(i + 1, k);
             x.setN(x.getN()+1);
        }else{
            while(i>=1 && k.getASCII() < x.getKey(i).getASCII()){
                i--;
            }
            i++;
            if (x.getChild(i).getN()==grade-1) {
                split(x, i, x.getChild(i));
                if (k.getASCII()>x.getKey(i).getASCII()) {
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
        n.setN(n.getN()-1);
    }
}


