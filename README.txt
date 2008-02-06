Groovy Dice - A groovier way to roll dice!
******************************************

Version 1.3 - Wed Feb 6, 2008

This software is distributed under the terms of the Apache v2.0 License
(see LICENSE.txt).





CONTENT OF THIS FILE
--------------------

1. What is this?
2. Features
3. Installation
4. Brought to you by...
5. "Thank you" list
6. More information





1. What is this?
****************

Groovy Dice is an open source Groovy-based dice rolling API that provides a
simple way to perform almost any kind of dice roll, making this project a
reasonable choice for those who are implementing any kind of dice-based game
on top of Java platform.


2. Features
***********

Here goes a list that shows some Groovy Dice's features:

+ Support to all types of dice. For example:
    - percentile dice (1.pd, 1.'d%')
    - any number of dice sides (5.d6, 10.d100, 3.d(user_input), 4.d4(1.d))

+ Full support to arithmetic operations and calculations. For example:
    - (-5.d10 * 4.d6.the(2.best).mean) / (5.d10 + 4).worst_die

+ Advanced dice filtering. For example:
    - 10.d20.only_if{it.is_even}

+ Support to simple and conditional modifiers. For example:
    - 7.d10 + 5.to_every_die
    - 7.d10 * 2.to_each_die_if{it in 1..3}

+ Extension points to allow the user change the default API.


3. Installation
***************

Just drop the groovydice JAR file in your application's CLASSPATH and you're
done.

Since this API is written in Groovy 1.5.1, you will need to add the Groovy JARs
to your CLASSPATH to make the stuff work. Also, if you want to call Groovy Dice
scripts from a Java application using the Java Scripting API (JSR 223), you'll
need to add the groovy-engine JAR to your CLASSPATH as well.

All JAR files you need are inside both 'bin' and 'lib' folders. 


4. Brought to you by...
***********************

---
Name  : Daniel Fernandes Martins
Role  : Lead developer
E-mail: daniel_martins at users.sourceforge.net
---


5. "Thank you" list
*******************

This project wouldn't be possible without G2One Inc. and all Groovy contributors'
great work.

http://groovy.codehaus.org
http://g2one.com

This project is built by Maven 2.0.8, which is a very good tool to handle the
project dependencies and reports.

http://maven.apache.org


6. More information
*******************

This package contains an offline version of the project's website at the
'doc/site' folder. The "official" online version can be found at:

http://groovydice.sourceforge.net/.

--EOF