import com.clj.panda.dao.StudentDao;
import com.clj.panda.model.entity.test.TestStudent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class Test_1 extends AbstractJUnit4SpringContextTests {
    @Resource
    private StudentDao studentDao;

    @Test
    public void insertStudent(){
        TestStudent student = new TestStudent();
        student.setId("1");
        student.setName("小明");
        student.setAge(20);
        student.setRemark("备注");
        student.setCreationTime(new Date().getTime());
        try {
            studentDao.insertStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void common(){
        Map<String,Object> map = new HashMap<String,Object>();
    }

}
