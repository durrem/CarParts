/*
mvn clean spring-boot:run
mvn clean test
https://mvnrepository.com/search?q=junit

https://github.com/durrem/CarParts.git


https://medium.com/the-resonant-web/spring-boot-2-0-project-structure-and-best-practices-part-2-7137bdcba7d3
https://docs.spring.io/spring-boot/docs/1.2.3.RELEASE/reference/html/getting-started-first-application.html
https://springhow.com/spring-boot-project-structure-and-convention/

https://spring.io/guides/tutorials/rest/


client authorizations ??? 
https://javascript.info/xmlhttprequest
https://developer.mozilla.org/en-US/docs/Learn/Forms/Sending_forms_through_JavaScript
https://spring.io/guides/gs/handling-form-submission/
https://spring.io/guides/gs/relational-data-access/

mvn org.springframework.boot:run

https://mkyong.com/spring/spring-jdbctemplate-querying-examples/
https://blog.sverrirs.com/2017/03/jsonify-inputfield-values-before-submit.html
https://www.baeldung.com/spring-http-logging
https://www.baeldung.com/jackson-object-mapper-tutorial


<input type="date">
<input type="number">
<input type="password">
<input type="search">
<input type="tel">
<input type="datetime-local">	!!
<input type="datetime-local" id="meeting-time"
       name="meeting-time" value="2018-06-12T19:30"
       min="2018-06-07T00:00" max="2018-06-14T00:00">

pattern="[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}" required>


submit:
https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/submit		=> formaction (url), formenctype (application/x-www-form-urlencoded, multipart/form-data, text/plain), formmethod (POST/GET)

http://username:password@www.example.com:80/path/to/file.php?foo=316&bar=this+has+spaces#anchor
https://developer.mozilla.org/en-US/docs/Web/API/FormData
FormData => multipart/form-data

https://stackoverflow.com/questions/17147821/how-to-make-a-whole-row-in-a-table-clickable-as-a-link

https://api.jquery.com/category/ajax/shorthand-methods/
jQuery.getJSON()	/ 	jQuery.post()	/	.load()	
https://stepansuvorov.com/blog/2014/04/jquery-put-and-delete/


https://github.com/ChrisCinelli/mysql_json
https://mkyong.com/spring-boot/spring-boot-jdbc-stored-procedure-examples/
*/

package carparts;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
//import org.springframework.http.RequestMethod;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement;  
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import javax.annotation.PostConstruct;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;


@RestController
public class Controller {

	@Autowired
	JdbcTemplate jdbcTemplate;
// Collection<Map<String, Object>> rows = jdbc.queryForList("SELECT QUERY");

	private SimpleJdbcCall sqlCall_1;


    @PostConstruct
    void init() {
        sqlCall_1 = new SimpleJdbcCall(jdbcTemplate).withProcedureName("MakeOrder");
	}


	@GetMapping(value = "/home")
	public  String getHome() {
		return "<img src=\"cox.jpg\">";
	}	


	@GetMapping(value = "/whoami")
	public  String getWhoAmI() {
		return "<img src=\"Mathias.jpg\">";
	}	


	@GetMapping(value = "/list/clients")
	public  String getClients() throws ClassNotFoundException, SQLException {		
		return getList( 1, "All Clients", "" );
	}	

	@GetMapping(value = "/list/articles")
	public  String getArticles() throws ClassNotFoundException, SQLException {		
		return getList( 2, "All Articles", "" );
	}	

	@GetMapping(value = "/list/orders")
	public  String getOrders() throws ClassNotFoundException, SQLException {		
		return getList( 3, "All Orders", "" );
	}	


	@GetMapping(value = "/list/scratchpad")
	public  String getScratchpad() throws ClassNotFoundException, SQLException {			
		return renderHtmlFormScratchpad( null, true );		
	}	


	@GetMapping(value = "/form/client/{id}")
	public  String getFormClient( @PathVariable(required = false) String id ) throws ClassNotFoundException, SQLException {				
		Client cli = null;
		if (id != null) {
			cli = jdbcTemplate.queryForObject( "SELECT *, NUMCLI Ident FROM Clients WHERE NUMCLI = ?", new Object[]{id}, new ClientRowMapper());				
			cli.Ident = id;
		}
		return renderHtmlFormClient( cli, false );
	}

	@GetMapping(value = "/form/client")
	public  String getFormClient1( ) throws ClassNotFoundException, SQLException {				
		Client cli = null;
		return renderHtmlFormClient( cli, false );
	}


	@GetMapping(value = "/form/article/{id}")
	public  String getFormArticle( @PathVariable(required = false) String id) throws ClassNotFoundException, SQLException {		
		Article art = null;
		if (id != null) {
			art = jdbcTemplate.queryForObject( "SELECT *, NUMART Ident FROM Articles WHERE NUMART = ?", new Object[]{id}, new ArticleRowMapper());				
			art.Ident = id;
		}		
		return renderHtmlFormArticle( art, false );	
	}

