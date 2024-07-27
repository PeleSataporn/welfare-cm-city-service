package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.embeddable.NewsFileDetailKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "NewsFileDetail")
public class NewsFileDetailDto {
    @EmbeddedId private NewsFileDetailKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("news_id")
    @JoinColumn(name = "news_id")
    private NewsDto news;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("file_resource_id")
    @JoinColumn(name = "file_resource_id")
    private FileResourceDto fileResource;
}
