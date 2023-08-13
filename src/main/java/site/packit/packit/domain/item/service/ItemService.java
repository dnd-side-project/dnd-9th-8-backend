package site.packit.packit.domain.item.service;

import org.springframework.stereotype.Service;
import site.packit.packit.domain.checkList.repository.CheckListRepository;
import site.packit.packit.domain.item.repository.ItemRepository;
import site.packit.packit.domain.travel.repository.TravelRepository;

@Service
public class ItemService {
    private final CheckListRepository checkListRepository;
    private final TravelRepository travelRepository;
    private final ItemRepository itemRepository;

    public ItemService(CheckListRepository checkListRepository, TravelRepository travelRepository, ItemRepository itemRepository) {
        this.checkListRepository = checkListRepository;
        this.travelRepository = travelRepository;
        this.itemRepository = itemRepository;
    }


}