	@GetMapping(value = "/form/article")
	public  String getFormArticle1( ) throws ClassNotFoundException, SQLException {		
		Article art = null;
		return renderHtmlFormArticle( art, false );	
	}
	

	@GetMapping(value = "/form/order/{id}")
	public  String getFormOrder(@PathVariable(required = false) String id) throws ClassNotFoundException, SQLException {		
		Order ord = null;
		List< OrderArticle >  oa = new ArrayList< OrderArticle >();
		if (id != null) {
			ord = jdbcTemplate.queryForObject( "SELECT *, NUMFAC Ident FROM Clients c, ClientBills cb where c.NUMCLI = cb.NUMCLI and NUMFAC = ?", new Object[]{id}, new OrderRowMapper());					
			ord.Ident = id;			
			oa = jdbcTemplate.query( "select CONCAT( NUMFAC, NUMLNG ) Ident, ba.VTEFRS, ba.QTEART, a.NUMART, a.VTEFRS VTEFRSA, LIBART from BillArticles ba, Articles a where ba.NUMART = a.NUMART and ba.NUMFAC = ? order by NUMLNG", new Object[]{id}, new OrderArticleRowMapper());
		}
		return renderHtmlFormOrder( ord, oa.toArray( new OrderArticle[ oa.size() ]), false );	
	}


	@GetMapping(value = "/form/order")
	public  String getFormOrder1() throws ClassNotFoundException, SQLException {		
		Order ord = null;
		List< OrderArticle >  oa = new ArrayList< OrderArticle >();
		return renderHtmlFormOrder( ord, oa.toArray( new OrderArticle[ oa.size() ]), false );	
	}

	
	// You can only cast to Integer[] by using: Integer[] arr = (Integer[])list.toArray(new Integer[list.size]); – Hardcoded Jul 5 '10 at 15:48
	
	
	@GetMapping(value = "/form/item/{id}")
	public  String getFormItem(@PathVariable(required = false) String id) throws ClassNotFoundException, SQLException {		
		OrderArticle ord = null;
		if (id != null) 
			ord = jdbcTemplate.queryForObject( "select CONCAT( NUMFAC, NUMLNG ) Ident, ba.VTEFRS, ba.QTEART, a.NUMART, a.VTEFRS VTEFRSA, LIBART from BillArticles ba, Articles a where ba.NUMART = a.NUMART and CONCAT( NUMFAC, NUMLNG ) = ? ", new Object[]{id}, new OrderArticleRowMapper());
		return renderHtmlFormItem( ord, false );	
	}


	@GetMapping(value = "/form/item")
	public  String getFormItem2() throws ClassNotFoundException, SQLException {		
		OrderArticle ord = null;
		return renderHtmlFormItem( ord, false );	
	}
	

	@PostMapping(value = "/client" , consumes = "application/json" )
	String postClient( @RequestBody String json )  throws com.fasterxml.jackson.core.JsonProcessingException {
		System.out.println("********************** POST CLIENT  ****************");								
		System.out.println( "JSON: " + json );
		
		// Convert data		
		ObjectMapper objectMapper = new ObjectMapper();			
		Client cli = objectMapper.readValue(json, Client.class);	
				
		// Store data: insert or update ?
		// -- jdbcTemplate	
// For single insert statements, the insert method of JdbcTemplate is good. However, for multiple inserts, it is better to use batchUpdate.
		 // List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream().map(name -> name.split(" ")).collect(Collectors.toList());
		// splitUpNames.forEach(name -> log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));		
		// jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);
	
	
		// Error handling 
		
		// Generate response 
		cli.Ident = "new";
		return renderHtmlFormClient( cli, true );
	}


	@PostMapping(path = "/article" , consumes = "application/json" )
	String postArticle( @RequestBody String html ) throws com.fasterxml.jackson.core.JsonProcessingException {
		System.out.println("********************** POST CLIENT  ****************");								
		System.out.println( html );
		
		ObjectMapper objectMapper = new ObjectMapper();			
		Article art = objectMapper.readValue(html, Article.class);	
		
		// Store data: insert or update ?
		
		
		art.Ident = "new";
		return renderHtmlFormArticle( art, true );
	}


	@PostMapping(path = "/order" , consumes = "application/json" )
	String postOrder( @RequestBody String html ) throws com.fasterxml.jackson.core.JsonProcessingException {
		System.out.println("********************** POST CLIENT  ****************");								
		System.out.println( html );
		
		ObjectMapper objectMapper = new ObjectMapper();			
		Order ord = objectMapper.readValue(html, Order.class);	
		OrderArticle[] oa = new OrderArticle[0];
				
		// Store data: insert or update ?
		
		ord.Ident = "new";
		return renderHtmlFormOrder( ord, oa, true );
	}


