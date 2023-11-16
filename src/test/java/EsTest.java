import com.es.demo.EsDemoApplication;
import com.es.demo.entity.UserDO;
import com.es.demo.repo.UserSearchRepo;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@SpringBootTest(classes = EsDemoApplication.class)
public class EsTest {
    @Autowired
    private UserSearchRepo userSearchRepo;
    @Autowired
    protected ElasticsearchOperations elasticsearchOperations;
    @Test
    public void saveTest(){
        for (int i = 0; i < 100; i++) {
            UserDO userDO = new UserDO();
            userDO.setId((long) i);
            userDO.setName("占友"+i);
            userDO.setAge(i);
            userDO.setDesc("hello.");
            UserDO save = userSearchRepo.save(userDO);
            System.out.println(save);
        }
    }
    @Test
    public void queryTest(){
        Optional<UserDO> optional = userSearchRepo.findById(1L);
        System.out.println(optional.orElse(null));
    }
    @Test
    public void countTest(){
        Query query = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).build();
        long count = elasticsearchOperations.count(query, UserDO.class);
        System.out.println(count);
    }
    @Test
    public void pageTest(){
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery(UserDO.NAME,"占友"));
        boolQuery.must(QueryBuilders.termQuery(UserDO.AGE,10));
        Query query = new NativeSearchQueryBuilder().withQuery(boolQuery).build();
        SearchHits<UserDO> result = elasticsearchOperations.search(query, UserDO.class);
        List<SearchHit<UserDO>> searchHits = result.getSearchHits();
        for (int i = 0; i < searchHits.size(); i++) {
            UserDO content = searchHits.get(i).getContent();
            System.out.println(content);
        }

    }
}
