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
import com.cicconesoftware.tripsentinel.exception.ConflictException;
import com.cicconesoftware.tripsentinel.exception.ResourceNotFoundException;
import com.cicconesoftware.tripsentinel.mapper.user.UserMapper;
import com.cicconesoftware.tripsentinel.repository.RoleRepository;
import com.cicconesoftware.tripsentinel.repository.UserRepository;

@Service
/** Implements the user application operations. */
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
            User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
            return userMapper.toUserResponseDto(user);                   
     }

    @Override
    public UserResponseDto getByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
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
    if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
    throw new ConflictException("User already exists with email: " + dto.getEmail());
    }

    Role customerRole = roleRepository.findByName(RoleType.CUSTOMER)
        .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + RoleType.CUSTOMER));

   

    user.setRoles(Set.of(customerRole));
    user.setStatus(UserStatus.ACTIVE);

    User savedUser = userRepository.save(user);

    return userMapper.toUserResponseDto(savedUser);
}

    @Override
public UserResponseDto adminCreate(AdminCreateUserRequestDto dto) {

    User user = userMapper.toUserEntity(dto);

    if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
    throw new ConflictException("User already exists with email: " + dto.getEmail());
}

    Set<Role> roles = dto.getRoles().stream()
        .map(roleType -> roleRepository.findByName(roleType)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleType)))
        .collect(Collectors.toSet());

    user.setRoles(roles);

    User savedUser = userRepository.save(user);

    return userMapper.toUserResponseDto(savedUser);
}

   @Override
    public UserResponseDto adminPatch(Long id, AdminPatchUserRequestDto dto) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (dto.getEmail() != null &&
        userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
        throw new ConflictException(
            "User already exists with email: " + dto.getEmail()
        );
        }

        userMapper.updateUserFromAdminPatchDto(dto, existingUser);

        // An omitted or empty role set means that existing role assignments are unchanged.
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            Set<Role> roles = dto.getRoles().stream()
                    .map(roleType -> roleRepository.findByName(roleType)
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleType)))
                    .collect(Collectors.toSet());

            existingUser.setRoles(roles);
        }

        User savedUser = userRepository.save(existingUser);

        return userMapper.toUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto adminUpdate(Long id, AdminUpdateUserRequestDto dto) {
        
        
        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
         if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
        throw new ConflictException("User already exists with email: " + dto.getEmail());
        }
        userMapper.updateUserFromAdminDto(dto, existingUser);
       
       Set<Role> roles = dto.getRoles().stream()
        .map(roleType -> roleRepository.findByName(roleType)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleType)))
        .collect(Collectors.toSet());

existingUser.setRoles(roles);
        User savedUser = userRepository.save(existingUser);
        return userMapper.toUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto userUpdate(Long id, UserUpdateProfileRequestDto dto) {

        

        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
        throw new ConflictException("User already exists with email: " + dto.getEmail());
        }
        // TODO(email-verification): Keep the current email until the requested address is verified.
        userMapper.updateUserFromUserDto(dto, existingUser);
        
        User savedUser = userRepository.save(existingUser);
        return userMapper.toUserResponseDto(savedUser);
    }

}
