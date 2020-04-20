package oap;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

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

    public static String postDocMultipart(String docId, String docPath) throws Exception {
            File docFile = new File(docPath);
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost("http://matisse:8383/oap/document/postit/" + docId);
            FileBody body = new FileBody(docFile, ContentType.APPLICATION_XML);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("xmlFile", body);
            StringBody notificationUrl = new StringBody("http://matisse:8080/OAPUploadDashboard/DashboardUpdateService/notify/"+docId, ContentType.MULTIPART_FORM_DATA);
            builder.addPart("notificationUrl", notificationUrl);
            HttpEntity postit = builder.build();
            post.setEntity(postit);
            HttpResponse httpResponse = client.execute(post);
            HttpEntity responseEntity = httpResponse.getEntity();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            responseEntity.writeTo(baos);
            String response = new String(baos.toByteArray());
            return response;
    }
//    public static void postDocMultipartBin(String docPath) {
//        try {
//            File docFile = new File(docPath);
//            HttpClient client = HttpClients.createDefault();
//            HttpPost post = new HttpPost("http://matisse:8383/oap/document/postit");
//
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            StringEntity textBody = new StringEntity("http://matisse:8888/oap/notify");
//            builder.addBinaryBody("xmlFile", docFile, ContentType.APPLICATION_XML, docFile.getName());
//            builder.addPart("xmlFile", body);
//            HttpEntity postit = builder.build();
//            post.setEntity(postit);
//            HttpResponse response = client.execute(post);
//            HttpEntity responseEntity = response.getEntity();
//            responseEntity.writeTo(System.out);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
    public static void postDocMime(String docPath) {
        try {
            File docFile = new File(docPath);
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost("http://matisse:8383/oap/document/postit");
            FileBody body = new FileBody(docFile, ContentType.APPLICATION_XML);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("xmlFile", body);
            HttpEntity postit = builder.build();
            post.setEntity(postit);
            HttpResponse response = client.execute(post);
            HttpEntity responseEntity = response.getEntity();
            responseEntity.writeTo(System.out);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void postDocRaw(String docPath) {
        try {
            File docFile = new File(docPath);
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost("http://matisse:8383/oap/document/postit");
            String fileContents = FileUtils.readFileToString(docFile);
            HttpEntity postit = new StringEntity(fileContents);
            post.setEntity(postit);
            HttpResponse response = client.execute(post);
            HttpEntity responseEntity = response.getEntity();
            responseEntity.writeTo(System.out);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postNotify(String docLocation, String postDocId) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost("http://localhost:8080/OAPUploadDashboard/DashboardUpdateService/notify/"+postDocId);
//            String docLocation = // "http://matisse:8383/oap/getXml/"+ mdDocid;
            StringBody postBody = new StringBody(docLocation, ContentType.MULTIPART_FORM_DATA);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("location", postBody);
            HttpEntity postit = builder.build();
            post.setEntity(postit);
            post.setHeader("Location", docLocation);
            HttpResponse response = client.execute(post);
            HttpEntity responseEntity = response.getEntity();
            responseEntity.writeTo(System.out);
        } catch (IOException iex) {
            iex.printStackTrace();
        }

    }
    public static void main(String[] args) {
        try {
//         JWhich.which("org.apache.http.client.HttpClient");
//        doPost(args);
            String postDocId = "CHABA102014";
            String docPath = "/Users/kamb/tomcat/7/content/OAPUploadDashboard/MetadataDocs/CHAB/CHABA102014/CHABA102014_OADS.xml";
//        postDocRaw(docPath);
//        postDocMime(docPath);
            String docId = postDocMultipart(postDocId, docPath);
//            docId = postDocMultipart(postDocId, docPath);
//            String docId = "http://matisse:8383/oap/document/getXml/97";
            postNotify(docId, postDocId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
