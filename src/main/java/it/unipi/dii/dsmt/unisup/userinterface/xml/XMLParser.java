package it.unipi.dii.dsmt.unisup.userinterface.xml;

import com.thoughtworks.xstream.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;

import it.unipi.dii.dsmt.unisup.beans.User;
import org.w3c.dom.*;
import org.xml.sax.*;

public class XMLParser {
    public void storeUserInformation(User user) {
        XStream xStream = new XStream();

        try (
                FileOutputStream fileOutputStream = new FileOutputStream("xml/config/user.xml");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                ) {
            String xml = xStream.toXML(user);
            objectOutputStream.writeObject(xml);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User loadUserInformation() {
        XStream xs = new XStream();
        String x = null;

        try {
            x = new String(Files.readAllBytes(Paths.get("xml/config/user.xml")));
        } catch(Exception e) {
            System.out.println("Couldn't load the parameters");
        }
        return (User) xs.fromXML(x);
    }
}
