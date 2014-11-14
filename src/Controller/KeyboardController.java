
package Controller;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

public class KeyboardController extends JPanel implements KeyEventDispatcher {
        
    private ArrayList<JButton> buttonsArray;
    
    public KeyboardController(ArrayList<JButton> pButtonsArray){
        this.buttonsArray = pButtonsArray;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V)
                buttonsArray.get(20).doClick();
        }
        else if (e.getID() == KeyEvent.KEY_RELEASED) {
            try{
                String replaceAll = ((JTextComponent) e.getSource()).getText().replaceAll("[^-0-9]", "");
                ((JTextComponent) e.getSource()).setText(replaceAll);
            }catch(Exception evt){
                //Continuar
            }
            switch(e.getKeyChar()){
                case KeyEvent.VK_0:
                    buttonsArray.get(0).doClick();
                    break;
                case KeyEvent.VK_1:
                    buttonsArray.get(1).doClick();
                    break;
                case KeyEvent.VK_2:
                    buttonsArray.get(2).doClick();
                    break;
                case KeyEvent.VK_3:
                    buttonsArray.get(3).doClick();
                    break;
                case KeyEvent.VK_4:
                    buttonsArray.get(4).doClick();
                    break;
                case KeyEvent.VK_5:
                    buttonsArray.get(5).doClick();
                    break;
                case KeyEvent.VK_6:
                    buttonsArray.get(6).doClick();
                    break;
                case KeyEvent.VK_7:
                    buttonsArray.get(7).doClick();
                    break;
                case KeyEvent.VK_8:
                    buttonsArray.get(8).doClick();
                    break;
                case KeyEvent.VK_9:
                    buttonsArray.get(9).doClick();
                    break;
                case KeyEvent.VK_PERIOD:
                    buttonsArray.get(10).doClick();
                    break;
                case KeyEvent.VK_EQUALS:
                    buttonsArray.get(11).doClick();
                    break;
                case KeyEvent.VK_ENTER:
                    buttonsArray.get(11).doClick();
                    break;
                case KeyEvent.VK_C:
                    buttonsArray.get(12).doClick();
                    break;
                case KeyEvent.VK_ESCAPE:
                    buttonsArray.get(13).doClick();
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    buttonsArray.get(14).doClick();
                    break;
                case '+':
                    buttonsArray.get(15).doClick();
                    break;
                case '-':
                    buttonsArray.get(16).doClick();
                    break;
                case '*':
                    buttonsArray.get(17).doClick();
                    break;
                case '/':
                    buttonsArray.get(18).doClick();
                    break;
                case '%':
                    buttonsArray.get(19).doClick();
                    break;
            }
        } else if (e.getID() == KeyEvent.KEY_TYPED) {}
        return false;
    }
}