	@PostMapping(value = "/form/item", consumes = "application/json" )
	public  String postFormItem( @RequestBody String html ) throws ClassNotFoundException, SQLException {		
		OrderArticle ord = null;
		System.out.println("********************** POST ADD ITEM TO ORDER ****************");								
		System.out.println( html );
		
//		ObjectMapper objectMapper = new ObjectMapper();			
//		Order ord = objectMapper.readValue(html, Order.class);	
//		OrderArticle[] oa = new OrderArticle[0];
//		return renderHtmlFormItem( ord, false );	
/*		
		Order ord = null;
		List< OrderArticle >  oa = new ArrayList< OrderArticle >();
		if (id != null) {
			ord = jdbcTemplate.queryForObject( "SELECT *, NUMFAC Ident FROM Clients c, ClientBills cb where c.NUMCLI = cb.NUMCLI and NUMFAC = ?", new Object[]{id}, new OrderRowMapper());					
			ord.Ident = id;			
			oa = jdbcTemplate.query( "select CONCAT( NUMFAC, NUMLNG ) Ident, ba.VTEFRS, ba.QTEART, a.NUMART, a.VTEFRS VTEFRSA, LIBART from BillArticles ba, Articles a where ba.NUMART = a.NUMART and ba.NUMFAC = ? order by NUMLNG", new Object[]{id}, new OrderArticleRowMapper());
		}		
		return renderHtmlFormOrder( ord, oa.toArray( new OrderArticle[ oa.size() ]), false );	
*/
		return renderHtmlFormItem( null, false /*OrderArticle oa, boolean readOnly*/ );
	}


	@PostMapping(path = "/scratchpad/order", consumes = "application/json" )
	String postScraClient( @RequestBody String json ) throws com.fasterxml.jackson.core.JsonProcessingException {
		System.out.println("********************** POST SCRATCHPAD MAKE ****************");								
//		System.out.println( json );

// DO we have an order to copy ??
// Do we have one or more clients ?
// => prepare order head(s)
// => add articles to order(s)
// iterate over list, take always first occurence: order (take client, take items ), client (take client), article (take item)

// => Use stored procedure !!

		Order ord = new Order();
		Client cli = null;
		List< OrderArticle > ol = new ArrayList< OrderArticle >();
		Item[] il = parseToItemList( json );
		String idList = toItemIdList( il, false );
		
		System.out.println( "################## IdList: " + idList );
			
		// CALL MakeOrder( idList )
		SqlParameterSource in = new MapSqlParameterSource().addValue("itemIdList", idList );
		sqlCall_1.execute(in);
			
  			
//		return "New order made ... ";	// Selected __ 
		return renderHtmlFormOrder( ord, ol.toArray( new OrderArticle[ ol.size() ]), false );			
	}


	@DeleteMapping(path = "/form/item", consumes = "application/json" )
	public  String delFormItem( @RequestBody String html ) throws ClassNotFoundException, SQLException {		
	/*
		OrderArticle ord = null;
		if (id != null) 
			ord = jdbcTemplate.queryForObject( "select CONCAT( NUMFAC, NUMLNG ) Ident, ba.VTEFRS, ba.QTEART, a.NUMART, a.VTEFRS VTEFRSA, LIBART from BillArticles ba, Articles a where ba.NUMART = a.NUMART and CONCAT( NUMFAC, NUMLNG ) = ? ", new Object[]{id}, new OrderArticleRowMapper());
		return renderHtmlFormItem( ord, false );	
*/
		return "Item deleted";	// Refresh ??
	}


	@DeleteMapping(path = "/scratchpad", consumes = "application/json" )
	String delScraClient( @RequestBody String json ) throws SQLException, com.fasterxml.jackson.core.JsonProcessingException {
		System.out.println("********************** DELETE  SCRATCHPAD MAKE ****************");								
		System.out.println( "JSON: " + json );
				
		Item[] il = parseToItemList( json );
		
		for (Item i : il) {
			if (!i.checked)
				continue;
			int type = 0;
			switch (i.type) {
			case "Client":	type = 1;	break;
			case "Article":	type = 2;	break;
			case "Order":	type = 3;	break;
			}
			// Use Ident ???
			jdbcTemplate.update( "delete from Scratchpad where UserId = ? and Type = ? and Code = ?", 1, type, i.code );
			// where Ident = ? 
		}
		return renderHtmlFormScratchpad( null, true );
	}


	@PostMapping(path = "/scratchpad/client/{id}")
	String postScraClient2( @PathVariable String id  ) {
		System.out.println("********************** POST SCRATCHPAD CLIENT  ****************");								
		System.out.println( id );
//		if (id != null) 
			jdbcTemplate.update( "insert into Scratchpad ( UserId, Type, Code, Description )values ( ?, ?, ?, ? )", 1, 1, id, "" );
		return "Client: " + id + " stored to scratchpad.";
	}


	@PostMapping(path = "/scratchpad/article/{id}")
	String postScraArticle( @PathVariable String id  ) {
		System.out.println("********************** POST SCRATCHPAD ARTICLE ****************");								
		System.out.println( id );
//		if (id != null) 
			jdbcTemplate.update( "insert into Scratchpad ( UserId, Type, Code, Description )values ( ?, ?, ?, ? )", 1, 2, id, "" );
		return "Article: " + id + " stored to scratchpad.";		
	}


