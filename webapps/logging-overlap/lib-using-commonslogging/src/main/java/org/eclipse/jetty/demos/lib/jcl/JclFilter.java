//
// ========================================================================
// Copyright (c) 1995 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.demos.lib.jcl;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JclFilter implements Filter
{
    private static final Log LOG = LogFactory.getLog(JclFilter.class);

    @Override
    public void init(FilterConfig filterConfig)
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        if (request instanceof HttpServletRequest)
        {
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            LOG.info("commons-logging sees the request: " + httpServletRequest.getRequestURI());
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy()
    {

    }
}
