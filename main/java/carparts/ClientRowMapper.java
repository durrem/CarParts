package carparts;

import java.sql.SQLException;
import java.sql.ResultSet;
import org.springframework.jdbc.core.RowMapper;



public class ClientRowMapper implements RowMapper<Client> {
    @Override
    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
        Client client = new Client();
		
		client.Ident = rs.getString("Ident");
		client.NOMCLI = rs.getString("NOMCLI");
		client.ADRESSE = rs.getString("ADRESSE");
		client.NOPLOC = rs.getString("NOPLOC");
		client.LOCALITE = rs.getString("LOCALITE");
		client.INDNOTEL = rs.getString("INDNOTEL");
		client.TOT_ACHATS = rs.getDouble("TOT_ACHATS");
		client.TOT_PAYE = rs.getDouble("TOT_PAYE");
		client.CLI_DEPUIS = rs.getDate("CLI_DEPUIS");
		
				
/*        client.setId(rs.getInt("ID"));
        client.setFirstName(rs.getString("FIRST_NAME"));
        client.setLastName(rs.getString("LAST_NAME"));
        client.setAddress(rs.getString("ADDRESS"));*/

/*
--	NUMCLI 	INT AUTO_INCREMENT  PRIMARY KEY,
	NUMCLI 	INT PRIMARY KEY,
	TYPCLI	VARCHAR(15),
	NOMCLI	VARCHAR(50) NOT NULL,
	ALATTN	VARCHAR(50) NOT NULL,
	ADRESSE	VARCHAR(50) NOT NULL,
	NOPLOC	VARCHAR(10) NOT NULL,
	LOCALITE VARCHAR(25) NOT NULL,
	INDNOTEL VARCHAR(20) NOT NULL,
	EMAIL VARCHAR(75) NOT NULL,
	POURCENTS	decimal( 10, 4 ) NOT NULL,
	CLI_DEPUIS	DATE NOT NULL,
	TOT_ACHATS	decimal( 10, 2 ) NOT NULL,
	TOT_PAYE	decimal( 10, 2 ) NOT NULL,
	CANTCLI	VARCHAR(15) NOT NULL,
	COMCLI VARCHAR(100) NOT NULL
*/

        return client;
    }
}

