package edu.tcu.cs.frogcrew.user;

import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FrogCrewUserService implements UserDetailsService {

    private final FrogCrewUserRepository frogCrewUserRepository;
    private final PasswordEncoder passwordEncoder;

    public FrogCrewUserService(FrogCrewUserRepository frogCrewUserRepository,
                               PasswordEncoder passwordEncoder) {
        this.frogCrewUserRepository = frogCrewUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public FrogCrewUser findById(Integer id) {
        return frogCrewUserRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("user", id));
    }

    public FrogCrewUser findByUsername(String username) {
        return frogCrewUserRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    public FrogCrewUser addCrewMember(FrogCrewUser frogCrewUser) {
        frogCrewUser.setPassword(passwordEncoder.encode(frogCrewUser.getPassword()));
        frogCrewUser.setEnabled(true);
        System.out.println("REGISTERED USER: " +
            frogCrewUser.getUsername() +
            ", password: " +
            frogCrewUser.getPassword() +
            ", enabled: " +
            frogCrewUser.isEnabled()
        );
        return frogCrewUserRepository.save(frogCrewUser);
    }
    public List<FrogCrewUser> findByPosition(String position) {
        return frogCrewUserRepository
                .findFrogCrewUsersByQualifiedPositionsContainingIgnoreCase(position);
    }
    

    public List<FrogCrewUser> findAll() {
        return frogCrewUserRepository.findAll();
    }
    

    public FrogCrewUser updateCrewMember(Integer userId, FrogCrewUser frogCrewUser) {
        return frogCrewUserRepository.findById(userId)
            .map(oldUser -> {
                oldUser.setFirstName(frogCrewUser.getFirstName());
                oldUser.setLastName(frogCrewUser.getLastName());
                oldUser.setEmail(frogCrewUser.getEmail());
                oldUser.setPhoneNumber(frogCrewUser.getPhoneNumber());
                oldUser.setRole(frogCrewUser.getRole());
                oldUser.setQualifiedPositions(frogCrewUser.getQualifiedPositions());
                return frogCrewUserRepository.save(oldUser);
            })
            .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        System.out.println("LOGIN ATTEMPT FOR USERNAME: " + username);
        return frogCrewUserRepository.findByUsername(username)
            .map(user -> {
                System.out.println("FOUND USER: " + user.getUsername());
                return new MyUserPrincipal(user);
            })
            .orElseThrow(() -> new UsernameNotFoundException(username + " is not found"));
    }
    @Transactional
public void deleteCrewMember(Integer userId) {
    frogCrewUserRepository.deleteById(userId);
}
}
