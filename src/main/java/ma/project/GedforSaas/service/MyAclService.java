package ma.project.GedforSaas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import ma.project.GedforSaas.model.Document;


@Service
@Transactional
public class MyAclService {

    protected static final int LEVEL_NEGATE_READ = 0;
    protected static final int LEVEL_GRANT_READ = 1;
    protected static final int LEVEL_GRANT_WRITE = 2;
    protected static final int LEVEL_GRANT_ADMIN = 3;
    private MutableAclService aclService;

    public MyAclService(MutableAclService aclService) {
        this.aclService = aclService;
    }

    public void init(Long idP, Long idC) {

        ObjectIdentityImpl parentId = new ObjectIdentityImpl(Document.class, idP);

        MutableAcl parentAcl = this.aclService.createAcl(parentId);
        parentAcl.insertAce(parentAcl.getEntries().size(), BasePermission.READ,
                new PrincipalSid(SecurityContextHolder.getContext().getAuthentication()), true);
        this.aclService.updateAcl(parentAcl);


        ObjectIdentityImpl ChildId = new ObjectIdentityImpl(Document.class, idC);
        MutableAcl childAcl = this.aclService.createAcl(ChildId);


        childAcl.setParent(parentAcl); // set parent here
        this.aclService.updateAcl(childAcl);
    }

    public void createObjectIdentity() {

        ObjectIdentity objectIdentity = new ObjectIdentityImpl(Document.class, 3L);
        MutableAcl acl = this.aclService.createAcl(objectIdentity);
        System.out.println("acl = " + acl);
    }

    public void findAcl() {

        ObjectIdentity objectIdentity = new ObjectIdentityImpl(Document.class, 1L);
        Acl acl = aclService.readAclById(objectIdentity);
        System.out.println("acl = " + acl);
    }

    public void addPermission(String recipient, int level, Long id) {

//		 We need to construct an ACL-specific Sid. Note the prefix contract is defined
//		 on the superclass method's JavaDocs
        Sid sid = null;
        if (recipient.startsWith("ROLE_")) {
            sid = new GrantedAuthoritySid(recipient);
        } else {
            sid = new PrincipalSid(recipient);
        }
        // Next we need to create a Permission
        org.springframework.security.acls.model.Permission permission = null;
        if (level == LEVEL_NEGATE_READ || level == LEVEL_GRANT_READ) {
            permission = BasePermission.READ;
        } else if (level == LEVEL_GRANT_WRITE) {
            permission = BasePermission.WRITE;
        } else if (level == LEVEL_GRANT_ADMIN) {
            permission = BasePermission.ADMINISTRATION;
        } else {
            throw new IllegalArgumentException("Unsupported LEVEL_");
        }
        // Search for the ACL to update and
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(Document.class, id);

        // Attempt to retrieve the existing ACL, creating an ACL if it doesn't already
        // exist for this ObjectIdentity
        MutableAcl acl = null;
        try {
            acl = (MutableAcl) this.aclService.readAclById(objectIdentity);
        } catch (org.springframework.security.acls.model.NotFoundException nfe) {
            acl = this.aclService.createAcl(objectIdentity);
            Assert.notNull(acl, "Acl could not be retrieved or created");
        }


        //MutableAcl acl = (MutableAcl) aclService.readAclById(objectIdentity);

        acl.insertAce(acl.getEntries().size(), permission,
                sid, level != LEVEL_NEGATE_READ);
        this.aclService.updateAcl(acl);

        System.out.println("acl = " + acl);
    }

}

