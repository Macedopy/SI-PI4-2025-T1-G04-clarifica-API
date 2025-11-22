package construction.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException; // Necessário para tratamento de erro 404

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    @Transactional
    public List<UserDTO> allCustomers() {
        return userRepository.findAllCustomers()
        .stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO customerById(String id) {
        return userRepository.findByIdOptional(id)
            .map(this::mapToDTO) // Mapeia User para UserDTO
            .orElseThrow(() -> new NotFoundException("Cliente com ID " + id + " não encontrado."));
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
            )
        );
    }
}
