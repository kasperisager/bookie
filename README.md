# Bookie

[![Build Status](http://img.shields.io/travis/kasperisager/bookie.svg?style=flat)](https://travis-ci.org/kasperisager/bookie) [![Test Coverage](http://img.shields.io/coveralls/kasperisager/bookie.svg?style=flat)](https://coveralls.io/r/kasperisager/bookie) [![Open Issues](http://img.shields.io/github/issues/kasperisager/bookie.svg?style=flat)]
(https://github.com/kasperisager/bookie/issues)

_Bookie_ is a ticket reservation system for movie theaters written by [@kasperisager](https://github.com/kasperisager) and [@sigriddalsgard](https://github.com/sigriddalsgard) as our first semester project at the [IT University of Copenhagen](http://itu.dk). Many cups of coffee were harmed in its making.

It features a homebrewed data-persistence library named _Donkey_ which sits on top of [JDBC](http://www.oracle.com/technetwork/java/javase/jdbc) and acts as an ORM. Donkey speaks both [MySQL](https://www.mysql.com/), [PostgreSQL](http://www.postgresql.org/), as well as [SQLite](https://sqlite.org/) and takes care of handling any differences between these so we don't have to. We're lazy students after all.

---

Copyright &copy; 2014 [The Authors](https://github.com/kasperisager/bookie/graphs/contributors). Licensed under the terms of the [MIT license](LICENSE.md).
