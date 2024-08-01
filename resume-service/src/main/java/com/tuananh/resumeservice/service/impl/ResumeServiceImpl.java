package com.tuananh.resumeservice.service.impl;

import com.tuananh.resumeservice.advice.exception.PermissionException;
import com.tuananh.resumeservice.advice.exception.ResourceNotFoundException;
import com.tuananh.resumeservice.dto.mapper.ResumeMapper;
import com.tuananh.resumeservice.dto.request.CreateResumeRequest;
import com.tuananh.resumeservice.dto.request.UpdateResumeRequest;
import com.tuananh.resumeservice.dto.response.*;
import com.tuananh.resumeservice.entity.Job;
import com.tuananh.resumeservice.entity.Resume;
import com.tuananh.resumeservice.repository.ResumeRepository;
import com.tuananh.resumeservice.service.ResumeService;
import com.tuananh.resumeservice.service.client.AuthClient;
import com.tuananh.resumeservice.service.client.CompanyClient;
import com.tuananh.resumeservice.service.client.JobClient;
import com.tuananh.resumeservice.util.constant.ResumeStateEnum;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResumeServiceImpl implements ResumeService {
    ResumeRepository resumeRepository;
    ResumeMapper resumeMapper;
    AuthClient authClient;
    @Qualifier("com.tuananh.resumeservice.service.client.JobClient")
    JobClient jobClient;
    CompanyClient companyClient;

    FilterBuilder fb;
    FilterParser filterParser;
    FilterSpecificationConverter filterSpecificationConverter;


    /**
     * @param id - jobId
     * @return Resume Object on a given skillId
     */
//    @Override
//    public Job fetchById(long id) {
//        return resumeRepository.findById(id).orElseThrow(
//                () -> new ResourceNotFoundException("SKILL", "skillId", id));
//    }

    /**
     * @param pageable - page, size, sort(field,asc(desc))
     * @return ResultPaginationDTO based on a given spec and pageable
     */
    @Override
    public ResultPaginationDTO fetchMyResume(String id, Pageable pageable) {
        FilterNode node = filterParser.parse("userId='" + id + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);

        Set<Long> jobIds = new HashSet<>();

        pageResume.stream().forEach(r -> jobIds.add(r.getJobId()));
        List<JobResponse> jobs = jobClient.fetchByIdIn(jobIds).getData();

        List<MyResumeResponse> result = pageResume.stream().map(resume -> {
            JobResponse jobResponse = jobs.stream()
                    .filter(job -> job.getId() == resume.getJobId())
                    .findFirst().get();

            return MyResumeResponse.builder()
                    .id(resume.getJobId())
                    .status(resume.getStatus())
                    .email(resume.getEmail())
                    .url(resume.getUrl())

                    .jobInfo(JobInfo.builder()
                            .id(jobResponse.getId())
                            .name(jobResponse.getName())
                            .build())

                    .companyInfo(CompanyInfo.builder()
                            .id(jobResponse.getCompany().getId())
                            .name(jobResponse.getCompany().getName())
                            .build())

                    .build();
        }).toList();

        var mt = ResultPaginationDTO.Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(pageResume.getTotalPages())
                .total(pageResume.getTotalElements())
                .build();

        return ResultPaginationDTO.builder()
                .meta(mt)
                .result(result)
                .build();
    }

    /**
     * @param resumeRequest - CreateResumeRequest Object
     * @return ResumeResponse Object
     */
    @Override
    public ResumeResponse create(CreateResumeRequest resumeRequest) {
        var myInfo = authClient.getInfo().getData();
        var jobInfo = jobClient.getById(resumeRequest.getJobId()).getData();

        Resume resume = Resume.builder()
                .url(resumeRequest.getUrl())
                .email(myInfo.getEmail())
                .status(ResumeStateEnum.PENDING)
                .userId(myInfo.getId())
                .jobId(resumeRequest.getJobId())
                .build();

        resumeRepository.save(resume);

        return resumeMapper.toResumeResponse(resume, myInfo, jobInfo);
    }

    /**
     * @param resumeRequest - UpdateResumeRequest Object
     * @return ResumeResponse Object
     */
    @Override
    public ResumeResponse update(long id, UpdateResumeRequest resumeRequest) {
        Resume resume = resumeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("RESUME", "resumeId", id));

        var myInfo = authClient.getInfo().getData();
        var jobInfo = jobClient.getById(resume.getJobId()).getData();

        resume.setUrl(resumeRequest.getUrl());

        resumeRepository.save(resume);

        return resumeMapper.toResumeResponse(resume, myInfo, jobInfo);
    }

    /**
     * @param id - Input resumeId
     * @return boolean indicating if the delete of Resume details is successful or not
     */
    @Override
    public boolean delete(long id) {
        Resume resume = resumeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("RESUME", "resumeId", id));

        resumeRepository.deleteById(resume.getId());
        return true;
    }

    /**
     * @param jobId - Input
     * @param pageable - page, size, sort(field,asc(desc))
     * @return - List ResumeResponse Object
     */
    @Override
    public ResultPaginationDTO fetchResumeByJob(long jobId, Pageable pageable) throws PermissionException {
        // Check if the companyId of jobId and user match
        Job job = jobClient.getById(jobId).getData();
        UserResponse user = authClient.getInfo().getData();

        if (job.getCompanyId() != user.getCompany().getId()) {
            throw new PermissionException("Forbidden");
        }

        FilterNode node = filterParser.parse("jobId='" + jobId + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);

        Set<String> userIds = new HashSet<>();

        pageResume.stream().forEach(r -> userIds.add(r.getUserId()));
        List<SimpInfoUserResponse> users = authClient.fetchUserByIdIn(userIds).getData();

        List<ResumeResponse> result = pageResume.stream().map(resume -> {
            SimpInfoUserResponse userResponse = users.stream()
                    .filter(userInfo -> Objects.equals(userInfo.getId(), resume.getUserId()))
                    .findFirst().get();

            return ResumeResponse.builder()
                    .id(resume.getJobId())
                    .status(resume.getStatus())
                    .email(resume.getEmail())
                    .url(resume.getUrl())
                    .userInfo(UserInfo.builder()
                            .id(userResponse.getId())
                            .name(userResponse.getName())
                            .email(userResponse.getEmail())
                            .build())
                    .build();
        }).toList();

        var mt = ResultPaginationDTO.Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(pageResume.getTotalPages())
                .total(pageResume.getTotalElements())
                .build();

        return ResultPaginationDTO.builder()
                .meta(mt)
                .result(result)
                .build();
    }
}
