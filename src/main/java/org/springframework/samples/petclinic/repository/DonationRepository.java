package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Donation;

public interface DonationRepository extends Repository<Donation, Integer>{

	Collection<Donation> findAll() throws DataAccessException;
	
	void save(Donation donation) throws DataAccessException;
}
