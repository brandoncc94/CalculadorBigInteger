
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
            if(bigNumber.equals(pNumber.valueOf())) return new MyBigInteger("0");
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
        int resto = 0;
        String result = "";
        
        //Reespaldamos el primer elemento a ver si es un -, para agregarlo al final
        String p1 = bigNumber.substring(0, 1);
        String p2 = pNumber.valueOf().substring(0, 1);
        
        //Mantenemos la inmutabilidad, por lo que se crea un objeto nuevo
        MyBigInteger bigNumber1 = new MyBigInteger(bigNumber);
        MyBigInteger bigNumber2 = new MyBigInteger(pNumber.valueOf());
        
        //Quitamos el negativo
        bigNumber2.setNumero(bigNumber2.valueOf().replaceAll("-", ""));
        bigNumber1.setNumero(bigNumber1.valueOf().replaceAll("-", ""));
        
        bigNumber1.setNumero(new StringBuilder(bigNumber1.valueOf()).reverse().toString());
        bigNumber2.setNumero(new StringBuilder(bigNumber2.valueOf()).reverse().toString());
            
        // Convertimos el string de los números a un arreglo de char's
        char[] num1Array = bigNumber1.valueOf().toCharArray();
        char[] num2Array = bigNumber2.valueOf().toCharArray();
        
        //Creamos arreglos para mantener los números parciles
        ArrayList<Integer> parciales = new ArrayList<>();
        ArrayList<ArrayList<Integer>> listaParciales = new ArrayList<>();
        
        //Arreglos para guardar los números más y menos significativos
        ArrayList<Integer> MSD = new ArrayList<>();
        ArrayList<Integer> LSD = new ArrayList<>();
        
        int subTotal, cont;
        
        //Agrupamos en "cajas" los resultados parciales y los vamos guardando
        //en la lista de parciales
        for(int k = 0; k < num2Array.length; k++){
            cont = k;
            for(int n = 0; n < ((num1Array.length + num2Array.length) - 1); n++)
                parciales.add(0);
            
            for(int j = 0; j < ((num1Array.length + num2Array.length) - 1); j++){
                if(j - k < num1Array.length){
                    if(cont == 0)
                        parciales.set(j, Character.getNumericValue(num1Array[j - k]) * Character.getNumericValue(num2Array[k]));
                    else
                        cont--;
                }
            }
            Collections.reverse(parciales);
            listaParciales.add(parciales);
            parciales = new ArrayList<>();
        }
        
        //Inicializamos los arreglos del MSD y LSD
        for(int n = 0; n < (num1Array.length + num2Array.length); n++){
            MSD.add(0);
            LSD.add(0);
        }
        
        int subMSD = 0, subLSD = 0;
        
        //Finalmente se procede a sumar las columnas de los números y acomodarlos
        //en su MSD, o LSD respectivamente
        for(int j = 0; j < listaParciales.get(0).size(); j++){
            subMSD = 0;
            subLSD = 0;
            for(int k = 0; k < num2Array.length; k++){
                String num = String.valueOf(listaParciales.get(k).get(j));
                num = new StringBuilder(num).reverse().toString();
                if(num.length() > 1)
                    subMSD += Character.getNumericValue(num.charAt(1));
                subLSD += Character.getNumericValue(num.charAt(0));
            }
            MSD.set(j, subMSD);
            LSD.set(j + 1, subLSD);
        }
        
        //Una vez listos los arreglos MSD y LSD se procede a sumar y retornar la multiplicación
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
        
        //Quitar ceros al inicio
        result = result.replaceAll("^0+(?!$)", "");
        
        //Si el número era negativo se le coloca el menos al inicio
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
        String zero = "0";
        MyBigInteger dividendo = new MyBigInteger(bigNumber);
        MyBigInteger divisor = new MyBigInteger(pNumber.valueOf());
        
        if("-".equals(p2)){
            flag1=true;
            divisor = divisor.abs();
            
        }
        if("-".equals(p1)){
            flag2=true;
            dividendo = dividendo.abs();
            
        }
        int num1Len = dividendo.valueOf().length();
        int num2Len = divisor.valueOf().length();
        
        int maxDigits = num1Len-num2Len;
         
        if(divisor.valueOf().equals("0")){
            System.out.println("Division by zero");
            return null;
        }
        if(!dividendo.higher(divisor.valueOf())){
            if(pFlag)
                return this;
            return new MyBigInteger("0");
        }
        
        
        String q1 = "1";
        int i = 0;
        
        while(i < maxDigits){
            q1+=zero;
            i++;
        }
        while(true){
            if(!new MyBigInteger(dividendo.valueOf()).higher(new MyBigInteger(divisor.valueOf()).multiply(new MyBigInteger(q1)).valueOf())){
                String qAux;
                qAux=q1.substring(0,q1.length()-1);
                q1=qAux;
            }
            else{
                break;
            }
        }
        int pos= 0;
        int maxPos = q1.length();
        while(pos<maxPos){
            String qAux = q1;
            if((Integer.parseInt(qAux.substring(pos,pos+1)))==9){
                pos++;continue;
            }
            qAux = qAux.substring(0,pos)+(Integer.parseInt(qAux.substring(pos,pos+1))+1)+qAux.substring(pos+1,maxPos);
            if(dividendo.higher(new MyBigInteger(divisor.valueOf()).multiply(new MyBigInteger(qAux)).valueOf())){
                q1=qAux;
            }
            else{
                pos++;
            }
        }
        MyBigInteger q = new MyBigInteger(q1);
        MyBigInteger r = new MyBigInteger(dividendo.valueOf()).sub(new MyBigInteger(q1).multiply(new MyBigInteger(divisor.valueOf())));
        
        if(pFlag){
            if("0".equals(r.valueOf())){
                return r;
            }
            if(flag2){
                return new MyBigInteger("-"+r.valueOf());
            }
            return r;
        }
        else{
            if(!flag1 && !flag2){
                return q;
            }
            if(flag1){
                if(flag2){
                    return q;
                }
                return new MyBigInteger("-"+q.valueOf());
            }
            return new MyBigInteger("-"+q.valueOf());            
            
        }
    }
    
    //Potencia de un número
    public MyBigInteger pow(MyBigInteger pNumber){
        //Matenemos la integridad de los bigInteger, inmutabilidad
        MyBigInteger result = new MyBigInteger(bigNumber);
        MyBigInteger cont = new MyBigInteger(pNumber.valueOf());
        
        //Guardamos el primer carácter para verificar si es un - al final
        String p1 = result.valueOf().substring(0, 1);
        String p2 = cont.valueOf().substring(0, 1);
        
        //Reemplazamos los - y los quitamos, para así trabajar con los números
        result.setNumero(result.valueOf().replaceAll("-", ""));
        cont.setNumero(cont.valueOf().replaceAll("-", ""));
        
        MyBigInteger backup = new MyBigInteger(result.valueOf());
        
        //Mientras que sea distinto de uno, elevamos
        while(!"1".equals(cont.valueOf())){
            //Multiplicamos el resultado
            result = result.multiply(backup);
            cont = cont.sub(new MyBigInteger("1"));
        }
        //Si el número a elevar es negativo, entonces se procede al recíproco
        if("-".equals(p2))
            result.setNumero("1/" + result.valueOf());
        //Sí el primer número tiene un negativo se le restaura de nuevo al resultado final
        if(("-".equals(p1)))
            result.setNumero("-" + result.valueOf());
        
        //Retornamos el BigInteger nuevo con el resultado
        return result;
    }
    
    //Obtener el mímimo común mútiplo
    public MyBigInteger MCM(MyBigInteger pNumber){
        return new MyBigInteger(new MyBigInteger(bigNumber).division(MCD(pNumber), 
                        false).multiply(pNumber).valueOf());
    }
    
    //Obtener el máximo común divisor
    public MyBigInteger MCD(MyBigInteger pNumber){
        MyBigInteger result = new MyBigInteger(bigNumber);
        if(pNumber.higher(result.valueOf()))
            return MCD_aux(result, new MyBigInteger(pNumber.valueOf()));
        else
            return MCD_aux(new MyBigInteger(pNumber.valueOf()), result);
    }
    
    //Función MCD auxiliar recursiva
    public MyBigInteger MCD_aux(MyBigInteger pNumber1, MyBigInteger pNumber2){
        if("0".equals(pNumber2.valueOf()))
            return pNumber1;
        else
            return MCD_aux(pNumber2, pNumber1.division(pNumber2, true));
    }
    
    //Función de fibonacci
    public MyBigInteger fibo(){
        if(bigNumber.equals("0")){
            return this;
        }
        if(!higher("0")){
            return null;
        }
        MyBigInteger one =  new MyBigInteger("1");
        MyBigInteger I = new MyBigInteger("1");
        MyBigInteger P = new MyBigInteger("0");
        MyBigInteger U = new MyBigInteger("1");
        while(!I.higher(bigNumber)){
            I = I.add(one);
            MyBigInteger tmp = P.add(U);
            P=U;
            U=tmp;
        }
        return U;
    }
    
    //Funcion de phi
    public MyBigInteger phi(){
        if("1".equals(bigNumber)){
            return this;
        }
        MyBigInteger n = new MyBigInteger(bigNumber);
        MyBigInteger tot = new MyBigInteger(bigNumber);
        MyBigInteger p = new MyBigInteger("2");
        MyBigInteger one = new MyBigInteger("1");
        while(!new MyBigInteger(p.valueOf()).multiply(new MyBigInteger(p.valueOf())).higher(n.valueOf()) 
                || new MyBigInteger(p.valueOf()).multiply(new MyBigInteger(p.valueOf())).valueOf().equals(n.valueOf())){
            if(n.division(p, true).valueOf().equals("0")){
                tot = tot.division(p,false);
                tot = tot.multiply(p.sub(one));
                while(n.division(p, true).valueOf().equals("0"))
                    n = n.division(p,false);
            }
            p = p.add(one);
        }
        if(!one.higher(n.valueOf())){
            tot = tot.division(n,false);
            tot = tot.multiply(n.sub(one));
        }
        return tot;
    }

    //Función de factorización prima única
    public MyBigInteger getPrimeFact(){
        MyBigInteger number = new MyBigInteger(bigNumber).abs();
        MyBigInteger result = new MyBigInteger("");
        MyBigInteger cont = new MyBigInteger("2");
        
        MyBigInteger mod;
        while(!number.valueOf().equals("1")){
            mod = number.division(cont, true);
            if(isPrimo(cont) && mod.valueOf().equals("0")){
                result.setNumero(result.valueOf() + " * " + cont.valueOf());
                number = number.division(cont, false);
            }
            else
                cont = cont.add(new MyBigInteger("1"));
        }
        
        result.setNumero(result.valueOf().replaceFirst("\\*", ""));
        return result;
    }
    
    //Función para saber la cantidad de factores de un número
    public MyBigInteger getFactors(){
        MyBigInteger result = getPrimeFact();
        result.setNumero(result.valueOf().replaceAll(" * ", ""));
        
        String numero = result.valueOf();
        String backup = "";
        ArrayList<Integer> cantFact = new ArrayList<>();
        
        String[] numArray = numero.split("\\*");
        boolean flag = true;
        for(int i = 0; i < numArray.length; i++){
            if(i == 0){
                backup = numArray[i];
                cantFact.add(1);
            }
            if(backup.equals(numArray[i]))
                flag = false;
            else{
                flag = true;
                cantFact.add(1);
            }
            cantFact.set(cantFact.size() - 1, cantFact.get(cantFact.size() - 1) + 1);
            backup = numArray[i];
        }
        
        result.setNumero("1");
        for (Integer factor : cantFact) {
            result = result.multiply(new MyBigInteger(String.valueOf(factor)));
        }
        
        return result;
    }
    
    //Función para saber si un número es primo o no
    public boolean isPrimo(MyBigInteger pNumber){
        MyBigInteger number = new MyBigInteger(pNumber.valueOf()).abs();
        
        if (number.valueOf().equals("2"))
            return true;   
        
        MyBigInteger backup = number.division(new MyBigInteger("2"), false);
        MyBigInteger cont = new MyBigInteger("3");
        while (backup.higher(cont.valueOf())){
            if (number.division(new MyBigInteger("2"), true).valueOf().equals("0")) 
                return false;
            cont = cont.add(new MyBigInteger("2"));
        }
        return true;
    }
    
    //Obtener el factorial de un número
    public MyBigInteger fact(){
        //Respaldamos el número para mantener la inmutabilidad
        MyBigInteger backup = new MyBigInteger(bigNumber);
        MyBigInteger result = new MyBigInteger(bigNumber);
        
        //Guardamos el primer carácter para verficiar al final si hay que agregarle un -
        String p1 = bigNumber.substring(0, 1);
        backup.setNumero(backup.valueOf().replaceAll("-", ""));
        result.setNumero(result.valueOf().replaceAll("-", ""));
        
        //Le restamos uno al número de backup
        backup = backup.sub(new MyBigInteger("1"));
        
        //Mientras sea diferente de 1, multiplicamos el resultado por el resultado - 1 (backup)
        while(!"1".equals(backup.valueOf())){
            result = result.multiply(backup);
            backup = backup.sub(new MyBigInteger("1"));
        }
        
        //Agregamos un menos de ser necesario
        if(("-".equals(p1)))
            result.setNumero("-" + result.valueOf());
        
        //Retornamos el nuevo número
        return result;
    }
    
    //Función para averiguar Goldbach
    public MyBigInteger getGoldBach(){
        MyBigInteger backup = new MyBigInteger(bigNumber);
        MyBigInteger backup2 = new MyBigInteger(bigNumber);
        
        if(isPrimo(backup))
            return backup;
        // Contar los primos
        int primes = 0;
        while(!backup.valueOf().equals("0")){
            if(isPrimo(backup))
                primes++;
            backup = backup.sub(new MyBigInteger("1"));
        }
         
        // Almacenar los primos en una lista
        ArrayList<MyBigInteger> primesArray = new ArrayList<>();
        int n = 0;
        MyBigInteger cont = new MyBigInteger("1");
        while(!backup2.valueOf().equals(cont.valueOf())){
            if (isPrimo(cont)){
                primesArray.add(new MyBigInteger(cont.valueOf()));
            }
            cont = cont.add(new MyBigInteger("1"));
        }
        
        MyBigInteger left = new MyBigInteger("0");
        MyBigInteger right = new MyBigInteger(String.valueOf(primes - 1));
        String sum = "";
        
        while (true) {
            if(left.valueOf().equals(right.valueOf())){
                left = left.add(new MyBigInteger("1"));
                if(left.valueOf().equals(String.valueOf(primes - 1)))
                    break;
                right.setNumero(String.valueOf(primes - 1));
            }
            
            sum = primesArray.get(Integer.parseInt(left.valueOf())).add(
                       primesArray.get(Integer.parseInt(right.valueOf()))).valueOf();
            if(sum.equals(backup2.valueOf())){
                return new MyBigInteger(String.valueOf(primesArray.get(Integer.parseInt(left.valueOf())).valueOf() 
                        + " y " + primesArray.get(Integer.parseInt(right.valueOf())).valueOf()));
            }
            else if(backup.higher(sum)) left = left.add(new MyBigInteger("1"));
            else right = right.sub(new MyBigInteger("1"));
        }
        
        return backup2;
    }
    
    //Función de valor absoluto
    public MyBigInteger abs(){
        String backup = bigNumber;
        return new MyBigInteger(backup.replaceAll("-", ""));
    }
    
    public boolean higher(String pNumber2){
        int num1Len = bigNumber.length();
        int num2Len = pNumber2.length();
        int i = 0;
        if(bigNumber.equals(pNumber2))return true;
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