package com.cm.welfarecmcity.api.news;

import com.cm.welfarecmcity.api.fileresource.FileResourceRepository;
import com.cm.welfarecmcity.api.fileresource.FileResourceService;
import com.cm.welfarecmcity.api.news.model.*;
import com.cm.welfarecmcity.api.newsfiledetail.NewsFileDetailRepository;
import com.cm.welfarecmcity.dto.NewsDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import jakarta.transaction.Transactional;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

  @Autowired
  private NewsRepository newsRepository;

  @Autowired
  private FileResourceRepository fileResourceRepository;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  @Autowired
  private NewsLogicRepository newsLogicRepository;

  @Autowired
  private NewsFileDetailRepository newsFileDetailRepository;

  @Autowired
  private FileResourceService fileResourceService;

  @Transactional
  public ResponseModel<ResponseId> createNews() {
    val news = new NewsDto();
    news.setName(null);
    news.setDescription(null);

    return responseDataUtils.updateDataSuccess(newsRepository.save(news).getId());
  }

  @Transactional
  public ResponseModel<ResponseId> createNewsUp(CreateNewsReq req) {
    val news = newsRepository.findById(req.getNewsId()).get();
    news.setName(req.getName());
    news.setDescription(req.getDescription());

    if (req.getCoverImgId() != 0) {
      news.setCoverImg(fileResourceRepository.findById(req.getCoverImgId()).get());
    }

    return responseDataUtils.updateDataSuccess(newsRepository.save(news).getId());
  }

  @Transactional
  public List<SearchNewsRes> searchNews() {
    val listNews = new ArrayList<SearchNewsRes>();

    Sort sort = Sort.by(Sort.Direction.DESC, "createDate");

    val finNews = newsRepository.findAll(sort);
    finNews.forEach(news -> {
      val res = new SearchNewsRes();
      res.setId(news.getId());
      res.setName(news.getName());
      res.setDescription(news.getDescription());
      res.setCreateDate(news.getCreateDate());

      listNews.add(res);
    });

    return listNews;
  }

  @Transactional
  public List<SearchNewsMainRes> searchNewsMain() {
    return newsLogicRepository.searchNewsMain();
  }

  @Transactional
  public GetNewsRes getNews(Long id) {
    val finNews = newsRepository.findById(id).get();

    val res = new GetNewsRes();
    res.setId(finNews.getId());
    res.setName(finNews.getName());
    res.setDescription(finNews.getDescription());
    res.setCoverImgId(finNews.getCoverImg() != null ? finNews.getCoverImg().getId(): null);

    return res;
  }

  @Transactional
  public void editNews(EditNewsReq req) {
    val news = newsRepository.findById(req.getId()).get();
    news.setName(req.getName());
    news.setDescription(req.getDescription());

    if (req.getCoverImgId() != 0) {
      news.setCoverImg(fileResourceRepository.findById(req.getCoverImgId()).get());
    }

    newsRepository.save(news);
  }

  @Transactional
  public void deleteNews(Long id) {
    val news = newsRepository.findById(id).get();
    newsRepository.delete(news);
  }

  @Transactional
  public List<SearchImagesRes> getNewsFiles(Long id) {
    val newsFiles = newsFileDetailRepository.findByNewsId(id).get();

    return newsFiles.stream()
            .map(newsFile -> {
                try {
                    return SearchImagesRes.builder().id(newsFile.getFileResource().getId()).image(moveFileToString(newsFile.getFileResource().getImage())).build();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            })
            .collect(Collectors.toList());
  }

  @Transactional
  public String moveFileToString(Blob blob) throws SQLException {
    val imageBytes = fileResourceService.getImageBytes(blob);
    return Base64.getEncoder().encodeToString(imageBytes);
  }
}
