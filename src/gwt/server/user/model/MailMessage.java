package gwt.server.user.model;

import java.util.Date;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class MailMessage {

    public static final String SENDER = "sender";
    public static final String RECIPIENTS = "recipients";
    public static final String SUBJECT = "subject";
    public static final String CONTENT = "content";
    public static final String SENT_DATE = "sentDate";
    
    private Entity entity;
    
    public MailMessage(String sender, String recipients, String subject,
            String content, Date sentDate) {
        entity = new Entity(MailMessage.class.getSimpleName());
        entity.setProperty(SENDER, sender);
        entity.setProperty(RECIPIENTS, recipients);
        entity.setProperty(SUBJECT, subject);
        entity.setProperty(CONTENT, content);
        entity.setProperty(SENT_DATE, sentDate);
    }
    
    public MailMessage(Entity entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return entity;
    }
    
    public Key getKey() {
        return entity.getKey();
    }
    
    public String getSender() {
        return (String)entity.getProperty(SENDER);
    }
    
    public String getRecipients() {
        return (String)entity.getProperty(RECIPIENTS);
    }
    
    public String getSubject() {
        return (String)entity.getProperty(SUBJECT);
    }
    
    public String getContent() {
        return (String)entity.getProperty(CONTENT);
    }
    
    public Date getSentDate() {
        return (Date)entity.getProperty(SENT_DATE);
    }
}
