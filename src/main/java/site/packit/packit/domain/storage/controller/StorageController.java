package site.packit.packit.domain.storage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.packit.packit.domain.storage.dto.UpdateStorage;
import site.packit.packit.domain.storage.service.StorageService;
import site.packit.packit.global.response.success.SuccessApiResponse;

@RequestMapping("/api/storage")
@RestController
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/{travelId}")
    public ResponseEntity<SuccessApiResponse> updateStorage(
            @PathVariable Long travelId, @RequestBody UpdateStorage updateStorage
    ) {
        storageService.toggleStorage(updateStorage, travelId);
        return ResponseEntity.ok(
                SuccessApiResponse.of(
                        "보관함 추가/취소에 성공했습니다."
                ));
    }

}
