package fr.formation.concertNote.controller;


import fr.formation.concertNote.dto.ConcertDto;
import fr.formation.concertNote.model.Concert;
import fr.formation.concertNote.model.User;
import fr.formation.concertNote.service.ConcertService;
import fr.formation.concertNote.service.RatingService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.OptionalDouble;

@Controller
@RequestMapping("/concerts")
public class ConcertController {

    @Autowired
    private ConcertService concertService;


    @Autowired
    private RatingService ratingService; // ✅ À AJOUTER ICI

    @GetMapping
    public String listConcerts(Model model, HttpSession session) {
        model.addAttribute("concerts", concertService.getAllConcerts());
        model.addAttribute("user", session.getAttribute("user"));
        return "concerts";
    }

    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("concertDto", new ConcertDto());
        return "add-concert";
    }

    @PostMapping("/add")
    public String addConcert(@Valid @ModelAttribute("concertDto") ConcertDto dto,
                             BindingResult result,
                             HttpSession session)
    {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        if (result.hasErrors()) {
            return "add-concert";
        }

        Concert concert = new Concert();
        concert.setBand(dto.getBand());
        concert.setVenue(dto.getVenue());
        concert.setDate(dto.getDate());

        concertService.saveConcert(concert);
        return "redirect:/concerts";
    }

    @GetMapping("/{id}")
    public String concertDetail(@PathVariable Long id, Model model, HttpSession session) {
        Optional<Concert> concert = concertService.findById(id);
        if (concert.isPresent()) {
            model.addAttribute("concert", concert.get());
            OptionalDouble average = ratingService.getAverageRatingForConcert(concert.get());
            model.addAttribute("averageRating", average);
            model.addAttribute("user", session.getAttribute("user"));
            return "concert-detail";
        } else {
            return "redirect:/concerts";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Optional<Concert> concert = concertService.findById(id);
        if (concert.isEmpty()) return "redirect:/concerts";

        Concert c = concert.get();
        ConcertDto dto = new ConcertDto();
        dto.setBand(c.getBand());
        dto.setVenue(c.getVenue());
        dto.setDate(c.getDate());

        model.addAttribute("concertDto", dto);
        model.addAttribute("concertId", c.getId());

        return "edit-concert";
    }

    @PostMapping("/edit/{id}")
    public String updateConcert(@PathVariable Long id,
                                @Valid @ModelAttribute("concertDto") ConcertDto dto,
                                BindingResult result,
                                HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Optional<Concert> existing = concertService.findById(id);
        if (existing.isEmpty()) return "redirect:/concerts";

        if (result.hasErrors()) {
            model.addAttribute("concertId", id); // pour le formulaire
            return "edit-concert";
        }

        Concert concert = existing.get();
        concert.setBand(dto.getBand());
        concert.setVenue(dto.getVenue());
        concert.setDate(dto.getDate());

        concertService.saveConcert(concert);
        return "redirect:/concerts/" + id;

    }

    @PostMapping("/delete/{id}")
    public String deleteConcert(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        concertService.findById(id).ifPresent(concertService::deleteConcert);
        return "redirect:/concerts";
    }


}
