


package Model;

import Controller.MainController;

public class DataInput {
    private MainController controller;
    private String currentNumber;
    private String total;

    public DataInput(MainController pControllerReference){
        this.controller = pControllerReference;    
        this.currentNumber = "";
        this.total = "";
    }
    
    public DataInput(String number) {
        this.currentNumber = number;
        this.total = "";
    }

    public String getNumber() {
        return currentNumber;
    }
    
    public String getTotal() {
        return total;
    }
    
    public void setNumber(String number) {
        this.currentNumber = this.getNumber() + number;
        controller.showResult(this.getNumber());
    }
    
    public void setTotal(String total) {
        this.total = total;
    }    
    
    public void backupInformation(){
        this.setTotal(this.currentNumber);
        this.currentNumber = "";
    }
    
    public void resetDefaults(){
        this.currentNumber = "";
    }
    
    public MyBigInteger applyResult(MyBigInteger pNumber1, MyBigInteger pNumber2, String pType){
        MyBigInteger result = null;
        try{
            switch(pType){
                case "+":
                    result = pNumber1.add(pNumber2);
                    break;
                case "-":
                    result = pNumber1.sub(pNumber2);
                    break;
                case "*":
                    result = pNumber1.multiply(pNumber2);
                    break;
                case "/":
                    result = pNumber1.division(pNumber2,false);
                    break;
                case "mod":
                    result = pNumber1.division(pNumber2,true);
                    break;
                case "^":
                    result = pNumber1.pow(pNumber2);
                    break;
                case "MCD":
                    result = pNumber1.MCD(pNumber2);
                    break;
                case "MCM":
                    result = pNumber1.MCM(pNumber2);
                    break;
                case "!":
                    result = pNumber2.fact();
                    break;
                case "!prima":
                    result = pNumber2.factPrimaUnica();
                    break;
                case "fibo":
                    result = pNumber2.fibo();
                    break;
                case "phi":
                    result = pNumber2.phi();
                    break;
                case "abs":
                    result = pNumber2.abs();
                    break;
                  
            }
        }catch(Exception e){
            System.out.println("Error " + e.getMessage());
        }
        return result;
    }
    
    public void executeOperation(String pType){
        MyBigInteger number1 = new MyBigInteger(this.total);
        MyBigInteger number2 = new MyBigInteger(this.currentNumber);
        MyBigInteger result = applyResult(number1, number2, pType);
        this.total = result.valueOf();
        this.total = this.total.replaceAll("^0+(?!$)", "");
        this.currentNumber = "";
    }
}
