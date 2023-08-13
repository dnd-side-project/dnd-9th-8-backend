package site.packit.packit.domain.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.packit.packit.domain.item.entity.Item;

public interface ItemRepository
        extends JpaRepository<Item, Long> {

}
