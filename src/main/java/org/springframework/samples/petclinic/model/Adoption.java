package org.springframework.samples.petclinic.model;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;



@Entity
@Table(name = "adoptions")
public class Adoption extends BaseEntity{
	@NotEmpty
	@Column(name = "owner")
	private String owner;
	
	@NotEmpty
	@Column(name = "possible_owner")
	private String possibleOwner;
	
	@NotEmpty
	@Column(name = "description")
	private String description;
	
	@Column(name = "adoption_state_type")
	private AdoptionStateType adoptionStateType;
	
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;
	
	
	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPossibleOwner() {
		return this.possibleOwner;
	}
	
	public void setPossibleOwner(String possibleOwner) {
		this.possibleOwner = possibleOwner;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public AdoptionStateType getAdoptionStateType() {
		return this.adoptionStateType;
	}
	
	public void setAdoptionStateType(AdoptionStateType adoptionStateType) {
		this.adoptionStateType = adoptionStateType;
	}
	
	public Pet getPet() {
		return this.pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}
}