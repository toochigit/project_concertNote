package fr.formation.concertNote.controller;

import fr.formation.concertNote.model.Concert;
import fr.formation.concertNote.model.User;
import fr.formation.concertNote.service.ConcertService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/concerts")
public class ConcertController {

    @Autowired
    private ConcertService concertService;

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
        model.addAttribute("concert", new Concert());
        return "add-concert";
    }

    @PostMapping("/add")
    public String addConcert(@ModelAttribute Concert concert, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        concertService.saveConcert(concert);
        return "redirect:/concerts";
    }

    @GetMapping("/{id}")
    public String concertDetail(@PathVariable Long id, Model model, HttpSession session) {
        Optional<Concert> concert = concertService.findById(id);
        if (concert.isPresent()) {
            model.addAttribute("concert", concert.get());
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

        model.addAttribute("concert", concert.get());
        return "edit-concert";
    }

    @PostMapping("/edit/{id}")
    public String updateConcert(@PathVariable Long id,
                                @ModelAttribute Concert updatedConcert,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Optional<Concert> existing = concertService.findById(id);
        if (existing.isEmpty()) return "redirect:/concerts";

        Concert concert = existing.get();
        concert.setBand(updatedConcert.getBand());
        concert.setVenue(updatedConcert.getVenue());
        concert.setDate(updatedConcert.getDate());

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
