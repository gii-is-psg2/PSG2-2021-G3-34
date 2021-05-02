package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.model.AdoptionStateType;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.AdoptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AdoptionService {
	
	private AdoptionRepository adoptionRepository;
	
	@Autowired
	public AdoptionService(AdoptionRepository adoptionRepository) {
		this.adoptionRepository = adoptionRepository;
	}
	
	@Transactional
	public void saveAdoption(Adoption adoption) throws DataAccessException {
		adoptionRepository.save(adoption);
	}
	
	@Transactional(readOnly = true)
	public Adoption findAdoptionById(int id) throws DataAccessException {
		return adoptionRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Iterable<Adoption> findAll(){
		return adoptionRepository.findAll();
	}
	
	@Transactional
	public Adoption findAdoptionByPossibleOwnerAndPet(String possibleOwner,Pet pet) throws DataAccessException {
		return adoptionRepository.findByPossibleOwnerAndPet(possibleOwner,pet);
	}

	@Transactional
	public List<Adoption> findAllAdoptionsWithPendingState(List<Adoption> adoptions){
		List<Adoption> res = new ArrayList<>();
		for (Adoption adoption: adoptions) {
			if(adoption.getAdoptionStateType().equals(AdoptionStateType.PENDING)) {
				res.add(adoption);
			}
		}
		return res;
	}
	
	@Transactional
	public void acceptAdoptionApplication(Adoption adoption) {
		adoption.setAdoptionStateType(AdoptionStateType.ACCEPTED);
		this.adoptionRepository.save(adoption);
	}
	
	@Transactional
	public void denyAdoptionApplication(Adoption adoption) {
		adoption.setAdoptionStateType(AdoptionStateType.DECLINED);
		this.adoptionRepository.save(adoption);
	}

	
	
}