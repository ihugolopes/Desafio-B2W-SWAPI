package com.b2w.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.b2w.model.Planet;

public interface PlanetService {

	List<Planet> findByName(String name);

	Planet salvar(Planet planet);

	Optional<Planet> findById(String id);

	void delete(String id);

	List<Planet> findAll(Pageable page);

}
