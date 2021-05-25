package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.security.Principal;
import java.util.Collection;
import java.util.Comparator;

import org.springframework.beans.BeanUtils;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private final PetService petService;
        private final OwnerService ownerService;

	@Autowired
	public PetController(PetService petService, OwnerService ownerService) {
		this.petService = petService;
                this.ownerService = ownerService;
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable("ownerId") int ownerId) {
		return this.ownerService.findOwnerById(ownerId);
	}
        
       
	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetValidator());
	}

	@GetMapping(value = "/pets/new")
	public String initCreationForm(Owner owner, ModelMap model, Principal principal) {
		Pet pet = new Pet();
		if (!owner.getUser().getUsername().equals(principal.getName())) {
			model.addAttribute("message", "No puede añadir una mascota a otro dueño");
			return "owners/ownerDetails";
		}	
		model.put("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/pets/new")
	public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, ModelMap model, Principal principal) {	
		if (result.hasErrors()) {
			model.put("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}
		else if (!owner.getUser().getUsername().equals(principal.getName())) {
			model.addAttribute("message", "No puede añadir una mascota a otro dueño");
			return "owners/ownerDetails";
		}
		else {
            owner.addPet(pet);
                this.petService.savePet(pet);
            return "redirect:/owners/{ownerId}";
		}
	}

	@GetMapping(value = "/pets/{petId}/edit")
	public String initUpdateForm(@PathVariable("petId") int petId, ModelMap model, Principal principal) {
		Pet pet = this.petService.findPetById(petId);
		if (!pet.getOwner().getUser().getUsername().equals(principal.getName())) {
			model.addAttribute("message", "No puede actualizar una mascota que no es suya");
			return "owners/ownerDetails";
		}
		model.put("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

    /**
     *
     * @param pet
     * @param result
     * @param petId
     * @param model
     * @param owner
     * @param model
     * @return
     */
        @PostMapping(value = "/pets/{petId}/edit")
	public String processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner,@PathVariable("petId") int petId, ModelMap model, Principal principal) {
		if (result.hasErrors()) {
			model.put("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}
		else if (!owner.getUser().getUsername().equals(principal.getName())) {
			model.addAttribute("message", "No puede actualizar una mascota que no es suya");
			return "owners/ownerDetails";
		}
		else {
            Pet petToUpdate=this.petService.findPetById(petId);
			BeanUtils.copyProperties(pet, petToUpdate, "id","owner","visits");                                                                                  
            this.petService.savePet(petToUpdate);                    
                   
			return "redirect:/owners/{ownerId}";
		}
	}
        
        @GetMapping(value = { "/pets/{petId}/delete"})
    	public String delete(@PathVariable int petId, @PathVariable("ownerId") int ownerId, ModelMap model, Principal principal) {
    		Owner ow = this.ownerService.findOwnerById(ownerId);
        	Pet pet = this.petService.findPetById(petId);
    		if (!ow.getUser().getUsername().equals(principal.getName())) {
    			model.addAttribute("message", "No puede eliminar una mascota que no es suya");
    			return "owners/ownerDetails";
    		}
        	ow.removePet(pet);
    		this.petService.deletePet(pet.getId());
    		return "redirect:/owners/{ownerId}";
    	}

}