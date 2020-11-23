package com.b2w;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.b2w.client.RestTemplateSWAPI;
import com.b2w.model.Planet;
import com.b2w.model.ResultsSWAPI;
import com.b2w.repository.PlanetRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestTemplatePlanetTeste {
	
    //URL base para onde as requests serão feitas
    final String BASE_PATH = "http://localhost:8080/api/planet";
 
    //Injetamos o repositório para acesso ao Banco de dados
    @Autowired
    private PlanetRepository repository;
     
    //Definimos o restTemplate
    private RestTemplate restTemplate;
    
    //Definimos o restTemplateSWAPI para testar e disponibilidade da SW API para consulta de aparicoes
    @Autowired
	RestTemplateSWAPI restTemplateSWAPI;
     
    //Definimos o JacksonMapper responsável por converter
    //JSON para Objeto e vice versa
    private ObjectMapper MAPPER = new ObjectMapper();
    
    Planet planet1, planet2;
     
    //Definimos o que será feito antes da execução do teste
    @Before
    public void setUp() throws Exception {
   
        //Inicializamos o objeto restTemplate
        restTemplate = new RestTemplate();
        
        planet1 = repository.save(new Planet("Alderaa", "tropical", "rainforests"));
        planet2 = repository.save(new Planet("Tatooine", "tropical", "rainforests"));
    }
     
    @After
    public void tearDown() {
 
    	repository.delete(planet1);
    	repository.delete(planet2);
    }
    
    
    @Test
    public void deveSalvarUmPlanet() throws JsonProcessingException{
 
        //Criamos uma novo planet
    	Planet planet =  Planet.builder().name("Tester").climate("climate").terrain("terrain").build();
    	
        //Fazemos um HTTP request do tipo POST passando a pessoa como parâmetro
        Planet response = restTemplate.postForObject(BASE_PATH, planet, Planet.class);
 
        //Verificamos se o resultado da requisição é igual ao esperado
        //Se sim significa que tudo correu bem
        Assert.assertEquals("Tester", response.getName());
    }
    
    
    @Test
    public void deveBuscarPorId() throws IOException{
  
        Planet planet = restTemplate.getForObject(BASE_PATH + "/5fbc0ca32d49647b675e6df0", Planet.class);
         
        Assert.assertNotNull(planet);
    }
    
    @Test
   	public void deveDarErroSeIdNaoExistir() {
   		try {
   			restTemplate.getForObject(BASE_PATH + "/5fbc0ca32d49647b6", Planet.class);
   		}catch(Exception e) {
   			Assert.assertEquals(null, e.getCause());
   		}
   	}
    
    @Test
    public void deveBuscarPorNome() throws IOException{ 	
	
    	ResponseEntity<String>  respostaBusca = restTemplate.getForEntity(BASE_PATH + "/name?name=Hoth", String.class);
		Assert.assertEquals(200, respostaBusca.getStatusCodeValue());
		
    }
    
    @Test
    public void deveRealizarTentativaDeBuscaPorNome() throws IOException{ 	
    	
    	ResponseEntity<String>  respostaBusca = restTemplate.getForEntity(BASE_PATH + "/name?name=Hodth", String.class);
    	Assert.assertEquals(200, respostaBusca.getStatusCodeValue());
  
    }
    
    @Test
    public void deveBuscarTodos() throws IOException{
    	
    	String response = restTemplate.getForObject(BASE_PATH + "/", String.class);
    	
    	List<Planet> persons = MAPPER.readValue(response, 
    			MAPPER.getTypeFactory().constructCollectionType(List.class, Planet.class));
    	   	
    	Assert.assertNotNull(persons);
    }
    
    @Test
    public void deveDarErroSeCampoNameEstiverNulo() throws IOException{

    	Planet planet =  Planet.builder().climate("climate").terrain("terrain").build();
  
    	try {
    		
    		Planet response = restTemplate.postForObject(BASE_PATH, planet, Planet.class);

    	} catch(Exception ex) {
    		
    		Assert.assertEquals(null, ex.getCause());
    		
    	}
    	
    }
    
    @Test
    public void deveDarErroSeCampoClimateEstiverNulo() throws IOException{
    	
    	Planet planet =  Planet.builder().name("Test").terrain("terrain").build();
    	
    	try {
    		
    		Planet response = restTemplate.postForObject(BASE_PATH, planet, Planet.class);
    		
    	} catch(Exception ex) {
    		
    		Assert.assertEquals(null, ex.getCause());
    		
    	}
    	
    }
    
    @Test
    public void deveDarErroSeCampoTerrainEstiverNulo() throws IOException{
    	
    	Planet planet =  Planet.builder().name("Test").climate("climate").build();
    	
    	try {
    		
    		restTemplate.postForObject(BASE_PATH, planet, Planet.class);
    		
    	} catch(Exception ex) {
    		
    		Assert.assertEquals(null, ex.getCause());
    		
    	}
    	
    }
    
    @Test
    public void deveExcluirUmPlanet() {
    	
    	List<Planet> planeta = repository.findByNameContaining("Alderaa");
    	
    	repository.delete(planeta.get(0));
    	
    	List<Planet> response = repository.findByNameContaining("Alderaa");
    	
    	Assert.assertTrue(response.isEmpty());

    	Assert.assertFalse(planeta.isEmpty());
    	 
    }
    
    @Test
    public void deveTestarReturnDeAparicoes() {
		ResponseEntity<ResultsSWAPI> result = restTemplateSWAPI.ReturnAparicoes();
    	Assert.assertNotNull(result);
     }
    
 
	
	

}
