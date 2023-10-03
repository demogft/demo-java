package com.scalesec.vulnado;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import java.util.List;
import java.io.Serializable;

@RestController
@EnableAutoConfiguration
@CrossOrigin(origins = "http://trustedwebsite.com") //Make sure that enabling CORS is safe here.
public class CommentsController {
  @Value("${app.secret}")
  private String secret;

  @GetMapping(value = "/comments", produces = "application/json")
  List<Comment> comments(@RequestHeader(value="x-auth-token") String token) {
    User.assertAuth(secret, token);
    return Comment.fetchAll(); 
  }

  @PostMapping(value = "/comments", produces = "application/json", consumes = "application/json")
  Comment createComment(@RequestHeader(value="x-auth-token") String token, @RequestBody CommentRequest input) {
    return Comment.create(input.getUsername(), input.getBody());
  }

  @DeleteMapping(value = "/comments/{id}", produces = "application/json")
  Boolean deleteComment(@RequestHeader(value="x-auth-token") String token, @PathVariable("id") String id) {
    return Comment.delete(id);
  }
}

class CommentRequest implements Serializable {
  private final String username;
  private final String body;

  public CommentRequest(String username, String body) {
    this.username = username;
    this.body = body;
  }

  public String getUsername() {
    return this.username;
  }

  public String getBody() {
    return this.body;
  }
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequest extends RuntimeException {
  public BadRequest(String exception) {
    super(exception);
  }
}

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class ServerError extends RuntimeException {
  public ServerError(String exception) {
    super(exception);
  }
}