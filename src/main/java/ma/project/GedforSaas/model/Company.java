package ma.project.GedforSaas.model;

// import java.util.ArrayList;
// import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.project.GedforSaas.abstractSupperClasses.AbstractSupperClass;
// import ma.project.GedforSaas.folder.Folder;
// import ma.project.GedforSaas.ressource.Resource;
// import ma.project.GedforSaas.template.Template;

@Entity
@Table(name = "company")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company extends AbstractSupperClass {
	private static final long serialVersionUID = 100L;
	@Id
	@SequenceGenerator(name = "company_sequence", sequenceName = "company_sequence",
			allocationSize = 1, initialValue = 100)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_sequence")
	@Column(name = "company_id")
	private Long id;
	private String name;
	private String adress;
	private String city;
	private String country;
	private String campagnyName;
	private String phoneNumber;
	private Long businessNumber;
}

