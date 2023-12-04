CREATE TABLE IF NOT EXISTS post(
id serial PRIMARY KEY,
title text,
description text,
link text UNIQUE,
created timestamp
);