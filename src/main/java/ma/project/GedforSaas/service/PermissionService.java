package ma.project.GedforSaas.service;

import ma.project.GedforSaas.exception.ResourceNotFoundExceptionConstimized;
import ma.project.GedforSaas.model.Permission;
import ma.project.GedforSaas.repository.PermissionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.ID_NOT_FOUND;

@Service
@Transactional
public class PermissionService {
	private final PermissionRepository permissionRepository;

	@Autowired
	public PermissionService(PermissionRepository permissionRepository) {
		this.permissionRepository = permissionRepository;
	}

	public Permission addPermission(Permission permission) {
		return permissionRepository.save(permission);
	}

	public List<Permission> findAllPermissions() {
		return permissionRepository.findAll();
	}

	public Permission updatePermission(Permission permission) {
		return permissionRepository.save(permission);
	}

	public Permission findPermissionById(Long id) {
		return permissionRepository.findPermissionById(id).orElseThrow(
				() -> new ResourceNotFoundExceptionConstimized(TO_LOCALE(ID_NOT_FOUND, LOCALE)));
	}

	@Transactional
	public void deletePermission(Long id) {
		permissionRepository.deletePermissionById(id);
	}

}
