package konyvtaros.Formok;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JLabel;


/**
 *
 * @author Dr. Mészáros András - 2016.12.06.
 * 
 */
public class Nevjegy extends JDialog {
  
  private final Dimension kep = Toolkit.getDefaultToolkit().getScreenSize();
  private final JLabel lbProgramnev = new JLabel("javaKönyváros könyvnyilvántartó program");
  private final JLabel lbKeszito = new JLabel("Készítette: Dr. Mészáros András");

  public Nevjegy() {
    setTitle("Névjegy");
    setBounds(kep.width/2-150, kep.height/2-50, 500, 100);
    setLayout(new GridLayout(2, 1));
    add(lbProgramnev);
    lbProgramnev.setFont(new Font("sans-serif", Font.BOLD, 20));
    add(lbKeszito);
    lbKeszito.setFont(new Font("Arial", Font.ITALIC, 16));
    setModal(true);
    setVisible(true);
  }
}
