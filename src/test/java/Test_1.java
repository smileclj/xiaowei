import com.clj.panda.dao.test.TestStudentMapper;
import com.clj.panda.model.entity.test.TestStudent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class Test_1 extends AbstractJUnit4SpringContextTests {


    @Test
    public void query(){

    }

    @Test
    public void common(){
        Map<String,Object> map = new HashMap<String,Object>();
    }

}
