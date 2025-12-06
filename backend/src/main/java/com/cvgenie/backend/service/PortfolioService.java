package com.cvgenie.backend.service;

import com.cvgenie.backend.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PortfolioService {
    ApiResponse<Portfolio> addPortfolio(long userId, Portfolio portfolio, MultipartFile imageFile);
    ApiResponse<Portfolio> getPortfolioByUser(long userId);
    ApiResponse<Portfolio> getPortfolio(long id);
    ApiResponse<Portfolio> updatePortfolio(long portfolioId,  Portfolio portfolio);
    ApiResponse<Portfolio> updatePortfolioImage(long portfolioId, MultipartFile imageFile);
    ApiResponse<String> deletePortfolioImage(long portfolioId);

    //    /////////////////////////////////////// SKILLS ///////////////////////////////////////
//    ApiResponse<Skill> addSkill(long portfolioId, Skill skills);
    ApiResponse<List<Skill>> addSkills(long portfolioId, List<Skill> skills);
    ApiResponse<Skill> deleteSkill(long portfolioId, Skill skill);
//    ApiResponse<List<Skill>> deleteSkills(long portfolioId, List<Skill> skills);


    //    /////////////////////////////////////// EXPERIENCE ///////////////////////////////////////
    ApiResponse<List<Experience>> addExperience(long portfolioId, List<Experience> experienceList);
    ApiResponse<Experience> updateExperience(long portfolioId, Experience experience);
    ApiResponse<Experience> deleteExperience(long portfolioId, Experience experience);


    //    /////////////////////////////////////// EDUCATION ///////////////////////////////////////
    ApiResponse<List<Education>> addEducation(long portfolioId, List<Education> educationList);
    ApiResponse<Education> updateEducation(long portfolioId, Education education);
    ApiResponse<Education> deleteEducation(long portfolioId, Education education);


    //    /////////////////////////////////////// LANGUAGES ///////////////////////////////////////
    ApiResponse<List<Language>> addLanguages(long portfolioId, List<Language> languageList);
    ApiResponse<Language> deleteLanguage(long portfolioId, Language language);


    //    /////////////////////////////////////// AWARDS ///////////////////////////////////////
    ApiResponse<List<Award>> addAwards(long portfolioId, List<Award> awardList);
    ApiResponse<Award> deleteAward(long portfolioId, Award award);


    //    /////////////////////////////////////// PROJECTS ///////////////////////////////////////
    ApiResponse<List<Project>> addProjects(long portfolioId, List<Project> projectList);
    ApiResponse<Project> updateProject(long portfolioId, Project project);
    ApiResponse<Project> deleteProject(long portfolioId, Project project);


    //    /////////////////////////////////////// LINKS ///////////////////////////////////////
    ApiResponse<List<Link>> addLinks(long portfolioId, List<Link> linkList);
    ApiResponse<Link> deleteLink(long portfolioId, Link link);

}
