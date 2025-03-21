package com.blackpanthers.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blackpanthers.model.Cricketer;
import com.blackpanthers.service.CricketerService;

import jakarta.validation.Valid;

@Controller
public class CricketerController {

    private final CricketerService cricketerService;

    @Autowired
    public CricketerController(CricketerService cricketerService) {
        this.cricketerService = cricketerService;
    }

    @GetMapping("/")
    public String showRegistrationForm(Model model) {
        model.addAttribute("cricketer", new Cricketer());
        model.addAttribute("jerseySizes", Cricketer.JerseySize.values());
        model.addAttribute("battingStyles", Cricketer.BattingStyle.values());
        model.addAttribute("bowlingStyles", Cricketer.BowlingStyle.values());
        model.addAttribute("playerTypes", Cricketer.PlayerType.values());
        return "register";
    }

    @PostMapping("/register")
    public String registerCricketer(
            @Valid @ModelAttribute("cricketer") Cricketer cricketer,
            BindingResult bindingResult,
            @RequestParam("photoFile") MultipartFile photoFile,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Add enum values to model in case of validation errors
        model.addAttribute("jerseySizes", Cricketer.JerseySize.values());
        model.addAttribute("battingStyles", Cricketer.BattingStyle.values());
        model.addAttribute("bowlingStyles", Cricketer.BowlingStyle.values());
        model.addAttribute("playerTypes", Cricketer.PlayerType.values());

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            // Register the cricketer
            Cricketer registeredCricketer = cricketerService.registerCricketer(cricketer, photoFile);
            
            // Add success message
            redirectAttributes.addFlashAttribute("successMessage", 
                "Registration successful! A confirmation email has been sent to " + registeredCricketer.getEmail());
            
            return "redirect:/success";
        } catch (IllegalArgumentException e) {
            // Handle business validation errors
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        } catch (IOException e) {
            // Handle file processing errors
            model.addAttribute("errorMessage", "Error processing photo: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "success";
    }

    // AJAX endpoints for validation
    @GetMapping("/api/check-email")
    @ResponseBody
    public Map<String, Boolean> checkEmailAvailability(@RequestParam String email) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", cricketerService.isEmailRegistered(email));
        return response;
    }

//    @GetMapping("/api/check-jersey-number")
//    @ResponseBody
//    public Map<String, Boolean> checkJerseyNumberAvailability(@RequestParam Integer jerseyNumber) {
//        Map<String, Boolean> response = new HashMap<>();
//        response.put("exists", cricketerService.isJerseyNumberTaken(jerseyNumber));
//        return response;
//    }
}
