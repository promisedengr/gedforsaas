package ma.project.GedforSaas.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.project.GedforSaas.abstractSupperClasses.AbstractSupperClass;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ConfirmationToken extends AbstractSupperClass {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9031769415590864548L;

	/**
	 * 
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(nullable = false)
	private String token;

	@Column(nullable = false)
	private LocalDateTime expiresAt;

	private LocalDateTime confirmedAt;

	@ManyToOne
	@JoinColumn(nullable = false, name = "user_id")
	@JsonIgnore
	private User user;

	public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, User user) {
		this.token = token;
		this.expiresAt = expiresAt;
		this.user = user;
	}

}
