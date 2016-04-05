# liblevenshtein

## Java

### A library for generating Finite State Transducers based on Levenshtein Automata.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.dylon/liblevenshtein/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.dylon/liblevenshtein)
[![Reference Status](https://www.versioneye.com/java/com.github.dylon:liblevenshtein/reference_badge.svg)](https://www.versioneye.com/java/com.github.dylon:liblevenshtein/references)
[![License](https://img.shields.io/github/license/universal-automata/liblevenshtein-java.svg)](http://www.opensource.org/licenses/mit-license.php)
[![Build Status](https://travis-ci.org/universal-automata/liblevenshtein-java.svg?branch=master)](https://travis-ci.org/universal-automata/liblevenshtein-java)
[![Coverage Status](https://coveralls.io/repos/github/universal-automata/liblevenshtein-java/badge.svg?branch=master)](https://coveralls.io/github/universal-automata/liblevenshtein-java?branch=master)
[![Code Climate](https://codeclimate.com/github/universal-automata/liblevenshtein-java/badges/gpa.svg)](https://codeclimate.com/github/universal-automata/liblevenshtein-java)
[![Issue Count](https://codeclimate.com/github/universal-automata/liblevenshtein-java/badges/issue_count.svg)](https://codeclimate.com/github/universal-automata/liblevenshtein-java)
[![Dependency Status](https://www.versioneye.com/user/projects/570345d4fcd19a0051853d99/badge.svg)](https://www.versioneye.com/user/projects/570345d4fcd19a0051853d99)
[![Gitter](https://badges.gitter.im/universal-automata/liblevenshtein-java.svg)](https://gitter.im/universal-automata/liblevenshtein-java?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

Levenshtein transducers accept a query term and return all terms in a
dictionary that are within n spelling errors away from it. They constitute a
highly-efficient (space _and_ time) class of spelling correctors that work very
well when you do not require context while making suggestions.  Forget about
performing a linear scan over your dictionary to find all terms that are
sufficiently-close to the user's query, using a quadratic implementation of the
[Levenshtein distance](https://en.wikipedia.org/wiki/Levenshtein_distance) or
[Damerau-Levenshtein
distance](https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance),
these babies find _all_ the terms from your dictionary in linear time _on the
length of the query term_ (not on the size of the dictionary, on the length of
the query term).

If you need context, then take the candidates generated by the transducer as a
starting place, and plug them into whatever model you're using for context (such
as by selecting the sequence of terms that have the greatest probability of
appearing together).

For a quick demonstration, please visit the [Github Page,
here](http://universal-automata.github.io/liblevenshtein/).

The library is currently written in Java, CoffeeScript, and JavaScript, but I
will be porting it to other languages, soon.  If you have a specific language
you would like to see it in, or package-management system you would like it
deployed to, let me know.

### Documentation

When it comes to documentation, you have several options:
- [Wiki](https://github.com/universal-automata/liblevenshtein/wiki)
- [Javadoc](http://universal-automata.github.io/liblevenshtein/docs/javadoc/)
- [Source Code](https://github.com/universal-automata/liblevenshtein-java/tree/master/src)

### Basic Usage:

### Minimum Java Version

liblevenshtein has been developed against Java &ge; 1.8.  It will not work with prior versions.

#### Installation

##### Maven

Add a dependency on com.github.dylon:liblevenshtein:2.1.4-alpha.1 to your project's POM:

```xml
<dependency>
  <groupId>com.github.dylon</groupId>
  <artifactId>liblevenshtein</artifactId>
  <version>2.1.4-alpha.1</version>
</dependency>
```

##### Apache Buildr

```ruby
'com.github.dylon:liblevenshtein:jar:2.1.4-alpha.1'
```

##### Apache Ivy

```xml
<dependency org="com.github.dylon" name="liblevenshtein" rev="2.1.4-alpha.1" />
```

##### Groovy Grape

```groovy
@Grapes(
@Grab(group='com.github.dylon', module='liblevenshtein', version='2.1.4-alpha.1')
)
```

##### Gradle / Grails

Add a dependency on com.github.dylon:liblevenshtein:2.1.4-alpha.1 to your project's <code>build.gradle</code>:

```groovy
compile 'com.github.dylon:liblevenshtein:2.1.4-alpha.1'
```

##### Scala SBT

```scala
libraryDependencies += "com.github.dylon" % "liblevenshtein" % "2.1.4-alpha.1"
```

##### Leiningen

```clojure
[com.github.dylon/liblevenshtein "2.1.4-alpha.1"]
```

#### Git

You probably don't want this option unless you just want to dig through the source or help me with maintenance (yes, please!), but if you'd like to checkout the Git repo, clone it from GitHub:

```
$ git clone https://github.com/universal-automata/liblevenshtein-java.git
Cloning into 'liblevenshtein-java'...
remote: Counting objects: 1570, done.
remote: Compressing objects: 100% (23/23), done.
remote: Total 1570 (delta 4), reused 0 (delta 0), pack-reused 1541
Receiving objects: 100% (1570/1570), 245.49 KiB | 0 bytes/s, done.
Resolving deltas: 100% (664/664), done.
Checking connectivity... done.
```

#### Usage

Once you've checked out the library, use it:

```java
package pkg.of.awesomeness;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.dylon.liblevenshtein.levenshtein.Algorithm;
import com.github.dylon.liblevenshtein.levenshtein.Candidate;
import com.github.dylon.liblevenshtein.levenshtein.ITransducer;
import com.github.dylon.liblevenshtein.levenshtein.factory.TransducerBuilder;

public class GetSpellingCandidates {

  public static void main(final String... args) throws IOException {
    int argsIdx = 0;
    final String dictionaryPath = args[argsIdx ++];
    final int maxDistance = Integer.parseInt(args[argsIdx ++]);

    final Collection<String> dictionary = buildDictionary(dictionaryPath);

    final ITransducer<Candidate> transducer = new TransducerBuilder()
      .algorithm(Algorithm.TRANSPOSITION)
      .defaultMaxDistance(maxDistance)
      .dictionary(dictionary)
      .build();

    for (int i = argsIdx; i < args.length; ++i) {
      final String queryTerm = args[i];
      for (final Candidate candidate : transducer.transduce(queryTerm)) {
        final String candidateTerm = candidate.term();
        final int distance = candidate.distance();
        System.out.printf("d(%s, %s) = %d%n", queryTerm, candidateTerm, distance);
      }
    }
  }

  private static Collection<String> buildDictionary(final String dictionaryPath)
      throws IOException {

    try (final Reader dictionaryReader = new FileReader(dictionaryPath);
         final BufferedReader lineReader = new BufferedReader(dictionaryReader)) {

      final List<String> dictionary = new ArrayList<>();

      String line;
      while (null != (line = lineReader.readLine())) {
        dictionary.add(line);
      }

      return dictionary;
    }
  }
}
```

```sh
# Calls GetSpellingCandidates with the newline-delimited, dictionary of terms, a
# maximum distance of 2, and the following terms to find spelling candidates
# for: foo; bar; baz
java pkg.of.awesomeness.GetSpellingCandidates /path/to/dictionary.txt 2 foo bar baz
```

Please see the [wiki](https://github.com/universal-automata/liblevenshtein/wiki) for more details.

### Reference

This library is based largely on the work of [Stoyan
Mihov](http://www.lml.bas.bg/~stoyan/), [Klaus
Schulz](http://www.cis.uni-muenchen.de/people/schulz.html), and Petar Nikolaev
Mitankin: "[Fast String Correction with
Levenshtein-Automata](http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.16.652
"Klaus Schulz and Stoyan Mihov (2002)")".  For more details, please see the
[wiki](https://github.com/universal-automata/liblevenshtein/wiki).
