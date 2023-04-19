package ma.project.GedforSaas.abstractSupperClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractSupperClass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@CreationTimestamp
	@Column
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.sql.Timestamp dateCreated;

	@UpdateTimestamp
	@Column
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.sql.Timestamp dateModified;

	@CreatedBy
	protected String createdBy;

	@LastModifiedBy
	protected String lastModifiedBy;

	@ElementCollection
	protected List<String> listContributors = new ArrayList<String>();

	@PrePersist
	public void onPrePersist() {
		getListContributors().add(getCreatedBy());
	}

	@PreUpdate
	public void onPreUpdate() {
		getListContributors().add(getLastModifiedBy());
	}

//	@PreRemove
//	public void onPreRemove() {
//		System.out.println("\n\n\n\n\n\n\n\n\n\nffffffffff");
//		getListContributors().add(getLastModifiedBy());
//	}

}
