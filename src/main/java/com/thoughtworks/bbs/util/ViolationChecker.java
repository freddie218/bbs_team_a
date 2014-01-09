package com.thoughtworks.bbs.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViolationChecker {
    private Set<String> violationWords = new LinkedHashSet<String>();

    private static class SingletonHolder {
        private static final ViolationChecker INSTANCE = new ViolationChecker();
    }

    public void init() {
        Properties vocabulary = new Properties();
        InputStream propertiesInput = this.getClass().getClassLoader().getResourceAsStream("illegalwords.properties");
        try {
            if(null != propertiesInput)
                vocabulary.load(propertiesInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Object key : vocabulary.keySet()) {
            String words = vocabulary.getProperty((String)key);
            Collections.addAll(violationWords, words.split("\\|"));
        }
    }

    public Set<String> getViolationWords() {
        return violationWords;
    }

    public boolean contentLegal(String content) {
        StringBuilder reg = new StringBuilder();
        for (Object word : violationWords.toArray()) {
            reg.append(word).append("|");
        }
        if(reg.length() > 1)
            reg.deleteCharAt(reg.length() - 1);
        Pattern pattern = Pattern.compile(reg.toString());
        Matcher matcher = pattern.matcher(content);
        return !matcher.find();
    }

    public static ViolationChecker getInstance() {
        ViolationChecker checker = SingletonHolder.INSTANCE;
        checker.init();
        return checker;
    }
}
