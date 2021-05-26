package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.model.AdoptionStateType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adoptions")
public class AdoptionController {

	private final AdoptionService adoptionService;

	private final PetService petService;

	private final OwnerService ownerService;
	
	private final UserService userService;

	@Autowired
	public AdoptionController(AdoptionService adoptionService, PetService petService, OwnerService ownerService, UserService userService) {
		this.adoptionService = adoptionService;
		this.petService = petService;
		this.ownerService = ownerService;
		this.userService = userService;
	}

	@GetMapping(value="/")
	public String adoptionList(ModelMap modelMap, Principal principal) {
		Optional<User> optionalPossibleOwner = this.userService.findUser(principal.getName());
		if (optionalPossibleOwner.isPresent())
			modelMap.addAttribute("possibleOwner", optionalPossibleOwner.get());
		else
			return "welcome";

		String view = "adoptions/adoptionList";
		Collection<Pet> pets = this.petService.findPetsInAdoption();
		modelMap.addAttribute("pets", pets);
		return view;
	}
	   
	@GetMapping(value="/pendingAdoptionsList")
	public String pendingAdoptionList(ModelMap modelMap) {
		modelMap.addAttribute("pendingAdoption", AdoptionStateType.PENDING);
		List<Adoption> adoptions = (List<Adoption>)this.adoptionService.findAll();
		modelMap.addAttribute("adoptions", this.adoptionService.findAllAdoptionsWithPendingState(adoptions));
		modelMap.addAttribute("mensaje","Adopciones pendientes");
		modelMap.addAttribute("boton","Ver todas las adopciones");
		modelMap.addAttribute("alternativa", "/adoptions/adoptionList");
		return "adoptions/adoptionState";
	}
	 
	@GetMapping(value="/adoptionList")
	public String allAdoptionList(ModelMap modelMap) {
		modelMap.addAttribute("pendingAdoption", AdoptionStateType.PENDING);
		modelMap.addAttribute("adoptions", this.adoptionService.findAll());
		modelMap.addAttribute("mensaje","Todas las adopciones");
		modelMap.addAttribute("boton","Ver adopciones pendientes");
		modelMap.addAttribute("alternativa", "/adoptions/pendingAdoptionsList");
		return "adoptions/adoptionState";
	} 

	@GetMapping(value = "/{petId}/applicationForm")
	public String initApplyForm(Map<String, Object> model, Principal principal,
			@PathVariable("petId") int petId) {

		Optional<User> optionalPossibleOwner = this.userService.findUser(principal.getName());
		if (!optionalPossibleOwner.isPresent()) {
			return "redirect:/login";
		
		
		} else {

			String possibleOwnerName = optionalPossibleOwner.get().getUsername();

			Owner owner = this.petService.findPetById(petId).getOwner();
			String ownerName = owner.getUser().getUsername();

		
			model.put("possibleOwner", possibleOwnerName);
			model.put("originalOwner", ownerName);
			model.put("adoption",new Adoption());
			return "adoptions/formularioAdopcion";
		}
	}

	@PostMapping(value = "/{petId}/applicationForm")
	public String sendApplicationForm(@PathVariable("petId") int petId,@Valid Adoption adoption, BindingResult result, 
			Map<String, Object> model, Principal principal, ModelMap modelMap) throws DataAccessException, DuplicatedPetNameException {
		Pet pet = this.petService.findPetById(petId);
		User possibleOwner;
		Optional<User> optionalPossibleOwner = this.userService.findUser(principal.getName());
		if (optionalPossibleOwner.isPresent())
			possibleOwner = optionalPossibleOwner.get();
		else
			return "welcome";
		
		if (result.hasErrors()) {
			String possibleOwnerName = possibleOwner.getUsername();

			Owner owner = pet.getOwner();
			String ownerName = owner.getUser().getUsername();
			
			model.put("possibleOwner", possibleOwnerName);
			model.put("originalOwner", ownerName);
			
			return "adoptions/formularioAdopcion";
		} else {
			Boolean alreadyExists = adoptionService.findAdoptionByPossibleOwnerAndPet(possibleOwner.getUsername(), pet) != null;
			if(Boolean.TRUE.equals(alreadyExists)){
				return "/adoptions/existingAdoption";
			}else {
				try {
					adoption.setPet(pet);
					modelMap.addAttribute("message", "registro correcto de la peticion");
					adoption.setAdoptionStateType(AdoptionStateType.PENDING);
					pet.addAdoption(adoption);
					
					this.adoptionService.saveAdoption(adoption);
					this.petService.savePet(pet);

					model.put("adoption",adoption);
					return "welcome";	
				}
				catch (DuplicatedPetNameException|DataAccessException e) {
					return "/adoptions/formularioAdopcion";
				}
			}
		}
	}
	
	
	@GetMapping(value="/accept/{adoptionId}")
	public String acceptAdoptionApplication(@PathVariable("adoptionId") int adoptionId, Principal principal, 
		Map<String, Object> model) throws Exception {
		Adoption adoption = this.adoptionService.findAdoptionById(adoptionId);
		
		this.adoptionService.acceptAdoptionApplication(adoption);

		Owner possibleOwner = this.ownerService.findOwnerByUsername(adoption.getPossibleOwner());
		Owner owner = this.ownerService.findOwnerByUsername(adoption.getOwner());
		Pet pet = adoption.getPet();
			
		owner.removePet(pet);
		possibleOwner.addPet(pet);
		pet.setInAdoption(false);
		
			
		this.ownerService.saveOwner(owner);
		this.ownerService.saveOwner(possibleOwner);
		this.petService.savePet(pet);
		return "redirect:/adoptions/pendingAdoptionsList";
		
	}
	
	@GetMapping(value="/deny/{adoptionId}")
	public String denyAdoptionApplication(@PathVariable("adoptionId") int adoptionId, Principal principal,
		Map<String, Object> model) throws Exception {
		Adoption adoption = this.adoptionService.findAdoptionById(adoptionId);

		
		this.adoptionService.denyAdoptionApplication(adoption);
		adoption.setAdoptionStateType(AdoptionStateType.DECLINED);
		this.adoptionService.saveAdoption(adoption);
		return "redirect:/adoptions/pendingAdoptionsList";
			
	}
}