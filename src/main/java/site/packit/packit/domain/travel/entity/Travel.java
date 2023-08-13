package site.packit.packit.domain.travel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import site.packit.packit.domain.member.entity.Member;
import site.packit.packit.domain.travel.constant.DestinationType;
import site.packit.packit.domain.travel.constant.TravelStatus;
import site.packit.packit.global.audit.BaseEntity;

import java.time.LocalDateTime;

import static site.packit.packit.domain.travel.constant.TravelStatus.INTENDED;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "travel")
@Entity
public class Travel
        extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private DestinationType destinationType;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private TravelStatus travelStatus = INTENDED;

    // TODO : 추후 여행 아이콘 기본값 설정
    @Column(length = 1000, nullable = false)
    private String iconUrl = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @Builder
    public Travel(
            String title,
            DestinationType destinationType,
            LocalDateTime startDate,
            LocalDateTime endDate,
            TravelStatus travelStatus,
            Member member
    ) {
        this.title = title;
        this.destinationType = destinationType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.travelStatus = travelStatus;
        this.member = member;
    }

    public void updateTravel(
            String title,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}