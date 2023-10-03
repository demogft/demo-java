package com.scalesec.vulnado;

import org.springframework.web.bind.annotation.*;
import org.springframework.boot.autoconfigure.*;

import java.util.List;
import java.io.IOException;

@RestController
@EnableAutoConfiguration
public class LinksController {

  @RequestMapping(value = "/links", method=RequestMethod.GET, produces = "application/json")
  List<String> links(@RequestParam String url) throws IOException{
    return LinkLister.getLinks(url);
  }

  @RequestMapping(value = "/links-v2", method=RequestMethod.GET, produces = "application/json")
  List<String> linksV2(@RequestParam String url) throws IOException, BadRequest {
    try {
        return LinkLister.getLinksV2(url);
    } catch (BadRequest e) {
        throw e;
    }
  }
}