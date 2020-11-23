package com.b2w.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultsSWAPI {

	private List<PlanetSWAPI> results;

	public ResultsSWAPI(List<PlanetSWAPI> results, String name) {
		this.results = results;
	}

	
}
