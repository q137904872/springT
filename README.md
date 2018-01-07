# 帅比带你造轮子之IOC,AOP
###### qq:137904872
###### 装逼吹水甩锅交流群：53141769
目前只实现通过xml读取配置尚未使用注解，aop功能使用动态代理未使用cglib使用注意先实现接口.

##### 目录：
###### 1. 加载配置文件:
###### 2. IOC/DI:
* 类的实例化：
* 属性的注入：
###### 3. AOP:
* 动态代理实现
* 切面
* 连接点
#### 此讲解建议结合源码一起看，因不善于讲解只说明关键点,看懂这些关键点就可以自己实现一下;
### 1. 加载配置文件：
此部分功能是加载xml中的配置信息处理好放入实体类中供其他处使用;

实体类：

直接采取spring配置文件格式：
![](https://github.com/q137904872/logo/blob/master/logo/%60PK9AP9S%60B3M%5BX9Y1U3G~IR.png)

配置文件加载接口：
类实例化工厂会调用此接口的这几个方法获取处理好的配置信息；
![](https://github.com/q137904872/logo/blob/master/logo/xml加载接口.png)

解析xml就没什么好讲的dom,sax,dom4j?

### 2.  IOC/DI:
IOC（控制反转）让容器完成类的实例化并通过注入给我们使用；
##### * 类的实例化：
通过配置文件的配置信息将要实例化的类通过反射完成实例化并放入map供以后取用；
![](https://github.com/q137904872/logo/blob/master/logo/实例化调用.png)
![](https://github.com/q137904872/logo/blob/master/logo/实例化.png)

##### * 属性的注入：
通过配置文件的配置信息将需要注入的属性通过反射调用set方法注入属性；<br>
判断值注入还是对象注入;
![](https://github.com/q137904872/logo/blob/master/logo/属性注入调用.png)
取得要注入属性,通过set加属性名获取该属性set方法反射调用即可完成注入,若为值注入因从配置文件中获取的都是String所有要先进行转换;
![](https://github.com/q137904872/logo/blob/master/logo/反射调用set方法.png)

### 3. AOP:
##### * 动态代理实现（动态代理是jdk自带的没用过的朋友可以先去试一下）
我们都知道jdk原生的动态代理生成代理类是使用Proxy.newProxyInstance(被代理对象类加载器, 被代理对象的接口方法, 实现InvocationHandler接口的对象)方法;
![](https://github.com/q137904872/logo/blob/master/logo/生成代理类.png)
而InvocationHandler接口只有一个方法invoke(Object proxy/代理对象, Method method/被代理对象需被执行的方法, Object[] args/方法的参数),method参数就是被代理对象当前需被执行的方法,在它invoke之前后加上内容就是前后拦截;
![](https://github.com/q137904872/logo/blob/master/logo/invoke.png)
Proxy.newProxyInstance(被代理对象类加载器, 被代理对象的接口, 实现InvocationHandler接口的对象)返回的代理类是使用字节码编辑技术编辑一个继承Proxy类 实现被代理类的接口(第二个参数)的.class实例化返回的对象(有兴趣的朋友可以反编译出来看下),如此代理与被代理类都实现一样的接口方法(so动态代理只能代理有接口的方法),而代理类的接口方法中都会调用InvocationHandler(第三个参数).invoke(Object proxy/代理类自身this, Method method/与当前方法名相同的被代理对象的接口方法(第二个参数), Object[] args/方法的参数),由此完成代理,而我们通过完成invoke方法的编写实现前后拦截;