	@PostMapping(path = "/scratchpad/order/{id}")
	String postScraOrder( @PathVariable String id  ) {
		System.out.println("********************** POST SCRATCHPAD ORDER ****************");								
		System.out.println( id );
//		if (id != null) 
			jdbcTemplate.update( "insert into Scratchpad ( UserId, Type, Code, Description )values ( ?, ?, ?, ? )", 1, 3, id, "" );
		return "Order: " + id + " stored to scratchpad.";		
	}
	
	
	public  String getList( int id, String tit, String option ) throws ClassNotFoundException, SQLException {
		StringBuilder s = new StringBuilder();		
		ListHeader( s, id, tit, option );
		
		// Pagination ....
		s.append( "<br>" );		

		String fld = "L" + id + "F";
		int n = 1;
		switch (id) {
		case 1:
//			List<Client> clients = jdbcTemplate.query( "Select NUMCLI Ident, NOMCLI, ADRESSE, NOPLOC, LOCALITE, INDNOTEL from Clients order by NOMCLI", new ClientRowMapper());
			List<Client> clients = jdbcTemplate.query( "Select *, NUMCLI Ident from Clients order by NOMCLI", new ClientRowMapper());
			TableHeader( s, new String[] { "NOMCLI", "ADRESSE", "NOPLOC", "LOCALITE", "INDNOTEL" } );
			for(Client c : clients) {
				TableLine1( s, new String[] { c.NOMCLI, c.ADRESSE, c.NOPLOC, c.LOCALITE, c.INDNOTEL }, "client/" + c.Ident );				
				n++;
			}						
			TableFooter( s, null );				
			break;
		case 2:
			List<Article> articles = jdbcTemplate.query( "Select NUMART Ident, CDEART, NUMART, LIBART, ACHUSS, ACHFRS, VTEFRS, QTEART, CHIFF_AFF, FOUART, ENTREE, Created from Articles order by LIBART", new ArticleRowMapper());			
			TableHeader( s, new String[] { "NUMART", "LIBART", "VTEFRS", "QTEART" } );	
			for (Article a : articles) {
				TableLine1( s, new String[] { a.NUMART, a.LIBART, Double.toString( a.VTEFRS ), Integer.toString( a.QTEART ) }, "article/" + a.Ident );
				n++;
			}									
			TableFooter( s, null );				
			break;
			
		case 3:		
			List<Order> orders = jdbcTemplate.query( "select NUMFAC Ident, NOMCLI, ADRESSE, NOPLOC, LOCALITE, INDNOTEL, EMAIL, TOT_PAYE, NUMFAC, DEJA_PAYE, DATE_EMIS from Clients c, ClientBills cb where c.NUMCLI = cb.NUMCLI  order by NOMCLI, NUMFAC", new OrderRowMapper());			
			TableHeader( s, new String[] { "NOMCLI", "NOPLOC", "LOCALITE", "TOT_PAYE", "NUMFAC", "DEJA_PAYE", "DATE_EMIS" } );	
			for (Order o : orders) {
				TableLine1( s, new String[] { o.NOMCLI, o.NOPLOC, o.LOCALITE, Double.toString( o.TOT_PAYE ), Integer.toString( o.NUMFAC ), Double.toString( o.DEJA_PAYE ), o.DATE_EMIS.toString() }, "order/" +o.Ident );				
				n++;
			}												
			TableFooter( s, null );				
			break;
		}
				
//		String query = "SELECT * FROM EMPLOYEE WHERE ID = ?";
//		Client employee = jdbcTemplate.queryForObject( query, new Object[] { id }, new EmployeeRowMapper());

//		jdbcTemplate.query( "Select NOMCLI, ADRESSE, NOPLOC, LOCALITE, INDNOTEL from Clients order by NOMCLI", (rs, rowNum) -> new Client(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))).forEach(customer -> log.info(customer.toString()));
  							
		
/*		
  jdbcTemplate.query( "Select NOMCLI, ADRESSE, NOPLOC, LOCALITE, INDNOTEL from Clients order by NOMCLI", 
        "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "Josh" },
        (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
    ).forEach(customer -> log.info(customer.toString()));
*/		
			
		s.append( "</div>  " );
		s.append( "</form>" );
		return s.toString();
	}	
	
	
	void ListHeader(StringBuilder b, int id, String tit, String option ) {		
//		s.append( "<form name = \"postdata\"  class=\"w3-container\" onsubmit=\"return postData( '/items', this);\" >" );
		String url = "";
		String txt = "";
		switch (id) {
		case 1:
			url = "/form/client";
			txt = "Client";
			break;
		case 2:
			url = "/form/article";
			txt = "Article";
			break;
		case 3:
			url = "/form/order";
			txt = "Order";
			break;
		}
		b.append( "<form name = \"postdata\"  class=\"w3-container\" onsubmit=\"makeGetRequest('" + url + "')\" >" );
		b.append( "<div class=\"w3-container\">" );
		b.append( "<h2>" + tit + "</h2>" );		
		b.append( option );		
		b.append( "<br>" );
		b.append( "<input type=\"submit\" class=\"w3-btn w3-aqua w3-round-xlarge w3-border\" value=\"Create new " + txt + "\"/><br>" );			
	}
	

