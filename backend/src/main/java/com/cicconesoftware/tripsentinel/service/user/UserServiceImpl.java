package com.cicconesoftware.tripsentinel.service.user;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminPatchUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminUpdateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.CreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.UserResponseDto;
import com.cicconesoftware.tripsentinel.dto.user.UserUpdateProfileRequestDto;
import com.cicconesoftware.tripsentinel.entity.Role;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;
import com.cicconesoftware.tripsentinel.mapper.user.UserMapper;
import com.cicconesoftware.tripsentinel.repository.RoleRepository;
import com.cicconesoftware.tripsentinel.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, RoleRepository roleRepository) {
                this.userMapper = userMapper;
                this.userRepository = userRepository;
                this.roleRepository = roleRepository;
     }

     @Override
    public UserResponseDto getById(Long id) {
            User user = userRepository.findById(id).orElseThrow();
            return userMapper.toUserResponseDto(user);                   
     }

    @Override
    public UserResponseDto getByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return userMapper.toUserResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserResponseDto)
                .toList();
    }

    @Override
public UserResponseDto create(CreateUserRequestDto dto) {

    User user = userMapper.toUserEntity(dto);

    Role customerRole = roleRepository.findByName(RoleType.CUSTOMER)
        .orElseThrow();

    user.setRoles(Set.of(customerRole));
    user.setStatus(UserStatus.ACTIVE);

    User savedUser = userRepository.save(user);

    return userMapper.toUserResponseDto(savedUser);
}

    @Override
public UserResponseDto adminCreate(AdminCreateUserRequestDto dto) {

    User user = userMapper.toUserEntity(dto);

    Set<Role> roles = dto.getRoles().stream()
        .map(roleType -> roleRepository.findByName(roleType)
            .orElseThrow())
        .collect(Collectors.toSet());

    user.setRoles(roles);

    User savedUser = userRepository.save(user);

    return userMapper.toUserResponseDto(savedUser);
}

   @Override
    public UserResponseDto adminPatch(Long id, AdminPatchUserRequestDto dto) {
        User existingUser = userRepository.findById(id).orElseThrow();

        userMapper.updateUserFromAdminPatchDto(dto, existingUser);

        if (dto.getRoles() != null) {
            Set<Role> roles = dto.getRoles().stream()
                    .map(roleType -> roleRepository.findByName(roleType)
                            .orElseThrow())
                    .collect(Collectors.toSet());

            existingUser.setRoles(roles);
        }

        User savedUser = userRepository.save(existingUser);

        return userMapper.toUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto adminUpdate(Long id, AdminUpdateUserRequestDto dto) {
        User existingUser = userRepository.findById(id).orElseThrow();
        userMapper.updateUserFromAdminDto(dto, existingUser);
       Set<Role> roles = dto.getRoles().stream()
        .map(roleType -> roleRepository.findByName(roleType)
                .orElseThrow())
        .collect(Collectors.toSet());

existingUser.setRoles(roles);
        User savedUser = userRepository.save(existingUser);
        return userMapper.toUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto userUpdate(Long id, UserUpdateProfileRequestDto dto) {
        User existingUser = userRepository.findById(id).orElseThrow();
        userMapper.updateUserFromUserDto(dto, existingUser);
        User savedUser = userRepository.save(existingUser);
        return userMapper.toUserResponseDto(savedUser);
    }


 

}