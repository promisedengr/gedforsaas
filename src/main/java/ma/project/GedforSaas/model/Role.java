package ma.project.GedforSaas.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ma.project.GedforSaas.abstractSupperClasses.AbstractSupperClass;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Role extends AbstractSupperClass implements GrantedAuthority {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Long id;

	private String name;

	@JsonIgnore
	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	private List<User> user;

	public Role(String name) {
		this.name = name;
	}

	public Role(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String getAuthority() {
		return name;
	}
}
