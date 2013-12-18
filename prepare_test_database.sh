#!/bin/env sh
sed 's/bbs/bbs_test/g' db/schema.sql > db/test.sql
mysql -uroot < db/test.sql
