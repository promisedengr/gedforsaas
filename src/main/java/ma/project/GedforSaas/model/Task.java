package ma.project.GedforSaas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Audited
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;
    private String name; // title
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated; // generated automatically
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;

    private LocalDateTime customReminderDate;
    private double rappel; //by hours
    private String typeRappel; // TO_DO || Email || Call
    private String description;
    private String status; // Complete || In_progress || Draft
    private String relation; // *TypeResource => Resource  *TypeDocument => Document
    private double repete;
    private boolean custom; // si l'utilsateur clique sur Custom, il peut definir la date et l'heure du rappel
    private boolean reminded; //true if the notification send to user
    private String priority;// high | low | normal
    private boolean favorite;


    @NotAudited
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "ressource_id", nullable = true)
    @Nullable
    private Resource resource;
}
