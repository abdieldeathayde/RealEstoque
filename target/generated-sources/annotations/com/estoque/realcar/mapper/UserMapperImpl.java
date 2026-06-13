package com.estoque.realcar.mapper;

import com.estoque.realcar.dto.UserDTO;
import com.estoque.realcar.entities.User;
import com.estoque.realcar.entities.UserRole;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-13T16:22:37-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( dto.username() );
        user.setPassword( dto.password() );
        if ( dto.role() != null ) {
            user.setRole( Enum.valueOf( UserRole.class, dto.role() ) );
        }

        return user;
    }

    @Override
    public UserDTO toDTO(User user) {
        if ( user == null ) {
            return null;
        }

        String username = null;
        String password = null;
        String role = null;

        username = user.getUsername();
        password = user.getPassword();
        if ( user.getRole() != null ) {
            role = user.getRole().name();
        }

        UserDTO userDTO = new UserDTO( username, password, role );

        return userDTO;
    }

    @Override
    public List<UserDTO> toDTOs(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( users.size() );
        for ( User user : users ) {
            list.add( toDTO( user ) );
        }

        return list;
    }

    @Override
    public List<User> toEntities(List<UserDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( dtos.size() );
        for ( UserDTO userDTO : dtos ) {
            list.add( toEntity( userDTO ) );
        }

        return list;
    }
}
