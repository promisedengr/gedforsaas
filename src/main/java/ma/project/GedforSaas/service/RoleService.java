package ma.project.GedforSaas.service;

import ma.project.GedforSaas.exception.ResourceNotFoundExceptionConstimized;
import ma.project.GedforSaas.model.Role;
import ma.project.GedforSaas.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.ID_NOT_FOUND;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.ROLE_NAME_NOT_FOUND;

@Service
@Transactional
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	public Role addRole(Role profil) {
		return roleRepository.save(profil);
	}

	public List<Role> findAllRoles() {
		return roleRepository.findAll();
	}

	public Role updateRole(Role role) {
		return roleRepository.save(role);
	}

	public Role findRoleById(Long id) {
		return roleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundExceptionConstimized(TO_LOCALE(ID_NOT_FOUND, LOCALE)));
	}

	public Role findRoleByName(String roleName) {
		return roleRepository.findByName(roleName).orElseThrow(
				() -> new ResourceNotFoundExceptionConstimized(TO_LOCALE(ROLE_NAME_NOT_FOUND, LOCALE)));
	}

	public void deleteRole(Long id) {
		roleRepository.deleteById(id);
	}

}
