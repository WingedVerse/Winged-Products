package com.cvgenie.backend.controller;

import com.cvgenie.backend.entity.*;
import com.cvgenie.backend.service.PortfolioService;
import com.cvgenie.backend.serviceImpl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@CrossOrigin
@RestController
public class PortfolioController {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private PortfolioService portfolioService;


    @PostMapping(value = "/add-portfolio/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Portfolio>> addPortfolio(
            @PathVariable long userId,
            @RequestPart("portfolio") String portfolioJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        try {
            // Convert JSON String → Portfolio object
            ObjectMapper mapper = new ObjectMapper();
            Portfolio portfolio = mapper.readValue(portfolioJson, Portfolio.class);
            ApiResponse<Portfolio> response = portfolioService.addPortfolio(userId, portfolio, imageFile);
            if ("error".equalsIgnoreCase(response.getStatus())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/get-portfolio/{userId}")
    public ResponseEntity<ApiResponse<Portfolio>> getPortfolio(@PathVariable long userId){
        logger.info("Received Get Portfolio request for: {}", userId);
        ApiResponse<Portfolio> response = portfolioService.getPortfolioByUser(userId);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PutMapping("/update-portfolio/{portfolioId}")
    public ResponseEntity<ApiResponse<Portfolio>> updatePortfolio(@PathVariable long portfolioId, @RequestBody Portfolio portfolio){
        logger.info("Received Update Portfolio request for: {}", portfolioId);
        ApiResponse<Portfolio> response = portfolioService.updatePortfolio(portfolioId, portfolio);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/update-portfolio-image/{portfolioId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Portfolio>> updatePortfolioImage(@PathVariable long portfolioId,  @RequestPart(value = "imageFile", required = false) MultipartFile imageFile){
        try {
            ObjectMapper mapper = new ObjectMapper();
            logger.info("Received update Portfolio Image request for: {}", portfolioId);
            ApiResponse<Portfolio> response = portfolioService.updatePortfolioImage(portfolioId, imageFile);
            if ("error".equalsIgnoreCase(response.getStatus())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("delete-profile-pic/{portfolioId}")
    public ResponseEntity<ApiResponse<String>> deleteImage(@PathVariable long portfolioId) {
        logger.info("Received Update Portfolio request for: {}", portfolioId);
        ApiResponse<String> response = portfolioService.deletePortfolioImage(portfolioId);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }






    //    /////////////////////////////////////// SKILLS ///////////////////////////////////////
//    @PostMapping("/add-skill/{portfolioId}")
//    public ResponseEntity<ApiResponse<Skill>> addSkill(@PathVariable long portfolioId, @RequestBody Skill skill){
//        logger.info("Received Add skill to Portfolio request for: {}", portfolioId);
//        ApiResponse<Skill> response = portfolioService.addSkill(portfolioId, skill);
//        if ("error".equalsIgnoreCase(response.getStatus())) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
    @PutMapping("/add-skills/{portfolioId}")
    public ResponseEntity<ApiResponse<List<Skill>>> addSkills(@PathVariable long portfolioId, @RequestBody List<Skill> skills){
        logger.info("Received Add skills to Portfolio request for: {}", portfolioId);
        ApiResponse<List<Skill>> response = portfolioService.addSkills(portfolioId, skills);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @DeleteMapping("/delete-skill/{portfolioId}")
    public ResponseEntity<ApiResponse<Skill>> deleteSkill(@PathVariable long portfolioId, @RequestBody Skill skill){
        logger.info("Received delete skill request for: {}", portfolioId);
        ApiResponse<Skill> response = portfolioService.deleteSkill(portfolioId, skill);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
//    @DeleteMapping("/delete-skills/{portfolioId}")
//    public ResponseEntity<ApiResponse<List<Skill>>> deleteSkills(@PathVariable long portfolioId, @RequestBody List<Skill> skills){
//        logger.info("Received delete skills request for: {}", portfolioId);
//        ApiResponse<List<Skill>> response = portfolioService.deleteSkills(portfolioId, skills);
//        if ("error".equalsIgnoreCase(response.getStatus())) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }




    //    /////////////////////////////////////// EXPERIENCE ///////////////////////////////////////
    @PutMapping("/add-experiences/{portfolioId}")
    public ResponseEntity<ApiResponse<List<Experience>>> addExperience(@PathVariable long portfolioId, @RequestBody List<Experience> experienceList){
        logger.info("Received Add Experience to Portfolio request for: {}", portfolioId);
        ApiResponse<List<Experience>> response = portfolioService.addExperience(portfolioId, experienceList);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/update-experience/{portfolioId}")
    public ResponseEntity<ApiResponse<Experience>> updateExperience(@PathVariable long portfolioId, @RequestBody Experience experience){
        logger.info("Received Update Experience request for: {}", experience);
        ApiResponse<Experience> response = portfolioService.updateExperience(portfolioId,experience);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping("/delete-experience/{portfolioId}")
    public ResponseEntity<ApiResponse<Experience>> deleteExperience(@PathVariable long portfolioId, @RequestBody Experience experience){
        logger.info("Received delete Experience request for: {}", portfolioId);
        ApiResponse<Experience> response = portfolioService.deleteExperience(portfolioId, experience);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }




    //    /////////////////////////////////////// EDUCATION ///////////////////////////////////////
    @PutMapping("/add-educations/{portfolioId}")
    public ResponseEntity<ApiResponse<List<Education>>> addEducation(@PathVariable long portfolioId, @RequestBody List<Education> educationList){
        logger.info("Received Add Education to Portfolio request for: {}", portfolioId);
        ApiResponse<List<Education>> response = portfolioService.addEducation(portfolioId, educationList);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/update-education/{portfolioId}")
    public ResponseEntity<ApiResponse<Education>> updateEducation(@PathVariable long portfolioId, @RequestBody Education education){
        logger.info("Received Update Education request for: {}", education);
        ApiResponse<Education> response = portfolioService.updateEducation(portfolioId,education);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping("/delete-education/{portfolioId}")
    public ResponseEntity<ApiResponse<Education>> deleteEducation(@PathVariable long portfolioId, @RequestBody Education education){
        logger.info("Received delete Education request for: {}", portfolioId);
        ApiResponse<Education> response = portfolioService.deleteEducation(portfolioId, education);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }




    //    /////////////////////////////////////// LANGUAGES ///////////////////////////////////////
    @PutMapping("/add-languages/{portfolioId}")
    public ResponseEntity<ApiResponse<List<Language>>> addLanguage(@PathVariable long portfolioId, @RequestBody List<Language> languageList){
        logger.info("Received Add Languages to Portfolio request for: {}", portfolioId);
        ApiResponse<List<Language>> response = portfolioService.addLanguages(portfolioId, languageList);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @DeleteMapping("/delete-language/{portfolioId}")
    public ResponseEntity<ApiResponse<Language>> deleteLanguage(@PathVariable long portfolioId, @RequestBody Language language){
        logger.info("Received delete Language request for: {}", portfolioId);
        ApiResponse<Language> response = portfolioService.deleteLanguage(portfolioId, language);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }




    //    /////////////////////////////////////// AWARDS ///////////////////////////////////////
    @PutMapping("/add-awards/{portfolioId}")
    public ResponseEntity<ApiResponse<List<Award>>> addAwards(@PathVariable long portfolioId, @RequestBody List<Award> awardList){
        logger.info("Received Add Awards to Portfolio request for: {}", portfolioId);
        ApiResponse<List<Award>> response = portfolioService.addAwards(portfolioId, awardList);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @DeleteMapping("/delete-award/{portfolioId}")
    public ResponseEntity<ApiResponse<Award>> deleteAward(@PathVariable long portfolioId, @RequestBody Award award){
        logger.info("Received delete Award request for: {}", portfolioId);
        ApiResponse<Award> response = portfolioService.deleteAward(portfolioId, award);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }




    //    /////////////////////////////////////// PROJECTS ///////////////////////////////////////
    @PutMapping("/add-projects/{portfolioId}")
    public ResponseEntity<ApiResponse<List<Project>>> addProjects(@PathVariable long portfolioId, @RequestBody List<Project> projectsList){
        logger.info("Received Add Projects to Portfolio request for: {}", portfolioId);
        ApiResponse<List<Project>> response = portfolioService.addProjects(portfolioId, projectsList);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/update-project/{portfolioId}")
    public ResponseEntity<ApiResponse<Project>> updateProject(@PathVariable long portfolioId, @RequestBody Project project){
        logger.info("Received Update Project request for: {}", project);
        ApiResponse<Project> response = portfolioService.updateProject(portfolioId,project);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping("/delete-project/{portfolioId}")
    public ResponseEntity<ApiResponse<Project>> deleteProject(@PathVariable long portfolioId, @RequestBody Project project){
        logger.info("Received delete Project request for: {}", portfolioId);
        ApiResponse<Project> response = portfolioService.deleteProject(portfolioId, project);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }




    //    /////////////////////////////////////// LINKS ///////////////////////////////////////
    @PutMapping("/add-links/{portfolioId}")
    public ResponseEntity<ApiResponse<List<Link>>> addLinks(@PathVariable long portfolioId, @RequestBody List<Link> linksList){
        logger.info("Received Add Links to Portfolio request for: {}", portfolioId);
        ApiResponse<List<Link>> response = portfolioService.addLinks(portfolioId, linksList);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @DeleteMapping("/delete-link/{portfolioId}")
    public ResponseEntity<ApiResponse<Link>> deleteLink(@PathVariable long portfolioId, @RequestBody Link link){
        logger.info("Received delete Link request for: {}", portfolioId);
        ApiResponse<Link> response = portfolioService.deleteLink(portfolioId, link);
        if ("error".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
