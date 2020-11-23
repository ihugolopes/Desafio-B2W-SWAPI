package com.b2w.controller;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.b2w.client.RestTemplateSWAPI;
import com.b2w.dto.PlanetDTO;
import com.b2w.model.Planet;
import com.b2w.model.PlanetSWAPI;
import com.b2w.service.interfaces.PlanetService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/planet")
@CrossOrigin(origins = "*") // Para permitir a invocação de nossos endpoint de outras fontes, como uma
							// aplicação REACT ou Angular, precisamos habilitar o CORS (Cross-Origin
							// Resource Sharing), com a anotação @CrossOrigin.
@Api(value = "SW Planets - REST API") // As anotações @Api e @ApiOperation servirão para documenta pelo swagger, que
										// veremos mais a frente.
public class PlanetController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RestTemplateSWAPI swapi;

	private Calendar horaInicial = Calendar.getInstance();

	private List<PlanetSWAPI> result = new ArrayList<PlanetSWAPI>();

	private PlanetService planetService;

	PlanetController(PlanetService planetService) {
		this.planetService = planetService;
	}

	@ApiOperation(value = "Retorna um planeta por id")
	@GetMapping("/{id}")
	public ResponseEntity<Planet> buscarPorId(@PathVariable String id) {
		Optional<Planet> planet = planetService.findById(id);


		if (planet.isPresent()) {

			int aparicoes = encontraAparicao(result, planet.get());

			Planet planetWithCountFilmes = Planet.builder().id(planet.get().getId()).name(planet.get().getName())
					.climate(planet.get().getClimate()).terrain(planet.get().getTerrain()).countFilmes(aparicoes).build();

			return ResponseEntity.ok().body(planetWithCountFilmes);
		}

		return ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "Remove um planeta por id")
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable("id") String id) {
		Optional<Planet> optional = planetService.findById(id);

		if (optional.isPresent()) {
			planetService.delete(id);
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "Retorna todos planetas")
	@GetMapping
	public ResponseEntity<List<Planet>> lista(
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size,
			@RequestParam(value = "sort", required = false) String sort) {

//		Caso deseje gerar um DTO para Planet.
//		return PlanetDTO.converter(planets);
		
		Pageable sortedByName = PageRequest.of(page, size, Sort.by("name"));

		return ResponseEntity.ok().body(insereAparicao(planetService.findAll(sortedByName)));

	}

	@ApiOperation(value = "Busca um planeta por nome")
	@GetMapping("/name")
	public ResponseEntity<List<Planet>> buscarPorNome(@RequestParam(value="name", defaultValue="")  String name) {

		List<Planet> res = new ArrayList<>();
		List<Planet> planets = planetService.findByName(name);

		this.result = cache(this.result, horaInicial);

		for (Planet planet : planets) {

			int aparicoes = encontraAparicao(result, planet);

			Planet planetWithCountFilmes = Planet.builder().id(planet.getId()).name(planet.getName())
					.climate(planet.getClimate()).terrain(planet.getTerrain()).countFilmes(aparicoes).build();

			res.add(planetWithCountFilmes);
		}

		return ResponseEntity.ok().body(res);

	}

	@ApiOperation(value = "Cadastra um planeta contendo: nome, clima e terreno.")
	@PostMapping
	@Transactional
	public ResponseEntity<Planet> cadastrar(@RequestBody @Valid PlanetDTO planet, UriComponentsBuilder uriBuilder) {

		Planet planetConverted = Planet.builder().name(planet.getName()).climate(planet.getClimate())
				.terrain(planet.getTerrain()).build();

		Planet saveResult = planetService.salvar(planetConverted);

		URI uri = uriBuilder.path("/planet/{id}").buildAndExpand(saveResult.getId()).toUri();
		return ResponseEntity.created(uri).body(saveResult);
	}

	private int encontraAparicao(List<PlanetSWAPI> result, Planet planet) {

		for (PlanetSWAPI p : result) {

			if (planet.getName().equals(p.getName())) {
				return p.getFilms().size();
			}

		}

		return 0;
	}

	public List<Planet> insereAparicao(List<Planet> list) {
		List<Planet> res = new ArrayList<Planet>();

		this.result = cache(this.result, horaInicial);

		for (Planet planet : list) {

			res.add(Planet.builder().id(planet.getId()).name(planet.getName()).climate(planet.getClimate())
					.terrain(planet.getTerrain()).countFilmes(encontraAparicao(result, planet)).build());

		}

		return res;
	}

	private List<PlanetSWAPI> cache(List<PlanetSWAPI> result, Calendar horaInicial) {
		Calendar atual = Calendar.getInstance();
		Calendar horaComparar = (Calendar) horaInicial.clone();
		horaComparar.add(Calendar.HOUR_OF_DAY, 1);

		if (result.isEmpty()) {
			result = swapi.ReturnAparicoes().getBody().getResults();
		}

		if (atual.after(horaComparar)) {
			result = swapi.ReturnAparicoes().getBody().getResults();
			horaInicial = Calendar.getInstance();
		}

		return result;
	}

}
