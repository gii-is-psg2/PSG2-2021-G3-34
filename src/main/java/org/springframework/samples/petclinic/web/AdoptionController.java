package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adoptions")
public class AdoptionController {

	private final AdoptionService adoptionService;
	
	private final PetService petService;
	
	@Autowired
	public AdoptionController(AdoptionService adoptionService, PetService petService) {
		this.adoptionService = adoptionService;
		this.petService = petService;
	}
	
	@GetMapping()
	public String listadoAdopciones(ModelMap modelMap) {
		String vista = "adoptions/adoptionList";
		Iterable<Pet> pets = petService.findPetsInAdoption();
		modelMap.addAttribute("pets", pets);
		return vista;
	}
}