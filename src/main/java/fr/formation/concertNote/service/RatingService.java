package fr.formation.concertNote.service;

import fr.formation.concertNote.model.Concert;
import fr.formation.concertNote.model.Rating;
import fr.formation.concertNote.model.User;
import fr.formation.concertNote.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public Rating saveRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public List<Rating> getRatingsForConcert(Concert concert) {
        return ratingRepository.findByConcert(concert);
    }

    public boolean hasUserAlreadyRated(User user, Concert concert) {
        return ratingRepository.existsByUserAndConcert(user, concert);
    }

}
