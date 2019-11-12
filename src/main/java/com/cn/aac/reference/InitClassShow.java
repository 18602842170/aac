package com.cn.aac.reference;

/**
 * 类的初始化展示
 * @author Administrator
 *
 */
public class InitClassShow {
    /**
     * java类的生命周期就是指一个class文件从加载到卸载的全过程。
     * 类的完整生命周期包括7个部分：加载——验证——准备——解析——初始化——使用——卸载
     * 
     * 其中，验证——准备——解析  称为连接阶段，除了解析外，其他阶段是顺序发生的，
     * 而解析可以与这些阶段交叉进行，因为Java支持动态绑定（晚期绑定），需要运行时才能确定具体类型；在使用阶段实例化对象。
     * 
     * 类的初始化触发
     * 类的加载机制没有明确的触发条件，但是有5种情况下必须对类进行初始化，那么 加载——验证——准备 就必须在此之前完成了。
     *  1：通过new关键字实例化对象、读取或设置类的静态变量、调用类的静态方法。
     *  2：通过反射方式执行以上三种行为。
     *  3：初始化子类的时候，会触发父类的初始化。
     *  4：虚拟机启动时，初始化一个执行主类；（作为程序入口直接运行时（也就是直接调用main方法）。）
     *  5、使用jdk1.7的动态语言支持时，如果一个java.lang.invoke.MethodHandle实例最后的解析结果REF_getStatic、REF_putStatic、RE_invokeStatic的方法句柄，
     *  并且这个方法句柄对应的类没有进行初始化，则需要先触发其初始化。
     *  
     * 注意，有且只有五种情况必须对类进行初始化，这五种情况被称为“主动引用”，除了这五种情况，所有其他的类引用方式都不会触发类初始化，被称为“被动引用”。
     * 请看主动引用的示例代码：
     */
    public static void main(String[] args) throws Exception {
        /** 
         * 主动引用引起类的初始化的第四种情况就是运行InitClassShow的main方法时 
         * 导致InitClassShow初始化，这一点很好理解，就不特别演示了。 
         * 本代码演示了前三种情况，以下代码都会引起InitClass的初始化， 
         * 但由于初始化只会进行一次，运行时请将注解去掉，依次运行查看结果。 
         */
        
        //  主动引用引起类的初始化一: new对象、读取或设置类的静态变量、调用类的静态方法。  
        //        new InitClass();
        //        InitClass.a = "";
        //        String a = InitClass.a;
        //        InitClass.method();
        
        //  主动引用引起类的初始化二：通过反射实例化对象、读取或设置类的静态变量、调用类的静态方法。  
        //        Class cls = InitClass.class;
        //        cls.newInstance();
        
        //        Field f = cls.getDeclaredField("a");
        //        f.get(null);
        //        f.set(null, "s");
        
        //        Method md = cls.getDeclaredMethod("method");
        //        md.invoke(null, null);
        
        //  主动引用引起类的初始化三：实例化子类，引起父类初始化。  
        //  new SubInitClass();  
        
    }
}

class InitClass {
    
    static {
        System.out.println("初始化InitClass");
    }
    public static String a = null;
    
    public static void method() {
    }
}

class SubInitClass extends InitClass {
}
