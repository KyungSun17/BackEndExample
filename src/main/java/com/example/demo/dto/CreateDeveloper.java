package com.example.demo.dto;

import com.example.demo.entity.Developer;
import com.example.demo.type.DeveloperLevel;
import com.example.demo.type.DeveloperSkillType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;



public class CreateDeveloper {
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    @Getter
    @Setter
    public static class Request {
        @NotNull
        private DeveloperLevel developerLevel;
        @NotNull
        private DeveloperSkillType developerSkillType;
        @NotNull
        @Min(0)
        @Max(20)
        private Integer experienceYears;
        @NotNull
        @Size(min =3, max=50, message = "memberId size must 3 to 50")
        private String memberId;
        @NotNull
        @Size(min =3, max=20, message = "memberId size must 3 to 20")
        private String name;
        @NotNull
        @Min(18)
        private Integer age;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private DeveloperLevel developerLevel;
        private DeveloperSkillType developerSkillType;
        private Integer experienceYears;
        private String memberId;

        public static Response fromEntity(Developer developer) {
            return Response.builder()
                    .developerLevel(developer.getDeveloperLevel())
                    .developerSkillType(developer.getDeveloperSkillType())
                    .experienceYears(developer.getExperienceYears())
                    .memberId(developer.getMemberId())
                    .build();
        }
    }
}
