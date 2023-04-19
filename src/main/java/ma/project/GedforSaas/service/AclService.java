package ma.project.GedforSaas.service;

import ma.project.GedforSaas.model.Document;
import ma.project.GedforSaas.model.Folder;
import ma.project.GedforSaas.repository.AclRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AclService {

    @Autowired
    private AclRepository aclRepository;

    /**
     * *********************************************** INSERT METHODS ******************************************************
     */

    public void insert_acl_entry(String acl_object_identity, Integer aceOrder, Integer sid, Integer mask, boolean granting) {
        aclRepository.insert_acl_entry(Integer.valueOf(acl_object_identity), aceOrder, sid, mask, granting);
    }


    public void insert_acl_class(String acl_object_class) {
        aclRepository.insert_acl_class(acl_object_class);
    }


    public void insert_acl_sid(String principal, String sid) {
        aclRepository.insert_acl_sid(true, sid);
    }


    public void insert_acl_object_identity(String object_id_class, String object_id_identity, String parent_object,
                                           String owner_sid, boolean entries_inheriting) {
        if (parent_object == null) {
            aclRepository.insert_acl_object_identity_for_null_parent(
                    Integer.valueOf(object_id_class),
                    object_id_identity,
                    Integer.valueOf(owner_sid),
                    entries_inheriting);
        } else {
            aclRepository.insert_acl_object_identity(Integer.valueOf(object_id_class), object_id_identity, Integer.valueOf(parent_object),
                    Integer.valueOf(owner_sid), entries_inheriting);
        }
    }

    /**
     * *********************************************** GET METHODS ******************************************************
     */

    public List<Object> get_acl_entry() {
        return aclRepository.get_acl_entry();
    }


    public Integer get_acl_class_by_class(String acl_object_class) {
        return aclRepository.get_acl_class_by_class(acl_object_class);
    }

    public List<Object> get_acl_class() {
        return aclRepository.get_acl_class();
    }


    public List<Object> get_acl_sid() {
        return aclRepository.get_acl_sid();
    }


    public List<Object> get_acl_object_identity() {
        return aclRepository.get_acl_object_identity();
    }


    public List<Object> get_acl_entry_using_sid(Integer sid) {
        return aclRepository.get_acl_entry_using_sid(sid);
    }

    public Integer get_acl_entry_by_acl_object_identity_and_sid(String acl_object_identity, String sid) {
        return aclRepository.get_acl_entry_by_acl_object_identity_and_sid(Integer.valueOf(acl_object_identity),Integer.valueOf(sid));
    }

    public void delete_obj_identity(Integer sid) {
		aclRepository.delete_obj_identity(sid);
	}


	public Integer get_acl_object_by_object_id_class_and_object_id_identity(String object_id_class, String object_id_identity) {
        return aclRepository.get_acl_object_by_object_id_class_and_object_id_identity(Integer.valueOf(object_id_class), object_id_identity);
    }

    public List<Integer> get_acl_object_identity_in_acl_entry_using_sid(Integer sid) {
		return aclRepository.get_acl_object_identity_in_acl_entry_using_sid(sid);
	}


	public Integer get_acl_sid_by_sid(String sid) {
        return aclRepository.get_acl_sid_by_sid(sid);
    }

    public List<Object> get_acl_entry_by_acl_object_identity(String acl_object_identity) {
        return aclRepository.get_acl_entry_by_acl_object_identity(Integer.valueOf(acl_object_identity));
    }


    public Object get_acl_object(String object_id_class, String object_id_identity) {
        return aclRepository.get_acl_object(Integer.valueOf(object_id_class), object_id_identity);
    }

    public Integer get_acl_object_by_object_id_identity(String object_id_identity, Integer acl_class_id) {
        return aclRepository.get_acl_object_by_object_id_identity(object_id_identity, acl_class_id);
    }

    public Object get_acl_object_by_id(String id) {
        return aclRepository.get_acl_object_by_id(Integer.valueOf(id));
    }

    /**
     * *********************************************** SET OBJECT PERMISSION ******************************************************
     */

    @PreAuthorize("hasPermission(#folder, 'ADMINISTRATION')")
    public boolean set_permission_for_folders_object(Folder folder, String username, Integer mask) {
        Integer acl_class_id = this.get_acl_class_by_class("ma.project.GedforSaas.folder.Folder");
        Integer sid = this.get_acl_sid_by_sid(username);
        Integer acl_object_id = this.get_acl_object_by_object_id_class_and_object_id_identity(String.valueOf(acl_class_id), String.valueOf(folder.getId()));
        System.out.println("============================================================");
        System.out.println(sid);
        System.out.println(acl_object_id);
        List<Object> objectList = this.get_acl_entry_by_acl_object_identity(String.valueOf(acl_object_id));
        this.insert_acl_entry(String.valueOf(acl_object_id), objectList.size() + 1, sid, mask, true);
        return true;
    }

    @PreAuthorize("hasPermission(#document, 'ADMINISTRATION')")
    public boolean set_permission_for_docs_object(Document document, String username, Integer mask) {
        Integer acl_class_id = this.get_acl_class_by_class("ma.project.GedforSaas.document.Document");
        Integer sid = this.get_acl_sid_by_sid(username);
        Integer acl_object_id = this.get_acl_object_by_object_id_class_and_object_id_identity(String.valueOf(acl_class_id), String.valueOf(document.getId()));
        System.out.println("============================================================");
        System.out.println(sid);
        System.out.println(acl_object_id);
        List<Object> objectList = this.get_acl_entry_by_acl_object_identity(String.valueOf(acl_object_id));
        this.insert_acl_entry(String.valueOf(acl_object_id), objectList.size() + 1, sid, mask, true);
        return true;
    }


    /**
     * *********************************************** UPDATE OBJECT PERMISSION ******************************************************
     */

    public int update_granting_acl_entry(Integer id, boolean granting) {
        return aclRepository.update_granting_acl_entry(id, granting);
    }


    public int update_entries_inheriting_acl_object_identity(Integer id, boolean entries_inheriting) {
        return aclRepository.update_entries_inheriting_acl_object_identity(id, entries_inheriting);
    }


    public int update_permission(Integer id, Integer mask) {
        return aclRepository.update_permission(id, mask);
    }



    
    /*
     * Delete method
     *
     */
    public void delete_acl_sid(String email) {
		aclRepository.delete_acl_sid(email);
	}


	public void delete_acl_entry(Integer sid) {
		aclRepository.delete_acl_entry(sid);
	}



    
}
