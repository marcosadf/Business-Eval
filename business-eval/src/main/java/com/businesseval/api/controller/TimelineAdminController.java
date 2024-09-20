package com.businesseval.api.controller;

import java.util.Collections;
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
import com.businesseval.domain.model.Timeline;
import com.businesseval.domain.repository.TimelineRepository;
import com.businesseval.domain.service.TimelineService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/admin/timelines")
public class TimelineAdminController {
	private TimelineService timelineService;
	private TimelineRepository timelineRepository;
	
	@PostMapping
	public Timeline save(@Valid @RequestBody Timeline timeline) {
		return timelineService.save(timeline);
	}
	
	@PutMapping("/{timelineId}")
	public Timeline edit(@PathVariable Long timelineId ,@Valid @RequestBody Timeline timeline) {
		return timelineService.edit(timelineId, timeline);
	}

	
	@GetMapping("/{timelineId}")
	public Timeline search(@PathVariable Long timelineId) {
		return timelineService.search(timelineId);
	}
	
	@GetMapping("/description/contains")
	public List<Timeline> searchDescriptionContains(@RequestBody TextRequest timelineDescription){
		return timelineRepository.findByDescriptionContains(timelineDescription.getText());
	}

	@GetMapping
	public List<Timeline> listAll() {
		List<Timeline> timelines = timelineRepository.findAll();
		Collections.sort(timelines, (t1, t2) -> {
			// Para ordenar da mais recente para a mais antiga, inverta a ordem.
			return t2.getDatetime_edition().compareTo(t1.getDatetime_edition());
		});
		return timelines;
	}

	
	@DeleteMapping("/{timelineId}")
	public ResponseEntity<Void> delete(@PathVariable Long timelineId){
		return timelineService.delete(timelineId);
	}
}