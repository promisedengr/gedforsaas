package ma.project.GedforSaas.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.project.GedforSaas.abstractSupperClasses.AbstractSupperClass;
import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.DocType;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.content.cmis.*;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;
import org.springframework.versions.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Audited
@Entity
@Table(name = "document")
@Data
@NoArgsConstructor
@AllArgsConstructor
@CmisDocument
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Document extends AbstractSupperClass {


    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long id;


    @CmisName
    private String name;
    @Version
    private Long vstamp;
    private String statut;
    private boolean favorite;

    private boolean trash = false;

    private String type;

    @Column(length = 1000000)
    private String content;

    @NotAudited
    @ManyToOne
    @CmisReference(type = CmisReferenceType.Parent)
    private Folder folder;


    @NotAudited
    @ManyToOne
    private DocType doctype;


    @NotAudited
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;


    @NotAudited
    @OneToOne
    @JoinColumn(name = "filedb_id")
    private FileDB principaleFile;

    @NotAudited
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_id")
    private List<FileDB> secondaryFiles;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;


    @NotAudited
    @ManyToOne
    private NoteLabel label;

    @ContentId
    private UUID contentId;

    @ContentLength
    private Long contentLen;

    @MimeType
    private String contentMimeType;

    @LockOwner
    private String lockOwner;

    @CmisDescription
    private String description;

    @AncestorId
    private Long ancestorId;

    @AncestorRootId
    private Long ancestralRootId;

    @SuccessorId
    private Long successorId;

    @VersionNumber
    private String versionNumber = "0.0";

    @VersionLabel
    private String versionLabel;


    public Document(String name, String statut, String content, DocType docType) {
        super();
        this.name = name;
        this.statut = statut;
        this.content = content;
    }


}
