package site.packit.packit.domain.storage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.packit.packit.domain.member.entity.Member;
import site.packit.packit.domain.member.repository.MemberRepository;
import site.packit.packit.domain.storage.dto.UpdateStorage;
import site.packit.packit.domain.storage.entity.Storage;
import site.packit.packit.domain.storage.repository.StorageRepository;
import site.packit.packit.domain.travel.entity.Travel;
import site.packit.packit.domain.travel.repository.TravelRepository;
import site.packit.packit.global.exception.ResourceNotFoundException;

import static site.packit.packit.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static site.packit.packit.domain.travel.exception.TravelErrorCode.TRAVEL_NOT_FOUND;

@Service
public class StorageService {

    private final StorageRepository storageRepository;
    private final TravelRepository travelRepository;
    private final MemberRepository memberRepository;

    public StorageService(
            StorageRepository storageRepository,
            TravelRepository travelRepository,
            MemberRepository memberRepository
    ) {
        this.storageRepository = storageRepository;
        this.travelRepository = travelRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void toggleStorage(UpdateStorage updateStorage, Long travelId) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new ResourceNotFoundException(TRAVEL_NOT_FOUND));

        Member member = memberRepository.findById(updateStorage.memberId())
                .orElseThrow(()-> new ResourceNotFoundException(MEMBER_NOT_FOUND));

        Storage storage = storageRepository.findByMemberAndTravel(member, travel);

        if (storage != null) {
            // 이미 보관함에 있는 경우 삭제
            storageRepository.delete(storage);
        } else {
            // 보관함에 없는 경우 생성
            Storage newStorage = Storage.builder()
                    .member(member)
                    .travel(travel)
                    .build();
            storageRepository.save(newStorage);
        }
    }
}
