package org.springframework.samples.petclinic.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.BookingService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedBookingException;
import org.springframework.samples.petclinic.service.exceptions.NoRoomsAvailableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("owners/{ownerId}")
public class BookingController {

	
	private final BookingService bookingService;
	private final OwnerService ownerService;
	private static final String VISTA_EDICION_BOOKING= "bookings/createOrUpdateBookingForm";
	
	@Autowired
	public BookingController(final BookingService bookingService, final OwnerService ownerService) {

		this.bookingService = bookingService;
		this.ownerService = ownerService;
	}			
	
	@GetMapping("/bookings/new")
	public String initAddReview(@PathVariable("ownerId") final int ownerId, final ModelMap modelMap) {
		final Owner owner = this.ownerService.findOwnerById(ownerId);
		final Booking booking = this.bookingService.createBooking();
		
		final List<String> petsNames = owner.getPets().stream().map(p->p.getName()).collect(Collectors.toList());
		modelMap.put("owner", owner);
		modelMap.put("petsNames", petsNames);
		modelMap.put("booking", booking);
		
		
		return BookingController.VISTA_EDICION_BOOKING;
	}
	
	@PostMapping("/bookings/new")
	public String processNewHotel(@PathVariable("ownerId") final int ownerId, @Valid Booking booking, final BindingResult result,
			final ModelMap modelMap, final RedirectAttributes redirectAttributes) throws DuplicatedBookingException {
		
		modelMap.put("buttonCreate", true);
		final Owner owner = this.ownerService.findOwnerById(ownerId);
		
		
//			
		// Si hay errores
		if(result.hasErrors()) {
			final List<String> petsNames = owner.getPets().stream().map(p->p.getName()).collect(Collectors.toList());
			modelMap.put("booking", booking);
			modelMap.put("owner", owner);
			modelMap.put("petsNames", petsNames);
			return BookingController.VISTA_EDICION_BOOKING;
		}
		
		// No hay errores
		
		else { 
			try {
				int i = 0;
				final List<Pet> pets = owner.getPets().stream().collect(Collectors.toList());
				Pet optPet = null;
				while(i < pets.size()) {
					if(pets.get(i).getName().equals(booking.getPet().getName())) {
						optPet = pets.get(i);
						break;
					}
					i++;
				}
				booking.setPet(optPet);
				this.bookingService.saveBooking(booking);
				modelMap.addAttribute("messageSuccess", "Reserva creada con exito");
				redirectAttributes.addFlashAttribute("message", String.format("La reserva para la mascota '%s' ha sido añadida.", owner.getFirstName()));
				redirectAttributes.addFlashAttribute("messageType", "success");
				return "redirect:/owners/{ownerId}";
			} catch (final NoRoomsAvailableException e) {
				result.rejectValue("text", "No hay habitaciones disponibles" ,"No quedan habitaciones libres. Intentelo mas tarde.");
				return BookingController.VISTA_EDICION_BOOKING;
			} catch (final DuplicatedBookingException e) {
				result.rejectValue("text", "Reservas duplicadas" ,"Ya tiene una reserva para ese periodo de tiempo");
				return BookingController.VISTA_EDICION_BOOKING;
			}
			
		}
			
		
	}
	

	@GetMapping(value = "/bookings/{bookingId}/delete")
	@PreAuthorize("hasAuthority('admin') || hasAuthority('owner') && @isSameBookingOwner.hasPermission(#bookingId)")
	public String deleteBooking(@PathVariable("bookingId") final int bookingId, final ModelMap model, final RedirectAttributes redirectAttributes) {
			final Booking res = this.bookingService.deleteBooking(bookingId);
			if (res==null) {
				redirectAttributes.addFlashAttribute("message", "Error al borrar la reserva");
			}
			else {
				redirectAttributes.addFlashAttribute("message", "Reserva borrada con exito");
			}
			return "redirect:/owners/{ownerId}";
	}
	
}
