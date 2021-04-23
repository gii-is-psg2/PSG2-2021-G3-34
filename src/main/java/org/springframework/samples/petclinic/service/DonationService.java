package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.repository.CauseRepository;
import org.springframework.samples.petclinic.repository.DonationRepository;
import org.springframework.stereotype.Service;

@Service
public class DonationService {

	private final DonationRepository donationRepository;
	private final CauseRepository causeRepository;
	
	@Autowired
	public DonationService(DonationRepository donationRepository, CauseRepository causeRepository) {
		this.donationRepository = donationRepository;
		this.causeRepository = causeRepository;
	}
	
	public Donation findByDonationId(int donationId)  {
		return donationRepository.findByDonationId(donationId);
	}	


	public void saveDonation(Donation donation) throws DataAccessException  {
		donationRepository.save(donation);
	}

	public Double totalBudget(int causeId)  {
		return causeRepository.totalBudget(causeId);
	}

	public Collection<Donation> findDonations(int causeId)  {
		return causeRepository.findDonations(causeId);
	}

	public List<Double> findDonationsByCauses(List<Cause> causes) {
		List<Double> res=new ArrayList<>();
		for(Cause c:causes) {
			Double res1=0.;
				for(Donation d:this.findDonationsByCauseId(c.getId())) {
					res1+=d.getAmount();
			
					}
			res.add(res1);
		}
		return res;
	}

	private Collection<Donation> findDonationsByCauseId(Integer id) {
		return donationRepository.findByCauseId(id);
	}
}
