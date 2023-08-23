package com.diac.ydeas.gateway.converter;

import com.diac.ydeas.gateway.config.KeycloakConvertersConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {
        KeycloakConvertersConfig.class
})
public class ReactiveKeycloakJwtAuthenticationConverterTest {

    @Autowired
    private ReactiveKeycloakJwtAuthenticationConverter reactiveKeycloakJwtAuthenticationConverter;

    @Test
    public void whenConvert() {
        String username = "user";
        String usernameClaim = "preferred_username";
        String userRole = "USER";
        String roles = "roles";
        String claimRealmAccess = "realm_access";
        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(jwt.hasClaim(usernameClaim)).thenReturn(true);
        Mockito.when(jwt.getClaimAsString(usernameClaim)).thenReturn(username);
        Mockito.when(jwt.getClaimAsMap(claimRealmAccess)).thenReturn(
                Map.of(roles, List.of(userRole))
        );
        JwtAuthenticationToken expectedJwtAuthenticationToken = new JwtAuthenticationToken(
                jwt,
                List.of(new SimpleGrantedAuthority("USER")),
                username
        );
        assertThat(reactiveKeycloakJwtAuthenticationConverter.convert(jwt).block())
                .isEqualTo(expectedJwtAuthenticationToken);
    }
}