package construction.user;

public record UserDTO(String id, PII personalInformation) {
    public record PII(
        String firstName,
        String lastName,
        String email,
        String phone,
        String cpf
    ) {
        public PII 
        {
            if (cpf.length() != 14) {
                throw new IllegalArgumentException("CPF must be in the format XXX.XXX.XXX-XX");
            }
            if (email == null || !email.contains("@")) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }

        public String fullName() 
        {
            return firstName + " " + lastName;
        }
    }
}