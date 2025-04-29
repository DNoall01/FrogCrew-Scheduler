package edu.tcu.cs.frogcrew.user;

import edu.tcu.cs.frogcrew.system.Role;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FrogCrewUserServiceTest {

    @Mock
    FrogCrewUserRepository frogCrewUserRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    FrogCrewUserService frogCrewUserService;

    List<FrogCrewUser> frogCrewUsers;

    @BeforeEach
    void setUp() {
        FrogCrewUser u1 = new FrogCrewUser();
        u1.setId(1);
        u1.setFirstName("John");
        u1.setLastName("Smith");
        u1.setEmail("john.smith@gmail.com");
        u1.setEnabled(true);
        u1.setRole(Role.ADMIN);

        FrogCrewUser u2 = new FrogCrewUser();
        u2.setId(2);
        u2.setFirstName("Jane");
        u2.setLastName("Smith");
        u2.setEmail("jane.smith@gmail.com");
        u2.setEnabled(true);
        u2.setRole(Role.CREW);

        FrogCrewUser u3 = new FrogCrewUser();
        u3.setId(3);
        u3.setFirstName("Bob");
        u3.setLastName("Smith");
        u3.setEmail("bob.smith@gmail.com");
        u3.setEnabled(true);
        u3.setRole(Role.ADMIN);

        this.frogCrewUsers = new ArrayList<>();
        this.frogCrewUsers.add(u1);
        this.frogCrewUsers.add(u2);
        this.frogCrewUsers.add(u3);
    }

    @Test
    void testFindAllSuccess() {
        given(this.frogCrewUserRepository.findAll()).willReturn(this.frogCrewUsers);
        List<FrogCrewUser> result = this.frogCrewUserService.findAll();
        assertThat(result.size()).isEqualTo(this.frogCrewUsers.size());
        verify(this.frogCrewUserRepository).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        FrogCrewUser u = new FrogCrewUser();
        u.setId(1);
        u.setFirstName("John");
        u.setLastName("Smith");
        u.setEmail("john.smith@gmail.com");
        u.setEnabled(true);
        u.setRole(Role.ADMIN);

        given(this.frogCrewUserRepository.findById(1)).willReturn(Optional.of(u));

        FrogCrewUser found = this.frogCrewUserService.findById(1);

        assertThat(found.getId()).isEqualTo(1);
        assertThat(found.getFirstName()).isEqualTo("John");
        assertThat(found.getLastName()).isEqualTo("Smith");
        assertThat(found.getEmail()).isEqualTo("john.smith@gmail.com");
        assertThat(found.isEnabled()).isEqualTo(true);
        assertThat(found.getRole()).isEqualTo(Role.ADMIN);
        verify(this.frogCrewUserRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        given(this.frogCrewUserRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> this.frogCrewUserService.findById(1));

        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find user with id 1");
        verify(this.frogCrewUserRepository, times(1)).findById(1);
    }

    @Test
    void testAddCrewMemberSuccess() {
        FrogCrewUser u = new FrogCrewUser();
        u.setId(1);
        u.setFirstName("John");
        u.setLastName("Smith");
        u.setEmail("john.smith@gmail.com");
        u.setPhoneNumber("1234567890");
        u.setPassword("P@ssw0rd");
        u.setEnabled(true);
        u.setRole(Role.ADMIN);
        u.setQualifiedPositions(List.of("Director", "Producer"));

        //given(this.passwordEncoder.encode(u.getPassword())).willReturn("Encoded Password");
        given(this.frogCrewUserRepository.save(u)).willReturn(u);

        FrogCrewUser saved = this.frogCrewUserService.addCrewMember(u);

        assertThat(saved.getId()).isEqualTo(1);
        assertThat(saved.getFirstName()).isEqualTo("John");
        assertThat(saved.getLastName()).isEqualTo("Smith");
        assertThat(saved.getEmail()).isEqualTo("john.smith@gmail.com");
        assertThat(saved.getPhoneNumber()).isEqualTo("1234567890");
        assertThat(saved.isEnabled()).isEqualTo(true);
        assertThat(saved.getRole()).isEqualTo(Role.ADMIN);
        verify(this.frogCrewUserRepository, times(1)).save(u);
    }

    @Test
    void testUpdateSuccess() {
        FrogCrewUser oldUser = new FrogCrewUser();
        oldUser.setId(1);
        oldUser.setFirstName("John");
        oldUser.setLastName("Smith");
        oldUser.setEmail("john.smith@gmail.com");
        oldUser.setPhoneNumber("1234567890");
        oldUser.setPassword("P@ssw0rd");
        oldUser.setEnabled(true);
        oldUser.setRole(Role.ADMIN);
        oldUser.setQualifiedPositions(List.of("Director", "Producer"));

        FrogCrewUser update = new FrogCrewUser();
        update.setFirstName("John");
        update.setLastName("Doe");
        update.setEmail("john.doe@gmail.com");
        update.setPhoneNumber("0987654321");
        update.setPassword("P@ssw0rd");
        update.setEnabled(true);
        update.setRole(Role.CREW);
        update.setQualifiedPositions(List.of("Director", "Producer", "Camera-fixed"));

        given(this.frogCrewUserRepository.findById(1)).willReturn(Optional.of(oldUser));
        given(this.frogCrewUserRepository.save(oldUser)).willReturn(oldUser);

        FrogCrewUser updatedUser = this.frogCrewUserService.updateCrewMember(1, update);

        assertThat(updatedUser.getId()).isEqualTo(1);
        assertThat(updatedUser.getFirstName()).isEqualTo("John");
        assertThat(updatedUser.getLastName()).isEqualTo("Doe");
        assertThat(updatedUser.getEmail()).isEqualTo("john.doe@gmail.com");
        assertThat(updatedUser.getPhoneNumber()).isEqualTo("0987654321");
        verify(this.frogCrewUserRepository, times(1)).findById(1);
        verify(this.frogCrewUserRepository, times(1)).save(oldUser);
    }

    @Test
    void testUpdateNotFound() {
        FrogCrewUser update = new FrogCrewUser();
        update.setFirstName("John");
        update.setLastName("Smith");
        update.setEmail("john.smith@gmail.com");
        update.setPhoneNumber("1234567890");

        given(this.frogCrewUserRepository.findById(1)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> this.frogCrewUserService.updateCrewMember(1, update));

        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find user with id 1");
        verify(this.frogCrewUserRepository, times(1)).findById(1);
    }

}
