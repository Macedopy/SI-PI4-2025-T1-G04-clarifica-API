package construction.user;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UserService {
    
    @Inject
    UserRepository userRepository;

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        // Check if user with same CPF already exists
        if (userRepository.find("cpf", userDTO.getPersonalInformation().getCpf()).count() > 0) {
            throw new IllegalArgumentException("User with CPF " + userDTO.getPersonalInformation().getCpf() + " already exists.");
        }

        // Check if user with same email already exists
        if (userRepository.find("email", userDTO.getPersonalInformation().getEmail()).count() > 0) {
            throw new IllegalArgumentException("User with email " + userDTO.getPersonalInformation().getEmail() + " already exists.");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());

        user.setFirstName(userDTO.getPersonalInformation().getFirstName());
        user.setLastName(userDTO.getPersonalInformation().getLastName());
        user.setEmail(userDTO.getPersonalInformation().getEmail());
        user.setPhone(userDTO.getPersonalInformation().getPhone());
        user.setCpf(userDTO.getPersonalInformation().getCpf());
        user.setRole(userDTO.getRole());

        userRepository.persist(user);

        return mapToDTO(user);
    }

    @Transactional
    public List<UserDTO> allCustomers() {
        return userRepository.findAllCustomers()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO customerById(String id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new NotFoundException("Usuário com ID " + id + " não encontrado.");
        }
        return mapToDTO(user);
    }

    private UserDTO mapToDTO(User user) {
        return new UserDTO(
            user.getId(),
            new UserDTO.PII(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getCpf()
            ),
            user.getRole()
        );
    }
}
