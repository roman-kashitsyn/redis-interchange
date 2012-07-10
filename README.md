HRD
===

HRD stands for Human Readable Dump for Redis.
It is implementation of simple human readable redis dump format in the
Scala programming language with amazing Parser Combinators library.
Here is a simple example of dump:

    -- Comment line
    "key"       : "value"
    "hash"      : {"hashKey" : 1, "anotherKey" : "value"}
    "set"       : #{1 2 3}
    "sortedSet" : #{ (1 "firstEntry") (0.1 "anotherEntry")}
    "list"      : [1 "hello" "world"]
    "binary"    : |aGVsbG8=|

    -- Keys with expiration
    "exp1" [1341922275] : "value" -- Expires at 2012-10-07T07:11:15
    "exp2" [1d5h]       : "value" -- Expires after 1 day and 5 hours

EBNF grammar
============

    dump          = {entry};
    key           = atom ['[' timeToLive ']'];
    timeToLive    = (unixTimeStamp | expiresAfter);
    unixTimeStamp = integer
    expiresAfter  = timeSpan
    timeSpan      = [integer+ 'd'][integer+ 'h'][integer+ 'm'][integer+ 's']
    entry         = atom ':' (atom | hash | list | set | sortedSet);
    atom          = float | string | bytes;
    bytes         = '|' base64 '|';
    base64        = '[a-zA-Z0-9+/]+(=){0,2}';
    hash          = '{' [keyValuePair {',' keyValuePair} ] '}';
    keyValuePair  = atom ':' atom;
    list          = '[' {atom} ']';
    set           = '#{' {atom} '}';
    sortedSet     = '#{' {scoredPair} '}';
    scoredPair    = '(' score atom ')';
    score         = float;
