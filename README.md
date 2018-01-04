# 帅比带你造轮子之IOC,AOP
###### qq:137904872
###### 装逼吹水甩锅交流群：53141769
IOC,AOP经常见这样说那样说，其实当你去尝试了解实现原理时发现就是这么简单，伟大的不是实现而是理论、想法、点子，创新从造轮子开始；<br><br>
目前只实现通过xml读取配置尚未使用注解，aop功能使用动态代理未使用cglib使用注意先实现接口，有点low以后有空再完善；

##### 目录：
###### 1. 加载配置文件:
###### 2. IOC/DI:
* 类的实例化：
* 属性的注入：
###### 3. AOP:
* 动态代理
* 切面
* 连接点
#### 此讲解建议结合源码一起看，因不善于讲解只说明关键点；
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
![](https://github.com/q137904872/logo/tree/master/logo/实例化调用.png)
![](https://github.com/q137904872/logo/tree/master/logo/实例化.png)

##### * 属性的注入：
通过配置文件的配置信息将需要注入的属性通过反射调用set方法注入属性；
