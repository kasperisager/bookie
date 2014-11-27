# Bookie

[![Build Status](http://img.shields.io/travis/kasperisager/bookie.svg?style=flat)](https://travis-ci.org/kasperisager/bookie) [![Test Coverage](http://img.shields.io/coveralls/kasperisager/bookie.svg?style=flat)](https://coveralls.io/r/kasperisager/bookie) [![Open Issues](http://img.shields.io/github/issues/kasperisager/bookie.svg?style=flat)]
(https://github.com/kasperisager/bookie/issues)

_Bookie_ is a ticket reservation system for movie theaters written by [@kasperisager](https://github.com/kasperisager) and [@sigriddalsgard](https://github.com/sigriddalsgard) as our first semester project at the [IT University of Copenhagen](http://itu.dk). Many cups of coffee were harmed in its making :coffee:

It features a homebrewed data-persistence library named _Donkey_ which sits on top of [JDBC](http://www.oracle.com/technetwork/java/javase/jdbc) and acts as an ORM. Donkey speaks both [MySQL](https://www.mysql.com/), [PostgreSQL](http://www.postgresql.org/), as well as [SQLite](https://sqlite.org/) and takes care of handling any differences between these so we don't have to. We're lazy students after all.

## Donkey

JDBC is not the worst but gets really tiresome to work with. Registering drivers, getting connections, preparing statements, writing SQL, etc. are all things we're simply way too lazy to do. It's 2014: Who writes SQL by hand anymore? Donkey does away with all that, allowing us to focus on what matters: Data modelling. Isn't that really the reason why anyone would write an ORM?

The following is an example of a simple Donkey model:

```java
public class Person extends Model {
  public String name;
  public int age;
  
  public Person() {
    super("people", YourApp.db());
  }
}
```

This will give you a basic model that you can then `insert()`, `update()`, or `delete()` from your database. Donkey takes care of defining a database schema for you as long as you keep your fields `public`. Donkey of course also provides a solid set of tools for working more hands-on with databases.

---

Copyright &copy; 2014 [The Authors](https://github.com/kasperisager/bookie/graphs/contributors). Licensed under the terms of the [MIT license](LICENSE.md).
