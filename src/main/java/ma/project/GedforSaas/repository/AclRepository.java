package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ma.project.GedforSaas.model.Folder;

import java.util.List;

public interface AclRepository extends JpaRepository<Folder, Long> {

	/***
	 * @author ELmoudene_Youssef
	 *
	 *         ACL-ENTRY
	 * @param acl_object_identity
	 * @param sid
	 * @param granting
	 */

	@Modifying
	@Query(value = "INSERT INTO acl_entry(acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (:acl_object_identity,:ace_order,:sid,:mask,:granting,true,true)", nativeQuery = true)
	void insert_acl_entry(@Param("acl_object_identity") Integer acl_object_identity,
			@Param("ace_order") Integer ace_order, @Param("sid") Integer sid, @Param("mask") Integer mask,
			@Param("granting") boolean granting);

	@Modifying
	@Query(value = "update acl_entry  set granting = :granting where id = :id", nativeQuery = true)
	int update_granting_acl_entry(@Param("id") Integer id, @Param("granting") boolean granting);

	@Modifying
	@Query(value = "update acl_entry  set mask = :mask where id = :id", nativeQuery = true)
	int update_permission(@Param("id") Integer id, @Param("mask") Integer mask);

	@Query(value = "SELECT id FROM acl_entry   WHERE acl_object_identity = :acl_object_identity And sid = :sid", nativeQuery = true)
	Integer get_acl_entry_by_acl_object_identity_and_sid(@Param("acl_object_identity") Integer acl_object_identity,
			@Param("sid") Integer sid);



	@Query(value = "SELECT * FROM acl_entry   WHERE acl_object_identity = :acl_object_identity", nativeQuery = true)
	List<Object> get_acl_entry_by_acl_object_identity(@Param("acl_object_identity") Integer acl_object_identity);


	@Query(value = "SELECT mask FROM acl_entry   WHERE acl_object_identity = :acl_object_identity  And sid = :sid", nativeQuery = true)
	List<Integer> get_masks_acl_entry_by_acl_object_identity(@Param("acl_object_identity") Integer acl_object_identity, @Param("sid") Integer sid );



	@Modifying
	@Query(value = "SELECT * FROM acl_entry", nativeQuery = true)
	List<Object> get_acl_entry();

	@Modifying
	@Query(value = "SELECT * FROM acl_entry  WHERE sid = :sid", nativeQuery = true)
	List<Object> get_acl_entry_using_sid(@Param("sid") Integer sid);
	

	@Modifying
	@Query(value = "SELECT acl_object_identity FROM acl_entry  WHERE sid = :sid", nativeQuery = true)
	List<Integer> get_acl_object_identity_in_acl_entry_using_sid(@Param("sid") Integer sid);

	/**
	 * ACL_CLASS
	 *
	 * @param acl_object_class
	 */

	@Modifying
	@Query(value = "INSERT INTO acl_class(class) VALUES (:acl_object_class)", nativeQuery = true)
	void insert_acl_class(@Param("acl_object_class") String acl_object_class);

	@Modifying
	@Query(value = "SELECT * FROM acl_class", nativeQuery = true)
	List<Object> get_acl_class();

	@Query(value = "SELECT id FROM acl_class   WHERE class = :acl_object_class ", nativeQuery = true)
	Integer get_acl_class_by_class(@Param("acl_object_class") String acl_object_class);

	/**
	 * ALC_SID
	 *
	 * @param principal
	 * @param sid
	 */

	@Modifying
	@Query(value = "INSERT INTO acl_sid(principal, sid) VALUES (:principal,:sid)", nativeQuery = true)
	void insert_acl_sid(@Param("principal") boolean principal, @Param("sid") String sid);

	@Query(value = "SELECT id FROM acl_sid   WHERE  sid = :sid", nativeQuery = true)
	Integer get_acl_sid_by_sid(@Param("sid") String sid);// get id the sid using email

	@Modifying
	@Query(value = "SELECT * FROM acl_sid", nativeQuery = true)
	List<Object> get_acl_sid();

	/**
	 * ACL_OBJECT_IDENTITY
	 *
	 * @param object_id_class
	 * @param object_id_identity
	 * @param parent_object
	 * @param owner_sid
	 * @param entries_inheriting
	 */

	@Modifying
	@Query(value = "INSERT INTO acl_object_identity(object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (:object_id_class,:object_id_identity,:parent_object,:owner_sid,:entries_inheriting)", nativeQuery = true)
	void insert_acl_object_identity(@Param("object_id_class") Integer object_id_class,
			@Param("object_id_identity") String object_id_identity, @Param("parent_object") Integer parent_object,
			@Param("owner_sid") Integer owner_sid, @Param("entries_inheriting") boolean entries_inheriting);

	@Modifying
	@Query(value = "INSERT INTO acl_object_identity(object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (:object_id_class,:object_id_identity,NULL,:owner_sid,:entries_inheriting)", nativeQuery = true)
	void insert_acl_object_identity_for_null_parent(@Param("object_id_class") Integer object_id_class,
			@Param("object_id_identity") String object_id_identity, @Param("owner_sid") Integer owner_sid,
			@Param("entries_inheriting") boolean entries_inheriting);

	@Modifying
	@Query(value = "update acl_object_identity  set entries_inheriting = :entries_inheriting where id = :id", nativeQuery = true)
	int update_entries_inheriting_acl_object_identity(@Param("id") Integer id,
			@Param("entries_inheriting") boolean entries_inheriting);

	@Query(value = "SELECT id FROM acl_object_identity   WHERE object_id_class = :object_id_class And object_id_identity = :object_id_identity", nativeQuery = true)
	Integer get_acl_object_by_object_id_class_and_object_id_identity(@Param("object_id_class") Integer object_id_class,
			@Param("object_id_identity") String object_id_identity);

	@Query(value = "SELECT * FROM acl_object_identity   WHERE object_id_class = :object_id_class And object_id_identity = :object_id_identity", nativeQuery = true)
	Object get_acl_object(@Param("object_id_class") Integer object_id_class,
			@Param("object_id_identity") String object_id_identity);

	@Query(value = "SELECT id FROM acl_object_identity   WHERE object_id_identity = :object_id_identity And object_id_class = :object_id_class", nativeQuery = true)
	Integer get_acl_object_by_object_id_identity(@Param("object_id_identity") String object_id_identity,
			@Param("object_id_class") Integer object_id_class);

	@Query(value = "SELECT * FROM acl_object_identity   WHERE id = :id", nativeQuery = true)
	Object get_acl_object_by_id(@Param("id") Integer id);

	@Modifying
	@Query(value = "SELECT * FROM acl_object_identity", nativeQuery = true)
	List<Object> get_acl_object_identity();
	/*
	 * Delete
	 */
	@Modifying
	@Query(value = "DELETE FROM acl_sid where sid= :sid", nativeQuery = true)
	void delete_acl_sid(@Param("sid") String sid) ;
	
	@Modifying
	@Query(value = "DELETE FROM acl_entry where sid= :sid", nativeQuery = true)
	void delete_acl_entry(@Param("sid") Integer sid) ;
	
	@Modifying
	@Query(value = "DELETE FROM acl_object_identity where owner_sid= :owner_sid", nativeQuery = true)
	void delete_obj_identity(@Param("owner_sid") Integer sid) ;
}
