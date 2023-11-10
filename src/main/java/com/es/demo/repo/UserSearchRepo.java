package com.es.demo.repo;

import com.es.demo.entity.UserDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@NoRepositoryBean
public interface UserSearchRepo extends ElasticsearchRepository<UserDO,Long> {
    long getCount();
}
