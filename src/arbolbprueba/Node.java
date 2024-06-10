
package arbolbprueba;

import java.util.ArrayList;


public class Node {
    private int n; //number of keys
    ArrayList<Key> keys = new ArrayList<>(); //keys stored in nondrecreasing order
    private boolean isLeaf;
    private ArrayList<Node> children = new ArrayList<>();

    public Node() {
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public ArrayList<Key> getKeys() {
        return keys;
    }
    
    public Key getKey(int i){
        return keys.get(i);
    }
    
    public void setKey(Key key){
        this.keys.add(key);
    }

    public void setKeys(ArrayList<Key> keys) {
        this.keys = keys;
    }

    public boolean isIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public Node getChild(int i){
        return children.get(i);
    }
    
    public void setChild(Node child){
        this.children.add(child);
    }
    public void insertKey(int pos,Key k){
        keys.add(pos, k);
    }
    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }
     public void setChild(int index, Node child) {
        if (index >= 0 && index < children.size()) {
            children.set(index, child);
        } else if (index == children.size()) {
            children.add(child);
        }
    }
    @Override
    public String toString() {
        return "Node{" +
      
                "keys=" + keys +
                
                ", children=" + children +
                '}';
    }
    
    
}
