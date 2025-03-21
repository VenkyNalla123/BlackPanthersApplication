package com.blackpanthers.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cricketers")
public class Cricketer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotNull(message = "Date of birth is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be less than 100 characters")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be between 10 to 15 digits and may include a country code")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Jersey number is required")
    @Min(value = 1, message = "Jersey number must be at least 1")
    @Max(value = 1000, message = "Jersey number must be less than 1000")
    
    private Integer jerseyNumber;

    @NotNull(message = "Jersey size is required")
    @Enumerated(EnumType.STRING)
    private JerseySize jerseySize;

    @NotNull(message = "Batting style is required")
    @Enumerated(EnumType.STRING)
    private BattingStyle battingStyle;

    @NotNull(message = "Bowling style is required")
    @Enumerated(EnumType.STRING)
    private BowlingStyle bowlingStyle;

    @Lob
    private byte[] photo;

    private String photoContentType;

    @Column(name = "registration_date", updatable = false)
    private LocalDateTime registrationDate;

    private String emergencyContactName;
    private String emergencyContactPhone;

    @Enumerated(EnumType.STRING)
    private PlayerType playerType;

    private Integer yearsOfExperience;
    private Boolean hasMedicalCondition;
    private String medicalDetails;

    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
    }

    public enum JerseySize {
        S, M, L, XL, XXL
    }

    public enum BattingStyle {
        RIGHT_HANDED("Right-handed"),
        LEFT_HANDED("Left-handed");

        private final String displayName;

        BattingStyle(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum BowlingStyle {
        RIGHT_ARM_FAST("Right-arm fast"),
        LEFT_ARM_FAST("Left-arm fast"),
        RIGHT_ARM_SPIN("Right-arm spin"),
        LEFT_ARM_SPIN("Left-arm spin"),
        NONE("None");

        private final String displayName;

        BowlingStyle(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum PlayerType {
        BATSMAN("Batsman"),
        BOWLER("Bowler"),
        ALL_ROUNDER("All-rounder"),
        WICKET_KEEPER("Wicket-keeper");

        private final String displayName;

        PlayerType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getJerseyNumber() {
		return jerseyNumber;
	}

	public void setJerseyNumber(Integer jerseyNumber) {
		this.jerseyNumber = jerseyNumber;
	}

	public JerseySize getJerseySize() {
		return jerseySize;
	}

	public void setJerseySize(JerseySize jerseySize) {
		this.jerseySize = jerseySize;
	}

	public BattingStyle getBattingStyle() {
		return battingStyle;
	}

	public void setBattingStyle(BattingStyle battingStyle) {
		this.battingStyle = battingStyle;
	}

	public BowlingStyle getBowlingStyle() {
		return bowlingStyle;
	}

	public void setBowlingStyle(BowlingStyle bowlingStyle) {
		this.bowlingStyle = bowlingStyle;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getPhotoContentType() {
		return photoContentType;
	}

	public void setPhotoContentType(String photoContentType) {
		this.photoContentType = photoContentType;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getEmergencyContactName() {
		return emergencyContactName;
	}

	public void setEmergencyContactName(String emergencyContactName) {
		this.emergencyContactName = emergencyContactName;
	}

	public String getEmergencyContactPhone() {
		return emergencyContactPhone;
	}

	public void setEmergencyContactPhone(String emergencyContactPhone) {
		this.emergencyContactPhone = emergencyContactPhone;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}

	public void setPlayerType(PlayerType playerType) {
		this.playerType = playerType;
	}

	public Integer getYearsOfExperience() {
		return yearsOfExperience;
	}

	public void setYearsOfExperience(Integer yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}

	public Boolean getHasMedicalCondition() {
		return hasMedicalCondition;
	}

	public void setHasMedicalCondition(Boolean hasMedicalCondition) {
		this.hasMedicalCondition = hasMedicalCondition;
	}

	public String getMedicalDetails() {
		return medicalDetails;
	}

	public void setMedicalDetails(String medicalDetails) {
		this.medicalDetails = medicalDetails;
	}
    
    
    
}
