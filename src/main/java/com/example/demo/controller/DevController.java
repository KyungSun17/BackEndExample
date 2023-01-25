package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.exception.DMakerException;
import com.example.demo.service.DMakerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DevController {

    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {
        log.info("Get /developers Http/1.1");

        return dMakerService.getAllEmployedDevelopers();
    }

    @GetMapping("/developers/{memberId}")
    public DeveloperDetailDto getDeveloper(
            @PathVariable String memberId
    ) {
        log.info("Get /developers Http/1.1");

        return dMakerService.getDeveloper(memberId);
    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDevelopers(
            @Valid @RequestBody CreateDeveloper.Request request
    ) {

        log.info("Get /create-developers Http/1.1");

        return dMakerService.createDeveloper(request);
    }

    @PutMapping("/update-developer")
    public DeveloperDetailDto editDevelopers(
            @PathVariable String memberId,
            @Valid @RequestBody EditDeveloper.Request request
    ) {

        log.info("Get /create-developers Http/1.1");

        return dMakerService.editDeveloper(memberId, request);
    }

    @DeleteMapping("/developers/{memberId}")
    public DeveloperDetailDto deleteDeveloper(
            @PathVariable String memberId
    ){
        return dMakerService.deleteDeveloper(memberId);
    }

    @ExceptionHandler(DMakerException.class)
    public DMakerErrorResponse handleException (
            DMakerException e,
            HttpServletRequest request){
        log.error("errorCode : {}, url: {}, message : {}",e.getDMakerErrorCode(),request.getRequestURL(),e.getDetailMessage());

        return DMakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }
}
