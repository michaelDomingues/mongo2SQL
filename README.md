Mongodb query to sql query translator.

Examples:
in: db.user.find({name: 'julio'});
out: SELECT * FROM user WHERE name = ‘julio';
in: db.user.find({_id: 23113},{name: 1, age: 1});
out: SELECT name, age FROM user WHERE _id = 23113;
in: db.user.find({age: {$gte: 21}},{name: 1,_id: 1});
out: SELECT name, _id FROM user WHERE age >= 21;

The translator supports the following mongodb operators:
$or
$and (remember $and and comma separated values on an object are the same)
$lt
$lte
$gt
$gte
$ne
$in

Input File: input_db.txt  --> one mongo query per line
Ouput File: output_db.txt --> one sql query per line

TODO:
- Support to multiple combinations of $and and $or in a single query like:
 --> db.things.find({$and: [{$or : [{'a':1},{'b':2}]},{$or : [{'a':2},{'b':3}]}] })  <--

How to run:

- First pull from Git
- Compile
- Add mongodb queries to input file
- Run jar
- Check output file
