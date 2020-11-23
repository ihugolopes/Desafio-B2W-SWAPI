package com.b2w.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Planet implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String name;
	private String climate;
	private String terrain;
	private Integer countFilmes;
	
	public Planet(@NotBlank String name, @NotBlank String climate, @NotBlank String terrain) {
		super();
		this.name = name;
		this.climate = climate;
		this.terrain = terrain;
	}

}
