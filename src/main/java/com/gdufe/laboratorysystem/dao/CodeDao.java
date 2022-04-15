package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.VerificationCode;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeDao {

    //添加验证码
    int addCode(VerificationCode code);
}
