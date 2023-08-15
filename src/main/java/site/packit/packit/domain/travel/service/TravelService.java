package site.packit.packit.domain.travel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.packit.packit.domain.auth.info.CustomUserPrincipal;
import site.packit.packit.domain.checkList.entity.CheckList;
import site.packit.packit.domain.checkList.repository.CheckListRepository;
import site.packit.packit.domain.item.entity.Item;
import site.packit.packit.domain.item.repository.ItemRepository;
import site.packit.packit.domain.member.entity.Member;
import site.packit.packit.domain.member.repository.MemberRepository;
import site.packit.packit.domain.storage.repository.StorageRepository;
import site.packit.packit.domain.travel.dto.*;
import site.packit.packit.domain.travel.entity.Travel;
import site.packit.packit.domain.travel.repository.TravelRepository;
import site.packit.packit.global.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static site.packit.packit.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static site.packit.packit.domain.travel.exception.TravelErrorCode.TRAVEL_NOT_FOUND;

@Service
public class TravelService {

    private final MemberRepository memberRepository;
    private final TravelRepository travelRepository;
    private final ItemRepository itemRepository;
    private final CheckListRepository checkListRepository;

    private final StorageRepository storageRepository;

    public TravelService(MemberRepository memberRepository, TravelRepository travelRepository, ItemRepository itemRepository, CheckListRepository checkListRepository, StorageRepository storageRepository) {
        this.memberRepository = memberRepository;
        this.travelRepository = travelRepository;
        this.itemRepository = itemRepository;
        this.checkListRepository = checkListRepository;
        this.storageRepository = storageRepository;
    }


    /**
     * 새로운 여행 생성
     */
    public Long createTravel(CreateTravelRequest createTravelRequest) {

        if (!memberRepository.existsById(createTravelRequest.memberId())) {
            throw new ResourceNotFoundException(MEMBER_NOT_FOUND);
        }

        Member member = memberRepository.findById(createTravelRequest.memberId()).get();

        Travel createTravel = Travel.builder()
                .title(createTravelRequest.title())
                .destinationType(createTravelRequest.destinationType())
                .startDate(createTravelRequest.startDate())
                .endDate(createTravelRequest.endDate())
                .member(member)
                .build();

        travelRepository.save(createTravel);
        return createTravel.getId();
    }

    /**
     * 여행 수정
     */
    @Transactional
    public void updateTravel(Long travelId, UpdateTravelRequest updateTravelRequest) {

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new ResourceNotFoundException(TRAVEL_NOT_FOUND));

