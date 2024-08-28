package com.prashant.jobms.job.impl;


import com.prashant.jobms.job.JobRepository;
import com.prashant.jobms.job.JobService;
import com.prashant.jobms.job.Jobs;
import com.prashant.jobms.job.clients.CompanyClient;
import com.prashant.jobms.job.clients.ReviewClient;
import com.prashant.jobms.job.dto.JobDTO;
import com.prashant.jobms.job.external.Company;
import com.prashant.jobms.job.external.Review;
import com.prashant.jobms.job.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {
    //private List<Jobs> jobs = new ArrayList<>();
    JobRepository jobRepository;

    @Autowired
    RestTemplate restTemplate; //helps to call microservices from eureka cloud

    private CompanyClient companyClient;

    private ReviewClient reviewClient;
    @Autowired
    public JobServiceImpl(JobRepository jobRepository, CompanyClient companyClient, ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

    @Override
    public List<JobDTO> findAll() {

        List<Jobs> jobs = jobRepository.findAll();
        List<JobDTO> jobDTOS = new ArrayList<>();
//        RestTemplate restTemplate = new RestTemplate();

        for(Jobs job: jobs){
            //Using RestTemplate
/*            Company company = restTemplate.getForObject(
                    "http://COMPANY-SERVICE:8081/companies/"+job.getCompanyId(),
                    Company.class);*/

            //Using OpenFeign
            Company company = companyClient.getCompany(job.getCompanyId());

            //Using RestTemplate
            /*ResponseEntity<List<Review>> reviewResponse= restTemplate.exchange("http://REVIEW-SERVICE:8083/reviews?companyId=" + job.getCompanyId(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Review>>() {
                    });
                 List<Review> reviews = reviewResponse.getBody();*/

            //Using OpenFeign
            List<Review> reviews = reviewClient.getReviews(job.getCompanyId());
            JobDTO jobDTO = JobMapper.mapToJobWithCompanyDto(job,company,reviews);
            jobDTOS.add(jobDTO);
        }
        return jobDTOS;
    }

    @Override
    public void createJob(Jobs job) {
       jobRepository.save(job);
    }

    @Override
    public JobDTO getJobById(Long id) {
      /*for(Jobs jobs1: jobs){
          if(jobs1.getId().equals(id)){
              return jobs1;
          }
      }
      return null;*/
        //as findById return optional
        Jobs jobs = jobRepository.findById(id).orElse(null);


        Company company = restTemplate.getForObject(
                "http://COMPANY-SERVICE:8081/companies/"+jobs.getCompanyId(),
                Company.class);

        //if the response type is of generic collection we use restTemplate.exchange() method
        ResponseEntity<List<Review>> reviewResponse= restTemplate.exchange("http://REVIEW-SERVICE:8083/reviews?companyId=" + jobs.getCompanyId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Review>>() {
                });
        List<Review> reviews = reviewResponse.getBody();

        JobDTO jobDTO = JobMapper.mapToJobWithCompanyDto(jobs,company,reviews);
        //jobDTO.setCompany(company);
        return jobDTO;
    }

    @Override
    public boolean deleteJobById(Long id) {
       /* Iterator<Jobs> iterator = jobs.iterator();
        while(iterator.hasNext()){
            Jobs jobs1 = iterator.next();
            if(jobs1.getId().equals(id)){
                iterator.remove();
                return true;
            }
        }*/
        try{
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean updateJobById(Long id, Jobs updatedJob) {
        /*for(Jobs j : jobs){
            if(j.getId().equals(id)){
                j.setDescription(updatedJob.getDescription());
                j.setLocation(updatedJob.getLocation());
                j.setTitle(updatedJob.getTitle());
                j.setMinSalary(updatedJob.getMinSalary());
                return true;
            }
        }
        return false;*/
        Optional<Jobs> jobsOptional = jobRepository.findById(id);
        if(jobsOptional.isPresent()){
            Jobs jobs = jobsOptional.get();
            jobs.setTitle(updatedJob.getTitle());
            jobs.setDescription(updatedJob.getDescription());
            jobs.setMinSalary(updatedJob.getMinSalary());
            jobs.setLocation(updatedJob.getLocation());
            jobRepository.save(jobs);
            return true;
        }
        return false;
    }
}
