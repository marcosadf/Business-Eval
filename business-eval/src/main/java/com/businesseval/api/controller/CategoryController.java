package com.businesseval.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.businesseval.api.assembler.CategoryAssembler;
import com.businesseval.api.modelin.TextRequest;
import com.businesseval.api.modelout.CategoryOut;
import com.businesseval.domain.repository.CategoryRepository;
import com.businesseval.domain.service.CategoryService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
	private CategoryService categoryService;
	private CategoryRepository categoryRepository;
	private CategoryAssembler categoryAssembler;
	
	@GetMapping("/{categoryId}")
	public CategoryOut search(@PathVariable Long categoryId) {
		return categoryAssembler.toOut(categoryService.search(categoryId));
	}

	@GetMapping("/name")
	public CategoryOut searchName(@RequestBody TextRequest categoryName){
		return categoryAssembler.toOut(categoryService.searchByName(categoryName.getText()));
	}
	
	@GetMapping("/name/contains")
	public List<CategoryOut> searchNameContains(@RequestBody TextRequest categoryName){
		return categoryAssembler.toCollectionOut(categoryRepository.findByNameContains(categoryName.getText()));
	}

	@GetMapping
	public List<CategoryOut> listAll(){
		return categoryAssembler.toCollectionOut(categoryRepository.findAll());
	}

}