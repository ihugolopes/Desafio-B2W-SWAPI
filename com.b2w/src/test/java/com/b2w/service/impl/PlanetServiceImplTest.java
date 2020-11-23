package com.b2w.service.impl;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.b2w.client.RestTemplateSWAPI;
import com.b2w.model.Planet;
import com.b2w.repository.PlanetRepository;

public class PlanetServiceImplTest {
	
	@Autowired
	RestTemplateSWAPI rest;

	private PlanetServiceImpl service;

	@Mock
	private PlanetRepository repository;

	@Before
	public void setUp() {
		repository = Mockito.mock(PlanetRepository.class);
		service = new PlanetServiceImpl();

	}

	@Test
	public void deveBuscarPorNome() throws IOException {

		List<Planet> result = repository.findByNameContaining("Hoth");
		
		Assert.assertFalse(result.isEmpty());

	}

	@Test
	public void deveSalvar() {
		Planet planet = Planet.builder().name("Test").climate("test").terrain("test").build();
		
		Planet planetaRetorno = service.salvar(planet);
		
		Assert.assertEquals(planetaRetorno.getName(), planet.getName());
	}

//	@Test
//	public void testa_Listar_Todos() {
//		Planeta planeta1 = new Planeta("Teste1", "Teste", "Teste");
//		Planeta planeta2 = new Planeta("Teste2", "Teste", "Teste");
//		Planeta planeta3 = new Planeta("Teste3", "Teste", "Teste");
//		Planeta planeta4 = new Planeta("Teste4", "Teste", "Teste");
//		List<Planeta> planetas = new ArrayList<Planeta>();
//		planetas.add(planeta1);
//		planetas.add(planeta2);
//		planetas.add(planeta3);
//		planetas.add(planeta4);
//
//		when(repo.findAll()).thenReturn(planetas);
//
//		List<Planeta> planetasRetorno = serv.encontraTodos();
//		Assert.assertEquals(planetasRetorno.get(0).getNome(), planeta1.getNome());
//	}
//
//	@Test
//	public void testa_Encontra_Por_ID() {
//		Planeta planeta1 = new Planeta("TesteNovo", "Teste", "Teste");
//		Optional<Planeta> planetaOpt = Optional.of(planeta1);
//		planeta1.setId("Teste");
//		when(repo.findById(planeta1.getId())).thenReturn(planetaOpt);
//
//		Planeta planetasRetorno = serv.encontraPorId(planeta1.getId());
//		Assert.assertEquals(planetaOpt.get(), planetasRetorno);
//	}
//
//	@Test
//	public void testa_Encontra_Por_ID_Nao_Existente() {
//		try {
//			serv.encontraPorId("");
//		} catch (Exception e) {
//			Assert.assertEquals("Id não encontrada!!", e.getMessage());
//		}
//	}
//
//	@Test
//	public void testa_Listar_Por_Nome() {
//		Planeta planeta1 = new Planeta("Star Destroyer 1", "Teste", "Teste");
//		Planeta planeta2 = new Planeta("Star Destroyer 2", "Teste", "Teste");
//		Planeta planeta3 = new Planeta("Star Destroyer 3", "Teste", "Teste");
//		Planeta planeta4 = new Planeta("Star Destroyer 4", "Teste", "Teste");
//		List<Planeta> planetas = new ArrayList<Planeta>();
//		planetas.add(planeta1);
//		planetas.add(planeta2);
//		planetas.add(planeta3);
//		planetas.add(planeta4);
//
//		when(repo.findByNomeContaining("Teste")).thenReturn(planetas);
//
//		List<Planeta> planetasRetorno = serv.findByNome("Teste");
//		Assert.assertEquals(planetasRetorno.get(0).getNome(), planeta1.getNome());
//	}
}
