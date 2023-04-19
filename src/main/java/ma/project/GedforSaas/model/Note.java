package ma.project.GedforSaas.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.project.GedforSaas.abstractSupperClasses.AbstractSupperClass;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Note extends AbstractSupperClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Column(length = 1000000)
    private String content;
    private String image;
    private boolean favorite;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<NoteLabel> labels;


    @OneToMany(mappedBy = "note", cascade = CascadeType.REMOVE)
    private List<NoteTask> tasks;

    @ManyToOne
    @JoinColumn(name = "ressource_id", nullable = true)
    @Nullable
    private Resource resource;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
