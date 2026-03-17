package kr.co.glab.benchmark.service;

public interface HackerNewsItemProcessHandler {

    boolean supports(HackerNewsItemProcessContext context);

    HackerNewsItemProcessStatus handle(HackerNewsItemProcessContext context);

    void log(HackerNewsItemProcessContext context);
}
