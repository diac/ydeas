package com.diac.ydeas.gateway.converter;

import com.diac.ydeas.gateway.config.KeycloakConvertersConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = {
        KeycloakConvertersConfig.class
})
public class KeycloakGrantedAuthoritiesConverterTest {

    @Autowired
    private KeycloakGrantedAuthoritiesConverter keycloakGrantedAuthoritiesConverter;

    @Test
    public void whenConvert() {
        String userRole = "USER";
        String roles = "roles";
        String claimRealmAccess = "realm_access";
        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(jwt.getClaimAsMap(claimRealmAccess)).thenReturn(
                Map.of(roles, List.of(userRole))
        );
        Collection<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(userRole)
        );
        assertThat(keycloakGrantedAuthoritiesConverter.convert(jwt)).hasSameElementsAs(authorities);
    }
}