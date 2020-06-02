package oap

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients

import java.nio.charset.StandardCharsets

class DocumentUpdateListener {

    String notificationUrl
    String documentLocation
    String documentId

    static constraints = {
        notificationUrl ( nullable: false )
        documentId ( nullable: false )
    }

    def notifyListener(String documentXML) {
        try {
            HttpClient client = HttpClients.createDefault()
            System.out.println("ME_Notifying:" + notificationUrl)
            HttpPost post = new HttpPost(notificationUrl)
            post.setHeader("Location", documentLocation)
            post.setHeader("Content-Type", "text/xml")
            post.setEntity(new StringEntity(documentXML, StandardCharsets.UTF_8))
            HttpResponse response = client.execute(post)
            System.out.println("ME_response:"+response.getStatusLine())
            HttpEntity responseEntity = response.getEntity()
            System.out.print("ME_responseEntity:");
            responseEntity.writeTo(System.out)
            System.out.println();
        } catch (IOException iex) {
            iex.printStackTrace()
        }
    }
    def notifyListener() {
        try {
            HttpClient client = HttpClients.createDefault()
            System.out.println("ME_Notifying:" + notificationUrl)
            HttpPost post = new HttpPost(notificationUrl)
            post.setHeader("Location", documentLocation)
            HttpResponse response = client.execute(post)
            System.out.println("ME_response:"+response.getStatusLine())
            HttpEntity responseEntity = response.getEntity()
            System.out.print("ME_responseEntity:");
            responseEntity.writeTo(System.out)
            System.out.println();
        } catch (IOException iex) {
            iex.printStackTrace()
        }
    }
    def delayedNotifyListener() {
        new Thread(new Runnable() {
            @Override
            void run() {
                try {
                    Thread.sleep(100)
                    notifyListener()
                } catch (IOException iex) {
                    iex.printStackTrace()
                }
            }
        }).start();
    }
}
