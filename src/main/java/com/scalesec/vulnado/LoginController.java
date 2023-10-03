package com.scalesec.vulnado;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import java.io.Serializable;

@RestController
@EnableAutoConfiguration
public class LoginController {

  @Value("${app.secret}")
  private String secret;

  @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
  LoginResponse login(@RequestBody LoginRequest input) {
    User user = User.fetch(input.getUsername());
    if (Postgres.md5(input.getPassword()).equals(user.getHashedPassword())) {
      return new LoginResponse(user.token(secret));
    } else {
      throw new Unauthorized("Access Denied");
    }
  }
}

class LoginRequest implements Serializable {
  private String username;
  private String password;

  String getUsername() {
    return username;
  }

  void setUsername(String username) {
    this.username = username;
  }

  String getPassword() {
    return password;
  }

  void setPassword(String password) {
    this.password = password;
  }
}

class LoginResponse implements Serializable {
  private final String token;

  LoginResponse(String msg) { this.token = msg; }

  String getToken() {
    return token;
  }
}

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class Unauthorized extends RuntimeException {
  Unauthorized(String exception) {
    super(exception);
  }
}