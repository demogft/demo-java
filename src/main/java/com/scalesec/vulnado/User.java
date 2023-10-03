package com.scalesec.vulnado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class User {
  private static final Logger LOGGER = Logger.getLogger(User.class.getName());
  private final String id;
  private final String username;
  private final String hashedPassword;

  public User(String id, String username, String hashedPassword) {
    this.id = id;
    this.username = username;
    this.hashedPassword = hashedPassword;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getHashedPassword() {
    return hashedPassword;
  }

  public String token(String secret) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.builder().setSubject(this.username).signWith(key).compact();
  }

  public static void assertAuth(String secret, String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
      Jwts.parser()
        .setSigningKey(key)
        .parseClaimsJws(token);
    } catch(Exception e) {
      LOGGER.severe(e.getMessage());
      throw new Unauthorized(e.getMessage());
    }
  }

  public static User fetch(String un) {
    User user = null;
    try (Connection cxn = Postgres.connection()) {
      LOGGER.info("Opened database successfully");
      String query = "select * from users where username = ? limit 1";
      try (PreparedStatement stmt = cxn.prepareStatement(query)) {
        stmt.setString(1, un);
        LOGGER.info(query);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
          String userId = rs.getString("user_id");
          String fetchedUsername = rs.getString("username");
          String password = rs.getString("password");
          user = new User(userId, fetchedUsername, password);
        }
      }
    } catch (Exception e) {
      LOGGER.severe(e.getClass().getName()+": "+e.getMessage());
    }
    return user;
  }
}