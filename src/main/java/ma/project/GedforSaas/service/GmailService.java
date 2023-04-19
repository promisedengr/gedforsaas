package ma.project.GedforSaas.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.*;

@Service
public class GmailService {
    private static final String APPLICATION_NAME = "gmailService";
    private final Configuration configuration;

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = new ArrayList<>(
            Arrays.asList(GmailScopes.GMAIL_READONLY
            ));

    private static final String USER_IDENTIFIER_KEY = "218177790525-ip7s8qgf4i3dm9e5u4neu2ttmvl6ndk5.apps.googleusercontent.com";
    private GoogleAuthorizationCodeFlow flow;



    @Value("${google.secret.key.path}")
    private org.springframework.core.io.Resource gdSecretKeys;
    @Value("${google.credentials.folder.path}")
    private  Resource credentialsFolder;

    public GmailService(Configuration configuration) {
        this.configuration = configuration;
    }


    public List<Message> getMessages() throws IOException {
        System.out.println(this.credentialsFolder.getFile().getName());
        GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(gdSecretKeys.getInputStream()));
        this.flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, secrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(credentialsFolder.getFile()))
                .setAccessType("offline")
                .build();
        System.out.println(this.flow.getAccessType());
        System.out.println(this.flow.getClientId());
        Credential cred = this.flow.loadCredential(USER_IDENTIFIER_KEY);
        System.out.println(cred.getAccessToken());
        System.out.println(cred.getRefreshToken());
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, cred)
                .setApplicationName(APPLICATION_NAME)
                .build();
        Gmail.Users.GetProfile user =  service.users().getProfile(USER_IDENTIFIER_KEY);
        System.out.println("user ip: " + user.getUserId());
        ListMessagesResponse MsgResponse = service.users().messages().list("me").execute();
        List<Message> messages = new ArrayList<>();
        System.out.println("MESSAGE LENGTH:" + MsgResponse.getMessages());
        for (Message message:MsgResponse.getMessages()
             ) {
            Message m = service.users().messages().get("me",message.getId()).execute();
            messages.add(m);
        }
        System.out.println("MY LIST LENGTH: " + messages.size());
        return messages;
    }

    private  Credential getCredentials() throws IOException {
        // Load client secrets.
        InputStream in = GmailService.class.getResourceAsStream(credentialsFolder.getFile().toString());
        if (in == null) {
            throw new FileNotFoundException("Resource not found !");
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(credentialsFolder.getFile()))
                .setAccessType("offline")
                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        Credential credential  = flow.loadCredential(USER_IDENTIFIER_KEY);
        System.out.println(credential.getAccessToken());
        return credential;
    }




    public  Message sendEmail(
                              String bodyText,
                              String messageSubject,
                                    String toEmailAddress)
            throws MessagingException, IOException {
        GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(gdSecretKeys.getInputStream()));
        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, secrets, new ArrayList<>(
                Arrays.asList(GmailScopes.GMAIL_SEND
                )))
                .setDataStoreFactory(new FileDataStoreFactory(credentialsFolder.getFile())).build();
        Credential cred = this.flow.loadCredential(USER_IDENTIFIER_KEY);
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, cred)
                .setApplicationName(APPLICATION_NAME)
                .build();

        Gmail.Users.GetProfile user =  service.users().getProfile(USER_IDENTIFIER_KEY);
        System.out.println("user id: " + user.getUserId());


        MimeMessage email = this.createEmailWithAttachment(toEmailAddress, messageSubject, bodyText);
//        Properties props = new Properties();
//        Session session = Session.getDefaultInstance(props, null);
//        MimeMessage email = new MimeMessage(session);
//
////        email.setFrom(new InternetAddress(fromEmailAddress));
//        email.addRecipient(javax.mail.Message.RecipientType.TO,
//                new InternetAddress(toEmailAddress));
//        email.setSubject(messageSubject);
//        email.setText(bodyText,"text/html");



        // Encode and wrap the MIME message into a gmail message
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.getEncoder().encodeToString(rawMessageBytes);
        Message message = new Message();
        message.setRaw(encodedEmail);

        try {
            // Create send message
            message = service.users().messages().send("me", message).execute();
            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
            return message;
        } catch (GoogleJsonResponseException e) {
            System.err.println("Unable to send message: " + e.getDetails());
            throw e;
        }
    }



    public  MimeMessage createEmailWithAttachment(String to, String subject, String bodyText) throws MessagingException, IOException {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to)); //
        email.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(bodyText, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        email.setContent(multipart,"text/html");
        return email;
    }


}
