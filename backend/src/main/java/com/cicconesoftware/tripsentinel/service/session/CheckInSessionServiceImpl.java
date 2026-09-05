package com.cicconesoftware.tripsentinel.service.session;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cicconesoftware.tripsentinel.dto.session.AdminCreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.CheckInSessionResponseDto;
import com.cicconesoftware.tripsentinel.dto.session.CreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.UpdateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.entity.CheckInMethod;
import com.cicconesoftware.tripsentinel.entity.CheckInSession;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.SessionStatus;
import com.cicconesoftware.tripsentinel.exception.BadRequestException;
import com.cicconesoftware.tripsentinel.exception.ResourceNotFoundException;
import com.cicconesoftware.tripsentinel.mapper.session.CheckInSessionMapper;
import com.cicconesoftware.tripsentinel.repository.CheckInMethodRepository;
import com.cicconesoftware.tripsentinel.repository.CheckInSessionRepository;
import com.cicconesoftware.tripsentinel.repository.UserRepository;

@Service
@Transactional
/** Implements the check in session application operations. */
public class CheckInSessionServiceImpl implements CheckInSessionService {

    private final CheckInSessionRepository repository;
    private final CheckInSessionMapper mapper;
    private final UserRepository userRepository;
    private final CheckInMethodRepository checkInMethodRepository;
    

    public CheckInSessionServiceImpl(CheckInSessionRepository repository, CheckInSessionMapper mapper, UserRepository userRepository, CheckInMethodRepository checkInMethodRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.checkInMethodRepository = checkInMethodRepository;
    }

    @Override
    public CheckInSessionResponseDto getById(Long id) {
        CheckInSession session = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in session not found with id: " + id));
        
        return mapper.toCheckInSessionResponseDto(session);
    }

    @Override
    public List<CheckInSessionResponseDto> getByUserId(Long customerId) {
        List<CheckInSession> sessions = repository.findByCustomerId(customerId);
        return sessions.stream()
                .map(mapper::toCheckInSessionResponseDto)
                .toList();
    
    }

    @Override
    public List<CheckInSessionResponseDto> getByResponderId(Long responderId) {
        List<CheckInSession> sessions = repository.findByResponderId(responderId);
        return sessions.stream()
                .map(mapper::toCheckInSessionResponseDto)
                .toList();
    }

    @Override
    public List<CheckInSessionResponseDto> getAll() {
        List<CheckInSession> sessions = repository.findAll();
        return sessions.stream()
                .map(mapper::toCheckInSessionResponseDto)
                .toList();
    }

    @Override
    public CheckInSessionResponseDto adminCreateCheckInSession(AdminCreateCheckInSessionRequestDto dto) {
       User customer = userRepository.findById(dto.getCustomerId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + dto.getCustomerId()));

        User responder = userRepository.findById(dto.getResponderId())
            .orElseThrow(() -> new ResourceNotFoundException("Responder not found with id: " + dto.getResponderId()));

        validateParticipants(customer, responder);
        validateNewSessionTimes(dto.getStartAt(), dto.getExpectedReturnAt(), dto.getLatestCheckInAt());

        List<CheckInMethod> checkInMethods =
        checkInMethodRepository.findAllById(dto.getCheckInMethodIds());
        if (checkInMethods.size() != dto.getCheckInMethodIds().size()) {
        throw new BadRequestException("Invalid check-in method");
        }


      CheckInSession session = mapper.toCheckInSessionEntity(dto);

        session.setCustomer(customer);
        session.setResponder(responder);
        session.setCheckInMethods(new HashSet<>(checkInMethods));
        session.setStatus(SessionStatus.PLANNED);

        CheckInSession savedSession = repository.save(session);

