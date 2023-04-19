package ma.project.GedforSaas.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String reference;
    private String message;
    private Long unreadCount;
    private Date createdAt;
    private boolean muted;
    private String lastMessage;
    private Date lastMessageAt;
    private String type;
    @ManyToOne
    private User sender;
    @ManyToOne
    private User receiver;
}

