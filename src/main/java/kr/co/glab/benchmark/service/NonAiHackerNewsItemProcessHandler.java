package kr.co.glab.benchmark.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@Slf4j
public class NonAiHackerNewsItemProcessHandler implements HackerNewsItemProcessHandler {

    @Override
    public boolean supports(HackerNewsItemProcessContext context) {
        return !context.evaluation().aiMatched();
    }

    @Override
    public HackerNewsItemProcessStatus handle(HackerNewsItemProcessContext context) {
        return HackerNewsItemProcessStatus.NON_AI;
    }

    @Override
    public void log(HackerNewsItemProcessContext context) {
        log.debug("Skipping non-AI HN item. storyId={} title={}",
                context.item().id(), context.item().title());
    }
}
