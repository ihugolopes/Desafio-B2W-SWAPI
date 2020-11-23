package com.b2w.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.b2w.model.Planet;
import com.b2w.repository.PlanetRepository;
import com.b2w.service.interfaces.PlanetService;

@Service
public class PlanetServiceImpl implements PlanetService {

	@Autowired
	private PlanetRepository repository;
	
	@Override
	public Planet salvar(Planet planet) {
		return repository.save(planet);
	}
	
	@Override
	public Optional<Planet> findById(String id) {
		Optional<Planet> planet = repository.findById(id);
		return  planet;
	}

	@Override
	public List<Planet> findAll(Pageable page) {
		return repository.findAll(page).toList();
	}

	@Override
	public void delete(String id) {
			repository.delete(findById(id).get());
	}

	@Override
	public List<Planet> findByName(String name) {
		return repository.findByNameContaining(name);
	}

}
