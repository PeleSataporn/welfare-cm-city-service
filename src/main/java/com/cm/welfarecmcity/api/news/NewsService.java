package com.cm.welfarecmcity.api.news;

import com.cm.welfarecmcity.api.fileresource.FileResourceRepository;
import com.cm.welfarecmcity.api.news.model.CreateNewsReq;
import com.cm.welfarecmcity.api.news.model.EditNewsReq;
import com.cm.welfarecmcity.api.news.model.GetNewsRes;
import com.cm.welfarecmcity.api.news.model.SearchNewsRes;
import com.cm.welfarecmcity.dto.NewsDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

  @Autowired
  private NewsRepository newsRepository;

  @Autowired
  private FileResourceRepository fileResourceRepository;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  @Transactional
  public ResponseModel<ResponseId> createNews(CreateNewsReq req) {
    val news = new NewsDto();
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

    val finNews = newsRepository.findAll();
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
  public GetNewsRes getNews(Long id) {
    val finNews = newsRepository.findById(id).get();

    val res = new GetNewsRes();
    res.setId(finNews.getId());
    res.setName(finNews.getName());
    res.setDescription(finNews.getDescription());
    res.setCoverImgId(finNews.getCoverImg().getId());

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
}
