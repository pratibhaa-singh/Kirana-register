package com.kiranaregister.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {
  @Column(name = "created_at")
  @JsonIgnore
  @CreationTimestamp
  private Timestamp createdAt;

  @Column(name = "updated_at")
  @JsonIgnore
  @UpdateTimestamp
  private Timestamp updatedAt;
}
