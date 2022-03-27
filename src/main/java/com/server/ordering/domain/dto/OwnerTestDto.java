package com.server.ordering.domain.dto;

import com.server.ordering.domain.member.Owner;
import lombok.*;

import static lombok.AccessLevel.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class OwnerTestDto {

    private Long ownerId;
    private String password;

    public OwnerTestDto(Owner owner) {
        this.ownerId  = owner.getId();
        this.password = owner.getPassword();
    }
}
