package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name="causes")
public class Cause extends NamedEntity {

	@Column(name="description")
	@NotBlank
	private String description;
	
	@Column(name="target")
	@NotNull
	@Min(0)
	private Integer budgetTarget;
	
	@Column(name="organisation")
	@NotBlank
	private String organisation;

	@NotNull
	@Column(name = "is_closed")
	private Boolean isClosed;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cause")
	private Set<Donation> donations;
	
	protected Set<Donation> getDonationsInternal() {
	    if (this.donations == null) {
	       this.donations = new HashSet<>();
	    }
	    return this.donations;
	}

	public List<Donation> getDonations() {
	    List<Donation> sortedDonations = new ArrayList<>(getDonationsInternal());
	    PropertyComparator.sort(sortedDonations, new MutableSortDefinition("date", false, false));
	    return Collections.unmodifiableList(sortedDonations);
	}

	public void addDonation(Donation donation) {
	    getDonationsInternal().add(donation);
	    donation.setCause(this);
	}
}
