package site.packit.packit.domain.item.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.packit.packit.domain.checkList.entity.CheckList;
import site.packit.packit.domain.checkList.repository.CheckListRepository;
import site.packit.packit.domain.item.dto.CreateItemRequest;
import site.packit.packit.domain.item.entity.Item;
import site.packit.packit.domain.item.repository.ItemRepository;
import site.packit.packit.domain.travel.entity.Travel;
import site.packit.packit.domain.travel.repository.TravelRepository;
import site.packit.packit.global.exception.ResourceNotFoundException;

import static site.packit.packit.domain.checkList.excepiton.CheckListErrorCode.CHECKLIST_NOT_FOUND;
import static site.packit.packit.domain.travel.exception.TravelErrorCode.TRAVEL_NOT_FOUND;

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

    /**
     * 새로운 체크리스트 아이템 생성
     */
    @Transactional
    public Long createCheckList(Long travelId, Long checklistId, CreateItemRequest createItemRequest){

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new ResourceNotFoundException(TRAVEL_NOT_FOUND));

        CheckList checkList = checkListRepository.findById(checklistId)
                .orElseThrow(() -> new ResourceNotFoundException(CHECKLIST_NOT_FOUND));


        // 체크리스트에 속한 아이템 중 가장 큰 listOrder 값을 찾기
        Integer maxListOrder = itemRepository.findMaxListOrderByCheckList(checkList);

        if (maxListOrder == null) {
            maxListOrder = 0;
        }

        Item newitem = Item.builder()
                .title(createItemRequest.title())
                .listOrder(maxListOrder+1)
                .checkList(checkList)
                .build();

        itemRepository.save(newitem);
        return newitem.getId();

    }




}
