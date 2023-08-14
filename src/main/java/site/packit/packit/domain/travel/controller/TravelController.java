package site.packit.packit.domain.travel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.packit.packit.domain.travel.dto.*;
import site.packit.packit.domain.travel.service.TravelService;
import site.packit.packit.global.response.success.MultipleSuccessApiResponse;
import site.packit.packit.global.response.success.SingleSuccessApiResponse;
import site.packit.packit.global.response.success.SuccessApiResponse;

import java.util.List;


@RequestMapping("/api/travels")
@RestController
public class TravelController {

    private final TravelService travelService;
    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    /**
     * 새로운 여행 생성
     */
    @PostMapping(value="")
    public ResponseEntity<SingleSuccessApiResponse<Long>> createTravel(
            @RequestBody CreateTravelRequest createTravelRequest
    ) {
        Long travelId = travelService.createTravel(createTravelRequest);

        return ResponseEntity.ok(
                SingleSuccessApiResponse.of(
                        "새로운 여행이 생성되었습니다.", travelId
                )
        );
    }

    /**
     * 여행 수정
     */
    @PatchMapping(value = "{travelId}")
    public ResponseEntity<SingleSuccessApiResponse<Long>> updateTravel(
            @PathVariable Long travelId, @RequestBody UpdateTravelRequest updateTravelRequest
    ){
        travelService.updateTravel(travelId, updateTravelRequest);

        return ResponseEntity.ok(
                SingleSuccessApiResponse.of(
                        "여행 수정에 성공했습니다.", travelId
                )
        );
    }

    /**
     * 여행 삭제
     */
    @DeleteMapping(value = "{travelId}")
    public ResponseEntity<SuccessApiResponse> deleteTravel(
            @PathVariable Long travelId
    ){
        travelService.deleteTravel(travelId);

        return ResponseEntity.ok(
                SingleSuccessApiResponse.of(
                        "여행 삭제에 성공했습니다."
                )
        );
    }

    /**
     * 예정된 여행 조회
     */
    @GetMapping(value = "upcoming")
    public ResponseEntity<MultipleSuccessApiResponse<TravelListDto>> getUpcomingTravel(
            @RequestBody GetTravelRequest getTravelRequest
    ){
        List<TravelListDto> upcomingTravels = travelService.getUpcomingTravel(getTravelRequest);
        return ResponseEntity.ok(
                MultipleSuccessApiResponse.of(
                        "예정된 여행 조회에 성공했습니다.", upcomingTravels
                ));
    }

    /**
     * 지난 여행 조회
     */
    @GetMapping(value = "past")
    public ResponseEntity<MultipleSuccessApiResponse<TravelListDto>> getPastTravel(
            @RequestBody GetTravelRequest getTravelRequest
    ){
        List<TravelListDto> upcomingTravels = travelService.getPastTravel(getTravelRequest);
        return ResponseEntity.ok(
                MultipleSuccessApiResponse.of(
                        "지난 여행 조회에 성공했습니다.", upcomingTravels
                ));
    }

    /**
     * 여행 상세 조회
     */
    @GetMapping(value = "{travelId}")
    public ResponseEntity<SingleSuccessApiResponse<TravelDetailDto>> getDetailTravel(
            @PathVariable Long travelId
    ){
        TravelDetailDto travelDetailDto = travelService.getDetailTravel(travelId);
        return ResponseEntity.ok(
                SingleSuccessApiResponse.of(
                        "여행 상세 조회에 성공했습니다.", travelDetailDto
                )
        );
    }

    /**
     * 여행 상세 조회
     */

}
