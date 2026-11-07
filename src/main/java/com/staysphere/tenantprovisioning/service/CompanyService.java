package com.staysphere.tenantprovisioning.service;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.staysphere.tenantprovisioning.dto.CompanyRegistrationRequest;
import com.staysphere.tenantprovisioning.entity.Company;
import com.staysphere.tenantprovisioning.exception.CompanyNotFoundException;
import com.staysphere.tenantprovisioning.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    private static final SecureRandom RANDOM = new SecureRandom();

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company registerCompany(CompanyRegistrationRequest request) {

        String companyCode = generateCompanyCode(
                request.getCompanyName()
        );

        Company company = new Company();

        company.setCompanyName(request.getCompanyName());
        company.setCompanyCode(companyCode);

        return companyRepository.save(company);
    }

    private String generateCompanyCode(String companyName) {

        // Extract first three letters
        String prefix = companyName
                .replaceAll("[^a-zA-Z]", "")
                .toUpperCase(Locale.ROOT);

        if (prefix.length() < 4) {
            throw new IllegalArgumentException(
                    "Company name must contain at least  letters"
            );
        }

        prefix = prefix.substring(0, 4);

        // Try generating a unique company code
        for (int i = 0; i < 20; i++) {

            int randomNumber = 1000 + RANDOM.nextInt(9000);

            String companyCode = prefix + randomNumber;

            if (!companyRepository.existsByCompanyCode(companyCode)) {
                return companyCode;
            }
        }

        throw new IllegalStateException(
                "Unable to generate a unique company code"
        );
    }

    public Company getCompanyById(Long id){
        Optional<Company> company = companyRepository.findById(id);
        return company.orElseThrow(()->new CompanyNotFoundException("Company with Id" + id + "Not Found"));
    }

}