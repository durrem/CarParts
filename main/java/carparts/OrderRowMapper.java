
package carparts;


import java.sql.SQLException;
import java.sql.ResultSet;
import org.springframework.jdbc.core.RowMapper;


public class OrderRowMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
		
		order.Ident = rs.getString("Ident");
		order.NOMCLI = rs.getString("NOMCLI");
		order.ADRESSE = rs.getString("ADRESSE");
		order.NOPLOC = rs.getString("NOPLOC");
		order.LOCALITE = rs.getString("LOCALITE");
		order.INDNOTEL = rs.getString("INDNOTEL");
		order.EMAIL = rs.getString("EMAIL");
		order.TOT_PAYE = rs.getDouble("TOT_PAYE");
		order.DEJA_PAYE = rs.getDouble("DEJA_PAYE");
		order.NUMFAC = rs.getInt("NUMFAC");
		order.DATE_EMIS = rs.getDate("DATE_EMIS");

/*
        order.setId(rs.getInt("ID"));
        order.setFirstName(rs.getString("FIRST_NAME"));
        order.setLastName(rs.getString("LAST_NAME"));
        order.setAddress(rs.getString("ADDRESS"));
*/
        return order;
    }
}

