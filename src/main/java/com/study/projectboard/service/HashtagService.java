package com.study.projectboard.service;

import com.study.projectboard.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    public Object parseHashtagNames(String content) {
        return null;
    }

    public Object findHashtagsByNames(Set<String> expectedHashtagNames) {
        return null;
    }

    public void deleteHashtagWithoutArticles(Object any) {
    }
}
