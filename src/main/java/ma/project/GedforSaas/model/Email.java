package ma.project.GedforSaas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "emails")
@Audited
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    private String type;
    @ManyToOne
    @NotAudited
    private User fromUser;
    @ManyToOne
    @NotAudited
    private Resource toUser;
    private String cc;
    private Long ccCount;
    private String bcc;
    private Long bccCount;
    @Column(name = "date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @Column(name = "Valuesubject")
    private String subject;
    @Column(length = 1000000)
    private String content;
    @Column(length = 1000000)
    private String description;
    private String folder;
    private Boolean starred;
    private Boolean important;
    private Boolean unread;

    @NotAudited
    @JoinColumn(name = "company_id")
    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "email")
    @NotAudited
    private List<EmailLabel> emailLabel;
    @OneToMany(mappedBy = "email")
    @NotAudited
    private List<Attachments> attachments;

}
