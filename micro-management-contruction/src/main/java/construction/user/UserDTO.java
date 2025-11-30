package construction.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO {
    
    private final String id;
    private final PII personalInformation;
    private final UserRole role; // Supondo que UserRole é um Enum

    @JsonCreator
    public UserDTO(
        @JsonProperty("id") String id, 
        @JsonProperty("personalInformation") PII personalInformation, 
        @JsonProperty("role") UserRole role
    ) {
        this.id = id;
        this.personalInformation = personalInformation;
        this.role = role;
    }

    // Getters para os campos
    public String getId() {
        return id;
    }

    public PII getPersonalInformation() {
        return personalInformation;
    }

    public UserRole getRole() {
        return role;
    }

    public static class PII {
        
        private final String firstName;
        private final String lastName;
        private final String email;
        private final String phone;
        private final String cpf;

        @JsonCreator
        public PII(
            @JsonProperty("firstName") String firstName, 
            @JsonProperty("lastName") String lastName, 
            @JsonProperty("email") String email, 
            @JsonProperty("phone") String phone, 
            @JsonProperty("cpf") String cpf
        ) {
            // Validações movidas para o construtor da classe
            if (cpf == null || cpf.length() != 14) {
                throw new IllegalArgumentException("CPF must be in the format XXX.XXX.XXX-XX and must have 14 characters.");
            }
            if (email == null || !email.contains("@")) {
                throw new IllegalArgumentException("Invalid email format");
            }

            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
            this.cpf = cpf;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getCpf() {
            return cpf;
        }

        public String fullName() {
            return firstName + " " + lastName;
        }

    }
}
