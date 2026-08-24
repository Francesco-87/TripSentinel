package com.cicconesoftware.tripsentinel.service.session;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cicconesoftware.tripsentinel.dto.session.AdminCreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.CheckInSessionResponseDto;
import com.cicconesoftware.tripsentinel.dto.session.CreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.UpdateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.entity.CheckInMethod;
import com.cicconesoftware.tripsentinel.entity.CheckInSession;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.SessionStatus;
import com.cicconesoftware.tripsentinel.mapper.session.CheckInSessionMapper;
import com.cicconesoftware.tripsentinel.repository.CheckInMethodRepository;
import com.cicconesoftware.tripsentinel.repository.CheckInSessionRepository;
import com.cicconesoftware.tripsentinel.repository.UserRepository;

@Service
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
                .orElseThrow();
        
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
            .orElseThrow();

        User responder = userRepository.findById(dto.getResponderId())
            .orElseThrow();

        List<CheckInMethod> checkInMethods =
        checkInMethodRepository.findAllById(dto.getCheckInMethodIds());
        if (checkInMethods.size() != dto.getCheckInMethodIds().size()) {
        throw new IllegalArgumentException("Invalid check-in method");
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
            .orElseThrow();

        User responder = userRepository.findById(dto.getResponderId())
            .orElseThrow();

        List<CheckInMethod> checkInMethods =
        checkInMethodRepository.findAllById(dto.getCheckInMethodIds());
        if (checkInMethods.size() != dto.getCheckInMethodIds().size()) {
        throw new IllegalArgumentException("Invalid check-in method");
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
                .orElseThrow();
        mapper.updateCheckInSession(dto, session);
        session = repository.save(session);
        return mapper.toCheckInSessionResponseDto(session);
    }

    @Override
    public CheckInSessionResponseDto cancelCheckInSession(Long sessionId) {
        CheckInSession session = repository.findById(sessionId)
                .orElseThrow();
        session.setStatus(SessionStatus.CANCELLED);
        session = repository.save(session);
        return mapper.toCheckInSessionResponseDto(session);
    }
        

}
    
