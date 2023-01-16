# Spring Dynamic Velocity

A dynamic query template provider base on [Apache Velocity](https://velocity.apache.org) template engine.

You can refer to [Velocity Document](https://velocity.apache.org/engine/devel/user-guide.html) to know more about rules.

## Using

You need to configure a VelocityQueryTemplateProvider bean to use this template provider.

```java
@Bean
public DynamicQueryTemplateProvider dynamicQueryTemplateProvider() {
    VelocityQueryTemplateProvider provider = new VelocityQueryTemplateProvider();
    provider.setEncoding("UTF-8");
    provider.setTemplateLocation("classpath:/query");
    provider.setSuffix(".dsql");
    return provider;
}
```

The VelocityQueryTemplateProvider has the following parameters:

- `setTemplateLocation`: in case you do not specify the query template on the `@DynamicQuery` annotation, the provider will find it from external template files. The TemplateLocation Is location you put the external template files, default is `classpath:/query`
- `setSuffix`: is the suffix of the external template files, default is `.dsql`. If you don't want the provider to load external templates then set this value to `null`.
- `setEncoding`: is the encoding of templates, default is `UTF-8`
- `setConfiguration`: is a VelocityTemplateConfiguration, used to customize Velocity configuration.

Each template in external template file will start with a template name definition line. The template name definition line must be start with two dash characters (`--`).

## Directives

### Where Directive

`#where` directive knows to only insert `WHERE` if there is any content returned by the containing tags. Furthermore, if that content begins or ends with `AND` or `OR`, it knows to strip it off.

```sql
select t from User t
#where
  #if($firstName != $null)
    and t.firstName = :firstName
  #end
  #if($lastName != $null)
    and t.lastName = :lastName
  #end
#end
```

### Set Directive

`#set` directive is like the `#where` directive, it removes the commas if it appears at the begins or ends of the content. Also, it will insert `SET` if the content is not empty.

```sql
update User t
#set
  #if($firstName != $null)
    t.firstName = :firstName,
  #end
  #if($lastName != $null)
    t.lastName = :lastName,
  #end
#end
where t.userId = :userId
```

### Trim Directive

`#trim` directive has four parameters: `prefix`, `prefixOverrides`, `suffix`, `suffixOverrides`.

- `prefix` _(first parameter)_ is the string value that will be inserted at the start of the content if it is not empty, value type is string.
- `prefixOverrides` _(second parameter)_ are values that will be removed if they are at the start of a content, value type is list of strings.
- `suffix` _(third parameter)_ is the string value that will be inserted at the end of the content if it is not empty, value type is string.
- `suffixOverrides` _(fourth parameter)_ are values that will be removed if they are at the end of a content, value type is list of strings.

 ```sql
#trim("where (", ["and ", "or "], ")", [" and", " or"])
  #if($productName != $null)
    t.productName = :productName or
  #end
  #if($category != $null)
    t.category = :category or
  #end
#end
```
