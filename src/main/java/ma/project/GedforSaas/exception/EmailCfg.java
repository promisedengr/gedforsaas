package ma.project.GedforSaas.exception;

import lombok.*;
import ma.project.GedforSaas.model.Company;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailCfg {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String mailServer;
	private String password;
	private String username;
	private boolean authRequired;
	private boolean sslRequired;
	private Long portNumber;
	@OneToOne
	private Company company;
}

