package fr.formation.concertNote.controller;

import fr.formation.concertNote.dto.RatingDto;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import fr.formation.concertNote.model.Concert;
import fr.formation.concertNote.model.Rating;
import fr.formation.concertNote.model.User;
import fr.formation.concertNote.service.ConcertService;
import fr.formation.concertNote.service.RatingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private ConcertService concertService;

    @GetMapping("/add/{concertId}")
    public String showAddRatingForm(@PathVariable Long concertId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Concert concert = concertService.findById(concertId).orElse(null);
        if (concert == null) {
            return "redirect:/concerts";
        }

        // ❗ Empêcher de noter deux fois
        boolean alreadyRated = ratingService.hasUserAlreadyRated(user, concert);
        if (alreadyRated) {
            // On peut soit rediriger avec un paramètre flash, soit afficher un message
            return "redirect:/concerts/" + concertId + "?alreadyRated=true";
        }

        model.addAttribute("concert", concert);
        model.addAttribute("ratingDto", new RatingDto());
        return "add-rating";
    }

    @PostMapping("/add/{concertId}")
    public String submitRating(@PathVariable Long concertId,
                               @Valid @ModelAttribute("ratingDto") RatingDto dto,
                               BindingResult result,
                               HttpSession session,
                               Model model)
    {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Concert concert = concertService.findById(concertId).orElse(null);
        if (concert == null) {
            return "redirect:/concerts";
        }

        if (result.hasErrors()) {
            model.addAttribute("concert", concert);
            return "add-rating";
        }

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setConcert(concert);
        rating.setScore(dto.getScore());
        rating.setComment(dto.getComment());

        ratingService.saveRating(rating);

        return "redirect:/concerts/" + concertId;
    }
}
