package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.repository.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationService {

	private final DonationRepository donationRepository;
	
	@Autowired
	public DonationService(final DonationRepository donationRepository) {
		this.donationRepository = donationRepository;
	}
	
	@Transactional(readOnly = true)
	public Collection<Donation> findAll(){
		return this.donationRepository.findAll();
	}
	
	@Transactional
	public void saveDonation(final Donation donation) throws DataAccessException {
		this.donationRepository.save(donation);
	}
}
