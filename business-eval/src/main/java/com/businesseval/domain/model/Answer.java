package com.businesseval.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.businesseval.domain.ValidationGroups;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Answer {
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Valid
	@ConvertGroup(from = Default.class, to = ValidationGroups.BusinessUserId.class)
	@ManyToOne
	@NotNull
	@JoinColumn(name = "business_user_id")
	private BusinessUser businessUser;
	
	@Valid
	@ConvertGroup(from = Default.class, to = ValidationGroups.QuestionId.class)
	@ManyToOne
	@NotNull
	@JoinColumn(name = "question_id")
	private Question question;
	
	@Column(name = "value_answer",columnDefinition = "integer default 0")
	private Integer value;
}
