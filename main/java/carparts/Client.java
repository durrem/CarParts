package carparts;

import java.util.Date;


  public class Client {
	public String Ident;
	public String NOMCLI;
//	public String firstName;
//	public String lastName;
	public String ADRESSE;
	public String NOPLOC;
	public String LOCALITE; 
	public String INDNOTEL;
	public String EMAIL;
	public String COMCLI;
	public double POURCENTS;
	public double TOT_ACHATS;
	public double TOT_PAYE;
	public Date CLI_DEPUIS;

	public void setNOMCLI( String v ) { NOMCLI = v; }
	public void setADRESSE( String v ) { ADRESSE = v; }
	public void setNOPLOC( String v ) { NOPLOC = v; }
	public void setLOCALITE( String v ) { LOCALITE = v; }
	public void setINDNOTEL( String v ) { INDNOTEL = v; }

//	public void setFirstName( String v ) { firstName = v; }
//	public void setLastName( String v ) { lastName = v; }
	public void setClientName( String v ) { NOMCLI = v; }
	public void setAddress( String v ) { ADRESSE = v; }
	public void setPostCode( String v ) { NOPLOC = v; }
	public void setCity( String v ) { LOCALITE = v; }
	public void setPhone( String v ) { INDNOTEL = v; }
	public void setEMail( String v ) { EMAIL = v; }
	public void setReduction( double v ) { POURCENTS = v; }
	public void setComment( String v ) { COMCLI = v; }
	public void setBought( double v ) { TOT_ACHATS = v; }
	public void setPaid( double v ) { TOT_PAYE = v; }
	public void setSince( Date v ) { CLI_DEPUIS = v; }

/*
{
  "FirstName": "Mathias",
  "LastName": "Dⁿrrenberger",
  "Address": "Sunnestrahl 7",
  "PostCode": 8834,
  "City": "Schindellegi",
  "Phone": 10792008526,
  "EMail": "durrem@bluewin.ch",
  "Reduction": 15,
  "Comment": "the best client"
}
*/
	public String toString() {
		return NOMCLI + ";" + ADRESSE + ";" + NOPLOC + ";" + LOCALITE + ";" + INDNOTEL;		
	}
	  
/*	  
	public void setId( int id ) {}
	public void setFirstName( String v ) {}
	public void setLastName( String v ) {}
	public void setAddress( String v ) {}  
*/	
  }
  
  
/*  
	NUMCLI 	INT PRIMARY KEY,
	TYPCLI	VARCHAR(15),
	ALATTN	VARCHAR(50) NOT NULL,
	CANTCLI	VARCHAR(15) NOT NULL,
*/
