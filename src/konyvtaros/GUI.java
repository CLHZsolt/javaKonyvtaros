package konyvtaros;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;

import konyvtaros.Formok.*;

/**
 *
 * @author Dr. Mészáros András - 2016.12.06.
 * 
 */
public class GUI extends JFrame implements ActionListener, MouseListener {
  private final Dimension kep = Toolkit.getDefaultToolkit().getScreenSize();
  private final JMenuBar mbfoMenu= new JMenuBar();
  
  private final JMenu mProgram = new JMenu("Program");
  private final JMenuItem miNevjegy = new JMenuItem("Névjegy");
  
  private final JMenu mKonyvek = new JMenu("Könyvek");
  private final JMenuItem miUjKonyv = new JMenuItem("Új könyv felvitele");
  private final JMenuItem miKonyvFrissites = new JMenuItem("Könyv adatainak frissítése");
  private final JMenuItem miKonyvTorles = new JMenuItem("Könyv törlése");
  
  private final JMenu mKereses = new JMenu("Keresés");
  private final JMenuItem miKereses = new JMenuItem("Egy könyv keresése");
  
  private final JPanel pnKereses = new JPanel();
  private final JScrollPane pane;
  
  private final Font f = new Font("sans-serif", Font.PLAIN, 16);
  private final JLabel statusSor = new JLabel("Könyv törléséhez - jobb klikk, "+
           "könyv frissítéséhez - bal klikk");
  
  public GUI() {
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setTitle("javaKönyvtáros v1.0");
    setBounds(kep.width/2-350, kep.height/2-200, 800, 500);
    setLayout(new BorderLayout(10, 10));
    pnKereses.setLayout(new BoxLayout(pnKereses, BoxLayout.Y_AXIS));
    
    pane = new JScrollPane(pnKereses);
    
    add(pane);
    add(statusSor, BorderLayout.SOUTH);
    setJMenuBar(mbfoMenu);
    mbfoMenu.add(mProgram);
    mProgram.add(miNevjegy);
    miNevjegy.addActionListener(this);
    
    mbfoMenu.add(mKonyvek);
    mKonyvek.add(miUjKonyv);
    mKonyvek.add(miKonyvFrissites);
    mKonyvek.add(miKonyvTorles);
    miUjKonyv.addActionListener(this);
    miKonyvFrissites.addActionListener(this);
    miKonyvTorles.addActionListener(this);
    
    mbfoMenu.add(mKereses);
    mKereses.add(miKereses);
    miKereses.addActionListener(this);
    
    konyvtaros.Formok.KonyvForm.fontCsere(this, f);
    
    setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == miNevjegy) {
      new Nevjegy();
    } 
    
    if (e.getSource() == miUjKonyv) {
      new KonyvForm();
    }
    
    if (e.getSource() == miKonyvFrissites) {
      String s = JOptionPane.showInputDialog("Kérem a módosítandó könyv azonosító számát: ");
      if (s == null) return;
      Adatbazis a = new Adatbazis();
      ArrayList al = a.Lekerdezes("select * from konyvtar where id="+s);
      if (al != null ) {
        Konyv k = (Konyv)al.get(0);
        new KonyvForm(k, 'f');
      } 
    }
    
    if (e.getSource() == miKonyvTorles) {
      String s = JOptionPane.showInputDialog("Kérem a törlendő könyv azonosító számát: ");
      if (s == null) return;
      Adatbazis a = new Adatbazis();
      ArrayList<Konyv> al = a.Lekerdezes("select * from konyvtar where id="+s);
      if (al.size() > 0 ) {
        Konyv k = al.get(0);
        new KonyvForm(k, 't');
      } 
    }
   
    if (e.getSource() == miKereses) {
      String s = JOptionPane.showInputDialog("Kérem a keresett könyv címét, vagy szerzőjének nevét: ");
      if (s == null) return;
      Adatbazis a = new Adatbazis();
      ArrayList<Konyv> al = a.Lekerdezes("select * from konyvtar where "
              + "CIM like '%"+s+"%' or "
              + "SZERZOVEZETEKNEV like '%"+s+"%' or "
              + "SZERZOKERESZTNEV like '%"+s+"%'");
      
      if (al.size() > 0) {
        pnKereses.removeAll();
        for (Konyv k:al) {
          LinkLabel l = new LinkLabel();
          l.setID(k.getId());
          l.setText(k.getSzerzoVezeteknev()+" "+k.getSzerzoKeresztnev()+" - "+k.getCim());
          l.addMouseListener(this);     
          pnKereses.add(l);
        }
        konyvtaros.Formok.KonyvForm.fontCsere(pnKereses, f);
        pnKereses.revalidate();
        pnKereses.repaint();
      } else {
        JOptionPane.showMessageDialog(null, "A keresés nem eredményezett találatot!");
        panelTorles();
      }
    }
  }
  
  /**
   * A keresési panelen megjelenő találati elemeket törli
   */
  private void panelTorles() {
    pnKereses.removeAll();
    pnKereses.revalidate();
    pnKereses.repaint();
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {
    LinkLabel l = (LinkLabel)e.getSource();
    Adatbazis a = new Adatbazis();
    ArrayList<Konyv> lista = a.Lekerdezes("select * from konyvtar where id="+l.getID());
    if (lista.isEmpty()) {
      JOptionPane.showMessageDialog(null, "A kiválasztott könyv nem létezik.");
    } else {
      Konyv k = lista.get(0);
      if (e.getButton() == 1) { // Ha a felhasználó a bal gombot nyomta meg, akkor frissít
        new KonyvForm(k, 'f');
      } else if (e.getButton() == 3) {
        //Ha a felhasználó a jobb gombot nyomta meg, akkor töröl
        new KonyvForm(k, 't');
      }
    }
    panelTorles();
  }

  @Override
  public void mousePressed(MouseEvent e) {
    ;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
   ;
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    ;
  }

  @Override
  public void mouseExited(MouseEvent e) {
    ;
  }
  
}
