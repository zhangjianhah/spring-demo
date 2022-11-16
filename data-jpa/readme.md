

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
## 3.1 @Id 主键
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

## 3.2 @Column 常规字段

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


## 3.5 @ManyToMany(多对多)

以下为学生和老师表之间的多对多关系的示例：
这是学生表中

```
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "teacher_student", joinColumns = { @JoinColumn(name = "student_id") }, inverseJoinColumns = {@JoinColumn(name = "teacher_id") })
    private List<Teacher> teachers;
```

这是老师表
```
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "zj_teacher_student", joinColumns = { @JoinColumn(name = "teacher_id") }, inverseJoinColumns = {@JoinColumn(name = "student_id") })
    private List<Student> students;
```

当然，我们可以不加```@JoinTable```,这样系统会自动生成一个诸如```teache_student```这样的表来，但是不便于规范化，所以使用这样的中间表。  
在```@JoinTable```有三个参数：  
- ```name``` : 选填，中间表名称，如果不填，那么会默认生成```表A_表B```这样格式的名称
- ```joinColumns``` : 选填，与当前表关联的表的字段
- ```joinColumns``` : 选填，当前表所属的字段

对于```joinColumns``` 和```joinColumns```，具体参考上方示例即可。

​        


# 4.Dao
一般来说，只需要新建**接口**继承```JpaRepository```或者```JpaSpecificationExecutor```即可。这两者有些许不同之处，

```JpaRepository``` :  常规的库接口，已经实现了一些基础的功能，包括正删改查等。有些地方会使用```CrudRepository```这个接口，但是其实```JpaRepository```已经继承了后者，所以直接用```JpaRepository```即可。

```JpaSpecificationExecutor```: 提供了一些拓展功能，完成多条件查询，并且支持分页与排序。但是不能单独使用，需要配合其他接口使用，如JpaRepository.





## JpaRepository

JpaRepository有一些已经默认给出的方法，其中唯一一个需要注意的是

```
//查询所有的list，其中的参数是用于指定排序方式
List<T> findAll(Sort sort);
//示例,因为Sort的构造函数已经转为私有，所以不在能直接new来构造
teacherRepository.findAll(Sort.by(Sort.Direction.DESC, "teacherId","teacherName"));


//对于save()方法，如果entity中已经给了主键值，那么在保存之前会先查询该主键的记录是否存在。

```

此外，JPA提供了根据pojo对象来新建接口方法，可以达到写SQL查询的目的。例如以下，首先是一个pojo类

```java
public class Teacher {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long teacherId;

    @Column
    private String teacherName;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "zj_teacher_student", joinColumns = { @JoinColumn(name = "teacher_id") }, inverseJoinColumns = {@JoinColumn(name = "student_id") })
    private List<Student> students;


    @Column
    private Short status;
}
```

我们可以直接按照规则写方法名，而不必自己手动实现，其自动实现内部构造。

```
@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long>, JpaSpecificationExecutor<Teacher> {


    /**
     * 根据教师名称查询单个教师，这是单个，注意方法中teacher和teachers的区别
     * 这里find|get|read都一样
     * @param name
     * @return
     */
    Teacher findTeacherByTeacherName(String name);
    Teacher readTeacherByTeacherName(String name);
    Teacher getTeacherByTeacherName(String name);

    /**
     * 根据教师名称和状态码查询单个教师
     * @param name
     * @param status
     * @return
     */
    Teacher findTeacherByTeacherNameAndStatus(String name,Short status);

    /**
     * 根据教师状态查询教师列表
     * @param status
     * @return
     */
    List<Teacher> findTeachersByStatus(Short status);

    /**
     * 根据教师状态查询教师，并且按照教师ID排序，默认正序（即ASC）
     * @param status
     * @return
     */
    List<Teacher> findTeachersByStatusOrderByTeacherId(Short status);

    /**
     * 根据教师状态查询教师，并且按照教师ID倒序排序
     * @param status
     * @return
     */
    List<Teacher> findTeachersByStatusOrderByTeacherIdDesc(Short status);

    /**
     * 根据教师状态查询教师，并且教师状态码小于指定参数
     * @param status
     * @return
     */
    List<Teacher> findTeachersByStatusLessThan(Short status);

    /**
     * 根据教师名称和状态码查询教师列表，其中教师名称为模糊查询、
     * 需要注意的是，这里需要自己给参数添加运算符%，例如
     * findTeachersByTeacherNameLikeAndStatus("%zhang%",0)
     * @param teacherName
     * @param status
     * @return
     */
    List<Teacher> findTeachersByTeacherNameLikeAndStatus(String teacherName,Short status);


    /**
     * 根据教师状态查询，这里采用了参数排序，而不是利用方法名称规则
     * @param status
     * @param sort
     * @return
     */
    List<Teacher> findTeachersByStatusLessThan(Short status,Sort sort);


    @Override
    Page<Teacher> findAll(Pageable pageable);


    /**
     * 使用sql查询获取教师信息，这里采用HQL，如果想要使用原生SQL，那么@Query中nativeQuery设置为true
     * 这里的占位符用的是?+参数下标
     * 注意：：参数下标从1开始
     * @param status
     * @param name
     * @return
     */
    @Query(value="from Teacher teacher where teacher.status = ?1 and teacher.teacherName like  concat('%',?2,'%') ")
    List<Teacher> listTeacherBystatusAndName1(Short status,String name);
    /**
     * z这里与方法一中基本一致，采用了:+自定义参数名称的方式，与MyBatis类似
     * @param status
     * @param name
     * @return
     */
    @Query(value="from Teacher teacher where teacher.status = :status and teacher.teacherName like  concat('%',:name,'%') ")
    List<Teacher> listTeacherBystatusAndName2(@Param("status") Short status, @Param("name") String name);


    /**
     * 使用原生SQL获取教师名称，其实就是nativeQuery = true,默认值为false
     * @param status
     * @return
     */
    @Query(value="select teacher_name from teacher where status = ?1",nativeQuery = true)
    String listNameByStatus(Short status);
}

```








## JpaSpecificationExecutor

//TODO 这玩意比较复杂，下次再看


## 分页
jpa提供了一个分页方式``Page<T>``，具体使用方法为。前两个参数为当前页（从0开始）、每页数量。第三个参数非必填，主要是排序所用的参数
```java
 PageRequest pageable = PageRequest.of(currentPage - 1, size);
 Page<Video> videoPage = videoRepository.findAll(pageable);
```




# 5.事务

- UPDATE或者DELETE操作需要使用事务，此时需要定义Service层，在Service层的方法上添加```@Transactional```注解实现事务操作。
- 在@Query注解中，编写JPQL实现DELETE和UPDATE操作的时候，必须加上@modifying注解，以通知Spring Data 这是一个DELETE或UPDATE操作。
- @Query中不支持insert






```
@Modifying
@Query(value="update teacher set teacher_name = ?1 where teacher_id = ?2",nativeQuery = true)
void updaetTeacher(String name,Long id);
```

# 6.批量插入/修改







data=jpa
https://www.jianshu.com/p/c23c82a8fcfc
@GenericGenerator
https://blog.csdn.net/u011781521/article/details/72210980



https://blog.csdn.net/pengjunlee/article/details/79972059#%40OneToMany%EF%BC%88%E4%B8%80%E5%AF%B9%E5%A4%9A%EF%BC%89

