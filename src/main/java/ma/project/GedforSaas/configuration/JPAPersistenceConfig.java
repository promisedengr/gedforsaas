 package ma.project.GedforSaas.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ma.project.GedforSaas")
@PropertySource("classpath:application.properties")
@EntityScan(basePackages={ "ma.project.GedforSaas" })
public class JPAPersistenceConfig {

}
