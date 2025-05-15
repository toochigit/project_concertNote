package fr.formation.concertNote.service;

import fr.formation.concertNote.model.Concert;
import fr.formation.concertNote.repository.ConcertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConcertService {

    @Autowired
    private ConcertRepository concertRepository;

    public List<Concert> getAllConcerts() {
        return concertRepository.findAll();
    }

    public Concert saveConcert(Concert concert) {
        return concertRepository.save(concert);
    }

    public Optional<Concert> findById(Long id) {
        return concertRepository.findById(id);
    }

    public Optional<Concert> findWithRatings(Long id) {
        return concertRepository.findById(id); // les ratings sont déjà chargés grâce au mappedBy
    }

    public void deleteConcert(Concert concert) {
        concertRepository.delete(concert);
    }

}
