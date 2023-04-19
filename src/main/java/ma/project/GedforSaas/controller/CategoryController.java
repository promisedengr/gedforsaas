package ma.project.GedforSaas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ma.project.GedforSaas.model.Category;
import ma.project.GedforSaas.service.CategoryService;

@RestController
@RequestMapping("api/v1/category")
@AllArgsConstructor
@NoArgsConstructor
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/all")
	public ResponseEntity<List<Category>> getAllCategory() {

		return new ResponseEntity<List<Category>>(categoryService.getAllCategories(), HttpStatus.OK);
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id) {

		return new ResponseEntity<Category>(categoryService.getCategoryById(id), HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<Category> addCategory(@RequestBody Category category) {

		return new ResponseEntity<Category>(categoryService.addNewCategory(category), HttpStatus.CREATED);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Category> updateCategory(@PathVariable("id") Long id, @RequestBody Category category) {

		return new ResponseEntity<Category>(categoryService.updateCategory(id, category), HttpStatus.OK);
	}

	@DeleteMapping(value = "/remove/{id}")
	public ResponseEntity<Object> deletePost(@PathVariable Long id) {

		return new ResponseEntity<>(categoryService.removeCategory(id), HttpStatus.OK);
	}

}
