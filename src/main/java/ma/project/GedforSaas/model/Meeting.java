package ma.project.GedforSaas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// import ma.project.GedforSaas.user.User;
import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") //2022-06-10 18:00:00
    private Date start;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date end;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private boolean allDay;
    private boolean interactive;
    private boolean favorite;
    private String groupId;
    private String title;
    private String url;
    @ManyToOne
    private Resource resource;

    @ManyToOne
    private Company company;
}
