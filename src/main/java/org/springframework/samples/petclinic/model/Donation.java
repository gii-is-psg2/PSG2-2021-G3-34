package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "donations")
public class Donation  extends BaseEntity {

	@NotNull
	@Column(name = "amount")
	@Min(0)
	private Double amount;
	
    @NotNull
    @Column(name =  "date_of_donation")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate date;
	
    @NotBlank
    @Column(name="client")
    private String client;
	
    @ManyToOne
    @JoinColumn(name = "cause_id")
    private Cause cause;
}
