package ma.project.GedforSaas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.project.GedforSaas.abstractSupperClasses.AbstractSupperClass;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "template")
@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "@template_id")
public class Template extends AbstractSupperClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long id;

    private String name;

    // category on soukina
    @ManyToMany
    private List<DocType> docTypeList;

    private String description;

    @Column(nullable = false)
    private boolean defaultType = false;

    @Column(length = 1000000)
    private String contentJson;

    private String version;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;



    public Template(String name, DocType docType, String contentJson, String version) {
        this.name = name;
        this.contentJson = contentJson;
        this.version = version;
    }

    public Template(String name, String contentJson) {
        this.name = name;
        this.contentJson = contentJson;
    }

}
