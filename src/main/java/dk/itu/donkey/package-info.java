/**
 * Donkey is a data-persistence library built on top of JDBC and acts as a
 * database abstraction layer and object-relational mapper.
 *
 * <p>
 * The core aspects of Donkey, such as the concept of {@link Grammar}s as well
 * as the interfaces for the {@link Query}- and {@link Schema}-builders, were
 * modelled after the Laravel APIs. Laravel is a widely-adopted PHP framework
 * and it seemed natural to base the interfaces of Donkey on the already
 * well-established conventions put forth by Laravel.
 *
 * <p>
 * Thanks to {@link Grammar}s Donkey supports multiple flavours of relational
 * SQL databases. The currently supported database systems are:
 *
 * <ul>
 * <li><a href="https://www.mysql.com/">MySQL</a></li>
 * <li><a href="http://www.postgresql.org/">PostgreSQL</a></li>
 * <li><a href="https://sqlite.org/">SQLite</a></li>
 * </ul>
 *
 * <p>
 * Copyright (C) 2014 Kasper Kronborg Isager.
 *
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 * @see <a href="http://laravel.com/">Laravel - The PHP Framework For Web
 *      Artisans.</a>
 *
 * @version 1.0.0
 */
package dk.itu.donkey;
