
package Controller;

import Model.DataInput;
import View.FrmCalculator;

public class MainController {
    private DataInput model;
    private FrmCalculator view;
    private String lastOperation = "";
    
    public MainController(){
        //Establecer la referencia modelo-controlador
        this.model = new DataInput(this);
    }
    
    //Escablecer la referencia de la ventana al controlador
    public void setView(FrmCalculator pViewReference){
        this.view = pViewReference;
    }

    public String getLastOperation() {
        return lastOperation;
    }

    public void setLastOperation(String lastOperation) {
        this.lastOperation = lastOperation;
    }
    
    //Envía el número al data input para guardarlo como tal
    public void processNumber(String pCurrentNumber){
        model.setNumber(pCurrentNumber);
    }
    
    //Respalda el número al total y lo limpia
    public void backupNumber(){
        model.backupInformation();
    }
    
    //Envía un mensaje al modelo para que haga un reset de las variables
    public void cleanResult(){
        model.resetDefaults();
    }
    
    //Envía un mensaje a la vista para que imprima el número
    public void showResult(String pNumber){
        view.updateResult(pNumber);
    }
    
    //Operaciones
    //Envía el número al data input para que este sea manipulado como MyBigInteger
    public void sendOperationMessage(){
        switch(lastOperation){
            case "+":
                model.executeAdd();
                break;
            case "-":
                model.executeSub();
                break;
        }
        
        view.updateResult(model.getTotal());
    }
}
