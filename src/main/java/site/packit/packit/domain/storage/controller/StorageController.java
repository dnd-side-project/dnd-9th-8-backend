package site.packit.packit.domain.storage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.packit.packit.domain.storage.dto.StorageListDto;
import site.packit.packit.domain.storage.dto.UpdateStorage;
import site.packit.packit.domain.storage.service.StorageService;
import site.packit.packit.global.response.success.MultipleSuccessApiResponse;
import site.packit.packit.global.response.success.SuccessApiResponse;

import java.util.List;

@RequestMapping("/api/storage")
@RestController
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * 보관함 추가/취소 버튼
     */
    @PostMapping("/{travelId}")
    public ResponseEntity<SuccessApiResponse> updateStorage(
            @PathVariable Long travelId, @RequestBody UpdateStorage updateStorage
    ){
        storageService.toggleStorage(updateStorage.memberId(), travelId);
        return ResponseEntity.ok(
                SuccessApiResponse.of(
                        "보관함 추가/취소에 성공했습니다."
                ));
    }

    /**
     * 보관함 전체 목록
     */
    @GetMapping("")
    public ResponseEntity<MultipleSuccessApiResponse<StorageListDto>> getStorageList(
            @RequestBody UpdateStorage updateStorage
    ){
        List<StorageListDto> storageList = storageService.getStorageList(updateStorage.memberId());

        return ResponseEntity.ok(
                MultipleSuccessApiResponse.of(
                        "보관함 전체 목록이 조회되었습니다.", storageList
                )
        );
    }

}
