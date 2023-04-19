package ma.project.GedforSaas.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.project.GedforSaas.abstractSupperClasses.AbstractSupperClass;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TemplateConfig  extends AbstractSupperClass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "template_id")
	private Long id;
	private String name;
	private String titre;
	private String description;
	private String nature;
	private String rendu;
	private boolean auth_surchage ;
	private boolean doc_cible;
	private String motor_gen;
	private String format ;
	@ManyToOne
	private Company company;
	@ManyToMany
	private List<DocType> docTypes;
}
