package site.packit.packit.domain.item.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.packit.packit.domain.item.dto.CreateItemRequest;
import site.packit.packit.domain.item.service.ItemService;
import site.packit.packit.global.response.success.SingleSuccessApiResponse;

@RequestMapping("/api")
@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * 새로운 체크리스트 아이템 생성
     */
    @PostMapping(value="/travels/{travelId}/check-lists/{checklistId}/items")
    public ResponseEntity<SingleSuccessApiResponse<Long>> createCheckListItem(
            @PathVariable Long travelId, @PathVariable Long checklistId, @RequestBody CreateItemRequest createItemRequest
    ) {
        Long itemId = itemService.createCheckList(travelId, checklistId, createItemRequest);

        return ResponseEntity.ok(
                SingleSuccessApiResponse.of(
                        "새로운 아이템이 생성되었습니다.", itemId
                )
        );
    }

}
