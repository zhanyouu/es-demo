import com.es.demo.EsDemoApplication;
import com.es.demo.constant.SchoolStatus;
import com.es.demo.entity.UserDO;
import com.es.demo.repo.UserSearchRepo;
import org.elasticsearch.common.time.DateUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.BaseQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.util.unit.DataUnit;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@SpringBootTest(classes = EsDemoApplication.class)
public class EsTest {
    @Autowired
    private UserSearchRepo userSearchRepo;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void saveTest() {
        for (int i = 0; i < 100; i++) {
            UserDO userDO = new UserDO();
            userDO.setId((long) i);
            String name = "";
            if(i%5 == 0){
                name = "占友";
            }else if(i%5 == 1){
                name = "张三";
            }else if(i%5 == 2){
                name = "李四";
            }else if(i%5 == 3){
                name = "王五";
            }else if(i%5 ==4){
                name ="赵六";
            }
            userDO.setName(name);
            userDO.setAge(i);
            userDO.setEmail(new Random().nextInt(1000000) +"@qq.com");
            userDO.setBirthDay(new Date());
            userDO.setDesc("占友是一个学生，并且很喜欢学习！");
            SchoolStatus schoolStatus;
            if(i%2==0){
                schoolStatus = SchoolStatus.IN;
            }else {
                schoolStatus = SchoolStatus.OUT;
            }
            userDO.setStatus(schoolStatus.name());
            Boolean deleted;
            if(i%10==0){
                deleted = true;
            }else {
                deleted = false;
            }
            userDO.setDeleted(deleted);
            UserDO save = userSearchRepo.save(userDO);
            System.out.println(save);
        }
    }

    @Test
    public void queryTest() {
        Optional<UserDO> optional = userSearchRepo.findById(1L);
        System.out.println(optional.orElse(null));
    }

    @Test
    public void countTest() {
        Query query = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).build();
        long count = elasticsearchRestTemplate.count(query, UserDO.class);
        System.out.println(count);
    }

    /**
     * 分页查询
     */
    @Test
    public void pageTest() {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //根据词条精确查询，should非必须满足
        boolQuery.should(QueryBuilders.termQuery(UserDO.NAME, "占友"));
        boolQuery.should(QueryBuilders.termQuery(UserDO.NAME, "张三"));
        boolQuery.minimumShouldMatch(1);//最少满足一个条件

        //范围检索,must必须满足
        boolQuery.must(QueryBuilders.termQuery(UserDO.STATUS,SchoolStatus.IN.name()));
        boolQuery.must(QueryBuilders.termQuery(UserDO.DELETED,false));
        //全文检索
        boolQuery.filter(QueryBuilders.rangeQuery(UserDO.AGE).from(1).to(50));
        boolQuery.filter(QueryBuilders.matchQuery(UserDO.DESC, "学生"));
        BaseQuery query = new NativeSearchQueryBuilder().withQuery(boolQuery)
                .withPageable(PageRequest.of(0, 20, Sort.by(UserDO.AGE)))
                .build();
        SearchHits<UserDO> result = elasticsearchRestTemplate.search(query, UserDO.class);
        List<SearchHit<UserDO>> searchHits = result.getSearchHits();
        for (int i = 0; i < searchHits.size(); i++) {
            UserDO content = searchHits.get(i).getContent();
            System.out.println(content);
        }
    }

    /**
     * 滚动查询
     */
    @Test
    public void scrollTest() {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //文本检索
        boolQuery.must(QueryBuilders.matchQuery(UserDO.NAME, "占友"));
        //范围检索
        boolQuery.must(QueryBuilders.rangeQuery(UserDO.AGE).from(1).to(101));
        BaseQuery query = new NativeSearchQueryBuilder().withQuery(boolQuery)
                .withPageable(PageRequest.of(0, 10, Sort.by(UserDO.AGE)))
                .build();
        SearchScrollHits<UserDO> searchScrollHits = elasticsearchRestTemplate.searchScrollStart(UserDO.TIME_OUT, query,
                UserDO.class, IndexCoordinates.of(UserDO.INDEX_NAME));
        while (searchScrollHits.hasSearchHits()) {
            List<SearchHit<UserDO>> searchHit = searchScrollHits.getSearchHits();
            printUserDOs(searchHit);
            System.out.println(searchScrollHits.getScrollId());
            searchScrollHits = elasticsearchRestTemplate.searchScrollContinue(searchScrollHits.getScrollId(),
                    UserDO.TIME_OUT, UserDO.class, IndexCoordinates.of(UserDO.INDEX_NAME));
        }
    }

    private void printUserDOs(List<SearchHit<UserDO>> searchHit) {
        for (int i = 0; i < searchHit.size(); i++) {
            UserDO content = searchHit.get(i).getContent();
            System.out.println(content);
        }
    }
}
