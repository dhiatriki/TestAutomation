package com.automation.utils;

import jakarta.mail.*;
import jakarta.mail.search.FlagTerm;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {

    public static String getVerificationCode(String username, String password) {

        String host = "imap.gmail.com";
        String code = null;

        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");

            Session session = Session.getInstance(props);
            Store store = session.getStore();
            store.connect(host, username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.search(
                    new FlagTerm(new Flags(Flags.Flag.SEEN), false)
            );

            for (int i = messages.length - 1; i >= 0; i--) {

                String content = getTextFromMessage(messages[i]);

                Matcher matcher = Pattern.compile("\\b\\d{6}\\b").matcher(content);

                if (matcher.find()) {
                    code = matcher.group(0);
                    messages[i].setFlag(Flags.Flag.SEEN, true);
                    break;
                }
            }

            inbox.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ PRINT ONLY THE CODE
        System.out.println(code);

        return code;
    }

    private static String getTextFromMessage(Part p) throws Exception {

        if (p.isMimeType("text/plain")) {
            return (String) p.getContent();
        }

        if (p.isMimeType("text/html")) {
            return (String) p.getContent();
        }

        if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();

            for (int i = 0; i < mp.getCount(); i++) {
                String text = getTextFromMessage(mp.getBodyPart(i));
                if (text != null && !text.isEmpty()) {
                    return text;
                }
            }
        }

        return "";
    }
}

