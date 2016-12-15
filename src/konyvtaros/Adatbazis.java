package konyvtaros;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Dr. Mészáros András - 2016.12.03.
 * A Könyvtáros nevű projekt adatbázis-kezeléséért
 * felelős osztály.
 */
public class Adatbazis {

  private static final String FAJLNEV = "adatbazis.sqlite";
  
  /**
   * Az eljárás felépíti a kapcsolatot az adatbázissal, és 
   * visszaad egy Connection elemet. Ha a kapcsolat felépítése
   * nem sikerült, akkor null értéket ad vissza.
   */
  
  private Connection kapcsolatFelvetel() {
    Connection c = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:"+FAJLNEV);
    } catch (Exception e) {
       JOptionPane.showMessageDialog(null, "Hiba az adatbázis kapcsolat "
             + "felépítése során!\n"+e.getMessage(), 
              "HIBA!", 0);
      return c;
    }
    return c;
  }
  
  /**
   * Az eljárásnak az a feladata, hogy létrehozza az adatbázist
   * és annak a tábláit, amennyiben az nem létezik.
   * Ha a tábla létrehozása során az OK üzenet érkezik, akkor
   * az hibamentesen létrejött, ellenkező esetben az eljárás a
   * hibaüzenet szövegével tér vissza.
   * Ha a meghívás során nem sikerült kapcsolatot felépíteni az 
   * adatbázissal, akkor null értékkel tér vissza.
   */
  private String tablaLetrehozas() {
    Connection c = kapcsolatFelvetel();
    Statement stmt;
    if (c != null) {
      try {
        stmt = c.createStatement();
        String sql =  "CREATE TABLE KONYVTAR " +
                      "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                      "CIM TEXT NOT NULL, "+
                      "SZERZOVEZETEKNEV TEXT NOT NULL, "+
                      "SZERZOKERESZTNEV TEXT NOT NULL, "+
                      "HELYSZIN TEXT, "+
                      "MEGJEGYZES TEXT)";
        stmt.executeUpdate(sql);
        stmt.close();
        c.close();
      } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "Hiba a tábla létrehozása során!\n"
                 + "Hibaüzenet: "+e.getMessage(), 
              "HIBA!", 0);
        return null;
      }
      return "OK";
    }
    return null;
  }
  
  /**
   * SQL parancs kulcs-értékpár részének előkészítését végző eljárás. Paraméterként
   * egy Konyv objektumot vesz át, és a kitöltött tulajdonságok figyelembevétele
   * mellett visszaad egy SQL stringet.
   * @param k - könyv, amelynek adataiból az SQL stringet kell elkészíteni
   * @return String - amely tartalmazza a vonatkozó mezőket.
   */
  private String sqlString(Konyv k) {
    String s1 = "(CIM, SZERZOVEZETEKNEV, SZERZOKERESZTNEV";
    String s2 = ") VALUES ('"+k.getCim()+"', '"+k.getSzerzoVezeteknev()+"'"
            + ", '"+k.getSzerzoKeresztnev()+"'";
    s1+= ", HELYSZIN";
    s2+= ", '"+k.getHelyszin()+"'";
    s1+= ", MEGJEGYZES";
    s2+= ", '"+k.getMegjegyzes()+"'";
    //System.out.println(s1+s2+")");
    return s1+s2+")";
  }
  
  /**
   * 
   */
  private String updateString(Konyv k) {
    String s = "CIM='"+k.getCim()+"', ";
    s+= "SZERZOVEZETEKNEV='"+k.getSzerzoVezeteknev()+"', ";
    s+= "SZERZOKERESZTNEV='"+k.getSzerzoKeresztnev()+"'";
    if (k.getHelyszin() != null)
      s+=", HELYSZIN='"+k.getHelyszin()+"'";
    if (k.getMegjegyzes() != null)
      s+=", MEGJEGYZES='"+k.getMegjegyzes()+"'";
    s+=" WHERE ID="+k.getId();
    return s;
  }
  
  private String konyvEllenorzes(Konyv k) {
    if (k.getCim().length() == 0) {
      JOptionPane.showMessageDialog(null, "A lementéshez a könyv címét meg kell adni!", 
              "HIBA!", 0);
      return null;
    }
    if (k.getSzerzoVezeteknev().length() == 0) {
     JOptionPane.showMessageDialog(null, "A lementéshez a könyv szerzőjének "
             + "vezetéknevét meg kell adni!", 
              "HIBA!", 0);
      return null;
    }
    if (k.getSzerzoKeresztnev().length() == 0) {
     JOptionPane.showMessageDialog(null, "A lementéshez a könyv szerzőjének "
             + "keresztnevét meg kell adni!", 
              "HIBA!", 0);
      return null;
    }
    return "OK";
  }
  
  /**
   * Könyv lementését végző eljárás
   * @param k - Az adatbázisba lementendő könyvet tartalmazó objektum
   * @return - "OK" String, ha minden rendben zajlott, és a hibaüzenet szövege,
   * ha valami nem megfelelően működött.
   */
  public String Mentes(Konyv k) {
    if (konyvEllenorzes(k) == null ) return null;
    try {
      Connection c = kapcsolatFelvetel();
      if (c == null ) return null;
      c.setAutoCommit(false);
      String sql = "INSERT INTO KONYVTAR"+sqlString(k);
      Statement stmt = c.createStatement();
      stmt.executeUpdate(sql);
      //System.out.println("Insert parancs lefutott.");
      stmt.close();
      c.commit();
      c.close();    
    } catch (Exception e) {
       JOptionPane.showMessageDialog(null, "A mentés során hiba lépett fel!\n"
             + "A hibaüzenet: "+e.getMessage(), 
              "HIBA!", 0);
      return null;
    }
    return "OK";
  }
  
  
  /**
   * Könyv adatainak frissítése az adatbázisban
   * @param k - a korábban már lementett és frissíteni kívánt könyv
   * @return - "OK" üzenet, ha a frissítés gond nélkül lefutott, ha pedig
   * hiba történt, akkor a hibaüzenet szövege.
   */
  public String Frissites(Konyv k) {
    if (konyvEllenorzes(k) == null ) return null;
    if (k.getId() == null)
      throw new IllegalArgumentException("A frissítéshez a könyv ID-jét meg kell adni!");
    try {
      Connection c = kapcsolatFelvetel();
      c.setAutoCommit(false);
      String sql = "UPDATE KONYVTAR SET "+updateString(k);
      Statement stmt = c.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
      c.commit();
      c.close();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "A frissítés során hiba lépett fel!\n"
             + "A hibaüzenet: "+e.getMessage(), 
              "HIBA!", 0);
      return null;
    }
    return "OK";
  }
  
  /**
   * Könyv törlése az adatbázisból
   * @param k - a korábban már lementett és törölni akart könyv
   * @return - "OK", ha a törlés hiba nélkül lefutott, ellenkező esetben
   * a hibaüzenet szövege.
   */
  public String Torles(Konyv k) {
    if (k.getId() == 0) 
      throw new IllegalArgumentException("A könyv törléséhez az azonosítót"
              + "meg kell adni!");
    try {
      Connection c = kapcsolatFelvetel();
      c.setAutoCommit(false);
      Statement stmt = c.createStatement();
      String sql = "DELETE FROM konyvtar WHERE ID="+k.getId()+";";
      stmt.executeUpdate(sql);
      c.commit();
      stmt.close();
      c.close();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "A törlés során hiba lépett fel!\n"
             + "A hibaüzenet: "+e.getMessage(), 
              "HIBA!", 0);
      return null;
    }
    return "OK";
  }
  
  public ArrayList<Konyv> Lekerdezes(String sql) {
    ArrayList<Konyv> konyvlista = new ArrayList<>();
    ResultSet rs = null;
    Connection c = kapcsolatFelvetel();
    Statement stmt = null;
    try {
      c.setAutoCommit(false);
      stmt = c.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        konyvlista.add(new Konyv(rs.getInt("ID"),
                                 rs.getString("CIM"), 
                                 rs.getString("SZERZOVEZETEKNEV"),
                                 rs.getString("SZERZOKERESZTNEV"),
                                 rs.getString("HELYSZIN"),
                                 rs.getString("MEGJEGYZES")));
      }
      stmt.close();
      c.close();
    } catch (Exception e) {
      System.out.println(e.getMessage() + sql);
      konyvlista = null;
      return konyvlista;
    }
    return konyvlista;
  }
  
  public Adatbazis() {
    File f = new File(FAJLNEV);
    if (!f.exists()) {
      //Ha a fájl nem létezik, akkor megpróbáljuk létrehozni.
      String uzenet = tablaLetrehozas();
      if (!uzenet.equals("OK")) {
       JOptionPane.showMessageDialog(null, "Az adatbázis létrehozása nem "
               + "sikerült!", 
              "HIBA!", 0);
       throw new IllegalArgumentException("Adatbázis nem létezik, és nem "
               + "létrehozható!");
      }
    } 
  }
}
