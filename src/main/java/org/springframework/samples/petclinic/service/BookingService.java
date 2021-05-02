package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.repository.BookingRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedBookingException;
import org.springframework.samples.petclinic.service.exceptions.NoRoomsAvailableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

	private final BookingRepository bookingRepository;

	private final OwnerService ownerService;

	private final PetService petService;

	@Autowired
	public BookingService(final BookingRepository bookingRepository, final OwnerService ownerService,
			final PetService petService) {
		this.bookingRepository = bookingRepository;
		this.ownerService = ownerService;
		this.petService = petService;
	}

	@Transactional
	public void saveBooking(final Booking booking) throws NoRoomsAvailableException, DuplicatedBookingException {
		Collection<Integer> usedRooms = this.bookingRepository.findUsedRooms(booking.getStartDate(), booking.getEndDate());
		if (usedRooms.size() >= 20) {
			throw new NoRoomsAvailableException();
		} if (bookingRepository.findAll().stream().anyMatch(x->((!booking.getStartDate().isAfter(x.getEndDate()) && 
				!booking.getStartDate().isBefore(x.getStartDate())) || (!booking.getEndDate().isAfter(x.getEndDate()) && 
				!booking.getEndDate().isBefore(x.getStartDate()))) && x.getPet() == booking.getPet())) {
			throw new DuplicatedBookingException();
		}
		Boolean aux = true;
		int possibleRoom = 1;
		while (aux) {
			if (usedRooms.contains(possibleRoom))
				possibleRoom += 1;
			else
				aux = false;
		}
		booking.setRoom(possibleRoom);
		this.bookingRepository.save(booking);
		
	}

	@Transactional
	public Booking deleteBooking(final int bookingId) {
		final Booking booking = this.findBookingById(bookingId);
		if (booking == null) {
			return null;
		} else {
			this.bookingRepository.deleteById(bookingId);
			return booking;
		}

	}

	public Booking findBookingById(final int bookingId) {
		return this.bookingRepository.findBookingById(bookingId);
	}

	public Booking createBooking() {
		final Booking res = new Booking();
		return res;
	}

	public Collection<Integer> findUsedRooms(final LocalDate startDate, final LocalDate endDate) {

		return this.bookingRepository.findUsedRooms(startDate, endDate);
	}
	
	//Validacion
	
	public Collection<Booking> findBookingsByPetId(int petId) {
		return bookingRepository.findBookingsByPetId(petId);
	}

	
	

}