        return mapper.toCheckInSessionResponseDto(savedSession);
    }

    @Override
    public CheckInSessionResponseDto createCheckInSessionForUser(CreateCheckInSessionRequestDto dto, Long userId) {
        User customer = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + userId));

        User responder = userRepository.findById(dto.getResponderId())
            .orElseThrow(() -> new ResourceNotFoundException("Responder not found with id: " + dto.getResponderId()));

        validateParticipants(customer, responder);
        validateNewSessionTimes(dto.getStartAt(), dto.getExpectedReturnAt(), dto.getLatestCheckInAt());

        List<CheckInMethod> checkInMethods =
        checkInMethodRepository.findAllById(dto.getCheckInMethodIds());
        if (checkInMethods.size() != dto.getCheckInMethodIds().size()) {
        throw new BadRequestException("Invalid check-in method");
        }


      CheckInSession session = mapper.toCheckInSessionEntity(dto);

        session.setCustomer(customer);
        session.setResponder(responder);
        session.setCheckInMethods(new HashSet<>(checkInMethods));
        session.setStatus(SessionStatus.PLANNED);

        CheckInSession savedSession = repository.save(session);

        return mapper.toCheckInSessionResponseDto(savedSession);
    }

    @Override
    public CheckInSessionResponseDto updateCheckInSession(UpdateCheckInSessionRequestDto dto, Long sessionId) {
        CheckInSession session = repository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in session not found with id: " + sessionId));

        if (dto.getResponderId() != null) {
            User responder = userRepository.findById(dto.getResponderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Responder not found with id: " + dto.getResponderId()));
            validateParticipants(session.getCustomer(), responder);
            session.setResponder(responder);
        }

        if (dto.getCheckInMethodIds() != null) {
            List<CheckInMethod> checkInMethods = checkInMethodRepository.findAllById(dto.getCheckInMethodIds());
            if (checkInMethods.size() != dto.getCheckInMethodIds().size()) {
                throw new BadRequestException("Invalid check-in method");
            }
            session.setCheckInMethods(new HashSet<>(checkInMethods));
        }

        mapper.updateCheckInSession(dto, session);
        validateUpdatedSessionTimes(dto, session);
        session = repository.save(session);
        return mapper.toCheckInSessionResponseDto(session);
    }

    @Override
    public CheckInSessionResponseDto cancelCheckInSession(Long sessionId) {
        CheckInSession session = repository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in session not found with id: " + sessionId));
        session.setStatus(SessionStatus.CANCELLED);
        session = repository.save(session);
        return mapper.toCheckInSessionResponseDto(session);
    }

    private void validateParticipants(User customer, User responder) {
        if (customer == responder || (customer.getId() != null && customer.getId().equals(responder.getId()))) {
            throw new BadRequestException("Customer and responder must be different users");
        }
        if (!hasRole(customer, RoleType.CUSTOMER)) {
            throw new BadRequestException("Customer must have the CUSTOMER role");
        }
        if (!hasRole(responder, RoleType.RESPONDER)) {
            throw new BadRequestException("Responder must have the RESPONDER role");
        }
    }

    private boolean hasRole(User user, RoleType roleType) {
        return user.getRoles().stream().anyMatch(role -> role.getName() == roleType);
    }

    private void validateNewSessionTimes(
            LocalDateTime startAt,
            LocalDateTime expectedReturnAt,
            LocalDateTime latestCheckInAt) {
        if (!startAt.isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Session start time must be in the future");
        }
        validateTimeOrder(startAt, expectedReturnAt, latestCheckInAt);
    }

    private void validateUpdatedSessionTimes(UpdateCheckInSessionRequestDto dto, CheckInSession session) {
        if (dto.getStartAt() != null && !session.getStartAt().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Session start time must be in the future");
        }
        validateTimeOrder(session.getStartAt(), session.getExpectedReturnAt(), session.getLatestCheckInAt());
    }

    /** The latest check-in is the escalation deadline after the expected return time. */
    private void validateTimeOrder(
            LocalDateTime startAt,
            LocalDateTime expectedReturnAt,
            LocalDateTime latestCheckInAt) {
        if (!startAt.isBefore(expectedReturnAt)) {
            throw new BadRequestException("Expected return time must be after the session start time");
        }
        if (latestCheckInAt.isBefore(expectedReturnAt)) {
            throw new BadRequestException("Latest check-in time cannot be before the expected return time");
        }
    }

}
    
