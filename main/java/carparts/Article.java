package carparts;

import java.util.Date;


  public class Article {
	public String Ident;
	public String NUMART;
	public String CDEART;
	public String LIBART;
	public double ACHUSS;
	public double ACHFRS;
	public double VTEFRS;
	public int QTEART;
	public double CHIFF_AFF;
	public String FOUART;
	public int ENTREE;
	public Date Created;
				  
	public void setNumber( String v ) { NUMART = v; }
	public void setCode( String v ) { CDEART = v; }
	public void setDescription( String v ) { LIBART = v; }
	public void setSource( String v ) { FOUART = v;}
	public void setBuyUSD( double v ) { ACHUSS = v; }
	public void setBuyCHF( double v ) { ACHFRS = v; }
	public void setSellCHF( double v ) { VTEFRS = v; }
	public void setInStock( int v ) { QTEART = v; }
	public void setTurnaround( double v ) { CHIFF_AFF = v; }
	public void setCreated( Date v ) { Created = v; }
				  				  
/*	  
	public void setId( int id ) {}
	public void setFirstName( String v ) {}
	public void setLastName( String v ) {}
	public void setAddress( String v ) {}  
*/	
  }
  
  