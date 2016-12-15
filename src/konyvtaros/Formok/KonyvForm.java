package konyvtaros.Formok;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import konyvtaros.Adatbazis;
import konyvtaros.Konyv;

/**
 *
 * @author Dr. Mészáros András - 2016.12.11.
 * 
 */
public class KonyvForm extends JDialog implements ActionListener {
  
  private char mod;
  
  private final Dimension kep = Toolkit.getDefaultToolkit().getScreenSize();
  private final JLabel lbKonyvID = new JLabel("A könyv azonosító száma: ");
  private final JLabel lbKonyvIDAdat = new JLabel();
  private final JLabel lbCim = new JLabel("A könyv címe: ");
  private final JLabel lbSzerzoKeresztnev = new JLabel("A szerző keresztneve: ");
  private final JLabel lbSzerzoVezeteknev = new JLabel("A szerző vezetékneve: ");
  private final JLabel lbHelyszin = new JLabel("Helyszín: ");
  private final JLabel lbMegjegyzes = new JLabel("Megjegyzés: ");
  private final JButton btOK = new JButton("OK");
  private final JButton btMegsem = new JButton("Mégsem");
  
  private final JTextField txCim = new JTextField();
  private final JTextField txSzerzoKeresztnev = new JTextField();
  private final JTextField txSzerzoVezeteknev = new JTextField();
  private final JTextField txHelyszin = new JTextField();
  private final JTextField txMegjegyzes = new JTextField();
  
  private final Font f = new Font("sans-serif", Font.PLAIN, 16);
  
  public static void fontCsere (Component component, Font f) {
    component.setFont (f);
    if (component instanceof Container)
    {
        for (Component child : ( (Container) component).getComponents ())
        {
            fontCsere (child, f );
        }
    }
  }
  
  /**
   * A grafikus felület beállítása
   */
  private void GUI() {
    setTitle("Könyv adatainak megadása...");
    setBounds(kep.width/2-300, kep.height/2-200, 600, 400);
    setLayout(new GridLayout(7, 2, 10, 10));
    add(btOK);add(btMegsem);
    btMegsem.addActionListener(this);
    btOK.addActionListener(this);
    add(lbMegjegyzes,1,0);add(txMegjegyzes,1,1);
    add(lbHelyszin,2,0);add(txHelyszin,2,1);
    add(lbSzerzoKeresztnev,3,0);add(txSzerzoKeresztnev,3,1);
    add(lbSzerzoVezeteknev,4,0);add(txSzerzoVezeteknev,4,1);
    add(lbCim,5,0); add(txCim,5,1);
    add(lbKonyvID,6,0); add(lbKonyvIDAdat,6,1);
    fontCsere(this, f);
    setModal(true);
  }
  
  
  /**
   * Alapértelmezett konstruktor, amely egy üres formot hoz létre.
   */
  public KonyvForm() {
    GUI();
    lbKonyvIDAdat.setText("000000");
    setVisible(true);
  }
  
  /**
   * Módosított konstruktor, amely egy korábban megadott könyv
   * adatait jeleníti meg.
   * @param k - a paraméterként megadott könyv
   * @param m - frissítést vagy törlést eldöntő paraméter.
   */
  public KonyvForm(konyvtaros.Konyv k, char m) {
    GUI();
    this.mod = m;
    //System.out.println(k);
    lbKonyvIDAdat.setText(k.getId().toString());
    txCim.setText(k.getCim());
    txSzerzoVezeteknev.setText(k.getSzerzoVezeteknev());
    txSzerzoKeresztnev.setText(k.getSzerzoKeresztnev());
    txHelyszin.setText(k.getHelyszin());
    txMegjegyzes.setText(k.getMegjegyzes());
    if (mod == 't') 
      btOK.setText("Törlés");
    if (mod == 'f')
      btOK.setText("Frissítés");
    setVisible(true);
  }

  private Konyv kFeltoltes() {
    Konyv k = new Konyv();
    k.setID(Integer.parseInt(lbKonyvIDAdat.getText()));
    k.setCim(txCim.getText());
    k.setSzerzoVezeteknev(txSzerzoVezeteknev.getText());
    k.setSzerzoKeresztnev(txSzerzoKeresztnev.getText());
    k.setHelyszin(txHelyszin.getText());
    k.setMegjegyzes(txMegjegyzes.getText());
    return k;
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
   if (e.getSource() == btMegsem) {
     dispose();
   }
   if (e.getSource() == btOK) {
     Konyv k = kFeltoltes();
     switch (mod) {
       case 't': {
         Adatbazis a = new Adatbazis();
         a.Torles(k);
         break;
       }
       case 'f': {
         Adatbazis a = new Adatbazis();
         a.Frissites(k);
         break;
       }
       default: {
         Adatbazis a = new Adatbazis();
         a.Mentes(k);
       }
     }
     dispose();
   }
  }

}
