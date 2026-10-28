package pe.andeslink.andestrack.auth;

import java.util.List;

public record LoginResponse(String accessToken, String tokenType, long expiresInSeconds,
                            String email, String displayName, List<String> roles) {}
