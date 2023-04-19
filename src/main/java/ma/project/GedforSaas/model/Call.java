package ma.project.GedforSaas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "calls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Call {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "plan_id")
	private Long id;
	private String title;
	private String details;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date date;
	private String calledBy ;
	private String resourceCalled;
	private boolean favorite;
	@ManyToOne
	@JoinColumn(name = "ressource_id", nullable = true)
	@Nullable
	private Resource resource;
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
	

}
