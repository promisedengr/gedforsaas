package ma.project.GedforSaas.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ma.project.GedforSaas.exception.ResourceNotFoundExceptionConstimized;
import ma.project.GedforSaas.model.Category;
import ma.project.GedforSaas.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.CATEGORY_NOT_FOUND;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> getAllCategories() {

		return categoryRepository.findAll();
	}

	public Category getCategoryById(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundExceptionConstimized(TO_LOCALE(CATEGORY_NOT_FOUND, LOCALE)));
	}

	public Category addNewCategory(Category category) {

		return categoryRepository.save(category);
	}

	public Category updateCategory(Long id, Category category) {

		Category categoryInDB = getCategoryById(id);

		categoryInDB.setName(category.getName());
		categoryInDB.setResources(category.getResources());

		return categoryRepository.save(categoryInDB);
	}

	public Object removeCategory(Long id) {

		categoryRepository.deleteById(id);
		return "removed";
	}

}
