package ttl.larku.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@DependsOn("b")
class A {
    private B b;
//    public A() {}
//    public A(B b, @Autowired(required = false) Z z) {
//        this.b = b;
//        System.out.println("a class" + this.getClass() + ", b class: " + this.b.getA().getClass() + ", z: " + z);
//    }

    public void doA() {
        System.out.println("Doing A");
    }

    public void callB() {
        b.doB();
    }

//    @Autowired
    public void setB(B b) {
        this.b = b;
    }
}

@Component
class Z
{}

@Component
class B {
    private A a;
    //public B() {}
    public B(@Lazy A a) {
        this.a = a;
        System.out.println(this.a.getClass());
    }

    public void callA() {
        a.doA();
        System.out.println("In callA " + this.a.getClass());
    }

    public void doB() {
        System.out.println("Doing B");
    }

    public A getA() {
        return a;
    }
//    @Autowired
    public void setA(A a) {
        this.a = a;
    }
}

class DriverJava
{
    public static void main(String[] args) {
//        B b  = new B();
//        A a = new A();
//        b.setA(a);
//        a.setB(b);
    }
}

class CircularDriver
{

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("ttl.larku.app");
        context.refresh();

        B b = context.getBean("b", B.class);
        b.callA();

        A a = context.getBean("a", A.class);
        a.callB();
    }
}
