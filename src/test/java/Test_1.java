import com.clj.panda.dao.StudentDao;
import com.clj.panda.model.entity.test.TestStudent;
import com.clj.panda.task.TestJob3;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;

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
    public void testScheduler(){
        System.out.println("测试任务");
    }

    @Test
    public void testTask1() throws Exception{
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        JobDetail jobDetail = JobBuilder.newJob(TestJob3.class).withIdentity("testJob").build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("testTrigger").
                withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?")).forJob(jobDetail).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private void t1(){
        try {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(TestJob3.class).withIdentity("testJob","group1").build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("testTrigger").startNow().
                    withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?")).build();

//            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("testTrigger","group1").startNow().
//                withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();
            scheduler.scheduleJob(jobDetail,trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void common(){
        Map<String,Object> map = new HashMap<String,Object>();
    }

    public static void main(String[] args) throws Exception{
        Test_1 t1 = new Test_1();
        t1.t1();
    }

}
