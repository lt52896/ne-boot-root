/*
 * spring-mvc-logger logs requests/responses
 *
 * Copyright (c) 2013. Israel Zalmanov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ne.boot.service.logging;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

public class RequestWrapper extends ContentCachingRequestWrapper {
    private HttpHeaders headers;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public HttpHeaders getHeaders() {
        if (this.headers == null) {
            this.headers = new HttpHeaders();
            for (Enumeration<?> headerNames = getHeaderNames(); headerNames.hasMoreElements(); ) {
                String headerName = (String) headerNames.nextElement();
                for (Enumeration<?> headerValues = getHeaders(headerName);
                     headerValues.hasMoreElements(); ) {
                    String headerValue = (String) headerValues.nextElement();
                    this.headers.add(headerName, headerValue);
                }
            }
        }
        return headers;
    }

    public URI getURI() {
        try {
            StringBuffer url = getRequestURL();
            String query = getQueryString();
            if (StringUtils.hasText(query)) {
                url.append('?').append(query);
            }
            return new URI(url.toString());
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Could not get HttpServletRequest URI: " + ex.getMessage(), ex);
        }
    }
}
