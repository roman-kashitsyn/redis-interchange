HRD
===

HRD stands for Human Readable Dump for Redis.
It is implementation of simple human readable redis dump format in the
Scala programming language with amazing Parser Combinators library.
Here is a simple example of dump:

    "key"       : "value"
    "hash"      : {"hashKey" : 1, "anotherKey" : "value"}
    "set"       : #{1 2 3}
    "sortedSet" : #{ (1 "firstEntry") (0.1 "anotherEntry")}
    "list"      : [1 "hello" "world"]
    "binary"    : |base64|

BNF grammar
===========

    dump         ::= {entry}
    entry        ::= atom ':' (atom | hash | list | set | sortedSet)
    atom         ::= float | string | bytes
    bytes        ::= '|' base64 '|'
    base64       ::= 'a-zA-Z+/(=){0,2}'
    hash         ::= '{' [keyValuePair {',' keyValuePair} ] '}'
    keyValuePair ::= atom ':' atom
    list         ::= '[' {atom} ']'
    set          ::= '#{' {atom} '}'
    sortedSet    ::= '#{' {scoredPair} '}'
    scoredPair   ::= '(' score atom ')'
    score        ::= float
