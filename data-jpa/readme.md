

# 1.依赖
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
```

# 2.配置
```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/goods?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&useSSL=false&serverTimezone=GMT
    username: root
    password: root.1217
  jpa:
    database: MySQL
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
    show-sql: true
    hibernate:
      ddl-auto: update
```

database-platform 方言，这里使用mysql的方言  
show-sql：是否展示sql  
ddl-auto：用来配置程序启动时，对数据库的操作，其实是hibernate.hbm2ddl.auto的快捷方式，默认为none。


类型 | 说明
:---:|---
create | 每次运行该程序，没有表格会新建表格，表内有数据会清空
create-drop | 每次程序结束的时候会清空表 
update | 每次运行程序，没有表格会新建表格，表内有数据不会清空，只会更新(推荐)  
validate | 运行程序会校验数据与数据库的字段类型是否相同，不同会报错  
none | 啥都不做（默认值）  


# 3.Entity
##3.1 @Id 主键
下面为两个主键的策略示例，前者使用uuid，后者使用自增
```
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;
```
```
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    private Long id;
```

~~~~
@id                就是主键标志
@GeneratorValue    JPA通用策略生成器
@GenericGenerator  自定义主键生成策略
~~~~


我们通过```@GenericGenerator```定义一种主键生成策略，然后交由```@GeneratorValue```去生成这个策略并执行。
注意，可以看到上面示例中```@GenericGenerator```定义了一个name值，这里随意定义，
然后```@GeneratorValue```接受这个值去找到指定的策略。

##3.2 @Column 常规字段

- ```name``` : 可选，表示数据库表中列的名称。
- ```nullable``` : 可选，表示该字段是否允许为 null，默认为 true(null)。若设置为false 则该列不可为null值
- ```unique```：可选，表示该字段的大小，仅对 String 类型的字段有效，默认值 255.用来自定义列的长度 如 mobile (length=11)
- ```length```:可选，表示该字段的大小，仅对 String 类型的字段有效，默认值 255.用来自定义列的长度 如 mobile (length=11)
- ```insertable```:可选，表示在 ORM 框架执行插入操作时，该字段是否应出现 INSETRT
                         语句中，默认为 true
- ```updateable```:可选，表示在 ORM 框架执行更新操作时，该字段是否应该出现在 UPDATE 语句中，默认为 true. 对于一经创建就不可以更改的字段，该属性非常有用，如对于 birthday 字段。或者创建时间/注册时间(可以将其设置为 false 不可修改)
- ```precision```: 可选,列十进制精度(decimal precision)(默认值 0)
- ```scale```: 可选,如果列十进制数值范围(decimal scale)可用,在此设置(默认值 0)
- ```columnDefinition```: 可选，表示该字段在数据库中的实际类型。


通常 ORM 框架可以根据属性类型自动判断数据库中字段的类型，
    但是对于 Date 类型仍无法确定数据库中字段类型究竟是 DATE,TIME 还是 TIMESTAMP. 此
    外 ,String 的默认映射类型为 VARCHAR, 如果要将 String 类型映射到特定数据库的 BLOB
    或 TEXT 字段类型，该属性非常有用。
    示例:


```
        @Column(name="BIRTHDAY",nullable = false,columnDefinition="DATE")
        public String getBithday() {
            return birthday;
        }
        
        @Column(name = "id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
        private Long id;
        @Column(updatable = false, name = "flight_name", nullable = false, length=50)
        public String getName() { ... }
        @Lob
        @Column(columnDefinition="text")
        public String content;
```


## 3.3 @OneToOne(一对一)
```@OneToOne```是用于**一对一级联**。以下为常见示例。
```
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    name与referencedColumnName名称不应当相同，否则不会自动创建
//    @JoinColumn(name = "student_card_id",referencedColumnName = "student_card_id")
    @JoinColumn(name = "student_card_id")
    private StudentCard studentCard;
```

其中，```@OneToOne(cascade = CascadeType.ALL)```用于建立外键，这个时候其实已经建立连接了，但是默认的外键的名称为外键指向的表名+主键名。
因此，才会使用```@JoinColumn(name = "student_card_id")```规范外键的字段名称。
而且，默认情况下，外键指向的是主键，但是存在一些情况，需要级联的不是主键，故，可以用```referencedColumnName = "student_card_id"```来指定外键指向的字段。  
tips:在```@JoinColumn```中，name与referencedColumnName名称不应当相同，否则不会自动创建。也就是说，如果级联的外键是主键，那么傻都不要写。


## 3.4 @OneToMany 和 @ManyToOne(一对多、多对一)

这是一对多
```
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id")
    private ClassRoom classRoom;
```

这是多对一，经过测试，其实只要把一对多的写了，就可以用了。
```

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id")
    private ClassRoom classRoom;
```

Tips: @JoinColumn可以不写，这样jpa会默认生成一个中间表去保存这两张表之间的关联关系，其命名规则为```表A_表B```不过对于一对多、多对一来说，没必要。 



        








data=jpa
https://www.jianshu.com/p/c23c82a8fcfc
@GenericGenerator
https://blog.csdn.net/u011781521/article/details/72210980



https://blog.csdn.net/pengjunlee/article/details/79972059#%40OneToMany%EF%BC%88%E4%B8%80%E5%AF%B9%E5%A4%9A%EF%BC%89

