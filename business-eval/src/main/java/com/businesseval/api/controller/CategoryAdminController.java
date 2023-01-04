package com.businesseval.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.businesseval.api.modelin.TextRequest;
import com.businesseval.domain.model.Category;
import com.businesseval.domain.repository.CategoryRepository;
import com.businesseval.domain.service.CategoryService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class CategoryAdminController {
	private CategoryService categoryService;
	private CategoryRepository categoryRepository;
	
	@PostMapping
	public Category save(@Valid @RequestBody Category category) {
		return categoryService.save(category);
	}
	
	@PutMapping("/{categoryId}")
	public Category edit(@PathVariable Long categoryId ,@Valid @RequestBody Category category) {
		return categoryService.edit(categoryId, category);
	}
	
	@PutMapping("/position/{categoryId}")
	public Category editPosition(@PathVariable Long categoryId ,@Valid @RequestBody Category category) {
		return categoryService.editPosition(categoryId, category);
	}
	
	@GetMapping("/{categoryId}")
	public Category search(@PathVariable Long categoryId) {
		return categoryService.search(categoryId);
	}

	@GetMapping("/name")
	public Category searchName(@RequestBody TextRequest categoryName){
		return categoryService.searchByName(categoryName.getText());
	}
	
	@GetMapping("/name/contains")
	public List<Category> searchNameContains(@RequestBody TextRequest categoryName){
		return categoryRepository.findByNameContains(categoryName.getText());
	}

	@GetMapping
	public List<Category> listAll(){
		return categoryRepository.findAll();
	}
	
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Void> delete(@PathVariable Long categoryId){
		return categoryService.delete(categoryId);
	}
}