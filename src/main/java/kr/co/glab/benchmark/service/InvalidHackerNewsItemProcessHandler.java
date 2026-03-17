package kr.co.glab.benchmark.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@Slf4j
public class InvalidHackerNewsItemProcessHandler implements HackerNewsItemProcessHandler {

    @Override
    public boolean supports(HackerNewsItemProcessContext context) {
        return !context.evaluation().collectable();
    }

    @Override
    public HackerNewsItemProcessStatus handle(HackerNewsItemProcessContext context) {
        return HackerNewsItemProcessStatus.INVALID;
    }

    @Override
    public void log(HackerNewsItemProcessContext context) {
        log.debug("Skipping non-collectable HN item. storyId={}",
                context.item() == null ? null : context.item().id());
    }
}