	void TableHeader( StringBuilder b, String[] vals ) {
		b.append( "<table class=\"w3-table w3-striped w3-bordered\">" );		
		b.append("<tr>");
		for (String s : vals)		
			b.append("<th>" + s + "</th>");
		b.append("</tr>");
		b.append( System.getProperty("line.separator"));
	}	

	
	void TableLine1( StringBuilder b, String[] vals, String Ident ) {
//		b.append("<tr>");		
		b.append("<tr onclick=\"makeGetRequest('/form/" + Ident + "')\">");
		for (String s : vals)		
			b.append("<td>" + s + "</td>");
//		b.append( "<td><input class=\"w3-input\" type=\"number\" value=\"0\" id=\"" + vals[3] + "\" onclick=\"increment('" + vals[3] + "')\"></td>" );
		b.append("</tr>");
		b.append( System.getProperty("line.separator"));		
	}


	void TableLine2( StringBuilder b, String[] vals, String[] json, String Ident, int count ) {
		b.append("<tr>");		
		b.append("<td><input serialize-as=\"" + Integer.toString( count ) + "." + "checked\" type=\"checkbox\"></td>" );		// 	how to manage ??? <input type="checkbox" id="vehicle1" name="vehicle1" value="Bike">
		for (int i = 0; i < vals.length; i++) 
			b.append("<td onclick=\"makeGetRequest('/form/" + Ident + "')\" serialize-as=\"" + Integer.toString( count ) + "." + json[i] + "\" value=\"" + vals[i] + "\">" + vals[i] + "</td>");
//		b.append( "<td><input class=\"w3-input\" type=\"number\" value=\"0\" id=\"" + vals[3] + "\" onclick=\"increment('" + vals[3] + "')\"></td>" );


		b.append("</tr>");
		b.append( System.getProperty("line.separator"));		
	}


	void TableFooter( StringBuilder b, String[] vals ) {
		if (vals != null) {
			b.append("<tr>");		
			for (String s : vals)		
				if (s.length() == 0)				
					b.append("<td>" + s + "</td>");			
				else
					b.append("<td><b>" + s + "</b></td>");			
			b.append("</tr>");
		}
		b.append( "</table>" );		
		b.append( System.getProperty("line.separator"));
	}


	String renderHtmlFormClient( Client cli, boolean readOnly ) {
		
		// Missing: start date, achats, payee 
		
		StringBuilder s = new StringBuilder();
		s.append( "<form name = \"postdata\" class=\"w3-container\" >");		
		if (cli == null)
			s.append( "<h2>New Client</h2>" );
		else if (readOnly)
			s.append( "<h2>Show modified or new Client Data:</h2>" );			
		else 
			s.append( "<h2>Edit Client Data</h2>" );
				
		s.append( "<div class=\"w3-row-padding\">");	
		oneField( s, "w3-half", "Name", "text", "clientName", cli == null ? "" : cli.NOMCLI, readOnly );
		oneField( s, "w3-half", "Address", "text", "address", cli == null ? "" : cli.ADRESSE, readOnly );
		s.append( "</div>");
		
		s.append( "<div class=\"w3-row-padding\">");	
		oneField( s, "w3-half", "Post Code", "text", "postCode", cli == null ? "" : cli.NOPLOC, readOnly );
		oneField( s, "w3-half", "City", "text", "city", cli == null ? "" : cli.LOCALITE, readOnly );		
		s.append( "</div>");
		
		s.append( "<div class=\"w3-row-padding\">");
		oneField( s, "w3-third", "Telephone Number", "tel", "phone", cli == null ? "" : cli.INDNOTEL, readOnly );
		oneField( s, "w3-third", "Email", "email", "email", cli == null ? "" : cli.EMAIL, readOnly );		
		oneField( s, "w3-third", "Reduction", "number", "reduction", cli == null ? "" : Double.toString( cli.POURCENTS ), readOnly );
		s.append( "</div>" );

		s.append( "<div class=\"w3-row-padding\">");
		oneField( s, "w3-third", "Client since", "date", "since", cli == null || cli.CLI_DEPUIS == null ? "" : cli.CLI_DEPUIS.toString(), readOnly );
		oneField( s, "w3-third", "Total bought", "number", "bought", cli == null ? "" : Double.toString( cli.TOT_ACHATS ), readOnly );		
		oneField( s, "w3-third", "Total paid", "number", "paid", cli == null ? "" : Double.toString( cli.TOT_PAYE ), readOnly );
		s.append( "</div>" );

		s.append( "<div class=\"w3-row-padding\">");
		oneField( s, "", "Comment", "text", "comment", cli == null ? "" : cli.COMCLI, readOnly );				
		s.append( "</div>" );		

		if (!readOnly)
			s.append( "<br><button type=\"submit\" class=\"w3-btn w3-aqua w3-round-xlarge w3-border\" onclick=\"handleSubmit('/client',jsonify())\" >Save</button>" );
		if (cli != null)
			s.append( "<button type=\"button\" class=\"w3-btn w3-aqua w3-round-xlarge w3-border\" onclick=\"handlePostStatus('/scratchpad/client/" + cli.Ident + "')\" >Scratchpad</button>" );		
				
		s.append( "</form>");	// <br> ?? 									
		return s.toString();				
	}


