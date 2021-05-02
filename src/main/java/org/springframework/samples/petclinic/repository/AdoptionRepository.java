package org.springframework.samples.petclinic.repository;


import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.model.Pet;



public interface AdoptionRepository  extends CrudRepository<Adoption, Integer>  {

	Adoption findById(int id) throws DataAccessException;
	
	Adoption findByPossibleOwnerAndPet(String possibleOwner, Pet pet) throws DataAccessException;

	
}
