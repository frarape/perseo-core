/**
* Copyright 2015 Telefonica Investigación y Desarrollo, S.A.U
*
* This file is part of perseo-core project.
*
* perseo-core is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
* General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
* option) any later version.
*
* perseo-core is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
* implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
* for more details.
*
* You should have received a copy of the GNU Affero General Public License along with perseo-core. If not, see
* http://www.gnu.org/licenses/.
*
* For those usages not covered by the GNU Affero General Public License please contact with
* iot_support at tid dot es
*/

package es.tid.fiware.perseo;

import es.tid.fiware.perseo.test.Help;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author brox
 */
public class EventsServletTest {

    public EventsServletTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of doPost method, of class EventsServlet.
     */
    @Test
    public void testDoPost() throws Exception {
        System.out.println("doPost");
        Server server = Help.getServer(EventsServlet.class);
        server.start();
        try {
            String url = String.format("http://127.0.0.1:%d", Help.PORT);
            Help.Res r = Help.sendPost(url, Help.ExampleNotices[0]);
            assertEquals(200, r.code);
            r = Help.sendPost(url, "<<this is invalid JSON>>");
            assertEquals(400, r.code);
        } finally {
            server.stop();
        }
    }

}
