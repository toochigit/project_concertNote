package fr.formation.concertNote.controller;

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
        model.addAttribute("rating", new Rating());
        return "add-rating";
    }

    @PostMapping("/add/{concertId}")
    public String submitRating(@PathVariable Long concertId,
                               @ModelAttribute Rating rating,
                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Concert concert = concertService.findById(concertId).orElse(null);
        if (concert == null) {
            return "redirect:/concerts";
        }

        rating.setConcert(concert);
        rating.setUser(user);
        ratingService.saveRating(rating);

        return "redirect:/concerts/" + concertId;
    }
}
