package com.tp.license;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @GetMapping("/license"/*여기 주소로 오는 사용자에게 자격증 목록을 보여줌*/)
    public List<License> showLicense() { return licenseService.showLicense(); }
}

