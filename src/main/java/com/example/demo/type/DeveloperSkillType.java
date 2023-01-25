package com.example.demo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeveloperSkillType {
    BACK_END("백엔드"),
    FRONT_END("프론트엔드"),
    FULL_STACK("풀스택");

    private final String description;
}
