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

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class LambdaJspTest extends AbstractMainTest
{
    @Test
    public void canServeJspWithLambda() throws Exception
    {
        String expected = String.format("<dt>os.version</dt><dd>%s</dd>", System.getProperty("os.version"));

        assertThat(resourceWithUrl("http://localhost:8080/test/lambda.jsp"), containsString(expected));
    }
}
