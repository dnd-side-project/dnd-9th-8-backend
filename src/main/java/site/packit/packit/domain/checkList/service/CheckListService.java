package site.packit.packit.domain.checkList.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.packit.packit.domain.checkList.dto.CreateCheckListRequest;
import site.packit.packit.domain.checkList.entity.CheckList;
import site.packit.packit.domain.checkList.repository.CheckListRepository;
import site.packit.packit.domain.travel.entity.Travel;
import site.packit.packit.domain.travel.repository.TravelRepository;
import site.packit.packit.global.exception.ResourceNotFoundException;

import static site.packit.packit.domain.travel.exception.TravelErrorCode.TRAVEL_NOT_FOUND;

@Service
public class CheckListService {

    private final CheckListRepository checkListRepository;
    private final TravelRepository travelRepository;

    public CheckListService(CheckListRepository checkListRepository, TravelRepository travelRepository) {
        this.checkListRepository = checkListRepository;
        this.travelRepository = travelRepository;
    }

    /**
     * 새로운 체크리스트 생성
     */
    @Transactional
    public Long createCheckList(Long travelId, CreateCheckListRequest createCheckListRequest){

        if(!travelRepository.existsById(travelId)){
            throw new ResourceNotFoundException(TRAVEL_NOT_FOUND);
        }

        Travel travel = travelRepository.findById(travelId).get();

        // travel에 속한 체크리스트 중 가장 큰 listOrder 값을 찾기
        Integer maxListOrder = checkListRepository.findMaxListOrderByTravel(travel);

        if (maxListOrder == null) {
            maxListOrder = 0;
        }

        CheckList newCheckList = CheckList.builder()
                .title(createCheckListRequest.title())
                .listOrder(maxListOrder+1)
                .travel(travel)
                .build();

        checkListRepository.save(newCheckList);

        return newCheckList.getId();
    }
}
