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
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
 
/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy 
 */
@Service
public class OwnerService {


	private final OwnerRepository ownerRepository;	
	private VisitRepository visitRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public OwnerService(final OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}	

	@Transactional(readOnly = true)
	public Owner findOwnerById(final int id) throws DataAccessException {
		return this.ownerRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Owner> findOwnerByLastName(final String lastName) throws DataAccessException {
		return this.ownerRepository.findByLastName(lastName);
	}

	@Transactional(readOnly = true)
	public Owner findOwnerByUsername(String username) throws DataAccessException {
		return ownerRepository.findByUsername(username);
	}
	
	@Transactional
	public void saveOwner(final Owner owner) throws DataAccessException {
		//creating owner
		this.ownerRepository.save(owner);		
		//creating user
		this.userService.saveUser(owner.getUser());
		//creating authorities

		this.authoritiesService.saveAuthorities(owner.getUser().getUsername(), "owner");
	}

	//A 2.3.3.a
	@Transactional(readOnly = true)
	public Owner getPrincipal(){
		Owner res = null;
		
		final User currentUser = this.userService.getPrincipal();
		if(currentUser != null) {
			final Optional<Owner> optionalOwner = this.ownerRepository.findByUserUsername(currentUser.getUsername());
			if(optionalOwner.isPresent()) {
				res = optionalOwner.get();
			}
		}
		return res;
	}
	
	public Optional<Owner> findByUserUsername(final String username) {
		
		return this.ownerRepository.findByUserUsername(username);
	}
	
	@Transactional
	public Owner deleteOwnerById(final int id) throws DataAccessException {
		final Owner owner = this.findOwnerById(id);
    	if(owner==null)
    		return null;
    	else {
    		this.ownerRepository.deleteById(id);
			return owner;
    	}
	}		
	
	public void deleteOwner(Owner owner) {
		ownerRepository.delete(owner.getId());
	}
	
	public void deleteVisitsByPetId(int petId) {
		visitRepository.deleteVisitsByPetId(petId);
		
	}

}
