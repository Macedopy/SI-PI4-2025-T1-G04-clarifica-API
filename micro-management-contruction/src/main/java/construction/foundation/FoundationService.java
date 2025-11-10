package construction.foundation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.smallrye.common.constraint.NotNull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FoundationService {
    @Inject
    FoundationRepository userRepository;

    @Transactional
    public List<FoundationDTO> allCustomers() {
        return userRepository.findAllCustomers()
        .stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
    }

    @Transactional
    public FoundationDTO customerById(String id) {
        Foundation user = userRepository.findByIdOptional(id);
        return user;
    }

    private FoundationDTO mapToDTO(Foundation user) {
        return new FoundationDTO(
            user.getId(),
            new FoundationDTO.PII(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getCpf()
            )
        );
    }
}
