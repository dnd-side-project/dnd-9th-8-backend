package site.packit.packit.domain.checkList.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.packit.packit.domain.checkList.dto.CreateCheckListRequest;
import site.packit.packit.domain.checkList.service.CheckListService;
import site.packit.packit.domain.travel.dto.CreateTravelRequest;
import site.packit.packit.global.response.success.SingleSuccessApiResponse;

@RequestMapping("/api")
@RestController
public class CheckListController {

    private final CheckListService checkListService;

    public CheckListController(CheckListService checkListService) {
        this.checkListService = checkListService;
    }


    /**
     * 새로운 체크리스트 생성
     */
    @PostMapping(value="{travelId}/check-lists")
    public ResponseEntity<SingleSuccessApiResponse<Long>> createCheckList(
            @PathVariable Long travelId, @RequestBody CreateCheckListRequest createCheckListRequest
    ) {
        Long checkListId = checkListService.createCheckList(travelId, createCheckListRequest);

        return ResponseEntity.ok(
                SingleSuccessApiResponse.of(
                        "새로운 체크리스트가 생성되었습니다.", checkListId
                )
        );
    }

}
