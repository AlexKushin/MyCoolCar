package com.mycoolcar.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class StaticRequestFilter implements Filter {
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final List<String> fileExtensions =
            Arrays.asList(
                    "html",
                    "js",
                    "json",
                    "css",
                    "png",
                    "svg",
                    "eot",
                    "ttf",
                    "woff",
                    "appcache",
                    "jpg",
                    "jpeg",
                    "gif",
                    "ico"
            );

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String path = request.getServletPath();

        log.info("************************************");
        log.info("SpaWebFilter sent along its way path: " + path);
        boolean isApi = path.startsWith("/api");
        boolean isApiDoc = isApiDoc(path);
        boolean isImagesPath = path.startsWith("/getImages");

        boolean isResourceFile = !isApiDoc
                && !isApi
                && fileExtensions.stream().anyMatch(path::contains);

        if (isApi || isApiDoc || isImagesPath) {
            chain.doFilter(request, response);
        } else if (isResourceFile) {
            resourceToResponse("static" + path, response);
        } else {
            resourceToResponse("static/index.html", response);
        }
    }

    private void resourceToResponse(
            String resourcePath,
            HttpServletResponse response
    ) throws IOException {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath);

        if (inputStream == null) {
            response.sendError(404, "File not found");
            return;
        }
        setContentTypeByResourceType(resourcePath, response);

        inputStream.transferTo(response.getOutputStream());
    }

    private boolean isApiDoc(String path) {
        return pathMatcher.match("/**/swagger-ui/**", path)
                || pathMatcher.match("/swagger-ui/**", path)
                || pathMatcher.match("/**/api-docs/**", path);
    }

    private void setContentTypeByResourceType(String resourcePath, HttpServletResponse response) {
        String contentType = switch (resourcePath.substring(resourcePath.lastIndexOf("."))) {
            case ".js" -> "application/javascript";
            case ".svg" -> "image/svg+xml";
            case ".html" -> "text/html";
            case ".css" -> "text/css";
            default -> "application/octet-stream"; // Set a default content type if needed
        };

        response.setContentType(contentType);
    }

}