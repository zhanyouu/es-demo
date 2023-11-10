package com.es.demo.repo.impl;

import com.es.demo.entity.UserDO;
import com.es.demo.repo.UserSearchRepo;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserSearchRepoImpl extends SimpleElasticsearchRepository<UserDO,Long> implements UserSearchRepo {

    public UserSearchRepoImpl(ElasticsearchEntityInformation<UserDO, Long> metadata, ElasticsearchOperations operations) {
        super(metadata, operations);
    }

    @Override
    public long getCount() {
        return 0;
    }
}
