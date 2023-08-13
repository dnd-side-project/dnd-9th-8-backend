package site.packit.packit.domain.checkList.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.packit.packit.domain.travel.entity.Travel;

public interface CheckListRepository
        extends JpaRepository<Travel, Long> {

}