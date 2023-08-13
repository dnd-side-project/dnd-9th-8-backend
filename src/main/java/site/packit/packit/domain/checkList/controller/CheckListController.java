package site.packit.packit.domain.checkList.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.packit.packit.domain.checkList.dto.CreateCheckListRequest;
import site.packit.packit.domain.checkList.dto.UpdateCheckListRequest;
import site.packit.packit.domain.checkList.service.CheckListService;
import site.packit.packit.global.response.success.SingleSuccessApiResponse;
import site.packit.packit.global.response.success.SuccessApiResponse;

import java.util.List;

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
    @PostMapping(value="travels/{travelId}/check-lists")
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

    /**
     * 체크리스트 순서 수정
     */
    @PatchMapping(value = "travels/{travelId}/check-lists/order")
    public ResponseEntity<SuccessApiResponse> updateCheckList(
            @PathVariable Long travelId,@RequestBody List<UpdateCheckListRequest> updateCheckListRequests
    ){

        checkListService.updateCheckListOrder(travelId, updateCheckListRequests);

        return ResponseEntity.ok(
                SuccessApiResponse.of(
                        "체크리스트 순서 수정이 완료되었습니다."
                )
        );

    }

    /**
     * 체크리스트 삭제
     */
    @DeleteMapping(value = "travels/{travelId}/check-lists/{checkListId}")
    public ResponseEntity<SuccessApiResponse> deleteCheckList(
            @PathVariable Long travelId, @PathVariable Long checkListId
    ){
        checkListService.deleteCheckListAndReorder(travelId, checkListId);

        return ResponseEntity.ok(
                SuccessApiResponse.of(
                        "체크리스트 삭제가 완료되었습니다."
                )
        );

    }


}
