
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
        
        if(pNumber.higher(bigNumber)){
            MyBigInteger nc9 = this.complement9(pNumber.valueOf().length());
            String result = nc9.add(pNumber).valueOf();
            return new MyBigInteger("-"+result.substring(1));
        }
        
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
        int maximo = Math.max(num1Array.length, num2Array.length);
        int subMSD = 0, subLSD = 0;
        for(int j = 0; j < listaParciales.get(0).size(); j++){
            subMSD = 0;
            subLSD = 0;
            for(int k = 0; k < maximo; k++){
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
        
        return new MyBigInteger(result);
    }
    
    
    public MyBigInteger division(MyBigInteger pNumber,boolean pFlag){
        boolean flag1 = false;
        boolean flag2 = false;
        String result = "";
        String p1 = bigNumber.substring(0, 1);
        String p2 = pNumber.valueOf().substring(0, 1);
        
        MyBigInteger dividendo = new MyBigInteger(bigNumber);
        MyBigInteger divisor = new MyBigInteger(pNumber.valueOf());
        if(pNumber.valueOf().equals("0")){
            System.out.println("Division by zero");
            return null;
        }
        if("-".equals(p2)){
            flag1=true;
            divisor = divisor.abs();
        }
        if("-".equals(p1)){
            flag2=true;
            dividendo = dividendo.abs();
        }
        MyBigInteger q = new MyBigInteger("0");
        MyBigInteger r = new MyBigInteger(dividendo.valueOf());
        while(!divisor.higher(r.valueOf())){
            q = q.add(new MyBigInteger("1"));
            r = r.sub(divisor);
        }
        if(pFlag){
            System.out.println("enter");
            if("0".equals(r.valueOf())){
                return r;
            }
            if(flag1){
                if(flag2){
                    return divisor.sub(r);
                }
                else{
                    return r;
                }
            }
            if(flag2){
                return divisor.sub(r);
            }
            return r;
        }
        else{
            if(!flag1 && !flag2){
                return q;
            }
            if(flag1){
                if(flag2){
                    if("0".equals(r.valueOf())){
                        return q;
                    }else{
                        return new MyBigInteger("-"+q.valueOf()).sub(new MyBigInteger("-1")).abs();
                    }
                }
                return new MyBigInteger("-"+q.valueOf());
            }
            else{
                if("0".equals(r.valueOf())){
                    return new MyBigInteger("-"+q.valueOf());
                }else{
                    return new MyBigInteger("-"+q.valueOf()).sub(new MyBigInteger("-1"));
                }
            }
            
        }
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
    
    public boolean higher(String pNumber2){
        int num1Len = bigNumber.length();
        int num2Len = pNumber2.length();
        int i = 0;
        if("-".equals(bigNumber.substring(0, 1))){
            if(!"-".equals(pNumber2.substring(0,1))){
                return false;
            }
            else{
                i=1;
            }
        }
        if("-".equals(pNumber2.substring(0,1))){
            return true;
        }
        if(num1Len!=num2Len){
            if(num1Len>num2Len)
                return true;
            else
                return false;
        }
        while (i<num1Len) {
            if(Integer.parseInt(bigNumber.substring(i,i+1)) < Integer.parseInt(pNumber2.substring(i,i+1)))
                return false;
            if(Integer.parseInt(bigNumber.substring(i,i+1)) > Integer.parseInt(pNumber2.substring(i,i+1)))
                return true;
            i++;
        }
        return false;
        
    }
    
    public MyBigInteger complement9(int c){
        int i=0;
        String numC9="1";
        String zero = "0";
        while(i<c){
            numC9+=zero;
            i++;
        }
        MyBigInteger com9Add= new MyBigInteger(numC9);
        MyBigInteger numberC9 = com9Add.sub(this);
        return numberC9;
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
