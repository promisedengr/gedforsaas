package ma.project.GedforSaas;

import ma.project.GedforSaas.security.SpringSecurityAuditorAware;
import ma.project.GedforSaas.service.TemplateFileStorageServiceImpl;
import ma.project.GedforSaas.utils.AclPermissionEvaluator;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.content.cmis.EnableCmis;
import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.versions.jpa.config.JpaLockingAndVersioningConfig;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.Resource;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
@Configuration
@EnableWebMvc
//CMIS CONFIG
@EnableCmis(basePackages = "ma.project.GedforSaas",
        id = "1",
        name = "spring-content-with-cmis",
        description = "Spring Content CMIS Getting Started Guide",
        vendorName = "Spring Content OSS",
        productName = "Spring Content CMIS Connector",
        productVersion = "1.0.0")
@Import(JpaLockingAndVersioningConfig.class)
@EnableJpaRepositories(
        basePackages={  "ma.project.GedforSaas",
                "org.springframework.versions"},
        considerNestedRepositories=true)
@EnableFilesystemStores
public class GedForSaasApplication extends SpringBootServletInitializer {
    // CreateByReplacingPlaceholderText.replaceWord();
    @Resource
    TemplateFileStorageServiceImpl storageService;

    public static void main(String[] args)  {
        SpringApplication.run(GedForSaasApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new SpringSecurityAuditorAware();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Bean
    public AclPermissionEvaluator aclPermissionEvaluator() {
        return new AclPermissionEvaluator();
    }
}
// Swagger link : http://localhost:8050/swagger-ui/index.html#