package com.tp.license;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class LicenseController {

    @GetMapping(""/*여기 주소로 오는 사용자에게 자격증 목록을 보여줌*/)
    public List<License> showLicense() {
        return LicenseService.showLicense();
    }
}

