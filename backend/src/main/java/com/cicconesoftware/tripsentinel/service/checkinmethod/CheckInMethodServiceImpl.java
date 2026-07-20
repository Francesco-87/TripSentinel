package com.cicconesoftware.tripsentinel.service.checkinmethod;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cicconesoftware.tripsentinel.dto.checkinmethod.CheckInMethodResponseDto;
import com.cicconesoftware.tripsentinel.entity.CheckInMethod;
import com.cicconesoftware.tripsentinel.entity.enums.CheckInMethodType;
import com.cicconesoftware.tripsentinel.mapper.checkinmethod.CheckInMethodMapper;
import com.cicconesoftware.tripsentinel.repository.CheckInMethodRepository;

@Service
public class CheckInMethodServiceImpl implements CheckInMethodService {

    private final CheckInMethodMapper checkInMethodMapper;
    private final CheckInMethodRepository checkInMethodRepository;

    public CheckInMethodServiceImpl (CheckInMethodMapper mapper, CheckInMethodRepository repository){
        this.checkInMethodMapper = mapper;
        this.checkInMethodRepository = repository;
    }

   @Override
    public CheckInMethodResponseDto getById(Long id){

        CheckInMethod checkInMethod = checkInMethodRepository.findById(id)
            .orElseThrow();

            return checkInMethodMapper.toCheckInMethodResponseDto(checkInMethod);
    }

    @Override
    public CheckInMethodResponseDto getByName(CheckInMethodType name){
        CheckInMethod method = checkInMethodRepository.findByName(name)
            .orElseThrow();

            return checkInMethodMapper.toCheckInMethodResponseDto(method);
    }

    @Override
    public List<CheckInMethodResponseDto> getAll(){

        return checkInMethodRepository.findAll().stream()
                .map(checkInMethodMapper::toCheckInMethodResponseDto)
                .toList();
    }
}
