package com.server.ordering.domain.member;

import com.server.ordering.domain.PhoneNumber;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Getter
@MappedSuperclass
@RequiredArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public abstract class MemberBase {

    @NonNull
    protected String signInId;
    @NonNull
    protected String password;

    @NonNull
    @OneToOne(fetch = LAZY, cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "phone_number")
    protected PhoneNumber phoneNumber;

    public void putPassword(String password) {
        this.password = password;
    }
}
