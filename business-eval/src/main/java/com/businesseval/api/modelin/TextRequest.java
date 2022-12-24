package com.businesseval.api.modelin;

import lombok.Data;

@Data
public class TextRequest {
	private String text;
	
	TextRequest(String text){
		this.text = text;
	}
}
