package com.epro.ws2122;

import com.epro.ws2122.domain.BusinessUnitKeyResult;
import com.epro.ws2122.domain.BusinessUnitObjective;
import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.repository.BusinessUnitKeyResultRepository;
import com.epro.ws2122.repository.BusinessUnitObjectiveRepository;
import com.epro.ws2122.repository.CompanyKeyResultRepository;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("!test")
public class StartupDataCommandLineRunner implements CommandLineRunner {

    BusinessUnitKeyResultRepository bukrRepository;
    BusinessUnitObjectiveRepository buoRepository;
    CompanyKeyResultRepository ckrRepository;
    CompanyObjectiveRepository coRepository;

    public StartupDataCommandLineRunner(BusinessUnitKeyResultRepository bukrRepository,
                                        BusinessUnitObjectiveRepository buoRepository,
                                        CompanyKeyResultRepository ckrRepository,
                                        CompanyObjectiveRepository coRepository) {
        this.bukrRepository = bukrRepository;
        this.buoRepository = buoRepository;
        this.ckrRepository = ckrRepository;
        this.coRepository = coRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        bukrRepository.deleteAll();
        buoRepository.deleteAll();
        ckrRepository.deleteAll();
        coRepository.deleteAll();

        var co = new CompanyObjective().builder()
                .name("Company Objective 0")
                .startDate(LocalDate.of(2022, 1, 1))
                .build();

        var ckr = new CompanyKeyResult().builder()
                .name("Company Key Result 0")
                .confidence(100)
                .current(99)
                .goal(100)
                .companyObjective(co)
                .build();

        var buo = new BusinessUnitObjective();
        buo.setStartDate(LocalDate.of(2022, 2, 1));
        buo.setName("Business Unit Objective 0");

        var bukr = new BusinessUnitKeyResult();
        bukr.setName("Business Unit Key Result 0");
        bukr.setCurrent(0);
        bukr.setGoal(100);
        bukr.setConfidence(20);
        bukr.setBusinessUnitObjective(buo);

        coRepository.save(co);
        ckrRepository.save(ckr);
        buoRepository.save(buo);
        bukrRepository.save(bukr);
    }
}
