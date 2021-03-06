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

package es.tid.fiware.perseo.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 *
 * @author brox
 */
public class Help {

    public static final int PORT = 8129;

    public static final String[] ExampleRules = new String[]{
        "select id, price? as Price from iotEvent.win:length(100) group by id",
        "@Audit select *,\"blood_1_action\" as iotcepaction,"
        + "ev.BloodPressure? as Pression, ev.id? as Meter from pattern "
        + "[every ev=iotEvent(cast(cast(BloodPressure?,String),float)>1.5"
        + " and type=\"BloodMeter\")]"};
    public static final String[] ExampleNotices = new String[]{
        "{\n"
        + "\"BloodPressure\": 2,\n"
        + "\"id\":\"guay!\",\n"
        + "\"otro\":\"mas\",\n"
        + "\"numero\":4,\n"
        + "\"sub\": {\n"
        + "	\"subnumero\":18,\n"
        + "	\"subcadena\":\"SUB2\",\n"
        + "	\"subflotante\": 12.3,\n"
        + "	\"sub2\": { \"valor\": 3}\n"
        + "	}\n"
        + "}"};

    public static class Res {

        public int code;
        public String text;

        private Res(int responseCode, String text) {
            this.code = responseCode;
            this.text = text;
        }
    }

    public static Res doGet(String url) throws Exception {
        return doMethod(url, "GET");
    }

    public static Res doDelete(String url) throws Exception {
        return doMethod(url, "DELETE");
    }

    public static Res doMethod(String url, String method) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(method);
        int responseCode = con.getResponseCode();
        String body = getBodyResponse(con);
        return new Res(responseCode, body);
    }

    public static Res sendPost(String url, String body) throws Exception {
        return sendMethod(url, body, "POST");
    }

    public static Res sendPut(String url, String body) throws Exception {
        return sendMethod(url, body, "PUT");
    }

    public static Res sendMethod(String url, String body, String method) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(method);
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(body);
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        String responseBody = getBodyResponse(con);
        return new Res(responseCode, responseBody);
    }

    private static String getBodyResponse(HttpURLConnection con) throws IOException {
        int responseCode = con.getResponseCode();
        InputStream stream;
        if (responseCode / 100 == 2) {
            stream = con.getInputStream();
        } else {
            stream = con.getErrorStream();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public static Server getServer(Class klzz) {
        Server server = new Server(PORT);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(klzz, "/*");
        return server;
    }
}
