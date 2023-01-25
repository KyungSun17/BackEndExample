package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class DevDto {
    String name;
    Integer age;
    LocalDateTime startAt;

}
