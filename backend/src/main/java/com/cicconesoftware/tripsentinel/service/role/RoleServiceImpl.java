package com.cicconesoftware.tripsentinel.service.role;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cicconesoftware.tripsentinel.dto.role.RoleResponseDto;
import com.cicconesoftware.tripsentinel.mapper.role.RoleMapper;
import com.cicconesoftware.tripsentinel.repository.RoleRepository;
import com.cicconesoftware.tripsentinel.entity.Role;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;


@Service
public class RoleServiceImpl implements RoleService{

    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;

    public RoleServiceImpl (RoleMapper roleMapper, RoleRepository roleRepository){
        this.roleMapper = roleMapper;
        this.roleRepository = roleRepository;
    }



    @Override
    public RoleResponseDto getById(Long id){

        Role role = roleRepository.findById(id)
            .orElseThrow();


        return roleMapper.toRoleResponseDto(role);
    }

    @Override 
    public RoleResponseDto getByName(RoleType name){

        Role role = roleRepository.findByName(name)
              .orElseThrow();


        return roleMapper.toRoleResponseDto(role);
    }

    @Override
    public List<RoleResponseDto> getAll(){

        return roleRepository.findAll().stream()
                .map(roleMapper::toRoleResponseDto)
                .toList();
    }
    
}
