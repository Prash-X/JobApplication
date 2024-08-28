package com.prashant.jobms.job.mapper;

import com.prashant.jobms.job.Jobs;
import com.prashant.jobms.job.dto.JobDTO;
import com.prashant.jobms.job.external.Company;
import com.prashant.jobms.job.external.Review;

import java.util.List;

public class JobMapper {

    public static JobDTO mapToJobWithCompanyDto(
            Jobs job, Company company, List<Review> reviews){

        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(job.getId());
        jobDTO.setTitle(job.getTitle());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setMaxSalary(job.getMaxSalary());
        jobDTO.setMinSalary(job.getMinSalary());
        jobDTO.setCompany(company);
        jobDTO.setReview(reviews);

        return jobDTO;

    }
}
