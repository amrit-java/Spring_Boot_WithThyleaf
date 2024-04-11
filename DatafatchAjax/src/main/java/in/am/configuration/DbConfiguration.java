package in.am.configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfiguration {

	@Value("${db.url}")
	private String url;

	@Value("${db.username}")
	private String userName;

	@Value("${db.password}")
	private String password;

	
	
	  @Bean
	    public Connection getConnection() {
	        try {
	            Class.forName("org.postgresql.Driver");
	            return DriverManager.getConnection(url, userName, password);
	        } catch (ClassNotFoundException | SQLException e) {
	            throw new RuntimeException("Failed to create a database connection.", e);
	        }
	    }

}
