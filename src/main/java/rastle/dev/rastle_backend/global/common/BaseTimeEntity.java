package rastle.dev.rastle_backend.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false, name = "created_time")
    private LocalDateTime createdTime;
    @LastModifiedDate
    @Column(name = "modified_time")
    private LocalDateTime modifiedTime;
}