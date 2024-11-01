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

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.Resources;

/**
 * Using a {@link ServletContextHandler} serve static file content from multiple locations.
 *
 * <p>
 *     You have 2 url-patterns that static content is served from.
 *     <ul>
 *         <li>{@code /*} - the root url-pattern, serving content from {@code static-root/} in classloader</li>
 *         <li>{@code /alt/*} - the url-pattern serving content from {@code webapps/alt-root/} in file system</li>
 *     </ul>
 * </p>
 */
public class ServletFileServerMultipleLocations
{
    public static void main(String[] args) throws Exception
    {
        Path altPath = Paths.get("webapps/alt-root").toRealPath();
        System.err.println("Alt Base Resource is " + altPath);

        Server server = ServletFileServerMultipleLocations.newServer(8080, altPath);
        server.start();
        server.join();
    }

    public static Server newServer(int port, Path altPath) throws FileNotFoundException
    {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        ResourceFactory resourceFactory = ResourceFactory.of(context);
        Resource baseResource = resourceFactory.newClassLoaderResource("static-root");
        if (!Resources.isReadableDirectory(baseResource))
            throw new FileNotFoundException("Unable to find base-resource for [static-root]");
        context.setBaseResource(baseResource);
        context.setWelcomeFiles(new String[]{"index.html", "index.htm", "foo.htm"});
        server.setHandler(context);

        // add special pathspec of "/alt/" content mapped to the altPath
        ServletHolder holderAlt = new ServletHolder("static-alt", DefaultServlet.class);
        holderAlt.setInitParameter("resourceBase", altPath.toUri().toASCIIString());
        holderAlt.setInitParameter("dirAllowed", "true");
        holderAlt.setInitParameter("pathInfoOnly", "true");
        context.addServlet(holderAlt, "/alt/*");

        // Lastly, the default servlet for root content (always needed, to satisfy servlet spec)
        // It is important that this is last.
        ServletHolder holderDef = new ServletHolder("default", DefaultServlet.class);
        holderDef.setInitParameter("dirAllowed", "true");
        context.addServlet(holderDef, "/");

        return server;
    }
}
