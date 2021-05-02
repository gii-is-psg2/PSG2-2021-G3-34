package org.springframework.samples.petclinic.web;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.model.AdoptionStateType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

	@Autowired
	public AdoptionController(AdoptionService adoptionService, PetService petService, OwnerService ownerService) {
		this.adoptionService = adoptionService;
		this.petService = petService;
		this.ownerService = ownerService;
	}

	@GetMapping()
	public String adoptionList(ModelMap modelMap, Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Owner possibleOwner = this.ownerService.findOwnerByUsername(userDetails.getUsername());
		modelMap.addAttribute("possibleOwner", possibleOwner);

		String view = "adoptions/adoptionList";
		Iterable<Pet> pets = this.petService.findPetsInAdoption();
		
		modelMap.addAttribute("pets", pets);
		return view;
	}
	   
	@GetMapping(value="/pendingAdoptionsList")
	public String pendingAdoptionList(ModelMap modelMap) {
		modelMap.addAttribute("pendingAdoption", AdoptionStateType.PENDING);
		List<Adoption> adoptions = (List<Adoption>)this.adoptionService.findAll();
		modelMap.addAttribute("adoptions", this.adoptionService.findAllAdoptionsWithPendingState(adoptions));
		return "adoptions/stateAdoptionList";
	}
	 
	@GetMapping(value="/allAdoptionsList")
	public String allAdoptionList(ModelMap modelMap) {
		modelMap.addAttribute("pendingAdoption", AdoptionStateType.PENDING);
		modelMap.addAttribute("adoptions", this.adoptionService.findAll());
		return "adoptions/stateAdoptionList";
	} 

	@GetMapping(value = "/{petId}/applicationForm")
	public String initApplyForm(Map<String, Object> model, Authentication authentication,
			@PathVariable("petId") int petId) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Owner possibleOwner = this.ownerService.findOwnerByUsername(userDetails.getUsername());

		if (possibleOwner == null) {
			return "redirect:/login";
			
		} else {

			String possibleOwnerName = possibleOwner.getUser().getUsername();

			Owner owner = this.petService.findPetById(petId).getOwner();
			String ownerName = owner.getUser().getUsername();

		
			model.put("possibleOwner", possibleOwnerName);
			model.put("originalOwner", ownerName);
			model.put("adoption",new Adoption());
			return "/adoptions/applicationForm";
		}
	}

	@PostMapping(value = "/{petId}/applicationForm")
	public String sendApplicationForm(@PathVariable("petId") int petId,@Valid Adoption adoption, BindingResult result, 
			Map<String, Object> model, Authentication authentication) throws DataAccessException, DuplicatedPetNameException {
		Pet pet = this.petService.findPetById(petId);
		
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Owner possibleOwner = this.ownerService.findOwnerByUsername(userDetails.getUsername());
		if (result.hasErrors()) {
			String possibleOwnerName = possibleOwner.getUser().getUsername();

			Owner owner = pet.getOwner();
			String ownerName = owner.getUser().getUsername();
			
			model.put("possibleOwner", possibleOwnerName);
			model.put("originalOwner", ownerName);
			
			return "/adoptions/applicationForm";
		} else {
			Adoption alreadyExists = adoptionService.findAdoptionByPossibleOwnerAndPet(possibleOwner.getUser().getUsername()
					, pet);
			if(alreadyExists!=null){
				return "/adoptions/existingAdoption";
			}else {
				adoption.setPet(pet);
				adoption.setAdoptionStateType(AdoptionStateType.PENDING);
				pet.addAdoption(adoption);

				this.adoptionService.saveAdoption(adoption);
				this.petService.savePet(pet);

				model.put("adoption",adoption);
				return "welcome";	
				}
		}
	}
	
	
	@GetMapping(value="/accept/{adoptionId}")
	public String acceptAdoptionApplication(@PathVariable("adoptionId") int adoptionId, Authentication authentication,
		Map<String, Object> model) throws Exception {
		Boolean authenticated = authentication.isAuthenticated();
		Adoption adoption = this.adoptionService.findAdoptionById(adoptionId);
		
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Owner existingOwner = this.ownerService.findOwnerByUsername(userDetails.getUsername());
		
		 if(existingOwner!=null || !authenticated){
			return "welcome";
		}else {
			this.adoptionService.acceptAdoptionApplication(adoption);
			
			Owner possibleOwner = this.ownerService.findOwnerByUsername(adoption.getPossibleOwner());
			Owner owner = this.ownerService.findOwnerByUsername(adoption.getOwner());
			Pet pet = adoption.getPet();
			
			owner.removePet(pet);
			possibleOwner.addPet(pet);
			pet.setInAdoption(false);
			
			List<Adoption> adoptions =pet.getAdoptions();
			for(Adoption adop: adoptions) {
				if(adop.getAdoptionStateType().equals(AdoptionStateType.PENDING)) {
					this.adoptionService.denyAdoptionApplication(adop);
				}
			}
			
			this.ownerService.saveOwner(owner);
			this.ownerService.saveOwner(possibleOwner);
			this.petService.savePet(pet);
			model.put("pendingAdoption", AdoptionStateType.PENDING);
			return "redirect:/adoptions/pendingAdoptionsList";
		}
	}
	
	@GetMapping(value="/deny/{adoptionId}")
	public String denyAdoptionApplication(@PathVariable("adoptionId") int adoptionId, Authentication authentication,
		Map<String, Object> model) throws Exception {
		Boolean authenticated = authentication.isAuthenticated();
		Adoption adoption = this.adoptionService.findAdoptionById(adoptionId);
		
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Owner existingOwner = this.ownerService.findOwnerByUsername(userDetails.getUsername());
		
		 if(existingOwner!=null || !authenticated){
			return "welcome";
		}else {
			this.adoptionService.denyAdoptionApplication(adoption);
			model.put("pendingAdoption", AdoptionStateType.PENDING);
			return "redirect:/adoptions/pendingAdoptionsList";
		}	
	}
}