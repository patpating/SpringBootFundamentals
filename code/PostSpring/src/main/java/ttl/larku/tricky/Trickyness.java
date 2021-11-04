package ttl.larku.tricky;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

interface Trick {
    public void doTrick();
}

//@Component
//@Primary
@Qualifier("us-east")
//@Profile("dev")
class Trick1 implements Trick {
    @Override
    public void doTrick() {
        System.out.println("Handstand");
    }
}

//@Component
@Qualifier("us-west")
@Order(5)
//@Profile("prod")
class Trick2 implements Trick {
    @Override
    public void doTrick() {
        System.out.println("Somersault");
    }
}

//@Component
@Qualifier("us-west")
@Order(1)
//@Profile("prod")
class Trick3 implements Trick {
    @Override
    public void doTrick() {
        System.out.println("Card Trick");
    }
}

@Component
class Circus
{
    @Autowired(required = false)
    private Trick trick;

//    @Autowired
//    @Qualifier("us-west")
    private List<Trick> tricks;

    public void startShow() {
        trick.doTrick();
//        tricks.forEach(trick ->  trick.doTrick());
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("dev");
        context.scan("ttl.larku.tricky");
        context.refresh();

        Circus circus = context.getBean("circus", Circus.class);
        circus.startShow();
    }
}