        travel.updateTravel(updateTravelRequest.title(), updateTravelRequest.startDate(), updateTravelRequest.endDate());
    }

    /**
     * 여행 삭제
     */
    @Transactional
    public void deleteTravel(Long travelId) {

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new ResourceNotFoundException(TRAVEL_NOT_FOUND));

        List<CheckList> checkListsToDelete = checkListRepository.findByTravelId(travelId);
        for (CheckList checkList : checkListsToDelete) {
            // 해당 체크리스트에 딸린 아이템들 삭제
            List<Item> itemsToDelete = itemRepository.findByCheckListId(checkList.getId());
            itemRepository.deleteAll(itemsToDelete);
        }
        checkListRepository.deleteAll(checkListsToDelete);

        travelRepository.delete(travel);
    }

    /**
     * 예정된 여행 조회
     */
    @Transactional(readOnly = true)
    public List<TravelListDto> getUpcomingTravel(Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        List<Travel> upcomingTravels = travelRepository.findByStartDateAfterAndMemberIdOrderByStartDateAsc(now, memberId);

        return upcomingTravels.stream()
                .map(travel -> convertToDto(travel, isAddedToStorage(travel.getId(), memberId)))
                .collect(Collectors.toList());

    }

    // 디데이 계산
    private String calculateDDay(LocalDateTime startDate) {
        LocalDate today = LocalDate.now();
        LocalDate travelDate = startDate.toLocalDate();
        long daysUntilTravel = ChronoUnit.DAYS.between(today, travelDate);
        return daysUntilTravel >= 0 ? "D-" + daysUntilTravel : "D+" + Math.abs(daysUntilTravel);
    }

    /**
     * 지난 여행 조회
     */
    @Transactional(readOnly = true)
    public List<TravelListDto> getPastTravel(Long memberId) {
        LocalDateTime now = LocalDateTime.now();

        List<Travel> pastTravels = travelRepository.findByEndDateBeforeAndMemberIdOrderByEndDateDesc(now, memberId);

        return pastTravels.stream()
                .map(travel -> convertToDto(travel, isAddedToStorage(travel.getId(), memberId)))
                .collect(Collectors.toList());
    }

    private TravelListDto convertToDto(Travel travel, boolean isAddedToStorage) {
        List<CheckList> checkLists = checkListRepository.findByTravelIdOrderByListOrderAsc(travel.getId());
        int unfinishedItemCount = 0;
        int finishedItemCount = 0;

        for (CheckList checkList : checkLists) {
            List<Item> items = itemRepository.findByCheckListId(checkList.getId());
            for (Item item : items) {
                if (item.getIsChecked()) {
                    finishedItemCount++;
                } else {
                    unfinishedItemCount++;
                }
            }
        }

        return new TravelListDto(
                travel.getId(),
                travel.getTitle(),
                calculateDDay(travel.getStartDate()),
                travel.getDestinationType(),
                travel.getStartDate(),
                travel.getEndDate(),
                isAddedToStorage,
                unfinishedItemCount,
                finishedItemCount
        );
    }

    // 보관함 유무 여부 판단
    private boolean isAddedToStorage(Long travelId, Long memberId) {
        return storageRepository.existsByMemberIdAndTravelId(memberId, travelId);
    }

    /**
     * 여행 상세 조회
     */
    public TravelDetailDto getDetailTravel(Long memberId, Long travelId) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new ResourceNotFoundException(TRAVEL_NOT_FOUND));

        List<CheckList> checkLists = checkListRepository.findByTravelIdOrderByListOrderAsc(travelId);

        List<CheckListDto> checkListDtoList = new ArrayList<>();
        for (CheckList checkList : checkLists) {
            List<ItemDto> itemDtoList = itemRepository.findByCheckListIdOrderByListOrderAsc(checkList.getId())
                    .stream()
                    .map(item -> new ItemDto(item.getId(), item.getTitle(), item.getListOrder(), item.getIsChecked()))
                    .collect(Collectors.toList());

            CheckListDto checkListDto = new CheckListDto(
                    checkList.getId(),
                    checkList.getTitle(),
                    checkList.getListOrder(),
                    itemDtoList
            );

            checkListDtoList.add(checkListDto);
        }

        Boolean isInStorage = isAddedToStorage(memberId, travelId);

        return new TravelDetailDto(
                travel.getTitle(),
                calculateDDay(travel.getStartDate()),
                travel.getDestinationType(),
                travel.getStartDate(),
                travel.getEndDate(),
                checkListDtoList,
                isInStorage
        );
    }

    /**
     * 여행 불러오기
     */
    public Long createBringTravel(Long travelId, BringTravelRequest bringTravelRequest) {
        Travel originalTravel = travelRepository.findById(travelId)
                .orElseThrow(() -> new ResourceNotFoundException(TRAVEL_NOT_FOUND));

        // 복사해서 새로운 여행 생성
        Travel newTravel = Travel.builder()
                .title(bringTravelRequest.title())
                .destinationType(originalTravel.getDestinationType())
                .startDate(bringTravelRequest.startDate())
                .endDate(bringTravelRequest.endDate())
                .member(originalTravel.getMember())
                .build();
        travelRepository.save(newTravel);

        // 체크리스트 및 아이템 복사
        List<CheckList> originalCheckLists = checkListRepository.findByTravelIdOrderByListOrderAsc(travelId);
        for (CheckList originalCheckList : originalCheckLists) {
            CheckList newCheckList = CheckList.builder()
                    .title(originalCheckList.getTitle())
                    .listOrder(originalCheckList.getListOrder())
                    .travel(newTravel)
                    .build();
            checkListRepository.save(newCheckList);

            List<Item> originalItems = itemRepository.findByCheckListIdOrderByListOrderAsc(originalCheckList.getId());
            for (Item originalItem : originalItems) {
                Item newItem = Item.builder()
                        .title(originalItem.getTitle())
                        .listOrder(originalItem.getListOrder())
                        .isChecked(false) // 체크는 안 되어있게
                        .checkList(newCheckList)
                        .build();
                itemRepository.save(newItem);
            }
        }

        return newTravel.getId();
    }

