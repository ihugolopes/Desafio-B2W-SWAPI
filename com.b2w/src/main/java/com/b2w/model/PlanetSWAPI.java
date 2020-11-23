package com.b2w.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanetSWAPI {

	private String name;

	private List<String> films;

	public PlanetSWAPI(String name, List<String> films) {
		this.name = name;
		this.films = films;
	}

}
