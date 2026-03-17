package kr.co.glab.benchmark.service;

import kr.co.glab.benchmark.dto.HackerNewsItemDto;

public interface HackerNewsItemEvaluator {

    HackerNewsItemEvaluation evaluate(HackerNewsItemDto item);
}
