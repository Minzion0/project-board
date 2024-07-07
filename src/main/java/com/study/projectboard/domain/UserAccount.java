package com.study.projectboard.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Getter와 ToString 애노테이션을 사용하여 필드의 Getter 메소드와 toString 메소드를 자동 생성합니다.
@Getter
@ToString(callSuper = true)
// 테이블과 인덱스를 정의하는 애노테이션입니다.
@Table(indexes = {
    @Index(columnList = "email",unique = true),
    @Index(columnList = "createdAt"),
    @Index(columnList = "createdBy")
})
// 엔티티를 정의하는 애노테이션입니다.
@Entity
public class UserAccount extends AuditingFields {

    // ID 필드이며 길이를 50으로 제한합니다.
    @Id
    @Column(length = 50)
    private String userId;

    // Setter 애노테이션을 사용하여 필드의 Setter 메소드를 자동 생성합니다.
    @Setter
    @Column(nullable = false)
    private String userPassword;

    @Setter
    @Column(length = 100)
    private String email;

    @Setter
    @Column(length = 100)
    private String nickname;

    @Setter
    private String memo;

    // 기본 생성자입니다.
    public UserAccount() {}

    // 모든 필드를 초기화하는 생성자입니다.
    private UserAccount(String userId, String userPassword, String email, String nickname, String memo, String createdBy) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
    }

    // 정적 팩토리 메소드로 UserAccount 객체를 생성합니다.
    public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo) {
        return new UserAccount(userId, userPassword, email, nickname, memo, null);
    }

    // createdBy 필드를 포함하여 UserAccount 객체를 생성하는 정적 팩토리 메소드입니다.
    public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo, String createdBy) {
        return new UserAccount(userId, userPassword, email, nickname, memo, createdBy);
    }

    // equals 메소드입니다. userId가 동일한 경우 동일 객체로 간주합니다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount userAccount)) return false;
        return this.getUserId() != null && this.getUserId().equals(userAccount.userId);
    }

    // hashCode 메소드입니다. userId의 해시코드를 반환합니다.
    @Override
    public int hashCode() {
        return Objects.hash(this.getUserId());
    }
}