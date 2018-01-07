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
我们知道AOP其实就是在不修改方法的前提下在前后加上一些内容,这个方法我们把它叫切点,内容叫切面.而使用JDK自带的动态代理生成代理类我们只需要通过InvocationHandler.invoke接口方法能轻而易举的把切点与切面的内容拼接在一起.而如何通过配置文件灵活的把不同切点与切面组合在一起就是我们要做的.

在invoke中我们在拦截链中获取拦截当前方法的切面方法,然后按照切面方法的type先后执行切面方法;
![](https://github.com/q137904872/logo/blob/master/logo/拼接.png)
AOP_ConnectionPoint 包含切面信息,与执行当前本身切面方法
![](https://github.com/q137904872/logo/blob/master/logo/连接点.png)

在配置中把切点与切面通过连接点关联在一起,通过数据关系上理解切点与切面是多对多的关系,但是这样并不方便我们实现,而我们可以把配置信息处理成 切点方法——需要加上哪些切面方法 这样一对多的关系的一个集合 Map<String,List<AOP_ConnectionPoint>> interceptorChain拦截链;
![](https://github.com/q137904872/logo/blob/master/logo/配置.png)
