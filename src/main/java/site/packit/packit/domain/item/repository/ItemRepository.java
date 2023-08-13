package site.packit.packit.domain.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.packit.packit.domain.checkList.entity.CheckList;
import site.packit.packit.domain.item.entity.Item;
import site.packit.packit.domain.travel.entity.Travel;

public interface ItemRepository
        extends JpaRepository<Item, Long> {

    @Query("SELECT COALESCE(MAX(c.listOrder), 0) FROM Item c WHERE c.checkList = :checkList")
    Integer findMaxListOrderByCheckList(CheckList checkList);


}
