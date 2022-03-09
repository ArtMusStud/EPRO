package com.epro.ws2122;

import com.epro.ws2122.config.WebSecurityConfig;
import com.epro.ws2122.domain.BusinessUnitKeyResult;
import com.epro.ws2122.domain.BusinessUnitObjective;
import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.domain.userRoles.Role;
import com.epro.ws2122.domain.userRoles.User;
import com.epro.ws2122.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
@Profile("!test")
public class StartupDataCommandLineRunner implements CommandLineRunner {

    private final BusinessUnitKeyResultRepository bukrRepository;
    private final BusinessUnitObjectiveRepository buoRepository;
    private final CompanyKeyResultRepository ckrRepository;
    private final CompanyObjectiveRepository coRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        bukrRepository.deleteAll();
        buoRepository.deleteAll();
        ckrRepository.deleteAll();
        coRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();


        Role readOnlyRole = new Role();
        readOnlyRole.setName(WebSecurityConfig.ROLE_PREFIX + WebSecurityConfig.READ_ONLY_USER);
        Role coRole = new Role();
        coRole.setName(WebSecurityConfig.ROLE_PREFIX + WebSecurityConfig.CO_OKR_ADMIN);
        Role buoRole = new Role();
        buoRole.setName(WebSecurityConfig.ROLE_PREFIX + WebSecurityConfig.BUO_OKR_ADMIN);

        User readOnlyUser = new User();
        readOnlyUser.setUsername("readonly");
        readOnlyUser.setPassword(encoder.encode("pw"));
        readOnlyUser.setRole(readOnlyRole);
        User coAdmin = new User();
        coAdmin.setUsername("coadmin");
        coAdmin.setPassword(encoder.encode("copw"));
        coAdmin.setRole(coRole);
        User buoAdmin1 = new User();
        buoAdmin1.setUsername("buoadmin1");
        buoAdmin1.setPassword(encoder.encode("buopw"));
        buoAdmin1.setRole(buoRole);
        User buoAdmin2 = new User();
        buoAdmin2.setUsername("buoadmin2");
        buoAdmin2.setPassword(encoder.encode("buopw"));
        buoAdmin2.setRole(buoRole);
        roleRepository.saveAll(List.of(readOnlyRole, coRole, buoRole));
        userRepository.saveAll(List.of(readOnlyUser, coAdmin, buoAdmin1, buoAdmin2));


        var co = CompanyObjective.builder()
                .name("Company Objective 0")
                .startDate(LocalDate.of(2022, 1, 1))
                .build();

        var ckr = CompanyKeyResult.builder()
                .name("Company Key Result 0")
                .confidence(100)
                .current(99)
                .goal(100)
                .companyObjective(co)
                .build();

        var buo = BusinessUnitObjective.builder()
                .startDate(LocalDate.of(2022, 2, 1))
                .name("Business Unit Objective 0")
                .build();

        var bukr = BusinessUnitKeyResult.builder()
                .name("Business Unit Key Result 0")
                .current(0)
                .goal(100)
                .confidence(20)
                .businessUnitObjective(buo)
                .owner(buoAdmin1)
                .build();

        coRepository.save(co);
        ckrRepository.save(ckr);
        buoRepository.save(buo);
        bukrRepository.save(bukr);
    }
}
