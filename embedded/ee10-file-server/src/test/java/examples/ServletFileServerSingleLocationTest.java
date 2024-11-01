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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.component.LifeCycle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServletFileServerSingleLocationTest
{
    private Server server;

    @BeforeEach
    public void startServer() throws Exception
    {
        server = ServletFileServerSingleLocation.newServer(0);
        server.start();
    }

    @AfterEach
    public void stopServer()
    {
        LifeCycle.stop(server);
    }

    /**
     * Get file
     */
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, delimiterString = "|", textBlock = """
        requestPath     | expectedContents
        /index.html     | This is the index.html
        /               | This is the index.html
        /hello.html     | Hello from src/main/resources/static-root/
        /deeper/        | This is the foo.htm
        /deeper/foo.htm | This is the foo.htm
        /deeper/alt.txt | This is the alt.txt
        """)
    public void testGet(String requestPath, String expectedContents) throws Exception
    {
        HttpURLConnection http = (HttpURLConnection)server.getURI().resolve(requestPath).toURL().openConnection();
        http.connect();
        dumpRequestResponse(http);
        assertEquals(HttpURLConnection.HTTP_OK, http.getResponseCode());
        try (InputStream in = http.getInputStream())
        {
            String responseBody = IO.toString(in, StandardCharsets.UTF_8);
            assertThat(responseBody, containsString(expectedContents));
        }
    }

    private static void dumpRequestResponse(HttpURLConnection http)
    {
        System.out.println();
        System.out.println("----");
        System.out.printf("%s %s HTTP/1.1%n", http.getRequestMethod(), http.getURL());
        System.out.println("----");
        System.out.printf("%s%n", http.getHeaderField(null));
        http.getHeaderFields().entrySet().stream()
            .filter(entry -> entry.getKey() != null)
            .forEach((entry) -> System.out.printf("%s: %s%n", entry.getKey(), http.getHeaderField(entry.getKey())));
    }
}
