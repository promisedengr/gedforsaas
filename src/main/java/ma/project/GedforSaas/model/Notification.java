package ma.project.GedforSaas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long parentId;
    private String userEmail;
    private String resourceEmail;

    private String docName;
    private Long docId;

    private Long groupId;
    private String groupName;
    private String icon;
    private String title;
    private String type;
    private String action;
    private String description;
    private String link;
    private boolean useRouter;
    private boolean read;
    private boolean active;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    @ManyToOne
    private Company company;

}
