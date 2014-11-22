
package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MyBigInteger {
    private String bigNumber;
    private static int subOperation;

    public MyBigInteger(String numero) {
        this.bigNumber = numero;
        MyBigInteger.subOperation = 0;
    }

    public String valueOf() {
        return bigNumber;
    }

    public void setNumero(String numero) {
        this.bigNumber = numero;
    }
    
    
    //Función de sumar
    public MyBigInteger add(MyBigInteger pNumber){
        int carry = 0;
        String result = "";
        String pNumber2 = pNumber.valueOf();
        
        ArrayList<String> fixedNumbers = addZeros(pNumber2);
        bigNumber = fixedNumbers.get(0);
        pNumber2 = fixedNumbers.get(1);
        
        // Convertimos el string a un arreglo de char's
        char[] num1Array = bigNumber.toCharArray();
        char[] num2Array = pNumber2.toCharArray();
        int subTotal;
        
        for(int i = num1Array.length - 1; i >= 0; i--){
            subTotal = (num1Array[i] - '0') + (num2Array[i] + carry);
            if(subTotal <= '9'){
                result= (char) subTotal +result;	
                carry = 0;
            }
            else{		
                result = (char) (subTotal - 10) + result;
                carry = 1;
            }
        }
        
        //Si al final sobra 1 en el carry, lo concatenamos al total final
        if(carry == 1)
            result= "1" + result;
        if(MyBigInteger.subOperation == 2)
            result = "-" + result;
        
        MyBigInteger.subOperation = 1;
        return new MyBigInteger(result);
    }
    
    //Función de restar
    public MyBigInteger sub(MyBigInteger pNumber){
        String p1 = bigNumber.substring(0, 1);
        String p2 = pNumber.valueOf().substring(0, 1);
        
        if(("-".equals(p1) && !"-".equals(p2))
                || (!"-".equals(p1) && "-".equals(p2))){
            if("-".equals(p2))
                MyBigInteger.subOperation = 1;
            else
                MyBigInteger.subOperation = 2;
            bigNumber = bigNumber.replaceAll("-", "");
            pNumber.setNumero(pNumber.valueOf().replaceAll("-", ""));
            return add(pNumber);
        }
        
        int diff = 0;
        String result = "";
        String pNumber2 = pNumber.valueOf();

        ArrayList<String> fixedNumbers = addZeros(pNumber2);
        bigNumber = fixedNumbers.get(0);
        pNumber2 = fixedNumbers.get(1);

        // Convertimos el string a un arreglo de char's
        char[] num1Array = bigNumber.toCharArray();
        char[] num2Array = pNumber2.toCharArray();
        int subTotal;

        for(int i = num1Array.length - 1; i >= 0; i--){
            subTotal = (num1Array[i] - '0' - diff) - (num2Array[i] - '0');
            if(subTotal >= 0){
                result = subTotal + result;
                diff = 0;
            }
            else{
                result = 10 + subTotal + result;
                diff = 1;
            }   
        }
        MyBigInteger.subOperation = 2;
        return new MyBigInteger(result);
    }
    
    //Función de multiplicar a medias
    public MyBigInteger multiply(MyBigInteger pNumber){
        String result = "";
        String p1 = bigNumber.substring(0, 1);
        String p2 = pNumber.valueOf().substring(0, 1);
        
        bigNumber = bigNumber.replaceAll("-", "");
        pNumber.setNumero(pNumber.valueOf().replaceAll("-", ""));
         
        int resto = 0;
        bigNumber = new StringBuilder(bigNumber).reverse().toString();
        String pNumber2 = new StringBuilder(pNumber.valueOf()).reverse().toString();
            
        // Convertimos el string a un arreglo de char's
        char[] num1Array = bigNumber.toCharArray();
        char[] num2Array = pNumber2.toCharArray();
        ArrayList<Integer> parciales = new ArrayList<>();
        ArrayList<ArrayList<Integer>> listaParciales = new ArrayList<>();
        ArrayList<Integer> MSD = new ArrayList<>();
        ArrayList<Integer> LSD = new ArrayList<>();
        int subTotal, cont;
        
        for(int k = 0; k < num2Array.length; k++){
            cont = k;
            for(int n = 0; n < ((num1Array.length + num2Array.length) - 1); n++)
                parciales.add(0);
            
            for(int j = 0; j < ((num1Array.length + num2Array.length) - 1); j++){
                try{
                    if(cont == 0)
                        parciales.set(j, Character.getNumericValue(num1Array[j - k]) * Character.getNumericValue(num2Array[k]));
                    else
                        cont--;
                }catch(Exception e){ 
                }
            }
            Collections.reverse(parciales);
            listaParciales.add(parciales);
            parciales = new ArrayList<>();
        }
        
        System.out.println(listaParciales);
        String primero = String.valueOf(listaParciales.get(0).get(listaParciales.get(0).size() - 1));
        primero = new StringBuilder(primero).reverse().toString();
        
        for(int n = 0; n < ((num1Array.length + num2Array.length) - 1); n++){
            MSD.add(0);
            LSD.add(0);
        }
        System.out.println(MSD);

        if(primero.length() > 1)
            MSD.set((MSD.size() - 2), Character.getNumericValue(primero.charAt(1)));
        
        LSD.set(LSD.size() - 1, Character.getNumericValue(primero.charAt(0)));
        
        System.out.println(MSD);
        System.out.println(LSD);
        System.out.println("fin");
        
        
        for(int i = num1Array.length - 1; i >= 0; i--){
            subTotal = (num1Array[i] - '0') * (num2Array[i] + resto);
            if(subTotal <= '9'){
                System.out.println(subTotal);
                result= (char) subTotal +result;	
                resto = 0;
            }
            else{		
                System.out.println(Integer.parseInt(Character.toString(String.valueOf(subTotal).charAt(0))));
                result = (char) (subTotal - (10 * Integer.parseInt(Character.toString(String.valueOf(subTotal).charAt(0))))) + result;
                resto = Integer.parseInt(Character.toString(String.valueOf(subTotal).charAt(0)));
            }
        }
        
        if(("-".equals(p1) && !"-".equals(p2)) || (!"-".equals(p1) && "-".equals(p2)))
            result = "-" + result;
        
        return new MyBigInteger(result);
    }
    
    //Función de valor absoluto
    public MyBigInteger abs(){
        String result = "";
        char[] num1Array = this.bigNumber.toCharArray();
        if(num1Array[0] == '-')
            num1Array = Arrays.copyOfRange(num1Array, 1, num1Array.length);
            
        for(int i = num1Array.length - 1; i >= 0; i--)
            result = num1Array[i] - '0' + result;
        
        return new MyBigInteger(result);
    }
    
    public ArrayList<String> addZeros(String pNumber2){
        int cantidadZeros;
        String zero = "0";
        int num1Len = bigNumber.length();
        int num2Len = pNumber2.length();
        boolean addMinus1 = false, addMinus2 = false;
        ArrayList<String> tmpArray  = new ArrayList<>();
        
        if("-".equals(bigNumber.substring(0, 1))){
            bigNumber = bigNumber.replaceAll("-", "");
            addMinus1 = true;
        }
        if("-".equals(pNumber2.substring(0, 1))){
            pNumber2 = pNumber2.replaceAll("-", "");
            addMinus2 = true;
        }
        
        if(num1Len != num2Len){
            if(num1Len > num2Len){
                cantidadZeros = num1Len - num2Len;
                //Concatenamos los ceros extra a la izquierda
                for(int i = 1; i<=cantidadZeros;i++)
                    pNumber2 = zero + pNumber2;
            }
            else{
                cantidadZeros = num2Len - num1Len;
                //Concatenamos los ceros extra a la izquierda
                for(int i = 1; i<=cantidadZeros;i++)
                    bigNumber = zero + bigNumber;
            }
            if(addMinus1)
                bigNumber = "-" + bigNumber;
            if(addMinus2)
                pNumber2 = "-" + pNumber2;
        }
        
        tmpArray.add(bigNumber);
        tmpArray.add(pNumber2);
        return tmpArray;
    }
}
