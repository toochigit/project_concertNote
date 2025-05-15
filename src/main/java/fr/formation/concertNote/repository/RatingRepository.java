package fr.formation.concertNote.repository;


import fr.formation.concertNote.model.Rating;
import fr.formation.concertNote.model.Concert;
import fr.formation.concertNote.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByConcert(Concert concert);

    boolean existsByUserAndConcert(User user, Concert concert);


}
