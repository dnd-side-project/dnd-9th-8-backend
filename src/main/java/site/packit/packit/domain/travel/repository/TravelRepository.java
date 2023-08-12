package site.packit.packit.domain.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.packit.packit.domain.travel.entity.Travel;

public interface TravelRepository
        extends JpaRepository<Travel, Long> {

}