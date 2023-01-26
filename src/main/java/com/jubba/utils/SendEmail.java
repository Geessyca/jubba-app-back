package com.jubba.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendEmail {
    public boolean message(String destinatario, String assunto, String mensagem) {
        final Logger LOGGER = Logger.getAnonymousLogger();
        List<String> emails= new ArrayList<String>();
        emails.add("jubbaprogweb20222v1@outlook.com");
        emails.add("jubbaprogweb20222v2@outlook.com");
        emails.add("jubbaprogweb20222v3@outlook.com");
        emails.add("jubbaprogweb20222v4@outlook.com");
        Random gerador = new Random();
        String email = emails.get(gerador.nextInt(4));
        final String SERVIDOR_SMTP = "SMTP.office365.com";
        final int PORTA_SERVIDOR_SMTP = 587;
        final String CONTA_PADRAO = email;
        final String SENHA_CONTA_PADRAO = "****";

        final String from = "Jubba <"+email+">";
        final String to = destinatario;

        final String subject = assunto;
        final String messageContent = mensagem;
        final Properties config = new Properties();
          config.put("mail.smtp.auth", "true");
          config.put("mail.smtp.starttls.enable", "true");
          config.put("mail.smtp.host", SERVIDOR_SMTP);
          config.put("mail.smtp.port", PORTA_SERVIDOR_SMTP);
          config.put("mail.smtp.ssl.protocols", "TLSv1.2");
        final Session session = Session.getInstance(config, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CONTA_PADRAO, SENHA_CONTA_PADRAO);
            }

        });

      try {
            final Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
            message.setContent(messageContent, "text/html; charset=utf-8");
            message.setSentDate(new Date());
            Transport.send(message);
            return true;
        } catch (final MessagingException ex) {
            LOGGER.log(Level.WARNING, "Erro ao enviar mensagem: " + ex.getMessage(), ex);
            return false;
        }
    }
}
