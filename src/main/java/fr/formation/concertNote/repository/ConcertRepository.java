package fr.formation.concertNote.repository;


import fr.formation.concertNote.model.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
}