	String renderHtmlFormArticle( Article art, boolean readOnly ) {
		StringBuilder s = new StringBuilder();
		s.append( "<form name = \"postdata\" class=\"w3-container\" >");		
//		s.append( "<form name = \"postdata\" class=\"w3-container\" onsubmit=\"makePostRequest( '/article', this);\" >" );
		if (art == null)
			s.append( "<h2>New Article</h2>" );
		else if (readOnly)
			s.append( "<h2>Show modified or new Article Data:</h2>" );			
		else 
			s.append( "<h2>Edit Article Data</h2>" );

		s.append( "<div class=\"w3-row-padding\">");			
				
		oneField( s, "w3-third", "Article Number", "text", "number", art == null ? "" : art.NUMART, readOnly );
		oneField( s, "w3-third", "Article Code", "text", "code", art == null ? "" : art.CDEART, readOnly );
		oneField( s, "w3-third", "Source", "text", "source", art == null ? "" : art.FOUART, readOnly );		
		s.append( "</div>");

		s.append( "<div class=\"w3-row-padding\">");
		oneField( s, "", "Description", "text", "description", art == null ? "" : art.LIBART, readOnly );				
		s.append( "</div>" );		

		s.append( "<div class=\"w3-row-padding\">");			
		oneField( s, "w3-quarter", "Buy Price USD", "text", "buyUSD", art == null ? "" : Double.toString( art.ACHUSS ), readOnly );
		oneField( s, "w3-quarter", "CHF", "text", "buyCHF", art == null ? "" : Double.toString( art.ACHFRS ), readOnly );
		oneField( s, "w3-quarter", "Sell Price CHF", "text", "sellCHF", art == null ? "" : Double.toString( art.VTEFRS ), readOnly );
		oneField( s, "w3-quarter", "In Stock", "text", "inStock", art == null ? "" : Integer.toString( art.QTEART ), readOnly );
		s.append( "</div>");

		s.append( "<div class=\"w3-row-padding\">");	
		oneField( s, "w3-half", "Turnaround", "text", "turnaround", art == null ? "" : Double.toString( art.CHIFF_AFF ), readOnly );
		oneField( s, "w3-half", "In Stock Since", "date", "created", art == null || art.Created == null ? "" : art.Created.toString(), readOnly );
		s.append( "</div>");			

		if (!readOnly)
			s.append( "<br><button type=\"submit\" class=\"w3-btn w3-aqua w3-round-xlarge w3-border\" onclick=\"handleSubmit('/article',jsonify())\" >Save</button>" );
		if (art != null)
			s.append( "<button type=\"button\" class=\"w3-btn w3-aqua w3-round-xlarge w3-border\" onclick=\"handlePostStatus('/scratchpad/article/" + art.Ident + "')\" >Scratchpad</button>" );		
				
		s.append( "</form>");	// <br> ?? 									
		return s.toString();					
	}


