package com.tp.license;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    public List<License> showLicense() {
        return licenseRepository.findAll(); //저장된 데이터들 반환
    }
}



