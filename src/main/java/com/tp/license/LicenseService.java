package com.tp.license;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LicenseService {

    private static final Map<Long,License> store = new HashMap<>();

    public static List<License> showLicense() {
        return new ArrayList<>(store.values()); //저장된 데이터들을 꺼내서 반환
    }
}



