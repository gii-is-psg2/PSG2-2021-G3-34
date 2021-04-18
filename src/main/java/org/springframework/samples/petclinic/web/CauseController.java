package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.DonationService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CauseController {

	private static final String VIEWS_CAUSE_CREATE_OR_UPDATE_FORM = "causes/createOrUpdateCauseForm";
    private final CauseService causeService;
    private final DonationService donationService;


    @Autowired
    public CauseController(CauseService causeService, DonationService donationService) {
        this.causeService = causeService;
        this.donationService = donationService;
    }

    @GetMapping(value = { "/causes"} )
    public String showCauseList(Map<String, Object> model) {
        List<Cause> causes = new ArrayList<>();
        causes.addAll(this.causeService.findCauses());

        List<Double> donations=new ArrayList<>(this.donationService.findDonationsByCauses(causes));

        Map<Cause,Double> res=new HashMap<>();
        for(int i=0;i<causes.size();i++) {
        	res.put(causes.get(i), donations.get(i));
        }
        model.put("map", res);
        return "causes/causeList";
    }

    @GetMapping(value = "/causes/new")
    public String initCreationForm(Map<String, Object> model) {
        Cause cause = new Cause();
        cause.setIsClosed(false);
        model.put("cause", cause);
        return VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/causes/new")
    public String processCreationForm(@Valid Cause cause, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
        } else {
            this.causeService.saveCause(cause);
            return "redirect:/causes";    
        }
    }

    @GetMapping("/causes/{causeId}")
    public ModelAndView showCause(@PathVariable("causeId") int causeId, Map<String, Object> model) {
    	Collection<Donation> donations;
    	donations = this.donationService.findDonations(causeId);
        model.put("donations", donations);
        ModelAndView mav = new ModelAndView("causes/causeDetails");
        mav.addObject("cause",this.causeService.findCauseById(causeId));
        return mav;
    }
}
