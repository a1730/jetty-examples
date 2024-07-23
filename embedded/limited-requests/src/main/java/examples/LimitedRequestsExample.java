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

package examples;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.io.Content;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.Callback;

public class LimitedRequestsExample
{
    public static void main(String[] args) throws Exception
    {
        Server server = newServer(8080);
        server.start();
        server.join();
    }

    public static Server newServer(int port)
    {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);

        LimitedRequestsWrapper limitedRequestsWrapper = new LimitedRequestsWrapper();
        limitedRequestsWrapper.setMaxRequests(5);

        server.addConnector(connector);

        limitedRequestsWrapper.setHandler(new HelloHandler());

        server.setHandler(limitedRequestsWrapper);
        return server;
    }

    private static class HelloHandler extends Handler.Abstract
    {
        @Override
        public boolean handle(Request request, Response response, Callback callback) throws Exception
        {
            response.setStatus(200);
            response.getHeaders().put(HttpHeader.CONTENT_TYPE, "text/plain;charset=utf-8");
            Content.Sink.write(response, true, "Hello World", callback);
            return true;
        }
    }
}
