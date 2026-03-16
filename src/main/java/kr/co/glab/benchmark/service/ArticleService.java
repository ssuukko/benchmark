package kr.co.glab.benchmark.service;

import java.util.List;
import kr.co.glab.benchmark.dto.ArticleSummaryDto;

public interface ArticleService {

    List<ArticleSummaryDto> getRecentArticles();
}
