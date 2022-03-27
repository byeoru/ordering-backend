package com.server.ordering.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static lombok.AccessLevel.PUBLIC;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
public class QrcodeResultDto {

    private String waitingUrl;
    private String togoUrl;
    private List<String> tableUrls;
}
