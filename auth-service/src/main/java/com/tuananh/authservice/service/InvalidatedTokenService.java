package com.tuananh.authservice.service;

import com.tuananh.authservice.entity.InvalidatedToken;
import org.springframework.stereotype.Service;

public interface InvalidatedTokenService {

     /**
      * @param invalidatedToken - Input InvalidatedToken Object
      */
     void createInvalidatedToken(InvalidatedToken invalidatedToken);

     /**
      * @param id - Input invalidatedTokenId
      * @return boolean indicating if the id already exited or not
      */
     boolean existById(String id);
}
