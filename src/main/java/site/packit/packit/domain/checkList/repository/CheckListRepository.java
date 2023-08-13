package site.packit.packit.domain.checkList.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.packit.packit.domain.checkList.entity.CheckList;
import site.packit.packit.domain.travel.entity.Travel;

public interface CheckListRepository
        extends JpaRepository<CheckList, Long> {

//    Integer findTopByTravelOrderByListOrderDesc(Travel travel);

    @Query("SELECT COALESCE(MAX(c.listOrder), 0) FROM CheckList c WHERE c.travel = :travel")
    Integer findMaxListOrderByTravel(Travel travel);

}