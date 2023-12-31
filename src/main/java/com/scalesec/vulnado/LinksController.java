package com.scalesec.vulnado;

import org.springframework.web.bind.annotation.*;
import org.springframework.boot.autoconfigure.*;
import java.util.List;
import java.io.IOException;

@RestController
@EnableAutoConfiguration
public class LinksController {

  @GetMapping(value = "/links", produces = "application/json")
  List<String> links(@RequestParam String url) throws IOException{
    return LinkLister.getLinks(url);
  }

  @GetMapping(value = "/links-v2", produces = "application/json")
  List<String> linksV2(@RequestParam String url) throws IOException{
    return LinkLister.getLinks(url);
  }
}