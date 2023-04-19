package ma.project.GedforSaas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.project.GedforSaas.abstractSupperClasses.AbstractSupperClass;

import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User extends AbstractSupperClass implements UserDetails {

    /**
     *
     */
    private static final long serialVersionUID = 100L;

    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence",
            allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @Column(name = "user_id")
    private Long id;

    private String firstName;

    private String lastName;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    private Date birthDate;

    private String workspace;
    private String customerId;

    @Column(length = 10000)
    private String about;
    private String language;
    private String country;
    private String city;
    private String title;
    @Column(length = 10000)
    private String information;

    private boolean twoFactorAuth = false;
    private boolean changePasswordEverySixMonths = false;
    private String secret;

    private String signature;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nextDateChangePassword;
    private String certificat;
    private boolean active = true;
    private boolean confirmed = true;
    private String phoneNumber;
    private String postalCode;
    private String address;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ConfirmationToken> confirmationTokens;

    @ManyToMany(fetch = EAGER)
    private Collection<Role> authorities;


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<GroupUserAssociations> groupUserAssociationsList;




    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<UserCompany> userCompanyList;



    @ManyToMany
    @JoinTable(name = "Permission_User_Associations", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Override
    public Collection<Role> getAuthorities() {
        return authorities;
    }

    public String getUsername() {
        return email;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return active;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return confirmed;
    }

    public User(Long id, String firstName, String lastName, String email, Date birthDate, String workspace,
                boolean twoFactorAuth, String signature, String certificat, boolean active, boolean confirmed,
                String phoneNumber, List<Permission> permissions, Plan plan, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.workspace = workspace;
        this.twoFactorAuth = twoFactorAuth;
        this.signature = signature;
        this.certificat = certificat;
        this.active = active;
        this.confirmed = confirmed;
        this.phoneNumber = phoneNumber;
        this.permissions = permissions;
        this.plan = plan;
        this.role = role;
    }

    public User(Long id, String firstName, String lastName, String email, String password, Date birthDate,
                String workspace, boolean twoFactorAuth, String signature, String certificat, boolean active,
                boolean confirmed, String phoneNumber, List<Permission> permissions, Plan plan, Role role) {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isChangePasswordEverySixMonths() {
        return changePasswordEverySixMonths;
    }

    public void setChangePasswordEverySixMonths(boolean changePasswordEverySixMonths) {
        this.changePasswordEverySixMonths = changePasswordEverySixMonths;
    }
}
