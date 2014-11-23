
package bigintegercalculator;

import Model.MyBigInteger;
import View.FrmCalculator;

public class BigIntegerCalculator {

    public static void main(String[] args) {
        String n = "12345678";
        System.out.println(n.substring(0,n.length()-1));
        new FrmCalculator().setVisible(true);
    }
}
