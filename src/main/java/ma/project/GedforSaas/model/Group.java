package ma.project.GedforSaas.model;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.project.GedforSaas.abstractSupperClasses.AbstractSupperClass;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Group  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @NotAudited
    private Company company;


    @OneToMany(mappedBy = "group",  fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @NotAudited
    private List<GroupUserAssociations> groupUserAssociationsList;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    @NotAudited
    private Permission permission;
}
