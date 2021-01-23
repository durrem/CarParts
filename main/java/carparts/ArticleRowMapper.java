package carparts;


import java.sql.SQLException;
import java.sql.ResultSet;
import org.springframework.jdbc.core.RowMapper;


public class ArticleRowMapper implements RowMapper<Article> {
    @Override
    public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
        Article article = new Article();
		
		article.Ident = rs.getString("Ident");
		article.NUMART = rs.getString("NUMART");
		article.CDEART = rs.getString("CDEART");
		article.LIBART = rs.getString("LIBART");
		article.ACHUSS = rs.getDouble( "ACHUSS");
		article.ACHFRS = rs.getDouble( "ACHFRS");
		article.VTEFRS = rs.getDouble( "VTEFRS");
		article.QTEART = rs.getInt( "QTEART" );
		article.CHIFF_AFF = rs.getDouble( "CHIFF_AFF");
		article.FOUART = rs.getString("FOUART");
		article.ENTREE = rs.getInt( "ENTREE" );
		article.Created = rs.getDate( "Created" );
		
        return article;
    }
}

