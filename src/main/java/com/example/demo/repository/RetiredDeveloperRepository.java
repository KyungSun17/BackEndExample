package com.example.demo.repository;

import com.example.demo.entity.Developer;
import com.example.demo.entity.RetiredDeveloper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RetiredDeveloperRepository extends JpaRepository<RetiredDeveloper, Long>  {

}
