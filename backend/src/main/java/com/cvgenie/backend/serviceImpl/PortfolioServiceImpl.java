package com.cvgenie.backend.serviceImpl;

import com.cvgenie.backend.entity.*;
import com.cvgenie.backend.repository.*;
import com.cvgenie.backend.service.PortfolioService;
import com.cvgenie.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class PortfolioServiceImpl implements PortfolioService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private ExperienceRepository experienceRepository;
    @Autowired
    private EducationRepository educationRepository;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private AwardRepository awardRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private LinkRepository linkRepository;





    //    /////////////////////////////////////// PORTFOLIO ///////////////////////////////////////
    @Override
    public ApiResponse<Portfolio> addPortfolio(long userId, Portfolio portfolio, MultipartFile imageFile) {
        if (userId <= 0) {
            logger.warn("Invalid User ID to delete: {}", userId);
            return new ApiResponse<>("error", "Invalid User ID", null);
        }
        if (portfolio == null) {
            logger.warn("Attempted to add a null portfolio data.");
            return new ApiResponse<>("error", "Invalid portfolio data", null);
        }
        User existingUser = userService.getById(userId).getData();
        if (existingUser == null) {
            logger.warn("Attempted to add portfolio to a null User");
            return new ApiResponse<>("error", "User Not Found", null);
        }
        // Duplicate checks
        if (portfolioRepository.existsByUserId(userId)) {
                logger.warn("Duplicate Portfolio registration attempt: {}", userId);
                return new ApiResponse<>("error", "Portfolio already Exists", null);
        }
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                File folder = new File(uploadDir);
                if (!folder.exists()) folder.mkdirs();
                // generating unique name
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.write(filePath, imageFile.getBytes());
                // Save file name in DB
                portfolio.setImage(fileName);
            }
            if (portfolio.getSkills() != null) {
                portfolio.getSkills().forEach(s -> s.setPortfolio(portfolio));
            }
            if (portfolio.getExperience() != null) {
                portfolio.getExperience().forEach(e -> e.setPortfolio(portfolio));
            }
            if (portfolio.getEducation() != null) {
                portfolio.getEducation().forEach(e -> e.setPortfolio(portfolio));
            }
            if (portfolio.getLanguages() != null) {
                portfolio.getLanguages().forEach(l -> l.setPortfolio(portfolio));
            }
            if (portfolio.getAwards() != null) {
                portfolio.getAwards().forEach(a -> a.setPortfolio(portfolio));
            }
            if (portfolio.getProjects() != null) {
                portfolio.getProjects().forEach(p -> p.setPortfolio(portfolio));
            }
            if (portfolio.getLinks() != null) {
                portfolio.getLinks().forEach(l -> l.setPortfolio(portfolio));
            }
            portfolio.setUser(existingUser);
            Portfolio savedPortfolio = portfolioRepository.save(portfolio);
            logger.info("Portfolio Added successfully: {}", savedPortfolio);
            return new ApiResponse<>("success", "Portfolio Added successfully", savedPortfolio);
        } catch (Exception e) {
            logger.error("Failed to Add Portfolio: {}", e.getMessage(), e);
            throw new RuntimeException("Error Adding Portfolio. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<Portfolio> getPortfolioByUser(long userId) {
        logger.warn("Get Portfolio  by User Id attempt: {}",userId);
        if (userId <= 0) {
            return new ApiResponse<>("error", "Invalid User Id", null);
        }
        try{
            Portfolio portfolio = portfolioRepository.findByUserId(userId);
            if (portfolio == null){
                return new ApiResponse<>("error", "Invalid User Id", null);
            }
            else {
                return new ApiResponse<>("success", "Portfolio fetch successful", portfolio);
            }
        }catch (Exception e){
            logger.error("Failed to Fetch user: {}", e.getMessage(), e);
            throw new RuntimeException("Error Fetching User. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<Portfolio> getPortfolio(long id) {
        logger.warn("Get Portfolio by  Id attempt: {}",id);
        if (id <= 0) {
            return new ApiResponse<>("error", "Invalid Portfolio Id", null);
        }
        try{
            Portfolio portfolio = portfolioRepository.findById(id).orElse(null);
            if (portfolio == null){
                return new ApiResponse<>("error", "Invalid Portfolio Id", null);
            }
            else {
                return new ApiResponse<>("success", "Portfolio fetch successful", portfolio);
            }
        }catch (Exception e){
            logger.error("Failed to Fetch Portfolio: {}", e.getMessage(), e);
            throw new RuntimeException("Error Fetching Portfolio. Please try again later.", e);
        }    }

    @Override
    public ApiResponse<Portfolio> updatePortfolio(long portfolioId,  Portfolio portfolio) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to delete: {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (portfolio == null) {
            logger.warn("Attempted to update a null portfolio data.");
            return new ApiResponse<>("error", "Invalid portfolio data", null);
        }
        Portfolio existingPortfolio = getPortfolio(portfolioId).getData();
        if (existingPortfolio == null) {
            logger.warn("Attempted to update invalid portfolio");
            return new ApiResponse<>("error", "Portfolio Not Found", null);
        }
        try {
            if (portfolio.getFirstName() != null && !portfolio.getFirstName().isEmpty()) {
                existingPortfolio.setFirstName(portfolio.getFirstName());
            }
            if (portfolio.getMiddleName() != null && !portfolio.getMiddleName().isEmpty()) {
                existingPortfolio.setMiddleName(portfolio.getMiddleName());
            }
            if (portfolio.getLastName() != null && !portfolio.getLastName().isEmpty()) {
                existingPortfolio.setLastName(portfolio.getLastName());
            }
            if (portfolio.getDesignation() != null && !portfolio.getDesignation().isEmpty()) {
                existingPortfolio.setDesignation(portfolio.getDesignation());
            }
            if (portfolio.getEmail() != null && !portfolio.getEmail().isEmpty()) {
                existingPortfolio.setEmail(portfolio.getEmail());
            }
            if (portfolio.getPhone() != null && !portfolio.getPhone().isEmpty()) {
                existingPortfolio.setPhone(portfolio.getPhone());
            }
            if (portfolio.getLinkedIn() != null && !portfolio.getLinkedIn().isEmpty()) {
                existingPortfolio.setLinkedIn(portfolio.getLinkedIn());
            }
            if (portfolio.getAddress() != null) {
                Address exAddress = existingPortfolio.getAddress();
                if (portfolio.getAddress().getDoorNo() != null && !portfolio.getAddress().getDoorNo().isEmpty()) {
                    exAddress.setDoorNo(portfolio.getAddress().getDoorNo());
                }
                if (portfolio.getAddress().getStreet() != null && !portfolio.getAddress().getStreet().isEmpty()) {
                    exAddress.setStreet(portfolio.getAddress().getStreet());
                }
                if (portfolio.getAddress().getCity() != null && !portfolio.getAddress().getCity().isEmpty()) {
                    exAddress.setCity(portfolio.getAddress().getCity());
                }
                if (portfolio.getAddress().getState() != null && !portfolio.getAddress().getState().isEmpty()) {
                    exAddress.setState(portfolio.getAddress().getState());
                }
                if (portfolio.getAddress().getCountry() != null && !portfolio.getAddress().getCountry().isEmpty()) {
                    exAddress.setCountry(portfolio.getAddress().getCountry());
                }
                if (portfolio.getAddress().getZipCode() != 0) {
                    exAddress.setZipCode(portfolio.getAddress().getZipCode());
                }
                existingPortfolio.setAddress(exAddress);
            }
            if (portfolio.getSummary() != null && !portfolio.getSummary().isEmpty()) {
                existingPortfolio.setSummary(portfolio.getSummary());
            }
            Portfolio savedPortfolio = portfolioRepository.save(existingPortfolio);
            logger.info("Portfolio Updated successfully: {}", savedPortfolio);
            return new ApiResponse<>("success", "Portfolio Updated successfully", savedPortfolio);
        } catch (Exception e) {
            logger.error("Failed to Update Portfolio: {}", e.getMessage(), e);
            throw new RuntimeException("Error Updating Portfolio. Please try again later.", e);
        }    }

    @Override
    public ApiResponse<Portfolio> updatePortfolioImage(long portfolioId, MultipartFile imageFile) {
        logger.warn("Attempted to update a Portfolio pic.");
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID : {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (imageFile == null) {
            logger.warn("Attempted to update a Invalid Picture File.");
            return new ApiResponse<>("error", "Invalid Picture File.", null);
        }
        Portfolio existingPortfolio = getPortfolio(portfolioId).getData();

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                deletePortfolioImage(portfolioId);
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                File folder = new File(uploadDir);
                if (!folder.exists()) folder.mkdirs();
                // generating unique name
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.write(filePath, imageFile.getBytes());
                // Save file name in DB
                existingPortfolio.setImage(fileName);
            }
            Portfolio savedPortfolio = portfolioRepository.save(existingPortfolio);
            logger.info("Portfolio Image Updated successfully: {}", savedPortfolio);
            return new ApiResponse<>("success", "User Profile pic Updated successfully", savedPortfolio);
        } catch (Exception e) {
            logger.error("Failed to Update Profile pic: {}", e.getMessage(), e);
            throw new RuntimeException("Error Updating Profile pic. Please try again later.", e);
        }        }

    @Override
    public ApiResponse<String> deletePortfolioImage(long portfolioId) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID: {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        try {
            Portfolio portfolio = portfolioRepository.findById(portfolioId).orElse(null);
            if (portfolio == null) {
                return new ApiResponse<>("error", "Portfolio Not Found", null);
            }
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            // Delete file if exists
            if (portfolio.getImage() != null) {
                File file = new File(uploadDir + portfolio.getImage());
                if (file.exists()) file.delete();
            }
            // Remove file name from DB
            portfolio.setImage(null);
            portfolioRepository.save(portfolio);
            logger.info("Portfolio image removed: {}", portfolioId);
            return new ApiResponse<>("success", "Image Deleted Successfully", null);
        } catch (Exception e) {
            logger.error("Failed to delete portfolio image: {}", e.getMessage(), e);
            return new ApiResponse<>("error", "Failed to delete portfolio image", null);
        }
    }






    //    /////////////////////////////////////// SKILLS ///////////////////////////////////////
//    @Override
//    public ApiResponse<Skill>  addSkill(long portfolioId, Skill skill) {
//        if (portfolioId <= 0) {
//            logger.warn("Invalid Portfolio ID to add Skills: {}", portfolioId);
//            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
//        }
//        if (skill == null) {
//            logger.warn("Attempted to add a null Skill data.");
//            return new ApiResponse<>("error", "Invalid Skill data", null);
//        }
//        Portfolio portfolio = getPortfolio(portfolioId).getData();
//        if (portfolio == null) {
//            logger.warn("Attempted to add skill to a null portfolio");
//            return new ApiResponse<>("error", "Portfolio Not Found", null);
//        }
//        try {
////                List<Skill> existingSkills = portfolio.getSkills();
//                skill.setPortfolio(portfolio);
////                existingSkills.addAll(skills);
////                portfolio.setSkills(existingSkills);
//            Skill savedSkill = skillRepository.save(skill);
//            logger.info("Skill Added successfully: {}", savedSkill);
//            return new ApiResponse<>("success", "Skill Added successfully", savedSkill);
//        } catch (Exception e) {
//            logger.error("Failed to Add Skill: {}", e.getMessage(), e);
//            throw new RuntimeException("Error Adding Skill. Please try again later.", e);
//        }
//    }
    @Override
    public ApiResponse<List<Skill>>  addSkills(long portfolioId, List<Skill> skills) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to add Skills: {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (skills == null || skills.isEmpty()) {
            logger.warn("Attempted to add a null Skills data.");
            return new ApiResponse<>("error", "Invalid Skills data", null);
        }
        Portfolio portfolio = getPortfolio(portfolioId).getData();
        if (portfolio == null) {
            logger.warn("Attempted to add skills to a null portfolio");
            return new ApiResponse<>("error", "Portfolio Not Found", null);
        }
        try {
            if (!skills.isEmpty()) {
                List<Skill> existingSkills = portfolio.getSkills();
                skills.forEach(s -> s.setPortfolio(portfolio));
                existingSkills.addAll(skills);
                portfolio.setSkills(existingSkills);
            }
            Portfolio savedPortfolio = portfolioRepository.save(portfolio);
            logger.info("Skills Added successfully: {}", savedPortfolio.getSkills());
            return new ApiResponse<>("success", "Skills Added successfully", savedPortfolio.getSkills());
        } catch (Exception e) {
            logger.error("Failed to Add Portfolio: {}", e.getMessage(), e);
            throw new RuntimeException("Error Adding Skills. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<Skill> deleteSkill(long portfolioId, Skill skill) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to Delete Skill {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (skill == null) {
            logger.warn("Attempted to delete a null Skill.");
            return new ApiResponse<>("error", "Invalid Skill data", null);
        }
        Portfolio portfolio = getPortfolio(portfolioId).getData();
        if (portfolio == null) {
            logger.warn("Attempted to delete skill to a null portfolio");
            return new ApiResponse<>("error", "Portfolio Not Found", null);
        }
        try {
            if (skill.getId() != 0) {
                skillRepository.deleteById(skill.getId());
                logger.info("Skill deleted successfully: {}", skill);
                return new ApiResponse<>("success", "Skill deleted successfully", null);
            }else {
                logger.info("Skill not Found: {}", skill);
                return new ApiResponse<>("error", "Skill Not Found", skill);
            }
        } catch (Exception e) {
            logger.error("Failed to Delete Skill: {}", e.getMessage(), e);
            throw new RuntimeException("Error Deleting Skill. Please try again later.", e);
        }
    }

//    @Override
//    public ApiResponse<List<Skill>> deleteSkills(long portfolioId, List<Skill> skills) {
//        if (portfolioId <= 0) {
//            logger.warn("Invalid Portfolio ID to delete Skills: {}", portfolioId);
//            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
//        }
//        if (skills == null || skills.isEmpty()) {
//            logger.warn("Attempted to delete a null Skills.");
//            return new ApiResponse<>("error", "Invalid Skills data", null);
//        }
//        Portfolio portfolio = getPortfolio(portfolioId).getData();
//        if (portfolio == null) {
//            logger.warn("Attempted to delete skills to a null portfolio");
//            return new ApiResponse<>("error", "Portfolio Not Found", null);
//        }
//        try {
//            if (!skills.isEmpty()) {
//                for (Skill skl : skills){
//                    if (skl != null){
//                        skillRepository.deleteById(skl.getId());
//                        logger.info("Skill deleted successfully: {}", skl);
//                    }else {
//                        logger.info("Skill not Found: {}", skl);
//                        List<Skill> errorSkills = new ArrayList<>();
//                        errorSkills.add(skl);
//                        return new ApiResponse<>("error", "Skills Not Found", errorSkills);
//                    }
//                }
//            }
//            return new ApiResponse<>("success", "Skills deleted successfully", null);
//        } catch (Exception e) {
//            logger.error("Failed to Delete Skills: {}", e.getMessage(), e);
//            throw new RuntimeException("Error Deleting Skills. Please try again later.", e);
//        }
//    }






    //    /////////////////////////////////////// EXPERIENCE ///////////////////////////////////////
    @Override
    public ApiResponse<List<Experience>> addExperience(long portfolioId, List<Experience> experienceList) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to add Experience: {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (experienceList == null || experienceList.isEmpty()) {
            logger.warn("Attempted to add a null Experience data.");
            return new ApiResponse<>("error", "Invalid Experience data", null);
        }
        Portfolio portfolio = getPortfolio(portfolioId).getData();
        if (portfolio == null) {
            logger.warn("Attempted to add Experience to a Invalid portfolio");
            return new ApiResponse<>("error", "Portfolio Not Found", null);
        }
        for (Experience exp: experienceList){
            if (exp.isStillWorking()){
                exp.setEndDate(null);
            }
        }
        try {
            if (!experienceList.isEmpty()) {
                List<Experience> existingExperience = portfolio.getExperience();
                experienceList.forEach(s -> s.setPortfolio(portfolio));
                existingExperience.addAll(experienceList);
                portfolio.setExperience(existingExperience);
            }
            Portfolio savedPortfolio = portfolioRepository.save(portfolio);
            logger.info("Experience Added successfully: {}", savedPortfolio.getExperience());
            return new ApiResponse<>("success", "Experience Added successfully", savedPortfolio.getExperience());
        } catch (Exception e) {
            logger.error("Failed to Add Experience: {}", e.getMessage(), e);
            throw new RuntimeException("Error Adding Experience. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<Experience> updateExperience(long portfolioId, Experience experience) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to update Experience: {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (experience == null) {
            logger.warn("Attempted to update a null Experience data.");
            return new ApiResponse<>("error", "Invalid Experience data", null);
        }
        if (experience.getId() <= 0) {
            logger.warn("Attempted to update a null Experience data.");
            return new ApiResponse<>("error", "Invalid Experience Id", null);
        }
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElse(null);
        try {
            experience.setPortfolio(portfolio);
            Experience savedExperience = experienceRepository.save(experience);
            logger.info("Experience Updated successfully: {}", savedExperience);
            return new ApiResponse<>("success", "Experience Updated successfully", savedExperience);
        } catch (Exception e) {
            logger.error("Failed to Update Experience: {}", e.getMessage(), e);
            throw new RuntimeException("Error Updating Experience. Please try again later.", e);
        }      }

    @Override
    public ApiResponse<Experience> deleteExperience(long portfolioId, Experience experience) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to Delete Experience {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (experience == null) {
            logger.warn("Attempted to delete a null Experience.");
            return new ApiResponse<>("error", "Invalid Experience data", null);
        }
        Portfolio portfolio = getPortfolio(portfolioId).getData();
        if (portfolio == null) {
            logger.warn("Attempted to Delete Experience to a null portfolio");
            return new ApiResponse<>("error", "Portfolio Not Found", null);
        }
        try {
            if (experience.getId() != 0) {
                experienceRepository.deleteById(experience.getId());
                logger.info("Experience deleted successfully: {}", experience);
                return new ApiResponse<>("success", "Experience deleted successfully", null);
            }else {
                logger.info("Experience not Found: {}", experience);
                return new ApiResponse<>("error", "Experience Not Found", experience);
            }
        } catch (Exception e) {
            logger.error("Failed to Delete Experience: {}", e.getMessage(), e);
            throw new RuntimeException("Error Deleting Experience. Please try again later.", e);
        }
    }






    //    /////////////////////////////////////// EDUCATION ///////////////////////////////////////
    @Override
    public ApiResponse<List<Education>> addEducation(long portfolioId, List<Education> educationList) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to add Education: {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (educationList == null || educationList.isEmpty()) {
            logger.warn("Attempted to add a null Education data.");
            return new ApiResponse<>("error", "Invalid Education data", null);
        }
        Portfolio portfolio = getPortfolio(portfolioId).getData();
        if (portfolio == null) {
            logger.warn("Attempted to add Education to a Invalid portfolio");
            return new ApiResponse<>("error", "Portfolio Not Found", null);
        }
        for (Education edu: educationList){
            if (edu.isStillPursuing()){
                edu.setEndDate(null);
            }
        }
        try {
            if (!educationList.isEmpty()) {
                List<Education> existingEducation = portfolio.getEducation();
                educationList.forEach(s -> s.setPortfolio(portfolio));
                existingEducation.addAll(educationList);
                portfolio.setEducation(existingEducation);
            }
            Portfolio savedPortfolio = portfolioRepository.save(portfolio);
            logger.info("Education Added successfully: {}", savedPortfolio.getEducation());
            return new ApiResponse<>("success", "Education Added successfully", savedPortfolio.getEducation());
        } catch (Exception e) {
            logger.error("Failed to Add Education: {}", e.getMessage(), e);
            throw new RuntimeException("Error Adding Education. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<Education> updateEducation(long portfolioId, Education education) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to update Education: {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (education == null) {
            logger.warn("Attempted to update a null Education data.");
            return new ApiResponse<>("error", "Invalid Education data", null);
        }
        if (education.getId() <= 0) {
            logger.warn("Attempted to update a null Education data.");
            return new ApiResponse<>("error", "Invalid Education Id", null);
        }
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElse(null);
        try {
            education.setPortfolio(portfolio);
            Education savedEducation = educationRepository.save(education);
            logger.info("Education Updated successfully: {}", savedEducation);
            return new ApiResponse<>("success", "Education Updated successfully", savedEducation);
        } catch (Exception e) {
            logger.error("Failed to Update Education: {}", e.getMessage(), e);
            throw new RuntimeException("Error Updating Education. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<Education> deleteEducation(long portfolioId, Education education) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to Delete Education {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (education == null) {
            logger.warn("Attempted to delete a null Education.");
            return new ApiResponse<>("error", "Invalid Education data", null);
        }
        Portfolio portfolio = getPortfolio(portfolioId).getData();
        if (portfolio == null) {
            logger.warn("Attempted to Delete Education to a null portfolio");
            return new ApiResponse<>("error", "Portfolio Not Found", null);
        }
        try {
            if (education.getId() != 0) {
                educationRepository.deleteById(education.getId());
                logger.info("Education deleted successfully: {}", education);
                return new ApiResponse<>("success", "Education deleted successfully", null);
            }else {
                logger.info("Education not Found: {}", education);
                return new ApiResponse<>("error", "Education Not Found", education);
            }
        } catch (Exception e) {
            logger.error("Failed to Delete Education: {}", e.getMessage(), e);
            throw new RuntimeException("Error Deleting Education. Please try again later.", e);
        }
    }






    //    /////////////////////////////////////// LANGUAGES ///////////////////////////////////////
    @Override
    public ApiResponse<List<Language>> addLanguages(long portfolioId, List<Language> laguageList) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to add Languages: {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (laguageList == null || laguageList.isEmpty()) {
            logger.warn("Attempted to add a null Language data.");
            return new ApiResponse<>("error", "Invalid Language data", null);
        }
        Portfolio portfolio = getPortfolio(portfolioId).getData();
        if (portfolio == null) {
            logger.warn("Attempted to add Languages to a Invalid portfolio");
            return new ApiResponse<>("error", "Portfolio Not Found", null);
        }
        try {
            if (!laguageList.isEmpty()) {
                List<Language> existingLanguage = portfolio.getLanguages();
                laguageList.forEach(s -> s.setPortfolio(portfolio));
                existingLanguage.addAll(laguageList);
                portfolio.setLanguages(existingLanguage);
            }
            Portfolio savedPortfolio = portfolioRepository.save(portfolio);
            logger.info("Languages Added successfully: {}", savedPortfolio.getLanguages());
            return new ApiResponse<>("success", "Languages Added successfully", savedPortfolio.getLanguages());
        } catch (Exception e) {
            logger.error("Failed to Add Languages: {}", e.getMessage(), e);
            throw new RuntimeException("Error Adding Languages. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<Language> deleteLanguage(long portfolioId, Language language) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to Delete Language {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (language == null) {
            logger.warn("Attempted to delete a null Language.");
            return new ApiResponse<>("error", "Invalid Language data", null);
        }
        Portfolio portfolio = getPortfolio(portfolioId).getData();
        if (portfolio == null) {
            logger.warn("Attempted to Delete Language to a null portfolio");
            return new ApiResponse<>("error", "Portfolio Not Found", null);
        }
        try {
            if (language.getId() != 0) {
                languageRepository.deleteById(language.getId());
                logger.info("Language deleted successfully: {}", language);
                return new ApiResponse<>("success", "Language deleted successfully", null);
            }else {
                logger.info("Language not Found: {}", language);
                return new ApiResponse<>("error", "Language Not Found", language);
            }
        } catch (Exception e) {
            logger.error("Failed to Delete Language: {}", e.getMessage(), e);
            throw new RuntimeException("Error Deleting Language. Please try again later.", e);
        }
    }






    //    /////////////////////////////////////// AWARDS ///////////////////////////////////////
    @Override
    public ApiResponse<List<Award>> addAwards(long portfolioId, List<Award> awardList) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to add Awards: {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (awardList == null || awardList.isEmpty()) {
            logger.warn("Attempted to add a null Award data.");
            return new ApiResponse<>("error", "Invalid Award data", null);
        }
        Portfolio portfolio = getPortfolio(portfolioId).getData();
        if (portfolio == null) {
            logger.warn("Attempted to add Awards to a Invalid portfolio");
            return new ApiResponse<>("error", "Portfolio Not Found", null);
        }
        try {
            if (!awardList.isEmpty()) {
                List<Award> existingAwards = portfolio.getAwards();
                awardList.forEach(s -> s.setPortfolio(portfolio));
                existingAwards.addAll(awardList);
                portfolio.setAwards(existingAwards);
            }
            Portfolio savedPortfolio = portfolioRepository.save(portfolio);
            logger.info("Awards Added successfully: {}", savedPortfolio.getAwards());
            return new ApiResponse<>("success", "Awards Added successfully", savedPortfolio.getAwards());
        } catch (Exception e) {
            logger.error("Failed to Add Awards: {}", e.getMessage(), e);
            throw new RuntimeException("Error Adding Awards. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<Award> deleteAward(long portfolioId, Award award) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to Delete Award {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (award == null) {
            logger.warn("Attempted to delete a null Award.");
            return new ApiResponse<>("error", "Invalid Award data", null);
        }
        Portfolio portfolio = getPortfolio(portfolioId).getData();
        if (portfolio == null) {
            logger.warn("Attempted to Delete Award to a null portfolio");
            return new ApiResponse<>("error", "Portfolio Not Found", null);
        }
        try {
            if (award.getId() != 0) {
                awardRepository.deleteById(award.getId());
                logger.info("Award deleted successfully: {}", award);
                return new ApiResponse<>("success", "Award deleted successfully", null);
            }else {
                logger.info("Award not Found: {}", award);
                return new ApiResponse<>("error", "Award Not Found", award);
            }
        } catch (Exception e) {
            logger.error("Failed to Delete Award: {}", e.getMessage(), e);
            throw new RuntimeException("Error Deleting Award. Please try again later.", e);
        }
    }






    //    /////////////////////////////////////// PROJECTS ///////////////////////////////////////
    @Override
    public ApiResponse<List<Project>> addProjects(long portfolioId, List<Project> projectList) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to add Projects: {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (projectList == null || projectList.isEmpty()) {
            logger.warn("Attempted to add a null Project data.");
            return new ApiResponse<>("error", "Invalid Award data", null);
        }
        Portfolio portfolio = getPortfolio(portfolioId).getData();
        if (portfolio == null) {
            logger.warn("Attempted to add Projects to a Invalid portfolio");
            return new ApiResponse<>("error", "Portfolio Not Found", null);
        }
        for (Project pro: projectList){
            if (pro.isStillWorking()){
                pro.setEndDate(null);
            }
        }
        try {
            if (!projectList.isEmpty()) {
                List<Project> existingProjects = portfolio.getProjects();
                projectList.forEach(s -> s.setPortfolio(portfolio));
                existingProjects.addAll(projectList);
                portfolio.setProjects(existingProjects);
            }
            Portfolio savedPortfolio = portfolioRepository.save(portfolio);
            logger.info("Projects Added successfully: {}", savedPortfolio.getProjects());
            return new ApiResponse<>("success", "Projects Added successfully", savedPortfolio.getProjects());
        } catch (Exception e) {
            logger.error("Failed to Add Projects: {}", e.getMessage(), e);
            throw new RuntimeException("Error Adding Projects. Please try again later.", e);
        }     }

    @Override
    public ApiResponse<Project> updateProject(long portfolioId, Project project) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to update Project: {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (project == null) {
            logger.warn("Attempted to update a null Project data.");
            return new ApiResponse<>("error", "Invalid Project data", null);
        }
        if (project.getId() <= 0) {
            logger.warn("Attempted to update a null Project data.");
            return new ApiResponse<>("error", "Invalid Project Id", null);
        }
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElse(null);
        try {
            project.setPortfolio(portfolio);
            Project savedProject = projectRepository.save(project);
            logger.info("Project Updated successfully: {}", savedProject);
            return new ApiResponse<>("success", "Project Updated successfully", savedProject);
        } catch (Exception e) {
            logger.error("Failed to Update Project: {}", e.getMessage(), e);
            throw new RuntimeException("Error Updating Project. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<Project> deleteProject(long portfolioId, Project project) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to Delete Project {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (project == null) {
            logger.warn("Attempted to delete a null Project.");
            return new ApiResponse<>("error", "Invalid Project data", null);
        }
        Portfolio portfolio = getPortfolio(portfolioId).getData();
        if (portfolio == null) {
            logger.warn("Attempted to Delete Project to a null portfolio");
            return new ApiResponse<>("error", "Portfolio Not Found", null);
        }
        try {
            if (project.getId() != 0) {
                projectRepository.deleteById(project.getId());
                logger.info("Project deleted successfully: {}", project);
                return new ApiResponse<>("success", "Project deleted successfully", null);
            }else {
                logger.info("Project not Found: {}", project);
                return new ApiResponse<>("error", "Project Not Found", project);
            }
        } catch (Exception e) {
            logger.error("Failed to Delete Project: {}", e.getMessage(), e);
            throw new RuntimeException("Error Deleting Project. Please try again later.", e);
        }
    }






    //    /////////////////////////////////////// LINKS ///////////////////////////////////////
    @Override
    public ApiResponse<List<Link>> addLinks(long portfolioId, List<Link> linkList) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to add Links: {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (linkList == null || linkList.isEmpty()) {
            logger.warn("Attempted to add a null Link data.");
            return new ApiResponse<>("error", "Invalid Link data", null);
        }
        Portfolio portfolio = getPortfolio(portfolioId).getData();
        if (portfolio == null) {
            logger.warn("Attempted to add Links to a Invalid portfolio");
            return new ApiResponse<>("error", "Portfolio Not Found", null);
        }
        try {
            if (!linkList.isEmpty()) {
                List<Link> existingLinks = portfolio.getLinks();
                linkList.forEach(s -> s.setPortfolio(portfolio));
                existingLinks.addAll(linkList);
                portfolio.setLinks(existingLinks);
            }
            Portfolio savedPortfolio = portfolioRepository.save(portfolio);
            logger.info("Links Added successfully: {}", savedPortfolio.getLinks());
            return new ApiResponse<>("success", "Links Added successfully", savedPortfolio.getLinks());
        } catch (Exception e) {
            logger.error("Failed to Add Links: {}", e.getMessage(), e);
            throw new RuntimeException("Error Adding Links. Please try again later.", e);
        }
    }

    @Override
    public ApiResponse<Link> deleteLink(long portfolioId, Link link) {
        if (portfolioId <= 0) {
            logger.warn("Invalid Portfolio ID to Delete Link {}", portfolioId);
            return new ApiResponse<>("error", "Invalid Portfolio ID", null);
        }
        if (link == null) {
            logger.warn("Attempted to delete a null Link.");
            return new ApiResponse<>("error", "Invalid Link data", null);
        }
        Portfolio portfolio = getPortfolio(portfolioId).getData();
        if (portfolio == null) {
            logger.warn("Attempted to Delete Link to a null portfolio");
            return new ApiResponse<>("error", "Portfolio Not Found", null);
        }
        try {
            if (link.getId() != 0) {
                linkRepository.deleteById(link.getId());
                logger.info("Link deleted successfully: {}", link);
                return new ApiResponse<>("success", "Link deleted successfully", null);
            }else {
                logger.info("Link not Found: {}", link);
                return new ApiResponse<>("error", "Link Not Found", link);
            }
        } catch (Exception e) {
            logger.error("Failed to Delete Link: {}", e.getMessage(), e);
            throw new RuntimeException("Error Deleting Link. Please try again later.", e);
        }     }
}
