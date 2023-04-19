package ma.project.GedforSaas.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.project.GedforSaas.utils.UserRevisionListener;

@Table(name = "app_user_rev_entity")
@Entity
@RevisionEntity(UserRevisionListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRevEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_rev_generator")
	@SequenceGenerator(name = "user_rev_generator", allocationSize = 10, sequenceName = "app_userrev_seq")
	@RevisionNumber
	private int id;

	@RevisionTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "user_id")
	private Long userId;

}
