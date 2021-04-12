package org.springframework.samples.petclinic.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
	private Integer target;
	
	@Column(name="organisation")
	@NotBlank
	private String organisation;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cause")
	private Set<Donation> donations;
}
