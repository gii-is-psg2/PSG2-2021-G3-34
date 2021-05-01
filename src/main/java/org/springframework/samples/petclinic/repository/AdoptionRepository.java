package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.model.Pet;



public interface AdoptionRepository  extends CrudRepository<Adoption, Integer>  {
//	void save(Adoption adoption) throws DataAccessException;
//
//	void delete(Adoption adoption) throws DataAccessException;

	Adoption findById(int id) throws DataAccessException;
	
	Adoption findByPossibleOwnerAndPet(String possibleOwner, Pet pet) throws DataAccessException;

	
}
