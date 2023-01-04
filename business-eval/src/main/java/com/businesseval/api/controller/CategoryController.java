package com.businesseval.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/categories")
public class CategoryController {
	private CategoryService categoryService;
	private CategoryRepository categoryRepository;
	
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

}