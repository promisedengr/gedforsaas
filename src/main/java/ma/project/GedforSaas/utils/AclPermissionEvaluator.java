package ma.project.GedforSaas.utils;

// import ma.project.GedforSaas.model.Document;
import ma.project.GedforSaas.model.Document;
import ma.project.GedforSaas.model.Folder;
import ma.project.GedforSaas.repository.AclRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Used by Spring Security's expression-based access control implementation to evaluate
 * permissions for a particular object using the ACL module. Similar in behaviour to
 * {@link org.springframework.security.acls.AclEntryVoter AclEntryVoter}.
 *
 * @author Luke Taylor
 * @since 3.0
 */
public class AclPermissionEvaluator implements PermissionEvaluator {
    @Autowired
    private AclRepository aclRepository;
    Map<String, Integer> permissions = Stream.of(new Object[][]{
            {"READ", 1},
            {"WRITE", 2},
            {"CREATE", 4},
            {"DELETE", 8},
            {"ADMINISTRATION", 16},
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

    public AclPermissionEvaluator() {

    }

    /**
     * Determines whether the user has the given permission(s) on the domain object using
     * the ACL configuration. If the domain object is null, returns false (this can always
     * be overridden using a null check in the expression itself).
     */
    @Override
    public boolean hasPermission(
            Authentication auth, Object targetDomainObject, Object permission) {
        System.out.println("HAS_PERMISSIONS");
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }
        String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();
        System.out.println(targetType);
        return hasPrivilege(auth, targetType, permission.toString().toUpperCase());
    }

    @Override
    public boolean hasPermission(
            Authentication auth, Serializable targetId, String targetType, Object permission) {
        System.out.println("HAS_PERMISSIONS2");
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(auth, targetType.toUpperCase(),
                permission.toString().toUpperCase());
    }

    public boolean hasPermission(Object object, String permission) {
        Long ID;
        Integer acl_class_id;
        Integer sid = getSid();
        Integer permissionMask = this.permissions.get(permission);
        if (object instanceof Folder) {

            Folder folder = (Folder) object;
            ID = folder.getId();
            System.out.println("------FOLDER------- :" + folder.getName());
            acl_class_id = this.aclRepository.get_acl_class_by_class("ma.project.GedforSaas.folder.Folder");
        } else {
            Document document = (Document) object;
            ID = document.getId();
            System.out.println("------DOCUMENT------- :" + document.getName());
            acl_class_id = this.aclRepository.get_acl_class_by_class("ma.project.GedforSaas.document.Document");
        }
        Integer objectId = aclRepository.get_acl_object_by_object_id_class_and_object_id_identity(acl_class_id, String.valueOf(ID));
        List<Integer> masks = aclRepository.get_masks_acl_entry_by_acl_object_identity(objectId, sid);
        System.out.println(masks);
        System.out.println(permissionMask);
        for (Integer i : masks
        ) {
            if (i.equals(permissionMask) || i > permissionMask) {
                return true;
            }
        }
        return false;
    }

    private Integer getSid() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        System.out.println(username);
        return aclRepository.get_acl_sid_by_sid(username);
    }

    private boolean hasPrivilege(Authentication auth, String targetType, String permission) {

        for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
            if (grantedAuth.getAuthority().startsWith(targetType) &&
                    grantedAuth.getAuthority().contains(permission)) {
                return true;
            }
        }
        return false;
    }
}
