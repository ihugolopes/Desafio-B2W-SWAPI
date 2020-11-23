package com.b2w.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.b2w.model.Planet;

@Repository
public interface PlanetRepository extends MongoRepository<Planet, String> {

	List<Planet> findByNameContaining(String nome);

}
