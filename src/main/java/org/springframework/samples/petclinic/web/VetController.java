/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class VetController {

	private final VetService vetService;


	@Autowired
    public VetController(VetService clinicService) {
        this.vetService = clinicService;
    }
	 
	@ModelAttribute("specialties")
	public Collection<Specialty> populateSpecialties() {
	    return this.vetService.findSpecialties();
	}

	@GetMapping(value = { "/vets" })
	public String showVetList(Map<String, Object> model) {
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		model.put("vets", vets);
		return "vets/vetList";
	}

	@GetMapping(value = { "/vets.xml"})
	public @ResponseBody Vets showResourcesVetList() {
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		return vets;
	}
	
	 @GetMapping(value = { "/vets/{vetId}/delete"})
 	public String delete(@PathVariable("vetId") int vetId, ModelMap model) {
 		this.vetService.deleteVet(vetId);
 		return "redirect:/vets";
 	}

	@GetMapping(path="/vets/save")
	public String crearVeterinario(ModelMap modelmap) {
		String vista = "vets/createOrUpdateVetForm";
		modelmap.addAttribute("vet", new Vet());
		return vista;
	}
	
	@PostMapping(path="/vets/save")
    public String guardarAutor(@Valid Vet vet, BindingResult result, ModelMap modelmap, Principal principal) {
        if(result.hasErrors()) {
            modelmap.addAttribute("vet", vet);
            return "autores/editAutor";
        }else {
            vetService.save(vet);
            modelmap.addAttribute("message", "Veterinario guardado correctamente");
            return showVetList(modelmap);
        }
    }
	
	   @PostMapping(value = "/vets/{vetId}/edit")
	    public String processUpdateForm(@Valid Vet vet, BindingResult result,@PathVariable("vetId") int vetId, ModelMap model) {
	        if (result.hasErrors()) {
	            model.put("vet", vet);
	            return "vets/createOrUpdateVetForm";
	        }
	        else {
	            Vet vetToUpdate = this.vetService.findVetById(vetId);
	            vet.setId(vetToUpdate.getId());
	            this.vetService.save(vet);                    
	                    
	            return showVetList(model);
	        }
	    }
	   
	   @GetMapping(value = "/vets/{vetId}/edit")
	    public String initUpdateForm(@PathVariable("vetId") int vetId, ModelMap model) {
	        Vet vet = this.vetService.findVetById(vetId);
	        model.put("vet", vet);
	        return "vets/createOrUpdateVetForm";
	    }
	   
}
