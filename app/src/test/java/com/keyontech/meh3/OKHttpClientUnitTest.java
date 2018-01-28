package com.keyontech.deletemetestscrollingview;

//import android.support.test.runner.AndroidJUnit4;

//import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by kot on 1/21/18.
 */



import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.*;



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class OKHttpClientUnitTest {


    String JSONurl = "https://meh.com";
    String JSONHeaderContentType = "Content-Type: application/json";
    String JSONHeaderAccept = "Accept: application/json";
    String JSONHeaderContentType2 = "Content-Type: application/json";
    String JSONHeaderAccept2 = "Accept: application/json";


    @Test
    public void addition_isCorrect() throws Exception {

        assertEquals(4, 2 + 2);
    }

        @Test
        public void testSomethingElse() throws Exception {
            // Create a MockWebServer. These are lean enough that you can create a new
            // instance for every unit test.
            MockWebServer server = new MockWebServer();

            // Schedule some responses.
            server.enqueue(new MockResponse().setBody("hello, world!"));
            server.enqueue(new MockResponse().setBody("sup, bra?"));
            server.enqueue(new MockResponse().setBody("yo dog"));

            // Start the server.
            server.start();

            // Ask the server for its URL. You'll need this to make HTTP requests.
            HttpUrl baseUrl = server.url( JSONurl );

            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(baseUrl)
//                    .addHeader( JSONHeaderAccept, JSONHeaderContentType)
                    .build();

            Call call = okHttpClient.newCall(request);
            call.execute();

            // Optional: confirm that your app made the HTTP requests you were expecting.
            RecordedRequest request1 = server.takeRequest();
            assertEquals("/v1/chat/messages/", request1.getPath());
            assertNotNull(request1.getHeader("Authorization"));

            // Shut down the server. Instances cannot be reused.
            server.shutdown();
        }

}