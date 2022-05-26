package com.server.ordering.domain.dto.response;

import com.server.ordering.domain.Waiting;
import lombok.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static lombok.AccessLevel.*;

@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
public class WaitingPreviewDto {

    private Long waitingId;
    private Integer waitingNumber;
    private Byte numOfTeamMembers;
    private String phoneNumber;
    private String waitingRegisterTime;

    public WaitingPreviewDto(Waiting waiting) {
        this.waitingId = waiting.getId();
        this.waitingNumber = waiting.getMyWaitingNumber();
        this.numOfTeamMembers = waiting.getNumOfTeamMembers();
        this.phoneNumber = waiting.getPhoneNumber().getPhoneNumber();
        this.waitingRegisterTime = waiting.getWaitingRegisterTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss"));
    }
}
