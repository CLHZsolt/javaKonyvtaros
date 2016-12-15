package konyvtaros;

/**
 *
 * @author Dr. Mészáros András - 2016.12.03.
 * 
 */
public class Konyv {
  private Integer id;
  private String cim;
  private String szerzoKeresztnev;
  private String szerzoVezeteknev;
  private String helyszin;
  private String megjegyzes;

  public Integer getId() {
    return id;
  }
  
  public void setID(Integer id) {
    this.id = id;
  }
  
  public String getCim() {
    return cim;
  }

  public void setCim(String cim) {
    this.cim = cim;
  }

  public String getSzerzoKeresztnev() {
    return szerzoKeresztnev;
  }

  public void setSzerzoKeresztnev(String szerzoKeresztnev) {
    this.szerzoKeresztnev = szerzoKeresztnev;
  }

  public String getSzerzoVezeteknev() {
    return szerzoVezeteknev;
  }

  public void setSzerzoVezeteknev(String szerzoVezeteknev) {
    this.szerzoVezeteknev = szerzoVezeteknev;
  }

  public String getHelyszin() {
    return helyszin;
  }

  public void setHelyszin(String helyszin) {
    this.helyszin = helyszin;
  }

  public String getMegjegyzes() {
    return megjegyzes;
  }

  public void setMegjegyzes(String megjegyzes) {
    this.megjegyzes = megjegyzes;
  }

  @Override
  public String toString() {
    return "Könyv {cím=" + cim + 
            ", szerzője=" + szerzoVezeteknev+" "+szerzoKeresztnev + ", "
            + "helyszin=" + helyszin + ", megjegyzés=" + megjegyzes + '}';
  }
  
  public Konyv() {
    this.cim = null;
    this.szerzoVezeteknev=null;
    this.szerzoKeresztnev=null;
    this.helyszin = null;
    this.megjegyzes = null;
  }
  
  public Konyv(Integer id, String cim, String vezeteknev, String keresztnev, 
          String helyszin, String megjegyzes) {
    this.id = id;
    this.cim = cim;        
    this.szerzoVezeteknev = vezeteknev;
    this.szerzoKeresztnev = keresztnev;
    this.helyszin = helyszin;
    this.megjegyzes = megjegyzes;
  }
}
