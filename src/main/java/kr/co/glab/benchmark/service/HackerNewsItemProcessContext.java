package kr.co.glab.benchmark.service;

import kr.co.glab.benchmark.dto.HackerNewsItemDto;

public record HackerNewsItemProcessContext(
        HackerNewsItemDto item,
        HackerNewsItemEvaluation evaluation
) {
}
