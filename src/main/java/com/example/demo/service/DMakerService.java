package com.example.demo.service;

import com.example.demo.code.StatusCode;
import com.example.demo.dto.CreateDeveloper;
import com.example.demo.dto.DeveloperDetailDto;
import com.example.demo.dto.DeveloperDto;
import com.example.demo.dto.EditDeveloper;
import com.example.demo.entity.Developer;
import com.example.demo.entity.RetiredDeveloper;
import com.example.demo.exception.DMakerException;
import com.example.demo.repository.DeveloperRepository;
import com.example.demo.repository.RetiredDeveloperRepository;
import com.example.demo.type.DeveloperLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.exception.DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED;
import static com.example.demo.exception.DMakerErrorCode.NO_DEVELOPER;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    //ACID(Transaction)
    //Atomic(원자성 - 모든 작업이 반영되거나 모두 롤백되는 특성입니다)
    //Consistency (일관성 - 데이터는 미리 정의된 규칙에서만 수정이 가능, 숫자컬럼에 문자열값을 저장이 안됨)
    //Isolation (유일성 - DB거래는 한번에 하나만)
    //Durability(지속성 - commit하면 commit 이력이 있어야함)
    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getDeveloperLevel().ordinal())
                .memberId(request.getMemberId())
                .name(request.getName())
                .age(request.getAge())
                .statusCode(StatusCode.EMPLOYED)
                .build();

        developerRepository.save(developer);
        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        Integer experienceYears = request.getExperienceYears();
        if(request.getDeveloperLevel() == DeveloperLevel.SENIOR
            && experienceYears < 10){
            throw new RuntimeException(String.valueOf(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED));
        }
        
        if(request.getDeveloperLevel() == DeveloperLevel.JUNIOR 
        && (experienceYears < 4 || experienceYears > 10)){
            throw new RuntimeException(String.valueOf(NO_DEVELOPER));
        }

        Optional<Developer> developer = developerRepository.findByMemberId(request.getMemberId());
        if(developer.isPresent()){
            throw new RuntimeException(String.valueOf(NO_DEVELOPER));
        }
    }

    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDetailDto getDeveloper(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
    }

    @Transactional
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        Developer developer = developerRepository.findByMemberId(memberId).orElseThrow(
                () -> new DMakerException(NO_DEVELOPER)
        );

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(developer);

    }

    @Transactional
    public DeveloperDetailDto deleteDeveloper(String memberId) {
        //1. employed -> retired
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
        developer.setStatusCode(StatusCode.RETIRED);

        //2. save into RetiredDeveloper

        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(memberId)
                .name(developer.getName())
                .build();

        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDetailDto.fromEntity(developer);
    }
}