	String renderHtmlFormOrder( Order ord, OrderArticle[] oa, boolean readOnly ) {
		StringBuilder s = new StringBuilder();
		s.append( "<form name = \"postdata\" class=\"w3-container\" >");		
		if (ord == null)
			s.append( "<h2>New Order</h2>" );
		else if (readOnly)
			s.append( "<h2>Show modified or new Order Data:</h2>" );			
		else 
			s.append( "<h2>Edit Order Data</h2>" );
		
		s.append( "<div class=\"w3-row-padding\">");	
		oneField( s, "w3-half", "Name", "text", "clientName", ord == null ? "" : ord.NOMCLI, readOnly );
		oneField( s, "w3-half", "Address", "text", "address", ord == null ? "" : ord.ADRESSE, readOnly );
		s.append( "</div>");
		
		s.append( "<div class=\"w3-row-padding\">");	
		oneField( s, "w3-half", "Post Code", "text", "postCode", ord == null ? "" : ord.NOPLOC, readOnly );
		oneField( s, "w3-half", "City", "text", "city", ord == null ? "" : ord.LOCALITE, readOnly );		
		s.append( "</div>");
		
		s.append( "<div class=\"w3-row-padding\">");
		oneField( s, "w3-third", "Telephone Number", "tel", "phone", ord == null ? "" : ord.INDNOTEL, readOnly );
		oneField( s, "w3-third", "Email", "email", "email", ord == null ? "" : ord.EMAIL, readOnly );		
		oneField( s, "w3-third", "Reduction", "number", "reduction", ord == null ? "" : Double.toString( ord.POURCENTS ), readOnly );
		s.append( "</div>" );

		s.append( "<br><input type=\"submit\" class=\"w3-btn w3-aqua w3-round-xlarge w3-border\" onclick=\"handleSubmit('/order',jsonify())\" value=\"Save\"/>" );
		s.append( "<input type=\"button\" class=\"w3-btn w3-aqua w3-round-xlarge w3-border\" onclick=\"handleSubmit('/form/item',jsonify())\" value=\"Add Item\"/>" );	// Id ??
		if (ord != null)
			s.append( "<button type=\"button\" class=\"w3-btn w3-aqua w3-round-xlarge w3-border\" onclick=\"handlePostStatus('/scratchpad/order/" + ord.Ident + "')\" >Scratchpad</button>" );		
		
// Order items list ....
		s.append( "<h2>Items</h2>" );
		TableHeader( s, new String[] { "QTEART", "NUMART", "LIBART", "VTEFRSA", "VTEFRS" } );	
		double total = 0;
		for (OrderArticle o : oa) {
			TableLine1( s, new String[] { Integer.toString( o.QTEART ), o.NUMART, o.LIBART, Double.toString( o.VTEFRSA ), Double.toString( o.VTEFRS ) }, "item/" + o.Ident );				
			total += o.VTEFRS;
//			n++;
		}												
		TableFooter( s, new String[] { "Total", "", "", "", Double.toString( total ) } );			// Total, etc ....
		
		
		
// QTEART	NUMART	LIBART	VTEFRSA	VTEFRS			
		
			/*
		NUMFAC	INT PRIMARY KEY,
	NUMCLI	int not null,
	PCENT	decimal( 10, 4 ) NOT NULL,
	MODE_PAYMT	CHAR(1) NOT NULL,
	DEJA_PAYE	decimal( 10, 2 ) NOT NULL,
	FRAIS_TRSP	decimal( 10, 2 ) NOT NULL,
	DATE_EMIS	DATE NOT NULL,
	TVA	decimal( 10, 2 ),
*/
		
/*		
		s.append( "<div class=\"w3-row-padding\">");			
		s.append( "<div class=\"w3-half\"><label>First Name</label><input class=\"w3-input\" type=\"text\" placeholder=\"One\"></div>" );
		s.append( "<div class=\"w3-half\"><label>Last Name</label><input class=\"w3-input\" type=\"text\"></div>");
		s.append( "</div>");
		
		s.append( "<div class=\"w3-row-padding\">");
		s.append( "<div class=\"\"><label>Address</label><input class=\"w3-input\" type=\"text\"></div>" );
		s.append( "</div>" );
		s.append( "<div class=\"w3-row-padding\">");
		s.append( "<div class=\"w3-half\"><label>Telephone number</label><input class=\"w3-input\" type=\"text\"></div>" );
		s.append( "<div class=\"w3-half\"><label>Email</label><input class=\"w3-input\" type=\"text\"></div>" );		
		s.append( "</div>" );
*/				
				
		s.append( "</form>");		
		return s.toString();
	}

	
	String renderHtmlFormItem( OrderArticle oa, boolean readOnly ) {
		StringBuilder s = new StringBuilder();
		s.append( "<form name = \"postdata\" class=\"w3-container\" >");		
		if (oa == null)
			s.append( "<h2>New Order</h2>" );
		else if (readOnly)
			s.append( "<h2>Show modified or new Order Data:</h2>" );			
		else 
			s.append( "<h2>Edit Order Data</h2>" );
		
		s.append( "<div class=\"w3-row-padding\">");	
		oneField( s, "w3-half", "Article Number", "text", "number", oa == null ? "" : oa.NUMART, true );
		oneField( s, "w3-half", "Description", "text", "description", oa == null ? "" : oa.LIBART, true);
		s.append( "</div>");
		
		s.append( "<div class=\"w3-row-padding\">");
		oneField( s, "w3-third", "Quantity", "number", "quantity", oa == null ? "" : Integer.toString( oa.QTEART ), readOnly );
		oneField( s, "w3-third", "Price", "number", "price", oa == null ? "" : Double.toString( oa.VTEFRSA ), true );		
		oneField( s, "w3-third", "Total", "number", "total", oa == null ? "" : Double.toString( oa.VTEFRS ), true );
		s.append( "</div>" );
				
		s.append( "<br><input type=\"submit\" class=\"w3-btn w3-aqua w3-round-xlarge w3-border\" onclick=\"handleSubmit('/form/item',jsonify())\" value=\"Save\"/>" );
		s.append( "<input type=\"button\" class=\"w3-btn w3-aqua w3-round-xlarge w3-border\" onclick=\"makeDeleteRequest('/form/item',jsonify())\" value=\"Remove\"/><br>" );
		
		s.append( "</form>");		
		return s.toString();
	}
	
	
	String renderHtmlFormScratchpad( Article art, boolean readOnly ) throws SQLException {
		// Get scratchpad items, decode, generate table 
		StringBuilder s = new StringBuilder();
		s.append( "<form name = \"postdata\" class=\"w3-container\" >");		
		s.append( "<h2>Scratchpad Data</h2>" );
										
		TableHeader( s, new String[] { "Select", "Type", "Key Value", "Description" } );
		jdbcTemplate.query("select * from Scratchpad where UserId = 1", new RowCallbackHandler() {
			public void processRow(ResultSet resultSet) throws SQLException {
				// For heavens sake ! first record is already pointed to, hence usual iteration will "swallow" first record !!
				int cnt = 0;
				do {
					String code = resultSet.getString( "Code" );
					String desc = resultSet.getString( "Description" );
					switch (resultSet.getInt( "Type" )) {
					case 1:					
						TableLine2( s, new String[] { "Client", code, desc }, new String[] { "type", "code", "description" }, "/client/" + code, cnt );			// Use Ident as ref ??
						break;
					case 2:					
						TableLine2( s, new String[] { "Article", code, desc }, new String[] { "type", "code", "description" }, "/article/" + code, cnt );				
						break;
					case 3:
						TableLine2( s, new String[] { "Order", code, desc }, new String[] { "type", "code", "description" }, "/order/" + code, cnt );				
						break;
					}					
					cnt++;
				}
				while (resultSet.next());
			}
		});		
		TableFooter( s, null );	
				
		s.append( "<br><input type=\"submit\" class=\"w3-btn w3-aqua w3-round-xlarge w3-border\" onclick=\"makeDeleteRequest('/scratchpad',jsonify())\" value=\"Remove\"/>" );
		s.append( "<input type=\"button\" class=\"w3-btn w3-aqua w3-round-xlarge w3-border\" onclick=\"handleSubmit('/scratchpad/order',jsonify())\" value=\"Make new Order\"/><br>" );
		
		s.append( "</form>");		
		return s.toString();
	}


