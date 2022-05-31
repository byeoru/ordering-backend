package com.server.ordering.domain.dto.response;

import com.server.ordering.domain.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class BookmarkPreviewDto extends RestaurantPreviewDto {

    private Long bookmarkId;

    public BookmarkPreviewDto(Bookmark bookmark) {
        super(bookmark.getRestaurant());
        this.bookmarkId = bookmark.getId();
    }
}
