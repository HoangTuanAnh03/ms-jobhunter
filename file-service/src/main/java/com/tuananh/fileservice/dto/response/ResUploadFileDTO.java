package com.tuananh.fileservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResUploadFileDTO {
    private List<String> fileName;
    private Instant uploadedAt;
}
