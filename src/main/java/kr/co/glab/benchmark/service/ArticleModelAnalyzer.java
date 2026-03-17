package kr.co.glab.benchmark.service;

import java.util.Map;

public interface ArticleModelAnalyzer {

    Map<String, Integer> analyze(String content);
}
