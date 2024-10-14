package com.cm.welfarecmcity.dto.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsFileDetailKey implements Serializable {
  @Column(name = "news_id")
  private Long news_id;

  @Column(name = "file_resource_id")
  private Long file_resource_id;
}
