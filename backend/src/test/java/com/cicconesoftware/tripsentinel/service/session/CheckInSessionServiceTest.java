package com.cicconesoftware.tripsentinel.service.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cicconesoftware.tripsentinel.dto.session.AdminCreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.CheckInSessionResponseDto;
import com.cicconesoftware.tripsentinel.dto.session.CreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.UpdateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.entity.CheckInMethod;
import com.cicconesoftware.tripsentinel.entity.CheckInSession;
import com.cicconesoftware.tripsentinel.entity.Role;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.SessionStatus;
import com.cicconesoftware.tripsentinel.exception.BadRequestException;
import com.cicconesoftware.tripsentinel.mapper.session.CheckInSessionMapper;
import com.cicconesoftware.tripsentinel.repository.CheckInMethodRepository;
import com.cicconesoftware.tripsentinel.repository.CheckInSessionRepository;
import com.cicconesoftware.tripsentinel.repository.UserRepository;

class CheckInSessionServiceTest {

    @Mock
    private CheckInSessionRepository repository;

    @Mock
    private CheckInSessionMapper mapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CheckInMethodRepository checkInMethodRepository;

    private CheckInSessionService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        service = new CheckInSessionServiceImpl(
                repository,
                mapper,
                userRepository,
                checkInMethodRepository
        );
    }

    @Test
    void shouldGetSessionById() {
        // Arrange
        CheckInSession session = new CheckInSession();
        CheckInSessionResponseDto responseDto =
                org.mockito.Mockito.mock(CheckInSessionResponseDto.class);

        when(repository.findById(1L))
                .thenReturn(Optional.of(session));

        when(mapper.toCheckInSessionResponseDto(session))
                .thenReturn(responseDto);

        // Act
        CheckInSessionResponseDto result = service.getById(1L);

        // Assert
        assertSame(responseDto, result);

        verify(repository).findById(1L);
        verify(mapper).toCheckInSessionResponseDto(session);
    }

    @Test
    void shouldGetSessionsByCustomerId() {
        // Arrange
        CheckInSession first = new CheckInSession();
        CheckInSession second = new CheckInSession();

        CheckInSessionResponseDto firstDto =
                org.mockito.Mockito.mock(CheckInSessionResponseDto.class);

        CheckInSessionResponseDto secondDto =
                org.mockito.Mockito.mock(CheckInSessionResponseDto.class);

        when(repository.findByCustomerId(1L))
                .thenReturn(List.of(first, second));

        when(mapper.toCheckInSessionResponseDto(first))
                .thenReturn(firstDto);

        when(mapper.toCheckInSessionResponseDto(second))
                .thenReturn(secondDto);

        // Act
        List<CheckInSessionResponseDto> result =
                service.getByUserId(1L);

        // Assert
        assertEquals(2, result.size());
        assertSame(firstDto, result.get(0));
        assertSame(secondDto, result.get(1));

        verify(repository).findByCustomerId(1L);
    }

    @Test
    void shouldGetSessionsByResponderId() {
        // Arrange
        CheckInSession session = new CheckInSession();

        CheckInSessionResponseDto responseDto =
                org.mockito.Mockito.mock(CheckInSessionResponseDto.class);

        when(repository.findByResponderId(2L))
                .thenReturn(List.of(session));

        when(mapper.toCheckInSessionResponseDto(session))
                .thenReturn(responseDto);

        // Act
        List<CheckInSessionResponseDto> result =
                service.getByResponderId(2L);

        // Assert
        assertEquals(1, result.size());
        assertSame(responseDto, result.get(0));

        verify(repository).findByResponderId(2L);
    }

    @Test
    void shouldGetAllSessions() {
        // Arrange
        CheckInSession first = new CheckInSession();
        CheckInSession second = new CheckInSession();

        CheckInSessionResponseDto firstDto =
                org.mockito.Mockito.mock(CheckInSessionResponseDto.class);

        CheckInSessionResponseDto secondDto =
                org.mockito.Mockito.mock(CheckInSessionResponseDto.class);

        when(repository.findAll())
                .thenReturn(List.of(first, second));

        when(mapper.toCheckInSessionResponseDto(first))
                .thenReturn(firstDto);

        when(mapper.toCheckInSessionResponseDto(second))
                .thenReturn(secondDto);

        // Act
        List<CheckInSessionResponseDto> result =
                service.getAll();

        // Assert
        assertEquals(2, result.size());

        verify(repository).findAll();
    }

    @Test
    void shouldAdminCreateSession() {
        // Arrange
        AdminCreateCheckInSessionRequestDto dto =
                new AdminCreateCheckInSessionRequestDto();

        dto.setCustomerId(1L);
        dto.setResponderId(2L);
        dto.setCheckInMethodIds(Set.of(10L));
        dto.setStartAt(LocalDateTime.now().plusDays(1));
        dto.setExpectedReturnAt(LocalDateTime.now().plusDays(1).plusHours(4));
        dto.setLatestCheckInAt(LocalDateTime.now().plusDays(1).plusHours(5));

        User customer = new User();
        User responder = new User();
        customer.setRoles(Set.of(role(RoleType.CUSTOMER)));
        responder.setRoles(Set.of(role(RoleType.RESPONDER)));

        CheckInMethod method = new CheckInMethod();

        CheckInSession session = new CheckInSession();

        CheckInSessionResponseDto responseDto =
                org.mockito.Mockito.mock(CheckInSessionResponseDto.class);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(responder));

        when(checkInMethodRepository.findAllById(Set.of(10L)))
                .thenReturn(List.of(method));

        when(mapper.toCheckInSessionEntity(dto))
                .thenReturn(session);

        when(repository.save(session))
                .thenReturn(session);

        when(mapper.toCheckInSessionResponseDto(session))
                .thenReturn(responseDto);

        // Act
        CheckInSessionResponseDto result =
                service.adminCreateCheckInSession(dto);

        // Assert
        assertSame(responseDto, result);
        assertSame(customer, session.getCustomer());
        assertSame(responder, session.getResponder());

        assertEquals(
                Set.of(method),
                session.getCheckInMethods()
        );

        assertEquals(
                SessionStatus.PLANNED,
                session.getStatus()
        );

        verify(repository).save(session);
    }

    @Test
    void shouldCreateSessionForUser() {
        // Arrange
        CreateCheckInSessionRequestDto dto =
                new CreateCheckInSessionRequestDto();

        dto.setResponderId(2L);
        dto.setCheckInMethodIds(Set.of(10L));
        dto.setStartAt(LocalDateTime.now().plusDays(1));
        dto.setExpectedReturnAt(LocalDateTime.now().plusDays(1).plusHours(4));
        dto.setLatestCheckInAt(LocalDateTime.now().plusDays(1).plusHours(5));

        User customer = new User();
        User responder = new User();
        customer.setRoles(Set.of(role(RoleType.CUSTOMER)));
        responder.setRoles(Set.of(role(RoleType.RESPONDER)));

        CheckInMethod method = new CheckInMethod();

        CheckInSession session = new CheckInSession();

        CheckInSessionResponseDto responseDto =
                org.mockito.Mockito.mock(CheckInSessionResponseDto.class);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(responder));

        when(checkInMethodRepository.findAllById(Set.of(10L)))
                .thenReturn(List.of(method));

        when(mapper.toCheckInSessionEntity(dto))
                .thenReturn(session);

        when(repository.save(session))
                .thenReturn(session);

        when(mapper.toCheckInSessionResponseDto(session))
                .thenReturn(responseDto);

        // Act
        CheckInSessionResponseDto result =
                service.createCheckInSessionForUser(dto, 1L);

        // Assert
        assertSame(responseDto, result);
        assertSame(customer, session.getCustomer());
        assertSame(responder, session.getResponder());

        assertEquals(
                Set.of(method),
                session.getCheckInMethods()
        );

        assertEquals(
                SessionStatus.PLANNED,
                session.getStatus()
        );

        verify(repository).save(session);
    }

    @Test
    void shouldRejectSessionWhenCustomerAndResponderAreTheSameUser() {
        CreateCheckInSessionRequestDto dto = validCreateDto();
        User user = new User();
        user.setRoles(Set.of(role(RoleType.CUSTOMER), role(RoleType.RESPONDER)));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> service.createCheckInSessionForUser(dto, 1L));
    }

    @Test
    void shouldRejectSessionWhenAssignedUserIsNotAResponder() {
        CreateCheckInSessionRequestDto dto = validCreateDto();
        User customer = new User();
        User responder = new User();
        customer.setRoles(Set.of(role(RoleType.CUSTOMER)));
        responder.setRoles(Set.of(role(RoleType.CUSTOMER)));

        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(responder));

        assertThrows(BadRequestException.class, () -> service.createCheckInSessionForUser(dto, 1L));
    }

    @Test
    void shouldRejectSessionThatStartsInThePast() {
        CreateCheckInSessionRequestDto dto = validCreateDto();
        dto.setStartAt(LocalDateTime.now().minusHours(1));
        User customer = new User();
        User responder = new User();
        customer.setRoles(Set.of(role(RoleType.CUSTOMER)));
        responder.setRoles(Set.of(role(RoleType.RESPONDER)));

        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(responder));

        assertThrows(BadRequestException.class, () -> service.createCheckInSessionForUser(dto, 1L));
    }

    @Test
    void shouldUpdateSession() {
        // Arrange
        UpdateCheckInSessionRequestDto dto =
                new UpdateCheckInSessionRequestDto();

        CheckInSession existingSession =
                new CheckInSession();
        existingSession.setStartAt(LocalDateTime.now().plusDays(1));
        existingSession.setExpectedReturnAt(LocalDateTime.now().plusDays(1).plusHours(4));
        existingSession.setLatestCheckInAt(LocalDateTime.now().plusDays(1).plusHours(5));

        CheckInSessionResponseDto responseDto =
                org.mockito.Mockito.mock(CheckInSessionResponseDto.class);

        when(repository.findById(1L))
                .thenReturn(Optional.of(existingSession));

        when(repository.save(existingSession))
                .thenReturn(existingSession);

        when(mapper.toCheckInSessionResponseDto(existingSession))
                .thenReturn(responseDto);

        // Act
        CheckInSessionResponseDto result =
                service.updateCheckInSession(dto, 1L);

        // Assert
        assertSame(responseDto, result);

        verify(mapper)
                .updateCheckInSession(dto, existingSession);

        verify(repository).save(existingSession);
    }

    @Test
    void shouldUpdateResponderAndCheckInMethodsWhenSupplied() {
        UpdateCheckInSessionRequestDto dto = new UpdateCheckInSessionRequestDto();
        dto.setResponderId(3L);
        dto.setCheckInMethodIds(Set.of(10L));

        User customer = new User();
        customer.setRoles(Set.of(role(RoleType.CUSTOMER)));
        User responder = new User();
        responder.setRoles(Set.of(role(RoleType.RESPONDER)));
        CheckInMethod method = new CheckInMethod();

        CheckInSession session = new CheckInSession();
        session.setCustomer(customer);
        session.setStartAt(LocalDateTime.now().plusDays(1));
        session.setExpectedReturnAt(LocalDateTime.now().plusDays(1).plusHours(4));
        session.setLatestCheckInAt(LocalDateTime.now().plusDays(1).plusHours(5));

        CheckInSessionResponseDto responseDto =
                org.mockito.Mockito.mock(CheckInSessionResponseDto.class);

        when(repository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(3L)).thenReturn(Optional.of(responder));
        when(checkInMethodRepository.findAllById(Set.of(10L))).thenReturn(List.of(method));
        when(repository.save(session)).thenReturn(session);
        when(mapper.toCheckInSessionResponseDto(session)).thenReturn(responseDto);

        CheckInSessionResponseDto result = service.updateCheckInSession(dto, 1L);

        assertSame(responseDto, result);
        assertSame(responder, session.getResponder());
        assertEquals(Set.of(method), session.getCheckInMethods());
    }

    @Test
    void shouldCancelSession() {
        // Arrange
        CheckInSession session = new CheckInSession();
        session.setStatus(SessionStatus.PLANNED);

        CheckInSessionResponseDto responseDto =
                org.mockito.Mockito.mock(CheckInSessionResponseDto.class);

        when(repository.findById(1L))
                .thenReturn(Optional.of(session));

        when(repository.save(session))
                .thenReturn(session);

        when(mapper.toCheckInSessionResponseDto(session))
                .thenReturn(responseDto);

        // Act
        CheckInSessionResponseDto result =
                service.cancelCheckInSession(1L);

        // Assert
        assertSame(responseDto, result);

        assertEquals(
                SessionStatus.CANCELLED,
                session.getStatus()
        );

        verify(repository).save(session);
    }

    private Role role(RoleType roleType) {
        Role role = new Role();
        role.setName(roleType);
        return role;
    }

    private CreateCheckInSessionRequestDto validCreateDto() {
        CreateCheckInSessionRequestDto dto = new CreateCheckInSessionRequestDto();
        dto.setResponderId(2L);
        dto.setCheckInMethodIds(Set.of(10L));
        dto.setStartAt(LocalDateTime.now().plusDays(1));
        dto.setExpectedReturnAt(LocalDateTime.now().plusDays(1).plusHours(4));
        dto.setLatestCheckInAt(LocalDateTime.now().plusDays(1).plusHours(5));
        return dto;
    }
}
