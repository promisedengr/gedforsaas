package ma.project.GedforSaas.service;

import ma.project.GedforSaas.abstractSupperClasses.AbstractService;
import ma.project.GedforSaas.model.TemplateConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TemplateFileStorageServiceImpl implements TemplateFileStorageService {
	private final Path root = Paths.get("uploads");
	@Autowired
	private EntityManager entityManager;
	@Override
	public void init() {
		// TODO Auto-generated method stub
		try {
			if (Files.exists(root)){
				return;
			}
			Files.createDirectory(root);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void save(MultipartFile file, Long id) {
		// TODO Auto-generated method stub
		try {
			this.init();
			Files.copy(file.getInputStream(), this.root.resolve(id+"-"+file.getOriginalFilename()));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public Resource load(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	public Resource loadConverted(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	public List<TemplateConfig> findByCriteria(String value) {
		String query = "SELECT o FROM TemplateConfig o where 1=1 AND "; // here we add table name be careful to make first letter Uppercase
		query += AbstractService.addConstraint("o", "titre", value, "LIKE", "");
		query += AbstractService.addConstraint("o", "description", value, "LIKE", "OR");
		query += AbstractService.addConstraint("o", "nature", value, "LIKE", "OR");
		query += AbstractService.addConstraint("o", "motor_gen", value, "LIKE", "OR");
		query += AbstractService.addConstraint("o", "format", value, "LIKE", "OR");
		return entityManager.createQuery(query).getResultList();
	}
}
