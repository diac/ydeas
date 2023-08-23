package com.diac.ydeas.users.service;

import com.diac.ydeas.domain.model.User;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {
                UserServiceImpl.class
        },
        properties = {
                "keycloak.realmName=Test Realm"
        }
)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private Keycloak keycloak;

    @Test
    public void whenFindAll() {
        String realmName = "Test Realm";
        UUID uuid1 = UUID.randomUUID();
        String username1 = "user1";
        User user1 = User.builder()
                .uuid(uuid1)
                .username(username1)
                .build();
        List<User> users = List.of(user1);
        UserRepresentation userRepresentation1 = new UserRepresentation();
        userRepresentation1.setId(uuid1.toString());
        userRepresentation1.setUsername(username1);
        List<RoleRepresentation> roleRepresentations = List.of(
                new RoleRepresentation("USER", "RegularUser", true)
        );
        RoleScopeResource roleScopeResource = Mockito.mock(RoleScopeResource.class);
        Mockito.when(roleScopeResource.listEffective()).thenReturn(roleRepresentations);
        RoleMappingResource roleMappingResource = Mockito.mock(RoleMappingResource.class);
        Mockito.when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        UserResource userResource = Mockito.mock(UserResource.class);
        Mockito.when(userResource.roles()).thenReturn(roleMappingResource);
        List<UserRepresentation> userRepresentations = List.of(userRepresentation1);
        UsersResource usersResource = Mockito.mock(UsersResource.class);
        Mockito.when(usersResource.list()).thenReturn(userRepresentations);
        Mockito.when(usersResource.get(uuid1.toString())).thenReturn(userResource);
        RealmResource realmResource = Mockito.mock(RealmResource.class);
        Mockito.when(realmResource.users()).thenReturn(usersResource);
        Mockito.when(keycloak.realm(realmName)).thenReturn(realmResource);
        assertThat(userService.findAll()).hasSameElementsAs(users);
    }
}