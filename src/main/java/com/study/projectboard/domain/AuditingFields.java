package com.study.projectboard.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)//이 entity 에서도 auditing을
// 사용해준다는 표시 이것을 해주어야
//modifiedBy 나 그런것들이 자동으로 입력된다.
@MappedSuperclass// entity 상속해줄 클래스에 사용하는 어노테이션
public class AuditingFields {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)//date time 포맷을 사용한다는 설정
    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;//생성일시

    @CreatedBy
    @Column(nullable = false,length = 100,updatable = false)
    private String createdBy;//생성자

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;//수정일시

    @LastModifiedBy
    @Column(nullable = false,length = 100)
    private String modifiedBy;//수정자
}
