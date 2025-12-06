package com.cvgenie.backend.jwt;
import com.cvgenie.backend.entity.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final User user;

    public JwtAuthenticationToken(User user) {
        super(null); // Pass null for authorities, if not using them
        this.user = user;
        setAuthenticated(true); // Mark as authenticated
    }

    @Override
    public Object getCredentials() {
        return user.getPassword(); // or return null, depending on your security needs
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    public User getUser() {
        return user;
    }
}


