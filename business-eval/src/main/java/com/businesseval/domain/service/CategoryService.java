package com.businesseval.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.businesseval.config.LocaleConfig;
import com.businesseval.domain.exception.BusinessException;
import com.businesseval.domain.exception.EntityNotFoundException;
import com.businesseval.domain.model.Category;
import com.businesseval.domain.repository.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	private MessageSource messageSource = new LocaleConfig().messageSource();
	
	public Category save(Category category) {
		if(category.getId() != null) {
			Optional<Category> researchedCategory = categoryRepository.findById(category.getId());
			if(researchedCategory.isPresent()) {
				if(researchedCategory.get().getName().equals(category.getName())) {
					if(!categoryRepository.findByName(category.getName()).isEmpty()) {
						throw new BusinessException(messageSource.getMessage("name.category.exist", null, LocaleContextHolder.getLocale()));
					}
				}
			}
			else {
				throw new EntityNotFoundException(messageSource.getMessage("add.category.not.found", null, LocaleContextHolder.getLocale()));
			}
		}else if(!categoryRepository.findByName(category.getName()).isEmpty()) {
			throw new BusinessException(messageSource.getMessage("name.category.exist", null, LocaleContextHolder.getLocale()));
		}else {
			long numCategories = categoryRepository.count();
			category.setPosition(numCategories + 1);
		}
		return categoryRepository.save(category);
	}
	
	public Category edit(Long categoryId, Category category) {
		Category researchedCategory = search(categoryId);
		if(researchedCategory.getPosition() != category.getPosition()) {
			throw new EntityNotFoundException(messageSource.getMessage("position.category.not.edit", null, LocaleContextHolder.getLocale()));
		}
		category.setId(categoryId);
		return save(category);	
	}
	
	public Category editPosition(Long categoryId, Category category) {
		Category researchedCategory = search(categoryId);
		if(researchedCategory.getPosition() != category.getPosition()) {
			if(category.getPosition() > categoryRepository.count() || category.getPosition() < 0) {
				throw new EntityNotFoundException(messageSource.getMessage("position.category.not.valid", null, LocaleContextHolder.getLocale()));
			}
			List<Category> categories = categoryRepository.findAll();
			categories.stream().forEach(c -> {
				if(!c.equals(researchedCategory)) {
					if(c.getPosition() < researchedCategory.getPosition() && c.getPosition() >= category.getPosition()) {
						c.setPosition(c.getPosition() + 1);
						categoryRepository.save(c);
					}else if(c.getPosition() > researchedCategory.getPosition() && c.getPosition() <= category.getPosition()) {
						c.setPosition(c.getPosition() - 1);
						categoryRepository.save(c);
					}
				}
			});
		}
		researchedCategory.setPosition(category.getPosition());
		return categoryRepository.save(researchedCategory);	
	}
	
	public ResponseEntity<Void> delete(Long categoryId) {
		search(categoryId);
		categoryRepository.deleteById(categoryId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
	
	public Category search(Long categoryId) {
		Optional<Category> category = categoryRepository.findById(categoryId);
		if(category.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("category.not.found", null, LocaleContextHolder.getLocale()));
		}
		return category.get();
	}
	
	public Category searchByName(String categoryName) {
		List<Category> category = categoryRepository.findByName(categoryName);
		if(category.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("business.not.found", null, LocaleContextHolder.getLocale()));
		}
		return category.get(0);
	}
}
