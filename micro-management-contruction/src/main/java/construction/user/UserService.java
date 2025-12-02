package construction.user;

import construction.foundation.Foundation;
import construction.structure.Structure;
import construction.terrain_preparation.TerrainPreparation;
import construction.eletric.Eletric;
import construction.finishing.Finishing;
import construction.masonry.Masonry;
import construction.roofing.Roofing;
import construction.coatings.Coatings;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

import clarifica.infrastructure.Client.ManipulationClient; 

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
        User user = new User();

        String tokenDoServidor = ManipulationClient.obterTokenDoServidor();

        if (tokenDoServidor != null && !tokenDoServidor.equals("ERRO_TOKEN") && !tokenDoServidor.isEmpty()) {
            user.setId(tokenDoServidor);
        } else {
            System.out.println("Aviso: Não foi possível conectar ao servidor de IDs. Usando UUID local.");
            user.setId(UUID.randomUUID().toString().split("-")[0]);
        }

        user.setFirstName(userDTO.getPersonalInformation().getFirstName());
        user.setLastName(userDTO.getPersonalInformation().getLastName());
        user.setEmail(userDTO.getPersonalInformation().getEmail());
        user.setPhone(userDTO.getPersonalInformation().getPhone());
        user.setCpf(userDTO.getPersonalInformation().getCpf());
        user.setRole(userDTO.getRole());

        userRepository.persist(user);

        createEmptyPhases(user);

        return mapToDTO(user);
    }

    private void createEmptyPhases(User user) {
        String contractorName = user.fullName();

        // Foundation
        Foundation foundation = new Foundation();
        foundation.setId(UUID.randomUUID().toString());
        foundation.setName("Foundation - " + contractorName);
        foundation.setContractor(contractorName);
        foundation.setUser(user);
        foundation.persist();
        user.setFoundation(foundation);

        // Structure
        Structure structure = new Structure();
        structure.setId(UUID.randomUUID().toString());
        structure.setName("Structure - " + contractorName);
        structure.setContractor(contractorName);
        structure.setUser(user);
        structure.persist();
        user.setStructure(structure);

        // Eletric
        Eletric eletric = new Eletric();
        eletric.setId(UUID.randomUUID().toString());
        eletric.setName("Eletric - " + contractorName);
        eletric.setContractor(contractorName);
        eletric.setUser(user);
        eletric.persist();
        user.setElectrical(eletric);

        Masonry masonry = new Masonry();
        masonry.setId(UUID.randomUUID().toString());
        masonry.setName("Masonry - " + contractorName);
        masonry.setContractor(contractorName);
        masonry.setUser(user);
        masonry.persist();
        user.setMasonry(masonry);

        // Coatings
        Coatings coatings = new Coatings();
        coatings.setId(UUID.randomUUID().toString());
        coatings.setName("Coatings - " + contractorName);
        coatings.setContractor(contractorName);
        coatings.setUser(user);
        coatings.persist();
        user.setCoatings(coatings);

        Roofing roofing = new Roofing();
        roofing.setId(UUID.randomUUID().toString());
        roofing.setName("Roofing - " + contractorName);
        roofing.setContractor(contractorName);
        roofing.setUser(user);
        roofing.persist();
        user.setRoofing(roofing);

        Finishing finishing = new Finishing();
        finishing.setId(UUID.randomUUID().toString());
        finishing.setName("Finishing - " + contractorName);
        finishing.setContractor(contractorName);
        finishing.setUser(user);
        finishing.persist();
        user.setFinishing(finishing); 

        TerrainPreparation terrainPreparation = new TerrainPreparation();
        terrainPreparation.setId(UUID.randomUUID().toString());
        terrainPreparation.setName("Terrain Preparation - " + contractorName);
        terrainPreparation.setContractor(contractorName);
        terrainPreparation.setUser(user);
        terrainPreparation.persist();
        user.setTerrainPreparation(terrainPreparation);

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
        return userRepository.findByIdOptional(id)
            .map(this::mapToDTO)
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
            ),
            user.getRole()
        );
    }
}