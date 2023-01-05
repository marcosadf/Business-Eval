package com.businesseval.api.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.businesseval.api.modelin.CategoryIn;
import com.businesseval.api.modelout.CategoryOut;
import com.businesseval.domain.model.Category;
import com.businesseval.domain.model.Question;

@Component
public class CategoryAssembler {
	@Autowired
	private ModelMapper modelMapper;
	
	public CategoryAssembler(ModelMapper modelMapper) {
		modelMapper.createTypeMap(Category.class, CategoryOut.class)
		.<List<Question>>addMapping(src -> src.getQuestions(), (src, value) -> {
			if(value != null) {
				src.setQuestions((new QuestionAssembler(modelMapper)).toCollectionOut(value));
			}else {
				src.setQuestions(new ArrayList<>());
			}
		});
		this.modelMapper = modelMapper;
	}
	
	public Category toIn(CategoryIn category) {
		return modelMapper.map(category, Category.class);
	}
	
	public CategoryOut toOut(Category category) {
		return modelMapper.map(category, CategoryOut.class);
	}

	public List<CategoryOut> toCollectionOut(List<Category> listCategories) {
		return listCategories.stream().map(this::toOut).collect(Collectors.toList());
	}
}
