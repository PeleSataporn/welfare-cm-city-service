package com.cm.welfarecmcity.api.user;

import com.cm.welfarecmcity.utils.ResponseDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResponseDataUtils responseDataUtils;

}
