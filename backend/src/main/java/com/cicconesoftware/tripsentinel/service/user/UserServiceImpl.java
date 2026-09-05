package com.cicconesoftware.tripsentinel.service.user;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminPatchUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminUpdateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.CreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.UserResponseDto;
import com.cicconesoftware.tripsentinel.dto.user.UserUpdateProfileRequestDto;
import com.cicconesoftware.tripsentinel.entity.Role;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.SessionStatus;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;
import com.cicconesoftware.tripsentinel.exception.ConflictException;
import com.cicconesoftware.tripsentinel.exception.ResourceNotFoundException;
import com.cicconesoftware.tripsentinel.mapper.user.UserMapper;
import com.cicconesoftware.tripsentinel.repository.RoleRepository;
import com.cicconesoftware.tripsentinel.repository.CheckInSessionRepository;
import com.cicconesoftware.tripsentinel.repository.UserRepository;

@Service
@Transactional
/** Implements the user application operations. */
public class UserServiceImpl implements UserService {

    private static final Set<SessionStatus> OPEN_SESSION_STATUSES = Set.of(
            SessionStatus.PLANNED,
            SessionStatus.ACTIVE,
            SessionStatus.CHECKED_IN,
            SessionStatus.MISSED,
            SessionStatus.ESCALATED);

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CheckInSessionRepository checkInSessionRepository;

    public UserServiceImpl(
            UserMapper userMapper,
            UserRepository userRepository,
            RoleRepository roleRepository,
            CheckInSessionRepository checkInSessionRepository) {
                this.userMapper = userMapper;
                this.userRepository = userRepository;
                this.roleRepository = roleRepository;
                this.checkInSessionRepository = checkInSessionRepository;
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

        // An omitted or empty role set means that existing role assignments are unchanged.
        Set<Role> roles = null;
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            roles = dto.getRoles().stream()
                    .map(roleType -> roleRepository.findByName(roleType)
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleType)))
                    .collect(Collectors.toSet());
        }

        validateAdministrativeChange(id, existingUser, dto.getStatus(), roles);
        userMapper.updateUserFromAdminPatchDto(dto, existingUser);
        if (roles != null) {
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
       Set<Role> roles = dto.getRoles().stream()
        .map(roleType -> roleRepository.findByName(roleType)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleType)))
        .collect(Collectors.toSet());

        validateAdministrativeChange(id, existingUser, dto.getStatus(), roles);
        userMapper.updateUserFromAdminDto(dto, existingUser);
        existingUser.setRoles(roles);
        User savedUser = userRepository.save(existingUser);
        return userMapper.toUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto userUpdate(Long id, UserUpdateProfileRequestDto dto) {

        

        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        if (existingUser.getStatus() == UserStatus.INACTIVE) {
            throw new ConflictException("Inactive users cannot update their profile");
        }
        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
        throw new ConflictException("User already exists with email: " + dto.getEmail());
        }
        // TODO(email-verification): Keep the current email until the requested address is verified.
        userMapper.updateUserFromUserDto(dto, existingUser);
        
        User savedUser = userRepository.save(existingUser);
        return userMapper.toUserResponseDto(savedUser);
    }

    private void validateAdministrativeChange(
            Long userId,
            User existingUser,
            UserStatus requestedStatus,
            Set<Role> requestedRoles) {
        if (requestedStatus == UserStatus.INACTIVE && existingUser.getStatus() != UserStatus.INACTIVE) {
            ensureNoOpenSessions(userId, "deactivation");
        }

        if (requestedRoles == null) {
            return;
        }

        Set<RoleType> existingRoleTypes = existingUser.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        Set<RoleType> requestedRoleTypes = requestedRoles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        if (existingRoleTypes.contains(RoleType.CUSTOMER)
                && !requestedRoleTypes.contains(RoleType.CUSTOMER)
                && checkInSessionRepository.existsByCustomerIdAndStatusIn(userId, OPEN_SESSION_STATUSES)) {
            throw new ConflictException(
                    "Customer role cannot be removed while the user has open check-in sessions");
        }
        if (existingRoleTypes.contains(RoleType.RESPONDER)
                && !requestedRoleTypes.contains(RoleType.RESPONDER)
                && checkInSessionRepository.existsByResponderIdAndStatusIn(userId, OPEN_SESSION_STATUSES)) {
            throw new ConflictException(
                    "Responder role cannot be removed while the user has open check-in sessions");
        }
    }

    private void ensureNoOpenSessions(Long userId, String operation) {
        if (checkInSessionRepository.existsByCustomerIdAndStatusIn(userId, OPEN_SESSION_STATUSES)) {
            throw new ConflictException(
                    "Customer still has open check-in sessions; resolve them before " + operation);
        }
        if (checkInSessionRepository.existsByResponderIdAndStatusIn(userId, OPEN_SESSION_STATUSES)) {
            throw new ConflictException(
                    "Responder still has open check-in sessions; reassign or resolve them before " + operation);
        }
    }

}
