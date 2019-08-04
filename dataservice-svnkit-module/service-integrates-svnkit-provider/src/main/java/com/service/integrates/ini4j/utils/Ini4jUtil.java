package com.service.integrates.ini4j.utils;

import org.ini4j.Ini;
import org.ini4j.Profile;

import java.util.Map;

public class Ini4jUtil {
    /**
     * 添加配置信息
     *
     * @param ini     配置文件
     * @param section 节
     * @param key     键
     * @param value   值
     * @return
     */
    public static Ini addConfig(Ini ini, String section, String key, String value) {
        Profile.Section sec;
        if (!ini.keySet().contains(section)) {
            sec = ini.add(section);
        } else {
            sec = ini.get(section);
        }
        sec.put(key, value);
        return ini;
    }

    /**
     * 添加配置信息
     *
     * @param ini     配置文件
     * @param section 节
     * @param values  配置
     * @return
     */
    public static Ini addConfigs(Ini ini, String section, Map<String, String> values) {
        Profile.Section sec;
        if (!ini.keySet().contains(section)) {
            sec = ini.add(section);
        } else {
            sec = ini.get(section);
        }
        sec.putAll(values);
        return ini;
    }
}
