package com.businesseval.domain.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.businesseval.config.LocaleConfig;
import com.businesseval.domain.exception.EntityNotFoundException;
import com.businesseval.domain.model.Timeline;
import com.businesseval.domain.repository.TimelineRepository;

@Service
public class TimelineService {
	@Autowired
	private TimelineRepository timelineRepository;

	private MessageSource messageSource = new LocaleConfig().messageSource();
	
	public Timeline save(Timeline timeline) {
		if(timeline.getId() != null) {
			Optional<Timeline> researchedTimeline = timelineRepository.findById(timeline.getId());
			if(!researchedTimeline.isPresent()) {
				throw new EntityNotFoundException(messageSource.getMessage("add.timeline.not.found", null, LocaleContextHolder.getLocale()));
			}
		}
		else {
			timeline.setDatetime_edition(new Date(System.currentTimeMillis()));			
		}
		return timelineRepository.save(timeline);
	}
	
	public Timeline edit(Long timelineId, Timeline timeline) {
		search(timelineId);
		timeline.setId(timelineId); 
		return save(timeline);	
	}
	
	public ResponseEntity<Void> delete(Long timelineId) {
		timelineRepository.deleteById(timelineId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
	
	public Timeline search(Long timelineId) {
		Optional<Timeline> timeline = timelineRepository.findById(timelineId);
		if(timeline.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("entity.not.found", null, LocaleContextHolder.getLocale()));
		}
		return timeline.get();
	}

}
