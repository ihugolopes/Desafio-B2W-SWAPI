package com.b2w.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.b2w.model.Planet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanetDTO {

	@NotNull @NotEmpty @Length(min = 3, max = 50)
	private String name;
	@NotNull @NotEmpty @Length(min = 3, max = 50)
	private String climate;
	@NotNull @NotEmpty @Length(min = 3, max = 50)
	private String terrain;
	
	public PlanetDTO(Planet planet) {
		this.name = planet.getName();
		this.climate = planet.getClimate();
		this.terrain = planet.getTerrain();
	}
	
	public static List<PlanetDTO> converter(List<Planet> topicos) {
		return topicos.stream().map(PlanetDTO::new).collect(Collectors.toList());
	}
	
}
