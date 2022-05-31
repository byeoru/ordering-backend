package com.server.ordering.domain.dto.response;

import com.server.ordering.domain.RepresentativeMenu;
import com.server.ordering.domain.dto.FoodDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class RepresentativeMenuDto extends FoodDto {

    private Long representativeMenuId;

    public RepresentativeMenuDto(RepresentativeMenu representativeMenu) {
        super(representativeMenu);
        this.representativeMenuId = representativeMenu.getId();
    }
}
