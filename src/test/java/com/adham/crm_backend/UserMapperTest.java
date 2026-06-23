package com.adham.crm_backend;

import com.adham.crm_backend.dto.UserResponse;
import com.adham.crm_backend.entity.Role;
import com.adham.crm_backend.entity.RoleName;
import com.adham.crm_backend.entity.User;
import com.adham.crm_backend.mapper.UserMapper;
import com.adham.crm_backend.mapper.UserMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;



    @SpringBootTest
    class UserMapperTest {

        private final UserMapper userMapper = new UserMapperImpl(); // الكلاس المتولّد من MapStruct

        @Test
        void whenMappingUserToResponse_thenFieldsMapCorrectly() {
            Role role = Role.builder().id(1L).name(RoleName.ROLE_ADMIN).build();
            User user = User.builder()
                    .id(1L)
                    .fullName("Adham Test")
                    .email("adham@test.com")
                    .passwordHash("some-hash")
                    .isActive(true)
                    .roles(Set.of(role))
                    .build();

            UserResponse response = userMapper.toResponse(user);

            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getFullName()).isEqualTo("Adham Test");
            assertThat(response.getRoles()).containsExactly("ROLE_ADMIN");
        }
    }


