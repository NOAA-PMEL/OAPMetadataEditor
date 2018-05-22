package oap;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ServiceTest {

    public static void doGet(String[] args) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet("http://matisse:8383/oap/document/test?query=ca");
            CloseableHttpResponse response = ((CloseableHttpClient) client).execute(get);
            response.getEntity().writeTo(System.out);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void doPost(String[] args) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost("http://matisse:8383/oap/document/test");
            HttpEntity postit = new StringEntity("{query:ca}");
            post.setEntity(postit);
            CloseableHttpResponse response = ((CloseableHttpClient) client).execute(post);
            response.getEntity().writeTo(System.out);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // JWhich.which("org.apache.http.client.HttpClient");
        doPost(args);
    }
}
