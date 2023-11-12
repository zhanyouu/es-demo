import com.es.demo.EsDemoApplication;
import com.es.demo.entity.UserDO;
import com.es.demo.repo.UserSearchRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;

import java.util.Optional;

@SpringBootTest(classes = EsDemoApplication.class)
public class EsTest {
    @Autowired
    private UserSearchRepo userSearchRepo;
    @Test
    public void save(){
        UserDO userDO = new UserDO();
        userDO.setId(2L);
        userDO.setName("zhanyou");
        userDO.setAge(18);
        UserDO save = userSearchRepo.save(userDO);
        System.out.println(save);
    }
    @Test
    public void query(){
        Optional<UserDO> optional = userSearchRepo.findById(1L);
        System.out.println(optional.orElse(null));
    }
}
