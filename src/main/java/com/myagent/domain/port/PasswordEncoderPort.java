package com.myagent.domain.port;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
    String generateToken();
}
