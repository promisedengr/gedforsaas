package ma.project.GedforSaas.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.project.GedforSaas.abstractSupperClasses.AbstractSupperClass;

import org.springframework.content.cmis.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "folder")
@Data
@NoArgsConstructor
@AllArgsConstructor
@CmisFolder
public class Folder extends AbstractSupperClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private Long id;

    @CmisName
    private String name;

    @CmisDescription
    private String description;

    private String path;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @CmisReference(type = CmisReferenceType.Parent)
    private Folder folder;

    private Long parentFolderId;

    private boolean trash = false;

    @ManyToOne
    private DocType docType;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @Version
    private Long vstamp;
    @CmisReference(type = CmisReferenceType.Child)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "folder", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Document> documents = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;

    public Folder(String name) {
        super();
        this.name = name;
    }
}
