package com.dev.democatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.democatalog.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
