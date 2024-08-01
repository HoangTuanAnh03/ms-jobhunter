package com.tuananh.jobservice.service.impl;

import com.tuananh.jobservice.advice.exception.PermissionException;
import com.tuananh.jobservice.advice.exception.ResourceNotFoundException;
import com.tuananh.jobservice.dto.mapper.JobMapper;
import com.tuananh.jobservice.dto.request.CreateJobRequest;
import com.tuananh.jobservice.dto.request.UpdateJobRequest;
import com.tuananh.jobservice.dto.response.Company;
import com.tuananh.jobservice.dto.response.JobResponse;
import com.tuananh.jobservice.dto.response.ResultPaginationDTO;
import com.tuananh.jobservice.entity.Job;
import com.tuananh.jobservice.repository.JobRepository;
import com.tuananh.jobservice.repository.SkillRepository;
import com.tuananh.jobservice.service.JobService;
import com.tuananh.jobservice.service.client.AuthClient;
import com.tuananh.jobservice.service.client.CompanyClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobServiceImpl implements JobService {
    JobRepository jobRepository;
    SkillRepository skillRepository;
    JobMapper jobMapper;
    AuthClient authClient;
    CompanyClient companyClient;

    /**
     * @param id - jobId
     * @return Job Object on a given skillId
     */
    @Override
    public Job fetchById(long id) {
        return jobRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("SKILL", "skillId", id));
    }

    /**
     * @param spec     - filter
     * @param pageable - page, size, sort(field,asc(desc))
     * @return ResultPaginationDTO based on a given spec and pageable
     */
    @Override
    public ResultPaginationDTO fetchPagination(Specification<Job> spec, Pageable pageable) {
        Page<Job> pJob = this.jobRepository.findAll(spec, pageable);

        var mt = ResultPaginationDTO.Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(pJob.getTotalPages())
                .total(pJob.getTotalElements())
                .build();

        return ResultPaginationDTO.builder()
                .meta(mt)
                .result(pJob.getContent())
                .build();
    }

    /**
     * @param jobRequest - CreateJobRequest Object
     * @return Job Object saved to database
     */
    @Override
    public Job create(CreateJobRequest jobRequest) {
        var myInfo = authClient.getInfo().getData();

        Job job = Job.builder()
                .name(jobRequest.getName())
                .location(jobRequest.getLocation())
                .salary(jobRequest.getSalary())
                .quantity(jobRequest.getQuantity())
                .level(jobRequest.getLevel())
                .description(jobRequest.getDescription())
                .startDate(jobRequest.getStartDate())
                .endDate(jobRequest.getEndDate())
                .active(true)
                .companyId(myInfo.getCompany().getId())
                .skills(skillRepository.findByIdIn(jobRequest.getSkills()))
                .build();

        return jobRepository.save(job);
    }

    /**
     * @param jobRequest -UpdateJobRequest Object
     * @return Job Object updated to database
     */
    @Override
    public Job update(long id, UpdateJobRequest jobRequest) throws PermissionException {
        Job job = jobRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("JOB", "jobId", id));

        var myInfo = authClient.getInfo().getData();

        if (job.getCompanyId() != myInfo.getCompany().getId()) {
            throw new PermissionException("Forbidden");
        }

        job.setName(jobRequest.getName());
        job.setLocation(jobRequest.getLocation());
        job.setSalary(jobRequest.getSalary());
        job.setQuantity(jobRequest.getQuantity());
        job.setLevel(jobRequest.getLevel());
        job.setDescription(jobRequest.getDescription());
        job.setStartDate(jobRequest.getStartDate());
        job.setEndDate(jobRequest.getEndDate());
        job.setSkills(skillRepository.findByIdIn(jobRequest.getSkills()));

        return jobRepository.save(job);
    }

    /**
     * @param id - Input jobId
     * @return boolean indicating if the delete of Job details is successful or not
     */
    @Override
    public boolean delete(long id) throws PermissionException {
        Job job = jobRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("JOB", "jobId", id));

        var myInfo = authClient.getInfo().getData();

        if (job.getCompanyId() != myInfo.getCompany().getId()) {
            throw new PermissionException("Forbidden");
        }

        jobRepository.deleteById(job.getId());
        return true;
    }

    /**
     * @param listId
     * @return
     */
    @Override
    public List<JobResponse> fetchByIdIn(Set<Long> listId) {
        List<Job> jobs = jobRepository.findByIdIn(listId);



        List<Company> companies = companyClient.fetchByIdIn(jobs.stream().map(Job::getCompanyId).toList()).getData();



        return jobs.stream()
                .map(j -> JobResponse.builder()
                        .id(j.getId())
                        .name(j.getName())
                        .location(j.getLocation())
                        .salary(j.getSalary())
                        .level(j.getLevel())
                        .description(j.getDescription())
                        .startDate(j.getStartDate())
                        .endDate(j.getEndDate())
                        .active(j.isActive())
                        .company(companies.stream().filter(c -> c.getId() == j.getCompanyId()).findFirst().get())
                        .skills(j.getSkills())
                        .build()).toList();
    }

}
