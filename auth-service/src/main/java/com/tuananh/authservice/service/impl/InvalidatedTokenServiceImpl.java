package com.tuananh.authservice.service.impl;

import com.tuananh.authservice.entity.InvalidatedToken;
import com.tuananh.authservice.repository.InvalidatedTokenRepository;
import com.tuananh.authservice.service.InvalidatedTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvalidatedTokenServiceImpl implements InvalidatedTokenService {
    InvalidatedTokenRepository invalidatedTokenRepository;

    /**
     * @param invalidatedToken - Input InvalidatedToken Object
     */
    @Override
    public void createInvalidatedToken(InvalidatedToken invalidatedToken){
        invalidatedTokenRepository.save(invalidatedToken);
    }

    /**
     * @param id - Input invalidatedTokenId
     * @return boolean indicating if the id already exited or not
     */
    @Override
    public boolean existById(String id){
        return invalidatedTokenRepository.existsById(id);
    }
}
