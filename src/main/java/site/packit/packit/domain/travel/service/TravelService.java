package site.packit.packit.domain.travel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.packit.packit.domain.member.entity.Member;
import site.packit.packit.domain.member.repository.MemberRepository;
import site.packit.packit.domain.travel.dto.CreateTravelRequest;
import site.packit.packit.domain.travel.dto.UpdateTravelRequest;
import site.packit.packit.domain.travel.entity.Travel;
import site.packit.packit.domain.travel.repository.TravelRepository;
import site.packit.packit.global.exception.ResourceNotFoundException;
import static site.packit.packit.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static site.packit.packit.domain.travel.constant.TravelStatus.INTENDED;
import static site.packit.packit.domain.travel.exception.TravelErrorCode.TRAVEL_NOT_FOUND;

@Service
public class TravelService {

    private final MemberRepository memberRepository;
    private final TravelRepository travelRepository;


    public TravelService(MemberRepository memberRepository, TravelRepository travelRepository) {
        this.memberRepository = memberRepository;
        this.travelRepository = travelRepository;
    }


    /**
     * 새로운 여행 생성
     */
    public Long createTravel(CreateTravelRequest createTravelRequest) {

        if(!memberRepository.existsById(createTravelRequest.memberId())){
            throw new ResourceNotFoundException(MEMBER_NOT_FOUND);
        }

        Member member = memberRepository.findById(createTravelRequest.memberId()).get();

        Travel createTravel = Travel.builder()
                .title(createTravelRequest.title())
                .destinationType(createTravelRequest.destinationType())
                .startDate(createTravelRequest.startDate())
                .endDate(createTravelRequest.endDate())
                .travelStatus(INTENDED)
                .member(member)
                .build();

        travelRepository.save(createTravel);
        return createTravel.getId();
    }

    /**
     * 여행 수정
     */
    @Transactional
    public void updateTravel(Long travelId, UpdateTravelRequest updateTravelRequest){

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new ResourceNotFoundException(TRAVEL_NOT_FOUND));

        travel.updateTravel(updateTravelRequest.title(), updateTravelRequest.startDate(), updateTravelRequest.endDate());
    }

    /**
     * 여행 삭제
     */
    @Transactional
    public void deleteTravel(Long travelId){

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new ResourceNotFoundException(TRAVEL_NOT_FOUND));

        // TODO : 여행 하위 항목 (리스트, 리스트 세부) 삭제 코드 추가

        travelRepository.delete(travel);
    }



}
