package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.repository.CauseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CauseService {

	private final CauseRepository causeRepository;
	
	
	@Autowired
	public CauseService(final CauseRepository causeRepository) {
		this.causeRepository = causeRepository;
	}
	
	@Transactional(readOnly = true)
	public Collection<Cause> findAll(){
		return this.causeRepository.findAll();
	}
	
	@Transactional
	public void saveCause(final Cause cause) throws DataAccessException {
		this.causeRepository.save(cause);
	}
	
	public Integer getTotalBudgetAchieved(Cause cause) {
		return cause.getTarget() - cause.getDonations().stream().map(x->x.getAmount())
				.mapToInt(Integer::valueOf).sum();
	}
}
