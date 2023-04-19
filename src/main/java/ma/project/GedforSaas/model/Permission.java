package ma.project.GedforSaas.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ma.project.GedforSaas.abstractSupperClasses.AbstractSupperClass;

@Entity
@Table(name = "permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Permission extends AbstractSupperClass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "permission_id")
	private Long id;

	private Long isFolder;
	private String permission;
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@ManyToMany
	@JoinTable(name = "Document_Permission_Associations", joinColumns = @JoinColumn(name = "permission_id"), inverseJoinColumns = @JoinColumn(name = "document_id"))
	private List<Document> documents = new ArrayList<>();

	@OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
	private List<Folder> folder = new ArrayList<Folder>();

	@ManyToMany
	@JoinTable(name = "Permission_User_Associations", joinColumns = @JoinColumn(name = "permission_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> users = new ArrayList<>();

	@OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
	private List<Group> group = new ArrayList<Group>();

	public Permission(Long isFolder, String permission) {
		super();
		this.isFolder = isFolder;
		this.permission = permission;

	}

}
