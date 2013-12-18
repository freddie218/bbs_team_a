#!/bin/env sh
mysql -uroot < db/schema.sql
mysql -uroot < db/users.sql
