
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
    
    public void executeAdd(){
        MyBigInteger number1 = new MyBigInteger(this.total);
        MyBigInteger number2 = new MyBigInteger(this.currentNumber);
        MyBigInteger result = number1.add(number2);
        this.total = result.valueOf();
        this.currentNumber = "";
    }
    
    public void executeSub(){
        MyBigInteger number1 = new MyBigInteger(this.total);
        MyBigInteger number2 = new MyBigInteger(this.currentNumber);
        MyBigInteger result = number1.sub(number2);
        this.total = result.valueOf();
        this.currentNumber = "";
    }
}
