package com.scalesec.vulnado;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.ArrayList;
import java.net.*;
import java.io.IOException;
import java.util.logging.Logger;

public class LinkLister {

  private static final Logger logger = Logger.getLogger(LinkLister.class.getName());

  private LinkLister() {
    // private constructor to hide the implicit public one
  }

  public static List<String> getLinks(String url) throws IOException {
    List<String> result = new ArrayList<>();
    Document doc = Jsoup.connect(url).get();
    Elements links = doc.select("a[href]");
    for (Element link : links) {
      result.add(link.absUrl("href"));
    }
    return result;
  }

  public static List<String> getLinksV2(String url) throws BadRequest {
    try {
      URL aUrl = new URL(url);
      String host = aUrl.getHost();
      logger.info(host);
      if (host.startsWith("172.") || host.startsWith("192.168") || host.startsWith("10.")) {
        throw new BadRequest("Use of Private IP");
      } else {
        return getLinks(url);
      }
    } catch (Exception e) {
      throw new BadRequest(e.getMessage());
    }
  }

  public static class BadRequest extends Exception {
    public BadRequest(String message) {
      super(message);
    }
  }
}