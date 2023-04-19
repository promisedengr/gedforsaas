package ma.project.GedforSaas.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCompany implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 100L;

	@Id
	@SequenceGenerator(name = "userCompany_sequence", sequenceName = "userCompany_sequence",
			allocationSize = 1, initialValue = 100)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userCompany_sequence")
	private Long id;

	@ManyToOne
	@JsonIgnore
	private User user;

	@ManyToOne
	private Company company;

	@ManyToOne
	private Role role;

}