	void oneField( StringBuilder s, String partition, String label, String type, String json, String value, boolean ro ) {
		s.append( "<div class=\"" );
		s.append( partition );
		s.append( "\"><label>" );
		s.append( label );
		s.append( "</label><b><input class=\"w3-input\" type=\"" );
		s.append( type );
		s.append( "\" serialize-as=\"" );
		s.append( json );
		if (value != null) {
			s.append( "\" value=\"" );
			s.append( value );
		}				
		s.append( "\"" );
		if (ro)
			s.append( " readonly" );
		s.append( "\" ></b></div>" );
	}


	Item[] parseToItemList( String json ) throws com.fasterxml.jackson.core.JsonProcessingException {	
		List< Item > il = new ArrayList< Item >();		
		int cp = 0;
		while (cp < json.length()) {
			switch (json.charAt( cp )) {
			case ':':
				cp = parseItem( json, cp, il );
				break;
			default:
				break;
			}
			cp++;
		}		
// { "0": { "checked": true, "type": "Client", "code": 322 }, "1": { "checked": true, "type": "Article", "code": 10.6709 }, "2": { "checked": false, "type": "Article", "code": 10.3549 }, ...
// locate ":", take tail, find next ":", convert snippet in between to "Item" 
// accolade, numlabel, ':', <read item>, ',', loop until end
		return il.toArray( new Item[ il.size() ] );
	}


	int parseItem( String json, int cp, List< Item > il ) throws com.fasterxml.jackson.core.JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();				
		String v = json.substring( cp + 1, json.indexOf( '}', cp + 1 ) + 2 );
		System.out.println( "################### Data:" + v );
		Item item = objectMapper.readValue(v, Item.class);
		il.add( item );
		return json.indexOf( '}', cp + 1 ) + 2;		
	}
	
	
	String toItemIdList( Item[] il, boolean ignoreChecked ) {
		StringBuilder s = new StringBuilder();
		for (Item i : il) {
			if (!ignoreChecked && !i.checked)
				continue;
			s.append( i.Ident );
			s.append( ',' );
		}
		if (s.length() > 0)
			s.deleteCharAt( s.length() - 1 );		// Remove last ','
		return s.toString();
	}
	
	
			
}

/*
@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(EmployeeRepository repository) {

    return args -> {
      log.info("Preloading " + repository.save(new Employee("Bilbo Baggins", "burglar")));
      log.info("Preloading " + repository.save(new Employee("Frodo Baggins", "thief")));
    };
  }
  
  import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

constructor:
 EmployeeController(EmployeeRepository repository) {
    this.repository = repository;
  }
  
  
@PutMapping("/employees/{id}")
  Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
  
}*/

/*
https://www.baeldung.com/spring-jdbc-jdbctemplate

int result = jdbcTemplate.queryForObject(
    "SELECT COUNT(*) FROM EMPLOYEE", Integer.class);
	
 return jdbcTemplate.update(
      "INSERT INTO EMPLOYEE VALUES (?, ?, ?, ?)", id, "Bill", "Gates", "USA");

public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();

        employee.setId(rs.getInt("ID"));
        employee.setFirstName(rs.getString("FIRST_NAME"));
        employee.setLastName(rs.getString("LAST_NAME"));
        employee.setAddress(rs.getString("ADDRESS"));

        return employee;
    }
}



public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/hello").setViewName("hello");
		registry.addViewController("/login").setViewName("login");
	}




package com.example.securingweb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/", "/home").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.permitAll();
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		UserDetails user =
			 User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(user);
	}
}

*/