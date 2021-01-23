package carparts;

import java.sql.SQLException;
import java.sql.ResultSet;
import org.springframework.jdbc.core.RowMapper;


public class OrderArticleRowMapper implements RowMapper<OrderArticle> {
    @Override
    public OrderArticle mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderArticle oa = new OrderArticle();
		
		oa.Ident = rs.getString("Ident");
//		oa.NUMFAC = rs.getString("NUMFAC");
//		oa.NUMLNG = rs.getString("NUMLNG");
		oa.QTEART = rs.getInt("QTEART");
		oa.VTEFRS = rs.getDouble("VTEFRS");
		
		oa.NUMART = rs.getString("NUMART");
//		oa.CDEART = rs.getString("CDEART");
		oa.LIBART = rs.getString("LIBART");
		oa.VTEFRSA = rs.getDouble("VTEFRSA");
		
        return oa;
    }
}
