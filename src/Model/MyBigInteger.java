
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
        return new MyBigInteger(result.replaceAll("^0+(?!$)", ""));
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
        
        for(int n = 0; n < (num1Array.length + num2Array.length); n++){
            MSD.add(0);
            LSD.add(0);
        }
        
        int subMSD = 0, subLSD = 0;
        for(int j = 0; j < listaParciales.get(0).size(); j++){
            subMSD = 0;
            subLSD = 0;
            for(int k = 0; k < num2Array.length; k++){
                String num = String.valueOf(listaParciales.get(k).get(j));
                num = new StringBuilder(num).reverse().toString();
                try{
                    subMSD += Character.getNumericValue(num.charAt(1));
                }catch(Exception e){ }
                
                subLSD += Character.getNumericValue(num.charAt(0));
            }
            MSD.set(j, subMSD);
            LSD.set(j + 1, subLSD);
        }
        for(int i = MSD.size() - 1; i >= 0; i--){
            subTotal = MSD.get(i) + LSD.get(i) + resto;
            if(subTotal <= 9){
                result = subTotal + result;
                resto = 0;
            }else{
                resto = new Double(subTotal/10).intValue();
                result = (subTotal - (10 * resto)) + result;
            }
        }
        
        if(("-".equals(p1) && !"-".equals(p2)) || (!"-".equals(p1) && "-".equals(p2)))
            result = "-" + result;
        
        result = result.replaceAll("^0+(?!$)", "");
        return new MyBigInteger(result);
    }
    
    public MyBigInteger division(MyBigInteger pNumber){
        return null;
    }
    
    public MyBigInteger pow(MyBigInteger pNumber){
        MyBigInteger result = new MyBigInteger(bigNumber);
        String p1 = bigNumber.substring(0, 1);
        String p2 = pNumber.valueOf().substring(0, 1);
        
        bigNumber = bigNumber.replaceAll("-", "");
        pNumber.setNumero(pNumber.valueOf().replaceAll("-", ""));
        result.setNumero(result.valueOf().replaceAll("-", ""));
        
        while(!"1".equals(pNumber.valueOf())){
            result = result.multiply(new MyBigInteger(bigNumber));
            pNumber = pNumber.sub(new MyBigInteger("1"));
        }
        
        if("-".equals(p2))
            result.setNumero("1/" + result.valueOf());
        if(("-".equals(p1)))
            result.setNumero("-" + result.valueOf());
        
        return result;
    }
    
    public MyBigInteger MCM(MyBigInteger pNumber){
        return new MyBigInteger(new MyBigInteger(String.valueOf(Integer.parseInt(bigNumber) /
                Integer.parseInt(MCD(pNumber).valueOf()))).multiply(pNumber).valueOf());
    }
    
    public MyBigInteger MCD(MyBigInteger pNumber){
        MyBigInteger result = new MyBigInteger(bigNumber);
        if(result.valueOf().compareTo(pNumber.valueOf()) <  0)
            return MCD_aux(result, pNumber);
        else
            return MCD_aux(pNumber, result);
    }
    
    public MyBigInteger MCD_aux(MyBigInteger pNumber1, MyBigInteger pNumber2){
        if("0".equals(pNumber2.valueOf()))
            return pNumber1;
        else
            return MCD_aux(pNumber2, new MyBigInteger(String.valueOf(Integer.parseInt(pNumber1.valueOf()) % Integer.valueOf(pNumber2.valueOf()))));
    }
    
    
    public int mcd( int a, int b) 
    { 
    if ( b==0) 
    return a; 
    else 
    return mcd( b, a%b); 
    } 
    public MyBigInteger fact(){
        MyBigInteger backup = new MyBigInteger(bigNumber);
        MyBigInteger result = new MyBigInteger(bigNumber);
        
        String p1 = bigNumber.substring(0, 1);
        bigNumber = bigNumber.replaceAll("-", "");
        backup.setNumero(backup.valueOf().replaceAll("-", ""));
        result.setNumero(result.valueOf().replaceAll("-", ""));
        
        backup = backup.sub(new MyBigInteger("1"));
        while(!"1".equals(backup.valueOf())){
            result = result.multiply(backup);
            backup = backup.sub(new MyBigInteger("1"));
        }
        
        if(("-".equals(p1)))
            result.setNumero("-" + result.valueOf());
        
        return result;
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
