
package arbolbprueba;


public class Key {
    private String keyValue;

    public Key(String keyValue) {
        this.keyValue = keyValue;
    }

    public Key() {
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
    public int getASCII(){
        int sum = 0;
        for (int i = 0; i < keyValue.length(); i++) {
            char character = keyValue.charAt(i);
            int asciiValue = (int) character; // Convierte el carácter a su valor ASCII
            sum += asciiValue; // Suma el valor ASCII al total
        }    
        return sum; // Devuelve la suma total
    }
    public boolean compare(Key k1,Key k2){
        boolean iguales=false;
        if (k1.getASCII()==k2.getASCII()) {
            if (k1.getKeyValue().equals(k2.getKeyValue())) {
                iguales= true;
            }
        }
        return iguales;
    }
}
