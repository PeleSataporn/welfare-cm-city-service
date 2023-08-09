package com.cm.welfarecmcity.api.news;

import com.cm.welfarecmcity.api.news.model.*;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/news")
public class NewsController {

  @Autowired
  private NewsService service;

  @PostMapping
  public ResponseModel<ResponseId> createNews(@RequestBody CreateNewsReq req) {
    return service.createNews(req);
  }

  @PostMapping("/search")
  public List<SearchNewsRes> searchNews() {
    return service.searchNews();
  }

  @PostMapping("/search/main")
  public List<SearchNewsMainRes> searchNewsMain() {
    return service.searchNewsMain();
  }

  @GetMapping("/{id}")
  public GetNewsRes getNews(@PathVariable Long id) {
    return service.getNews(id);
  }

  @PutMapping("/update")
  public void editNews(@RequestBody EditNewsReq req) {
    service.editNews(req);
  }

  @DeleteMapping("/{id}")
  public void deleteNews(@PathVariable Long id) {
    service.deleteNews(id);
  }
}
