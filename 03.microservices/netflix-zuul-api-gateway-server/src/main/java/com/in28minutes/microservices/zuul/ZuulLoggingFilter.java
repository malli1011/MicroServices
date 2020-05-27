package com.in28minutes.microservices.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ZuulLoggingFilter extends ZuulFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String filterType() {
        return "pre"; //before(pre) or after (post) or only for errors (error)
    }

    @Override
    public int filterOrder() {
        return 1; // order of the filter it multiple filters present
    }

    @Override
    public boolean shouldFilter() {
        return true; // should filter execute for every request
    }

    @Override
    public Object run() throws ZuulException {
        //Actual filter logic
        System.out.println("Inside Zuul API gateway");
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        request.getHeaderNames().asIterator().forEachRemaining(System.out::println);
        logger.info("request ->{} request uri -> {}", request, request.getRequestURI());
        return null;
    }
}
