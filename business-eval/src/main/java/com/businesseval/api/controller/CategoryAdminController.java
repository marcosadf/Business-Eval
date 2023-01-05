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

import com.businesseval.api.assembler.CategoryAssembler;
import com.businesseval.api.modelin.CategoryIn;
import com.businesseval.api.modelin.TextRequest;
import com.businesseval.api.modelout.CategoryOut;
import com.businesseval.domain.repository.CategoryRepository;
import com.businesseval.domain.service.CategoryService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class CategoryAdminController {
	private CategoryService categoryService;
	private CategoryRepository categoryRepository;
	private CategoryAssembler categoryAssembler;
	
	@PostMapping
	public CategoryOut save(@Valid @RequestBody CategoryIn category) {
		return categoryAssembler.toOut(categoryService.save(categoryAssembler.toIn(category)));
	}
	
	@PutMapping("/{categoryId}")
	public CategoryOut edit(@PathVariable Long categoryId ,@Valid @RequestBody CategoryIn category) {
		return categoryAssembler.toOut(categoryService.edit(categoryId, categoryAssembler.toIn(category)));
	}
	
	@PutMapping("/position/{categoryId}")
	public CategoryOut editPosition(@PathVariable Long categoryId ,@Valid @RequestBody CategoryIn category) {
		return categoryAssembler.toOut(categoryService.editPosition(categoryId, categoryAssembler.toIn(category)));
	}
	
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
	
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Void> delete(@PathVariable Long categoryId){
		return categoryService.delete(categoryId);
	}
}